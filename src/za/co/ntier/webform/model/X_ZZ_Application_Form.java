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
/** Generated Model - DO NOT CHANGE */
package za.co.ntier.webform.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for ZZ_Application_Form
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_Application_Form")
public class X_ZZ_Application_Form extends PO implements I_ZZ_Application_Form, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250816L;

    /** Standard Constructor */
    public X_ZZ_Application_Form (Properties ctx, int ZZ_Application_Form_ID, String trxName)
    {
      super (ctx, ZZ_Application_Form_ID, trxName);
      /** if (ZZ_Application_Form_ID == 0)
        {
			setName (null);
			setZZ_Application_Form_ID (0);
			setZZ_HasPivotalPlanSubmited (false);
// N
			setZZ_HasWSPSubmited (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Application_Form (Properties ctx, int ZZ_Application_Form_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Application_Form_ID, trxName, virtualColumns);
      /** if (ZZ_Application_Form_ID == 0)
        {
			setName (null);
			setZZ_Application_Form_ID (0);
			setZZ_HasPivotalPlanSubmited (false);
// N
			setZZ_HasWSPSubmited (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Application_Form (Properties ctx, String ZZ_Application_Form_UU, String trxName)
    {
      super (ctx, ZZ_Application_Form_UU, trxName);
      /** if (ZZ_Application_Form_UU == null)
        {
			setName (null);
			setZZ_Application_Form_ID (0);
			setZZ_HasPivotalPlanSubmited (false);
// N
			setZZ_HasWSPSubmited (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Application_Form (Properties ctx, String ZZ_Application_Form_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Application_Form_UU, trxName, virtualColumns);
      /** if (ZZ_Application_Form_UU == null)
        {
			setName (null);
			setZZ_Application_Form_ID (0);
			setZZ_HasPivotalPlanSubmited (false);
// N
			setZZ_HasWSPSubmited (false);
// N
        } */
    }

    /** Load Constructor */
    public X_ZZ_Application_Form (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder ("X_ZZ_Application_Form[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
	{
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_ID)
			.getPO(getC_BPartner_ID(), get_TrxName());
	}

	/** Set Business Partner.
		@param C_BPartner_ID Identifies a Business Partner
	*/
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner.
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Description.
		@param Description Optional short description of the record
	*/
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription()
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Document No.
		@param DocumentNo Document sequence number of the document
	*/
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo()
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set Name.
		@param Name Alphanumeric identifier of the entity
	*/
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName()
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Employees.
		@param NumberEmployees Number of employees
	*/
	public void setNumberEmployees (int NumberEmployees)
	{
		set_ValueNoCheck (COLUMNNAME_NumberEmployees, Integer.valueOf(NumberEmployees));
	}

	/** Get Employees.
		@return Number of employees
	  */
	public int getNumberEmployees()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NumberEmployees);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Organization Name.
		@param OrgName Name of the Organization
	*/
	public void setOrgName (String OrgName)
	{
		set_ValueNoCheck (COLUMNNAME_OrgName, OrgName);
	}

	/** Get Organization Name.
		@return Name of the Organization
	  */
	public String getOrgName()
	{
		return (String)get_Value(COLUMNNAME_OrgName);
	}

	/** Set Search Key.
		@param Value Search key for the record in the format required - must be unique
	*/
	public void setValue (String Value)
	{
		set_Value (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue()
	{
		return (String)get_Value(COLUMNNAME_Value);
	}

	/** Set Application Form.
		@param ZZ_Application_Form_ID Application Form
	*/
	public void setZZ_Application_Form_ID (int ZZ_Application_Form_ID)
	{
		if (ZZ_Application_Form_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Application_Form_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Application_Form_ID, Integer.valueOf(ZZ_Application_Form_ID));
	}

	/** Get Application Form.
		@return Application Form	  */
	public int getZZ_Application_Form_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Application_Form_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZ_Application_Form_UU.
		@param ZZ_Application_Form_UU ZZ_Application_Form_UU
	*/
	public void setZZ_Application_Form_UU (String ZZ_Application_Form_UU)
	{
		set_Value (COLUMNNAME_ZZ_Application_Form_UU, ZZ_Application_Form_UU);
	}

	/** Get ZZ_Application_Form_UU.
		@return ZZ_Application_Form_UU	  */
	public String getZZ_Application_Form_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Application_Form_UU);
	}

	/** Set Discipline Total Learners.
		@param ZZ_DisciplineTotalLearners Discipline Total Learners
	*/
	public void setZZ_DisciplineTotalLearners (int ZZ_DisciplineTotalLearners)
	{
		set_Value (COLUMNNAME_ZZ_DisciplineTotalLearners, Integer.valueOf(ZZ_DisciplineTotalLearners));
	}

	/** Get Discipline Total Learners.
		@return Discipline Total Learners	  */
	public int getZZ_DisciplineTotalLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_DisciplineTotalLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Has Pivotal Plan Submited.
		@param ZZ_HasPivotalPlanSubmited Has the organisation submitted the Pivotal Plan and Report In previous financial year?
	*/
	public void setZZ_HasPivotalPlanSubmited (boolean ZZ_HasPivotalPlanSubmited)
	{
		set_Value (COLUMNNAME_ZZ_HasPivotalPlanSubmited, Boolean.valueOf(ZZ_HasPivotalPlanSubmited));
	}

	/** Get Has Pivotal Plan Submited.
		@return Has the organisation submitted the Pivotal Plan and Report In previous financial year?
	  */
	public boolean isZZ_HasPivotalPlanSubmited()
	{
		Object oo = get_Value(COLUMNNAME_ZZ_HasPivotalPlanSubmited);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Has WSP Submited.
		@param ZZ_HasWSPSubmited Has the organisation submitted the WSP/ATR In previous financial year?
	*/
	public void setZZ_HasWSPSubmited (boolean ZZ_HasWSPSubmited)
	{
		set_Value (COLUMNNAME_ZZ_HasWSPSubmited, Boolean.valueOf(ZZ_HasWSPSubmited));
	}

	/** Get Has WSP Submited.
		@return Has the organisation submitted the WSP/ATR In previous financial year?
	  */
	public boolean isZZ_HasWSPSubmited()
	{
		Object oo = get_Value(COLUMNNAME_ZZ_HasWSPSubmited);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Organisation Registration Number .
		@param ZZ_Org_Reg_No Organisation Registration Number 
	*/
	public void setZZ_Org_Reg_No (String ZZ_Org_Reg_No)
	{
		set_Value (COLUMNNAME_ZZ_Org_Reg_No, ZZ_Org_Reg_No);
	}

	/** Get Organisation Registration Number .
		@return Organisation Registration Number 	  */
	public String getZZ_Org_Reg_No()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Org_Reg_No);
	}

	/** Set SDL Number.
		@param ZZ_SDL_No SDL Number
	*/
	public void setZZ_SDL_No (String ZZ_SDL_No)
	{
		set_Value (COLUMNNAME_ZZ_SDL_No, ZZ_SDL_No);
	}

	/** Get SDL Number.
		@return SDL Number	  */
	public String getZZ_SDL_No()
	{
		return (String)get_Value(COLUMNNAME_ZZ_SDL_No);
	}

	/** Set Side SDL Number.
		@param ZZ_Side_SDL_No Side SDL Number
	*/
	public void setZZ_Side_SDL_No (String ZZ_Side_SDL_No)
	{
		set_Value (COLUMNNAME_ZZ_Side_SDL_No, ZZ_Side_SDL_No);
	}

	/** Get Side SDL Number.
		@return Side SDL Number	  */
	public String getZZ_Side_SDL_No()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Side_SDL_No);
	}

	/** Set Trade Total Learners.
		@param ZZ_TradeTotalLearners Trade Total Learners
	*/
	public void setZZ_TradeTotalLearners (int ZZ_TradeTotalLearners)
	{
		set_Value (COLUMNNAME_ZZ_TradeTotalLearners, Integer.valueOf(ZZ_TradeTotalLearners));
	}

	/** Get Trade Total Learners.
		@return Trade Total Learners	  */
	public int getZZ_TradeTotalLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_TradeTotalLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set VAT.
		@param ZZ_VAT VAT
	*/
	public void setZZ_VAT (String ZZ_VAT)
	{
		set_Value (COLUMNNAME_ZZ_VAT, ZZ_VAT);
	}

	/** Get VAT.
		@return VAT	  */
	public String getZZ_VAT()
	{
		return (String)get_Value(COLUMNNAME_ZZ_VAT);
	}

	/** Set VAT Exemption.
		@param ZZ_VAT_Exemption VAT Exemption
	*/
	public void setZZ_VAT_Exemption (byte[] ZZ_VAT_Exemption)
	{
		set_Value (COLUMNNAME_ZZ_VAT_Exemption, ZZ_VAT_Exemption);
	}

	/** Get VAT Exemption.
		@return VAT Exemption	  */
	public byte[] getZZ_VAT_Exemption()
	{
		return (byte[])get_Value(COLUMNNAME_ZZ_VAT_Exemption);
	}
}