package piixcolor.vue;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import piixcolor.controleur.AccueilControleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;


@SuppressWarnings("serial")
public class VueFinPartie extends Vue {
	
	/**
	 * Chemin vers l'image de fin de partie.
	 */
	private static final String IMAGE_END = Modele.DOSSIER_ASSETS + "/partie-finie.png";
	
	/**
	 * Chemin vers l'image du  bouton "rejouer".
	 */
	private static final String IMAGE_REPLAY_BUTTON = Modele.DOSSIER_ASSETS + "/btn-rejouer.png";
	
	/**
	 * Chemin vers l'image du bouton "rejouer" quand la souris passe dessus.
	 */
	private static final String IMAGE_REPLAY_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-rejouer-hover.png";
	
	/**
	 * Chemin vers l'image du bouton "accueil".
	 */
	private static final String IMAGE_HOME_BUTTON = Modele.DOSSIER_ASSETS + "/btn-accueil.png";
	
	/**
	 * Chemin vers l'image du bouton "accueil" quand la souris passe dessus.
	 */
	private static final String IMAGE_HOME_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-accueil-hover.png";

	/**
	 * Constructeur de la VueFinPartie. Appel le superconstructeur de Vue (sans controleur ?).
	 * De nombreux assets sont charg�s et le fond de la vue n'est pas compl�tement opaque.
	 * Il faut aussi d�finir des Listener pour les deux boutons.
	 * 
	 * @param f La fen�tre.
	 */
	public VueFinPartie(Fenetre f) {
		super(f, null);
		
		setBackground(new Color(255, 255, 255, 210));
		
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//LOAD AND DISPLAY END IMAGE
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets((int)(Fenetre.FRAME_HEIGHT * 0.1), 0, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		container.add(new JLabel(new ImageIcon(IMAGE_END)), c);
		
		
		//LOAD AND DISPLAY BUTTONS
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets((int)(Fenetre.FRAME_HEIGHT * 0.2), 0, 0, 0);
		c.gridx = 0;
		c.gridy = 2;
		JLabel btnJouer = new JLabel(new ImageIcon(IMAGE_REPLAY_BUTTON));
		btnJouer.addMouseListener(new playButtonListener());
		btnJouer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		container.add(btnJouer, c);
		
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(25, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 3;
		JLabel btnAdmin = new JLabel(new ImageIcon(IMAGE_HOME_BUTTON));
		btnAdmin.addMouseListener(new homeButtonListener());
		btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		container.add(btnAdmin, c);
		
		
		add(container);
	}

	public void actualise(int sig) {}
	
	/**
	 * Listener impl�ment� sur le bouton "rejouer". 
	 * Il sert � d�tecter le clic dessus ainsi que le survol pour faire changer le style du bouton.
	 */
	class playButtonListener implements MouseListener {

		/**
		 * Lorsque l'on clique sur le bouton on joue une nouvelle partie.
		 */
		public void mouseClicked(MouseEvent e) {
			fenetre.switchPanel(new VuePlateau(fenetre, new PlateauControleur(Modele.getInstance())));	
		}

		/**
		 * Change le style du bouton quand la souris le survol.
		 */
		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_REPLAY_BUTTON_HOVER));
		}

		/**
		 * Remets le style par d�faut du bouton lorsque la souris ne le survol plus.
		 */
		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_REPLAY_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	/**
	 * Listener impl�ment� sur le bouton "accueil". 
	 * Il sert � d�tecter le clic dessus ainsi que le survol pour faire changer le style du bouton.
	 */
	class homeButtonListener implements MouseListener {

		/**
		 * Lorsque l'on clique sur le bouton on retourne � l'accueil.
		 */
		public void mouseClicked(MouseEvent e) {
			fenetre.switchPanel(new VueAccueil(fenetre, new AccueilControleur(Modele.getInstance())));		
		}

		/**
		 * Change le style du bouton quand la souris le survol.
		 */
		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_HOME_BUTTON_HOVER));
		}

		/**
		 * Remets le style par d�faut du bouton lorsque la souris ne le survol plus.
		 */
		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_HOME_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}

}
