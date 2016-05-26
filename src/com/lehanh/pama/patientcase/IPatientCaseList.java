package com.lehanh.pama.patientcase;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.DiagnoseCatagory;
import com.lehanh.pama.catagory.DrCatagory;
import com.lehanh.pama.catagory.PrognosticCatagory;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.catagory.SurgeryCatagory;

public interface IPatientCaseList {

	boolean isListRootCase();
	
	PatientCaseEntity createDetailCase(PatientCaseStatus status);
	IPatientCaseList createRootCase();
	
	void updateCase(PatientCaseEntity paCaseEntity, DrCatagory drCat,
			List<ServiceCatagory> serviceList,
			List<PrognosticCatagory> progCatList, String prognosticOtherText,
			List<DiagnoseCatagory> diagCatList, String diagOtherText,
			String noteFromPa, String noteFromDr,
			List<SurgeryCatagory> surList, String surgeryNote,
			Date surgeryDate, boolean complication, boolean beauty,
			String smallSurgery, String drAdvice, Date nextApp,
			AppointmentCatagory appPurpose, String appNote, String patientInfo);

	Object[] getAllVersions();
	IPatientCaseList getSubList(int parentId);
	
	PatientCaseEntity getLastExamHaveStatus(PatientCaseStatus... caseStatus);
	boolean isNotHaveAnyEntity();

	boolean isLastCreatedExam(PatientCaseEntity selectedEntity);
	boolean isCreatingExam(PatientCaseEntity entity);

	String getSummary(boolean includeConsult);
	String getSurgerySummary();

	ISurgeryImageList getSurgeryImageList();

	IPrescription getPharmacyList(int rootId, int detalId);

	int calculateDateAfterRoot(int rootId, int id) throws ParseException;

}