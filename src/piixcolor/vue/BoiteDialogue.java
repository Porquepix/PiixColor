package piixcolor.vue;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import piixcolor.modele.Modele;

public class BoiteDialogue {
	
	/**
	 * Gere avec des boites de dialogue l'enregistrement de la configuration actuelle dans le fichier passer en param�tre.
	 * 
	 * @param path Fichier ou la configuration sera enregistr�e
	 */
	public static void enregistrerConfig(String path) {
		try {
			Modele.getInstance().enregistrer(path);
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration a bien �t� sauvgard�e.", "Information", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration n'a pas pu �tre sauvgard�e car le fichier config.xml est introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration n'a pas pu �tre sauvgard�e car une erreur lors de l'�criture du fichier config.xml est intervenue.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Cr�er un boite de dialogue avec plusieurs choix possible. Cette boite est attach�e � la fenetre.
	 * 
	 * @param type Type de la boite de dialogue
	 * @param titre Titre de la boite de dialogue
	 * @param message Message a affich� dans la boite de dialogue
	 * @param options Choix (bouttons) a affich� dans la boite de dialogue 
	 * @param optionParDefaut Choix s�lectionn� par defaut 
	 * @return Le code de retour de la boite de dialogue
	 * 
	 * @see JOptionPane
	 */
	public static int createOptionBox(int type, String titre, String message, String[] options, int optionParDefaut) {
		int retour = JOptionPane.showOptionDialog(Fenetre.getInstance(), message, titre, type, JOptionPane.QUESTION_MESSAGE, null, options, options[optionParDefaut]); 
		return retour;
	}
	
	/**
	 * Cr�er une boite de dialogue sans choix possible (uniquement un boutton pour la fermer)
	 * 
	 * @param type Type de la boite de dialogue
	 * @param titre Titre de la boite de dialogue
	 * @param message Message a affich�
	 * 
	 * @see JOptionPane
	 */
	public static void createModalBox(int type, String titre, String message) {
		JOptionPane.showMessageDialog(Fenetre.getInstance(), message, titre, type);
	}

}
