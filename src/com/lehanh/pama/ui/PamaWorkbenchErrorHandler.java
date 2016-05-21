package com.lehanh.pama.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
//import org.eclipse.ui.statushandlers.StatusAdapter;
//import org.eclipse.ui.statushandlers.StatusManager;
//import org.eclipse.ui.statushandlers.WorkbenchErrorHandler;

import com.lehanh.pama.util.PamaHome;

public class PamaWorkbenchErrorHandler /*extends WorkbenchErrorHandler*/ {

	/*public void handle(final StatusAdapter statusAdapter, int style) {
		if (statusAdapter.getStatus().matches(IStatus.ERROR) && ((style != StatusManager.NONE))) {
            PamaHome.application.logError("Uncaught Exception", statusAdapter.getStatus().getException());
            //UnexpectedErrorDialog();
            MessageBox dialog = 
				  new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText("Lổi chương trình");
			String messageError = statusAdapter.getStatus().getException().getMessage();
			if (messageError == null) {
				messageError = statusAdapter.getStatus().getException().toString();
			}
			dialog.setMessage(messageError);
			dialog.open();
	    } else {
	    	super.handle(statusAdapter, style);
	    }
	}*/
}
