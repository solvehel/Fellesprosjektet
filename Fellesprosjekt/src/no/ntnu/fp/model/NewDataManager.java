package no.ntnu.fp.model;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import no.ntnu.fp.model.Notification.NotificationType;
import no.ntnu.fp.model.XmlSerializer.RequestDataType;
import no.ntnu.fp.model.XmlSerializer.SendDataType;
import no.ntnu.fp.model.XmlSerializer.XmlType;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ReceiveMessageWorker;
import no.ntnu.fp.net.co.ReceiveMessageWorker.MessageListener;
import no.ntnu.fp.storage.DBManager;
import no.ntnu.fp.storage.DBManagerInterface;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * @author Sebastian Zalewski
 */
public class NewDataManager {
	//TODO private DBManager dbManager = new DBManager();
	private DBManagerInterface dbManager;
	private XmlSerializer xmlSerializer = new XmlSerializer();
	
	/**
	 * The email and connection to a specified user connected to the server.
	 */
	private Map<String, Connection> clientConnections = new HashMap<String, Connection>();
	
	/**
	 * The email to the user and connection to the user being watched.
	 */
	private Map<String, Connection> clientsWatchingOtherUserAppointments = new HashMap<String, Connection>();
	
	/**
	 * This method should be called when the server receives a message. <br />
	 * Handles the message that is received.
	 * 
	 * @param message - The message that the server received.
	 * @param connection - The connection that sent the message.. 
	 */
	public void messageReceived(String message, Connection connection) {
		try {
			XmlType xmlType = xmlSerializer.getXmlType(message);
			
			switch (xmlType) {
			case LOGIN:
				handleLoginMessage(message, connection);
				break;
			case LOGOUT:
				handleLogoutMessage(message);
				break;
			case UPDATE_DATA:
				handleUpdateDataMessage(message, connection);
				break;
			case REQUEST_DATA:
				handleRequestDataMessage(message, connection);
				break;
			case CREATE_APPOINTMENT:
				handleCreateAppointmentMessage(message, connection);
				break;
			case REMOVE_APPOINTMENT:
				handleRemoveAppointmentMessage(message, connection);
				break;
			}
		} catch (ValidityException e1) {
			e1.printStackTrace();
		} catch (ParsingException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * This method should be called when a client is disconnected from the server. <br />
	 * Removes the connection from clientConnections.
	 * 
	 * @param conn - The connection that is closed.
	 */
	public void connectionClosed(Connection conn) {
		// remove connection from client connections
		Iterator<String> iterator = clientConnections.keySet().iterator();
		while (iterator.hasNext()) {
			String email = iterator.next();
			if (clientConnections.get(email).equals(conn))
				clientConnections.remove(email);
		}	
	}
	
	/**
	 * Checks if login is successful, if it is then adds the client that sent the login to  clientConnections <br />
	 * and sends notifications to that client. This method also sends the login result to the client.
	 * 
	 * @param xml - The XML string containing the login information.
	 * @param connection - The connection to the client that sent the XML string.
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	private void handleLoginMessage(String xml, Connection connection) throws ValidityException, ParsingException, IOException {
		String[] loginInfo = xmlSerializer.toLogin(xml);
		String email = loginInfo[0];
		String password = loginInfo[1];
		
		boolean isLoginSuccessful = dbManager.authenticateLogin(email, password);
		if (isLoginSuccessful) {
			// add client to clientConnections and send notifications to client
			clientConnections.put(email, connection);
			Notification[] notifications = dbManager.getNotifications(email);
			if (notifications.length != 0) {
				String xmlNotifications = xmlSerializer.sendDataToXml(SendDataType.NOTIFICATIONS, notifications).toXML();
				connection.send(xmlNotifications);
			}
		}
		
		String xmlLoginResult = xmlSerializer.sendDataToXml(SendDataType.LOGIN_RESULT, new Boolean[]{isLoginSuccessful}).toXML();
		connection.send(xmlLoginResult);
	}
	
	/**
	 * Removes the client from clientConnections.
	 * 
	 * @param xml - The XML string containing the logout information (i.e. email).
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	private void handleLogoutMessage(String xml) throws ValidityException, ParsingException, IOException {
		String email = xmlSerializer.toLogout(xml);
		clientConnections.remove(email);
	}
	
	/**
	 * Updates the appointments and notifications in the database. <br />
	 * Creates new notifications and sends them to the clients. <br />
	 * Sends update info to clients watching this connections appointments, <br />
	 * if this is an appointment update message.
	 * 
	 * @param xml - The XML string containing the update information.
	 * @param connection - The connection to the client that sent the XML string.
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ParseException
	 */
	private void handleUpdateDataMessage(String xml, Connection connection) throws ValidityException, ParsingException, IOException, ParseException {
		Object[] objects = xmlSerializer.toUpdateData(xml);
		
		if (xmlSerializer.isNotification(xml)) {
			long notificationId = (Long) objects[0];
			String email = (String) objects[1];
			boolean hasAccepted = (Boolean) objects[1];
			
			if (!hasAccepted) {
				Notification oldNotification = dbManager.getNotification(notificationId);
				
				// create a new notification with type PARTICIPANT_CANCELED_APPOINTMENT
				long newNotificationId = dbManager.createNotification(NotificationType.PARTICIPANT_CANCELED_APPOINTMENT.toString(), oldNotification.getAppointmentId());
				Person[] personsDeclined = oldNotification.getParticipantCanceled();
				Notification newNotification = dbManager.getNotification(newNotificationId);
				
				// add persons that have canceled this appointment to the new notification and update database
				newNotification.addParticipantCanceled(dbManager.getPerson(email));
				dbManager.addNotificationParticipantDeclined(newNotificationId, email);
				for (int i = 0; i < personsDeclined.length; i++) {
					newNotification.addParticipantCanceled(personsDeclined[i]);
					dbManager.addNotificationParticipantDeclined(newNotificationId, personsDeclined[i].getEmail());
				}
				
				sendNotification(newNotificationId, connection);
			}
			// the user has already answered this notification, therefore he must be removed from the notification participants list
			dbManager.removeNotificationParticipant(notificationId, email);
			
			// if there are zero persons associated with this notification, then it will be deleted
			if (dbManager.getNotificationParticipantCount(notificationId) == 0)
				dbManager.removeNotification(notificationId);
		}
		else {
			Appointment appointment = (Appointment) objects[0];
			
			if (appointment.getTitle() != null) {
				dbManager.updateAppointmentTitle(appointment.getId(), appointment.getTitle());
				// create notification with type APPOINTMENT_TITLE_CHANGED
				long notificationId = dbManager.createNotification(NotificationType.APPOINTMENT_TITLE_CHANGED.toString(), appointment.getId());
				sendNotification(notificationId, connection);
			}
			if (appointment.getStartTime() != null) {
				dbManager.updateAppointmentStartTime(appointment.getId(), appointment.getStartTime());
				// create notification with type APPOINTMENT_START_TIME_CHANGED
				long notificationId = dbManager.createNotification(NotificationType.APPOINTMENT_START_TIME_CHANGED.toString(), appointment.getId());
				sendNotification(notificationId, connection);
			}
			if (appointment.getEndTime() != null) {
				dbManager.updateAppointmentEndTime(appointment.getId(), appointment.getEndTime());
				// create notification with type APPOINTMENT_END_TIME_CHANGED
				long notificationId = dbManager.createNotification(NotificationType.APPOINTMENT_END_TIME_CHANGED.toString(), appointment.getId());
				sendNotification(notificationId, connection);
			}
			if (appointment.getDescription() != null) {
				dbManager.updateAppointmentDescription(appointment.getId(), appointment.getDescription());
				// create notification with type APPOINTMENT_DESCRIPTION_CHANGED
				long notificationId = dbManager.createNotification(NotificationType.APPOINTMENT_DESCRIPTION_CHANGED.toString(), appointment.getId());
				sendNotification(notificationId, connection);
			}
			if (appointment.getMeetingRoomName() != null) {
				if (appointment.getMeetingRoomName().equals(""))
					dbManager.updateAppointmentMeetingRoom(appointment.getId(), null);
				else
					dbManager.updateAppointmentMeetingRoom(appointment.getId(), appointment.getMeetingRoomName());
				// create notification with type APPOINTMENT_MEETING_ROOM_CHANGED
				long notificationId = dbManager.createNotification(NotificationType.APPOINTMENT_MEETING_ROOM_CHANGED.toString(), appointment.getId());
				sendNotification(notificationId, connection);
			}
			if (appointment.getParticipants() != null) {
				Appointment oldAppointment = dbManager.getAppointment(appointment.getId());
				Person[] oldParticipants = oldAppointment.getParticipants();
				Person[] newParticipants = appointment.getParticipants();
				ArrayList<Person> participantsRemoved = compareArrays(oldParticipants, newParticipants);
				ArrayList<Person> participantsAdded = compareArrays(newParticipants, oldParticipants);
				
				long appointmentRemovedNotificationId = dbManager.createNotification(NotificationType.PERSON_REMOVED_FROM_APPOINTMENT.toString(), appointment.getId(), participantsRemoved);
				sendNotification(appointmentRemovedNotificationId, connection);
				
				long newAppointmentNotificaitonId = dbManager.createNotification(NotificationType.NEW_APPOINTMENT.toString(), appointment.getId(), participantsAdded);
				sendNotification(newAppointmentNotificaitonId, connection);
				
				dbManager.updateAppointmentParticipants(appointment.getId(), appointment.getParticipants());
			}
			
			// send update info to clients watching this users appointments
			Appointment newAppointment = dbManager.getAppointment(appointment.getId());
			String xmlAppointmentUpdate = xmlSerializer.updateDataToXml(newAppointment).toXML();
			sendInfoToClientsWatchingAnotherUser(xmlAppointmentUpdate, connection);
		}
	}
	
	/**
	 * Compares two arrays and returns the persons that are not in arrayTwo <br />
	 * but are in arrayOne.
	 * 
	 * @param arrayOne - An array containing Person objects.
	 * @param arrayTwo - An array containing Person objects.
	 * @return the persons that are not in arrayTwo but are in arrayOne
	 */
	private ArrayList<Person> compareArrays(Person[] arrayOne, Person[] arrayTwo) {
		ArrayList<Person> result = new ArrayList<Person>();
		
		for (int i = 0; i < arrayOne.length; i++) {
			boolean isPersonInArrayTwo = false;
			for (int j = 0; j < arrayTwo.length; j++) {
				if (arrayOne[i].getEmail().equals(arrayTwo[j].getEmail())) {
					isPersonInArrayTwo = true;
				}
			}
			if (!isPersonInArrayTwo) {
				result.add(arrayOne[i]);
			}
		}
		
		return result;
	}
	
	/**
	 * Retrieves the requested data from the database and sends it to the client.
	 * 
	 * @param xml - The XML string containing request data information.
	 * @param connection - The connection to the client that sent the XML string.
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ParseException
	 */
	private void handleRequestDataMessage(String xml, Connection connection) throws ValidityException, ParsingException, IOException, ParseException {
		Object[] objects = xmlSerializer.toRequestData(xml);

		RequestDataType type = xmlSerializer.getRequestDataType(xml);
		switch (type) {
			case MEETING_ROOM_OCCUPIED_TIME:
				// send meeting room occupied time points to client
				String meetingRoomName = (String) objects[0];
				Appointment[] timePoints = dbManager.getOccupiedTimePoints(meetingRoomName);
				String xmlTimePoints = xmlSerializer.sendDataToXml(SendDataType.APPOINTMENT_TIME_INTERVALS, timePoints).toXML();
				connection.send(xmlTimePoints);
				break;
			case MEETING_ROOMS:
				// send meeting rooms to client
				MeetingRoom[] meetingRooms = dbManager.getMeetingRooms();
				String xmlMeetingRooms = xmlSerializer.sendDataToXml(SendDataType.MEETING_ROOMS, meetingRooms).toXML();
				connection.send(xmlMeetingRooms);
				break;
			case NOTIFICATIONS:
				// send notifications to client
				String email = (String) objects[0];
				Notification[] notifications = dbManager.getNotifications(email);
				String xmlNotifications = xmlSerializer.sendDataToXml(SendDataType.NOTIFICATIONS, notifications).toXML();
				connection.send(xmlNotifications);
				break;
			case PERSON_APPOINTMENTS:
				// send appointments from a specified person to client
				email = (String) objects[0];
				Calendar startTime = (Calendar) objects[1];
				Calendar endTime = (Calendar) objects[2];
				Appointment[] appointments = dbManager.getAppointments(email, startTime, endTime);
				String xmlAppointments = xmlSerializer.sendDataToXml(SendDataType.APPOINTMENTS, appointments).toXML();
				connection.send(xmlAppointments);
				
				// if user is watching appointments to another user
				if (clientConnections.get(email) != connection)
					clientsWatchingOtherUserAppointments.put(email, connection);
				
				break;
			case PERSONS:
				// send employees to client
				Person[] persons = dbManager.getPersons();
				String xmlPersons = xmlSerializer.sendDataToXml(SendDataType.PERSONS, persons).toXML();
				connection.send(xmlPersons);
				break;
		}
	}
	
	/**
	 * Creates a new appointment and notification. <br />
	 * Sends the notification to other clients.
	 * 
	 * @param xml - The XML string containing the create appointment message.
	 * @param connection - The connection to the client that sent the XML string.
	 * @throws ConnectException
	 * @throws IOException
	 */
	private void handleCreateAppointmentMessage(String xml, Connection connection) throws ConnectException, IOException {
		long appointmentId = dbManager.createAppointment();
		String xmlAppointmentId = xmlSerializer.sendDataToXml(SendDataType.NEW_APPOINTMENT_ID, new Long[]{appointmentId}).toXML();
		connection.send(xmlAppointmentId);
		
		// create notification with type NEW_APPOINTMENT
		long notificationId = dbManager.createNotification(NotificationType.NEW_APPOINTMENT.toString(), appointmentId);
		sendNotification(notificationId, connection);
		
		sendInfoToClientsWatchingAnotherUser(xml, connection);
	}
	
	/**
	 * Removes the specified appointment from the database. <br />
	 * Creates a notification and sends it to other clients.
	 * 
	 * @param xml - The XML string containing the remove appointment information (i.e. appointment id).
	 * @param connection - The connection to the client that sent the XML string.
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	private void handleRemoveAppointmentMessage(String xml, Connection connection) throws ValidityException, ParsingException, IOException {
		long appointmentId = xmlSerializer.toRemoveAppointment(xml);
		dbManager.removeAppointment(appointmentId);
		
		// create notification with type APPOINTMENT_CANCELED
		long notificationId = dbManager.createNotification(NotificationType.APPOINTMENT_CANCELED.toString(), appointmentId);
		sendNotification(notificationId, connection);
		
		sendInfoToClientsWatchingAnotherUser(xml, connection);
	}
	
	/**
	 * Sends the specified notification to the clients that are associated with this notification.
	 * 
	 * @param notificationId - The id to the notification to be sent.
	 * @param whereNotToSendNotification - The connection not to sent the notification to. This is the connection that caused the notification.
	 * @throws ConnectException
	 * @throws IOException
	 */
	private void sendNotification(long notificationId, Connection whereNotToSendNotification) throws ConnectException, IOException {
		Notification notification = dbManager.getNotification(notificationId);
		String xmlNotification = xmlSerializer.sendDataToXml(SendDataType.NOTIFICATIONS, new Notification[]{notification}).toXML();
		
		// Send the notification to other users, associated with this notification, that are currently connected to the server.
		Person[] persons = dbManager.getNotificationParticipants(notificationId);
		for (int i = 0; i < persons.length; i++) {
			Connection conn = clientConnections.get(persons[i].getEmail());
			if (!conn.equals(whereNotToSendNotification)) // don't send this notification to the person that caused it
				conn.send(xmlNotification);
		}
	}
	
	/**
	 * Sends a message to a client that is watching appointments to another user.
	 * 
	 * @param xml - The XML string containing the info to be sent to other clients.
	 * @param connection - The connection to the client being watched.
	 * @throws ConnectException
	 * @throws IOException
	 */
	private void sendInfoToClientsWatchingAnotherUser(String xml, Connection connection) throws ConnectException, IOException {
		Iterator<String> iterator = clientsWatchingOtherUserAppointments.keySet().iterator();
		while (iterator.hasNext()) {
			String mail = iterator.next();
			if (clientsWatchingOtherUserAppointments.get(mail).equals(connection)) {
				clientConnections.get(mail).send(xml);
			}
		}
	}
}
