package za.co.ntier.webform.process;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.compiere.process.SvrProcess;

import za.co.ntier.api.model.I_ZZSdfOrganisation;
import za.co.ntier.api.model.X_ZZSdfOrganisation;
import za.co.ntier.api.util.NtierProcessUtil;

@org.adempiere.base.annotation.Process(name="za.co.ntier.webform.process.SdfUnlinkOrg")
public class SdfUnlinkOrg extends SvrProcess{

	@Override
	protected void prepare() {
		// do nothing
		
	}

	@Override
	protected String doIt() throws Exception {
		Map<String, Map<Object, Object>> selectedRecordsMap = NtierProcessUtil.getSelectedRecordsFromTempTable(get_TrxName(), getAD_PInstance_ID());
		for (Map.Entry<String, Map<Object, Object>> selectedRecordInfoEntry : selectedRecordsMap.entrySet()) {
			
			BigDecimal sdfOrganisationID = (BigDecimal)selectedRecordInfoEntry.getValue().get(I_ZZSdfOrganisation.COLUMNNAME_ZZSdfOrganisation_ID);
			
			X_ZZSdfOrganisation sdfOrganisation = new X_ZZSdfOrganisation(getCtx(), sdfOrganisationID.intValueExact(), get_TrxName());
			sdfOrganisation.setZZ_DocStatus(X_ZZSdfOrganisation.ZZ_DOCSTATUS_Delinked);
			sdfOrganisation.saveEx();
			
		}
		
		return null;
	}

}
