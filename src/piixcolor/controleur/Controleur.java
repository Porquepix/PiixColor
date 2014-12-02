package piixcolor.controleur;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.Observateur;


public abstract class Controleur implements Observateur {
	
	private Modele modele;
	
	public Controleur(Modele m) {
		this.modele = m;
	}
	
	public Modele getModele () {
		return this.modele;
	}

	public int getNbForme(){
		return getModele().getFormesConfig().size();
	}
	
	public int getNbObjetColore() {
		return getModele().getReserveForme().size();
	}
	
	public int getNbCouleur(){
		return getModele().getCouleursConfig().size();
	}

}
