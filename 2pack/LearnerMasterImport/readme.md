## prepare

run zzApplyMigrateValues.sql to create sql function zzApplyMigrateValues on postgresql

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
