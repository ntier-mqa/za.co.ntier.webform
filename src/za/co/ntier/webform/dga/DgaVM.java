package za.co.ntier.webform.dga;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_AD_User;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.EMail;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_EDP_Application;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_EDP_Application;
import za.co.ntier.api.model.X_ZZ_FormContact;
import za.co.ntier.webform.component.DeclarationPanel;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.AddressInfo;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.form.bean.component.OrganisationSizeInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;
import za.co.ntier.webform.sdr.viewmodel.BaseAppVM;

public class DgaVM extends BaseAppVM{
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	X_ZZ_Application_Form applicationForm; 
	
	DaoManage dgaDaoManage = new DaoManage();
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		initApp();
		
		mainTab = new NavTab();
		initDeclareForm();
		initOrganisationInfo();
		initEdpEmployeeInfor();
	}

	private void initApp() {
		if (StringUtils.isNotBlank(menuContextInfo.getApplicationFormUU())) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), menuContextInfo.getApplicationFormUU(), null);
			if (!applicationForm.isActive()) {
				MasterUtil.showDialog("ZZDGADeletedApp", MasterUtil.fCloseActiveWindow);
				
			}
			
			dgaDaoManage.setDao(applicationForm);
		}
		
		dgaDaoManage.setPoSupplier(I_ZZ_Application_Form.Table_Name, daoManage -> {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), 0, null);
			applicationForm.setZZProgramType(menuContextInfo.getProgramType().toString());
			applicationForm.setAD_Org_ID(menuContextInfo.getProgramMasterData().getAD_Org_ID());
			applicationForm.setZZ_Program_Master_Data_ID(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID());
			applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Draft);
			applicationForm.setZZTotalNumberApplied(0);
			applicationForm.setName(
					MUser.getNameOfUser(Env.getAD_Client_ID(Env.getCtx())) + " - " + LocalDateTime.now().format(MasterUtil.dtf));
			return applicationForm;
		});

	}
	
	private void initDeclareForm() {
		NavTabPanel declareDetailTab = new DeclarationPanel(mainTab);
		declareDetailTab.setTabTitle("Declaration");
		// name component
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel userNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_UserName)
				, I_ZZ_Application_Form.COLUMNNAME_UserName
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name)
				.setDefaultValue(
						MUser.getNameOfUser(Env.getAD_User_ID(Env.getCtx())));
		cols.add(userNameCol);
		
		ColumnModel docDateCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_DateDoc)
				, I_ZZ_Application_Form.COLUMNNAME_DateDoc
				).setReadonly(true)
				.setTableName(I_ZZ_Application_Form.Table_Name)
				.setDefaultValue(
						Timestamp.valueOf(LocalDateTime.now()));
		cols.add(docDateCol);
		
		ColumnModel acknowledgeCol = CheckboxCellModel.getCheckboxColModel(
				"Click here to acknowledge commitment"
				, null
				).required();
		acknowledgeCol.setEventHandle((event, cellModel) -> {});;
		cols.add(acknowledgeCol);

		TableModel tmAppName = TableModel.getTableBean(TableModel.class, cols, false);
		tmAppName.setColumnInfos(cols);
		tmAppName.setSclass("dgaAppInfo");
		tmAppName.setDaoManage(dgaDaoManage);
		tmAppName.init();
		
		declareDetailTab.getCompModel().add(tmAppName);
		
	}
	
	private void initOrganisationInfo() {
		NavTabPanel orgInfoTab = new NavTabPanel(mainTab);
		orgInfoTab.setTabTitle("Organisation Information");
		
		// name component
		List<ColumnModel> cols = new ArrayList<>();
		ColumnModel sdlNumberCol = CellModel.getColModelForText(
				"Skills Development Levy (SDL) Number (Paying or Exempted)"
				, I_ZZ_Application_Form.COLUMNNAME_ZZ_SDL_No
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name);
				
		cols.add(sdlNumberCol);
		
		ColumnModel orgNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_OrgName)
				, I_ZZ_Application_Form.COLUMNNAME_OrgName
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name);
				
		cols.add(orgNameCol);
		
		ColumnModel sideSdlNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_ZZ_Side_SDL_No)
				, I_ZZ_Application_Form.COLUMNNAME_ZZ_Side_SDL_No
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name);
		
		cols.add(sideSdlNoCol);
		
		ColumnModel registrationNoCol = CellModel.getColModelForText(
				"Registration No"
				, I_ZZ_Application_Form.COLUMNNAME_ReferenceNo
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name);
		
		cols.add(registrationNoCol);
		
		ColumnModel vatCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_ZZ_VAT)
				, I_ZZ_Application_Form.COLUMNNAME_ZZ_VAT
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name);
		
		cols.add(vatCol);
		
		ColumnModel vatUploadCol = UploadCellModel.getUploadColumnModel("", null, null, "VAT Exempt Cert.")
				.required()
				.setTableName(I_ZZ_Application_Form.Table_Name);
		
		cols.add(vatUploadCol);
		
		
		TableModel tmOrgInfo = TableModel.getTableBean(TableModel.class, cols, false);
		tmOrgInfo.setSectionHeader("ORGANISATION INFORMATION");
		tmOrgInfo.setColumnInfos(cols);
		tmOrgInfo.setSclass("dgaOrgInfo");
		tmOrgInfo.setDaoManage(dgaDaoManage);
		tmOrgInfo.init();
		
		orgInfoTab.getCompModel().add(tmOrgInfo);
		
		Function<TableModel, PO> poSupplier = tableModel -> {
				X_ZZ_FormContact po = new X_ZZ_FormContact(Env.getCtx(), 0, null);
				po.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
				po.setZZ_ContactType(tableModel.getDataType());
				return po;
			};
		// physical address 
		TableModel tmPhysicalAddress = BuildFormUtil.buildFormContact(menuContextInfo.getProgramType(), AddressType.PHYSICAL, applicationForm, null);
		tmPhysicalAddress.setPoSupplier(poSupplier);
		
		orgInfoTab.getCompModel().add(tmPhysicalAddress);
		
		// postal address
		TableModel tmPostalAddress = BuildFormUtil.buildFormContact(menuContextInfo.getProgramType(), AddressType.POSTAL, applicationForm, tmPhysicalAddress);
		tmPostalAddress.setPoSupplier(poSupplier);
		
		orgInfoTab.getCompModel().add(tmPostalAddress);
		
		// org contact
		if (!menuContextInfo.getProgramType().isCetTvet() && menuContextInfo.getProgramType() != ProgramType.STANDARD_SETTING) {
			//orgSizeInfo = new OrganisationSizeInfo();  

			TableModel orgContact = BuildFormUtil.buildFormContact(menuContextInfo.getProgramType(), AddressType.ORG, applicationForm, null);
			orgContact.setPoSupplier(poSupplier);
			orgInfoTab.getCompModel().add(orgContact);
			
			TableModel alternateOrgContact = BuildFormUtil.buildFormContact(menuContextInfo.getProgramType(), AddressType.ORG_ALTER, applicationForm, null);
			alternateOrgContact.setPoSupplier(poSupplier);
			orgInfoTab.getCompModel().add(alternateOrgContact);
		}
		
		if (menuContextInfo.getProgramType() == ProgramType.STANDARD_SETTING){
			TableModel alternateOrgContact = BuildFormUtil.buildFormContact(menuContextInfo.getProgramType(), AddressType.ORG_ALTER, applicationForm, null);
			alternateOrgContact.setPoSupplier(poSupplier);
			orgInfoTab.getCompModel().add(alternateOrgContact);
		}
		
		if (StringUtils.isNoneBlank(menuContextInfo.getContextParam(WebForm.bpGroupMenuContextKeyNonPlus))) {
			//cetTvetColleges = MasterUtil.getBpartnersByGroup(menuContextInfo.getContextParam(WebForm.bpGroupMenuContextKeyNonPlus));
		}
	}
	
	private void initEdpEmployeeInfor() {
		NavTabPanel edpEmpInfoTab = new NavTabPanel(mainTab);
		edpEmpInfoTab.setTabTitle("Employee Information");
		
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZFirstName)
				, I_ZZ_EDP_Application.COLUMNNAME_Name
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
				
		cols.add(firstNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZSurname)
				, I_ZZ_EDP_Application.COLUMNNAME_Surname
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(surnameCol);
		
		ColumnModel idPasportCol = CellModel.getColModelForIDPASS(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_ZZ_ID_Passport_No)
				, I_ZZ_EDP_Application.COLUMNNAME_ZZ_ID_Passport_No
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(idPasportCol);
		
		
		ColumnModel contactNumCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_Cellphonenumber)
				, I_ZZ_EDP_Application.COLUMNNAME_Cellphonenumber
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(contactNumCol);
		
		ColumnModel altContactNumCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_AltCellphonenumber)
				, I_ZZ_EDP_Application.COLUMNNAME_AltCellphonenumber
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(altContactNumCol);
		
		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_EMail)
				, I_ZZ_EDP_Application.COLUMNNAME_EMail
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(emailCol);
		
		ColumnModel positionCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_Position)
				, I_ZZ_EDP_Application.COLUMNNAME_Position
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(positionCol);
		
		ColumnModel highestQualityCol = 
				ListCellModel.getListColumnModel(
						MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_ZZ_LI_HighestEducation_ID)
						, I_ZZ_EDP_Application.COLUMNNAME_ZZ_LI_HighestEducation_ID
						, MasterUtil.getHighestEducations()
						, highestEducation -> {return highestEducation.getName();}
						, highestEducation -> {return highestEducation.getZZ_LI_HighestEducation_ID();}
					).setUseForID(true)
					.required()
					.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(highestQualityCol);
		
		ColumnModel  nqfLevelCol = 
				ListCellModel.getListColumnModel(
						MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_ZZ_NQF_Level)
						, I_ZZ_EDP_Application.COLUMNNAME_ZZ_NQF_Level
						, MasterUtil.getNQFLevel()
						, nqfLevel -> {return nqfLevel.getName();}
						, nqfLevel -> {return nqfLevel.getValue();}
					).setUseForID(true)
					.required()
					.setTableName(I_ZZ_EDP_Application.Table_Name);
		cols.add(nqfLevelCol);
		
		ColumnModel letterUploadCol = UploadCellModel.getUploadColumnModel("", null, null, "Motivation Letter")
				.required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(letterUploadCol);
		
		List<PO> saveds = null;
		if (applicationForm != null) {
			saveds = new Query(Env.getCtx(), X_ZZ_EDP_Application.Table_Name,
					X_ZZ_EDP_Application.COLUMNNAME_ZZ_Application_Form_ID + "=?", null)
							.setParameters(applicationForm.getZZ_Application_Form_ID())
							.setOnlyActiveRecords(true).list();
		}
		
		
		TableModel tmEdpEmpInfo = TableModel.getTableBean(TableModel.class, cols, false);
		tmEdpEmpInfo.setViewModel(TableModel.VIEW_CARD);
		tmEdpEmpInfo.setColumnInfos(cols);
		tmEdpEmpInfo.setSclass("edpEmpInfo");
		
		tmEdpEmpInfo.setPoSupplier(daoManage -> {
			X_ZZ_EDP_Application edp = new X_ZZ_EDP_Application(Env.getCtx(), 0, null);
			edp.setZZ_Application_Form_ID(applicationForm.getZZ_Application_Form_ID());
			edp.setAD_Org_ID(applicationForm.getAD_Org_ID());
			return edp;
		});
		
		
		
		tmEdpEmpInfo.init(saveds);
		
		edpEmpInfoTab.getCompModel().add(tmEdpEmpInfo);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab);
	}

	@Override
	public List<DaoManage> getDaoManages() {
		return List.of(dgaDaoManage);
	}
	
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public FormInfo getFormInfo() {
		return formInfo;
	}

	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}

	public NavTab getMainTab() {
		return mainTab;
	}

	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}
}
