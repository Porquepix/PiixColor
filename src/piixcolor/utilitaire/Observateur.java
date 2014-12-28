package piixcolor.utilitaire;

import piixcolor.modele.Modele;


public interface Observateur {
	
	/**
	 * Signal de modification de la liste des couleurs.
	 */
	public static final int SIG_COLORS_UPDATE = 0;
	/**
	 * Signal de modification de la liste des formes.
	 */
	public static final int SIG_FORMES_UPDATE = 1;
	/**
	 * Signal de modification de la liste des objets colorés.
	 */
	public static final int SIG_RESERVE_UPDATE = 2;
	/**
	 * Signal de supression d'images.
	 */
	public static final int SIG_IMAGE_DELETE = 3;
	/**
	 * Signal de sauvegarde d'images.
	 */
	public static final int SIG_IMAGE_SAVE = 3;
	/**
	 * Signal de fin de partie.
	 */
	public static final int SIG_PARTIE_FINIE = 4;
	
	/**
	 * Méthode abstraite d'Observateur qui permetra à chaque observateur de s'actualiser quand le modèle est mis à jour.
	 * 
	 * @param sig Signal donnant une information supplémentaire sur la mise à jour.
	 * @see Modele#notifier(int)
	 */
	void actualise(int sig);
}
