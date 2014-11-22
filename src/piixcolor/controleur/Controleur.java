package piixcolor.controleur;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.Observateur;


public abstract class Controleur implements Observateur {
	
	private Modele modele;
	
	public Controleur(Modele m) {
		this.modele = m;
	}
	
	public Modele getModel () {
		return this.modele;
	}

}
