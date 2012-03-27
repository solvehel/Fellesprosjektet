/*Contents of CalendarProgran.class */

//Import packages
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;


import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CalendarProgram extends JPanel  {
	protected static JLabel lblMonth, lblYear;
	protected static JButton btnPrev, btnNext;
	protected static JTable tblCalendar;
	protected static JComboBox cmbYear;
	protected static DefaultTableModel mtblCalendar; //Table model
	protected static JScrollPane stblCalendar; //The scrollpane
	protected static int realYear, realMonth, realDay, currentYear, currentMonth;
	protected static GridBagConstraints gbc;
	protected static CalenderView cv;
	
	public CalendarProgram () {
		
	}
	
	public CalendarProgram (CalenderView cv){
		this.cv = cv;
		//Create controls
		lblMonth = new JLabel ("January");
		lblYear = new JLabel ("Change year:");
		cmbYear = new JComboBox();
		btnPrev = new JButton ("<<");
		btnNext = new JButton (">>");
		mtblCalendar = new DefaultTableModel(){public boolean isCellEditable(int rowIndex, int mColIndex){return false;}};
		tblCalendar = new JTable(mtblCalendar);
		stblCalendar = new JScrollPane(tblCalendar);

		//Layout
		
		//Register action listeners
		btnPrev.addActionListener(new btnPrev_Action());
		btnNext.addActionListener(new btnNext_Action());
		cmbYear.addActionListener(new cmbYear_Action());
		this.setLayout(null);
		//Add controls to pane
		add(lblMonth);
		add(stblCalendar);
		add(lblYear);
		add(cmbYear);
		add(btnPrev);
		add(btnNext);
		
		//Set bounds
		this.setBounds(0, 0, 270, 200);
		lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 100, 25);
		lblYear.setBounds(110, 189, 80, 20);
		cmbYear.setBounds(190, 189, 70, 20);
		btnPrev.setBounds(10, 25, 48, 25);
		btnNext.setBounds(210, 25, 48, 25);
		stblCalendar.setBounds(10, 50, 250, 139);
		
		//Get real month/year
		GregorianCalendar cal = new GregorianCalendar(); //Create calendar
		realDay = cal.get(GregorianCalendar.DAY_OF_MONTH); //Get day
		realMonth = cal.get(GregorianCalendar.MONTH); //Get month
		realYear = cal.get(GregorianCalendar.YEAR); //Get year
		currentMonth = realMonth; //Match month and year
		currentYear = realYear;
		
		//Add listener
		SelectionListener listener = new SelectionListener(tblCalendar);
		tblCalendar.getSelectionModel().addListSelectionListener(listener);
		tblCalendar.getColumnModel().getSelectionModel().addListSelectionListener(listener);
		
		
		//Add headers
		String[] headers = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"}; //All headers
		for (int i=0; i<7; i++){
			mtblCalendar.addColumn(headers[i]);
		}
		
		tblCalendar.getParent().setBackground(tblCalendar.getBackground()); //Set background

		//No resize/reorder
		tblCalendar.getTableHeader().setResizingAllowed(false);
		tblCalendar.getTableHeader().setReorderingAllowed(false);

		//Single cell selection
		tblCalendar.setColumnSelectionAllowed(true);
		tblCalendar.setRowSelectionAllowed(true);
		tblCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//Set row/column count
		tblCalendar.setRowHeight(19);
		mtblCalendar.setColumnCount(7);
		mtblCalendar.setRowCount(6);
		
		
		//Populate table
		for (int i=realYear-100; i<=realYear+100; i++){
			cmbYear.addItem(String.valueOf(i));
		}
		
		//Refresh calendar
		refreshCalendar(realMonth, realYear); //Refresh calendar
	}
	

	public void add (Component comp, int x, int y, GridBagConstraints gbc) {
		gbc.gridx = x;
		gbc.gridy = y;
		add(comp, gbc);
	}
	
	public static void refreshCalendar(int month, int year){
		//Variables
		String[] months =  {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		int nod, som; //Number Of Days, Start Of Month
			
		//Allow/disallow buttons
		btnPrev.setEnabled(true);
		btnNext.setEnabled(true);
		if (month == 0 && year <= realYear-10){btnPrev.setEnabled(false);} //Too early
		if (month == 11 && year >= realYear+100){btnNext.setEnabled(false);} //Too late
		lblMonth.setText(months[month]); //Refresh the month label (at the top)
		lblMonth.setBounds(160-lblMonth.getPreferredSize().width/2, 25, 180, 25); //Re-align label with calendar
		cmbYear.setSelectedItem(String.valueOf(year)); //Select the correct year in the combo box
		
		//Clear table
		for (int i=0; i<6; i++){
			for (int j=0; j<7; j++){
				mtblCalendar.setValueAt(null, i, j);
			}
		}
		
		//Get first day of month and number of days
		GregorianCalendar cal = new GregorianCalendar(year, month, 1);
		nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		som = cal.get(GregorianCalendar.DAY_OF_WEEK);
		
		//Draw calendar
		for (int i=1; i<=nod; i++){
			int row = new Integer((i+som-2)/7);
			int column  =  (i+som-2)%7;
			mtblCalendar.setValueAt(i, row, column);
		}

		//Apply renderers
		tblCalendar.setDefaultRenderer(tblCalendar.getColumnClass(0), new tblCalendarRenderer());
	}

	static class tblCalendarRenderer extends DefaultTableCellRenderer{
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column){
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			if (column == 5 || column == 6){ //Week-end
				setBackground(new Color(255, 220, 220));
			}
			else{ //Week
				setBackground(new Color(255, 255, 255));
			}
			if (value != null){
				if (Integer.parseInt(value.toString()) == realDay && currentMonth == realMonth && currentYear == realYear){ //Today
					setBackground(new Color(220, 220, 255));
				}
			}
			if (selected) {
				setBackground(new Color(0, 0, 255));
			}
			setBorder(null);
			setForeground(Color.black);
			return this;  
		}
	}

	static class btnPrev_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 0){ //Back one year
				currentMonth = 11;
				currentYear -= 1;
			}
			else{ //Back one month
				currentMonth -= 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	static class btnNext_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (currentMonth == 11){ //Foward one year
				currentMonth = 0;
				currentYear += 1;
			}
			else{ //Foward one month
				currentMonth += 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
	}
	static class cmbYear_Action implements ActionListener{
		public void actionPerformed (ActionEvent e){
			if (cmbYear.getSelectedItem() != null){
				String b = cmbYear.getSelectedItem().toString();
				currentYear = Integer.parseInt(b);
				refreshCalendar(currentMonth, currentYear);
			}
		}
	}
	
	
	static class SelectionListener implements ListSelectionListener {
		JTable table;
		
		SelectionListener(JTable table) {
			this.table = table;
		}
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {	
				if ((e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()) || e.getSource() == table.getColumnModel().getSelectionModel() && table.getColumnSelectionAllowed()) {
					int row = table.getSelectionModel().getMinSelectionIndex();
					int [] columns = table.getColumnModel().getSelectedColumns();
					int firstDay = 0;
					int daysToGoBack = 0;
					for (int i = 0; i < 7; i++) {
						if (table.getModel().getValueAt(row, i) != null) {
							firstDay = (Integer) table.getModel().getValueAt(row, i);
							break;
						}
						else daysToGoBack++;
					}
					if (firstDay == 0) return;
					GregorianCalendar cal = new GregorianCalendar(currentYear, currentMonth, 1);
					int nod = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
					int daysLeftOfMonth = nod - firstDay;
					int formerMonthDays = 0;
					if (daysToGoBack > 0) {
						GregorianCalendar cal2;
						if (currentMonth == 1) {
							cal2 = new GregorianCalendar(currentYear-1, 12, 1);
						}
						else {
							cal2 = new GregorianCalendar(currentYear, currentMonth-1, 1);
						}
						formerMonthDays = cal2.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
					}
					
					cv.setWeek(firstDay, daysLeftOfMonth, currentYear, currentMonth, daysToGoBack, formerMonthDays, columns[0]);
				}
			}
		}
	}
	
}


