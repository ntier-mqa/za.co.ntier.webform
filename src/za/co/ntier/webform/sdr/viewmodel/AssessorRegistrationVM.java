package za.co.ntier.webform.sdr.viewmodel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.model.GenericPO;
import org.apache.commons.lang3.StringUtils;
import org.compiere.minigrid.ColumnInfo;
import org.compiere.model.I_C_Location;
import org.compiere.model.MForm;
import org.compiere.model.MLocation;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_C_Location;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZAssessorPerson;
import za.co.ntier.api.model.I_ZZDocumentUpload;
import za.co.ntier.api.model.I_ZZLinkAssessorQualification;
import za.co.ntier.api.model.I_ZZLinkAssessorSkillsProgramme;
import za.co.ntier.api.model.I_ZZLkpSchoolEmis;
import za.co.ntier.api.model.I_ZZQctoQualification;
import za.co.ntier.api.model.I_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.I_ZZQualification_v;
import za.co.ntier.api.model.I_ZZSkillsProgramme_v;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.MQAConfiguration;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_C_BPartner;
import za.co.ntier.api.model.X_ZZAssessorPerson;
import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZDocumentUploadFile;
import za.co.ntier.api.model.X_ZZLinkAssessorQualification;
import za.co.ntier.api.model.X_ZZLinkAssessorSkillsProgramme;
import za.co.ntier.api.model.X_ZZLkpSchoolEmis;
import za.co.ntier.api.model.X_ZZLkpStatssaAreaCode;
import za.co.ntier.api.model.X_ZZQualification_v;
import za.co.ntier.api.model.X_ZZSkillsProgramme_v;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.api.model.X_ZZ_LI_CitizenResidentialStatus;
import za.co.ntier.api.model.X_ZZ_LI_HomeLanguage;
import za.co.ntier.api.model.X_ZZ_LI_SocioEconomicStatus;
import za.co.ntier.api.model.X_ZZ_Nationality;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.CommandSetting;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDTypeCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil.SettingAddress;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil.SettingTableMode;

public class AssessorRegistrationVM extends StepAppVM{
	public static String moderatorFormUU = "dbdc4e66-6cef-403b-9fc6-8669b2458bf1";
	public static String assessorFormUU = "53779b2b-eb44-4f13-8c03-a8407c3fceae";
	public static String moderatorScopeFormUU = "ebf7630f-6c99-44a0-a047-b5bbfba05b57";
	public static String assessorScopeFormUU = "b9a07c16-b8dc-4afd-a1b4-1bc7cfd5a950";
	
	private TableModel tmNames;
	private NavTab mainTab;
	X_ZZAssessorPerson assessorPerson;
	X_ZZAssessorPerson assessorPersonExpried;
	X_ZZAssessorPerson assessorPersonParent;
	private TableModel tmIdentity;
	public TableModel getTmIdentity() {
		return tmIdentity;
	}
	DaoManage daoManage = new DaoManage();
	
