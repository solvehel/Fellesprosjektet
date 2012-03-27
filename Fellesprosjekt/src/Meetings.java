import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;



public class Meetings extends JFrame implements ActionListener {

	private JButton slett;
	private JButton detaljer;
	private GridBagConstraints gbc;
	private JList list; 
	ListSelectionModel listSelectionModel;
	public static int valg;
	
	
	/**
	 * 
	 */
	public Meetings(){
		gbc = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		gbc.insets = new Insets(5,10,5,10);
		
		JPanel listPanel = new JPanel(new GridBagLayout());
		
		String[] option = { "valg1", "valg2", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3", "valg3" };
		list = new JList(option);
		listSelectionModel = list.getSelectionModel();
		listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
		
		JScrollPane listPane = new JScrollPane(list);
		listPane.setPreferredSize(new Dimension(200,225));
		listPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		listPane.setBorder(BorderFactory.createEtchedBorder());
		listPanel.add(listPane, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(listPanel, gbc);
	
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		slett = new JButton("   Slett   ");
		slett.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 0;
		buttonPanel.add(slett, gbc);
		
		detaljer = new JButton("Detaljer");
		detaljer.addActionListener(this);
		gbc.gridx = 0;
		gbc.gridy = 1;
		buttonPanel.add(detaljer, gbc);
		
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		panel.add(buttonPanel, gbc);
		this.pack();
		this.setVisible(true);
	
		
		
	}
	
	public static void main(String[] args) {
		Meetings frame = new Meetings();
		frame.setTitle("Avtaler");
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(slett)){
			System.out.println("Sletter rad " + valg);
		}
		else if(e.getSource().equals(detaljer)){
			System.out.println("Viser detaljer for rad " + valg);
		}
		
			
		
	}
	
	class SharedListSelectionHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty()) {
				System.out.println("Har dette noengang skjedd?");
			}
			else {
				int minIndex = lsm.getMinSelectionIndex();
				int maxIndex = lsm.getMaxSelectionIndex();
				for (int i = minIndex; i<=maxIndex; i++) {
					if (lsm.isSelectedIndex(i));{
						valg = i;
					}
				}
			}
			
		}
		
	}

		
	
}	
	
	

