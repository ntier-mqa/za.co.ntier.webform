package za.co.ntier.webform.sdr.component.tab.bean;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.Query;

import za.co.ntier.api.model.I_ZZBankingDetails;
import za.co.ntier.api.model.I_ZZSdfOrganisation;
import za.co.ntier.api.model.X_AD_User;
import za.co.ntier.api.model.X_ZZBankingDetails;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;

public class OrglinkTabPanel extends NavTabPanel {

	private TableModel orgLinks;
	private X_AD_User person;
	
	private TableModel orgLinkComp;
	
	private X_ZZSdfOrganisation orgLinkPo;
	
	private TableModel bankDetailComp;
	
	private X_ZZBankingDetails bankDetailPo;
	
	public OrglinkTabPanel(NavTab parent, X_AD_User person) {
		super(parent);
		
		orgLinks = getOrgLinks(person, null);
		orgLinkComp = initOrgLinkModel(null, null);
		bankDetailComp = initBankInfo(null, null);
	}
	
	
	private TableModel getOrgLinks(X_AD_User person, X_ZZSdf sdf){
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel orgNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID)
				, I_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID
				);
		cols.add(orgNameCol);
		
		ColumnModel sdfStatusCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZSdfStatus)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZSdfStatus
				);
		cols.add(sdfStatusCol);
		
		ColumnModel sdfTypeCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZSdfType)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZSdfType
				);
		cols.add(sdfTypeCol);
		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("orglinkList");
		
		Query savedDataQuery = MTable.get(I_ZZSdfOrganisation.Table_ID).createQuery(String.format("%s = ?",
				I_ZZSdfOrganisation.COLUMNNAME_AD_User_ID), null);
		savedDataQuery.setParameters(person.getAD_User_ID());
		savedDataQuery.setOrderBy(I_ZZSdfOrganisation.COLUMNNAME_Created);
		
		namesBean.init(sdf, savedDataQuery.list(), null);
		namesBean.setFormView(false);
		return namesBean;
	}

	private TableModel initOrgLinkModel(X_AD_User person, X_ZZSdf sdf) {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel actingForEmployerCol = CheckboxCellModel.getCheckboxColModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZActingForEmployer)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZActingForEmployer
				);
		cols.add(actingForEmployerCol);
		
		ColumnModel sdfFunctionCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZSdfFunction)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZSdfFunction
				, MasterUtil.getLkpFunctionLists()
				, sdfFunction -> {return sdfFunction.getName();}
				, sdfFunction -> {return sdfFunction.getValue();}
				);
		cols.add(sdfFunctionCol);
		
		ColumnModel appointmentProcedureCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure
				, MasterUtil.getLkpAppointment()
				, appointment -> {return appointment.getName();}
				, appointment -> {return appointment.getValue();}
				);
		cols.add(appointmentProcedureCol);
		
		ColumnModel appointmentOtherCol = CellModel.getColModelForText(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther
				);
		cols.add(appointmentOtherCol);
		
		ColumnModel replacingPrimaryCol = CheckboxCellModel.getCheckboxColModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZReplacingPrimarySDF)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZReplacingPrimarySDF
				);
		cols.add(replacingPrimaryCol);
		
		ColumnModel secondarySdfCol = CheckboxCellModel.getCheckboxColModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name, I_ZZSdfOrganisation.COLUMNNAME_ZZSecondarySdf)
				, I_ZZSdfOrganisation.COLUMNNAME_ZZSecondarySdf
				);
		cols.add(secondarySdfCol);
		
		UploadColumnModel btAppointmentLetterCol = UploadCellModel.getUploadColumnModel(
				"", null, null, "UPLOAD LETTER OF APPOINTMENT"
				);
		cols.add(btAppointmentLetterCol);
		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("orglink");
		
		/*
		 * Query savedDataQuery =
		 * MTable.get(I_ZZSdfOrganisation.Table_ID).createQuery(String.format("%s = ?",
		 * I_ZZSdfOrganisation.COLUMNNAME_AD_User_ID), null);
		 * savedDataQuery.setParameters(person.getAD_User_ID());
		 * savedDataQuery.setOrderBy(I_ZZSdfOrganisation.COLUMNNAME_Created);
		 */
		
		namesBean.init(sdf, null, null);
		
		return namesBean;
	}

	private TableModel initBankInfo(X_AD_User person, X_ZZSdf sdf) {
		List<ColumnModel> cols = new ArrayList<>();

		UploadColumnModel btBankDetailCol = UploadCellModel.getUploadColumnModel(
				"", null, null, "UPLOAD BANK DETAILS"
				);
		cols.add(btBankDetailCol);
		
		ColumnModel warningBankDetailUploadCol = CellModel.getColModelForLabel(
				"Banking details uploaded must be signed off by the Organsation CFO as true and correct"
				);
		cols.add(warningBankDetailUploadCol);
		
		ColumnModel bankNameCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_BankName)
				, I_ZZBankingDetails.COLUMNNAME_BankName
				, MasterUtil.getLkpFunctionLists()
				, sdfFunction -> {return sdfFunction.getName();}
				, sdfFunction -> {return sdfFunction.getValue();}
				);
		cols.add(bankNameCol);
		/*
		 * ColumnModel bankNameCol = CellModel.getColModelForText(
		 * MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
		 * I_ZZBankingDetails.COLUMNNAME_BankName) ,
		 * I_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID ); cols.add(orgNameCol);
		 * 
		 * ColumnModel sdfFunctionCol = ListCellModel.getListColumnModel(
		 * MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZSdfFunction) ,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZSdfFunction ,
		 * MasterUtil.getLkpFunctionLists() , sdfFunction -> {return
		 * sdfFunction.getName();} , sdfFunction -> {return sdfFunction.getValue();} );
		 * cols.add(sdfFunctionCol);
		 * 
		 * ColumnModel appointmentProcedureCol = ListCellModel.getListColumnModel(
		 * MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure) ,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure ,
		 * MasterUtil.getLkpAppointment() , appointment -> {return
		 * appointment.getName();} , appointment -> {return appointment.getValue();} );
		 * cols.add(appointmentProcedureCol);
		 * 
		 * ColumnModel appointmentOtherCol = CellModel.getColModelForText(
		 * MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther) ,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther );
		 * cols.add(appointmentOtherCol);
		 * 
		 * ColumnModel replacingPrimaryCol = CheckboxCellModel.getCheckboxColModel(
		 * MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZReplacingPrimarySDF) ,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZReplacingPrimarySDF );
		 * cols.add(replacingPrimaryCol);
		 * 
		 * ColumnModel secondarySdfCol = CheckboxCellModel.getCheckboxColModel(
		 * MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZSecondarySdf) ,
		 * I_ZZSdfOrganisation.COLUMNNAME_ZZSecondarySdf ); cols.add(secondarySdfCol);
		 * 
		 * UploadColumnModel btAppointmentLetterCol =
		 * UploadCellModel.getUploadColumnModel( "", null, null,
		 * "UPLOAD LETTER OF APPOINTMENT" ); cols.add(btAppointmentLetterCol);
		 */
		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("bankDetails");
		
		/*
		 * Query savedDataQuery =
		 * MTable.get(I_ZZSdfOrganisation.Table_ID).createQuery(String.format("%s = ?",
		 * I_ZZSdfOrganisation.COLUMNNAME_AD_User_ID), null);
		 * savedDataQuery.setParameters(person.getAD_User_ID());
		 * savedDataQuery.setOrderBy(I_ZZSdfOrganisation.COLUMNNAME_Created);
		 */
		
		namesBean.init(sdf, null, null);
		
		return namesBean;
	}
	/**
	 * @return the person
	 */
	public X_AD_User getPerson() {
		return person;
	}


	/**
	 * @param person the person to set
	 */
	public void setPerson(X_AD_User person) {
		this.person = person;
	}


	/**
	 * @return the orgLinks
	 */
	public TableModel getOrgLinks() {
		return orgLinks;
	}


	/**
	 * @param orgLinks the orgLinks to set
	 */
	public void setOrgLinks(TableModel orgLinks) {
		this.orgLinks = orgLinks;
	}


	/**
	 * @return the orgLinkComp
	 */
	public TableModel getOrgLinkComp() {
		return orgLinkComp;
	}


	/**
	 * @param orgLinkComp the orgLinkComp to set
	 */
	public void setOrgLinkComp(TableModel orgLinkComp) {
		this.orgLinkComp = orgLinkComp;
	}


	/**
	 * @return the bankDetailComp
	 */
	public TableModel getBankDetailComp() {
		return bankDetailComp;
	}


	/**
	 * @param bankDetailComp the bankDetailComp to set
	 */
	public void setBankDetailComp(TableModel bankDetailComp) {
		this.bankDetailComp = bankDetailComp;
	}

}
