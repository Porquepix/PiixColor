package piixcolor.main;

import java.io.File;

import javax.swing.JOptionPane;

import piixcolor.controleur.AccueilController;
import piixcolor.controleur.Controleur;
import piixcolor.modele.Modele;
import piixcolor.vue.BoiteDialogue;
import piixcolor.vue.Fenetre;
import piixcolor.vue.VueAccueil;

public class Boot {

	public static void main(String[] args) {
		checkSetup(); //Verifie que tout est OK avant de lancer le programme
		
		Fenetre f = Fenetre.getInstance();
		Controleur c = new AccueilController(Modele.getInstance());
		f.switchPanel(new VueAccueil(f, c));	
	}

	private static void checkSetup() {
		File f;
		
		f = new File(Modele.DOSSIER_FORMES);
		if (!f.exists()) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "�l�ment introuvable", "Le dossier 'images' (" + f.getAbsolutePath() + "), requis pour le fonctionnement de l'application, est introuvable.");
			System.exit(-1);
		}
		
		f = new File(Modele.DOSSIER_ASSETS);
		if (!f.exists()) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "�l�ment introuvable", "Le dossier 'assets' (" + f.getAbsolutePath() + "), requis pour le fonctionnement de l'application, est introuvable.");
			System.exit(-1);
		}
		
		f = new File(Modele.FICHIER_CONFIG);
		if (!f.exists()) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "�l�ment introuvable", "Le fichier 'config.xml' (" + f.getAbsolutePath() + "), requis pour le fonctionnement de l'application, est introuvable.");
			System.exit(-1);
		}
	}

}
