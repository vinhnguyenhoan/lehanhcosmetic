package com.lehanh.pama.ui.util;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.lehanh.pama.ui.IFormManager;

public class FormManager implements IFormManager {

	private static final String ALL_CONTROL = "ALL_CONTROL";
	private static final String READ_ONLY = "READ_ONLY";
	private final Map<String, List<Control>> formControls = new HashMap<String, List<Control>>();
	
	private Class<?>[] ignoreControlType = {Table.class};
	
	private Map<String, Button[]> radioGroups = new HashMap<String, Button[]>();
	private List<Button> defaultRadios = new LinkedList<Button>();
	
	private List<Control> ignoreControls = new LinkedList<Control>();
	private List<Button> addNewButtons = new LinkedList<Button>();
	private List<Button> updateButtons = new LinkedList<Button>();
	private List<Button> saveButtons = new LinkedList<Button>();
	private List<Button> cancelButtons = new LinkedList<Button>();
	
	public FormManager() {
	}
	
	@Override
	public IFormManager addAllControlFromComposite(Composite composite, Control... ignoreControls) {
		return addAllControlFromComposite(composite, false, false, ignoreControls);
	}
	
	@Override
	public IFormManager addAllControlFromComposite(Composite composite, boolean ignoreLabel, boolean ignoreButtons, Control... ignoreControls) {
		this.ignoreControls.addAll(Arrays.asList(ignoreControls));
		List<Control> collectedControls = collectControls(composite, ignoreLabel, ignoreButtons); 
		formControls.put(ALL_CONTROL, collectedControls);
		return this;
	}
	
	@Override
	public List<Control> getAllControls() {
		LinkedList<Control> result = new LinkedList<Control>();
		for (List<Control> listControl : formControls.values()) {
			result.addAll(listControl);
		}
		return result;
	}
	
	private List<Control> collectControls(Composite composite, boolean ignoreLabel, boolean ignoreButtons) {
		List<Control> collectedControls = new LinkedList<Control>();
		
		for (Class<?> ignoreType : ignoreControlType) {
			if (ignoreType.isInstance(composite)) {
				return collectedControls;
			}
		}
		if (ignoreFromList(composite, this.ignoreControls)) {
			return collectedControls;
		}
		if (ignoreButtons) {
			if (ignoreFromList(composite, this.addNewButtons)
					|| ignoreFromList(composite, this.updateButtons)
					|| ignoreFromList(composite, this.saveButtons)
					|| ignoreFromList(composite, this.cancelButtons)) {
				return collectedControls;
			}
		}
		
		// If subclass of Composite sure that is a kind of component so just handle this as a control
		if (!composite.getClass().equals(Composite.class)) {
			collectedControls.add(composite);
			return collectedControls;
		}
		
		for (Control child : composite.getChildren()) {
			if (ignoreFromList(child, this.ignoreControls)) {
				continue;
			}
			if (ignoreButtons) {
				if (ignoreFromList(child, this.addNewButtons)
						|| ignoreFromList(child, this.updateButtons)
						|| ignoreFromList(child, this.saveButtons)
						|| ignoreFromList(child, this.cancelButtons)) {
					continue;
				}
			}
			if (ignoreLabel && (child instanceof CLabel || child instanceof Label)) {
				continue;
			}
			if (Composite.class == child.getClass()) {
				collectedControls.addAll(collectControls((Composite) child, ignoreLabel, ignoreButtons));
			} else {
				if (SWT.READ_ONLY == (child.getStyle() & SWT.READ_ONLY)) {
					child.setData(READ_ONLY, true);
					setEditable(false, child);
				}
				collectedControls.add(child);
			}
		}
		return collectedControls;
	}

