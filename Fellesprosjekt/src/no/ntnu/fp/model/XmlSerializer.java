/*
 * Created on Oct 22, 2004
 */
package no.ntnu.fp.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import no.ntnu.fp.model.Notification.NotificationType;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

/**
 * @author tho, Sebastian Zalewski
 */
public class XmlSerializer {
	/**
	 * The type of a XML string.
	 * <br /><br /> 
	 * LOGIN - the XML string tells the server that a person is trying to login <br />
	 * LOGOUT- the XML string tells the server that a person is trying to logout <br />
	 * UPDATE_DATA - the XML string tells the server to update the database (e.g. appointment title, description...) <br />
	 * REQUEST_DATA - the XML string tells the server that it is making a request (e.g. get persons, meeting rooms...) <br />
	 * SEND_DATA - the XML string that is sent from the server to a client with data (e.g. Person[], MeetingRoom[]...) <br />
	 * CREATE_APPOINTMENT - the XML string that tells the server to create a new appointment <br />
	 * REMOVE_APPOINTMENT - the XML string tells the server to remove a specified appointment <br />
	 */
	public enum XmlType {
		LOGIN, LOGOUT, UPDATE_DATA, REQUEST_DATA, SEND_DATA, CREATE_APPOINTMENT, REMOVE_APPOINTMENT
	}
	
	/**
	 * The concrete type of a REQUEST_DATA XML string.
	 * <br /><br /> 
	 * PERSONS - requests all persons stored in the database <br />
	 * MEETING_RROMS - requests all meeting rooms stored in the database <br />
	 * PERSON_APPOINTEMNTS - requests the appointments to a specified person in a given time interval <br />
	 * MEETING_ROOM_OCCUPIED_TIME - requests the occupied time points in a specified meeting room <br />
	 * NOTIFICATIONS - requests the notifications to a specified person <br />
	 */
	public enum RequestDataType {
		PERSONS, MEETING_ROOMS, PERSON_APPOINTMENTS, MEETING_ROOM_OCCUPIED_TIME, NOTIFICATIONS
	}
	
	/**
	 * The concrete type of a SEND_DATA XML string.
	 * <br /><br />
	 * LOGIN_RESULT - the XML string contains the login result (if the user managed to login) <br />
	 * NEW_APPOINTMENT_ID - the XML string contains an id to a newly created appointment <br />
	 * PERSONS - the XML string contains persons <br />
	 * APPOINTMENTS - the XML string contains appointments <br />
	 * MEETING_ROOM - the XML string contains meeting rooms <br />
	 * APPOINTMENT_TIME_INTERVAL - the XML string contains appointments with start and end time <br />
	 * NOTIFICATIONS - the XML string contains notifications <br />
	 */
	public enum SendDataType {
		LOGIN_RESULT, NEW_APPOINTMENT_ID, PERSONS, APPOINTMENTS, MEETING_ROOMS, APPOINTMENT_TIME_INTERVALS, NOTIFICATIONS
	}

	/**
	 * Login string.
	 * 
	 * @param email - email to a person (is unique)
	 * @param password - password to a person
	 * @return an XML document containing login information
	 */
	public Document loginToXml(String email, String password) {
		Element element = new Element("login");
		
		Element elementEmail = new Element("email");
		elementEmail.appendChild(email);
		
		Element elementPassword = new Element("password");
		elementPassword.appendChild(password);
		
		element.appendChild(elementEmail);
		element.appendChild(elementPassword);
		
		return new Document(element);
	}
	
	/**
	 * Converts an XML string containing login information to an array of strings.
	 * 
	 * @param xml - The XML string containing the login information.
	 * @return String[0] contains email, String[1] contains password.
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	public String[] toLogin(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		return assembleLogin(doc.getRootElement());
	}
	
	private String[] assembleLogin(Element loginElement) {
		String email = "";
		String password = "";
		
		// get email
		Element element = loginElement.getFirstChildElement("email");
		if (element != null)
			email = element.getValue();
		
		// get password
		element = loginElement.getFirstChildElement("password");
		if (element != null)
			password = element.getValue();
		
		return new String[]{email, password};
	}
	
	/**
	 * Logout string.
	 * 
	 * @param email - An unique email to a person.
	 * @return an XML document containing the email to the person that is going to logout
	 */
	public Document logoutToXml(String email) {
		Element element = new Element("logout");
		
		Element elementEmail = new Element("email");
		elementEmail.appendChild(email);
		
		element.appendChild(elementEmail);
		
		return new Document(element);
	}
	
