package piixcolor.vue;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import piixcolor.modele.Modele;

public class BoiteDialogue {
	
	public static void enregistrerConfig(String path) {
		try {
			Modele.getInstance().saveCouleurConfig();
			Modele.getInstance().saveFormeConfig();
			Modele.getInstance().enregistrer(path);
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration a bien �t� sauvgard�e.", "Information", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration n'a pas pu �tre sauvgard�e car le fichier config.xml est introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration n'a pas pu �tre sauvgard�e car une erreur lors de l'�criture du fichier config.xml est intervenue.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public static int createOptionBox(int type, String titre, String message, String[] options, int optionParDefaut) {
		int retour = JOptionPane.showOptionDialog(Fenetre.getInstance(), message, titre, type, JOptionPane.QUESTION_MESSAGE, null, options, options[optionParDefaut]); 
		return retour;
	}
	
	public static void createModalBox(int type, String titre, String message) {
		JOptionPane.showMessageDialog(Fenetre.getInstance(), message, titre, type);
	}

}
