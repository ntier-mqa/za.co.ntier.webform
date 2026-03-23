DELETE FROM AD_Field_Trl WHERE AD_Field_ID IN 
    (SELECT AD_Field_ID FROM AD_Field WHERE AD_Field_UU IN 
        ('14e3e49c-dc03-4194-95b7-7caae7d656ac', '704b360e-cb99-4aaf-ac48-8cef5126c297',
        '438f14b2-fa83-41e6-a9c6-21321a7e3d71', 'ddb8e71b-29b7-4eed-8443-1f312f65bc57', 
        'f6d95c1c-242f-4a42-ab49-262a64c6c664', 'd340d747-9ef8-4a60-9723-6e2ea3bd677a',
        'b03447ab-5a62-46f8-811e-d3d9e970e001', 'fe5d8cde-a973-4ecc-9150-8bbb2fafb8ac')
    );

DELETE FROM AD_Field WHERE AD_Field_UU IN 
    ('14e3e49c-dc03-4194-95b7-7caae7d656ac', '704b360e-cb99-4aaf-ac48-8cef5126c297',
    '438f14b2-fa83-41e6-a9c6-21321a7e3d71', 'ddb8e71b-29b7-4eed-8443-1f312f65bc57', 
    'f6d95c1c-242f-4a42-ab49-262a64c6c664', 'd340d747-9ef8-4a60-9723-6e2ea3bd677a',
    'b03447ab-5a62-46f8-811e-d3d9e970e001', 'fe5d8cde-a973-4ecc-9150-8bbb2fafb8ac');

DELETE FROM AD_ViewColumn WHERE AD_ViewColumn_UU='a516709a-8e57-4250-b988-8c6b1621f6a9';

DELETE FROM AD_Column_Trl WHERE AD_Column_ID IN (SELECT AD_Column_ID FROM AD_Column WHERE AD_Column_UU IN 
    ('bf589f1a-e646-49d8-854d-1bd1e9443b15', '03edaf4e-e0c5-41d0-aae5-9fcf04f1055d',  
    '5399da41-5fdf-4a84-8db0-3b33d2302dcc', '2471f5f9-8289-476d-b32e-5e063b06f84a', 
    'ae2968e2-6a31-4bb8-9b3c-46862650345f', '0da09684-8d1c-4649-a3fe-b1cea14dc2f6',
    '6f120539-f81e-44d0-9e67-e37b3c6e21b0', '0bf96ec9-62a3-4c5a-bc21-a35f56d1ee10'));

DELETE FROM AD_Column WHERE AD_Column_UU IN 
    ('bf589f1a-e646-49d8-854d-1bd1e9443b15', '03edaf4e-e0c5-41d0-aae5-9fcf04f1055d',  
    '5399da41-5fdf-4a84-8db0-3b33d2302dcc', '2471f5f9-8289-476d-b32e-5e063b06f84a', 
    'ae2968e2-6a31-4bb8-9b3c-46862650345f', '0da09684-8d1c-4649-a3fe-b1cea14dc2f6',
    '6f120539-f81e-44d0-9e67-e37b3c6e21b0', '0bf96ec9-62a3-4c5a-bc21-a35f56d1ee10');

select AD_Field_UU from ad_field where ad_column_id=1003450

 select name from ad_column where ad_column_id=1003450

update AD_Column SET IsIdentifier = 'N' WHERE AD_Column_UU='af4a39ce-b2f8-4074-a773-00a339ecc32c';



 SELECT Referenced_Table_ID 
 FROM AD_ViewComponent INNER JOIN AD_Table ON 
    (AD_ViewComponent.AD_Table_ID = AD_Table.AD_Table_ID) WHERE AD_ViewComponent.AD_Table_ID = ''1000001''





select password from ad_user where email = 'ynaidoo@ntier.co.za'
select password from ad_user where email = 'hieplq@hasuvimex.vn'

SELECT count (*) FROM ZZ_Program_Master_Data WHERE ZZ_Program_Master_Data_UU = '7e8aa88d-a1ee-4377-a7e7-6887d4be1c73'

select * FROM ZZ_Program_Master_Data WHERE ZZ_Program_Master_Data_UU = '3fa8546b-415d-44ff-9459-88f52a3b1b68'

select SDLNumber from Organisation SDLNumber

select name, name2 from C_BPartner where name2  is not null value = 'L010739243'

select count (*) from C_BPartner where name2  is not null

select * from C_BPartner where name2 = name

select ZZSdf.* from ZZSdf INNER JOIN ad_user ON ZZSdf.AD_User_ID = ad_user.AD_User_ID
WHERE ad_user.email = 'maugustine@ntier.co.za'
select ZZSdf.* from ZZSdf INNER JOIN ad_user ON ZZSdf.AD_User_ID = ad_user.AD_User_ID
WHERE ad_user.email = 'hieplq@hasuvimex.vn'


update ZZSdf  set ZZ_LI_HomeLanguage_ID = null where ZZSdf_id =  1000000

select * from ZZ_FormContact where ZZ_Application_Form_ID = 1000199

select ZZ_Grant_Status from ZZ_WSP_ATR_Approvals where  ZZ_WSP_ATR_Approvals_UU='4091af0b-0f25-4e3f-bc62-5a6758698f8d'

# select sdl

select ad_user.ZZ_ID_Passport_No from ad_user WHERE ad_user.ZZ_ID_Passport_No is not null

