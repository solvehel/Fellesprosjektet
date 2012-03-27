 

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class Login extends JFrame implements ActionListener, KeyListener {
		
	private JButton logginn;
	private JButton avbryt;
	private GridBagConstraints gbc;
	//private mainGUI main;
	private String bruker;
	private char[] passord;
	private String pass;
	private JTextField br;
	private JPasswordField pw;
	
	
	public Login() {
		
		gbc = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		gbc.insets = new Insets(5,10,5,10);
	
		JPanel textPanel = new JPanel(new GridBagLayout());	
		JLabel labelbr = new JLabel();
		JLabel labelpw = new JLabel();
		br = new JTextField(10);
		br.addActionListener(this);
		pw = new JPasswordField(10);
		br.addActionListener(this);
		labelbr.setText("Brukernavn: ");
		labelpw.setText("Passord: ");
		
		gbc.gridx=0;
		gbc.gridy=0;
		textPanel.add(labelbr,gbc);
		
		gbc.gridx=1;
		gbc.gridy=0;
		br.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(br,gbc);
		
		gbc.gridx=0;
		gbc.gridy=1;
		textPanel.add(labelpw, gbc);
		
		gbc.gridx=1;
		gbc.gridy=1;
		pw.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(pw,gbc);

		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		panel.add(textPanel,gbc);
		
		

		JPanel buttonPanel = new JPanel(new GridBagLayout());		
		gbc.gridwidth=1;
		logginn = new JButton("Logg inn");
		logginn.addActionListener(this);
		gbc.gridx=0;
		gbc.gridy=1;
		buttonPanel.add(logginn, gbc);
		
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
		bruker = br.getText();
		passord = pw.getPassword();
		if(e.getSource().equals(logginn)){
			pass = passord.toString();
			System.out.println("Brukernavn: " + bruker + "\nPassord: " + pass);
		}
		else if(e.getSource().equals(avbryt)){
			System.exit(0);
		}
		
		
			
		
	}
	
	public static void main(String[] args) {
		Login frame = new Login();
		frame.setTitle("Logg inn");
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
