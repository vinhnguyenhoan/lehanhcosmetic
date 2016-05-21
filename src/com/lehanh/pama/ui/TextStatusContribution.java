package com.lehanh.pama.ui;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

class TextStatusContribution extends ControlContribution {
	
	private Control text;
	
	private Listener listener;

	private Display display;

	private boolean button;
	
	public String initialText;
	
	TextStatusContribution(String id) {
		this(id, false);
	}
	
	TextStatusContribution(String id, boolean button) {
		super(id);
		this.button = button;
	}
	
	Point computeSize(int w, int h, boolean changed) {
		return text.computeSize(w, h, changed);
	}
	
	void setSelectionListener(Listener listener) {
		this.listener = listener;
	}

	@Override
	protected Control createControl(Composite parent) {
		display = parent.getDisplay();
		if (button) {
			text = new Button(parent, SWT.CENTER);
		} else {
			Composite comp = new Composite(parent, SWT.NONE);
			comp.setLayout(new GridLayout(1, true));
//			StatusLineLayoutData ld = new StatusLineLayoutData();
//			comp.setLayoutData()
			text = new CLabel(comp, SWT.CENTER);
		}
		if (initialText != null) {
			if (button) {
				((Button) text).setText(initialText);
			} else {
				((CLabel) text).setText(initialText);
			}
		}
		if (listener != null) {
			text.addListener(SWT.Selection, listener);
		}
		return text;
	}
	
	void setText(String s) {
		if (s == null) {
			s = ""; //$NON-NLS-1$
		}
		if (button) {
			((Button) text).setText(s);
		} else {
			((CLabel) text).setText(s);
		}
		text.getParent().layout();
		text.getParent().getParent().layout();
		text.getParent().getParent().redraw();
	}
	
	void setTextAsync(final String s) {
		if (display == null) {
			return;
		}
		display.asyncExec(new Runnable() {
			
			@Override
			public void run() {
				setText(s);
			}
		});
	}

	void setBounds(int x, int y, int w, int h) {
		this.text.setBounds(x, y, w, h);
	}
}
