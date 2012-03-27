import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;


public class AcceptMeeting  extends JFrame implements ActionListener {

	private JButton godta;
	private JButton forkast;
	private GridBagConstraints gbc;
	private JTable table;

	
	public AcceptMeeting(){
		initgui();
	}
	

	public void initgui() {
		int i = 0;
		gbc = new GridBagConstraints();
		JPanel panel = new JPanel(new GridBagLayout());
		this.getContentPane().add(panel);
		gbc.insets = new Insets(5,10,5,10);
		
		
		JPanel textPanel = new JPanel(new GridBagLayout());
		JTextArea anavn = new JTextArea(1,20);
		JTextArea dato = new JTextArea(1,10);
		JTextArea rom = new JTextArea(1,10);
		JTextArea beskriv = new JTextArea(5,20);
		JTextArea deltar = new JTextArea(5,20);
		anavn.setEditable(false);
		dato.setEditable(false);
		rom.setEditable(false);
		beskriv.setEditable(false);
		deltar.setEditable(false);
		
		
		
		gbc.gridx=0;
		gbc.gridy=i;
		anavn.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(anavn, gbc);
		
		
		i++;
		gbc.gridx=0;
		gbc.gridy=i;
		dato.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(dato, gbc);
		
		i++;
		gbc.gridx=0;
		gbc.gridy=i;
		rom.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(rom, gbc);
		
		i++;
		gbc.gridx=0;
		gbc.gridy=i;
		beskriv.setLineWrap(true);
		JScrollPane sp = new JScrollPane(beskriv);
		sp.setPreferredSize(new Dimension(225,100));
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(sp, gbc);

		i++;
		gbc.gridx=0;
		gbc.gridy=i;
		table = new JTable(10, 2);
		
		JScrollPane scp = new JScrollPane(table);
		scp.setPreferredSize(new Dimension(225,100));
		scp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scp.setBorder(BorderFactory.createEtchedBorder());
		textPanel.add(scp, gbc);
		
		
		
		JPanel buttonPanel = new JPanel();
		godta = new JButton("Godta");
		godta.addActionListener(this);
		forkast = new JButton("Forkast");
		forkast.addActionListener(this);
		buttonPanel.add(godta);
		buttonPanel.add(forkast);
		
		
		
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		panel.add(textPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		
		panel.add(buttonPanel,gbc);
		
		this.pack();
		this.setVisible(true);
	}
	
	
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(godta)){
			System.out.println("Godtatt");
		}
		else if(e.getSource().equals(forkast)){
			System.out.println("Forkast");
		}
		
			
		
	}
	
	public static void main(String[] args) {
		AcceptMeeting frame = new AcceptMeeting();
		frame.setTitle("Godta avtale");
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	

	
	
	
}
