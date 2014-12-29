package piixcolor.vue;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import piixcolor.controleur.AccueilControleur;
import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;

@SuppressWarnings("serial")
public class VueAccueil extends Vue {

	/**
	 * Chemin vers l'image de fond de l'accueil.
	 */
	private static final String IMAGE_BACKGROUND = Modele.DOSSIER_ASSETS + "/bg.jpg";
	
	/**
	 * Chemin vers l'image du logo PiixColor.
	 */
	private static final String IMAGE_LOGO = Modele.DOSSIER_ASSETS + "/logo.png";
	
	/**
	 * Chemin vers l'image du bouton "jouer".
	 */
	private static final String IMAGE_PLAY_BUTTON = Modele.DOSSIER_ASSETS + "/btn-jouer.png";
	
	/**
	 * Chemin vers l'image du bouton "jouer" quand la souris passe dessus.
	 */
	private static final String IMAGE_PLAY_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-jouer-hover.png";
	
	/**
	 * Chemin vers l'image du bouton "espace enseignant".
	 */
	private static final String IMAGE_ADMIN_BUTTON = Modele.DOSSIER_ASSETS + "/btn-enseignant.png";
	
	/**
	 * Chemin vers l'image du bouton "espace enseignant" quand la souris passe dessus.
	 */
	private static final String IMAGE_ADMIN_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-enseignant-hover.png";
	
	/**
	 * Image de fond de la vue accueil.
	 */
	private BufferedImage background;

	/**
	 * Constructeur de la VueAccueil. Appel le superconstructeur de Vue.
	 * De nombreux assets sont charg�s.
	 * Il faut aussi d�finir des Listener pour les deux boutons.
	 * 
	 * @param f La fen�tre.
	 * @param controleur Le controleur.
	 * @see AccueilControleur
	 */
	public VueAccueil(Fenetre f, AccueilControleur controleur) {
		super(f, controleur);
		
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//LOAD BACKGROUND
		try {
			BufferedImage bg = ImageIO.read(new File(IMAGE_BACKGROUND));
			background = getControleur().resizeImage(bg, Fenetre.FRAME_WIDTH, Fenetre.FRAME_HEIGHT);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		
		//LOAD AND DISPLAY LOGO
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets((int)(Fenetre.FRAME_HEIGHT * 0.1), 0, 0, 0);
		c.gridx = 0;
		c.gridy = 1;
		container.add(new JLabel(new ImageIcon(IMAGE_LOGO)), c);
		
		
		//LOAD AND DISPLAY BUTTONS
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets((int)(Fenetre.FRAME_HEIGHT * 0.25), 0, 0, 0);
		c.gridx = 0;
		c.gridy = 2;
		JLabel btnJouer = new JLabel(new ImageIcon(IMAGE_PLAY_BUTTON));
		btnJouer.addMouseListener(new playButtonListener());
		btnJouer.setCursor(new Cursor(Cursor.HAND_CURSOR));
		container.add(btnJouer, c);
		
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(25, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 3;
		JLabel btnAdmin = new JLabel(new ImageIcon(IMAGE_ADMIN_BUTTON));
		btnAdmin.addMouseListener(new adminButtonListener());
		btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
		container.add(btnAdmin, c);
		
		
		add(container);
	}

	/**
	 * Red�finition de paintComponent pour adapter le dessin du graphics � la taille de notre fen�tre.
	 */
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    int x = (this.getWidth() - background.getWidth(null)) / 2;
	    int y = (this.getHeight() - background.getHeight(null)) / 2;
	    g2d.drawImage(background, x, y, null);
	}
	
	public void actualise(int sig) {
		
	}
	
	/**
	 * Listener impl�ment� sur le bouton "jouer". 
	 * Il sert � d�tecter le clic dessus ainsi que le survol pour faire changer le style du bouton.
	 */
	class playButtonListener implements MouseListener {

		/**
		 * Lorsque l'on clique sur le bouton le plateau de jeu s'ouvre et une nouvelle partie commence.
		 */
		public void mouseClicked(MouseEvent e) {
			Modele m = Modele.getInstance();
			if(m.getCouleursConfig().size() == 0 || m.getFormesConfig().size() == 0) {
				BoiteDialogue.createModalBox(JOptionPane.CANCEL_OPTION, "Attention", "Attention ! Il n'y a aucune image ou couleurs dans la configuration actuelle !");
			} else {
				fenetre.switchPanel(new VuePlateau(fenetre, new PlateauControleur(m)));	
			}
		}

		/**
		 * Change le style du bouton quand la souris le survol.
		 */
		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_PLAY_BUTTON_HOVER));
		}

		/**
		 * Remets le style par d�faut du bouton lorsque la souris ne le survol plus.
		 */
		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_PLAY_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	/**
	 * Listener impl�ment� sur le bouton "espace enseignant". 
	 * Il sert � d�tecter le clic dessus ainsi que le survol pour faire changer le style du bouton.
	 */
	class adminButtonListener implements MouseListener {

		/**
		 * Lorsque l'on clique sur le bouton l'administration s'ouvre.
		 */
		public void mouseClicked(MouseEvent e) {
			fenetre.switchPanel(new VueAdmin(fenetre, new AdminControleur(Modele.getInstance())));		
		}

		/**
		 * Change le style du bouton quand la souris le survol.
		 */
		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_ADMIN_BUTTON_HOVER));
		}

		/**
		 * Remets le style par d�faut du bouton lorsque la souris ne le survol plus.
		 */
		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_ADMIN_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}

}
