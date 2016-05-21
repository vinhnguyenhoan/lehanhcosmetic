package com.lehanh.pama.patientcase;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IPatientSearcher {

	Patient getPatientDetailById(Long id) throws SQLException, ParseException;
	
	List<Patient> searchPatient(Date lastUpdate, String name, String phone) throws SQLException, ParseException;

}