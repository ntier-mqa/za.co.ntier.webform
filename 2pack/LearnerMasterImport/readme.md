## Migrate stragtegy

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

      `q.SAQAQualificationID as "ZZQualification_ID[Value]",`

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
   		q.SAQAQualificationID as "ZZQualification_ID[Value]",
   		lev.SAQACode as ZZNqfLevel,
   		ls.Credits as ZZCredits,
   		CASE when qab.saqacode is null or qab.saqacode = 'N/A' THEN CAST(qab.id as nvarchar(250)) ELSE qab.saqacode END AS ZZQualityAssuranceBody,
   		ls.Registrationstartdate as Registrationstartdate,
   		ls.Registrationenddate as Registrationenddate,
   		ls.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
   		CASE ls.IsDeleted WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS IsActive,
   		ls.id as ZZMigrationCode,
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
	alterQ.SAQAQualificationID as "ZZReplacementQualification_ID[Value]",
	qq.NewRegistrationStartDate as ZZNewRegistrationStartDate,
	qq.NewRegistrationEndDate as ZZNewRegistrationEndDate,
	qq.NewLastEnrolmentDate as ZZNewLastEnrolmentDate,
	qq.NewLastAchievementDate as ZZNewLastAchievementDate,
	CASE qq.IsReplacement WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReplacement,
	CASE qq.IsReregistered WHEN 0 THEN 'N' WHEN 1 THEN 'Y' END AS ZZIsReregistered,
	qq.MinimumElectiveCredits AS ZZMinimumElectiveCredits,
	q.SAQAQualificationID as "ZZQualification_ID[Value]",
	CASE qq.ArtisanQualificationYesNoID WHEN 1 THEN 'N' WHEN 2 THEN 'Y' END AS ZZArtisanQualification,
	qq.id as ZZMigrationCode,
	CONCAT_WS(':', 'ZZLkpOfoOccupation', 'ZZLkpOfoOccupation_id', ooc.id) as ZZMigrateValues    --CONCAT_WS (
	--	CONCAT_WS(':', 'ZZLkpOfoOccupation', 'ZZLkpOfoOccupation_id', ooc.id),
	--	CONCAT_WS(':', 'ZZLkpOfoOccupation', ooc.id),
	--	CONCAT_WS(':', 'ref', 'ZZLkpNqfLevel', 'ZZNqfLevel', lev.id)
	--) as ZZMigrateValuesFROM
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
