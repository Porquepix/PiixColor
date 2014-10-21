package pixcolor.test;

import java.io.File;
import java.io.FilenameFilter;

public class Listing {

	public static void liste(String path) {
		File f = new File(path);
		System.out.println("Images dans le dossier " + f.getAbsolutePath() + "\n");
		
		FilenameFilter filtre = new FilenameFilter() {
			  public boolean accept(File dir, String name) {
				    return name.endsWith(".png");
			  }
		};

		File[] files = f.listFiles(filtre);

		if (files != null)
			for (File file : files) {
				System.out.println(file.getAbsolutePath());
			}
	}

}
