package za.co.ntier.webform.sdr.component.bean.column;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.PO;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import za.co.ntier.webform.sdr.component.bean.ColumnModel;
import za.co.ntier.webform.sdr.component.bean.RowModel;

public class PresetTitleColumnModel<T> extends ColumnModel {
	public PresetTitleColumnModel(String colTitle) {
		super(colTitle);
	}
	public PresetTitleColumnModel(String colTitle, String daoPropertyName) {
		super(colTitle, daoPropertyName);
	}

	public Function<T, String> getDisplayConvert() {
		return displayConvert;
	}
	public void setDisplayConvert(Function<T, String> displayConvert) {
		this.displayConvert = displayConvert;
	}

	private Function<T, String> displayConvert;
	
	private BiFunction<RowModel, List<PO>, Boolean> matchingLoaded;
	
	public boolean isMatching(RowModel row, List<PO> savedPo) {
		if (getMatchingLoaded() != null) {
			return getMatchingLoaded().apply(row, savedPo);
		}else {
			throw new AdempiereException(Msg.getMsg(Env.getCtx(), "ZZMissingMatchingDelegate"));
		}
	}
	public BiFunction<RowModel, List<PO>, Boolean> getMatchingLoaded() {
		return matchingLoaded;
	}
	public void setMatchingLoaded(BiFunction<RowModel, List<PO>, Boolean> matchingLoaded) {
		this.matchingLoaded = matchingLoaded;
	}

}
