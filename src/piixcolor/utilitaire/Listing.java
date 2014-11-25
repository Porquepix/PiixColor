package piixcolor.utilitaire;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.JOptionPane;

import piixcolor.vue.Fenetre;

public class Listing {

	public static File[] listeImages(String path) {
		File f = new File(path);
		
		if (!f.exists()) {
			JOptionPane.showMessageDialog(Fenetre.getInstance(), "Impossible d'ouvrir le dossier : '" + f.getAbsolutePath() + "'", "Erreur", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		FilenameFilter filtre = new FilenameFilter() {
			  public boolean accept(File dir, String name) {
				    return name.endsWith(Config.FORMAT_IMAGE_SAVE);
			  }
		};

		File[] files = f.listFiles(filtre);
		
		Arrays.sort(files, new Comparator<File>() {
	        public int compare(File f1, File f2) {
	            long d1 = f1.lastModified();
	            long d2 = f2.lastModified();
	            return d1 > d2 ? 1 : d1 < d2 ? -1 : 0;
	        }
	    });
		
		return files;
	}

}
