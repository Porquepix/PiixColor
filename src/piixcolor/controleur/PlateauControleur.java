package piixcolor.controleur;

import java.awt.Point;
import java.util.List;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.ObjetColore;
import piixcolor.vue.Fenetre;

public class PlateauControleur extends Controleur{

	private Point coordObjetCourant = null;
	
	public PlateauControleur(Modele m){
		super(m);
	}
	
	/**
	 * A l'aide des coordonn�es physique d'un objet s�lectionn�, on d�termine les coordonn�e dans la matrice d'un objet
	 * 
	 * @param coordPhys
	 * 			Coordonn�es physique d'un objet, elle d�pand de la taille de la fen�tre et donc de la r�solution de l'�cran de l'utilisateur
	 * @return
	 * 			Coordonn�es dans la matrice de l'objet Ex : la case (0,1) repr�sente la case de la premi�re couleur.
	 */
	public Point coordMatrice(Point coordPhys) {
		int x = (int)coordPhys.getX() / (Fenetre.FRAME_WIDTH / (getNbForme() + 1));
		int y = (int)coordPhys.getY() / (Fenetre.FRAME_HEIGHT / (getNbCouleur() * 2 + 1));
		Point p = new Point(x, y);
		return p;
	}
	
	public int coordObjetCourantToIndice() {
			int nbLigne = (int) (coordObjetCourant.getY() - getNbCouleur());
			return (int) coordObjetCourant.getX() + ((getNbForme() + 1) * (nbLigne - 1));
	}
	
	public boolean positionCorrecte (Point coordPhys) {
		Point coord = coordMatrice(coordPhys);
		if (coord.getY() > getNbForme()){
			return false;
		} else {
			ObjetColore objetCorrect = new ObjetColore(getModele().getCouleursConfig().get((int)coord.getY()-1), getModele().getFormesConfig().get((int)coord.getX()-1));
			ObjetColore objetCourant = getModele().getReserveForme().get(coordObjetCourantToIndice());
			return objetCourant.equals(objetCorrect);
		}
	}
	
	public void actualise(List l) {
		// TODO Auto-generated method stub
		
	}

	public Point getCoordObjetCourant() {
		return coordObjetCourant;
	}

	public void setCoordObjetCourant(Point coordPhys) {
		this.coordObjetCourant = coordMatrice(coordPhys);
	}
}
