package za.co.ntier.webform.sdr.viewmodel;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.compiere.model.I_AD_User;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Year;
import org.compiere.model.MBPartner;
import org.compiere.model.MColumn;
import org.compiere.model.MRefList;
import org.compiere.model.MReference;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.MYear;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.model.X_C_BPartner;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.ValueNamePair;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listitem;

import za.co.ntier.api.model.I_ZZLearnerDGProgramme;
import za.co.ntier.api.model.I_ZZRegistrationDocs;
import za.co.ntier.api.model.I_ZZ_Program_Master_Data;
import za.co.ntier.api.model.X_ZZLearner;
import za.co.ntier.api.model.X_ZZLearnerDGProgramme;
import za.co.ntier.api.model.X_ZZLearnerLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOArtisans;
import za.co.ntier.api.model.X_ZZLearnerQCTOLearnership;
import za.co.ntier.api.model.X_ZZLearnerQCTOSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearnerSkillsProgramme;
import za.co.ntier.api.model.X_ZZLearner_v;
import za.co.ntier.api.model.X_ZZLearnership;
import za.co.ntier.api.model.X_ZZPerson;
import za.co.ntier.api.model.X_ZZQctoLearnership;
import za.co.ntier.api.model.X_ZZQctoSkillsProgramme;
import za.co.ntier.api.model.X_ZZRegistrationDocs;
import za.co.ntier.api.model.X_ZZSkillsProgramme;
import za.co.ntier.api.model.X_ZZ_Program_Master_Data;
import za.co.ntier.webform.form.AttachmentUtil;
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
import za.co.ntier.webform.sdr.component.bean.TableModel.TitleInfo;
import za.co.ntier.webform.sdr.component.bean.TableModel.ViewType;
import za.co.ntier.webform.sdr.component.bean.cell.DateCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ListCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.PresetTitleCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.UploadCellModel;
import za.co.ntier.webform.sdr.component.bean.cell.ValueAdaptCellModel;
import za.co.ntier.webform.sdr.component.bean.column.ListColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.PresetTitleColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.UploadColumnModel;
import za.co.ntier.webform.sdr.component.bean.column.ValueAdaptColumnModel;

/**
 * ViewModel for Learner Qualification Registration.
 * Follows the step-based flow:
 * 1. Select the Learner and Intervention
 * 2. Fill in details for the selected Intervention and Save
 */
public class LearnerQualficationRegVM extends StepAppVM
{
	public static final String								INTERVENTION_DG_PROGRAMMES	= "Discretionary Grant Programmes";
	private static final String							INTERVENTION_QCTO_ARTISANS	= "QCTO Artisans";
	private static final String							INTERVENTION_QCTO_LEARNERSHIPS = "QCTO Learnerships";
	private static final String							INTERVENTION_QCTO_SKILLS	= "QCTO Skills Programmes";
	private static final String							INTERVENTION_LEARNERSHIPS	= "Learnerships";
	private static final String							INTERVENTION_SKILLS			= "Skills Programmes";
	private static final String							INTERVENTION_QCTO_LEARNERSHIP = "QCTO Learnership";
	private static final String							INTERVENTION_QCTO_SKILL		= "QCTO Skills Programme";
	private static final String							INTERVENTION_LEARNERSHIP	= "Learnership";
	private static final String							INTERVENTION_SKILL			= "Skills Programme";

	public static final String								UU_QCTO_ARTISANS			= "fcfe3129-e217-42bc-aea1-1c0975782d62";
	public static final String								UU_QCTO_LEARNERSHIPS		= "b1f00823-76bf-4bfa-b389-51e6826697e8";
	public static final String								UU_QCTO_SKILLS_PROGRAMMES	= "8b1e7369-4763-4085-a458-933fbff0e1b8";
	public static final String								UU_LEARNERSHIPS				= "58f0fbf5-f57e-48f9-af65-4910a01a045a";
	public static final String								UU_SKILLS_PROGRAMMES		= "2832696b-6133-4d56-898b-73b67ae62c12";

	static final List<InterventionConfig> INTERVENTION_CONFIGS = List.of(
		new InterventionConfig(
			INTERVENTION_QCTO_ARTISANS,
			X_ZZLearnerQCTOArtisans.Table_Name,
			X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearnerQCTOArtisans_ID,
			X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID,
			X_ZZQctoLearnership.Table_Name,
			X_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID,
			X_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode,
			X_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle,
			UU_QCTO_ARTISANS,
			id -> new X_ZZQctoLearnership(Env.getCtx(), id, null)),
		new InterventionConfig(
			INTERVENTION_QCTO_LEARNERSHIPS,
			X_ZZLearnerQCTOLearnership.Table_Name,
			X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZLearnerQCTOLearnership_ID,
			X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID,
			X_ZZQctoLearnership.Table_Name,
			X_ZZQctoLearnership.COLUMNNAME_ZZQctoLearnership_ID,
			X_ZZQctoLearnership.COLUMNNAME_ZZLearnershipCode,
			X_ZZQctoLearnership.COLUMNNAME_ZZLearnershipTitle,
			UU_QCTO_LEARNERSHIPS,
			id -> new X_ZZQctoLearnership(Env.getCtx(), id, null),
			INTERVENTION_QCTO_LEARNERSHIP),
		new InterventionConfig(
			INTERVENTION_QCTO_SKILLS,
			X_ZZLearnerQCTOSkillsProgramme.Table_Name,
			X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZLearnerQCTOSkillsProgramme_ID,
			X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID,
			X_ZZQctoSkillsProgramme.Table_Name,
			X_ZZQctoSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID,
			X_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode,
			X_ZZQctoSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle,
			UU_QCTO_SKILLS_PROGRAMMES,
			id -> new X_ZZQctoSkillsProgramme(Env.getCtx(), id, null),
			INTERVENTION_QCTO_SKILL),
		new InterventionConfig(
			INTERVENTION_LEARNERSHIPS,
			X_ZZLearnerLearnership.Table_Name,
			X_ZZLearnerLearnership.COLUMNNAME_ZZLearnerLearnership_ID,
			X_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID,
			X_ZZLearnership.Table_Name,
			X_ZZLearnership.COLUMNNAME_ZZLearnership_ID,
			X_ZZLearnership.COLUMNNAME_ZZLearnershipCode,
			X_ZZLearnership.COLUMNNAME_ZZLearnershipTitle,
			UU_LEARNERSHIPS,
			id -> new X_ZZLearnership(Env.getCtx(), id, null),
			INTERVENTION_LEARNERSHIP),
		new InterventionConfig(
			INTERVENTION_SKILLS,
			X_ZZLearnerSkillsProgramme.Table_Name,
			X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZLearnerSkillsProgramme_ID,
			X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID,
			X_ZZSkillsProgramme.Table_Name,
			X_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID,
			X_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeCode,
			X_ZZSkillsProgramme.COLUMNNAME_ZZSkillsProgrammeTitle,
			UU_SKILLS_PROGRAMMES,
			id -> new X_ZZSkillsProgramme(Env.getCtx(), id, null),
			INTERVENTION_SKILL));

	static final class InterventionConfig
	{
		final String displayName;
		final String learnerTable;
		final String learnerKeyColumn;
		final String learnerProgrammeColumn;
		final String programmeTable;
		final String programmeKeyColumn;
		final String programmeCodeColumn;
		final String programmeTitleColumn;
		final String programmeMasterUuid;
		final Function<Integer, PO> programmeLoader;
		final List<String> aliases;

		InterventionConfig(	String displayName, String learnerTable, String learnerKeyColumn,
							String learnerProgrammeColumn, String programmeTable, String programmeKeyColumn,
							String programmeCodeColumn, String programmeTitleColumn, String programmeMasterUuid,
							Function<Integer, PO> programmeLoader, String... aliases)
		{
			this.displayName = displayName;
			this.learnerTable = learnerTable;
			this.learnerKeyColumn = learnerKeyColumn;
			this.learnerProgrammeColumn = learnerProgrammeColumn;
			this.programmeTable = programmeTable;
			this.programmeKeyColumn = programmeKeyColumn;
			this.programmeCodeColumn = programmeCodeColumn;
			this.programmeTitleColumn = programmeTitleColumn;
			this.programmeMasterUuid = programmeMasterUuid;
			this.programmeLoader = programmeLoader;
			this.aliases = Arrays.asList(aliases);
		}

		boolean matches(String interventionValue)
		{
			if (interventionValue == null)
				return false;
			if (displayName.equalsIgnoreCase(interventionValue) || learnerTable.equalsIgnoreCase(interventionValue))
				return true;
			return aliases.stream().anyMatch(alias -> alias.equalsIgnoreCase(interventionValue));
		}

		String getJoinClause()
		{
			String alias = programmeTable.startsWith("ZZQcto") ? "q" : "p";
			return "LEFT JOIN " + programmeTable + " " + alias + " ON " + alias + "." + programmeKeyColumn + " = "
					+ learnerTable + "." + learnerProgrammeColumn;
		}
	}

	private TableModel										tmLearnerSelection;
	private TableModel										tmLearnerSelectionInfo;
	private TableModel										tmRegistration;
	private ColumnModel										actionCol;

	private TableModel										tmDGProgrammeDetails;
	private TableModel										tmDGDocuments;
	private final DaoManage								dgDaoManage					= new DaoManage();
	private ColumnModel										colProgMasterData;
	private ValueAdaptColumnModel							colFinancialYear;
	private ValueAdaptColumnModel							colLeadSdp;
	private ValueAdaptColumnModel							colLeadSdpContact;

	private PresetTitleColumnModel<X_ZZRegistrationDocs>	dgDocumentNameCol;
	private UploadColumnModel								dgUploadCol;
	private X_ZZ_Program_Master_Data						selectedDGProgramme;
	private boolean											dgProgrammeActive			= false;
	private boolean											isDgSubmit					= false;
	private int												dgSelectedTabIndex			= 0;

	private TableModel										tmRegistrationDocuments;
	private PresetTitleColumnModel<X_ZZRegistrationDocs>	regDocumentNameCol;
	private UploadColumnModel								regUploadCol;
	private int												regSelectedTabIndex			= 0;

	private ValueNamePair									selectedIntervention;
	private X_ZZLearner_v									learnerSelected;
	private X_ZZLearner										learner;
	private X_ZZPerson										personSelected;

	private int												adminBpId;
	private int												loginId;

	private final DaoManage								daoManage					= new DaoManage();

	public TableModel getTmLearnerSelection()
	{
		return tmLearnerSelection;
	}

	public void setTmLearnerSelection(TableModel tmLearnerSelection)
	{
		this.tmLearnerSelection = tmLearnerSelection;
	}

	public TableModel getTmLearnerSelectionInfo()
	{
		return tmLearnerSelectionInfo;
	}

	public void setTmLearnerSelectionInfo(TableModel tmLearnerSelectionInfo)
	{
		this.tmLearnerSelectionInfo = tmLearnerSelectionInfo;
	}

	public TableModel getTmRegistration()
	{
		return tmRegistration;
	}

	public void setTmRegistration(TableModel tmRegistration)
	{
		this.tmRegistration = tmRegistration;
	}

	public ValueNamePair getSelectedIntervention()
	{
		return selectedIntervention;
	}

	public X_ZZLearner_v getLearnerSelected()
	{
		return learnerSelected;
	}

	public X_ZZPerson getPersonSelected()
	{
		return personSelected;
	}

	@Override
	public Object getMainApp()
	{
		return null;
	}

	@Override
	public List<DaoManage> getDaoManages()
	{
		return List.of(daoManage);
	}

	@Override
	public List<ISaveForm> getSaveComponents()
	{
		List<ISaveForm> saves = new ArrayList<>();
		if (tmRegistration != null)
		{
			saves.add(tmRegistration);
		}
		return saves;
	}

	@Override
	protected void showResult(boolean isSubmit)
	{
		if (dgProgrammeActive)
		{
			if (isDgSubmit || isSubmit)
			{
				String msg = Msg.getMsg(Env.getCtx(), "ZZLearnerRegisterSuccess", true);
				if (msg == null || msg.isBlank() || "ZZLearnerRegisterSuccess".equals(msg))
				{
					msg = "Registered successfully";
				}
				MasterUtil.showInfoDialog("Success", List.of(msg), MasterUtil.fCloseActiveWindow);
			}
			else
			{
				MasterUtil.showInfoDialog("Draft application saved successfully.", MasterUtil.fCloseActiveWindow);
			}
			return;
		}

		String msg = Msg.getMsg(Env.getCtx(), "ZZLearnerRegisterSuccess", true);
		if (msg == null || msg.isBlank() || "ZZLearnerRegisterSuccess".equals(msg))
		{
			msg = "Registered successfully";
		}
		MasterUtil.showInfoDialog("Success", List.of(msg), MasterUtil.fCloseActiveWindow);
	}

	@Init(superclass = true)
	public void init(@ExecutionArgParam(WebForm.menuContextInfoKey)
	MenuContextInfo menuContextInfo)
	{
		loginId = Env.getAD_User_ID(Env.getCtx());
		if (loginId > 0)
		{
			MUser sdpAdmin = new MUser(Env.getCtx(), loginId, null);
			adminBpId = sdpAdmin.getC_BPartner_ID();
		}

		setFormInfo(new FormInfo(menuContextInfo));
		initStep("selectRegistration");
		initLearnerSelection();
		initLearnerSelectionInfo();
		initDGProgrammeDetails();
		initDGDocuments();
		initRegistrationDocuments();

		if (menuContextInfo != null && menuContextInfo.getRecordID() > 0)
		{
			int learnerId = menuContextInfo.getRecordID();
			loadLearner(learnerId);

			if (tmLearnerSelection != null && tmLearnerSelection.getRow() != null)
			{
				for (ColumnModel col : tmLearnerSelection.getRow().keySet())
				{
					if (col instanceof ValueAdaptColumnModel && Boolean.FALSE.equals(col.getShowTitle()))
					{
						CellModel cell = tmLearnerSelection.getRow().get(col);
						cell.setValue(learnerSelected != null ? learnerSelected : learner);
					}
				}
			}
		}
	}

	public int getSelectedLearnerId()
	{
		if (learnerSelected != null && learnerSelected.getZZLearner_ID() > 0)
			return learnerSelected.getZZLearner_ID();
		if (learner != null && learner.getZZLearner_ID() > 0)
			return learner.getZZLearner_ID();
		return 0;
	}

