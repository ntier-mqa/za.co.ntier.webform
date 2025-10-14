package za.co.ntier.webform.form.bean.program;

import java.util.List;
import java.util.Map;

import za.co.ntier.api.model.I_ZZLearnersApplied;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.AreaData;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.webform.form.bean.component.IntData;
import za.co.ntier.webform.form.bean.component.PostalData;
import za.co.ntier.webform.form.bean.component.ProjectInput;

public class ArtisanAidesProgram extends AbstractProgram {
	private ProjectInput qualification;
	private ProjectInput skill;

	public ArtisanAidesProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		ColumnInfo<?> colNoEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoEmployedLearners);
		colNoEmployed.setCalTotal(true);
		ColumnInfo<?> colNoUnEmployed = ColumnInfo.getColPositiveNumber(ColumnInfo.colNoUnEmployedLabel, I_ZZLearnersApplied.COLUMNNAME_ZZNoUnEmployedLearners);
		colNoUnEmployed.setCalTotal(true);
		ColumnInfo<?> colTotal = ColumnInfo.getColExpression(ColumnInfo.colTotalLearnersLabel, row -> {
			return Util.sumCol(row, List.of(colNoEmployed, colNoUnEmployed));
		});
		
		
		qualification = ProjectInput.getProject(
				List.of(colNoEmployed,
						colNoUnEmployed,
						colTotal));
		qualification.setSubSectionHeader("QUALIFICATION");
		qualification.setDataType("QUALIFICATION");
		qualification.initProject(applicationForm);
		
		skill = ProjectInput.getProject(
				List.of(colNoEmployed,
						colNoUnEmployed,
						colTotal));
		skill.setSubSectionHeader("SKILLS PROGRAMME");
		skill.setDataType("SKILLS PROGRAMME");
		skill.initProject(applicationForm);
	}

	/**
	 * @return the qualification
	 */
	public ProjectInput getQualification() {
		return qualification;
	}

	/**
	 * @return the skill
	 */
	public ProjectInput getSkill() {
		return skill;
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm)  {
		super.saveForm(applicationForm);
		qualification.save(trxName, applicationForm);
		skill.save(trxName, applicationForm);
	}

	/**
	 * @param qualification the qualification to set
	 */
	public void setQualification(ProjectInput qualification) {
		this.qualification = qualification;
	}

	/**
	 * @param skill the skill to set
	 */
	public void setSkill(ProjectInput skill) {
		this.skill = skill;
	}

	@Override
	public boolean isProgramValid() {
	    // valid if either table has at least one valid line
	    return hasAtLeastOneValidRow(qualification) || hasAtLeastOneValidRow(skill);
	}

	private static boolean hasAtLeastOneValidRow(AnnexureInfo table) {
	    if (table == null || table.getRows() == null) return false;

	    // try all possible learner-count columns the table might use
	    ColumnInfo<?> cNoLearners   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoLearnersLabel,   table);
	    ColumnInfo<?> cNoEmployed   = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoEmployedLabel,   table);
	    ColumnInfo<?> cNoUnemployed = AnnexureInfo.lookupColByTitle(ColumnInfo.colNoUnEmployedLabel, table);

	    ColumnInfo<?> cPostal = AnnexureInfo.lookupColByTitle(ColumnInfo.colPostalCodeLabel, table);
	    ColumnInfo<?> cArea   = AnnexureInfo.lookupColByTitle(ColumnInfo.colAreaLabel,       table);

	    for (Map<ColumnInfo<?>, Object> row : table.getRows()) {
	        int n = 0;
	        if (cNoLearners   != null && row.get(cNoLearners)   instanceof IntData) n += safeInt((IntData) row.get(cNoLearners));
	        if (cNoEmployed   != null && row.get(cNoEmployed)   instanceof IntData) n += safeInt((IntData) row.get(cNoEmployed));
	        if (cNoUnemployed != null && row.get(cNoUnemployed) instanceof IntData) n += safeInt((IntData) row.get(cNoUnemployed));

	        boolean postalOk = (cPostal != null && row.get(cPostal) instanceof PostalData)
	                && notEmpty(((PostalData) row.get(cPostal)).getPostal());

	        boolean areaOk = (cArea != null && row.get(cArea) instanceof AreaData)
	                && ((AreaData) row.get(cArea)).isSelected();

	        if (n > 0 && postalOk && areaOk) return true;
	    }
	    return false;
	}

	private static int safeInt(IntData d) {
	    return (d.getValue() == null) ? 0 : d.getValue();
	}
	private static boolean notEmpty(String s) {
	    return s != null && !s.trim().isEmpty();
	}

}
