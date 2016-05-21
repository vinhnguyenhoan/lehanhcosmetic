package com.lehanh.pama.ui;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

class SeparatorContribution extends ControlContribution {

	SeparatorContribution() {
		super("com.lehanh.pama.ui.BarStatusContribution");
	}

	@Override
	protected Control createControl(Composite parent) {
		Label label = new Label(parent, SWT.SEPARATOR);
		return label;
	}
}
