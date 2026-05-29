## Migrate stragtegy (Learnership as example)

start with bussiness table like table Learnership

<details>

<summary>Learnership DDL</summary>

```sql

CREATE TABLE MQA.dbo.Learnership (
	ID int IDENTITY(1,1) NOT NULL,
	LearnershipCode nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	LearnershipTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	LearnershipTypeId int NOT NULL,
	QualificationID int NULL,
	NQFLevelID int NOT NULL,
	Credits int NOT NULL,
	QualityAssuranceBodyID int NOT NULL,
	OFOOccupationID int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	MigrationRecordID int NULL,
	MinimumElectiveCredits int NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_Learnership PRIMARY KEY (ID)
);

```

</details>

1. on idempiere create table ZZLearnership with standard column
2. for each columns investigate it's reference or not

   1. in case reference table isn't much records and have only code/title column then create it on idempiere as a reference list store id to description column to use later
   2. in case reference table have a lot record or more columns then make it as separate table add column ZZMigrationCode to store id
3. to import old data

   1. open window of ZZLearnership input a record and export it to csv to get template (after that delete the record)
   2. build a query data with some note

      1. column is direct value like LearnershipTitle, LearnershipCode just direct query it

      `ls.LearnershipTitle as ZZLearnershipTitle,`

      2. reference column use ad_reference

      `lev.SAQACode as ZZNqfLevel,`

      3. column is simple and easy convert like IsDeleted

      `CASE ls.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive`

      4. easy workaround to fix data

      `CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody`

      5. reference table has column with unique column so idempiere can be help to lookup id

      `q.SAQAQualificationID as "ZZQualification_ID[ZZSaqaQualificationCode]",`

      6. reference columns hasn't unique value so can't lookup like (5) then save id to ZZMigrateValues (for post process by sql function zzApplyMigrateValues)

         1. case one column

         `'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues`

         2. case multi columns

         ```

         CONCAT_WS (';',
         	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)),
         	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12))
         ) as ZZMigrateValues

         ```

   <details>

   <summary>Query Learnership</summary>

   ```

   	SELECT
   		'*' as "AD_Org_ID[Name]",
   		ls.LearnershipTitle as ZZLearnershipTitle,
   		ls.LearnershipCode as ZZLearnershipCode,
   		lst.description as ZZLearnershipType,
   		q.SAQAQualificationID as "ZZQualification_ID[ZZSaqaQualificationCode]",
   		lev.SAQACode as ZZNqfLevel,
   		ls.Credits as ZZCredits,
   		CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
   		ls.Registrationstartdate as Registrationstartdate,
   		ls.Registrationenddate as Registrationenddate,
   		ls.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
   		CASE ls.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
   		ls.id as "ZZMigrationCode/K",
   		'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null

   		--CONCAT_WS (';',
   		--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12)), -- ooc.id is null then this line is null and get out from expression
   		--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12))
   		--) as ZZMigrateValues

   	FROM 
   		Learnership ls 
   		left join lkpNQFLevel lev on ls.NQFLevelID = lev.id
   		left join Qualification q on ls.QualificationID = q.ID 
   		left join lkpLearnershipType lst on ls.LearnershipTypeId = lst.ID 
   		left join LkpOfoOccupation ooc on ls.OFOOccupationID = ooc.ID
   		left join lkpQualityAssuranceBody qab on ls.QualityAssuranceBodyID = qab.ID

   ```

   </details>
4. export result of query learnership to csv and import to window learnership
5. in case not yet update sql function zzApplyMigrateValues then run file [zzApplyMigrateValues.sql](zzApplyMigrateValues.sql)
6. parse ZZMigrateValues and lookup reference id to update to table learnership by run sql function

   `SELECT zzApplyMigrateValues('ZZLearnership');`

## Global validate unique data

### Qualification (unique)

`select count (*), count (distinct(SAQAQualificationID)) from Qualification`

### lkpQualityAssuranceBody (not unique)
1. check unique

`select count(*), count (distinct (description)), count (distinct (saqacode)) from lkpQualityAssuranceBody`

2. find out non-unique value

`select saqacode from lkpQualityAssuranceBody group by saqacode having count(*) > 1`

3. Hot fix by use id for non-unique saqacode

`CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,`

