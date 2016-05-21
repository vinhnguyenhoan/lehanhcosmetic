package com.lehanh.pama.patientcase;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IAppointmentManager {

	int clearOldAppointment();

	List<AppointmentSchedule> searchPatientAppointment(Date selection) throws SQLException, ParseException;

}
