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
		Point p = new Point((int)coordPhys.getX()/((Fenetre.getInstance().getWidth())/(getNbForme() + 1)), (int)coordPhys.getY()/((Fenetre.getInstance().getHeight()-2)/(getNbCouleur() + 1)));
		return p;
	}
	
	public void actualise(List l) {
		// TODO Auto-generated method stub
		
	}
}
