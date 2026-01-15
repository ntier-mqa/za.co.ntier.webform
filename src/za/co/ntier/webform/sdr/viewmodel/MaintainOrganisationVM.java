package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.I_AD_User;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZ_FormContact;
import za.co.ntier.api.model.X_C_BPartner;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceCellModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

public class MaintainOrganisationVM extends BaseVM {
	private FormInfo formInfo;
	private TableModel names;
	TableModel physicalAddress;
	TableModel postalAddress;
	private MenuContextInfo menuContextInfo;
	X_C_BPartner orgPO;
	
	private NavTab mainTab;
	
	private void initOrg(MenuContextInfo menuContextInfo) {
		int sdfOrganisationID = menuContextInfo.getRecordID();
		Query orgPOQuery = MTable.get(Env.getCtx(), X_C_BPartner.Table_Name)
				.createQuery(String.format("%s.%s = ?", X_ZZSdfOrganisation.Table_Name, X_ZZSdfOrganisation.COLUMNNAME_ZZSdfOrganisation_ID), null);
				
		orgPOQuery.addJoinClause(String.format("INNER JOIN %s ON (%s.%s = %s.%s)",
				X_ZZSdfOrganisation.Table_Name,
				X_ZZSdfOrganisation.Table_Name,
				X_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID,
				X_C_BPartner.Table_Name,
				X_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID));
		orgPOQuery.setParameters(sdfOrganisationID);
		org.compiere.model.X_C_BPartner orgPOCore = orgPOQuery.first();
		
		orgPO = new X_C_BPartner(Env.getCtx(), orgPOCore.getC_BPartner_ID(), null);
	}

	
	
	private void initMaintab(){
		mainTab = new NavTab();
		
		NavTabPanel generalDetailTab = new NavTabPanel(mainTab);
		generalDetailTab.setTabTitle("GENERAL DETAILS");
		// new component
		generalDetailTab.getCompModel().add(initGeneralDetailComp());
		
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("CONTACT DETAILS");
		// new component
		contactDetailTab.getCompModel().add(initContactDetailComp());

		// address tab
		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("address");
		addressDetailTab.setTabTitle("ADDRESS DETAILS");
		
		physicalAddress = BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical", false, orgPO.getC_BPartner_ID());
		postalAddress = BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", false, orgPO.getC_BPartner_ID());
		addressDetailTab.getCompModel().add(physicalAddress);
		addressDetailTab.getCompModel().add(BuildFormUtil.getAddressControlComp(physicalAddress, postalAddress));
		addressDetailTab.getCompModel().add(postalAddress);
	}
	
