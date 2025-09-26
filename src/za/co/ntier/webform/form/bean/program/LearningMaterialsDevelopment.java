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
	
	 // Keep column refs so we can read row values reliably
    private ColumnInfo<?> colQualification;
    private ColumnInfo<?> colWriterName;
    private ColumnInfo<?> colWriterCell;
    private ColumnInfo<?> colWriterEmail;
    private ColumnInfo<?> colProvider;
    private ColumnInfo<?> colCV;
	
	public LearningMaterialsDevelopment(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		colQualification = ColumnInfo.getColText("QUALIFICATION/SKILLS PROGRAMME APPLIED FOR", I_ZZLearningMaterial.COLUMNNAME_ZZQualificationProgramme);
        colWriterName   = ColumnInfo.getColText("NAME OF WRITER (ABRIDGED CV OF WRITER MUST BE ATTACHED)", I_ZZLearningMaterial.COLUMNNAME_ZZNameWriter);
        colWriterCell   = ColumnInfo.getColText("CELL NUMBER OF WRITER", I_ZZLearningMaterial.COLUMNNAME_ZZCellNoWriter);
        colWriterEmail  = ColumnInfo.getColText("EMAIL ADDRESS OF WRITER", I_ZZLearningMaterial.COLUMNNAME_ZZEmailWriter);
        colProvider     = ColumnInfo.getColText("ACCREDITED TRAINING PROVIDER", I_ZZLearningMaterial.COLUMNNAME_ZZAccreditedTrainingProvider);
        //colCV           = ColumnInfo.getColFileUpload("Unabridged CV", "CV", I_ZZLearningMaterial.COLUMNNAME_ZZUnabridgedCVFile, I_ZZLearningMaterial.COLUMNNAME_ZZUnabridgedCVFileName);
        colCV = ColumnInfo.getColFileUpload("Unabridged CV", "CV");  // To load to attachments instead

		
        List<ColumnInfo<?>> cols = List.of(
                colQualification, colWriterName, colWriterCell, colWriterEmail, colProvider,colCV
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
		
		// Show fileNames in the grid when editing apps
		if (savedDaos != null) {
		    for (int i = 0; i < savedDaos.size() && i < learningMaterials.getRows().size(); i++) {
		        PO dao = savedDaos.get(i); // X_ZZLearningMaterial
		        org.compiere.model.MAttachment att =
		            org.compiere.model.MAttachment.get(applicationForm.getCtx(), dao.get_Table_ID(), dao.get_ID());
		        if (att != null && att.getEntries() != null && att.getEntries().length > 0) {
		            String name = att.getEntries()[0].getName();
		            za.co.ntier.webform.form.bean.component.UploadData u =
		                (za.co.ntier.webform.form.bean.component.UploadData) learningMaterials.getRows().get(i).get(colCV);
		            if (u != null) u.setFileName(name);
		        }
		    }
		}

	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		learningMaterials.save(trxName, applicationForm);
		applicationForm.setZZTotalNumberApplied(learningMaterials.getRows().size());
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
	
	@Override
	public boolean isProgramValid() {
	    if (learningMaterials == null || learningMaterials.getRows() == null) return false;

	    for (var row : learningMaterials.getRows()) {
	        String q    = s(row.get(colQualification));
	        String name = s(row.get(colWriterName));
	        String cell = s(row.get(colWriterCell));
	        String mail = s(row.get(colWriterEmail));
	        String prov = s(row.get(colProvider));

	        boolean complete = notEmpty(q)
	                        && notEmpty(name)
	                        && isTenDigits(cell)
	                        && isEmail(mail)
	                        && notEmpty(prov);

	        if (complete) return true; // 1 complete row unlocks Next
	    }
	    return false;
	}


	private static String s(Object o){
	    if (o == null) return null;
	    String t = String.valueOf(o).trim();
	    return t.isEmpty() ? null : t;
	}
	private static boolean notEmpty(String v){ return v != null && !v.isBlank(); }
	private static boolean isTenDigits(String s){ return s != null && s.matches("^\\s*\\d{10}\\s*$"); }
	private static boolean isEmail(String s){ return s != null && s.matches("^[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}$"); }


}
