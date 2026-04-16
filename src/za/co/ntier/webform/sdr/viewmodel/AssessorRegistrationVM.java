package za.co.ntier.webform.sdr.viewmodel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.ClientInfo;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.desktop.DefaultDesktop;
import org.adempiere.webui.desktop.WindowRegistry;
import org.adempiere.webui.event.DialogEvents;
import org.adempiere.webui.event.ValueChangeListener;
import org.adempiere.webui.factory.InfoManager;
import org.adempiere.webui.panel.IHelpContext;
import org.adempiere.webui.panel.InfoPanel;
import org.adempiere.webui.part.WindowContainer;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.ZKUpdateUtil;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_AD_CtxHelp;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZAssessorPerson;
import za.co.ntier.api.model.I_ZZLinkAssessorQualification;
import za.co.ntier.api.model.I_ZZLinkAssessorSkillsProgramme;
import za.co.ntier.api.model.I_ZZLkpSchoolEmis;
import za.co.ntier.api.model.I_ZZQualification;
import za.co.ntier.api.model.I_ZZSkillsProgramme;
import za.co.ntier.api.model.I_ZZ_AlternateIDType;
import za.co.ntier.api.model.I_ZZ_Application_Form;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZAssessorPerson;
import za.co.ntier.api.model.X_ZZLinkAssessorQualification;
import za.co.ntier.api.model.X_ZZLkpSchoolEmis;
import za.co.ntier.api.model.X_ZZLkpStatssaAreaCode;
import za.co.ntier.api.model.X_ZZQualification;
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
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.CommandSetting;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public class AssessorRegistrationVM extends BaseAppVM {

	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;
	
	private TableModel tmNames;
	private NavTab mainTab;
	X_ZZAssessorPerson assessorPerson;
	
	DaoManage daoManage = new DaoManage();
	
	@Override
	public Object getMainApp() {
		return null;
	}

	@Override
	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}
	
	@Override
	public List<DaoManage> getDaoManages() {
		return List.of(daoManage);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab, tmNames);
	}

	@Override
	protected void showResult(boolean isSubmit) {
		if(isNew) {
			MasterUtil.showInfoDialog("ZZAssessorCreatedSuccess", MasterUtil.fCloseActiveWindow);
		}else {
			MasterUtil.showInfoDialog("ZZAssessorSavedSuccess", MasterUtil.fCloseActiveWindow);
		}
	}
	
	/**
	 * @param menuContextInfo the menuContextInfo to set
	 */
	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}
	
	/**
	 * @param formInfo the formInfo to set
	 */
	public void setFormInfo(FormInfo formInfo) {
		this.formInfo = formInfo;
	}
	
	/**
	 * @return the formInfo
	 */
	public FormInfo getFormInfo() {
		return formInfo;
	}
	
	private MUser_New person;
	boolean isNew = true;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
			
		daoManage.setPoSupplier(I_AD_User.Table_Name, daoManage -> {
			person = new MUser_New(Env.getCtx(), 0, null);
			String name = (String)tmNames.getRow().get(firstNameCol).getValue();
			person.setName(name);
			return person;
		});
		
		daoManage.setPoSupplier(I_ZZAssessorPerson.Table_Name, daoManage -> {
			assessorPerson = new X_ZZAssessorPerson(Env.getCtx(), 0, null);
			return assessorPerson;
		});
		
		setFormInfo(new FormInfo(menuContextInfo));
		setMainTab(new NavTab());
		initForm();
		
		idNoCol.setEventHandle((event, cellMode) -> {
			if (!Objects.equals(cellMode.getDirtyValue(), cellMode.getValue())){
				return;
			}
			
			Object idValue = cellMode.getDirtyValue();
			
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDCellMode = (ListCellModel<X_ZZ_AlternateIDType>)cellMode.getRowModel().get(alternateIDTypeCol);
			
			if (idValue != null)
				cellMode.getColModel().setDefaultValue(idValue);//help it don't set to blank when reload data
			
			if (idValue != null && alternateIDCellMode.getSelectedItem() != null)
				loadSaved((String)idValue, alternateIDCellMode.getSelectedItem().getZZ_AlternateIDType_ID());

		});
		
		alternateIDTypeCol.setEventHandle((event, cellMode) -> {
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDCellMode = (ListCellModel<X_ZZ_AlternateIDType>)cellMode;
			
			alternateIDCellMode.resetDefaultValue();
			alternateIDCellMode.getColModel().setDefaultValue(alternateIDCellMode.getSelectedItem().getName(), MasterUtil.nameAlternateIdTypeCompare);//help it don't set back to rsa id when reload data
			
			IDCellModel idCellMode = (IDCellModel)cellMode.getRowModel().get(idNoCol);
			
			idCellMode.validate();
			if (alternateIDCellMode.getSelectedItem() != null && idCellMode.getDirtyValue() != null)
				loadSaved((String)idCellMode.getDirtyValue(), alternateIDCellMode.getSelectedItem().getZZ_AlternateIDType_ID());
		});
	}

	private void loadSaved (String idValue, int idTypeId) {
		Query userQuery = MTable.get(Env.getCtx(), I_AD_User.Table_Name)
				.createQuery(String.format("(%s = ? AND %s.%s = ?) OR (%s = ? AND %s.%s = ?)", 
												I_AD_User.COLUMNNAME_ZZ_ID_Passport_No
												, I_ZZ_AlternateIDType.Table_Name
												, I_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID
												, I_AD_User.COLUMNNAME_ZZOtherIDNo
												, I_ZZ_AlternateIDType.Table_Name
												, I_ZZ_AlternateIDType.COLUMNNAME_ZZ_AlternateIDType_ID), null);
		
		userQuery.addTableDirectJoin(I_ZZ_AlternateIDType.Table_Name);

		userQuery.setParameters(idValue, idTypeId, idValue, idTypeId);
		userQuery.setOnlyActiveRecords(true);
		person = userQuery.firstOnly();
		
		X_ZZAssessorPerson assessorPersonSaved = null;
		
		if (person != null) {
			daoManage.setDao(person);
			Query savedDataQuery = MTable.get(Env.getCtx(), I_ZZAssessorPerson.Table_Name)
					.createQuery(String.format("%s = ?"
							, I_ZZAssessorPerson.COLUMNNAME_AD_User_ID), null);
			
			savedDataQuery.setParameters(person.getAD_User_ID());
			savedDataQuery.setOnlyActiveRecords(true);
			
			assessorPersonSaved = savedDataQuery.firstOnly();
			
			firstNameCol.setDefaultValue(person.getName());
		}else {
			daoManage.resetDao(I_AD_User.Table_Name);
			firstNameCol.setDefaultValue(null);
		}
		
		if (assessorPersonSaved != null) {
			boolean isDraft = assessorPersonSaved.getZZ_DocStatus() == null || X_ZZAssessorPerson.ZZ_DOCSTATUS_Draft.equals(assessorPersonSaved.getZZ_DocStatus());
			if (!isDraft) {
				MasterUtil.showInfoDialog("ZZAssessorWrongStatus", MasterUtil.fCloseActiveWindow);
			}
			daoManage.setDao(assessorPersonSaved);
		}else {
			daoManage.resetDao(I_ZZAssessorPerson.Table_Name);
		}
		
		isNew = assessorPersonSaved == null;
		assessorPerson = assessorPersonSaved;
		
		loadData();
	}
	
	private void loadData() {
		tmNames.reloadDao();
		
		mainTab.getTabPanelModel().forEach(tabModel -> {
			tabModel.getCompModel().forEach(tableModel -> {
				((TableModel)tableModel).reloadDao();
			});
		});
		
		mainTab.getTabPanelModel().forEach(tabModel -> {
			tabModel.getCompModel().forEach(tableModel -> {
				((TableModel)tableModel).loadSavedData();
			});
		});
	}
	
	private void initForm() {
		tmNames = initTbName();
		initGeneralDetail();
		initContactDetail();
		initHealthFunction();
		//initAddresss();
		initEducationDetail();
		initQualification();
		initSkillsProgramme();
	}

	ColumnModel idNoCol;
	ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;
	
	
	private void initGeneralDetail() {
		List<ColumnModel> cols = new ArrayList<>();
		
		alternateIDTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID)
				, I_AD_User.COLUMNNAME_ZZ_AlternateIDType_ID
				, MasterUtil.getAlternateIDType()
				, title -> {return title.getName();}
				, title -> {return title.getZZ_AlternateIDType_ID();}
			).setzClass(X_ZZ_AlternateIDType.class);
		alternateIDTypeCol.setUseForID(true)
			.setDefaultValue(IDCellModel.idTypeRSA_ID, MasterUtil.nameAlternateIdTypeCompare)
			.required()
			.setTableName(I_AD_User.Table_Name);
		cols.add(alternateIDTypeCol);
		
		idNoCol = IDCellModel.getIDColumnModel()
				.required().setTableName(I_AD_User.Table_Name);
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

		TableModel tmGeneralDetail = TableModel.getTableBean(TableModel.class, cols, false, I_ZZAssessorPerson.Table_Name);
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

		ColumnModel greettingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZLkpTitle)
				, I_ZZAssessorPerson.COLUMNNAME_ZZLkpTitle
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required();
		cols.add(greettingCol);
		
		firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZFirstName)
				, I_ZZAssessorPerson.COLUMNNAME_ZZFirstName
				).required();
			
		cols.add(firstNameCol);
		
		ColumnModel midNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZMiddleName)
				, I_ZZAssessorPerson.COLUMNNAME_ZZMiddleName
				);
		cols.add(midNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZAssessorPerson.Table_Name, I_ZZAssessorPerson.COLUMNNAME_ZZSurname)
				, I_ZZAssessorPerson.COLUMNNAME_ZZSurname
				).required();
		cols.add(surnameCol);
		
		TableModel tmNames = TableModel.getTableBean(TableModel.class, cols, false, I_ZZAssessorPerson.Table_Name);
		tmNames.setSclass("srd-name srd-name-assessor");
		tmNames.setDaoManage(daoManage);
		tmNames.init();
		
		return tmNames;
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
				CellModel.SEARCH_CELL);
		lastSchoolEmisCol.required();
		
		lastSchoolEmisCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			obj -> {
				Object [] objs = (Object [])obj;
				X_ZZLkpSchoolEmis selected = new X_ZZLkpSchoolEmis(Env.getCtx(), (int)objs[0], null);// TODO make a get function to cache
				cellModel.setValue(selected);
			}
			, I_ZZLkpSchoolEmis.Table_Name
			, I_ZZLkpSchoolEmis.COLUMNNAME_ZZLkpSchoolEmis_ID);
		});
		
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
			obj -> {
				Object [] objs = (Object [])obj;
				X_ZZLkpStatssaAreaCode selected = new X_ZZLkpStatssaAreaCode(Env.getCtx(), (int)objs[0], null);// TODO make a get function to cache
				cellModel.setValue(selected);
			}
			, X_ZZLkpStatssaAreaCode.Table_Name
			, X_ZZLkpStatssaAreaCode.COLUMNNAME_ZZLkpStatssaAreaCode_ID);
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
					Msg.getElement(Env.getCtx(), "ZZQualificationCode")
					, I_ZZQualification.COLUMNNAME_Value)
				.setReadonly(true)
				.setTableName(I_ZZQualification.Table_Name);
		cols.add(qualificationCodeCol);
		
		ColumnModel qualificationTitleCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), "ZZQualificationTitle")
				, I_ZZQualification.COLUMNNAME_Name)
			.setReadonly(true)
			.setTableName(I_ZZQualification.Table_Name);
		cols.add(qualificationTitleCol);
		
		ColumnModel qualificationCreditsCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQualification.Table_Name, I_ZZQualification.COLUMNNAME_ZZCredits)
				, I_ZZQualification.COLUMNNAME_ZZCredits)
			.setReadonly(true)
			.setTableName(I_ZZQualification.Table_Name);
		cols.add(qualificationCreditsCol);
		
		ColumnModel registrationStartDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQualification.Table_Name, I_ZZQualification.COLUMNNAME_Registrationstartdate)
				, I_ZZQualification.COLUMNNAME_Registrationstartdate)
			.setReadonly(true)
			.setTableName(I_ZZQualification.Table_Name);
		cols.add(registrationStartDateCol);
		
		ColumnModel registrationEndDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZQualification.Table_Name, I_ZZQualification.COLUMNNAME_Registrationenddate)
				, I_ZZQualification.COLUMNNAME_Registrationenddate)
			.setReadonly(true)
			.setTableName(I_ZZQualification.Table_Name);
		cols.add(registrationEndDateCol);
					
		TableModel tmQualificationLink = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLinkAssessorQualification.Table_Name);
		tmQualificationLink.setViewModel(ViewType.VIEW_GRID);
		tmQualificationLink.setSclass("srd-qualification-scope srd-qualification-scope-assessor");
		tmQualificationLink.setCommandSetting(CommandSetting.getNonAddButton());
		tmQualificationLink.setCreateNewRowWhenEmpty(false);
		tmQualificationLink.init();
		
		tabPanelQualificationScope.getCompModel().add(tmQualificationLink);
		
		
		chooseQualificationCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			obj -> {
				// build include selected ids
				Object [] objs = (Object [])obj;
				
				List<Object> ids = new ArrayList<Object>();
				ids.addAll(Arrays.asList(objs));
				
				if (ids.size() == 0)
					ids.add(0);
				
				String placeholders = ids.stream()
					    .map(i -> "?")
					    .collect(Collectors.joining(","));
				
				// build exclude selected ids
				List<Object> excludeIds = new ArrayList<Object>();
				tmQualificationLink.getRows().forEach(rowModel -> {
					X_ZZQualification qualificationPo = (X_ZZQualification)rowModel.getRowData().getDataNullable(I_ZZQualification.Table_Name);
					if (qualificationPo != null) {
						excludeIds.add(qualificationPo.getZZQualification_ID());
					}
				});
				
				if (excludeIds.size() == 0)
					excludeIds.add(0);
				
				String excludePlaceholde = excludeIds.stream()
					    .map(i -> "?")
					    .collect(Collectors.joining(","));
				
				Query qualificationQuery = MTable.get(Env.getCtx(), I_ZZQualification.Table_ID)
						.createQuery(String.format("%s IN (%s) AND %s NOT IN (%s)", 
								I_ZZQualification.COLUMNNAME_ZZQualification_ID, 
								placeholders,
								I_ZZQualification.COLUMNNAME_ZZQualification_ID,
								excludePlaceholde), null);
				
				ids.addAll(excludeIds);
				qualificationQuery.setParameters(ids);
				
				// convert to list of list
				List<PO> selectedQualifications = qualificationQuery.list();
				List<List<PO>> daos = new ArrayList<>();
				selectedQualifications.forEach(dao -> {
					List<PO> rowData = new ArrayList<>();
					rowData.add(dao);
					daos.add(rowData);
				});
				tmQualificationLink.addNewRows(daos);
			}
			, I_ZZQualification.Table_Name
			, I_ZZQualification.COLUMNNAME_ZZQualification_ID
			, true);
		});
		
		tmQualificationLink.setAfterSave((po, rowModel) -> {
			if (po != null)
				return true;
			
			po = rowModel.getRowData().getDataNewWhenNull(I_ZZLinkAssessorQualification.Table_Name);
			
			X_ZZLinkAssessorQualification linkPO = X_ZZLinkAssessorQualification.class.cast(po);
			linkPO.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
			
			X_ZZQualification qualification = (X_ZZQualification)rowModel.getRowData().getDataNullable(I_ZZQualification.Table_Name);
			linkPO.setZZQualification_ID(qualification.getZZQualification_ID());
			
			linkPO.saveEx(linkPO.get_TrxName());
			
			return true;
		});
		
		tmQualificationLink.setLoadSavedDataHandle(tableModel -> {
			if (assessorPerson != null) {
				Query linkAssessorQuery = MTable.get(Env.getCtx(), I_ZZLinkAssessorQualification.Table_Name)
				 		.createQuery(String.format("%s = ?", X_ZZLinkAssessorQualification.COLUMNNAME_ZZAssessorPerson_ID), null);
				linkAssessorQuery.setOrderBy(X_ZZLinkAssessorQualification.COLUMNNAME_ZZLinkAssessorQualification_ID);
				linkAssessorQuery.setParameters(assessorPerson.getZZAssessorPerson_ID());
				
				List<PO> linkObjs = linkAssessorQuery.list();
				
				Query qualificationQuery = MTable.get(Env.getCtx(), I_ZZQualification.Table_Name)
				 		.createQuery(String.format("%s = ?", X_ZZLinkAssessorQualification.COLUMNNAME_ZZAssessorPerson_ID), null);
				//qualificationQuery.addTableDirectJoin(I_ZZLinkAssessorQualification.Table_Name);
				qualificationQuery.addJoinClause(String.format( " JOIN %s ON (%s.%s = %s.%s)", 
						I_ZZLinkAssessorQualification.Table_Name,
						I_ZZLinkAssessorQualification.Table_Name,
						I_ZZLinkAssessorQualification.COLUMNNAME_ZZQualification_ID,
						I_ZZQualification.Table_Name,
						I_ZZQualification.COLUMNNAME_ZZQualification_ID));
				
				qualificationQuery.setOrderBy(I_ZZLinkAssessorQualification.Table_Name + "." + X_ZZLinkAssessorQualification.COLUMNNAME_ZZLinkAssessorQualification_ID);
				qualificationQuery.setParameters(assessorPerson.getZZAssessorPerson_ID());
				
				List<PO> qualificationObjs = qualificationQuery.list();
				
				List<List<PO>> savedObjs = RowData.mergedList(linkObjs, qualificationObjs);
				
				tmQualificationLink.resetMultiPo(savedObjs);
			}
			 
		});
		
	}
	
	private void initSkillsProgramme() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ValueAdaptColumnModel chooseQualificationCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				null,
				null, 
				CellModel.SEARCH_CELL);
		chooseQualificationCol.setShowTitle(false);
		cols.add(chooseQualificationCol);
		
		TableModel tmQualificationComp = TableModel.getTableBean(TableModel.class, cols, false, null);
		tmQualificationComp.setSclass("srd-skillsprogramme-scope-comp srd-skillsprogramme-scope-comp-assessor");
		tmQualificationComp.init();
		
		NavTabPanel tabPanelSkillsProgramme = new NavTabPanel(mainTab);
		tabPanelSkillsProgramme.setTabTitle("Skills Programme Scope");
		tabPanelSkillsProgramme.getCompModel().add(tmQualificationComp);
		
		cols = new ArrayList<>();
		
		ColumnModel skillsProgrammeCodeCol = CellModel.getColModelForLabel(
					Msg.getElement(Env.getCtx(), "ZZSkillsProgrammeCode")
					, I_ZZSkillsProgramme.COLUMNNAME_Value)
				.setReadonly(true)
				.setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(skillsProgrammeCodeCol);
		
		ColumnModel skillsProgrammeTitleCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), "ZZSkillsProgrammeTitle")
				, I_ZZSkillsProgramme.COLUMNNAME_Name)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(skillsProgrammeTitleCol);
		
		ColumnModel skillsProgrammeCreditsCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZSkillsProgramme.Table_Name, I_ZZSkillsProgramme.COLUMNNAME_ZZCredits)
				, I_ZZSkillsProgramme.COLUMNNAME_ZZCredits)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(skillsProgrammeCreditsCol);
		
		ColumnModel registrationStartDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZSkillsProgramme.Table_Name, I_ZZSkillsProgramme.COLUMNNAME_Registrationstartdate)
				, I_ZZSkillsProgramme.COLUMNNAME_ZZCredits)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(registrationStartDateCol);
		
		ColumnModel registrationEndDateCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_ZZSkillsProgramme.Table_Name, I_ZZSkillsProgramme.COLUMNNAME_Registrationenddate)
				, I_ZZSkillsProgramme.COLUMNNAME_Registrationenddate)
			.setReadonly(true)
			.setTableName(I_ZZSkillsProgramme.Table_Name);
		cols.add(registrationEndDateCol);
					
		TableModel tmSkillsProgramme = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLinkAssessorSkillsProgramme.Table_Name);
		tmSkillsProgramme.setViewModel(ViewType.VIEW_GRID);
		tmSkillsProgramme.setSclass("srd-qualification-scope srd-qualification-scope-assessor");
		
		tmSkillsProgramme.init();
		
		tabPanelSkillsProgramme.getCompModel().add(tmSkillsProgramme);
		
		chooseQualificationCol.setEventHandle((event, cellModel) -> {
			showInfoPanel(
			obj -> {
				Object [] objs = (Object [])obj;
				
				List<Object> ids = Arrays.asList(objs);
				
				String placeholders = ids.stream()
					    .map(i -> "?")
					    .collect(Collectors.joining(","));
				
				Query skillsProgrammeQuery = MTable.get(Env.getCtx(), I_ZZSkillsProgramme.Table_ID)
						.createQuery(String.format("%s IN (%s)", I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID, placeholders), null);
				
				skillsProgrammeQuery.setParameters(ids);
				
				List<PO> selectedSkillsProgramme = skillsProgrammeQuery.list();
				tmSkillsProgramme.reset(selectedSkillsProgramme);
			}
			, I_ZZSkillsProgramme.Table_Name
			, I_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID
			, true);
		});
		
	}
	
	/*
	 * private void initAddresss() { TableModel tmPostalAddress =
	 * BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", null);
	 * TableModel tmPhysicalAddress =
	 * BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical",
	 * tmPostalAddress);
	 * 
	 * tmPostalAddress.setPoSupplier(rowModel -> { X_ZZPersonAddress address =
	 * BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
	 * //address.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
	 * return address;
	 * 
	 * });
	 * 
	 * tmPhysicalAddress.setPoSupplier(rowModel -> { X_ZZPersonAddress address =
	 * BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
	 * //address.setZZAssessorPerson_ID(assessorPerson.getZZAssessorPerson_ID());
	 * return address; });
	 * 
	 * NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
	 * addressDetailTab.setSclass("sdr-address sdr-address-assessor");
	 * addressDetailTab.setTabTitle("Address Details");
	 * 
	 * addressDetailTab.getCompModel().add(tmPhysicalAddress);
	 * //addressDetailTab.getCompModel().add(BuildFormUtil.getAddressControlComp(
	 * physicalAddress, postalAddress));
	 * addressDetailTab.getCompModel().add(tmPostalAddress); }
	 */
	
	public TableModel getTmNames() {
		return tmNames;
	}

	public NavTab getMainTab() {
		return mainTab;
	}

	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}
	
	public static void showInfoPanel(Consumer<Object> closeHandle, String tableName, String colID) {
		showInfoPanel(closeHandle, tableName, colID, false);
	}
	
	public static void showInfoPanel(Consumer<Object> closeHandle, String tableName, String colID, boolean isMultiChoose){
		// create info window
		Component activeWin = SessionManager.getAppDesktop().getActiveWindow();
		Integer winNo = WindowRegistry.getWindowNo(activeWin);
		
		InfoPanel ip = InfoManager.create(winNo, tableName, colID, null, isMultiChoose, null, true);
		
		// set layout for info window
		ip.setVisible(true);
		ip.setStyle("border: 2px");
		ip.setClosable(true);
		ip.addValueChangeListener(evt -> {
			closeHandle.accept(evt.getNewValue());
		});
		
		ip.addEventListener(DialogEvents.ON_WINDOW_CLOSE, new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				InfoPanel showedIp = (InfoPanel)event.getTarget();
				if (!showedIp.isCancelled()) {
					Object[] result = showedIp.getSelectedKeys();
					if (result != null && result.length > 0) {
						closeHandle.accept(result);
					}
					
				}
			}
		});
		ip.setId(ip.getTitle()+"_"+ip.getWindowNo());
		
		//ip.setAttribute(Window.MODE_KEY, Window.MODE_HIGHLIGHTED);
		ip.setBorder("normal");
		ip.setClosable(true);
		int height = ClientInfo.get().desktopHeight;
		int width = ClientInfo.get().desktopWidth;
		if (width <= ClientInfo.MEDIUM_WIDTH)
		{
			ZKUpdateUtil.setWidth(ip, "100%");
			ZKUpdateUtil.setHeight(ip, "100%");
		}
		else
		{
			height = height * 85 / 100;
    		width = width * 80 / 100;
    		ZKUpdateUtil.setWidth(ip, width + "px");
    		ZKUpdateUtil.setHeight(ip, height + "px");
		}
		ip.setContentStyle("overflow: auto");
		
		AEnv.showWindow(ip);
	}
	
	@Override
	public void doSave(String trxName) {
		boolean isDraft = true;
		if (assessorPerson != null) {
			isDraft = assessorPerson.getZZ_DocStatus() == null || X_ZZAssessorPerson.ZZ_DOCSTATUS_Draft.equals(assessorPerson.getZZ_DocStatus());
		}
		
		if (!isDraft) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZAssessorWrongStatus"));
		}
		
		super.doSave(trxName);
		assessorPerson.setAD_User_ID(person.getAD_User_ID());
		assessorPerson.save(trxName);
	}
	
	@Override
	public void doSubmit(String trxName) {
		assessorPerson.setZZ_DocStatus(X_ZZAssessorPerson.ZZ_DOCSTATUS_Pending);
		assessorPerson.saveEx(trxName);
		super.doSubmit(trxName);
	}
	
	@Override
	public boolean isSupportSubmit() {
		return true;
	}
}
