package piixcolor.controleur;

import piixcolor.modele.Model;

public abstract class Controleur {
	
	private Model model;
	
	public Controleur(Model m) {
		this.model = m;
	}
	
	public Model getModel () {
		return this.model;
	}

}
