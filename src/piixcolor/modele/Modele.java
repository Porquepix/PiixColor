package piixcolor.modele;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import piixcolor.utilitaire.Observateur;

public abstract class Modele {
	private ArrayList<Observateur> observateurs = new ArrayList<Observateur>();
	private ArrayList<Color> couleursConfig;
	private ArrayList<BufferedImage> formesConfig;

	// TODO Implémenter ObjetsColores Matthieu !
	// private LinkedList<ObjetColore> reserveForme;

	public void ajoutObservateur(Observateur ob) {
		observateurs.add(ob);
	}

	public void retireObservateur(Observateur ob) {
		observateurs.remove(ob);
	}
	
	public void notifier() {
		for (Observateur ob : observateurs) {
			ob.actualise();
		}
	}

	public ArrayList<Color> getCouleursConfig() {
		return couleursConfig;
	}

	public void setCouleursConfig(ArrayList<Color> couleursConfig) {
		this.couleursConfig = couleursConfig;
		notifier();
	}

	public ArrayList<BufferedImage> getFormesConfig() {
		return formesConfig;
	}

	public void setFormesConfig(ArrayList<BufferedImage> formesConfig) {
		this.formesConfig = formesConfig;
		notifier();
	}
	
	
}