	private static final boolean ignoreFromList(Control child, List<? extends Control> ignoreControls) {
		for (Control ignoreControl : ignoreControls) {
			if (child == ignoreControl) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IFormManager enableAll(boolean enabled) {
		for (Control control : formControls.get(ALL_CONTROL)) {
			if (ignoreFromList(control, this.ignoreControls)) {
				continue;
			}
			control.setEnabled(enabled);
		}
		return this;
	}

	@Override
	public IFormManager setEditableAll(boolean editable) {
		for (Control control : formControls.get(ALL_CONTROL)) {
			if (ignoreFromList(control, this.ignoreControls)) {
				continue;
			}
			setEditable(editable, control);
		}
		return this;
	}

	@Override
	public IFormManager setEditable(boolean editable, Control... controls) {
		if (controls == null || controls.length == 0) {
			return this;
		}
		
		for (Control control : controls) {
			Boolean isReadOnly = (Boolean) control.getData(READ_ONLY);
			if (control instanceof Text && !Boolean.TRUE.equals(isReadOnly)) {
				// Ignore if read only style control
				((Text) control).setEditable(editable);
			} else if (control instanceof CCombo) {
				CCombo ccombo = (CCombo) control;
				ccombo.setListVisible(editable);
				ccombo.setEnabled(editable);
			} else if (control instanceof Combo) {
				Combo combo = (Combo) control;
				combo.setListVisible(editable);
				combo.setEnabled(editable);
			} else if (control instanceof TableCombo) {
				TableCombo tC = (TableCombo) control;
				tC.setEnabled(editable);
				//tC.setTableVisible(editable);
			} else {
				control.setEnabled(editable);
			}
		}
		return this;
	}

	@Override
	public IFormManager clearFormData() {
		FormManager.clearFormData(this.formControls.get(ALL_CONTROL), ignoreControls, defaultRadios);
		return this;
	}
	
	public static final void clearFormData(List<Control> controls, List<Control> ignoreControls, List<Button> defaultRadios) {
		for (Control control : controls) {
			if (ignoreFromList(control, ignoreControls)) {
				continue;
			}
			if (control instanceof Text) {
				((Text) control).setText(StringUtils.EMPTY);
			} else if (control instanceof CCombo) {
				UIControlUtils.revert((CCombo) control);
			} else if (control instanceof Combo) {
				// TODO revert index 0 or -1
			} else if (control instanceof TableCombo) {
				((TableCombo) control).getTextControl().setText(StringUtils.EMPTY);
			} else if (control instanceof CDateTime) {
				((CDateTime) control).setSelection((Date) null);
			} else if (control instanceof Button) {
				((Button) control).setSelection(false);
				for (Button dB : defaultRadios) {
					if (control == dB) {
						((Button) control).setSelection(true);
					}
				}
			}
		}
	}

	@Override
	public IFormManager addCreateButtons(Button... addNewBtn) {
		addNewButtons.addAll(Arrays.asList(addNewBtn));
		ignoreControls.addAll(addNewButtons);
		return this;
	}

	@Override
	public IFormManager addEditButtons(Button... editBtn) {
		updateButtons.addAll(Arrays.asList(editBtn));
		ignoreControls.addAll(updateButtons);
		return this;
	}

	@Override
	public IFormManager addSaveButtons(Button... saveBtn) {
		saveButtons.addAll(Arrays.asList(saveBtn));
		ignoreControls.addAll(saveButtons);
		return this;
	}

	@Override
	public IFormManager addCancelButtons(Button... cancelBtn) {
		cancelButtons.addAll(Arrays.asList(cancelBtn));
		ignoreControls.addAll(cancelButtons);
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IFormManager saved(boolean stillEdit) {
		return this.enableButtons(false, saveButtons, cancelButtons)
				   .enableButtons(stillEdit, updateButtons)
				   .enableButtons(true, addNewButtons)
				   .setEditableAll(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IFormManager edit() {
		return this.enableButtons(false, addNewButtons, updateButtons)
		   		   .enableButtons(true, saveButtons, cancelButtons)
		           .setEditableAll(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IFormManager cancel(boolean stillEdit) {
		return this.enableButtons(false, cancelButtons, saveButtons)
				   .enableButtons(stillEdit, updateButtons)
				   .enableButtons(true, addNewButtons)
				   .setEditableAll(false)
				   .clearFormData();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IFormManager addNew() {
		return this.enableButtons(false, addNewButtons, updateButtons)
		           .enableButtons(true, saveButtons, cancelButtons)
				   .setEditableAll(true)
				   .clearFormData();
	}

	private FormManager enableButtons(boolean enable, List<Button>... buttonsLists) {
		for (List<Button> buttonsList : buttonsLists) {
			if (buttonsList == null) {
				continue;
			}
			for (Button button : buttonsList) {
				button.setEnabled(enable);
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IFormManager setEnableAllButtons(boolean isEnable) {
		return this.enableButtons(false, cancelButtons, saveButtons, updateButtons, addNewButtons);
	}

	@Override
	public IFormManager addRadioGroup(String groupId, final Button... radioButtons) {
		if (radioButtons == null) {
			return this;
		}
		this.radioGroups.put(groupId, radioButtons);
		for (final Button button : radioButtons) {
			button.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					setDeselectAllButton(!button.getSelection(), radioButtons, button);
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					setDeselectAllButton(!button.getSelection(), radioButtons, button);
				}
			});
		}
		return this;
	}

	private void setDeselectAllButton(boolean select, Button[] radioButtons, Button... ignoreButtons) {
		rootLoop: 
		for (final Button button : radioButtons) {
			for (final Button igButton : ignoreButtons) {
				if (button == igButton) {
					continue rootLoop;
				}
			}
			button.setSelection(select);
		}
	}

	@Override
	public IFormManager defaultRadios(Button... radioButtons) {
		if (radioButtons == null || radioButtons.length == 0) {
			this.defaultRadios = new LinkedList<Button>();
		} else {
			this.defaultRadios = Arrays.asList(radioButtons);
		}
		return this;
	}

	@Override
	public Button getSelected(String groupId) {
		for(Button but : radioGroups.get(groupId)) {
			if (but.getSelection()) {
				return but;
			}
		}
		return null;
	}

	public static final void showMessage(Shell parent, String text, String message) {
		MessageBox dialog = new MessageBox(parent, SWT.ICON_INFORMATION | SWT.OK);
		dialog.setText(text);
		dialog.setMessage(message);
		dialog.open();
	}
	
	public static final void showError(Shell parent, String text, String message) {
		MessageBox dialog = new MessageBox(parent, SWT.ICON_ERROR | SWT.OK);
		dialog.setText(text);
		dialog.setMessage(message);
		dialog.open();
	}
}