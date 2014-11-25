package piixcolor.controleur;

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
	
	public void actualise() {
		// TODO Auto-generated method stub
		
	}
	
	
}
