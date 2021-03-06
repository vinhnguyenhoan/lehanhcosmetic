package com.lehanh.pama.ui.patientcase;

import static com.lehanh.pama.ui.util.UIControlUtils.getValueFromCombo;
import static com.lehanh.pama.ui.util.UIControlUtils.initialCombo;
import static com.lehanh.pama.ui.util.UIControlUtils.selectComboById;
import static com.lehanh.pama.ui.util.UIControlUtils.setText;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.nebula.widgets.tablecombo.TableCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.DiagnoseCatagory;
import com.lehanh.pama.catagory.DrCatagory;
import com.lehanh.pama.catagory.PrognosticCatagory;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.catagory.SurgeryCatagory;
import com.lehanh.pama.patientcase.AppointmentSchedule;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.MedicalPersonalInfo;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.patientcase.PatientCaseStatus;
import com.lehanh.pama.ui.PamaFormUI;
import com.lehanh.pama.ui.patientcase.ExamVersionComboViewer.ISelectionDetailChangedListener;
import com.lehanh.pama.ui.util.CatagoryToUITextByDesc;
import com.lehanh.pama.ui.util.FormManager;
import com.lehanh.pama.ui.util.ObjectToUIText;
import com.lehanh.pama.ui.util.PamaResourceManager;
import com.lehanh.pama.ui.util.UIControlUtils;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaHome;

import net.sf.paperclips.DefaultGridLook;
import net.sf.paperclips.GridColumn;
import net.sf.paperclips.GridPrint;
import net.sf.paperclips.PaperClips;
import net.sf.paperclips.Print;
import net.sf.paperclips.PrintJob;
import net.sf.paperclips.TextPrint;
import net.sf.paperclips.ui.PrintPreview;

public class PatientCaseView extends PamaFormUI implements IPatientViewPartListener, IPatientView, ISelectionDetailChangedListener {
	
	public static final String ID = "com.lehanh.pama.patientCaseView"; //$NON-NLS-1$
	private Text smallSurgeryText;
	private Text drAdviceText;
	private Text prognosticOtherText;
	private Text diagnoseOtherText;
	private Text noteFromDrText;
	private Text surgeryNoteText;
	private Text noteFromPaText;
	private Text appNoteText;
	private CCombo drCombo;

	private CDateTime surgeryDateCDate;
	private Button complicationCheckBtn;
	private Button beautyBut;
	
	private Button adviceBtn;
	private Button newCaseBtn;
	private Button reExamBtn;
	private Button updateBtn;
	private Button saveBtn;
	private Button cancelBtn;
	private CDateTime nextAppCDate;
	private CCombo appPurposrCombo;
	private CCombo advCombo;
	private Button printCodePAButton;
	
	private Composite composite;

	private static Color grey;
	
	private ICatagoryManager catManager;
	
	private IPatientManager paManager;
	
	private TableComboViewer serviceTComboViewer;
	private TableComboViewer prognosticTComboViewer;
	private TableComboViewer diagnoseTComboViewer;
	private TableComboViewer surgeryTComboViewer;
	private CCombo surgeryToPrintCombo;

	private ExamVersionComboViewer examCombo;
	private TableComboViewer caseVersionTComboViewer;
	private TableComboViewer examVersionTComboViewer;
	
	//private PatientCaseCatagoryComboViewer surgeryToPrintComboViewer;
	private PatientCaseCatagoryComboViewer surgeryCatComboViewer;
	private PatientCaseCatagoryComboViewer serviceCatComboViewer;
	private PatientCaseCatagoryComboViewer diagnoseCatComboViewer;
	private PatientCaseCatagoryComboViewer prognosticCatComboViewer;
	
	private ObjectToUIText<Patient, Long> patientInfo = new ObjectToUIText<Patient, Long>() {
		
		@Override
		public String showUI(Patient object) {
			if (object == null) {
				return null;
			}
			
			String result = object.getName();
			if (!StringUtils.isEmpty(object.getCellPhone())) {
				result += " SDT - " + object.getCellPhone(); //$NON-NLS-1$
			}
			return result;
		}
		
		@Override
		public Long getIdForUI(Patient object) {
			if (object == null) {
				return null;
			}
			return object.getId();
		}
	};
	

	public PatientCaseView() {
		catManager = (ICatagoryManager) PamaHome.getService(ICatagoryManager.class);
		paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		paManager.addPaListener(this, ID);
	}

