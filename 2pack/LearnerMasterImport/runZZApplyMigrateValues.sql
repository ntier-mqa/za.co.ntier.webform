-- run only one time before apply learner
-- alter table zzsdf drop column zzperson_id
-- drop table zzperson

-- run only one time before apply MQA Learner (client)
-- update ZZLkpOfoOccupation set ZZMigrationCode = description::NUMERIC where description is not null
-- update ZZSkillsProgramme set (ZZSkillsProgrammeCode, zzSkillsProgrammeTitle) = (value, name)
-- update ZZQualification set (ZZSaqaQualificationCode, ZZSaqaQualificationTitle) = (value, name)

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
delete from ZZSkillsProgramme where ZZSkillsProgramme_ID not in (select ZZSkillsProgramme_ID from c_bp_skillsprogramme);
delete from zzqctoqualification;
delete from ZZQualification;
delete from ZZModule;
delete from ZZQCTOModule;
delete from ZZUnitStandard;




SELECT zzApplyMigrateValues('ZZModule');
select ZZMigrateValues from ZZModule where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZQctoModule');
select ZZMigrateValues from ZZQctoModule where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZUnitStandard');
select ZZMigrateValues from ZZUnitStandard where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZQualification');
select ZZMigrateValues from ZZQualification where ZZMigrateValues like '%ERR%'
--
UPDATE zzqctoqualification
SET ZZMigrateValues = SPLIT_PART(ZZMigrateValues, ' - ERR:', 1)
WHERE ZZMigrateValues LIKE '% - ERR:%';

SELECT 
    ZZMigrateValues AS OriginalValue,
    SPLIT_PART(ZZMigrateValues, ' - ERR:', 1) AS WillLookLikeThis
FROM zzqctoqualification
WHERE ZZMigrateValues LIKE '% - ERR:%';
--

SELECT zzApplyMigrateValues('zzqctoqualification');
select ZZMigrateValues from zzqctoqualification where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZSkillsProgramme');
select ZZMigrateValues from ZZSkillsProgramme where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZQCTOSkillsProgramme');
select ZZMigrateValues from ZZQCTOSkillsProgramme where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('zzlearnership');
select ZZMigrateValues from zzlearnership where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('zzqctolearnership');
select ZZMigrateValues from zzqctolearnership where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZQCTOSkillsProgrammeModule');
select ZZMigrateValues from ZZQCTOSkillsProgrammeModule where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZQCTOLearnershipModule');
select ZZMigrateValues from ZZQCTOLearnershipModule where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZSkillsProgrammeUnitStandard');
select ZZMigrateValues from ZZSkillsProgrammeUnitStandard where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('ZZLearnershipUnitStandard');
select ZZMigrateValues from ZZLearnershipUnitStandard where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('aaaa');
select ZZMigrateValues from aaaa where ZZMigrateValues like '%ERR%'

SELECT zzApplyMigrateValues('aaaa');
select ZZMigrateValues from aaaa where ZZMigrateValues like '%ERR%'

