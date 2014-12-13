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

import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.Controleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;

@SuppressWarnings("serial")
public class VueAccueil extends Vue {

	private static final String IMAGE_BACKGROUND = Modele.DOSSIER_ASSETS + "/bg.jpg";
	private static final String IMAGE_LOGO = Modele.DOSSIER_ASSETS + "/logo.png";
	private static final String IMAGE_PLAY_BUTTON = Modele.DOSSIER_ASSETS + "/btn-jouer.png";
	private static final String IMAGE_PLAY_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-jouer-hover.png";
	private static final String IMAGE_ADMIN_BUTTON = Modele.DOSSIER_ASSETS + "/btn-enseignant.png";
	private static final String IMAGE_ADMIN_BUTTON_HOVER = Modele.DOSSIER_ASSETS + "/btn-enseignant-hover.png";
	
	private BufferedImage image;

	public VueAccueil(Fenetre f, Controleur controleur) {
		super(f, controleur);
		
		JPanel container = new JPanel();
		container.setOpaque(false);
		container.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//LOAD BACKGROUND
		try {
			BufferedImage bg = ImageIO.read(new File(IMAGE_BACKGROUND));
			image = getControleur().resizeImage(bg, Fenetre.FRAME_WIDTH, Fenetre.FRAME_HEIGHT);
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

	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    int x = (this.getWidth() - image.getWidth(null)) / 2;
	    int y = (this.getHeight() - image.getHeight(null)) / 2;
	    g2d.drawImage(image, x, y, null);
	}
	
	public void actualise(int sig) {
		
	}
	
	class playButtonListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			Modele m = Modele.getInstance();
			if(m.getCouleursConfig().size() == 0 || m.getFormesConfig().size() == 0) {
				BoiteDialogue.createModalBox(JOptionPane.CANCEL_OPTION, "Attention", "Attention ! Il n'y a aucune image ou couleurs dans la configuration actuelle !");
			} else {
				fenetre.switchPanel(new VuePlateau(fenetre, new PlateauControleur(m)));	
			}
		}

		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_PLAY_BUTTON_HOVER));
		}

		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_PLAY_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}
	
	class adminButtonListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			fenetre.switchPanel(new VueAdmin(fenetre, new AdminControleur(Modele.getInstance())));		
		}

		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_ADMIN_BUTTON_HOVER));
		}

		public void mouseExited(MouseEvent e) {	
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_ADMIN_BUTTON));
		}

		public void mousePressed(MouseEvent e) {
			
		}

		public void mouseReleased(MouseEvent e) {
			
		}
		
	}

}
