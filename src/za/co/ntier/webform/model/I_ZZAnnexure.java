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

/** Generated Interface for ZZAnnexure
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
@SuppressWarnings("all")
public interface I_ZZAnnexure 
{

    /** TableName=ZZAnnexure */
    public static final String Table_Name = "ZZAnnexure";

    /** AD_Table_ID=1000051 */
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

    /** Column name IsShowTitle */
    public static final String COLUMNNAME_IsShowTitle = "IsShowTitle";

	/** Set Show Title	  */
	public void setIsShowTitle (boolean IsShowTitle);

	/** Get Show Title	  */
	public boolean isShowTitle();

    /** Column name Title */
    public static final String COLUMNNAME_Title = "Title";

	/** Set Title.
	  * Name this entity is referred to as
	  */
	public void setTitle (String Title);

	/** Get Title.
	  * Name this entity is referred to as
	  */
	public String getTitle();

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

    /** Column name ZZAnnexure_ID */
    public static final String COLUMNNAME_ZZAnnexure_ID = "ZZAnnexure_ID";

	/** Set Annexure Definition	  */
	public void setZZAnnexure_ID (int ZZAnnexure_ID);

	/** Get Annexure Definition	  */
	public int getZZAnnexure_ID();

    /** Column name ZZAnnexure_UU */
    public static final String COLUMNNAME_ZZAnnexure_UU = "ZZAnnexure_UU";

	/** Set ZZAnnexure_UU	  */
	public void setZZAnnexure_UU (String ZZAnnexure_UU);

	/** Get ZZAnnexure_UU	  */
	public String getZZAnnexure_UU();

    /** Column name ZZFirst */
    public static final String COLUMNNAME_ZZFirst = "ZZFirst";

	/** Set First Column	  */
	public void setZZFirst (String ZZFirst);

	/** Get First Column	  */
	public String getZZFirst();

    /** Column name ZZFirstSubcolumn */
    public static final String COLUMNNAME_ZZFirstSubcolumn = "ZZFirstSubcolumn";

	/** Set First Subcolumn	  */
	public void setZZFirstSubcolumn (String ZZFirstSubcolumn);

	/** Get First Subcolumn	  */
	public String getZZFirstSubcolumn();

    /** Column name ZZFourth */
    public static final String COLUMNNAME_ZZFourth = "ZZFourth";

	/** Set Fourth Column	  */
	public void setZZFourth (String ZZFourth);

	/** Get Fourth Column	  */
	public String getZZFourth();

    /** Column name ZZFourthSubcolumn */
    public static final String COLUMNNAME_ZZFourthSubcolumn = "ZZFourthSubcolumn";

	/** Set Fourth Subcolumn	  */
	public void setZZFourthSubcolumn (String ZZFourthSubcolumn);

	/** Get Fourth Subcolumn	  */
	public String getZZFourthSubcolumn();

    /** Column name ZZHeader */
    public static final String COLUMNNAME_ZZHeader = "ZZHeader";

	/** Set Header.
	  * Header Annexure
	  */
	public void setZZHeader (String ZZHeader);

	/** Get Header.
	  * Header Annexure
	  */
	public String getZZHeader();

    /** Column name ZZSecond */
    public static final String COLUMNNAME_ZZSecond = "ZZSecond";

	/** Set Second Column	  */
	public void setZZSecond (String ZZSecond);

	/** Get Second Column	  */
	public String getZZSecond();

    /** Column name ZZSecondSubcolumn */
    public static final String COLUMNNAME_ZZSecondSubcolumn = "ZZSecondSubcolumn";

	/** Set Second Subcolumn	  */
	public void setZZSecondSubcolumn (String ZZSecondSubcolumn);

	/** Get Second Subcolumn	  */
	public String getZZSecondSubcolumn();

    /** Column name ZZThird */
    public static final String COLUMNNAME_ZZThird = "ZZThird";

	/** Set Third Column	  */
	public void setZZThird (String ZZThird);

	/** Get Third Column	  */
	public String getZZThird();

    /** Column name ZZThirdSubcolumn */
    public static final String COLUMNNAME_ZZThirdSubcolumn = "ZZThirdSubcolumn";

	/** Set Third Subcolumn	  */
	public void setZZThirdSubcolumn (String ZZThirdSubcolumn);

	/** Get Third Subcolumn	  */
	public String getZZThirdSubcolumn();

    /** Column name ZZ_Program_Master_Data_ID */
    public static final String COLUMNNAME_ZZ_Program_Master_Data_ID = "ZZ_Program_Master_Data_ID";

	/** Set Program Master Data	  */
	public void setZZ_Program_Master_Data_ID (int ZZ_Program_Master_Data_ID);

	/** Get Program Master Data	  */
	public int getZZ_Program_Master_Data_ID();
}
