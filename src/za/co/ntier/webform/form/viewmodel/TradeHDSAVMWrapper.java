package za.co.ntier.webform.form.viewmodel;

import java.util.ArrayList;
import java.util.List;

import org.compiere.model.Query;
import org.compiere.util.Env;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.WebForm;
import za.co.ntier.webform.form.bean.TradeHDSA;
import za.co.ntier.webform.model.I_ZZ_Program_Master_Data;
import za.co.ntier.webform.model.I_ZZ_Program_Trade;
import za.co.ntier.webform.model.I_ZZ_Trade;
import za.co.ntier.webform.model.X_ZZ_Program_Trade;

public class TradeHDSAVMWrapper {
	private List<TradeHDSA> tradeHDSAs;
	private boolean hasWPAReq = false;
	private boolean hasAccreditation = false;
	
	private List<String> colHeaders;
	private List<Integer> colSizes;
	
	private int totalLearners = 0;
	/**
	 * @return the tradeHDSAs
	 */
	public List<TradeHDSA> getTradeHDSAs() {
		return tradeHDSAs;
	}

	@Init
	public void init(@ExecutionArgParam(WebForm.programMasterDataIDKey) int programMasterDataID) {
		List<X_ZZ_Program_Trade> programTrades = new Query(Env.getCtx(), I_ZZ_Program_Trade.Table_Name, 
        		String.format("%s = ?", I_ZZ_Program_Master_Data.COLUMNNAME_ZZ_Program_Master_Data_ID), null)
			.setParameters(programMasterDataID)
			.setClient_ID()
			.setOrderBy(I_ZZ_Program_Trade.COLUMNNAME_Line)
			.list();
		List<TradeHDSA> tradeHDSAs = new ArrayList<>();
        
        programTrades.stream().forEach((programTrade) -> {
        	I_ZZ_Trade trade = programTrade.getZZ_Trade();
        	TradeHDSA tradeHDSA = new TradeHDSA(trade.getName(), programTrade.isZZ_WPA_Req(), programTrade.isZZ_Is_Accred_SLA_Req());
        	tradeHDSAs.add(tradeHDSA);
        	
        	if (tradeHDSA.isUploadWPA()) {
				hasWPAReq = true;
			}
        	
        	if (tradeHDSA.isUploadAccreditation()) {
        		hasAccreditation = true;
        	}
        });
        
        setTradeHDSAs(tradeHDSAs);
        
        if (hasWPAReq && hasAccreditation) {
        	colHeaders = List.of("Trade", "No. of Learners", "Site Postal Code",
        			"Area/Suburb", "Site Province", "Attach WPA", "Attach Accreditation");
        	colSizes = List.of(3, 1, 1, 2, 1, 2, 2);
        }else if (hasWPAReq) {
			colHeaders = List.of("Trade", "No. of Learners", "Site Postal Code",
					"Area/Suburb", "Site Province", "Attach WPA");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else if (hasAccreditation) {
			colHeaders = List.of("Trade", "No. of Learners", "Site Postal Code",
					"Area/Suburb", "Site Province", "Attach Accreditation");
			colSizes = List.of(3, 1, 1, 2, 1, 4);
		} else {
			colHeaders = List.of("Trade", "No. of Learners", "Site Postal Code",
					"Area/Suburb", "Site Province");
			colSizes = List.of(3, 2, 2, 3, 2);
		}
        
	}

	/**
	 * @return the hasWPAReq
	 */
	public boolean hasWPAReq() {
		return hasWPAReq;
	}

	/**
	 * @param hasWPAReq the hasWPAReq to set
	 */
	public void setHasWPAReq(boolean hasWPAReq) {
		this.hasWPAReq = hasWPAReq;
	}

	/**
	 * @return the hasAccreditation
	 */
	public boolean hasAccreditation() {
		return hasAccreditation;
	}

	/**
	 * @param hasAccreditation the hasAccreditation to set
	 */
	public void setHasAccreditation(boolean hasAccreditation) {
		this.hasAccreditation = hasAccreditation;
	}
	
	@NotifyChange("totalLearners")
	@Command({ "noOfLearnerChange" })
	public void noOfLearnerChange(@BindingParam("trade") TradeHDSA trade) {
		
		setTotalLearners(tradeHDSAs.stream()
				.filter(t -> t.getNoOfLearners() != null)
				.mapToInt(TradeHDSA::getNoOfLearners)
				.sum());
	}
	
	

	/**
	 * @param tradeHDSAs the tradeHDSAs to set
	 */
	public void setTradeHDSAs(List<TradeHDSA> tradeHDSAs) {
		this.tradeHDSAs = tradeHDSAs;
	}

	@Command
	public void uploadFile(@BindingParam("media") Media media, @BindingParam("isDSA") boolean isDSA,
			@BindingParam("trade") TradeHDSA trade, @BindingParam("index") int index) {
		trade.uploadFile(media, isDSA);
		
	}

	/**
	 * @return the colHeaders
	 */
	public List<String> getColHeaders() {
		return colHeaders;
	}

	/**
	 * @param colHeaders the colHeaders to set
	 */
	public void setColHeaders(List<String> colHeaders) {
		this.colHeaders = colHeaders;
	}

	/**
	 * @return the colSizes
	 */
	public List<Integer> getColSizes() {
		return colSizes;
	}

	/**
	 * @param colSizes the colSizes to set
	 */
	public void setColSizes(List<Integer> colSizes) {
		this.colSizes = colSizes;
	}

	/**
	 * @return the totalLearners
	 */
	public int getTotalLearners() {
		return totalLearners;
	}

	/**
	 * @param totalLearners the totalLearners to set
	 */
	public void setTotalLearners(int totalLearners) {
		this.totalLearners = totalLearners;
	}
}
