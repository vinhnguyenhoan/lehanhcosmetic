package com.lehanh.pama.patientcase;

public interface IPatientViewPartListener {

	void patientChanged(Patient oldPa, Patient newPa, String[] callIds);

	void patientCaseChanged(PatientCaseEntity oldCase, PatientCaseEntity newCase, int rootCase, String[] callIds);

}