### LkpOfoOccupation (not unique)
1. check unique

`select count (*), count (distinct(code)), count (distinct(description)) from LkpOfoOccupation`

2. use ZZMigrateValues to remap id

'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues

## QCTOQualification

<details>

<summary>validate data</summary>

```sql

select count (), count (distinct(SAQAQualificationID)) from Qualification
select count (), count (distinct(description)) from lkpQCTOQualificationType
select count (*), count (distinct(code)), count (distinct(description)) from LkpOfoOccupation

```

</details>

<details>

<summary>QCTOQualification Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	qq.SAQAQualificationID as ZZSaqaQualificationCode,
	qq.SAQAQualificationTitle as ZZSaqaQualificationTitle,
	lev.SAQACode as ZZNqfLevel,
	qq.Credits as ZZCredits,
	qq.Registrationstartdate as Registrationstartdate,
	qq.Registrationenddate as Registrationenddate,
	qq.LastEnrolmentDate as	ZZLastEnrolmentDate,
	qq.LastAchievementDate as ZZLastAchievementDate,
	qqt.description as ZZQctoQualificationType,
	alterQ.SAQAQualificationID as "ZZReplacementQualification_ID[ZZSaqaQualificationCode]",
	qq.NewRegistrationStartDate as ZZNewRegistrationStartDate,
	qq.NewRegistrationEndDate as ZZNewRegistrationEndDate,
	qq.NewLastEnrolmentDate as ZZNewLastEnrolmentDate,
	qq.NewLastAchievementDate as ZZNewLastAchievementDate,
	CASE qq.IsReplacement WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReplacement,
	CASE qq.IsReregistered WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReregistered,
	qq.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
	q.SAQAQualificationID as "ZZQualification_ID[ZZSaqaQualificationCode]",
	CASE qq.ArtisanQualificationYesNoID WHEN 1 THEN 'N' WHEN 2 THEN 'Y' END AS ZZArtisanQualification,
	qq.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues
	
FROM
	QCTOQualification qq
	left join lkpNQFLevel lev on qq.NQFLevelID = lev.id
	left join lkpQCTOQualificationType qqt on qq.QCTOQualificationTypeId = qqt.ID
	left join Qualification alterQ on qq.QualificationID = alterQ.ID
	left join Qualification q on qq.QualificationID = q.ID
	left join LkpOfoOccupation ooc on qq.OFOOccupationID = ooc.ID

```
</details>    

<details>

<summary>Q&A</summary>

1. ArtisanQualificationYesNoID (current value 1,2 but expert it's 0,1)
   ```
   select distinct ArtisanQualificationYesNoID from QCTOQualification
   ```

</details>


## QCTOLearnership

<details>

<summary>QCTOLearnership DDL</summary>

```sql