	private void loadLearner(int learnerId)
	{
		learner = new X_ZZLearner(Env.getCtx(), learnerId, null);
		if (learner.getZZPerson_ID() > 0)
		{
			personSelected = new X_ZZPerson(Env.getCtx(), learner.getZZPerson_ID(), null);
		}
		try
		{
			learnerSelected = new X_ZZLearner_v(Env.getCtx(), learnerId, null);
		}
		catch (Exception e)
		{
			log.warning("Could not load ZZLearner_v view (" + e.getMessage() + ")");
		}
		if (personSelected != null)
		{
			tmLearnerSelectionInfo.reset(personSelected);
		}
		else if (learnerSelected != null)
		{
			tmLearnerSelectionInfo.reset(learnerSelected);
		}
		BindUtils.postNotifyChange(null, null, tmLearnerSelectionInfo, "rows");

		dgProgrammeActive = false;
		isDgSubmit = false;
		selectedDGProgramme = null;
		BindUtils.postNotifyChange(null, null, this, "dgProgrammeActive");
		BindUtils.postNotifyChange(null, null, this, "selectedDGProgramme");

		BindUtils.postNotifyChange(null, null, this, "*");
	}

	public List<ValueNamePair> getFilteredInterventionList()
	{
		List<String> allowedOrder = new ArrayList<>();
		for (InterventionConfig config : INTERVENTION_CONFIGS)
		{
			allowedOrder.add(config.displayName);
		}
		allowedOrder.add(INTERVENTION_DG_PROGRAMMES);

		List<ValueNamePair> allInterventions = MasterUtil.getLkpInterventionList();
		List<ValueNamePair> filtered = new ArrayList<>();

		for (String target : allowedOrder)
		{
			ValueNamePair found = null;
			if (allInterventions != null)
			{
				for (ValueNamePair vnp : allInterventions)
				{
					if (target.equalsIgnoreCase(vnp.getValue()) || target.equalsIgnoreCase(vnp.getName()))
					{
						found = vnp;
						break;
					}
					if (target.startsWith("Skills Programme") && (vnp.getValue().startsWith("Skills Programme") || vnp.getName().startsWith(
																																			"Skills Programme")))
					{
						found = vnp;
						break;
					}
				}
			}
			if (found != null && !filtered.contains(found))
			{
				filtered.add(found);
			}
			else if (found == null)
			{
				filtered.add(new ValueNamePair(target, target));
			}
		}

		return filtered;
	}

