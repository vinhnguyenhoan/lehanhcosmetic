package com.lehanh.pama.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.lehanh.pama.db.DatabaseManager;
import com.lehanh.pama.patientcase.Patient;
import com.lehanh.pama.util.DateUtils;

public class PatientDao implements IDao {

	private static final String PATIENT_TABLE = "patient";
//  private static final String `id` SMALLINT NOT NULL AUTO_INCREMENT,
//	private static final String IMAGE_NAME = `image_name` VARCHAR(70),
//	private static final String `name` VARCHAR(70) NOT NULL,
//	private static final String `address` VARCHAR(150),
//	private static final String `birth_day` DATE NOT NULL,
//	private static final String `is_fermale` tinyint(1) NOT NULL,
//	private static final String `cell_phone` VARCHAR(12),
//	private static final String `phone` VARCHAR(15),
//	private static final String `email` VARCHAR(70),
//	private static final String `career` VARCHAR(70),	
//	private static final String `last_visit` DATE,
//	private static final String `last_surgery` DATE,
//	private static final String `next_appointment` DATE,
//	private static final String `patient_level` tinyint,
//	private static final String `note` VARCHAR(700),
//	private static final String `medical_personal_info` VARCHAR(5000),
	
	public boolean insert(Patient patient) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			String sql = "insert into " + PATIENT_TABLE + 
					" (" + 
					(patient.getId() != null ? " id, " : "") + 
					"image_name, name, address, birth_day, is_fermale, cell_phone, phone, email, career, last_visit, last_surgery, " +
						"next_appointment, patient_level, note, medical_personal_info) " + 
					" values ("+
					(patient.getId() != null ? " ?, " : "") +
					"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			if (patient.getId() != null) {
				ps.setLong(i++, patient.getId());
			}
			ps.setString(i++, patient.getImageName());
			ps.setString(i++, patient.getName());
			ps.setString(i++, patient.getAddress());
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getBirthDay()));
			ps.setBoolean(i++, patient.isFermale());
			ps.setString(i++, patient.getCellPhone());
			ps.setString(i++, patient.getPhone());
			ps.setString(i++, patient.getEmail());
			ps.setString(i++, patient.getCareer());
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getLastVisit()));
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getLastSurgery()));
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getNextAppointment()));
			ps.setInt(i++, patient.getPatientLevel());
			ps.setString(i++, patient.getNote());
			ps.setString(i++, patient.getMedicalPersonalInfoText());

			System.out.println("SQL: " + ps);
			int resultUpdate = ps.executeUpdate();
			if (resultUpdate <= 0) {
				return false;
			}
			ResultSet resultIdKey = ps.getGeneratedKeys();
			if (resultIdKey.next()) {
				Long resultId = resultIdKey.getLong(1);
				patient.setId(resultId);
				return true;
			}
			throw new SQLException("Insert statement did not generate an AutoID"); //$NON-NLS-1$
			
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}
	
	public void update(Patient patient) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement(
					"update " + PATIENT_TABLE + 
					" set " +
					" image_name=?, name=?, address=?, birth_day=?, is_fermale=?, cell_phone=?, phone=?, email=?, career=?, " +
						" last_visit=?, last_surgery=?, next_appointment=?, " +
						" patient_level=?, note=?, medical_personal_info=? " + 
					" where id=? ");

			int i = 1;
			ps.setString(i++, patient.getImageName());
			ps.setString(i++, patient.getName());
			ps.setString(i++, patient.getAddress());
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getBirthDay()));
			ps.setBoolean(i++, patient.isFermale());
			ps.setString(i++, patient.getCellPhone());
			ps.setString(i++, patient.getPhone());
			ps.setString(i++, patient.getEmail());
			ps.setString(i++, patient.getCareer());
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getLastVisit()));
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getLastSurgery()));
			ps.setDate(i++, SqlUtils.newSqlDate(patient.getNextAppointment()));
			ps.setInt(i++, patient.getPatientLevel());
			ps.setString(i++, patient.getNote());
			ps.setString(i++, patient.getMedicalPersonalInfoText());

			ps.setLong(i++, patient.getId());
			System.out.println("SQL: " + ps);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public List<Patient> searchPatient(Long id, Date lastUpdate, String name, String phone) throws SQLException, ParseException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			
			List<String> whereClauseQ = new LinkedList<String>();
			if (id != null) {
				whereClauseQ.add("id = ?");
			}
			if (lastUpdate != null) {
				//whereClauseQ.add("last_visit = ?");
				whereClauseQ.add("medical_personal_info LIKE ?");
			}
			if (name != null) {
				whereClauseQ.add("lower(name) LIKE ?");
			}
			if (phone != null) {
				whereClauseQ.add("lower(cell_phone) LIKE ?");
			}
			
			List<Patient> result = new LinkedList<Patient>();
			if (whereClauseQ.isEmpty()) {
				return result; 
			}
			
			String whereClause = StringUtils.EMPTY;
			for (String clause : whereClauseQ) {
				if (StringUtils.EMPTY.equals(whereClause)) {
					whereClause = clause;
				} else {
					whereClause += " AND " + clause;
				}
			}
			
			ps = conn.prepareStatement("select * from " + PATIENT_TABLE + " where " + whereClause);
			int index = 1;
			if (id != null) {
				ps.setLong(index++, id);
			}
			if (lastUpdate != null) {
				//ps.setDate(index++, DateUtils.utilDateToSqlDate(lastUpdate));
				//"date":"06/08/2004"
				String dateQ = "%\"date\":\"" + DateUtils.convertDateDataType(lastUpdate) + "\"%";
				ps.setString(index++, dateQ);
			}
			if (name != null) {
				ps.setString(index++, "%" + name.toLowerCase().trim() + "%");
			}
			if (phone != null) {
				ps.setString(index++, "%" + phone.toLowerCase().trim() + "%");
			}
			
			System.out.println("SQL: " + ps);
			rs = ps.executeQuery();
			while (rs.next()) {
				Patient cat = populate(rs);
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

	public static final Patient populate(ResultSet rs) throws SQLException, ParseException {
		//image_name, name, address, birth_day, is_fermale, cell_phone, phone, email, career, last_visit, last_surgery, " +
		//"next_appointment, patient_level, note, medical_personal_info
		
		Patient patient = new Patient();
		patient.setId(rs.getLong("id"));
		patient.setAddress(rs.getString("address"));
		patient.setBirthDay(DateUtils.sqlDateToutilDate(rs.getDate("birth_day")));
		patient.setCareer(rs.getString("career"));
		patient.setCellPhone(rs.getString("cell_phone"));
		patient.setEmail(rs.getString("email"));
		patient.setFermale(rs.getBoolean("is_fermale"));
		patient.setImageName(rs.getString("image_name"));
		patient.setLastSurgery(DateUtils.sqlDateToutilDate(rs.getDate("last_surgery")));
		patient.setLastVisit(DateUtils.sqlDateToutilDate(rs.getDate("last_visit")));
		patient.setName(rs.getString("name"));
		patient.setNextAppointment(DateUtils.sqlDateToutilDate(rs.getDate("next_appointment")));
		patient.setNote(rs.getString("note"));
		patient.setPatientLevel(rs.getInt("patient_level"));
		patient.setPhone(rs.getString("phone"));
		patient.setMedicalPersonalInfoText(rs.getString("medical_personal_info"));
		
		return patient;
	}

	public void deleteAll() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = DatabaseManager.getInstance().getConn();
			ps = conn.prepareStatement("delete FROM " + PATIENT_TABLE);
			System.out.println("SQL: " + ps);
			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}	
	}

}