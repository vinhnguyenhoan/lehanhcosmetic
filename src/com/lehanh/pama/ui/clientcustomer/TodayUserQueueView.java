package com.lehanh.pama.ui.clientcustomer;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.lehanh.pama.patientcase.AppointmentSchedule;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.util.UIControlUtils;
import com.lehanh.pama.util.PamaHome;

public class TodayUserQueueView implements IPatientViewPartListener/*extends ViewPart*/ {

	private IPatientManager paManager;
	
	public TodayUserQueueView() {
		paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		paManager.addPaListener(this, ID);
	}
	
	public static final String ID = "com.lehanh.pama.todayUserQueueView"; //$NON-NLS-1$
	private CDateTime appDate;
	private AppointmentTable tableViewer;
	
	@PostConstruct
	public void createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginHeight = 0;
		gl_composite_1.verticalSpacing = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		Label lblNgyHn = new Label(composite_1, SWT.NONE);
		lblNgyHn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		lblNgyHn.setText(Messages.TodayUserQueueView_ngayhen);
		
		this.appDate = new CDateTime(composite_1, CDT.BORDER);
		this.appDate.setNullText(Messages.TodayUserQueueView_chonngay);
		this.appDate.setPattern("dd/MM/yyyy"); //$NON-NLS-1$

		GridData gd_dateTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dateTime.widthHint = 90;
		appDate.setLayoutData(gd_dateTime);
		
		Button refreshButton = new Button(composite_1, SWT.NONE);
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		refreshButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		refreshButton.setText(Messages.TodayUserQueueView_lammoi);
		
		this.tableViewer = new AppointmentTable(ID, composite);
		
		appDate.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				search();
			}
		});
		// initial view
		this.appDate.setSelection(GregorianCalendar.getInstance().getTime());
		search();
	}

	private void search() {
		this.appDate.getSelection();
		List<AppointmentSchedule> paList;
		try {
			paList = paManager.getAppointmentManager().searchPatientAppointment(appDate.getSelection());
			this.tableViewer.setInput(paList);
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
			UIControlUtils.openMessageBox(tableViewer.getTable().getShell(), Messages.TodayUserQueueView_loidb, e.getMessage());
		}
	}

	@Focus
	public void setFocus() {
		this.appDate.setFocus();
	}

	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		//this.tableViewer.setInput(null);
		// TODO check resolved
	}

	@Override
	public void patientCaseChanged(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase, String[] callIds) {
		// do nothing
	}
}
