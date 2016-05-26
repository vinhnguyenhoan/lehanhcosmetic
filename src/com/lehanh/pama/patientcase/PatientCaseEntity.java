package com.lehanh.pama.patientcase;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lehanh.pama.IJsonDataObject;
import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.PrescriptionItem;
import com.lehanh.pama.util.DateUtils;
import com.lehanh.pama.util.ValidateUtils;

public class PatientCaseEntity implements Serializable, IJsonDataObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7533407014313088176L;

	@Expose
	private int id = -1;
	
	@Expose
	private long oldId = -1;
	
	@Expose
	@SerializedName("date")
	private String dateAsText;
	private Date date;

	@Expose
	@SerializedName("ss")
	private String status;
	private PatientCaseStatus statusEnum;
	
	@Expose
	@SerializedName("sDate")
	private String surgeryDateAsText;
	private Date surgeryDate;
	
	@Expose
	private Long drId;	
//	private DrCatagory dr;
	
	@Expose
	@SerializedName("sIds")
	private List<String> serviceNames;	
//	private List<ServiceCatagory> serviceList;
	
	@Expose
	@SerializedName("progIds")
	private List<String> prognosticCatagoryNames;
//	private List<PrognosticCatagory> prognosticCatagoryList;
	@Expose
	@SerializedName("progOth")
	private String prognosticOther;
	
	@Expose
	@SerializedName("diagIds")
	private List<String> diagnoseCatagoryNames;
//	private List<DiagnoseCatagory> diagnoseCatagoryList;
	@Expose
	@SerializedName("diagOth")
	private String diagnoseOther;
	
	@Expose
	@SerializedName("sgIds")
	private List<String> surgeryCatagoryNames;
