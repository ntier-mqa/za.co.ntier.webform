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
    'ZZLkpQualificationType'
  );

delete from ZZQCTOLearnershipModule
delete from ZZQCTOSkillsProgrammeModule
delete from ZZSkillsProgramme where ZZSkillsProgramme_ID not in (select ZZSkillsProgramme_ID from c_bp_skillsprogramme)
delete from zzlearnership
delete from zzqctoqualification
delete from zzqctolearnership
delete from ZZQCTOSkillsProgramme
delete from ZZQualification
delete from ZZModule
delete from ZZQCTOModule
delete from ZZUnitStandard



SELECT zzApplyMigrateValues('ZZModule');
select ZZMigrateValues from ZZModule where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQctoModule');
select ZZMigrateValues from ZZQctoModule where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZLearnershipUnitStandard');
select ZZMigrateValues from ZZLearnershipUnitStandard where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZSkillsProgrammeUnitStandard');
select ZZMigrateValues from ZZSkillsProgrammeUnitStandard where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQctoLearnership');
select ZZMigrateValues from ZZQctoLearnership where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQctoLearnershipModule');
select ZZMigrateValues from ZZQctoLearnershipModule where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQctoQualification');
select ZZMigrateValues from ZZQctoQualification where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQctoSkillsProgramme');
select ZZMigrateValues from ZZQctoSkillsProgramme where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQctoSkillsProgrammeModule');
select ZZMigrateValues from ZZQctoSkillsProgrammeModule where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZQualification');
select ZZMigrateValues from ZZQualification where ZZMigrateValues like 'err%'

SELECT zzApplyMigrateValues('ZZSkillsProgramme');
select ZZMigrateValues from ZZSkillsProgramme where ZZMigrateValues like 'err%'
