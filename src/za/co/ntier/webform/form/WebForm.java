package za.co.ntier.webform.form;

import org.adempiere.webui.panel.ADForm;
import org.compiere.util.Env;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;

@org.idempiere.ui.zk.annotation.Form(name = "za.co.ntier.webform.form.EmployerApplicationForm")
public class WebForm extends ADForm{

	private static final long serialVersionUID = -5402852171052424756L;
	
	@Override
	protected void init(int adFormId, String name) {
		String formTitle = Env.getContext(Env.getCtx(), m_WindowNo, "+formTitle");
		super.init(adFormId, formTitle);
	}
	
	@Override
	protected void initForm() {
		String zulPath = Env.getContext(Env.getCtx(), m_WindowNo, "+zulPath");
		
		String zulPathRelative = WebForm.class.getResource(zulPath).toString();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(WebForm.class.getClassLoader());
		Component inc = null;
		try {
			inc = Executions.createComponents(zulPathRelative, null, null);
		}finally {
			Thread.currentThread().setContextClassLoader(cl);
		}

		Div outterDiv = new Div();
		this.appendChild(outterDiv);
		outterDiv.setSclass("mqaWebForm");
		
		Div container = new	Div();
		container.setSclass("container");
		outterDiv.appendChild(container);
		
		container.appendChild(inc);
	}
	
}
