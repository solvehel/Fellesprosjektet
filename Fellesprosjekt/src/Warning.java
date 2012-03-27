 

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Warning extends JFrame implements ActionListener, KeyListener {
		
	private JButton slett;
	private JButton avbryt;
	private GridBagConstraints gbc;
	
	public Warning() {
		
		gbc = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		gbc.insets = new Insets(5,10,5,10);
		
		
		JLabel label = new JLabel();
		label.setText("Vil du slette denne avtalen? ");
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=2;
		buttonPanel.add(label,gbc);

		gbc.gridwidth=1;
		slett = new JButton("Slett");
		slett.addActionListener(this);
		gbc.gridx=0;
		gbc.gridy=1;
		buttonPanel.add(slett, gbc);
		
		avbryt = new JButton("Avbryt");
		avbryt.addActionListener(this);
		gbc.gridx=1;
		gbc.gridy=1;
		buttonPanel.add(avbryt, gbc);
		
		
		gbc.gridx=0;
		gbc.gridy=1;
		panel.add(buttonPanel,gbc);
		
		this.pack();
		this.setVisible(true);
		
		
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(slett)){
			System.out.println("Slettet");
		}
		else if(e.getSource().equals(avbryt)){
			System.out.println("Avbryt");
		}
		
			
		
	}
	
	public static void main(String[] args) {
		Warning frame = new Warning();
		frame.setTitle("Advarsel");
		frame.setBounds(0, 0, 300, 200);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
	}

	

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
