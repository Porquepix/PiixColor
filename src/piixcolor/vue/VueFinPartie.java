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
	
	private static final String IMAGE_END = Modele.DOSSIER_ASSETS + "/partie-finie.png";
	private static final String IMAGE_REPLAY_BUTTON = Modele.DOSSIER_ASSETS + "/btn-rejouer.png";
	private static final String IMAGE_REPLAY_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-rejouer-hover.png";
	private static final String IMAGE_HOME_BUTTON = Modele.DOSSIER_ASSETS + "/btn-accueil.png";
	private static final String IMAGE_HOME_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-accueil-hover.png";

	public VueFinPartie(Fenetre f) {
		super(f, null);
		
		setBackground(Color.WHITE);
		
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);
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

	public void actualise(int sig) {
		
	}
	
	class playButtonListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			fenetre.switchPanel(new VuePlateau(fenetre, new PlateauControleur(Modele.getInstance())));	
		}

		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_REPLAY_BUTTON_HOVER));
		}

		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_REPLAY_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	class homeButtonListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			fenetre.switchPanel(new VueAccueil(fenetre, new AccueilControleur(Modele.getInstance())));		
		}

		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_HOME_BUTTON_HOVER));
		}

		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_HOME_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}

}
