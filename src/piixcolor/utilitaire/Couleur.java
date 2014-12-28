package piixcolor.utilitaire;

import java.awt.Color;

public enum Couleur {
	
	BLEU (3, 169, 244),
	BEIGE (218, 163, 122),
	GRIS (158, 158, 158),
	JAUNE (255, 255, 0),
	ORANGE (255, 152, 0),
	MARRON (114, 73, 44),
	ROSE (236, 64, 122),
	ROUGE (229, 57, 53),
	VERT (118, 255, 3),
	VIOLET (156, 39, 176);
	
	/**
	 * Couleur correspondant � la couleur de notre classe �num�r� dans la classe Color de Java.
	 */
	private Color couleur;

	/**
	 * Constructeur de Couleur permettant construire une couleur voulue � l'aide des trois composantes rouge, verte et bleue.
	 * On appel simplement le constructeur de Color.
	 * 
	 * @param r Degr�s de rouge.
	 * @param g Degr�s de vert.
	 * @param b Degr�s de bleu.
	 */
	Couleur(int r, int g, int b) {
		this.setCouleur(new Color(r, g, b));
	}

	/**
	 * Accesseur de couleur.
	 * 
	 * @return L'attribut couleur.
	 */
	public Color getCouleur() {
		return couleur;
	}

	/**
	 * Mutateur de couleur.
	 * 
	 * @param coul nouvelle couleur pour l'attribut couleur.
	 */
	public void setCouleur(Color coul) {
		this.couleur = coul;
	}
	
	/**
	 * M�thode pour tester si deux couleurs sont �quivalentes.
	 * On appelle simplement la m�thode d'equals en comparant l'attribut couleur de chacune des deux couleurs.
	 * 
	 * @param c Couleur � comparer avec la couleur courante.
	 * @return Un bool�en exprimant si les deux couleurs sont �quivalentes.
	 */
	public boolean equals(Couleur c) {
		return c.getCouleur().equals(couleur);
	}

}
