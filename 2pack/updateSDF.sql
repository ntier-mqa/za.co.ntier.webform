DO $$ 
DECLARE 
    -- Define your list of UUIDs here
    cols text[] := ARRAY[
					'02e6c37d-42c4-4c4a-ad64-ef8b4fbe6491', 
 					'3b7c57b9-5af5-409a-9051-4312737ba78e', 
					'41757649-0974-49de-8ea9-583c6e0f71cb',    -- don't use ZZSdfType replace by ZZSdfRoleType
 					'23c8c816-7854-461b-ac08-69d296d3ad6f',    -- ZZAssessorPerson.EMail
 					'758211d5-7f94-41bb-bc6e-db88c96b927d',    -- ZZAssessorPerson.Birthday
 					'98eb82d8-79f4-4268-a003-21a727e0d2aa',    -- ZZAssessorPerson.phone
 					'0bfab1de-24db-4bca-bee7-25771bd5b6b1',    -- ZZAssessorPerson.phone 2
 					'5dd410c7-4f4e-4e60-a96d-4008ccbc3bb8',    -- ZZAssessorPerson.ZZ_ID_Passport_No
 					--'8690e0b0-bc9d-4421-b94e-90648e99f40c',    -- ad_user.ZZSurname
					'0bf96ec9-62a3-4c5a-bc21-a35f56d1ee10',    -- ad_user.Surname
					'20997b9e-afdc-48b9-a6f2-0ae6e6f381e5',    -- ad_user.ZZ_Passport_No
					'e23d281d-9995-460a-ad78-c09b61c69b2d',     -- ZZSdf.ZZ_AlternateIDType_ID
					'a613cbc2-fd7f-4f0b-ba82-4f646e3fa11b'     -- ZZAssessor.ZZ_AlternateIDType_ID

				];
	fields text[] := ARRAY[
															-- Business Partner (MQA) > Organisation Linkage
					'6ddce600-523a-451e-a991-9f9be04bdfd3', -- SDL Number
					'e046f501-ecdc-40ec-acd5-d879a9afb42c', -- Link End (Financial Year)
					'ff4aed9d-5fea-4555-ac65-5ee117039c86', -- Link Start (Financial Year)
 					'7ca41a10-3089-4e93-9bb7-70c355e00217'  -- Partner Parent
				];

	refers text [] := ARRAY[
					'618c2cc6-011a-462b-89e4-6a7b7ef6e354'   -- don't use ZZSdfType replace by ZZSdfRoleType
                ]; 

	 --menus text [] := ARRAY[
					--'d1c16a4b-9a05-41fc-a2f8-050f1f383431' -- menu MaintainOrg
	--			]; 

	tabs text [] := ARRAY[ 
					'f6e58064-8a20-41d8-96bb-57556dcc7beb' -- user -> sdf (old)
				]; 
BEGIN 
	-- remove col/field
    DELETE FROM AD_Field_Trl WHERE AD_FIELd_id IN 
        (SELECT AD_Field_id FROM AD_Field WHERE AD_Column_ID IN
            (SELECT AD_Column_ID FROM AD_Column WHERE AD_Column_UU = ANY(cols))
        );

    DELETE FROM AD_Field WHERE AD_Column_ID IN
        	(SELECT AD_Column_ID FROM AD_Column WHERE AD_Column_UU = ANY(cols))
		OR AD_Field_UU = ANY(fields);

    DELETE FROM AD_Column_Trl WHERE AD_Column_ID IN 
        (SELECT AD_Column_ID FROM AD_Column WHERE AD_Column_UU = ANY(cols));

    DELETE FROM AD_Column WHERE AD_Column_UU = ANY(cols);
	
	-- update old ZZSdfOrganisation when add role type
	update ZZSdfOrganisation set zzsdfroletype = 'Primary' where zzsdfroletype is null and zzreplacingprimarysdf = 'Y';
	update ZZSdfOrganisation set zzsdfroletype = 'Secondary' where zzsdfroletype is null and (zzreplacingprimarysdf = 'N' OR zzreplacingprimarysdf is NULL);

	-- remove reference. it's cacade so child data is remove follow
	DELETE FROM AD_Reference WHERE AD_Reference_UU = ANY(refers);
	
	-- remove menu only
    -- DELETE FROM AD_Tree_Favorite_Node WHERE AD_Menu_ID IN (SELECT AD_Menu_ID FROM AD_Menu WHERE AD_Menu_UU = ANY(menus));

	--DELETE FROM AD_Menu WHERE AD_Menu_UU = ANY(menus);

	DELETE FROM AD_Tab WHERE AD_Tab_UU = ANY(tabs);
   
END $$;
 
