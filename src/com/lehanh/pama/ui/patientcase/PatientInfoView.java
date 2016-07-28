package com.lehanh.pama.ui.patientcase;

import static com.lehanh.pama.ui.util.UIControlUtils.getValueFromCombo;
import static com.lehanh.pama.ui.util.UIControlUtils.initialCombo;
import static com.lehanh.pama.ui.util.UIControlUtils.isChanged;
import static com.lehanh.pama.ui.util.UIControlUtils.listenModifiedByClient;
import static com.lehanh.pama.ui.util.UIControlUtils.selectComboById;
import static com.lehanh.pama.ui.util.UIControlUtils.setText;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.patientcase.IPatientManager;
import com.lehanh.pama.patientcase.IPatientViewPartListener;
import com.lehanh.pama.patientcase.MedicalPersonalInfo;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.PamaFormUI;
import com.lehanh.pama.ui.util.CatagoryToUITextByDesc;
import com.lehanh.pama.ui.util.PamaResourceManager;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.PamaException;
import com.lehanh.pama.util.PamaHome;

public class PatientInfoView extends PamaFormUI implements IPatientViewPartListener, IPatientView {
	
	private static final int IMAGE_H = 190;

	private static final int IMAGE_W = 142;

	public static final String ID = "com.lehanh.pama.patientInfoView"; //$NON-NLS-1$
	
	private Text idText;
	private Text mobiText;
	private Text phoneText;
	private Text paNoteText;
	private Text nameText;
	private Text addText;
	private Text emailText;
	private Text careerText;
	private Text anamnesisText;
	private Text medicalHistoryText;
	private Text detailExamText;
	
	private CLabel ageLbl;
	private static final String AGE = Messages.PatientInfoView_tuoi;

	//private CalendarCombo birthDayCalendar;
	//private DateTime birthDayCalendar;
	private CDateTime birthDayCalendar;
	
	private Canvas paImageLabel;
	
	private CCombo paLevelCombo;

	private Composite composite;

	private Button addNewBtn;

	private Button editBtn;

	private Button saveBtn;

	private Button cancelBtn;
	
	private Composite buttonsComposite;
	
	private ICatagoryManager catManager;
	
	private IPatientManager paManager;

	private Button femaleRadio;
	private Button maleRadio;

	private String patientPath;
	
	private static final String SEX_GROUP = "SEX_GROUP"; //$NON-NLS-1$

	private static final String BLANKFILE_NAME = "BLANKFILE_NAME"; //$NON-NLS-1$

	private String fileName;
	
	public PatientInfoView() {
		catManager = (ICatagoryManager) PamaHome.getService(ICatagoryManager.class);
		paManager = (IPatientManager) PamaHome.getService(IPatientManager.class);
		paManager.addPaListener(this, ID);

		this.patientPath = PamaHome.application.getProperty(PamaHome.PATIENT_IMAGE_PATH, PamaHome.DEFAULT_PATIENT_IMAGE_PATH);
		if (!(patientPath.endsWith("\\") || patientPath.endsWith("/"))) { //$NON-NLS-1$ //$NON-NLS-2$
			patientPath = patientPath + "\\"; //$NON-NLS-1$
		}
	}