	@Override
	public List<DaoManage> getDaoManages() {
		return List.of(daoManage);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab, tmNames);
	}

	
	
	private static String EmailTemplateAssessorRegistrationConfirmation_UU = "c157e9e9-e05f-4286-b2b9-d9a95361d4cd";
	
	public static class EmailPoAssessorInfo extends GenericPO {
		MUser sdpAdmin;
		MUser_New assessorUser;
		X_ZZAssessorPerson assessorPerson;
		
		MenuContextInfo menuContextInfo;
		private static final long serialVersionUID = -433026634223871908L;

		public EmailPoAssessorInfo(MUser sdpAdmin, X_ZZAssessorPerson assessorPerson) {
			super(MUser.Table_Name, Env.getCtx(), 0);
			this.sdpAdmin = sdpAdmin;
			this.assessorPerson = assessorPerson;
			this.assessorUser = MUser_New.get(getCtx(), assessorPerson.getAD_User_ID());
		}
		
		public String getSubjectName() {
			boolean isAssessor = X_ZZAssessorPerson.ZZASSESSORROLE_Assessor.equals(assessorPerson.getZZAssessorRole());
			boolean isScopeExtension = assessorPerson.getParent_ID() > 0;
			boolean isReRegistration = X_ZZAssessorPerson.ZZREGISTRATIONTYPE_Re_Registration.equals(assessorPerson.getZZRegistrationType());
			
			if (!isScopeExtension && isAssessor && isReRegistration){
				return "Re-registration";
			}else if (!isScopeExtension && !isAssessor && isReRegistration){
				return "Re-registration";
			}else if (isAssessor && isScopeExtension){
				return "Scope Extension";
			}else if (!isAssessor && isScopeExtension) {
				return "Scope Extension";
			}else if (isAssessor && !isScopeExtension) {
				return "Assessor Registration";
			}else if (!isAssessor && !isScopeExtension) {
				return "Moderator Registration";
			}else {
				throw new AdempiereException("Case Not Handle");
			}
		}
		
		public String getAssessorFirstName() {
			return assessorUser.getZZFirstName();
		}
		
		public String getAssessorSurname() {
			return assessorUser.getZZSurname();
		}
		
		public String getReceiverName() {
			return sdpAdmin.getName();
		}
		
		public Timestamp getSubmittedDate() {
			return assessorPerson.getZZSubmittedDate();
		}
		
	}
	
	@Override
	protected void showResult(boolean isSubmit) {
		if (isSubmit) {
			
			MasterUtil.sentEmailSdf(EmailTemplateAssessorRegistrationConfirmation_UU, new EmailPoAssessorInfo(sdpAdmin, assessorPerson), sdpAdmin);
			
			// MasterUtil.sentEmailSdf(EmailTemplateAssessorRegistrationConfirmation_UU, assessorPerson, person);
		}
		if(isSubmit) {
			MasterUtil.showInfoDialog("ZZAssessorSubmitedSuccess", MasterUtil.fCloseActiveWindow);
		}else if(isNew) {
			MasterUtil.showInfoDialog("ZZAssessorCreatedSuccess", MasterUtil.fCloseActiveWindow);
		}else {
			MasterUtil.showInfoDialog("ZZAssessorSavedSuccess", MasterUtil.fCloseActiveWindow);
		}
	}
	
	private MUser_New person;
	boolean isNew = true;
	MForm adForm;
	
	private boolean isExtensionScope() {
		return moderatorScopeFormUU.equals(adForm.getAD_Form_UU()) || assessorScopeFormUU.equals(adForm.getAD_Form_UU());
	}
	
	private boolean isModerator() {
		return moderatorFormUU.equals(adForm.getAD_Form_UU()) || moderatorScopeFormUU.equals(adForm.getAD_Form_UU());
	}
	
	MUser sdpAdmin;
	
	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		int loginId = Env.getAD_User_ID(Env.getCtx());
		sdpAdmin = MUser.get(loginId);
		adForm = MForm.get(menuContextInfo.getFormId());
		
		setFormInfo(new FormInfo(menuContextInfo));
		
		initDaoManage();
		
		String openFormModel = Env.getContext(Env.getCtx(), menuContextInfo.getWinNo(), "+" + MenuContextInfo.OpenFormModelKey);
		if (MenuContextInfo.OpenFormModelEdit.equals(openFormModel)) {
			initStepRegistryAssessor();
		}else if (!isExtensionScope()){
			initStepIdentity();
		}else {
			initStepRegistryAssessor();
		}
		
	}
	
	void initDaoManage() {
		daoManage.setPoSupplier(I_AD_User.Table_Name, daoManage -> {
			person = new MUser_New(Env.getCtx(), 0, null);
			String name = (String)tmNames.getRow().get(firstNameCol).getValue();
			person.setName(name);
			return person;
		});
		
		daoManage.setPoSupplier(I_ZZAssessorPerson.Table_Name, daoManage -> {
			assessorPerson = initAssessor();
			return assessorPerson;
		});
	}
	
	private X_ZZAssessorPerson initAssessor() {
		X_ZZAssessorPerson assessorPerson = new X_ZZAssessorPerson(Env.getCtx(), 0, null);
		assessorPerson.setC_BPartner_ID(sdpAdmin.getC_BPartner_ID());
		if (isModerator()) {
			assessorPerson.setZZAssessorRole(X_ZZAssessorPerson.ZZASSESSORROLE_Moderator);
		}else {
			assessorPerson.setZZAssessorRole(X_ZZAssessorPerson.ZZASSESSORROLE_Assessor);
		}
		
		if (existingAssessorForCopy != null) {
			assessorPerson.setZZGender(existingAssessorForCopy.getZZGender());
			assessorPerson.setZZEquity(existingAssessorForCopy.getZZEquity());
			assessorPerson.setZZ_LI_HomeLanguage_ID(existingAssessorForCopy.getZZ_LI_HomeLanguage_ID());
			assessorPerson.setZZ_Nationality_ID(existingAssessorForCopy.getZZ_Nationality_ID());
			assessorPerson.setZZ_LI_CitizenResidentialStatus_ID(existingAssessorForCopy.getZZ_LI_CitizenResidentialStatus_ID());
			assessorPerson.setZZ_LI_SocioEconomicStatus_ID(existingAssessorForCopy.getZZ_LI_SocioEconomicStatus_ID());
			
			assessorPerson.setZZHealthSeeing(existingAssessorForCopy.getZZHealthSeeing());
			assessorPerson.setZZHealthHearing(existingAssessorForCopy.getZZHealthHearing());
			assessorPerson.setZZHealthCommunicating(existingAssessorForCopy.getZZHealthCommunicating());
			assessorPerson.setZZHealthWalking(existingAssessorForCopy.getZZHealthWalking());
			assessorPerson.setZZHealthRemembering(existingAssessorForCopy.getZZHealthRemembering());
			assessorPerson.setZZHealthSelfcare(existingAssessorForCopy.getZZHealthSelfcare());
			
			assessorPerson.setZZLkpSchoolEmis_ID(existingAssessorForCopy.getZZLkpSchoolEmis_ID());
			assessorPerson.setZZLkpSchoolEmisText(existingAssessorForCopy.getZZLkpSchoolEmisText());
			assessorPerson.setZZLastSchoolYear(existingAssessorForCopy.getZZLastSchoolYear());
			assessorPerson.setZZLkpStatssaAreaCode_ID(existingAssessorForCopy.getZZLkpStatssaAreaCode_ID());
			assessorPerson.setZZPopiActStatus(existingAssessorForCopy.getZZPopiActStatus());
			assessorPerson.setZZPopiActStatusDate(existingAssessorForCopy.getZZPopiActStatusDate());
			
			assessorPerson.setZZPhysicalLocation_ID(existingAssessorForCopy.getZZPhysicalLocation_ID());
			assessorPerson.setZZPostalLocation_ID(existingAssessorForCopy.getZZPostalLocation_ID());
		}
		
		return assessorPerson;
	}
	
	private void initStepIdentity(){
		setStep("identity");
		if (tmIdentity == null)
			initIdentity();
	}
	
	private void initIdentity() {
		List<ColumnModel> cols = new ArrayList<>();
		
		identityAlternateIDTypeCol = IDTypeCellModel.getIDTypeCol();
		cols.add(identityAlternateIDTypeCol);
		
		identityIdNoCol = IDCellModel.getIDColumnModel()
				.required().setTableName(I_AD_User.Table_Name);
		cols.add(identityIdNoCol);
		
		ColumnModel validateCol = CellModel.getColModelForGenericCell("VALIDATE", null, CellModel.BUTTON_CELL);
		validateCol.setShowTitle(false);
		cols.add(validateCol);
		
		ColumnModel msgCol = CellModel.getColModelForLabel(null);
		msgCol.setShowTitle(false);
		cols.add(msgCol);
		
		String nextStepLabel = isModerator() ? "Register Moderator" : "Register Assessor";
		ColumnModel nextStepCol = CellModel.getColModelForGenericCell(nextStepLabel, null, CellModel.BUTTON_CELL);
		nextStepCol.setShowTitle(false);
		cols.add(nextStepCol);
		
		tmIdentity = TableModel.getTableBean(TableModel.class, cols, false, I_ZZAssessorPerson.Table_Name);
		tmIdentity.setSclass("srd-general srd-general-assessor-identity");
		tmIdentity.setViewModel(ViewType.VIEW_FORM);
		tmIdentity.setSectionHeader("Identity Details");
		tmIdentity.init();
		tmIdentity.getRow().get(nextStepCol).setVisible(false);
		
		identityAlternateIDTypeCol.setEventHandle((event, cellMode) -> {
			IDCellModel idCellMode = (IDCellModel)cellMode.getRowModel().get(identityIdNoCol);
			idCellMode.validate();
			
		});
		
		validateCol.setEventHandle((event, cellModel) -> {
			cellModel.getRowModel().get(msgCol).setVisible(false);
			
			cellModel.getRowModel().get(nextStepCol).setVisible(false);
			
			if (!tmIdentity.validate(false)) {
				MasterUtil.showInfoDialog("ZZAssessorWrongInputValidateIdentity", null);
				return;
			}
			
			Object idValue = cellModel.getRowModel().get(identityIdNoCol).getDirtyValue();
			
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDCellMode = (ListCellModel<X_ZZ_AlternateIDType>)cellModel.getRowModel().get(identityAlternateIDTypeCol);
			
			if (idValue != null && alternateIDCellMode.getSelectedItem() != null) {
				person = null;
				assessorPerson = null;
				assessorPersonParent = null;
				loadSavedFromInputParam((String)idValue, alternateIDCellMode.getSelectedItem().getZZ_AlternateIDType_ID());
				
				String msg = null;
				if (person != null && assessorPerson != null) {
					msg = "ZZAssessorMaintain";
				}else if(person != null && assessorPerson == null) {
					msg = assessorPersonExpried != null ? "ZZAssessorReRegistryAssessor" : "ZZAssessorNewAssessor";
				}else {
					msg = "ZZAssessorNewPerson";
				}
				
				cellModel.getRowModel().get(msgCol).setValue(Msg.getMsg(Env.getCtx(), msg));
				cellModel.getRowModel().get(msgCol).setVisible(true);
				
				cellModel.getRowModel().get(nextStepCol).setVisible(true);
			}else {
				MasterUtil.showInfoDialog("ZZAssessorInputValidateIdentity", null);
			}
			
		});
		
		nextStepCol.setEventHandle((event, cellModel) -> {
			initStepRegistryAssessor();
		});
	}
	
	
	TableModel tmGeneralDetail;
	
	ColumnModel idNoCol;
	ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;
	
	ColumnModel identityIdNoCol;
	ListColumnModel<X_ZZ_AlternateIDType> identityAlternateIDTypeCol;
	
	private void initStepRegistryAssessor(){
		setStep("registryAssessor");
		
		setMainTab(new NavTab());
		
		if (getMenuContextInfo().getRecordID() > 0) {
			loadForEdit();
		}
		
		initRegistryAssessorForm();
		if (assessorPersonExpried != null) {
			daoManage.setDao(assessorPersonExpried);
		}
		if (existingAssessorForCopy != null) {
			daoManage.getDaoForSave(I_ZZAssessorPerson.Table_Name);
		}
		loadData();
		if (assessorPersonExpried != null) {
			daoManage.resetDao(I_ZZAssessorPerson.Table_Name);
		}
		if (person == null && tmGeneralDetail != null) {
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDTypeCell = (ListCellModel<X_ZZ_AlternateIDType>)tmGeneralDetail.getRow().get(alternateIDTypeCol);
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> identityAlternateIDTypeCell = (ListCellModel<X_ZZ_AlternateIDType>)tmIdentity.getRow().get(identityAlternateIDTypeCol);
						
			alternateIDTypeCell.setValue(identityAlternateIDTypeCell.getSelectedID());
			
			CellModel idNoCell = tmGeneralDetail.getRow().get(idNoCol);
			CellModel identityIdNoCell = tmIdentity.getRow().get(identityIdNoCol);
			idNoCell.setValue(identityIdNoCell.getValue());
		}
	}
	
	private void loadForEdit() {
		X_ZZAssessorPerson assessorPersonCtx = (X_ZZAssessorPerson)MTable.get(Env.getCtx(), I_ZZAssessorPerson.Table_Name)
				.getPO(getMenuContextInfo().getRecordID(), null);
		
		if (assessorPersonCtx == null) {
			MasterUtil.showInfoDialog("ZZAssessorNotFoundAssessor", MasterUtil.fCloseActiveWindow);
		}else {
			person = (MUser_New)MTable.get(Env.getCtx(), I_AD_User.Table_Name)
					.getPO(assessorPersonCtx.getAD_User_ID(), null);
		}
		
		if (person == null) {
			MasterUtil.showInfoDialog("ZZAssessorNotFoundUser", MasterUtil.fCloseActiveWindow);
		}
		
		boolean isEditCase = false;
		if(assessorPersonCtx.getParent_ID() == 0 && isExtensionScope()) {
			assessorPersonParent = assessorPersonCtx;
			isEditCase = false;
		}else if(assessorPersonCtx.getParent_ID() == 0 && !isExtensionScope()) {
			// edit assessor
			isEditCase = true;
		}else if(assessorPersonCtx.getParent_ID() > 0 && isExtensionScope()) {
			//edit scope extension
			isEditCase = true;
		}else if(assessorPersonCtx.getParent_ID() > 0 && !isExtensionScope()) {
			// wrong state
			MasterUtil.showInfoDialog("ZZAssessorOpenWrongForm", MasterUtil.fCloseActiveWindow);
		}else {
			// out of case
			MasterUtil.showInfoDialog("ZZAssessorNotHandle", MasterUtil.fCloseActiveWindow);
		}
		
		daoManage.setDao(person);
		if (isEditCase) {
			validateAssessorState(assessorPersonCtx);
			assessorPerson = assessorPersonCtx;
			daoManage.setDao(assessorPerson);
			
			isNew = false;
		}
		
		selectedQuaIDs = querySelectedScopeID(true);
		selectedSkillsIDs = querySelectedScopeID(false);
		
	}
	
	AbstractMap.SimpleEntry<List<BigDecimal>, List<BigDecimal>> selectedQuaIDs;
	AbstractMap.SimpleEntry<List<BigDecimal>, List<BigDecimal>> selectedSkillsIDs;
	private AbstractMap.SimpleEntry<List<BigDecimal>, List<BigDecimal>> querySelectedScopeID (boolean quaID){
		List<BigDecimal> qctoScopeIds = new ArrayList<>();
		List<BigDecimal> nonQctoScopeIds = new ArrayList<>();
		qctoScopeIds.add(BigDecimal.ZERO);
		nonQctoScopeIds.add(BigDecimal.ZERO);
		
		AbstractMap.SimpleEntry<List<BigDecimal>, List<BigDecimal>> result = new AbstractMap.SimpleEntry<>(qctoScopeIds, nonQctoScopeIds);
		
		if (!isExtensionScope()) {
			return result;
		}
		
		int parentId = 0;
		int curId = 0;
		if (assessorPersonParent != null) {
			parentId = assessorPersonParent.getZZAssessorPerson_ID();
		}else if (assessorPerson != null) {
			parentId = assessorPerson.getZZAssessorPerson_ID();
		}else {
			throw new AdempiereException("ZZAssessorWrongFormState");
		}
		
		if (assessorPerson != null) {
			curId = assessorPerson.getZZAssessorPerson_ID();
		}
		
		List<List<Object>> scopeIds =  DB.getSQLArrayObjectsEx(null, String.format("""
				SELECT %s, %s FROM %s WHERE ZZAssessorPerson_ID IN  (
					%s
					WHERE input_person.ZZAssessorPerson_ID = ? and cohort.ZZAssessorPerson_ID != ?
				)
				"""
				, quaID?I_ZZLinkAssessorQualification.COLUMNNAME_ZZQctoQualification_ID:I_ZZLinkAssessorSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID
				, quaID?I_ZZLinkAssessorQualification.COLUMNNAME_ZZQualification_ID:I_ZZLinkAssessorSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID
				, quaID?I_ZZLinkAssessorQualification.Table_Name:I_ZZLinkAssessorSkillsProgramme.Table_Name
				, assessorTreeQuery
				)
				,parentId
				, curId);
		
		if (scopeIds == null || scopeIds.size() == 0)
			return result;
		
		for (List<Object>scopeId : scopeIds) {
			if (scopeId.get(0) != null) {
				qctoScopeIds.add((BigDecimal)scopeId.get(0));
			}else if (scopeId.get(1) != null) {
				nonQctoScopeIds.add((BigDecimal)scopeId.get(1));
			}
		}
		
		return result;
					
	}
	
	void validateAssessorState(X_ZZAssessorPerson assessorPersonValidate) {
		boolean isDraft = assessorPersonValidate.getZZ_DocStatus() == null || X_ZZAssessorPerson.ZZ_DOCSTATUS_Draft.equals(assessorPersonValidate.getZZ_DocStatus());
		if (!isDraft) {
			MasterUtil.showInfoDialog("ZZAssessorWrongStatus", MasterUtil.fCloseActiveWindow);
		}
	}
	
	X_ZZAssessorPerson existingAssessorForCopy = null;
	
	private void loadSavedFromInputParam (String idValue, int idTypeId) {
		existingAssessorForCopy = null;
		Query userQuery = MTable.get(Env.getCtx(), I_AD_User.Table_Name)
				.createQuery(String.format("(%s = ? AND %s = ?) OR (%s = ? AND %s = ?)", 
												I_AD_User.COLUMNNAME_ZZ_ID_Passport_No
												, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID
												, I_AD_User.COLUMNNAME_ZZOtherIDNo
												, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID), null);
		
		userQuery.setParameters(idValue, idTypeId, idValue, idTypeId);
		userQuery.setOnlyActiveRecords(true);
		person = userQuery.firstOnly();
		
		X_ZZAssessorPerson assessorPersonSaved = null;
		
		
		if (person == null && isModerator() && !isExtensionScope()) {
			// not exists person so not exists Assessor
			MasterUtil.showInfoDialog("ZZAssessorNotExistsAssessor", MasterUtil.fCloseActiveWindow);
			return;
		}
		
		if (person != null) {
			daoManage.setDao(person);
			if (isModerator() && !isExtensionScope()) {
				Query existAssessorQuery = MTable.get(Env.getCtx(), I_ZZAssessorPerson.Table_Name)
						.createQuery("""
								ZZAssessorPerson.AD_User_ID = ?
								AND ZZAssessorPerson.Parent_ID IS NULL
								AND ZZAssessorPerson.ZZAssessorRole = ?
								AND ZZAssessorPerson.ZZ_DocStatus = ?
								AND ZZAssessorPerson.EndDate >= (CURRENT_DATE - (INTERVAL '1 month' * ?))
								AND ZZAssessorPerson.C_BPartner_ID = ?
								""", null);
				existAssessorQuery.setParameters(person.getAD_User_ID()
						, X_ZZAssessorPerson.ZZASSESSORROLE_Assessor
						, X_ZZAssessorPerson.ZZ_DOCSTATUS_Approved
						, MQAConfiguration.getMonthsBeforeExpiry()
						, sdpAdmin.getC_BPartner_ID());
				
				existAssessorQuery.setOnlyActiveRecords(true);
				
				existAssessorQuery.setOrderBy("ZZAssessorPerson.EndDate");
				
				existingAssessorForCopy = existAssessorQuery.first();
				if (existingAssessorForCopy == null) {
					// exists person but not exists Assessor still active (not yet end date)
					MasterUtil.showInfoDialog("ZZAssessorNotExistsAssessor", MasterUtil.fCloseActiveWindow);
					return;
				}
			}
			
			// load first assessor to check it's exists also check expired
			Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZAssessorPerson.Table_Name)
					.createQuery("""
							ZZAssessorPerson.C_BPartner_ID = ?
							AND ZZAssessorPerson.AD_User_ID = ?
							AND (CASE WHEN ZZAssessorPerson.Parent_ID IS NULL THEN 'N' ELSE 'Y' END) = ?
							AND (CASE WHEN ZZAssessorPerson.Parent_ID IS NULL THEN ZZAssessorPerson.ZZAssessorRole ELSE parent.ZZAssessorRole END) = ?
							""", null);
			
			
			savedDataQuery.addJoinClause(String.format("LEFT JOIN %s parent ON (%s.%s = parent.%s)"
					, I_ZZAssessorPerson.Table_Name
					, I_ZZAssessorPerson.Table_Name
					, I_ZZAssessorPerson.COLUMNNAME_Parent_ID
					, I_ZZAssessorPerson.COLUMNNAME_ZZAssessorPerson_ID));

			if (isExtensionScope()) {
				savedDataQuery.setOrderBy(// sort to get draft and latest scope extension when multi exists 
						"""
						(ZZAssessorPerson.ZZ_DocStatus = 'DR') DESC, ZZAssessorPerson.Created DESC
						""");
			}
			
			if (!isExtensionScope()) {
				savedDataQuery.setOrderBy( 
						"""
						(ZZAssessorPerson.ZZ_DocStatus <> 'AP') DESC, ZZAssessorPerson.EndDate DESC
						""");
			}
			
			String role = X_ZZAssessorPerson.ZZASSESSORROLE_Assessor;
			if (isModerator()) {
				role = X_ZZAssessorPerson.ZZASSESSORROLE_Moderator;
			}
			savedDataQuery.setParameters(sdpAdmin.getC_BPartner_ID(), person.getAD_User_ID(), isExtensionScope(), role);
			savedDataQuery.setOnlyActiveRecords(true);
			assessorPersonSaved = savedDataQuery.first();

			if (!isExtensionScope() && assessorPersonSaved != null && assessorPersonSaved.getEndDate() != null) {
				LocalDate endDate = assessorPersonSaved.getEndDate().toLocalDateTime().toLocalDate();

				LocalDate moment = LocalDate.now().minusMonths(MQAConfiguration.getMonthsBeforeExpiry()); 

				boolean isAfterMoment = moment.isAfter(endDate);
				
				if (isAfterMoment) {
					assessorPersonExpried = assessorPersonSaved;
					assessorPersonSaved = null;
				}
			}
		}else {
			daoManage.resetDao(I_AD_User.Table_Name);
			//firstNameCol.setDefaultValue(null);
		}
		
		if (assessorPersonSaved != null) {
			validateAssessorState(assessorPersonSaved);
			daoManage.setDao(assessorPersonSaved);
		}else {
			if (isExtensionScope()) {
				//TODO:open scope extension form but isn't exists scope extension on draft state 
				// => try to load main assessor in case not exists main show error and create new scope extension and load main
				// => in case exists main it become create new scope extension for that main
				MasterUtil.showInfoDialog("ZZAssessorNotExistsMain", MasterUtil.fCloseActiveWindow);
			}
			daoManage.resetDao(I_ZZAssessorPerson.Table_Name);
		}
		
		
		isNew = assessorPersonSaved == null;
		assessorPerson = assessorPersonSaved;
	}
	
	private void loadData() {
		if(person != null) {
			// don't reload when null to keep user input
			tmNames.reloadDao();
			firstNameCol.setDefaultValue(person.getName());
		}

		//if (assessorPerson != null)
		mainTab.getTabPanelModel().forEach(tabModel -> {
			tabModel.getCompModel().forEach(tableModel -> {
				((TableModel)tableModel).reloadDao();
			});
		});
		
		tmLinkedBpartner.loadSavedData();
			
		mainTab.getTabPanelModel().forEach(tabModel -> {
			tabModel.getCompModel().forEach(tableModel -> {
				((TableModel)tableModel).loadSavedData();
			});
		});
	}
	
	private void initRegistryAssessorForm() {
		tmNames = initTbName();
		initLinkedBpartner();
		
		if (!isExtensionScope()) {
			initGeneralDetail();
			initContactDetail();
			initHealthFunction();
			initAddresss();
			initEducationDetail();
		}
		initQualification();
		initSkillsProgramme();
		initUploadDocument();
	}

	private void initGeneralDetail() {
		List<ColumnModel> cols = new ArrayList<>();
		
		alternateIDTypeCol = IDTypeCellModel.getIDTypeCol();
		alternateIDTypeCol.setReadonly(true);
		cols.add(alternateIDTypeCol);
		
		idNoCol = IDCellModel.getIDColumnModel()
				.required().setTableName(I_AD_User.Table_Name);
		idNoCol.setReadonly(true);
		cols.add(idNoCol);

		ColumnModel dateOfBirthCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Birthday)
				, I_AD_User.COLUMNNAME_Birthday
			).required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(dateOfBirthCol);

		ColumnModel genderCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZGender)
				, I_ZZAssessorPerson.COLUMNNAME_ZZGender
				, MasterUtil.getLkpGenders()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required();
		cols.add(genderCol);

		ColumnModel equityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZEquity)
				, I_ZZAssessorPerson.COLUMNNAME_ZZEquity
				, MasterUtil.getLkpEquity()
				, title -> {return title.toString();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required();
		cols.add(equityCol);

		ColumnModel homeLanguageCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_HomeLanguage_ID
				, MasterUtil.getHomeLanguage()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_HomeLanguage_ID();}
			).setzClass(X_ZZ_LI_HomeLanguage.class)
			.setUseForID(true)
			.required();
		cols.add(homeLanguageCol);
		
		ColumnModel nationalityCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_Nationality_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_Nationality_ID
				, MasterUtil.getNationality()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_Nationality_ID();}
			).setzClass(X_ZZ_Nationality.class)
			.setUseForID(true)
			.required();
		cols.add(nationalityCol);

		ColumnModel citizenResidentialStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_CitizenResidentialStatus_ID
				, MasterUtil.getCitizenResidentialStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_CitizenResidentialStatus_ID();}
			).setzClass(X_ZZ_LI_CitizenResidentialStatus.class)
			.setUseForID(true)
			.required();
		cols.add(citizenResidentialStatusCol);

		ColumnModel socioEconomicStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID)
				, I_ZZAssessorPerson.COLUMNNAME_ZZ_LI_SocioEconomicStatus_ID
				, MasterUtil.getSocioEconomicStatus()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_LI_SocioEconomicStatus_ID();}
			).setzClass(X_ZZ_LI_SocioEconomicStatus.class)
			.setUseForID(true)
			.required();
		cols.add(socioEconomicStatusCol);

		tmGeneralDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZAssessorPerson.Table_Name);
		tmGeneralDetail.setSclass("srd-general srd-general-assessor");
		
		tmGeneralDetail.setDaoManage(daoManage);
		
		tmGeneralDetail.init();
		
		NavTabPanel tabPanelGeneralDetail = new NavTabPanel(mainTab);
		tabPanelGeneralDetail.setTabTitle("General Details");
		// new component
		tabPanelGeneralDetail.getCompModel().add(tmGeneralDetail);
		
	}

	ColumnModel firstNameCol;
	
	private TableModel initTbName() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel greettingCol = ListCellModel.getLkpTitleColumnModel();
		cols.add(greettingCol);
		
		firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZFirstName)
				, I_AD_User.COLUMNNAME_ZZFirstName
				).required();
			
		cols.add(firstNameCol);
		
		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZMiddleName)
				, I_AD_User.COLUMNNAME_ZZMiddleName
				);
		cols.add(midNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZSurname)
				, I_AD_User.COLUMNNAME_ZZSurname
				).required();
		cols.add(surnameCol);
		
		TableModel tmNames = TableModel.getTableBean(TableModel.class, cols, false, I_AD_User.Table_Name);
		tmNames.setSclass("srd-name srd-name-assessor");
		tmNames.setDaoManage(daoManage);
		tmNames.init();
		
		return tmNames;
	}
	
	private TableModel tmLinkedBpartner;
	public TableModel getTmLinkedBpartner() {
		return tmLinkedBpartner;
	}
	void initLinkedBpartner() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel valueCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(X_C_BPartner.Table_Name, X_C_BPartner.COLUMNNAME_Value)
				, X_C_BPartner.COLUMNNAME_Value
				).setReadonly(true);
		cols.add(valueCol);
		
		ColumnModel nameCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(X_C_BPartner.Table_Name, X_C_BPartner.COLUMNNAME_Name)
				, X_C_BPartner.COLUMNNAME_Name
				).setReadonly(true);
		cols.add(nameCol);
		
		tmLinkedBpartner = TableModel.getTableBean(TableModel.class, cols, false, X_C_BPartner.Table_Name);
		tmLinkedBpartner.setSclass("srd-name srd-linked-bpartner");
		tmLinkedBpartner.setViewModel(ViewType.VIEW_GRID);
		tmLinkedBpartner.init();
		
		tmLinkedBpartner.setLoadSavedDataHandle(tableModel -> {
			int ownerPartnerID = 0;
			if (assessorPerson != null) {
				ownerPartnerID = assessorPerson.getC_BPartner_ID();
			}
			if (ownerPartnerID == 0 && sdpAdmin.getC_BPartner_ID() > 0) {
				ownerPartnerID = sdpAdmin.getC_BPartner_ID();
			}
			 
			if(ownerPartnerID > 0) {
				PO linkedBpartner = MBPartner_New.get(Env.getCtx(), ownerPartnerID);
				
				tableModel.reset(List.of(linkedBpartner));
			}
			
		});
		
	}

	private void initContactDetail() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required();
		cols.add(cellPhoneNumberCol);
		
		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				);
		cols.add(telephoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required();
		cols.add(emailCol);

		TableModel tmContactDetail = TableModel.getTableBean(TableModel.class, cols, false, I_AD_User.Table_Name);
		tmContactDetail.setSclass("srd-contact srd-contact-assessor");
		tmContactDetail.setDaoManage(daoManage);
		tmContactDetail.init();
		
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("Contact Details");
		contactDetailTab.getCompModel().add(tmContactDetail);

	}
	
	public static final String healthFunctionDefault = "No difficulty";
	BiFunction<ListCellModel<ValueNamePair>, ValueNamePair, Boolean> healthFunctionNameCompare = (cellModel, item) -> {
		String compareValue = cellModel.getColModel().getSelectedItemDisplayConvert().apply(item);
		return cellModel.getColModel().getDefaultValue().equals(compareValue);
	};
	
	private void initHealthFunction () {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel seeingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSeeing)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSeeing
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
				.setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(seeingCol);
		
		ColumnModel hearingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthHearing)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthHearing
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
				.setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(hearingCol);
		
		ColumnModel communicatingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthCommunicating)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthCommunicating
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
				.setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(communicatingCol);
		
		ColumnModel walkingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthWalking)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthWalking
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
				.setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(walkingCol);
		
		ColumnModel rememberingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthRemembering)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthRemembering
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
				.setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(rememberingCol);
		
		ColumnModel selfcareCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSelfcare)
				, I_ZZAssessorPerson.COLUMNNAME_ZZHealthSelfcare
				, MasterUtil.getHealthFunctions()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
				.setDefaultValue(healthFunctionDefault, healthFunctionNameCompare)
				.required();
		cols.add(selfcareCol);
		
		TableModel tmHealthFunctions = TableModel.getTableBean(TableModel.class, cols, false, I_ZZAssessorPerson.Table_Name);
		tmHealthFunctions.setSclass("srd-health-function srd-health-function-assessor");
		tmHealthFunctions.setDaoManage(daoManage);
		tmHealthFunctions.init();
		
		NavTabPanel tabPanelHealthFunctions = new NavTabPanel(mainTab);
		tabPanelHealthFunctions.setTabTitle("Health Functions Values");
		tabPanelHealthFunctions.getCompModel().add(tmHealthFunctions);
	}
	
	private void initEducationDetail() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ValueAdaptColumnModel lastSchoolEmisCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				Msg.getElement(Env.getCtx(), "ZZLastSchoolEmis"), 
				I_ZZAssessorPerson.COLUMNNAME_ZZLkpSchoolEmis_ID, 
				CellModel.SEARCH_CELL).setAllowClearText(true);
		lastSchoolEmisCol.required();
		
		lastSchoolEmisCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			
			X_ZZLkpSchoolEmis schoolEmis = (X_ZZLkpSchoolEmis)value;
			return schoolEmis.getName();
		});
		
		lastSchoolEmisCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			
			X_ZZLkpSchoolEmis schoolEmis = (X_ZZLkpSchoolEmis)value;
			return schoolEmis.getZZLkpSchoolEmis_ID();
		});
		
		lastSchoolEmisCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;
			
			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;
			
			return new X_ZZLkpSchoolEmis(Env.getCtx(), id, null);// TODO make a get function to cache
		});
		
		cols.add(lastSchoolEmisCol);
		
		ColumnModel lastSchoolEmisTextCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZLkpSchoolEmisText), 
				I_ZZAssessorPerson.COLUMNNAME_ZZLkpSchoolEmisText
				);
		cols.add(lastSchoolEmisTextCol);
		
		lastSchoolEmisTextCol.setEventHandle((event, cellModel) -> {
			if (cellModel.getValue() != null) {
				CellModel lastSchoolEmisCell = cellModel.getRowModel().get(lastSchoolEmisCol);
				lastSchoolEmisCell.setValue(null);
			}
		});
		
		lastSchoolEmisCol.setBeforeParseInputState(cellModel -> {
			CellModel lastSchoolEmisTextCell = cellModel.getRowModel().get(lastSchoolEmisTextCol);
			cellModel.getColModel().setMandatory(lastSchoolEmisTextCell.getValue() == null);
			
		});
		
		lastSchoolEmisCol.setEventHandle((event, cellModel) -> {
			if (event.getTarget() instanceof Button)
				showInfoPanel(
				InfoPanelPara.getInstance(I_ZZLkpSchoolEmis.Table_Name
						, I_ZZLkpSchoolEmis.COLUMNNAME_ZZLkpSchoolEmis_ID)
				, (obj, infoPanel) -> {
					Object [] objs = (Object [])obj;
					X_ZZLkpSchoolEmis selected = new X_ZZLkpSchoolEmis(Env.getCtx(), (int)objs[0], null);// TODO make a get function to cache
					cellModel.setValue(selected);
					// clear text on lastSchoolEmisTextCol
					cellModel.getRowModel().get(lastSchoolEmisTextCol).setValue(null);
				});
			else if (event.getTarget() instanceof Textbox){
				// clear text so set selected item to null
				Textbox textField = (Textbox)event.getTarget();
				if(StringUtils.isBlank(textField.getValue()))
					cellModel.setValue(null);
			}
		});
		
		ColumnModel lastSchoolYearCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZLastSchoolYear), 
				I_ZZAssessorPerson.COLUMNNAME_ZZLastSchoolYear
				).required();
		cols.add(lastSchoolYearCol);
		
		ValueAdaptColumnModel areaCodeCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZLkpStatssaAreaCode_ID),
				I_ZZAssessorPerson.COLUMNNAME_ZZLkpStatssaAreaCode_ID, 
				CellModel.SEARCH_CELL);
		areaCodeCol.required();
		
		areaCodeCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			InfoPanelPara.getInstance(X_ZZLkpStatssaAreaCode.Table_Name
					, X_ZZLkpStatssaAreaCode.COLUMNNAME_ZZLkpStatssaAreaCode_ID)
			, (obj, infoPanel) -> {
				Object [] objs = (Object [])obj;
				X_ZZLkpStatssaAreaCode selected = new X_ZZLkpStatssaAreaCode(Env.getCtx(), (int)objs[0], null);// TODO make a get function to cache
				cellModel.setValue(selected);
			});
		});
		
		areaCodeCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			X_ZZLkpStatssaAreaCode schoolEmis = (X_ZZLkpStatssaAreaCode)value;
			return schoolEmis.getName();
		});
		
		areaCodeCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			
			X_ZZLkpStatssaAreaCode statssaAreaCode = (X_ZZLkpStatssaAreaCode)value;
			return statssaAreaCode.getZZLkpStatssaAreaCode_ID();
		});
		
		areaCodeCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;
			
			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;
			
			return new X_ZZLkpStatssaAreaCode(Env.getCtx(), id, null);// TODO make a get function to cache
		});
		
		cols.add(areaCodeCol);
		
		ColumnModel popiActStatusCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZPopiActStatus)
				, I_ZZAssessorPerson.COLUMNNAME_ZZPopiActStatus
				, MasterUtil.getPopiActStatus()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required();
		cols.add(popiActStatusCol);
		
		ColumnModel popiActStatusDateCol = DateCellModel.getDateColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZPopiActStatusDate)
				, I_ZZAssessorPerson.COLUMNNAME_ZZPopiActStatusDate
				).required()
				;
				//.setDefaultValue(Timestamp.valueOf(LocalDateTime.now()));
		cols.add(popiActStatusDateCol);
		
		TableModel tmEducationDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZAssessorPerson.Table_Name);
		tmEducationDetail.setSclass("srd-education-detail srd-education-detail-assessor");
		tmEducationDetail.setDaoManage(daoManage);
		tmEducationDetail.init();
		
		NavTabPanel tabPanelEducationDetail = new NavTabPanel(mainTab);
		tabPanelEducationDetail.setTabTitle("Education Details");
		tabPanelEducationDetail.getCompModel().add(tmEducationDetail);
	}
	
	private String getQuaInfoWhere() {
		
		String filterByBp = null;
		if (!isExtensionScope()) {
			filterByBp = String.format("""
					C_BPartner_ID = %s
					and EndDate is not null
					and EndDate >= CURRENT_DATE::TIMESTAMP
					""", sdpAdmin.getC_BPartner_ID());			
		}else if(isExtensionScope()) {
			String selectedQctoQua = selectedQuaIDs.getKey().stream()
                    .map(bd -> String.valueOf(bd.intValueExact()))
                    .collect(Collectors.joining(", "));
			
			String selectedQua = selectedQuaIDs.getValue().stream()
                    .map(bd -> String.valueOf(bd.intValueExact()))
                    .collect(Collectors.joining(", "));
			
			filterByBp = String.format("""
					C_BPartner_ID = %s
					and EndDate is not null
					and EndDate >= CURRENT_DATE::TIMESTAMP
					AND (
						(ZZFromQcto = 'Y' AND ZZQualification_v_ID NOT IN (%s))
						OR (ZZFromQcto = 'N' AND ZZQualification_v_ID NOT IN (%s))
					)
					""", sdpAdmin.getC_BPartner_ID()
					, selectedQctoQua
					, selectedQua);
		}
		
		return filterByBp;
	}
	
	private String getSkillsInfoWhere() {
		String filterByBp = null;
		if (!isExtensionScope()) {
			filterByBp = String.format("""
					C_BPartner_ID = %s
					and EndDate is not null
					and EndDate >= CURRENT_DATE::TIMESTAMP
					""", sdpAdmin.getC_BPartner_ID());			
		}else if(isExtensionScope()) {
			String selectedQctoSkills = selectedSkillsIDs.getKey().stream()
                    .map(bd -> String.valueOf(bd.intValueExact()))
                    .collect(Collectors.joining(", "));
			
			String selectedSkills = selectedSkillsIDs.getValue().stream()
                    .map(bd -> String.valueOf(bd.intValueExact()))
                    .collect(Collectors.joining(", "));
			
			filterByBp = String.format("""
					C_BPartner_ID = %s
					and EndDate is not null
					and EndDate >= CURRENT_DATE::TIMESTAMP
					AND (
						(ZZFromQcto = 'Y' AND ZZSkillsProgramme_v_ID NOT IN (%s))
						OR (ZZFromQcto = 'N' AND ZZSkillsProgramme_v_ID NOT IN (%s))
					)
					""", sdpAdmin.getC_BPartner_ID()
					, selectedQctoSkills
					, selectedSkills);
		}
		
		
		
		return filterByBp;
	}
	TableModel tmQualificationLink;
	private void initQualification() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ValueAdaptColumnModel chooseQualificationCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				null, 
				null, 
				CellModel.SEARCH_CELL);
		chooseQualificationCol.setShowTitle(false);
		cols.add(chooseQualificationCol);
		
		TableModel tmQualificationComp = TableModel.getTableBean(TableModel.class, cols, false, null);
		tmQualificationComp.setSclass("srd-qualification-scope-comp srd-qualification-scope-comp-assessor");
		tmQualificationComp.init();
		
		NavTabPanel tabPanelQualificationScope = new NavTabPanel(mainTab);
		tabPanelQualificationScope.setTabTitle("Qualification Scope");
		tabPanelQualificationScope.getCompModel().add(tmQualificationComp);
		
		cols = new ArrayList<>();
		
		ColumnModel qualificationCodeCol = CellModel.getColModelForLabel(
					Msg.getElement(Env.getCtx(), I_ZZQualification_v.COLUMNNAME_ZZSaqaQualificationCode)
					, I_ZZQualification_v.COLUMNNAME_ZZSaqaQualificationCode)
				.setReadonly(true)
				.setTableName(I_ZZQualification_v.Table_Name);
		cols.add(qualificationCodeCol);
		
		ColumnModel qualificationTitleCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), I_ZZQualification_v.COLUMNNAME_ZZSaqaQualificationTitle)
				, I_ZZQualification_v.COLUMNNAME_ZZSaqaQualificationTitle)
			.setReadonly(true)
			.setTableName(I_ZZQualification_v.Table_Name);
		cols.add(qualificationTitleCol);
		
		ColumnModel qualificationCreditsCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQualification_v.Table_Name, I_ZZQualification_v.COLUMNNAME_ZZCredits)
				, I_ZZQualification_v.COLUMNNAME_ZZCredits)
			.setReadonly(true)
			.setTableName(I_ZZQctoQualification.Table_Name);
		cols.add(qualificationCreditsCol);
		
		ColumnModel registrationStartDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQualification_v.Table_Name, I_ZZQualification_v.COLUMNNAME_Registrationstartdate)
				, I_ZZQualification_v.COLUMNNAME_Registrationstartdate)
			.setReadonly(true)
			.setTableName(I_ZZQctoQualification.Table_Name);
		cols.add(registrationStartDateCol);
		
		ColumnModel registrationEndDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQualification_v.Table_Name, I_ZZQualification_v.COLUMNNAME_Registrationenddate)
				, I_ZZQualification_v.COLUMNNAME_Registrationenddate)
			.setReadonly(true)
			.setTableName(I_ZZQualification_v.Table_Name);
		cols.add(registrationEndDateCol);
					
		tmQualificationLink = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLinkAssessorQualification.Table_Name);
		tmQualificationLink.setViewModel(ViewType.VIEW_GRID);
		tmQualificationLink.setSclass("srd-qualification-scope srd-qualification-scope-assessor");
		tmQualificationLink.setCommandSetting(CommandSetting.getNonAddButton());
		tmQualificationLink.setCreateNewRowWhenEmpty(false);
		tmQualificationLink.init();
		
		tabPanelQualificationScope.getCompModel().add(tmQualificationLink);
		
		chooseQualificationCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
					
			InfoPanelPara.getInstance(I_ZZQualification_v.Table_Name
					, I_ZZQualification_v.COLUMNNAME_ZZQualification_v_UU).setMultiChoose(true).setWhereClause(getQuaInfoWhere())
			, (obj, infoPanel) -> {
				int colIndexZZFromQcto = -1;
				int colIndexScopeId = -1;
				
				for (int colInfoIndex = 0; colInfoIndex < infoPanel.getContentPanel().getLayout().length; colInfoIndex++) {
					ColumnInfo colInfo = infoPanel.getContentPanel().getLayout()[colInfoIndex];
					if (I_ZZQualification_v.COLUMNNAME_ZZFromQcto.equals(colInfo.getColumnName())) {
						colIndexZZFromQcto = colInfoIndex;
					}else if (I_ZZQualification_v.COLUMNNAME_ZZQualification_v_ID.equals(colInfo.getColumnName())) {
						colIndexScopeId = colInfoIndex;
					}
				}
				
				List<Object> scopeSelectedIDs = new ArrayList<Object>();
				List<Object> qctoScopeSelectedIDs = new ArrayList<Object>();
				
				for (List<Object> selectedRow : infoPanel.getSelectedRowInfo().values()) {
					ValueNamePair qctoValue = (ValueNamePair)selectedRow.get(colIndexZZFromQcto);
					if (X_ZZQualification_v.ZZFROMQCTO_Yes.equals(qctoValue.getValue())){
						qctoScopeSelectedIDs.add(selectedRow.get(colIndexScopeId));
					}else {
						scopeSelectedIDs.add(selectedRow.get(colIndexScopeId));
					}
				}
				
				if (scopeSelectedIDs.size() == 0) {
					scopeSelectedIDs.add(0);
				}
				
				if (qctoScopeSelectedIDs.size() == 0) {
					qctoScopeSelectedIDs.add(0);
				}
				
				List<Object> qctoScopeExcludeIds = new ArrayList<Object>();
				List<Object> scopeExcludeIds = new ArrayList<Object>();
				
				tmQualificationLink.getRows().forEach(rowModel -> {
					X_ZZQualification_v scopeViewPo = (X_ZZQualification_v)rowModel.getRowData().getDataNullable(I_ZZQualification_v.Table_Name);
					if (X_ZZQualification_v.ZZFROMQCTO_Yes.equals(scopeViewPo.getZZFromQcto())) {
						qctoScopeExcludeIds.add(scopeViewPo.getZZQualification_v_ID());
					}else {
						scopeExcludeIds.add(scopeViewPo.getZZQualification_ID());
					}
				});
				
				
				// build include selected ids
				String qctoScopePlaceholders = MasterUtil.createPlaceHoldForInClause(qctoScopeSelectedIDs);
				String qctoScopeExcludePlaceholde = MasterUtil.createPlaceHoldForInClause(qctoScopeExcludeIds);
				
				String scopePlaceholders = MasterUtil.createPlaceHoldForInClause(scopeSelectedIDs);
				String scopeExcludePlaceholde = MasterUtil.createPlaceHoldForInClause(scopeExcludeIds);
				
				Query scopeQuery = MTable.get(Env.getCtx(), I_ZZQualification_v.Table_ID)
						.createQuery(String.format("""
								(ZZFromQcto = 'Y' AND ZZQualification_v_ID IN (%s) AND ZZQualification_v_ID NOT IN (%s))
								OR (ZZFromQcto = 'N' AND ZZQualification_v_ID IN (%s) AND ZZQualification_v_ID NOT IN (%s))
								""" 
								, qctoScopePlaceholders
								, qctoScopeExcludePlaceholde
								, scopePlaceholders
								, scopeExcludePlaceholde), null);
				
				qctoScopeSelectedIDs.addAll(qctoScopeExcludeIds);
				qctoScopeSelectedIDs.addAll(scopeSelectedIDs);
				qctoScopeSelectedIDs.addAll(scopeExcludeIds);
				
				scopeQuery.setParameters(qctoScopeSelectedIDs);
				
				// convert to list of list
				List<PO> selectedQualifications = scopeQuery.list();
				List<List<PO>> daos = RowData.standardToMultiPo(selectedQualifications);
				tmQualificationLink.addNewRows(daos);
				
			});
		});
		
		tmQualificationLink.setAfterSave((rowDbEventArgs) -> {
			if (!rowDbEventArgs.isRowEven())
				return true;
			
			PO po = rowDbEventArgs.row().getRowData().getDataNewWhenNull(I_ZZLinkAssessorQualification.Table_Name);
			
			X_ZZLinkAssessorQualification linkPO = X_ZZLinkAssessorQualification.class.cast(po);
			linkPO.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
			
			X_ZZQualification_v scopePO = (X_ZZQualification_v)rowDbEventArgs.row().getRowData().getDataNullable(I_ZZQualification_v.Table_Name);
			
			if (scopePO != null && X_ZZQualification_v.ZZFROMQCTO_Yes.equals(scopePO.getZZFromQcto())) {
				linkPO.setZZQctoQualification_ID(scopePO.getZZQualification_v_ID());
				linkPO.setZZQualification_ID(0);
			}else if (scopePO != null && X_ZZQualification_v.ZZFROMQCTO_No.equals(scopePO.getZZFromQcto())) {
				linkPO.setZZQualification_ID(scopePO.getZZQualification_v_ID());
				linkPO.setZZQctoQualification_ID(0);
			}

			linkPO.saveEx(assessorPerson.get_TrxName());
			
			return true;
		});
		
		tmQualificationLink.setLoadSavedDataHandle(tableModel -> {
			if (assessorPerson == null && assessorPersonParent == null)
				return;
			
			String where =" (ass.zz_docstatus = 'DR' or ass.zz_docstatus is null or (ass.zz_docstatus = 'AP' and ZZLinkAssessorQualification.zz_isrecommended = 'Y')) AND " + assessorPersonCondition;
				
			String	orderBy = commonOrderBy + ", ZZLinkAssessorQualification.ZZLinkAssessorQualification_id";
			
			Query linkAssessorQuery = MTable.get(Env.getCtx(), I_ZZLinkAssessorQualification.Table_Name)
			 		.createQuery(where, null);
			
			linkAssessorQuery.addJoinClause("""
					join ZZAssessorPerson ass on (ass.zzassessorperson_id = ZZLinkAssessorQualification.zzassessorperson_id)
				""");
			linkAssessorQuery.setOrderBy(orderBy);
			
			if(assessorPerson != null) {
				linkAssessorQuery.setParameters(assessorPerson.getZZAssessorPerson_ID(), assessorPerson.getZZAssessorPerson_ID());
			}else if (assessorPersonParent != null){
				linkAssessorQuery.setParameters(assessorPersonParent.getZZAssessorPerson_ID(), assessorPersonParent.getZZAssessorPerson_ID());
			}
			
			List<PO> linkObjs = linkAssessorQuery.list();
			
			Query scopeQuery = MTable.get(Env.getCtx(), I_ZZQualification_v.Table_Name)
			 		.createQuery(where, null);
			scopeQuery.setOrderBy(orderBy);
			
			scopeQuery.addJoinClause("""
					join ZZLinkAssessorQualification on 
						(
						(ZZFromQcto = 'Y' AND ZZQualification_v.zzqualification_v_id = ZZLinkAssessorQualification.zzqctoqualification_id)
						OR (ZZFromQcto = 'N' AND ZZQualification_v.zzqualification_v_id = ZZLinkAssessorQualification.zzqualification_id)
						)
					""");
			
			scopeQuery.addJoinClause("""
						join ZZAssessorPerson ass on (ass.zzassessorperson_id = ZZLinkAssessorQualification.zzassessorperson_id)
					""");
		
			if(assessorPerson != null) {
				scopeQuery.setParameters(assessorPerson.getZZAssessorPerson_ID(), assessorPerson.getZZAssessorPerson_ID());
			}else if (assessorPersonParent != null){
				scopeQuery.setParameters(assessorPersonParent.getZZAssessorPerson_ID(), assessorPersonParent.getZZAssessorPerson_ID());
			}
			
			
			List<PO> scopeObjs = scopeQuery.list();
			
			List<List<PO>> savedObjs = RowData.mergedList(linkObjs, scopeObjs, (link, scope) -> {
				X_ZZLinkAssessorQualification linkPo = (X_ZZLinkAssessorQualification)link;
				X_ZZQualification_v scopePO = (X_ZZQualification_v)scope;
				if (X_ZZQualification_v.ZZFROMQCTO_Yes.equals(scopePO.getZZFromQcto()) && linkPo.getZZQctoQualification_ID() == scopePO.getZZQualification_v_ID()) {
					return true;
				}else if (X_ZZQualification_v.ZZFROMQCTO_No.equals(scopePO.getZZFromQcto()) && linkPo.getZZQualification_ID() == scopePO.getZZQualification_v_ID()) {
					return true;
				}
				return false;
			});
			
			tmQualificationLink.resetMultiPo(savedObjs);
			
			 
		});
		
		tmQualificationLink.setAfterDelete((trxName, rowModel) -> {
			rowModel.getRowData().getDataNullable(I_ZZLinkAssessorQualification.Table_Name).deleteEx(true, trxName);
			return true;
		});
		
		tmQualificationLink.setRowReadonlyLogic(new Function<RowModel, Boolean>() {
			
			@Override
			public Boolean apply(RowModel rowModel) {
				if (assessorPerson == null && assessorPersonParent == null) {
					return false;
				}
				
				X_ZZLinkAssessorQualification linkQua = (X_ZZLinkAssessorQualification)rowModel.getRowData().getDataNullable(I_ZZLinkAssessorQualification.Table_Name);
				
				if (linkQua == null)
					return false;
				
				int currentAssessorId = 0;
				if (assessorPerson != null)
					currentAssessorId = assessorPerson.getZZAssessorPerson_ID();
				
				return linkQua.getZZAssessorPerson_ID() > 0 && linkQua.getZZAssessorPerson_ID() != currentAssessorId;
			}
		});
		
	}
	private TableModel tmQctoSkillsProgramme;
	private String assessorTreeQuery = """
			\sSELECT cohort.ZZAssessorPerson_ID
			 FROM ZZAssessorPerson input_person
    
			 	INNER JOIN ZZAssessorPerson cohort 
			   	ON (
			       (input_person.parent_id IS NOT NULL 
			        AND (cohort.parent_id = input_person.parent_id OR cohort.ZZAssessorPerson_ID = input_person.parent_id))
			       OR
			       (input_person.parent_id IS NULL 
			        AND (cohort.parent_id = input_person.ZZAssessorPerson_ID OR cohort.ZZAssessorPerson_ID = input_person.ZZAssessorPerson_ID))
			   )\s""";
	private String assessorPersonCondition = "\sass.ZZAssessorPerson_ID IN (" + assessorTreeQuery + 
			" WHERE input_person.ZZAssessorPerson_ID = ?)";
	
	String	commonOrderBy = """
			\sCASE WHEN ass.ZZAssessorPerson_ID = ? THEN 0 ELSE 1 end DESC
			, ass.ZZAssessorPerson_ID\s
			""";
	
	private void initSkillsProgramme() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ValueAdaptColumnModel chooseSkillsProgrammeCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				null,
				null, 
				CellModel.SEARCH_CELL);
		chooseSkillsProgrammeCol.setShowTitle(false);
		cols.add(chooseSkillsProgrammeCol);
		
		TableModel tmQualificationComp = TableModel.getTableBean(TableModel.class, cols, false, null);
		tmQualificationComp.setSclass("srd-skillsprogramme-scope-comp srd-skillsprogramme-scope-comp-assessor");
		tmQualificationComp.init();
		
		NavTabPanel tabPanelSkillsProgramme = new NavTabPanel(mainTab);
		tabPanelSkillsProgramme.setTabTitle("Skills Programme Scope");
		tabPanelSkillsProgramme.getCompModel().add(tmQualificationComp);
		
		cols = new ArrayList<>();
		
		ColumnModel skillsProgrammeCodeCol = CellModel.getColModelForLabel(
					Msg.getElement(Env.getCtx(), I_ZZSkillsProgramme_v.COLUMNNAME_ZZSkillsProgrammeCode)
					, I_ZZSkillsProgramme_v.COLUMNNAME_ZZSkillsProgrammeCode)
				.setReadonly(true)
				.setTableName(I_ZZSkillsProgramme_v.Table_Name);
		cols.add(skillsProgrammeCodeCol);
		
		ColumnModel skillsProgrammeTitleCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), I_ZZSkillsProgramme_v.COLUMNNAME_ZZSkillsProgrammeTitle)
				, I_ZZSkillsProgramme_v.COLUMNNAME_ZZSkillsProgrammeTitle)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme_v.Table_Name);
		cols.add(skillsProgrammeTitleCol);
		
		ColumnModel skillsProgrammeCreditsCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZSkillsProgramme_v.Table_Name, I_ZZSkillsProgramme_v.COLUMNNAME_ZZCredits)
				, I_ZZSkillsProgramme_v.COLUMNNAME_ZZCredits)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme_v.Table_Name);
		cols.add(skillsProgrammeCreditsCol);
		
		ColumnModel registrationStartDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQctoSkillsProgramme.Table_Name, I_ZZQctoSkillsProgramme.COLUMNNAME_Registrationstartdate)
				, I_ZZQctoSkillsProgramme.COLUMNNAME_Registrationstartdate)
			.setReadonly(true)
			.setTableName(I_ZZQctoSkillsProgramme.Table_Name);
		cols.add(registrationStartDateCol);
		
		ColumnModel registrationEndDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZSkillsProgramme_v.Table_Name, I_ZZSkillsProgramme_v.COLUMNNAME_Registrationenddate)
				, I_ZZSkillsProgramme_v.COLUMNNAME_Registrationenddate)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme_v.Table_Name);
		cols.add(registrationEndDateCol);
					
		tmQctoSkillsProgramme = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLinkAssessorSkillsProgramme.Table_Name);
		tmQctoSkillsProgramme.setViewModel(ViewType.VIEW_GRID);
		tmQctoSkillsProgramme.setSclass("srd-qualification-scope srd-qualification-scope-assessor");
		tmQctoSkillsProgramme.setCommandSetting(CommandSetting.getNonAddButton());
		tmQctoSkillsProgramme.setCreateNewRowWhenEmpty(false);
		tmQctoSkillsProgramme.init();
		
		tabPanelSkillsProgramme.getCompModel().add(tmQctoSkillsProgramme);
		
		chooseSkillsProgrammeCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			InfoPanelPara.getInstance(I_ZZSkillsProgramme_v.Table_Name
					, I_ZZSkillsProgramme_v.COLUMNNAME_ZZSkillsProgramme_v_UU).setMultiChoose(true).setWhereClause(getSkillsInfoWhere())
			,(obj, infoPanel) -> {
				
				int colIndexZZFromQcto = -1;
				int colIndexScopeId = -1;
				
				for (int colInfoIndex = 0; colInfoIndex < infoPanel.getContentPanel().getLayout().length; colInfoIndex++) {
					ColumnInfo colInfo = infoPanel.getContentPanel().getLayout()[colInfoIndex];
					if (I_ZZSkillsProgramme_v.COLUMNNAME_ZZFromQcto.equals(colInfo.getColumnName())) {
						colIndexZZFromQcto = colInfoIndex;
					}else if (I_ZZSkillsProgramme_v.COLUMNNAME_ZZSkillsProgramme_v_ID.equals(colInfo.getColumnName())) {
						colIndexScopeId = colInfoIndex;
					}
				}
				
				List<Object> scopeSelectedIDs = new ArrayList<Object>();
				List<Object> qctoScopeSelectedIDs = new ArrayList<Object>();
				
				for (List<Object> selectedRow : infoPanel.getSelectedRowInfo().values()) {
					ValueNamePair qctoValue = (ValueNamePair)selectedRow.get(colIndexZZFromQcto);
					if (X_ZZSkillsProgramme_v.ZZFROMQCTO_Yes.equals(qctoValue.getValue())){
						qctoScopeSelectedIDs.add(selectedRow.get(colIndexScopeId));
					}else {
						scopeSelectedIDs.add(selectedRow.get(colIndexScopeId));
					}
				}
				
				if (scopeSelectedIDs.size() == 0) {
					scopeSelectedIDs.add(0);
				}
				
				if (qctoScopeSelectedIDs.size() == 0) {
					qctoScopeSelectedIDs.add(0);
				}
				
				List<Object> qctoScopeExcludeIds = new ArrayList<Object>();
				List<Object> scopeExcludeIds = new ArrayList<Object>();
				
				tmQctoSkillsProgramme.getRows().forEach(rowModel -> {
					X_ZZSkillsProgramme_v scopeViewPo = (X_ZZSkillsProgramme_v)rowModel.getRowData().getDataNullable(X_ZZSkillsProgramme_v.Table_Name);
					if (X_ZZSkillsProgramme_v.ZZFROMQCTO_Yes.equals(scopeViewPo.getZZFromQcto())) {
						qctoScopeExcludeIds.add(scopeViewPo.getZZSkillsProgramme_v_ID());
					}else {
						scopeExcludeIds.add(scopeViewPo.getZZSkillsProgramme_v_ID());
					}
				});
				
				
				// build include selected ids
				String qctoScopePlaceholders = MasterUtil.createPlaceHoldForInClause(qctoScopeSelectedIDs);
				String qctoScopeExcludePlaceholde = MasterUtil.createPlaceHoldForInClause(qctoScopeExcludeIds);
				
				String scopePlaceholders = MasterUtil.createPlaceHoldForInClause(scopeSelectedIDs);
				String scopeExcludePlaceholde = MasterUtil.createPlaceHoldForInClause(scopeExcludeIds);
				
				Query scopeQuery = MTable.get(Env.getCtx(), I_ZZSkillsProgramme_v.Table_ID)
						.createQuery(String.format("""
								(ZZFromQcto = 'Y' AND ZZSkillsProgramme_v_ID IN (%s) AND ZZSkillsProgramme_v_ID NOT IN (%s))
								OR (ZZFromQcto = 'N' AND ZZSkillsProgramme_v_ID IN (%s) AND ZZSkillsProgramme_v_ID NOT IN (%s))
								""" 
								, qctoScopePlaceholders
								, qctoScopeExcludePlaceholde
								, scopePlaceholders
								, scopeExcludePlaceholde), null);
				
				qctoScopeSelectedIDs.addAll(qctoScopeExcludeIds);
				qctoScopeSelectedIDs.addAll(scopeSelectedIDs);
				qctoScopeSelectedIDs.addAll(scopeExcludeIds);
				
				scopeQuery.setParameters(qctoScopeSelectedIDs);
				
				// convert to list of list
				List<PO> selectedQualifications = scopeQuery.list();
				List<List<PO>> daos = RowData.standardToMultiPo(selectedQualifications);
				tmQctoSkillsProgramme.addNewRows(daos);
			});
		});
		
		tmQctoSkillsProgramme.setAfterSave((rowDbEventArgs) -> {
			if (!rowDbEventArgs.isRowEven())
				return true;
			
			PO po = rowDbEventArgs.row().getRowData().getDataNewWhenNull(I_ZZLinkAssessorSkillsProgramme.Table_Name);
			
			X_ZZLinkAssessorSkillsProgramme linkPO = X_ZZLinkAssessorSkillsProgramme.class.cast(po);
			linkPO.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
			
			X_ZZSkillsProgramme_v scopePO = (X_ZZSkillsProgramme_v)rowDbEventArgs.row().getRowData().getDataNullable(I_ZZSkillsProgramme_v.Table_Name);
			
			if (scopePO != null && X_ZZSkillsProgramme_v.ZZFROMQCTO_Yes.equals(scopePO.getZZFromQcto())) {
				linkPO.setZZQctoSkillsProgramme_ID(scopePO.getZZSkillsProgramme_v_ID());
				linkPO.setZZSkillsProgramme_ID(0);
			}else if (scopePO != null && X_ZZSkillsProgramme_v.ZZFROMQCTO_No.equals(scopePO.getZZFromQcto())) {
				linkPO.setZZSkillsProgramme_ID(scopePO.getZZSkillsProgramme_v_ID());
				linkPO.setZZQctoSkillsProgramme_ID(0);
			}
			
			linkPO.saveEx(assessorPerson.get_TrxName());
			
			return true;
		});
		
		tmQctoSkillsProgramme.setLoadSavedDataHandle(tableModel -> {
			if (assessorPerson == null && assessorPersonParent == null)
				return;
			
			String where = " (ass.zz_docstatus = 'DR' or ass.zz_docstatus is null or (ass.zz_docstatus = 'AP' and ZZLinkAssessorSkillsProgramme.zz_isrecommended = 'Y')) AND " + assessorPersonCondition;
			
			String	orderBy = commonOrderBy + ", ZZLinkAssessorSkillsProgramme.ZZLinkAssessorSkillsProgramme_id";
					
			Query linkAssessorQuery = MTable.get(Env.getCtx(), I_ZZLinkAssessorSkillsProgramme.Table_Name)
			 		.createQuery(where, null);
			
			linkAssessorQuery.addJoinClause("""
					join ZZAssessorPerson ass on (ass.zzassessorperson_id = ZZLinkAssessorSkillsProgramme.zzassessorperson_id)
				""");
			
			linkAssessorQuery.setOrderBy(orderBy);
			
			if(assessorPerson != null) {
				linkAssessorQuery.setParameters(assessorPerson.getZZAssessorPerson_ID(), assessorPerson.getZZAssessorPerson_ID());
			}else if (assessorPersonParent != null){
				linkAssessorQuery.setParameters(assessorPersonParent.getZZAssessorPerson_ID(), assessorPersonParent.getZZAssessorPerson_ID());
			}
			
			List<PO> linkObjs = linkAssessorQuery.list();
			
			Query scopeQuery = MTable.get(Env.getCtx(), I_ZZSkillsProgramme_v.Table_Name)
			 		.createQuery(where, null);
			scopeQuery.setOrderBy(orderBy);
			
			
			scopeQuery.addJoinClause("""
					join ZZLinkAssessorSkillsProgramme on 
						(
						(ZZFromQcto = 'Y' AND ZZSkillsProgramme_v.ZZSkillsProgramme_v_id = ZZLinkAssessorSkillsProgramme.ZZQctoSkillsProgramme_ID)
						OR (ZZFromQcto = 'N' AND ZZSkillsProgramme_v.ZZSkillsProgramme_v_id = ZZLinkAssessorSkillsProgramme.ZZSkillsProgramme_ID)
						)
					""");
		
			scopeQuery.addJoinClause("""
					join ZZAssessorPerson ass on (ass.zzassessorperson_id = ZZLinkAssessorSkillsProgramme.zzassessorperson_id)
				""");
			
			if(assessorPerson != null) {
				scopeQuery.setParameters(assessorPerson.getZZAssessorPerson_ID(), assessorPerson.getZZAssessorPerson_ID());
			}else if (assessorPersonParent != null){
				scopeQuery.setParameters(assessorPersonParent.getZZAssessorPerson_ID(), assessorPersonParent.getZZAssessorPerson_ID());
			}
			
			
			List<PO> scopeObjs = scopeQuery.list();
			
			List<List<PO>> savedObjs = RowData.mergedList(linkObjs, scopeObjs, (link, scope) -> {
				X_ZZLinkAssessorSkillsProgramme linkPo = (X_ZZLinkAssessorSkillsProgramme)link;
				X_ZZSkillsProgramme_v scopePO = (X_ZZSkillsProgramme_v)scope;
				if (X_ZZSkillsProgramme_v.ZZFROMQCTO_Yes.equals(scopePO.getZZFromQcto()) && linkPo.getZZQctoSkillsProgramme_ID() == scopePO.getZZSkillsProgramme_v_ID()) {
					return true;
				}else if (X_ZZSkillsProgramme_v.ZZFROMQCTO_No.equals(scopePO.getZZFromQcto()) && linkPo.getZZSkillsProgramme_ID() == scopePO.getZZSkillsProgramme_v_ID()) {
					return true;
				}
				return false;
			});
			
			tmQctoSkillsProgramme.resetMultiPo(savedObjs);
			 
		});
		
		tmQctoSkillsProgramme.setAfterDelete((trxName, rowModel) -> {
			rowModel.getRowData().getDataNullable(I_ZZLinkAssessorSkillsProgramme.Table_Name).deleteEx(true, trxName);
			return true;
		});
		
		
		tmQctoSkillsProgramme.setRowReadonlyLogic(new Function<RowModel, Boolean>() {
			
			@Override
			public Boolean apply(RowModel rowModel) {
				if (assessorPerson == null && assessorPersonParent == null) {
					return false;
				}
				
				X_ZZLinkAssessorSkillsProgramme linkQua = (X_ZZLinkAssessorSkillsProgramme)rowModel.getRowData().getDataNullable(I_ZZLinkAssessorSkillsProgramme.Table_Name);
				
				if (linkQua == null)
					return false;
				
				int currentAssessorId = 0;
				if (assessorPerson != null)
					currentAssessorId = assessorPerson.getZZAssessorPerson_ID();
				
				return linkQua.getZZAssessorPerson_ID() > 0 && linkQua.getZZAssessorPerson_ID() != currentAssessorId;
			}
		});
	}
	
	
	private void initAddresss() { 
		TableModel tmPostalAddress = BuildFormUtil.getAddressDetailComp(
			  SettingTableMode.getSimple("Postal"), 
			  SettingAddress.getSimple("Postal"));
  
		TableModel tmPhysicalAddress = BuildFormUtil.getAddressDetailComp(
				SettingTableMode.getSimple("Physical"), 
				SettingAddress.getSimple("Physical", tmPostalAddress));
  
		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("sdr-address sdr-address-assessor");
		addressDetailTab.setTabTitle("Address Details");
	  
		addressDetailTab.getCompModel().add(tmPhysicalAddress);
		addressDetailTab.getCompModel().add(tmPostalAddress);
	  
		tmPhysicalAddress.setAfterAppSave((tableModel, trxName) -> {
			TableModel tmAddress = (TableModel)tableModel;
			X_C_Location location = tmAddress.getRow().getDataOneRow(X_C_Location.class, I_C_Location.Table_Name);
			if (location != null) {
				assessorPerson.setZZPhysicalLocation_ID(location.getC_Location_ID());
				assessorPerson.saveEx(trxName);
			}
			
			return true;
		});
	  
		tmPostalAddress.setAfterAppSave((tableModel, trxName) -> {
			TableModel tmAddress = (TableModel)tableModel;
			X_C_Location location = tmAddress.getRow().getDataOneRow(X_C_Location.class, I_C_Location.Table_Name);
			if (location != null) {
				assessorPerson.setZZPostalLocation_ID(location.getC_Location_ID());
				assessorPerson.saveEx(trxName);
			}
			
			return true;
		});
	  
		tmPhysicalAddress.setLoadSavedDataHandle(tm -> {
			if (assessorPerson != null && assessorPerson.getZZPhysicalLocation_ID() > 0) {
				X_C_Location physicalLocation = MLocation.getCopy(Env.getCtx(), assessorPerson.getZZPhysicalLocation_ID(), null);
				tm.getRow().setDataOneRow(physicalLocation);
			  
			}else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});
	  
		tmPostalAddress.setLoadSavedDataHandle(tm -> {
			if (assessorPerson != null && assessorPerson.getZZPostalLocation_ID() > 0) {
				X_C_Location postalLocation = MLocation.getCopy(Env.getCtx(), assessorPerson.getZZPostalLocation_ID(), null);
				tm.getRow().setDataOneRow(postalLocation);
			}else {
				tm.getRow().setDataOneRow(null);
			}
			tm.reloadDao();
		});
	}
	 
	
	private void initUploadDocument() {
		 Query docUpQuery = MTable.get(Env.getCtx(), I_ZZDocumentUpload.Table_Name)
				.createQuery(String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_AD_Form_ID), null);
		docUpQuery.addTableDirectJoin(I_ZZ_Program_Master_Data.Table_Name);
		List<X_ZZDocumentUpload> docUploads	= docUpQuery.setParameters(getMenuContextInfo().getFormId()).list();
		
		TableModel tmUploadDocInfo = initUploadTab(mainTab, "Upload Document", docUploads);
		
		if (tmUploadDocInfo != null) {
			tmUploadDocInfo.setBeforeSave((rowDbEventArgs) -> {
				if (!rowDbEventArgs.isPOEven())
					return true;
				
				X_ZZDocumentUploadFile documentUploadFile = (X_ZZDocumentUploadFile)rowDbEventArgs.po();
				if (documentUploadFile.getZZAssessorPerson_ID() == 0)
					documentUploadFile.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
				
				return true;
			});
			
			tmUploadDocInfo.setLoadSavedDataHandle(tableModel -> {
				if (assessorPerson != null) {
					Query uploadDocQuery = MTable.get(Env.getCtx(), X_ZZDocumentUploadFile.Table_Name).createQuery(
				            String.format("%s = ?", X_ZZAssessorPerson.COLUMNNAME_ZZAssessorPerson_ID), null
				        );

					List<PO> documentUploadFiles = uploadDocQuery
			            .setOrderBy(X_ZZDocumentUploadFile.COLUMNNAME_ZZDocumentUploadFile_ID)
			            .setParameters(assessorPerson.getZZAssessorPerson_ID())
			            .list();
			        
					tableModel.resetMultiPo(RowData.standardToMultiPo(documentUploadFiles), tableModel.getTitleInfo());
				}
			});
		}
	}
	  
	public TableModel getTmNames() {
		return tmNames;
	}

	public NavTab getMainTab() {
		return mainTab;
	}

	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}
	

	@Override
	public void doSave(String trxName) {
		if(assessorPerson == null && isExtensionScope()) {
			assessorPerson = initAssessor();
			// this case don't show general detail tab because hasn't table model for assessorPerson
			// need to save it first to get id when save scope
			assessorPerson.saveEx(trxName);
		}
		boolean isDraft = true;
		if (assessorPerson != null) {
			isDraft = assessorPerson.getZZ_DocStatus() == null || X_ZZAssessorPerson.ZZ_DOCSTATUS_Draft.equals(assessorPerson.getZZ_DocStatus());
		}
		
		if (!isDraft) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZAssessorWrongStatus"));
		}
		
		if (isExtensionScope() && isNew) {
			assessorPerson.setParent_ID(assessorPersonParent.getZZAssessorPerson_ID());
		}
		
		super.doSave(trxName);
		
		if (!isExtensionScope() && isNew && assessorPersonExpried != null) {
			assessorPerson.setZZRegistrationType(X_ZZAssessorPerson.ZZREGISTRATIONTYPE_Re_Registration);
		}else if (!isExtensionScope() && isNew && assessorPersonExpried == null){
			assessorPerson.setZZRegistrationType(X_ZZAssessorPerson.ZZREGISTRATIONTYPE_Registration);
		}
		
		assessorPerson.setAD_User_ID(person.getAD_User_ID());
		
		assessorPerson.saveEx(trxName);
	}
	
	@Override
	public void doSubmit(String trxName) {
		if (tmQualificationLink.getRows().size() == 0
				&& tmQctoSkillsProgramme.getRows().size() == 0) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZAssessorMissingLinkQualificationSkillsProgramme"));
		}
		
		assessorPerson.setZZ_DocStatus(X_ZZAssessorPerson.ZZ_DOCSTATUS_Pending);
		assessorPerson.setZZSubmittedDate(Timestamp.from(Instant.now()));
		assessorPerson.saveEx(trxName);
		super.doSubmit(trxName);
	}
	
	@Override
	public boolean isSupportSubmit() {
		return true;
	}
}
