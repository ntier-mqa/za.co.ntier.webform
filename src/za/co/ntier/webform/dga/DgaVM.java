package za.co.ntier.webform.dga;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.InputEvent;

import com.google.common.base.Objects;

import za.co.ntier.api.model.I_ZZDocumentUpload;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.I_ZZ_EDP_Application;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZDocumentUploadFile;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.api.model.X_ZZ_EDP_Application;
import za.co.ntier.api.model.X_ZZ_FormContact;
import za.co.ntier.api.model.X_ZZ_LI_HighestEducation;
import za.co.ntier.webform.component.DeclarationPanel;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.AddressType;
import za.co.ntier.webform.form.bean.ProgramType;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.form.viewmodel.DiscretionaryGrantsApplicationProgramVM;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.PresetTitleCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.SDLCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.PresetTitleColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;
import za.co.ntier.webform.sdr.viewmodel.BaseAppVM;

public class DgaVM extends BaseAppVM{
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	private NavTab mainTab;
	private X_ZZ_Application_Form applicationForm; 
	
	DaoManage dgaDaoManage = new DaoManage();
	
	@Override
	public boolean isShowSaveOnFirstTab() {
		return false;
	}
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		initApp();
		
		mainTab = new NavTab();
		initDeclareForm();
		initOrganisationInfo();
		if (ProgramType.EDP_APP_EMPLOYER == menuContextInfo.getProgramType())
			initEdpInfor(true);
		
		if (ProgramType.EDP_APP_INDIVIDUAL == menuContextInfo.getProgramType())
			initEdpInfor(false);
		