	/**
	 * Converts an XML string containing logout information to a string containing an email.
	 * 
	 * @param xml - The XML string to containing the email to the person.
	 * @return a string containing an email
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	public String toLogout(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		return assembleLogout(doc.getRootElement());
	}
	
	private String assembleLogout(Element logoutElement) {
		String email = "";
		
		Element element = logoutElement.getFirstChildElement("email");
		if (email != null)
			email = element.getValue();
		
		return email;
	}
	
	/**
	 * Creates an update XML document. The XML string contains all the information <br />
	 * from the appointment that is not null.
	 * 
	 * @param appointment - The appointment to convert to an XML document.
	 * @return a document containing the information that is not null in the appointment object
	 */
	public Document updateDataToXml(Appointment appointment) {
		Element element = new Element("update_data");
		element.appendChild(appointmentToElement(appointment));
		return new Document(element);
	}
	
	/**
	 * Creates an update XML document for notification.
	 * 
	 * @param notificationId - The notification to be updated.
	 * @param email - The email that identifies a person.
	 * @param accepted - If person has accepted the appointment.
	 * @return an XML document that tells if a specified person has accepted or declined an appointment
	 */
	public Document updateDataToXml(long notificationId, String email, boolean accepted) {
		Element element = new Element("update_data");
		
		Element notificationElement = new Element("notification");
		Element idElement = new Element("id");
		idElement.appendChild(new Long(notificationId).toString());
		
		Element resultElement;
		if (accepted)
			resultElement = new Element("accepted");
		else
			resultElement = new Element("declined");
		
		Element emailElement = new Element("email");
		emailElement.appendChild(email);
		
		resultElement.appendChild(emailElement);
		notificationElement.appendChild(resultElement);
		element.appendChild(idElement);
		element.appendChild(notificationElement);
		
		return new Document(element);
	}
	
	/**
	 * This method converts an XML string to an object array containing an Appointment or notificationId, email and accepted.
	 * 
	 * @param xml - The XML string containing either appointment or notification information.
	 * @return if the XML string is an update to appointment then Object[0]:Appointment <br />
	 * 		   else if the XML string is a notification update then Object[0]:notificationId(long), Object[1]:email(String), Object[2]:accepted(boolean)
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ParseException
	 */
	public Object[] toUpdateData(String xml) throws ValidityException, ParsingException, IOException, ParseException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		Element updateDataElement = doc.getRootElement();
		
		if (updateDataElement.getFirstChildElement("appointment") != null) {
			return new Object[]{toAppointment(xml)};
		}
		else if (updateDataElement.getFirstChildElement("notification") != null) {	
			long notificationId = -1;
			String email = null;
			boolean accepted = false;
			
			Element idElement = updateDataElement.getFirstChildElement("id");
			if (idElement != null)
				notificationId = Long.parseLong(idElement.getValue());
			
			Element notificationElement = updateDataElement.getFirstChildElement("notification");
			if (updateDataElement != null) {
				Element emailElement = null;
				Element resultElement = notificationElement.getFirstChildElement("accepted");
				if (resultElement != null) {
					accepted = true;
					emailElement = resultElement.getFirstChildElement("email");
				}
				else {
					resultElement = notificationElement.getFirstChildElement("declined");
					if (resultElement != null)
						emailElement = resultElement.getFirstChildElement("email");
				}
				
				if (emailElement != null)
					email = emailElement.getValue();
			}
			
			return new Object[]{notificationId, email, accepted};
		}
		
