package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MTable;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;

import za.co.ntier.api.model.I_C_BPartner;
import za.co.ntier.api.model.I_ZZBankingDetails;
import za.co.ntier.api.model.I_ZZSdfOrganisation;
import za.co.ntier.api.model.I_ZZSdfOrganisation_v;
import za.co.ntier.api.model.MUser_New;
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
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;
import za.co.ntier.webform.sdr.component.tab.bean.OrglinkTabPanel;

public class SdrOrgLinkVM extends BaseAppVM {
	private MenuContextInfo menuContextInfo;
	private FormInfo formInfo;

	private X_ZZSdf sdfPo;
	
	private TableModel orgSearchModel;
	
	private TableModel sdfOrgModel;

	private X_ZZSdfOrganisation sdfOrgPo;

	private TableModel bankDetailModel;

	private X_ZZBankingDetails bankDetailPo;

	private X_C_BPartner orgPo;
	
	@Init
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey) MenuContextInfo menuContextInfo){
		this.setMenuContextInfo(menuContextInfo);
		setFormInfo(new FormInfo(menuContextInfo));
		
		sdfPo = MasterUtil.querySdf(Env.getAD_User_ID(Env.getCtx()));
		if (sdfPo == null)
			MasterUtil.showDialog("ZZOrgLinksSDFNotFound", MasterUtil.fCloseActiveWindow);
		else {
			initForm();
		}
	}

	private void initForm() {
		orgSearchModel = initOrgSearchModel(null, null);
		
		sdfOrgModel = initSdfOrgModel(null, null);
		
		bankDetailModel = initBankDetailModel(null, null);

	}
	
	private TableModel initOrgSearchModel(MUser_New person, X_ZZSdf sdf) {
		List<ColumnModel> cols = new ArrayList<>();
		
		ColumnModel sdlNoCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_ZZ_SDL_No), 
				I_C_BPartner.COLUMNNAME_Value);
		cols.add(sdlNoCol);
		
		sdlNoCol.setEventHandle((event, cellModel) -> {
			if (StringUtils.isBlank((String)cellModel.getValue())) {
				return;
			}
			Query searchOrgQuery = MTable.get(Env.getCtx(), I_C_BPartner.Table_Name)
					.createQuery(String.format("%s = ? AND %s = 'Y'", I_C_BPartner.COLUMNNAME_Value, I_C_BPartner.COLUMNNAME_ZZ_Is_MQA_Sector), null);
			searchOrgQuery.setParameters(cellModel.getValue());
			
			X_C_BPartner sOrgPo = searchOrgQuery.first();
			
			if (sOrgPo == null) {
				MasterUtil.showDialog("ZZOrgLinksNotFoundOrg", MasterUtil.fCloseActiveWindow);
			}else {
				orgPo = sOrgPo;
				orgSearchModel.getRow().setData(orgPo);
				orgSearchModel.reloadDao();
			}
		});
		
		ColumnModel orgNameCol = CellModel.getColModelForText(
				MasterUtil.getNameOfColTranslated(I_ZZSdfOrganisation_v.Table_Name,
						I_ZZSdfOrganisation_v.COLUMNNAME_OrgName), 
				I_C_BPartner.COLUMNNAME_Name).setReadonly(true);
		cols.add(orgNameCol);

		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("orgSearch");

		namesBean.init(null, null, null);

		return namesBean;
	}
	
	private TableModel initSdfOrgModel(MUser_New person, X_ZZSdf sdf) {
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
				});
		cols.add(sdfFunctionCol);

		ColumnModel appointmentProcedureCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure),
				I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedure, MasterUtil.getLkpAppointment(), appointment -> {
					return appointment.getName();
				}, appointment -> {
					return appointment.getValue();
				});
		cols.add(appointmentProcedureCol);

		ColumnModel appointmentOtherCol = CellModel.getColModelForText(
				MasterUtil.getDescOfColTranslated(I_ZZSdfOrganisation.Table_Name,
						I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther),
				I_ZZSdfOrganisation.COLUMNNAME_ZZAppointmentProcedureOther);
		cols.add(appointmentOtherCol);

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
		cols.add(secondarySdfCol);

		UploadColumnModel btAppointmentLetterCol = UploadCellModel.getUploadColumnModel("", null, null,
				"UPLOAD LETTER OF APPOINTMENT");
		cols.add(btAppointmentLetterCol);
		
		UploadColumnModel btBankDetailCol = UploadCellModel.getUploadColumnModel("", null, null, "UPLOAD BANK DETAILS");
		cols.add(btBankDetailCol);

		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("orglink");

		namesBean.init(null, null, null);

		return namesBean;
	}
	
	private TableModel initBankDetailModel(MUser_New person, X_ZZSdf sdf) {
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
				}).required();
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
				}).required();
		cols.add(accountTypeCol);
		
		ColumnModel bankChangeConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsChanged),
				I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsChanged, MasterUtil.getYesNoList(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).required();
		cols.add(bankChangeConfirmCol);
		
		ColumnModel bankConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsCorrect),
				I_ZZBankingDetails.COLUMNNAME_ZZBankDetailsCorrect, MasterUtil.getYesNoList(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).required();
		cols.add(bankConfirmCol);
		
		ColumnModel adminConfirmCol = ListCellModel.getListColumnModel(
				MasterUtil.getDescOfColTranslated(I_ZZBankingDetails.Table_Name,
						I_ZZBankingDetails.COLUMNNAME_ZZAdminDetailsCorrect),
				I_ZZBankingDetails.COLUMNNAME_ZZAdminDetailsCorrect, MasterUtil.getYesNoList(), lkpAccountType -> {
					return lkpAccountType.getName();
				}, lkpAccountType -> {
					return lkpAccountType.getValue();
				}).required();
		cols.add(adminConfirmCol);

		TableModel namesBean = TableModel.getTableBean(TableModel.class, cols, false);
		namesBean.setSclass("bankDetails");

		namesBean.init(null, null);

		return namesBean;
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
	 * @param bankDetailPo the bankDetailPo to set
	 */
	public void setBankDetailPo(X_ZZBankingDetails bankDetailPo) {
		this.bankDetailPo = bankDetailPo;
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
		sdfOrgPo.setZZ_DocStatus(X_ZZSdfOrganisation.ZZ_DOCSTATUS_Pending);
		sdfOrgPo.saveEx(trxName);
	}
	
	@Override
	public List<ISaveForm> getSaveComponents() {
		return List.of();
	}
}
