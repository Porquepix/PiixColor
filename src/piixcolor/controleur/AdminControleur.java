package piixcolor.controleur;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.Observateur;

public class AdminControleur extends Controleur {

	public AdminControleur(Modele m) {
		super(m);
	}
	
	/**
	 * Enregistre une image � l'interieur de l'application.
	 * L'image est pr�alablement redimensionn�e pour gagner de l'espace disque.
	 * La m�thode poss�de une s�curit� pour �viter l'�crasement d'image (dans le cas o� le nom de l'image import�e est d�j� pr�sent). 
	 * 
	 * @param image image a enregist�
	 * @return true si l'image a pu �tre enregistr�, false sinon
	 */
	public boolean saveImage(File image) {
		try {
			BufferedImage i = ImageIO.read(image);
			String imageName = image.getName().split("\\.")[0];
			
			//s�curit� pour emp�cher l'�crasement d'image
			File f = new File(Modele.DOSSIER_FORMES + imageName + "." + Modele.FORMAT_IMAGE_SAVE);
			int j = 1;
			while (f.exists()) {
				f = new File(Modele.DOSSIER_FORMES + imageName + " (" + j + ")." + Modele.FORMAT_IMAGE_SAVE);
				j++;
			}
			
			ImageIO.write(resizeImage(i, Modele.IMG_SIZE, Modele.IMG_SIZE), Modele.FORMAT_IMAGE_SAVE, f);
			
			getModele().notifier(Observateur.SIG_IMAGE_SAVE);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Un probleme est survenu lors de l'importaion de l'image");
			return false;
		}
	}
	
	public boolean deleteImage(File image) {
		if (image.delete()) {
			return true;
		}
		return false;
	}

	/**
	 * Redimensionne une image.
	 * 
	 * @param image image a redimensionn�
	 * @param newWidth largeur de l'image redimensionn�
	 * @param newHeight hauteur de l'image redimensionn�
	 * @return  image correctement redimensionn�
	 */
	private BufferedImage resizeImage(BufferedImage image, int newWidth, int newHeight) {
		return imageToBufferedImage(image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
	}

	/**
	 * Convertie une image de la class Image en image de la class BufferedImage.
	 * 
	 * @param image image a convertir
	 * @return image convertie
	 */
	private BufferedImage imageToBufferedImage(Image image) {
		BufferedImage bi = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bi;
	}

	public void actualise(int sig) {
		
	}

}
