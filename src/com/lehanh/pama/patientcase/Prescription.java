package com.lehanh.pama.patientcase;

import java.util.List;
import java.util.TreeMap;

import com.lehanh.pama.catagory.PrescriptionItem;

public class Prescription implements IPrescription {

	private final Long patientId;

	private final TreeMap<String, PrescriptionItem> data;

	private PatientCaseEntity patientCase;
	
	Prescription(Long patientId, PatientCaseEntity patientCase) {
		this.patientId = patientId;
		this.patientCase = patientCase;
		this.data = new TreeMap<String, PrescriptionItem>();
		List<PrescriptionItem> pre = this.patientCase.getPrescription();
		if (pre != null) {
			for (PrescriptionItem item : pre) {
				data.put(item.getDrug(), item);
			}
		}
	}

}
