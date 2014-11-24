package piixcolor.vue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import piixcolor.controleur.Controleur;
import piixcolor.modele.Modele;
import piixcolor.utilitaire.Observateur;


public abstract class Vue extends JPanel implements Observateur {
	
	protected Fenetre fenetre;
	private Controleur controleur;
	private Modele modele;
	
	public Vue(Fenetre fenetre, Controleur controleur, Modele modele) {
		this.fenetre = fenetre;
		this.controleur = controleur;
		this.modele = modele;
		setSize(fenetre.getWidth(), fenetre.getHeight());
	}
	
	public Controleur getControleur() {
		return this.controleur;
	}

}
