package za.co.ntier.webform.form.bean;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.compiere.util.DB;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

public class DisciplineHDSA {

	private String discipline;
	private String fileNameWPA;
	private String uploadWPATitle = "Upload WPA";
	private boolean isUploadWPA = false;
	private boolean isUploadAccreditation = false;
	private String uploadAccreditationTitle = "Upload Accred./SLA";
	private String fileNameAccreditation;
	private Integer noOfLearners;
	private String sitePostalCode;
	private String siteProvince;
	private String siteRuralUrban;

	public DisciplineHDSA(String discipline, boolean isUploadAccreditation, boolean isUploadWPA) {
		setDiscipline(discipline);
		setUploadAccreditation(isUploadAccreditation);
		setUploadWPA(isUploadWPA);
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
		List<Object> regionInfos = null;
		
		if (StringUtils.isNotBlank(sitePostalCode)) {
			String sql = """
					SELECT 
						r.name AS regionName, c.name AS cityName 
					FROM 
						C_City c INNER JOIN c_region r ON c.c_region_id = r.c_region_id
					WHERE 
						c.Postal = ?
					""";
			regionInfos = DB.getSQLValueObjectsEx(null, sql, sitePostalCode);
		} 
		
		if (regionInfos != null) {
			this.siteProvince = (String) regionInfos.get(0);
			this.siteRuralUrban = (String) regionInfos.get(1);
		}else{
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
}
