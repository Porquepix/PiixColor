package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import piixcolor.controleur.Controleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;

public class VuePlateau extends Vue implements MouseListener, MouseMotionListener {
	private static final String IMAGE_RETURN = Modele.DOSSIER_ASSETS + "returnArrow.gif";
	
	JLayeredPane layeredPane;
	JPanel matrice;
	JLabel formeCourante;
	Container caseFormeCourante;
	int ajustementX;
	int ajustementY;
	Dimension dimension;

	
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
		
		setLayout(new BorderLayout());
		
		// Utilisation du JLayeredPane
		Dimension dimensionVue = new Dimension(fenetre.getWidth(), fenetre.getHeight());
		layeredPane = new JLayeredPane();
		this.add(layeredPane);
		layeredPane.setPreferredSize(dimensionVue);
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);
		
		// Ajout de la matrice
		matrice = new JPanel();
		layeredPane.add(matrice, JLayeredPane.DEFAULT_LAYER);
		matrice.setPreferredSize(dimensionVue);
		matrice.setLayout(new GridLayout((controleur.getNbCouleur()*2) + 1, controleur.getNbForme() + 1));
		matrice.setBounds(0, 0, dimensionVue.width, dimensionVue.height);
		
		for (int i = 0; i < ((controleur.getNbCouleur()*2) + 1) * (controleur.getNbForme() + 1); i++) {
			JPanel square = new JPanel(new BorderLayout());
			matrice.add(square);
			square.setBackground(Color.white);
			if(i < (controleur.getNbCouleur()+1) * (controleur.getNbForme() +1)) {
				square.setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}

		// Création du bouton de retour à l'accueil
		JLabel image;
		JPanel panel;
		image = new JLabel(new ImageIcon(IMAGE_RETURN));
		image.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				fenetre.switchPanel(new VueAccueil(fenetre));
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
		panel =  (JPanel) matrice.getComponent(0);
		panel.add(image);

		initVue();
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
	public VuePlateau(Fenetre f, PlateauControleur c, Dimension dimensionVue) {
		super(f, c);
		setLayout(new BorderLayout());

		// Utilisation du JLayeredPane
		layeredPane = new JLayeredPane();
		this.add(layeredPane);
		layeredPane.setPreferredSize(dimensionVue);
		
		// Ajout de la matrice
		matrice = new JPanel();
		layeredPane.add(matrice, JLayeredPane.DEFAULT_LAYER);
		matrice.setPreferredSize(dimensionVue);
		matrice.setLayout(new GridLayout((controleur.getNbCouleur()*2) + 1, controleur.getNbForme() + 1));
		matrice.setBounds(0, 0, dimensionVue.width, dimensionVue.height);
		
		for (int i = 0; i < c.nbCaseMatrice(); i++) {
			JPanel square = new JPanel(new BorderLayout());
			matrice.add(square);
			square.setBackground(Color.white);
			if(i < (controleur.getNbCouleur()+1) * (controleur.getNbForme() +1)) {
				square.setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}

		initVue();
	}
	/**
	 * Initialisation de la vue plateau :
	 * Trois boucles qui parcourent les panels de la matrice pour y afficher les formes, couleurs et objets colorés correspondant à la configuration en cours
	 */
	public void initVue(){
		JLabel image;
		JPanel panel;
		
		//Ajout Formes :
		for(int i = 1; i <= controleur.getNbForme(); i++){
			image = new JLabel(new ImageIcon(controleur.getModele().getFormesConfig().get(i-1).getAbsolutePath()));
			panel = (JPanel) matrice.getComponent(i);
			panel.add(image);
		}
		
		//Ajout Couleurs :
		int k = 0;
		for(int i = controleur.getNbForme()+1; i <= (controleur.getNbCouleur()*(controleur.getNbForme()+1)); i = i + controleur.getNbForme() + 1){
			image = new JLabel(new ImageIcon());
			panel =  (JPanel) matrice.getComponent(i);
			panel.setBackground(controleur.getModele().getCouleursConfig().get(k).getCouleur());
			panel.add(image);
			k++;
		}
		
		//Ajout des formes colorés à "formes"
		int l = ((controleur.getNbCouleur()+1)*(controleur.getNbForme()+1));
		for(int i = 0; i < controleur.getNbObjetColore(); i++){
			JLabel objetColore = new JLabel(new ImageIcon(controleur.getModele().getReserveForme().get(i).getImage()));
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
		JPanel panel = new JPanel();
		for(int i = 1; i < matrice.getComponentCount(); i++) {
			panel = (JPanel) matrice.getComponent(i);
			panel.setBackground(Color.white);
			panel.removeAll();
		}
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
			//System.out.println(emplacementParent);
			System.out.println(((PlateauControleur)controleur).coordObjet(emplacementParent));
			if(emplacementParent.getY() >= (matrice.getComponent((controleur.getNbCouleur()+1)*(controleur.getNbForme()+1))).getLocation().getY()) {

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
			if (c instanceof JLabel || c == null || parent.getComponentCount() > 0) {
				caseFormeCourante.add(formeCourante);
			} else {
				parent.add(formeCourante);
			}
			formeCourante.setVisible(true);
		}
	}

	public void actualise(List l) {
		refreshVue();
	}

}
