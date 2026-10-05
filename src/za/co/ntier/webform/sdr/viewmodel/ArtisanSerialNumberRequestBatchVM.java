package za.co.ntier.webform.sdr.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import za.co.ntier.api.model.I_ZZDocumentUpload;
import za.co.ntier.api.model.I_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.I_ZZLearner_v;
import za.co.ntier.api.model.I_ZZ_ArtisanBatchLearner;
import za.co.ntier.api.model.X_ZZDocumentUpload;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZQctoLearnership;
import za.co.ntier.api.model.X_ZZ_ArtisanBatchLearner;
import za.co.ntier.api.model.X_ZZ_ArtisanReqBatch;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.sdr.component.bean.CellModel;
import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;
import za.co.ntier.webform.sdr.component.bean.TableModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;

public class ArtisanSerialNumberRequestBatchVM extends StepAppVM
{

	private X_ZZLearner_v				learnerSelected;
	private X_ZZLearnerQCTOArtisans		artisanProgrammeSelected;
	private X_ZZ_ArtisanBatchLearner	editingBatchLearner;
	private X_ZZ_ArtisanReqBatch		batchHeader;
	private int							adminBpId;

	private TableModel					tmLearnerSelection;
	private TableModel					tmLearnerSelectionInfo;
	private TableModel					tmStagedLearners;
	private TableModel					tmDocumentUpload;

	@Init(superclass = true)
	public void init()
	{
		int loginId = Env.getAD_User_ID(Env.getCtx());
		if (loginId > 0)
		{
			MUser sdpAdmin = new MUser(Env.getCtx(), loginId, null);
			adminBpId = sdpAdmin.getC_BPartner_ID();
		}

		initLearnerSelection();
		initLearnerSelectionInfo();
		initStagedLearnersGrid();
		initDocumentUpload();
	}

	public X_ZZLearner_v getLearnerSelected()
	{
		return learnerSelected;
	}

	public void setLearnerSelected(X_ZZLearner_v learnerSelected)
	{
		this.learnerSelected = learnerSelected;
	}

	public X_ZZLearnerQCTOArtisans getArtisanProgrammeSelected()
	{
		return artisanProgrammeSelected;
	}

	public void setArtisanProgrammeSelected(X_ZZLearnerQCTOArtisans artisanProgrammeSelected)
	{
		this.artisanProgrammeSelected = artisanProgrammeSelected;
	}

	public TableModel getTmLearnerSelection()
	{
		return tmLearnerSelection;
	}

	public TableModel getTmLearnerSelectionInfo()
	{
		return tmLearnerSelectionInfo;
	}

	public TableModel getTmStagedLearners()
	{
		return tmStagedLearners;
	}

	public TableModel getTmDocumentUpload()
	{
		return tmDocumentUpload;
	}

