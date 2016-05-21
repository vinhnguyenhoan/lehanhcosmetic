package com.lehanh.pama.patientcase;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.lehanh.pama.IJsonDataObject;
import com.lehanh.pama.catagory.AppointmentCatagory;
import com.lehanh.pama.util.ValidateUtils;

public class AppointmentSchedule implements Serializable, IJsonDataObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1063676387287507607L;
	
	@Expose
	@SerializedName("appoId")
	private Long id;
	
	@Expose
	private Long patientId;
	
	@Expose
	@SerializedName("appoDate")
	private Date appointmentDate;
	
	@Expose
	@SerializedName("appoIsRes")
	private boolean resolved;
	
	@Expose
	@SerializedName("appoType")
	private Long appointmentType;
	private AppointmentCatagory appointmentCatagory;
	
	@Expose
	@SerializedName("appoNote")
	private String note;
	
	@Expose
	@SerializedName("paInfo")
	private String paInfo;
	
	public AppointmentSchedule(Long id, Long patientId, Date appointmentDate,
			Long appointmentType, String note, String paInfo, boolean resolved) {
		this.id = id;
		this.patientId = patientId;
		this.appointmentDate = appointmentDate;
		this.appointmentType = appointmentType;
		this.note = note;
		this.paInfo = paInfo;
		this.resolved = resolved;
	}
	
	public AppointmentSchedule() {
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(Date appointmentDate) {
		ValidateUtils.validateIsAllEmpty(Messages.AppointmentSchedule_phaichonngayhen, appointmentDate);
		this.appointmentDate = appointmentDate;
	}
	
	public boolean isResolved() {
		return resolved;
	}
	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}
	
	public Long getAppointmentType() {
		if (appointmentType == null) {
			return -1l;
		}
		return appointmentType;
	}
	public void setAppointmentType(Long appointmentType) {
		this.appointmentType = appointmentType;
	}
	
	public AppointmentCatagory getAppointmentCatagory() {
		return appointmentCatagory;
	}
	public void setAppointmentCatagory(AppointmentCatagory appointmentCatagory) {
		this.appointmentCatagory = appointmentCatagory;
		if (appointmentCatagory != null) {
			this.setAppointmentType(appointmentCatagory.getId());
		}
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public String getPaInfo() {
		return paInfo;
	}
	public void setPaInfo(String paInfo) {
		this.paInfo = paInfo;
	}

}
