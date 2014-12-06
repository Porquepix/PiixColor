package piixcolor.vue;
import javax.swing.JPanel;

import piixcolor.controleur.Controleur;
import piixcolor.utilitaire.Observateur;


public abstract class Vue extends JPanel implements Observateur {
	
	protected Fenetre fenetre;
	protected Controleur controleur;
	
	public Vue(Fenetre fenetre, Controleur controleur) {
		this.fenetre = fenetre;
		this.controleur = controleur;
		setSize(fenetre.getWidth(), fenetre.getHeight());
	}
	
	public Controleur getControleur() {
		return this.controleur;
	}
	
	public Vue getView() {
		return this;
	}

}
