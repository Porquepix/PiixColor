package piixcolor.utilitaire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.*;

public class Config {
	
	public static final int IMG_SIZE = 100;
	public static final int MAX_SELECTED_FORMES = 4;
	public static final int MAX_SELECTED_COULEURS = 4;
	public static final String DOSSIER_FORME = "images/";
	public static final String FORMAT_IMAGE_SAVE = "png";
	public static final String FICHIER_CONFIG = "config.xml";
	
	
	private static Config config = null;
	private Element racine;
	private Document document;
	
	private Config(String path) {
		SAXBuilder builder = new SAXBuilder();
		try {
			document = builder.build(new File(path));
			racine = document.getRootElement();
		} catch (JDOMException e) {
			System.err.println("Impossible de parser le fichier XML.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Impossible de lire le fichier de configuration.");
			e.printStackTrace();
		}
	}
	
	public void enregistrer(String path) throws FileNotFoundException, IOException {
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(path));
	}
	
	/**
	 * Methode de test a mettre dans les controlleur ou modele
	 */
	public void getMatriceCouleur() {
		List listCouleurs = racine.getChild("matrice").getChild("couleurs").getChildren("couleur");
		Iterator i = listCouleurs.iterator();
		while(i.hasNext()) {
		      Element courant = (Element)i.next();
		      System.out.println(courant.getText());
		      //ICI CONNECTER VALEUR AVEC Couleur.getValue(int);
		}
	}
	
	//Changer int par une couleur
	public void addMatriceCouleur(int couleur) {
		Element listCouleurs = racine.getChild("matrice").getChild("couleurs");
		Element e = new Element("couleur");
		e.setText(Integer.toString(couleur));
		listCouleurs.addContent(e);
	}
	
	public static Config getInstance() {
		if (config == null) config = new Config(FICHIER_CONFIG);
		return config;
	}

}
