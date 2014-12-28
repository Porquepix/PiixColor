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
	 * Couleur correspondant à la couleur de notre classe énuméré dans la classe Color de Java.
	 */
	private Color couleur;

	/**
	 * Constructeur de Couleur permettant construire une couleur voulue à l'aide des trois composantes rouge, verte et bleue.
	 * On appel simplement le constructeur de Color.
	 * 
	 * @param r Degrès de rouge.
	 * @param g Degrès de vert.
	 * @param b Degrès de bleu.
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
	 * Méthode pour tester si deux couleurs sont équivalentes.
	 * On appelle simplement la méthode d'equals en comparant l'attribut couleur de chacune des deux couleurs.
	 * 
	 * @param c Couleur à comparer avec la couleur courante.
	 * @return Un booléen exprimant si les deux couleurs sont équivalentes.
	 */
	public boolean equals(Couleur c) {
		return c.getCouleur().equals(couleur);
	}

}