	@Override
	public void createFormUI(Composite parent) {
		this.composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
		composite_1.setLayout(new GridLayout(3, false));
		
		this.paImageLabel = new CLabel(composite_1, SWT.BORDER);
		GridData gd_paImageLabel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_paImageLabel.widthHint = IMAGE_W + 2;
		gd_paImageLabel.heightHint = IMAGE_H + 2;
		paImageLabel.setLayoutData(gd_paImageLabel);
//		paImageLabel.setImage(SWTResourceManager.getImage("D:\\LeHanh\\soft\\Backup LHS\\20151025\\Run\\LHS\\Images\\Local\\PatientPic\\17176.jpg"/*, 120, 150*/));
		
		Composite composite_2 = new Composite(composite_1, SWT.BORDER);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_2.setLayout(new GridLayout(4, false));
		
		CLabel lblId = new CLabel(composite_2, SWT.NONE);
		lblId.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblId.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblId.setText("ID:"); //$NON-NLS-1$
		
		idText = new Text(composite_2, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_idText = new GridData(SWT.LEFT, SWT.CENTER, true, false, 3, 1);
		gd_idText.widthHint = 100;
		idText.setLayoutData(gd_idText);
		
		CLabel lblHTn = new CLabel(composite_2, SWT.NONE);
		lblHTn.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblHTn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblHTn.setText(Messages.PatientInfoView_hoten);
		
		nameText = new Text(composite_2, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblNewLabel = new CLabel(composite_2, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblNewLabel.setText(Messages.PatientInfoView_diachi);
		
		addText = new Text(composite_2, SWT.BORDER);
		addText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblNgySinh = new CLabel(composite_2, SWT.NONE);
		lblNgySinh.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblNgySinh.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblNgySinh.setText(Messages.PatientInfoView_ngaysinh);
		
		Composite composite_4 = new Composite(composite_2, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		GridLayout gl_composite_4 = new GridLayout(4, false);
		gl_composite_4.verticalSpacing = 0;
		gl_composite_4.marginWidth = 0;
		gl_composite_4.marginHeight = 3;
		composite_4.setLayout(gl_composite_4);
		
		//this.birthDayCalendar = new CalendarCombo(composite_4, SWT.CALENDAR);
		//this.birthDayCalendar = new DateTime (composite_4, SWT.CALENDAR);
		this.birthDayCalendar = new CDateTime(composite_4, CDT.BORDER | CDT.SPINNER);
		birthDayCalendar.setNullText(Messages.PatientInfoView_nhapngay);
		birthDayCalendar.setPattern("dd/MM/yyyy"); //$NON-NLS-1$
		
		this.ageLbl = new CLabel(composite_4, SWT.NONE);
		ageLbl.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		this.femaleRadio = new Button(composite_4, SWT.RADIO);
		femaleRadio.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		femaleRadio.setText(Messages.PatientInfoView_nu);
		
		this.maleRadio = new Button(composite_4, SWT.RADIO);
		maleRadio.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		maleRadio.setText(Messages.PatientInfoView_nam);
		
		CLabel lblD = new CLabel(composite_2, SWT.NONE);
		lblD.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblD.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblD.setText(Messages.PatientInfoView_dd);
		
		mobiText = new Text(composite_2, SWT.BORDER);
		mobiText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblinThoi = new CLabel(composite_2, SWT.NONE);
		lblinThoi.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblinThoi.setText(Messages.PatientInfoView_dienthoai);
		
		phoneText = new Text(composite_2, SWT.BORDER);
		phoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblNghNghip = new CLabel(composite_2, SWT.NONE);
		lblNghNghip.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblNghNghip.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		lblNghNghip.setText("Email:"); //$NON-NLS-1$
		
		emailText = new Text(composite_2, SWT.BORDER);
		emailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		CLabel lblNghNghip_1 = new CLabel(composite_2, SWT.NONE);
		lblNghNghip_1.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblNghNghip_1.setText(Messages.PatientInfoView_nghenghiep);
		
		careerText = new Text(composite_2, SWT.BORDER);
		careerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_3 = new Composite(composite_1, SWT.BORDER);
		composite_3.setLayout(new GridLayout(2, false));
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
		
		CLabel lblMcTinh = new CLabel(composite_3, SWT.NONE);
		lblMcTinh.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblMcTinh.setText(Messages.PatientInfoView_mdtinhthan);
		
		this.paLevelCombo = new CCombo(composite_3, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		GridData gd_paLevelCombo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_paLevelCombo.widthHint = 90;
		paLevelCombo.setLayoutData(gd_paLevelCombo);
		
		CLabel lblGhiChV = new CLabel(composite_3, SWT.NONE);
		lblGhiChV.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblGhiChV.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblGhiChV.setText(Messages.PatientInfoView_ghichuvebenhnhan);
		
		paNoteText = new Text(composite_3, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		paNoteText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Composite composite_6 = new Composite(composite, SWT.NONE);
		composite_6.setLayout(new GridLayout(1, false));
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		Label lblChiTitT = new Label(composite_6, SWT.NONE);
		lblChiTitT.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		lblChiTitT.setText(Messages.PatientInfoView_chitiettuvanthamkham);
		
		detailExamText = new Text(composite_6, SWT.BORDER /*| SWT.READ_ONLY*/ | SWT.V_SCROLL | SWT.MULTI);
		detailExamText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_8 = new Composite(composite, SWT.NONE);
		composite_8.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_8.setLayout(new GridLayout(1, false));
		
		Label label_5 = new Label(composite_8, SWT.NONE);
		label_5.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		label_5.setText(Messages.PatientInfoView_tiencan);
		
		anamnesisText = new Text(composite_8, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		anamnesisText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label label_6 = new Label(composite_8, SWT.NONE);
		label_6.setFont(SWTResourceManager.getFont("Segoe UI", 10, SWT.NORMAL)); //$NON-NLS-1$
		label_6.setText(Messages.PatientInfoView_benhsu);
		
		medicalHistoryText = new Text(composite_8, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		medicalHistoryText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		
		this.buttonsComposite = new Composite(composite, SWT.NONE);
		buttonsComposite.setLayout(new GridLayout(4, true));
		buttonsComposite.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false, 1, 1));
		
		this.addNewBtn = new Button(buttonsComposite, SWT.NONE);
		addNewBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNew();
			}
		});
		addNewBtn.setText(Messages.PatientInfoView_themmoi);
		addNewBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		
		this.editBtn = new Button(buttonsComposite, SWT.NONE);
		editBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		editBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				edit();
			}
		});
		editBtn.setText(Messages.PatientInfoView_chinhsua);
		
		this.saveBtn = new Button(buttonsComposite, SWT.NONE);
		saveBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});
		saveBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		saveBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		saveBtn.setText(Messages.PatientInfoView_luu);
		
		this.cancelBtn = new Button(buttonsComposite, SWT.NONE);
		cancelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cancel();
			}
		});
		cancelBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cancelBtn.setFont(SWTResourceManager.getFont("Arial", 10, SWT.NORMAL)); //$NON-NLS-1$
		cancelBtn.setText(Messages.PatientInfoView_huy);
	}
	
	@Override
	public void organizeUIComponent() {
		paImageLabel.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				Image image = (Image) paImageLabel.getData();
				if (image == null || image.isDisposed()) {
					return;
				}
				GC gc = e.gc;
				Rectangle imageB = image.getBounds();
                int x = (IMAGE_W / 2) - (imageB.width / 2), y = (IMAGE_H / 2) - (imageB.height / 2);
                gc.drawImage(image, x, y);
                gc.dispose();
			}
		});
		
		getFormManager().addAllControlFromComposite(composite, true, false, buttonsComposite)
						.addCreateButtons(addNewBtn).addEditButtons(editBtn).addSaveButtons(saveBtn).addCancelButtons(cancelBtn)
						.addRadioGroup(SEX_GROUP, this.maleRadio, this.femaleRadio).defaultRadios(this.femaleRadio)
						.setEditableAll(false)
						.cancel(paManager.getCurrentPatient() != null);
		
		// init paLevelCombo
		initialCombo(paLevelCombo, catManager.getCatagoryByType(CatagoryType.SPIRIT_LEVEL).values(), Messages.PatientInfoView_chuaxacdinh, 0,
				new CatagoryToUITextByDesc());
		
		// listen modified detailExamText
		listenModifiedByClient(detailExamText);
		
		birthDayCalendar.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (birthDayCalendar.getSelection() == null) {
					ageLbl.setText(StringUtils.EMPTY);
					return;
				}
				ageLbl.setText(DateUtils.calculateAge(birthDayCalendar.getSelection()) + " " + AGE); //$NON-NLS-1$
				ageLbl.getParent().layout(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}
		});
		
		Menu popupMenu = new Menu(paImageLabel);
	    
	    MenuItem loadImage = new MenuItem(popupMenu, SWT.NONE);
	    loadImage.setText(Messages.PatientInfoView_chonanh);
	    loadImage.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				loadPatientImg();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				loadPatientImg();;
			}
		});
	    
	    paImageLabel.setMenu(popupMenu);
	    
		// initial view
	    cancel();
	}

	private void loadPatientImg() {
		if (!isEditing()) {
			return;
		}
		FileDialog dlg = new FileDialog(this.paImageLabel.getShell());
		// Change the title bar text
		dlg.setText(Messages.PatientInfoView_anhbenhnhan);

		String[] filterExt = { "*.*", "*.png", "*.jpeg", "*.jpg", "*.PNG", "*.JPEG", "*.JPG" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		dlg.setFilterExtensions(filterExt);

		// Calling open() will open and run the dialog.
		// It will return the selected directory, or null if user cancels
		String dir = dlg.open();
		if (!StringUtils.isBlank(dir)) {
			String fileName = BLANKFILE_NAME;
			if (paManager.getCurrentPatient() != null && paManager.getCurrentPatient().getId() != null) {
				fileName = String.valueOf(paManager.getCurrentPatient().getId());
			}
			String ext = FilenameUtils.getExtension(new File(dir).getName());
			
			Image image = setImage(dir, fileName + "." + ext); //$NON-NLS-1$
			movePatientImg(dir, fileName + "." + ext, image); //$NON-NLS-1$
		}
	}

	private void movePatientImg(String dir, String fileName, Image image) {
		if (image == null) {
			return;
		}
		// move file to 
		File toFolderFile = new File(patientPath);
		if (!toFolderFile.exists()) {
			if (!toFolderFile.mkdirs()) {
				throw new PamaException("Can not create folder " + patientPath); //$NON-NLS-1$
			}
		}
		//final String fileName = new File(dir).getName();
		
		String ext = FilenameUtils.getExtension(fileName);
		int extI = SWT.IMAGE_PNG;
		if ("jpeg".equalsIgnoreCase(ext) || "jpg".equalsIgnoreCase(ext)) { //$NON-NLS-1$ //$NON-NLS-2$
			extI = SWT.IMAGE_JPEG;
		}
		
		ImageLoader loader = new ImageLoader();
	    loader.data = new ImageData[] {image.getImageData()};
	    loader.save(patientPath + fileName, extI);
	}

	private Image setImage(String dir, String fileName) {
		Image image = null;
		
		if (!StringUtils.isBlank(dir) && new File(dir).exists()) {
			try {
				image = PamaResourceManager.getImage(dir, StringUtils.EMPTY, IMAGE_W, IMAGE_H);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		setImage(image);
		
		if (image == null) {
			this.fileName = null;
		} else {
			this.fileName = fileName;//fromPath.getFileName().toFile().getName();
		}
		return image;
	}
	
	private void setImage(Image image) {
		Image oldImg = (Image) paImageLabel.getData();
		paImageLabel.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if (oldImg != null && !oldImg.isDisposed()) {
					oldImg.dispose();
				}
			}
		});
		paImageLabel.setData(image);
		paImageLabel.redraw();
	}

	private void handleData(Patient patient) {
		if (patient == null) {
			return;
		}
		setText(idText, String.valueOf(patient.getId()));
		setText(mobiText, patient.getCellPhone());
		setText(phoneText, patient.getPhone());
		setText(nameText, patient.getName());
		setText(addText, patient.getAddress());
		setText(emailText, patient.getEmail());
		setText(careerText, patient.getCareer());

		//birthDayCalendar.setDate(patient.getBirthDay());
		if (patient.getBirthDay() != null) {
//			int[] date = DateUtils.getDate(patient.getBirthDay());
//			birthDayCalendar.setDate(date[0], date[1], date[2]);
			birthDayCalendar.setSelection(patient.getBirthDay());
		}
		
		ageLbl.setText(patient.getAge() + " " + AGE); //$NON-NLS-1$
		
		setText(paNoteText, patient.getNote());
		selectComboById(paLevelCombo, patient.getPatientLevel());
		
		String imgName = patient.getImageName();
		setImage(patientPath + imgName, imgName);
		
		MedicalPersonalInfo medicalPersonalInfo = patient.getMedicalPersonalInfo();
		setText(anamnesisText, medicalPersonalInfo.getAnamnesis());
		setText(medicalHistoryText, medicalPersonalInfo.getMedicalHistory());
		setText(detailExamText, medicalPersonalInfo.getPatientCaseList().getSummary(true));
	}
	
	private void cancel() {
		view(paManager.getCurrentPatient(), false);
	}
	
	private void save() {
		int paLevel = -1;
		Catagory selectedPaLevel = (Catagory) getValueFromCombo(paLevelCombo);
		if (selectedPaLevel != null) {
			paLevel = selectedPaLevel.getId().intValue();
		}
		
		String detailExam = null;
		if (isChanged(detailExamText)) {
			detailExam = detailExamText.getText();
		}
		try {
			/*
			id, imagePath, name, address, birthDay, isFermale,
			cellPhone, phone, email, career, patientLevel, note, medicalHistory, anamnesis
			 */
			Patient savedPa = paManager.updatePatient(ID, fileName,
					// personal info
					nameText.getText(), addText.getText(), birthDayCalendar.getSelection(),
					femaleRadio == getFormManager().getSelected(SEX_GROUP),
					mobiText.getText(), phoneText.getText(), emailText.getText(), careerText.getText(), paLevel, paNoteText.getText(),
					// medical info
					detailExam, medicalHistoryText.getText(), anamnesisText.getText());
			
			if (savedPa != null && BLANKFILE_NAME.equals(fileName)) {
				File oldName = new File(patientPath + fileName);
				File newName = new File(patientPath + savedPa.getId());
				oldName.renameTo(newName);
			}
			getFormManager().saved(paManager.getCurrentPatient() != null);
		} catch (InvalidParameterException e) {
			MessageBox dialog = 
				  new MessageBox(composite.getShell(), SWT.ICON_ERROR | SWT.OK);
			dialog.setText(Messages.PatientInfoView_loinhaplieu);
			dialog.setMessage(e.getMessage());
			dialog.open();
		}
	}

	private void edit() {
		// do nothing for data
		getFormManager().edit();
	}

	private void addNew() {
		paManager.selectPatient(ID, (Patient) null);
		setImage(null);
		getFormManager().addNew();
	}

	@Focus
	public void setFocus() {
		// do nothing
	}

	@Override
	public void patientChanged(Patient oldPa, Patient newPa, String[] callIds) {
		/*if (callIds != null && Arrays.binarySearch(callIds, ID) > -1) {
			return;
		}*/
		view(newPa, isEditing());
	}

	private void view(Patient newPa, boolean isEditing) {
		if (isEditing) {
			return;
		}
		getFormManager().cancel(newPa != null);
		setImage(null);
		handleData(newPa);
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