select value from C_BPartner where EXISTS (select * from ZZ_Levy_Paying where ZZ_Levy_Paying.C_BPartner_ID = C_BPartner.C_BPartner_ID)
and exists (
    
    select * from ZZ_WSP_ATR_Approvals INNER JOIN  ZZ_Program_Master_Data ON (ZZ_Program_Master_Data_ID=1000073)
    inner join C_Year on (C_Year.C_Year_ID=ZZ_Program_Master_Data.C_Year_ID AND ZZ_WSP_ATR_Approvals.ZZ_Financial_Year= C_Year.FiscalYear)
        
    where ZZ_WSP_ATR_Approvals.C_BPartner_ID = C_BPartner.C_BPartner_ID and ZZ_Grant_Status = 'A'
    
    )


select ZZExecutiveStatus from ZZ_EDP_Application where ZZ_EDP_Application_id = 1000021

DELETE FROM ZZ_EDP_Application WHERE ZZ_EDP_Application_ID=1000015
select * from ZZ_EDP_Application where ZZ_EDP_Application_ID=1000015

select c_bpartner.C_BPartner_ID , c_bpartner.value from ZZ_Application_Form inner join c_bpartner on ZZ_Application_Form.C_BPartner_ID = c_bpartner.C_BPartner_ID 
where ZZ_Program_Master_Data_ID = 1000072 AND c_bpartner.value = 'L720726027'

select value from c_bpartner where c_bpartner_id = 1042782
1053355

select * from ZZ_Program_Master_Data where ZZ_Program_Master_Data_ID = 1000072

select ZZ_Program_Master_Data_ID , C_BPartner_ID  from ZZ_Application_Form where ZZ_Program_Master_Data_ID=


select * from ZZDocumentUploadFile where ZZDocumentUploadFile_id = 1000084

CREATE OR REPLACE VIEW ZZSdfOrganisation_v(ZZFirstName, ZZMiddleName, ZZSurname, ZZ_ID_Passport_No, Phone, Phone2, Email, ZZ_LI_HighestEducation_ID, ZZHighestEducationDesc, ZZAccreditedTrainingProvider, ZZExperience, ZZCurrentOccupation, ZZYearsInOccupation, OrgName, C_BPartner_ID, ZZ_SDL_No, ZZActingForEmployer, ZZSdfFunction, ZZAppointmentProcedure, ZZReplacingPrimarySDF, ZZSecondarySdf, AD_Client_ID, AD_Org_ID, Created, CreatedBy, IsActive, Updated, UpdatedBy, ZZSdfOrganisation_ID, zz_docstatus, zz_docaction, zz_date_approved, ZZSdfOrganisation_v_ID, zz_date_rejected, ZZSdfOrganisation_v_UU, zz_approved_id, zz_rejected_id) AS SELECT sdf.ZZFirstName AS ZZFirstName, sdf.ZZMiddleName AS ZZMiddleName, sdf.ZZSurname AS ZZSurname, usr.ZZ_ID_Passport_No AS ZZ_ID_Passport_No, usr.Phone AS Phone, usr.Phone2 AS Phone2, usr.Email AS Email, sdf.ZZ_LI_HighestEducation_ID AS ZZ_LI_HighestEducation_ID, sdf.ZZHighestEducationDesc AS ZZHighestEducationDesc, sdf.ZZAccreditedTrainingProvider AS ZZAccreditedTrainingProvider, sdf.ZZExperience AS ZZExperience, sdf.ZZCurrentOccupation AS ZZCurrentOccupation, sdf.ZZYearsInOccupation AS ZZYearsInOccupation, org.Name AS OrgName, org.C_BPartner_ID AS C_BPartner_ID, org.Value AS ZZ_SDL_No, orgLink.ZZActingForEmployer AS ZZActingForEmployer, orgLink.ZZSdfFunction AS ZZSdfFunction, orgLink.ZZAppointmentProcedure AS ZZAppointmentProcedure, orgLink.ZZReplacingPrimarySDF AS ZZReplacingPrimarySDF, orgLink.ZZSecondarySdf AS ZZSecondarySdf, orgLink.AD_Client_ID AS AD_Client_ID, orgLink.AD_Org_ID AS AD_Org_ID, orgLink.Created AS Created, orgLink.CreatedBy AS CreatedBy, orgLink.IsActive AS IsActive, orgLink.Updated AS Updated, orgLink.UpdatedBy AS UpdatedBy, orgLink.ZZSdfOrganisation_ID AS ZZSdfOrganisation_ID, zzStatus.name AS zz_docstatus, orglink.zz_docaction AS zz_docaction, orglink.zz_date_approved AS zz_date_approved, orgLink.ZZSdfOrganisation_ID AS ZZSdfOrganisation_v_ID, orglink.zz_date_rejected AS zz_date_rejected, orgLink.ZZSdfOrganisation_UU AS ZZSdfOrganisation_v_UU, orglink.zz_approved_id AS zz_approved_id, orglink.zz_rejected_id AS zz_rejected_id FROM ZZSdfOrganisation orgLink INNER JOIN ZZSdf sdf ON orgLink.zzsdf_id = sdf.zzsdf_id INNER JOIN ad_user usr ON sdf.ad_user_id = usr.ad_user_id INNER JOIN c_bpartner org on orgLink.c_bpartner_id = org.c_bpartner_id INNER JOIN AD_Ref_List zzStatus ON (zzStatus.AD_Reference_ID = 1000006 AND orgLink.ZZ_DocStatus = zzStatus.Value)

select  ZZSdfOrganisation_ID from zzsdforganisation_v where 
zz_sdl_no = 'L010701540'
ZZSdfOrganisation_ID='1000006'



select zz_application_form.* from ZZ_EDP_Application inner join zz_application_form on ZZ_EDP_Application.ZZ_Application_Form_ID = zz_application_form.ZZ_Application_Form_ID

where ZZ_ID_Passport_No = '0101011031081'