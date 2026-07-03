package za.co.ntier.webform.sdr.viewmodel;

import java.util.Arrays;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Init;

@Init(superclass = true)
public class StepAppVM extends BaseAppVM {
	private List<String> steps;

	public List<String> getSteps() {
		return steps;
	}

	public void setStep(String stepName) {
		setSteps(stepName);
	}
	
    public void initStep(String stepName) {
    	setSteps(stepName);
    }
    
    private void setSteps(String stepName) {
    	steps = Arrays.asList(stepName);
		BindUtils.postNotifyChange(this, "steps");
    }
}
