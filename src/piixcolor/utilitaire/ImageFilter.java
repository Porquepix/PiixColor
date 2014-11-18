package piixcolor.utilitaire;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String fileExtension = ExtensionImage.getExtension(f);
		if (fileExtension != null) {
			for (ExtensionImage ext : ExtensionImage.values()) {
				if (ext.getExtention().compareTo(fileExtension) == 0) {
					return true;
				}
			}
		}

		return false;
	}

	public String getDescription() {
		String description = "Seulement les images (";
		for (ExtensionImage ext : ExtensionImage.values()) {
			description += "." + ext.getExtention() + ", ";
		}
		description = description.substring(0, description.length() - 2) + ")";
		return description;
	}

}