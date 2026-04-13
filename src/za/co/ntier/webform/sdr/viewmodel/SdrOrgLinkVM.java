package za.co.ntier.webform.sdr.viewmodel;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.X_C_Bank;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.Event;

import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZBankingDetails;
import za.co.ntier.api.model.I_ZZSdfOrganisation;
import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.api.model.MBPartner_New;
import za.co.ntier.api.model.X_ZZBankingDetails;
import za.co.ntier.api.model.X_ZZSdf;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.component.FormInfo;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.ISaveForm;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.CheckboxCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;

public class SdrOrgLinkVM extends BaseAppVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;

	private X_ZZSdf sdfPo;
	
	private TableModel orgSearchModel;
	
	private TableModel sdfOrgModel;

	private TableModel bankDetailModel;
		
	private X_C_BPartner orgPo;
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of(sdfOrgModel, bankDetailModel);
	}
	
	boolean isEditModel = false;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		
		isEditModel = menuContextInfo.getRecordID() != 0;
		
		sdfPo = MasterUtil.querySdf(Env.getAD_User_ID(Env.getCtx()));
		if (sdfPo == null)
			MasterUtil.showInfoDialog("ZZOrgLinksSDFNotFound", MasterUtil.fCloseActiveWindow);
		else if (sdfPo.getZZMaintainStatus() == null || X_ZZSdf.ZZMAINTAINSTATUS_No.equalsIgnoreCase(sdfPo.getZZMaintainStatus())){
			MasterUtil.showInfoDialog("ZZOrgLinksSDFWrongStatus", MasterUtil.fCloseActiveWindow);
		}else {
			initForm();
		}
	}

	private void initForm() {
		
		orgSearchModel = initOrgSearchModel();
		
		sdfOrgModel = initSdfOrgModel();
		
		bankDetailModel = initBankDetailModel();
		
		if (isEditModel && sdfOrgModel.getRow().getData() != null) {
			String sdfRoleType = ((X_ZZSdfOrganisation)sdfOrgModel.getRow().getData()).getZZSdfRoleType();
			
			sdfOrgModel.getRow().get(btBankDetailCol).setVisible(!X_ZZSdfOrganisation.ZZSDFROLETYPE_SecondarySDF.equals(sdfRoleType));
			
			bankDetailModel.setUsed(!X_ZZSdfOrganisation.ZZSDFROLETYPE_SecondarySDF.equals(sdfRoleType));
		}
			

	}
	
	BiConsumer<Event, CellModel> sdlnoChangeHandle = (event, cellModel) -> {
		if (StringUtils.isBlank((String)cellModel.getValue())) {
			return;
		}
		Query searchOrgQuery = MTable.get(Env.getCtx(), I_C_BPartner.Table_Name)
				.createQuery(String.format("%s = ? AND %s = 'Y'", I_C_BPartner.COLUMNNAME_Value, I_C_BPartner.COLUMNNAME_ZZ_Is_MQA_Sector), null);
		searchOrgQuery.setParameters(cellModel.getValue());
		
		MBPartner_New sOrgPo = searchOrgQuery.first();
		
		if (sOrgPo == null) {
			MasterUtil.showInfoDialog("ZZOrgLinksNotFoundOrg", MasterUtil.fCloseActiveWindow);
		}else {
			orgPo = sOrgPo;
			orgSearchModel.getRow().setData(orgPo);
			orgSearchModel.reloadDao();
		}
	};
	
	private TableModel initOrgSearchModel() {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel sdlNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZ_SDL_No), 
				I_C_BPartner.COLUMNNAME_Value);
		sdlNoCol.setReadonly(isEditModel);
		cols.add(sdlNoCol);
		
		if (!isEditModel) {
			sdlNoCol.setEventHandle(sdlnoChangeHandle);
		}
		
		ColumnModel orgNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdfOrganisation_v.Table_Name,
						I_ZZSdfOrganisation_v.COLUMNNAME_OrgName), 
				I_C_BPartner.COLUMNNAME_Name).setReadonly(true);
		cols.add(orgNameCol);

		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("orgSearch");

		namesBean.init();

		return namesBean;
	}
	
	ListColumnModel<ValueNamePair> sdrRoleTyleCol = null;
	ColumnModel btBankDetailCol;
	private TableModel initSdfOrgModel() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel actingForEmployerCol = CheckboxCellModel.getCheckboxColModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZActingForEmployer),
				I_ZZSdfOrganisation.COLUMNNAME_ZZActingForEmployer);
		cols.add(actingForEmployerCol);

		ColumnModel sdfFunctionCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZSdfFunction),
				I_ZZSdfOrganisation.COLUMNNAME_ZZSdfFunction, MasterUtil.getLkpFunctionLists(), sdfFunction -> {
					return sdfFunction.getName();
				}, sdfFunction -> {
					return sdfFunction.getValue();
				}).setzClass(ValueNamePair.class);
		cols.add(sdfFunctionCol);

		ColumnModel appointmentProcedureCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure),
				I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure, MasterUtil.getLkpAppointment(), appointment -> {
					return appointment.getName();
				}, appointment -> {
					return appointment.getValue();
				}).setzClass(ValueNamePair.class);
		cols.add(appointmentProcedureCol);

		ColumnModel appointmentOtherCol = CellModel.getColModelForText(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther),
				I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther);
		cols.add(appointmentOtherCol);
		
		sdrRoleTyleCol = ListCellModel.getListColumnModel(
				"SDF Role", 
				I_ZZSdfOrganisation.COLUMNNAME_ZZSdfRoleType,
				MasterUtil.getSdfRoleType(),
				ref -> {
					return ref.getName();
				},
				ref -> {return ref.getValue();},
				CellModel.RADIO_CELL
				).setzClass(ValueNamePair.class);
			sdrRoleTyleCol.required();
			sdrRoleTyleCol.setTableName(I_ZZSdfOrganisation.Table_Name);
		
		cols.add(sdrRoleTyleCol);
		
		sdrRoleTyleCol.setEventHandle((event, cellModel) -> {
			Object roleTypeValue = cellModel.getValue();
			cellModel.getRowModel().get(btBankDetailCol).setVisible(!X_ZZSdfOrganisation.ZZSDFROLETYPE_SecondarySDF.equals(roleTypeValue));
			bankDetailModel.setUsed(!X_ZZSdfOrganisation.ZZSDFROLETYPE_SecondarySDF.equals(roleTypeValue));
			
		});
			