	private TableModel initContactDetailComp() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel titleCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Title)
				, I_AD_User.COLUMNNAME_Title
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(titleCol);
		
		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Name)//TODO SDF name
				, I_AD_User.COLUMNNAME_Name
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(firstNameCol);
		
		ColumnModel lastNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Name)//TODO name 2
				, I_AD_User.COLUMNNAME_Name
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(lastNameCol);
		
		//Designation
		/*ColumnModel lastNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_de)//TODO name 2
				, I_AD_User.COLUMNNAME_Name
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(lastNameCol);*/
		
		ColumnModel telephoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				).setTableName(I_AD_User.Table_Name);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required()
				.setTableName(I_AD_User.Table_Name);
		cols.add(cellPhoneNumberCol);

		ColumnModel orgFaxCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Fax)
				, I_AD_User.COLUMNNAME_Fax
				).setTableName(I_AD_User.Table_Name);
		cols.add(orgFaxCol);
		
		ColumnModel emailCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required()
				.setReadonly(true)
				.setTableName(I_AD_User.Table_Name);
		cols.add(emailCol);
		
		ColumnModel provinceCol = ProvinceCellModel.getProvinceColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_C_Region_ID)
				, I_ZZ_FormContact.COLUMNNAME_C_Region_ID).required();
		cols.add(provinceCol);
		
		
		TableModel orgContactDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		orgContactDetailBean.setSclass("srd-org-contact");
		
		orgContactDetailBean.setDaoManage(orgDaoManage);
		
		orgContactDetailBean.init(null, null);
		
		return orgContactDetailBean;
	}

	private TableModel initGeneralDetailComp() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel registrationNumCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ReferenceNo)
				, I_C_BPartner.COLUMNNAME_ReferenceNo
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(registrationNumCol);

		ColumnModel sdlNumTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZLevyNumberType)
				, I_C_BPartner.COLUMNNAME_ZZLevyNumberType
				, MasterUtil.getLevyNumberType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(sdlNumTypeCol);
		
		ColumnModel orgRegistrationNumTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZOrgRegistrationNumberType)
				, I_C_BPartner.COLUMNNAME_ZZOrgRegistrationNumberType
				, MasterUtil.getOrganisationRegistrationNumberType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(orgRegistrationNumTypeCol);
		
		ColumnModel sarsNumCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZSarsNumber)
				, I_C_BPartner.COLUMNNAME_ZZSarsNumber
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(sarsNumCol);
		
		ColumnModel numOfEmpCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZ_Number_Of_Employees)
				, I_C_BPartner.COLUMNNAME_ZZ_Number_Of_Employees
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(numOfEmpCol);
		
		ColumnModel numOfEmpProfileCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getDescOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZNumberOfEmployeesProfile)
				, I_C_BPartner.COLUMNNAME_ZZNumberOfEmployeesProfile
			).setTableName(I_C_BPartner.Table_Name);
		cols.add(numOfEmpProfileCol);
				
		ColumnModel terminatedEmpCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZTerminatedEmployees)
				, I_C_BPartner.COLUMNNAME_ZZTerminatedEmployees
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(terminatedEmpCol);
		
		ColumnModel sicCodeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZSicCode)
				, I_C_BPartner.COLUMNNAME_ZZSicCode
				, MasterUtil.getSicCode()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(sicCodeCol);
		
		ColumnModel subSectorCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZSubSector)
				, I_C_BPartner.COLUMNNAME_ZZSubSector
				, MasterUtil.getSubSector()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(subSectorCol);
		
		ColumnModel orgTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZOrganisationType)
				, I_C_BPartner.COLUMNNAME_ZZOrganisationType
				, MasterUtil.getOrganisationType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(orgTypeCol);
		
		ColumnModel chamberCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZChamberCode)
				, I_C_BPartner.COLUMNNAME_ZZChamberCode
				, MasterUtil.getChamberCode()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(chamberCol);
		
		TableModel orgGeneralDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		orgGeneralDetailBean.setSclass("srd-org-general");
		
		orgGeneralDetailBean.setDaoManage(orgDaoManage);
		
		orgGeneralDetailBean.init(null, null);
		
		return orgGeneralDetailBean;
	}

	DaoManage orgDaoManage = new DaoManage();
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		initOrg(menuContextInfo);
		
		setFormInfo(new FormInfo(menuContextInfo));
		names = initNames();
		
		orgDaoManage = new DaoManage();
		orgDaoManage.setDao(orgPO);
		
		initMaintab();
	}

	private TableModel initNames() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel legalNameCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZLegalName")
				, I_C_BPartner.COLUMNNAME_Name
				).required()
				.setTableName(I_C_BPartner.Table_Name);
		cols.add(legalNameCol);
		
		ColumnModel tradeNameCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZTradeName")
				, I_C_BPartner.COLUMNNAME_Name2
				).required()
				.setTableName(I_C_BPartner.Table_Name);
				;
		cols.add(tradeNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZ_SDL_No")
				, I_C_BPartner.COLUMNNAME_Value
				).required()
				.setTableName(I_C_BPartner.Table_Name);
		cols.add(surnameCol);

		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		
		namesBean.init(null, List.of(orgPO));
		return namesBean;
	}
	/**
	 * @return the names
	 */
	public TableModel getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public void setNames(TableModel names) {
		this.names = names;
	}

	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}

	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	/**
	 * @return the menuContextInfo
	 */
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	/**
	 * @return the mainTab
	 */
	public NavTab getMainTab() {
		return mainTab;
	}

	/**
	 * @param mainTab the mainTab to set
	 */
	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}
	
	
	@Override
	protected void doSave(String trxName) {
		mainTab.save(null, trxName);
		orgPO.saveEx(trxName);
	}
	
	@Override
	protected boolean showResult(Exception exc) {
		if (exc != null) {
			showException(exc);
		}else {
			MasterUtil.showDialog("ZZOrgSavedSuccess", MasterUtil.fCloseActiveWindow);
		}

		return false;
	}
}
