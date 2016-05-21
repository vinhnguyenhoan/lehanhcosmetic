package com.lehanh.pama.patientcase;

public enum PatientCaseStatus {

	CONSULT(Messages.PatientCaseStatus_tuvan),
	//SURGERY("Phẩu thuật"),
	EXAM(Messages.PatientCaseStatus_thamkham),
	//NEW("Đang thực hiện")
	;

	public final String desc;
	
	private PatientCaseStatus(String desc) {
		this.desc = desc;
	}
	
}