/*
		ColumnModel replacingPrimaryCol = CheckboxCellModel.getCheckboxColModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZReplacingPrimarySDF),
				I_ZZSdfOrganisation.COLUMNNAME_ZZReplacingPrimarySDF);
		cols.add(replacingPrimaryCol);

		ColumnModel secondarySdfCol = CheckboxCellModel
				.getCheckboxColModel(
						MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
								I_ZZSdfOrganisation.COLUMNNAME_ZZSecondarySdf),
						I_ZZSdfOrganisation.COLUMNNAME_ZZSecondarySdf);
		
		secondarySdfCol.setComposeValidator(cellModel -> {
				CheckboxCellModel replacingPrimaryCell = (CheckboxCellModel)cellModel.getRowModel().get(replacingPrimaryCol);
				CheckboxCellModel secondarySdfCell = (CheckboxCellModel)cellModel;
				
				if ((secondarySdfCell.getValue() == null || !((Boolean)secondarySdfCell.getValue())) && 
						(replacingPrimaryCell.getValue() == null || !((Boolean)replacingPrimaryCell.getValue()))) {
					cellModel.getValidateMsgs().add(Msg.getMsg(Env.getCtx(), "ZZValidateOrgLinkRequiedSDFType"));
					return Boolean.FALSE;
				}
				
				if ((secondarySdfCell.getValue() != null && (Boolean)secondarySdfCell.getValue()) && 
						(replacingPrimaryCell.getValue() != null && (Boolean)replacingPrimaryCell.getValue())) {
					cellModel.getValidateMsgs().add(Msg.getMsg(Env.getCtx(), "ZZValidateOrgLinkWrongSDFType"));
					return Boolean.FALSE;
				}
				
				return Boolean.TRUE;
			});
		cols.add(secondarySdfCol);
*/
			
		ColumnModel btAppointmentLetterCol = UploadCellModel.getUploadColumnModel("", null, null,
				"UPLOAD LETTER OF APPOINTMENT")
			.required()
			.setShowTitle(false);
		cols.add(btAppointmentLetterCol);
		
		btBankDetailCol = UploadCellModel.getUploadColumnModel("", null, null, "UPLOAD BANK DETAILS")
			.required()
			.setShowTitle(false);
		cols.add(btBankDetailCol);

		TableModel tmSdrOrgLink = TableModel.getTableBean(TableModel.class, cols, false);
		tmSdrOrgLink.setSclass("orglink");

		tmSdrOrgLink.setPoSupplier(t -> {
			if (orgPo == null) {
				throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZOrgLinkMissingOrg"));
			}
			
			X_ZZSdfOrganisation sdfOrgPo = new X_ZZSdfOrganisation(Env.getCtx(), 0, null);
			sdfOrgPo.setZZ_DocStatus(X_ZZSdfOrganisation.ZZ_DOCSTATUS_Draft);
			
			return sdfOrgPo;
				
		});
		
		tmSdrOrgLink.setBeforeSave((po, rowModel) -> {
			X_ZZSdfOrganisation sdfOrgPo = (X_ZZSdfOrganisation)po;
			sdfOrgPo.setC_BPartner_ID(orgPo.getC_BPartner_ID());
			sdfOrgPo.setZZSdf_ID(sdfPo.getZZSdf_ID());
			return true;
		});
		
		List<PO> savedSdrOrgLinks = null;
		if (isEditModel) {
			X_ZZSdfOrganisation sdfOrgPo = new X_ZZSdfOrganisation(Env.getCtx(), menuContextInfo.getRecordID(), null);
			savedSdrOrgLinks = List.of(sdfOrgPo);
			
			orgPo =  MBPartner_New.get(Env.getCtx(), sdfOrgPo.getC_BPartner_ID(), null);
			
			orgSearchModel.getRow().setData(orgPo);
			orgSearchModel.reloadDao();
		}
		
		tmSdrOrgLink.init(savedSdrOrgLinks);

		return tmSdrOrgLink;
	}
	
	private TableModel initBankDetailModel() {
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel warningBankDetailUploadCol = CellModel.getColModelForLabel(
				"Banking details uploaded must be signed off by the Organsation CFO as true and correct");
		cols.add(warningBankDetailUploadCol);

		ColumnModel accountHolderCol = CellModel
				.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
								I_ZZBankingDetails.COLUMNNAME_ZZAccountHolder),
						I_ZZBankingDetails.COLUMNNAME_ZZAccountHolder)
				.required();
		cols.add(accountHolderCol);
		
		ColumnModel bankNameCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_C_Bank_ID),
				I_ZZBankingDetails.COLUMNNAME_C_Bank_ID, MasterUtil.getBanks(), bank -> {
					return bank.getName();
				}, bank -> {
					return bank.getC_Bank_ID();
				}).setzClass(X_C_Bank.class).required();
		cols.add(bankNameCol);

		ColumnModel branchCodeCol = CellModel
				.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
								I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number),
						I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Number)
				.required();
		cols.add(branchCodeCol);
		
		ColumnModel branchNameCol = CellModel
				.getColModelForText(
						MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
								I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name),
						I_ZZBankingDetails.COLUMNNAME_ZZ_Branch_Name)
				.required();
		cols.add(branchNameCol);

		ColumnModel accountNoCol = CellModel
				.getColModelForText(MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_AccountNo), I_ZZBankingDetails.COLUMNNAME_AccountNo)
				.required();
		cols.add(accountNoCol);

		ColumnModel accountTypeCol = ListCellModel.getListColumnModel(
				MasterUtil.getNameOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_AccountType),
				I_ZZBankingDetails.COLUMNNAME_AccountType, MasterUtil.getLkpAccountType(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(accountTypeCol);
		
		ColumnModel bankChangeConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsChanged),
				I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsChanged, MasterUtil.getYesNoList(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(bankChangeConfirmCol);
		
		ColumnModel bankConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsCorrect),
				I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsCorrect, MasterUtil.getYesNoList(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(bankConfirmCol);
		
		ColumnModel adminConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_ZZAdminDetailsCorrect),
				I_ZZBankingDetails.COLUMNNAME_ZZAdminDetailsCorrect, MasterUtil.getYesNoList(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).setzClass(ValueNamePair.class).required();
		cols.add(adminConfirmCol);

		
		
		TableModel tmBank = TableModel.getTableBean(TableModel.class, cols, false);
		tmBank.setSclass("bankDetails");

		tmBank.setPoSupplier(t -> {			
			X_ZZBankingDetails bankDetailPo = new X_ZZBankingDetails(Env.getCtx(), 0, null);
			return bankDetailPo;
		});
		
		tmBank.setBeforeSave((po, rowModel) -> {
			X_ZZSdfOrganisation sdfOrgPo = (X_ZZSdfOrganisation)sdfOrgModel.getRow().getData();
			((X_ZZBankingDetails)po).setZZSdfOrganisation_ID(sdfOrgPo.getZZSdfOrganisation_ID());
			return true;
		});
		
		List<PO> savedBanks = null;
		if (isEditModel) {
			Query queryBankDetailQuery =
				MTable.get(Env.getCtx(), I_ZZBankingDetails.Table_Name).createQuery(String.format("%s = ?",
						I_ZZBankingDetails.COLUMNNAME_ZZSdfOrganisation_ID), null);
			
				queryBankDetailQuery.setParameters(menuContextInfo.getRecordID());
				
				X_ZZBankingDetails bankDetailPo = queryBankDetailQuery.first();
				
				if (bankDetailPo != null)
					savedBanks = List.of(bankDetailPo);
		}
		
		tmBank.init(savedBanks);

		return tmBank;
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
	 * @return the bankDetailModel
	 */
	public TableModel getBankDetailModel() {
		return bankDetailModel;
	}

	/**
	 * @param bankDetailModel the bankDetailModel to set
	 */
	public void setBankDetailModel(TableModel bankDetailModel) {
		this.bankDetailModel = bankDetailModel;
	}


	/**
	 * @return the sdfOrgModel
	 */
	public TableModel getSdfOrgModel() {
		return sdfOrgModel;
	}

	/**
	 * @param sdfOrgModel the sdfOrgModel to set
	 */
	public void setSdfOrgModel(TableModel sdfOrgModel) {
		this.sdfOrgModel = sdfOrgModel;
	}
	
	/**
	 * @return the orgSearchModel
	 */
	public TableModel getOrgSearchModel() {
		return orgSearchModel;
	}

	/**
	 * @param orgSearchModel the orgSearchModel to set
	 */
	public void setOrgSearchModel(TableModel orgSearchModel) {
		this.orgSearchModel = orgSearchModel;
	}
	
	@Override
	public boolean isSupportSubmit() {
		return true;
	}
	
	@Override
	public void doSubmit(String trxName) {
		super.doSubmit(trxName);
		X_ZZSdfOrganisation sdfOrgPo = ((X_ZZSdfOrganisation)sdfOrgModel.getRow().getData());
		sdfOrgPo.setZZ_DocStatus(X_ZZSdfOrganisation.ZZ_DOCSTATUS_Pending);
		sdfOrgPo.setZZ_Submission_Date(Timestamp.valueOf(LocalDateTime.now()));
		sdfOrgPo.saveEx(trxName);
	}

	@Override
	public Object getMainApp() {
		return null;
	}
	
	@Override
	protected void showResult(boolean isSubmit) {
		if (isSubmit) {
			MasterUtil.showInfoDialog("ZZRequestOrgLinkSubmited", MasterUtil.fCloseActiveWindow);
			int loginId = Env.getAD_User_ID(Env.getCtx());
			MUser receiver = MUser.get(loginId);
			
			MasterUtil.sentEmailSdf("96dc984d-af82-4dd3-9b10-a59386a5a03b", sdfOrgModel.getRow().getData(), receiver);
		}else {
			MasterUtil.showInfoDialog("ZZRequestOrgLinkSaved", MasterUtil.fCloseActiveWindow);
			
		}
		
	}
	
	@Override
	public void doSave(String trxName) {
		if (orgPo == null) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZOrgLinksMissingOrg"));
		}
		
		int sdfOrganisationID = isEditModel? menuContextInfo.getRecordID():0;
		
		Object selectedRole = sdfOrgModel.getRow().get(sdrRoleTyleCol).getValue();
		
		// check org already link to other sdf with this role type
		Query querySameRoleTypeOnOtherSdf = MTable.get(Env.getCtx(), I_ZZSdfOrganisation.Table_Name)
				.createQuery(String.format("%s <> ? AND %s = ? AND %s = ? AND %s <> ?", 
						I_ZZSdfOrganisation.COLUMNNAME_ZZSdfOrganisation_ID,
						I_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID, 
						I_ZZSdfOrganisation.COLUMNNAME_ZZSdfRoleType,
						I_ZZSdfOrganisation.COLUMNNAME_ZZ_DocStatus)
						, null);
		
		querySameRoleTypeOnOtherSdf.setParameters(sdfOrganisationID, 
				orgPo.getC_BPartner_ID(), 
				selectedRole, 
				X_ZZSdfOrganisation.ZZ_DOCSTATUS_Delinked);
		
		if (querySameRoleTypeOnOtherSdf.list().size() > 0) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZRequestOrgLinkRoleTypeUsed"));
		}
		
		// check org already link to this sdf
		Query queryAlreadyLinkToThisSdf = MTable.get(Env.getCtx(), I_ZZSdfOrganisation.Table_Name)
				.createQuery(String.format("%s <> ? AND %s = ? AND %s = ? AND %s <> ?", 
						I_ZZSdfOrganisation.COLUMNNAME_ZZSdfOrganisation_ID,
						I_ZZSdfOrganisation.COLUMNNAME_C_BPartner_ID, 
						I_ZZSdfOrganisation.COLUMNNAME_ZZSdf_ID,
						I_ZZSdfOrganisation.COLUMNNAME_ZZ_DocStatus)
						, null);
		
		queryAlreadyLinkToThisSdf.setParameters(sdfOrganisationID, 
				orgPo.getC_BPartner_ID(), 
				sdfPo.getZZSdf_ID(),
				X_ZZSdfOrganisation.ZZ_DOCSTATUS_Delinked);
		
		if (queryAlreadyLinkToThisSdf.list().size() > 0) {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZRequestOrgLinkSdfUsed"));
		}
		
		super.doSave(trxName);
	}
	
}
