package za.co.ntier.webform.form.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.model.X_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZ_FormDiscipline;

public class ProgramCetTvetInfo implements ISaveForm {
	public static final String COL_NAME_titleHeaderText = "titleHeaderText";
	public static final String COL_NAME_customizeDetaileTemplate = "customizeDetaileTemplate";
	public static final String DetaileTemplate_LecturersExposedIndustry = "Lecturers exposed to industry";

	public static final String ATTSUBAnnexureCustomize = "Attribute Sub Annexure Customize";
	public static final String SUBAnnexure_TradeBeneficiaries = "TradeBeneficiaries";

	public static final String ATTSUBAnnexureColumnType = "Attribute Sub Annexure Column Type";

	public static final String SUBAnnexureColumnType_Number = "Number";
	private MenuContextInfo menuContextInfo;

	private List<AnnexureInfo> annexureInfos;
	private AddressInfo addressInfo;

	private List<LearnerInputInfo> tradeInfo;

	@SuppressWarnings("unchecked")
	public ProgramCetTvetInfo(MenuContextInfo menuContextInfo) throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.setMenuContextInfo(menuContextInfo);

		annexureInfos = new ArrayList<>();
		AnnexureInfo annexure = null;
		AnnexureInfo subAnnexure = null;
		if (menuContextInfo.getProgramType() == ProgramType.CET) {
			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE A (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Number Of Beneficiaries Applying For")),
					"CET learners funded to access AET Programmes");

			annexureInfos.add(annexure);

			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE B (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Number of beneficiaries applying for"),
							ColumnInfo.getColPositiveNumber("Discipline Applying For")),
					"Number of TVET Colleges and HEI graduates that entered CET Internships");

			annexureInfos.add(annexure);

			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE C (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Number of beneficiaries applying for")),
					"CET Managers receiving training on curriculum related studies");

			subAnnexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
					List.of(ColumnInfo.getColPositiveNumber("Requested Programme"),
							ColumnInfo.getColPositiveNumber("Number of managers")));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE D (Applicable to CET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Number of beneficiaries applying for")),
					"Number of CET Colleges lecturers awarded skills development programmes");

			subAnnexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
					List.of(ColumnInfo.getColPositiveNumber("Requested Programme")));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);
		} else if (menuContextInfo.getProgramType() == ProgramType.TVET) {
			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE E (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Number of beneficiaries applying for"),
							ColumnInfo.getColPositiveNumber("Discipline Applying For")),
					"Number of TVET College graduates that entered an internship programme (the MQA prioritises engineering and related disciplines and may support other disciplines at its sole discretion)");

			subAnnexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
					List.of(ColumnInfo.getColPositiveNumber("Field of Study"),
							ColumnInfo.getColPositiveNumber("Number of learners")));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE F  (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Number of beneficiaries applying for"),
							ColumnInfo.getColPositiveNumber("Programme Applying For")),
					"TVET Managers receiving training on curriculum related studies");

			subAnnexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
					List.of(ColumnInfo.getColPositiveNumber("Requested Programme"),
							ColumnInfo.getColPositiveNumber("Number of managers")));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE G (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Total Number of beneficiaries applying for")),
					"Number of TVET students requiring Work Integrated Learning to complete their work integrated learning placements (WIL)");

			subAnnexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"Please supply the list of possible fields of study for this WIL",
					List.of(ColumnInfo.getColPositiveNumber("Field of Study:"),
							ColumnInfo.getColPositiveNumber("Number of learners applied for:")));
			annexure.setSubAnnexure(subAnnexure);

			annexureInfos.add(annexure);

			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE H (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColPositiveNumber("Total Number of beneficiaries applying for")),
					"TVET Lecturers to be awarded bursaries to further their studies");

			annexureInfos.add(annexure);

			List<String> twoTitleFirstRow = List.of("Occupational Health and Safety", "Other, specify");
			annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
					"ANNEXURE I (Applicable to TVET Colleges)",
					List.of(ColumnInfo.getColLabel("Name of the Intervention"),
							ColumnInfo.getColTwoValue("Total Number of beneficiaries applying for"),
							ColumnInfo.getColTwoTitle("Programme Applied for")),
					"Lecturers exposed to industry through skills programmes", twoTitleFirstRow);

			annexureInfos.add(annexure);

		} else if (menuContextInfo.getProgramType() == ProgramType.TVET_BURSARS) {
			List<Object> rObjs = MasterUtil.queryLearnerInputInfos(
					menuContextInfo.getProgramMasterData().getZZ_Program_Master_Data_ID(),
					X_ZZ_FormDiscipline.ZZ_DISCIPLINETYPE_Trade);

			tradeInfo = (List<LearnerInputInfo>) rObjs.get(0);
			if (tradeInfo != null) {
				annexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class,
						"TVET UNEMPLOYED BURSARS SUPPORT FUNDING APPLICATION",
						List.of(ColumnInfo.getColLabel("Name of the Intervention"),
								ColumnInfo.getColPositiveNumber("Total Number of beneficiaries applying for")),
						"TVETtvet unemployed bursars support funding application");

				subAnnexure = AnnexureInfo.getAnnexureInfoOneLine(AnnexureInfo.class, null,
						List.of(ColumnInfo.getColList("Field of Study", tradeInfo),
								ColumnInfo.getColPositiveNumber("Number of learners")));
				annexure.setSubAnnexure(subAnnexure);

				annexureInfos.add(annexure);
			}
		}

		addressInfo = new AddressInfo(menuContextInfo.getProgramType(), false, null);
	}

	public AddressInfo getAddressInfo() {
		return addressInfo;
	}

	/**
	 * @return the annexureInfos
	 */
	public List<AnnexureInfo> getAnnexureInfos() {
		return annexureInfos;
	}

	public MenuContextInfo getMenuContextInfo() {
		return menuContextInfo;
	}

	public List<LearnerInputInfo> getTradeInfo() {
		return tradeInfo;
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}

	/**
	 * @param annexureInfos the annexureInfos to set
	 */
	public void setAnnexureInfos(List<AnnexureInfo> annexureInfos) {
		this.annexureInfos = annexureInfos;
	}

	public void setMenuContextInfo(MenuContextInfo menuContextInfo) {
		this.menuContextInfo = menuContextInfo;
	}

	public void setTradeInfo(List<LearnerInputInfo> tradeInfo) {
		this.tradeInfo = tradeInfo;
	}

	@Override
	public void saveForm(X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated method stub
		
	}
}
