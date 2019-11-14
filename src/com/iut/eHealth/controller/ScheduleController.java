package com.iut.eHealth.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iut.eHealth.entity.AppointmentInfo;
import com.iut.eHealth.entity.DoctorInfo;
import com.iut.eHealth.entity.PatientInfo;
import com.iut.eHealth.entity.StatelessSchedule;
import com.iut.eHealth.service.ScheduleService;

@Controller
@RequestMapping("/")
public class ScheduleController {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@RequestMapping("/")
	public String homepage(Model theModel) {
		
		return "homepage";
	}
	
	@RequestMapping("/doctorList")
	public String doctorList(Model theModel) {
		
		List<DoctorInfo> doctors = scheduleService.getDoctors();
		
		theModel.addAttribute("doctors",doctors);
		
		/*Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);*/
		
		return "doctorList";
		
	}
	
	@RequestMapping("/scheduleList")
	public String ScheduleList(Model theModel,
			@RequestParam(value="id", required=false) int id,
			@RequestParam(value="appointDate", required=false) String appointDate,
			HttpSession session) throws ParseException {
		
		session.setAttribute("doctorId", id);
		List<DoctorInfo> doctor = scheduleService.getDoctors();	
		
		DoctorInfo temp = new DoctorInfo();
		temp = scheduleService.getDoctor(id);
		theModel.addAttribute("doctors",temp);
		
		List<StatelessSchedule> schedule = scheduleService.getSchedule();
		
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String strDate = dateFormat.format(date);
		/*Date appDate = dateFormat.parse(appointDate);
		String appDateStr = dateFormat.format(appDate);
		appDate = dateFormat.parse(appDateStr);
		long dateDifference = appDate.getTime()-date.getTime();
		System.out.println(date.toString()+" "+appointDate.toString()+" "+dateDifference+"%%%%%%%%%%%%%%%%%%%");*/
		
		List<AppointmentInfo>appointments = scheduleService.getAppointmentInfo(id);
		
		//System.out.println("Date is - "+strDate+"++++++++++++++++++++++++++++++++++++++++++++++");
		
		for(int i=0;i<schedule.size();i++) {
		
			for(int j=0;j<appointments.size();j++) {
				String str1 = appointments.get(j).getStartTime().trim();
				String str2 = schedule.get(i).getStartTime().trim();
				
				String checkDate1 = appointments.get(j).getDate().trim();
				strDate = strDate.trim();
				if(str1.equals(str2) && strDate.equals(checkDate1)) {
					schedule.get(i).setFlag(true);
				}
			}
		}
		theModel.addAttribute("schedule",schedule);
		
		return "schedule-list";
		
		}
	
	@RequestMapping("/appointmentBooking")
	public String BookingInfo(Model theModel,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime, HttpSession session) {
		
		String loggedIn = (String) session.getAttribute("loggedIn");
		
		if("true".equals(loggedIn))
		{
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(date);
			System.out.println("Date is - "+strDate);
			
			AppointmentInfo appointments = new AppointmentInfo();
			
			int doctorId = (int) session.getAttribute("doctorId");
			DoctorInfo theDoctor = scheduleService.getDoctor(doctorId);
			
			int patientId = (int) session.getAttribute("patientId");
			PatientInfo thePatient = scheduleService.getPatient(patientId);
			
			appointments.setDoctorId(doctorId);
			appointments.setDoctorName(theDoctor.getName());
			appointments.setPatientId(patientId);
			appointments.setPatientName(thePatient.getName());
			appointments.setStartTime(startTime);
			appointments.setEndTime(endTime);
			appointments.setDate(strDate);
			
			scheduleService.setAppointmentInfo(appointments);
			return "forward:/scheduleList?id="+doctorId;
		}
		else
		{
			return "forward:/logIn";
		}
	}
	
	@RequestMapping("signIn")
	public String signIn(Model theModel)
	{
		PatientInfo patient = new PatientInfo();
		
		theModel.addAttribute("patient", patient);
		
		return "signIn";
	}
	
	@RequestMapping("savePatient")
	public String savePatient(@ModelAttribute("patient") PatientInfo thePatient,
							Model theModel,HttpSession session)
	{
		boolean flag = scheduleService.checkPatient(thePatient.getEmail());
		
		if(flag==false) {
			scheduleService.savePatient(thePatient);

			session.setAttribute("loggedIn", "true");
			session.setAttribute("patientId", thePatient.getId());
			session.setAttribute("signInFailed", "false");
			return "redirect:/";
		}
		else {
			session.setAttribute("signInFailed", "true");
			return "forward:/signIn";
		}
		
		
	}
	
	@RequestMapping("logIn")
	public String logIn(Model theModel)
	{
		
		return "logIn";
	}
	
	@RequestMapping("verifyPatient")
	public String verifyPatient(Model theModel,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			HttpSession session)
	{
		
		int id = scheduleService.verifyPatient(email,password);
		
		if(id==-1)
		{
			session.setAttribute("loggedIn", "false");
			theModel.addAttribute("loginFailed","true");
			return "forward:/logIn";
		}
		else
		{
			session.setAttribute("loggedIn", "true");
			theModel.addAttribute("loginFailed","false");
			PatientInfo thePatient = scheduleService.getPatient(id);
			session.setAttribute("patientId", thePatient.getId());
			session.setAttribute("patientName", thePatient.getName());
			return "forward:/";
		}
	}
	
	@RequestMapping("logout")
	public String logout(Model theModel, HttpSession session) {
		
		//session.setAttribute("loggedIn", "false");
		//session.setAttribute("patientId", -1);
		//session.setAttribute("patientName", null);
		
		session.invalidate();
		
		return "homepage";
	}
	
	@RequestMapping("patientHistory")
	public String patientHistory(Model theModel, HttpSession session) {
		
		int id = (int) session.getAttribute("patientId");
		PatientInfo thePatient = scheduleService.getPatient(id);
		theModel.addAttribute("thePatient",thePatient);
		
		List<AppointmentInfo> patientHistory = scheduleService.getPastPatientAppointment(id);
		theModel.addAttribute("patientHistory",patientHistory);
		for(int i=0;i<patientHistory.size();i++)
		{
			System.out.println(patientHistory.get(i).toString());
		}
		return "patientHistory";
	}

}
