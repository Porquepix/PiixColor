package piixcolor.utilitaire;

import java.awt.Color;

public enum Couleur {
	
	BLEU (3, 169, 244),
	GRIS (158, 158, 158),
	JAUNE (255, 255, 0),
	ORANGE (255, 152, 0),
	MARRON (121, 85, 72),
	NOIR (0, 0, 0),
	ROSE (255, 175, 175),
	ROUGE (244, 67, 54),
	VERT (118, 255, 3),
	VIOLET (156, 39, 176);
	
	private Color couleur;

	Couleur(int r, int g, int b) {
		this.setCouleur(new Color(r, g, b));
	}


	public Color getCouleur() {
		return couleur;
	}


	public void setCouleur(Color coul) {
		this.couleur = coul;
	}
	
	public boolean equals(Couleur c) {
		return c.equals(couleur);
	}

}
