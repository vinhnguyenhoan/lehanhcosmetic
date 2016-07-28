package com.lehanh.pama.ui;

import java.util.List;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public interface IFormManager {

	IFormManager addAllControlFromComposite(Composite composite, Control... ignoreControls);

	IFormManager addAllControlFromComposite(Composite composite, boolean ignoreLabel, boolean ignoreButtons, Control... ignoreControls);

	IFormManager enableAll(boolean enabled);

	IFormManager setEditableAll(boolean editable);

	IFormManager setEditable(boolean editable, Control... controls);
	
	IFormManager clearFormData();

	IFormManager addCreateButtons(Button... addNewBtn);

	IFormManager addEditButtons(Button... editBtn);

	IFormManager addSaveButtons(Button... saveBtn);

	IFormManager addCancelButtons(Button... cancelBtn);

	IFormManager cancel(boolean stillEdit);

	IFormManager saved(boolean stillEdit);

	IFormManager edit();

	IFormManager addNew();

	IFormManager setEnableAllButtons(boolean isEnable);

	IFormManager addRadioGroup(String groupId, Button... radioButtons);

	IFormManager defaultRadios(Button... radioButtons);

	Button getSelected(String groupId);

	List<Control> getAllControls();

}