package za.co.ntier.webform.form.bean;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.Media;

import za.co.ntier.webform.form.viewmodel.master.MasterUtil;

public class DisciplineHDSA {
	/**
	 * @return the discipline
	 */
	public String getDiscipline() {
		return discipline;
	}
	/**
	 * @param discipline the discipline to set
	 */
	public void setDiscipline(String discipline) {
		this.discipline = discipline;
	}
	/**
	 * @return the noOfLearners
	 */
	public Integer getNoOfLearners() {
		return noOfLearners;
	}
	/**
	 * @param noOfLearners the noOfLearners to set
	 */
	public void setNoOfLearners(Integer noOfLearners) {
		this.noOfLearners = noOfLearners;
	}
	/**
	 * @return the sitePostalCode
	 */
	public String getSitePostalCode() {
		return sitePostalCode;
	}
	/**
	 * @param sitePostalCode the sitePostalCode to set
	 */
	@NotifyChange({"siteProvince", "siteRuralUrban"})
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
	 * @return the siteProvince
	 */
	public String getSiteProvince() {
		return siteProvince;
	}
	/**
	 * @param siteProvince the siteProvince to set
	 */
	public void setSiteProvince(String siteProvince) {
		this.siteProvince = siteProvince;
	}
	/**
	 * @return the siteRuralUrban
	 */
	public String getSiteRuralUrban() {
		return siteRuralUrban;
	}
	/**
	 * @param siteRuralUrban the siteRuralUrban to set
	 */
	public void setSiteRuralUrban(String siteRuralUrban) {
		this.siteRuralUrban = siteRuralUrban;
	}
	/**
	 * @return the attachWPA
	 */
	public Boolean getAttachWPA() {
		return attachWPA;
	}
	/**
	 * @param attachWPA the attachWPA to set
	 */
	public void setAttachWPA(Boolean attachWPA) {
		this.attachWPA = attachWPA;
	}
	/**
	 * @return the attachAccreditation
	 */
	public Boolean getAttachAccreditation() {
		return attachAccreditation;
	}
	/**
	 * @param attachAccreditation the attachAccreditation to set
	 */
	public void setAttachAccreditation(Boolean attachAccreditation) {
		this.attachAccreditation = attachAccreditation;
	}
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
	public final static List<String> colHeaders = List.of("Descipline", "No. of Learners", "Site Postal Code", "Site Province", "Site Rural/Urban", "Attach WPA", "Attach Accreditation");
	public final static List<Integer> colSizes = List.of(3, 1, 1, 2, 1, 2, 2);
	
	private String discipline;
	private Integer noOfLearners;
	private String sitePostalCode;
	private String siteProvince;
	private String siteRuralUrban;
	private Boolean attachWPA;
	private Boolean attachAccreditation;
	
	public DisciplineHDSA(String discipline, Integer noOfLearners, String sitePostalCode) {
		this.discipline = discipline;
		this.noOfLearners = noOfLearners;
		setSitePostalCode(sitePostalCode);
	}
	
	private String fileNameDSA;
	private String fileNameHDSA;
	
	
    public void uploadFile(Media media, boolean isDSA) {
        if (media != null && isDSA) {
            setFileNameDSA(media.getName());
        }else if (media != null && !isDSA) {
        	setFileNameHDSA(media.getName());
        }
    }
	/**
	 * @return the fileNameDSA
	 */
	public String getFileNameDSA() {
		return fileNameDSA;
	}
	/**
	 * @param fileNameDSA the fileNameDSA to set
	 */
	
	public void setFileNameDSA(String fileNameDSA) {
		this.fileNameDSA = fileNameDSA;
		
		BindUtils.postNotifyChange(this, "fileNameDSA");
	}
	/**
	 * @return the fileNameHDSA
	 */
	public String getFileNameHDSA() {
		return fileNameHDSA;
	}
	/**
	 * @param fileNameHDSA the fileNameHDSA to set
	 */
	
	public void setFileNameHDSA(String fileNameHDSA) {
		this.fileNameHDSA = fileNameHDSA;
		BindUtils.postNotifyChange(this, "fileNameHDSA");
	}
}
