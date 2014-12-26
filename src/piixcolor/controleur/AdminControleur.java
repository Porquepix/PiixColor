package piixcolor.controleur;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import piixcolor.modele.Modele;
import piixcolor.utilitaire.Observateur;

public class AdminControleur extends Controleur {

	/**
	 * Constructeur du controleur de l'admin. Il se contente d'appeler celui de la classe Controleur.
	 * 
	 * @param m modele de l'application
	 * 
	 * @see Controleur#Controleur(Modele)
	 */
	public AdminControleur(Modele m) {
		super(m);
	}
	
	/**
	 * Enregistre une image à l'interieur de l'application (Forme).
	 * L'image est préalablement redimensionnée pour gagner de l'espace disque.
	 * La méthode possède une sécurité pour éviter l'écrasement d'image (dans le cas où le nom de l'image importée est déjà présent). 
	 * 
	 * @param image image a enregisté
	 * @return true si l'image a pu être enregistré, false sinon
	 */
	public boolean saveImage(File image) {
		try {
			BufferedImage i = ImageIO.read(image);
			String imageName = image.getName().split("\\.")[0];
			
			//sécurité pour empécher l'écrasement de l'image
			File f = new File(Modele.DOSSIER_FORMES + imageName + "." + Modele.FORMAT_IMAGE_SAVE);
			int j = 1;
			while (f.exists()) {
				f = new File(Modele.DOSSIER_FORMES + imageName + " (" + j + ")." + Modele.FORMAT_IMAGE_SAVE);
				j++;
			}
			
			BufferedImage imageResize = resizeImage(i, Modele.IMG_SIZE, Modele.IMG_SIZE);
			ImageIO.write(imageResize, Modele.FORMAT_IMAGE_SAVE, f);
			
			getModele().notifier(Observateur.SIG_IMAGE_SAVE);
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Un probleme est survenu lors de l'importaion de l'image");
			return false;
		}
	}

	/**
	 * Supprime une image de l'application.
	 * 
	 * @param image image a supprimé
	 * @return true si l'image a pu être supprimé, false sinon
	 */
	public boolean deleteImage(File image) {
		if (image.delete()) {
			return true;
		}
		return false;
	}
}
