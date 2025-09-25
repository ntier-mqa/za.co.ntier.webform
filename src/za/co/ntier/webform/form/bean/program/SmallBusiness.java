package za.co.ntier.webform.form.bean.program;

import java.util.List;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;

import za.co.ntier.api.model.I_ZZDetailSmallBusinesse;
import za.co.ntier.api.model.X_ZZDetailSmallBusinesse;
import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.bean.component.AnnexureInfo;
import za.co.ntier.webform.form.bean.component.ColumnInfo;

public class SmallBusiness extends AbstractProgram{
	private AnnexureInfo detailSmallBusiness;
	

	public SmallBusiness(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		ColumnInfo<?> numOfTrainees = ColumnInfo.getColPositiveNumber("Number of trainees", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZNoTrainees);
		numOfTrainees.setCalTotal(true);
		List<ColumnInfo<?>> cols = List.of(
				ColumnInfo.getColText("Name of business/cooperative/NPO/NGO/CBO to be supported", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZBusinessesName)
				, ColumnInfo.getColText("Name of programme and level", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZProgrammeName)
				, ColumnInfo.getColPositiveNumber("Number of credits offered", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZNoCreditOffered)
				, ColumnInfo.getColPositiveNumber("Duration of training (Days)", I_ZZDetailSmallBusinesse.COLUMNNAME_ZZNoTrainingDay)
				, numOfTrainees);
		
		
		detailSmallBusiness = AnnexureInfo.getAnnexureInfo(AnnexureInfo.class, cols, true);
		detailSmallBusiness.setSubSectionHeader("LIST OF BUSINESSES/COOPERATIVES/NPO/NGO/CBO TO BE SUPPORTED AND TRAINING PROGRAMMES");
		detailSmallBusiness.setTableTitle("All the below fields are compulsory. Please provide details of small businesses and or cooperatives to be trained. A maximum amount of R20 000 is payable per entity for training support. Further details of the approach and rationale for training must be submitted in a separate proposal submitted with this form");
		detailSmallBusiness.setShowAddButton(true);
		detailSmallBusiness.setPoSupplier((annexure, appForm) -> {
						X_ZZDetailSmallBusinesse po= new X_ZZDetailSmallBusinesse(appForm.getCtx(), 0, appForm.get_TrxName());
						po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
						return po;
					});
		
		List<PO> savedDaos = null;
		if(getApplicationForm() != null) {
			String where = String.format("%s = ?", 
					I_ZZDetailSmallBusinesse.COLUMNNAME_ZZ_Application_Form_ID);
			
			Query querySavedDaos = MTable.get(I_ZZDetailSmallBusinesse.Table_ID).createQuery(where, null);
			savedDaos = querySavedDaos.setParameters(getApplicationForm().getZZ_Application_Form_ID()).list();
		}
		detailSmallBusiness.init(applicationForm, savedDaos);
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
