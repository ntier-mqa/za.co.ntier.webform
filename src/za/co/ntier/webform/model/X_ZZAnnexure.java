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

/** Generated Model for ZZAnnexure
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZAnnexure")
public class X_ZZAnnexure extends PO implements I_ZZAnnexure, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250917L;

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, int ZZAnnexure_ID, String trxName)
    {
      super (ctx, ZZAnnexure_ID, trxName);
      /** if (ZZAnnexure_ID == 0)
        {
			setName (null);
			setZZAnnexure_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, int ZZAnnexure_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZAnnexure_ID, trxName, virtualColumns);
      /** if (ZZAnnexure_ID == 0)
        {
			setName (null);
			setZZAnnexure_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, String ZZAnnexure_UU, String trxName)
    {
      super (ctx, ZZAnnexure_UU, trxName);
      /** if (ZZAnnexure_UU == null)
        {
			setName (null);
			setZZAnnexure_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, String ZZAnnexure_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZAnnexure_UU, trxName, virtualColumns);
      /** if (ZZAnnexure_UU == null)
        {
			setName (null);
			setZZAnnexure_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZAnnexure (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZAnnexure[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Data Type.
		@param DataType Type of data
	*/
	public void setDataType (String DataType)
	{
		set_Value (COLUMNNAME_DataType, DataType);
	}

	/** Get Data Type.
		@return Type of data
	  */
	public String getDataType()
	{
		return (String)get_Value(COLUMNNAME_DataType);
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

	/** Set Number of Months.
		@param NoMonths Number of Months
	*/
	public void setNoMonths (int NoMonths)
	{
		set_Value (COLUMNNAME_NoMonths, Integer.valueOf(NoMonths));
	}

	/** Get Number of Months.
		@return Number of Months	  */
	public int getNoMonths()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NoMonths);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Annexure Definition.
		@param ZZAnnexure_ID Annexure Definition
	*/
	public void setZZAnnexure_ID (int ZZAnnexure_ID)
	{
		if (ZZAnnexure_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZAnnexure_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZAnnexure_ID, Integer.valueOf(ZZAnnexure_ID));
	}

	/** Get Annexure Definition.
		@return Annexure Definition	  */
	public int getZZAnnexure_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZAnnexure_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZAnnexure_UU.
		@param ZZAnnexure_UU ZZAnnexure_UU
	*/
	public void setZZAnnexure_UU (String ZZAnnexure_UU)
	{
		set_Value (COLUMNNAME_ZZAnnexure_UU, ZZAnnexure_UU);
	}

	/** Get ZZAnnexure_UU.
		@return ZZAnnexure_UU	  */
	public String getZZAnnexure_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZAnnexure_UU);
	}

	/** Set Number Of Beneficiaries Applying.
		@param ZZBeneficiaries Number Of Beneficiaries Applying For
	*/
	public void setZZBeneficiaries (int ZZBeneficiaries)
	{
		set_Value (COLUMNNAME_ZZBeneficiaries, Integer.valueOf(ZZBeneficiaries));
	}

	/** Get Number Of Beneficiaries Applying.
		@return Number Of Beneficiaries Applying For
	  */
	public int getZZBeneficiaries()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZBeneficiaries);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Discipline Applying For.
		@param ZZDiscipline Discipline Applying For
	*/
	public void setZZDiscipline (String ZZDiscipline)
	{
		set_Value (COLUMNNAME_ZZDiscipline, ZZDiscipline);
	}

	/** Get Discipline Applying For.
		@return Discipline Applying For
	  */
	public String getZZDiscipline()
	{
		return (String)get_Value(COLUMNNAME_ZZDiscipline);
	}

	/** Set No Learners.
		@param ZZNoLearners Number of learners applying should be based on
	*/
	public void setZZNoLearners (int ZZNoLearners)
	{
		set_Value (COLUMNNAME_ZZNoLearners, Integer.valueOf(ZZNoLearners));
	}

	/** Get No Learners.
		@return Number of learners applying should be based on
	  */
	public int getZZNoLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Programme Applying.
		@param ZZProgramme Programme Applying For
	*/
	public void setZZProgramme (int ZZProgramme)
	{
		set_Value (COLUMNNAME_ZZProgramme, Integer.valueOf(ZZProgramme));
	}

	/** Get Programme Applying.
		@return Programme Applying For
	  */
	public int getZZProgramme()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZProgramme);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Total Number Beneficiaries.
		@param ZZTotalBeneficiaries Total Number of beneficiaries applying for
	*/
	public void setZZTotalBeneficiaries (int ZZTotalBeneficiaries)
	{
		set_Value (COLUMNNAME_ZZTotalBeneficiaries, Integer.valueOf(ZZTotalBeneficiaries));
	}

	/** Get Total Number Beneficiaries.
		@return Total Number of beneficiaries applying for
	  */
	public int getZZTotalBeneficiaries()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZTotalBeneficiaries);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}