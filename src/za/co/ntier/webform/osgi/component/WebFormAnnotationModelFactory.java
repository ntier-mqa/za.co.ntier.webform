package za.co.ntier.webform.osgi.component;

import org.adempiere.base.AnnotationBasedModelFactory;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true, service = org.adempiere.base.IModelFactory.class, property = "service.ranking:Integer=1")
public class WebFormAnnotationModelFactory extends AnnotationBasedModelFactory {

}
