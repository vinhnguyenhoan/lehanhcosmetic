package com.lehanh.pama.patientcase;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Display;

import com.lehanh.pama.ICatagoryManager;
import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.DiagnoseCatagory;
import com.lehanh.pama.catagory.DrCatagory;
import com.lehanh.pama.catagory.PrognosticCatagory;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.catagory.SurgeryCatagory;
import com.lehanh.pama.db.dao.PatientDao;
import com.lehanh.pama.db.dao.ScheduleDao;
import com.lehanh.pama.ui.util.ObjectToUIText;
import com.lehanh.pama.util.PamaException;
import com.lehanh.pama.util.PamaHome;

public class PatientManager implements IPatientManager, IPatientSearcher, IAppointmentManager {

	//private List<AppointmentSchedule> listAppointmentToday;
	
	private ICatagoryManager catagoryManager;

	private Patient patientSelected;
	
	private PatientCaseEntity patientCaseDetailSelected;
	
	private Map<String, IPatientViewPartListener> paListeners = new HashMap<String, IPatientViewPartListener>();

	private class NotifyPaRunnable implements Runnable {
		
		private Patient oldPa;
		private Patient newPa;
		
		private PatientCaseEntity oldCase;
		private PatientCaseEntity newCase;
		public int rootCase;
		
		private String[] callIds;

		private boolean notifyPatient = true;
		
		@Override
		public void run() {
			if (notifyPatient) {
				for (Entry<String, IPatientViewPartListener> pL : paListeners.entrySet()) {
					pL.getValue().patientChanged(oldPa, newPa, callIds);
				}
			} else {
				for (Entry<String, IPatientViewPartListener> pL : paListeners.entrySet()) {
					pL.getValue().patientCaseChanged(oldCase, newCase, rootCase, callIds);
				}
			}
		}
	}
	
	private final NotifyPaRunnable notifyPaRunnable = new NotifyPaRunnable();
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.IService#initialize()
	 */
	@Override
	public void initialize() throws SQLException {
		catagoryManager = (ICatagoryManager) PamaHome.getService(ICatagoryManager.class);
		// Load all appointment
		reloadAppointment();
	}

	private void reloadAppointment() throws SQLException {
		GregorianCalendar calToday = new GregorianCalendar();
		calToday.set(Calendar.HOUR_OF_DAY, 0);
		calToday.set(Calendar.MINUTE, 0);
		calToday.set(Calendar.SECOND, 0);
	}

	@Override
	public IPatientSearcher getPatientSearcher() {
		return this;
	}

	/////////////////////////////// com.lehanh.pama.patientcase.IPatientSearcher /////////////////////////////// 
	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientSearcher#getPatientDetailById(java.lang.Long)
	 */
	@Override
	public Patient getPatientDetailById(Long id) throws SQLException, ParseException {
		if (id == null) {
			return null;
		}
		List<Patient> result = new PatientDao().searchPatient(id, null, null, null);
		return (result == null || result.isEmpty()) ? null : result.get(0);
	}

	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientSearcher#searchPatient(java.util.Date, java.util.Date, java.lang.String)
	 */
	@Override
	public List<Patient> searchPatient(Date lastUpdate, String name, String phone) throws SQLException, ParseException {
		List<Patient> result = new PatientDao().searchPatient(null, lastUpdate, name, phone);
		return result;
	}

