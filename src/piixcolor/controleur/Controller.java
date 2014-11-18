package piixcolor.controleur;

import piixcolor.modele.Model;

public abstract class Controller {
	
	private Model model;
	
	public Controller(Model m) {
		this.model = m;
	}
	
	public Model getModel () {
		return this.model;
	}

}
