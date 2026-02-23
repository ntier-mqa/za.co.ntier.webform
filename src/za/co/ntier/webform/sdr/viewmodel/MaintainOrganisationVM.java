package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.I_C_Location;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_AD_User;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.X_C_BPartner_Location;
import org.compiere.model.X_C_Location;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.event.PagingListener;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZOrganisationLinkage;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.I_ZZ_FormContact;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZOrganisationLinkage;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ProvinceCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

public class MaintainOrganisationVM extends BaseAppVM {
	private FormInfo formInfo;
	private TableModel names;
	TableModel physicalAddress;
	TableModel postalAddress;
	private MenuContextInfo menuContextInfo;
	MBPartner_New orgPO;
	
	private NavTab mainTab;
	
	private void initOrg(MenuContextInfo menuContextInfo) {
		int sdfOrganisationID = menuContextInfo.getRecordID();
		
		if (sdfOrganisationID > 0) {
			Query orgPOQuery = MTable.get(Env.getCtx(), I_C_BPartner.Table_Name)
					.createQuery(String.format("%s.%s = ?", X_ZZSdfOrganisation.Table_Name, X_ZZSdfOrganisation.COLUMNNAME_ZZSdfOrganisation_ID), null);
					
			orgPOQuery.addJoinClause(String.format("INNER JOIN %s ON (%s.%s = %s.%s)",
					X_ZZSdfOrganisation.Table_Name,
					X_ZZSdfOrganisation.Table_Name,
					X_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID,
					I_C_BPartner.Table_Name,
					X_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID));
			orgPOQuery.setParameters(sdfOrganisationID);
			
			orgPO = orgPOQuery.first();
		}else {
			MasterUtil.showInfoDialog("ZZMaintainOrgFromMenu", MasterUtil.fCloseActiveWindow);
			
		}
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
		TableModel contactTableModel = initContactDetailComp();
		contactTableModel.setViewModel(TableModel.ViewType.VIEW_CARD);
		contactDetailTab.getCompModel().add(contactTableModel);

		// address tab
		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("address");
		addressDetailTab.setTabTitle("ADDRESS DETAILS");
		
		postalAddress = BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", false, orgPO.getC_BPartner_ID(), null);
		physicalAddress = BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical", false, orgPO.getC_BPartner_ID(), postalAddress);
		
		addressDetailTab.getCompModel().add(physicalAddress);
		//addressDetailTab.getCompModel().add(BuildFormUtil.getAddressControlComp(physicalAddress, postalAddress));
		addressDetailTab.getCompModel().add(postalAddress);
		
		initChildOrg();
		
		if (!"Parent".equals(orgPO.getZZOrganisationType())) {
			mainTab.getTabPanelModel().remove(childTabPanel);
		}
		
	}
	
