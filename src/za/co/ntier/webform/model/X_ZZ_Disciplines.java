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

/** Generated Model for ZZ_Disciplines
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZ_Disciplines")
public class X_ZZ_Disciplines extends PO implements I_ZZ_Disciplines, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250803L;

    /** Standard Constructor */
    public X_ZZ_Disciplines (Properties ctx, int ZZ_Disciplines_ID, String trxName)
    {
      super (ctx, ZZ_Disciplines_ID, trxName);
      /** if (ZZ_Disciplines_ID == 0)
        {
			setName (null);
			setZZ_Disciplines_ID (0);
			setZZ_Is_SPOI (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Disciplines (Properties ctx, int ZZ_Disciplines_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Disciplines_ID, trxName, virtualColumns);
      /** if (ZZ_Disciplines_ID == 0)
        {
			setName (null);
			setZZ_Disciplines_ID (0);
			setZZ_Is_SPOI (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Disciplines (Properties ctx, String ZZ_Disciplines_UU, String trxName)
    {
      super (ctx, ZZ_Disciplines_UU, trxName);
      /** if (ZZ_Disciplines_UU == null)
        {
			setName (null);
			setZZ_Disciplines_ID (0);
			setZZ_Is_SPOI (false);
// N
        } */
    }

    /** Standard Constructor */
    public X_ZZ_Disciplines (Properties ctx, String ZZ_Disciplines_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZ_Disciplines_UU, trxName, virtualColumns);
      /** if (ZZ_Disciplines_UU == null)
        {
			setName (null);
			setZZ_Disciplines_ID (0);
			setZZ_Is_SPOI (false);
// N
        } */
    }

    /** Load Constructor */
    public X_ZZ_Disciplines (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZ_Disciplines[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
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

	/** Set ZZ_Disciplines_UU.
		@param ZZ_Disciplines_UU ZZ_Disciplines_UU
	*/
	public void setZZ_Disciplines_UU (String ZZ_Disciplines_UU)
	{
		set_Value (COLUMNNAME_ZZ_Disciplines_UU, ZZ_Disciplines_UU);
	}

	/** Get ZZ_Disciplines_UU.
		@return ZZ_Disciplines_UU	  */
	public String getZZ_Disciplines_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZ_Disciplines_UU);
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
}