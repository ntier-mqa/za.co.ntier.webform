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

/** Generated Model for ZZ_Program_Learnerships
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_Program_Learnerships")
public class X_ZZ_Program_Learnerships extends PO implements I_ZZ_Program_Learnerships, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250816L;

    /** Standard Constructor */
    public X_ZZ_Program_Learnerships (Properties ctx, int ZZ_Program_Learnerships_ID, String trxName)
    {
      super (ctx, ZZ_Program_Learnerships_ID, trxName);
      /** if (ZZ_Program_Learnerships_ID == 0)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Program_Learnerships_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Program_Learnerships (Properties ctx, int ZZ_Program_Learnerships_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Program_Learnerships_ID, trxName, virtualColumns);
      /** if (ZZ_Program_Learnerships_ID == 0)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Program_Learnerships_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Program_Learnerships (Properties ctx, String ZZ_Program_Learnerships_UU, String trxName)
    {
      super (ctx, ZZ_Program_Learnerships_UU, trxName);
      /** if (ZZ_Program_Learnerships_UU == null)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Program_Learnerships_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Program_Learnerships (Properties ctx, String ZZ_Program_Learnerships_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Program_Learnerships_UU, trxName, virtualColumns);
      /** if (ZZ_Program_Learnerships_UU == null)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Program_Learnerships_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Load Constructor */
    public X_ZZ_Program_Learnerships (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client
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
      StringBuilder sb = new StringBuilder ("X_ZZ_Program_Learnerships[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Line No.
		@param Line Unique line for this document
	*/
	public void setLine (int Line)
	{
		set_ValueNoCheck (COLUMNNAME_Line, Integer.valueOf(Line));
	}

	/** Get Line No.
		@return Unique line for this document
	  */
	public int getLine()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Line);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Accred/SLA Req.
		@param ZZ_Is_Accred_SLA_Req Accred/SLA Req
	*/
	public void setZZ_Is_Accred_SLA_Req (boolean ZZ_Is_Accred_SLA_Req)
	{
		set_Value (COLUMNNAME_ZZ_Is_Accred_SLA_Req, Boolean.valueOf(ZZ_Is_Accred_SLA_Req));
	}

	/** Get Accred/SLA Req.
		@return Accred/SLA Req	  */
	public boolean isZZ_Is_Accred_SLA_Req()
	{
		Object oo = get_Value(COLUMNNAME_ZZ_Is_Accred_SLA_Req);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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
			set_Value (COLUMNNAME_ZZ_Learnerships_ID, null);
		else
			set_Value (COLUMNNAME_ZZ_Learnerships_ID, Integer.valueOf(ZZ_Learnerships_ID));
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

	/** 4IR = 4 */
	public static final String ZZ_LEARNERSHIPS_TYPE_4IR = "4";
	/** AET = A */
	public static final String ZZ_LEARNERSHIPS_TYPE_AET = "A";
	/** General = G */
	public static final String ZZ_LEARNERSHIPS_TYPE_General = "G";
	/** Set Learnerships Type.
		@param ZZ_Learnerships_Type Learnerships Type
	*/
	public void setZZ_Learnerships_Type (String ZZ_Learnerships_Type)
	{

		set_Value (COLUMNNAME_ZZ_Learnerships_Type, ZZ_Learnerships_Type);
	}

	/** Get Learnerships Type.
		@return Learnerships Type	  */
	public String getZZ_Learnerships_Type()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Learnerships_Type);
	}

	/** Level 1 = 01 */
	public static final String ZZ_NQF_LEVEL_Level1 = "01";
	/** Level 2 = 02 */
	public static final String ZZ_NQF_LEVEL_Level2 = "02";
	/** Level 3 = 03 */
	public static final String ZZ_NQF_LEVEL_Level3 = "03";
	/** Level 4 = 04 */
	public static final String ZZ_NQF_LEVEL_Level4 = "04";
	/** Level 5 = 05 */
	public static final String ZZ_NQF_LEVEL_Level5 = "05";
	/** Level 6 = 06 */
	public static final String ZZ_NQF_LEVEL_Level6 = "06";
	/** Level 7 = 07 */
	public static final String ZZ_NQF_LEVEL_Level7 = "07";
	/** Level 8 = 08 */
	public static final String ZZ_NQF_LEVEL_Level8 = "08";
	/** Level 9 = 09 */
	public static final String ZZ_NQF_LEVEL_Level9 = "09";
	/** Level 10 = 10 */
	public static final String ZZ_NQF_LEVEL_Level10 = "10";
	/** Level1-3 = L3 */
	public static final String ZZ_NQF_LEVEL_Level1_3 = "L3";
	/** N1 - N3 = NN */
	public static final String ZZ_NQF_LEVEL_N1_N3 = "NN";
	/** Pre Basic = PB */
	public static final String ZZ_NQF_LEVEL_PreBasic = "PB";
	/** Set NQF Level.
		@param ZZ_NQF_Level NQF Level
	*/
	public void setZZ_NQF_Level (String ZZ_NQF_Level)
	{

		set_Value (COLUMNNAME_ZZ_NQF_Level, ZZ_NQF_Level);
	}

	/** Get NQF Level.
		@return NQF Level	  */
	public String getZZ_NQF_Level()
	{
		return (String)get_Value(COLUMNNAME_ZZ_NQF_Level);
	}

	/** Set Program Learnerships.
		@param ZZ_Program_Learnerships_ID Program Learnerships
	*/
	public void setZZ_Program_Learnerships_ID (int ZZ_Program_Learnerships_ID)
	{
		if (ZZ_Program_Learnerships_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Program_Learnerships_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Program_Learnerships_ID, Integer.valueOf(ZZ_Program_Learnerships_ID));
	}

	/** Get Program Learnerships.
		@return Program Learnerships	  */
	public int getZZ_Program_Learnerships_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Program_Learnerships_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZ_Program_Learnerships_UU.
		@param ZZ_Program_Learnerships_UU ZZ_Program_Learnerships_UU
	*/
	public void setZZ_Program_Learnerships_UU (String ZZ_Program_Learnerships_UU)
	{
		set_Value (COLUMNNAME_ZZ_Program_Learnerships_UU, ZZ_Program_Learnerships_UU);
	}

	/** Get ZZ_Program_Learnerships_UU.
		@return ZZ_Program_Learnerships_UU	  */
	public String getZZ_Program_Learnerships_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Program_Learnerships_UU);
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

	/** Set WPA Req.
		@param ZZ_WPA_Req WPA Req
	*/
	public void setZZ_WPA_Req (boolean ZZ_WPA_Req)
	{
		set_Value (COLUMNNAME_ZZ_WPA_Req, Boolean.valueOf(ZZ_WPA_Req));
	}

	/** Get WPA Req.
		@return WPA Req	  */
	public boolean isZZ_WPA_Req()
	{
		Object oo = get_Value(COLUMNNAME_ZZ_WPA_Req);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
	}
}