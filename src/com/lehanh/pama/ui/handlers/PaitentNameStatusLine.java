package com.lehanh.pama.ui.handlers;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

public class PaitentNameStatusLine {
	
	public static CLabel label;
	
	@PostConstruct
	public void createControls(Composite parent) {
		label = new CLabel(parent, SWT.NONE);
		label.setFont(SWTResourceManager.getFont(Messages.PaitentNameStatusLine_0, 10, SWT.BOLD));
		label.setForeground(SWTResourceManager.getColor(128, 0, 0));
		//label.setSize(78, 21);
		label.setText(Messages.PaitentNameStatusLine_benhan);
	}
}