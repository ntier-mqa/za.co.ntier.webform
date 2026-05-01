# ==============================================================================
# SHAREPOINT ADMIN TOOLS - MASTER SCRIPT (MAXIMUM PERFORMANCE EDITION)
# ==============================================================================

# --- GLOBAL LOGGING SYSTEM ---
$script:LogFile = "./general_log_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"

function Write-Log {
    param(
        [string]$Message, 
        [string]$Level = "INFO"
    )
    $timestamp = Get-Date -Format "HH:mm:ss"
    $logLine = "[$timestamp] ${Level}: $Message"
    
    # Print to console with matching colors
    switch ($Level) {
        "START"     { Write-Host $logLine -ForegroundColor Cyan }
        "SUCCESS"   { Write-Host $logLine -ForegroundColor Green }
        "ACTION"    { Write-Host $logLine -ForegroundColor Green }
        "SKIP"      { Write-Host $logLine -ForegroundColor Yellow }
        "ERROR"     { Write-Host $logLine -ForegroundColor Red }
        "EXCEPTION" { Write-Host $logLine -ForegroundColor Red }
        "INFO"      { Write-Host $logLine -ForegroundColor Gray }
        Default     { Write-Host $logLine -ForegroundColor White }
    }
    
    # Append to the file defined in the script scope
    if ($null -ne $script:LogFile) {
        $logLine | Out-File -FilePath $script:LogFile -Append
    }
}

# ==============================================================================
# FUNCTION 1: CONNECT TO SHAREPOINT
# ==============================================================================
function Connect-SharePointEnvironment {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory=$true)][string]$SiteUrl,
        [Parameter(Mandatory=$true)][string]$ClientId,
        [Parameter(Mandatory=$true)][string]$Tenant,
        [Parameter(Mandatory=$false)][switch]$UseDeviceLogin 
    )

    Write-Log "Initiating connection to SharePoint..." -Level "INFO"
    try {
        if ($UseDeviceLogin) {
            Write-Log "Authentication Mode: Device Login (Check console for code)" -Level "INFO"
            Connect-PnPOnline -Url $SiteUrl -DeviceLogin -ClientId $ClientId -Tenant $Tenant -ErrorAction Stop
        } else {
            Write-Log "Authentication Mode: Interactive Browser" -Level "INFO"
            Connect-PnPOnline -Url $SiteUrl -Interactive -ClientId $ClientId -Tenant $Tenant -ErrorAction Stop
        }
        Write-Log "Successfully connected to $SiteUrl" -Level "SUCCESS"
    }
    catch {
        Write-Log "Connection failed: $($_.Exception.Message)" -Level "EXCEPTION"
        throw $_ 
    }
}

