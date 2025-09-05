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

import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;

/** Generated Model for ZZ_FormContact
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_FormContact")
public class X_ZZ_FormContact extends PO implements I_ZZ_FormContact, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250903L;

    /** Standard Constructor */
    public X_ZZ_FormContact (Properties ctx, int ZZ_FormContact_ID, String trxName)
    {
      super (ctx, ZZ_FormContact_ID, trxName);
      /** if (ZZ_FormContact_ID == 0)
        {
        } */
    }

    /** Standard Constructor */
    public X_ZZ_FormContact (Properties ctx, int ZZ_FormContact_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_FormContact_ID, trxName, virtualColumns);
      /** if (ZZ_FormContact_ID == 0)
        {
        } */
    }

    /** Standard Constructor */
    public X_ZZ_FormContact (Properties ctx, String ZZ_FormContact_UU, String trxName)
    {
      super (ctx, ZZ_FormContact_UU, trxName);
      /** if (ZZ_FormContact_UU == null)
        {
        } */
    }

    /** Standard Constructor */
    public X_ZZ_FormContact (Properties ctx, String ZZ_FormContact_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_FormContact_UU, trxName, virtualColumns);
      /** if (ZZ_FormContact_UU == null)
        {
        } */
    }

    /** Load Constructor */
    public X_ZZ_FormContact (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZ_FormContact[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Address.
		@param Address Address
	*/
	public void setAddress (String Address)
	{
		set_ValueNoCheck (COLUMNNAME_Address, Address);
	}

	/** Get Address.
		@return Address	  */
	public String getAddress()
	{
		return (String)get_Value(COLUMNNAME_Address);
	}

	public org.compiere.model.I_C_City getC_City() throws RuntimeException
	{
		return (org.compiere.model.I_C_City)MTable.get(getCtx(), org.compiere.model.I_C_City.Table_ID)
			.getPO(getC_City_ID(), get_TrxName());
	}

	/** Set City.
		@param C_City_ID City
	*/
	public void setC_City_ID (int C_City_ID)
	{
		if (C_City_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_City_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_City_ID, Integer.valueOf(C_City_ID));
	}

	/** Get City.
		@return City
	  */
	public int getC_City_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_City_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Region getC_Region() throws RuntimeException
	{
		return (org.compiere.model.I_C_Region)MTable.get(getCtx(), org.compiere.model.I_C_Region.Table_ID)
			.getPO(getC_Region_ID(), get_TrxName());
	}

	/** Set Region.
		@param C_Region_ID Identifies a geographical Region
	*/
	public void setC_Region_ID (int C_Region_ID)
	{
		if (C_Region_ID < 1)
			set_ValueNoCheck (COLUMNNAME_C_Region_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_C_Region_ID, Integer.valueOf(C_Region_ID));
	}

	/** Get Region.
		@return Identifies a geographical Region
	  */
	public int getC_Region_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Region_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Contact Name.
		@param ContactName Business Partner Contact Name
	*/
	public void setContactName (String ContactName)
	{
		set_ValueNoCheck (COLUMNNAME_ContactName, ContactName);
	}

	/** Get Contact Name.
		@return Business Partner Contact Name
	  */
	public String getContactName()
	{
		return (String)get_Value(COLUMNNAME_ContactName);
	}

	/** Set EMail Address.
		@param EMail Electronic Mail Address
	*/
	public void setEMail (String EMail)
	{
		set_Value (COLUMNNAME_EMail, EMail);
	}

	/** Get EMail Address.
		@return Electronic Mail Address
	  */
	public String getEMail()
	{
		return (String)get_Value(COLUMNNAME_EMail);
	}

	/** Set Phone.
		@param Phone Identifies a telephone number
	*/
	public void setPhone (String Phone)
	{
		set_ValueNoCheck (COLUMNNAME_Phone, Phone);
	}

	/** Get Phone.
		@return Identifies a telephone number
	  */
	public String getPhone()
	{
		return (String)get_Value(COLUMNNAME_Phone);
	}

	/** Set 2nd Phone.
		@param Phone2 Identifies an alternate telephone number.
	*/
	public void setPhone2 (String Phone2)
	{
		set_Value (COLUMNNAME_Phone2, Phone2);
	}

	/** Get 2nd Phone.
		@return Identifies an alternate telephone number.
	  */
	public String getPhone2()
	{
		return (String)get_Value(COLUMNNAME_Phone2);
	}

	/** Set ZIP.
		@param Postal Postal code
	*/
	public void setPostal (String Postal)
	{
		set_Value (COLUMNNAME_Postal, Postal);
	}

	/** Get ZIP.
		@return Postal code
	  */
	public String getPostal()
	{
		return (String)get_Value(COLUMNNAME_Postal);
	}

	public I_ZZ_Application_Form getZZ_Application_Form() throws RuntimeException
	{
		return (I_ZZ_Application_Form)MTable.get(getCtx(), I_ZZ_Application_Form.Table_ID)
			.getPO(getZZ_Application_Form_ID(), get_TrxName());
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

	/** Alternate Responsible Candidacy = AlternateResponsibleCandidacy */
	public static final String ZZ_CONTACTTYPE_AlternateResponsibleCandidacy = "AlternateResponsibleCandidacy";
	/** Alternate Responsible Internship = AlternateResponsibleInternship */
	public static final String ZZ_CONTACTTYPE_AlternateResponsibleInternship = "AlternateResponsibleInternship";
	/** Main = Main */
	public static final String ZZ_CONTACTTYPE_Main = "Main";
	/** Main Alter = Main Alter */
	public static final String ZZ_CONTACTTYPE_MainAlter = "Main Alter";
	/** Org = Org */
	public static final String ZZ_CONTACTTYPE_Org = "Org";
	/** Org Alter = Org Alter */
	public static final String ZZ_CONTACTTYPE_OrgAlter = "Org Alter";
	/** Physical = Physical */
	public static final String ZZ_CONTACTTYPE_Physical = "Physical";
	/** Postal = Postal */
	public static final String ZZ_CONTACTTYPE_Postal = "Postal";
	/** Vacation = Vacation */
	public static final String ZZ_CONTACTTYPE_Vacation = "Vacation";
	/** Set Contact Type.
		@param ZZ_ContactType Contact Type
	*/
	public void setZZ_ContactType (String ZZ_ContactType)
	{

		set_Value (COLUMNNAME_ZZ_ContactType, ZZ_ContactType);
	}

	/** Get Contact Type.
		@return Contact Type	  */
	public String getZZ_ContactType()
	{
		return (String)get_Value(COLUMNNAME_ZZ_ContactType);
	}

	/** Set Designation.
		@param ZZ_Designation Designation
	*/
	public void setZZ_Designation (String ZZ_Designation)
	{
		set_Value (COLUMNNAME_ZZ_Designation, ZZ_Designation);
	}

	/** Get Designation.
		@return Designation	  */
	public String getZZ_Designation()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Designation);
	}

	/** Set Form Contact.
		@param ZZ_FormContact_ID Form Contact
	*/
	public void setZZ_FormContact_ID (int ZZ_FormContact_ID)
	{
		if (ZZ_FormContact_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_FormContact_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_FormContact_ID, Integer.valueOf(ZZ_FormContact_ID));
	}

	/** Get Form Contact.
		@return Form Contact	  */
	public int getZZ_FormContact_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_FormContact_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZ_FormContact_UU.
		@param ZZ_FormContact_UU ZZ_FormContact_UU
	*/
	public void setZZ_FormContact_UU (String ZZ_FormContact_UU)
	{
		set_Value (COLUMNNAME_ZZ_FormContact_UU, ZZ_FormContact_UU);
	}

	/** Get ZZ_FormContact_UU.
		@return ZZ_FormContact_UU	  */
	public String getZZ_FormContact_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_FormContact_UU);
	}

	/** Set Side Name.
		@param ZZ_SideName Side Name
	*/
	public void setZZ_SideName (String ZZ_SideName)
	{
		set_Value (COLUMNNAME_ZZ_SideName, ZZ_SideName);
	}

	/** Get Side Name.
		@return Side Name	  */
	public String getZZ_SideName()
	{
		return (String)get_Value(COLUMNNAME_ZZ_SideName);
	}
}