	//@PostConstruct
	@Override
	public void createFormUI(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		
		this.composite = new Composite(sc, SWT.NONE);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginHeight = 3;
		gl_composite.verticalSpacing = 3;
		composite.setLayout(gl_composite);
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(3, false);
		gl_composite_1.marginHeight = 3;
		gl_composite_1.verticalSpacing = 3;
		composite_1.setLayout(gl_composite_1);
		
		CLabel lblCho = new CLabel(composite_1, SWT.NONE);
		lblCho.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblCho.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		lblCho.setSize(147, 21);
		lblCho.setText(Messages.PatientCaseView_chonbenhan);
		
		CLabel lblCho2 = new CLabel(composite_1, SWT.NONE);
		lblCho2.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblCho2.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		lblCho2.setSize(147, 21);
		lblCho2.setText(Messages.PatientCaseView_chonlankham);
		
		CLabel lblBcST = new CLabel(composite_1, SWT.NONE);
		lblBcST.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblBcST.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		lblBcST.setSize(78, 21);
		lblBcST.setText(Messages.PatientCaseView_bstuvan);
		
		this.caseVersionTComboViewer = new TableComboViewer(composite_1, SWT.BORDER | SWT.READ_ONLY);
		TableCombo caseVersionTCombo = caseVersionTComboViewer.getTableCombo();
		caseVersionTCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.examVersionTComboViewer = new TableComboViewer(composite_1, SWT.BORDER | SWT.READ_ONLY);
		TableCombo examVersionTCombo = examVersionTComboViewer.getTableCombo();
		examVersionTCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		grey = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		this.examCombo = new ExamVersionComboViewer(caseVersionTComboViewer, examVersionTComboViewer, grey);
		
		this.drCombo = new CCombo(composite_1, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		drCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		drCombo.setSize(174, 21);
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(5, false);
		gl_composite_2.verticalSpacing = 3;
		gl_composite_2.marginHeight = 3;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		
		CLabel lblNewLabel_1 = new CLabel(composite_2, SWT.NONE);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(128, 0, 0));
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD)); //$NON-NLS-1$
		lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 5, 1));
		lblNewLabel_1.setText(Messages.PatientCaseView_thongtintuvanthamkham);
		
		Label label = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		CLabel lblDchVThm = new CLabel(composite_2, SWT.NONE);
		lblDchVThm.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblDchVThm.setBounds(0, 0, 61, 21);
		lblDchVThm.setText(Messages.PatientCaseView_dichvu);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		this.serviceTComboViewer = new TableComboViewer(composite_2, SWT.BORDER | SWT.READ_ONLY);
		TableCombo serviceTCombo = serviceTComboViewer.getTableCombo();
		GridData gd_serviceTCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_serviceTCombo.widthHint = 400;
		serviceTCombo.setLayoutData(gd_serviceTCombo);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		CLabel lblChnon = new CLabel(composite_2, SWT.NONE);
		lblChnon.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblChnon.setText(Messages.PatientCaseView_trieuchung);
		
		CLabel lblKhc = new CLabel(composite_2, SWT.NONE);
		lblKhc.setText(Messages.PatientCaseView_ngoaids);
		lblKhc.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		Label lblGhiChu = new Label(composite_2, SWT.NONE);
		lblGhiChu.setText(Messages.PatientCaseView_ghichutukhach);
		
		Label lblGhiChT = new Label(composite_2, SWT.NONE);
		lblGhiChT.setText(Messages.PatientCaseView_ghichutubacsi);
		new Label(composite_2, SWT.NONE);
		
		this.prognosticTComboViewer = new TableComboViewer(composite_2, SWT.BORDER | SWT.READ_ONLY);
		TableCombo prognosticTCombo = prognosticTComboViewer.getTableCombo();
		GridData gd_prognosticTCombo = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_prognosticTCombo.widthHint = 400;
		prognosticTCombo.setLayoutData(gd_prognosticTCombo);
		
		prognosticOtherText = new Text(composite_2, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_prognosticOtherText = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 2);
		gd_prognosticOtherText.widthHint = 200;
		prognosticOtherText.setLayoutData(gd_prognosticOtherText);
		
		noteFromPaText = new Text(composite_2, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		noteFromPaText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));
		
		noteFromDrText = new Text(composite_2, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		noteFromDrText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 5));
		new Label(composite_2, SWT.NONE);
		
		CLabel label_3 = new CLabel(composite_2, SWT.NONE);
		label_3.setText(Messages.PatientCaseView_chandoan);
		label_3.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		CLabel label_4 = new CLabel(composite_2, SWT.NONE);
		label_4.setText(Messages.PatientCaseView_ngoaids);
		label_4.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL));
		
		this.diagnoseTComboViewer = new TableComboViewer(composite_2, SWT.BORDER | SWT.READ_ONLY);
		TableCombo diagnoseTCombo = diagnoseTComboViewer.getTableCombo();
		GridData gd_diagnoseTCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_diagnoseTCombo.widthHint = 400;
		diagnoseTCombo.setLayoutData(gd_diagnoseTCombo);
		
		diagnoseOtherText = new Text(composite_2, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_diagnoseOtherText = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 2);
		gd_diagnoseOtherText.widthHint = 200;
		diagnoseOtherText.setLayoutData(gd_diagnoseOtherText);
		new Label(composite_2, SWT.NONE);
		
		CLabel lblNewLabel_2 = new CLabel(composite_2, SWT.NONE);
		lblNewLabel_2.setForeground(SWTResourceManager.getColor(128, 0, 0));
		lblNewLabel_2.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.BOLD)); //$NON-NLS-1$
		lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 5, 1));
		lblNewLabel_2.setText(Messages.PatientCaseView_thongtinphauthuat);
		
		Label label_1 = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 5, 1));
		
		CLabel lblPhuThut = new CLabel(composite_2, SWT.NONE);
		lblPhuThut.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblPhuThut.setBounds(0, 0, 61, 21);
		lblPhuThut.setText(Messages.PatientCaseView_phauthuat);
		
		Label lblGhiCh = new Label(composite_2, SWT.NONE);
		lblGhiCh.setText(Messages.PatientCaseView_ghichu);
		
		CLabel lblThThut = new CLabel(composite_2, SWT.NONE);
		lblThThut.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblThThut.setText(Messages.PatientCaseView_thuthuat);
		
		CLabel lblLiKhuynCa = new CLabel(composite_2, SWT.NONE);
		lblLiKhuynCa.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblLiKhuynCa.setText(Messages.PatientCaseView_loikhuyencuabs);
		
		this.advCombo = new CCombo(composite_2, SWT.BORDER | SWT.READ_ONLY);
		advCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.surgeryTComboViewer = new TableComboViewer(composite_2, SWT.BORDER | SWT.READ_ONLY);
		TableCombo surgeryTCombo = surgeryTComboViewer.getTableCombo();
		GridData gd_surgeryTCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_surgeryTCombo.widthHint = 400;
		surgeryTCombo.setLayoutData(gd_surgeryTCombo);
		
		surgeryNoteText = new Text(composite_2, SWT.BORDER);
		GridData gd_surgeryNoteText = new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 3);
		gd_surgeryNoteText.widthHint = 200;
		surgeryNoteText.setLayoutData(gd_surgeryNoteText);
		
		smallSurgeryText = new Text(composite_2, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_smallSurgeryText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3);
		gd_smallSurgeryText.heightHint = 80;
		smallSurgeryText.setLayoutData(gd_smallSurgeryText);
		
		drAdviceText = new Text(composite_2, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		drAdviceText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 3));
		
		Composite composite_3 = new Composite(composite_2, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(4, false);
		gl_composite_3.horizontalSpacing = 0;
		gl_composite_3.marginWidth = 0;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 3));
		
		this.complicationCheckBtn = new Button(composite_3, SWT.CHECK);
		complicationCheckBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		complicationCheckBtn.setText(Messages.PatientCaseView_bienchung);
		
		this.beautyBut = new Button(composite_3, SWT.CHECK);
		beautyBut.setText(Messages.PatientCaseView_hailong);
		new Label(composite_3, SWT.NONE);
		new Label(composite_3, SWT.NONE);
		
		Label lblNgyPt = new Label(composite_3, SWT.NONE);
		lblNgyPt.setText(Messages.PatientCaseView_ngaypt);
		
		this.surgeryDateCDate = new CDateTime(composite_3, CDT.BORDER | CDT.SPINNER);
		GridData gd_surgeryDateCDate = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_surgeryDateCDate.widthHint = 90;
		surgeryDateCDate.setLayoutData(gd_surgeryDateCDate);
		surgeryDateCDate.setNullText(Messages.PatientCaseView_ngaymo);
		surgeryDateCDate.setPattern("dd/MM/yyyy"); //$NON-NLS-1$
		
		this.surgeryToPrintCombo = new CCombo(composite_3, SWT.BORDER | SWT.READ_ONLY);
		surgeryToPrintCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.printCodePAButton = new Button(composite_3, SWT.NONE);
		printCodePAButton.setText(Messages.PatientCaseView_inphieu);
		
		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		GridLayout gl_composite_4 = new GridLayout(1, false);
		gl_composite_4.verticalSpacing = 0;
		gl_composite_4.marginWidth = 0;
		gl_composite_4.marginHeight = 0;
		composite_4.setLayout(gl_composite_4);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);
		
		Label label_2 = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		
		Composite composite_6 = new Composite(composite, SWT.NONE);
		composite_6.setLayout(new GridLayout(11, false));
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, true, 1, 1));
		
		Label lblNgyHn = new Label(composite_6, SWT.NONE);
		lblNgyHn.setText(Messages.PatientCaseView_ngayhen);
		
		this.nextAppCDate = new CDateTime(composite_6, CDT.BORDER | CDT.SPINNER);
		GridData gd_nextAppCDate = new GridData(SWT.LEFT, SWT.CENTER, false, false, 10, 1);
		gd_nextAppCDate.widthHint = 90;
		nextAppCDate.setLayoutData(gd_nextAppCDate);
		nextAppCDate.setNullText(Messages.PatientCaseView_ngayhen);
		nextAppCDate.setPattern("dd/MM/yyyy"); //$NON-NLS-1$
		
		Label label_5 = new Label(composite_6, SWT.NONE);
		label_5.setText(Messages.PatientCaseView_de);
		
		this.appPurposrCombo = new CCombo(composite_6, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		appPurposrCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblGhiCh_1 = new Label(composite_6, SWT.NONE);
		lblGhiCh_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblGhiCh_1.setText(Messages.PatientCaseView_ghichu);
		
		appNoteText = new Text(composite_6, SWT.BORDER);
		appNoteText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.adviceBtn = new Button(composite_6, SWT.NONE);
		adviceBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				advice();
			}
		});
		adviceBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		adviceBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));
		adviceBtn.setText(Messages.PatientCaseView_tuvan);
		
		this.reExamBtn = new Button(composite_6, SWT.NONE);
		reExamBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reExam();
			}
		});
		reExamBtn.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		reExamBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		reExamBtn.setText(Messages.PatientCaseView_thamkham);
		
		this.newCaseBtn = new Button(composite_6, SWT.NONE);
		newCaseBtn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		newCaseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				newCase();
			}
		});
		newCaseBtn.setText(Messages.PatientCaseView_benhanmoi);
		newCaseBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		this.updateBtn = new Button(composite_6, SWT.NONE);
		updateBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				update();
			}
		});
		updateBtn.setText(Messages.PatientCaseView_chinhsua);
		updateBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		this.saveBtn = new Button(composite_6, SWT.NONE);
		saveBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});
		saveBtn.setText(Messages.PatientCaseView_luu);
		saveBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		this.cancelBtn = new Button(composite_6, SWT.NONE);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancel();
			}
		});
		cancelBtn.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
		cancelBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		cancelBtn.setText(Messages.PatientCaseView_huy);

		/*
	     * Set the absolute size of the child child.setSize(400, 400);
	     */
	    // Set the child as the scrolled content of the ScrolledComposite
	    sc.setContent(composite);

	    // Set the minimum size
	    sc.setMinSize(composite.getSize().x, composite.getSize().y);

	    // Expand both horizontally and vertically
	    sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	    sc.layout();
	    
	    sc.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private static final TreeMap<Integer, RelComponentElement> allRelEle = new TreeMap<>(); 
	private static int idGenerator = 1;
	
	private RelComponentElement COM_NEW_EXAM;
	private RelComponentElement COM_NEW_CASE;
	private RelComponentElement COM_EDIT;
	private RelComponentElement COM_SAVE;
	private RelComponentElement COM_CANCEL;
	private RelComponentElement COM_SELECT_CASE;
	private RelComponentElement COM_SELECT_DETAIL;
	private RelComponentElement COM_FORM;

	private class RelComponentElement {
		
		private final int id;
		
		private Control[] controls;
	
		private Map<Integer, Function<RelComponentElement, Boolean>> enbOtherRelStatus = new HashMap<>();
		
		private RelComponentElement(int id, Control... controls) {
			this.id = id;
			this.controls = controls;
			allRelEle.put(id, this);
		}
		
		private RelComponentElement enb(Integer id, Function<RelComponentElement, Boolean> func) {
			enbOtherRelStatus.put(id, func);
			return this;
		}
		
		private void enable(boolean isEnable) {
			if (controls == null) {
				return;
			}
			for (Control control : controls) {
				Boolean isReadOnly = (Boolean) control.getData("READ_ONLY");
				if (control instanceof Text && !Boolean.TRUE.equals(isReadOnly)) {
					// Ignore if read only style control
					((Text) control).setEditable(isEnable);
				} else if (control instanceof CCombo) {
					CCombo ccombo = (CCombo) control;
					ccombo.setListVisible(isEnable);
					ccombo.setEnabled(isEnable);
				} else if (control instanceof Combo) {
					Combo combo = (Combo) control;
					combo.setListVisible(isEnable);
					combo.setEnabled(isEnable);
				} else if (control instanceof TableCombo) {
					TableCombo tC = (TableCombo) control;
					tC.setEnabled(isEnable);
					//tC.setTableVisible(editable);
				} else {
					control.setEnabled(isEnable);
				}
			}
		}
		
		private void updateEnableRelView() {
			for (Entry<Integer, Function<RelComponentElement, Boolean>> entry : enbOtherRelStatus.entrySet()) {
				int uiId = entry.getKey();
				RelComponentElement relCom = allRelEle.get(uiId);
				relCom.enable(entry.getValue().apply(relCom));
			}
		}
	}
	
	/*
		TƯ vấn	Thăm khám	Benh án mới	Chinh sua	Luu	Huy	Chon benh an	Lan kham
		TƯ vấn	dis	dis	enb	dis	enb	enb	---	enb
		Thăm khám	dis	dis	enb	dis	enb	enb	---	enb
		Benh án mới	dis	dis	dis	dis	enb	enb	---	enb
		Chinh sua	dis	dis	dis	dis	enb if view current	enb if view current	---	enb
		Luu	dis	dis	dis	end	dis	dis	---	dis
		Huy	dis	dis	dis	end	dis	dis	---	dis
		Chon benh an	dis	dis	enb	dis	enb	enb	---	enb
		Lan kham	dis	dis	enb	dis	enb	enb	enb + upd + Lan kham	enb
		form	enb	enb	dis	enb	dis	dis	---	dis + view
	*/
	//	COM_NEW_EXAM, COM_NEW_CASE, COM_EDIT, COM_SAVE, COM_CANCEL, COM_SELECT_CASE, COM_SELECT_DETAIL
	private static final Function<RelComponentElement, Boolean> ena = f -> true;
	private static final Function<RelComponentElement, Boolean> dis = f -> false;

	@Override
	public void organizeUIComponent() {
		getFormManager()
						.addCreateButtons(newCaseBtn, adviceBtn, reExamBtn)
							.addEditButtons(updateBtn).addSaveButtons(saveBtn).addCancelButtons(cancelBtn)
							
						.addAllControlFromComposite(composite, true, true, examCombo.getTableCombos())
						.setEditableAll(false)
						.cancel(paManager.getCurrentPatient() != null)
		// TODO fix this -> disable all button at first
		//.setEnableAllButtons(false)
		;

		COM_NEW_EXAM = new RelComponentElement(idGenerator++, this.adviceBtn, this.reExamBtn);
		COM_NEW_CASE = new RelComponentElement(idGenerator++, this.newCaseBtn);
		COM_EDIT = new RelComponentElement(idGenerator++, this.updateBtn);
		COM_SAVE = new RelComponentElement(idGenerator++, this.saveBtn);
		COM_CANCEL = new RelComponentElement(idGenerator++, this.cancelBtn);
		COM_SELECT_CASE = new RelComponentElement(idGenerator++, caseVersionTComboViewer.getTableCombo());
		COM_SELECT_DETAIL = new RelComponentElement(idGenerator++, examVersionTComboViewer.getTableCombo());
		
		List<Control> allControlForm = getFormManager().getAllControls();
		COM_FORM = new RelComponentElement(idGenerator++, allControlForm.toArray(new Control[allControlForm.size()]));
		
		COM_NEW_EXAM.enb(COM_NEW_EXAM.id, dis)
					.enb(COM_NEW_CASE.id, dis)
					.enb(COM_EDIT.id, dis)
					.enb(COM_SAVE.id, ena)
					.enb(COM_CANCEL.id, ena)
					.enb(COM_SELECT_CASE.id, dis)
					.enb(COM_SELECT_DETAIL.id, dis)
					.enb(COM_FORM.id, ena);
		COM_NEW_CASE.enb(COM_NEW_EXAM.id, ena)
					.enb(COM_NEW_CASE.id, dis)
					.enb(COM_EDIT.id, dis)
					.enb(COM_SAVE.id, dis)
					.enb(COM_CANCEL.id, dis)
					.enb(COM_SELECT_CASE.id, ena)
					.enb(COM_SELECT_DETAIL.id, ena)
					.enb(COM_FORM.id, dis);
		COM_EDIT.enb(COM_NEW_EXAM.id, dis)
					.enb(COM_NEW_CASE.id, dis)
					.enb(COM_EDIT.id, dis)
					.enb(COM_SAVE.id, ena)
					.enb(COM_CANCEL.id, ena)
					.enb(COM_SELECT_CASE.id, dis)
					.enb(COM_SELECT_DETAIL.id, dis)
					.enb(COM_FORM.id, ena);
		COM_SAVE.enb(COM_NEW_EXAM.id, ena)
					.enb(COM_NEW_CASE.id, ena)
					.enb(COM_EDIT.id, p -> paManager.getCurrentPatient() != null)
					.enb(COM_SAVE.id, dis)
					.enb(COM_CANCEL.id, dis)
					.enb(COM_SELECT_CASE.id, ena)
					.enb(COM_SELECT_DETAIL.id, ena)
					.enb(COM_FORM.id, dis);
		COM_CANCEL.enb(COM_NEW_EXAM.id, ena)
					.enb(COM_NEW_CASE.id, ena)
					.enb(COM_EDIT.id, p -> paManager.getCurrentPatient() != null)
					.enb(COM_SAVE.id, dis)
					.enb(COM_CANCEL.id, dis)
					.enb(COM_SELECT_CASE.id, ena)
					.enb(COM_SELECT_DETAIL.id, ena)
					.enb(COM_FORM.id, dis);
		COM_FORM.enb(COM_NEW_EXAM.id, ena)
					.enb(COM_NEW_CASE.id, ena)
					.enb(COM_EDIT.id, ena)
					.enb(COM_SAVE.id, dis)
					.enb(COM_CANCEL.id, dis)
					.enb(COM_SELECT_CASE.id, ena)
					.enb(COM_SELECT_DETAIL.id, ena)
					.enb(COM_FORM.id, dis);
		
		printCodePAButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					printCodePA();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				try {
					printCodePA();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		// initial versions
		examCombo.addSelectionDetailChangedListener(this);

		// initial Combo values
		CatagoryToUITextByDesc catToDesc = new CatagoryToUITextByDesc();
		initialCombo(drCombo, catManager.getCatagoryByType(CatagoryType.DR).values(), Messages.PatientCaseView_chonbs, 0, catToDesc);
		initialCombo(appPurposrCombo, catManager.getCatagoryByType(CatagoryType.APPOINTMENT).values(), Messages.PatientCaseView_chon, 0, catToDesc);
		initialCombo(advCombo, catManager.getCatagoryByType(CatagoryType.ADVICE).values(), null, 0, catToDesc);
		advCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAdvice(advCombo.getText());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selectAdvice(advCombo.getText());
			}
		});
		
		//this.surgeryToPrintComboViewer = new PatientCaseCatagoryComboViewer(false, catManager, grey, surgeryToPrintCombo, CatagoryType.SURGERY);
		this.surgeryTComboViewer.getTableCombo().getTextControl().addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				initialCombo(surgeryToPrintCombo, surgeryCatComboViewer.getMultiSelectionCatList(), null, 0, catToDesc);
			}
		});
		this.surgeryCatComboViewer = new PatientCaseCatagoryComboViewer(catManager, grey, surgeryTComboViewer, CatagoryType.SURGERY);
		this.diagnoseCatComboViewer = new PatientCaseCatagoryComboViewer(catManager, grey, diagnoseTComboViewer, CatagoryType.DIAGNOSE, diagnoseOtherText);
		this.prognosticCatComboViewer = new PatientCaseCatagoryComboViewer(catManager, grey, prognosticTComboViewer, CatagoryType.PROGNOSTIC
				, prognosticOtherText
				//, diagnoseCatComboViewer
				//, surgeryCatComboViewer
		);
		this.serviceCatComboViewer = new PatientCaseCatagoryComboViewer(catManager, true, grey, serviceTComboViewer, CatagoryType.SERVICE
				, prognosticCatComboViewer, diagnoseCatComboViewer, surgeryCatComboViewer
		);
		
		// initial view
		viewPatientInfo(paManager.getCurrentPatient());
	}

	private void printCodePA() throws ParseException {
		final Patient currPa = paManager.getCurrentPatient();
		final SurgeryCatagory selectedSC = (SurgeryCatagory) UIControlUtils.getValueFromCombo(surgeryToPrintCombo);
		final PatientCaseEntity detailExam = this.examCombo.getSelectedDetailEntity();
		final int rootId = this.examCombo.getSelectedRootId();
		
		if (currPa == null || currPa.getId() == null || selectedSC == null || rootId < 0 || detailExam == null) {
			return;
		}
		Display display = composite.getDisplay();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("PatientCaseView.java"); //$NON-NLS-1$
		shell.setLayout(new GridLayout());
		shell.setSize(600, 800);

		final PrintJob job = new PrintJob("PatientId", //$NON-NLS-1$
				createPrint(currPa.getId(), selectedSC.getSymbol(), DateUtils.convertDateDataType(GregorianCalendar.getInstance()), 
						paManager.getCurrentPatient().getMedicalPersonalInfo().getPatientCaseList().calculateDateAfterRoot(rootId, detailExam.getId())));

		Composite buttonPanel = new Composite(shell, SWT.NONE);
		buttonPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buttonPanel.setLayout(new RowLayout(SWT.HORIZONTAL));

		final PrintPreview preview = new PrintPreview(shell, SWT.BORDER);

		Button print = new Button(buttonPanel, SWT.PUSH);
		print.setText("Print"); //$NON-NLS-1$
		print.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				PaperClips.print(job, new PrinterData());
			}
		});

		preview.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));preview.setFitHorizontal(true);preview.setFitVertical(true);
		preview.setPrintJob(job);

		shell.open();
	}

	private Print createPrint(Long id, String symbol, String date, int calculateDateAfterRoot) {
		GridPrint grid = new GridPrint(new DefaultGridLook(5, 5));
		grid.addColumn(new GridColumn(SWT.LEFT, SWT.DEFAULT, 10));
		grid.addColumn(new GridColumn(SWT.RIGHT, SWT.DEFAULT, 10));

		final FontData fontData = PamaResourceManager.getFont("Arial", 20, SWT.BOLD).getFontData()[0]; //$NON-NLS-1$
		grid.add(SWT.LEFT, new TextPrint(calculateDateAfterRoot == 0 ? "B" : "A" + calculateDateAfterRoot + "day", fontData));
		grid.add(SWT.RIGHT, new TextPrint(date, fontData));

		final FontData bFontData = PamaResourceManager.getFont("Arial", 50, SWT.BOLD).getFontData()[0]; //$NON-NLS-1$
		grid.add(SWT.CENTER, new TextPrint(String.valueOf(id), bFontData), GridPrint.REMAINDER);

		grid.add(SWT.CENTER, new TextPrint(symbol, fontData), GridPrint.REMAINDER);

		return grid;
	}

	private void selectAdvice(String text) {
		text = "- " + text;
		String currText = this.drAdviceText.getText();
		if (StringUtils.isBlank(currText)) {
			currText = text;
		} else {
			currText += System.lineSeparator() + text;
		}
		this.drAdviceText.setText(currText);
	}

	/* (non-Javadoc)
	 * @see com.lehanh.pama.ui.patientcase.ExamVersionComboViewer.ISelectionDetailChangedListener#selectionChanged(com.lehanh.pama.patientcase.PatientCaseEntity)
	 */
	@Override
	public void viewDetailCase(PatientCaseEntity model) {
		// examCombo tables are ignored so must disable when cancel
		if (model == null/* || examCombo.getSelectedEntity() == model*/) {
			//cancelForm(false);
		} else {
			// after cancel then enable again to selectable versions list
			//cancelForm(true);
			
			//this.examCombo.selectionChanged(model);
			selectServiceCatagory(model);
			
			selectComboById(drCombo, model.getDrId());
			setText(prognosticOtherText, model.getPrognosticOther());
			setText(diagnoseOtherText, model.getDiagnoseOther());
			setText(noteFromPaText, model.getNoteFromClient());
			setText(noteFromDrText, model.getNoteFromDr());
			setText(surgeryNoteText, model.getSurgeryNote());
			try {
				surgeryDateCDate.setSelection(model.getSurgeryDate());
			} catch (ParseException e) {
				// ignore
				e.printStackTrace();
			}
			complicationCheckBtn.setSelection(model.isComplication());
			beautyBut.setSelection(model.isBeautiful());
			setText(smallSurgeryText, model.getSmallSurgery());
			setText(drAdviceText, model.getAdviceFromDr());
			
			viewAppointmentSchedule(model.getAppoSchedule());
		}
	
		paManager.selectDetailPatientCase(ID, model, examCombo.getSelectedRootId());
		
		this.COM_FORM.updateEnableRelView();
	}
	
	/*private void cancelForm(boolean isAllowEdit) {
		getFormManager().cancel(isAllowEdit)
						.setEditable(isAllowEdit, examCombo.getTableCombos());
	}*/

	private void selectServiceCatagory(PatientCaseEntity model) {
		// Remain order of selection
		serviceCatComboViewer.selectionChanged(model.getServiceNames());
		prognosticCatComboViewer.selectionChanged(model.getPrognosticCatagoryNames());
		diagnoseCatComboViewer.selectionChanged(model.getDiagnoseCatagoryNames());
		surgeryCatComboViewer.selectionChanged(model.getSurgeryCatagoryNames());
	}

	private void viewAppointmentSchedule(AppointmentSchedule appoSchedule) {
		if (appoSchedule == null) {
			return; // sure that appointment controls cleared date before
		}
		nextAppCDate.setSelection(appoSchedule.getAppointmentDate());
		selectComboById(appPurposrCombo, appoSchedule.getAppointmentCatagory() == null ? null : appoSchedule.getAppointmentCatagory().getId());
		setText(appNoteText, appoSchedule.getNote());
	}
	
	private void viewPatientInfo(Patient patient) {
		if (patient == null) {
			return;
		}
		
		MedicalPersonalInfo mInfo = patient.getMedicalPersonalInfo();
		examCombo.setInput(mInfo.getPatientCaseList());
	}
	
	private void cancel() {
		paManager.cancelEditingPatientCase();
		// show again patient info as a viewing status
		viewPatientInfo(paManager.getCurrentPatient());
		this.COM_CANCEL.updateEnableRelView();
	}

	@SuppressWarnings("unchecked")
	private void save() {
		try {
			paManager.updatePatientCase(ID, this.examCombo.getSelectedRootId(), this.examCombo.getSelectedDetailEntity(),
										(DrCatagory) getValueFromCombo(this.drCombo),
										(List<ServiceCatagory>) this.serviceCatComboViewer.getMultiSelectionCatList(),
										(List<PrognosticCatagory>) this.prognosticCatComboViewer.getMultiSelectionCatList(),
										this.prognosticOtherText.getText(),
										(List<DiagnoseCatagory>) this.diagnoseCatComboViewer.getMultiSelectionCatList(),
										this.diagnoseOtherText.getText(),
										this.noteFromPaText.getText(),
										this.noteFromDrText.getText(),
										(List<SurgeryCatagory>) this.surgeryCatComboViewer.getMultiSelectionCatList(),
										this.surgeryNoteText.getText(),
										this.surgeryDateCDate.getSelection(),
										this.complicationCheckBtn.getSelection(),
										this.beautyBut.getSelection(),
										this.smallSurgeryText.getText(),
										this.drAdviceText.getText(),
										this.nextAppCDate.getSelection(),
										(AppointmentCatagory) getValueFromCombo(this.appPurposrCombo),
										this.appNoteText.getText(), this.patientInfo );
			
		} catch (InvalidParameterException e) {
			MessageBox dialog = 
				  new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText(Messages.PatientCaseView_loinhaplieu);
			dialog.setMessage(e.getMessage());
			dialog.open();
			return;
		}
		
		//getFormManager().saved(paManager.getCurrentPatient() != null);
		this.COM_SAVE.updateEnableRelView();
	}

	private void update() {
		// do nothing for data
		//getFormManager().edit();
		this.COM_EDIT.updateEnableRelView();
		//return getFormManager();
	}

	private void newCase() {
		// do nothing for data
		newAction(null);
		this.COM_NEW_CASE.updateEnableRelView();;
	}
	
	private void reExam() {
		// do nothing for data
		newAction(PatientCaseStatus.EXAM);
		this.COM_NEW_EXAM.updateEnableRelView();
	}

	private void advice() {
		// do nothing for data
		newAction(PatientCaseStatus.CONSULT);
		this.COM_NEW_EXAM.updateEnableRelView();
	}

	private void newAction(PatientCaseStatus status) {
		if (status != null) {
			if (this.examCombo.getDetailInput() == null) {
				FormManager.showMessage(this.composite.getShell(), "Hướng dẩn", "Tạo bệnh án mới !");
				return;
			}
			this.examCombo.getDetailInput().createDetailCase(status);
		} else {
			if (this.examCombo.getInput() == null) {
				FormManager.showMessage(this.composite.getShell(), "Hướng dẩn", "Chọn bệnh nhân trước khi tạo bệnh án !");
				return;
			}
			this.examCombo.getInput().createRootCase();
		}
		// view patient and view lasest exam is creating exam case
		viewPatientInfo(this.paManager.getCurrentPatient());
		
		if (status != null) {
			// switch form to editing -> enable all controls
			// make sure disable exam combo
			//update().setEditable(false, examCombo.getTableCombos());
		} else {
			//cancelForm(true);
		}
	}

	@Focus
	public void setFocus() {
		//viewData(paManager.getCurrentPatient());
	}

	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		viewPatientInfo(newPa);
	}

	@Override
	public boolean isEditing() {
		return this.saveBtn.getEnabled();
	}

	@Override
	public void patientCaseChanged(PatientCaseEntity oldCase,
			PatientCaseEntity newCase, int rootCase, String[] callIds) {
		// do nothing
	}

}