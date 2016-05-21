package com.lehanh.pama.ui.handlers;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class PaitentCaseStatusLine {
	
	public static CLabel label;
	
	@PostConstruct
	public void createControls(Composite parent) {
		label = new CLabel(parent, SWT.NONE);
		//label.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		label.setForeground(SWTResourceManager.getColor(128, 0, 0));
		//label.setSize(78, 21);
		label.setText(Messages.PaitentCaseStatusLine_benhan);
	}
}