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

/** Generated Model for ZZDocumentUploadFile
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZDocumentUploadFile")
public class X_ZZDocumentUploadFile extends PO implements I_ZZDocumentUploadFile, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250904L;

    /** Standard Constructor */
    public X_ZZDocumentUploadFile (Properties ctx, int ZZDocumentUploadFile_ID, String trxName)
    {
      super (ctx, ZZDocumentUploadFile_ID, trxName);
      /** if (ZZDocumentUploadFile_ID == 0)
        {
			setName (null);
			setZZDocumentUploadFile_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZDocumentUploadFile (Properties ctx, int ZZDocumentUploadFile_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZDocumentUploadFile_ID, trxName, virtualColumns);
      /** if (ZZDocumentUploadFile_ID == 0)
        {
			setName (null);
			setZZDocumentUploadFile_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZDocumentUploadFile (Properties ctx, String ZZDocumentUploadFile_UU, String trxName)
    {
      super (ctx, ZZDocumentUploadFile_UU, trxName);
      /** if (ZZDocumentUploadFile_UU == null)
        {
			setName (null);
			setZZDocumentUploadFile_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZDocumentUploadFile (Properties ctx, String ZZDocumentUploadFile_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZDocumentUploadFile_UU, trxName, virtualColumns);
      /** if (ZZDocumentUploadFile_UU == null)
        {
			setName (null);
			setZZDocumentUploadFile_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZDocumentUploadFile (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZDocumentUploadFile[")
        .append(get_ID()).append(",Name=").append(getName()).append("]");
      return sb.toString();
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

	/** Set Document Upload File.
		@param ZZDocumentUploadFile_ID Document Upload File
	*/
	public void setZZDocumentUploadFile_ID (int ZZDocumentUploadFile_ID)
	{
		if (ZZDocumentUploadFile_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZDocumentUploadFile_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZDocumentUploadFile_ID, Integer.valueOf(ZZDocumentUploadFile_ID));
	}

	/** Get Document Upload File.
		@return Document Upload File	  */
	public int getZZDocumentUploadFile_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZDocumentUploadFile_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZDocumentUploadFile_UU.
		@param ZZDocumentUploadFile_UU ZZDocumentUploadFile_UU
	*/
	public void setZZDocumentUploadFile_UU (String ZZDocumentUploadFile_UU)
	{
		set_Value (COLUMNNAME_ZZDocumentUploadFile_UU, ZZDocumentUploadFile_UU);
	}

	/** Get ZZDocumentUploadFile_UU.
		@return ZZDocumentUploadFile_UU	  */
	public String getZZDocumentUploadFile_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZDocumentUploadFile_UU);
	}

	public I_ZZDocumentUpload getZZDocumentUpload() throws RuntimeException
	{
		return (I_ZZDocumentUpload)MTable.get(getCtx(), I_ZZDocumentUpload.Table_ID)
			.getPO(getZZDocumentUpload_ID(), get_TrxName());
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

	/** Set Document Uploaded.
		@param ZZDocumentUploaded Binary Of Document Upload
	*/
	public void setZZDocumentUploaded (byte[] ZZDocumentUploaded)
	{
		set_Value (COLUMNNAME_ZZDocumentUploaded, ZZDocumentUploaded);
	}

	/** Get Document Uploaded.
		@return Binary Of Document Upload
	  */
	public byte[] getZZDocumentUploaded()
	{
		return (byte[])get_Value(COLUMNNAME_ZZDocumentUploaded);
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