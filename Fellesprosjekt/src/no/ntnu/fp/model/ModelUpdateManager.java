package no.ntnu.fp.model;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import no.ntnu.fp.model.XmlSerializer.XmlType;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ReceiveMessageWorker;
import no.ntnu.fp.net.co.ReceiveMessageWorker.MessageListener;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * @author Sebastian Zalewski
 */
public class ModelUpdateManager implements MessageListener {
	private XmlSerializer xmlSerializer = new XmlSerializer();
	private String requestedData;
	private UpdateListener updateListener;
	
	/**
	 * Constructor. Adds this object as a message listener in receiveMessageWorker thread.
	 * 
	 * @param receiveMessageWorker
	 */
	public ModelUpdateManager(ReceiveMessageWorker receiveMessageWorker, UpdateListener updateListener) {
		receiveMessageWorker.addMessageListener(this);
		this.updateListener = updateListener;
	}
	
	/**
	 * This method must be called only after a login message has been sent to the server.
	 * 
	 * @return true if login was successful
	 */
	public boolean getRequestedLoginResult() {
		while (requestedData == null);
		Object[] objects = fromXmlToObjects(requestedData);
		Boolean result = (Boolean) objects[0];
		requestedData = null;
		
		return result;
	}
	
	/**
	 * This method must be called only after an employee request has been sent to the server.
	 * 
	 * @return an array containing employees
	 */
	public Person[] getRequestedEmployees() {
		while (requestedData == null);
		Object[] objects = fromXmlToObjects(requestedData);
		Person[] employees = new Person[objects.length];
		fromObjectArrayToTArray(objects, employees);
		requestedData = null;
		
		return employees; 
	}
	
	/**
	 * This method must be called only after an appointment request has been sent to the server.
	 * 
	 * @return an array containing the requested appointments
	 */
	public Appointment[] getRequestedAppointments() {
		while (requestedData == null);
		Object[] objects = fromXmlToObjects(requestedData);
		Appointment[] appointments = new Appointment[objects.length];
		fromObjectArrayToTArray(objects, appointments);
		requestedData = null;
		
		return appointments;
	}
	
	/**
	 * This method must be called only after an meeting room request has been sent to the server.
	 * 
	 * @return an array containing the requested meeting rooms
	 */
	public MeetingRoom[] getRequestedMeetingRooms() {
		while (requestedData == null);
		Object[] objects = fromXmlToObjects(requestedData);
		MeetingRoom[] meetingRooms = new MeetingRoom[objects.length];
		fromObjectArrayToTArray(objects, meetingRooms);
		requestedData = null;
		
		return meetingRooms;
	}
	
	/**
	 * This method must be called only after a meeting room occupied time request has been sent to the server.
	 * 
	 * @return a map containing the occupied time points in a specified meeting room
	 */
	public Map<Calendar, Calendar> getRequestedMeetingRoomOccupiedTime() {
		while (requestedData == null);
		Object[] objects = fromXmlToObjects(requestedData);
		Appointment[] appointments = new Appointment[objects.length];
		fromObjectArrayToTArray(objects, appointments);
		requestedData = null;
		
		Map<Calendar, Calendar> map = new HashMap<Calendar, Calendar>();
		for (int i = 0; i < appointments.length; i++)
			map.put(appointments[i].getStartTime(), appointments[i].getEndTime());
		
		return map;
	}
	
	/**
	 * This method must be called only after a create appointment message has been sent to the server.
	 * 
	 * @return the requested appointment id
	 */
	public long getRequestedAppointmentId() {
		while (requestedData == null);
		Object[] objects = fromXmlToObjects(requestedData);
		long appointmentId = (Long) objects[0];
		requestedData = null;
		
		return appointmentId;
	}
	
	private Object[] fromXmlToObjects(String xml) {
		try {
			return xmlSerializer.toSendData(xml);
		} catch (ValidityException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Converts an object array to a T array. (i.e. Person[], Appointment[], Notification[], etc...)
	 * 
	 * @param <T>
	 * @param objects
	 * @param emptyArray 
	 */
	@SuppressWarnings("unchecked")
	private <T> void fromObjectArrayToTArray(Object[] objects, T[] emptyArray) {
		// TODO: check warning
		if (objects.length != emptyArray.length)
			return;
		
		for (int i = 0; i < objects.length; i++)
			emptyArray[i] = (T) objects[i];
	}

	/**
	 * {@inheritDoc}
	 */
	public void messageReceived(String message, Connection connection) {
		try {
			if (xmlSerializer.isNotification(message)) {
				Object[] objects = fromXmlToObjects(message);
				Notification[] notifications = new Notification[objects.length];
				fromObjectArrayToTArray(objects, notifications);
				for (int i = 0; i < notifications.length; i++)
					updateListener.notificationReceived(notifications[i]);
			}
			else if (xmlSerializer.getXmlType(message) == XmlType.SEND_DATA) {
				requestedData = message;
			}
			else if (xmlSerializer.getXmlType(message) == XmlType.UPDATE_DATA) {
				Object[] objects = fromXmlToObjects(message);
				Appointment appointment = (Appointment) objects[0];
				updateListener.appointmentUpdateReceived(appointment);
			}
			else if (xmlSerializer.getXmlType(message) == XmlType.CREATE_APPOINTMENT) {
				updateListener.appointmentCreated();
			}
			else if (xmlSerializer.getXmlType(message) == XmlType.REMOVE_APPOINTMENT) {
				Object[] objects = fromXmlToObjects(message);
				long appointmentId = (Long) objects[0];
				updateListener.appointmentRemoved(appointmentId);
			}
		} catch (ValidityException e) {
			e.printStackTrace();
		} catch (ParsingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}}
	 */
	public void connectionClosed(Connection conn) {
	}
}
