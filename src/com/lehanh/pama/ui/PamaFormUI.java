package com.lehanh.pama.ui;

import javax.annotation.PostConstruct;

import org.eclipse.swt.widgets.Composite;
//import org.eclipse.ui.part.ViewPart;

import com.lehanh.pama.ui.util.FormManager;

public abstract class PamaFormUI /*extends ViewPart*/ implements AFormUIView {

	private final IFormManager formManager = new FormManager();
	
	@PostConstruct
	public final void createPartControl(Composite parent) {
		this.createFormUI(parent);
		organizeUIComponent();
	}

	protected abstract void createFormUI(Composite parent);
	
	protected final IFormManager getFormManager() {
		return this.formManager;
	}
}
