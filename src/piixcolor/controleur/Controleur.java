package piixcolor.controleur;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import piixcolor.modele.Modele;


public abstract class Controleur {
	
	/**
	 * Instance du modele pour faciliter son accès dans les controleurs.
	 */
	private Modele modele;
	
	/**
	 * Constructeur de la classe.
	 * 
	 * @param m instance du modele
	 */
	public Controleur(Modele m) {
		this.modele = m;
	}
	
	/**
	 * Accesseur permetant d'obtenir l'instance du modele.
	 * 
	 * @return instance du modele
	 */
	public Modele getModele () {
		return this.modele;
	}

	/**
	 * Retourne le nombre de formes presentes dans la matrice.
	 * 
	 * @return nombre de formes presentes dans la matrice
	 */
	public int getNbForme(){
		return getModele().getFormesConfig().size();
	}
	
	/**
	 * Retourne le nombre de couleurs presentes dans la matrice.
	 * 
	 * @return nombre de couleurs presentes dans la matrice
	 */
	public int getNbCouleur(){
		return getModele().getCouleursConfig().size();
	}
	
	/**
	 * Retourne le nombre d'objets colorés présents dans la réserve.
	 * 
	 * @return nombre d'objets colorés présents dans la réserve
	 */
	public int getNbObjetColore() {
		return getModele().getReserveForme().size();
	}
	
	/**
	 * Retourne le nombre de cases totales du plateau de jeu (cases de la matrice + cases de la réserve).
	 * 
	 * @return nombre de cases totales du plateau de jeu
	 */
	public int taillePlateau() {
		return (getNbCouleur()*2 + 1) * (getNbForme() + 1);
	}
	
	/**
	 * Retroune le nombre maximal d'objets colorées plassablent dans la réserve.
	 * 
	 * @return nombre maximal d'objets colorées plassablent dans la réserve
	 */
	public int getMaxObjColore() {
		return (taillePlateau() - 1) - ((getNbCouleur() + 1) * (getNbForme() + 1));
	}
	
	/**
	 * Redimensionne une image.
	 * 
	 * @param image image a redimensionné
	 * @param newWidth largeur de l'image redimensionné
	 * @param newHeight hauteur de l'image redimensionné
	 * @return  image correctement redimensionné
	 */
	public BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
		return imageToBufferedImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
	}

	/**
	 * Convertie une image de la class Image en image de la class BufferedImage.
	 * 
	 * @param image image a convertir
	 * @return image convertie
	 */
	protected BufferedImage imageToBufferedImage(Image image) {
		BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bi;
	}

}
