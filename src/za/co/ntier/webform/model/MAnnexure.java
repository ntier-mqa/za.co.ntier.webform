package za.co.ntier.webform.model;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.compiere.model.MColumn;
import org.compiere.model.MRefList;
import org.compiere.util.Env;
import org.zkoss.bind.BindUtils;

import za.co.ntier.webform.form.bean.ProgramCetTvetInfo;
import za.co.ntier.webform.form.bean.SubAnnexure;

public class MAnnexure extends X_ZZAnnexure {

	public MAnnexure(Properties ctx, int ZZAnnexure_ID, String trxName) {
		super(ctx, ZZAnnexure_ID, trxName);
	}

	private static final long serialVersionUID = -4916660585335630255L;
	
	private List<Entry<MAnnexure, MRefList>> masterColumns;
	private List<Entry<MAnnexure, MRefList>> subColumns;
	private List<SubAnnexure> subAnnexures = new ArrayList<>();
	
	public void setMasterColumns(List<Entry<MAnnexure, MRefList>> masterColumns) {
		this.masterColumns = masterColumns;
	}

	public void setSubColumns(List<Entry<MAnnexure, MRefList>> subColumns) {
		this.subColumns = subColumns;
	}

	public List<Entry<MAnnexure, MRefList>> getMasterColumns() {
		if (masterColumns == null) {
			masterColumns = new ArrayList<>();
			
			if (getTitle() != null) {
				MRefList refList = new MRefList(getCtx(), 0, null);
				refList.setName(getTitle());
				refList.set_Attribute(ProgramCetTvetInfo.COL_NAME_titleHeaderText, get_Attribute(ProgramCetTvetInfo.COL_NAME_titleHeaderText));
				masterColumns.add(new AbstractMap.SimpleEntry<>(this, refList));
			}
			
			if (getZZFirst() != null) {
				masterColumns.add(getMRefList(COLUMNNAME_ZZFirst, getZZFirst()));
			}
			
			if (getZZSecond() != null) {
				masterColumns.add(getMRefList(COLUMNNAME_ZZSecond, getZZSecond()));
			}
			
			if (getZZThird() != null) {
				masterColumns.add(getMRefList(COLUMNNAME_ZZThird, getZZThird()));
			}
			
			if (getZZFourth() != null) {
				masterColumns.add(getMRefList(COLUMNNAME_ZZFourth, getZZFourth()));
			}
		}
		return masterColumns;
	}
	
	public String masterColNumber() {
		return String.valueOf(12/masterColumns.size());
	}
	
	public String subColNumber() {
		return String.valueOf(12/subColumns.size());
	}
	
	protected Entry<MAnnexure, MRefList>  getMRefList(String columnName, String refListValue) {
		MColumn column = MColumn.get(p_info.getAD_Column_ID(columnName));
		int referenceID = column.getAD_Reference_Value_ID();
		MRefList refList = MRefList.get(Env.getCtx(), referenceID, refListValue, null);
		return new AbstractMap.SimpleEntry<>(this, refList);
	}
	
	public List<Entry<MAnnexure, MRefList>> getSubColumns() {
		if (subColumns == null) {
			subColumns = new ArrayList<>();
			
			if (getZZFirstSubcolumn() != null) {
				subColumns.add(getMRefList(COLUMNNAME_ZZFirstSubcolumn, getZZFirstSubcolumn()));
			}
			
			if (getZZSecondSubcolumn() != null) {
				subColumns.add(getMRefList(COLUMNNAME_ZZSecondSubcolumn, getZZSecondSubcolumn()));
			}
			
			if (getZZThirdSubcolumn() != null) {
				subColumns.add(getMRefList(COLUMNNAME_ZZThirdSubcolumn, getZZThirdSubcolumn()));
			}
			
			if (getZZFourthSubcolumn() != null) {
				subColumns.add(getMRefList(COLUMNNAME_ZZFourth, getZZFourthSubcolumn()));
			}
			
			if (subColumns.size() > 0)
				subAnnexures.add(new SubAnnexure());
		}

		return subColumns;
	}
	//AD_Ref_List
	//ZZSub-Annex AD_Reference_UU=fe97c4e6-f33e-49c9-a4c4-0096b53e5c6a
	 //ZZAnnexure AD_Reference_UU=14c1459a-6899-4ce1-9dec-b4f51e664edc

	public List<SubAnnexure> getSubAnnexures() {
		return subAnnexures;
	}

	public void setSubAnnexures(List<SubAnnexure> subAnnexures) {
		this.subAnnexures = subAnnexures;
	}
	
	public void addSubAnnexure() {
		subAnnexures.add(new SubAnnexure());
		BindUtils.postNotifyChange(this, "subAnnexures");
	}
	
}
