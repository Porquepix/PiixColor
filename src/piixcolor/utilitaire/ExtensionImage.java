package piixcolor.utilitaire;

import java.io.File;

public enum ExtensionImage {
	
	PNG("png"), GIF("gif"), JPG("jpg"), JPEG("jpeg");
	
	private String ext;
	
	ExtensionImage(String ext) {
		this.ext = ext;
	}
	
	public String getExtention() {
		return this.ext;
	}

	/**
	 * 
	 * 
	 * @param image
	 * @return
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

	public static boolean isValidExtension(String extension) {
		for (ExtensionImage ext : ExtensionImage.values()) {
			if (ext.getExtention().compareTo(extension) == 0) {
				return true;
			}
		}
		return false;
	}
	
	

}
