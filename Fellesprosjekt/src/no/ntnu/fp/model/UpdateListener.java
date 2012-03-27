package no.ntnu.fp.model;

/**
 * @author Sebastian Zalewski
 */
public interface UpdateListener {
	/**
	 * This method is invoked when a notification is received from the server.
	 * 
	 * @param notification - The notification received from the server.
	 */
	public void notificationReceived(Notification notification);
	
	/**
	 * This method is invoked when a user is watching another user appointments. <br />
	 * <br /><br />
	 * NB! The update is an update to the user that is currently being watched.<br />
	 * This method can still be invoked even if the user is not watching another user. <br />
	 * After getAppointments is called, to another user, then this method will be invoked every time <br />
	 * there is an update to an appointment to that user.
	 * 
	 * @param appointment - The new appointment to the user currently being watched.
	 */
	public void appointmentUpdateReceived(Appointment appointment);
	
	/**
	 * This method is invoked when a user is watching another user appointments, <br />
	 * and the user being watched gets a new appointment.
	 * <br /><br />
	 * NB! The update is an update to the user that is currently being watched.<br />
	 * This method can still be invoked even if the user is not watching another user. <br />
	 * After getAppointments is called, to another user, then this method will be invoked every time <br />
	 * the user being watched get a new appointment.
	 */
	public void appointmentCreated();
	
	/**
	 * This method is invoked when a user is watching another user appointments, <br />
	 * and an appointment is removed from the user being watched.
	 * <br /><br />
	 * NB! The update is an update to the user that is currently being watched.<br />
	 * This method can still be invoked even if the user is not watching another user. <br />
	 * After getAppointments is called, to another user, then this method will be invoked every time <br />
	 * an appointment is removed from that user.
	 * 
	 * @param appointmentId - The appointment id to the appointment that is removed.
	 */
	public void appointmentRemoved(long appointmentId);
}
