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
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;

/** Generated Model for ZZ_FormDiscipline
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_FormDiscipline")
public class X_ZZ_FormDiscipline extends PO implements I_ZZ_FormDiscipline, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250923L;

    /** Standard Constructor */
    public X_ZZ_FormDiscipline (Properties ctx, int ZZ_FormDiscipline_ID, String trxName)
    {
      super (ctx, ZZ_FormDiscipline_ID, trxName);
      /** if (ZZ_FormDiscipline_ID == 0)
        {
			setZZ_FormDiscipline_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZ_FormDiscipline (Properties ctx, int ZZ_FormDiscipline_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_FormDiscipline_ID, trxName, virtualColumns);
      /** if (ZZ_FormDiscipline_ID == 0)
        {
			setZZ_FormDiscipline_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZ_FormDiscipline (Properties ctx, String ZZ_FormDiscipline_UU, String trxName)
    {
      super (ctx, ZZ_FormDiscipline_UU, trxName);
      /** if (ZZ_FormDiscipline_UU == null)
        {
			setZZ_FormDiscipline_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZ_FormDiscipline (Properties ctx, String ZZ_FormDiscipline_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_FormDiscipline_UU, trxName, virtualColumns);
      /** if (ZZ_FormDiscipline_UU == null)
        {
			setZZ_FormDiscipline_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZ_FormDiscipline (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZ_FormDiscipline[")
        .append(get_ID()).append("]");
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

	/** Set Start Date.
		@param StartDate First effective day (inclusive)
	*/
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate()
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
	}

	/** Set Accred File Name.
		@param ZZAccredFileName Accred File Name
	*/
	public void setZZAccredFileName (String ZZAccredFileName)
	{
		set_Value (COLUMNNAME_ZZAccredFileName, ZZAccredFileName);
	}

	/** Get Accred File Name.
		@return Accred File Name	  */
	public String getZZAccredFileName()
	{
		return (String)get_Value(COLUMNNAME_ZZAccredFileName);
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

	/** Set No Lecture.
		@param ZZNoLecture Number Of Lecture
	*/
	public void setZZNoLecture (int ZZNoLecture)
	{
		set_Value (COLUMNNAME_ZZNoLecture, Integer.valueOf(ZZNoLecture));
	}

	/** Get No Lecture.
		@return Number Of Lecture
	  */
	public int getZZNoLecture()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZNoLecture);
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

	/** Set Accred File.
		@param ZZ_AccredFile Accred File
	*/
	public void setZZ_AccredFile (byte[] ZZ_AccredFile)
	{
		set_Value (COLUMNNAME_ZZ_AccredFile, ZZ_AccredFile);
	}

	/** Get Accred File.
		@return Accred File	  */
	public byte[] getZZ_AccredFile()
	{
		return (byte[])get_Value(COLUMNNAME_ZZ_AccredFile);
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

	/** 4IR Learnership = 4IRLearnership */
	public static final String ZZ_DISCIPLINETYPE_4IRLearnership = "4IRLearnership";
	/** AET Learnership = AETLearnership */
	public static final String ZZ_DISCIPLINETYPE_AETLearnership = "AETLearnership";
	/** Discipline = Discipline */
	public static final String ZZ_DISCIPLINETYPE_Discipline = "Discipline";
	/** General Learnership = GeneralLearnership */
	public static final String ZZ_DISCIPLINETYPE_GeneralLearnership = "GeneralLearnership";
	/** Trade = Trade */
	public static final String ZZ_DISCIPLINETYPE_Trade = "Trade";
	/** UnknowLearnership = UnknowLearnership */
	public static final String ZZ_DISCIPLINETYPE_UnknowLearnership = "UnknowLearnership";
	/** Set Discipline Type.
		@param ZZ_DisciplineType Discipline Type
	*/
	public void setZZ_DisciplineType (String ZZ_DisciplineType)
	{

		set_Value (COLUMNNAME_ZZ_DisciplineType, ZZ_DisciplineType);
	}

	/** Get Discipline Type.
		@return Discipline Type	  */
	public String getZZ_DisciplineType()
	{
		return (String)get_Value(COLUMNNAME_ZZ_DisciplineType);
	}

	public I_ZZ_Disciplines getZZ_Disciplines() throws RuntimeException
	{
		return (I_ZZ_Disciplines)MTable.get(getCtx(), I_ZZ_Disciplines.Table_ID)
			.getPO(getZZ_Disciplines_ID(), get_TrxName());
	}

	/** Set Disciplines.
		@param ZZ_Disciplines_ID Disciplines
	*/
	public void setZZ_Disciplines_ID (int ZZ_Disciplines_ID)
	{
		if (ZZ_Disciplines_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Disciplines_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Disciplines_ID, Integer.valueOf(ZZ_Disciplines_ID));
	}

	/** Get Disciplines.
		@return Disciplines	  */
	public int getZZ_Disciplines_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Disciplines_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Form Discipline.
		@param ZZ_FormDiscipline_ID Form Discipline
	*/
	public void setZZ_FormDiscipline_ID (int ZZ_FormDiscipline_ID)
	{
		if (ZZ_FormDiscipline_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_FormDiscipline_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_FormDiscipline_ID, Integer.valueOf(ZZ_FormDiscipline_ID));
	}

	/** Get Form Discipline.
		@return Form Discipline	  */
	public int getZZ_FormDiscipline_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_FormDiscipline_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZ_FormDiscipline_UU.
		@param ZZ_FormDiscipline_UU ZZ_FormDiscipline_UU
	*/
	public void setZZ_FormDiscipline_UU (String ZZ_FormDiscipline_UU)
	{
		set_Value (COLUMNNAME_ZZ_FormDiscipline_UU, ZZ_FormDiscipline_UU);
	}

	/** Get ZZ_FormDiscipline_UU.
		@return ZZ_FormDiscipline_UU	  */
	public String getZZ_FormDiscipline_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_FormDiscipline_UU);
	}

	public I_ZZ_Learnerships getZZ_Learnerships() throws RuntimeException
	{
		return (I_ZZ_Learnerships)MTable.get(getCtx(), I_ZZ_Learnerships.Table_ID)
			.getPO(getZZ_Learnerships_ID(), get_TrxName());
	}

	/** Set Learnerships.
		@param ZZ_Learnerships_ID Learnerships master
	*/
	public void setZZ_Learnerships_ID (int ZZ_Learnerships_ID)
	{
		if (ZZ_Learnerships_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Learnerships_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Learnerships_ID, Integer.valueOf(ZZ_Learnerships_ID));
	}

	/** Get Learnerships.
		@return Learnerships master
	  */
	public int getZZ_Learnerships_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Learnerships_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_ZZ_Trade getZZ_Trade() throws RuntimeException
	{
		return (I_ZZ_Trade)MTable.get(getCtx(), I_ZZ_Trade.Table_ID)
			.getPO(getZZ_Trade_ID(), get_TrxName());
	}

	/** Set Trade.
		@param ZZ_Trade_ID Trade
	*/
	public void setZZ_Trade_ID (int ZZ_Trade_ID)
	{
		if (ZZ_Trade_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Trade_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Trade_ID, Integer.valueOf(ZZ_Trade_ID));
	}

	/** Get Trade.
		@return Trade	  */
	public int getZZ_Trade_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Trade_ID);
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