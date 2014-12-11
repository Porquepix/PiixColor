package piixcolor.utilitaire;

import java.awt.Color;

public enum Couleur {
	
	BLEU (3, 169, 244),
	BEIGE (220, 220, 180),
	GRIS (158, 158, 158),
	JAUNE (255, 255, 0),
	ORANGE (255, 152, 0),
	MARRON (114, 73, 44),
	ROSE (236, 64, 122),
	ROUGE (229, 57, 53),
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
		return c.getCouleur().equals(couleur);
	}

}
