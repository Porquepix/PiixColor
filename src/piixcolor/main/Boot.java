package piixcolor.main;

import java.io.File;

import javax.swing.JOptionPane;

import piixcolor.controleur.AccueilControleur;
import piixcolor.modele.Modele;
import piixcolor.vue.BoiteDialogue;
import piixcolor.vue.Fenetre;
import piixcolor.vue.VueAccueil;

public class Boot {

	public static void main(String[] args) {
		checkSetup(); //Verifie que tout est OK avant de lancer le programme
		
		Fenetre f = Fenetre.getInstance();
		AccueilControleur c = new AccueilControleur(Modele.getInstance());
		f.switchPanel(new VueAccueil(f, c));	
	}

	/**
	 * Fonction verifiants que tous les fichiers necessaires au fonctionnement de l'application sont prrésent.
	 * Si ce n'est pas le cas affiche une boîte de dialogue et ferme l'application.
	 */
	private static void checkSetup() {
		File f;
		
		f = new File(Modele.DOSSIER_FORMES);
		if (!f.exists()) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Élément introuvable", "Le dossier 'images' (" + f.getAbsolutePath() + "), requis pour le fonctionnement de l'application, est introuvable.");
			System.exit(-1);
		}
		
		f = new File(Modele.DOSSIER_ASSETS);
		if (!f.exists()) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Élément introuvable", "Le dossier 'assets' (" + f.getAbsolutePath() + "), requis pour le fonctionnement de l'application, est introuvable.");
			System.exit(-1);
		}
		
		f = new File(Modele.FICHIER_CONFIG);
		if (!f.exists()) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Élément introuvable", "Le fichier 'config.xml' (" + f.getAbsolutePath() + "), requis pour le fonctionnement de l'application, est introuvable.");
			System.exit(-1);
		}
	}

}
