package com.lehanh.pama.ui.patientcase;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import com.lehanh.pama.ui.Activator;

public class PatientPerspectiveHandler {

	public static final String ID = "com.lehanh.pama.patientPerspective";
	
    @Execute
    public void switchPersepctive(MApplication app, EPartService partService, EModelService modelService) {
//    	Activator.modelService = modelService;
//    	Activator.partService = partService;
//    	Activator.app = app;
    	
        List<MPerspective> perspectives = modelService.findElements(app, ID, MPerspective.class, null);
        partService.switchPerspective(perspectives.get(0));
        //List<MPart> parts = modelService.findElements(app, PatientCaseView.ID, MPart.class, null);
        //partService.activate(parts.get(0));
    }
}
