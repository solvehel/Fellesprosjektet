import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import no.ntnu.fp.model.Appointment;
import no.ntnu.fp.model.ModelManager;
import no.ntnu.fp.model.Notification;
import no.ntnu.fp.model.UpdateListener;


public class mainGUI implements UpdateListener {
	
	protected static ModelManager model;
	protected static RegisterMeeting rm;
	protected static CalenderView cv;
	
	public mainGUI(ModelManager model) {
		this.model = model;
		try
		{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch (ClassNotFoundException e) {}
		catch (InstantiationException e) {}
		catch (IllegalAccessException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		
		cv = new CalenderView(this);
		rm = new RegisterMeeting(this);
		cv.setVisible(true, cv);
		rm.setVisible(true, rm);
		
	}

	@Override
	public void notificationReceived(Notification notification) {
		cv.addNotification(notification);
		
	}

	@Override
	public void appointmentUpdateReceived(Appointment appointment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appointmentCreated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appointmentRemoved(long appointmentId) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main (String [] args) {
		mainGUI main = new mainGUI(new ModelManager());
	}
}
