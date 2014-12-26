package piixcolor.utilitaire;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {

	/**
	 * Redéfinition de la fonction accept de FileFilter.
	 * Accepte tout les fichiers de type image (definit par la classe ExtensionImage)
	 * 
	 * @see FileFilter#accept(File)
	 * @see ExtensionImage
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String fileExtension = ExtensionImage.getExtension(f);
		if (fileExtension != null) {
			if (ExtensionImage.isValidExtension(fileExtension)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Redéfinition de la fonction getDescription de FileFilter.
	 * Retourne la listes des extensions d'images valide.
	 * 
	 * @see FileFilter#getDescription()
	 * @see ExtensionImage
	 */
	public String getDescription() {
		String description = "Seulement les images (";
		for (ExtensionImage ext : ExtensionImage.values()) {
			description += "." + ext.getExtention() + ", ";
		}
		description = description.substring(0, description.length() - 2) + ")";
		return description;
	}

}