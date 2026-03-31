package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_AD_User;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import com.google.common.base.Objects;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZOrgTrainingCommittee;
import za.co.ntier.api.model.I_ZZOrganisationLinkage;
import za.co.ntier.api.model.I_ZZSdf;
import za.co.ntier.api.model.I_ZZ_EDP_Application;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZOrgTrainingCommittee;
import za.co.ntier.api.model.X_ZZOrganisationLinkage;
import za.co.ntier.api.model.X_ZZPersonAddress;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.CellModel.InputCheckResult;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.DaoManage;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;
import za.co.ntier.webform.sdr.component.util.BuildFormUtil;

public class MaintainOrganisationVM extends BaseAppVM {
	private FormInfo formInfo;
	private TableModel names;
	private TableModel physicalAddress;
	private TableModel postalAddress;
	private TableModel contactTableModel;
	
	private MenuContextInfo menuContextInfo;
	MBPartner_New orgPO;
	
	private NavTab mainTab;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		
		initMaintab();
		
		MBPartner_New sOrgPO = initOrg(menuContextInfo);
		
		/*if (sOrgPO != null)
			names.getRow().get(sdlNoCol).setValue(sOrgPO.getValue());
			*/
		loadDataFollowOrg(sOrgPO);
	}
	
	private MBPartner_New initOrg(MenuContextInfo menuContextInfo) {
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
			
			return orgPOQuery.first();
			
		}
	
		return null;
	}

	TableModel tmGeneralDetail;
	
	private void initMaintab(){
		names = initNames();
		
		mainTab = new NavTab();
		
		NavTabPanel generalDetailTab = new NavTabPanel(mainTab);
		generalDetailTab.setTabTitle("GENERAL DETAILS");
		// new component
		tmGeneralDetail = initGeneralDetailComp();
		generalDetailTab.getCompModel().add(tmGeneralDetail);
		
		NavTabPanel contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("CONTACT DETAILS");
		// new component
		contactTableModel = initContactDetailComp();
		contactTableModel.setViewModel(TableModel.ViewType.VIEW_CARD);
		contactDetailTab.getCompModel().add(contactTableModel);
		
		// address tab
		NavTabPanel addressDetailTab = new NavTabPanel(mainTab);
		addressDetailTab.setSclass("address");
		addressDetailTab.setTabTitle("ADDRESS DETAILS");
		
		postalAddress = BuildFormUtil.getAddressDetailComp("Postal ", "Postal", "Postal", null);
		postalAddress.setPoSupplier(rowModel -> {
			X_ZZPersonAddress address = BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
			address.setC_BPartner_ID(orgPO.getC_BPartner_ID());
			return address;
		});
		
		physicalAddress = BuildFormUtil.getAddressDetailComp("Physical ", "Physical", "Physical", postalAddress);
		physicalAddress.setPoSupplier(rowModel -> {
			X_ZZPersonAddress address = BuildFormUtil.getNewAddress(rowModel.getTableModel().getDataType());
			address.setC_BPartner_ID(orgPO.getC_BPartner_ID());
			return address;
		});
		
		
		addressDetailTab.getCompModel().add(physicalAddress);
		//addressDetailTab.getCompModel().add(BuildFormUtil.getAddressControlComp(physicalAddress, postalAddress));
		addressDetailTab.getCompModel().add(postalAddress);
		
		initChildOrg();
		
		initTrainingCommittee();
	}

	private void loadDataFollowOrg(MBPartner_New orgPO) {
		if (orgPO == null)
			return;
		
		this.orgPO = orgPO;
		
		if (orgDaoManage == null) {
			orgDaoManage = new DaoManage();
		}
		
		orgDaoManage.setDao(orgPO);
		
		// show/hidden child tab follow org type
		visiableChildOrg(orgPO.getZZOrganisationType());
		
		// load org contact 
		Query contactQuery = MTable.get(Env.getCtx(), org.compiere.model.I_AD_User.Table_Name)
				.createQuery(
						String.format("%s = ?", I_AD_User.COLUMNNAME_C_BPartner_ID), null);
		contactQuery.setParameters(orgPO.getC_BPartner_ID());
		List<PO> contacts = contactQuery.list();
		
		contactTableModel.reset(contacts);
		
		// init name
		names.reloadDao();
		
		// reload general detail
		tmGeneralDetail.reloadDao();
		
		// reload address
		PO savedPo = BuildFormUtil.getSavedAddress(orgPO.getC_BPartner_ID(), physicalAddress.getDataType(), false);
		physicalAddress.getRow().setData(savedPo);
		physicalAddress.reloadDao();
		
		savedPo = BuildFormUtil.getSavedAddress(orgPO.getC_BPartner_ID(), postalAddress.getDataType(), false);
		postalAddress.getRow().setData(savedPo);
		postalAddress.reloadDao();
		
		// load TrainingCommittee
		Query trainingCommitteeQuery = MTable.get(Env.getCtx(), I_ZZOrgTrainingCommittee.Table_Name)
				.createQuery(
						String.format("%s = ?", I_ZZOrgTrainingCommittee.COLUMNNAME_C_BPartner_ID), null);
		trainingCommitteeQuery.setParameters(orgPO.getC_BPartner_ID());
		List<PO> trainingCommittees = trainingCommitteeQuery.list();
		
		tmTrainingCommittee.reset(trainingCommittees);
		
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
				, I_ZZSdf.COLUMNNAME_ZZSurname
				).required()
				;
		cols.add(lastNameCol);
		
		
		ColumnModel designationCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_ZZ_Designation)
				, I_AD_User.COLUMNNAME_ZZ_Designation
				).required()
				;
		cols.add(designationCol);
		
		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				).required()
				;
		cols.add(cellPhoneNumberCol);
		
		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone2)
				, I_AD_User.COLUMNNAME_Phone2
				);
		cols.add(telephoneNumberCol);

		/*ColumnModel orgFaxCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Fax)
				, I_AD_User.COLUMNNAME_Fax
				);
		cols.add(orgFaxCol);*/
		
		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				).required();
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
		
		orgContactDetailBean.setPoSupplier(rowModel -> {
			MUser_New nContact = new MUser_New(Env.getCtx(), 0, null);
			nContact.setAD_Org_ID(0);
			checkOrg();
			nContact.setC_BPartner_ID(orgPO.getC_BPartner_ID());
			nContact.setNotificationType(X_AD_User.NOTIFICATIONTYPE_EMailPlusNotice);
			return nContact; 
		});
		
		orgContactDetailBean.init(null);
		
		return orgContactDetailBean;
	}

	private void checkOrg () {
		if (orgPO == null)
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZMaintainOrgMissingOrg"));
	}
	
	private TableModel initGeneralDetailComp() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel orgRegistrationNumTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZOrgRegistrationNumberType)
				, I_C_BPartner.COLUMNNAME_ZZOrgRegistrationNumberType
				, MasterUtil.getOrganisationRegistrationNumberType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(orgRegistrationNumTypeCol);

		ColumnModel sdlNumTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZLevyNumberType)
				, I_C_BPartner.COLUMNNAME_ZZLevyNumberType
				, MasterUtil.getLevyNumberType()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).required()
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(sdlNumTypeCol);
		
		ColumnModel registrationNumCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZOrgReferenceNo")
				, I_C_BPartner.COLUMNNAME_ReferenceNo
			).setReadonly(true)
			.setTableName(I_C_BPartner.Table_Name);
		cols.add(registrationNumCol);
		
		ColumnModel sarsNumCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZPayeNumber")
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
		
		

		
		ListColumnModel<ValueNamePair> unionisedCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZUnionised), 
				I_C_BPartner.COLUMNNAME_ZZUnionised,
				MasterUtil.getYesNoList(),
				ref -> {return ref.getName();},
				ref -> {return ref.getValue();},
				CellModel.RADIO_CELL
				).setzClass(ValueNamePair.class);
		unionisedCol.required();
		unionisedCol.setTableName(I_C_BPartner.Table_Name);
		
		cols.add(unionisedCol);
		
		
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

			visiableChildOrg((String)cellModel.getValue());
			
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

	private void visiableChildOrg(String orgType) {
		if ("Parent".equals(orgType) &&
				!mainTab.getTabPanelModel().contains(childTabPanel)){
			mainTab.getTabPanelModel().add(childTabPanel);
		}else if (!"Parent".equals(orgType) &&
				mainTab.getTabPanelModel().contains(childTabPanel))
				mainTab.getTabPanelModel().remove(childTabPanel);
		
		if (orgPO != null && "Parent".equals(orgType)){
			Query childOrgQuery = MTable.get(Env.getCtx(), I_ZZOrganisationLinkage.Table_Name)
					.createQuery(String.format("%s = ?", I_ZZOrganisationLinkage.COLUMNNAME_BPartner_Parent_ID), null); 
			childOrgQuery.setParameters(orgPO.getC_BPartner_ID());
			List<PO> saveds = childOrgQuery.list();
			
			((TableModel)childTabPanel.getCompModel().get(0)).reset(saveds);
		}
		
		
	}

	void initChildOrg() {
		childTabPanel = new NavTabPanel(mainTab);
		childTabPanel.setTabTitle("CHILD ORGANISATION");
		
		List<ColumnModel> cols = new ArrayList<ColumnModel>();
		
		/*
		 * ColumnModel startDateCol = CellModel.getColModelForPositiveNumber(
		 * MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name,
		 * I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkStartYear) ,
		 * I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkStartYear ).required()
		 * .setTableName(I_ZZOrganisationLinkage.Table_Name);
		 * 
		 * cols.add(startDateCol);
		 * 
		 * ColumnModel endDateCol = CellModel.getColModelForPositiveNumber(
		 * MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name,
		 * I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkEndYear) ,
		 * I_ZZOrganisationLinkage.COLUMNNAME_ZZLinkEndYear )
		 * .setTableName(I_ZZOrganisationLinkage.Table_Name);
		 * 
		 * cols.add(endDateCol);
		 * 
		 * startDateCol.setValidateHandle((cellModel, validateMsgs) -> { //Integer
		 * startYear = (Integer)cellModel.getDirtyValue();
		 * 
		 * //Integer erndYear =
		 * (Integer)cellModel.getRowModel().get(endDateCol).getDirtyValue(); });
		 * 
		 * endDateCol.setValidateHandle((cellModel, validateMsgs) -> { //Integer endYear
		 * = (Integer)cellModel.getDirtyValue();
		 * 
		 * //Integer startYear =
		 * (Integer)cellModel.getRowModel().get(startDateCol).getDirtyValue(); });
		 */
		
		ColumnModel childSdlNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name, I_ZZOrganisationLinkage.COLUMNNAME_ZZ_SDL_No)
				, I_ZZOrganisationLinkage.COLUMNNAME_ZZ_SDL_No
			).setMandatory(true)
			.setTableName(I_ZZOrganisationLinkage.Table_Name);
		
		cols.add(childSdlNoCol);
		
		ColumnModel legaNameCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), "ZZLegalName")
			);
		
		cols.add(legaNameCol);
		
		ColumnModel tradeNameCol = CellModel.getColModelForLabel(
				Msg.getElement(Env.getCtx(), "ZZTradeName")
			);
		
		cols.add(tradeNameCol);
		
		
		ListColumnModel<ValueNamePair> parentUploadCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZOrganisationLinkage.Table_Name, I_ZZOrganisationLinkage.COLUMNNAME_ZZ_Parent_Uploads), 
				I_ZZOrganisationLinkage.COLUMNNAME_ZZ_Parent_Uploads,
				MasterUtil.getYesNoList(),
				ref -> {return ref.getName();},
				ref -> {return ref.getValue();},
				CellModel.RADIO_CELL
				).setzClass(ValueNamePair.class);
		parentUploadCol.required();
		parentUploadCol.setTableName(I_ZZOrganisationLinkage.Table_Name);
		
		cols.add(parentUploadCol);
		
		ColumnModel linkRequestCol = UploadCellModel.getUploadColumnModel("", null, null,
				"Upload Link Request")
			.required();
			
		cols.add(linkRequestCol);
		
		childSdlNoCol.addCellPropertyChangeListener(evt -> {
			CellModel sdlNoCell = (CellModel)evt.getSource();
			RowModel row = sdlNoCell.getRowModel();
			CellModel legaNameCell = row.get(legaNameCol);
			CellModel tradeNameCell = row.get(tradeNameCol);
			
			
			if (sdlNoCell.getValue() == null) {
				legaNameCell.setValue(null);
				tradeNameCell.setValue(null);
				
			}else {
				MBPartner_New childOrg = MBPartner_New.get(Env.getCtx(), (String)sdlNoCell.getValue());
				legaNameCell.setValue(childOrg.getName());
				tradeNameCell.setValue(childOrg.getName2());
				
			}
		});
		
		childSdlNoCol.setValidateHandle((cellModel, messages) -> {
			RowModel row = cellModel.getRowModel();
			CellModel legaNameCell = row.get(legaNameCol);
			CellModel tradeNameCell = row.get(tradeNameCol);
			
			
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
				
			}else {
				if (orgPO == null) {
					messages.add(Msg.getMsg(Env.getCtx(), "ZZMaintainOrgMissingOrg"));
					return;
				}
				Query childOrgQuery = MTable.get(Env.getCtx(), I_ZZOrganisationLinkage.Table_Name)
						.createQuery(String.format("%s = ? AND %s <> ?", 
								I_ZZOrganisationLinkage.COLUMNNAME_C_BPartner_ID, 
								I_ZZOrganisationLinkage.COLUMNNAME_BPartner_Parent_ID
								), null);
				
				childOrgQuery.setParameters(childOrg.getC_BPartner_ID(), orgPO.getC_BPartner_ID());
				
				if (childOrgQuery.first() != null) {
					messages.add(Msg.getMsg(Env.getCtx(), "ZZMaintainOrgChildLinked"));
				}else {
					for (RowModel otherRow : cellModel.getTableModel().getValidateRows()) {
						if (otherRow != row) {
							Object otherSdlNoValue = otherRow.get(cellModel.getColModel()).getDirtyValue();
							if (StringUtils.isNotBlank(dirtyValue) && Objects.equal(otherSdlNoValue, dirtyValue)) {
								messages.add(Msg.getMsg(Env.getCtx(), "ZZMaintainOrgDuplicateSdlNoOtherRow"));
							}
						}
					}
				}
			}
			
		});
		
		TableModel tmChildOrgModel = TableModel.getTableBean(TableModel.class, cols, false);
		tmChildOrgModel.setPoSupplier(rowModel -> {
			X_ZZOrganisationLinkage childOrgLink = new X_ZZOrganisationLinkage(Env.getCtx(), 0, null);
			
			return childOrgLink;
		});
		
		tmChildOrgModel.setBeforeSave((po, rowModel) -> {
			X_ZZOrganisationLinkage childOrgLink = (X_ZZOrganisationLinkage)po;
			childOrgLink.setBPartner_Parent_ID(orgPO.getC_BPartner_ID());
			
			String sdlNo = (String)rowModel.get(childSdlNoCol).getValue();
			MBPartner_New childOrg = MBPartner_New.get(Env.getCtx(), sdlNo);
			childOrgLink.setC_BPartner_ID(childOrg.getC_BPartner_ID());
			return true;
		});
		
		tmChildOrgModel.setSclass("LinkOrgChild");
		tmChildOrgModel.setViewModel(ViewType.VIEW_GRID);
		tmChildOrgModel.setShowAddButton(true);
		
		tmChildOrgModel.init();
		
		childTabPanel.getCompModel().add(tmChildOrgModel);
	}
	
	TableModel tmTrainingCommittee;
	private void initTrainingCommittee() {
		NavTabPanel trainingCommitteeTabPanel = new NavTabPanel(mainTab);
		trainingCommitteeTabPanel.setTabTitle("TRAINING COMMITTEE");
		
		List<ColumnModel> cols = new ArrayList<ColumnModel>();
		
		ColumnModel greettingCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZLkpTitle)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZLkpTitle
				, MasterUtil.getLkpTitleLists()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class)
			.setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(greettingCol);
		
		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZFirstName)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZFirstName
				).required()
				.setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(firstNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZSurname)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZSurname
				).setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(surnameCol);
		
		ColumnModel idNoCol = IDCellModel.getIDColumnModel()
				.required()
				.setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(idNoCol);
		
		ColumnModel designationCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZ_Designation)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZ_Designation
				, MasterUtil.getDesignation()
				, title -> {return title.getName();}
				, title -> {return title.getValue();}
			).setzClass(ValueNamePair.class).
			required().
			setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(designationCol);
		
		ColumnModel telephoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_Phone2)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_Phone2
				).setTableName(I_ZZOrgTrainingCommittee.Table_Name).
				required();
		cols.add(telephoneNumberCol);

		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_Phone)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_Phone
				).required()
				.setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(cellPhoneNumberCol);

		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_EMail)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_EMail
				).required()
				.setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(emailCol);
		
		ColumnModel nameOfUnionCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZNameOfUnion)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZNameOfUnion
				).setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(nameOfUnionCol);
		
		ColumnModel positionInUnionCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZPositionInUnion)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZPositionInUnion
				).setTableName(I_ZZOrgTrainingCommittee.Table_Name);
		cols.add(positionInUnionCol);
		
		tmTrainingCommittee = TableModel.getTableBean(TableModel.class, cols, false);
		tmTrainingCommittee.setPoSupplier(rowModel -> {
			X_ZZOrgTrainingCommittee orgTrainingCommittee = new X_ZZOrgTrainingCommittee(Env.getCtx(), 0, null);
			
			return orgTrainingCommittee;
		});
		
		tmTrainingCommittee.setBeforeSave((po, rowModel) -> {
			X_ZZOrgTrainingCommittee orgTrainingCommittee = (X_ZZOrgTrainingCommittee)po;
			orgTrainingCommittee.setC_BPartner_ID(orgPO.getC_BPartner_ID());
			return true;
		});
		
		tmTrainingCommittee.setSclass("orgTrainingCommittee");
		tmTrainingCommittee.setViewModel(ViewType.VIEW_CARD);
		
		tmTrainingCommittee.init();
		
		trainingCommitteeTabPanel.getCompModel().add(tmTrainingCommittee);
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
	
	ColumnModel sdlNoCol;
	
	private TableModel initNames() {
		List<ColumnModel> cols = new ArrayList<>();

		sdlNoCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZ_SDL_No")
				, I_C_BPartner.COLUMNNAME_Value
				).setReadonly(true);
		cols.add(sdlNoCol);
		
		/*
		 * sdlNoCol.setEventHandle((event, cellModel) -> { if
		 * (StringUtils.isBlank((String)cellModel.getValue())) { return; } Query
		 * searchOrgQuery = MTable.get(Env.getCtx(), I_C_BPartner.Table_Name)
		 * .createQuery(String.format("%s = ? AND %s = 'Y'",
		 * I_C_BPartner.COLUMNNAME_Value, I_C_BPartner.COLUMNNAME_ZZ_Is_MQA_Sector),
		 * null); searchOrgQuery.setParameters(cellModel.getValue());
		 * 
		 * MBPartner_New sOrgPo = searchOrgQuery.first();
		 * 
		 * if (sOrgPo == null) { MasterUtil.showInfoDialog("ZZOrgMaintainNotFoundOrg",
		 * MasterUtil.fCloseActiveWindow); }else { orgPO = sOrgPo;
		 * loadDataFollowOrg(orgPO); } });
		 */
		
		ColumnModel legalNameCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZLegalName")
				, I_C_BPartner.COLUMNNAME_Name
				).setReadonly(true)
				.setTableName(I_C_BPartner.Table_Name);
		cols.add(legalNameCol);
		
		ColumnModel tradeNameCol = CellModel.getColModelForText(
				Msg.getElement(Env.getCtx(), "ZZTradeName")
				, I_C_BPartner.COLUMNNAME_Name2
				).setReadonly(true)
				.setTableName(I_C_BPartner.Table_Name);
				;
		cols.add(tradeNameCol);

		
		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setDaoManage(orgDaoManage);
		namesBean.init();
		
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
	
	@Override
	public void doSave(String trxName) {
		checkOrg();
		super.doSave(trxName);
		
		String maintainOrgStatus = MBPartner_New.ZZMAINTAINSTATUS_Yes;
		for (NavTabPanel tabPanel : mainTab.getTabPanelModel()) {
			if (tmTrainingCommittee == tabPanel.getCompModel().get(0)) {
				continue;// don't check tmTrainingCommittee is option
			}
			InputCheckResult inputCheckResult = tabPanel.parseInputState();
			if (!inputCheckResult.getFillMandatory()) {
				maintainOrgStatus = MBPartner_New.ZZMAINTAINSTATUS_No;
				break;
			}
		}
		orgPO.setZZMaintainStatus(maintainOrgStatus);
		orgPO.saveEx(trxName);
		
	}
}
