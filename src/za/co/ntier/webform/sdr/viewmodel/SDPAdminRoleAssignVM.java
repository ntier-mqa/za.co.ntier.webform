package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.compiere.model.I_AD_Role;
import org.compiere.model.I_AD_User_Roles;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_I_BPartner;
import org.compiere.model.MRole;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_AD_User_Roles;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_AD_User;
import za.co.ntier.api.model.I_ZZAssessorPerson;
import za.co.ntier.api.model.I_ZZLinkAssessorQualification;
import za.co.ntier.api.model.I_ZZLkpSchoolEmis;
import za.co.ntier.api.model.I_ZZOrgTrainingCommittee;
import za.co.ntier.api.model.I_ZZ_AlternateIDType;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.MUser_New;
import za.co.ntier.api.model.X_ZZLkpSchoolEmis;
import za.co.ntier.api.model.X_ZZ_AlternateIDType;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.RowModel.RowData;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.TableModel.CommandSetting;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.IDCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.IDTypeCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.NavTab;
import za.co.ntier.webform.sdr.component.tab.bean.NavTabPanel;

public class SDPAdminRoleAssignVM extends BaseAppVM{
	private NavTab mainTab;
	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){

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
				loadUser((String)idValue, alternateIDCellMode.getSelectedItem().getZZ_AlternateIDType_ID());

		});
		
		alternateIDTypeCol.setEventHandle((event, cellMode) -> {
			@SuppressWarnings("unchecked")
			ListCellModel<X_ZZ_AlternateIDType> alternateIDCellMode = (ListCellModel<X_ZZ_AlternateIDType>)cellMode;
			
			alternateIDCellMode.resetDefaultValue();
			alternateIDCellMode.getColModel().setDefaultValue(alternateIDCellMode.getSelectedItem().getName(), MasterUtil.nameAlternateIdTypeCompare);//help it don't set back to rsa id when reload data
			
			IDCellModel idCellMode = (IDCellModel)cellMode.getRowModel().get(idNoCol);
			
			idCellMode.validate();
			if (alternateIDCellMode.getSelectedItem() != null && idCellMode.getDirtyValue() != null)
				loadUser((String)idCellMode.getDirtyValue(), alternateIDCellMode.getSelectedItem().getZZ_AlternateIDType_ID());
		});
	}
	
	private void loadUser(String idValue, int idTypeId) {
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
		MUser_New person = userQuery.firstOnly();
		
		if (person != null) {
			tmContactDetail.getRow().setDataOneRow(person);
			tmContactDetail.reloadDao();
			
			//tmRoleAssign.load
			Query roleAssignQuery = MTable.get(Env.getCtx(), I_AD_User_Roles.Table_Name)
					.createQuery(String.format("%s = ? AND %s", 
							I_AD_User.COLUMNNAME_AD_User_ID,
							MRole.getWhereRoleType(SDPAdminRole + "," + NonSDPAdminRole, I_AD_Role.Table_Name))
							, null);
			roleAssignQuery.addTableDirectJoin(I_AD_Role.Table_Name);
			roleAssignQuery.setParameters(person.getAD_User_ID());
			
			List<PO> roleAssigns = roleAssignQuery.list();
			List<List<PO>> savedDatas = RowData.standardToMultiPo(roleAssigns);
			for (List<PO> savedData : savedDatas) {
				X_AD_User_Roles roleAssign = (X_AD_User_Roles)savedData.get(0);
				savedData.add(MRole.get(Env.getCtx(), roleAssign.getAD_Role_ID()));
			}
			tmRoleAssign.resetMultiPo(savedDatas);
			
		}
		
		
	}

	ListColumnModel<X_ZZ_AlternateIDType> alternateIDTypeCol;
	ColumnModel idNoCol;
	NavTabPanel contactDetailTab;
	
	private void initForm() {
		contactDetailTab = new NavTabPanel(mainTab);
		contactDetailTab.setTabTitle("User Details");
		
		initUser();
		initRoleAssign();
		
		tmContactDetail.setAfterSave((po, rowModel) -> {
			if (po != null && I_AD_User.Table_Name.equals(po.get_TableName())) {
				MUser_New user = (MUser_New)po;
				for(RowModel roleAssignRow:tmRoleAssign.getRows()) {
					X_AD_User_Roles roleAssign = roleAssignRow.getDataOneRow(X_AD_User_Roles.class, X_AD_User_Roles.Table_Name);
					roleAssign.setAD_User_ID(user.getAD_User_ID());
				}
			}
			return true;
		});
		
		tmContactDetail.setBeforeSave((po, rowModel) -> {
			if (po != null) {
				MUser_New user = (MUser_New)po;
				if (user.getName() == null)
					user.setName(user.getZZFirstName());
				
				if (!MUser_New.NOTIFICATIONTYPE_EMailPlusNotice.equals(user.getNotificationType())) {
					user.setNotificationType(MUser_New.NOTIFICATIONTYPE_EMailPlusNotice);
				}
			}
			
			
			return true;
		});
	}

	TableModel tmContactDetail;
	ValueAdaptColumnModel bpartnerCol;
	private void initUser() {
		List<ColumnModel> cols = new ArrayList<>();
		
		alternateIDTypeCol = IDTypeCellModel.getIDTypeCol();
		cols.add(alternateIDTypeCol);
		
		idNoCol = IDCellModel.getIDColumnModel()
				.required().setTableName(I_AD_User.Table_Name);
		cols.add(idNoCol);
		
		ColumnModel greettingCol = ListCellModel.getLkpTitleColumnModel();
		greettingCol.setMandatory(false);
		cols.add(greettingCol);
		
		ColumnModel firstNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZFirstName)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZFirstName
				);
		cols.add(firstNameCol);
		
		ColumnModel surnameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZOrgTrainingCommittee.Table_Name, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZSurname)
				, I_ZZOrgTrainingCommittee.COLUMNNAME_ZZSurname
				);
		cols.add(surnameCol);
		
		ColumnModel cellPhoneNumberCol = CellModel.getColModelForPhone(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_Phone)
				, I_AD_User.COLUMNNAME_Phone
				);
		cols.add(cellPhoneNumberCol);
		
		ColumnModel emailCol = CellModel.getColModelForEmail(
				MasterUtil.getNameOfColTranslated(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_EMail)
				, I_AD_User.COLUMNNAME_EMail
				);
		
		cols.add(emailCol);
		
		bpartnerCol = settingBpartnerCol();
		cols.add(bpartnerCol);
		
		tmContactDetail = TableModel.getTableBean(TableModel.class, cols, false, I_AD_User.Table_Name);
		tmContactDetail.setSclass("SDPAdminRoleAssign");
		tmContactDetail.init();
		
		contactDetailTab.getCompModel().add(tmContactDetail);
		
	}

	private ValueAdaptColumnModel settingBpartnerCol() {
		ValueAdaptColumnModel chooseBpartnerCol = ValueAdaptCellModel.getValueAdaptColumnModel(
				Msg.getElement(Env.getCtx(), "OrgName"), 
				I_AD_User.COLUMNNAME_C_BPartner_ID, 
				CellModel.SEARCH_CELL);
		chooseBpartnerCol.required();
		
		chooseBpartnerCol.setEventHandle((event, cellModel) -> {
			AssessorRegistrationVM.showInfoPanel(
			obj -> {
				Object [] objs = (Object [])obj;
				MBPartner_New selected = (MBPartner_New)MBPartner_New.get(Env.getCtx(), (int)objs[0]);
				cellModel.setValue(selected);
			}
			, I_C_BPartner.Table_Name
			, I_C_BPartner.COLUMNNAME_C_BPartner_ID);
		});
		
		chooseBpartnerCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			
			MBPartner_New schoolEmis = (MBPartner_New)value;
			return schoolEmis.getName();
		});
		
		chooseBpartnerCol.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			
			MBPartner_New schoolEmis = (MBPartner_New)value;
			return schoolEmis.getC_BPartner_ID();
		});
		
		chooseBpartnerCol.setValueFromDaoAdaptHandle(obj -> {
			if (obj == null)
				return null;
			
			Integer id = Integer.class.cast(obj);
			if (id == 0)
				return null;
			
			return (MBPartner_New)MBPartner_New.get(Env.getCtx(), id);// TODO make a get function to cache
		});
		
		return chooseBpartnerCol;
	}
	
	private List<MRole> getRoles(){
		return MTable.get(Env.getCtx(), MRole.Table_ID)
			.createQuery(MRole.getWhereRoleType(SDPAdminRole + "," + NonSDPAdminRole, null), null)
			.list();
	}
	
	public final String SDPAdminRole = "E1";
	public final String NonSDPAdminRole = "E2";
	
	TableModel tmRoleAssign;
	ColumnModel roleCol;
	private void initRoleAssign() {
		List<ColumnModel> cols = new ArrayList<>();
		
		roleCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_AD_User_Roles.Table_Name, I_AD_User_Roles.COLUMNNAME_AD_Role_ID)
				, I_AD_User_Roles.COLUMNNAME_AD_Role_ID
				, getRoles()
				, title -> {return title.getName();}
				, title -> {return title.getAD_Role_ID();}
			).setzClass(MRole.class).required();
		cols.add(roleCol);
		
		roleCol.setEventHandle((event, cellModel) -> {
			@SuppressWarnings("unchecked")
			ListCellModel<MRole> roleCell = (ListCellModel<MRole>) cellModel;
			cellModel.getRowModel().setDataOneRow(roleCell.getSelectedItem());
			cellModel.getRowModel().fillRowDataFromDao(List.of(I_AD_Role.Table_Name));
		});
		
		ColumnModel roleTypeCol = CellModel.getColModelForLabel(
				MasterUtil.getNameOfColTranslated(I_AD_Role.Table_Name, I_AD_Role.COLUMNNAME_RoleType)
				, I_AD_Role.COLUMNNAME_RoleType).
				setTableName(I_AD_Role.Table_Name);
		cols.add(roleTypeCol);
		
		tmRoleAssign = TableModel.getTableBean(TableModel.class, cols, false, I_AD_User_Roles.Table_Name);
		tmRoleAssign.setViewModel(ViewType.VIEW_GRID);
		tmRoleAssign.setSclass("sdpRoleAssign");
		tmRoleAssign.setCommandSetting(CommandSetting.getFullButton());
		tmRoleAssign.setCreateNewRowWhenEmpty(true);
		tmRoleAssign.init();
		
		contactDetailTab.getCompModel().add(tmRoleAssign);
	}
	
	public NavTab getMainTab() {
		return mainTab;
	}

	public void setMainTab(NavTab mainTab) {
		this.mainTab = mainTab;
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(mainTab);
	}
	
	@Override
	public void doSave(String trxName) {
		boolean hasSDPAdminRole = false;
		for (RowModel row : tmRoleAssign.getRows()) {
			@SuppressWarnings("unchecked")
			ListCellModel<MRole> roleCell = (ListCellModel<MRole>)row.get(roleCol);
			if (SDPAdminRole.equals(roleCell.getSelectedItem().getRoleType())) {
				hasSDPAdminRole = true;
			}
		}
		
		if (hasSDPAdminRole) {
			int userID = Env.getAD_User_ID(Env.getCtx());
			MUser_New user = MUser_New.get(Env.getCtx(), userID);
			
			ValueAdaptCellModel bpartnerCell = (ValueAdaptCellModel)tmContactDetail.getRow().get(bpartnerCol);
			MBPartner_New selectedBp = (MBPartner_New)bpartnerCell.getSelectedValue();
			if ((selectedBp == null || selectedBp.getC_BPartner_ID() != user.getC_BPartner_ID()) 
					&& user.getC_BPartner_ID() > 0) {
				tmContactDetail.getRow().get(bpartnerCol).setValue(MBPartner_New.get(Env.getCtx(), user.getC_BPartner_ID()));
			}
		}
		super.doSave(trxName);
	}
}
