update AD_Column set ReadOnlyLogic = null where AD_Column_UU in ('d5fd7bd1-a5d9-445c-8865-f4c46e94ffd9', '40e8f623-c300-43be-9846-deb7ecd5b864', '5c6a80fa-9923-4549-b416-1302ad096d30')
update AD_Field set ReadOnlyLogic = null where AD_Field_UU in ('e1a921be-d006-42ae-b252-3ce2a1d1881e')

update ad_field set EntityType = 'MQA Learner' where AD_Tab_ID=1000292





-- check ad

SELECT ae."name" elemName, af."name" fieldName, ac."name" colName, at."name" tabName, aw."name" winName
FROM 
	AD_Column ac inner join ad_field af on ac.ad_column_id = af.ad_column_id 
	inner join ad_tab at on at.ad_tab_id = af.ad_tab_id 
	inner join ad_window aw on aw.ad_window_id = at.ad_window_id 
	inner join ad_element ae on ac.ad_element_id = ae.ad_element_id 
where
    AD_Column_UU='09f68d41-87b3-4f95-8279-8dd8ad391027' 
	AD_Column_UU IN ('cf996da7-b7d0-4d1f-9890-0314834b97ae', '6e3c46bb-1d09-4257-9750-433d41773570', '330d33f8-7428-42a3-bb48-b64aa945f531')


-- id
select ad_user.ZZ_ID_Passport_No from ad_user WHERE ad_user.ZZ_ID_Passport_No is not null

update ad_user set ZZ_ID_Passport_No = '6412025156084' where ZZ_ID_Passport_No = '6412025156085'
			
-- password
select password from ad_user where email = 'ynaidoo@ntier.co.za'
select password from ad_user where email = 'hieplq@hasuvimex.vn'
select password from ad_user where email = 'maugustine@ntier.co.za'


-- SDL
select value from C_BPartner where EXISTS (select * from ZZ_Levy_Paying where ZZ_Levy_Paying.C_BPartner_ID = C_BPartner.C_BPartner_ID)
and exists (
    
    select * from ZZ_WSP_ATR_Approvals INNER JOIN  ZZ_Program_Master_Data ON (
    	ZZ_Program_Master_Data_UU='5ebe2392-69ce-4cf2-88a5-cf6787f7bad6') -- EDP Application by Employers
    	-- c3eb1942-c79d-4208-93e1-b99ee38a73f6 -- EDP Application by Indivadua
    inner join C_Year on (C_Year.C_Year_ID=ZZ_Program_Master_Data.C_Year_ID AND ZZ_WSP_ATR_Approvals.ZZ_Financial_Year= C_Year.FiscalYear)
        
    where ZZ_WSP_ATR_Approvals.C_BPartner_ID = C_BPartner.C_BPartner_ID and ZZ_Grant_Status = 'A'
    
    )
    
    select * from ZZQctoQualification
SELECT 
	tb.name 
from 
	ad_table tb inner join AD_Ref_Table reTb on tb.ad_table_id  = reTb.ad_table_id
	inner join AD_Reference re on re.ad_reference_id = reTb.ad_reference_id 
where re."name" = 'ZZ_Achievement_Status_Ref'

SELECT zzApplyMigrateValues('ZZQctoLearnership');

select * from ad_menu

truncate ZZLearnership

Database error querying standard table: zzlkpofooccupation

select count (*) from ZZQualification


SELECT t.ad_tree_id, treetype, node_id, parent_id, seqno
FROM ad_tree t
LEFt JOIN ad_treenode tn ON  tn.ad_tree_id=t.ad_tree_id
WHERE TreeType = 'MM'
order by t.ad_tree_id, node_id, parent_id, seqno


SELECT tr.AD_Tree_ID 
FROM 
AD_Tree tr JOIN AD_Table t ON (tr.AD_Table_ID=t.AD_Table_ID) 
WHERE tr.AD_Client_ID=? AND tr.TreeType=? AND tr.IsActive='Y' AND t.TableName = ? ORDER BY tr.AD_Tree_ID
select * from ZZLkpOfoOccupationTree