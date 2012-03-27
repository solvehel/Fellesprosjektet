package no.ntnu.fp.model;

import java.util.ArrayList;

/**
 * @author Sebastian Zalewski
 */
public class Notification {
	public enum NotificationType {
		NEW_APPOINTMENT,
		APPOINTMENT_CANCELED,
		PARTICIPANT_CANCELED_APPOINTMENT,
		APPOINTMENT_TITLE_CHANGED,
		APPOINTMENT_DESCRIPTION_CHANGED,
		APPOINTMENT_START_TIME_CHANGED,
		APPOINTMENT_END_TIME_CHANGED,
		APPOINTMENT_MEETING_ROOM_CHANGED,
		PERSON_REMOVED_FROM_APPOINTMENT
	}
	
	/**
	 * The notification id.
	 */
	private long id;
	
	/**
	 * The type of this notification.
	 */
	private NotificationType type;
	
	/**
	 * The appointment id that this notification is associated with.
	 */
	private long appointmentId;
	
	/**
	 * Participants that have canceled this meeting.
	 */
	private ArrayList<Person> participantsCanceled = new ArrayList<Person>();
	
	/**
	 * Constructor.
	 * 
	 * @param appointmentId - The appointment id that this notification is associated with.
	 * @param type - The type of this notification.
	 */
	public Notification(long id, long appointmentId, NotificationType type) {
		this.id = id;
		this.appointmentId = appointmentId;
		this.type = type;
	}
	
	/**
	 * @return an unique identifier that identifies this object
	 */
	public long getNotificationId() {
		return id;
	}
	
	/**
	 * @return The appointment id that this notification is associated with.
	 */
	public long getAppointmentId() {
		return appointmentId;
	}
	
	/**
	 * @return The type of this notification.
	 */
	public NotificationType getType() {
		return type;
	}
	
	/**
	 * @param type - The new type of this notification.
	 */
	public void setType(NotificationType type) {
		this.type = type;
	}
	
	/**
	 * @param person - The person that has canceled this appointment from his schedule.
	 */
	public void addParticipantCanceled(Person person) {
		participantsCanceled.add(person);
	}
	
	/**
	 * @return an array containing all the persons that have canceled this appointment form theirs schedule
	 */
	public Person[] getParticipantCanceled() {
		Person[] persons = new Person[participantsCanceled.size()];
		participantsCanceled.toArray(persons);
		return persons;
	}
}
