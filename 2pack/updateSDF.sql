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
					'a613cbc2-fd7f-4f0b-ba82-4f646e3fa11b',    -- ZZAssessor.ZZ_AlternateIDType_ID
					'6fba0609-5c99-48fc-b69f-a3f6c6e1214e',     -- ZZSdf.ZZ_LI_Disability_ID

'85f699c9-88a7-45d9-b4da-7250b1d73c5c',  -- ZZAssessorPerson.ZZFirstName
'8a5b5875-5717-41f2-a7bc-4c7087892651', -- ZZAssessorPerson.ZZLkpTitle
'd840dc78-9c1f-4cd3-a2b4-5f41a3058bb7', -- ZZAssessorPerson.ZZMiddleName
'95536df9-e84c-4bb9-b457-653a43ac775a', -- ZZAssessorPerson.ZZSurname

'9e899de1-4db8-4b62-bea5-5eeb04db55e8',  -- ZZSdf.ZZFirstName
'd26b8088-d0b9-4c1d-a2fd-913b45302b90', -- ZZSdf.ZZLkpTitle
'0205b43c-5c69-41bd-8c2f-d119f925236b', -- ZZSdf.ZZMiddleName
'7621cb43-ecae-4012-a746-a4608062de61', -- ZZSdf.ZZSurname
'e7dccfe1-73bb-489e-acc1-557d60532293'  -- ZZSdf.ZZ_Passport_No

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

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZAssessorPerson') 
          AND LOWER(column_name)= LOWER('ZZFirstName')
    ) THEN
        UPDATE ad_user
			SET ZZFirstName = ZZAssessorPerson.ZZFirstName
			FROM ZZAssessorPerson
			WHERE ad_user.ad_user_id = ZZAssessorPerson.ad_user_id   -- The join connection
			  AND ad_user.ZZFirstName IS NULL             -- Only update if ad_user is empty
			  AND ZZAssessorPerson.ZZFirstName IS NOT NULL;
        
    END IF;

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZAssessorPerson') 
          AND LOWER(column_name)= LOWER('ZZMiddleName')
    ) THEN
        UPDATE ad_user
			SET ZZMiddleName = ZZAssessorPerson.ZZMiddleName
			FROM ZZAssessorPerson
			WHERE ad_user.ad_user_id = ZZAssessorPerson.ad_user_id   -- The join connection
			  AND ad_user.ZZMiddleName IS NULL             -- Only update if ad_user is empty
			  AND ZZAssessorPerson.ZZMiddleName IS NOT NULL;
        
    END IF;

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZAssessorPerson') 
          AND LOWER(column_name)= LOWER('ZZSurname')
    ) THEN
        UPDATE ad_user
			SET ZZSurname = ZZAssessorPerson.ZZSurname
			FROM ZZAssessorPerson
			WHERE ad_user.ad_user_id = ZZAssessorPerson.ad_user_id   -- The join connection
			  AND ad_user.ZZSurname IS NULL             -- Only update if ad_user is empty
			  AND ZZAssessorPerson.ZZSurname IS NOT NULL;
        
    END IF;

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZAssessorPerson') 
          AND LOWER(column_name)= LOWER('ZZLkpTitle')
    ) THEN
        UPDATE ad_user
			SET ZZLkpTitle = ZZAssessorPerson.ZZLkpTitle
			FROM ZZAssessorPerson
			WHERE ad_user.ad_user_id = ZZAssessorPerson.ad_user_id   -- The join connection
			  AND ad_user.ZZLkpTitle IS NULL             -- Only update if ad_user is empty
			  AND ZZAssessorPerson.ZZLkpTitle IS NOT NULL;
        
    END IF;

IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZSdf') 
          AND LOWER(column_name)= LOWER('ZZFirstName')
    ) THEN
        UPDATE ad_user
			SET ZZFirstName = ZZSdf.ZZFirstName
			FROM ZZSdf
			WHERE ad_user.ad_user_id = ZZSdf.ad_user_id   -- The join connection
			  AND ad_user.ZZFirstName IS NULL             -- Only update if ad_user is empty
			  AND ZZSdf.ZZFirstName IS NOT NULL;
        
    END IF;

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZSdf') 
          AND LOWER(column_name)= LOWER('ZZMiddleName')
    ) THEN
        UPDATE ad_user
			SET ZZMiddleName = ZZSdf.ZZMiddleName
			FROM ZZSdf
			WHERE ad_user.ad_user_id = ZZSdf.ad_user_id   -- The join connection
			  AND ad_user.ZZMiddleName IS NULL             -- Only update if ad_user is empty
			  AND ZZSdf.ZZMiddleName IS NOT NULL;
        
    END IF;

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZSdf') 
          AND LOWER(column_name)= LOWER('ZZSurname')
    ) THEN
        UPDATE ad_user
			SET ZZSurname = ZZSdf.ZZSurname
			FROM ZZSdf
			WHERE ad_user.ad_user_id = ZZSdf.ad_user_id   -- The join connection
			  AND ad_user.ZZSurname IS NULL             -- Only update if ad_user is empty
			  AND ZZSdf.ZZSurname IS NOT NULL;
        
    END IF;

	IF EXISTS (
        SELECT 1 
        FROM information_schema.columns 
        WHERE table_schema = 'adempiere'
          AND LOWER(table_name) = LOWER('ZZSdf') 
          AND LOWER(column_name)= LOWER('ZZLkpTitle')
    ) THEN
        UPDATE ad_user
			SET ZZLkpTitle = ZZSdf.ZZLkpTitle
			FROM ZZSdf
			WHERE ad_user.ad_user_id = ZZSdf.ad_user_id   -- The join connection
			  AND ad_user.ZZLkpTitle IS NULL             -- Only update if ad_user is empty
			  AND ZZSdf.ZZLkpTitle IS NOT NULL;
        
    END IF;


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

