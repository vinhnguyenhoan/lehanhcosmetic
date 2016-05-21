package com.lehanh.pama.ui.patientcase;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
//import org.eclipse.ui.PlatformUI;
//import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.ui.util.ObjectToUIText;
import com.lehanh.pama.ui.util.UIControlUtils;

public class SurgeryImageListSelectionDialog extends Dialog {

	protected String[] dir;
	private CCombo surgerCombo;
	private List<Catagory> allSurgeryCat;
	private Object result;

	public SurgeryImageListSelectionDialog(Shell parentShell, List<Catagory> allSurgeryCat) {
		super(parentShell);
		this.allSurgeryCat = allSurgeryCat;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("New Label"); //$NON-NLS-1$

		this.surgerCombo = new CCombo(container, SWT.BORDER);
		surgerCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label = new Label(container, SWT.NONE);
		label.setText("File: "); //$NON-NLS-1$

		final Composite fileCom = new Composite(container, SWT.NONE);
		fileCom.setLayout(new GridLayout(5, true));

		// Create the text box extra wide to show long paths
		final Text imagePathText = new Text(fileCom, SWT.BORDER);
		/*imagePathText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (dir == null || dir.length < 1) {
					dir = new String[1];
				}
				dir[0] = imagePathText.getText();
				// TODO validateCurrentSelection();
			}
		});*/
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 4;
		imagePathText.setLayoutData(data);

		// Clicking the button will allow the user
		// to select a directory
		Button button = new Button(fileCom, SWT.PUSH);
		button.setText(Messages.SurgeryImageListSelectionDialog_chon);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog dlg = new FileDialog(parent.getShell(), SWT.MULTI);

				// Set the initial filter path according
				// to anything they've selected or typed in
				dlg.setFilterPath(imagePathText.getText());

				// Change the title bar text
				dlg.setText(Messages.SurgeryImageListSelectionDialog_anhphauthuat);

				String[] filterExt = { "*.*", "*.png", "*.jpeg", "*.jpg", "*.PNG", "*.JPEG", "*.JPG" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				dlg.setFilterExtensions(filterExt);

				// Calling open() will open and run the dialog.
				// It will return the selected directory, or
				// null if user cancels
				//SurgeryImageListSelectionDialog.this.dir = dlg.open();
				if (dlg.open() != null) {
					// Set the text box to the new selection
					String path = dlg.getFilterPath();
					String[] dirs = dlg.getFileNames();
					if (dirs != null) {
						imagePathText.setText(Arrays.toString(dirs));
						SurgeryImageListSelectionDialog.this.dir = new String[dirs.length];
						for (int i = 0; i < dirs.length; i++) {
							String aDir = path + "/" + dirs[i];
							SurgeryImageListSelectionDialog.this.dir[i] = aDir;
						}
					}
				}
			}
		});

		setElements(allSurgeryCat);
		
		return container;
	}

	String[] filePath() {
		return this.dir;
	}

	// overriding this methods allows you to set the
	// title of the custom dialog
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.SurgeryImageListSelectionDialog_chonanhchophauthuat);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 200);
	}

	public void setElements(List<Catagory> allSurgery) {
		if (allSurgery == null || allSurgery.isEmpty()) {
			return;
		}
		UIControlUtils.initialCombo(surgerCombo, allSurgery, null, 0,
			new ObjectToUIText<Catagory, Long>() {
	
				@Override
				public String showUI(Catagory cat) {
					return cat.getName() + " (" + cat.getSymbol() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				}
	
				@Override
				public Long getIdForUI(Catagory cat) {
					return cat.getId();
				}
	
			});
		result = UIControlUtils.getValueFromCombo(surgerCombo);
		
		surgerCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = UIControlUtils.getValueFromCombo(surgerCombo);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				result = UIControlUtils.getValueFromCombo(surgerCombo);
			}
		});
	}

	Object getResult() {
		return this.result;
	}
}

// public class SurgeryImageListSelectionDialog /*extends
// ElementListSelectionDialog*/ {

// private Object[] fElements;
// protected String dir;
//
// public SurgeryImageListSelectionDialog(Shell parent, ILabelProvider renderer)
// {
// super(parent, renderer);
// super.setSize(50, 7);
// }
//
// /**
// * Sets the elements of the list.
// *
// * @param elements
// * the elements of the list.
// */
// public void setElements(Object[] elements) {
// fElements = elements;
// }
//
// protected Control createDialogArea(final Composite parent) {
// Composite contents = (Composite) this.createDialogAreaParent(parent);
//
// createMessageArea(contents);
// createFilterText(contents);
// createFilteredList(contents);
//
// setListElements(fElements);
//
// setSelection(getInitialElementSelections().toArray());
//
// final Composite fileCom = new Composite(parent, SWT.NONE);
// fileCom.setLayout(new GridLayout(6, true));
// new Label(fileCom, SWT.NONE).setText("File: ");
//
// // Create the text box extra wide to show long paths
// final Text imagePathText = new Text(fileCom, SWT.BORDER);
// imagePathText.addModifyListener(new ModifyListener() {
//
// @Override
// public void modifyText(ModifyEvent e) {
// dir = imagePathText.getText();
// validateCurrentSelection();
// }
// });
// GridData data = new GridData(GridData.FILL_HORIZONTAL);
// data.horizontalSpan = 4;
// imagePathText.setLayoutData(data);
//
// // Clicking the button will allow the user
// // to select a directory
// Button button = new Button(fileCom, SWT.PUSH);
// button.setText("Chọn...");
// button.addSelectionListener(new SelectionAdapter() {
// public void widgetSelected(SelectionEvent event) {
// FileDialog dlg = new FileDialog(parent.getShell());
//
// // Set the initial filter path according
// // to anything they've selected or typed in
// dlg.setFilterPath(imagePathText.getText());
//
// // Change the title bar text
// dlg.setText("Ảnh phẩu thuật");
//
// String[] filterExt = { "*.*", "*.png", "*.jpeg", "*.jpg", "*.PNG", "*.JPEG",
// "*.JPG" };
// dlg.setFilterExtensions(filterExt);
//
// // Calling open() will open and run the dialog.
// // It will return the selected directory, or
// // null if user cancels
// SurgeryImageListSelectionDialog.this.dir = dlg.open();
// if (dir != null) {
// // Set the text box to the new selection
// imagePathText.setText(dir);
// }
// }
// });
//
// return contents;
// }
//
// private Control createDialogAreaParent(Composite parent) {
// // create a composite with standard margins and spacing
// Composite composite = new Composite(parent, SWT.NONE);
// GridLayout layout = new GridLayout();
// layout.marginHeight =
// convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
// layout.marginWidth =
// convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
// layout.verticalSpacing =
// convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
// layout.horizontalSpacing =
// convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
// composite.setLayout(layout);
// composite.setLayoutData(new GridData(GridData.FILL_BOTH));
// applyDialogFont(composite);
// return composite;
// }
//
// protected boolean validateCurrentSelection() {
// boolean result = super.validateCurrentSelection();
// if (!result) {
// return false;
// }
//
// IStatus status;
// boolean isExistFile = dir != null ? new File(dir).exists() : false;
// if (isExistFile) {
// status = new Status(IStatus.OK, PlatformUI.PLUGIN_ID,
// IStatus.OK, "", null);
// } else {
// status = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID,
// IStatus.ERROR, dir != null ? "File không tồn tại" : "Chưa chọn file", null);
// }
//
// updateStatus(status);
//
// return result;
// }
//
// String filePath() {
// return this.dir;
// }
// }
