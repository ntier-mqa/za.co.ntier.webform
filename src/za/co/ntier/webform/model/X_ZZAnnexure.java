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
	private static final long serialVersionUID = 20250820L;

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, int ZZAnnexure_ID, String trxName)
    {
      super (ctx, ZZAnnexure_ID, trxName);
      /** if (ZZAnnexure_ID == 0)
        {
			setZZAnnexure_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, int ZZAnnexure_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZAnnexure_ID, trxName, virtualColumns);
      /** if (ZZAnnexure_ID == 0)
        {
			setZZAnnexure_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, String ZZAnnexure_UU, String trxName)
    {
      super (ctx, ZZAnnexure_UU, trxName);
      /** if (ZZAnnexure_UU == null)
        {
			setZZAnnexure_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZAnnexure (Properties ctx, String ZZAnnexure_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZAnnexure_UU, trxName, virtualColumns);
      /** if (ZZAnnexure_UU == null)
        {
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
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Line.
		@param LineNo Line No
	*/
	public void setLineNo (int LineNo)
	{
		set_Value (COLUMNNAME_LineNo, Integer.valueOf(LineNo));
	}

	/** Get Line.
		@return Line No
	  */
	public int getLineNo()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_LineNo);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Title.
		@param Title Name this entity is referred to as
	*/
	public void setTitle (String Title)
	{
		set_Value (COLUMNNAME_Title, Title);
	}

	/** Get Title.
		@return Name this entity is referred to as
	  */
	public String getTitle()
	{
		return (String)get_Value(COLUMNNAME_Title);
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

	/** Number Of Beneficiaries Applying For = Beneficiaries */
	public static final String ZZFIRST_NumberOfBeneficiariesApplyingFor = "Beneficiaries";
	/** Discipline Applying For = Discipline */
	public static final String ZZFIRST_DisciplineApplyingFor = "Discipline";
	/** Programme Applying For = Programme */
	public static final String ZZFIRST_ProgrammeApplyingFor = "Programme";
	/** Total Number Of Beneficiaries Applying For = Total Number Beneficiaries */
	public static final String ZZFIRST_TotalNumberOfBeneficiariesApplyingFor = "Total Number Beneficiaries";
	/** Set First Column.
		@param ZZFirst First Column
	*/
	public void setZZFirst (String ZZFirst)
	{

		set_Value (COLUMNNAME_ZZFirst, ZZFirst);
	}

	/** Get First Column.
		@return First Column	  */
	public String getZZFirst()
	{
		return (String)get_Value(COLUMNNAME_ZZFirst);
	}

	/** Number of learners = Learners */
	public static final String ZZFIRSTSUBCOLUMN_NumberOfLearners = "Learners";
	/** Number of learners applied for = Learners Applied */
	public static final String ZZFIRSTSUBCOLUMN_NumberOfLearnersAppliedFor = "Learners Applied";
	/** Number of managers = Managers */
	public static final String ZZFIRSTSUBCOLUMN_NumberOfManagers = "Managers";
	/** Requested Programme = Programme */
	public static final String ZZFIRSTSUBCOLUMN_RequestedProgramme = "Programme";
	/** Field of Study = Study */
	public static final String ZZFIRSTSUBCOLUMN_FieldOfStudy = "Study";
	/** Set First Subcolumn.
		@param ZZFirstSubcolumn First Subcolumn
	*/
	public void setZZFirstSubcolumn (String ZZFirstSubcolumn)
	{

		set_Value (COLUMNNAME_ZZFirstSubcolumn, ZZFirstSubcolumn);
	}

	/** Get First Subcolumn.
		@return First Subcolumn	  */
	public String getZZFirstSubcolumn()
	{
		return (String)get_Value(COLUMNNAME_ZZFirstSubcolumn);
	}

	/** Number Of Beneficiaries Applying For = Beneficiaries */
	public static final String ZZFOURTH_NumberOfBeneficiariesApplyingFor = "Beneficiaries";
	/** Discipline Applying For = Discipline */
	public static final String ZZFOURTH_DisciplineApplyingFor = "Discipline";
	/** Programme Applying For = Programme */
	public static final String ZZFOURTH_ProgrammeApplyingFor = "Programme";
	/** Total Number Of Beneficiaries Applying For = Total Number Beneficiaries */
	public static final String ZZFOURTH_TotalNumberOfBeneficiariesApplyingFor = "Total Number Beneficiaries";
	/** Set Fourth Column.
		@param ZZFourth Fourth Column
	*/
	public void setZZFourth (String ZZFourth)
	{

		set_Value (COLUMNNAME_ZZFourth, ZZFourth);
	}

	/** Get Fourth Column.
		@return Fourth Column	  */
	public String getZZFourth()
	{
		return (String)get_Value(COLUMNNAME_ZZFourth);
	}

	/** Number of learners = Learners */
	public static final String ZZFOURTHSUBCOLUMN_NumberOfLearners = "Learners";
	/** Number of learners applied for = Learners Applied */
	public static final String ZZFOURTHSUBCOLUMN_NumberOfLearnersAppliedFor = "Learners Applied";
	/** Number of managers = Managers */
	public static final String ZZFOURTHSUBCOLUMN_NumberOfManagers = "Managers";
	/** Requested Programme = Programme */
	public static final String ZZFOURTHSUBCOLUMN_RequestedProgramme = "Programme";
	/** Field of Study = Study */
	public static final String ZZFOURTHSUBCOLUMN_FieldOfStudy = "Study";
	/** Set Fourth Subcolumn.
		@param ZZFourthSubcolumn Fourth Subcolumn
	*/
	public void setZZFourthSubcolumn (String ZZFourthSubcolumn)
	{

		set_Value (COLUMNNAME_ZZFourthSubcolumn, ZZFourthSubcolumn);
	}

	/** Get Fourth Subcolumn.
		@return Fourth Subcolumn	  */
	public String getZZFourthSubcolumn()
	{
		return (String)get_Value(COLUMNNAME_ZZFourthSubcolumn);
	}

	/** Set Header.
		@param ZZHeader Header Annexure
	*/
	public void setZZHeader (String ZZHeader)
	{
		set_Value (COLUMNNAME_ZZHeader, ZZHeader);
	}

	/** Get Header.
		@return Header Annexure
	  */
	public String getZZHeader()
	{
		return (String)get_Value(COLUMNNAME_ZZHeader);
	}

	/** Number Of Beneficiaries Applying For = Beneficiaries */
	public static final String ZZSECOND_NumberOfBeneficiariesApplyingFor = "Beneficiaries";
	/** Discipline Applying For = Discipline */
	public static final String ZZSECOND_DisciplineApplyingFor = "Discipline";
	/** Programme Applying For = Programme */
	public static final String ZZSECOND_ProgrammeApplyingFor = "Programme";
	/** Total Number Of Beneficiaries Applying For = Total Number Beneficiaries */
	public static final String ZZSECOND_TotalNumberOfBeneficiariesApplyingFor = "Total Number Beneficiaries";
	/** Set Second Column.
		@param ZZSecond Second Column
	*/
	public void setZZSecond (String ZZSecond)
	{

		set_Value (COLUMNNAME_ZZSecond, ZZSecond);
	}

	/** Get Second Column.
		@return Second Column	  */
	public String getZZSecond()
	{
		return (String)get_Value(COLUMNNAME_ZZSecond);
	}

	/** Number of learners = Learners */
	public static final String ZZSECONDSUBCOLUMN_NumberOfLearners = "Learners";
	/** Number of learners applied for = Learners Applied */
	public static final String ZZSECONDSUBCOLUMN_NumberOfLearnersAppliedFor = "Learners Applied";
	/** Number of managers = Managers */
	public static final String ZZSECONDSUBCOLUMN_NumberOfManagers = "Managers";
	/** Requested Programme = Programme */
	public static final String ZZSECONDSUBCOLUMN_RequestedProgramme = "Programme";
	/** Field of Study = Study */
	public static final String ZZSECONDSUBCOLUMN_FieldOfStudy = "Study";
	/** Set Second Subcolumn.
		@param ZZSecondSubcolumn Second Subcolumn
	*/
	public void setZZSecondSubcolumn (String ZZSecondSubcolumn)
	{

		set_Value (COLUMNNAME_ZZSecondSubcolumn, ZZSecondSubcolumn);
	}

	/** Get Second Subcolumn.
		@return Second Subcolumn	  */
	public String getZZSecondSubcolumn()
	{
		return (String)get_Value(COLUMNNAME_ZZSecondSubcolumn);
	}

	/** Number Of Beneficiaries Applying For = Beneficiaries */
	public static final String ZZTHIRD_NumberOfBeneficiariesApplyingFor = "Beneficiaries";
	/** Discipline Applying For = Discipline */
	public static final String ZZTHIRD_DisciplineApplyingFor = "Discipline";
	/** Programme Applying For = Programme */
	public static final String ZZTHIRD_ProgrammeApplyingFor = "Programme";
	/** Total Number Of Beneficiaries Applying For = Total Number Beneficiaries */
	public static final String ZZTHIRD_TotalNumberOfBeneficiariesApplyingFor = "Total Number Beneficiaries";
	/** Set Third Column.
		@param ZZThird Third Column
	*/
	public void setZZThird (String ZZThird)
	{

		set_Value (COLUMNNAME_ZZThird, ZZThird);
	}

	/** Get Third Column.
		@return Third Column	  */
	public String getZZThird()
	{
		return (String)get_Value(COLUMNNAME_ZZThird);
	}

	/** Number of learners = Learners */
	public static final String ZZTHIRDSUBCOLUMN_NumberOfLearners = "Learners";
	/** Number of learners applied for = Learners Applied */
	public static final String ZZTHIRDSUBCOLUMN_NumberOfLearnersAppliedFor = "Learners Applied";
	/** Number of managers = Managers */
	public static final String ZZTHIRDSUBCOLUMN_NumberOfManagers = "Managers";
	/** Requested Programme = Programme */
	public static final String ZZTHIRDSUBCOLUMN_RequestedProgramme = "Programme";
	/** Field of Study = Study */
	public static final String ZZTHIRDSUBCOLUMN_FieldOfStudy = "Study";
	/** Set Third Subcolumn.
		@param ZZThirdSubcolumn Third Subcolumn
	*/
	public void setZZThirdSubcolumn (String ZZThirdSubcolumn)
	{

		set_Value (COLUMNNAME_ZZThirdSubcolumn, ZZThirdSubcolumn);
	}

	/** Get Third Subcolumn.
		@return Third Subcolumn	  */
	public String getZZThirdSubcolumn()
	{
		return (String)get_Value(COLUMNNAME_ZZThirdSubcolumn);
	}

	/** Set Program Master Data.
		@param ZZ_Program_Master_Data_ID Program Master Data
	*/
	public void setZZ_Program_Master_Data_ID (int ZZ_Program_Master_Data_ID)
	{
		if (ZZ_Program_Master_Data_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Program_Master_Data_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Program_Master_Data_ID, Integer.valueOf(ZZ_Program_Master_Data_ID));
	}

	/** Get Program Master Data.
		@return Program Master Data	  */
	public int getZZ_Program_Master_Data_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Program_Master_Data_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}