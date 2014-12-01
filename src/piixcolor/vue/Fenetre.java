package piixcolor.vue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import piixcolor.modele.Modele;

public class Fenetre extends JFrame {
	
	public static int FRAME_WIDTH = 1000;
	public static int FRAME_HEIGHT = 600;
	public static final String FRAME_TITLE = "PiixColor";
	
	private static Fenetre instance = null;

	private Fenetre () {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	closeOperation();
            }
		});
		
		setTitle(FRAME_TITLE);
		pack();
		setVisible(true);
		setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40));

		FRAME_WIDTH = this.getWidth();
		FRAME_HEIGHT = this.getHeight();
		
		setLocationRelativeTo(null);
	}
	
	private void closeOperation() {
    	if (Modele.getInstance().isEstModifie()) {
    		String[] options = {"Enregistrer", "Ne pas enregistrer", "Annuler"}; 
			int retour = BoiteDialogue.createOptionBox(JOptionPane.YES_NO_CANCEL_OPTION, "Attention", "Attention ! Des modifications ont été aporté a la configuration sans être sauvegardé. Voulez-vous enregister ses modifications ?", options, 0);
			if (retour == JOptionPane.OK_OPTION) {
				BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
				System.exit(0);
			} else if(retour == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
    	} else {		
    		System.exit(0);
    	}
	}
	
	public static Fenetre getInstance() {
		if (instance == null) instance = new Fenetre();
		return instance;
	}

	public void switchPanel(JPanel p) {
		this.getContentPane().removeAll();
		this.setContentPane(p);
		this.getContentPane().revalidate();
	}

}
