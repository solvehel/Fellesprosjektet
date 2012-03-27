
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class RegisterMeeting extends JFrame {

	protected static mainGUI mainGUI;
	
	//Creates a frame with all the JPanels organized. 
	public RegisterMeeting (mainGUI mainGUI) {
		
		this.mainGUI = mainGUI;
		
		//PANEL
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		
		//GRIDBAG
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		
		//TEXT_AREA
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		JPanel textArea = textArea();
		textArea.setBorder(BorderFactory.createLineBorder(Color.PINK));
		add(textArea, 0, 0, gbc, panel);
		
		//ADD_PARTICIPIANT_AREA
		JPanel addParticipantArea = addParticipantArea();
		addParticipantArea.setBorder(BorderFactory.createLineBorder(Color.PINK));
		add(addParticipantArea, 0, 1, gbc, panel);
		
		//ADD BUTTOM BUTTONS
		gbc.gridwidth = 1;
		JPanel auto_cancel = auto_cancel();
		auto_cancel.setBorder(BorderFactory.createLineBorder(Color.PINK));
		JPanel rooms_save = rooms_save();
		rooms_save.setBorder(BorderFactory.createLineBorder(Color.PINK));
		//gbc.anchor = GridBagConstraints.WEST;
		add(rooms_save, 0, 2, gbc, panel);
		//gbc.anchor = GridBagConstraints.EAST;
		add(auto_cancel, 1, 2, gbc, panel);
		
		
		
		this.pack();
		
		
	}
	
	public void setVisible (boolean bool, RegisterMeeting rm) {
		rm.setVisible(bool);
	}
	
	//Returns a panel with all the testFields.
	protected JPanel textArea () {
		//PANEL
		JPanel panel = new JPanel();
		
		//TEXTFIELDS
		JTextField meeting_name = new JTextField(15);
		JTextField starting_time = new JTextField (3);
		JTextField finished_time = new JTextField(3);
		JTextField starting_date = new JTextField(3);
		JTextField finished_date = new JTextField(3);
		
		//TEXTAREAS
		JTextArea description = new JTextArea(5,20);
		description.setBorder(BorderFactory.createLoweredBevelBorder());
	
		//GRIDBAG
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		
		//TITLE
		gbc.anchor = GridBagConstraints.WEST;
		add(new JLabel("Title:"), 0, 0, gbc, panel);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(meeting_name, 1, 0, gbc, panel);
		gbc.fill = GridBagConstraints.NONE;
		
		
		//START TIME
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		add(new JLabel("Start:"), 0, 2, gbc, panel);
		gbc.anchor = GridBagConstraints.CENTER;
		add(starting_time, 1, 2, gbc, panel);
		add(starting_date, 2, 2, gbc, panel);
		
		//END TIME
		gbc.anchor = GridBagConstraints.WEST;
		add(new JLabel("End:"), 0, 3, gbc, panel);
		gbc.anchor = GridBagConstraints.CENTER;
		add(finished_time, 1, 3, gbc, panel);
		add(finished_date, 2, 3, gbc, panel);	
		
		//DESCRIPTION
		gbc.anchor = GridBagConstraints.NORTH;
		add(new JLabel("Description:"), 0, 4, gbc, panel);
		gbc.gridwidth = 8;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		add(description, 0, 5, gbc, panel);
		
		return panel;
	}
	
	//Returns a panel with two lists(One for potential participans, and participants)
	//and two buttons ("add" and "remove")
	public JPanel addParticipantArea () {
		
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5);
		
		//POTENTIAL PARTICIPIANTS
		JList potentialParticipiants = new JList();
		JScrollPane potentialScrollPane = new JScrollPane(potentialParticipiants);
		potentialScrollPane.setPreferredSize(new Dimension(100,130));
		add(potentialScrollPane, 0, 0, gbc, panel);
		
		//BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridBagLayout());
		JButton add = new JButton("Add");
		JButton remove = new JButton("Remove");
		add(add, 0, 0, gbc, buttonPanel);
		add(remove, 0, 1, gbc, buttonPanel);
		add(buttonPanel, 1, 0, gbc, panel);
		
		//ADDED PARTICIPIANTS
		JList addedParticipiants = new JList();
		JScrollPane addedScrollPane = new JScrollPane(addedParticipiants);
		addedScrollPane.setPreferredSize(new Dimension(100,130));
		add(addedScrollPane, 2, 0, gbc, panel);
		
		return panel;
	}
	
	//Returns a panel with the buttons "auto" and "cancel".	
	public JPanel auto_cancel () {
		
		JPanel panel = new JPanel();
		
		//BUTTONS
		JButton auto = new JButton("  Auto  ");
		JButton cancel = new JButton("Cancel");
		
		//GRIDBAG
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,5,10,5);
		
		//AUTO
		gbc.anchor = GridBagConstraints.EAST;
		add(auto, 0, 0, gbc, panel);
				
		//CANCEL
		gbc.anchor = GridBagConstraints.EAST;
		add(cancel, 0, 1, gbc, panel);
		
		
		
		return panel;
		
	}

	//Returns a panel with the combobox Rooms and the button "save".
	public JPanel rooms_save () {
		
		JPanel panel = new JPanel();
		
		//GRIDBAG
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,40,10,40);
		//COMBOBOX
		JComboBox rooms = new JComboBox();
		
		//BUTTON
		JButton save = new JButton("  Save  ");
		
		
		//ROOMS
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		add(rooms, 0, 0, gbc, panel);
		
		//SAVE
		gbc.anchor = GridBagConstraints.WEST;
		add(save, 0, 1, gbc, panel);
		
		return panel;
	}
	
	//Adds the object to the input panel.
	public void add(Component object, int gridx, int gridy, GridBagConstraints gbc, JPanel panel) {
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		panel.add(object, gbc);
	}
	
}
