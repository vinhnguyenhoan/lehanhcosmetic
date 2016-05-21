package com.lehanh.pama.ui.clientcustomer;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
//import org.eclipse.ui.part.ViewPart;

import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.util.UIControlUtils;
import com.lehanh.pama.util.PamaHome;
import org.eclipse.nebula.widgets.cdatetime.CDT;

public class UserSearchView implements IPatientViewPartListener /*extends ViewPart*/ {

	public static final String ID = "com.lehanh.pama.userSearchView"; //$NON-NLS-1$
	private Text name_text;
	private Text phone_text;
	private PatientTable tableViewer;
	private CDateTime calendarCombo;
	//private CCombo date_filter_combo;
	
	private IPatientManager paManager;
	
	public UserSearchView() {
		paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		paManager.addPaListener(this, ID);
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		Composite content_com = new Composite(parent, SWT.NONE);
		content_com.setBounds(0, 0, 662, 298);
		content_com.setLayout(new GridLayout(1, false));
		
		Composite filter_composite = new Composite(content_com, SWT.BORDER);
		filter_composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		filter_composite.setLayout(new GridLayout(8, false));
		
		Label lblHTn = new Label(filter_composite, SWT.NONE);
		lblHTn.setText(Messages.UserSearchView_hoten);
		
		name_text = new Text(filter_composite, SWT.BORDER);
		name_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSinThoi = new Label(filter_composite, SWT.NONE);
		lblSinThoi.setText(Messages.UserSearchView_sodt);
		
		phone_text = new Text(filter_composite, SWT.BORDER);
		phone_text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label date_filter_label = new Label(filter_composite, SWT.NONE);
		date_filter_label.setText(Messages.UserSearchView_ngaygannhat);
		// this.date_filter_combo = new CCombo(filter_composite, SWT.BORDER);
		// date_filter_combo.setListVisible(true);
		// date_filter_combo.setItems(new String[]{"Ng\u00E0y t\u01B0 v\u1EA5n", "Ng\u00E0y ph\u1EA9u thu\u1EADt", "Ng\u00E0y t\u00E1i kh\u00E1m"});
		// date_filter_combo.setEditable(false);
		// date_filter_combo.select(0);
		
		this.calendarCombo = new CDateTime(filter_composite, CDT.BORDER);
		this.calendarCombo.setNullText(Messages.UserSearchView_nhapngay);
		this.calendarCombo.setPattern("dd/MM/yyyy"); //$NON-NLS-1$
		
		GridData gd_calendarCombo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_calendarCombo.widthHint = 70;
		calendarCombo.setLayoutData(gd_calendarCombo);
		
		Button btnTm = new Button(filter_composite, SWT.FLAT);
		btnTm.setText(Messages.UserSearchView_timkiem);

		SelectionAdapter searchSelect = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				search();
			}

		};
		btnTm.addSelectionListener(searchSelect);
		name_text.addSelectionListener(searchSelect);
		phone_text.addSelectionListener(searchSelect);
		calendarCombo.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				search();
			}

		});
		
		Button btnClear = new Button(filter_composite, SWT.FLAT);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearFilter();
			}
		});
		btnClear.setText(Messages.UserSearchView_xoaboloc);
		
		this.tableViewer = new PatientTable(ID, content_com);
		
		filter_composite.setFocus();
	}

	private void clearFilter() {
		calendarCombo.setSelection(null);
		name_text.setText(StringUtils.EMPTY);
		phone_text.setText(StringUtils.EMPTY);
	}

	private void search() {
		List<Patient> paList;
		try {
			paList = paManager.getPatientSearcher().searchPatient(calendarCombo.getSelection(), name_text.getText(), phone_text.getText());
			this.tableViewer.setInput(paList);
		} catch (SQLException e) {
			e.printStackTrace();
			UIControlUtils.openMessageBox(tableViewer.getTable().getShell(), Messages.UserSearchView_loidb, e.getMessage());
		} catch (ParseException e) {
			UIControlUtils.openMessageBox(tableViewer.getTable().getShell(), Messages.UserSearchView_loidb, e.getMessage());
		}
	}

	@Focus
	public void setFocus() {
		name_text.setFocus();
	}

	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		tableViewer.setInput(null);
	}

	@Override
	public void patientCaseChanged(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase, String[] callIds) {
		// do nothing		
	}
}
