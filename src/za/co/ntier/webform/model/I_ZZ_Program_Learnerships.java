/******************************************************************************
 * Product: iDempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2012 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package za.co.ntier.webform.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for ZZ_Program_Learnerships
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
@SuppressWarnings("all")
public interface I_ZZ_Program_Learnerships 
{

    /** TableName=ZZ_Program_Learnerships */
    public static final String Table_Name = "ZZ_Program_Learnerships";

    /** AD_Table_ID=1000039 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 6 - System - Client 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(6);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Tenant.
	  * Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Unit.
	  * Organizational entity within tenant
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Unit.
	  * Organizational entity within tenant
	  */
	public int getAD_Org_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Line */
    public static final String COLUMNNAME_Line = "Line";

	/** Set Line No.
	  * Unique line for this document
	  */
	public void setLine (int Line);

	/** Get Line No.
	  * Unique line for this document
	  */
	public int getLine();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name ZZ_Is_Accred_SLA_Req */
    public static final String COLUMNNAME_ZZ_Is_Accred_SLA_Req = "ZZ_Is_Accred_SLA_Req";

	/** Set Accred/SLA Req	  */
	public void setZZ_Is_Accred_SLA_Req (boolean ZZ_Is_Accred_SLA_Req);

	/** Get Accred/SLA Req	  */
	public boolean isZZ_Is_Accred_SLA_Req();

    /** Column name ZZ_Learnerships_ID */
    public static final String COLUMNNAME_ZZ_Learnerships_ID = "ZZ_Learnerships_ID";

	/** Set Learnerships.
	  * Learnerships master
	  */
	public void setZZ_Learnerships_ID (int ZZ_Learnerships_ID);

	/** Get Learnerships.
	  * Learnerships master
	  */
	public int getZZ_Learnerships_ID();

	public I_ZZ_Learnerships getZZ_Learnerships() throws RuntimeException;

    /** Column name ZZ_Learnerships_Type */
    public static final String COLUMNNAME_ZZ_Learnerships_Type = "ZZ_Learnerships_Type";

	/** Set Learnerships Type	  */
	public void setZZ_Learnerships_Type (String ZZ_Learnerships_Type);

	/** Get Learnerships Type	  */
	public String getZZ_Learnerships_Type();

    /** Column name ZZ_NQF_Level */
    public static final String COLUMNNAME_ZZ_NQF_Level = "ZZ_NQF_Level";

	/** Set NQF Level	  */
	public void setZZ_NQF_Level (String ZZ_NQF_Level);

	/** Get NQF Level	  */
	public String getZZ_NQF_Level();

    /** Column name ZZ_Program_Learnerships_ID */
    public static final String COLUMNNAME_ZZ_Program_Learnerships_ID = "ZZ_Program_Learnerships_ID";

	/** Set Program Learnerships	  */
	public void setZZ_Program_Learnerships_ID (int ZZ_Program_Learnerships_ID);

	/** Get Program Learnerships	  */
	public int getZZ_Program_Learnerships_ID();

    /** Column name ZZ_Program_Learnerships_UU */
    public static final String COLUMNNAME_ZZ_Program_Learnerships_UU = "ZZ_Program_Learnerships_UU";

	/** Set ZZ_Program_Learnerships_UU	  */
	public void setZZ_Program_Learnerships_UU (String ZZ_Program_Learnerships_UU);

	/** Get ZZ_Program_Learnerships_UU	  */
	public String getZZ_Program_Learnerships_UU();

    /** Column name ZZ_Program_Master_Data_ID */
    public static final String COLUMNNAME_ZZ_Program_Master_Data_ID = "ZZ_Program_Master_Data_ID";

	/** Set Program Master Data	  */
	public void setZZ_Program_Master_Data_ID (int ZZ_Program_Master_Data_ID);

	/** Get Program Master Data	  */
	public int getZZ_Program_Master_Data_ID();

    /** Column name ZZ_WPA_Req */
    public static final String COLUMNNAME_ZZ_WPA_Req = "ZZ_WPA_Req";

	/** Set WPA Req	  */
	public void setZZ_WPA_Req (boolean ZZ_WPA_Req);

	/** Get WPA Req	  */
	public boolean isZZ_WPA_Req();
}
