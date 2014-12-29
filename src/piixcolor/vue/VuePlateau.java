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
	/**
	 * Chemin vers l'icone du bouton de sortie.
	 */
	private static final String IMAGE_RETURN = Modele.DOSSIER_ASSETS + "exit.png";
	/**
	 * layeredPane, permet le glisser d�poser.
	 */
	JLayeredPane layeredPane;
	/**
	 * La matrice, grille contenant tout le plateau.
	 */
	JPanel matrice;
	/**
	 * Forme que le joueur est entrain de d�placer.
	 */
	JLabel formeCourante;
	/**
	 * Case d'o� provenait la forme courante.
	 * @see VuePlateau#formeCourante
	 */
	Container caseFormeCourante;
	/**
	 * Ajustement horizontal du curseur.
	 */
	int ajustementX;
	/**
	 * Ajustement vertical du curseur.
	 */
	int ajustementY;
	/**
	 * Dimension de la matrice.
	 */
	Dimension dimensionMatrice;

	/**
	 * Constructeur classique d'une vue de plateau de jeu. On cr�e un LayeredPane pour le drag and drop, on cr�e un Panel repr�sentant la matrice que l'on divise en grille de sous pannels carr�s.
	 * On cr�e un bouton pour retourner � l'accueil. Enfin on initialise la vue en appelant initVue().
	 * 
	 * @param f 
	 * 		Fenetre dans laquelle se trouve la vue du plateau
	 * @param c
	 * 		Controleur de la vue du plateau
	 * @see VuePlateau#initVue()
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
		
		// Cr�ation du bouton de retour � l'accueil
		ajoutBoutonRetour();		
	}
	
	/**
	 * Constructeur permettant de cr�er une vue plateau compl�tment statique et d�pourvu de bouton de retour.
	 * Elle est n�anmoins mise � jour automatiquement en fonction des modification du mod�le gr�ce � son controleur. Constructeur servant � la g�n�ration d'aper�us.
	 * 
	 * @param f 
	 * 		Fenetre dans laquelle se trouve la vue du plateau
	 * @param c
	 * 		Controleur de la vue du plateau
	 * @param dimension
	 * 		Dimension voulue pour l'aper�u
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
	 * Cr�ation d'un GridLayout correspondant on nombre de case actuel puis parcours de la matrice pour y ajouter ses composantes et son quadrillage.
	 * Ensuite trois boucles qui parcourent les panels de la matrice pour y afficher les formes, couleurs et objets color�s correspondant � la configuration en cours.
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
		
		//Ajout des formes color�s � "formes"
		int l = ((getControleur().getNbCouleur()+1)*(getControleur().getNbForme()+1));
		for (int i = 0; i < getControleur().getNbObjetColore(); i++) {
			BufferedImage image1 = getControleur().getModele().getReserveForme().get(i).getImage();
			JLabel objetColore = new JLabel(new ImageIcon(getControleur().resizeImage(image1, ((PlateauControleur) getControleur()).getCaseHeight(), ((PlateauControleur) getControleur()).getCaseHeight())));	
			//On emp�che d'�crire au del� du plateau mais aussi sur le bouton de retour donc si on atteint taillePlateau() - 1 on arr�te de dessiner des formes
			if(l >= getControleur().taillePlateau() - 1) {
				break;
			}
			panel =  (JPanel) matrice.getComponent(l);
			l++;
			panel.add(objetColore);
		}
	}
	
	/**
	 * M�thode de nettoyage de la vue plateau :
	 * On retire tous les composants des panels de la matrice et on repeint leurs fonds en blanc.
	 */
	public void clearVue() {
		if (matrice != null)
		layeredPane.remove(matrice);
	}
	
	/**
	 * Rafraichissement de la vue :
	 * On nettoie la vue de tous ses anciens composants et on la r�initialise avec le mod�le mis � jour
	 * @see VuePlateau#clearVue()
	 * @see VuePlateau#initVue()
	 */
	public void refreshVue() {
		clearVue();
		initVue();
	}

	/**
	 * Ajout du bouton de retour lorsque la matrice � cr�er n'est pas un apercu.
	 * Il faut cr�er un label avec un MouseListener pour rep�rer quand l'utilisateur clique sur le bouton. Il faut �galement charger l'image de ce bouton dans les assets.
	 * On place le bouton en bas � droite du plateau dans la derni�re case.
	 */
	public void ajoutBoutonRetour(){
		JLabel image;
		JPanel panel;
		image = new JLabel(new ImageIcon(IMAGE_RETURN));
		image.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				getControleur().getModele().retireObservateur(getView());
				fenetre.switchPanel(new VueAccueil(fenetre, new AccueilControleur(Modele.getInstance())));
			}
			public void mouseEntered(MouseEvent e) {}
				
			public void mouseExited(MouseEvent e) {}

			public void mousePressed(MouseEvent e) {}

			public void mouseReleased(MouseEvent e) {}
		});
		panel =  (JPanel) matrice.getComponent(getControleur().taillePlateau() - 1);
		panel.add(image);
	}
	
	/**
	 * M�thode pour r�cup�rer les �v�nements concernant un d�placement de la souris lorsque le bouton de celle-ci est press�.
	 * Il faut s'assurer que cela ne marche qu'avec le bouton gauche de la souris.
	 * On test si la vue a une forme courante et si oui on peut la d�placer en fonction des mouvements de la souris en prenant compte de l'ajustement vertical et horizontal.
	 */
	public void mouseDragged(MouseEvent me) {
		if(SwingUtilities.isLeftMouseButton(me)) {
			if (formeCourante == null)
				return;
			formeCourante.setLocation(me.getX() + ajustementX, me.getY() + ajustementY);
		}
	}

	public void mouseMoved(MouseEvent arg0) {}

	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent me) {}
	
	public void mouseExited(MouseEvent arg0) {}


	/**
	 * M�thode pour r�cup�rer un �v�nement concernant la pression des boutons de la souris.
	 * Il faut s'assurer que cela ne marche qu'avec le bouton gauche de la souris.
	 * On observe le composant de la matrice sur lequel l'utilisateur � cliqu�.
	 * On s'assure qu'il s'agit bien d'une forme color� (divers test sur la position et la contenance des cases de la matrice) et si c'est bien le cas on peut donner une forme courante � la vue et passer cette forme dans la couche draggable.
	 * De plus il faut g�rer l'affichage de cette forme courante lorsqu'on clique dessus : il faut prendre en compte l'ajustement vertical et horizontal.
	 */
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

	/**
	 * M�thode pour r�cup�rer un �v�nement concernant le relachement d'un bouton de la souris.
	 * Il faut s'assurer que cela ne marche qu'avec le bouton gauche de la souris.
	 * On effectue divers test pour v�rifier que l'utilisateur relache bien la souris sur la matrice et qu'il existe bien une forme courante dragg�e.
	 * Apr�s cela on peut tester si l'emplacement est correct pour l'objet color� et enfin tester si la partie est finie � l'aide de divers m�thode du PlateauControleur
	 * @see PlateauControleur#positionCorrecte(Point)
	 * @see PlateauControleur#estFini(JPanel)
	 */
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

	/**
	 * M�thode d'actualisation appel�e quand le mod�le est mis � jour et qu'il notifie ses observateurs.
	 * Dans la vue Plateau un seul signal nous interesse : si la partie est finie il faut switcher vers la vue de fin de partie.
	 * Si un autre signal est envoy� on se contentera de rafraichir la vue.
	 */
	public void actualise(int sig) {
		if (sig == SIG_PARTIE_FINIE) {
			getControleur().getModele().retireObservateur(this);
			layeredPane.add(new VueFinPartie(fenetre), 0);
			layeredPane.setOpaque(false);
			layeredPane.removeMouseListener(this);
			layeredPane.removeMouseMotionListener(this);
		} else {			
			refreshVue();
		}
	}
}
