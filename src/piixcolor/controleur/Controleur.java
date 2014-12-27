package piixcolor.controleur;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import piixcolor.modele.Modele;


public abstract class Controleur {
	
	/**
	 * Instance du modele pour faciliter son acc�s dans les controleurs.
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
	 * Retourne le nombre d'objets color�s pr�sents dans la r�serve.
	 * 
	 * @return nombre d'objets color�s pr�sents dans la r�serve
	 */
	public int getNbObjetColore() {
		return getModele().getReserveForme().size();
	}
	
	/**
	 * Retourne le nombre de cases totales du plateau de jeu (cases de la matrice + cases de la r�serve).
	 * 
	 * @return nombre de cases totales du plateau de jeu
	 */
	public int taillePlateau() {
		return (getNbCouleur()*2 + 1) * (getNbForme() + 1);
	}
	
	/**
	 * Retroune le nombre maximal d'objets color�es plassablent dans la r�serve.
	 * 
	 * @return nombre maximal d'objets color�es plassablent dans la r�serve
	 */
	public int getMaxObjColore() {
		return (taillePlateau() - 1) - ((getNbCouleur() + 1) * (getNbForme() + 1));
	}
	
	/**
	 * Redimensionne une image.
	 * 
	 * @param image image a redimensionn�
	 * @param newWidth largeur de l'image redimensionn�
	 * @param newHeight hauteur de l'image redimensionn�
	 * @return  image correctement redimensionn�
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
