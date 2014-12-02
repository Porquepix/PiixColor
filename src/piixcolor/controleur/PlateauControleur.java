package piixcolor.controleur;

import java.util.List;

import piixcolor.modele.Modele;

public class PlateauControleur extends Controleur{

	public Modele modele;
	
	public PlateauControleur(Modele m){
		super(m);
	}

	public int getNbCouleur(){
		return getModele().getCouleursConfig().size();
	}
	
	public int getNbForme(){
		return getModele().getFormesConfig().size();
	}
	
	public int getNbObjetColore() {
		return getModele().getReserveForme().size();
	}
	
	public void actualise(List l) {
		// TODO Auto-generated method stub
		
	}
}
