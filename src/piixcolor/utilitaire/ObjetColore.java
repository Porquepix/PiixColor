package piixcolor.utilitaire;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjetColore {

	/**
	 * Couleur de l'objet.
	 */
	private Couleur couleur;

	/**
	 * Image représentant la forme de l'objet.
	 */
	private BufferedImage image;

	/**
	 * Fichier original de l'image.
	 */
	private File origineFile;

	/**
	 * Constructeur d'un objet coloré.
	 * 
	 * @param couleur
	 *            Couleur de l'objet
	 * @param image
	 *            Image (Forme) de l'objet
	 */
	public ObjetColore(Couleur couleur, File image) {
		this.couleur = couleur;
		this.origineFile = image;
		try {
			this.image = ImageIO.read(image);
			this.filtreImage(couleur);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remplace les couleurs sombre de l'image de l'objet coloré par la couleur
	 * passer en paramètre. Les couleurs les plus claires deviennent
	 * transparente.
	 * 
	 * @param c
	 *            Nouvelle couleur de l'image
	 */
	public void filtreImage(Couleur c) {
		// change le mode de l'image pour gérer la transparence
		this.changeImageMode(BufferedImage.TYPE_INT_ARGB);

		int w = image.getWidth();
		int h = image.getHeight();
		int pixels[] = new int[w * h];
		// extrait l'image et la met dans le tableu pixels
		image.getRGB(0, 0, w, h, pixels, 0, w);

		for (int i = 0; i < w * h; i++) {
			// extraction des composantes du pixel (a = alpha, r = red, g =
			// green, b = blue)
			int a = (pixels[i] & 0xFF000000) >> 24;
			int r = (pixels[i] & 0xFF0000) >> 16;
			int g = (pixels[i] & 0xFF00) >> 8;
			int b = (pixels[i] & 0xFF);

			// valeur de la tolérance
			int threshhold = 230;

			// si le pixels est trop blanc on met l'opacité à zéro
			// sinon on le remplace par la couleur
			if (r > threshhold && b > threshhold && g > threshhold) {
				pixels[i] = 0x00FFFFFF;
			} else {
				pixels[i] = (a << 24) | (c.getCouleur().getRGB() & 0x00FFFFFF);
			}
		}

		// on met le nouveau tableau pixel obtenu dans l'image
		image.setRGB(0, 0, w, h, pixels, 0, w);
	}

	/**
	 * Change le mode de l'image de l'objet coloré.
	 * 
	 * @param newMode
	 *            Le nouveau de l'image
	 * 
	 * @see BufferedImage
	 */
	public void changeImageMode(int newMode) {
		BufferedImage newImage = new BufferedImage(image.getWidth(),
				image.getHeight(), newMode);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		this.image = newImage;
	}

	/**
	 * Accesseur de la couleur de l'objet coloré.
	 * 
	 * @return La couleur de l'objet coloré
	 */
	public Couleur getCouleur() {
		return couleur;
	}

	/**
	 * Accesseur de l'image de l'objet coloré.
	 * 
	 * @return L'image de l'objet coloré
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Accesseur du fichier original de l'objet coloré.
	 * 
	 * @return Le fichier original de l'objet coloré
	 */
	public File getOrigineFile() {
		return origineFile;
	}

	/**
	 * Redéfinition de la méthode equals. Deux objets colorés sont égaux si est
	 * seulement si leurs couleurs et leurs fichiers d'origine sont égaux.
	 * 
	 */
	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}
		ObjetColore oc = (ObjetColore) o;
		return this.getOrigineFile().equals(oc.getOrigineFile())
				&& this.getCouleur().equals(oc.getCouleur());
	}

	/**
	 * Redéfinitiond e la méthode toString.
	 * 
	 * @return Le nom de la forme suivuit du nom de la couleur
	 */
	public String toString() {
		return origineFile.getName() + " " + couleur.toString();
	}

}
