package piixcolor.controleur;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.ObjetColore;
import piixcolor.vue.Fenetre;

public class PlateauControleur extends Controleur{

	private Point coordObjetCourant = null;
	private List<ObjetColore> possibilities;
	
	public PlateauControleur(Modele m){
		super(m);
		
		possibilities = new ArrayList<ObjetColore>();
		for (int i =0; i < getNbCouleur(); i++) {
			for (int j = 0; j < getNbForme(); j++) {
				possibilities.add(new ObjetColore(getModele().getCouleursConfig().get(i), getModele().getFormesConfig().get(j)));
			}
		}
	}
	
	/**
	 * A l'aide des coordonnées physique d'un objet sélectionné, on détermine les coordonnée dans la matrice d'un objet
	 * 
	 * @param coordPhys
	 * 			Coordonnées physique d'un objet, elle dépand de la taille de la fenêtre et donc de la résolution de l'écran de l'utilisateur
	 * @return
	 * 			Coordonnées dans la matrice de l'objet Ex : la case (0,1) représente la case de la première couleur.
	 */
	public Point coordMatrice(Point coordPhys) {
		int x = (int)coordPhys.getX() / (Fenetre.FRAME_WIDTH / (getNbForme() + 1));
		int y = (int)coordPhys.getY() / getCaseHeight();
		Point p = new Point(x, y);
		return p;
	}
	
	public int coordObjetCourantToIndice() {
			int nbLigne = (int) (coordObjetCourant.getY() - getNbCouleur());
			return (int) coordObjetCourant.getX() + ((getNbForme() + 1) * (nbLigne - 1));
	}
	
	public boolean positionCorrecte (Point coordPhys) {
		Point coord = coordMatrice(coordPhys);
		if (coord.getY() > getNbCouleur()){
			return false;
		} else {
			ObjetColore objetCorrect = new ObjetColore(getModele().getCouleursConfig().get((int)coord.getY()-1), getModele().getFormesConfig().get((int)coord.getX()-1));
			ObjetColore objetCourant = getModele().getReserveForme().get(coordObjetCourantToIndice());
			return objetCourant.equals(objetCorrect);
		}
	}
	

	public void estFini(JPanel matrice) {
		boolean fini = true;
		for (int i =  (getNbCouleur() + 1) * (getNbForme() + 1); i < taillePlateau() - 1; i++) {
			Component c = matrice.getComponent(i);		
			if (((Container) c).getComponentCount() == 0) {
				continue; //Si il n'y a aucune forme on passe l'�tape
			}
			
			//On recupere l'index de l'objet
			Point coord = coordMatrice(c.getLocation());
			int nbLigne = (int) (coord.getY() - getNbCouleur());
			int index =  (int) coord.getX() + ((getNbForme() + 1) * (nbLigne - 1));
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
			getModele().notifier(SIG_PARTIE_FINIE);
		}
	}
	
	public void actualise(int sig) {
		// TODO Auto-generated method stub
		
	}

	public Point getCoordObjetCourant() {
		return coordObjetCourant;
	}

	public void setCoordObjetCourant(Point coordPhys) {
		this.coordObjetCourant = coordMatrice(coordPhys);
	}
	
	public int getCaseHeight() {
		return (Fenetre.FRAME_HEIGHT / (getNbCouleur() * 2 + 1));
	}
}
