## Delete old data

```
SQL
-- RUN one time at first time apply LearnerAssessment.zip to remove old columns/tables
alter table zzsdf drop column zzperson_id
drop table zzperson
```

## Delete old data

```SQL
DELETE FROM ad_ref_list rl
USING ad_reference ar
WHERE rl.ad_reference_id = ar.ad_reference_id
  AND ar.name IN (
    'ZZLkpNqfLevel',
    'ZZLkpLearnershipType',
    'ZZLkpAetLevel',
    'ZZLkpQualityAssuranceBody',
    'ZZLkpQctoQualificationType',
    'ZZLkpQctoLearnershipType',
    'ZZLkpSkillsProgrammeType',
    'ZZLkpLearningType',
    'ZZLkpModuleType',
    'ZZLkpSkillsProgrammeGrantType',
    'ZZLkpQualificationType',
    'ZZLkpUnitStandardType'
  );

delete from ZZLearnershipUnitStandard;
delete from SkillsProgrammeUnitStandard;

delete from ZZQCTOLearnershipModule;
delete from ZZQCTOSkillsProgrammeModule;

delete from zzqctolearnership;
delete from zzlearnership;
delete from ZZQCTOSkillsProgramme;

delete from  zzlinkassessorskillsprogramme;

delete from ZZSkillsProgramme where ZZSkillsProgramme_ID not in (select ZZSkillsProgramme_ID from c_bp_skillsprogramme);
delete from zzqctoqualification;

delete from  zzlinkassessorqualification;

delete from ZZQualification;
delete from ZZModule;
delete from ZZQCTOModule;
delete from ZZUnitStandard;
```

## Login to system

### Import Reference
1. UnChecked "Parent link column" on AD_Reference_ID on table AD_Ref_List save and reset cache
![1780456488990](image/updateLearner/1780456488990.png)

2. Open window "Reference" use csv import file  reference.csv
3. Open window "Reference List" use csv import file listReference.csv

4. Checked "Parent link column" on AD_Reference_ID on table AD_Ref_List save and reset cache
![1780456488990](image/updateLearner/1780456488990.png)

## Login to client (MQA)
### Import LkpOfoOccupation
1. Open Tree window to setup OFO Occupation tree and run verify tree
![1780471953694](image/updateLearner/1780471953694.png)

2. Open "OFO Year" to import list of years

3. open windown "OFO Occupation Tree" to csv import file LkpOfoOccupation.csv

### csv import bellow list
1. Open window "Module" use csv import file module.csv
2. Open window "QCTOModule" use csv import file QCTOModule.csv
3. Open window "Unit Standard" use csv import file UnitStandard.csv
4. Open window "Qualification" use csv import file Qualification.csv
4. Open window "QCTO Qualification" use csv import file qctoQualification.csv
4. Open window "Skills Programme" use csv import file SkillsProgramme.csv
4. Open window "QCTO Skills Programme" use csv import file QCTOSkillsProgramme.csv
4. Open window "Learnership" use csv import file learnership.csv
4. Open window "QCTO learnership" use csv import file qctolearnership.csv
4. Open window "QCTO Skills Programme Module" use csv import file QCTOSkillsProgrammeModule.csv
4. Open window "QCTO Learnership Module" use csv import file QCTOLearnershipModule.csv
4. Open window "Skills Programme UnitStandard" use csv import file SkillsProgrammeUnitStandard.csv
4. Open window "Learnership UnitStandard" use csv import file LearnershipUnitStandard.csv

## Open a database GUI like dbeaver
### update sql function by run zzApplyMigrateValues.sql
### resolve and update reference by do bellow step for each table
1. run `SELECT zzApplyMigrateValues([tableName])`
2. verify error by run `select ZZMigrateValues from [tableName] where ZZMigrateValues like '% - ERR:%'`
3. incase found error then resolve error 
4. clear error by run before script and run again from 1
```sql
UPDATE ZZQctoModule
SET ZZMigrateValues = SPLIT_PART(ZZMigrateValues, ' - ERR:', 1)
WHERE ZZMigrateValues LIKE '% - ERR:%';
```
5. list of table run update reference
```sql
SELECT zzApplyMigrateValues('ZZModule');
select ZZMigrateValues from ZZModule where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZQctoModule');
select ZZMigrateValues from ZZQctoModule where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZUnitStandard');
select ZZMigrateValues from ZZUnitStandard where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZQualification');
select ZZMigrateValues from ZZQualification where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('zzqctoqualification');
select ZZMigrateValues from zzqctoqualification where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZSkillsProgramme');
select ZZMigrateValues from ZZSkillsProgramme where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZQCTOSkillsProgramme');
select ZZMigrateValues from ZZQCTOSkillsProgramme where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('zzlearnership');
select ZZMigrateValues from zzlearnership where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('zzqctolearnership');
select ZZMigrateValues from zzqctolearnership where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZQCTOSkillsProgrammeModule');
select ZZMigrateValues from ZZQCTOSkillsProgrammeModule where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZQCTOLearnershipModule');
select ZZMigrateValues from ZZQCTOLearnershipModule where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZSkillsProgrammeUnitStandard');
select ZZMigrateValues from ZZSkillsProgrammeUnitStandard where ZZMigrateValues like '% - ERR:%'

SELECT zzApplyMigrateValues('ZZLearnershipUnitStandard');
select ZZMigrateValues from ZZLearnershipUnitStandard where ZZMigrateValues like '% - ERR:%
```