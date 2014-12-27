package piixcolor.vue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import piixcolor.modele.Modele;

@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	
	/**
	 * Largeur de la fenetre avec un minimal de 1000 px.
	 */
	public static int FRAME_WIDTH = 1000;
	
	/**
	 * Hauteur de la fenetre avec un minimal de 600 px.
	 */
	public static int FRAME_HEIGHT = 600;
	
	/**
	 * Titre de la fenetre.
	 */
	public static final String FRAME_TITLE = "PiixColor";
	
	/**
	 * Chemin vers la miniature / icone de la fenetre.
	 */
	private static final String LOGO = Modele.DOSSIER_ASSETS + "miniature.png";
	
	/**
	 * Instance de la fenetre (pattern Singleton)
	 */
	private static Fenetre instance = null;

	/**
	 * Constructeur de la fenetre. Créer une fenetre en fesant apelle au méthode de JFrame.
	 */
	private Fenetre () {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	closeOperation();
            }
		});
		
		setIconImage(new ImageIcon(LOGO).getImage());
		
		setTitle(FRAME_TITLE);
		pack();
		setVisible(true);
		setResizable(false);
		setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight()));

		FRAME_WIDTH = this.getWidth();
		FRAME_HEIGHT = this.getHeight();
		
		setLocationRelativeTo(null);
	}
	
	/**
	 * Méthode appellé lors de la fermeture de la fenetre.
	 * Informe l'utilisateur si une configuration a été modifiée mais pas sauvegardée.
	 */
	private void closeOperation() {
    	if (Modele.getInstance().isModifie()) {
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
	
	/**
	 * Méthode permetant de recupérer la seule instance de la fenetre (pattern Singleton).
	 * 
	 * @return L'instance de la fenetre
	 */
	public static Fenetre getInstance() {
		if (instance == null) instance = new Fenetre();
		return instance;
	}

	/**
	 * Remplace le content pane de la fentre par le panel passer en paramètre.
	 * 
	 * @param p Nouveau content pane
	 */
	public void switchPanel(JPanel p) {
		this.getContentPane().removeAll();
		this.setContentPane(p);
		this.getContentPane().revalidate();
	}

}
