package za.co.ntier.webform.form.bean;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class DisciplineHDSA {
	public final static List<String> colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code",
			"Site Province", "Site Rural/Urban", "Attach WPA", "Attach Accreditation");
	public final static List<Integer> colSizes = List.of(3, 1, 1, 2, 1, 2, 2);

	/**
	 * @return the colheaders
	 */
	public static List<String> getColheaders() {
		return colHeaders;
	}

	/**
	 * @return the colsizes
	 */
	public static List<Integer> getColsizes() {
		return colSizes;
	}

	private String discipline;
	private String fileNameWPA;
	private String uploadWPATitle = "Upload WPA";
	private String uploadAccreditationTitle = "Upload Accred./SLA";
	private String fileNameAccreditation;
	private Integer noOfLearners;
	private String sitePostalCode;
	private String siteProvince;
	private String siteRuralUrban;

	public DisciplineHDSA(String discipline, Integer noOfLearners, String sitePostalCode) {
		this.discipline = discipline;
		this.noOfLearners = noOfLearners;
		setSitePostalCode(sitePostalCode);
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
	 * @return the sitePostalCode
	 */
	public String getSitePostalCode() {
		return sitePostalCode;
	}

	/**
	 * @return the siteProvince
	 */
	public String getSiteProvince() {
		return siteProvince;
	}

	/**
	 * @return the siteRuralUrban
	 */
	public String getSiteRuralUrban() {
		return siteRuralUrban;
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
	 * @param sitePostalCode the sitePostalCode to set
	 */
	@NotifyChange({ "siteProvince", "siteRuralUrban" })
	public void setSitePostalCode(String sitePostalCode) {
		this.sitePostalCode = sitePostalCode;
		if (StringUtils.isNotBlank(sitePostalCode)) {
			// Perform postal code lookup to set siteProvince and siteRuralUrban
			// This is a placeholder for the actual implementation
			this.siteProvince = Province.getProvincesWithoutTotal().get(new Random().nextInt(9)).getFullName();
			this.siteRuralUrban = MasterUtil.municipalityTypes.get(new Random().nextInt(2)).getName();
		} else {
			this.siteProvince = null;
			this.siteRuralUrban = null;
		}
	}

	/**
	 * @param siteProvince the siteProvince to set
	 */
	public void setSiteProvince(String siteProvince) {
		this.siteProvince = siteProvince;
	}

	/**
	 * @param siteRuralUrban the siteRuralUrban to set
	 */
	public void setSiteRuralUrban(String siteRuralUrban) {
		this.siteRuralUrban = siteRuralUrban;
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
}
