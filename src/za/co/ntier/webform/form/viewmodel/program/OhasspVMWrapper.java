package za.co.ntier.webform.form.viewmodel.program;

import org.zkoss.bind.annotation.Init;

import za.co.ntier.webform.form.bean.program.OhasspProgram;

/**
 * If super has an init method but its ChildViewModel doesn't, you can add @Init(superclass=true) on the ChildViewModel to
use super's init.
 */
@Init(superclass=true)
public class OhasspVMWrapper extends ProgramVMWrapper<OhasspProgram>{

}
