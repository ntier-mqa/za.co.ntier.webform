package za.co.ntier.webform.form.bean.program;
import java.lang.reflect.InvocationTargetException;
import za.co.ntier.webform.form.IProgram;
import za.co.ntier.webform.form.ISaveForm;
import za.co.ntier.webform.form.MenuContextInfo;

public class CentreOfSpecialisationProgram extends ArtisanDevProgram implements ISaveForm, IProgram{

	public CentreOfSpecialisationProgram(MenuContextInfo menuContextInfo) throws NoSuchMethodException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		super(menuContextInfo);
	}

}
