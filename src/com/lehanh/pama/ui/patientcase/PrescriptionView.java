package com.lehanh.pama.ui.patientcase;

import static com.lehanh.pama.ui.util.UIControlUtils.getTextAsInt;
import static com.lehanh.pama.ui.util.UIControlUtils.getValueFromCombo;
import static com.lehanh.pama.ui.util.UIControlUtils.initialCombo;
import static com.lehanh.pama.ui.util.UIControlUtils.revert;
import static com.lehanh.pama.ui.util.UIControlUtils.selectComboOrSetTextByName;
import static com.lehanh.pama.ui.util.UIControlUtils.setText;

import java.sql.SQLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.DrugCatagory;
import com.lehanh.pama.catagory.PrescriptionCatagory;
import com.lehanh.pama.catagory.PrescriptionItem;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.PamaFormUI;
import com.lehanh.pama.ui.util.CatagoryToUITextByDesc;
import com.lehanh.pama.ui.util.ObjectToUIText;
import com.lehanh.pama.ui.util.PamaResourceManager;
import com.lehanh.pama.ui.util.UIControlUtils;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaException;
import com.lehanh.pama.util.PamaHome;

import net.sf.paperclips.DefaultGridLook;
import net.sf.paperclips.GridColumn;
import net.sf.paperclips.GridPrint;
import net.sf.paperclips.LinePrint;
import net.sf.paperclips.PaperClips;
import net.sf.paperclips.Print;
import net.sf.paperclips.PrintJob;
import net.sf.paperclips.TextPrint;
import net.sf.paperclips.ui.PrintPreview;

public class PrescriptionView extends PamaFormUI implements IPatientViewPartListener, IPatientView, ISelectionChangedListener {
	
	private CCombo sampleCombo;
	private Button saveAsTemplateBtn;
	//private Button deleteTemplateBtn;
	private PrescriptionTable tableViewer;
	private CCombo surgeryNameCombo;
	private Text otherDrugText;
	private Text numberDayText;
	private Label unitLabel;
	private Text unitPerText;
	private Text useNumSSPerDayText;
	private Text numberPerUseText;
	private Label totalNoteLabel;
	private Text drugOriginalText;
	private CCombo useCombo;
	private CCombo sessionCombo;
	private CCombo noteCombo;
	private Button updateLineBtn;
	private Button deleteLineBtn;
	private CDateTime printDateSelecter;
	private Button printBtn;
	private Button addNewBtn;
	// private Button editButton;
	private Button saveBtn;
	private Button cancelBtn;

	private ICatagoryManager catManager;
	
	private IPatientManager paManager;
	
	private Composite composite;
	private Composite buttonComposite;
	private PrescriptionCatagory preCat;
	private PatientCaseEntity currPaCase;
	private final CatagoryToUITextByDesc catToUIByDesc = new CatagoryToUITextByDesc();
	
	@SuppressWarnings("rawtypes")
	private final ObjectToUIText catToUIByName = new ObjectToUIText() {
 
		@Override
		public String showUI(Object object) {
			return ((Catagory) object).getName();
		}

		@Override
		public Object getIdForUI(Object object) {
			return ((Catagory) object).getId();
		}
	};
	
	public PrescriptionView() {
		catManager = (ICatagoryManager) PamaHome.getService(ICatagoryManager.class);
		paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		paManager.addPaListener(this, ID);
	}

	public static final String ID = "com.lehanh.pama.prescriptionView"; //$NON-NLS-1$
	
