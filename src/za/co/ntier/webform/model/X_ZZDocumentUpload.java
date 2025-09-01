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

/** Generated Model for ZZDocumentUpload
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZDocumentUpload")
public class X_ZZDocumentUpload extends PO implements I_ZZDocumentUpload, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250901L;

    /** Standard Constructor */
    public X_ZZDocumentUpload (Properties ctx, int ZZDocumentUpload_ID, String trxName)
    {
      super (ctx, ZZDocumentUpload_ID, trxName);
      /** if (ZZDocumentUpload_ID == 0)
        {
			setIsMandatory (false);
// N
			setZZDocumentUpload_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZDocumentUpload (Properties ctx, int ZZDocumentUpload_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZDocumentUpload_ID, trxName, virtualColumns);
      /** if (ZZDocumentUpload_ID == 0)
        {
			setIsMandatory (false);
// N
			setZZDocumentUpload_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZDocumentUpload (Properties ctx, String ZZDocumentUpload_UU, String trxName)
    {
      super (ctx, ZZDocumentUpload_UU, trxName);
      /** if (ZZDocumentUpload_UU == null)
        {
			setIsMandatory (false);
// N
			setZZDocumentUpload_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZDocumentUpload (Properties ctx, String ZZDocumentUpload_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZDocumentUpload_UU, trxName, virtualColumns);
      /** if (ZZDocumentUpload_UU == null)
        {
			setIsMandatory (false);
// N
			setZZDocumentUpload_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZDocumentUpload (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZDocumentUpload[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
    }

	/** Set Mandatory.
		@param IsMandatory Data entry is required in this column
	*/
	public void setIsMandatory (boolean IsMandatory)
	{
		set_Value (COLUMNNAME_IsMandatory, Boolean.valueOf(IsMandatory));
	}

	/** Get Mandatory.
		@return Data entry is required in this column
	  */
	public boolean isMandatory()
	{
		Object oo = get_Value(COLUMNNAME_IsMandatory);
		if (oo != null)
		{
			 if (oo instanceof Boolean)
				 return ((Boolean)oo).booleanValue();
			return "Y".equals(oo);
		}
		return false;
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

	/** Set Document Upload.
		@param ZZDocumentUpload_ID Document Upload
	*/
	public void setZZDocumentUpload_ID (int ZZDocumentUpload_ID)
	{
		if (ZZDocumentUpload_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZDocumentUpload_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZDocumentUpload_ID, Integer.valueOf(ZZDocumentUpload_ID));
	}

	/** Get Document Upload.
		@return Document Upload	  */
	public int getZZDocumentUpload_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZDocumentUpload_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZDocumentUpload_UU.
		@param ZZDocumentUpload_UU ZZDocumentUpload_UU
	*/
	public void setZZDocumentUpload_UU (String ZZDocumentUpload_UU)
	{
		set_Value (COLUMNNAME_ZZDocumentUpload_UU, ZZDocumentUpload_UU);
	}

	/** Get ZZDocumentUpload_UU.
		@return ZZDocumentUpload_UU	  */
	public String getZZDocumentUpload_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZDocumentUpload_UU);
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