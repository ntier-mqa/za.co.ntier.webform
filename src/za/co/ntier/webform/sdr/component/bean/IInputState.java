package za.co.ntier.webform.sdr.component.bean;

import java.util.List;
import java.util.function.Function;

import org.compiere.util.CLogger;

import za.co.ntier.webform.sdr.component.bean.CellModel.InputCheckResult;

public interface IInputState {
	public static final CLogger log = CLogger.getCLogger(RowModel.class);
	
	public default boolean isIgnore() {
		return false;
	}
	
	public InputCheckResult parseInputState();
	
	/**
	 * not yet implement complete
	 * @param row
	 * @param rowInputCheckResult
	 * @param logInfo
	 */
	public static void parseInputState(IInputState row, InputCheckResult rowInputCheckResult, Function<IInputState, String> logInfo) {
		if(row.isIgnore())
			return;
		
		InputCheckResult cellInputCheckResult = row.parseInputState();
		if (!cellInputCheckResult.getEmpty()) {// has at least once field have value
			rowInputCheckResult.setEmpty(false);
		}
		
		if (!cellInputCheckResult.getFillMandatory()) {
			rowInputCheckResult.setFillMandatory(false);// has at least once field have value
			String sLogInfo = null;
			if (logInfo != null)
				sLogInfo = logInfo.apply(row);
			else
				sLogInfo = row.toString();
			
			log.warning(sLogInfo);
		}
		
		if (!cellInputCheckResult.getNotChange()) {
			rowInputCheckResult.setNotChange(false);// has at least once field has change when compare to default
		}
	}
	/**
	 * not yet implement complete 
	 * @param inputStates
	 * @return
	 */
	public static InputCheckResult batchParseInputState(List<?> inputStates) {
		
		InputCheckResult rowInputCheckResult = new InputCheckResult();
		rowInputCheckResult.setEmpty(true).setFillMandatory(true).setNotChange(true);
		
		for (Object row : inputStates) {
			if(row instanceof IInputState) {
				IInputState.parseInputState((IInputState)row, rowInputCheckResult, null);
			}
			
		}
				
		return rowInputCheckResult;
	}
}
