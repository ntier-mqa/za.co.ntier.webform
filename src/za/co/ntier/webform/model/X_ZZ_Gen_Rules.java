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
import org.compiere.model.PO;
import org.compiere.model.POInfo;

/** Generated Model for ZZ_Gen_Rules
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_Gen_Rules")
public class X_ZZ_Gen_Rules extends PO implements I_ZZ_Gen_Rules, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250903L;

    /** Standard Constructor */
    public X_ZZ_Gen_Rules (Properties ctx, int ZZ_Gen_Rules_ID, String trxName)
    {
      super (ctx, ZZ_Gen_Rules_ID, trxName);
      /** if (ZZ_Gen_Rules_ID == 0)
        {
			setName (null);
			setZZ_Gen_Rules_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Gen_Rules (Properties ctx, int ZZ_Gen_Rules_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Gen_Rules_ID, trxName, virtualColumns);
      /** if (ZZ_Gen_Rules_ID == 0)
        {
			setName (null);
			setZZ_Gen_Rules_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Gen_Rules (Properties ctx, String ZZ_Gen_Rules_UU, String trxName)
    {
      super (ctx, ZZ_Gen_Rules_UU, trxName);
      /** if (ZZ_Gen_Rules_UU == null)
        {
			setName (null);
			setZZ_Gen_Rules_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Gen_Rules (Properties ctx, String ZZ_Gen_Rules_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Gen_Rules_UU, trxName, virtualColumns);
      /** if (ZZ_Gen_Rules_UU == null)
        {
			setName (null);
			setZZ_Gen_Rules_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZ_Gen_Rules (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZ_Gen_Rules[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
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

	/** Set General Rules.
		@param ZZ_Gen_Rules_ID General Rules
	*/
	public void setZZ_Gen_Rules_ID (int ZZ_Gen_Rules_ID)
	{
		if (ZZ_Gen_Rules_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZ_Gen_Rules_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZ_Gen_Rules_ID, Integer.valueOf(ZZ_Gen_Rules_ID));
	}

	/** Get General Rules.
		@return General Rules	  */
	public int getZZ_Gen_Rules_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZ_Gen_Rules_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZ_Gen_Rules_UU.
		@param ZZ_Gen_Rules_UU ZZ_Gen_Rules_UU
	*/
	public void setZZ_Gen_Rules_UU (String ZZ_Gen_Rules_UU)
	{
		set_Value (COLUMNNAME_ZZ_Gen_Rules_UU, ZZ_Gen_Rules_UU);
	}

	/** Get ZZ_Gen_Rules_UU.
		@return ZZ_Gen_Rules_UU	  */
	public String getZZ_Gen_Rules_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Gen_Rules_UU);
	}
}