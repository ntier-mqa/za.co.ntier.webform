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

/** Generated Model for ZZLearnersApplied
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZLearnersApplied")
public class X_ZZLearnersApplied extends PO implements I_ZZLearnersApplied, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250923L;

    /** Standard Constructor */
    public X_ZZLearnersApplied (Properties ctx, int ZZLearnersApplied_ID, String trxName)
    {
      super (ctx, ZZLearnersApplied_ID, trxName);
      /** if (ZZLearnersApplied_ID == 0)
        {
			setZZLearnersApplied_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZLearnersApplied (Properties ctx, int ZZLearnersApplied_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZLearnersApplied_ID, trxName, virtualColumns);
      /** if (ZZLearnersApplied_ID == 0)
        {
			setZZLearnersApplied_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZLearnersApplied (Properties ctx, String ZZLearnersApplied_UU, String trxName)
    {
      super (ctx, ZZLearnersApplied_UU, trxName);
      /** if (ZZLearnersApplied_UU == null)
        {
			setZZLearnersApplied_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZLearnersApplied (Properties ctx, String ZZLearnersApplied_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZLearnersApplied_UU, trxName, virtualColumns);
      /** if (ZZLearnersApplied_UU == null)
        {
			setZZLearnersApplied_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZLearnersApplied (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZLearnersApplied[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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

	/** Set Name 2.
		@param Name2 Additional Name
	*/
	public void setName2 (String Name2)
	{
		set_ValueNoCheck (COLUMNNAME_Name2, Name2);
	}

	/** Get Name 2.
		@return Additional Name
	  */
	public String getName2()
	{
		return (String)get_Value(COLUMNNAME_Name2);
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

	/** Set Learners Applied.
		@param ZZLearnersApplied_ID For input &quot;No of Learners applied for&quot;, &quot;No. of Unemployed Learners&quot;,  &quot;Total Number of Learners Applied For&quot;, &quot;No. of employed Learners&quot;
	*/
	public void setZZLearnersApplied_ID (int ZZLearnersApplied_ID)
	{
		if (ZZLearnersApplied_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZLearnersApplied_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZLearnersApplied_ID, Integer.valueOf(ZZLearnersApplied_ID));
	}

	/** Get Learners Applied.
		@return For input &quot;No of Learners applied for&quot;, &quot;No. of Unemployed Learners&quot;,  &quot;Total Number of Learners Applied For&quot;, &quot;No. of employed Learners&quot;
	  */
	public int getZZLearnersApplied_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZLearnersApplied_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZLearnersApplied_UU.
		@param ZZLearnersApplied_UU ZZLearnersApplied_UU
	*/
	public void setZZLearnersApplied_UU (String ZZLearnersApplied_UU)
	{
		set_Value (COLUMNNAME_ZZLearnersApplied_UU, ZZLearnersApplied_UU);
	}

	/** Get ZZLearnersApplied_UU.
		@return ZZLearnersApplied_UU	  */
	public String getZZLearnersApplied_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZLearnersApplied_UU);
	}

	/** Set No Employed Learners.
		@param ZZNoEmployedLearners No Employed Learners
	*/
	public void setZZNoEmployedLearners (int ZZNoEmployedLearners)
	{
		set_Value (COLUMNNAME_ZZNoEmployedLearners, Integer.valueOf(ZZNoEmployedLearners));
	}

	/** Get No Employed Learners.
		@return No Employed Learners	  */
	public int getZZNoEmployedLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoEmployedLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set No Of Lectures Absorbed.
		@param ZZNoLecturesAbsorbed No Of Lectures Absorbed
	*/
	public void setZZNoLecturesAbsorbed (int ZZNoLecturesAbsorbed)
	{
		set_Value (COLUMNNAME_ZZNoLecturesAbsorbed, Integer.valueOf(ZZNoLecturesAbsorbed));
	}

	/** Get No Of Lectures Absorbed.
		@return No Of Lectures Absorbed	  */
	public int getZZNoLecturesAbsorbed()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLecturesAbsorbed);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set No Of Lectures Allocated.
		@param ZZNoLecturesAllocated No Of Lectures Allocated
	*/
	public void setZZNoLecturesAllocated (int ZZNoLecturesAllocated)
	{
		set_Value (COLUMNNAME_ZZNoLecturesAllocated, Integer.valueOf(ZZNoLecturesAllocated));
	}

	/** Get No Of Lectures Allocated.
		@return No Of Lectures Allocated	  */
	public int getZZNoLecturesAllocated()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLecturesAllocated);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set No Of Lectures Completed.
		@param ZZNoLecturesCompleted Number Of Lectures Completed The Programme
	*/
	public void setZZNoLecturesCompleted (int ZZNoLecturesCompleted)
	{
		set_Value (COLUMNNAME_ZZNoLecturesCompleted, Integer.valueOf(ZZNoLecturesCompleted));
	}

	/** Get No Of Lectures Completed.
		@return Number Of Lectures Completed The Programme
	  */
	public int getZZNoLecturesCompleted()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLecturesCompleted);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set No Of Lectures Continuing.
		@param ZZNoLecturesContinuing Number Of Lectures Continuing The Programme
	*/
	public void setZZNoLecturesContinuing (int ZZNoLecturesContinuing)
	{
		set_Value (COLUMNNAME_ZZNoLecturesContinuing, Integer.valueOf(ZZNoLecturesContinuing));
	}

	/** Get No Of Lectures Continuing.
		@return Number Of Lectures Continuing The Programme
	  */
	public int getZZNoLecturesContinuing()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLecturesContinuing);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set No Of Lectures Resigned.
		@param ZZNoLecturesResigned No Of Lectures Resigned
	*/
	public void setZZNoLecturesResigned (int ZZNoLecturesResigned)
	{
		set_Value (COLUMNNAME_ZZNoLecturesResigned, Integer.valueOf(ZZNoLecturesResigned));
	}

	/** Get No Of Lectures Resigned.
		@return No Of Lectures Resigned	  */
	public int getZZNoLecturesResigned()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLecturesResigned);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Total No Learners Applied For.
		@param ZZNoTotalLearners Total No Learners Applied For
	*/
	public void setZZNoTotalLearners (int ZZNoTotalLearners)
	{
		set_Value (COLUMNNAME_ZZNoTotalLearners, Integer.valueOf(ZZNoTotalLearners));
	}

	/** Get Total No Learners Applied For.
		@return Total No Learners Applied For	  */
	public int getZZNoTotalLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoTotalLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set No UnEmployed Learners.
		@param ZZNoUnEmployedLearners No UnEmployed Learners
	*/
	public void setZZNoUnEmployedLearners (int ZZNoUnEmployedLearners)
	{
		set_Value (COLUMNNAME_ZZNoUnEmployedLearners, Integer.valueOf(ZZNoUnEmployedLearners));
	}

	/** Get No UnEmployed Learners.
		@return No UnEmployed Learners	  */
	public int getZZNoUnEmployedLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoUnEmployedLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set WPA File Name.
		@param ZZWPAFileName WPA File Name
	*/
	public void setZZWPAFileName (String ZZWPAFileName)
	{
		set_Value (COLUMNNAME_ZZWPAFileName, ZZWPAFileName);
	}

	/** Get WPA File Name.
		@return WPA File Name	  */
	public String getZZWPAFileName()
	{
		return (String)get_Value(COLUMNNAME_ZZWPAFileName);
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

	/** Set WPA File.
		@param ZZ_WPAFile WPA File
	*/
	public void setZZ_WPAFile (byte[] ZZ_WPAFile)
	{
		set_Value (COLUMNNAME_ZZ_WPAFile, ZZ_WPAFile);
	}

	/** Get WPA File.
		@return WPA File	  */
	public byte[] getZZ_WPAFile()
	{
		return (byte[])get_Value(COLUMNNAME_ZZ_WPAFile);
	}
}