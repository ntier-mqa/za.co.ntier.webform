package za.co.ntier.webform.form.bean.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;

import za.co.ntier.webform.form.MasterUtil;
import za.co.ntier.webform.model.I_ZZLearnersApplied;
import za.co.ntier.webform.model.I_ZZ_Application_Form;
import za.co.ntier.webform.model.X_ZZLearnersApplied;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class ProjectInput extends AnnexureInfo {
	
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos) {
		return ProjectInput.getProject(initColumnInfos, null);
	}
	
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, String secctionTitle){
		return getProject(initColumnInfos, secctionTitle, true);
	}
	/**
	 * no row title, no total, has section title
	 * 
	 * @param secctionTitle
	 * @param initColumnInfos
	 * @return
	 */
	public static ProjectInput getProject(List<ColumnInfo<?>> initColumnInfos, String secctionTitle, boolean addAreaCol){
		List<ColumnInfo<?>> columnInfos = new ArrayList<>(initColumnInfos);
		if(addAreaCol) {
			columnInfos.add(ColumnInfo.getColPostal(ColumnInfo.colPostalCodeLabel, I_ZZLearnersApplied.COLUMNNAME_Postal));
			columnInfos.add(
					ColumnInfo.getColArea(ColumnInfo.colAreaLabel, MasterUtil.getInitCities(), I_ZZLearnersApplied.COLUMNNAME_C_City_ID));	
		}
				
		ProjectInput projectInput = AnnexureInfo.getAnnexureInfo(ProjectInput.class, columnInfos, false);
		
		BiFunction<AnnexureInfo, X_ZZ_Application_Form, PO> poSupplier = (annexure, appForm) -> {
			X_ZZLearnersApplied po = new X_ZZLearnersApplied(Env.getCtx(), 0, appForm.get_TrxName());
			po.setDataType(annexure.getDataType());
			po.setZZ_Application_Form_ID(appForm.getZZ_Application_Form_ID());
			return po;
		};
		projectInput.setPoSupplier(poSupplier);
		
		projectInput.setSectionHeader(secctionTitle);
		
		return projectInput;		

	}
	
	public void initProject(X_ZZ_Application_Form applicationForm) {
		List<PO> learnersApplieds = queryLearnersApplied(applicationForm);
		init(applicationForm, learnersApplieds, null);
	}
	
	public void initProject(X_ZZ_Application_Form applicationForm, List<Map<ColumnInfo<?>, Object>> rowTitles) {
		List<PO> learnersApplieds = queryLearnersApplied(applicationForm);
		init(applicationForm, learnersApplieds, rowTitles);

	}
	
	public List<PO> queryLearnersApplied(X_ZZ_Application_Form applicationForm) {
		List<PO> learnersApplieds = null;
		// get saved data
		if (applicationForm != null) {
			String whereLearnersApplied = null;
			if(getDataType() == null) {
				whereLearnersApplied = String.format("%s = ?", I_ZZ_Application_Form.COLUMNNAME_ZZ_Application_Form_ID);
			}else {
				whereLearnersApplied = String.format("%s = ? AND %s = ?", 
						I_ZZLearnersApplied.COLUMNNAME_ZZ_Application_Form_ID,
						I_ZZLearnersApplied.COLUMNNAME_DataType);
			}

			Query queryLearnersApplied = MTable.get(I_ZZLearnersApplied.Table_ID).createQuery(whereLearnersApplied, null);
			if(getDataType() == null) {
				queryLearnersApplied.setParameters(applicationForm.getZZ_Application_Form_ID());
			}else {
				queryLearnersApplied.setParameters(applicationForm.getZZ_Application_Form_ID(), getDataType());
			}
			
			learnersApplieds = queryLearnersApplied.list();
		}
		
		return learnersApplieds;
	}
	
}