	@Command
	@NotifyChange({ "learnerSelected", "artisanProgrammeSelected", "tmStagedLearners" })
	public void addLearnerToBatch()
	{
		if (learnerSelected == null)
		{
			MasterUtil.showInfoDialog("ZZSelectLearnerError", null);
			return;
		}
		if (artisanProgrammeSelected == null)
		{
			MasterUtil.showInfoDialog("ZZLearnerNoArtisanProgrammeError", null);
			return;
		}

		if (!tmDocumentUpload.validate(true))
		{
			return;
		}

		if (batchHeader == null)
		{
			batchHeader = new X_ZZ_ArtisanReqBatch(Env.getCtx(), 0, null);
			batchHeader.setZZ_SDP_ID(adminBpId);
			batchHeader.setDocStatus(X_ZZLearner_v.ZZ_DOCSTATUS_Draft);
			batchHeader.saveEx();
		}

		X_ZZ_ArtisanBatchLearner line = editingBatchLearner;
		if (line == null)
		{
			line = new X_ZZ_ArtisanBatchLearner(Env.getCtx(), 0, null);
			line.setZZ_ArtisanReqBatch_ID(batchHeader.getZZ_ArtisanReqBatch_ID());
		}

		line.setZZLearner_ID(learnerSelected.getZZLearner_ID());
		line.setZZLearnerQCTOArtisans_ID(artisanProgrammeSelected.getZZLearnerQCTOArtisans_ID());
		line.saveEx();

		RowModel uploadRow = tmDocumentUpload.getRow();
		if (uploadRow != null)
		{
			for (CellModel cell : uploadRow.values())
			{
				if (cell instanceof UploadCellModel)
				{
					UploadCellModel uploadCell = (UploadCellModel) cell;
					if (uploadCell.getFileName() == null || uploadCell.getBytes() != null)
					{
						uploadCell.attachFile(line, null);
					}
				}
			}
		}

		List<PO> lines = new Query(Env.getCtx(), I_ZZ_ArtisanBatchLearner.Table_Name, I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZ_ArtisanReqBatch_ID + " = ?", null)
																																								.setParameters(batchHeader.getZZ_ArtisanReqBatch_ID())
																																								.list();
		tmStagedLearners.reset(lines);

		learnerSelected = null;
		artisanProgrammeSelected = null;
		editingBatchLearner = null;
		if (tmLearnerSelection != null)
			tmLearnerSelection.reset(new ArrayList<>());
		if (tmLearnerSelectionInfo != null)
			tmLearnerSelectionInfo.reset(new ArrayList<>());
		clearDocumentUploads();
	}

	@Command
	public void submitBatch()
	{
		if (batchHeader != null)
		{
			batchHeader.setDocStatus(X_ZZLearner_v.ZZ_DOCSTATUS_Submitted);
			batchHeader.setZZ_SubmittedBy_ID(Env.getAD_User_ID(Env.getCtx()));
			batchHeader.saveEx();
			MasterUtil.showInfoDialog("ZZBatchSubmittedSuccess", MasterUtil.fCloseActiveWindow);

			batchHeader = null;
			clearBatch();
		}
		else
		{
			MasterUtil.showInfoDialog("ZZBatchEmptySubmitError", null);
		}
	}

	@Command
	@NotifyChange({ "learnerSelected", "artisanProgrammeSelected", "tmStagedLearners" })
	public void discardBatch()
	{
		if (batchHeader != null)
		{
			DB.executeUpdate(	"DELETE FROM "	+ I_ZZ_ArtisanBatchLearner.Table_Name + " WHERE " + I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZ_ArtisanReqBatch_ID
								+ " = ?", batchHeader.getZZ_ArtisanReqBatch_ID(), null);
			batchHeader.deleteEx(true);
		}
		batchHeader = null;
		clearBatch();

		MasterUtil.showInfoDialog("ZZDraftBatchDiscarded", MasterUtil.fCloseActiveWindow);
	}

	private void clearBatch()
	{
		learnerSelected = null;
		artisanProgrammeSelected = null;
		editingBatchLearner = null;

		if (tmStagedLearners != null)
			tmStagedLearners.reset(new ArrayList<PO>());
		if (tmLearnerSelection != null)
			tmLearnerSelection.reset(new ArrayList<>());
		if (tmLearnerSelectionInfo != null)
			tmLearnerSelectionInfo.reset(new ArrayList<>());
	}

	private String buildSdpArtisanLearnerFilter()
	{
		if (adminBpId <= 0)
			return "1=0";

		return String.format(
								I_ZZLearner_v.COLUMNNAME_ZZLearner_ID	+ " IN (" +
								"SELECT " + I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID + " " +
								"FROM " + I_ZZLearnerQCTOArtisans.Table_Name + " " +
								"WHERE " + I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZ_SDP_ID + " = %d " +
								"AND " + I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearnerQCTOArtisans_ID + " NOT IN (" +
								"SELECT " + I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZLearnerQCTOArtisans_ID + " " +
								"FROM " + I_ZZ_ArtisanBatchLearner.Table_Name + " " +
								"WHERE " + I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZ_SerialNumber + " IS NOT NULL " +
								"AND " + I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZ_SerialNumber + " != ''))",
								adminBpId);
	}