	@Override
	public void createFormUI(Composite parent) {
		this.composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayout(new GridLayout(3, false));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setText(Messages.PrescriptionView_toathuocmau);
		
		this.sampleCombo = new CCombo(composite_1, SWT.BORDER);
		GridData gd_sampleCombo = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_sampleCombo.widthHint = 350;
		sampleCombo.setLayoutData(gd_sampleCombo);
		
		this.saveAsTemplateBtn = new Button(composite_1, SWT.NONE);
		saveAsTemplateBtn.setText(Messages.PrescriptionView_luulamtoamau);
		saveAsTemplateBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveAsTemplate();
			}
		});

		// this.deleteTemplateBtn = new Button(composite_1, SWT.NONE);
		// deleteTemplateBtn.setText("Xóa toa mẫu");
		// deleteTemplateBtn.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// deleteTemplate();
		// }
		// });
		
		this.tableViewer = new PrescriptionTable(composite_1);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		
		Composite composite_2 = new Composite(composite, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		CTabFolder tabFolder_1 = new CTabFolder(composite_2, SWT.BORDER | SWT.BOTTOM);
		tabFolder_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tabFolder_1.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		
		CTabItem tbtmChiTit = new CTabItem(tabFolder_1, SWT.NONE);
		tbtmChiTit.setText(Messages.PrescriptionView_chitiet);
		
		Composite composite_4 = new Composite(tabFolder_1, SWT.NONE);
		tbtmChiTit.setControl(composite_4);
		composite_4.setLayout(new GridLayout(4, false));
		tabFolder_1.setSelection(0);
		
		Composite composite_5 = new Composite(composite_4, SWT.NONE);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		composite_5.setLayout(new GridLayout(2, false));
		
		Label lblTnThuc = new Label(composite_5, SWT.NONE);
		lblTnThuc.setText(Messages.PrescriptionView_tenthuoc);
		
		this.surgeryNameCombo = new CCombo(composite_5, SWT.BORDER);
		surgeryNameCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblThucKhc = new Label(composite_5, SWT.NONE);
		lblThucKhc.setText(Messages.PrescriptionView_thuockhac);
		
		this.otherDrugText = new Text(composite_5, SWT.BORDER);
		otherDrugText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpCchDng = new Group(composite_4, SWT.NONE);
		grpCchDng.setLayout(new GridLayout(2, false));
		grpCchDng.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpCchDng.setText(Messages.PrescriptionView_huongdan);
		
		Label lblCchDng = new Label(grpCchDng, SWT.NONE);
		lblCchDng.setText(Messages.PrescriptionView_cachdung);
		
		this.useCombo = new CCombo(grpCchDng, SWT.BORDER);
		useCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCUng = new Label(grpCchDng, SWT.NONE);
		lblCUng.setText(Messages.PrescriptionView_cuuong);
		
		this.sessionCombo = new CCombo(grpCchDng, SWT.BORDER);
		sessionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblLu = new Label(grpCchDng, SWT.NONE);
		lblLu.setText(Messages.PrescriptionView_luuy);
		
		this.noteCombo = new CCombo(grpCchDng, SWT.BORDER);
		noteCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Group grpSLng = new Group(composite_4, SWT.NONE);
		grpSLng.setLayout(new GridLayout(5, false));
		grpSLng.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		grpSLng.setText(Messages.PrescriptionView_soluong);
		
		Label lblSNgy = new Label(grpSLng, SWT.NONE);
		lblSNgy.setText(Messages.PrescriptionView_songay);
		
		this.numberDayText = new Text(grpSLng, SWT.BORDER);
		numberDayText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblunitLabel = new Label(grpSLng, SWT.NONE);
		//unitLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		lblunitLabel.setText(Messages.PrescriptionView_dongvi);
		
		this.unitLabel = new Label(grpSLng, SWT.NONE);
		new Label(grpSLng, SWT.NONE);
		//unitLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
		
		Label lblNgyDng = new Label(grpSLng, SWT.NONE);
		lblNgyDng.setText(Messages.PrescriptionView_ngaydung);
		
		this.useNumSSPerDayText = new Text(grpSLng, SWT.BORDER);
		useNumSSPerDayText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblln = new Label(grpSLng, SWT.NONE);
		lblln.setText(Messages.PrescriptionView_landung);
		
		this.numberPerUseText = new Text(grpSLng, SWT.BORDER);
		numberPerUseText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.unitPerText = new Text(grpSLng, SWT.BORDER);
		this.unitPerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		this.totalNoteLabel = new Label(grpSLng, SWT.NONE);
		totalNoteLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
		totalNoteLabel.setText("New Label"); //$NON-NLS-1$
		new Label(grpSLng, SWT.NONE);
		
		Label lblTnGc = new Label(composite_5, SWT.NONE);
		lblTnGc.setText(Messages.PrescriptionView_tengoc);
		
		this.drugOriginalText = new Text(composite_5, SWT.BORDER);
		drugOriginalText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_6 = new Composite(composite_4, SWT.NONE);
		composite_6.setLayout(new GridLayout(1, false));
		composite_6.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		
		this.updateLineBtn = new Button(composite_6, SWT.NONE);
		updateLineBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, false, 1, 1));
		updateLineBtn.setText(Messages.PrescriptionView_luudong);
		updateLineBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateLine();
			}
		});
		
		this.deleteLineBtn = new Button(composite_6, SWT.NONE);
		deleteLineBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		deleteLineBtn.setText(Messages.PrescriptionView_xoadong);
		deleteLineBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteLine();
			}
		});
		
		CTabItem tbtmGhiCh_1 = new CTabItem(tabFolder_1, SWT.NONE);
		tbtmGhiCh_1.setText(Messages.PrescriptionView_xemghichuthuoc);
		
		Text drugNote = new Text(tabFolder_1, SWT.BORDER | SWT.READ_ONLY);
		tbtmGhiCh_1.setControl(drugNote);
		
		this.buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(6, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		CLabel lblNewLabel_1 = new CLabel(buttonComposite, SWT.NONE);
		lblNewLabel_1.setText(Messages.PrescriptionView_ngayin);
		
		this.printDateSelecter = new CDateTime(buttonComposite, CDT.NONE);
		
		this.printBtn = new Button(buttonComposite, SWT.NONE);
		GridData gd_printBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_printBtn.widthHint = 50;
		printBtn.setLayoutData(gd_printBtn);
		printBtn.setText(Messages.PrescriptionView_in);
		printBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				print();
			}
		});
		
		this.addNewBtn = new Button(buttonComposite, SWT.NONE);
		addNewBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		addNewBtn.setText(Messages.PrescriptionView_taomoi);
		addNewBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNew();
			}
		});
		

		// this.editButton = new Button(buttonComposite, SWT.NONE);
		// editButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
		// false, 1, 1));
		// editButton.setText("Chỉnh sửa");
		// editButton.addSelectionListener(new SelectionAdapter() {
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// edit();
		// }
		// });
		
		this.saveBtn = new Button(buttonComposite, SWT.NONE);
		saveBtn.setText(Messages.PrescriptionView_moi);
		saveBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});
		
		this.cancelBtn = new Button(buttonComposite, SWT.NONE);
		cancelBtn.setText(Messages.PrescriptionView_huy);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancel();
			}
		});
	}

	@Override
	public void organizeUIComponent() {
		printDateSelecter.setPattern("dd/MM/yyyy"); //$NON-NLS-1$
		printDateSelecter.setSelection(GregorianCalendar.getInstance().getTime());
		
		// initial data for Combos
		initialCombo(sampleCombo, catManager.getCatagoryByType(CatagoryType.PRESCRIPTION).values(), null, -1, catToUIByDesc);
		initialCombo(surgeryNameCombo, catManager.getCatagoryByType(CatagoryType.DRUG).values(), null, -1, catToUIByName);
		initialCombo(useCombo, catManager.getCatagoryByType(CatagoryType.DRUG_USE).values(), null, -1, catToUIByDesc);
		//initialCombo(sessionCombo, catManager.getCatagoryByType(CatagoryType.DRUG_SESSION_PER_DAY).values(), "---", 0, catToUI);
		initialCombo(noteCombo, catManager.getCatagoryByType(CatagoryType.DRUG_NOTICE).values(), null, -1, catToUIByDesc);
		
		// initial add listener
		this.tableViewer.addSelectionChangedListener(this);
		
		this.sampleCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedPrescriptionCat((PrescriptionCatagory) getValueFromCombo(sampleCombo));
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				selectedPrescriptionCat((PrescriptionCatagory) getValueFromCombo(sampleCombo));
			}
		});
		
		this.surgeryNameCombo.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewPrescriptionItem((DrugCatagory) getValueFromCombo(surgeryNameCombo));
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				viewPrescriptionItem((DrugCatagory) getValueFromCombo(surgeryNameCombo));
			}
		});
		
		this.useNumSSPerDayText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				try {
					if (StringUtils.isBlank(useNumSSPerDayText.getText())) {
						return;
					}
					int perDay = Integer.parseInt(useNumSSPerDayText.getText());
					initialCombo(sessionCombo, DrugCatagory.generateSessionPerDay(perDay), null, -1, null);
					
				} catch (NumberFormatException ex) {
					throw new PamaException(Messages.PrescriptionView_nhapsocholandungtrongngay);
				}
			}
		});
		
		// initial view
		patientCaseChanged(null, paManager.getCurrCase(), 0, new String[]{ID});
	}

	private void viewPrescriptionItem(DrugCatagory modelObj) {
		if (modelObj == null) {
			clearDetailForm();
			return;
		}
		setText(this.drugOriginalText, modelObj.getDesc());
		
		setText(this.numberDayText, String.valueOf(modelObj.getNumDay()));
		setText(this.unitLabel, modelObj.getUnit());
		setText(this.unitPerText, modelObj.getUnitPer());
		setText(this.useNumSSPerDayText, String.valueOf(modelObj.getNumSs()));
		setText(this.numberPerUseText, String.valueOf(modelObj.getPerSs()));
		setText(this.totalNoteLabel, modelObj.getTotalNote());
		selectComboOrSetTextByName(this.useCombo, modelObj.getUse());
		selectComboOrSetTextByName(this.sessionCombo, modelObj.getSs());
		selectComboOrSetTextByName(this.noteCombo, modelObj.getNote());
		
		this.unitLabel.getParent().layout(true);
	}

	private void cancel() {
		if (currPaCase != null) {
			viewData(currPaCase.getPrescription());
		}
	}

	private void save() {
		if (this.currPaCase != null) {
			validateItems();
			this.currPaCase.setPrescription(this.tableViewer.getInput());
			this.paManager.updatePatient(ID, true);
		}
	}

