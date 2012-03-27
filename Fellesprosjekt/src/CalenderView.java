import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import no.ntnu.fp.model.Notification;


public class CalenderView extends JFrame {
	
	protected static JPanel panel;
	protected static JPanel cp;
	protected static DefaultListModel messagesModel;
	protected static JLabel messagesText;
	protected static JList messages;
	protected static JScrollPane messagesScrollPane;
	protected static DefaultTableModel tableModel;
	protected static JTable weekTable;
	protected static JScrollPane weekTableScroll;
	protected static JList appointments;
	protected static JScrollPane appointmentsScrollPane;
	protected static JLabel showFor;
	protected static JList users;
	protected static JScrollPane usersScrollPane;
	protected static JButton newAppointment;
	protected static JButton editAppointment;
	protected static JButton deleteAppointment;
	protected static JButton myAppointments;
	protected static JButton logOut;
	protected mainGUI mainGUI;
	
	
	
	public CalenderView (mainGUI mainGUI) {
		
		this.mainGUI = mainGUI;
		//Layout
		GridBagConstraints gbc = new GridBagConstraints();

		panel = new JPanel();
		cp = new CalendarProgram(this);
		messagesText = new JLabel("Messages:");
		messagesModel = new DefaultListModel();
		messages = new JList(messagesModel);
		messagesScrollPane = new JScrollPane(messages);
		tableModel = new DefaultTableModel();
		weekTable = new JTable(tableModel);
		weekTableScroll = new JScrollPane(weekTable);
		appointments = new JList();
		appointmentsScrollPane = new JScrollPane(appointments);
		showFor = new JLabel("Show for:");
		users = new JList();
		usersScrollPane = new JScrollPane(users);
		newAppointment = new JButton ("New appointment");
		editAppointment = new JButton("Edit appointment");
		deleteAppointment = new JButton("Delete appointment");
		myAppointments = new JButton("My appointments");
		logOut = new JButton("Log out");

		panel.setLayout(null);
		
		

		//ADD
		panel.add(cp);
		panel.add(messagesText);
		panel.add(messagesScrollPane);
		panel.add(weekTableScroll);
		panel.add(appointmentsScrollPane);
		panel.add(showFor);
		panel.add(usersScrollPane);
		panel.add(newAppointment);
		panel.add(editAppointment);
		panel.add(deleteAppointment);
		panel.add(myAppointments);
		panel.add(logOut);
		
		//Notcies list
		
		
		//Placement and size
		cp.setBounds(10, 0, 270, 230);
		messagesText.setBounds(20, 220, 70, 30);
		messagesScrollPane.setBounds(20,245, 250, 295);
		weekTableScroll.setBounds(300, 25, 550, 163);
		appointmentsScrollPane.setBounds(300, 245, 550, 295);
		showFor.setBounds(880,16,70,30);
		usersScrollPane.setBounds(880,40,130,205);
		newAppointment.setBounds(880, 260, 130, 30);
		editAppointment.setBounds(880, 305, 130, 30);
		deleteAppointment.setBounds(880, 350, 130, 30);
		myAppointments.setBounds(880, 395, 130, 30);
		logOut.setBounds(880, 470, 130, 30);
		
		//Week table config
		String[] headers = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"}; 
		for (int i=0; i<7; i++){
			tableModel.addColumn(headers[i]);
		}
		
		weekTable.getTableHeader().setResizingAllowed(false);
		weekTable.getTableHeader().setReorderingAllowed(false);
		weekTable.setColumnSelectionAllowed(true);
		weekTable.setRowSelectionAllowed(true);
		weekTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		weekTable.setRowHeight(146);
		tableModel.setColumnCount(7);
		tableModel.setRowCount(2);
		weekTableScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		this.setContentPane(panel);
		this.setSize(1050,600);
	}
	
	public void setVisible (boolean bool, CalenderView cv) {
		cv.setVisible(bool);
	}
	
	public void setWeek (int firstDay, int daysLeft, int year, int currentMonth, int daysToGoBack, int formerMonthDays, int column) {
		int newYear = year%100;
		currentMonth ++;
		String[] headers = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
		if (daysToGoBack > 0) {
			firstDay = formerMonthDays - daysToGoBack +1;
			if (currentMonth == 1) {
				newYear --;
				currentMonth = 12;
			}
			else currentMonth--;
			for (int i = 0; i < daysToGoBack; i++) {
				headers[i] = headers[i] + " " + firstDay + "." + currentMonth + "." + newYear;
				firstDay++;
			}
			if (currentMonth == 12) {
				newYear ++;
				currentMonth = 1;
			}
			else currentMonth++;
			firstDay = 1;
			for (int i = daysToGoBack; i < 7; i++) {
				headers[i] = headers[i] + " " + firstDay + "." + currentMonth + "." + newYear;
				firstDay++;
			}
		}
		
		else if (daysLeft < 7) {
			for (int i = 0; i <= daysLeft; i++) {
				headers[i] = headers[i] + " " + firstDay + "." + currentMonth + "." + newYear;
				firstDay ++;
			}
			currentMonth ++;
			if (currentMonth > 12) {
				currentMonth = 1;
				newYear++;
			}
			firstDay = 1;
			for (int i = daysLeft+1; i < 7; i++) {
				headers[i] = headers[i] + " " + firstDay + "." + currentMonth + "." + newYear;
			}
		}
		else {
			for (int i = 0; i < 7; i++) {
			headers[i] = headers[i] + " " + firstDay + "." + currentMonth + "." + newYear;
			firstDay ++;
			}
		}
		for (int i=0; i<7; i++){
			weekTable.getColumnModel().getColumn(i).setHeaderValue(headers[i]);
		}

		weekTable.changeSelection(0, column,false, false);
		weekTable.getTableHeader().resizeAndRepaint();
		
	}
	
	public void addNotification(Notification notis) {
		messagesModel.addElement(notis);
	}
	
}
