package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import piixcolor.controleur.AccueilControleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;

@SuppressWarnings("serial")
public class VuePlateau extends Vue implements MouseListener, MouseMotionListener {
	private static final String IMAGE_RETURN = Modele.DOSSIER_ASSETS + "exit.png";
	
	JLayeredPane layeredPane;
	JPanel matrice;
	JLabel formeCourante;
	Container caseFormeCourante;
	int ajustementX;
	int ajustementY;
	Dimension dimensionMatrice;

	
/**
 * Constructeur classique d'une vue de plateau de jeu. On crée un LayeredPane pour le drag and drop, on crée un Panel représentant la matrice que l'on divise en grille de sous pannels carrés.
 * On crée un bouton pour retourner à l'accueil. Enfin on initialise la vue en appelant initVue().
 * 
 * @param f 
 * 		Fenetre dans laquelle se trouve la vue du plateau
 * @param c
 * 		Controleur de la vue du plateau
 * @see initVue
 */
	public VuePlateau(Fenetre f, PlateauControleur c) {
		super(f, c);
		
		getControleur().getModele().ajoutObservateur(this);
		
		setLayout(new BorderLayout());
		
		// Utilisation du JLayeredPane
		dimensionMatrice = new Dimension(fenetre.getWidth(), fenetre.getHeight());
		layeredPane = new JLayeredPane();
		this.add(layeredPane);
		layeredPane.setPreferredSize(dimensionMatrice);
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);
		
		// Initialisation de la vue
		initVue();
		
