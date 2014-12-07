package piixcolor.controleur;

import java.awt.Point;
import java.util.List;

import piixcolor.modele.Modele;
import piixcolor.vue.Fenetre;

public class PlateauControleur extends Controleur{

	public Modele modele;
	
	public PlateauControleur(Modele m){
		super(m);
	}
		
	public int nbCaseMatrice() {
		return ((getNbCouleur()*2) + 1) * (getNbForme() + 1);
	}
	
	/**
	 * A l'aide des coordonnées physique d'un objet sélectionné, on détermine les coordonnée dans la matrice d'un objet
	 * 
	 * @param coordPhys
	 * 			Coordonnées physique d'un objet, elle dépand de la taille de la fenêtre et donc de la résolution de l'écran de l'utilisateur
	 * @return
	 * 			Coordonnées dans la matrice de l'objet Ex : la case (1,0) représente la case de la première forme incolore dans la première ligne de la matrice.
	 */
	public Point coordObjet(Point coordPhys) {
		int x = (int)coordPhys.getX() / (Fenetre.FRAME_WIDTH / (getNbForme() + 1));
		int y = (int)coordPhys.getY() / (Fenetre.FRAME_HEIGHT / (getNbCouleur() * 2 + 1));
		Point p = new Point(x, y);
		return p;
	}
	
	public void actualise(List l) {
		// TODO Auto-generated method stub
		
	}
}
