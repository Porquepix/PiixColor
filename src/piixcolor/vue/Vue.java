package piixcolor.vue;
import javax.swing.JPanel;

import piixcolor.controleur.Controleur;
import piixcolor.utilitaire.Observateur;


@SuppressWarnings("serial")
public abstract class Vue extends JPanel implements Observateur {
	
	/**
	 * Fenetre o� toutes les vues sont plac�es
	 */
	protected Fenetre fenetre;
	/**
	 * Chaque vue poss�de un controleur.
	 */
	protected Controleur controleur;
	
	/**
	 * Constructeur d'une vue appel� par toutes les autres vues.
	 * 
	 * @param fenetre La fen�tre.
	 * @param controleur Le contr�leur.
	 * @see VueAdmin#VueAdmin(Fenetre, piixcolor.controleur.AdminControleur)
	 * @see VueAccueil#VueAccueil(Fenetre, piixcolor.controleur.AccueilControleur)
	 * @see VueFinPartie#VueFinPartie(Fenetre)
	 * @see VuePlateau#VuePlateau(Fenetre, piixcolor.controleur.PlateauControleur)
	 */
	public Vue(Fenetre fenetre, Controleur controleur) {
		this.fenetre = fenetre;
		this.controleur = controleur;
		setSize(fenetre.getWidth(), fenetre.getHeight());
	}
	
	/**
	 * Accesseur du controleur d'une vue.
	 * 
	 * @return Le controleur de la vue.
	 */
	public Controleur getControleur() {
		return this.controleur;
	}
	
	public Vue getView() {
		return this;
	}

}
