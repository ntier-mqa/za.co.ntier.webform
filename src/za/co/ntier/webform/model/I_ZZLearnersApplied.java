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
import org.compiere.model.*;
import org.compiere.util.KeyNamePair;

/** Generated Interface for ZZLearnersApplied
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
@SuppressWarnings("all")
public interface I_ZZLearnersApplied 
{

    /** TableName=ZZLearnersApplied */
    public static final String Table_Name = "ZZLearnersApplied";

    /** AD_Table_ID=1000061 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

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

    /** Column name C_City_ID */
    public static final String COLUMNNAME_C_City_ID = "C_City_ID";

	/** Set City.
	  * City
	  */
	public void setC_City_ID (int C_City_ID);

	/** Get City.
	  * City
	  */
	public int getC_City_ID();

	public org.compiere.model.I_C_City getC_City() throws RuntimeException;

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

    /** Column name DataType */
    public static final String COLUMNNAME_DataType = "DataType";

	/** Set Data Type.
	  * Type of data
	  */
	public void setDataType (String DataType);

	/** Get Data Type.
	  * Type of data
	  */
	public String getDataType();

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

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name Postal */
    public static final String COLUMNNAME_Postal = "Postal";

	/** Set ZIP.
	  * Postal code
	  */
	public void setPostal (String Postal);

	/** Get ZIP.
	  * Postal code
	  */
	public String getPostal();

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

    /** Column name ZZLearnersApplied_ID */
    public static final String COLUMNNAME_ZZLearnersApplied_ID = "ZZLearnersApplied_ID";

	/** Set Learners Applied.
	  * For input &quot;
No of Learners applied for&quot;
, &quot;
No. of Unemployed Learners&quot;
,  &quot;
Total Number of Learners Applied For&quot;
, &quot;
No. of employed Learners&quot;

	  */
	public void setZZLearnersApplied_ID (int ZZLearnersApplied_ID);

	/** Get Learners Applied.
	  * For input &quot;
No of Learners applied for&quot;
, &quot;
No. of Unemployed Learners&quot;
,  &quot;
Total Number of Learners Applied For&quot;
, &quot;
No. of employed Learners&quot;

	  */
	public int getZZLearnersApplied_ID();

    /** Column name ZZLearnersApplied_UU */
    public static final String COLUMNNAME_ZZLearnersApplied_UU = "ZZLearnersApplied_UU";

	/** Set ZZLearnersApplied_UU	  */
	public void setZZLearnersApplied_UU (String ZZLearnersApplied_UU);

	/** Get ZZLearnersApplied_UU	  */
	public String getZZLearnersApplied_UU();

    /** Column name ZZNoEmployedLearners */
    public static final String COLUMNNAME_ZZNoEmployedLearners = "ZZNoEmployedLearners";

	/** Set No Employed Learners	  */
	public void setZZNoEmployedLearners (int ZZNoEmployedLearners);

	/** Get No Employed Learners	  */
	public int getZZNoEmployedLearners();

    /** Column name ZZNoLearners */
    public static final String COLUMNNAME_ZZNoLearners = "ZZNoLearners";

	/** Set No Learners.
	  * Number of learners applying should be based on
	  */
	public void setZZNoLearners (int ZZNoLearners);

	/** Get No Learners.
	  * Number of learners applying should be based on
	  */
	public int getZZNoLearners();

    /** Column name ZZNoTotalLearners */
    public static final String COLUMNNAME_ZZNoTotalLearners = "ZZNoTotalLearners";

	/** Set Total No Learners Applied For	  */
	public void setZZNoTotalLearners (int ZZNoTotalLearners);

	/** Get Total No Learners Applied For	  */
	public int getZZNoTotalLearners();

    /** Column name ZZNoUnEmployedLearners */
    public static final String COLUMNNAME_ZZNoUnEmployedLearners = "ZZNoUnEmployedLearners";

	/** Set No UnEmployed Learners	  */
	public void setZZNoUnEmployedLearners (int ZZNoUnEmployedLearners);

	/** Get No UnEmployed Learners	  */
	public int getZZNoUnEmployedLearners();

    /** Column name ZZ_Application_Form_ID */
    public static final String COLUMNNAME_ZZ_Application_Form_ID = "ZZ_Application_Form_ID";

	/** Set Application Form	  */
	public void setZZ_Application_Form_ID (int ZZ_Application_Form_ID);

	/** Get Application Form	  */
	public int getZZ_Application_Form_ID();

	public I_ZZ_Application_Form getZZ_Application_Form() throws RuntimeException;

    /** Column name ZZ_WPAFile */
    public static final String COLUMNNAME_ZZ_WPAFile = "ZZ_WPAFile";

	/** Set WPA File	  */
	public void setZZ_WPAFile (byte[] ZZ_WPAFile);

	/** Get WPA File	  */
	public byte[] getZZ_WPAFile();
}
