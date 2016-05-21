package com.lehanh.pama.patientcase;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.lehanh.pama.IService;
import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.catagory.DiagnoseCatagory;
import com.lehanh.pama.catagory.DrCatagory;
import com.lehanh.pama.catagory.PrognosticCatagory;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.catagory.SurgeryCatagory;
import com.lehanh.pama.ui.util.ObjectToUIText;

public interface IPatientManager extends IService {

	Patient getCurrentPatient();
	
	void addPaListener(IPatientViewPartListener paL, String id);
	
	IPatientSearcher getPatientSearcher();
	
	IAppointmentManager getAppointmentManager();

	void selectPatient(String uiId, Patient newPatient);
	void selectPatient(String uid, Long patientId) throws SQLException, ParseException;
	void selectDetailPatientCase(String uiId, PatientCaseEntity detailEntity, int rootCase);

	Patient updatePatient(String uiId, String imagePath, String name, String address,
			Date birthDay, boolean isFermale, String cellPhone, String phone,
			String email, String career, int patientLevel, String note, String detailExam,
			String medicalHistory, String anamnesis);

	void updatePatient(String uid, boolean notify);

	void updatePatientCase(String uiId, Integer rootCaseId, PatientCaseEntity paCaseEntity,
			DrCatagory drCat,
			List<ServiceCatagory> serviceList,
			List<PrognosticCatagory> progCatList, String prognosticOtherText,
			List<DiagnoseCatagory> diagCatList, String diagOtherText,
			String noteFromPa, String noteFromDr,
			List<SurgeryCatagory> surList, String surgeryNote,
			Date surgeryDate, boolean complication, boolean beauty,
			String smallSurgery, String drAdvice, Date nextApp,
			AppointmentCatagory appPurpose, String appNot, ObjectToUIText<Patient, Long> toPatientInfo);
	
	void cancelEditingPatientCase();

	PatientCaseEntity getCurrCase();

}