	/////////////////////////////// com.lehanh.pama.patientcase.IAppointmentManager /////////////////////////////// 
	@Override
	public IAppointmentManager getAppointmentManager() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IAppointmentManager#searchPatientAppointmentToday(boolean)
	 */
	@Override
	public List<AppointmentSchedule> searchPatientAppointment(Date selection) throws SQLException, ParseException {
		if (selection == null) {
			return null;
		}
		List<AppointmentSchedule> listAppointmentToday = new ScheduleDao().loadAllAppointment(selection);
		
		Map<Long, Catagory> allAppointmentType = catagoryManager.getCatagoryByType(CatagoryType.APPOINTMENT);
		for (AppointmentSchedule aS : listAppointmentToday) {
			if (aS.getAppointmentType() == null || aS.getAppointmentType() <= 0) {
				continue;
			}
			Catagory appointmentType = allAppointmentType.get(aS.getAppointmentType());
			if (appointmentType == null) {
				throw new PamaException(Messages.PatientManager_cannotfindappointmenttype + aS.getAppointmentType());
			}
			aS.setAppointmentCatagory((AppointmentCatagory) appointmentType);
		}
		return listAppointmentToday;
	}

	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IAppointmentManager#clearOldAppointment()
	 */
	@Override
	public int clearOldAppointment() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/////////////////////////////// com.lehanh.pama.patientcase.IPatientManager /////////////////////////////// 
	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientManager#updatePatient(java.lang.String, java.lang.String, java.lang.String, java.util.Date, boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Patient updatePatient(String uiId, String imagePath, String name, String address, Date birthDay, boolean isFermale,
			String cellPhone, String phone, String email, String career, int patientLevel, String note,  String detailExam,
			String medicalHistory, String anamnesis) {
		// validate input data
		Patient validateSample = new Patient();
		updatePatient(name, imagePath, address, birthDay, isFermale, cellPhone, phone,
				email, career, patientLevel, note, validateSample);
		
		Patient result = new Patient();
		if (patientSelected != null) {
			result.setId(patientSelected.getId());
		}
		updatePatient(name, imagePath, address, birthDay, isFermale, cellPhone, phone,
				email, career, patientLevel, note, result);
		
		MedicalPersonalInfo medicalPersonalInfo = result.getMedicalPersonalInfo();
		if (patientSelected != null) {
			medicalPersonalInfo = patientSelected.getMedicalPersonalInfo();
			result.setMedicalPersonalInfo(medicalPersonalInfo);
		}
		medicalPersonalInfo.setAnamnesis(anamnesis);
		medicalPersonalInfo.setMedicalHistory(medicalHistory);
		medicalPersonalInfo.setSummary(detailExam);
		
		try {
			if (result.getId() == null) {
				new PatientDao().insert(result);
			} else {
				new PatientDao().update(result);
			}
		} catch (SQLException e) {
			throw new PamaException(Messages.PatientManager_loicapnhatdb + e.getMessage());
		}
		this.patientSelected = result; // update patientSelected before call cancelEditing
		getCurrentPatient().reloadMedicalInfo();
		notifyPaListener(patientSelected, result, uiId);
		return this.patientSelected;
	}

	private static void updatePatient(String name, String imagePath, String address, Date birthDay,
			boolean isFermale, String cellPhone, String phone, String email,
			String career, int patientLevel, String note, Patient result) {
		result.setName(name);
		result.setImageName(imagePath);
		result.setAddress(address);
		result.setBirthDay(birthDay);
		result.setFermale(isFermale);
		result.setCellPhone(cellPhone);
		result.setPhone(phone);
		result.setEmail(email);
		result.setCareer(career);
		result.setPatientLevel(patientLevel);
		result.setNote(note);
	}
	
	@Override
	public void updatePatient(String uid, boolean notify) {
		try {
			new PatientDao().update(this.patientSelected);
		} catch (SQLException e) {
			throw new PamaException(Messages.PatientManager_loicapnhapdb + e.getMessage());
		}
		getCurrentPatient().reloadMedicalInfo();
		if (notify) {
			notifyPaListener(null, this.patientSelected, uid);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientManager#addPaListener(com.lehanh.pama.patientcase.IPatientViewPartListener)
	 */
	@Override
	public synchronized void addPaListener(IPatientViewPartListener paL, String id) {
		this.paListeners.put(id, paL);
	}
	
	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientManager#getCurrentPatient()
	 */
	@Override
	public Patient getCurrentPatient() {
		return this.patientSelected;
	}

	/* (non-Javadoc)
	 * @see com.lehanh.pama.patientcase.IPatientManager#updatePatientCase(com.lehanh.pama.patientcase.PatientCaseEntity, com.lehanh.pama.catagory.DrCatagory, java.util.List, java.util.List, java.lang.String, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.String, java.util.Date, boolean, boolean, java.lang.String, java.lang.String, java.util.Date, com.lehanh.pama.catagory.AppointmentCatagory, java.lang.String)
	 */
	@Override
	public void updatePatientCase(String uiId, Integer rootCaseId, PatientCaseEntity paCaseEntity,
			DrCatagory drCat, List<ServiceCatagory> serviceList,
			List<PrognosticCatagory> progCatList, String prognosticOtherText,
			List<DiagnoseCatagory> diagCatList, String diagOtherText,
			String noteFromPa, String noteFromDr,
			List<SurgeryCatagory> surList, String surgeryNote,
			Date surgeryDate, boolean complication, boolean beauty,
			String smallSurgery, String drAdvice, Date nextApp,
			AppointmentCatagory appPurpose, String appNote, ObjectToUIText<Patient, Long> toPatientInfo) {
		
		if (patientSelected == null) {
			throw new IllegalAccessError(Messages.PatientManager_chuachonbenhnhan);
		}
		if (rootCaseId == null) {
			throw new IllegalAccessError(Messages.PatientManager_chuachonbenhan);
		}
		IPatientCaseList rootCasesList = patientSelected.getMedicalPersonalInfo().getPatientCaseList();
		rootCasesList.getSubList(rootCaseId)
				     .updateCase(paCaseEntity, drCat, serviceList, progCatList, prognosticOtherText,
								diagCatList, diagOtherText, noteFromPa, noteFromDr, surList, surgeryNote,
								surgeryDate, complication, beauty, smallSurgery, drAdvice, nextApp,
								appPurpose, appNote, toPatientInfo != null ? toPatientInfo.showUI(patientSelected) : StringUtils.EMPTY);
		try {
			new PatientDao().update(patientSelected);
		
			AppointmentSchedule appSche = paCaseEntity.getAppoSchedule();
			if (appSche != null) {
				if (appSche.getId() == null) {
					appSche.setPatientId(patientSelected.getId());
					new ScheduleDao().insert(appSche);
				} else {
					new ScheduleDao().update(appSche);
				}
			}
		} catch (SQLException e) {
			throw new PamaException(Messages.PatientManager_loicapnhatdb + e.getMessage());
		}
		getCurrentPatient().reloadMedicalInfo();
		notifyPaListener(patientSelected, patientSelected, uiId);
	}

	@Override
	public void cancelEditingPatientCase() {
		getCurrentPatient().reloadMedicalInfo();
	}

	@Override
	public void selectPatient(String uiId, Patient newPatient) {
		Patient oldPa = this.patientSelected;
		this.patientSelected = newPatient;
		notifyPaListener(oldPa, patientSelected, uiId);
	}
	
	@Override
	public void selectPatient(String uid, Long patientId) throws SQLException, ParseException {
		Patient patient = getPatientDetailById(patientId);
		selectPatient(uid, patient);
	}
	
	@Override
	public void selectDetailPatientCase(String uiId, PatientCaseEntity detailEntity, int rootCase) {
		PatientCaseEntity oldCase =  this.patientCaseDetailSelected;
		this.patientCaseDetailSelected = detailEntity;
		notifyPaCaseListener(oldCase, patientCaseDetailSelected, rootCase, uiId);
	}

	private void notifyPaListener(final Patient oldPa, final Patient newPa, String... callIds) {
		notifyPaRunnable.oldPa = oldPa;
		notifyPaRunnable.newPa = newPa;
		notifyPaRunnable.callIds = callIds;
		notifyPaRunnable.notifyPatient = true;
		Display.getCurrent().asyncExec(notifyPaRunnable);
	}
	
	private void notifyPaCaseListener(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase, String... callIds) {
		notifyPaRunnable.oldCase = oldCase;
		notifyPaRunnable.newCase = newCase;
		notifyPaRunnable.rootCase = rootCase;
		notifyPaRunnable.callIds = callIds;
		notifyPaRunnable.notifyPatient = false;
		Display.getCurrent().asyncExec(notifyPaRunnable);
	}

	@Override
	public PatientCaseEntity getCurrCase() {
		return this.patientCaseDetailSelected;
	}

}
