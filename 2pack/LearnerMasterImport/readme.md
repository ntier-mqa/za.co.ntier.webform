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

      `CASE ls.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive`

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
   		CASE ls.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive,
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

    1. some columns is rename

    SAQAQualificationID => ZZSAQAQualificationCode
	QCTOQualificationTypeId => ZZQctoQualificationType
	OFOOccupationID => ZZLkpOfoOccupation_ID
	ArtisanQualificationYesNoID => ZZArtisanQualification

    2. on idempiere open window "QCTO Qualification", export csv import template ZZQctoQualification_importTemplate.csv

    **this step do one time to build query on step 5 just do again when change table define**

    3. check below is unique

```sql

select count (), count (distinct(SAQAQualificationID)) from Qualification
select count (), count (distinct(description)) from lkpQCTOQualificationType

```

    4. columns need to remap after import (because code/title isn't unique so can't use lookup)
      ZZLkpOfoOccupation

```
select count (*), count (distinct(code)), count (distinct(description)) from LkpOfoOccupation
```

    5. export old data to csv

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
	--CONCAT_WS (
	--	CONCAT_WS(':', 'ZZLkpOfoOccupation', 'ZZLkpOfoOccupation_id', ooc.id),
	--	CONCAT_WS(':', 'ZZLkpOfoOccupation', ooc.id),
	--	CONCAT_WS(':', 'ref', 'ZZLkpNqfLevel', 'ZZNqfLevel', lev.id)
	--) as ZZMigrateValues
FROM
	QCTOQualification qq
	left join lkpNQFLevel lev on qq.NQFLevelID = lev.id
	left join lkpQCTOQualificationType qqt on qq.QCTOQualificationTypeId = qqt.ID
	left join Qualification alterQ on qq.QualificationID = alterQ.ID
	left join Qualification q on qq.QualificationID = q.ID
	left join LkpOfoOccupation ooc on qq.OFOOccupationID = ooc.ID

```

    => export result to csv QCTOQualification_[date].csv
	6. open window "QCTO Qualification" and import QCTOQualification_[date].csv
	7. to lookup reference id and update run bellow sql function
		"SELECT zzApplyMigrateValues('ZZQctoQualification');"

    8. on window "QCTO Qualification" search column "Migrate Values" for "%err" to verify error
	correct data when have error and run sql function again

Q&A

1. ArtisanQualificationYesNoID (current value 1,2 but expert it's 0,1)
   ```
   select distinct ArtisanQualificationYesNoID from QCTOQualification
   ```

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
	CASE qls.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive,
	CASE qls.ArtisanLearnershipYesNoID WHEN 1 THEN 'N' WHEN 2 THEN 'Y' END AS ZZArtisanLearnership,
	qls.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
	--CONCAT_WS (';',
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12)), -- ooc.id is null then this line is null and get out from expression
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12))
	--) as ZZMigrateValues
	
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
	CASE qsp.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive,
	CASE qsp.IsOHS WHEN 0 THEN 'N' END AS ZZIsOHS,
	CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
	qsp.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
	--CONCAT_WS (';',
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12)), -- ooc.id is null then this line is null and get out from expression
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12))
	--) as ZZMigrateValues
	
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
	CASE sp.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive,
	sp.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
	--CONCAT_WS (';',
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12)), -- ooc.id is null then this line is null and get out from expression
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12))
	--) as ZZMigrateValues
	
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
	CASE q.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive,
	q.id as "ZZMigrationCode/K",
	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id:' + CAST(ooc.id as NVARCHAR(12)) as ZZMigrateValues -- ooc.id null then result is null
	
	--CONCAT_WS (';',
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12)), -- ooc.id is null then this line is null and get out from expression
	--	'ZZLkpOfoOccupation:ZZLkpOfoOccupation_id' + CAST(ooc.id as NVARCHAR(12))
	--) as ZZMigrateValues
	
FROM 
	Qualification q 
	left join lkpNQFLevel lev on q.NQFLevelID = lev.id
	left join lkpQualityAssuranceBody qab on q.QualityAssuranceBodyID = qab.ID
	left join lkpQualificationType qt on q.QualificationTypeId = qt.ID 
	left join Qualification rq on q.ReplacementQualificationID = rq.ID 
	left join LkpOfoOccupation ooc on q.OFOOccupationID = ooc.ID

```
</details>