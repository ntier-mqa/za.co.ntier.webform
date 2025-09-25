package za.co.ntier.webform.form.bean.program;

import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;

import za.co.ntier.api.model.I_ZZLearningMaterial;
import za.co.ntier.api.model.X_ZZLearningMaterial;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public class LearningMaterialsDevelopment extends AbstractProgram{
	private AnnexureInfo learningMaterials;
	
	public LearningMaterialsDevelopment(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		
		List<ColumnInfo<?>> cols = List.of(
				ColumnInfo.getColText("QUALIFICATION/SKILLS PROGRAMME APPLIED FOR", I_ZZLearningMaterial.COLUMNNAME_ZZQualificationProgramme)
				, ColumnInfo.getColText("NAME OF WRITER (ABRIDGED CV OF WRITER MUST BE ATTACHED)", I_ZZLearningMaterial.COLUMNNAME_ZZNameWriter)
				, ColumnInfo.getColText("CELL NUMBER OF WRITER", I_ZZLearningMaterial.COLUMNNAME_ZZCellNoWriter)
				, ColumnInfo.getColText("EMAIL ADDRESS OF WRITER", I_ZZLearningMaterial.COLUMNNAME_ZZEmailWriter)
				, ColumnInfo.getColText("ACCREDITED TRAINING PROVIDER", I_ZZLearningMaterial.COLUMNNAME_ZZAccreditedTrainingProvider)
				, ColumnInfo.getColFileUpload("Unabridged CV", "Unabridged CV", I_ZZLearningMaterial.COLUMNNAME_ZZUnabridgedCVFile, I_ZZLearningMaterial.COLUMNNAME_ZZUnabridgedCVFileName)
				);
		
		learningMaterials = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, cols, false);
		learningMaterials.setSubSectionHeader("Learning Material Development");
		learningMaterials.setShowAddButton(true);
		learningMaterials.setPoSupplier((annexure, appForm) -> {
						X_ZZLearningMaterial po= new X_ZZLearningMaterial(appForm.getCtx(), 0, appForm.get_TrxName());
						po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
						return po;
					});
		
		List<PO> savedDaos = null;
		if(getApplicationForm() != null) {
			String where = String.format("%s = ?", 
					I_ZZLearningMaterial.COLUMNNAME_ZZ_Application_Form_ID);
			
			Query querySavedDaos = MTable.get(I_ZZLearningMaterial.Table_ID).createQuery(where, null);
			savedDaos = querySavedDaos.setParameters(getApplicationForm().getZZ_Application_Form_ID()).list();
		}
		
		learningMaterials.init(applicationForm, savedDaos);
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		learningMaterials.save(trxName, applicationForm);
	}

	/**
	 * @return the learningMaterials
	 */
	public AnnexureInfo getLearningMaterials() {
		return learningMaterials;
	}

	/**
	 * @param learningMaterials the learningMaterials to set
	 */
	public void setLearningMaterials(AnnexureInfo learningMaterials) {
		this.learningMaterials = learningMaterials;
	}

}
