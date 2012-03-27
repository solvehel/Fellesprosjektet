package no.ntnu.fp.model;

import java.util.Calendar;
import java.util.Map;

/**
 * @author Sebastian Zalewski
 */
public class ModelManager {
	DatabaseUpdateManager databaseUpdateManager;
	ModelUpdateManager modelUpdateManager;
	
	/**
	 * @param email - The email to the person that is login in.
	 * @param password - The password to the user.
	 * @return true if the login was successful 
	 */
	public boolean login(String email, String password) {
		databaseUpdateManager.sendLogin(email, password);
		return modelUpdateManager.getRequestedLoginResult();
	}
	
	/**
	 * @param email - The email to the person that is login off.
	 */
	public void logout(String email) {
		databaseUpdateManager.sendLogout(email);
	}
	
	/**
	 * Returns the employees from the server.
	 * <br /><br />
	 * NB! If you use the set methods in the returned objects you are only making an <br />
	 * update to the objects on the client, not the server!
	 * 
	 * @return an array of Person objects
	 */
	public Person[] getEmployees() {
		databaseUpdateManager.requestEmployees();
		return modelUpdateManager.getRequestedEmployees();
	}
	
	/**
	 * Returns the appointments to the specified person in the specified time interval, <br />
	 * from the server.
	 * <br /><br />
	 * NB! If you use the set methods in the returned objects you are only making an <br />
	 * update to the objects on the client, not the server!
	 * 
	 * @param email - The email that identifies the person.
	 * @param start - The start of the time interval.
	 * @param end - The end of the time interval.
	 * @return the appointments to the specified person in the specified time interval
	 */
	public Appointment[] getAppointments(String email, Calendar start, Calendar end) {
		databaseUpdateManager.requestAppointments(email, start, end);
		Appointment[] appointments = modelUpdateManager.getRequestedAppointments();
		return appointments;
	}
	
	/**
	 * Returns the meeting rooms from the server.
	 * <br /><br />
	 * NB! If you use the set methods in the returned objects you are only making an <br />
	 * update to the objects on the client, not the server!
	 * 
	 * @return an array of MeetingRoom objects
	 */
	public MeetingRoom[] getMeetingRooms() {
		databaseUpdateManager.requestMeetingRooms();
		return modelUpdateManager.getRequestedMeetingRooms();
	}
	
	/**
	 * The method returns all the start and end times to the appointments taking place in the specified meeting room, <br />
	 * from the execution time of this method.
	 * 
	 * @param meetingRoomName - The unique identifier to the specified meeting room.
	 * @return a map containing the occupied time in the specified meeting room in the given time interval
	 */
	public Map<Calendar, Calendar> getMeetingRoomOccupiedTime(String meetingRoomName) {
		databaseUpdateManager.requestMeetingRoomOccupiedTime(meetingRoomName);
		return modelUpdateManager.getRequestedMeetingRoomOccupiedTime();
	}
	
	/**
	 * Finds and returns an available meeting room. <br />
	 * NB! The meeting room is not set! <br />
	 * setAppointmentMeetingRoom(..) must be called to set the meeting room
	 * <br /><br />
	 * NB! If you use the set methods in the returned object you are only making an <br />
	 * update to the object on the client, not the server!
	 * 
	 * @param startTime - The start time of an appointment.
	 * @param endTime - The end time of an appointment.
	 * @return the available meeting room (null if meeting room is not found)
	 */
	public MeetingRoom findAndReturnMeetingRoom(Calendar startTime, Calendar endTime) {
		MeetingRoom[] meetingRooms = getMeetingRooms();
		
		for (int i = 0; i < meetingRooms.length; i++) {
			Map<Calendar, Calendar> occupiedTimePoints = getMeetingRoomOccupiedTime(meetingRooms[i].getName());
			if (MeetingRoom.isTimeAvailable(occupiedTimePoints, startTime, endTime))
				return meetingRooms[i];
		}
		
		return null;
	}
	
	/**
	 * Creates a new appointment in the database.
	 * Returns an appointment id that can be used to change or remove this appointment.
	 * 
	 * @return the appointment id
	 */
	public long createAppointment() {
		databaseUpdateManager.sendCreateAppointment();
		return modelUpdateManager.getRequestedAppointmentId();
	}
	
	/**
	 * Removes the specified appointment from the database.
	 * 
	 * @param appointmentId - the id to the appointment that is going to be removed
	 */
	public void removeAppointment(long appointmentId) {
		databaseUpdateManager.sendRemoveAppointment(appointmentId);
	}
	
	/**
	 * Updates the appointment title to the specified appointment in the database.
	 * 
	 * @param appointmentId - The id to the appointment that is going to change its title.
	 * @param title - The new title of the appointment.
	 */
	public void setAppointmentTitle(long appointmentId, String title) {
		databaseUpdateManager.sendUpdateAppointmentTitle(appointmentId, title);
	}
	
	/**
	 * Updates the appointment description to the specified appointment in the database.
	 * 
	 * @param appointmentId - The id to the specified appointment.
	 * @param description - The new description to the appointment.
	 */
	public void setAppointmentDescription(long appointmentId, String description) {
		databaseUpdateManager.sendUpdateAppointmentDescription(appointmentId, description);
	}
	
	/**
	 * Sets the appointment time and sets the meeting room to null in the database.
	 * 
	 * @param appointmentId - The id to the specified appointment.
	 * @param startTime - The new start time to the appointment.
	 * @param endTime - The new end time to the appointment.
	 */
	public void setAppointmentTime(long appointmentId, Calendar startTime, Calendar endTime) {
		databaseUpdateManager.sendUpdateAppointmentStartTime(appointmentId, startTime);
		databaseUpdateManager.sendUpdateAppointmentEndTime(appointmentId, endTime);
		databaseUpdateManager.sendUpdateAppointmentMeetingRoom(appointmentId, "");
	}
	
	/**
	 * Sets the appointment meeting room in the database.
	 * 
	 * @param appointmentId - The id to the specified appointment.
	 * @param meetingRoomName - The name to the new meeting room.
	 */
	public void setAppointmentMeetingRoom(long appointmentId, String meetingRoomName) {
		databaseUpdateManager.sendUpdateAppointmentMeetingRoom(appointmentId, meetingRoomName);
	}
	
	/**
	 * Sets the participants to the specified appointment in the database.
	 * 
	 * @param appointmentId - The id to the specified appointment.
	 * @param person - An array of Person objects containing the appointment participants.
	 */
	public void setAppointmentParticipants(long appointmentId, Person[] person) {
		databaseUpdateManager.sendUpdateAppointmentParticipants(appointmentId, person);
	}
	
	/**
	 * Every notification must be answered. If the answer is OK then isAccepted must be false!
	 * 
	 * @param notificationId - The id to the specified notification.
	 * @param email - The email to the person that is giving an answer to the notification.
	 * @param isAccepted - If the appointment is accepted or declined. If notification answer is OK then isAccepted must be false.
	 */
	public void answerNotification(long notificationId, String email, boolean isAccepted) {
		databaseUpdateManager.sendAcceptAppointment(notificationId, email, isAccepted);
	}
}
