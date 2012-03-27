package no.ntnu.fp.model;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Calendar;

import no.ntnu.fp.model.XmlSerializer.RequestDataType;
import no.ntnu.fp.net.co.Connection;

/**
 * @author Sebastian Zalewski
 */
public class DatabaseUpdateManager {
	private Connection connection;
	private XmlSerializer xmlSerializer = new XmlSerializer();
	
	/**
	 * Constructor
	 * 
	 * @param connection - The connection that the client uses to send the data to server.
	 */
	public DatabaseUpdateManager(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Sends login information to server.
	 * 
	 * @param email - The email to the person that is trying to login.
	 * @param password - The password to the person that is trying to login.
	 */
	public void sendLogin(String email, String password) {
		String xml = xmlSerializer.loginToXml(email, password).toXML();
		send(xml);
	}
	
	/**
	 * Sends logout information to server.
	 * 
	 * @param email - The email to the person that is trying to logout.
	 */
	public void sendLogout(String email) {
		String xml = xmlSerializer.logoutToXml(email).toXML();
		send(xml);
	}

	/**
	 * Sends a request to the server regarding employees.
	 */
	public void requestEmployees() {
		String xml = xmlSerializer.requestDataToXml(RequestDataType.PERSONS).toXML();
		send(xml);
	}
	
	/**
	 * Sends a request to the server regarding appointments to a specified person in a given time interval.
	 * 
	 * @param email - The email that identifies a person.
	 * @param start - The start of the time interval.
	 * @param end - The end of the time interval.
	 */
	public void requestAppointments(String email, Calendar start, Calendar end) {
		String xml = xmlSerializer.requestDataToXml(RequestDataType.PERSON_APPOINTMENTS, email, start, end).toXML();
		send(xml);
	}
	
	/**
	 * Sends a request to the server regarding meeting rooms.
	 */
	public void requestMeetingRooms() {
		String xml = xmlSerializer.requestDataToXml(RequestDataType.MEETING_ROOMS).toXML();
		send(xml);
	}
	
	/**
	 * Sends a request to the server regarding occupied time points in a specified meeting room <br />
	 * from the execution time of this method.
	 * 
	 * @param meetingRoomName - The name of the meeting room.
	 */
	public void requestMeetingRoomOccupiedTime(String meetingRoomName) {
		String xml = xmlSerializer.requestDataToXml(RequestDataType.MEETING_ROOM_OCCUPIED_TIME, meetingRoomName).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to create a new appointment.
	 */
	public void sendCreateAppointment() {
		String xml = xmlSerializer.createAppointmentToXml().toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to remove a specified appointment.
	 * 
	 * @param appointmentId - The appointment id to the specified appointment.
	 */
	public void sendRemoveAppointment(long appointmentId) {
		String xml = xmlSerializer.removeAppointmentToXml(appointmentId).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to update an appointment title.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its title.
	 * @param title - The new title of the appointment.
	 */
	public void sendUpdateAppointmentTitle(long appointmentId, String title) {
		Appointment appointment = new Appointment(appointmentId);
		appointment.setTitle(title);
		String xml = xmlSerializer.updateDataToXml(appointment).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to update an appointment description.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its description.
	 * @param description - The new description to the appointment.
	 */
	public void sendUpdateAppointmentDescription(long appointmentId, String description) {
		Appointment appointment = new Appointment(appointmentId);
		appointment.setDescription(description);
		String xml = xmlSerializer.updateDataToXml(appointment).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to update an appointment endTime.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its end time.
	 * @param endTime - The new appointment end time.
	 */
	public void sendUpdateAppointmentEndTime(long appointmentId, Calendar endTime) {
		Appointment appointment = new Appointment(appointmentId);
		appointment.setTime(null, endTime);
		String xml = xmlSerializer.updateDataToXml(appointment).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to update an appointment start time.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its start time.
	 * @param startTime - The new appointment start time.
	 */
	public void sendUpdateAppointmentStartTime(long appointmentId, Calendar startTime) {
		Appointment appointment = new Appointment(appointmentId);
		appointment.setTime(startTime, null);
		String xml = xmlSerializer.updateDataToXml(appointment).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to update an appointment meeting room.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its meeting room.
	 * @param meetingRoomName - The name of the new meeting room.
	 */
	public void sendUpdateAppointmentMeetingRoom(long appointmentId, String meetingRoomName) {
		Appointment appointment = new Appointment(appointmentId);
		appointment.setMeetingRoomName(meetingRoomName);
		String xml = xmlSerializer.updateDataToXml(appointment).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells it to update the participants of a specified appointment.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its participants.
	 * @param persons - The new participants of the appointment.
	 */
	public void sendUpdateAppointmentParticipants(long appointmentId, Person[] persons) {
		Appointment appointment = new Appointment(appointmentId);
		appointment.setParticipants(persons);
		String xml = xmlSerializer.updateDataToXml(appointment).toXML();
		send(xml);
	}
	
	/**
	 * Sends a message to the server that tells if a specified person has accepted or declined an appointment.
	 * 
	 * @param notificationId - The id to the specified notification.
	 * @param email - The email to the specified person.
	 * @param isAccepted - If the appointment is accepted or declined.
	 */
	public void sendAcceptAppointment(long notificationId, String email, boolean isAccepted) {
		String xml = xmlSerializer.updateDataToXml(notificationId, email, isAccepted).toXML();
		send(xml);
	}
	
	/**
	 * Sends an XML string to the server.
	 * 
	 * @param xml - The XML string to be sent to the server.
	 */
	private void send(String xml) {
		try {
			connection.send(xml);
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
