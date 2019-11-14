package com.iut.eHealth.dao;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.iut.eHealth.entity.AppointmentInfo;

public interface AppointmentInfoDAO {
	
	public List<AppointmentInfo> getAppointmentInfo(int doctorId);

	public void setAppointmentInfo(AppointmentInfo appointments);
	
	public List<AppointmentInfo> getPastPatientAppointments(int patientId);

}
