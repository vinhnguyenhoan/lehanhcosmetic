package com.lehanh.pama.patientcase;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lehanh.pama.IJsonDataObject;
import com.lehanh.pama.util.JsonMapper;

public class MedicalPersonalInfo implements Serializable, IJsonDataObject {
	
	/*
	 * Auto gen
	 */
	private static final long serialVersionUID = -912854231669513801L;
	
	@Expose
	@SerializedName("allPa")
	private List<PatientCaseEntity> patientCases;
	private PatientCaseList patientCaseList;
	
	@Expose
	@SerializedName("summary")
	//private PatientCaseSummary patientCaseSummary;
	private String summary;
	
	@Expose
	@SerializedName("ana")
	private String anamnesis;
	@Expose
	@SerializedName("mH")
	private String medicalHistory;

	private Long patientId;
	
	public MedicalPersonalInfo() {
	}
	
	public MedicalPersonalInfo(Long patientId) {
		this.patientId = patientId;
	}
	
	void setPatientId(Long pId) {
		this.patientId = pId;
	}
	
	public IPatientCaseList getPatientCaseList() {
		if (patientCaseList == null) {
			patientCaseList = new PatientCaseList(patientId, getPatientCases());
		}
		return this.patientCaseList;
	}

	public List<PatientCaseEntity> getPatientCases() {
		if (this.patientCases == null) {
			this.patientCases = new LinkedList<PatientCaseEntity>();
		}
		return this.patientCases;
	}

	public void setPatientCases(List<PatientCaseEntity> pa) {
		this.patientCases = pa;
	}
	
	public  String getSummary() {
		return this.summary;
	}
	
	public void setSummary(String text) {
		this.summary = ((PatientCaseList) this.getPatientCaseList()).setSummary(text);
	}
	
	public String getAnamnesis() {
		return anamnesis;
	}

	public void setAnamnesis(String anamnesis) {
		this.anamnesis = anamnesis;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public void setMedicalHistory(String medicalHistory) {
		this.medicalHistory = medicalHistory;
	}

	public static void main(String[] args) {
		MedicalPersonalInfo mI = new MedicalPersonalInfo(1l);
		List<PatientCaseEntity> patientCase = new LinkedList<PatientCaseEntity>();

		PatientCaseEntity p1 = new PatientCaseEntity();
		List<PatientCaseEntity> p1List = new LinkedList<PatientCaseEntity>();
		PatientCaseEntity p11 = new PatientCaseEntity();
		p1List.add(p11);
		p1.setReExamInfo(p1List);

		PatientCaseEntity p2 = new PatientCaseEntity();

		patientCase.add(p1);
		patientCase.add(p2);
		mI.patientCases = patientCase;

		mI.anamnesis = null;

		String medicalHistory = "khong benh";
		mI.medicalHistory = medicalHistory;

		String json = JsonMapper.toJson(mI);
		System.out.println(json);

		MedicalPersonalInfo mI2 = JsonMapper.fromJson(json,
				MedicalPersonalInfo.class);
		System.out.println(mI2);
	}
}