# ==============================================================================
# FUNCTION 2: RESTORE DELETED FILES (Optimized with Hash Table)
# ==============================================================================
function Restore-PnPDeletedFiles {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory=$true)][string]$SitePrefix,
        [Parameter(Mandatory=$true)][string]$SearchPath,
        [Parameter(Mandatory=$false)][string]$InputFile,
        [Parameter(Mandatory=$false)][string]$BackupRoot,
        [Parameter(Mandatory=$false)][string]$JsonCache
    )

    $script:LogFile = "./restore_log_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"
    Write-Log "Starting Restore Process..." -Level "START"
    
    $doBackupMove = -not [string]::IsNullOrWhiteSpace($BackupRoot)
    if (-not $doBackupMove) { Write-Log "No BackupRoot provided. Restoring in place (No move)." -Level "INFO" }

    $useJsonCache = -not [string]::IsNullOrWhiteSpace($JsonCache)

    # --- STEP 1 & 2: MANAGE RECYCLE BIN (JSON OR IN-MEMORY) ---
    if ($useJsonCache) {
        if (Test-Path $JsonCache) {
            Write-Log "Loaded existing JSON cache from $JsonCache" -Level "INFO"
            $deletedItems = Get-Content $JsonCache -Raw | ConvertFrom-Json
        } 
        else {
            Write-Log "JSON not found. Fetching Recycle Bin from SharePoint..." -Level "INFO"
            $rawBin = Get-PnPRecycleBinItem | Where-Object { $_.ItemType -eq 'File' -and $_.DirName -like "*$SearchPath*" }
            
            $deletedItems = $rawBin | ForEach-Object {
                $cleanDir = $_.DirName -replace "^/?sites/erprepository/", ""
                $calculatedPath = "$cleanDir/$($_.LeafName)" -replace "//", "/"
                [PSCustomObject]@{
                    Id          = $_.Id.ToString()
                    LeafName    = $_.LeafName
                    DeletedDate = $_.DeletedDate
                    ShortPath   = $calculatedPath
                }
            } | Sort-Object ShortPath, @{Expression="DeletedDate"; Descending=$true} 

            $deletedItems | ConvertTo-Json -Depth 3 | Out-File $JsonCache
            Write-Log "Saved $($deletedItems.Count) items to $JsonCache" -Level "INFO"
        }
    } 
    else {
        Write-Log "No JsonCache provided. Fetching live Recycle Bin objects into memory..." -Level "INFO"
        $rawBin = Get-PnPRecycleBinItem | Where-Object { $_.ItemType -eq 'File' -and $_.DirName -like "*$SearchPath*" }
        
        $deletedItems = $rawBin | ForEach-Object {
            $cleanDir = $_.DirName -replace "^/?sites/erprepository/", ""
            $calculatedPath = "$cleanDir/$($_.LeafName)" -replace "//", "/"
            [PSCustomObject]@{
                Id          = $_.Id.ToString()
                ShortPath   = $calculatedPath
                DeletedDate = $_.DeletedDate
                CSOMObject  = $_ 
            }
        } | Sort-Object ShortPath, @{Expression="DeletedDate"; Descending=$true} 
        
        Write-Log "Cached $($deletedItems.Count) live objects in memory." -Level "INFO"
    }

    # --- HASH TABLE OPTIMIZATION: Keep only the FIRST unique path (newest) ---
    Write-Log "Optimizing Recycle Bin data into Hash Table..." -Level "INFO"
    $uniqueBinItems = @{}
    foreach ($item in $deletedItems) {
        if (-not $uniqueBinItems.ContainsKey($item.ShortPath)) {
            $uniqueBinItems[$item.ShortPath] = $item
        }
    }
    Write-Log "Reduced down to $($uniqueBinItems.Count) unique, newest files ready for O(1) lookup." -Level "INFO"

    # --- STEP 3: DETERMINE FILES TO RESTORE ---
    if ([string]::IsNullOrWhiteSpace($InputFile)) {
        Write-Log "No InputFile provided! Preparing to restore ALL deleted items from $SearchPath..." -Level "INFO"
        $regexSearchPath = [regex]::Escape($SearchPath) + "/?"
        $restoreItems = $uniqueBinItems.Values | ForEach-Object { $_.ShortPath -ireplace "^$regexSearchPath", "" } | Select-Object -Unique
    } 
    else {
        if (-not (Test-Path $InputFile)) {
            Write-Log "Input file $InputFile not found!" -Level "ERROR"
            return
        }
        $restoreItems = Get-Content $InputFile | Where-Object { -not [string]::IsNullOrWhiteSpace($_) } | Select-Object -Unique
    }
    
    $totalItems = $restoreItems.Count
    Write-Log "Found $totalItems unique items to process." -Level "INFO"

    if ($totalItems -eq 0) { Write-Log "No items to process. Exiting." -Level "SKIP"; return }

    # --- STEP 4: LOOP ON RESTORE ITEMS ---
    $counter = 1
    foreach ($restoreItem in $restoreItems) {
        $cleanLine = $restoreItem -replace "\\", "/" 
        $shortOriginalPath = "$SearchPath/$cleanLine" -replace "//", "/"
        $fullOriginalPath = "$SitePrefix/$shortOriginalPath" -replace "//", "/"
        $fileName = [System.IO.Path]::GetFileName($shortOriginalPath)
        
        if ($doBackupMove) {
            $targetFolder = "$BackupRoot/$cleanLine" -replace "//", "/"
            $targetFolderDir = [System.IO.Path]::GetDirectoryName($targetFolder).Replace("\","/")
            $shortTargetUrl = "$targetFolderDir/$fileName" -replace "//", "/"
            $fullTargetUrl = "$SitePrefix/$shortTargetUrl" -replace "//", "/"
        }

        Write-Log "Processing ($counter/$totalItems) | File: $fileName" -Level "START"

        try {
            # 1. Instant Memory Lookup First! (Saves API calls if it's not even in the bin)
            $matchedItem = $uniqueBinItems[$shortOriginalPath]
            if ($null -eq $matchedItem) { 
                Write-Log "Path not found in deleted items cache: $shortOriginalPath" -Level "ERROR"
                $counter++; continue 
            }
            $itemId = $matchedItem.Id

            # 2. Check Origin (Only done if it exists in the bin)
            $inOrigin = Get-PnPFile -Url $shortOriginalPath -ErrorAction SilentlyContinue
            if ($null -ne $inOrigin) { Write-Log "File already exists at $shortOriginalPath" -Level "SKIP"; $counter++; continue }
            
            # 3. Check Backup
            if ($doBackupMove) {
                $inBackup = Get-PnPFile -Url $shortTargetUrl -ErrorAction SilentlyContinue
                if ($null -ne $inBackup) { Write-Log "File already exists in Backup: $shortTargetUrl" -Level "SKIP"; $counter++; continue }
            }

            # 4. Fetch CSOM
            if ($useJsonCache) {
                Write-Log "Found ID in JSON cache: $itemId" -Level "INFO"
                $binItem = Get-PnPRecycleBinItem -Identity $itemId -ErrorAction SilentlyContinue
                if ($null -eq $binItem) { Write-Log "Identity $itemId not found in live Recycle Bin." -Level "ERROR"; $counter++; continue }
            } else {
                Write-Log "Using direct CSOM object from memory" -Level "INFO"
                $binItem = $matchedItem.CSOMObject
            }

            # 5. Restore & Move
            $shortOriginalDir = [System.IO.Path]::GetDirectoryName($shortOriginalPath).Replace("\","/")
            Resolve-PnPFolder -SiteRelativePath $shortOriginalDir | Out-Null

            $binItem.Restore()
            $binItem.Context.ExecuteQuery()
            Write-Log "Restored item via CSOM" -Level "ACTION"

            if ($doBackupMove) {
                Resolve-PnPFolder -SiteRelativePath $targetFolderDir | Out-Null
                Move-PnPFile -SourceUrl $fullOriginalPath -TargetUrl $fullTargetUrl -Force
                Write-Log "Moved successfully to $fullTargetUrl" -Level "SUCCESS"
            } else {
                Write-Log "Restored successfully to $shortOriginalPath" -Level "SUCCESS"
            }
        }
        catch { Write-Log "$shortOriginalPath - $($_.Exception.Message)" -Level "EXCEPTION" }
        
        $counter++
    }
    Write-Log "Restore Process Complete." -Level "INFO"
}

# ==============================================================================
# FUNCTION 3: COPY FILES USING CSV
# ==============================================================================
function Copy-PnPFilesFromCsv {
    [CmdletBinding()]
    param (
        [Parameter(Mandatory=$true)][string]$SitePrefix,
        [Parameter(Mandatory=$true)][string]$BackupRoot,
        [Parameter(Mandatory=$true)][string]$NewFolderContent,
        [Parameter(Mandatory=$true)][string]$CsvFile
    )

    $script:LogFile = "./copy_files_log_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"
    Write-Log "Starting Copy Process from CSV..." -Level "START"

    if (-not (Test-Path $CsvFile)) { Write-Log "CSV file $CsvFile not found!" -Level "ERROR"; return }

    $csvData = Import-Csv -Path $CsvFile
    $totalItems = $csvData.Count
    Write-Log "Found $totalItems rows to process in CSV." -Level "INFO"

    $counter = 1
    foreach ($row in $csvData) {
        if ([string]::IsNullOrWhiteSpace($row.ZZOldAttachmentPath) -or [string]::IsNullOrWhiteSpace($row.ZZAttachmentPath)) {
            Write-Log "Row $counter has missing path data. Skipping." -Level "SKIP"; $counter++; continue
        }

        $oldSubPath = $row.ZZOldAttachmentPath -replace "\\", "/"
        $newSubPath = $row.ZZAttachmentPath -replace "\\", "/"
        $shortSourcePath = "$BackupRoot/$oldSubPath" -replace "//", "/"
        $fullSourceUrl = "$SitePrefix/$shortSourcePath" -replace "//", "/"
        $shortTargetPath = "$NewFolderContent/$newSubPath" -replace "//", "/"
        $fullTargetUrl = "$SitePrefix/$shortTargetPath" -replace "//", "/"
        
        $fileName = [System.IO.Path]::GetFileName($shortSourcePath)
        $targetFolderDir = [System.IO.Path]::GetDirectoryName($shortTargetPath).Replace("\","/")

        Write-Log "Processing ($counter/$totalItems) | Copying: $fileName" -Level "START"

        try {
            $sourceExists = Get-PnPFile -Url $shortSourcePath -ErrorAction SilentlyContinue
            if ($null -eq $sourceExists) { Write-Log "Source file not found at $shortSourcePath" -Level "ERROR"; $counter++; continue }

            $targetExists = Get-PnPFile -Url $shortTargetPath -ErrorAction SilentlyContinue
            if ($null -ne $targetExists) { Write-Log "File already exists at destination: $shortTargetPath" -Level "SKIP"; $counter++; continue }

            Resolve-PnPFolder -SiteRelativePath $targetFolderDir | Out-Null
            Copy-PnPFile -SourceUrl $fullSourceUrl -TargetUrl $fullTargetUrl -Force
            Write-Log "Copied successfully to $shortTargetPath" -Level "SUCCESS"
        }
        catch { Write-Log "$shortSourcePath - $($_.Exception.Message)" -Level "EXCEPTION" }
        
        $counter++
    }
    Write-Log "Copy process complete." -Level "INFO"
}

# ==============================================================================
# FUNCTION 4: RECURSIVE DELETE EMPTY FOLDERS
# ==============================================================================
function Remove-PnPEmptyFolders {
    param (
        [Parameter(Mandatory=$true)][string]$FolderUrl,
        [Parameter(Mandatory=$false)][switch]$IsRootCall = $true
    )

    if ($IsRootCall) {
        $script:LogFile = "./folder_cleanup_log_$(Get-Date -Format 'yyyyMMdd_HHmmss').txt"
        Write-Log "Starting Recursive Empty Folder Cleanup at: $FolderUrl" -Level "START"
    }

    $subFolders = Get-PnPFolderItem -FolderSiteRelativeUrl $FolderUrl -ItemType Folder -ErrorAction SilentlyContinue

    foreach ($folder in $subFolders) {
        if ($folder.Name -eq "Forms" -or $folder.Name -eq "t" -or $folder.Name -eq "w") { continue }
        $subFolderUrl = "$FolderUrl/$($folder.Name)" -replace "//", "/"
        Remove-PnPEmptyFolders -FolderUrl $subFolderUrl -IsRootCall:$false
    }

    $files = Get-PnPFolderItem -FolderSiteRelativeUrl $FolderUrl -ItemType File
    $remainingFolders = Get-PnPFolderItem -FolderSiteRelativeUrl $FolderUrl -ItemType Folder | Where-Object { $_.Name -notin @("Forms", "t", "w") }

    if (($files.Count -eq 0) -and ($remainingFolders.Count -eq 0)) {
        if ($IsRootCall) { Write-Log "Root folder '$FolderUrl' is empty, but kept for safety." -Level "INFO"; return }

        try {
            $folderToDelete = Get-PnPFolder -Url $FolderUrl
            $folderToDelete.DeleteObject()
            $folderToDelete.Context.ExecuteQuery()
            Write-Log "Deleted empty folder -> $FolderUrl" -Level "ACTION"
        }
        catch { Write-Log "Could not delete $FolderUrl - $($_.Exception.Message)" -Level "EXCEPTION" }
    }

    if ($IsRootCall) { Write-Log "Cleanup complete." -Level "INFO" }
}

# ==============================================================================
# EXECUTION ZONE (UNCOMMENT THE SCENARIO YOU WANT TO RUN)
# ==============================================================================


# ---------------------------------------------------------
# EXAMPLE 0: CONNECT TO SHAREPOINT (ALWAYS DO THIS FIRST)
# ---------------------------------------------------------
Connect-SharePointEnvironment -SiteUrl "https://miningqualificationsauth.sharepoint.com/sites/erprepository" `
                              -ClientId "149f8696-0059-4a8a-9e7a-69cf1ec6ac2c" `
                              -Tenant "miningqualificationsauth.onmicrosoft.com"

Restore-PnPDeletedFiles -SitePrefix "/sites/erprepository" `
                        -SearchPath "Shared Documents/prod"
                        

<#

# ---------------------------------------------------------
# EXAMPLE 1: STANDARD RESTORE & MOVE (Using JSON Cache)
# ---------------------------------------------------------
Restore-PnPDeletedFiles -SitePrefix "/sites/erprepository" `
                        -SearchPath "Shared Documents/prod" `
                        -InputFile "./Only_In_UAT.txt" `
                        -BackupRoot "backupProd/prod" `
                        -JsonCache "./searchItems.json"


# ---------------------------------------------------------
# EXAMPLE 2: RESTORE IN PLACE ONLY (No Move, In-Memory CSOM)
# ---------------------------------------------------------
Restore-PnPDeletedFiles -SitePrefix "/sites/erprepository" `
                        -SearchPath "Shared Documents/prod" `
                        -InputFile "./Only_In_UAT.txt"


# ---------------------------------------------------------
# EXAMPLE 3: RESTORE EVERYTHING (No Input File, No Cache)
# ---------------------------------------------------------
Restore-PnPDeletedFiles -SitePrefix "/sites/erprepository" `
                        -SearchPath "Shared Documents/prod" `
                        -BackupRoot "backupProd/prod"


# ---------------------------------------------------------
# EXAMPLE 4: COPY FILES USING CSV
# ---------------------------------------------------------
Copy-PnPFilesFromCsv -SitePrefix "/sites/erprepository" `
                     -BackupRoot "Shared Documents/backup/uat" `
                     -NewFolderContent "Shared Documents/new_uat_content" `
                     -CsvFile "./correctPath.csv"


# ---------------------------------------------------------
# EXAMPLE 5: CLEAN UP EMPTY FOLDERS
# ---------------------------------------------------------
Remove-PnPEmptyFolders -FolderUrl "Shared Documents/backup/uat"

#>