package com.lehanh.pama.ui.patientcase;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.ISurgeryImageList;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.PamaFormUI;
import com.lehanh.pama.ui.util.PamaResourceManager;
import com.lehanh.pama.util.PamaHome;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DetailImageView extends PamaFormUI implements IPatientViewPartListener, IPatientView {
	
	public static final String ID = "com.lehanh.pama.detailImageView"; //$NON-NLS-1$
	
	private SurgeryGallary gallery;

	private ImageCanvasPainter canvasPainter;

	private IPatientManager paManager;

	// private CCombo filterCombo;

	public DetailImageView() {
		paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		paManager.addPaListener(this, ID);
	}

	@Override
	//@PostConstruct
	public void createFormUI(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		SashForm sashForm = new SashForm(composite, SWT.BORDER | SWT.SMOOTH);
		sashForm.setBackground(PamaResourceManager.getColor(SWT.COLOR_GRAY));
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite galleryCom = new Composite(sashForm, SWT.NONE);
		GridLayout gl_galleryCom = new GridLayout(1, false);
		gl_galleryCom.verticalSpacing = 0;
		gl_galleryCom.marginWidth = 0;
		gl_galleryCom.marginHeight = 0;
		gl_galleryCom.horizontalSpacing = 0;
		galleryCom.setLayout(gl_galleryCom);
		galleryCom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// Composite filterCom = new Composite(galleryCom, SWT.NONE);
		// GridLayout gl = new GridLayout(1, false);
		// gl.marginBottom = 5;
		// gl.marginWidth = 0;
		// gl.marginHeight = 0;
		// gl.horizontalSpacing = 0;
		// filterCom.setLayout(gl);
		// filterCom.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false,
		// 1, 1));
		//
		// this.filterCombo = new CCombo(filterCom, SWT.BORDER | SWT.DROP_DOWN |
		// SWT.READ_ONLY);
		// filterCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		// false, 1, 1));
		
		this.gallery = new SurgeryGallary(ID, galleryCom, SWT.V_SCROLL | SWT.MULTI, new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

//		Composite galleryControlCom = new Composite(galleryCom, SWT.NONE);
//		galleryControlCom.setLayout(new GridLayout(1, false));
//		galleryControlCom.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		
//		Button btnShowEmptyCasesButton = new Button(galleryControlCom, SWT.CHECK);
//		btnShowEmptyCasesButton.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				showEmptyCasesButton(btnShowEmptyCasesButton.getSelection());
//			}
//		});
//		btnShowEmptyCasesButton.setBounds(0, 0, 93, 16);
//		btnShowEmptyCasesButton.setText(Messages.DetailImageView_hienlankhamtrong);
		
		Composite composite_1 = new Composite(sashForm, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.verticalSpacing = 0;
		gl_composite_1.marginWidth = 0;
		gl_composite_1.marginHeight = 0;
		gl_composite_1.horizontalSpacing = 0;
		composite_1.setLayout(gl_composite_1);
		
		this.canvasPainter = new ImageCanvasPainter(composite_1);
		Canvas canvas = this.canvasPainter.getCanvas();
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		sashForm.setWeights(new int[] {1, 3});
	}
	
	@Override
	public void organizeUIComponent() {
		// initial actions
		this.gallery.setCanvasPainter(this.canvasPainter);
		// initial Combo values
		this.patientChanged(null, paManager.getCurrentPatient(), null);
	}
	
	@Focus
	public void setFocus() {
		// do nothing
	}

	@Override
	public boolean isEditing() {
		// do nothing, just add and remove so not have editing status
		return false;
	}

	private void showEmptyCasesButton(boolean isShow) {
		gallery.updateFolderEmpty(isShow);
		canvasPainter.clearImages();
	}

	/*
	 * (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientViewPartListener#patientChanged(com.lehanh.pama.patientcase.Patient, com.lehanh.pama.patientcase.Patient)
	 * View all surgery images from new selected patient
	 */
	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		if (newPa != null) {
			ISurgeryImageList allImages = newPa.getMedicalPersonalInfo().getPatientCaseList()
																		   .getSurgeryImageList()
																		   //.getAllImageGroup(this.showGroupFromPA)
																		   ;
			gallery.updateGallery(allImages, callIds);
		} else {
			gallery.updateGallery(null, callIds);
		}
		canvasPainter.clearImages();
	}

	@Override
	public void patientCaseChanged(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase, String[] callIds) {
		if (callIds != null && Arrays.asList(callIds).contains(ID)) {
			return;
		}
		if (newCase == null || newCase.getId() < 0 || rootCase < 0) {
			return;
		}
		gallery.showCase(newCase.getId(), rootCase);
	}
}