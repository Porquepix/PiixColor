package piixcolor.vue;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JOptionPane;

import piixcolor.modele.Modele;

public class BoiteDialogue {
	
	/**
	 * Gere avec des boites de dialogue l'enregistrement de la configuration actuelle dans le fichier passer en paramètre.
	 * 
	 * @param path Fichier ou la configuration sera enregistrée
	 */
	public static void enregistrerConfig(String path) {
		try {
			Modele.getInstance().enregistrer(path);
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration a bien été sauvgardée.", "Information", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration n'a pas pu être sauvgardée car le fichier config.xml est introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "La configuration n'a pas pu être sauvgardée car une erreur lors de l'écriture du fichier config.xml est intervenue.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Créer un boite de dialogue avec plusieurs choix possible. Cette boite est attachée à la fenetre.
	 * 
	 * @param type Type de la boite de dialogue
	 * @param titre Titre de la boite de dialogue
	 * @param message Message a affiché dans la boite de dialogue
	 * @param options Choix (bouttons) a affiché dans la boite de dialogue 
	 * @param optionParDefaut Choix sélectionné par defaut 
	 * @return Le code de retour de la boite de dialogue
	 * 
	 * @see JOptionPane
	 */
	public static int createOptionBox(int type, String titre, String message, String[] options, int optionParDefaut) {
		int retour = JOptionPane.showOptionDialog(Fenetre.getInstance(), message, titre, type, JOptionPane.QUESTION_MESSAGE, null, options, options[optionParDefaut]); 
		return retour;
	}
	
	/**
	 * Créer une boite de dialogue sans choix possible (uniquement un boutton pour la fermer)
	 * 
	 * @param type Type de la boite de dialogue
	 * @param titre Titre de la boite de dialogue
	 * @param message Message a affiché
	 * 
	 * @see JOptionPane
	 */
	public static void createModalBox(int type, String titre, String message) {
		JOptionPane.showMessageDialog(Fenetre.getInstance(), message, titre, type);
	}

}
