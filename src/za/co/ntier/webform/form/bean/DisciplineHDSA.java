package za.co.ntier.webform.form.bean;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.compiere.model.MCity;
import org.compiere.model.MRegion;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class DisciplineHDSA {	
	
	private String discipline;
	private String fileNameWPA;
	private String uploadWPATitle = "Upload WPA";
	private boolean isUploadWPA = false;
	private boolean isUploadAccreditation = false;
	private String uploadAccreditationTitle = "Upload Accred./SLA";
	private String fileNameAccreditation;
	private Integer noOfLearners;
	private String postalCode;
	private MRegion province;
	private Collection<MCity> areas;
	private MCity areaSelected;
	
	public DisciplineHDSA(String discipline, boolean isUploadAccreditation, boolean isUploadWPA) {
		setDiscipline(discipline);
		setUploadAccreditation(isUploadAccreditation);
		setUploadWPA(isUploadWPA);
		setAreas(MasterUtil.getCities().stream().limit(MasterUtil.limitItem).toList());
	}

	/**
	 * @return the discipline
	 */
	public String getDiscipline() {
		return discipline;
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
	public String getFileNameAccreditation() {
		return fileNameAccreditation;
	}

	/**
	 * @return the noOfLearners
	 */
	public Integer getNoOfLearners() {
		return noOfLearners;
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
	 * @param discipline the discipline to set
	 */
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}

	/**
	 * @param fileNameWPA the fileNameWPA to set
	 */

	public void setFileNameWPA(String fileNameWPA) {
		this.fileNameWPA = fileNameWPA;

		BindUtils.postNotifyChange(this, "fileNameWPA");
	}
	
	public void setFileNameAccreditation(String fileNameAccreditation) {
		this.fileNameAccreditation = fileNameAccreditation;

		BindUtils.postNotifyChange(this, "fileNameAccreditation");
	}

	/**
	 * @param noOfLearners the noOfLearners to set
	 */
	public void setNoOfLearners(Integer noOfLearners) {
		this.noOfLearners = noOfLearners;
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
			setFileNameAccreditation(media.getName());
		}
	}

	/**
	 * @return the uploadWPATitle
	 */
	public String getUploadWPATitle() {
		return uploadWPATitle;
	}

	/**
	 * @param uploadWPATitle the uploadWPATitle to set
	 */
	public void setUploadWPATitle(String uploadWPATitle) {
		this.uploadWPATitle = uploadWPATitle;
	}

	/**
	 * @return the uploadAccreditationTitle
	 */
	public String getUploadAccreditationTitle() {
		return uploadAccreditationTitle;
	}

	/**
	 * @param uploadAccreditationTitle the uploadAccreditationTitle to set
	 */
	public void setUploadAccreditationTitle(String uploadAccreditationTitle) {
		this.uploadAccreditationTitle = uploadAccreditationTitle;
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
	 * @return the isUploadAccreditation
	 */
	public boolean isUploadAccreditation() {
		return isUploadAccreditation;
	}

	/**
	 * @param isUploadAccreditation the isUploadAccreditation to set
	 */
	public void setUploadAccreditation(boolean isUploadAccreditation) {
		this.isUploadAccreditation = isUploadAccreditation;
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
}
