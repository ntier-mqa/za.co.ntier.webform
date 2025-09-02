package za.co.ntier.webform.form.viewmodel;

import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.UploadDocComponent;

/**
 * If super has an init method but its ChildViewModel doesn't, you can
 * add @Init(superclass=true) on the ChildViewModel to use super's init.
 */
@Init(superclass = true)
public class UploadDocVMWrapper extends ComponentVMWrapper<UploadDocComponent> {

}
