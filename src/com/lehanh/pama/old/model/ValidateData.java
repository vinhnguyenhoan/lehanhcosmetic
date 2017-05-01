package com.lehanh.pama.old.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.lehanh.pama.catagory.Catagory;
import com.lehanh.pama.catagory.CatagoryManager;
import com.lehanh.pama.catagory.CatagoryType;
import com.lehanh.pama.catagory.ServiceCatagory;
import com.lehanh.pama.db.DatabaseManager;
import com.lehanh.pama.db.dao.PatientDao;
import com.lehanh.pama.patientcase.IPatientCaseList;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.patientcase.PatientCaseEntity;
import com.lehanh.pama.ui.util.MainApplication;
import com.lehanh.pama.util.PamaHome;

public class ValidateData {

	private static final boolean SHOW_PATIENT_INFO = false;
	
	public static void main(String[] args) throws SQLException, IOException, ParseException {
		checkDeletedSurgery();
	}

	private static void checkDeletedSurgery() throws SQLException, IOException, ParseException {
		PamaHome.application = new MainApplication();
		DatabaseManager.initialize();

		CatagoryManager catM = new CatagoryManager();
		catM.initialize();
		
		Map<String, Catagory> mapSgName = new HashMap<>();
		for (Catagory sg : catM.getCatagoryByType(CatagoryType.SURGERY).values()) {
			mapSgName.put(sg.getName(), sg);
		}

		List<Patient> allPa = loadAllPatient();
		
		Set<String> surgeryDeleted = new HashSet<>();
		boolean printedPa = false;
		for (Patient pa : allPa) {
			printedPa = false;
			if (pa.getId() == 17163) {
				System.out.println("DEBUG");
			}
			
			IPatientCaseList paList = pa.getMedicalPersonalInfo().getPatientCaseList();
			for (Object entityObj : paList.getAllVersions()) {
				PatientCaseEntity paCase = (PatientCaseEntity) entityObj;
				IPatientCaseList detailCases = paList.getSubList(paCase.getId());
				
				for (Object detailObj : detailCases.getAllVersions()) {
					PatientCaseEntity detailPa = (PatientCaseEntity) detailObj;
					
					for (String surgery : detailPa.getSurgeryCatagoryNames()) {
						if (mapSgName.containsKey(surgery)) {
							continue;
						}
						// Log surgery deleted here
						if (!surgeryDeleted.contains(surgery)) {
							if (SHOW_PATIENT_INFO && !printedPa) {
								System.out.println("Patient: " + pa.getName() + " - " + pa.getId());
								printedPa = true;
							}
							System.out.println(surgery);
						}
						surgeryDeleted.add(surgery);
					}
				}
			}
		}
		
		// Show all
		System.out.println("Summay");
		Arrays.toString(surgeryDeleted.toArray());
	}
	
	public static final List<Patient> loadAllPatient() throws SQLException, ParseException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement("select * from patient");
			List<Patient> result = new LinkedList<Patient>();
			
			System.out.println("SQL: " + ps);
			rs = ps.executeQuery();
			while (rs.next()) {
				Patient cat = PatientDao.populate(rs);
				result.add(cat);
			}
			return result;
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
}
