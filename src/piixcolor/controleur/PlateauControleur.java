package piixcolor.controleur;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.ObjetColore;
import piixcolor.utilitaire.Observateur;
import piixcolor.vue.Fenetre;

public class PlateauControleur extends Controleur{

	/**
	 * Coordonées de l'objet courant sur la fenêtre.
	 */
	private Point coordObjetCourant = null;
	
	/**
	 * Liste des formes colorés possiblement correctes à placer dans le tableau. Si toutes les formes possiblement plaçables de la réserve de forme sont placées le jeu est terminé.
	 */
	private List<ObjetColore> possibilities;
	
	/**
	 * Constructeur du controleur du plateau. Il appelle celui de la classe Controleur et initialise la liste des possibilités de réponses correctes à partir de la configuration du plateau.
	 * 
	 * @param m modele de l'application
	 * 
	 * @see Controleur#Controleur(Modele)
	 * @see Controleur#getNbCouleur()
	 * @see Controleur#getNbForme()
	 * @see ObjetColore#ObjetColore(piixcolor.utilitaire.Couleur, java.io.File)
	 */
	public PlateauControleur(Modele m){
		super(m);
		
		possibilities = new ArrayList<ObjetColore>();
		for (int i = 0; i < getNbCouleur(); i++) {
			for (int j = 0; j < getNbForme(); j++) {
				possibilities.add(new ObjetColore(getModele().getCouleursConfig().get(i), getModele().getFormesConfig().get(j)));
			}
		}
	}
	
	/**
	 * A l'aide des coordonnées physique d'un objet sélectionné(en pixels par rapport au coin gauche de la fenêtre), on détermine les coordonnées dans la matrice d'un objet
	 * 
	 * @param coordPhys Coordonnées physique d'un objet, elle dépand de la taille de la fenêtre et donc de la résolution de l'écran de l'utilisateur
	 * @return Coordonnées dans la matrice de l'objet Ex : la case (0,1) représente la case de la première couleur.
	 */
	public Point coordMatrice(Point coordPhys) {
		int x = (int)coordPhys.getX() / getCaseWidth();
		int y = (int)coordPhys.getY() / getCaseHeight();
		Point p = new Point(x, y);
		return p;
	}
	
	/**
	 * Méthode permettant de convertir la position de l'objet courant dans la réserve de forme en l'indice correspondant à cette même forme dans la liste d'objet coloré du Modele.
	 * On appelle la méthode coordoToIndice avec les coordonnées de l'objet courant.
	 * Ex: Le premier objet de la réserve de formes colorées correspond à l'indice 0 de la listes de ces formes dans le programme.
	 * 
	 * @return l'indice correspondant à la forme coloré courante
	 * @see PlateauControleur#coordToIndice(Point)
	 */
	public int coordObjetCourantToIndice() {
			return coordToIndice(coordObjetCourant);
	}
	
	/**
	 * Méthode permettant de convertir la position d'un objet dans la réserve de forme en l'indice correspondant à cette même forme dans la liste d'objet coloré du Modele.
	 * On utilise cette méthode dans coordObjetCourantToIndice() et dans positionCorrect()
	 * Ex: Le premier objet de la réserve de formes colorées correspond à l'indice 0 de la listes de ces formes dans le programme.
	 * @param coord La coordonnée à convertir en indice
	 * @return l'indice dans la réserve de forme correspondant à la coordonnée en paramètre
	 */
	public int coordToIndice(Point coord) {
		//Premier calcul du numéro de la ligne sur laquelle se trouve l'objet courant
		int nbLigne = (int) (coord.getY() - getNbCouleur());
		return (int) coord.getX() + ((getNbForme() + 1) * (nbLigne - 1));
	}
	
	/**
	 * Méthode permettant de vérifier si le positionnement d'une forme coloré sur la matrice est correct ou pas. On appelle la méthode coordMatrice() pour obtenir une coordonnée compréhensible sur la matrice.
	 * On doit aussi déterminer de quel objet il est question et pour cela on doit savoir l'indice de l'objet courant dans la liste du modele. Pour cela on appelle coordObjetCourantToIndice(). On a égallement besoin de la méthode equals (redéfinie pour les ObjetColore) pour comparer la forme lachée avec l'objet attendu.
	 * 
	 * @param coordPhys coordonnée physique de la position sur laquelle la forme est lachée
	 * @return un booléen : vrai si la forme et bien placée, faux sinon.
	 * @see PlateauControleur#coordMatrice(Point)
	 * @see PlateauControleur#coordObjetCourantToIndice()
	 * @see Controleur#getNbCouleur()
	 * @see Controleur#getModele()
	 * @see ObjetColore#equals(Object)
	 */
	public boolean positionCorrecte (Point coordPhys) {
		Point coordMatricielle = coordMatrice(coordPhys);
		if (coordMatricielle.getY() > getNbCouleur()){
			return false;
		} else {
			ObjetColore objetCorrect = new ObjetColore(getModele().getCouleursConfig().get((int)coordMatricielle.getY()-1), getModele().getFormesConfig().get((int)coordMatricielle.getX()-1));
			ObjetColore objetCourant = getModele().getReserveForme().get(coordObjetCourantToIndice());
			return objetCourant.equals(objetCorrect);
		}
	}
	
/**
 * Test si la partie en cours est finie ou pas. On appelera cette méthode à chaque formes déposées sur la matrice. Dans le cas ou la partie est terminée il faudra notifier le programme avec un signal de fin de partie.
 * On parcourt toute la réserve de forme en testant si la forme déposée fait partie des possibilités
 * 
 * @param matrice La matrice du plateau
 */
	public void estFini(JPanel matrice) {
		boolean fini = true;
		for (int i =  (getNbCouleur() + 1) * (getNbForme() + 1); i < taillePlateau() - 1; i++) {
			Component c = matrice.getComponent(i);		
			if (((Container) c).getComponentCount() == 0) {
				continue; //Si il n'y a aucune forme on passe l'étape
			}
			
			//On recupere l'index de l'objet dans la liste à partir de ses coordonnées
			Point coord = coordMatrice(c.getLocation());
			int index =  coordToIndice(coord);
			if (index >= getNbObjetColore()) {
				break;
			}
			
			//On regarde si l'objet fait partie des possibilités
			ObjetColore obj = getModele().getReserveForme().get(index);
			if (possibilities.contains(obj)) {
				fini = false;
				break;
			}
		}
		if (fini) {
			getModele().notifier(Observateur.SIG_PARTIE_FINIE);
		}
	}

	/**
	 * Accesseur de la coordonnée de l'objet courant
	 * 
	 * @return {@link PlateauControleur#coordObjetCourant}
	 */
	public Point getCoordObjetCourant() {
		return coordObjetCourant;
	}

	/**
	 * Mutateur de la coordonnée de l'objet courant
	 * @param coordPhys coordonnée physique nouvellement assignée à l'objet courant
	 */
	public void setCoordObjetCourant(Point coordPhys) {
		this.coordObjetCourant = coordMatrice(coordPhys);
	}
	
	/**
	 * Détermine la taille de la hauteur d'une case en pixels à partir de la définition de l'écran de l'utilisateur.
	 * @return hauteur d'une case en pixel
	 */
	public int getCaseHeight() {
		return (Fenetre.FRAME_HEIGHT / (getNbCouleur() * 2 + 1));
	}
	
	/**
	 * Détermine la taille de la longueur d'une case en pixels à partir de la définition de l'écran de l'utilisateur.
	 * @return longueur d'une case en pixels
	 */
	public int getCaseWidth() {
		return (Fenetre.FRAME_WIDTH / (getNbForme() + 1));
	}
}