CREATE TABLE MQA.dbo.QCTOLearnership (
	ID int IDENTITY(1,1) NOT NULL,
	LearnershipCode nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	LearnershipTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	QCTOLearnershipTypeId int NOT NULL,
	QualificationID int NOT NULL,
	NQFLevelID int NOT NULL,
	Credits int NOT NULL,
	OFOOccupationID int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	MinimumElectiveCredits int NULL,
	ArtisanLearnershipYesNoID int NULL,
	MigrationRecordID int NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	LastEnrolmentDate datetime NULL,
	LastAchievementDate datetime NULL,
	CONSTRAINT PK_QCTOLearnership PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>

1. find out ArtisanLearnershipYesNoID values

`select distinct ArtisanLearnershipYesNoID from QCTOLearnership`

</details>

<details>

<summary>QCTOLearnership Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	qls.LearnershipTitle as ZZLearnershipTitle,
	qls.LearnershipCode as ZZLearnershipCode,
	qlst.description as ZZQCTOLearnershipType,
	q.SAQAQualificationID as "ZZQualification_ID[ZZSaqaQualificationCode]",
	lev.SAQACode as ZZNqfLevel,
	qls.Credits as ZZCredits,
	qls.Registrationstartdate as Registrationstartdate,
	qls.Registrationenddate as Registrationenddate,
	qls.LastEnrolmentDate as	ZZLastEnrolmentDate,
	qls.LastAchievementDate as ZZLastAchievementDate,
	qls.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
	CASE qls.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	CASE qls.ArtisanLearnershipYesNoID WHEN 1 THEN 'N' WHEN 2 THEN 'Y' END AS ZZArtisanLearnership,
	qls.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
FROM 
	QCTOLearnership qls 
	left join lkpNQFLevel lev on qls.NQFLevelID = lev.id
	left join Qualification q on qls.QualificationID = q.ID 
	left join lkpQCTOLearnershipType qlst on qls.QCTOLearnershipTypeId = qlst.ID 
	left join LkpOfoOccupation ooc on qls.OFOOccupationID = ooc.ID

```
</details>

## QCTOSkillsProgramme

<details>

<summary>QCTOSkillsProgramme DDL</summary>

```sql

CREATE TABLE MQA.dbo.QCTOSkillsProgramme (
	ID int IDENTITY(1,1) NOT NULL,
	SkillsProgrammeCode nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	SkillsProgrammeTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	NQFLevelID int NOT NULL,
	Credits int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	QualityAssuranceBodyID int NOT NULL,
	SkillsProgrammeTypeID int NOT NULL,
	QualificationID int NULL,
	AETLevelID int NULL,
	OFOOccupationID int NULL,
	MinimumElectiveCredits int NULL,
	SkillsProgrammeGrantTypeID int NULL,
	MigrationRecordID int NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	IsOHS tinyint DEFAULT 0 NULL,
	LastEnrolmentDate datetime NULL,
	CONSTRAINT PK_QCTOSkillsProgramme PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>

1. get IsOHS values

`select distinct IsOHS from QCTOSkillsProgramme`

</details>

<details>

<summary>QCTOSkillsProgramme Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	qsp.SkillsProgrammeCode as ZZSkillsProgrammeCode,
	qsp.SkillsProgrammeTitle as ZZSkillsProgrammeTitle,
	q.SAQAQualificationID as "ZZQualification_ID[ZZSaqaQualificationCode]",
	lev.SAQACode as ZZNqfLevel,
	qsp.Credits as ZZCredits,
	lspt.description AS ZZSkillsProgrammeType,
	spgt.description as ZZSkillsProgrammeGrantType,
	al.description AS ZZAETLevel,
	qsp.Registrationstartdate as Registrationstartdate,
	qsp.Registrationenddate as Registrationenddate,
	qsp.LastEnrolmentDate as	ZZLastEnrolmentDate,
	qsp.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
	CASE qsp.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	CASE qsp.IsOHS WHEN 0 THEN 'N' END AS ZZIsOHS,
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	qsp.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
FROM 
	QCTOSkillsProgramme qsp 
	left join lkpNQFLevel lev on qsp.NQFLevelID = lev.id
	left join Qualification q on qsp.QualificationID = q.ID 
	left join LkpOfoOccupation ooc on qsp.OFOOccupationID = ooc.ID
	left join lkpQualityAssuranceBody qab on qsp.QualityAssuranceBodyID = qab.ID
	left join lkpSkillsProgrammeType lspt on qsp.SkillsProgrammeTypeID = lspt.id
	left join lkpAETLevel al on qsp.AETLevelID = al.id
	left join lkpSkillsProgrammeGrantType spgt on qsp.SkillsProgrammeGrantTypeID = spgt.id


```
</details>


## SkillsProgramme

<details>

<summary>SkillsProgramme DDL</summary>

```sql

CREATE TABLE MQA.dbo.SkillsProgramme (
	ID int IDENTITY(1,1) NOT NULL,
	SkillsProgrammeCode nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	SkillsProgrammeTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	NQFLevelID int NOT NULL,
	Credits int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	QualityAssuranceBodyID int NOT NULL,
	SkillsProgrammeTypeID int NOT NULL,
	QualificationID int NULL,
	AETLevelID int NULL,
	OFOOccupationID int NULL,
	MinimumElectiveCredits int NULL,
	SkillsProgrammeGrantTypeID int NULL,
	MigrationRecordID int NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	IsOHS tinyint DEFAULT 0 NULL,
	CONSTRAINT PK_SkillsProgramme PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>



</details>

<details>

<summary>SkillsProgramme Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	sp.SkillsProgrammeCode as ZZSkillsProgrammeCode,
	sp.SkillsProgrammeTitle as ZZSkillsProgrammeTitle,
	lev.SAQACode as ZZNqfLevel,
	sp.Credits as ZZCredits,
	sp.Registrationstartdate as Registrationstartdate,
	sp.Registrationenddate as Registrationenddate,
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	lspt.description AS ZZSkillsProgrammeType,
	q.SAQAQualificationID as "ZZQualification_ID[ZZSaqaQualificationCode]",
	al.description AS ZZAETLevel,
	sp.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
	spgt.description as ZZSkillsProgrammeGrantType,
	CASE sp.IsOHS WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsOHS,
	CASE sp.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	sp.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
FROM 
	SkillsProgramme sp 
	left join lkpNQFLevel lev on sp.NQFLevelID = lev.id
	left join Qualification q on sp.QualificationID = q.ID 
	left join lkpAETLevel al on sp.AETLevelID = al.id
	left join LkpOfoOccupation ooc on sp.OFOOccupationID = ooc.ID
	left join lkpSkillsProgrammeGrantType spgt on sp.SkillsProgrammeGrantTypeID = spgt.id
	left join lkpSkillsProgrammeType lspt on sp.SkillsProgrammeTypeID = lspt.id
	left join lkpQualityAssuranceBody qab on sp.QualityAssuranceBodyID = qab.ID

```

</details>

<details>

<summary>Q&A</summary>
1. SkillsProgrammeCode is not unique
`SELECT SkillsProgrammeCode FROM SkillsProgramme group by SkillsProgrammeCode having count(*) > 1`
</details>

## Qualification

<details>

<summary>Qualification DDL</summary>

```sql

CREATE TABLE MQA.dbo.Qualification (
	ID int IDENTITY(1,1) NOT NULL,
	SAQAQualificationID nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	SAQAQualificationTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	NQFLevelID int NOT NULL,
	Credits int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	LastEnrolmentDate datetime NOT NULL,
	LastAchievementDate datetime NOT NULL,
	QualityAssuranceBodyID int NOT NULL,
	QualificationTypeId int NOT NULL,
	ReplacementQualificationID int NULL,
	NewRegistrationStartDate datetime NULL,
	NewRegistrationEndDate datetime NULL,
	NewLastEnrolmentDate datetime NULL,
	NewLastAchievementDate datetime NULL,
	OFOOccupationID int NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	MigrationRecordID int NULL,
	IsReplacement tinyint NULL,
	IsReregistered tinyint NULL,
	MinimumElectiveCredits int NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_Qualification PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>



</details>

<details>

<summary>Qualification Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	q.SAQAQualificationID as ZZSAQAQualificationCode,
	q.SAQAQualificationTitle as ZZSAQAQualificationTitle,
	lev.SAQACode as ZZNqfLevel,
	q.Credits as ZZCredits,
	q.Registrationstartdate as Registrationstartdate,
	q.Registrationenddate as Registrationenddate,
	q.LastEnrolmentDate AS ZZLastEnrolmentDate,
	q.LastAchievementDate as ZZLastAchievementDate,
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	qt.description as ZZQualificationType,
	rq.SAQAQualificationID as "ZZReplacementQualification_ID[ZZSAQAQualificationCode]",
	q.NewRegistrationStartDate as ZZNewRegistrationStartDate,
	q.NewRegistrationEndDate as ZZNewRegistrationEndDate,
	q.NewLastEnrolmentDate as ZZNewLastEnrolmentDate,
	q.NewLastAchievementDate AS ZZNewLastAchievementDate,
	CASE q.IsReplacement WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReplacement,
	CASE q.IsReregistered WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReregistered,
	q.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
	CASE q.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	q.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
FROM 
	Qualification q 
	left join lkpNQFLevel lev on q.NQFLevelID = lev.id
	left join lkpQualityAssuranceBody qab on q.QualityAssuranceBodyID = qab.ID
	left join lkpQualificationType qt on q.QualificationTypeId = qt.ID 
	left join Qualification rq on q.ReplacementQualificationID = rq.ID 
	left join LkpOfoOccupation ooc on q.OFOOccupationID = ooc.ID

```
</details>


## QCTOModule

<details>

<summary>QCTOModule DDL</summary>

```sql

CREATE TABLE MQA.dbo.QCTOModule (
	ID int IDENTITY(1,1) NOT NULL,
	ModuleCode nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	ModuleTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	QualityAssuranceBodyID int NULL,
	Credits int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	LastEnrolmentDate datetime NOT NULL,
	LastAchievementDate datetime NOT NULL,
	NQFLevelID int NOT NULL,
	LearningTypeID int NOT NULL,
	ModuleTypeID int NOT NULL,
	OFOOccupationID int NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_QCTOModule PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>



</details>

<details>

<summary>QCTOModule Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	qm.ModuleCode as ZZModuleCode,
	qm.ModuleTitle as ZZModuleTitle,
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	qm.Credits as ZZCredits,
	qm.Registrationstartdate as Registrationstartdate,
	qm.Registrationenddate as Registrationenddate,
	qm.LastEnrolmentDate AS ZZLastEnrolmentDate,
	qm.LastAchievementDate as ZZLastAchievementDate,
	lev.SAQACode as ZZNqfLevel,
	lt.description as ZZLearningType,
	mt.description as ZZModuleType,
	CASE qm.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	qm.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
FROM 
	QCTOModule qm 
	left join lkpNQFLevel lev on qm.NQFLevelID = lev.id
	left join lkpQualityAssuranceBody qab on qm.QualityAssuranceBodyID = qab.ID
	left join LkpOfoOccupation ooc on qm.OFOOccupationID = ooc.ID
	left join lkpLearningType lt on qm.LearningTypeID = lt.ID 
	left join lkpModuleType mt ON qm.ModuleTypeID = mt.ID 

```
</details>

<details>

<summary>Q&A</summary>

</details>

## Module

<details>

<summary>Module DDL</summary>

```sql

CREATE TABLE MQA.dbo.Module (
	ID int IDENTITY(1,1) NOT NULL,
	ModuleCode nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	ModuleTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	QualityAssuranceBodyID int NULL,
	Credits int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	LastEnrolmentDate datetime NOT NULL,
	LastAchievementDate datetime NOT NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	MigrationRecordID int NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_Module PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>



</details>

<details>

<summary>Module Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	m.ModuleCode as ZZModuleCode,
	m.ModuleTitle as ZZModuleTitle,
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	m.Credits as ZZCredits,
	m.Registrationstartdate as Registrationstartdate,
	m.Registrationenddate as Registrationenddate,
	m.LastEnrolmentDate AS ZZLastEnrolmentDate,
	m.LastAchievementDate as ZZLastAchievementDate,
	CASE m.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	m.id as "ZZMigrationCode/K"
FROM 
	Module m 
	left join lkpQualityAssuranceBody qab on m.QualityAssuranceBodyID = qab.ID

```
</details>

<details>

<summary>Q&A</summary>

</details>


## QCTOLearnershipModule

<details>

<summary>QCTOLearnershipModule DDL</summary>

```sql

CREATE TABLE MQA.dbo.QCTOLearnershipModule (
	ID int IDENTITY(1,1) NOT NULL,
	QCTOLearnershipID int NOT NULL,
	QCTOModuleID int NOT NULL,
	ModuleTypeID int NOT NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_QCTOLearnershipModule PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>

select ModuleCode from QCTOModule where isDeleted = 0 group by ModuleCode having count (*) > 1
	
select ql.LearnershipCode from QCTOLearnership ql where isDeleted = 0 group by ql.LearnershipCode having COUNT (*) > 1

</details>

<details>

<summary>QCTOLearnershipModule Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	ql.LearnershipCode as "ZZQctoLearnership_ID[ZZLearnershipCode]",
	qm.ModuleCode as "ZZQCTOModule_ID[ZZModuleCode]",
	mt.description as ZZModuleType,
	
	CASE qlm.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	qlm.id as "ZZMigrationCode/K"
FROM 
	QCTOLearnershipModule qlm 
	left join QCTOLearnership ql on qlm.QCTOLearnershipID = ql.ID 
	left join QCTOModule qm on qlm.QCTOModuleID = qm.ID 
	left join lkpModuleType mt on qlm.ModuleTypeID = mt.ID 

```
</details>

<details>

<summary>Q&A</summary>

</details>

## QCTOSkillsProgrammeModule

<details>

<summary>QCTOSkillsProgrammeModule DDL</summary>

```sql

CREATE TABLE MQA.dbo.QCTOSkillsProgrammeModule (
	ID int IDENTITY(1,1) NOT NULL,
	QCTOSkillsProgrammeID int NOT NULL,
	QCTOModuleID int NOT NULL,
	ModuleTypeID int NOT NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_QCTOSkillsProgrammeModule PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>



</details>

<details>

<summary>QCTOSkillsProgrammeModule Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	qsp.SkillsProgrammeCode as "ZZQctoSkillsProgramme_ID[ZZSkillsProgrammeCode]",
	qm.ModuleCode as "ZZQCTOModule_ID[ZZModuleCode]",
	mt.description as ZZModuleType,
	
	CASE qspm.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	qspm.id as "ZZMigrationCode/K"
FROM 
	QCTOSkillsProgrammeModule qspm 
	left join QCTOSkillsProgramme qsp on qspm.QCTOSkillsProgrammeID = qsp.ID 
	left join QCTOModule qm on qspm.QCTOModuleID = qm.ID 
	left join lkpModuleType mt on qspm.ModuleTypeID = mt.ID 

```

</details>

<details>

<summary>Q&A</summary>

</details>


## UnitStandard

<details>

<summary>UnitStandard DDL</summary>

```sql

CREATE TABLE MQA.dbo.UnitStandard (
	ID int IDENTITY(1,1) NOT NULL,
	SAQAUnitStandardID nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	SAQAUnitStandardTitle nvarchar(250) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	NQFLevelID int NOT NULL,
	Credits int NOT NULL,
	RegistrationStartDate datetime NOT NULL,
	RegistrationEndDate datetime NOT NULL,
	LastEnrolmentDate datetime NOT NULL,
	LastAchievementDate datetime NOT NULL,
	ReplacementUnitStandardID int NULL,
	NewRegistrationStartDate datetime NULL,
	NewRegistrationEndDate datetime NULL,
	NewLastEnrolmentDate datetime NULL,
	NewLastAchievementDate datetime NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	MigrationRecordID int NULL,
	QualityAssuranceBodyID int NULL,
	IsReplacement tinyint NULL,
	IsReregistered tinyint NULL,
	MQAUnitStandardID nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_UnitStandard PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>



</details>

<details>

<summary>UnitStandard Query</summary>

```sql

SELECT
	'*' as "AD_Org_ID[Name]",
	us.SAQAUnitStandardID as ZZSaqaUnitStandardCode,
	us.SAQAUnitStandardTitle as ZZSaqaUnitStandardTitle,
	lev.SAQACode as ZZNqfLevel,
	us.Credits as ZZCredits,
	us.Registrationstartdate as Registrationstartdate,
	us.Registrationenddate as Registrationenddate,
	us.LastEnrolmentDate as	ZZLastEnrolmentDate,
	us.LastAchievementDate as ZZLastAchievementDate,
	rus.SAQAUnitStandardID AS "ZZReplacementUnitStandard_ID[ZZSaqaUnitStandardCode]",
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	us.NewRegistrationStartDate as ZZNewRegistrationStartDate,
	us.NewRegistrationEndDate as ZZNewRegistrationEndDate,
	us.NewLastEnrolmentDate as ZZNewLastEnrolmentDate,
	us.NewLastAchievementDate as ZZNewLastAchievementDate,
	CASE us.IsReplacement WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReplacement,
	CASE us.IsReregistered WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReregistered,
	CASE us.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	us.MQAUnitStandardID as ZZMqaUnitStandardCode,
	us.id as "ZZMigrationCode/K"
FROM
	UnitStandard us
	left join UnitStandard rus on us.ReplacementUnitStandardID = rus.id
	left join lkpNQFLevel lev on us.NQFLevelID = lev.id
	left join lkpQualityAssuranceBody qab on us.QualityAssuranceBodyID = qab.ID

```
</details>

<details>

<summary>Q&A</summary>

</details>


## QCTOLearnership

<details>

<summary>LearnershipUnitStandard DDL</summary>

```sql

CREATE TABLE MQA.dbo.LearnershipUnitStandard (
	ID int IDENTITY(1,1) NOT NULL,
	LearnershipID int NOT NULL,
	UnitStandardID int NOT NULL,
	UnitStandardTypeID int NOT NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_LearnershipUnitStandard PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>

1. UnitStandard (moment not unique so use ZZMigrateValues)
`select SAQAUnitStandardID from UnitStandard where IsDeleted = 0 group by SAQAUnitStandardID having count (*) > 1`

2. Learnership (moment unique so can use idempiere lookup by query LearnershipCode)
`select LearnershipCode from Learnership where IsDeleted = 0 group by LearnershipCode having count (*) > 1`

3. lkpUnitStandardType (unique so can use idempiere lookup by query LearnershipCode)
`select description from lkpUnitStandardType where IsDeleted = 0 group by description having count (*) > 1`

</details>

<details>

<summary>LearnershipUnitStandard Query</summary>

```sql

select
	'*' as "AD_Org_ID[Name]",
	lsu.id as "ZZMigrationCode/K",
	CASE us.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	ls.LearnershipCode as "ZZLearnership_ID[ZZLearnershipCode]",
	lust.Description as ZZUnitStandardType,
	'ZZUnitStandard:ZZUnitStandard_id:' + CAST(us.id as NVARCHAR(12)) as ZZMigrateValues
from 
	LearnershipUnitStandard lsu
	left join UnitStandard us on lsu.UnitStandardID = us.id
	left join Learnership ls on lsu.LearnershipID = ls.ID 
	left join lkpUnitStandardType lust on lsu.UnitStandardTypeID = lsu.ID 

```
</details>

<details>

<summary>Q&A</summary>

</details>


## SkillsProgrammeUnitStandard

<details>

<summary>SkillsProgrammeUnitStandard DDL</summary>

```sql

CREATE TABLE MQA.dbo.SkillsProgrammeUnitStandard (
	ID int IDENTITY(1,1) NOT NULL,
	SkillsProgrammeID int NOT NULL,
	UnitStandardID int NOT NULL,
	UnitStandardTypeID int NOT NULL,
	DateCreated datetime NOT NULL,
	CreatedBy int NOT NULL,
	DateUpdated datetime NOT NULL,
	UpdatedBy int NOT NULL,
	IsDeleted tinyint NOT NULL,
	SysStartTime datetime2 DEFAULT sysutcdatetime() NOT NULL,
	SysEndTime datetime2 DEFAULT CONVERT([datetime2],'9999-12-31 23:59:59.9999999') NOT NULL,
	CONSTRAINT PK_SkillsProgrammeUnitStandard PRIMARY KEY (ID)
);

```

</details>

<details>

<summary>validate data</summary>

1. SkillsProgramme (moment SkillsProgrammeCode isn't unique so use ZZMigrateValues to matching)
`select SkillsProgrammeCode from SkillsProgramme where IsDeleted  = 0 group by SkillsProgrammeCode having count(*) > 0`

2. UnitStandard (moment not unique so use ZZMigrateValues)
`select SAQAUnitStandardID from UnitStandard where IsDeleted = 0 group by SAQAUnitStandardID having count (*) > 1`

3. lkpUnitStandardType (unique so can use idempiere lookup by query LearnershipCode)
`select description from lkpUnitStandardType where IsDeleted = 0 group by description having count (*) > 1`

</details>

<details>

<summary>SkillsProgrammeUnitStandard Query</summary>

```sql


select
	'*' as "AD_Org_ID[Name]",
	spus.id as "ZZMigrationCode/K",
	CASE spus.IsDeleted WHEN 0 THEN 'Y' WHEN 1 THEN 'N' END AS IsActive,
	lust.Description as ZZUnitStandardType,
	CONCAT_WS (';',
         	'ZZSkillsProgramme:ZZSkillsProgramme_ID:' + CAST(sp.id as NVARCHAR(12)),
         	'ZZUnitStandard:ZZUnitStandard_id:' + CAST(us.id as NVARCHAR(12))
         ) as ZZMigrateValues
from 
	SkillsProgrammeUnitStandard spus
	left join UnitStandard us on spus.UnitStandardID = us.id
	left join SkillsProgramme sp on spus.SkillsProgrammeID = sp.ID 
	left join lkpUnitStandardType lust on spus.UnitStandardTypeID = lust.ID 

```
</details>

<details>

<summary>Q&A</summary>

</details>


