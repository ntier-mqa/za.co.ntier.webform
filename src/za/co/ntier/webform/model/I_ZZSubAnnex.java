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

/** Generated Interface for ZZSubAnnex
 *  @author iDempiere (generated) 
 *  @version Release 12
 */
@SuppressWarnings("all")
public interface I_ZZSubAnnex 
{

    /** TableName=ZZSubAnnex */
    public static final String Table_Name = "ZZSubAnnex";

    /** AD_Table_ID=1000060 */
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

	public I_ZZAnnexure getZZAnnexure() throws RuntimeException;

    /** Column name ZZFieldStudy */
    public static final String COLUMNNAME_ZZFieldStudy = "ZZFieldStudy";

	/** Set Field Of Study	  */
	public void setZZFieldStudy (int ZZFieldStudy);

	/** Get Field Of Study	  */
	public int getZZFieldStudy();

    /** Column name ZZLearners */
    public static final String COLUMNNAME_ZZLearners = "ZZLearners";

	/** Set Number Of learners	  */
	public void setZZLearners (int ZZLearners);

	/** Get Number Of learners	  */
	public int getZZLearners();

    /** Column name ZZManagers */
    public static final String COLUMNNAME_ZZManagers = "ZZManagers";

	/** Set Number Of Managers	  */
	public void setZZManagers (int ZZManagers);

	/** Get Number Of Managers	  */
	public int getZZManagers();

    /** Column name ZZRequestedProgramme */
    public static final String COLUMNNAME_ZZRequestedProgramme = "ZZRequestedProgramme";

	/** Set Requested Programme	  */
	public void setZZRequestedProgramme (int ZZRequestedProgramme);

	/** Get Requested Programme	  */
	public int getZZRequestedProgramme();

    /** Column name ZZSubAnnex_ID */
    public static final String COLUMNNAME_ZZSubAnnex_ID = "ZZSubAnnex_ID";

	/** Set Sub Annex	  */
	public void setZZSubAnnex_ID (int ZZSubAnnex_ID);

	/** Get Sub Annex	  */
	public int getZZSubAnnex_ID();

    /** Column name ZZSubAnnex_UU */
    public static final String COLUMNNAME_ZZSubAnnex_UU = "ZZSubAnnex_UU";

	/** Set ZZSubAnnex_UU	  */
	public void setZZSubAnnex_UU (String ZZSubAnnex_UU);

	/** Get ZZSubAnnex_UU	  */
	public String getZZSubAnnex_UU();

    /** Column name ZZ_Application_Form_ID */
    public static final String COLUMNNAME_ZZ_Application_Form_ID = "ZZ_Application_Form_ID";

	/** Set Application Form	  */
	public void setZZ_Application_Form_ID (int ZZ_Application_Form_ID);

	/** Get Application Form	  */
	public int getZZ_Application_Form_ID();

	public I_ZZ_Application_Form getZZ_Application_Form() throws RuntimeException;

    /** Column name ZZ_Trade_ID */
    public static final String COLUMNNAME_ZZ_Trade_ID = "ZZ_Trade_ID";

	/** Set Trade	  */
	public void setZZ_Trade_ID (int ZZ_Trade_ID);

	/** Get Trade	  */
	public int getZZ_Trade_ID();

	public I_ZZ_Trade getZZ_Trade() throws RuntimeException;
}
