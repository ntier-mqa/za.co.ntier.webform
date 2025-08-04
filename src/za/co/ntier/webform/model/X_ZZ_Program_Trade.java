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

/** Generated Model for ZZ_Program_Trade
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_Program_Trade")
public class X_ZZ_Program_Trade extends PO implements I_ZZ_Program_Trade, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250803L;

    /** Standard Constructor */
    public X_ZZ_Program_Trade (Properties ctx, int ZZ_Program_Trade_ID, String trxName)
    {
      super (ctx, ZZ_Program_Trade_ID, trxName);
      /** if (ZZ_Program_Trade_ID == 0)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Is_SPOI (false);
// N
			setZZ_Program_Trade_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Program_Trade (Properties ctx, int ZZ_Program_Trade_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Program_Trade_ID, trxName, virtualColumns);
      /** if (ZZ_Program_Trade_ID == 0)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Is_SPOI (false);
// N
			setZZ_Program_Trade_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Program_Trade (Properties ctx, String ZZ_Program_Trade_UU, String trxName)
    {
      super (ctx, ZZ_Program_Trade_UU, trxName);
      /** if (ZZ_Program_Trade_UU == null)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Is_SPOI (false);
// N
			setZZ_Program_Trade_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Program_Trade (Properties ctx, String ZZ_Program_Trade_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Program_Trade_UU, trxName, virtualColumns);
      /** if (ZZ_Program_Trade_UU == null)
        {
			setZZ_Is_Accred_SLA_Req (false);
// N
			setZZ_Is_SPOI (false);
// N
			setZZ_Program_Trade_ID (0);
			setZZ_WPA_Req (false);
// N
        } */
    }

    /** Load Constructor */
    public X_ZZ_Program_Trade (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZ_Program_Trade[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Set Is SPOI.
		@param ZZ_Is_SPOI Is SPOI
	*/
	public void setZZ_Is_SPOI (boolean ZZ_Is_SPOI)
	{
		set_Value (COLUMNNAME_ZZ_Is_SPOI, Boolean.valueOf(ZZ_Is_SPOI));
	}

	/** Get Is SPOI.
		@return Is SPOI	  */
	public boolean isZZ_Is_SPOI()
	{
		Object oo = get_Value(COLUMNNAME_ZZ_Is_SPOI);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Trade.
		@param ZZ_Program_Trade_ID Trade
	*/
	public void setZZ_Program_Trade_ID (int ZZ_Program_Trade_ID)
	{
		if (ZZ_Program_Trade_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Program_Trade_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Program_Trade_ID, Integer.valueOf(ZZ_Program_Trade_ID));
	}

	/** Get Trade.
		@return Trade	  */
	public int getZZ_Program_Trade_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Program_Trade_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZ_Program_Trade_UU.
		@param ZZ_Program_Trade_UU ZZ_Program_Trade_UU
	*/
	public void setZZ_Program_Trade_UU (String ZZ_Program_Trade_UU)
	{
		set_Value (COLUMNNAME_ZZ_Program_Trade_UU, ZZ_Program_Trade_UU);
	}

	/** Get ZZ_Program_Trade_UU.
		@return ZZ_Program_Trade_UU	  */
	public String getZZ_Program_Trade_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Program_Trade_UU);
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
			set_Value (COLUMNNAME_ZZ_Trade_ID, null);
		else
			set_Value (COLUMNNAME_ZZ_Trade_ID, Integer.valueOf(ZZ_Trade_ID));
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