		// Création du bouton de retour à l'accueil
		ajoutBoutonRetour();		
	}
	
	/**
	 * Constructeur permettant de créer une vue plateau complètment statique et dépourvu de bouton de retour.
	 * Elle est néanmoins mise à jour automatiquement en fonction des modification du modèle grâce à son controleur. Constructeur servant à la génération d'aperçus.
	 * 
	 * @param f 
	 * 		Fenetre dans laquelle se trouve la vue du plateau
	 * @param c
	 * 		Controleur de la vue du plateau
	 * @param dimensionVue
	 * 		Dimension voulue pour l'aperçu
	 * @see VueAdmin
	 */
	public VuePlateau(Fenetre f, PlateauControleur c, Dimension dimension) {
		super(f, c);
		setLayout(new BorderLayout());

		dimensionMatrice = dimension;
		
		// Utilisation du JLayeredPane
		layeredPane = new JLayeredPane();
		this.add(layeredPane);
		layeredPane.setPreferredSize(dimensionMatrice);

		initVue();
	}
	/**
	 * Initialisation de la vue plateau :
	 * Création d'un GridLayout correspondant on nombre de case actuel puis parcours de la matrice pour y ajouter ses composantes et son quadrillage.
	 * Ensuite trois boucles qui parcourent les panels de la matrice pour y afficher les formes, couleurs et objets colorés correspondant à la configuration en cours.
	 */
	public void initVue(){
		JLabel image;
		JPanel panel;
		
		if (getControleur().getNbCouleur() == 0) {
			setBackground(Color.WHITE);
			return;
		}
		
		if (getControleur().getNbForme() == 0) {
			setBackground(Color.WHITE);
			return;
		}
		
		// Ajout de la matrice
		matrice = new JPanel();
		matrice.setPreferredSize(dimensionMatrice);
		matrice.setBounds(0, 0, dimensionMatrice.width, dimensionMatrice.height);
		layeredPane.add(matrice, JLayeredPane.DEFAULT_LAYER);
		
		matrice.setLayout(new GridLayout((getControleur().getNbCouleur()*2) + 1, getControleur().getNbForme() + 1));
		
		for (int i = 0; i < getControleur().taillePlateau(); i++) {
			JPanel square = new JPanel(new BorderLayout());
			matrice.add(square);
			square.setBackground(Color.white);
			if(i < (getControleur().getNbCouleur()+1) * (getControleur().getNbForme() +1)) {
				square.setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}
		
		//Ajout Formes :
		for(int i = 1; i <= getControleur().getNbForme(); i++){
			try {
				BufferedImage image1 = ImageIO.read(new File(getControleur().getModele().getFormesConfig().get(i-1).getAbsolutePath()));
				image = new JLabel(new ImageIcon(getControleur().resizeImage(image1, ((PlateauControleur) getControleur()).getCaseHeight(), ((PlateauControleur) getControleur()).getCaseHeight())));
				panel = (JPanel) matrice.getComponent(i);
				panel.add(image);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//Ajout Couleurs :
		int k = 0;
		for(int i = getControleur().getNbForme()+1; i <= (getControleur().getNbCouleur()*(getControleur().getNbForme()+1)); i = i + getControleur().getNbForme() + 1){
			image = new JLabel(new ImageIcon());
			panel =  (JPanel) matrice.getComponent(i);
			panel.setBackground(getControleur().getModele().getCouleursConfig().get(k).getCouleur());
			panel.add(image);
			k++;
		}
		
		//Ajout des formes colorés à "formes"
		int l = ((getControleur().getNbCouleur()+1)*(getControleur().getNbForme()+1));
		for (int i = 0; i < getControleur().getNbObjetColore(); i++) {
			BufferedImage image1 = getControleur().getModele().getReserveForme().get(i).getImage();
			JLabel objetColore = new JLabel(new ImageIcon(getControleur().resizeImage(image1, ((PlateauControleur) getControleur()).getCaseHeight(), ((PlateauControleur) getControleur()).getCaseHeight())));	
			//On empèche d'écrire au delà du plateau mais aussi sur le bouton de retour donc si on atteint taillePlateau() - 1 on arrête de dessiner des formes
			if(l >= getControleur().taillePlateau() - 1) {
				break;
			}
			panel =  (JPanel) matrice.getComponent(l);
			l++;
			panel.add(objetColore);
		}
	}
	
	/**
	 * Méthode de nettoyage de la vue plateau :
	 * On retire tous les composants des panels de la matrice et on repeint leurs fonds en blanc.
	 */
	public void clearVue() {
		if (matrice != null)
		layeredPane.remove(matrice);
	}
	
	/**
	 * Rafraichissement de la vue :
	 * On nettoie la vue de tous ses anciens composants et on la réinitialise avec le modèle mis à jour
	 * @see clearVue
	 * @see initVue
	 */
	public void refreshVue() {
		clearVue();
		initVue();
	}

	public void ajoutBoutonRetour(){
		JLabel image;
		JPanel panel;
		image = new JLabel(new ImageIcon(IMAGE_RETURN));
		image.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				getControleur().getModele().retireObservateur(getView());
				fenetre.switchPanel(new VueAccueil(fenetre, new AccueilControleur(Modele.getInstance())));
			}
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
					@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
					@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
					@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub	
			}
		});
		panel =  (JPanel) matrice.getComponent(getControleur().taillePlateau() - 1);
		panel.add(image);
	}
	
	public void mouseDragged(MouseEvent me) {
		if(SwingUtilities.isLeftMouseButton(me)) {
			if (formeCourante == null)
				return;
			formeCourante.setLocation(me.getX() + ajustementX, me.getY() + ajustementY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			formeCourante = null;
			Component c = matrice.findComponentAt(e.getX(), e.getY());
			caseFormeCourante =  c.getParent();
			if (c instanceof JPanel)
				return;
			Point emplacementParent = c.getParent().getLocation();
			((PlateauControleur)getControleur()).setCoordObjetCourant(emplacementParent);
			if(emplacementParent.getY() >= (matrice.getComponent((getControleur().getNbCouleur()+1)*(getControleur().getNbForme()+1))).getLocation().getY()) {

				ajustementX = emplacementParent.x - e.getX();
				ajustementY = emplacementParent.y - e.getY();
				formeCourante = (JLabel) c;
				formeCourante.setLocation(e.getX() + ajustementX, e.getY() + ajustementY);
				formeCourante.setSize(formeCourante.getWidth(), formeCourante.getHeight());
				layeredPane.add(formeCourante, JLayeredPane.DRAG_LAYER);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			if (formeCourante == null)
				return;
			formeCourante.setVisible(false);
			Component c = matrice.findComponentAt(e.getX(), e.getY());
			Container parent = (Container) c;
			if (c instanceof JLabel || c == null || parent.getComponentCount() > 0 || !((PlateauControleur)getControleur()).positionCorrecte(new Point(e.getX(), e.getY()))) {
				caseFormeCourante.add(formeCourante);
			} else {
				parent.add(formeCourante);
			}
			formeCourante.setVisible(true);
			((PlateauControleur) getControleur()).estFini(matrice);
		}
	}

	public void actualise(int sig) {
		if (sig == SIG_PARTIE_FINIE) {
			getControleur().getModele().retireObservateur(this);
			fenetre.switchPanel(new VueFinPartie(fenetre));
		}
		
		refreshVue();
	}
}