//	private void edit() {
//	}

	private void addNew() {
		viewData(null);
	}

	private void print() {
		final Patient currPa = paManager.getCurrentPatient();
		if (this.currPaCase == null || currPa == null || currPa.getId() == null) {
			return;
		}
		Display display = composite.getDisplay();
		Shell shell = new Shell(display, SWT.SHELL_TRIM);
		shell.setText("GridPrintVerticalAlignmentExample.java"); //$NON-NLS-1$
		shell.setLayout(new GridLayout());
		shell.setSize(600, 800);

		final PrintJob job = new PrintJob("Prescription", //$NON-NLS-1$
				createPrint(tableViewer.getInput(), 
						String.valueOf(currPa.getId()), 
						currPa.getName(), currPa.getAge(), currPa.getFermale(), currPa.getAddress(), currPaCase.getDiagnoseCatagoryNamesAsText(", "),  //$NON-NLS-1$
						(currPaCase.getAppoSchedule() != null ? currPaCase.getAppoSchedule().getAppointmentDate() : null)
						));

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

	private Print createPrint(List<PrescriptionItem> listItem, String idPa, String paName, int age,
			String gender, String add, String diagnose, Date appDate) {
		GridPrint grid = new GridPrint(new DefaultGridLook(5, 5));
		int totalCol = 5;
		for (int i = 0; i < totalCol; i++) {
			grid.addColumn(new GridColumn(SWT.CENTER, SWT.DEFAULT, 20));
		}
		
		// start MS benh nhan
		GridPrint child = new GridPrint(new DefaultGridLook(10, 10));
		child.addColumn(new GridColumn(SWT.RIGHT, SWT.DEFAULT, 0));
		child.addColumn(new GridColumn(SWT.RIGHT, SWT.DEFAULT, 0));
		grid.add(SWT.RIGHT, child, GridPrint.REMAINDER);
		
		final FontData fontData = PamaResourceManager.getFont("Arial", 10, SWT.BOLD).getFontData()[0]; //$NON-NLS-1$
		child.add(SWT.RIGHT, new TextPrint(Messages.PrescriptionView_msbenhnhan, fontData));
		child.add(SWT.RIGHT, new TextPrint(idPa, fontData));
		// end MS benh nhan

		printBlank(grid, 4, fontData);
		
		// Header toa thuoc
		grid.add(SWT.CENTER, new TextPrint(Messages.PrescriptionView_toathuoc, PamaResourceManager.getFont("Arial", 22, SWT.BOLD).getFontData()[0]) //$NON-NLS-2$ //$NON-NLS-1$
				, GridPrint.REMAINDER);

		printBlank(grid, 1, fontData);

		// start patient info
		child = new GridPrint(new DefaultGridLook(10, 10));
		child.addColumn(new GridColumn(SWT.RIGHT, SWT.DEFAULT, 0));
		child.addColumn(new GridColumn(SWT.RIGHT, SWT.DEFAULT, 25));
		child.addColumn(new GridColumn(SWT.LEFT, SWT.DEFAULT, 25));
		child.addColumn(new GridColumn(SWT.LEFT, SWT.DEFAULT, 0));
		grid.add(SWT.LEFT, child, GridPrint.REMAINDER);
		
		child.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_benhnhan, fontData));
		child.add(SWT.LEFT, new TextPrint(paName, fontData));
		child.add(SWT.RIGHT, new TextPrint(Messages.PrescriptionView_tuoi + age, fontData));
		child.add(SWT.RIGHT, new TextPrint(Messages.PrescriptionView_gioitinh + gender.toUpperCase(), fontData));
		
		child.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_diachi, fontData));
		child.add(SWT.LEFT, new TextPrint(add, fontData));
		child.add(SWT.RIGHT, new TextPrint(StringUtils.EMPTY, fontData));
		child.add(SWT.RIGHT, new TextPrint(StringUtils.EMPTY, fontData));
		
		child.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_chandoan, fontData));
		child.add(SWT.LEFT, new TextPrint(diagnose, fontData));
		child.add(SWT.RIGHT, new TextPrint(StringUtils.EMPTY, fontData));
		child.add(SWT.RIGHT, new TextPrint(StringUtils.EMPTY, fontData));

		grid.add(new LinePrint(), GridPrint.REMAINDER);

		int index = 1;
		for (PrescriptionItem item : listItem) {
			grid.add(SWT.LEFT, new TextPrint(index + Messages.PrescriptionView_17, fontData));
			grid.add(SWT.LEFT, new TextPrint(item.getDrug(), fontData, GridPrint.REMAINDER), 3);
			grid.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_18 + String.valueOf(item.getTotal()) + Messages.PrescriptionView_19 + item.getUnit(), fontData));

			grid.add(SWT.LEFT, new TextPrint(StringUtils.EMPTY, fontData));
			grid.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_ngay + item.getUse(), fontData));
			grid.add(SWT.LEFT, new TextPrint(String.valueOf(item.getNumSs()) + Messages.PrescriptionView_lanmoilan, fontData));
			grid.add(SWT.LEFT, new TextPrint(String.valueOf(item.getPerSs()) + "    " + item.getUnitPer(), fontData)); //$NON-NLS-1$
			grid.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_23 + item.getSs() + Messages.PrescriptionView_24 + item.getNote(), fontData));

			index++;
		}
		
		printBlank(grid, 5, fontData);
		
		// Footer
		GridPrint footer = new GridPrint(new DefaultGridLook(10, 10));
		footer.addColumn(new GridColumn(SWT.LEFT, SWT.DEFAULT, 1));
		footer.addColumn(new GridColumn(SWT.CENTER, SWT.DEFAULT, 1));
		grid.add(SWT.CENTER, footer, GridPrint.REMAINDER);
		
		footer.add(SWT.LEFT, new TextPrint(StringUtils.EMPTY, fontData));
		Date printDate = printDateSelecter.getSelection();
		int[] dateA = DateUtils.getDate(printDate);
		if (dateA != null) {
			footer.add(SWT.CENTER, new TextPrint(Messages.PrescriptionView_tphcmngay + dateA[2] + Messages.PrescriptionView_thang + dateA[1] + Messages.PrescriptionView_nam + dateA[0], fontData));
		} else {
			footer.add(SWT.CENTER, new TextPrint(StringUtils.EMPTY, fontData));
		}
		footer.add(SWT.LEFT, new TextPrint(StringUtils.EMPTY, fontData));
		footer.add(SWT.CENTER, new TextPrint(Messages.PrescriptionView_bacsy, fontData));
		
		printBlank(footer, 2, fontData);

		footer.add(SWT.LEFT, new TextPrint(Messages.PrescriptionView_ngaytaikham + DateUtils.convertDateDataType(appDate), fontData));
		footer.add(SWT.CENTER, new TextPrint(Messages.PrescriptionView_pgstslehanh, fontData));

		return grid;
	}
	
	private void printBlank(GridPrint grid, int numLine, FontData fontData) {
		for (int i = 0; i < numLine; i++) {
			grid.add(SWT.CENTER, new TextPrint(StringUtils.EMPTY, fontData), GridPrint.REMAINDER);
		}
	}

	private void deleteLine() {
		this.tableViewer.delete(this.surgeryNameCombo.getText());
	}

	private void updateLine() {
		this.tableViewer.updateLine(getCurrentItem());
	}

	private PrescriptionItem getCurrentItem() {
		PrescriptionItem result = new PrescriptionItem();
		result.setDrug(surgeryNameCombo.getText());
		result.setDrugDesc(drugOriginalText.getText());
		result.setUnit(unitLabel.getText());
		result.setUnitPer(unitPerText.getText());
		result.setNumDay(UIControlUtils.getTextAsFloat(numberDayText, Messages.PrescriptionView_nhapsochosongay));
		result.setNumSs(getTextAsInt(useNumSSPerDayText, Messages.PrescriptionView_nhapsocholandungtrongngay));
		result.setPerSs(UIControlUtils.getTextAsFloat(numberPerUseText, Messages.PrescriptionView_nhapsochosothuocmoilan));
		result.setUse(this.useCombo.getText());
		result.setSs(this.sessionCombo.getText());
		result.setNote(this.noteCombo.getText());
		
		return result;
	}

