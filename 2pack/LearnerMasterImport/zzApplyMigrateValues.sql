CREATE OR REPLACE FUNCTION zzApplyMigrateValues(p_table_name TEXT)
RETURNS VOID AS $$
DECLARE
    v_row RECORD; -- Renamed from 'r' to prevent variable/alias namespace collision
    v_tokens TEXT[];
    v_token TEXT;
    parts TEXT[];
    
    v_validation_type CHAR(1);
    v_table_lookup TEXT;
    v_update_col TEXT;
    v_value TEXT;
    v_ref_name TEXT;
    v_resolved_id TEXT;
    
    -- Exact case-sensitive names resolved from system catalogs
    v_real_table_name TEXT;
    v_real_table_lookup TEXT;
    v_real_migrate_col TEXT;
    v_real_update_col TEXT;
    v_lookup_id_col TEXT;
    v_lookup_mig_col TEXT;
    
    -- NEW: Variables to store column data types dynamically
    v_update_col_type TEXT;
    v_lookup_mig_type TEXT;
    
    -- Error handling variables
    v_error_msg TEXT;
    v_has_error BOOLEAN;
BEGIN
    -- 1. Find the exact case-sensitive spelling of the target table name
    SELECT table_name INTO v_real_table_name
    FROM information_schema.tables
    WHERE lower(table_name) = lower(p_table_name)
      AND table_schema = current_schema()
    LIMIT 1;
    
    IF v_real_table_name IS NULL THEN
        RAISE EXCEPTION 'Table "%" not found in the current schema.', p_table_name;
    END IF;

    -- 2. Find the exact case-sensitive spelling of the ZZMigrateValues column
    SELECT column_name INTO v_real_migrate_col
    FROM information_schema.columns
    WHERE table_name = v_real_table_name 
      AND lower(column_name) = 'zzmigratevalues'
      AND table_schema = current_schema()
    LIMIT 1;
    
    IF v_real_migrate_col IS NULL THEN
        RAISE EXCEPTION 'Column "ZZMigrateValues" not found (case-insensitive) in table %', v_real_table_name;
    END IF;

    -- 3. Loop through records using dynamically verified identifiers
    FOR v_row IN EXECUTE format(
        'SELECT ctid AS row_id, %I AS migrate_values FROM %I WHERE %I IS NOT NULL AND %I <> %L AND %I NOT LIKE %L', 
        v_real_migrate_col, v_real_table_name, v_real_migrate_col, v_real_migrate_col, '', v_real_migrate_col, 'err:%'
    ) 
    LOOP
        v_has_error := FALSE;
        v_error_msg := NULL;
        
        v_tokens := string_to_array(v_row.migrate_values, ';');
        
        FOREACH v_token IN ARRAY v_tokens LOOP
            v_token := trim(v_token);
            IF v_token = '' THEN CONTINUE; END IF;
            
            parts := string_to_array(v_token, ':');
            
            -------------------------------------------------------------------
            -- CASE A: Pattern starts with 'ref'
            -------------------------------------------------------------------
            IF parts[1] = 'ref' THEN
                IF array_length(parts, 1) < 4 THEN
                    CONTINUE;
                END IF;
                
                v_ref_name   := parts[2];
                v_update_col := parts[3];
                v_value      := parts[4];
                
                SELECT validationtype INTO v_validation_type 
                FROM ad_reference 
                WHERE name = v_ref_name;
                
                IF v_validation_type IS NULL OR v_validation_type NOT IN ('L', 'T') THEN
                    v_has_error := TRUE;
                    v_error_msg := 'ValidationType must be L or T (Found: ' || COALESCE(v_validation_type, 'NULL') || ')';
                    EXIT; 
                END IF;
                
                -- Resolve target update column case AND data type safely
                SELECT column_name, data_type INTO v_real_update_col, v_update_col_type
                FROM information_schema.columns
                WHERE table_name = v_real_table_name 
                  AND lower(column_name) = lower(v_update_col)
                  AND table_schema = current_schema()
                LIMIT 1;
                
                IF v_real_update_col IS NULL THEN
                    v_has_error := TRUE;
                    v_error_msg := 'Target update column ' || v_update_col || ' does not exist in table ' || v_real_table_name;
                    EXIT;
                END IF;
                
                -- Logic for List type ('L')
                IF v_validation_type = 'L' THEN
                    SELECT rl.value INTO v_resolved_id
                    FROM ad_ref_list rl 
                    -- Changed alias here from 'r' to 'ad_ref' to be safe
                    INNER JOIN ad_reference ad_ref ON ad_ref.ad_reference_id = rl.ad_reference_id 
                    WHERE ad_ref.name = v_ref_name AND rl.description = v_value;
                    
                    IF v_resolved_id IS NULL THEN
                        v_has_error := TRUE;
                        v_error_msg := 'v_resolved_id is null for reference list description: ' || v_value;
                        EXIT;
                    END IF;
                    
                    -- Dynamic CAST added to UPDATE
                    EXECUTE format('UPDATE %I SET %I = CAST($1 AS %s) WHERE ctid = $2', v_real_table_name, v_real_update_col, v_update_col_type) 
                    USING v_resolved_id, v_row.row_id;
                    
                -- Logic for Table type ('T')
                ELSIF v_validation_type = 'T' THEN
                    SELECT tb.name INTO v_table_lookup
                    FROM ad_table tb 
                    INNER JOIN ad_ref_table reTb ON tb.ad_table_id = reTb.ad_table_id
                    INNER JOIN ad_reference re ON re.ad_reference_id = reTb.ad_reference_id 
                    WHERE re.name = v_ref_name;
                    
                    IF v_table_lookup IS NULL THEN
                        v_has_error := TRUE;
                        v_error_msg := 'v_table_lookup is null for reference: ' || v_ref_name;
                        EXIT;
                    END IF;
                    
                    -- Resolve exact case-sensitive name of the lookup table
                    SELECT table_name INTO v_real_table_lookup
                    FROM information_schema.tables
                    WHERE lower(table_name) = lower(v_table_lookup)
                      AND table_schema = current_schema()
                    LIMIT 1;
                    
                    IF v_real_table_lookup IS NULL THEN
                        v_has_error := TRUE;
                        v_error_msg := 'Lookup table ' || v_table_lookup || ' does not exist in database';
                        EXIT;
                    END IF;
                    
                    -- Resolve column configurations and types for the lookup table safely
                    SELECT column_name INTO v_lookup_id_col FROM information_schema.columns WHERE table_name = v_real_table_lookup AND lower(column_name) = lower(v_table_lookup || '_id') AND table_schema = current_schema() LIMIT 1;
                    SELECT column_name, data_type INTO v_lookup_mig_col, v_lookup_mig_type FROM information_schema.columns WHERE table_name = v_real_table_lookup AND lower(column_name) = 'zzmigrationcode' AND table_schema = current_schema() LIMIT 1;
                    
                    IF v_lookup_id_col IS NULL OR v_lookup_mig_col IS NULL THEN
                        v_has_error := TRUE;
                        v_error_msg := 'Missing ID/ZZMigrationCode columns in lookup table: ' || v_real_table_lookup;
                        EXIT;
                    END IF;
                    
                    BEGIN
                        -- Dynamic CAST added to lookup query SELECT
                        EXECUTE format('SELECT %I FROM %I WHERE %I = CAST($1 AS %s)', v_lookup_id_col, v_real_table_lookup, v_lookup_mig_col, v_lookup_mig_type)
                        USING v_value INTO v_resolved_id;
                        
                        IF v_resolved_id IS NULL THEN
                            v_has_error := TRUE;
                            v_error_msg := 'v_resolved_id is null in table ' || v_real_table_lookup || ' for code: ' || v_value;
                            EXIT;
                        END IF;
                        
                        -- Dynamic CAST added to UPDATE
                        EXECUTE format('UPDATE %I SET %I = CAST($1 AS %s) WHERE ctid = $2', v_real_table_name, v_real_update_col, v_update_col_type) 
                        USING v_resolved_id, v_row.row_id;
                        
                    EXCEPTION WHEN OTHERS THEN
                        v_has_error := TRUE;
                        v_error_msg := 'Database error querying lookup table ' || v_real_table_lookup || ': ' || SQLERRM;
                        EXIT;
                    END;
                END IF;
                
            -------------------------------------------------------------------
            -- CASE B: Standard Table Lookup Pattern
            -------------------------------------------------------------------
            ELSE
                IF array_length(parts, 1) = 1 THEN
                    CONTINUE;
                END IF;
                
                v_table_lookup := parts[1];
                
                IF array_length(parts, 1) = 3 THEN
                    v_update_col := parts[2];
                    v_value      := parts[3];
                ELSIF array_length(parts, 1) = 2 THEN
                    v_update_col := v_table_lookup || '_ID';
                    v_value      := parts[2];
                ELSE
                    CONTINUE;
                END IF;
                
                -- Resolve target update column casing AND data type dynamically
                SELECT column_name, data_type INTO v_real_update_col, v_update_col_type
                FROM information_schema.columns
                WHERE table_name = v_real_table_name 
                  AND lower(column_name) = lower(v_update_col)
                  AND table_schema = current_schema()
                LIMIT 1;
                
                IF v_real_update_col IS NULL THEN
                    v_has_error := TRUE;
                    v_error_msg := 'Target update column ' || v_update_col || ' does not exist in table ' || v_real_table_name;
                    EXIT;
                END IF;
                
                -- Resolve exact name of the lookup table
                SELECT table_name INTO v_real_table_lookup
                FROM information_schema.tables
                WHERE lower(table_name) = lower(v_table_lookup)
                      AND table_schema = current_schema()
                LIMIT 1;
                
                IF v_real_table_lookup IS NULL THEN
                    v_has_error := TRUE;
                    v_error_msg := 'Lookup table ' || v_table_lookup || ' does not exist in database';
                    EXIT;
                END IF;
                
                -- Resolve lookup table internal column casing AND data type dynamically
                SELECT column_name INTO v_lookup_id_col FROM information_schema.columns WHERE table_name = v_real_table_lookup AND lower(column_name) = lower(v_table_lookup || '_id') AND table_schema = current_schema() LIMIT 1;
                SELECT column_name, data_type INTO v_lookup_mig_col, v_lookup_mig_type FROM information_schema.columns WHERE table_name = v_real_table_lookup AND lower(column_name) = 'zzmigrationcode' AND table_schema = current_schema() LIMIT 1;
                
                IF v_lookup_id_col IS NULL OR v_lookup_mig_col IS NULL THEN
                    v_has_error := TRUE;
                    v_error_msg := 'Missing ID/ZZMigrationCode columns in lookup table: ' || v_real_table_lookup;
                    EXIT;
                END IF;
                
                BEGIN
                    -- Dynamic CAST added to lookup query SELECT
                    EXECUTE format('SELECT %I FROM %I WHERE %I = CAST($1 AS %s)', v_lookup_id_col, v_real_table_lookup, v_lookup_mig_col, v_lookup_mig_type)
                    USING v_value INTO v_resolved_id;
                    
                    IF v_resolved_id IS NULL THEN
                        v_has_error := TRUE;
                        v_error_msg := 'v_resolved_id is null in table ' || v_real_table_lookup || ' for code: ' || v_value;
                        EXIT;
                    END IF;
                    
                    -- Dynamic CAST added to UPDATE
                    EXECUTE format('UPDATE %I SET %I = CAST($1 AS %s) WHERE ctid = $2', v_real_table_name, v_real_update_col, v_update_col_type) 
                    USING v_resolved_id, v_row.row_id;
                    
                EXCEPTION WHEN OTHERS THEN
                    v_has_error := TRUE;
                    v_error_msg := 'Database error querying standard table ' || v_real_table_lookup || ': ' || SQLERRM;
                    EXIT;
                END;
                
            END IF;
        END LOOP;
        
        -- Safe fallback error update using verified casing paths
        IF v_has_error THEN
            EXECUTE format('UPDATE %I SET %I = $1 WHERE ctid = $2', v_real_table_name, v_real_migrate_col)
            USING 'err:' || v_row.migrate_values || ' - ' || v_error_msg, v_row.row_id;
        END IF;
        
    END LOOP;
END;
$$ LANGUAGE plpgsql;