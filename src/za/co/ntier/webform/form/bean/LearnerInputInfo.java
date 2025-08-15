package za.co.ntier.webform.form.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class LearnerInputInfo {	
	private int learnerInputProgramID;
	private int learnerInputID;
	private String learnerInputText;
	
	private String fileNameWPA;
	private boolean isUploadWPA = false;
	private boolean isUploadAccred = false;
	private String fileNameAccred;
	private String postalCode;
	private MRegion province;
	private Collection<MCity> areas;
	private MCity areaSelected;
	
	private Integer noLearners = null;
	private Integer noEmployed = null;
	private Integer noUnEmployed = null;
	private Integer noTotalApply = null;
	
	public LearnerInputInfo() {
		
	}
	
	public LearnerInputInfo(List<Object> args) {
		
		this.learnerInputProgramID = ((BigDecimal)args.get(0)).intValueExact();
		this.learnerInputID = ((BigDecimal)args.get(1)).intValueExact();
		this.isUploadWPA = args.get(2) != null && "Y".equalsIgnoreCase((String)args.get(2));
		this.isUploadAccred = args.get(3) != null && "Y".equalsIgnoreCase((String)args.get(3));
		this.learnerInputText = (String)args.get(4);
		
		setAreas(MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList());
	}


	/**
	 * @return the fileNameWPA
	 */
	public String getFileNameWPA() {
		return fileNameWPA;
	}

	/**
	 * @return the fileNameHDSA
	 */
	public String getFileNameAccred() {
		return fileNameAccred;
	}

	/**
	 * @return the noLearners
	 */
	public Integer getNoLearners() {
		return noLearners;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @return the province
	 */
	public MRegion getProvince() {
		return province;
	}


	/**
	 * @param fileNameWPA the fileNameWPA to set
	 */

	public void setFileNameWPA(String fileNameWPA) {
		this.fileNameWPA = fileNameWPA;

		BindUtils.postNotifyChange(this, "fileNameWPA");
	}
	
	public void setFileNameAccred(String fileNameAccred) {
		this.fileNameAccred = fileNameAccred;

		BindUtils.postNotifyChange(this, "fileNameAccred");
	}

	/**
	 * @param noLearners the noLearners to set
	 */
	public void setNoLearners(Integer noLearners) {
		this.noLearners = noLearners;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	@NotifyChange({ "province", "areas" })
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		
		Collection<MCity> areaFilters = new ArrayList<>();
		
		if (StringUtils.isNotBlank(postalCode)) {
			MasterUtil.getCities().stream()
				.filter(city -> city.getPostal() != null && postalCode.equalsIgnoreCase(city.getPostal()))
				.limit(MasterUtil.limitItem)
				.forEach(city -> {
					areaFilters.add(city);
				});
		}
		
		areaSelected = null;
		if (!areaFilters.isEmpty()) {
			province = MRegion.get(areaFilters.iterator().next().getC_Region_ID());
			if (areaFilters.size() == 1) {
				areaSelected = areaFilters.iterator().next();
			}
		}else {
			MasterUtil.getCities().stream()
			.limit(MasterUtil.limitItem)
			.forEach(city -> {
				areaFilters.add(city);
			});
			
			province = null;
		}
		
		areas = areaFilters;
		
		
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(MRegion province) {
		this.province = province;
	}

	public void uploadFile(Media media, boolean isDSA) {
		if (media != null && isDSA) {
			setFileNameWPA(media.getName());
		} else if (media != null && !isDSA) {
			setFileNameAccred(media.getName());
		}
	}

	/**
	 * @return the uploadWPATitle
	 */
	public String getUploadWPATitle() {
		return " WPA";
	}

	/**
	 * @return the uploadAccreditationTitle
	 */
	public String getUploadAccredTitle() {
		return " Accred./SLA";
	}

	/**
	 * @return the isUploadWPA
	 */
	public boolean isUploadWPA() {
		return isUploadWPA;
	}

	/**
	 * @param isUploadWPA the isUploadWPA to set
	 */
	public void setUploadWPA(boolean isUploadWPA) {
		this.isUploadWPA = isUploadWPA;
	}

	/**
	 * @return the isUploadAccred
	 */
	public boolean isUploadAccred() {
		return isUploadAccred;
	}

	/**
	 * @param isUploadAccred the isUploadAccred to set
	 */
	public void setUploadAccred(boolean isUploadAccred) {
		this.isUploadAccred = isUploadAccred;
	}

	/**
	 * @return the areas
	 */
	public Collection<MCity> getAreas() {
		return areas;
	}

	/**
	 * @param areas the areas to set
	 */
	public void setAreas(Collection<MCity> areas) {
		this.areas = areas;
	}

	/**
	 * @return the areaSelected
	 */
	public MCity getAreaSelected() {
		return areaSelected;
	}

	/**
	 * @param areaSelected the areaSelected to set
	 */
	@NotifyChange({ "province", "postalCode" })
	public void setAreaSelected(MCity areaSelected) {
		this.areaSelected = areaSelected;
		province = MRegion.get(areaSelected.getC_Region_ID());
		postalCode = areaSelected.getPostal();
	}


	/**
	 * @return the learnerInputID
	 */
	public int getLearnerInputID() {
		return learnerInputID;
	}


	/**
	 * @param learnerInputID the learnerInputID to set
	 */
	public void setLearnerInputID(int learnerInputID) {
		this.learnerInputID = learnerInputID;
	}


	/**
	 * @return the learnerInputText
	 */
	public String getLearnerInputText() {
		return learnerInputText;
	}


	/**
	 * @param learnerInputText the learnerInputText to set
	 */
	public void setLearnerInputText(String learnerInputText) {
		this.learnerInputText = learnerInputText;
	}


	/**
	 * @return the learnerInputProgramID
	 */
	public int getLearnerInputProgramID() {
		return learnerInputProgramID;
	}


	/**
	 * @param learnerInputProgramID the learnerInputProgramID to set
	 */
	public void setLearnerInputProgramID(Integer learnerInputProgramID) {
		this.learnerInputProgramID = learnerInputProgramID;
	}


	/**
	 * @return the noEmployed
	 */
	public Integer getNoEmployed() {
		return noEmployed;
	}


	/**
	 * @param noEmployed the noEmployed to set
	 */
	public void setNoEmployed(Integer noEmployed) {
		this.noEmployed = noEmployed;
	}


	/**
	 * @return the noUnEmployed
	 */
	public Integer getNoUnEmployed() {
		return noUnEmployed;
	}


	/**
	 * @param noUnEmployed the noUnEmployed to set
	 */
	public void setNoUnEmployed(Integer noUnEmployed) {
		this.noUnEmployed = noUnEmployed;
	}


	/**
	 * @return the noTotalApply
	 */
	public Integer getNoTotalApply() {
		return noTotalApply;
	}


	/**
	 * @param noTotalApply the noTotalApply to set
	 */
	public void setNoTotalApply(Integer noTotalApply) {
		this.noTotalApply = noTotalApply;
	}
}