	private TableModel initContactDetailComp() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel titleCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Title)
				, I_AD_User.COLUMNNAME_Title
				).required()
				;
		cols.add(titleCol);
		
		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZFirstName)
				, I_AD_User.COLUMNNAME_Name
				).required()
				;
		cols.add(firstNameCol);
		
		ColumnModel lastNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdf.Table_Name, I_ZZSdf.COLUMNNAME_ZZSurname)
				, I_AD_User.COLUMNNAME_ZZSurname
				).required()
				;
		cols.add(lastNameCol);
		
		
		ColumnModel designationCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_Designation)
				, I_AD_User.COLUMNNAME_ZZ_Designation
				).required()
				;
		cols.add(designationCol);
		
		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				);
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required()
				;
		cols.add(cellPhoneNumberCol);

		ColumnModel orgFaxCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Fax)
				, I_AD_User.COLUMNNAME_Fax
				);
		cols.add(orgFaxCol);
		
		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				)
				;
		cols.add(emailCol);
		
		/*ColumnModel provinceCol = ProvinceCellModel.getProvinceColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZ_FormContact.Table_Name, I_ZZ_FormContact.COLUMNNAME_C_Region_ID)
				, I_C_Location.COLUMNNAME_C_Region_ID)
				.required()
				.setTableName(I_C_Location.Table_Name);
		cols.add(provinceCol);
		
		
		X_C_Location location;
		orgPO.getLocations(true);
		X_C_BPartner_Location bpLocation = orgPO.getPrimaryC_BPartner_Location();
		if (bpLocation != null) {
			if (bpLocation.getC_Location_ID() > 0) {
				location = (X_C_Location)bpLocation.getC_Location();
				orgDaoManage.setDao(location);
			}
		}
		
		orgDaoManage.setPoSupplier(I_C_Location.Table_Name, daoManage -> {
			X_C_BPartner_Location bpLocationx = orgPO.getPrimaryC_BPartner_Location();
			if (bpLocationx == null) {
				bpLocationx = new X_C_BPartner_Location(Env.getCtx(), 0, daoManage.getTrxName());
				bpLocationx.setC_BPartner_ID(orgPO.getC_BPartner_ID());
			}
			
			X_C_Location locationx = new X_C_Location(Env.getCtx(), 0, daoManage.getTrxName());
			locationx.saveEx();
			bpLocationx.setC_Location_ID(locationx.getC_Location_ID());
			bpLocationx.saveEx();
			
			return locationx;
		});*/
		TableModel orgContactDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		orgContactDetailBean.setSclass("srd-org-contact");
		
		Query contactQuery = MTable.get(Env.getCtx(), org.compiere.model.I_AD_User.Table_Name)
				.createQuery(
						String.format("%s = ?", I_AD_User.COLUMNNAME_C_BPartner_ID), null);
		contactQuery.setParameters(orgPO.getC_BPartner_ID());
		List<PO> contacts = contactQuery.list();
		
		orgContactDetailBean.setPoSupplier(rowModel -> {
			MUser_New nContact = new MUser_New(Env.getCtx(), 0, null);
			nContact.setAD_Org_ID(0);
			nContact.setC_BPartner_ID(orgPO.getC_BPartner_ID());
			nContact.setNotificationType(X_AD_User.NOTIFICATIONTYPE_EMailPlusNotice);
			return nContact; 
		});
		
		orgContactDetailBean.init(contacts);
		
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
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(sdlNumTypeCol);
		
		ColumnModel orgRegistrationNumTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZOrgRegistrationNumberType)
				, I_C_BPartner.COLUMNNAME_ZZOrgRegistrationNumberType
				, MasterUtil.getOrganisationRegistrationNumberType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
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
				, title -> {return title.getValue() + " - " + title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(sicCodeCol);
		
		ColumnModel subSectorCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZSubSector)
				, I_C_BPartner.COLUMNNAME_ZZSubSector
				, MasterUtil.getSubSector()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(subSectorCol);
		
		ColumnModel orgTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZOrganisationType)
				, I_C_BPartner.COLUMNNAME_ZZOrganisationType
				, MasterUtil.getOrganisationType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(orgTypeCol);
		
		orgTypeCol.setEventHandle((event, cellModel) -> {

			if ("Parent".equals(cellModel.getValue()) &&
					!mainTab.getTabPanelModel().contains(childTabPanel)){
				mainTab.getTabPanelModel().add(childTabPanel);
			}else if (!"Parent".equals(cellModel.getValue()) &&
					mainTab.getTabPanelModel().contains(childTabPanel))
 				mainTab.getTabPanelModel().remove(childTabPanel);
			
			
		});
		
		ColumnModel chamberCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZChamberCode)
				, I_C_BPartner.COLUMNNAME_ZZChamberCode
				, MasterUtil.getChamberCode()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(chamberCol);
		
		TableModel orgGeneralDetailBean = TableModel.getTableBean(TableModel.class, cols, false);
		orgGeneralDetailBean.setSclass("srd-org-general");
		
		orgGeneralDetailBean.setDaoManage(orgDaoManage);
		
		orgGeneralDetailBean.init();
		
		return orgGeneralDetailBean;
	}

	void initChildOrg() {
		childTabPanel = new NavTabPanel(mainTab);
		childTabPanel.setTabTitle("CHILD ORGANISATION");
		
		List<ColumnModel> cols = new ArrayList<ColumnModel>();
		
		ColumnModel startDateCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name, I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkStartYear)
				, I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkStartYear
			).required()
			.setTableName(I_ZZOrganisationLinkage.Table_Name);
		
		cols.add(startDateCol);
		
		ColumnModel endDateCol = CellModel.getColModelForPositiveNumber(
				MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name, I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkEndYear)
				, I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkEndYear
			)
			.setTableName(I_ZZOrganisationLinkage.Table_Name);
		
		cols.add(endDateCol);
		
		ColumnModel sdlNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name, I_ZZOrganisationLinkage.COLUMNNAME_ZZ_SDL_No)
				, I_ZZOrganisationLinkage.COLUMNNAME_ZZ_SDL_No
			).setMandatory(true)
			.setTableName(I_ZZOrganisationLinkage.Table_Name);
		
		cols.add(sdlNoCol);
		
		ColumnModel legaNameCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), "ZZLegalName")
			);
		
		cols.add(legaNameCol);
		
		ColumnModel tradeNameCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), "ZZTradeName")
			);
		
		cols.add(tradeNameCol);
		
		ColumnModel numOfEmployeeCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZ_Number_Of_Employees)
			);
		
		cols.add(numOfEmployeeCol);
		
		ColumnModel linkRequestCol = UploadCellModel.getUploadColumnModel("", null, null,
				"Upload Link Request")
			.required();
			
		cols.add(linkRequestCol);
		
		sdlNoCol.addCellPropertyChangeListener(evt -> {
			CellModel sdlNoCell = (CellModel)evt.getSource();
			RowModel row = sdlNoCell.getRowModel();
			CellModel legaNameCell = row.get(legaNameCol);
			CellModel tradeNameCell = row.get(tradeNameCol);
			CellModel numOfEmployeeCell = row.get(numOfEmployeeCol);
			
			if (sdlNoCell.getValue() == null) {
				legaNameCell.setValue(null);
				tradeNameCell.setValue(null);
				numOfEmployeeCell.setValue(null);
			}else {
				MBPartner_New childOrg = MBPartner_New.get(Env.getCtx(), (String)sdlNoCell.getValue());
				legaNameCell.setValue(childOrg.getName());
				tradeNameCell.setValue(childOrg.getName2());
				numOfEmployeeCell.setValue(childOrg.getZZ_Number_Of_Employees());
			}
		});
		
		sdlNoCol.setValidateHandle((cellModel, messages) -> {
			RowModel row = cellModel.getRowModel();
			CellModel legaNameCell = row.get(legaNameCol);
			CellModel tradeNameCell = row.get(tradeNameCol);
			CellModel numOfEmployeeCell = row.get(numOfEmployeeCol);
			
			String dirtyValue = (String)cellModel.getDirtyValue();
			MBPartner_New childOrg = null;
			if (StringUtils.isNoneEmpty(dirtyValue)) {
				childOrg = MBPartner_New.get(Env.getCtx(), dirtyValue);
				if (childOrg == null) {
					messages.add(Msg.getMsg(Env.getCtx(), "ZZMaintainOrgChildOrgNotFound"));
				}
			}
			
			if (childOrg == null) {
				legaNameCell.setValue(null);
				tradeNameCell.setValue(null);
				numOfEmployeeCell.setValue(null);
			}else {
				Query childOrgQuery = MTable.get(Env.getCtx(), I_ZZOrganisationLinkage.Table_Name)
						.createQuery(String.format("%s = ? AND %s=?", I_ZZOrganisationLinkage.COLUMNNAME_C_BPartner_ID, I_ZZOrganisationLinkage.COLUMNNAME_BPartner_Parent_ID), null); 
				childOrgQuery.setParameters(childOrg.getC_BPartner_ID(), orgPO.getC_BPartner_ID());
				
				if (childOrgQuery.first() != null) {
					messages.add(Msg.getMsg(Env.getCtx(), "ZZMaintainOrgChildLinked"));
				}
			}
			
		});
		
		TableModel tmChildOrgModel = TableModel.getTableBean(TableModel.class, cols, false);
		tmChildOrgModel.setPoSupplier(rowModel -> {
			X_ZZOrganisationLinkage childOrgLink = new X_ZZOrganisationLinkage(Env.getCtx(), 0, null);
			
			return childOrgLink;
		});
		
		tmChildOrgModel.setBeforeSave(po -> {
			X_ZZOrganisationLinkage childOrgLink = (X_ZZOrganisationLinkage)po;
			childOrgLink.setBPartner_Parent_ID(orgPO.getC_BPartner_ID());
			return true;
		});
		
		tmChildOrgModel.setSclass("LinkOrgChild");
		tmChildOrgModel.setViewModel(ViewType.VIEW_GRID);
		
		Query childOrgQuery = MTable.get(Env.getCtx(), I_ZZOrganisationLinkage.Table_Name)
				.createQuery(String.format("%s = ?", I_ZZOrganisationLinkage.COLUMNNAME_BPartner_Parent_ID), null); 
		childOrgQuery.setParameters(orgPO.getC_BPartner_ID());
		List<PO> saveds = childOrgQuery.list();
		
		tmChildOrgModel.init(saveds);
		
		childTabPanel.getCompModel().add(tmChildOrgModel);
	}
	
	public static record DependencyFields(CellModel legaNameCell, CellModel tradeNameCell, CellModel numOfEmployeeCell) {}
	
	void setDependencyField(MBPartner_New childOrg, DependencyFields dependencyFields) {
		if (childOrg == null) {
			dependencyFields.legaNameCell.setValue(null);
			dependencyFields.tradeNameCell.setValue(null);
			dependencyFields.numOfEmployeeCell.setValue(null);
		}else {
			dependencyFields.legaNameCell.setValue(childOrg.getName());
			dependencyFields.tradeNameCell.setValue(childOrg.getName2());
			dependencyFields.numOfEmployeeCell.setValue(childOrg.getZZ_Number_Of_Employees());
		}
	}
	
	NavTabPanel childTabPanel;
	
	DaoManage orgDaoManage = new DaoManage();
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		
		initOrg(menuContextInfo);
		if (orgPO != null) {
			orgDaoManage = new DaoManage();
			orgDaoManage.setDao(orgPO);
			
			names = initNames();
			initMaintab();
		}
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
		namesBean.setDaoManage(orgDaoManage);
		namesBean.init(null, null);
		
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
	public List<DaoManage> getDaoManages() {
		return List.of(orgDaoManage);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab, names);
	}
	
	@Override
	protected void showResult(boolean isSubmit) {
		MasterUtil.showInfoDialog("ZZOrgSavedSuccess", MasterUtil.fCloseActiveWindow);
	}



	@Override
	public Object getMainApp() {
		return orgPO;
	}
	
	@Override
	public String deleteLabel() {
		return "Cancel";
	}
}
