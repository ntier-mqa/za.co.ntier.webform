package za.co.ntier.webform.form;

public interface IProgram extends ISaveForm {
	
	default boolean isProgramValid() {
		return true;
	}

}
