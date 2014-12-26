package piixcolor.utilitaire;

import java.io.File;

public enum ExtensionImage {
	
	PNG("png"), GIF("gif"), JPG("jpg"), JPEG("jpeg");
	
	/**
	 * Nom de l'extension
	 */
	private String ext;
	
	/**
	 * Constructeur d'une extension d'image.
	 * 
	 * @param ext nom de l'extension a cr�er
	 */
	ExtensionImage(String ext) {
		this.ext = ext;
	}
	
	/**
	 * Accesseur du nom de l'extension.
	 * 
	 * @return nom de l'extension
	 */
	public String getExtention() {
		return this.ext;
	}

	/**
	 * Retroune l'extension du fichier pass� en param�tre.
	 * 
	 * @param image fichier 
	 * @return l'extension du fichier
	 */
	public static String getExtension(File image) {
		String ext = null;
		String name = image.getName();
		
		int i = name.lastIndexOf('.');
		if (i > 0 && i < name.length() - 1) {
			ext = name.substring(i + 1).toLowerCase();
		}
		
		return ext;
	}

	/**
	 * Teste la validiter d'une extension (si l'extension est pr�sente dans la liste des �num�rations).
	 * 
	 * @param extension extension � tester
	 * @return true si l'extension est valide, false sinon
	 */
	public static boolean isValidExtension(String extension) {
		for (ExtensionImage ext : ExtensionImage.values()) {
			if (ext.getExtention().compareTo(extension) == 0) {
				return true;
			}
		}
		return false;
	}
	
	

}