//	private List<SurgeryCatagory> surgeryCatagoryList;
	
	@Expose
	@SerializedName("sgNote")
	private String surgeryNote;
	
	@Expose
	@SerializedName("noteCl")
	private String noteFromClient;
	@Expose
	@SerializedName("noteDr")
	private String noteFromDr;
	
	@Expose
	@SerializedName("sSurgery")
	private String smallSurgery;
	@Expose
	@SerializedName("advice")
	private String adviceFromDr;
	
	@Expose
	@SerializedName("comp")
	private boolean complication;
	@Expose
	@SerializedName("save")
	private boolean beautiful;
	
	@Expose
	@SerializedName("appoS")
	private AppointmentSchedule appoSchedule;

	@Expose
	private TreeMap<String, TreeMap<String, TreeMap<String, Object>>> picInfos;

	@Expose
	@SerializedName("prescription")
	private List<PrescriptionItem> prescription;
	
	@Expose
	private List<PatientCaseEntity> reExamInfo;

	public PatientCaseEntity() {
	}
	
	PatientCaseEntity(String dateText, PatientCaseStatus status) {
		this.setDateAsText(dateText);
		this.setStatusEnum(status);
	}

	PatientCaseEntity(String dateText) {
		this(dateText, null);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public long getOldId() {
		return oldId;
	}

	public void setOldId(long oldId) {
		this.oldId = oldId;
	}

	public boolean isNew() {
		return this.id == -1;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PatientCaseStatus getStatusEnum() {
		if (this.status == null) {
			return null;
		}
		
		if (statusEnum == null) {
			statusEnum = PatientCaseStatus.valueOf(this.status);
		}
		return statusEnum;
	}

	public void setStatusEnum(PatientCaseStatus statusEnum) {
		this.statusEnum = statusEnum;
		this.status = statusEnum == null ? null : statusEnum.name();
	}

	public Long getDrId() {
		return drId;
	}

	public void setDrId(Long drId) {
		if (drId == null || drId < 0) {
			throw new InvalidParameterException(Messages.PatientCaseEntity_phaichonbacsy);
		}
		this.drId = drId;
	}

//	public DrCatagory getDr() {
//		return dr;
//	}
//
//	void setDr(DrCatagory dr) {
//		this.dr = dr;
//	}

	public List<String> getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(List<String> serviceIds) {
		ValidateUtils.validateIsAllEmpty(Messages.PatientCaseEntity_phaichondichvu, serviceIds);
		this.serviceNames = serviceIds;
	}

//	public List<ServiceCatagory> getServiceList() {
//		return serviceList;
//	}
//
//	void setServiceList(List<ServiceCatagory> serviceList) {
//		this.serviceList = serviceList;
//	}

	public List<String> getPrognosticCatagoryNames() {
		return prognosticCatagoryNames;
	}

	public void setPrognosticCatagoryNames(List<String> prognosticCatagoryIds) {
		this.prognosticCatagoryNames = prognosticCatagoryIds;
	}

//	public List<PrognosticCatagory> getPrognosticCatagoryList() {
//		return prognosticCatagoryList;
//	}
//
//	void setPrognosticCatagoryList(
//			List<PrognosticCatagory> prognosticCatagoryList) {
//		this.prognosticCatagoryList = prognosticCatagoryList;
//	}

	public List<String> getDiagnoseCatagoryNames() {
		return diagnoseCatagoryNames;
	}

	public void setDiagnoseCatagoryNames(List<String> diagnoseCatagoryIds) {
		this.diagnoseCatagoryNames = diagnoseCatagoryIds;
	}

//	public List<DiagnoseCatagory> getDiagnoseCatagoryList() {
//		return diagnoseCatagoryList;
//	}
//
//	void setDiagnoseCatagoryList(List<DiagnoseCatagory> diagnoseCatagoryList) {
//		this.diagnoseCatagoryList = diagnoseCatagoryList;
//	}

	public List<String> getSurgeryCatagoryNames() {
		return surgeryCatagoryNames;
	}

	public void setSurgeryCatagoryNames(List<String> surgeryCatagoryIds) {
		this.surgeryCatagoryNames = surgeryCatagoryIds;
	}

//	public List<SurgeryCatagory> getSurgeryCatagoryList() {
//		return surgeryCatagoryList;
//	}
//
//	void setSurgeryCatagoryList(List<SurgeryCatagory> surgeryCatagoryList) {
//		this.surgeryCatagoryList = surgeryCatagoryList;
//	}

	public String getSurgeryNote() {
		return surgeryNote;
	}

	public void setSurgeryNote(String surgeryNote) {
		this.surgeryNote = surgeryNote;
	}

	public String getNoteFromClient() {
		return noteFromClient;
	}

	public void setNoteFromClient(String noteFromClient) {
		this.noteFromClient = noteFromClient;
	}

	public String getNoteFromDr() {
		return noteFromDr;
	}

	public void setNoteFromDr(String noteFromDr) {
		this.noteFromDr = noteFromDr;
	}

	public String getSmallSurgery() {
		return smallSurgery;
	}

	public void setSmallSurgery(String smallSurgery) {
		this.smallSurgery = smallSurgery;
	}

	public String getAdviceFromDr() {
		return adviceFromDr;
	}

	public void setAdviceFromDr(String adviceFromDr) {
		this.adviceFromDr = adviceFromDr;
	}

	public TreeMap<String, TreeMap<String, TreeMap<String, Object>>> getPicInfos() {
		return picInfos;
	}

	public void setPicInfos(TreeMap<String, TreeMap<String, TreeMap<String, Object>>> picInfos) {
		this.picInfos = picInfos;
	}

	public List<PatientCaseEntity> getReExamInfo() {
		if (reExamInfo == null) {
			reExamInfo = new LinkedList<PatientCaseEntity>();
		}
		return reExamInfo;
	}

	public void setReExamInfo(List<PatientCaseEntity> reExamInfo) {
		this.reExamInfo = reExamInfo;
	}

	public String getSurgeryDateAsText() {
		return surgeryDateAsText;
	}

	public void setSurgeryDateAsText(String surgeryDateAsText) {
		this.surgeryDateAsText = surgeryDateAsText;
	}

	public Date getSurgeryDate() throws ParseException {
		if (surgeryDate == null) {
			surgeryDate = DateUtils.convertDateDataType(surgeryDateAsText);
		}
		return surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
		this.surgeryDateAsText = DateUtils.convertDateDataType(surgeryDate);
	}
	
	public AppointmentSchedule getAppoSchedule() {
		return appoSchedule;
	}

	public void setAppoSchedule(AppointmentSchedule appoSchedule) {
		this.appoSchedule = appoSchedule;
	}

	public boolean isComplication() {
		return complication;
	}

	public void setComplication(boolean complication) {
		this.complication = complication;
	}

	public boolean isBeautiful() {
		return beautiful;
	}

	public void setBeautiful(boolean beautiful) {
		this.beautiful = beautiful;
	}

	public void setDateAsText(String date) {
		this.dateAsText = date;
		this.date = null;
	}
	
	public String getDateAsText() {
		return dateAsText;
	}

	public Date getDate() throws ParseException {
		if (date == null) {
			date = DateUtils.convertDateDataType(dateAsText);
		}
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
		this.dateAsText = DateUtils.convertDateDataType(date);
	}

	public String getPrognosticOther() {
		return this.prognosticOther;
	}

	public String getDiagnoseOther() {
		return this.diagnoseOther;
	}

	public void setPrognosticOther(String prognosticOther) {
		this.prognosticOther = prognosticOther;
	}

	public void setDiagnoseOther(String diagnoseOther) {
		this.diagnoseOther = diagnoseOther;
	}
	
	public List<PrescriptionItem> getPrescription() {
		return prescription;
	}

	public void setPrescription(List<PrescriptionItem> prescription) {
		this.prescription = prescription;
	}

	void updateData(Long drCat,
			List<String> serviceList,
			List<String> progCatList, String prognosticOtherText,
			List<String> diagCatList, String diagOtherText,
			String noteFromPa, String noteFromDr,
			List<String> surList, String surgeryNote,
			Date surgeryDate, boolean complication, boolean beauty,
			String smallSurgery, String drAdvice, 
			Date nextApp, AppointmentCatagory appPurpose, String appNote, String paInfo) {
		
		if ((progCatList == null || progCatList.isEmpty()) && StringUtils.isBlank(prognosticOtherText)) {
			throw new InvalidParameterException(Messages.PatientCaseEntity_phaichontrieuchung);
		}
		if ((diagCatList == null || diagCatList.isEmpty()) && StringUtils.isBlank(diagOtherText)) {
			throw new InvalidParameterException(Messages.PatientCaseEntity_phaichonchandoan);
		}
		
		this.setDrId(drCat);
		this.setServiceNames(serviceList);
		this.setPrognosticCatagoryNames(progCatList);
		this.setPrognosticOther(prognosticOtherText);
		this.setDiagnoseCatagoryNames(diagCatList);
		this.setDiagnoseOther(diagOtherText);
		this.setNoteFromClient(noteFromPa);
		this.setNoteFromDr(noteFromDr);
		
		this.setSurgeryCatagoryNames(surList);
		this.setSurgeryNote(surgeryNote);
		this.setSurgeryDate(surgeryDate);
		this.setComplication(complication);
		this.setBeautiful(beauty);
		this.setSmallSurgery(smallSurgery);
		this.setAdviceFromDr(drAdvice);
		
		if (!ValidateUtils.validateIsAllEmpty(null, appPurpose, nextApp, appNote)) {
			if (this.appoSchedule == null) {
				this.appoSchedule = new AppointmentSchedule();
			}
			
			this.appoSchedule.setAppointmentCatagory(appPurpose);
			this.appoSchedule.setAppointmentDate(nextApp);
			this.appoSchedule.setNote(appNote);
			this.appoSchedule.setPaInfo(paInfo);
		}
	}

	public String getDiagnoseCatagoryNamesAsText(String separate) {
		String result = StringUtils.EMPTY;
		int index = 0;
		if (this.diagnoseCatagoryNames != null) {
			for (String dia : this.diagnoseCatagoryNames) {
				result += dia;
				if (index < this.diagnoseCatagoryNames.size() - 1) {
					result += separate;
				}
				index++;
			}
		}
		if (!StringUtils.isBlank(diagnoseOther)) {
			if (!StringUtils.EMPTY.equals(result)) {
				result += separate;
			}
			result += diagnoseOther;
		}
		
		return result;
	}

}