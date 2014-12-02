package piixcolor.controleur;

import java.awt.Point;

import piixcolor.modele.Modele;

import com.sun.xml.internal.ws.api.server.Container;

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
	
	public void actualise() {
		// TODO Auto-generated method stub
		
	}
}
