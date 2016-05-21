package com.lehanh.pama.ui.clientcustomer;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class ClientCustomerPerspectiveHandler {

	public static final String ID = "com.lehanh.pama.clientCustomerPerspective"; //$NON-NLS-1$
	
    @Execute
    public void switchPersepctive(MPerspective activePerspective, MApplication app, EPartService partService, EModelService modelService) {
    	List<MPerspective> perspectives = modelService.findElements(app, null, MPerspective.class, null);
        for (MPerspective perspective : perspectives) {
        	if (ID.equals(perspective.getElementId())) {
                partService.switchPerspective(perspective);
                return;
            }
        }
    }
}
