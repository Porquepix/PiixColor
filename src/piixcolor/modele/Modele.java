package piixcolor.modele;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import piixcolor.utilitaire.Couleur;
import piixcolor.utilitaire.ObjetColore;
import piixcolor.utilitaire.Observateur;

public class Modele {
	
	
	private List<Observateur> observateurs = new ArrayList<Observateur>();
	private List<Color> couleursConfig = new ArrayList<Color>();
	private List<File> formesConfig = new ArrayList<File>();
	private List<ObjetColore> reserveForme = new ArrayList<ObjetColore>();
	
	public static final int IMG_SIZE = 100;
	public static final int MAX_SELECTED_FORMES = 3;
	public static final int MAX_SELECTED_COULEURS = 4;
	public static final String DOSSIER_FORME = "images/";
	public static final String FORMAT_IMAGE_SAVE = "png";
	public static final String FICHIER_CONFIG = "config.xml";
	
	
	private static Modele instance = null;
	private Element racine;
	private Document document;
	
	private Modele(String path) {
		SAXBuilder builder = new SAXBuilder();
		try {
			document = builder.build(new File(path));
			racine = document.getRootElement();
			this.loadCouleurConfig();
			this.loadFormeConfig();
			this.loadReserveForme();
		} catch (JDOMException e) {
			System.err.println("Impossible de parser le fichier XML.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Impossible de lire le fichier de configuration.");
			e.printStackTrace();
		}
	}
	
	public static Modele getInstance() {
		if (instance == null) instance = new Modele(FICHIER_CONFIG);
		return instance;
	}
	
	public void enregistrer(String path) throws FileNotFoundException, IOException {
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(path));
	}
	
	public void loadCouleurConfig() {
		List listCouleurs = racine.getChild("matrice").getChild("couleurs").getChildren("couleur");
		Iterator i = listCouleurs.iterator();
		while(i.hasNext()) {
		      Element courant = (Element)i.next();
		      this.couleursConfig.add((Couleur.values()[Integer.parseInt(courant.getText())-1]).getCouleur());
		}
	}
	
	public void loadFormeConfig() {
		List listFormes = racine.getChild("matrice").getChild("formes").getChildren("forme");
		Iterator i = listFormes.iterator();
		while(i.hasNext()) {
		      Element courant = (Element)i.next();
		      File f = new File(Modele.DOSSIER_FORME + courant.getText());
		      this.formesConfig.add(f);
		}
	}
	
	public void loadReserveForme() {
		List listObjetColore = racine.getChild("formePool").getChildren("forme");
		Iterator i = listObjetColore.iterator();
		while(i.hasNext()) {
		      Element courant = (Element)i.next();
		      ObjetColore o;
			try {
				o = new ObjetColore((Couleur.values()[Integer.parseInt(courant.getChild("couleur").getText())-1]), ImageIO.read(new File(Modele.DOSSIER_FORME + courant.getChild("path").getText())));
				this.reserveForme.add(o);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.err.println("Un probleme est survenu avec le format de l'image");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Un probleme est survenu lors du chargement de l'image");
			}
		}
	}

	public void addMatriceCouleur(Couleur couleur) {
		Element listCouleurs = racine.getChild("matrice").getChild("couleurs");
		Element e = new Element("couleur");
		e.setText(couleur.toString());
		listCouleurs.addContent(e);
	}
	
	public void ajoutObservateur(Observateur ob) {
		observateurs.add(ob);
	}

	public void retireObservateur(Observateur ob) {
		observateurs.remove(ob);
	}
	
	public void notifier() {
		for (Observateur ob : observateurs) {
			ob.actualise();
		}
	}

	public List<Color> getCouleursConfig() {
		return couleursConfig;
	}

	public void setCouleursConfig(ArrayList<Color> couleursConfig) {
		this.couleursConfig = couleursConfig;
		notifier();
	}

	public List<File> getFormesConfig() {
		return formesConfig;
	}

	public List<ObjetColore> getReserveForme() {
		return reserveForme;
	}

	public void setReserveForme(List<ObjetColore> reserveForme) {
		this.reserveForme = reserveForme;
		notifier();
	}

	public void setFormesConfig(ArrayList<File> formesConfig) {
		this.formesConfig = formesConfig;
		notifier();
	}
	
	
}