//	private void deleteTemplate() {
//		
//	}

	private void saveAsTemplate() {
		PrescriptionCatagory preCatToSave = this.preCat;
		if (preCatToSave == null) {
			PrescriptionCatagory selectedSample = (PrescriptionCatagory) UIControlUtils.getValueFromCombo(this.sampleCombo);
			String preName = this.sampleCombo.getText();
			if (selectedSample != null) {
				preName = selectedSample.getName();
			}
			if (StringUtils.isEmpty(preName)) {
				MessageBox dialog = new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
				dialog.setText(Messages.PrescriptionView_loinhaplieu);
				dialog.setMessage(Messages.PrescriptionView_nhaptencuatoamau);
				dialog.open();
				return;
			}
			preCatToSave = new PrescriptionCatagory();
			preCatToSave.setName(preName);
			preCatToSave.setDesc(preName);
		}
		
		validateItems();
		preCatToSave.setData(this.tableViewer.getInput());
		try {
			this.catManager.saveCatagory(preCatToSave);
			this.preCat = preCatToSave;
			initialCombo(sampleCombo, catManager.getCatagoryByType(CatagoryType.PRESCRIPTION).values(), null, -1, catToUIByDesc);
		} catch (SQLException e) {
			throw new PamaException("SQL error " + e.getMessage()); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("rawtypes")
	private void validateItems() {
		if (this.tableViewer.getInput() == null || ((List) this.tableViewer.getInput()).isEmpty()) {
			MessageBox dialog = new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText(Messages.PrescriptionView_loinhaplieu);
			dialog.setMessage(Messages.PrescriptionView_chuahapdanhsachthuoc);
			dialog.open();
		}
	}

	@Focus
	public void setFocus() {
		// do nothing
	}

	@Override
	public boolean isEditing() {
		// do nothing
		return false;
	}

	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		viewData(null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		Object modelObj = ((IStructuredSelection) event.getSelection()).getFirstElement();
		if (modelObj == null) {
			return;
		}
		if (modelObj instanceof PrescriptionItem) {
			viewPrescriptionItem((PrescriptionItem) modelObj);
		}
	}

	private void viewPrescriptionItem(PrescriptionItem modelObj) {
		selectComboOrSetTextByName(this.surgeryNameCombo, modelObj.getDrug());
		setText(this.otherDrugText, modelObj.getOtherDrug());
		setText(this.drugOriginalText, modelObj.getDrugDesc());
		
		setText(this.numberDayText, String.valueOf(modelObj.getNumDay()));
		setText(this.unitLabel, modelObj.getUnit());
		setText(this.unitPerText, modelObj.getUnitPer());
		setText(this.useNumSSPerDayText, String.valueOf(modelObj.getNumSs()));
		setText(this.numberPerUseText, String.valueOf(modelObj.getPerSs()));
		setText(this.totalNoteLabel, modelObj.getTotalNote());
		selectComboOrSetTextByName(this.useCombo, modelObj.getUse());
		selectComboOrSetTextByName(this.sessionCombo, modelObj.getSs());
		selectComboOrSetTextByName(this.noteCombo, modelObj.getNote());
		
		this.unitLabel.getParent().layout(true);
	}

	private void selectedPrescriptionCat(PrescriptionCatagory preCat) {
		this.preCat = preCat;
		List<PrescriptionItem> itemList = null;
		if (this.preCat != null) {
			itemList = preCat.getData();
		}
		viewData(itemList);
	}

	private void viewData(List<PrescriptionItem> itemList) {
		this.tableViewer.setInput(itemList);
		clearDetailForm();
	}

	private void clearDetailForm() {
		revert(this.surgeryNameCombo);
		final String empty = StringUtils.EMPTY;
		this.otherDrugText.setText(empty);
		this.drugOriginalText.setText(empty);
		this.unitLabel.setText(empty);
		this.unitPerText.setText(empty);
		setText(this.numberDayText, empty);
		setText(this.useNumSSPerDayText, empty);
		setText(this.numberPerUseText, empty);
		setText(this.totalNoteLabel, empty);
		revert(this.useCombo);
		revert(this.sessionCombo);
		revert(this.noteCombo);
		
		this.unitLabel.getParent().layout(true);
	}
	
	@Override
	public void patientCaseChanged(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase, String[] callIds) {
		this.currPaCase = newCase;
		if (this.currPaCase == null) {
			viewData(null);
		} else {
			viewData(currPaCase.getPrescription());
		}
	}

}
