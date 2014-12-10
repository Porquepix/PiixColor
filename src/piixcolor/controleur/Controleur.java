package piixcolor.controleur;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.Observateur;


public abstract class Controleur implements Observateur {
	
	private Modele modele;
	
	public Controleur(Modele m) {
		this.modele = m;
	}
	
	public Modele getModele () {
		return this.modele;
	}

	public int getNbForme(){
		return getModele().getFormesConfig().size();
	}
	
	public int getNbObjetColore() {
		return getModele().getReserveForme().size();
	}
	
	public int getNbCouleur(){
		return getModele().getCouleursConfig().size();
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