	private void initLearnerSelection()
	{
		List<ColumnModel> cols = new ArrayList<>();
		ValueAdaptColumnModel chooseLearnerCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																								"Select Learner", null, CellModel.SEARCH_CELL);
		chooseLearnerCol.setShowTitle(false);
		cols.add(chooseLearnerCol);

		chooseLearnerCol.setEventHandle((event, cellModel) -> {
			InfoPanelPara infoPara = InfoPanelPara.getInstance(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZLearner_ID);

			String sdpFilter = buildSdpArtisanLearnerFilter();
			infoPara.setWhereClause(sdpFilter);

			showInfoPanel(infoPara, (obj, infoPanel) -> {
				Object[] objs = (Object[]) obj;
				int learnerIdSelected = (int) objs[0];
				learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerIdSelected, null);

				tmLearnerSelectionInfo.reset(learnerSelected);

				if (tmLearnerSelection != null && tmLearnerSelection.getRow() != null)
				{
					for (ColumnModel colModel : tmLearnerSelection.getRow().keySet())
					{
						if (colModel instanceof ValueAdaptColumnModel && Boolean.FALSE.equals(colModel.getShowTitle()))
						{
							CellModel cell = tmLearnerSelection.getRow().get(colModel);
							cell.setValue(learnerSelected);
						}
					}
				}

				List<PO> artisans = new Query(	Env.getCtx(), I_ZZLearnerQCTOArtisans.Table_Name,
												I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID + " = ? AND " +
																									I_ZZLearnerQCTOArtisans.COLUMNNAME_ZZ_SDP_ID + " = ?", null)
																																								.setParameters(	learnerIdSelected,
																																												adminBpId)
																																								.list();

				if (!artisans.isEmpty())
				{
					artisanProgrammeSelected = (X_ZZLearnerQCTOArtisans) artisans.get(0);
				}
				else
				{
					artisanProgrammeSelected = null;
				}

				clearDocumentUploads();
				BindUtils.postNotifyChange(null, null, ArtisanSerialNumberRequestBatchVM.this, "*");
			});
		});

		tmLearnerSelection = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmLearnerSelection.setViewModel(TableModel.ViewType.VIEW_FORM);
		tmLearnerSelection.init();
	}

	void initLearnerSelectionInfo()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_ZZFirstName),
														I_ZZLearner_v.COLUMNNAME_ZZFirstName);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(I_ZZLearner_v.Table_Name, I_ZZLearner_v.COLUMNNAME_Surname),
											I_ZZLearner_v.COLUMNNAME_Surname);
		cols.add(col);

		ValueAdaptColumnModel idCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																					MasterUtil.getNameOfColTranslated(	I_ZZLearner_v.Table_Name,
																														I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No),
																					I_ZZLearner_v.COLUMNNAME_ZZ_ID_Passport_No,
																					CellModel.LABEL_CELL);

		idCol.setValueFromDaoAdaptHandle(obj -> {
			String idPassport = (String) obj;
			if (idPassport == null || idPassport.isBlank())
			{
				if (learnerSelected != null)
				{
					return learnerSelected.getZZOtherIDNo();
				}
			}
			return idPassport;
		});
		cols.add(idCol);

		tmLearnerSelectionInfo = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearner_v.Table_Name);
		tmLearnerSelectionInfo.setViewModel(TableModel.ViewType.VIEW_GRID);
		tmLearnerSelectionInfo.init();
	}

	private void initStagedLearnersGrid()
	{
		List<ColumnModel> cols = new ArrayList<>();

		cols.add(createLearnerColumn(
										MasterUtil.getNameOfColTranslated(	I_ZZ_ArtisanBatchLearner.Table_Name,
																			I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZLearner_ID),
										I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZLearner_ID));

		cols.add(createLearnershipColumn(
											MasterUtil.getNameOfColTranslated(	I_ZZ_ArtisanBatchLearner.Table_Name,
																				I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZLearnerQCTOArtisans_ID),
											I_ZZ_ArtisanBatchLearner.COLUMNNAME_ZZLearnerQCTOArtisans_ID));

		ColumnModel editCol = CellModel.getColModelForGenericCell("Edit", null, CellModel.BUTTON_CELL);
		editCol.setEventHandle((event, cellModel) -> {
			editStagedLearner(cellModel.getRowModel());
		});
		cols.add(editCol);

		ColumnModel deleteCol = CellModel.getColModelForGenericCell("Delete", null, CellModel.BUTTON_CELL);
		deleteCol.setEventHandle((event, cellModel) -> {
			X_ZZ_ArtisanBatchLearner po = cellModel.getRowModel().getDataOneRow(X_ZZ_ArtisanBatchLearner.class, I_ZZ_ArtisanBatchLearner.Table_Name);
			if (po != null)
			{
				po.deleteEx(true);
				if (editingBatchLearner != null && editingBatchLearner.get_ID() == po.get_ID())
				{
					editingBatchLearner = null;
					learnerSelected = null;
					artisanProgrammeSelected = null;
					if (tmLearnerSelection != null)
						tmLearnerSelection.reset(new ArrayList<>());
					if (tmLearnerSelectionInfo != null)
						tmLearnerSelectionInfo.reset(new ArrayList<>());
					clearDocumentUploads();
					BindUtils.postNotifyChange(null, null, ArtisanSerialNumberRequestBatchVM.this, "*");
				}
			}
			tmStagedLearners.removeRow(cellModel.getRowModel());
		});
		cols.add(deleteCol);

		tmStagedLearners = TableModel.getTableBean(TableModel.class, cols, false, I_ZZ_ArtisanBatchLearner.Table_Name);
		tmStagedLearners.setSclass("srd-ZZ_ArtisanBatchLearner");
		tmStagedLearners.setViewModel(TableModel.ViewType.VIEW_GRID);
		tmStagedLearners.setCreateNewRowWhenEmpty(false);
		tmStagedLearners.init();
	}

	private void initDocumentUpload()
	{
		List<ColumnModel> cols = new ArrayList<>();

		int programId = 0;
		if (getMenuContextInfo() != null)
		{
			if (getMenuContextInfo().getProgramMasterData() != null)
			{
				programId = getMenuContextInfo().getProgramMasterData().getZZ_Program_Master_Data_ID();
			}
			else
			{
				// Fallback: extract UUID directly from Env Context if SdrForm didn't load the
				// program data
				String uuid = Env.getContext(Env.getCtx(), getMenuContextInfo().getWinNo(), WebForm.programMasterDataUUMenuContextKey);
				if (uuid != null && !uuid.isEmpty())
				{
					int fetchedId = DB.getSQLValue(	null,
													"SELECT ZZ_Program_Master_Data_ID FROM ZZ_Program_Master_Data WHERE ZZ_Program_Master_Data_UU = ?",
													uuid);
					if (fetchedId > 0)
					{
						programId = fetchedId;
					}
				}
			}
		}

		List<X_ZZDocumentUpload> docUploads = new Query(Env.getCtx(), I_ZZDocumentUpload.Table_Name,
														I_ZZDocumentUpload.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ? AND "
																										+ I_ZZDocumentUpload.COLUMNNAME_IsActive + " = 'Y'",
														null)
																.setParameters(programId)
																.setOrderBy(I_ZZDocumentUpload.COLUMNNAME_Name)
																.list();

		for (X_ZZDocumentUpload docUpload : docUploads)
		{
			String title = docUpload.getName();
			ColumnModel uploadCol = UploadCellModel.getUploadColumnModel(title, null, null, "Upload").setMandatory(docUpload.isMandatory());
			cols.add(uploadCol);
		}

		tmDocumentUpload = TableModel.getTableBean(TableModel.class, cols, false, I_ZZ_ArtisanBatchLearner.Table_Name);
		tmDocumentUpload.setSclass("srd-batch-doc-upload");
		tmDocumentUpload.setViewModel(TableModel.ViewType.VIEW_FORM);
		tmDocumentUpload.init();
	}

	private void clearDocumentUploads()
	{
		if (tmDocumentUpload != null && tmDocumentUpload.getRow() != null)
		{
			for (CellModel cell : tmDocumentUpload.getRow().values())
			{
				if (cell instanceof UploadCellModel)
				{
					((UploadCellModel) cell).cmdRemoveAttachment();
				}
			}
			tmDocumentUpload.getRow().resetRow();
		}
	}

	private ValueAdaptColumnModel createLearnerColumn(String label, String propertyName)
	{
		ValueAdaptColumnModel col = ValueAdaptCellModel.getValueAdaptColumnModel(label, propertyName, CellModel.LABEL_CELL);
		col.setValueFromDaoAdaptHandle(val -> {
			if (val != null && (Integer) val > 0)
			{
				X_ZZLearner_v l = new X_ZZLearner_v(Env.getCtx(), (Integer) val, null);
				String name = l.getZZFirstName() + " " + l.getSurname();
				String id = l.getZZ_ID_Passport_No();
				if (id == null || id.isBlank())
					id = l.getZZOtherIDNo();
				return name + " (" + (id != null ? id : "") + ")";
			}
			return "";
		});
		return col;
	}

	private ValueAdaptColumnModel createLearnershipColumn(String label, String propertyName)
	{
		ValueAdaptColumnModel col = ValueAdaptCellModel.getValueAdaptColumnModel(label, propertyName, CellModel.LABEL_CELL);
		col.setValueFromDaoAdaptHandle(val -> {
			if (val != null && (Integer) val > 0)
			{
				X_ZZLearnerQCTOArtisans link = new X_ZZLearnerQCTOArtisans(Env.getCtx(), (Integer) val, null);
				if (link.getZZQctoLearnership_ID() > 0)
				{
					X_ZZQctoLearnership l = new X_ZZQctoLearnership(Env.getCtx(), link.getZZQctoLearnership_ID(), null);
					return l.getZZLearnershipTitle();
				}
			}
			return "";
		});
		return col;
	}

	private void editStagedLearner(RowModel row)
	{
		X_ZZ_ArtisanBatchLearner learnerPO = row.getDataOneRow(X_ZZ_ArtisanBatchLearner.class, I_ZZ_ArtisanBatchLearner.Table_Name);
		if (learnerPO == null)
			return;

		editingBatchLearner = learnerPO;
		learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerPO.getZZLearner_ID(), null);
		artisanProgrammeSelected = new X_ZZLearnerQCTOArtisans(Env.getCtx(), learnerPO.getZZLearnerQCTOArtisans_ID(), null);

		if (tmLearnerSelection != null && tmLearnerSelection.getRow() != null)
		{
			for (ColumnModel colModel : tmLearnerSelection.getRow().keySet())
			{
				if (colModel instanceof ValueAdaptColumnModel)
				{
					CellModel cell = tmLearnerSelection.getRow().get(colModel);
					cell.setValue(learnerSelected);
				}
			}
		}

		tmLearnerSelectionInfo.reset(learnerSelected);

		if (tmDocumentUpload != null && tmDocumentUpload.getRow() != null)
		{
			for (CellModel cell : tmDocumentUpload.getRow().values())
			{
				if (cell instanceof UploadCellModel)
				{
					((UploadCellModel) cell).setValueFromDao(learnerPO);
				}
			}
		}

		BindUtils.postNotifyChange(null, null, this, "learnerSelected");
		BindUtils.postNotifyChange(null, null, this, "artisanProgrammeSelected");
	}
}
