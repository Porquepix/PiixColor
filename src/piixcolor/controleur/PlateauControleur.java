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
	 * A l'aide des coordonn�es physique d'un objet s�lectionn�, on d�termine les coordonn�e dans la matrice d'un objet
	 * 
	 * @param coordPhys
	 * 			Coordonn�es physique d'un objet, elle d�pand de la taille de la fen�tre et donc de la r�solution de l'�cran de l'utilisateur
	 * @return
	 * 			Coordonn�es dans la matrice de l'objet Ex : la case (1,0) repr�sente la case de la premi�re forme incolore dans la premi�re ligne de la matrice.
	 */
	public Point coordObjet(Point coordPhys) {
		Point p = new Point((int)coordPhys.getX()/((Fenetre.getInstance().getWidth())/(getNbForme() + 1)), (int)coordPhys.getY()/((Fenetre.getInstance().getHeight()-2)/(getNbCouleur() + 1)));
		return p;
	}
	
	public void actualise(List l) {
		// TODO Auto-generated method stub
		
	}
}
