package no.ntnu.fp.model;

import java.util.*;

/**
 * @author Sebastian Zalewski
 */
public class Appointment {
	private String appointmentLeaderEmail;
	private Calendar startTime;
	private Calendar endTime;
	private String title;
	private String description;
	private String meetingRoomName;
	
	/**
	 * A list containing all the participants of this appointment.
	 */
	private Project participants = new Project();
	
	/**
	 * This member variable holds a unique identifier for this object.
	 */
	private long id;
	
	public Appointment(long appointmentId) {
		id = appointmentId;
	}
	
	/**
	 * Returns this object's unique identification.
	 * 
	 * @return The appointemt's unique identification.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the appointment leader email
	 */
	public String getAppointmentLeaderEmail() {
		return appointmentLeaderEmail;
	}
	
	/**
	 * Sets the appointment leader email.
	 * 
	 * @param email - identifies the appointment leader
	 */
	public void setAppointmentLeaderEmail(String email) {
		this.appointmentLeaderEmail = email;
	}
	
	/**
	 * @return the start time of the appointment
	 */
	public Calendar getStartTime() {
		return startTime;
	}
	
	/**
	 * @return the end time of this appointment
	 */
	public Calendar getEndTime() {
		return endTime;
	}
	
	/**
	 * Sets the start and end time to this appointment.
	 * 
	 * @param startTime - The start time of this appointment.
	 * @param endTime - The end time of this appointment.
	 */
	public void setTime(Calendar startTime, Calendar endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	/**
	 * @return the description of this appointment
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description to this appointment.
	 * 
	 * @param description - The description of this appointment.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the title of this appointment
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title of this appointment.
	 * 
	 * @param title - The new title of this appointment.
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the meeting room name
	 */
	public String getMeetingRoomName() {
		return meetingRoomName;
	}
	
	/**
	 * Sets the meeting room name that is used to reference a meeting room.
	 * 
	 * @param meetingRoomName - the name of the meeting room
	 */
	public void setMeetingRoomName(String meetingRoomName) {
		this.meetingRoomName = meetingRoomName;
	}
	
	/**
	 * @return the persons that are participating in this meeting
	 */
	public Person[] getParticipants() {
		Person[] persons = new Person[participants.getPersonCount()];
		participants.toArray(persons);
		return persons;
	}
	
	/**
	 * Sets the participants of this appointment. <br />
	 * If the appointment already has a list of participants, <br />
	 * then this list will be replaced by the input.
	 * 
	 * @param participants - The participants of this appointment.
	 */
	public void setParticipants(Person[] participants) {
		this.participants = new Project();
		for (int i = 0; i < participants.length; i++)
			this.participants.addPerson(participants[i]);
	}
	
	/**
	 * Adds a participant to this appointment.
	 * 
	 * @param participant - the participant to be added
	 */
	public void addParticipant(Person participant) {
		participants.addPerson(participant);
	}
	
	/**
	 * Removes a participant from this appointment.
	 * 
	 * @param participant - the participant to be removed.
	 */
	public void removeParticipant(Person participant) {
		participants.removePerson(participant);
	}
}