	private String buildSdpLearnerFilter()
	{
		if (adminBpId <= 0 && loginId <= 0)
		{
			return "1=0";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(X_ZZLearner_v.Table_Name).append(".").append(X_ZZLearner_v.COLUMNNAME_IsActive).append(" = 'Y'");
		sb.append(" AND ").append(X_ZZLearner_v.Table_Name).append(".").append(X_ZZLearner_v.COLUMNNAME_ZZLearner_ID).append(" IN (");

		List<String> subQueries = new ArrayList<>();
		if (loginId > 0)
		{
			subQueries.add(String.format(	"SELECT l.%s FROM %s l WHERE l.%s = %d OR l.CreatedBy = %d",
											X_ZZLearner.COLUMNNAME_ZZLearner_ID, X_ZZLearner.Table_Name,
											X_ZZLearner.COLUMNNAME_ZZ_SDPAdmin_ID, loginId,
											loginId));
		}
		if (adminBpId > 0)
		{
			subQueries.add(String.format(	"SELECT l.%s FROM %s l WHERE l.%s IN (SELECT AD_User_ID FROM AD_User WHERE C_BPartner_ID = %d) OR l.CreatedBy IN (SELECT AD_User_ID FROM AD_User WHERE C_BPartner_ID = %d)",
											X_ZZLearner.COLUMNNAME_ZZLearner_ID, X_ZZLearner.Table_Name,
											X_ZZLearner.COLUMNNAME_ZZ_SDPAdmin_ID, adminBpId,
											adminBpId));
			subQueries.add(String.format(	"SELECT %s FROM %s WHERE %s = %d",
											X_ZZLearnerLearnership.COLUMNNAME_ZZLearner_ID, X_ZZLearnerLearnership.Table_Name,
											X_ZZLearnerLearnership.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format(	"SELECT %s FROM %s WHERE %s = %d",
											X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZLearner_ID, X_ZZLearnerSkillsProgramme.Table_Name,
											X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format(	"SELECT %s FROM %s WHERE %s = %d",
											X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZLearner_ID, X_ZZLearnerQCTOLearnership.Table_Name,
											X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format(	"SELECT %s FROM %s WHERE %s = %d",
											X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZLearner_ID, X_ZZLearnerQCTOSkillsProgramme.Table_Name,
											X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format(	"SELECT %s FROM %s WHERE %s = %d",
											X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, X_ZZLearnerQCTOArtisans.Table_Name,
											X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZ_SDP_ID, adminBpId));
			subQueries.add(String.format(	"SELECT %s FROM %s WHERE %s = %d",
											I_ZZLearnerDGProgramme.COLUMNNAME_ZZLearner_ID, I_ZZLearnerDGProgramme.Table_Name,
											I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID, adminBpId));
		}

		sb.append(String.join(" UNION ", subQueries));
		sb.append(")");

		return sb.toString();
	}

	public boolean isInterventionDG()
	{
		if (selectedIntervention == null)
			return false;
		String val = selectedIntervention.getValue();
		String name = selectedIntervention.getName();
		return INTERVENTION_DG_PROGRAMMES.equalsIgnoreCase(val) || INTERVENTION_DG_PROGRAMMES.equalsIgnoreCase(name)
				|| "DG Programmes".equalsIgnoreCase(val) || "DG Programme".equalsIgnoreCase(val);
	}

	private void initLearnerSelection()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ValueAdaptColumnModel chooseLearnerCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																								MasterUtil.getNameOfColTranslated(	X_ZZLearner.Table_Name,
																																	X_ZZLearner.COLUMNNAME_ZZLearner_ID),
																								null,
																								CellModel.SEARCH_CELL);
		chooseLearnerCol.setShowTitle(false);
		chooseLearnerCol.setBtText("Learner");
		cols.add(chooseLearnerCol);

		chooseLearnerCol.setEventHandle((event, cellModel) -> {
			InfoPanelPara infoPara = InfoPanelPara.getInstance(X_ZZLearner_v.Table_Name, X_ZZLearner_v.COLUMNNAME_ZZLearner_ID);
			String sdpFilter = buildSdpLearnerFilter();
			if (sdpFilter != null && !sdpFilter.isBlank())
			{
				infoPara.setWhereClause(sdpFilter);
			}

			showInfoPanel(infoPara, (obj, infoPanel) -> {
				Object[] objs = (Object[]) obj;
				if (objs == null || objs.length == 0 || objs[0] == null)
					return;
				int learnerIdSelected = objs[0] instanceof Number ? ((Number) objs[0]).intValue() : Integer.parseInt(objs[0].toString());
				loadLearner(learnerIdSelected);
			});
		});

		ListColumnModel<ValueNamePair> interventionListCol = ListCellModel.getListColumnModel(
																								Msg.getElement(Env.getCtx(), "ZZInterventionList"),
																								null,
																								getFilteredInterventionList(),
																								title -> title.getName(),
																								title -> title.getValue());
		interventionListCol.setzClass(ValueNamePair.class).required();
		cols.add(interventionListCol);

		actionCol = CellModel.getColModelForGenericCell("", null, CellModel.BUTTON_CELL);
		actionCol.setShowTitle(false);
		cols.add(actionCol);

		interventionListCol.setEventHandle((event, cellModel) -> {
			@SuppressWarnings("unchecked")
			SelectEvent<Listitem, ValueNamePair> selectEvent = (SelectEvent<Listitem, ValueNamePair>) event;
			selectedIntervention = selectEvent.getSelectedObjects().iterator().next();

			CellModel actionCell = cellModel.getRowModel().get(actionCol);
			String btnText = isInterventionDG() ? "Grant Programmes" : selectedIntervention.getName();
			actionCol.setTitle(btnText);
			actionCell.setBtText(btnText);
			actionCell.setVisible(true);
			BindUtils.postNotifyChange(null, null, actionCell, "btText");
			BindUtils.postNotifyChange(null, null, actionCell, "visible");
			BindUtils.postNotifyChange(null, null, tmLearnerSelection, "row");

			dgProgrammeActive = false;
			isDgSubmit = false;
			selectedDGProgramme = null;
			BindUtils.postNotifyChange(null, null, this, "dgProgrammeActive");
			BindUtils.postNotifyChange(null, null, this, "selectedDGProgramme");
		});

		actionCol.setEventHandle((event, cellModel) -> {
			if (getSelectedLearnerId() <= 0)
			{
				MasterUtil.showInfoDialog("Please select a learner first.", null);
				return;
			}
			if (selectedIntervention == null)
			{
				MasterUtil.showInfoDialog("Please select an intervention type.", null);
				return;
			}
			if (isInterventionDG())
			{
				openDGProgrammeLookup();
			}
			else
			{
				openProgramInfoPanelOrLoad();
			}
		});

		tmLearnerSelection = TableModel.getTableBean(TableModel.class, cols, false, X_ZZLearner_v.Table_Name);
		tmLearnerSelection.setViewModel(ViewType.VIEW_FORM);
		tmLearnerSelection.setSclass("srd-LearnerRegistration-selectLearner");
		tmLearnerSelection.init();

		tmLearnerSelection.getRow().get(actionCol).setVisible(false);
	}

	private void initLearnerSelectionInfo()
	{
		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel col = CellModel.getColModelForLabel(
														MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_ZZFirstName),
														X_ZZPerson.COLUMNNAME_ZZFirstName);
		cols.add(col);

		col = CellModel.getColModelForLabel(
											MasterUtil.getNameOfColTranslated(X_ZZPerson.Table_Name, X_ZZPerson.COLUMNNAME_Surname),
											X_ZZPerson.COLUMNNAME_Surname);
		cols.add(col);

		ValueAdaptColumnModel idCol = ValueAdaptCellModel.getValueAdaptColumnModel(
																					MasterUtil.getNameOfColTranslated(	X_ZZPerson.Table_Name,
																														X_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No),
																					X_ZZPerson.COLUMNNAME_ZZ_ID_Passport_No,
																					CellModel.LABEL_CELL);
		idCol.setValueFromDaoAdaptHandle(obj -> {
			String idPassport = (String) obj;
			if (idPassport == null || idPassport.isBlank())
			{
				if (personSelected != null)
				{
					return personSelected.getZZOtherIDNo();
				}
				if (learnerSelected != null)
				{
					return learnerSelected.getZZOtherIDNo();
				}
			}
			return idPassport;
		});
		cols.add(idCol);

		tmLearnerSelectionInfo = TableModel.getTableBean(TableModel.class, cols, false, X_ZZPerson.Table_Name);
		tmLearnerSelectionInfo.setViewModel(ViewType.VIEW_GRID);
		tmLearnerSelectionInfo.setSclass("srd-LearnerRegistration-selectLearnerInfo");
		tmLearnerSelectionInfo.init();
	}

	private void openProgramInfoPanelOrLoad()
	{
		if (selectedIntervention == null)
			return;

		InterventionConfig config = getInterventionConfig(selectedIntervention.getValue());
		if (config == null)
		{
			log.warning("Unsupported learner intervention: " + selectedIntervention.getValue());
			return;
		}

		InfoPanelPara para = InfoPanelPara.getInstance(config.programmeTable, config.programmeKeyColumn);
		String whereClause = buildProgrammeBaseWhereClause(config.learnerTable, config.programmeTable);
		if (whereClause != null && !whereClause.isBlank())
		{
			para.setWhereClause(whereClause);
		}

		showInfoPanel(para, (obj, infoPanel) -> {
			Object[] values = (Object[]) obj;
			if (values == null || values.length == 0 || values[0] == null)
				return;
			int progId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
			if (progId <= 0)
				return;

			PO selectedProg = config.programmeLoader.apply(progId);
			showRegistrationStep(selectedProg);
		});
	}

	static InterventionConfig getInterventionConfig(String interventionValue)
	{
		for (InterventionConfig config : INTERVENTION_CONFIGS)
		{
			if (config.matches(interventionValue))
				return config;
		}
		return null;
	}

	static String getTableNameForIntervention(String interventionValue)
	{
		InterventionConfig config = getInterventionConfig(interventionValue);
		return config != null ? config.learnerTable : null;
	}

	private void showRegistrationStep(PO selectedProg)
	{
		if (selectedIntervention == null)
			return;

		InterventionConfig config = getInterventionConfig(selectedIntervention.getValue());
		if (config == null)
		{
			log.warning("Unsupported learner intervention: " + selectedIntervention.getValue());
			return;
		}

		String tableName = config.learnerTable;
		String programmeTable = config.programmeTable;
		String learnerProgrammeId = config.learnerProgrammeColumn;
		String programmeIdColumn = config.programmeKeyColumn;
		String codeColumn = config.programmeCodeColumn;
		String titleColumn = config.programmeTitleColumn;
		Function<Integer, PO> programmeLoader = config.programmeLoader;
		String joinClause = config.getJoinClause();
		String primaryKeyColumn = config.learnerKeyColumn;

		List<ColumnModel> cols = selectableProgrammeColumns(tableName, programmeTable, learnerProgrammeId,
															programmeIdColumn, codeColumn, titleColumn, programmeLoader);

		tmRegistration = TableModel.getTableBean(TableModel.class, cols, false, tableName);
		tmRegistration.setViewModel(ViewType.VIEW_FORM);
		tmRegistration.setSclass("two-col srd-learner-" + selectedIntervention.getName().toLowerCase().replace(' ', '-'));
		tmRegistration.setPoSupplier(row -> createLearnerChildRecord(tableName));
		tmRegistration.setCreateNewRowWhenEmpty(true);
		tmRegistration.init();

		tmRegistration.setLoadSavedDataHandle(model -> {
			if (getSelectedLearnerId() <= 0)
				return;
			String where = tableName + "." + X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID + " = ? AND "
					+ tableName + "." + I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus + " = ?";
			Object[] params;
			if (selectedProg != null)
			{
				where += " AND " + tableName + "." + learnerProgrammeId + " = ?";
				params = new Object[]{ getSelectedLearnerId(), X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft, selectedProg.get_ID() };
			}
			else
			{
				params = new Object[]{ getSelectedLearnerId(), X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft };
			}
			Query query = MTable.get(Env.getCtx(), tableName).createQuery(where, null)
															.setParameters(params);
			if (primaryKeyColumn != null)
			{
				query.setOrderBy(tableName + "." + primaryKeyColumn + " DESC");
			}

			PO draft = query.first();
			if (draft == null)
			{
				model.resetMultiPo(new ArrayList<>());
				return;
			}

			List<PO> row = new ArrayList<>();
			row.add(draft);
			if (!joinClause.isBlank())
			{
				row.addAll(getChildRelatedRecords(draft, tableName));
			}
			model.resetMultiPo(List.of(row));
		});

		tmRegistration.setBeforeSave(event -> {
			if (event.row() != null && !event.row().isEditable())
			{
				return true;
			}
			if (event.isPOEven() && event.po() != null && tableName.equals(event.po().get_TableName()) && getSelectedLearnerId() > 0)
			{
				if (event.po().get_ID() <= 0 || event.po().get_ValueAsInt(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID) <= 0)
					event.po().set_ValueOfColumn(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, getSelectedLearnerId());
			}
			if (event.po() != null && event.po().get_ColumnIndex(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus) >= 0)
			{
				String curStatus = event.po().get_ValueAsString(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus);
				if (curStatus == null || curStatus.trim().isEmpty())
				{
					event.po().set_ValueOfColumn(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
				}
			}
			PO programme = getLearnerProgramme(event.po(), tableName);
			if (programme != null)
			{
				int credits = programme.get_ValueAsInt("ZZCredits");
				if (credits > 0)
					event.po().set_ValueOfColumn("ZZCredits", credits);
			}
			return true;
		});

		tmRegistration.reloadDao();
		tmRegistration.loadSavedData();

		if (selectedProg != null && tmRegistration.getRow() != null)
		{
			ColumnModel progCol = tmRegistration.getColumnInfos().stream()
					.filter(c -> c.getDaoPropertyName() != null && c.getDaoPropertyName().equalsIgnoreCase(learnerProgrammeId))
					.findFirst()
					.orElse(cols.size() > 1 ? cols.get(1) : null);
			if (progCol != null)
			{
				CellModel progCell = tmRegistration.getRow().get(progCol);
				if (progCell != null)
				{
					progCell.setValue(selectedProg);
					progCell.resetValidate();
				}
			}
			ColumnModel tCol = tmRegistration.getColumnInfos().stream()
					.filter(c -> c.getDaoPropertyName() != null && c.getDaoPropertyName().equalsIgnoreCase(titleColumn))
					.findFirst()
					.orElse(cols.size() > 3 ? cols.get(3) : null);
			if (tCol != null)
			{
				CellModel titleCell = tmRegistration.getRow().get(tCol);
				if (titleCell != null)
				{
					titleCell.setValue(selectedProg.get_Value(titleColumn));
					titleCell.resetValidate();
				}
			}
			PO draftPo = tmRegistration.getRow().getRowData() != null
					? tmRegistration.getRow().getRowData().getDataNullable(tableName)
					: null;
			if (draftPo != null)
			{
				draftPo.set_ValueOfColumn(learnerProgrammeId, selectedProg.get_ID());
				int credits = selectedProg.get_ValueAsInt("ZZCredits");
				if (credits > 0 && draftPo.get_ColumnIndex("ZZCredits") >= 0)
				{
					draftPo.set_ValueOfColumn("ZZCredits", credits);
				}
			}
		}

		PO draft = tmRegistration.getRow() != null && tmRegistration.getRow().getRowData() != null
				? tmRegistration.getRow().getRowData().getDataNullable(tableName)
				: null;
		loadRegistrationDocuments(draft != null ? draft.get_ID() : 0);

		setRegSelectedTabIndex(0);
		setStep("registration");
		BindUtils.postNotifyChange(null, null, this, "tmRegistration");
	}

	@Command
	public void backToSelection()
	{
		if (regSelectedTabIndex > 0)
		{
			setRegSelectedTabIndex(0);
			return;
		}
		setStep("selectRegistration");
	}

	private List<ColumnModel> selectableProgrammeColumns(	String learnerTable, String programmeTable,
															String learnerProgrammeId, String programmeIdColumn, String codeColumn, String titleColumn,
															Function<Integer, PO> programmeLoader)
	{
		List<ColumnModel> cols = new ArrayList<>();

		boolean isQctoArtisans = X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable);
		String rawHeaderLabel = isQctoArtisans ? "Artisan Code" : Msg.getElement(Env.getCtx(), codeColumn);
		String codeHeaderLabel = rawHeaderLabel != null ? rawHeaderLabel.replaceAll("SkillsProgramme", "Skills Programme") : rawHeaderLabel;

		ValueAdaptColumnModel programmeCol = ValueAdaptCellModel.getValueAdaptColumnModel(	codeHeaderLabel, learnerProgrammeId,
																							CellModel.SEARCH_CELL);
		programmeCol.setTableName(learnerTable);
		programmeCol.required();

		String sdpColName = X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_SDP_ID;
		String employerColName = X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZ_Employer_ID;

		ValueAdaptColumnModel sdpCol = ValueAdaptCellModel.getValueAdaptColumnModel(Msg.getElement(Env.getCtx(), sdpColName),
																					sdpColName, CellModel.SEARCH_CELL);
		sdpCol.setTableName(learnerTable).required();
		sdpCol.setBtText("");

		ValueAdaptColumnModel employerCol = ValueAdaptCellModel.getValueAdaptColumnModel(	Msg.getElement(Env.getCtx(), employerColName),
																							employerColName, CellModel.SEARCH_CELL);
		employerCol.setTableName(learnerTable).required();

		cols.add(sdpCol);
		cols.add(programmeCol);
		cols.add(employerCol);

		ColumnModel titleCol = label(programmeTable, titleColumn);
		cols.add(titleCol);

		String programmeWhereClause = buildProgrammeBaseWhereClause(learnerTable, programmeTable);

		configureProgrammeSelector(	sdpCol, null,
									id -> new MBPartner(Env.getCtx(), id, null), X_C_BPartner.COLUMNNAME_C_BPartner_ID,
									X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.Table_Name,
									" C_BPartner.IsActive ='Y' AND C_BPartner.ZZ_Is_SDP='Y'",
									programmeCol, learnerTable);
		configureProgrammeSelector(	programmeCol, titleCol, programmeLoader, programmeIdColumn, codeColumn, titleColumn,
									programmeTable, programmeWhereClause, sdpCol, learnerTable);
		configureProgrammeSelector(	employerCol, null,
									id -> new MBPartner(Env.getCtx(), id, null), X_C_BPartner.COLUMNNAME_C_BPartner_ID,
									X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.COLUMNNAME_Name, X_C_BPartner.Table_Name,
									"C_BPartner.IsActive ='Y' AND C_BPartner.C_BPartner_ID IN  (SELECT AD_User.C_BPartner_ID FROM AD_User WHERE AD_User.IsActive ='Y')",
									null, learnerTable);

		String studentColumn = learnerTable.equals(X_ZZLearnerQCTOLearnership.Table_Name) ? X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZStudentNumber
						: learnerTable.equals(X_ZZLearnerQCTOSkillsProgramme.Table_Name) ? X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZStudentNumber
							: learnerTable.equals(X_ZZLearnerLearnership.Table_Name) ? X_ZZLearnerLearnership.COLUMNNAME_ZZStudentNumber
							: learnerTable.equals(X_ZZLearnerQCTOArtisans.Table_Name) ? X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZStudentNumber
							: X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZStudentNumber;

		cols.add(text(learnerTable, studentColumn));

		String fundingTypeColumn = getFundingTypeColumn(learnerTable);
		if (fundingTypeColumn != null)
		{
			MTable learnerModelTable = MTable.get(Env.getCtx(), learnerTable);
			MColumn fundingCol = learnerModelTable != null ? learnerModelTable.getColumn(fundingTypeColumn) : null;
			int fundingRefId = fundingCol != null ? fundingCol.getAD_Reference_Value_ID() : 0;
			if (fundingRefId <= 0)
			{
				MReference ref = new Query(Env.getCtx(), MReference.Table_Name, "Name=?", null)
									.setParameters("Funding Type")
									.first();
				if (ref != null)
					fundingRefId = ref.getAD_Reference_ID();
			}
			ValueNamePair[] fundingPairs = fundingRefId > 0 ? MRefList.getList(Env.getCtx(), fundingRefId, false) : null;
			List<ValueNamePair> fundingTypeList = fundingPairs != null && fundingPairs.length > 0
																									? Arrays.asList(fundingPairs)
																									: List.of(
																												new ValueNamePair(	X_ZZLearnerDGProgramme.ZZFUNDINGTYPE_IndustryFunded,
																																"Industry Funded"),
																												new ValueNamePair(	X_ZZLearnerDGProgramme.ZZFUNDINGTYPE_SETAFunded,
																																"SETA Funded"));

			ListColumnModel<ValueNamePair> colFundingType = ListCellModel.getListColumnModel(
																									Msg.getElement(Env.getCtx(), fundingTypeColumn),
																									fundingTypeColumn,
																									fundingTypeList,
																									ValueNamePair::getName,
																									ValueNamePair::getValue);
			colFundingType.setzClass(ValueNamePair.class).required();
			colFundingType.setTableName(learnerTable);
			cols.add(colFundingType);
		}

		ColumnModel startDateCol = editableDate(learnerTable, "ZZCommencementDate").required();
		startDateCol.setTitle("Start Date");
		cols.add(startDateCol);
		cols.add(editableDate(learnerTable, "ZZCompletionDate"));

		return cols;
	}

	private String getFundingTypeColumn(String learnerTable)
	{
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable)
				|| X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable)
				|| X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable)
				|| X_ZZLearnerLearnership.Table_Name.equals(learnerTable)
				|| X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
		{
			return "ZZFundingType";
		}
		if (learnerTable != null)
		{
			MTable learnerModelTable = MTable.get(Env.getCtx(), learnerTable);
			if (learnerModelTable != null && learnerModelTable.getColumn("ZZFundingType") != null)
			{
				return "ZZFundingType";
			}
		}
		return null;
	}

	private String buildProgrammeBaseWhereClause(String learnerTable, String programmeTable)
	{
		boolean isQctoArtisans = X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable);
		String programmeWhereClause = programmeTable	+ ".IsActive = 'Y' AND (" + programmeTable + ".ZZLastEnrolmentDate IS NULL OR " + programmeTable
										+ ".ZZLastEnrolmentDate >= CURRENT_DATE)";

		if (isQctoArtisans)
		{
			programmeWhereClause += " AND (ZZQctoLearnership.ZZArtisanLearnership = 'Y' OR ZZQctoLearnership.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoQualification q WHERE q.ZZArtisanQualification = 'Y'))";
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZQctoLearnership.ZZQualification_ID IN (    select zzlinkassessorqualification.ZZQualification_ID    from zzlinkassessorqualification
													inner join zzassessorperson on zzassessorperson.zzassessorperson_id = zzlinkassessorqualification.ZZAssessorPerson_ID
													where zzassessorperson.ZZ_DocStatus = 'AP'       and zzlinkassessorqualification.ZZQualification_ID IS NOT NULL) """;
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZQctoSkillsProgramme.ZZQctoSkillsProgramme_ID IN (select ZZQctoSkillsProgramme_ID from zzlinkassessorskillsprogramme
															where zzassessorperson_id in (	select zzassessorperson_id from zzassessorperson
																							where ZZ_DocStatus='AP') AND ZZQctoSkillsProgramme_ID IS NOT NULL ) """;
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZLearnership.ZZQualification_ID IN (
							select zzlinkassessorqualification.ZZQualification_ID    from zzlinkassessorqualification
							inner join zzassessorperson        on zzassessorperson.zzassessorperson_id = zzlinkassessorqualification.ZZAssessorPerson_ID
							where zzassessorperson.ZZ_DocStatus = 'AP'       and zzlinkassessorqualification.ZZQualification_ID IS NOT NULL)""";
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
		{
			programmeWhereClause += """
							AND ZZSkillsProgramme.zzskillsprogramme_ID IN (select zzskillsprogramme_id from zzlinkassessorskillsprogramme
																	where zzassessorperson_id in (	select zzassessorperson_id from zzassessorperson
																									where ZZ_DocStatus='AP') AND zzskillsprogramme_id IS NOT NULL ) """;
		}
		return programmeWhereClause;
	}

	private ColumnModel label(String tableName, String columnName)
	{
		return CellModel.getColModelForLabel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName)
						.setTableName(tableName).setReadonly(true);
	}

	private ColumnModel text(String tableName, String columnName)
	{
		return CellModel.getColModelForText(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName).setTableName(tableName);
	}

	private ColumnModel editableDate(String tableName, String columnName)
	{
		return DateCellModel.getDateColumnModel(MasterUtil.getNameOfColTranslated(tableName, columnName), columnName).setTableName(tableName);
	}

	private void configureProgrammeSelector(ValueAdaptColumnModel programmeCol, ColumnModel titleCol, Function<Integer, PO> programmeLoader,
											String idColumn, String codeColumn, String titleColumn,
											String programmeTable, String whereClause, ColumnModel progColRef, String learnerTable)
	{
		programmeCol.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			PO po = (PO) value;
			Object val = po.get_Value(codeColumn);
			if (val != null && !val.toString().isBlank())
				return val;
			Object name = po.get_Value("Name");
			if (name != null && !name.toString().isBlank())
				return name;
			Object code = po.get_Value("ZZProviderCode");
			if (code != null && !code.toString().isBlank())
				return code;
			return po.toString();
		});
		programmeCol.setValueAdaptHandle(value -> value == null ? null : ((PO) value).get_ID());
		programmeCol.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			if (id <= 0)
				return null;
			return programmeLoader.apply(id);
		});
		programmeCol.setEventHandle((event, cellModel) -> {
			if (cellModel.getRowModel() != null && !cellModel.getRowModel().isEditable())
			{
				return;
			}
			InfoPanelPara para = InfoPanelPara.getInstance(programmeTable, idColumn);
			String finalWhereClause = whereClause;

			if (progColRef != null && learnerTable != null && cellModel.getRowModel() != null)
			{
				CellModel progCell = cellModel.getRowModel().get(progColRef);
				if (progCell != null && progCell.getValue() != null)
				{
					int selectedRefId = 0;
					Object val = progCell.getValue();
					if (val instanceof PO)
					{
						selectedRefId = ((PO) val).get_ID();
					}
					else if (val instanceof Number)
					{
						selectedRefId = ((Number) val).intValue();
					}

					if (selectedRefId > 0)
					{
						if (X_C_BPartner.Table_Name.equals(programmeTable))
						{
							finalWhereClause = buildSdpLookupWhereClauseForSelectedProgramme(learnerTable, selectedRefId, whereClause);
						}
						else
						{
							finalWhereClause = buildProgrammeLookupWhereClauseForSelectedSdp(learnerTable, programmeTable, selectedRefId, whereClause);
						}
					}
				}
			}

			if (finalWhereClause != null && !finalWhereClause.isBlank())
			{
				para.setWhereClause(finalWhereClause);
			}
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int selectedId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				PO selected = programmeLoader.apply(selectedId);
				cellModel.setValue(selected);
				cellModel.resetValidate();
				if (titleCol != null)
				{
					cellModel.getRowModel().get(titleCol).setValue(selected.get_Value(titleColumn));
				}
			});
		});
	}

	private String buildSdpLookupWhereClauseForSelectedProgramme(String learnerTableName, int selectedProgrammeId, String defaultWhereClause)
	{
		StringBuilder sqlWhere = new StringBuilder(defaultWhereClause != null && !defaultWhereClause.isBlank()	? defaultWhereClause
																												: "C_BPartner.IsActive ='Y' AND C_BPartner.ZZ_Is_SDP='Y'");
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_Trades.C_BPartner_ID FROM C_BP_Trades WHERE C_BP_Trades.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoLearnership q WHERE q.ZZQctoLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND C_BP_Trades.IsActive = 'Y' AND C_BP_Trades.ZZ_Status = 'AC' AND (C_BP_Trades.EndDate IS NULL OR C_BP_Trades.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT bp.C_BPartner_ID FROM C_BP_OC bp WHERE bp.ZZQctoQualification_ID IN (SELECT q.ZZQctoQualification_ID FROM ZZQctoLearnership q WHERE q.ZZQctoLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT bp.C_BPartner_ID FROM C_BP_OC bp WHERE bp.ZZQualification_ID IN (SELECT q.ZZQualification_ID FROM ZZLearnership q WHERE q.ZZLearnership_ID = ")
					.append(selectedProgrammeId)
					.append(") AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_SkillsProgramme.C_BPartner_ID FROM C_BP_SkillsProgramme WHERE C_BP_SkillsProgramme.ZZQctoSkillsProgramme_ID = ")
					.append(selectedProgrammeId)
					.append(" AND C_BP_SkillsProgramme.IsActive = 'Y' AND C_BP_SkillsProgramme.ZZ_Status = 'AC' AND (C_BP_SkillsProgramme.EndDate IS NULL OR C_BP_SkillsProgramme.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND C_BPartner.C_BPartner_ID IN (SELECT C_BP_SkillsProgramme.C_BPartner_ID FROM C_BP_SkillsProgramme WHERE C_BP_SkillsProgramme.ZZSkillsProgramme_ID = ")
					.append(selectedProgrammeId)
					.append(" AND C_BP_SkillsProgramme.IsActive = 'Y' AND C_BP_SkillsProgramme.ZZ_Status = 'AC' AND (C_BP_SkillsProgramme.EndDate IS NULL OR C_BP_SkillsProgramme.EndDate >= CURRENT_DATE))");
		}
		return sqlWhere.toString();
	}

	private String buildProgrammeLookupWhereClauseForSelectedSdp(	String learnerTableName, String programmeTableName, int selectedSdpBPartnerId,
																	String defaultWhereClause)
	{
		StringBuilder sqlWhere = new StringBuilder(defaultWhereClause != null && !defaultWhereClause.isBlank()	? defaultWhereClause
																												: programmeTableName + ".IsActive = 'Y'");
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoQualification_ID IN (SELECT t.ZZQctoQualification_ID FROM C_BP_Trades t WHERE t.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND t.IsActive = 'Y' AND t.ZZ_Status = 'AC' AND (t.EndDate IS NULL OR t.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoQualification_ID IN (SELECT bp.ZZQctoQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQualification_ID IN (SELECT bp.ZZQualification_ID FROM C_BP_OC bp WHERE bp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND bp.IsActive = 'Y' AND bp.ZZ_Status = 'AC' AND (bp.EndDate IS NULL OR bp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZQctoSkillsProgramme_ID IN (SELECT sp.ZZQctoSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTableName))
		{
			sqlWhere.append(" AND ")
					.append(programmeTableName)
					.append(".ZZSkillsProgramme_ID IN (SELECT sp.ZZSkillsProgramme_ID FROM C_BP_SkillsProgramme sp WHERE sp.C_BPartner_ID = ")
					.append(selectedSdpBPartnerId)
					.append(" AND sp.IsActive = 'Y' AND sp.ZZ_Status = 'AC' AND (sp.EndDate IS NULL OR sp.EndDate >= CURRENT_DATE))");
		}
		return sqlWhere.toString();
	}

	private PO getLearnerProgramme(PO child, String learnerTable)
	{
		int programmeId = 0;
		if (Objects.isNull(child))
			return null;
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
		{
			programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQctoLearnership_ID);
			if (programmeId <= 0)
				programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZQualification_ID);
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOLearnership.COLUMNNAME_ZZQctoLearnership_ID);
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerQCTOSkillsProgramme.COLUMNNAME_ZZQctoSkillsProgramme_ID);
		else if (X_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerLearnership.COLUMNNAME_ZZLearnership_ID);
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
			programmeId = child.get_ValueAsInt(X_ZZLearnerSkillsProgramme.COLUMNNAME_ZZSkillsProgramme_ID);
		if (programmeId <= 0)
			return null;
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(learnerTable))
			return new X_ZZQctoLearnership(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerQCTOLearnership.Table_Name.equals(learnerTable))
			return new X_ZZQctoLearnership(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZQctoSkillsProgramme(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerLearnership.Table_Name.equals(learnerTable))
			return new X_ZZLearnership(Env.getCtx(), programmeId, null);
		if (X_ZZLearnerSkillsProgramme.Table_Name.equals(learnerTable))
			return new X_ZZSkillsProgramme(Env.getCtx(), programmeId, null);
		return null;
	}

	private List<PO> getChildRelatedRecords(PO child, String learnerTable)
	{
		List<PO> relatedRecords = new ArrayList<>();
		PO programme = getLearnerProgramme(child, learnerTable);
		if (programme != null)
			relatedRecords.add(programme);

		return relatedRecords;
	}

	private PO createLearnerChildRecord(String tableName)
	{
		PO record;
		if (X_ZZLearnerQCTOArtisans.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOArtisans(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerQCTOLearnership.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOLearnership(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerQCTOSkillsProgramme.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerQCTOSkillsProgramme(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerLearnership.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerLearnership(Env.getCtx(), 0, null);
		}
		else if (X_ZZLearnerSkillsProgramme.Table_Name.equals(tableName))
		{
			record = new X_ZZLearnerSkillsProgramme(Env.getCtx(), 0, null);
		}
		else
		{
			throw new AdempiereException("Unsupported learner child table: " + tableName);
		}
		if (getSelectedLearnerId() > 0)
		{
			record.set_ValueOfColumn(X_ZZLearnerQCTOArtisans.COLUMNNAME_ZZLearner_ID, getSelectedLearnerId());
		}
		if (record.get_ColumnIndex(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus) >= 0)
		{
			record.set_ValueOfColumn(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
		}
		return record;
	}

	@Override
	public void doSave(String trxName)
	{
		if (getSelectedLearnerId() <= 0)
		{
			throw new AdempiereException("Please select a learner before saving.");
		}
		int currentUserId = Env.getAD_User_ID(Env.getCtx());
		if (learner != null && currentUserId > 0)
		{
			learner.setZZ_SDPAdmin_ID(currentUserId);
			learner.saveEx(trxName);
		}
		if (dgProgrammeActive)
		{
			saveDGRegistration(trxName, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
			return;
		}
		if (tmRegistration != null && tmRegistration.getRows() != null && selectedIntervention != null)
		{
			String tableName = getTableNameForIntervention(selectedIntervention.getValue());
			if (tableName != null)
			{
				for (RowModel row : tmRegistration.getRows())
				{
					if (row.getRowData() != null)
					{
						PO po = row.getRowData().getDataNullable(tableName);
						if (po != null)
						{
							if (po.get_ColumnIndex(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus) >= 0)
							{
								po.set_ValueOfColumn(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
							}
						}
					}
				}
			}
		}
		super.doSave(trxName);
		saveRegistrationDocuments(trxName);
	}

	@Override
	public void doSubmit(String trxName)
	{
		int currentUserId = Env.getAD_User_ID(Env.getCtx());
		if (learner != null && currentUserId > 0)
		{
			learner.setZZ_SDPAdmin_ID(currentUserId);
			learner.saveEx(trxName);
		}
		if (dgProgrammeActive)
		{
			List<String> missingCols = validateDGProgrammeDetails();
			if (!missingCols.isEmpty())
			{
				throw new AdempiereException("Please fill all mandatory fields:\n• " + String.join("\n• ", missingCols));
			}
			saveDGRegistration(trxName, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Created);
			return;
		}
		if (tmRegistration != null && tmRegistration.getRows() != null && selectedIntervention != null)
		{
			String tableName = getTableNameForIntervention(selectedIntervention.getValue());
			if (tableName != null)
			{
				for (RowModel row : tmRegistration.getRows())
				{
					if (row.getRowData() != null)
					{
						PO po = row.getRowData().getDataNullable(tableName);
						if (po != null)
						{
							if (po.get_ColumnIndex(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus) >= 0)
							{
								po.set_ValueOfColumn(I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_DocStatus, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Created);
								po.saveEx(trxName);
							}
						}
					}
				}
			}
		}
		super.doSubmit(trxName);
		saveRegistrationDocuments(trxName);
	}

	@Override
	public boolean isSupportSubmit()
	{
		return true;
	}

	@Override
	public boolean isSupportSave()
	{
		return true;
	}

	@Command
	public void save()
	{
		saveApp();
	}

	@Command
	public void cancelApp()
	{
		MasterUtil.closeActiveWindow();
	}

	@Command
	public void cancel()
	{
		cancelApp();
	}

	private String buildDGProgrammeWhereClause()
	{
		StringBuilder sql = new StringBuilder(I_ZZ_Program_Master_Data.Table_Name + "." + I_ZZ_Program_Master_Data.COLUMNNAME_IsActive + " = 'Y'");
		sql.append(" AND ").append(I_ZZ_Program_Master_Data.Table_Name).append(".").append(I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_IsLearnerRegistration).append(
																																								" = 'Y'");
		return sql.toString();
	}

	private void openDGProgrammeLookup()
	{
		if (getSelectedLearnerId() <= 0)
		{
			MasterUtil.showInfoDialog("Please select a learner first.", null);
			return;
		}

		InfoPanelPara para = InfoPanelPara.getInstance(
														I_ZZ_Program_Master_Data.Table_Name,
														I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID);
		para.setWhereClause(buildDGProgrammeWhereClause());

		showInfoPanel(para, (obj, infoPanel) -> {
			Object[] values = (Object[]) obj;
			if (values == null || values.length == 0 || values[0] == null)
				return;
			int progId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
			if (progId <= 0)
				return;

			loadDGProgramme(progId);
		});
	}

	private void loadDGProgramme(int progId)
	{
		selectedDGProgramme = new X_ZZ_Program_Master_Data(Env.getCtx(), progId, null);
		dgProgrammeActive = true;

		if (tmLearnerSelection != null && tmLearnerSelection.getRow() != null && actionCol != null)
		{
			CellModel cell = tmLearnerSelection.getRow().get(actionCol);
			if (cell != null)
			{
				actionCol.setTitle("Grant Programmes");
				cell.setBtText("Grant Programmes");
				BindUtils.postNotifyChange(null, null, cell, "btText");
			}
		}

		setDgSelectedTabIndex(0);

		if (tmDGProgrammeDetails == null)
		{
			initDGProgrammeDetails();
		}

		// Reset the DaoManage for the DG form so any previous draft is cleared.
		dgDaoManage.resetDao(I_ZZLearnerDGProgramme.Table_Name);

		// Look for an existing saved record for this learner and programme
		X_ZZLearnerDGProgramme existingDg = findExistingDGDraft(
																getSelectedLearnerId(),
																selectedDGProgramme.getZZ_Program_Master_Data_ID(),
																null);
		if (existingDg != null)
		{
			dgDaoManage.setDao(existingDg);
		}

		// Reload the DAO into the existing TableModel/RowModel that ZK UI is bound to
		tmDGProgrammeDetails.reloadDao();

		// Populate programme title
		if (colProgMasterData != null && tmDGProgrammeDetails.getRow() != null)
		{
			String title = selectedDGProgramme.getTitle();
			if (title == null || title.isBlank())
				title = selectedDGProgramme.getName();
			CellModel progCell = tmDGProgrammeDetails.getRow().get(colProgMasterData);
			if (progCell != null)
			{
				progCell.setValue(title);
				BindUtils.postNotifyChange(null, null, progCell, "value");
			}
		}

		// Default financial year from programme if not already populated from draft
		if (colFinancialYear != null && tmDGProgrammeDetails.getRow() != null && selectedDGProgramme.getC_Year_ID() > 0)
		{
			CellModel fyCell = tmDGProgrammeDetails.getRow().get(colFinancialYear);
			if (fyCell != null && (fyCell.getValue() == null || extractId(fyCell.getValue()) <= 0))
			{
				MYear yr = new MYear(Env.getCtx(), selectedDGProgramme.getC_Year_ID(), null);
				fyCell.setValue(yr);
				fyCell.resetValidate();
				BindUtils.postNotifyChange(null, null, fyCell, "value");
				BindUtils.postNotifyChange(null, null, fyCell, "displayValue");
			}
		}

		// Default SDP from adminBpId linked to logged-in SDP Admin if not already populated from draft
		if (colLeadSdp != null && tmDGProgrammeDetails.getRow() != null && adminBpId > 0)
		{
			CellModel sdpCell = tmDGProgrammeDetails.getRow().get(colLeadSdp);
			if (sdpCell != null && (existingDg == null || sdpCell.getValue() == null || extractId(sdpCell.getValue()) <= 0))
			{
				MBPartner sdpBp = new MBPartner(Env.getCtx(), adminBpId, null);
				sdpCell.setValue(sdpBp);
				sdpCell.resetValidate();
				BindUtils.postNotifyChange(null, null, sdpCell, "value");
				BindUtils.postNotifyChange(null, null, sdpCell, "displayValue");
			}
		}

		// Default Lead SDP Contact to the logged-in SDP Admin user if not already populated from draft
		if (colLeadSdpContact != null && tmDGProgrammeDetails.getRow() != null && loginId > 0)
		{
			CellModel contactCell = tmDGProgrammeDetails.getRow().get(colLeadSdpContact);
			if (contactCell != null && (existingDg == null || contactCell.getValue() == null || extractId(contactCell.getValue()) <= 0))
			{
				MUser sdpUser = new MUser(Env.getCtx(), loginId, null);
				contactCell.setValue(sdpUser);
				contactCell.resetValidate();
				BindUtils.postNotifyChange(null, null, contactCell, "value");
				BindUtils.postNotifyChange(null, null, contactCell, "displayValue");
			}
		}


		// Post notify change on all cells in the row so ZK refreshes every field
		if (tmDGProgrammeDetails.getRow() != null)
		{
			for (CellModel cell : tmDGProgrammeDetails.getRow().values())
			{
				BindUtils.postNotifyChange(null, null, cell, "value");
				if (cell instanceof ValueAdaptCellModel)
				{
					BindUtils.postNotifyChange(null, null, cell, "displayValue");
				}
				if (cell instanceof ListCellModel)
				{
					BindUtils.postNotifyChange(null, null, cell, "model");
					BindUtils.postNotifyChange(null, null, cell, "selectedItem");
				}
				if (cell instanceof DateCellModel)
				{
					BindUtils.postNotifyChange(null, null, cell, "localDate");
				}
			}
			BindUtils.postNotifyChange(null, null, tmDGProgrammeDetails.getRow(), "*");
		}

		loadDGDocuments();

		BindUtils.postNotifyChange(null, null, this, "selectedDGProgramme");
		BindUtils.postNotifyChange(null, null, this, "dgProgrammeActive");
		BindUtils.postNotifyChange(null, null, this, "tmDGProgrammeDetails");
		BindUtils.postNotifyChange(null, null, this, "tmDGDocuments");
		BindUtils.postNotifyChange(null, null, this, "dgSelectedTabIndex");
	}

	private void initDGProgrammeDetails()
	{
		if (tmDGProgrammeDetails != null)
			return;

		List<ColumnModel> cols = new ArrayList<>();

		ColumnModel colRefNo = CellModel.getColModelForText(
															MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber),
															I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber);
		cols.add(colRefNo);

		colProgMasterData = CellModel.getColModelForText(
															MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Program_Master_Data_ID),
															null).setReadonly(true);
		cols.add(colProgMasterData);

		colLeadSdp = ValueAdaptCellModel.getValueAdaptColumnModel(
																						MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																															I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID),
																						I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID,
																						CellModel.SEARCH_CELL);
		colLeadSdp.required();
		cols.add(colLeadSdp);

		ValueAdaptColumnModel colLeadEmployer = ValueAdaptCellModel.getValueAdaptColumnModel(
																								MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																	I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID),
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID,
																								CellModel.SEARCH_CELL);
		colLeadEmployer.required();
		cols.add(colLeadEmployer);

		List<ValueNamePair> yesNoOptions = List.of(
													new ValueNamePair("N", "No"),
													new ValueNamePair("Y", "Yes"));

		ListColumnModel<ValueNamePair> colLeadSdpLevy = ListCellModel.getListColumnModel(
																							MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy),
																							I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy,
																							yesNoOptions,
																							ValueNamePair::getName,
																							ValueNamePair::getValue);
		colLeadSdpLevy.setzClass(ValueNamePair.class).required();
		cols.add(colLeadSdpLevy);

		ListColumnModel<ValueNamePair> colLeadEmployerLevy = ListCellModel.getListColumnModel(
																								MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																	I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy),
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy,
																								yesNoOptions,
																								ValueNamePair::getName,
																								ValueNamePair::getValue);
		colLeadEmployerLevy.setzClass(ValueNamePair.class).required();
		cols.add(colLeadEmployerLevy);

		colLeadSdpContact = ValueAdaptCellModel.getValueAdaptColumnModel(
																								MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																	I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID),
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID,
																								CellModel.SEARCH_CELL);
		colLeadSdpContact.required();
		cols.add(colLeadSdpContact);

		ValueAdaptColumnModel colLeadEmployerContact = ValueAdaptCellModel.getValueAdaptColumnModel(
																									MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																		I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID),
																									I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID,
																									CellModel.SEARCH_CELL);
		colLeadEmployerContact.required();
		cols.add(colLeadEmployerContact);

		ValueAdaptColumnModel colSecondarySdp = ValueAdaptCellModel.getValueAdaptColumnModel(
																								MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																	I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID),
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID,
																								CellModel.SEARCH_CELL);
		cols.add(colSecondarySdp);

		ValueAdaptColumnModel colSecondaryEmployer = ValueAdaptCellModel.getValueAdaptColumnModel(
																									MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																		I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID),
																									I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID,
																									CellModel.SEARCH_CELL);
		cols.add(colSecondaryEmployer);

		ListColumnModel<ValueNamePair> colSecondarySdpLevy = ListCellModel.getListColumnModel(
																								MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																	I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy),
																								I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy,
																								yesNoOptions,
																								ValueNamePair::getName,
																								ValueNamePair::getValue);
		colSecondarySdpLevy.setzClass(ValueNamePair.class);
		cols.add(colSecondarySdpLevy);

		ListColumnModel<ValueNamePair> colSecondaryEmployerLevy = ListCellModel.getListColumnModel(
																									MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																		I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy),
																									I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy,
																									yesNoOptions,
																									ValueNamePair::getName,
																									ValueNamePair::getValue);
		colSecondaryEmployerLevy.setzClass(ValueNamePair.class);
		cols.add(colSecondaryEmployerLevy);

		ValueAdaptColumnModel colSecondarySdpContact = ValueAdaptCellModel.getValueAdaptColumnModel(
																									MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																		I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID),
																									I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID,
																									CellModel.SEARCH_CELL);
		cols.add(colSecondarySdpContact);

		ValueAdaptColumnModel colSecondaryEmployerContact = ValueAdaptCellModel.getValueAdaptColumnModel(
																											MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																				I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID),
																											I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID,
																											CellModel.SEARCH_CELL);
		cols.add(colSecondaryEmployerContact);



		MColumn fundingCol = MTable	.get(Env.getCtx(), I_ZZLearnerDGProgramme.Table_Name)
									.getColumn(I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType);
		int fundingRefId = fundingCol != null ? fundingCol.getAD_Reference_Value_ID() : 1000367;
		ValueNamePair[] fundingPairs = MRefList.getList(Env.getCtx(), fundingRefId, false);
		List<ValueNamePair> fundingTypeList = fundingPairs != null && fundingPairs.length > 0
																								? Arrays.asList(fundingPairs)
																								: List.of(
																											new ValueNamePair(	X_ZZLearnerDGProgramme.ZZFUNDINGTYPE_IndustryFunded,
																																"Industry Funded"),
																											new ValueNamePair(	X_ZZLearnerDGProgramme.ZZFUNDINGTYPE_SETAFunded,
																																"SETA Funded"));

		ListColumnModel<ValueNamePair> colFundingType = ListCellModel.getListColumnModel(
																							MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																																I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType),
																							I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType,
																							fundingTypeList,
																							ValueNamePair::getName,
																							ValueNamePair::getValue);
		colFundingType.setzClass(ValueNamePair.class).required();
		cols.add(colFundingType);

		ColumnModel colContractNo = CellModel.getColModelForText(
																	MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																										I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber),
																	I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber);
		cols.add(colContractNo);

		colFinancialYear = ValueAdaptCellModel.getValueAdaptColumnModel(
																		MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																											I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID),
																		I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID,
																		CellModel.SEARCH_CELL);
		colFinancialYear.required();
		cols.add(colFinancialYear);

		ColumnModel colCommenceDate = DateCellModel.getDateColumnModel(
																		MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																											I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate),
																		I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate).required();
		cols.add(colCommenceDate);

		ColumnModel colCompletionDate = DateCellModel.getDateColumnModel(
																			MasterUtil.getNameOfColTranslated(	I_ZZLearnerDGProgramme.Table_Name,
																												I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate),
																			I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate).required();
		cols.add(colCompletionDate);

		String sdpWhere = I_C_BPartner.Table_Name	+ "." + I_C_BPartner.COLUMNNAME_IsActive + " = 'Y' AND "
							+ I_C_BPartner.Table_Name + ".ZZ_Is_SDP = 'Y'";
		String empWhere = I_C_BPartner.Table_Name + "." + I_C_BPartner.COLUMNNAME_IsActive + " = 'Y'";
		configureBPartnerSearch(colLeadSdp, sdpWhere, colLeadSdpContact);
		configureBPartnerSearch(colSecondarySdp, sdpWhere, colSecondarySdpContact);
		configureBPartnerSearch(colLeadEmployer, empWhere, colLeadEmployerContact);
		configureBPartnerSearch(colSecondaryEmployer, empWhere, colSecondaryEmployerContact);

		configureUserContactSearch(colLeadSdpContact, colLeadSdp);
		configureUserContactSearch(colSecondarySdpContact, colSecondarySdp);
		configureUserContactSearch(colLeadEmployerContact, colLeadEmployer);
		configureUserContactSearch(colSecondaryEmployerContact, colSecondaryEmployer);

		configureYearSearch(colFinancialYear);

		tmDGProgrammeDetails = TableModel.getTableBean(TableModel.class, cols, false, I_ZZLearnerDGProgramme.Table_Name);
		tmDGProgrammeDetails.setViewModel(ViewType.VIEW_FORM);
		tmDGProgrammeDetails.setSclass("two-col srd-dg-programme");
		tmDGProgrammeDetails.setDaoManage(dgDaoManage);
		tmDGProgrammeDetails.init();

		if (colLeadSdp != null && tmDGProgrammeDetails.getRow() != null && adminBpId > 0)
		{
			CellModel sdpCell = tmDGProgrammeDetails.getRow().get(colLeadSdp);
			if (sdpCell != null)
			{
				MBPartner sdpBp = new MBPartner(Env.getCtx(), adminBpId, null);
				sdpCell.setValue(sdpBp);
				sdpCell.resetValidate();
			}
		}
		if (colLeadSdpContact != null && tmDGProgrammeDetails.getRow() != null && loginId > 0)
		{
			CellModel contactCell = tmDGProgrammeDetails.getRow().get(colLeadSdpContact);
			if (contactCell != null)
			{
				MUser sdpUser = new MUser(Env.getCtx(), loginId, null);
				contactCell.setValue(sdpUser);
				contactCell.resetValidate();
			}
		}
	}

	private void initDGDocuments()
	{
		List<ColumnModel> cols = new ArrayList<>();
		String docHeader = MasterUtil.getNameOfColTranslated(I_ZZRegistrationDocs.Table_Name, I_ZZRegistrationDocs.COLUMNNAME_Name);
		if (docHeader == null || docHeader.isBlank())
		{
			docHeader = "Document Description";
		}
		dgDocumentNameCol = PresetTitleCellModel.getPresetTitleColumnModel(
																			docHeader,
																			doc -> doc != null ? (doc.getName() + (doc.isMandatory() ? " *" : "")) : "");
		cols.add(dgDocumentNameCol);

		dgUploadCol = UploadCellModel.getUploadColumnModel("Action", null, null, "Upload Document");
		cols.add(dgUploadCol);

		tmDGDocuments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZRegistrationDocs.Table_Name);
		tmDGDocuments.setViewModel(ViewType.VIEW_GRID);
		tmDGDocuments.setSclass("srd-dg-documents");
		tmDGDocuments.setCreateNewRowWhenEmpty(false);
		tmDGDocuments.init(null, TitleInfo.empty);
	}

	private List<X_ZZRegistrationDocs> loadRegistrationDocumentDefinitions(int programmeMasterId, boolean includeSharedDocuments)
	{
		if (programmeMasterId <= 0 && !includeSharedDocuments)
			return new ArrayList<>();

		String programmeFilter;
		if (programmeMasterId > 0 && includeSharedDocuments)
		{
			programmeFilter = "(" + I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ? OR "
					+ I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " IS NULL)";
		}
		else if (programmeMasterId > 0)
		{
			programmeFilter = I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?";
		}
		else
		{
			programmeFilter = I_ZZRegistrationDocs.COLUMNNAME_ZZ_Program_Master_Data_ID + " IS NULL";
		}

		String sql = "SELECT * FROM " + I_ZZRegistrationDocs.Table_Name
				+ " WHERE " + programmeFilter + " AND " + I_ZZRegistrationDocs.COLUMNNAME_IsActive + " = 'Y' "
				+ "ORDER BY " + I_ZZRegistrationDocs.COLUMNNAME_Name + ", "
				+ I_ZZRegistrationDocs.COLUMNNAME_ZZRegistrationDocs_ID;

		Map<String, X_ZZRegistrationDocs> uniqueDocuments = new LinkedHashMap<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			if (programmeMasterId > 0)
			{
				pstmt.setInt(1, programmeMasterId);
			}
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				X_ZZRegistrationDocs candidate = new X_ZZRegistrationDocs(Env.getCtx(), rs, null);
				String name = candidate.getName();
				String documentKey = name == null || name.isBlank()
						? "#" + candidate.getZZRegistrationDocs_ID()
						: name.trim().toLowerCase(Locale.ROOT);
				X_ZZRegistrationDocs current = uniqueDocuments.get(documentKey);
				if (current == null || (candidate.isMandatory() && !current.isMandatory()))
				{
					uniqueDocuments.put(documentKey, candidate);
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Failed to load registration documents for programme master data " + programmeMasterId, e);
		}
		finally
		{
			DB.close(rs, pstmt);
		}
		return new ArrayList<>(uniqueDocuments.values());
	}

	private void loadDGDocuments()
	{
		if (tmDGDocuments == null || dgDocumentNameCol == null || dgUploadCol == null)
		{
			initDGDocuments();
		}

		int programmeMasterId = selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0;
		List<X_ZZRegistrationDocs> docUploads = loadRegistrationDocumentDefinitions(programmeMasterId, false);

		boolean hasDisabilityLetter = false;
		for (X_ZZRegistrationDocs doc : docUploads)
		{
			if (doc.getName() != null && doc.getName().trim().equalsIgnoreCase("Doctor - Disability Letter"))
			{
				hasDisabilityLetter = true;
				break;
			}
		}
		if (!hasDisabilityLetter)
		{
			X_ZZRegistrationDocs disDoc = new X_ZZRegistrationDocs(Env.getCtx(), 0, null);
			disDoc.setName("Doctor - Disability Letter");
			disDoc.setIsMandatory(isDisabilitySelected());
			docUploads.add(disDoc);
		}

		if (docUploads.isEmpty())
		{
			tmDGDocuments.resetMultiPo(new ArrayList<>());
			return;
		}

		List<Map<ColumnModel, Object>> rowTitles = new ArrayList<>();
		for (X_ZZRegistrationDocs doc : docUploads)
		{
			Map<ColumnModel, Object> m = new HashMap<>();
			m.put(dgDocumentNameCol, doc);
			rowTitles.add(m);
		}

		tmDGDocuments.resetMultiPo(null, TitleInfo.createTitleInfo(rowTitles));

		// Check for existing draft to populate any previously attached files
		X_ZZLearnerDGProgramme existingDg = findExistingDGDraft(
																getSelectedLearnerId(),
																selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0,
																null);

		boolean isDisabilityYes = isDisabilitySelected();
		for (int i = 0; i < docUploads.size() && i < tmDGDocuments.getRows().size(); i++)
		{
			X_ZZRegistrationDocs doc = docUploads.get(i);
			RowModel row = tmDGDocuments.getRows().get(i);
			CellModel uploadCell = row.get(dgUploadCol);
			if (uploadCell instanceof UploadCellModel)
			{
				UploadCellModel uCell = (UploadCellModel) uploadCell;
				boolean isMandatory = doc.isMandatory();
				if (doc.getName() != null && doc.getName().trim().equalsIgnoreCase("Doctor - Disability Letter"))
				{
					isMandatory = isDisabilityYes;
					doc.setIsMandatory(isMandatory);
				}
				uCell.setBtText(isMandatory ? "Upload Document *" : "Upload Document");
				if (existingDg != null)
				{
					String existingFile = AttachmentUtil.getFileNameFromAttachmentEntries(existingDg, doc.getName(), null);
					if (existingFile != null && !existingFile.isBlank())
					{
						uCell.setFileName(existingFile);
					}
				}
			}
		}

		BindUtils.postNotifyChange(null, null, tmDGDocuments, "rows");
	}

	public TableModel getTmRegistrationDocuments()
	{
		return tmRegistrationDocuments;
	}

	public int getRegSelectedTabIndex()
	{
		return regSelectedTabIndex;
	}

	public void setRegSelectedTabIndex(int regSelectedTabIndex)
	{
		this.regSelectedTabIndex = regSelectedTabIndex;
		BindUtils.postNotifyChange(null, null, this, "regSelectedTabIndex");
	}

	private int getProgramMasterDataIdForIntervention(String interventionValue)
	{
		InterventionConfig config = getInterventionConfig(interventionValue);
		if (config == null)
			return 0;

		return Math.max(0, DB.getSQLValueEx(null, "SELECT " + I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID
				+ " FROM " + I_ZZ_Program_Master_Data.Table_Name
				+ " WHERE " + I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_UU + " = ?",
			config.programmeMasterUuid));
	}

	private void initRegistrationDocuments()
	{
		List<ColumnModel> cols = new ArrayList<>();
		String docHeader = MasterUtil.getNameOfColTranslated(I_ZZRegistrationDocs.Table_Name, I_ZZRegistrationDocs.COLUMNNAME_Name);
		if (docHeader == null || docHeader.isBlank())
		{
			docHeader = "Document Description";
		}
		regDocumentNameCol = PresetTitleCellModel.getPresetTitleColumnModel(
																			docHeader,
																			doc -> doc != null ? (doc.getName() + (doc.isMandatory() ? " *" : "")) : "");
		cols.add(regDocumentNameCol);

		regUploadCol = UploadCellModel.getUploadColumnModel("Action", null, null, "Upload Document");
		cols.add(regUploadCol);

		tmRegistrationDocuments = TableModel.getTableBean(TableModel.class, cols, false, I_ZZRegistrationDocs.Table_Name);
		tmRegistrationDocuments.setViewModel(ViewType.VIEW_GRID);
		tmRegistrationDocuments.setSclass("srd-dg-documents");
		tmRegistrationDocuments.setCreateNewRowWhenEmpty(false);
		tmRegistrationDocuments.init(null, TitleInfo.empty);
	}

	private void loadRegistrationDocuments(int selectedRecordId)
	{
		if (tmRegistrationDocuments == null || regDocumentNameCol == null || regUploadCol == null)
		{
			initRegistrationDocuments();
		}

		regSelectedTabIndex = 0;
		BindUtils.postNotifyChange(null, null, this, "regSelectedTabIndex");

		int pmdId = getProgramMasterDataIdForIntervention(selectedIntervention != null ? selectedIntervention.getValue() : null);
		if (pmdId <= 0 && selectedIntervention != null)
		{
			pmdId = getProgramMasterDataIdForIntervention(selectedIntervention.getName());
		}

		List<X_ZZRegistrationDocs> docUploads = loadRegistrationDocumentDefinitions(pmdId, true);

		boolean hasDisabilityLetter = false;
		for (X_ZZRegistrationDocs doc : docUploads)
		{
			if (doc.getName() != null && doc.getName().trim().equalsIgnoreCase("Doctor - Disability Letter"))
			{
				hasDisabilityLetter = true;
				break;
			}
		}
		if (!hasDisabilityLetter)
		{
			X_ZZRegistrationDocs disDoc = new X_ZZRegistrationDocs(Env.getCtx(), 0, null);
			disDoc.setName("Doctor - Disability Letter");
			disDoc.setIsMandatory(isDisabilitySelected());
			docUploads.add(disDoc);
		}

		if (docUploads.isEmpty())
		{
			tmRegistrationDocuments.resetMultiPo(new ArrayList<>());
			BindUtils.postNotifyChange(null, null, this, "tmRegistrationDocuments");
			return;
		}

		List<Map<ColumnModel, Object>> rowTitles = new ArrayList<>();
		for (X_ZZRegistrationDocs doc : docUploads)
		{
			Map<ColumnModel, Object> m = new HashMap<>();
			m.put(regDocumentNameCol, doc);
			rowTitles.add(m);
		}

		tmRegistrationDocuments.resetMultiPo(null, TitleInfo.createTitleInfo(rowTitles));

		// Only the currently loaded draft can provide existing attachments.
		PO existingRecord = null;
		if (selectedIntervention != null && selectedRecordId > 0)
		{
			String tableName = getTableNameForIntervention(selectedIntervention.getValue());
			if (tableName != null)
			{
				existingRecord = MTable.get(Env.getCtx(), tableName).getPO(selectedRecordId, null);
			}
		}

		boolean isDisabilityYes = isDisabilitySelected();
		for (int i = 0; i < docUploads.size() && i < tmRegistrationDocuments.getRows().size(); i++)
		{
			X_ZZRegistrationDocs doc = docUploads.get(i);
			RowModel row = tmRegistrationDocuments.getRows().get(i);
			CellModel uploadCell = row.get(regUploadCol);
			if (uploadCell instanceof UploadCellModel)
			{
				UploadCellModel uCell = (UploadCellModel) uploadCell;
				boolean isMandatory = doc.isMandatory();
				if (doc.getName() != null && doc.getName().trim().equalsIgnoreCase("Doctor - Disability Letter"))
				{
					isMandatory = isDisabilityYes;
					doc.setIsMandatory(isMandatory);
				}
				uCell.setBtText(isMandatory ? "Upload Document *" : "Upload Document");
				if (existingRecord != null && existingRecord.get_ID() > 0)
				{
					String existingFile = AttachmentUtil.getFileNameFromAttachmentEntries(existingRecord, doc.getName(), null);
					if (existingFile != null && !existingFile.isBlank())
					{
						uCell.setFileName(existingFile);
					}
				}
			}
		}

		BindUtils.postNotifyChange(null, null, this, "tmRegistrationDocuments");
	}

	private void saveRegistrationDocuments(String trxName)
	{
		if (tmRegistrationDocuments == null || tmRegistrationDocuments.getRows() == null || selectedIntervention == null)
			return;

		String tableName = getTableNameForIntervention(selectedIntervention.getValue());
		if (tableName == null)
			return;

		List<PO> targetPos = new ArrayList<>();
		if (tmRegistration != null && tmRegistration.getRows() != null)
		{
			for (RowModel row : tmRegistration.getRows())
			{
				if (row.getRowData() != null)
				{
					PO po = row.getRowData().getDataNullable(tableName);
					if (po != null && po.get_ID() > 0 && !targetPos.contains(po))
					{
						targetPos.add(po);
					}
				}
			}
		}

		if (targetPos.isEmpty())
			return;

		for (PO targetPo : targetPos)
		{
			for (RowModel docRow : tmRegistrationDocuments.getRows())
			{
				CellModel docNameCell = docRow.get(regDocumentNameCol);
				String docName = null;
				if (docNameCell != null && docNameCell.getValue() instanceof X_ZZRegistrationDocs)
				{
					docName = ((X_ZZRegistrationDocs) docNameCell.getValue()).getName();
				}
				if (docName == null || docName.isBlank())
				{
					docName = "Registration Document";
				}

				CellModel cell = docRow.get(regUploadCol);
				if (cell instanceof UploadCellModel)
				{
					UploadCellModel uCell = (UploadCellModel) cell;
					byte[] bytes = uCell.getBytes();
					String fileName = uCell.getFileName();
					if (bytes != null && bytes.length > 0 && fileName != null && !fileName.isBlank())
					{
						AttachmentUtil.addOrReplaceAttachmentEntry(targetPo, fileName, bytes, docName, trxName);
						uCell.setBytes(null);
					}
					else if (fileName == null || fileName.isBlank())
					{
						AttachmentUtil.removeAttachmentEntry(targetPo, docName, trxName);
					}
				}
			}
		}
	}

	private List<String> validateDGProgrammeDetails()
	{
		List<String> missingCols = new ArrayList<>();
		if (tmDGProgrammeDetails == null || tmDGProgrammeDetails.getRow() == null)
			return missingCols;

		RowModel row = tmDGProgrammeDetails.getRow();
		for (ColumnModel col : row.keySet())
		{
			CellModel cell = row.get(col);
			if (col.isMandatory())
			{
				Object val = cell != null ? cell.getValue() : null;
				if (cell instanceof DateCellModel)
				{
					Timestamp ts = ((DateCellModel) cell).getTimestamp();
					if (ts != null)
						val = ts;
					else if (((DateCellModel) cell).getLocalDate() != null)
						val = ((DateCellModel) cell).getLocalDate();
				}
				if (cell instanceof ValueAdaptCellModel)
				{
					Object sel = ((ValueAdaptCellModel) cell).getSelectedValue();
					if (sel != null)
					{
						if (sel instanceof PO && ((PO) sel).get_ID() > 0)
							val = ((PO) sel).get_ID();
						else if (sel instanceof Number && ((Number) sel).intValue() > 0)
							val = sel;
						else if (val == null)
							val = sel;
					}
					if (val == null)
					{
						Object disp = ((ValueAdaptCellModel) cell).getDisplayValue();
						if (disp != null && !disp.toString().isBlank())
							val = disp;
					}
				}
				boolean empty = false;
				if (val == null)
				{
					empty = true;
				}
				else if (val instanceof String && ((String) val).isBlank())
				{
					empty = true;
				}
				else if (val instanceof Number && ((Number) val).intValue() <= 0)
				{
					empty = true;
				}
				else if (val instanceof ValueNamePair && (((ValueNamePair) val).getValue() == null || ((ValueNamePair) val).getValue().isBlank()))
				{
					empty = true;
				}
				if (empty)
				{
					String title = col.getTitle();
					if (title != null && !title.isBlank())
						missingCols.add(title);
				}
			}
			else if (cell != null)
			{
				Object val = cell.getValue();
				if (val != null && !(val instanceof String && ((String) val).isBlank()))
				{
					if (!cell.validate())
					{
						List<String> errs = cell.getValidateMsgs();
						if (errs != null && !errs.isEmpty())
						{
							for (String err : errs)
							{
								missingCols.add(col.getTitle() + ": " + err);
							}
						}
					}
				}
			}
		}
		return missingCols;
	}

	@Command
	public void nextDGTab()
	{
		if (getSelectedLearnerId() <= 0)
		{
			MasterUtil.showInfoDialog("Please select a learner first.", null);
			return;
		}
		if (selectedDGProgramme == null)
		{
			MasterUtil.showInfoDialog("Please select a DG Programme.", null);
			return;
		}
		List<String> missingCols = validateDGProgrammeDetails();
		if (!missingCols.isEmpty())
		{
			String msg = "Please fill all mandatory fields or correct the following:\n• " + String.join("\n• ", missingCols);
			MasterUtil.showInfoDialog(msg, null);
			return;
		}
		downloadLearnerAgreement();
		setDgSelectedTabIndex(1);
	}

	@Command
	public void saveDGAppDraft()
	{
		if (getSelectedLearnerId() <= 0)
		{
			MasterUtil.showInfoDialog("Please select a learner first.", null);
			return;
		}
		if (selectedDGProgramme == null)
		{
			MasterUtil.showInfoDialog("Please select a DG Programme.", null);
			return;
		}
		List<String> missingCols = validateDGProgrammeDetails();
		if (!missingCols.isEmpty())
		{
			String msg = "Please fill all mandatory fields or correct the following:\n• " + String.join("\n• ", missingCols);
			MasterUtil.showInfoDialog(msg, null);
			return;
		}

		isDgSubmit = false;
		trxWrapper(trxName -> {
			saveDGRegistration(trxName, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
		});
	}

	@Command
	public void submitDGApp()
	{
		if (getSelectedLearnerId() <= 0)
		{
			MasterUtil.showInfoDialog("Please select a learner first.", null);
			return;
		}
		if (selectedDGProgramme == null)
		{
			MasterUtil.showInfoDialog("Please select a DG Programme.", null);
			return;
		}
		List<String> missingCols = validateDGProgrammeDetails();
		if (!missingCols.isEmpty())
		{
			String msg = "Please fill all mandatory fields or correct the following:\n• " + String.join("\n• ", missingCols);
			MasterUtil.showInfoDialog(msg, null);
			return;
		}

		List<String> missingDocs = getMissingMandatoryDocuments(tmDGDocuments, dgDocumentNameCol, dgUploadCol);
		if (!missingDocs.isEmpty())
		{
			MasterUtil.showInfoDialog("Please upload the following mandatory documents:\n• " + String.join("\n• ", missingDocs), null);
			return;
		}

		isDgSubmit = true;
		trxWrapper(trxName -> {
			saveDGRegistration(trxName, X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Created);
		});
	}

	private List<String> validateRegistrationProgrammeDetails()
	{
		List<String> missingCols = new ArrayList<>();
		if (tmRegistration == null || tmRegistration.getRows() == null)
			return missingCols;

		for (RowModel row : tmRegistration.getRows())
		{
			if (row != null && row.isEditable())
			{
				for (ColumnModel col : row.keySet())
				{
					if (col != null && col.isMandatory())
					{
						CellModel cell = row.get(col);
						Object val = cell != null ? cell.getValue() : null;
						if (cell instanceof DateCellModel)
						{
							Timestamp ts = ((DateCellModel) cell).getTimestamp();
							if (ts != null)
								val = ts;
							else if (((DateCellModel) cell).getLocalDate() != null)
								val = ((DateCellModel) cell).getLocalDate();
						}
						if (cell instanceof ValueAdaptCellModel)
						{
							Object sel = ((ValueAdaptCellModel) cell).getSelectedValue();
							if (sel != null)
							{
								if (sel instanceof PO && ((PO) sel).get_ID() > 0)
									val = ((PO) sel).get_ID();
								else if (sel instanceof Number && ((Number) sel).intValue() > 0)
									val = sel;
								else if (val == null)
									val = sel;
							}
						}
						boolean empty = false;
						if (val == null)
							empty = true;
						else if (val instanceof String && ((String) val).isBlank())
							empty = true;
						else if (val instanceof Number && ((Number) val).intValue() <= 0)
							empty = true;
						else if (val instanceof ValueNamePair)
						{
							String v = ((ValueNamePair) val).getValue();
							if (v == null || v.isBlank())
								empty = true;
						}

						if (empty)
						{
							String title = col.getTitle();
							if (title != null && !title.isBlank() && !missingCols.contains(title))
								missingCols.add(title);
						}
					}
				}
			}
		}
		return missingCols;
	}

	private boolean isDisabilitySelected()
	{
		return personSelected != null && personSelected.getZZ_LI_Disability_ID() > 1000000;
	}

	private List<String> getMissingMandatoryDocuments(	TableModel documentTable, ColumnModel documentNameColumn,
													ColumnModel uploadColumn)
	{
		List<String> missingDocuments = new ArrayList<>();
		if (documentTable == null || documentTable.getRows() == null || documentNameColumn == null || uploadColumn == null)
			return missingDocuments;

		for (RowModel row : documentTable.getRows())
		{
			CellModel documentNameCell = row.get(documentNameColumn);
			if (documentNameCell == null || !(documentNameCell.getValue() instanceof X_ZZRegistrationDocs))
				continue;

			X_ZZRegistrationDocs document = (X_ZZRegistrationDocs) documentNameCell.getValue();
			if (document.getName() != null && document.getName().trim().equalsIgnoreCase("Doctor - Disability Letter"))
			{
				if (!isDisabilitySelected())
					continue;
			}
			else if (!document.isMandatory())
			{
				continue;
			}

			CellModel uploadCell = row.get(uploadColumn);
			if (uploadCell instanceof UploadCellModel)
			{
				UploadCellModel upload = (UploadCellModel) uploadCell;
				if (!upload.hasBytes() && (upload.getFileName() == null || upload.getFileName().isBlank()))
				{
					missingDocuments.add(document.getName());
				}
			}
		}
		return missingDocuments;
	}

	@Command
	public void nextRegTab()
	{
		List<String> missingCols = validateRegistrationProgrammeDetails();
		if (!missingCols.isEmpty())
		{
			String msg = "Please fill all mandatory fields marked with *:\n• " + String.join("\n• ", missingCols);
			MasterUtil.showInfoDialog(msg, null);
			return;
		}
		downloadLearnerAgreement();
		setRegSelectedTabIndex(1);
	}

	private static JasperReport learnerAgreementReport;

	/** Cached per plugin class loader; a failed compile leaves the cache empty for retry. */
	static synchronized JasperReport getLearnerAgreementReport() throws Exception
	{
		if (learnerAgreementReport == null)
		{
			try (InputStream source = LearnerQualficationRegVM.class.getResourceAsStream(
					"/za/co/ntier/report/jrxmls/LearnerAgreement.jrxml"))
			{
				if (source == null) throw new IllegalStateException("Learner Agreement template is missing.");
				learnerAgreementReport = JasperCompileManager.compileReport(source);
			}
		}
		return learnerAgreementReport;
	}

	private void downloadLearnerAgreement()
	{
		try
		{
			Map<String, Object> jasperParams = new HashMap<>();
			jasperParams.put(LearnerAgreementData.PARAMETER, getLearnerAgreementData());
			ClassLoader reportClassLoader = new ClassLoader(LearnerQualficationRegVM.class.getClassLoader())
			{
				@Override
				public InputStream getResourceAsStream(String name)
				{
					InputStream is = super.getResourceAsStream(name);
					if (is == null && name != null && name.startsWith("/"))
					{
						is = super.getResourceAsStream(name.substring(1));
					}
					return is;
				}
			};
			jasperParams.put(JRParameter.REPORT_CLASS_LOADER, reportClassLoader);

			JasperReport jasperReport = getLearnerAgreementReport();

			if (jasperReport != null)
			{
				JasperPrint print = JasperFillManager.fillReport(jasperReport, jasperParams, new JREmptyDataSource(1));
				byte[] pdfBytes = JasperExportManager.exportReportToPdf(print);
				if (pdfBytes != null && pdfBytes.length > 0)
				{
					Filedownload.save(pdfBytes, "application/pdf", "Learner_Agreement.pdf");
					return;
				}
			}

			MasterUtil.showInfoDialog("Could not generate Learner Agreement.", null);
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "Could not download Learner Agreement: " + e.getMessage(), e);
			MasterUtil.showInfoDialog("Could not generate Learner Agreement: " + e.getMessage(), null);
		}
	}

	private Map<String, String> getLearnerAgreementData()
	{
		InterventionConfig config = selectedIntervention == null ? null : getInterventionConfig(selectedIntervention.getValue());
		RowModel row = dgProgrammeActive && tmDGProgrammeDetails != null ? tmDGProgrammeDetails.getRow()
				: tmRegistration != null ? tmRegistration.getRow() : null;
		if (!dgProgrammeActive && tmRegistration != null && tmRegistration.getRows() != null)
		{
			List<RowModel> editable = new ArrayList<>();
			for (RowModel candidate : tmRegistration.getRows())
				if (candidate != null && candidate.isEditable()) editable.add(candidate);
			if (editable.size() != 1)
				throw new AdempiereException("Please select one registration row before generating the agreement.");
			row = editable.get(0);
		}
		if (row == null)
			throw new AdempiereException("Please select a registration row before generating the agreement.");
		Map<String, Object> values = new HashMap<>();
		String fundingType = "";
		for (ColumnModel column : row.keySet())
		{
			CellModel cell = row.get(column);
			if (cell != null && column.getDaoPropertyName() != null)
			{
				Object value = cell instanceof DateCellModel ? ((DateCellModel) cell).getTimestamp() : cell.getValue();
				if (cell instanceof ListCellModel)
					value = ((ListCellModel<?>) cell).getSelectedItem();
				if (cell instanceof ValueAdaptCellModel && ((ValueAdaptCellModel) cell).getSelectedValue() != null)
					value = ((ValueAdaptCellModel) cell).getSelectedValue();
				values.put(column.getDaoPropertyName(), value);
				if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType.equals(column.getDaoPropertyName()))
				{
					if (value instanceof ValueNamePair)
						fundingType = ((ValueNamePair) value).getName();
					else if (value != null)
					{
						String table = dgProgrammeActive ? I_ZZLearnerDGProgramme.Table_Name
								: config != null ? config.learnerTable : column.getTableName();
						MColumn metadata = table == null ? null : MTable.get(Env.getCtx(), table).getColumn(column.getDaoPropertyName());
						if (metadata != null && metadata.getAD_Reference_Value_ID() > 0)
							fundingType = MRefList.getListName(Env.getCtx(), metadata.getAD_Reference_Value_ID(), value.toString());
					}
				}
			}
		}
		String type = config == null ? "" : config.displayName;
		List<String> warnings = new ArrayList<>();
		Map<String, String> data = LearnerAgreementData.build(new X_ZZLearner_v(Env.getCtx(), getSelectedLearnerId(), null), values,
				config == null ? null : config.learnerProgrammeColumn, config == null ? null : config.programmeTable,
				config == null ? null : config.programmeCodeColumn, type, warnings);
		if (!warnings.isEmpty())
			MasterUtil.showInfoDialog("Agreement data needs review:\n• " + String.join("\n• ", warnings), null);
		data.put(LearnerAgreementData.FUNDING_TYPE, fundingType);
		if (dgProgrammeActive && selectedDGProgramme != null)
		{
			data.put(LearnerAgreementData.QUALIFICATION_TITLE, selectedDGProgramme.getTitle());
			data.put(LearnerAgreementData.PROGRAMME_TYPE, selectedDGProgramme.getTitle());
		}
		return data;
	}

	@Command
	public void prevDGTab()
	{
		setDgSelectedTabIndex(0);
	}

	@Command
	public void prevRegTab()
	{
		setRegSelectedTabIndex(0);
	}

	@Command
	public void saveRegAppDraft()
	{
		saveApp();
	}

	@Command
	public void submitRegApp()
	{
		if (getSelectedLearnerId() <= 0)
		{
			MasterUtil.showInfoDialog("Please select a learner first.", null);
			return;
		}

		List<String> missingCols = validateRegistrationProgrammeDetails();
		if (!missingCols.isEmpty())
		{
			String msg = "Please fill all mandatory fields marked with *:\n• " + String.join("\n• ", missingCols);
			MasterUtil.showInfoDialog(msg, null);
			return;
		}

		List<String> missingDocs = getMissingMandatoryDocuments(tmRegistrationDocuments, regDocumentNameCol, regUploadCol);
		if (!missingDocs.isEmpty())
		{
			MasterUtil.showInfoDialog("Please upload the following mandatory documents:\n• " + String.join("\n• ", missingDocs), null);
			return;
		}

		submitApp();
	}

	@Command
	public void nextTab()
	{
		if (dgProgrammeActive)
			nextDGTab();
		else
			nextRegTab();
	}

	@Command
	public void saveAppDraft()
	{
		if (dgProgrammeActive)
			saveDGAppDraft();
		else
			saveRegAppDraft();
	}

	private void saveDGRegistration(String trxName, String docStatus)
	{
		if (tmDGProgrammeDetails == null || tmDGProgrammeDetails.getRow() == null)
			return;

		RowModel row = tmDGProgrammeDetails.getRow();
		X_ZZLearnerDGProgramme dg = (X_ZZLearnerDGProgramme) dgDaoManage.getDao(I_ZZLearnerDGProgramme.Table_Name);
		if (dg == null)
		{
			dg = findExistingDGDraft(
										getSelectedLearnerId(),
										selectedDGProgramme != null ? selectedDGProgramme.getZZ_Program_Master_Data_ID() : 0,
										trxName);
		}

		if (dg == null)
		{
			dg = new X_ZZLearnerDGProgramme(Env.getCtx(), 0, trxName);
			dg.setZZLearner_ID(getSelectedLearnerId());
			if (selectedDGProgramme != null)
			{
				dg.setZZ_Program_Master_Data_ID(selectedDGProgramme.getZZ_Program_Master_Data_ID());
			}
		}
		else if (trxName != null)
		{
			dg.set_TrxName(trxName);
		}

		for (ColumnModel col : row.keySet())
		{
			String prop = col.getDaoPropertyName();
			if (prop == null)
				continue;
			CellModel cell = row.get(col);
			// For ListCellModel the underlying value may be a ValueNamePair or a plain String (the
			// code).
			// Calling getValue() on a ListCellModel returns the stored value directly, so we read
			// it once.
			Object val = cell != null ? cell.getValue() : null;

			if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZAgreementReferenceNumber.equals(prop))
			{
				dg.setZZAgreementReferenceNumber(extractString(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZCommencementDate.equals(prop))
			{
				Timestamp ts = extractTimestamp(cell, val);
				if (ts != null)
					dg.setZZCommencementDate(ts);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZCompletionDate.equals(prop))
			{
				Timestamp ts = extractTimestamp(cell, val);
				if (ts != null)
					dg.setZZCompletionDate(ts);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZContractNumber.equals(prop))
			{
				dg.setZZContractNumber(extractString(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSocioEconomicStatus.equals(prop))
			{
				// extractString returns the ValueNamePair's *value* (the DB code), not the display
				// label.
				String status = extractString(val);
				if (status != null && !status.isBlank())
					dg.setZZSocioEconomicStatus(status);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_SDP_ID.equals(prop))
			{
				int id = extractId(val);
				if (id > 0)
					dg.setZZ_SDP_ID(id);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderLevy.equals(prop))
			{
				// Always save – even if blank, so the field is honoured when user clears it
				dg.setZZLeadSDProviderLevy(extractString(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZLeadSDProviderContact_ID.equals(prop))
			{
				int id = extractId(val);
				if (id > 0)
					dg.setZZLeadSDProviderContact_ID(id);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProvider_ID.equals(prop))
			{
				// Optional – unconditionally set so that clearing the field is persisted
				dg.setZZSecondarySDProvider_ID(extractId(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderLevy.equals(prop))
			{
				dg.setZZSecondarySDProviderLevy(extractString(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondarySDProviderContact_ID.equals(prop))
			{
				// Optional – unconditionally set
				dg.setZZSecondarySDProviderContact_ID(extractId(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Employer_ID.equals(prop))
			{
				int id = extractId(val);
				if (id > 0)
					dg.setZZ_Employer_ID(id);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZWALevy.equals(prop))
			{
				// Always save – even if blank
				dg.setZZWALevy(extractString(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZWAContact_ID.equals(prop))
			{
				int id = extractId(val);
				if (id > 0)
					dg.setZZWAContact_ID(id);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWA_ID.equals(prop))
			{
				// Optional – unconditionally set
				dg.setZZSecondaryWA_ID(extractId(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWALevy.equals(prop))
			{
				dg.setZZSecondaryWALevy(extractString(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZSecondaryWAContact_ID.equals(prop))
			{
				// Optional – unconditionally set
				dg.setZZSecondaryWAContact_ID(extractId(val));
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFundingType.equals(prop))
			{
				// extractString returns the ValueNamePair's *value* (the DB code directly)
				String ft = extractString(val);
				if (ft != null && !ft.isBlank())
					dg.setZZFundingType(ft);
			}
			else if (I_ZZLearnerDGProgramme.COLUMNNAME_ZZFinancialYear_ID.equals(prop))
			{
				int id = extractId(val);
				if (id > 0)
					dg.setZZFinancialYear_ID(id);
			}
		}

		dg.setZZ_DocStatus(docStatus != null ? docStatus : X_ZZLearnerDGProgramme.ZZ_DOCSTATUS_Draft);
		dg.setZZRegistrationDate(new Timestamp(System.currentTimeMillis()));
		dg.saveEx(trxName);
		dgDaoManage.setDao(dg);

		if (tmDGDocuments != null && tmDGDocuments.getRows() != null)
		{
			for (RowModel docRow : tmDGDocuments.getRows())
			{
				CellModel docNameCell = docRow.get(dgDocumentNameCol);
				String docName = null;
				if (docNameCell != null && docNameCell.getValue() instanceof X_ZZRegistrationDocs)
				{
					docName = ((X_ZZRegistrationDocs) docNameCell.getValue()).getName();
				}
				if (docName == null || docName.isBlank())
				{
					docName = "Registration Document";
				}

				CellModel cell = docRow.get(dgUploadCol);
				if (cell instanceof UploadCellModel)
				{
					UploadCellModel uCell = (UploadCellModel) cell;
					byte[] bytes = uCell.getBytes();
					String fileName = uCell.getFileName();
					if (bytes != null && bytes.length > 0 && fileName != null && !fileName.isBlank())
					{
						AttachmentUtil.addOrReplaceAttachmentEntry(dg, fileName, bytes, docName, trxName);
						uCell.setBytes(null);
					}
					else if (fileName == null || fileName.isBlank())
					{
						AttachmentUtil.removeAttachmentEntry(dg, docName, trxName);
					}
				}
			}
		}
	}

	private X_ZZLearnerDGProgramme findExistingDGDraft(int learnerId, int progId, String trxName)
	{
		if (learnerId <= 0 || progId <= 0)
			return null;
		String whereClause = I_ZZLearnerDGProgramme.COLUMNNAME_ZZLearner_ID + " = ? AND "
								+ I_ZZLearnerDGProgramme.COLUMNNAME_ZZ_Program_Master_Data_ID + " = ?";
		return new Query(Env.getCtx(), I_ZZLearnerDGProgramme.Table_Name, whereClause, trxName)
																								.setParameters(learnerId, progId)
																								.setOrderBy(I_ZZLearnerDGProgramme.COLUMNNAME_ZZLearnerDGProgramme_ID
																											+ " DESC")
																								.first();
	}

	private void configureBPartnerSearch(ValueAdaptColumnModel colBp, String whereClause, ValueAdaptColumnModel autoContactCol)
	{
		colBp.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof MBPartner)
				return ((MBPartner) value).getName();
			if (value instanceof PO)
				return ((PO) value).get_ValueAsString(I_C_BPartner.COLUMNNAME_Name);
			return value.toString();
		});
		colBp.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof PO)
				return ((PO) value).get_ID();
			if (value instanceof Number)
				return ((Number) value).intValue();
			return value;
		});
		colBp.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			return id > 0 ? new MBPartner(Env.getCtx(), id, null) : null;
		});
		colBp.setEventHandle((event, cellModel) -> {
			InfoPanelPara para = InfoPanelPara.getInstance(I_C_BPartner.Table_Name, I_C_BPartner.COLUMNNAME_C_BPartner_ID);
			para.setWhereClause(whereClause);
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int bpId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				MBPartner bp = new MBPartner(Env.getCtx(), bpId, null);
				cellModel.setValue(bp);
				cellModel.resetValidate();

				if (autoContactCol != null && cellModel.getRowModel() != null)
				{
					if (bpId == adminBpId && loginId > 0)
					{
						MUser sdpUser = new MUser(Env.getCtx(), loginId, null);
						CellModel contactCell = cellModel.getRowModel().get(autoContactCol);
						if (contactCell != null)
						{
							contactCell.setValue(sdpUser);
							contactCell.resetValidate();
							BindUtils.postNotifyChange(null, null, contactCell, "value");
							BindUtils.postNotifyChange(null, null, contactCell, "displayValue");
						}
					}
					else
					{
						String userWhere = I_AD_User.Table_Name + "." + I_AD_User.COLUMNNAME_C_BPartner_ID + " = ? AND "
											+ I_AD_User.Table_Name + "." + I_AD_User.COLUMNNAME_IsActive + " = 'Y'";
						PO adUser = MTable	.get(Env.getCtx(), I_AD_User.Table_Name)
											.createQuery(userWhere, null)
											.setParameters(bpId)
											.first();
						if (adUser != null)
						{
							CellModel contactCell = cellModel.getRowModel().get(autoContactCol);
							if (contactCell != null)
							{
								contactCell.setValue(adUser);
								contactCell.resetValidate();
								BindUtils.postNotifyChange(null, null, contactCell, "value");
								BindUtils.postNotifyChange(null, null, contactCell, "displayValue");
							}
						}
					}
				}
			});
		});
	}

	private void configureUserContactSearch(ValueAdaptColumnModel colContact, ValueAdaptColumnModel parentBpCol)
	{
		colContact.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof MUser)
				return ((MUser) value).getName();
			if (value instanceof PO)
				return ((PO) value).get_ValueAsString(I_AD_User.COLUMNNAME_Name);
			return value.toString();
		});
		colContact.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof PO)
				return ((PO) value).get_ID();
			if (value instanceof Number)
				return ((Number) value).intValue();
			return value;
		});
		colContact.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			return id > 0 ? new MUser(Env.getCtx(), id, null) : null;
		});
		colContact.setEventHandle((event, cellModel) -> {
			InfoPanelPara para = InfoPanelPara.getInstance(I_AD_User.Table_Name, I_AD_User.COLUMNNAME_AD_User_ID);
			int bpId = 0;
			if (parentBpCol != null && cellModel.getRowModel() != null)
			{
				CellModel bpCell = cellModel.getRowModel().get(parentBpCol);
				if (bpCell != null)
				{
					bpId = extractId(bpCell.getValue());
					if (bpId <= 0 && bpCell instanceof ValueAdaptCellModel)
					{
						bpId = extractId(((ValueAdaptCellModel) bpCell).getSelectedValue());
					}
				}
			}
			String baseWhere = I_AD_User.Table_Name + "." + I_AD_User.COLUMNNAME_IsActive + " = 'Y'";
			if (bpId > 0)
			{
				para.setWhereClause(baseWhere + " AND " + I_AD_User.Table_Name + "." + I_AD_User.COLUMNNAME_C_BPartner_ID + " = " + bpId);
			}
			else
			{
				para.setWhereClause(baseWhere);
			}
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int userId = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				MUser u = new MUser(Env.getCtx(), userId, null);
				cellModel.setValue(u);
				cellModel.resetValidate();
			});
		});
	}

	private void configureYearSearch(ValueAdaptColumnModel colFinancialYear)
	{
		colFinancialYear.setDisplayAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof MYear)
			{
				String desc = ((MYear) value).getDescription();
				return (desc != null && !desc.isBlank()) ? desc : ((MYear) value).getFiscalYear();
			}
			if (value instanceof PO)
			{
				String desc = ((PO) value).get_ValueAsString(I_C_Year.COLUMNNAME_Description);
				return (desc != null && !desc.isBlank()) ? desc : ((PO) value).get_ValueAsString(I_C_Year.COLUMNNAME_FiscalYear);
			}
			if (value instanceof Number)
			{
				int id = ((Number) value).intValue();
				if (id > 0)
				{
					MYear y = new MYear(Env.getCtx(), id, null);
					String desc = y.getDescription();
					return (desc != null && !desc.isBlank()) ? desc : y.getFiscalYear();
				}
			}
			if (value instanceof String)
			{
				String s = (String) value;
				if (s.matches("\\d+"))
				{
					try
					{
						int id = Integer.parseInt(s);
						if (id > 1000)
						{
							MYear y = new MYear(Env.getCtx(), id, null);
							if (y.get_ID() > 0)
							{
								String desc = y.getDescription();
								return (desc != null && !desc.isBlank()) ? desc : y.getFiscalYear();
							}
						}
					}
					catch (Exception ignored)
					{}
				}
				MYear y = new Query(Env.getCtx(), I_C_Year.Table_Name, I_C_Year.COLUMNNAME_FiscalYear + " = ?", null)
																														.setParameters(s)
																														.first();
				if (y != null)
				{
					String desc = y.getDescription();
					if (desc != null && !desc.isBlank())
						return desc;
				}
				return s;
			}
			return value.toString();
		});
		colFinancialYear.setValueAdaptHandle(value -> {
			if (value == null)
				return null;
			if (value instanceof PO)
				return ((PO) value).get_ID();
			if (value instanceof Number)
				return ((Number) value).intValue();
			return value;
		});
		colFinancialYear.setValueFromDaoAdaptHandle(value -> {
			if (value == null)
				return null;
			int id = value instanceof Number ? ((Number) value).intValue() : Integer.parseInt(value.toString());
			return id > 0 ? new MYear(Env.getCtx(), id, null) : null;
		});
		colFinancialYear.setEventHandle((event, cellModel) -> {
			InfoPanelPara para = InfoPanelPara.getInstance(I_C_Year.Table_Name, I_C_Year.COLUMNNAME_C_Year_ID);
			para.setWhereClause(I_C_Year.Table_Name + "." + I_C_Year.COLUMNNAME_IsActive + " = 'Y'");
			showInfoPanel(para, (obj, infoPanel) -> {
				Object[] values = (Object[]) obj;
				if (values == null || values.length == 0 || values[0] == null)
					return;
				int id = values[0] instanceof Number ? ((Number) values[0]).intValue() : Integer.parseInt(values[0].toString());
				MYear yr = new MYear(Env.getCtx(), id, null);
				cellModel.setValue(yr);
				cellModel.resetValidate();
			});
		});
	}

	private Timestamp extractTimestamp(CellModel cell, Object val)
	{
		if (cell instanceof DateCellModel)
		{
			Timestamp ts = ((DateCellModel) cell).getTimestamp();
			if (ts != null)
				return ts;
		}
		if (val instanceof Timestamp)
			return (Timestamp) val;
		if (val instanceof java.util.Date)
			return new Timestamp(((java.util.Date) val).getTime());
		return null;
	}

	private int extractId(Object val)
	{
		if (val == null)
			return 0;
		if (val instanceof PO)
			return ((PO) val).get_ID();
		if (val instanceof ValueNamePair)
		{
			try
			{
				return Integer.parseInt(((ValueNamePair) val).getValue());
			}
			catch (Exception ignored)
			{}
		}
		if (val instanceof Number)
			return ((Number) val).intValue();
		try
		{
			return Integer.parseInt(val.toString());
		}
		catch (Exception e)
		{
			return 0;
		}
	}

	private String extractString(Object val)
	{
		if (val == null)
			return null;
		if (val instanceof ValueNamePair)
			return ((ValueNamePair) val).getValue();
		String s = val.toString();
		if ("Yes".equalsIgnoreCase(s))
			return "Y";
		if ("No".equalsIgnoreCase(s))
			return "N";
		return s;
	}

	public TableModel getTmDGProgrammeDetails()
	{
		return tmDGProgrammeDetails;
	}

	public void setTmDGProgrammeDetails(TableModel tmDGProgrammeDetails)
	{
		this.tmDGProgrammeDetails = tmDGProgrammeDetails;
	}

	public TableModel getTmDGDocuments()
	{
		return tmDGDocuments;
	}

	public void setTmDGDocuments(TableModel tmDGDocuments)
	{
		this.tmDGDocuments = tmDGDocuments;
	}

	public boolean isDgProgrammeActive()
	{
		return dgProgrammeActive;
	}

	public void setDgProgrammeActive(boolean dgProgrammeActive)
	{
		this.dgProgrammeActive = dgProgrammeActive;
	}

	public X_ZZ_Program_Master_Data getSelectedDGProgramme()
	{
		return selectedDGProgramme;
	}

	public void setSelectedDGProgramme(X_ZZ_Program_Master_Data selectedDGProgramme)
	{
		this.selectedDGProgramme = selectedDGProgramme;
	}

	public int getDgSelectedTabIndex()
	{
		return dgSelectedTabIndex;
	}

	public void setDgSelectedTabIndex(int dgSelectedTabIndex)
	{
		if (dgSelectedTabIndex == 1 && this.dgSelectedTabIndex == 0)
		{
			List<String> missingCols = validateDGProgrammeDetails();
			if (!missingCols.isEmpty())
			{
				String msg = "Please fill all mandatory fields or correct the following:\n• " + String.join("\n• ", missingCols);
				MasterUtil.showInfoDialog(msg, null);
				this.dgSelectedTabIndex = 0;
				BindUtils.postNotifyChange(null, null, this, "dgSelectedTabIndex");
				return;
			}
		}
		this.dgSelectedTabIndex = dgSelectedTabIndex;
		BindUtils.postNotifyChange(null, null, this, "dgSelectedTabIndex");
	}
}
