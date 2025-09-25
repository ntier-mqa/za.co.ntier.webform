package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;
import za.co.ntier.api.model.I_ZZDetailSmallBusinesse;
import za.co.ntier.api.model.X_ZZDetailSmallBusinesse;
import za.co.ntier.api.model.X_ZZ_Application_Form;

public class SmallBusiness extends AbstractProgram{
	private AnnexureInfo detailSmallBusiness;
	

	public SmallBusiness(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		List<ColumnInfo<?>> cols = List.of(
				ColumnInfo.getColText("Name of business/cooperative/NPO/NGO/CBO to be supported", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZBusinessesName)
				, ColumnInfo.getColText("Name of programme and level", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZProgrammeName)
				, ColumnInfo.getColText("Number of credits offered", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZNoCreditOffered)
				, ColumnInfo.getColText("Duration of training in day", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZNoTrainingDay)
				, ColumnInfo.getColText("Number of trainees", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZNoTrainees));
		
		detailSmallBusiness = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, cols, false);
		detailSmallBusiness.setSubSectionHeader("LIST OF BUSINESSES/COOPERATIVES/NPO/NGO/CBO TO BE SUPPORTED AND TRAINING PROGRAMMES");
		detailSmallBusiness.setTableTitle("All the below fields are compulsory. Please provide details of small businesses and or cooperatives to be trained. A maximum amount of R20 000 is payable per entity for training support. Further details of the approach and rationale for training must be submitted in a separate proposal submitted with this form. (The list of businesses/cooperatives to be trained can also be submitted together with the proposal in the event the provided space is insufficient.)");
		detailSmallBusiness.setShowAddButton(true);
		detailSmallBusiness.setPoSupplier((annexure, appForm) -> {
						X_ZZDetailSmallBusinesse po= new X_ZZDetailSmallBusinesse(appForm.getCtx(), 0, appForm.get_TrxName());
						po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
						return po;
					});
		
		detailSmallBusiness.init(applicationForm);
	}

	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		detailSmallBusiness.save(trxName, applicationForm);
		
	}
	
	public AnnexureInfo getDetailSmallBusiness() {
		return detailSmallBusiness;
	}

	public void setDetailSmallBusiness(AnnexureInfo detailSmallBusiness) {
		this.detailSmallBusiness = detailSmallBusiness;
	}
	
	@Override
	public boolean isProgramValid() {
	    // Columns in a row that must all be filled
	    List<ColumnInfo<?>> cols = detailSmallBusiness.getColumnInfos();

	    boolean hasCompleteRow = false;

	    for (var row : detailSmallBusiness.getRows()) {
	        int filled = 0;
	        for (var c : cols) {
	            Object v = row.get(c);
	            if (!isBlank(v)) filled++;
	        }
	        if (filled > 0 && filled < cols.size()) {
	            // Partial row: something typed but not all fields completed
	            return false;
	        }
	        if (filled == cols.size()) {
	            hasCompleteRow = true;
	        }
	    }
	    return hasCompleteRow;
	}

	private static boolean isBlank(Object v) {
	    if (v == null) return true;
	    String s = v.toString().trim();
	    return s.isEmpty();
	}


}
