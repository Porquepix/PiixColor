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
	 * Coordon�es de l'objet courant sur la fen�tre.
	 */
	private Point coordObjetCourant = null;
	
	/**
	 * Liste des formes color�s possiblement correctes � placer dans le tableau. Si toutes les formes possiblement pla�ables de la r�serve de forme sont plac�es le jeu est termin�.
	 */
	private List<ObjetColore> possibilities;
	
	/**
	 * Constructeur du controleur du plateau. Il appelle celui de la classe Controleur et initialise la liste des possibilit�s de r�ponses correctes � partir de la configuration du plateau.
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
	 * A l'aide des coordonn�es physique d'un objet s�lectionn�(en pixels par rapport au coin gauche de la fen�tre), on d�termine les coordonn�es dans la matrice d'un objet
	 * 
	 * @param coordPhys Coordonn�es physique d'un objet, elle d�pand de la taille de la fen�tre et donc de la r�solution de l'�cran de l'utilisateur
	 * @return Coordonn�es dans la matrice de l'objet Ex : la case (0,1) repr�sente la case de la premi�re couleur.
	 */
	public Point coordMatrice(Point coordPhys) {
		int x = (int)coordPhys.getX() / getCaseWidth();
		int y = (int)coordPhys.getY() / getCaseHeight();
		Point p = new Point(x, y);
		return p;
	}
	
	/**
	 * M�thode permettant de convertir la position de l'objet courant dans la r�serve de forme en l'indice correspondant � cette m�me forme dans la liste d'objet color� du Modele.
	 * On appelle la m�thode coordoToIndice avec les coordonn�es de l'objet courant.
	 * Ex: Le premier objet de la r�serve de formes color�es correspond � l'indice 0 de la listes de ces formes dans le programme.
	 * 
	 * @return l'indice correspondant � la forme color� courante
	 * @see PlateauControleur#coordToIndice(Point)
	 */
	public int coordObjetCourantToIndice() {
			return coordToIndice(coordObjetCourant);
	}
	
	/**
	 * M�thode permettant de convertir la position d'un objet dans la r�serve de forme en l'indice correspondant � cette m�me forme dans la liste d'objet color� du Modele.
	 * On utilise cette m�thode dans coordObjetCourantToIndice() et dans positionCorrect()
	 * Ex: Le premier objet de la r�serve de formes color�es correspond � l'indice 0 de la listes de ces formes dans le programme.
	 * @param coord La coordonn�e � convertir en indice
	 * @return l'indice dans la r�serve de forme correspondant � la coordonn�e en param�tre
	 */
	public int coordToIndice(Point coord) {
		//Premier calcul du num�ro de la ligne sur laquelle se trouve l'objet courant
		int nbLigne = (int) (coord.getY() - getNbCouleur());
		return (int) coord.getX() + ((getNbForme() + 1) * (nbLigne - 1));
	}
	
	/**
	 * M�thode permettant de v�rifier si le positionnement d'une forme color� sur la matrice est correct ou pas. On appelle la m�thode coordMatrice() pour obtenir une coordonn�e compr�hensible sur la matrice.
	 * On doit aussi d�terminer de quel objet il est question et pour cela on doit savoir l'indice de l'objet courant dans la liste du modele. Pour cela on appelle coordObjetCourantToIndice(). On a �gallement besoin de la m�thode equals (red�finie pour les ObjetColore) pour comparer la forme lach�e avec l'objet attendu.
	 * 
	 * @param coordPhys coordonn�e physique de la position sur laquelle la forme est lach�e
	 * @return un bool�en : vrai si la forme et bien plac�e, faux sinon.
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
 * Test si la partie en cours est finie ou pas. On appelera cette m�thode � chaque formes d�pos�es sur la matrice. Dans le cas ou la partie est termin�e il faudra notifier le programme avec un signal de fin de partie.
 * On parcourt toute la r�serve de forme en testant si la forme d�pos�e fait partie des possibilit�s
 * 
 * @param matrice La matrice du plateau
 */
	public void estFini(JPanel matrice) {
		boolean fini = true;
		for (int i =  (getNbCouleur() + 1) * (getNbForme() + 1); i < taillePlateau() - 1; i++) {
			Component c = matrice.getComponent(i);		
			if (((Container) c).getComponentCount() == 0) {
				continue; //Si il n'y a aucune forme on passe l'�tape
			}
			
			//On recupere l'index de l'objet dans la liste � partir de ses coordonn�es
			Point coord = coordMatrice(c.getLocation());
			int index =  coordToIndice(coord);
			if (index >= getNbObjetColore()) {
				break;
			}
			
			//On regarde si l'objet fait partie des possibilit�s
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
	 * Accesseur de la coordonn�e de l'objet courant
	 * 
	 * @return {@link PlateauControleur#coordObjetCourant}
	 */
	public Point getCoordObjetCourant() {
		return coordObjetCourant;
	}

	/**
	 * Mutateur de la coordonn�e de l'objet courant
	 * @param coordPhys coordonn�e physique nouvellement assign�e � l'objet courant
	 */
	public void setCoordObjetCourant(Point coordPhys) {
		this.coordObjetCourant = coordMatrice(coordPhys);
	}
	
	/**
	 * D�termine la taille de la hauteur d'une case en pixels � partir de la d�finition de l'�cran de l'utilisateur.
	 * @return hauteur d'une case en pixel
	 */
	public int getCaseHeight() {
		return (Fenetre.FRAME_HEIGHT / (getNbCouleur() * 2 + 1));
	}
	
	/**
	 * D�termine la taille de la longueur d'une case en pixels � partir de la d�finition de l'�cran de l'utilisateur.
	 * @return longueur d'une case en pixels
	 */
	public int getCaseWidth() {
		return (Fenetre.FRAME_WIDTH / (getNbForme() + 1));
	}
}
