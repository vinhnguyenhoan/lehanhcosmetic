package com.lehanh.pama.ui.util;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.lehanh.pama.util.PamaException;

public class UIControlUtils {

	private static final String FIRST_TEXT = "FIRST_TEXT";
	private static final String ID_KEY = "ID_KEY";
	private static final String VALUE_KEY = "VALUE_KEY";
	private static final String DIRTY = "DIRTY";
	private static final String START_INDEX = "START_INDEX";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final void initialCombo(CCombo combo, Iterable<?> listData, String firstText, int startIndex, ObjectToUIText objectToUIText) {
		// clear all data and item before set new values
		combo.removeAll();
		combo.setData(FIRST_TEXT, null);
		for (int i = 0; i < combo.getItemCount(); i++) {
			combo.setData(ID_KEY + i, null);
			combo.setData(VALUE_KEY + i, null);
		}
		
		// set new values and items
		int comboItemIndex = 0;
		if (firstText != null) {
			combo.add(firstText);
			combo.setData(FIRST_TEXT, firstText);
			comboItemIndex = 1;
		}
		
		for (Object value : listData) {
			if (objectToUIText != null) {
				combo.add(objectToUIText.showUI(value));
				combo.setData(ID_KEY + objectToUIText.getIdForUI(value), comboItemIndex);
			} else {
				combo.add(value.toString());
				combo.setData(ID_KEY + comboItemIndex, comboItemIndex);
			}
			combo.setData(VALUE_KEY + comboItemIndex, value);
			comboItemIndex++;
		}
		
		combo.setData(START_INDEX, startIndex);
		combo.select(startIndex);
	}
	
	public static final Object getValueFromCombo(CCombo combo) {
		int index = combo.getSelectionIndex();
		if (index < 0) {
			return null;
		}
		return combo.getData(VALUE_KEY + index);
	}

	public static void selectComboById(CCombo combo, Object key) {
		Integer index = (Integer) combo.getData(ID_KEY + key);
		if (index == null) {
			index = (Integer) combo.getData(START_INDEX);
		}
		if (index == null) {
			index = -1;
		}
		combo.select(index);
	}

	public static void selectComboOrSetTextByName(CCombo combo, String name) {
		if (combo.getItems() == null) {
			return;
		}
		if (name == null) {
			Integer index = (Integer) combo.getData(START_INDEX);
			if (index != null) {
				index = -1;
			}
			combo.select(index);
			return;
		}       
		
		name = name.trim();
		int index = 0;
		for (String item : combo.getItems()) {
			if (item.trim().equals(name)) {
				combo.select(index);
				return;
			}
			index++;
		}
		combo.setText(name);
	}
	
	public static final void listenModifiedByClient(final Text text) {
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				text.setData(DIRTY, text.isFocusControl());
			}
		});
	}

	public static final boolean isChanged(Text text) {
		Boolean isDirty = (Boolean) text.getData(DIRTY);
		return isDirty != null && isDirty;
	}

	public static final void revert(CCombo combo) {
		Integer startIndex = (Integer) combo.getData(START_INDEX);
		if (startIndex == null) {
			return;
		}
		combo.select(startIndex);
	}

	public static int getTextAsInt(Text textControl, String messError) {
		if (textControl == null) {
			return 0;
		} else {
			if (StringUtils.isBlank(textControl.getText())) {
				return 0;
			}
			try {
				return Integer.parseInt(textControl.getText());
			} catch (NumberFormatException e) {
				throw new PamaException(messError);
			}
		}
	}
	
	public static float getTextAsFloat(Text textControl, String messError) {
		if (textControl == null) {
			return 0;
		} else {
			if (StringUtils.isBlank(textControl.getText())) {
				return 0;
			}
			try {
				return Float.parseFloat(textControl.getText());
			} catch (NumberFormatException e) {
				throw new PamaException(messError);
			}
		}
	}
	
	public static void setText(Text textControl, String textContent) {
		if (textContent == null) {
			textControl.setText(StringUtils.EMPTY);
		} else {
			textControl.setText(textContent);
		}
	}
	
	public static void setText(Label labelControl, String textContent) {
		if (textContent == null) {
			labelControl.setText(StringUtils.EMPTY);
		} else {
			labelControl.setText(textContent);
		}
	}
	
	public static void setText(CLabel labelControl, String textContent) {
		if (textContent == null) {
			labelControl.setText(StringUtils.EMPTY);
		} else {
			labelControl.setText(textContent);
		}
	}
	
	public static void openMessageBox(Shell shell, String titile, String message) {
		MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
		dialog.setText(titile);
		dialog.setMessage(message);
		dialog.open();
	}

//	public static void initialCombo(TableComboViewer combo,
//			Collection<Catagory> values, String firstText, int startIndex,
//			CatagoryToUIText objectToUIText) {
//		
//		// set the content provider
//		combo.setContentProvider(ArrayContentProvider.getInstance());
//		
//		// set the label provider
//		combo.setLabelProvider(new SingleItemLabelProvider());
//
//		// load the data
//		combo.setInput(modelList);
//		
//		// add listener
//		combo.addSelectionChangedListener(new ItemSelected("Sample1"));

		
//		int comboItemIndex = 0;
//		
//		if (firstText != null) {
//			combo.add(firstText);
//			combo.setData(FIRST_TEXT, firstText);
//			comboItemIndex = 1;
//		}
//		
//		for (Object value : listData) {
//			if (objectToUIText != null) {
//				combo.add(objectToUIText.showUI(value));
//				combo.setData(ID_KEY + objectToUIText.getIdForUI(value), comboItemIndex);
//			} else {
//				combo.add(value.toString());
//				combo.setData(ID_KEY + comboItemIndex, comboItemIndex);
//			}
//			combo.setData(VALUE_KEY + comboItemIndex, value);
//			comboItemIndex++;
//		}
//		
//		combo.setData(START_INDEX, startIndex);
//		combo.select(startIndex);
//	}

}