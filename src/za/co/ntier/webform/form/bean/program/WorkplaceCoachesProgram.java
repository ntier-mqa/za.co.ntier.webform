package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.model.X_ZZ_Application_Form;

public class WorkplaceCoachesProgram implements ISaveForm, IProgram {
	private List<String> placementWorkplaces = List.of("This programme is for coaches who are appointed by the company to coach MQA Artisan Development Programme Learners"
			, "The placement of coaches is for a coach that will be placed at workplaces to coach the leaners through their structured workplace based learning"
			, "The duration of this programme is 12 months"
			, "The coach must be able to transfer skills and knowledge to learners in the workplace"
			, "The coach must preferably be an HDSA individual"
			, "Preference should be given to retrenched/retired/unemployed/employed individuals with relevant qualifications and experience in the relevant discipline on this project"
			, "Comply to the requirement as stated in the guidelines"
			);
	
	private List<String> trainingtWorkplaces = List.of("This intervention is employed persons who are currently coaching learners and employed people who are earmarked to become coaches"
			, "The training of coaches is for coaches that will be trained by the MQA appointed training provider who will train coaches on coaching skills and providing the coaches with tools and techniques necessary to effectively support the professional development of learners"
			, "The MQA will fund the training of coaches to develop their coaching skill"
			, "The MQA will appoint the service provider who will provide training for the coaches"
			);
	
	private Integer placementWorkplaceCoaches;
	public Integer getPlacementWorkplaceCoaches() {
		return placementWorkplaceCoaches;
	}
	public void setPlacementWorkplaceCoaches(Integer placementWorkplaceCoaches) {
		this.placementWorkplaceCoaches = placementWorkplaceCoaches;
	}
	public Integer getTrainingWorkplaceCoaches() {
		return trainingWorkplaceCoaches;
	}
	public void setTrainingWorkplaceCoaches(Integer trainingWorkplaceCoaches) {
		this.trainingWorkplaceCoaches = trainingWorkplaceCoaches;
	}

	private Integer trainingWorkplaceCoaches;
	
	public WorkplaceCoachesProgram(X_ZZ_Application_Form applicationForm) {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		applicationForm.setZZPlacementWorkplaceCoaches(placementWorkplaceCoaches);
		applicationForm.setZZTrainingWorkplaceCoaches(trainingWorkplaceCoaches);
		
	}
	/**
	 * @return the placementWorkplaces
	 */
	public List<String> getPlacementWorkplaces() {
		return placementWorkplaces;
	}
	/**
	 * @param placementWorkplaces the placementWorkplaces to set
	 */
	public void setPlacementWorkplaces(List<String> placementWorkplaces) {
		this.placementWorkplaces = placementWorkplaces;
	}
	/**
	 * @return the trainingtWorkplaces
	 */
	public List<String> getTrainingtWorkplaces() {
		return trainingtWorkplaces;
	}
	/**
	 * @param trainingtWorkplaces the trainingtWorkplaces to set
	 */
	public void setTrainingtWorkplaces(List<String> trainingtWorkplaces) {
		this.trainingtWorkplaces = trainingtWorkplaces;
	}
	
	@Override
	public boolean isProgramValid() {
		Integer d1 = placementWorkplaceCoaches;
	    Integer d2 = trainingWorkplaceCoaches;
	    // example rule: both required and > 0
	    return d1 != null && d1 > 0 && d2 != null && d2 > 0;
	    // or: at least one > 0 -> return (nvl(d1,0) + nvl(d2,0)) > 0;
	}

}
