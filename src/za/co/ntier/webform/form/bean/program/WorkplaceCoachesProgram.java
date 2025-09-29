package za.co.ntier.webform.form.bean.program;

import java.util.List;

import za.co.ntier.api.model.X_ZZ_Application_Form;
import za.co.ntier.webform.form.AbstractProgram;
import za.co.ntier.webform.form.MenuContextInfo;
import za.co.ntier.webform.form.Util;

public class WorkplaceCoachesProgram extends AbstractProgram {
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
	
	public WorkplaceCoachesProgram(MenuContextInfo menuContextInfo, X_ZZ_Application_Form applicationForm) {
		super(menuContextInfo, applicationForm);
		if (applicationForm != null) {
			placementWorkplaceCoaches = Util.convert(applicationForm.getZZPlacementWorkplaceCoaches());
			trainingWorkplaceCoaches = Util.convert(applicationForm.getZZTrainingWorkplaceCoaches());
		}
	}
	@Override
	public void saveForm(String trxName, X_ZZ_Application_Form applicationForm) {
		super.saveForm(applicationForm);
		applicationForm.setZZPlacementWorkplaceCoaches(Util.convert(placementWorkplaceCoaches));
		applicationForm.setZZTrainingWorkplaceCoaches(Util.convert(trainingWorkplaceCoaches));
		
		applicationForm.setZZTotalNumberApplied(Util.convert(trainingWorkplaceCoaches) + Util.convert(placementWorkplaceCoaches));
		
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
	
	public boolean isProgramValid() {
	    Integer d1 = placementWorkplaceCoaches;
	    Integer d2 = trainingWorkplaceCoaches;
	    return (v(d1) + v(d2)) > 0; // at least one > 0
	    // If BOTH must be > 0 instead, use:
	    // return v(d1) > 0 && v(d2) > 0;
	}

	private static int v(Integer x) { return x == null ? 0 : x; }

}
