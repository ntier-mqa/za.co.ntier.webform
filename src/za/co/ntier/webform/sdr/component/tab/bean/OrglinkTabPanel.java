package za.co.ntier.webform.sdr.component.tab.bean;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.zkoss.zul.ListModelList;

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

	private ListModelList<X_ZZSdfOrganisation> orgLinkModel;
	
	private X_AD_User person;
	
	private TableModel orgLinkComp;
	
	private X_ZZSdfOrganisation orgLinkPo;
	
	private TableModel bankDetailComp;
	
	private X_ZZBankingDetails bankDetailPo;
	
	public OrglinkTabPanel(NavTab parent, X_AD_User person) {
		super(parent);
		this.person = person;
		initOrgLinks();
		orgLinkComp = initOrgLinkModel(null, null);
		bankDetailComp = initBankInfo(null, null);
	}
	
	private void initOrgLinks(){
		Query savedDataQuery = MTable.get(I_ZZSdfOrganisation.Table_ID).createQuery(String.format("%s = ?",
				I_ZZSdfOrganisation.COLUMNNAME_AD_User_ID), null);
		savedDataQuery.setParameters(person.getAD_User_ID());
		savedDataQuery.setOrderBy(I_ZZSdfOrganisation.COLUMNNAME_Created);
		setOrgLinkModel(new ListModelList<>(savedDataQuery.list()));
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
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_BankName)
				, I_ZZBankingDetails.COLUMNNAME_BankName
				, MasterUtil.getBanks()
				, bank -> {return bank.getName();}
				, bank -> {return bank.getC_Bank_ID();}
				).required();
		cols.add(bankNameCol);
		
		ColumnModel branchNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name)
				, I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name
				).required();
		cols.add(branchNameCol);
		
		ColumnModel branchCodeCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number)
				, I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number
				).required();
		cols.add(branchCodeCol);
		
		ColumnModel accountHolderCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_ZZAccountHolder)
				, I_ZZBankingDetails.COLUMNNAME_ZZAccountHolder
				).required();
		cols.add(accountHolderCol);
		
		ColumnModel accountTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_AccountType)
				, I_ZZBankingDetails.COLUMNNAME_AccountType
				, MasterUtil.getLkpAccountType()
				, lkpAccountType -> {return lkpAccountType.getName();}
				, lkpAccountType -> {return lkpAccountType.getValue();}
				).required();
		cols.add(accountTypeCol);
		
		ColumnModel accountNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_AccountNo)
				, I_ZZBankingDetails.COLUMNNAME_AccountNo
				).required();
		cols.add(accountNoCol);
		
		
		ColumnModel adminConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_ZZAdminDetailsCorrect)
				, I_ZZBankingDetails.COLUMNNAME_ZZAdminDetailsCorrect
				, MasterUtil.getYesNoList()
				, lkpAccountType -> {return lkpAccountType.getName();}
				, lkpAccountType -> {return lkpAccountType.getValue();}
				).required();
		cols.add(adminConfirmCol);
		
		ColumnModel bankConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsCorrect)
				, I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsCorrect
				, MasterUtil.getYesNoList()
				, lkpAccountType -> {return lkpAccountType.getName();}
				, lkpAccountType -> {return lkpAccountType.getValue();}
				).required();
		cols.add(bankConfirmCol);
		
		ColumnModel bankChangeConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name, I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsChanged)
				, I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsChanged
				, MasterUtil.getYesNoList()
				, lkpAccountType -> {return lkpAccountType.getName();}
				, lkpAccountType -> {return lkpAccountType.getValue();}
				).required();
		cols.add(bankChangeConfirmCol);
		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("bankDetails");
		
		 Query savedDataQuery =
		 MTable.get(I_ZZBankingDetails.Table_ID).createQuery(String.format("%s = ?",
				 I_ZZBankingDetails.COLUMNNAME_C_BPartner_ID), null);
		 //savedDataQuery.setParameters(person.getAD_User_ID());
		 savedDataQuery.setOrderBy(I_ZZSdfOrganisation.COLUMNNAME_Updated);
		 //PO bankingDetails = savedDataQuery.first();
		 
		namesBean.init(null, null);
		
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

	/**
	 * @return the orgLinkModel
	 */
	public ListModelList<X_ZZSdfOrganisation> getOrgLinkModel() {
		return orgLinkModel;
	}

	/**
	 * @param orgLinkModel the orgLinkModel to set
	 */
	public void setOrgLinkModel(ListModelList<X_ZZSdfOrganisation> orgLinkModel) {
		this.orgLinkModel = orgLinkModel;
	}

}
