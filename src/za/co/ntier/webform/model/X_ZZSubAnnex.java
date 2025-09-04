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

/** Generated Model for ZZSubAnnex
 *  @author iDempiere (generated)
 *  @version Release 12 - $Id$ */
@org.adempiere.base.Model(table="ZZSubAnnex")
public class X_ZZSubAnnex extends PO implements I_ZZSubAnnex, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250904L;

    /** Standard Constructor */
    public X_ZZSubAnnex (Properties ctx, int ZZSubAnnex_ID, String trxName)
    {
      super (ctx, ZZSubAnnex_ID, trxName);
      /** if (ZZSubAnnex_ID == 0)
        {
			setZZSubAnnex_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZSubAnnex (Properties ctx, int ZZSubAnnex_ID, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZSubAnnex_ID, trxName, virtualColumns);
      /** if (ZZSubAnnex_ID == 0)
        {
			setZZSubAnnex_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZSubAnnex (Properties ctx, String ZZSubAnnex_UU, String trxName)
    {
      super (ctx, ZZSubAnnex_UU, trxName);
      /** if (ZZSubAnnex_UU == null)
        {
			setZZSubAnnex_ID (0);
        } */
    }

    /** Standard Constructor */
    public X_ZZSubAnnex (Properties ctx, String ZZSubAnnex_UU, String trxName, String ... virtualColumns)
    {
      super (ctx, ZZSubAnnex_UU, trxName, virtualColumns);
      /** if (ZZSubAnnex_UU == null)
        {
			setZZSubAnnex_ID (0);
        } */
    }

    /** Load Constructor */
    public X_ZZSubAnnex (Properties ctx, ResultSet rs, String trxName)
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
      StringBuilder sb = new StringBuilder ("X_ZZSubAnnex[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_ZZAnnexure getZZAnnexure() throws RuntimeException
	{
		return (I_ZZAnnexure)MTable.get(getCtx(), I_ZZAnnexure.Table_ID)
			.getPO(getZZAnnexure_ID(), get_TrxName());
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

	/** Set Field Of Study.
		@param ZZFieldStudy Field Of Study
	*/
	public void setZZFieldStudy (int ZZFieldStudy)
	{
		set_Value (COLUMNNAME_ZZFieldStudy, Integer.valueOf(ZZFieldStudy));
	}

	/** Get Field Of Study.
		@return Field Of Study	  */
	public int getZZFieldStudy()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZFieldStudy);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Number Of learners.
		@param ZZLearners Number Of learners
	*/
	public void setZZLearners (int ZZLearners)
	{
		set_Value (COLUMNNAME_ZZLearners, Integer.valueOf(ZZLearners));
	}

	/** Get Number Of learners.
		@return Number Of learners	  */
	public int getZZLearners()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZLearners);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Number Of Managers.
		@param ZZManagers Number Of Managers
	*/
	public void setZZManagers (int ZZManagers)
	{
		set_Value (COLUMNNAME_ZZManagers, Integer.valueOf(ZZManagers));
	}

	/** Get Number Of Managers.
		@return Number Of Managers	  */
	public int getZZManagers()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZManagers);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Requested Programme.
		@param ZZRequestedProgramme Requested Programme
	*/
	public void setZZRequestedProgramme (int ZZRequestedProgramme)
	{
		set_Value (COLUMNNAME_ZZRequestedProgramme, Integer.valueOf(ZZRequestedProgramme));
	}

	/** Get Requested Programme.
		@return Requested Programme	  */
	public int getZZRequestedProgramme()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZRequestedProgramme);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sub Annex.
		@param ZZSubAnnex_ID Sub Annex
	*/
	public void setZZSubAnnex_ID (int ZZSubAnnex_ID)
	{
		if (ZZSubAnnex_ID < 1)
			set_ValueNoCheck (COLUMNNAME_ZZSubAnnex_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_ZZSubAnnex_ID, Integer.valueOf(ZZSubAnnex_ID));
	}

	/** Get Sub Annex.
		@return Sub Annex	  */
	public int getZZSubAnnex_ID()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_ZZSubAnnex_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set ZZSubAnnex_UU.
		@param ZZSubAnnex_UU ZZSubAnnex_UU
	*/
	public void setZZSubAnnex_UU (String ZZSubAnnex_UU)
	{
		set_Value (COLUMNNAME_ZZSubAnnex_UU, ZZSubAnnex_UU);
	}

	/** Get ZZSubAnnex_UU.
		@return ZZSubAnnex_UU	  */
	public String getZZSubAnnex_UU()
	{
		return (String)get_Value(COLUMNNAME_ZZSubAnnex_UU);
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
}