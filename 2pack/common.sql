-- id
select ad_user.ZZ_ID_Passport_No from ad_user WHERE ad_user.ZZ_ID_Passport_No is not null

update ad_user set ZZ_ID_Passport_No = '6412025156084' where ZZ_ID_Passport_No = '6412025156085'

delete from zzsdf where AD_User_ID=1000007

delete from ZZSdfOrganisation where zzsdf_id in (select zzsdf_id from zzsdf where AD_User_ID=1000007)

delete from zzbankingdetails where 
	ZZSdfOrganisation_id in (select ZZSdfOrganisation_id from ZZSdfOrganisation 
									inner join zzsdf on zzsdf.zzsdf_id = ZZSdfOrganisation.zzsdf_id 
									where  zzsdf.AD_User_ID=1000007)


delete from zz_wsp_atr_submitted where 
	ZZSdfOrganisation_id in (select ZZSdfOrganisation_id from ZZSdfOrganisation 
									inner join zzsdf on zzsdf.zzsdf_id = ZZSdfOrganisation.zzsdf_id 
									where  zzsdf.AD_User_ID=1000007)
									
-- password
select password from ad_user where email = 'ynaidoo@ntier.co.za'
select password from ad_user where email = 'hieplq@hasuvimex.vn'
select password from ad_user where email = 'maugustine@ntier.co.za'

select ZZ_AlternateIDType_ID from ad_user where email = 'hieplq@hasuvimex.vn'
update ad_user set ZZ_ID_Passport_No = null where email = 'hieplq@hasuvimex.vn'

-- SDL
select value from C_BPartner where EXISTS (select * from ZZ_Levy_Paying where ZZ_Levy_Paying.C_BPartner_ID = C_BPartner.C_BPartner_ID)
and exists (
    
    select * from ZZ_WSP_ATR_Approvals INNER JOIN  ZZ_Program_Master_Data ON (
    	ZZ_Program_Master_Data_UU='5ebe2392-69ce-4cf2-88a5-cf6787f7bad6') -- EDP Application by Employers
    	-- c3eb1942-c79d-4208-93e1-b99ee38a73f6 -- EDP Application by Indivadua
    inner join C_Year on (C_Year.C_Year_ID=ZZ_Program_Master_Data.C_Year_ID AND ZZ_WSP_ATR_Approvals.ZZ_Financial_Year= C_Year.FiscalYear)
        
    where ZZ_WSP_ATR_Approvals.C_BPartner_ID = C_BPartner.C_BPartner_ID and ZZ_Grant_Status = 'A'
    
    )