		initUploadDoc();
	}

	

	private void initUploadDoc() {
		
		List<X_ZZDocumentUpload> docUploads = MTable.get(Env.getCtx(), I_ZZDocumentUpload.Table_Name)
				.createQuery(I_ZZDocumentUpload.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?", null)
				.setParameters(menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID()).list();

		if (docUploads.size() == 0)
			return;
		
		List<ColumnModel> cols = new ArrayList<>();
		
		Function<X_ZZDocumentUpload, String> convertDisplay = docUploadDef -> {
				//if (docUploadDef == null)
					//return null;
				return docUploadDef.getName();
			};
		
		final PresetTitleColumnModel<X_ZZDocumentUpload> documentNameCol = PresetTitleCellModel.getPresetTitleColumnModel(
				"Name"
				, convertDisplay
				);
		documentNameCol.setMatchingLoaded((rowModel, poSaved) -> {
			X_ZZDocumentUpload docDef = (X_ZZDocumentUpload)rowModel.get(documentNameCol).getValue();
			return docDef.getZZDocumentUpload_ID() == ((X_ZZDocumentUploadFile)poSaved).getZZDocumentUpload_ID();
		});
				
		cols.add(documentNameCol);
		
		UploadColumnModel uploadCol = UploadCellModel.getUploadColumnModel("", null, null, "doc");
		uploadCol.setRefDocUploadDefCol(documentNameCol);
		cols.add(uploadCol);
		
		NavTabPanel uploadDocInfoTab = new NavTabPanel(mainTab);
		uploadDocInfoTab.setTabTitle("Upload Document");
		
		TableModel tmUploadDocInfo = TableModel.getTableBean(TableModel.class, cols, false);
		tmUploadDocInfo.setViewModel(TableModel.ViewType.VIEW_GRID);
		tmUploadDocInfo.setColumnInfos(cols);
		tmUploadDocInfo.setSclass("uploadDocInfo");
		tmUploadDocInfo.setPoSupplier(rowModel -> {
			X_ZZDocumentUploadFile po = new X_ZZDocumentUploadFile(Env.getCtx(), 0, null);
			X_ZZDocumentUpload docDef = (X_ZZDocumentUpload)rowModel.get(documentNameCol).getValue();
			po.setName(docDef.getName());
			po.setZZDocumentUpload_ID(docDef.getZZDocumentUpload_ID());
			return po;
		});
		tmUploadDocInfo.setBeforeSave(beforeSave);
		
		List<PO> documentUploadFiles = null;
		if (applicationForm != null) {
			Query uploadDocQuery = MTable.get(Env.getCtx(), X_ZZDocumentUploadFile.Table_Name).createQuery(
		            String.format("%s = ?", X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID), null
		        );

	        documentUploadFiles = uploadDocQuery
	            .setOrderBy(X_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUploadFile_ID)
	            .setParameters(applicationForm.getZZ_Application_Form_ID())
	            .list();
		}
		
		tmUploadDocInfo.init(documentUploadFiles, DgaVM.getTitleMap(documentNameCol, docUploads));
		
		uploadDocInfoTab.getCompModel().add(tmUploadDocInfo);
		
	}
	
	public static List<Map<ColumnModel, Object>> getTitleMap(ColumnModel colModel, List<?> titles){
		List<Map<ColumnModel, Object>> arrTitles = new ArrayList<Map<ColumnModel,Object>>();
		
		for (Object title:titles) {
			Map<ColumnModel, Object> mTitle = Map.of(colModel, title);
			arrTitles.add(mTitle);
		}
		
		return arrTitles;
	}
	
	private void initApp() {
		if (StringUtils.isNotBlank(menuContextInfo.getApplicationFormUU())) {
			applicationForm = new X_ZZ_Application_Form(Env.getCtx(), menuContextInfo.getApplicationFormUU(), null);
			if (!applicationForm.isActive()) {
				MasterUtil.showInfoDialog("ZZDGADeletedApp", MasterUtil.fCloseActiveWindow);
				
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
	
	Function<PO, Boolean> beforeSave = po -> {
		if (po.get_TableName().equals(X_ZZ_Application_Form.Table_Name) || 
				po.get_ColumnIndex(X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID) < 0
				){
			return Boolean.TRUE;
		}
		
		if (applicationForm == null || applicationForm.get_ValueAsInt(X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID) == 0) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZDGAAppNotYetInitAppForm"));
		}
		
		if (po.get_Value(X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID) == null) {
			po.set_ValueOfColumn(X_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID, applicationForm.getZZ_Application_Form_ID());
		}
		return Boolean.TRUE;
		
	};
	
	private void initDeclareForm() {
		NavTabPanel declareDetailTab = new DeclarationPanel(mainTab);
		declareDetailTab.setSclass("tabDeclare");
		declareDetailTab.setTabTitle("Declaration");
		// name component
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel userNameCol = CellModel.getColModelForText(
				//MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_UserName)
				"Name and Surname"
				, I_ZZ_Application_Form.COLUMNNAME_UserName
				).required()
				.setTableName(I_ZZ_Application_Form.Table_Name)
				.setDefaultValue(
						MUser.getNameOfUser(Env.getAD_User_ID(Env.getCtx())));
		cols.add(userNameCol);
		
		ColumnModel docDateCol = DateCellModel.getDateColumnModel(
				//MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_DateDoc)
				"Date"
				, I_ZZ_Application_Form.COLUMNNAME_DateDoc
				).setReadonly(true)
				.setTableName(I_ZZ_Application_Form.Table_Name)
				.setDefaultValue(
						Timestamp.valueOf(LocalDateTime.now()));
		cols.add(docDateCol);
		
		boolean acknowledge = applicationForm != null;
		
		ColumnModel acknowledgeCol = CheckboxCellModel.getCheckboxColModel(
				"Click here to acknowledge commitment"
				, null
				).setRequireChecked(true)
				.setDefaultValue(acknowledge)
				;
		acknowledgeCol.setEventHandle((event, cellModel) -> {});;
		cols.add(acknowledgeCol);

		TableModel tmAppName = TableModel.getTableBean(TableModel.class, cols, false);
		tmAppName.setColumnInfos(cols);
		tmAppName.setSclass("dgaAppInfo");
		tmAppName.setDaoManage(dgaDaoManage);
		tmAppName.init();
		
		declareDetailTab.getCompModel().add(tmAppName);
		
	}
	ColumnModel sdlNumberCol;
	TableModel tmOrgInfo;
	private void initOrganisationInfo() {
		NavTabPanel orgInfoTab = new NavTabPanel(mainTab);
		orgInfoTab.setTabTitle("Organisation Information");
		
		// name component
		List<ColumnModel> cols = new ArrayList<>();
		sdlNumberCol = SDLCellModel.getSDLColumnModel(
				//"Skills Development Levy (SDL) Number (Paying or Exempted)"
				null
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
				MasterUtil.getNameOfColTranslated(I_ZZ_Application_Form.Table_Name, I_ZZ_Application_Form.COLUMNNAME_ZZ_Side_SDL_No) + " (if applicable)"
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
				.setShowTitle(false)
				.setTableName(I_ZZ_Application_Form.Table_Name);
		
		cols.add(vatUploadCol);
		
		tmOrgInfo = TableModel.getTableBean(TableModel.class, cols, false);
		tmOrgInfo.setSectionHeader("ORGANISATION INFORMATION");
		tmOrgInfo.setColumnInfos(cols);
		tmOrgInfo.setSclass("dgaOrgInfo");
		tmOrgInfo.setDaoManage(dgaDaoManage);
		tmOrgInfo.setSaveApp(this);
		tmOrgInfo.init();
		
		orgInfoTab.getCompModel().add(tmOrgInfo);
		
		Function<RowModel, PO> poSupplier = rowModel -> {
				X_ZZ_FormContact po = new X_ZZ_FormContact(Env.getCtx(), 0, null);
				po.setZZ_ContactType(rowModel.getTableModel().getDataType());
				return po;
			};
		// physical address 
		TableModel tmPhysicalAddress = BuildFormUtil.buildFormContact(null, AddressType.PHYSICAL, applicationForm, 
				poSupplier, beforeSave, null);
		tmPhysicalAddress.setPoSupplier(poSupplier);
		
		orgInfoTab.getCompModel().add(tmPhysicalAddress);
		
		// postal address
		TableModel tmPostalAddress = BuildFormUtil.buildFormContact(null, AddressType.POSTAL, applicationForm, poSupplier, beforeSave, tmPhysicalAddress);
		tmPostalAddress.setPoSupplier(poSupplier);
		
		orgInfoTab.getCompModel().add(tmPostalAddress);
		
		TableModel tmSizeOrgInfo = null;
		ColumnModel wspCol = null;
		ColumnModel colNoOfEmp = null;
		if (!menuContextInfo.getProgramType().isCetTvet() && menuContextInfo.getProgramType() != ProgramType.STANDARD_SETTING) {
			// Org Size Info
			List<ColumnModel> otherOrgInfoCols = new ArrayList<>();
			
			// default is zero and laod from I_ZZ_Application_Form.COLUMNNAME_NumberEmployees
			// when sdl change update value from bPartner.ZZ_Number_Of_Employees
			// user can be change it and after that save to I_ZZ_Application_Form.COLUMNNAME_NumberEmployees
			colNoOfEmp = CellModel.getColModelForPositiveNumber(
					"Number of Employees", I_ZZ_Application_Form.COLUMNNAME_NumberEmployees
					).required()
					.setTableName(I_ZZ_Application_Form.Table_Name);
			
			otherOrgInfoCols.add(colNoOfEmp);
			
			// 1. default value is false, value load from I_ZZ_Application_Form.COLUMNNAME_ZZ_HasWSPSubmited
			// 2. in case sdl change, value set by check ZZ_WSP_ATR_Approvals
			// 3. filed is read only from user but change by (2) and store to I_ZZ_Application_Form.COLUMNNAME_ZZ_HasWSPSubmited
			wspCol = CellModel.getColModelForLabel("Has the organisation submitted the WSP/ATR In previous financial year?", I_ZZ_Application_Form.COLUMNNAME_ZZ_HasWSPSubmited);
			wspCol.setDefaultValue(Boolean.FALSE);
			wspCol.setMandatory(true);
			otherOrgInfoCols.add(wspCol);
			
			tmSizeOrgInfo = TableModel.getTableBean(TableModel.class, otherOrgInfoCols, false);
			tmSizeOrgInfo.setSubSectionHeader("OTHER ORGANISATION INFO");
			tmSizeOrgInfo.setTableTitle("(please select the correct option pertaining to the company size)");
			tmSizeOrgInfo.setSclass("orgSize");
			tmSizeOrgInfo.setDaoManage(dgaDaoManage);
			tmSizeOrgInfo.init();
			orgInfoTab.getCompModel().add(tmSizeOrgInfo);

			// main org contact
			TableModel orgContact = BuildFormUtil.buildFormContact(null, AddressType.ORG, applicationForm, poSupplier, beforeSave, null);
			orgContact.setPoSupplier(poSupplier);
			orgInfoTab.getCompModel().add(orgContact);
			
			// alter org contact
			TableModel alternateOrgContact = BuildFormUtil.buildFormContact(null, AddressType.ORG_ALTER, applicationForm, poSupplier, beforeSave, null);
			alternateOrgContact.setPoSupplier(poSupplier);
			orgInfoTab.getCompModel().add(alternateOrgContact);
		}
		
		final TableModel tmSizeOrgInfoF = tmSizeOrgInfo;
		final ColumnModel wspColF = wspCol;
		final ColumnModel colNoOfEmpF = colNoOfEmp;
		
		// don't need add this before init table mode because it's handle when user keyin
		sdlNumberCol.setEventHandle((event, cell) -> {
			InputEvent iEvent = (InputEvent)event;
			MBPartner_New orgFromSdl = null;
			if (StringUtils.isNoneBlank(iEvent.getValue())) {
				orgFromSdl = MBPartner_New.get(Env.getCtx(), iEvent.getValue());
			}
			
			
			if (orgFromSdl != null) {
				cell.getRowModel().get(orgNameCol).setValue(orgFromSdl.getName());
				cell.getRowModel().get(vatCol).setValue(orgFromSdl.getTaxID());
				cell.getRowModel().get(registrationNoCol).setValue(orgFromSdl.getReferenceNo());
			}
			
			if (orgFromSdl != null && tmSizeOrgInfoF != null) {
				tmSizeOrgInfoF.getRow().get(colNoOfEmpF).setValue(orgFromSdl.getZZ_Number_Of_Employees());
				
				int prevApprovedCount = DB.getSQLValueEx(null, String.format(
						"SELECT Count (*) FROM ZZ_WSP_ATR_Approvals WHERE C_BPartner_ID = ? AND ZZ_Grant_Status = 'A'"),
						orgFromSdl.getC_BPartner_ID());

				tmSizeOrgInfoF.getRow().get(wspColF).setValue(Boolean.valueOf(prevApprovedCount > 0));
				
			}
			
		});
		
		if (menuContextInfo.getProgramType() == ProgramType.STANDARD_SETTING){
			TableModel alternateOrgContact = BuildFormUtil.buildFormContact(null, AddressType.ORG_ALTER, applicationForm, poSupplier, beforeSave, null);
			alternateOrgContact.setPoSupplier(poSupplier);
			orgInfoTab.getCompModel().add(alternateOrgContact);
		}
		
		if (StringUtils.isNoneBlank(menuContextInfo.getContextParam(WebForm.bpGroupMenuContextKeyNonPlus))) {
			//cetTvetColleges = MasterUtil.getBpartnersByGroup(menuContextInfo.getContextParam(WebForm.bpGroupMenuContextKeyNonPlus));
		}
	}
	
	
	private void initEdpInfor(boolean isEmployee) {
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
		
		idPasportCol.setValidateHandle((cellModel, validateMsgs) -> {
			// cellModel.getRowModel() is virtual row on case validate field
			PO currentDao = cellModel.getRowModel().getCurrentDao(cellModel);
			
			String idValue = (String)cellModel.getDirtyValue();
			Query dupIDQuery = MTable.get(Env.getCtx(), I_ZZ_EDP_Application.Table_Name)
					.createQuery(
							String.format("%s = ? AND (%s != ? OR 0 = ?)", 
									X_ZZ_EDP_Application.COLUMNNAME_ZZ_ID_Passport_No, I_ZZ_EDP_Application.COLUMNNAME_ZZ_EDP_Application_ID)
							, null);
			
			int edpApplicationID = 0;
			if (currentDao != null) {
				edpApplicationID = currentDao.get_ValueAsInt(I_ZZ_EDP_Application.COLUMNNAME_ZZ_EDP_Application_ID);
			}
			dupIDQuery.setParameters(idValue, edpApplicationID, edpApplicationID);
			
			if (dupIDQuery.first() != null) {
				validateMsgs.add(Msg.getMsg(Env.getCtx(), "ZZDGAEdpDuplicateID"));
			}else {
				for (RowModel row : cellModel.getTableModel().getValidateRows()) {
					if (row != cellModel.getRowModel()) {
						Object otherIdValue = row.get(cellModel.getColModel()).getValue();
						if (StringUtils.isNotBlank(idValue) && Objects.equal(otherIdValue, idValue)) {
							validateMsgs.add(Msg.getMsg(Env.getCtx(), "ZZDGAEdpDuplicateIDOtherRow"));
						}
					}
				}
			}
		});
		
		
		
		ColumnModel genderCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_ZZGender)
				);
		cols.add(genderCol);
		
		ColumnModel ageCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_Age)
				);
		cols.add(ageCol);
		
		idPasportCol.addCellPropertyChangeListener(evt -> {
			String passport = (String)evt.getNewValue();
			CellModel srcEvt = (CellModel)evt.getSource();
			
			if (srcEvt.getRowModel().get(genderCol) == null || srcEvt.getRowModel().get(ageCol) == null)
				return;
			
			if (StringUtils.isBlank(passport)) {
				srcEvt.getRowModel().get(genderCol).setValue(null);
				srcEvt.getRowModel().get(ageCol).setValue(null);
			}else {
				try{
					int yy = Integer.parseInt(passport.substring(0, 2));
					int mm = Integer.parseInt(passport.substring(2, 4));
					int dd = Integer.parseInt(passport.substring(4, 6));
	
					java.time.LocalDate today = java.time.LocalDate.now();
					int currentYY = today.getYear() % 100;
					int year = (yy <= currentYY ? 2000 + yy : 1900 + yy);
	
					java.time.LocalDate dob = java.time.LocalDate.of(year, mm, dd);
					int age = java.time.Period.between(dob, today).getYears();
					int g = Character.getNumericValue(passport.charAt(6)); // 7th digit
					String gender = (g < 5) ? "F" : "M";
					
					srcEvt.getRowModel().get(genderCol).setValue(gender);
					srcEvt.getRowModel().get(ageCol).setValue(age);
					
				}catch (Exception ex){
					throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZBindingWrongIDPassportNo"));
				}
				
			}
		});

		
		ColumnModel contactNumCol = CellModel.getColModelForPhone(
				"Tel Number"
				, I_ZZ_EDP_Application.COLUMNNAME_Cellphonenumber
				).required()
				.setTableName(I_ZZ_EDP_Application.Table_Name);
		
		cols.add(contactNumCol);
		
		ColumnModel altContactNumCol = CellModel.getColModelForPhone(
				"Alternative Number"
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
					)
					.setzClass(X_ZZ_LI_HighestEducation.class)
					.setUseForID(true)
					.required()
					.setTableName(I_ZZ_EDP_Application.Table_Name)
					;
		
		cols.add(highestQualityCol);
		
		ColumnModel  nqfLevelCol = 
				ListCellModel.getListColumnModel(
						MasterUtil.getNameOfColTranslated(I_ZZ_EDP_Application.Table_Name, I_ZZ_EDP_Application.COLUMNNAME_ZZ_NQF_Level)
						, I_ZZ_EDP_Application.COLUMNNAME_ZZ_NQF_Level
						, MasterUtil.getNQFLevel()
						, nqfLevel -> {return nqfLevel.getName();}
						, nqfLevel -> {return nqfLevel.getValue();}
					).setzClass(ValueNamePair.class)
				.setUseForID(true)
					.required()
					.setTableName(I_ZZ_EDP_Application.Table_Name);
		cols.add(nqfLevelCol);
		
		
		ListColumnModel<ValueNamePair> executiveStatus = ListCellModel.getListColumnModel(
				"Executive Status", 
				I_ZZ_EDP_Application.COLUMNNAME_ZZExecutiveStatus,
				MasterUtil.getExecutiveStatus(),
				ref -> {return ref.getName();},
				ref -> {return ref.getValue();},
				CellModel.RADIO_CELL
				).setzClass(ValueNamePair.class);
			executiveStatus.required();
			executiveStatus.setTableName(I_ZZ_EDP_Application.Table_Name);
			
		cols.add(executiveStatus);
		
		if(!isEmployee) {
			ColumnModel letterUploadCol = UploadCellModel.getUploadColumnModel("", null, null, "Motivation Letter")
					.required()
					.setShowTitle(false)
					.setTableName(I_ZZ_EDP_Application.Table_Name);
			
			cols.add(letterUploadCol);
		}
		
		
		MUser_New loginUser = MUser_New.get(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()));
		if (!isEmployee) {
			firstNameCol.setDefaultValue(loginUser.getName());
			surnameCol.setDefaultValue(loginUser.getZZSurname());
			idPasportCol.setDefaultValue(loginUser.getZZ_ID_Passport_No());
			contactNumCol.setDefaultValue(loginUser.getPhone());
			altContactNumCol.setDefaultValue(loginUser.getPhone2());
			emailCol.setDefaultValue(loginUser.getEMail());
		}
		
		List<PO> saveds = null;
		if (applicationForm != null) {
			saveds = new Query(Env.getCtx(), X_ZZ_EDP_Application.Table_Name,
					X_ZZ_EDP_Application.COLUMNNAME_ZZ_Application_Form_ID + "=?", null)
							.setParameters(applicationForm.getZZ_Application_Form_ID())
							.setOnlyActiveRecords(true).list();
		}
		
		
		tmEdpEmpInfo = TableModel.getTableBean(TableModel.class, cols, false);
		if (isEmployee)
			tmEdpEmpInfo.setViewModel(TableModel.ViewType.VIEW_CARD);
		else
			tmEdpEmpInfo.setViewModel(TableModel.ViewType.VIEW_FORM);
		
		tmEdpEmpInfo.setColumnInfos(cols);
		tmEdpEmpInfo.setSclass("edpEmpInfo");
		tmEdpEmpInfo.setBeforeSave(beforeSave);
		
		tmEdpEmpInfo.setPoSupplier(daoManage -> {
			X_ZZ_EDP_Application edp = new X_ZZ_EDP_Application(Env.getCtx(), 0, null);
			edp.setAD_Org_ID(applicationForm.getAD_Org_ID());
			return edp;
		});
		
		tmEdpEmpInfo.init(saveds);
		
		edpEmpInfoTab.getCompModel().add(tmEdpEmpInfo);
		
	}
	TableModel tmEdpEmpInfo = null;
	@Override
	public void doSave(String trxName) {
		super.doSave(trxName);
		String sdlNumber = (String)tmOrgInfo.getRow().get(sdlNumberCol).getValue();
		MBPartner_New bp = MBPartner_New.get(Env.getCtx(), sdlNumber);
		applicationForm.setC_BPartner_ID(bp.getC_BPartner_ID());
		
		if (menuContextInfo.getProgramType() == ProgramType.EDP_APP_INDIVIDUAL){
			applicationForm.setZZTotalNumberApplied(1);
		}
		
		if (tmEdpEmpInfo != null && menuContextInfo.getProgramType() == ProgramType.EDP_APP_EMPLOYER) {
			applicationForm.setZZTotalNumberApplied(tmEdpEmpInfo.getRows().size());
		}
		applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Draft);
		applicationForm.setDateDoc(Timestamp.valueOf(LocalDateTime.now()));
		applicationForm.saveEx(trxName);
	}
	
	@Override
	public void doSubmit(String trxName) {
		applicationForm.setZZ_DocStatus(X_ZZ_Application_Form.ZZ_DOCSTATUS_Submitted);
		applicationForm.saveEx(trxName);
	}
	
	@Override
	protected void showResult(boolean isSubmit) {
		String title = isSubmit?"Successfully submitted the application form":"Successfully saved the application form";
		
		List<String> msgs = new ArrayList<>();
		if (isSubmit)
			msgs.add("The application form has been submitted");
		else
			msgs.add("The application form has been saved");

		msgs.add("Your Application Reference No is:" + applicationForm.getDocumentNo());
		
		if (isSubmit) {
			msgs.add("This has been sent as an email to you for future reference");
		}else {
			msgs.add("Please note this for future queries");
		}

		MasterUtil.showInfoDialog(title, msgs, t -> {
			MasterUtil.closeActiveWindow();
			MasterUtil.openForm("3b0c2d85-8f2e-44e9-b030-4b134159a052");
		});	
		
		if (isSubmit)
			DiscretionaryGrantsApplicationProgramVM.sentEmail(applicationForm, menuContextInfo);
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
	
	@Override
	public boolean isSupportSubmit() {
		return true;
	}

	@Override
	public Object getMainApp() {
		return applicationForm;
	}
}