		return null;
	}
	
	/**
	 * Converts an XML string to an appointment.
	 * 
	 * @param xml - The XML string containing the appointment information.
	 * @return an appointment containing the information in the XML string
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ParseException
	 */
	public Appointment toAppointment(String xml) throws ValidityException, ParsingException, IOException, ParseException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		return assembleAppointment(doc.getRootElement().getFirstChildElement("appointment"));
	}
	
	private Appointment assembleAppointment(Element appointmentElement) throws ParseException {
		long id = -1;
		Calendar startTime = null, endTime = null;
		String title = null, leaderEmail = null, description = null, meetingRoomName = null;
		
		Element element = appointmentElement.getFirstChildElement("id");
		if (element != null)
			id = Long.parseLong(element.getValue());
		
		element = appointmentElement.getFirstChildElement("title");
		if (element != null)
			title = element.getValue();
		
		element = appointmentElement.getFirstChildElement("appointment_leader_email");
		if (element != null)
			leaderEmail = element.getValue();
		
		element = appointmentElement.getFirstChildElement("start_time");
		if (element != null)
			startTime = parseDateTime(element.getValue());
		
		element = appointmentElement.getFirstChildElement("end_time");
		if (element != null)
			endTime = parseDateTime(element.getValue());
		
		element = appointmentElement.getFirstChildElement("description");
		if (element != null)
			description = element.getValue();
		
		element = appointmentElement.getFirstChildElement("meeting_room_name");
		if (element != null)
			meetingRoomName = element.getValue();
		
		Appointment appointment = new Appointment(id);
		appointment.setTitle(title);
		appointment.setAppointmentLeaderEmail(leaderEmail);
		appointment.setTime(startTime, endTime);
		appointment.setDescription(description);
		appointment.setMeetingRoomName(meetingRoomName);
		
		element = appointmentElement.getFirstChildElement("participant");
		if (element != null) {
			Elements personElements = element.getChildElements("person");
			for (int i = 0; i < personElements.size(); i++) {
				String name = null, email = null;
				
				element = personElements.get(i).getFirstChildElement("name");
				if (element != null)
					name = element.getValue();
				
				element = personElements.get(i).getFirstChildElement("email");
				if (element != null)
					email = element.getValue();
				
				appointment.addParticipant(new Person(name, email, new Date()));
			}	
		}
		
		return appointment;
	}
	
	private Element appointmentToElement(Appointment appointment) {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, java.util.Locale.US);
		
		Element element = new Element("appointment");
		
		Element id = new Element("id");
		id.appendChild(new Long(appointment.getId()).toString());
		
		Element title = new Element("title");
		title.appendChild(appointment.getTitle());
		
		Element leaderEmail = new Element("appointment_leader_email");
		leaderEmail.appendChild(appointment.getAppointmentLeaderEmail());
		
		Element startTime = new Element("start_time");
		if (appointment.getStartTime() != null) {
			Date startTimeDate = new Date(appointment.getStartTime().getTimeInMillis());
			startTime.appendChild(format.format(startTimeDate));
		}
		
		Element endTime = new Element("end_time");
		if (appointment.getEndTime() != null) {
			Date endTimeDate = new Date(appointment.getEndTime().getTimeInMillis());
			endTime.appendChild(format.format(endTimeDate));
		}
		
		Element description = new Element("description");
		description.appendChild(appointment.getDescription());
		
		Element meetingRoomName = new Element("meeting_room_name");
		meetingRoomName.appendChild(appointment.getMeetingRoomName());
		
		Element participant = new Element("participant");
		Person[] participants = appointment.getParticipants();
		for (int i = 0; i < participants.length; i++) {
			Element person = new Element("person");
			
			Element name = new Element("name");
			name.appendChild(participants[i].getName());
			
			Element email = new Element("email");
			email.appendChild(participants[i].getEmail());
			
			person.appendChild(name);
			person.appendChild(email);
			participant.appendChild(person);
		}
		
		element.appendChild(id);
		if (appointment.getTitle() != null)
			element.appendChild(title);
		if (appointment.getAppointmentLeaderEmail() != null)
			element.appendChild(leaderEmail);
		if (appointment.getStartTime() != null)
			element.appendChild(startTime);
		if (appointment.getEndTime() != null)
			element.appendChild(endTime);
		if (appointment.getDescription() != null)
			element.appendChild(description);
		if (appointment.getMeetingRoomName() != null)
			element.appendChild(meetingRoomName);
		if (appointment.getParticipants().length != 0)
			element.appendChild(participant);
		
		return element;
	}
	
	/**
	 * Creates an XML string containing request information.
	 * <br /><br />
	 * NB! RequestData must be either PERSONS or MEETING_ROOMS!
	 * 
	 * @param type - The request type.
	 * @return an XML document containing the request information
	 */
	public Document requestDataToXml(RequestDataType type) {
		Element element = new Element("request_data");
		
		if (type == RequestDataType.PERSONS)
			element.appendChild(new Element("persons"));
		else if (type == RequestDataType.MEETING_ROOMS)
			element.appendChild(new Element("meeting_rooms"));
		
		return new Document(element);
	}
	
	/**
	 * Creates an XML string containing request information.
	 * <br /><br />
	 * NB! RequestDataType must be NOTIFICATIONS or MEETING_ROOM_OCCUPIED_TIME!
	 * <br /><br />
	 * If RequestDataType is MEETING_ROOM_OCCUPIED_TIME then this is a request that inquires the occupied time points of a specified meeting room.
	 * 
	 * @param type - The request type.
	 * @param id - An unique email that identifies a person.
	 * @return an XML document containing the request information
	 */
	public Document requestDataToXml(RequestDataType type, String id) {
		Element element = new Element("request_data");
		
		if (type == RequestDataType.NOTIFICATIONS) {
			Element notification = new Element("notification");
			
			Element emailElement = new Element("email");
			emailElement.appendChild(id);
			
			notification.appendChild(emailElement);
			
			element.appendChild(notification);
		}
		else if (type == RequestDataType.MEETING_ROOM_OCCUPIED_TIME) {
			Element meetingRoomOccupiedTimeElement = new Element("meeting_room_occupied_time");
			element.appendChild(meetingRoomOccupiedTimeElement);
			
			Element meetingRoom = new Element("meeting_room");
			
			Element name = new Element("name");
			name.appendChild(id);
			
			meetingRoom.appendChild(name);
			meetingRoomOccupiedTimeElement.appendChild(meetingRoom);
		}
		
		return new Document(element);
	}
	
	/**
	 * Creates an XML string containing request information.
	 * <br />
	 * <br />
	 * If RequestDataType is PERSON_APPOINTMENTS then this is a request that inquiries the appointments to the specified person in the given time interval.
	 * 
	 * @param type - The request type.
	 * @param id - an unique identifier for a person (i.e. email) 
	 * @param start - start of the time interval
	 * @param end - end of the time interval
	 * @return an XML document containing the request information
	 */
	public Document requestDataToXml(RequestDataType type, String id, Calendar start, Calendar end) {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, java.util.Locale.US);
		
		Element element = new Element("request_data");
		
		if (type == RequestDataType.PERSON_APPOINTMENTS) {
			Element personAppointmentsElement = new Element("person_appointments");
			element.appendChild(personAppointmentsElement);
			
			Element person = new Element("person");
			
			Element email = new Element("email");
			email.appendChild(id);
			
			Element appointment = new Element("appointment");
			
			Element startTime = new Element("start_time");
			Date startTimeDate = new Date(start.getTimeInMillis());
			startTime.appendChild(format.format(startTimeDate));
			
			Element endTime = new Element("end_time");
			Date endTimeDate = new Date(end.getTimeInMillis());
			endTime.appendChild(format.format(endTimeDate));
			
			person.appendChild(email);
			person.appendChild(appointment);
			appointment.appendChild(startTime);
			appointment.appendChild(endTime);
			personAppointmentsElement.appendChild(person);
		}
		
		return new Document(element);
	}
	
	/**
	 * The concrete type of a REQUEST_DATA XML string.
	 * <br /><br />
	 * PERSONS - requests all persons stored in the database <br />
	 * MEETING_RROMS - requests all meeting rooms stored in the database <br />
	 * PERSON_APPOINTEMNTS - requests the appointments to a specified person in a given time interval <br />
	 * MEETING_ROOM_OCCUPIED_TIME - requests the occupied time points in a specified meeting room <br />
	 * NOTIFICATIONS - requests the notifications to a specified person <br />
	 * 
	 * @param xml - The XML string of type REQUEST_DATA.
	 * @return the type of the request
	 * @throws IOException 
	 * @throws ParsingException 
	 * @throws ValidityException 
	 */
	public RequestDataType getRequestDataType(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		Element requestElement = doc.getRootElement();
		return getRequestDataType(requestElement);
	}
	
	/**
	 * The concrete type of a REQUEST_DATA XML string.
	 * <br /><br />
	 * PERSONS - requests all persons stored in the database <br />
	 * MEETING_RROMS - requests all meeting rooms stored in the database <br />
	 * PERSON_APPOINTEMNTS - requests the appointments to a specified person in a given time interval <br />
	 * MEETING_ROOM_OCCUPIED_TIME - requests the occupied time points in a specified meeting room <br />
	 * NOTIFICATIONS - requests the notifications to a specified person <br />
	 * 
	 * @param requestElement - The element containing the request information.
	 * @return the type of the request
	 */
	private RequestDataType getRequestDataType(Element requestElement) {
		Element element = requestElement.getFirstChildElement("persons");
		if (element != null)
			return RequestDataType.PERSONS;
		
		element = requestElement.getFirstChildElement("meeting_rooms");
		if (element != null)
			return RequestDataType.MEETING_ROOMS;
		
		element = requestElement.getFirstChildElement("person_appointments");
		if (element != null)
			return RequestDataType.PERSON_APPOINTMENTS;
		
		element = requestElement.getFirstChildElement("meeting_room_occupied_time");
		if (element != null)
			return RequestDataType.MEETING_ROOM_OCCUPIED_TIME;
		
		element = requestElement.getFirstChildElement("notification");
		if (element != null)
			return RequestDataType.NOTIFICATIONS;
		
		return null;
	}
	
	/**
	 * This method should only be used if the RequestDataType is PERSON_APPOINTMENTS, MEETING_ROOM_OCCUPIED_TIME or NOTIFICATIONS.
	 * Returns the identifier (i.e. person email or meeting room name), and a start and end time if the type is PERSON_APPOINTMENTS.
	 * The start and end time specifies a time interval.
	 * <br /><br />
	 * If RequestDataType is PERSON_APPOINTMENTS then this is a request that inquiries the appointments to the specified person in the given time interval. <br />
	 * If RequestDataType is MEETING_ROOM_OCCUPIED_TIME then this is a request that inquires the occupied time points of a specified meeting room. <br />
	 * If RequestDataType is NOTIFICATIONS then this is a request that inquiries the notifications to a specified person.
	 * 
	 * @param xml - The string containing the request information.
	 * @return If RequestDataType is NOTIFICATIONS or MEETING_ROOM_OCCUPIED_TIME then Object[0] = id : String (email or meeting room name) <br />
	 * 		   else if RequestDataType is PERSON_APPOINTMENTS Object[0] = id : String, Object[1] = startTime : Calendar, Object[2] endTime : Calendar 
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ParseException
	 */
	public Object[] toRequestData(String xml) throws ValidityException, ParsingException, IOException, ParseException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		Element element = doc.getRootElement();
		RequestDataType type = getRequestDataType(element);
		if (type == RequestDataType.PERSON_APPOINTMENTS) {
			Element personElement = element.getFirstChildElement("person_appointments").getFirstChildElement("person");
			if (personElement != null)
				return assembleRequestData(type, personElement);
			else return null;
		}
		else if (type == RequestDataType.MEETING_ROOM_OCCUPIED_TIME) {
			Element meetingRoomElement = element.getFirstChildElement("meeting_room_occupied_time").getFirstChildElement("meeting_room");
			if (meetingRoomElement != null) {
				String name = "";
				Element nameElement = meetingRoomElement.getFirstChildElement("name");
				if (nameElement != null)
					name = nameElement.getValue();
				return new Object[]{name};
			}
			else return null;
		}
		else if (type == RequestDataType.NOTIFICATIONS) {
			String email = "";
			Element notificationElement = element.getFirstChildElement("notification");
			if (notificationElement != null) {
				Element emailElement = notificationElement.getFirstChildElement("email");
				if (emailElement != null)
					email = emailElement.getValue();
			}
			
			return new Object[]{email};
		}
		else
			return null;
	}
	
	private Object[] assembleRequestData(RequestDataType type, Element requestElement) throws ParseException {
		String id = "";
		Calendar start = null, end = null;
	
		String stringId = "email";
		
		Element element = requestElement.getFirstChildElement(stringId);
		if (element != null)
			id = element.getValue();
		
		Element appointmentElement = requestElement.getFirstChildElement("appointment");
		if (appointmentElement != null) {
			element = appointmentElement.getFirstChildElement("start_time");
			if (element != null)
				start = parseDateTime(element.getValue());
			
			element = appointmentElement.getFirstChildElement("end_time");
			if (element != null)
				end = parseDateTime(element.getValue());
		}
		
		return new Object[]{id, start, end};
	}
	
	/**
	 * Creates an XML document that contains the send data information.
	 * 
	 * It is very important that the input to this function is given in the following format: <br />
	 * Input format: (LOGIN_RESULT, Boolean[]), (NEW_APPOINTMENT_ID, Long[]), (PERSONS, Person[]), (APPOINTMENTS, Appointment[]), <br />
	 * 				 (MEETING_ROOMS, MeetingRoom[]), (APPOINTMENT_TIME_INTERVALS, Appointment[]), (NOTIFICATIONS, Notification[])
	 * 
	 * @param type - send data type
	 * @param objects - Boolean[]/Long[]/Person[]/Appointment[]/MeetingRoom[]/Notification[]
	 * @return an XML document that contains send data information
	 */
	public Document sendDataToXml(SendDataType type, Object[] objects) {
		Element element = new Element("send_data");
		
		switch (type) {
			case LOGIN_RESULT:
				boolean result = (Boolean) objects[0];
				Element resultElement = new Element("login_result");
				resultElement.appendChild(new Integer(result ? 1 : 0).toString());
				element.appendChild(resultElement);
				break;
			case NEW_APPOINTMENT_ID:
				Long appointmentId = (Long) objects[0];
				Element appointmentIdElement = new Element("new_appointment_id");
				appointmentIdElement.appendChild(appointmentId.toString());
				element.appendChild(appointmentIdElement);
				break;
			case PERSONS:
				Person[] persons = (Person[]) objects;
				for (int i = 0; i < persons.length; i++)
					element.appendChild(personToElement(persons[i]));
				break;
			case APPOINTMENTS:
				Appointment[] appointments = (Appointment[]) objects;
				for (int i = 0; i < appointments.length; i++)
					element.appendChild(appointmentToElement(appointments[i]));
				break;
			case MEETING_ROOMS:
				MeetingRoom[] meetingRooms = (MeetingRoom[]) objects;
				for (int i = 0; i < meetingRooms.length; i++)
					element.appendChild(meetingRoomToElement(meetingRooms[i]));
				break;
			case APPOINTMENT_TIME_INTERVALS:
				Appointment[] timeIntervalsInAppointment = (Appointment[]) objects;
				for (int i = 0; i < timeIntervalsInAppointment.length; i++)
					element.appendChild(appointmentTimeIntervalToElement(timeIntervalsInAppointment[i]));
				break;
			case NOTIFICATIONS:
				Notification[] notifications = (Notification[]) objects;
				for (int i = 0; i < notifications.length; i++)
					element.appendChild(notificationToElement(notifications[i]));
				break;
		}
		
		return new Document(element);
	}
	
	/**
	 * The concrete type of a SEND_DATA XML string.
	 * <br /><br />
	 * LOGIN_RESULT - the XML string contains the login result (if the user managed to login) <br />
	 * NEW_APPOINTMENT_ID - the XML string contains an id to a newly created appointment <br />
	 * PERSONS - the XML string contains persons <br />
	 * APPOINTMENTS - the XML string contains appointments <br />
	 * MEETING_ROOM - the XML string contains meeting rooms <br />
	 * APPOINTMENT_TIME_INTERVAL - the XML string contains appointments with start and end time <br />
	 * NOTIFICATIONS - the XML string contains notifications <br />
	 * 
	 * @param xml - The XML string of type SEND_DATA.
	 * @return the type of the send data element
	 */
	public SendDataType getSendDataType(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		Element sendElement = doc.getRootElement();
		return getSendDataType(sendElement);
	}
	
	/**
	 * The concrete type of a SEND_DATA XML string.
	 * <br /><br />
	 * LOGIN_RESULT - the XML string contains the login result (if the user managed to login) <br />
	 * NEW_APPOINTMENT_ID - the XML string contains an id to a newly created appointment <br />
	 * PERSONS - the XML string contains persons <br />
	 * APPOINTMENTS - the XML string contains appointments <br />
	 * MEETING_ROOM - the XML string contains meeting rooms <br />
	 * APPOINTMENT_TIME_INTERVAL - the XML string contains appointments with start and end time <br />
	 * NOTIFICATIONS - the XML string contains notifications <br />
	 * 
	 * @param sendElement - the send element
	 * @return the type of the send data element
	 */
	private SendDataType getSendDataType(Element sendElement) {
		Element element = sendElement.getFirstChildElement("login_result");
		if (element != null)
			return SendDataType.LOGIN_RESULT;
		
		element = sendElement.getFirstChildElement("new_appointment_id");
		if (element != null)
			return SendDataType.NEW_APPOINTMENT_ID;
		
		element = sendElement.getFirstChildElement("person");
		if (element != null)
			return SendDataType.PERSONS;
		
		element = sendElement.getFirstChildElement("appointment");
		if (element != null)
			return SendDataType.APPOINTMENTS;
		
		element = sendElement.getFirstChildElement("meeting_room");
		if (element != null)
			return SendDataType.MEETING_ROOMS;
		
		element = sendElement.getFirstChildElement("appointment_time_interval");
		if (element != null)
			return SendDataType.APPOINTMENT_TIME_INTERVALS;
		
		element = sendElement.getFirstChildElement("notification");
		if (element != null)
			return SendDataType.NOTIFICATIONS;
		
		return null;
	}
	
	/**
	 * Converts an XML string containing the send data information to an array of objects.
	 * 
	 * @param xml - The XML String containing the send data information.
	 * @return an array containing a boolean if the SendDataType is LOGIN_RESULT <br />
	 * 		   an array containing a long if the SendDataType is NEW_APPOINTMENT_ID <br />
	 * 		   an array containing Person objects if the SendDataType is PERSONS <br />
	 * 		   an array containing Appointment objects if the SendDataType is APPOINTMENTS <br />
	 * 		   an array containing MeetingRoom objects if the SendDataType is MEETING_ROOMS <br />
	 * 		   an array containing Appointment objects that contain a time interval if the SendDataType is APPOINTMENT_TIME_INTERVAL <br />
	 * 		   an array containing Notification objects if the SendDataType is NOTIFICATIONS
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 * @throws ParseException
	 */
	public Object[] toSendData(String xml) throws ValidityException, ParsingException, IOException, ParseException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		Element element = doc.getRootElement();
		SendDataType type = getSendDataType(element);
		Object[] objects = null;
		
		switch (type) {
			case LOGIN_RESULT:
				Element resultElement = element.getFirstChildElement("login_result");
				objects = new Object[]{Integer.parseInt(resultElement.getValue()) != 0 ? true : false};
				break;
			case NEW_APPOINTMENT_ID:
				Element newAppointmentIdElement = element.getFirstChildElement("new_appointment_id");
				objects = new Object[]{Long.parseLong(newAppointmentIdElement.getValue())};
				break;
			case PERSONS:
				Elements personElements = element.getChildElements("person");
				objects = new Object[personElements.size()];
				for (int i = 0; i < personElements.size(); i++)
					objects[i] = assemblePerson(personElements.get(i));
				break;
			case APPOINTMENTS:
				Elements appointmentElements = element.getChildElements("appointment");
				objects = new Object[appointmentElements.size()];
				for (int i = 0; i < appointmentElements.size(); i++)
					objects[i] = assembleAppointment(appointmentElements.get(i));
				break;
			case MEETING_ROOMS:
				Elements meetingRoomElements = element.getChildElements("meeting_room");
				objects = new Object[meetingRoomElements.size()];
				for (int i = 0; i < meetingRoomElements.size(); i++)
					objects[i] = assembleMeetingRoom(meetingRoomElements.get(i));
				break;
			case APPOINTMENT_TIME_INTERVALS:
				Elements appointmentTimeIntervalElements = element.getChildElements("appointment_time_interval");
				objects = new Object[appointmentTimeIntervalElements.size()];
				for (int i = 0; i < appointmentTimeIntervalElements.size(); i++)
					objects[i] = assembleAppointment(appointmentTimeIntervalElements.get(i));
				break;
			case NOTIFICATIONS:
				Elements notificationElements = element.getChildElements("notification");
				objects = new Object[notificationElements.size()];
				for (int i = 0; i < notificationElements.size(); i++)
					objects[i] = assembleNotification(notificationElements.get(i));
				break;
		}
		
		return objects;
	}
	
	private Person assemblePerson(Element personElement) {
		String name = "", email = "";
		
		Element element = personElement.getFirstChildElement("name");
		if (element != null)
			name = element.getValue();
		
		element = personElement.getFirstChildElement("email");
		if (element != null)
			email = element.getValue();
		
		return new Person(name, email, new Date());
	}
	
	private Element personToElement(Person person) {
		Element element = new Element("person");
		
		Element name = new Element("name");
		name.appendChild(person.getName());
		
		Element email = new Element("email");
		email.appendChild(person.getEmail());
		
		element.appendChild(name);
		element.appendChild(email);
		
		return element;
	}
	
	private MeetingRoom assembleMeetingRoom(Element meetingRoomElement) {
		String name = "";
		int capacity = 0;
		
		Element element = meetingRoomElement.getFirstChildElement("name");
		if (element != null)
			name = element.getValue();
		
		element = meetingRoomElement.getFirstChildElement("capacity");
		if (element != null)
			capacity = Integer.parseInt(element.getValue());
		
		return new MeetingRoom(name, capacity);
	}
	
	private Element meetingRoomToElement(MeetingRoom meetingRoom) {
		Element element = new Element("meeting_room");
		
		Element name = new Element("name");
		name.appendChild(meetingRoom.getName());
		
		Element capacity = new Element("capacity");
		capacity.appendChild(new Integer(meetingRoom.getCapacity()).toString());
		
		element.appendChild(name);
		element.appendChild(capacity);
		
		return element;
	}
	
	private Element appointmentTimeIntervalToElement(Appointment appointment) {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, java.util.Locale.US);
		
		Element element = new Element("appointment_time_interval");
		
		Element startTime = new Element("start_time");
		Date startTimeDate = new Date(appointment.getStartTime().getTimeInMillis());
		startTime.appendChild(format.format(startTimeDate));
		
		Element endTime = new Element("end_time");
		Date endTimeDate = new Date(appointment.getEndTime().getTimeInMillis());
		endTime.appendChild(format.format(endTimeDate));
		
		element.appendChild(startTime);
		element.appendChild(endTime);
		
		return element;
	}
	
	private Notification assembleNotification(Element notificationElement) {
		long id = -1;
		long appointmentId = -1;
		NotificationType type = null;
		
		Element element = notificationElement.getFirstChildElement("id");
		if (element != null)
			id = Long.parseLong(element.getValue());
		
		element = notificationElement.getFirstChildElement("type");
		if (element != null)
			type = NotificationType.valueOf(element.getValue());
		
		element = notificationElement.getFirstChildElement("appointment_id");
		if (element != null)
			appointmentId = Long.parseLong(element.getValue());
		
		Notification notification = new Notification(id, appointmentId, type);
		
		Elements personElements = notificationElement.getChildElements("person");
		for (int i = 0; i < personElements.size(); i++) {
			String name = "", email = "";
			
			element = personElements.get(i).getFirstChildElement("name");
			if (element != null)
				name = element.getValue();
			
			element = personElements.get(i).getFirstChildElement("email");
			if (element != null)
				email = element.getValue();
			
			notification.addParticipantCanceled(new Person(name, email, new Date()));
		}
		
		return notification;
	}
	
	private Element notificationToElement(Notification notification) {
		Element element = new Element("notification");
		
		Element id = new Element("id");
		id.appendChild(new Long(notification.getNotificationId()).toString());
		
		Element type = new Element("type");
		type.appendChild(notification.getType().toString());
		
		Element appointmentId = new Element("appointment_id");
		appointmentId.appendChild(new Long(notification.getAppointmentId()).toString());
		
		element.appendChild(id);
		element.appendChild(type);
		element.appendChild(appointmentId);
		
		Person[] persons = notification.getParticipantCanceled();
		for (int i = 0; i < persons.length; i++) {
			Element person = new Element("person");
			Element name = new Element("name");
			name.appendChild(persons[i].getName());
			Element email = new Element("email");
			email.appendChild(persons[i].getEmail());
			person.appendChild(name);
			person.appendChild(email);
			element.appendChild(person);
		}
		
		return element;
	}
	
	/**
	 * Creates an XML document the element "create_appointment".
	 * This indicates that the database must create a new appointment.
	 * 
	 * @return the XML document containing the element "create_appointment"
	 */
	public Document createAppointmentToXml() {
		Element element = new Element("create_appointment");
		return new Document(element);
	}
	
	/**
	 * Creates an XML document containing the element "remove_appointment" with an id.
	 * This indicates that the database must remove the appointment with the specified id.
	 * 
	 * @param appointmentId - The id to the appointment that is going to be removed.
	 * @return an XML document that says which appointment is going to be removed
	 */
	public Document removeAppointmentToXml(long appointmentId) {
		Element element = new Element("remove_appointment");
		
		Element id = new Element("id");
		id.appendChild(new Long(appointmentId).toString());
		
		element.appendChild(id);
		
		return new Document(element);
	}
	
	/**
	 * Converts the XML string containing the remove appointment information to a long.
	 * The long is an appointment id, that is the appointment to be removed.
	 * 
	 * @param xml - The XML string containing the remove appointment information.
	 * @return - an appointment id
	 * @throws ValidityException
	 * @throws ParsingException
	 * @throws IOException
	 */
	public long toRemoveAppointment(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		return assembleRemoveAppointment(doc.getRootElement());
	}
	
	private long assembleRemoveAppointment(Element appointmentIdElement) {
		long id = -1;
		
		Element element = appointmentIdElement.getFirstChildElement("id");
		if (element != null)
			id = Long.parseLong(element.getValue());
		
		return id;
	}
	
	/**
	 * Returns the type of this XML string.
	 * <br /><br />
	 * LOGIN - the XML string tells the server that a person is trying to login <br />
	 * LOGOUT- the XML string tells the server that a person is trying to logout <br />
	 * UPDATE_DATA - the XML string tells the server to update the database (e.g. appointment title, description...) <br />
	 * REQUEST_DATA - the XML string tells the server that it is making a request (e.g. get persons, meeting rooms...) <br />
	 * SEND_DATA - the XML string that is sent from the server to a client with data (e.g. Person[], MeetingRoom[]...) <br />
	 * CREATE_APPOINTMENT - the XML string that tells the server to create a new appointment <br />
	 * REMOVE_APPOINTMENT - the XML string tells the server to remove a specified appointment <br />
	 * 
	 * @param xml - The XML string to be checked for type.
	 * @return the type of this XML document
	 */
	public XmlType getXmlType(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		return getXmlType(doc);
	}
	
	/**
	 * Returns the type of this XML document.
	 * <br /><br />
	 * LOGIN - the XML string tells the server that a person is trying to login <br />
	 * LOGOUT- the XML string tells the server that a person is trying to logout <br />
	 * UPDATE_DATA - the XML string tells the server to update the database (e.g. appointment title, description...) <br />
	 * REQUEST_DATA - the XML string tells the server that it is making a request (e.g. get persons, meeting rooms...) <br />
	 * SEND_DATA - the XML string that is sent from the server to a client with data (e.g. Person[], MeetingRoom[]...) <br />
	 * CREATE_APPOINTMENT - the XML string that tells the server to create a new appointment <br />
	 * REMOVE_APPOINTMENT - the XML string tells the server to remove a specified appointment <br />
	 * 
	 * @param doc - The XML document to be checked for type.
	 * @return the type of this XML document
	 */
	public XmlType getXmlType(Document doc) {
		Element element = doc.getRootElement();
		
		if (element.getQualifiedName().equals("login"))
			return XmlType.LOGIN;
		else if (element.getQualifiedName().equals("logout"))
			return XmlType.LOGOUT;
		else if (element.getQualifiedName().equals("update_data"))
			return XmlType.UPDATE_DATA;
		else if (element.getQualifiedName().equals("request_data"))
			return XmlType.REQUEST_DATA;
		else if (element.getQualifiedName().equals("send_data"))
			return XmlType.SEND_DATA;
		else if (element.getQualifiedName().equals("create_appointment"))
			return XmlType.CREATE_APPOINTMENT;
		else if (element.getQualifiedName().equals("remove_appointment"))
			return XmlType.REMOVE_APPOINTMENT;
		
		return null;
	}
	
	/**
	 * Checks if the XML string is a notification.
	 * 
	 * @param xml - The XMl string to be examined.
	 * @return true if the XML string is a notification
	 * @throws IOException 
	 * @throws ParsingException 
	 * @throws ValidityException 
	 */
	public boolean isNotification(String xml) throws ValidityException, ParsingException, IOException {
		nu.xom.Builder parser = new nu.xom.Builder(false);
		nu.xom.Document doc = parser.build(xml, "");
		
		if (getXmlType(doc) == XmlType.SEND_DATA) {
			Element element = doc.getRootElement();
			if (element.getFirstChildElement("notification") != null)
				return true;
		}
		
		return false;
	}
	
	private GregorianCalendar parseDateTime(String dateTime) throws ParseException {
		DateFormat format = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, java.util.Locale.US);
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(format.parse(dateTime).getTime());
		return calendar;
	}
}

