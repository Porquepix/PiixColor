package piixcolor.controleur;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.ObjetColore;
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
	
	public void addObjetColore(ObjetColore image) {
		List<ObjetColore> obj = new ArrayList<ObjetColore>(getModele().getReserveForme());
		obj.add(0, image);
		getModele().setReserveForme(obj);
	}

	public void removeObjetColore(ObjetColore image) {
		List<ObjetColore> obj = new ArrayList<ObjetColore>(getModele().getReserveForme());
		obj.remove(image);
		getModele().setReserveForme(obj);
	}

}
