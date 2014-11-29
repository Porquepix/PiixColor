package piixcolor.modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	private List<Couleur> couleursConfig = new ArrayList<Couleur>();
	private List<File> formesConfig = new ArrayList<File>();
	private List<ObjetColore> reserveForme = new ArrayList<ObjetColore>();
	
	public static final int IMG_SIZE = 100;
	public static final int MAX_SELECTED_FORMES = 3;
	public static final int MAX_SELECTED_COULEURS = 4;
	public static final String DOSSIER_FORMES = "images/";
	public static final String DOSSIER_ASSETS = DOSSIER_FORMES + "assets/";
	public static final String FORMAT_IMAGE_SAVE = "png";
	public static final String FICHIER_CONFIG = "config.xml";
	
	
	private static Modele instance = null;
	private Element racine;
	private Document document;
	private boolean estModifie;
	
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
		setEstModifie(false);
	}
	
	public static Modele getInstance() {
		if (instance == null) instance = new Modele(FICHIER_CONFIG);
		return instance;
	}
	
	public void enregistrer(String path) throws FileNotFoundException, IOException {
		saveCouleurConfig();
		saveFormeConfig();
		saveReserveConfig();
		
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(path));
        setEstModifie(false);
	}
	
	public void loadCouleurConfig() {
		List<Element> listCouleurs = racine.getChild("matrice").getChild("couleurs").getChildren("couleur");
		Iterator<Element> i = listCouleurs.iterator();
		while(i.hasNext()) {
		      Element courant = i.next();
		      this.couleursConfig.add((Couleur.values()[Integer.parseInt(courant.getText())]));
		}
	}
	
	public void saveCouleurConfig() {
		Element couleurs = racine.getChild("matrice").getChild("couleurs");
		couleurs.removeChildren("couleur");
		for (Couleur c : couleursConfig) {
			Element couleur = new Element("couleur");
			couleur.setText(Integer.toString(c.ordinal()));
			couleurs.addContent(couleur);
		}
	}
	
	public void loadFormeConfig() {
		List<Element> listFormes = racine.getChild("matrice").getChild("formes").getChildren("forme");
		Iterator<Element> i = listFormes.iterator();
		while(i.hasNext()) {
		      Element courant = i.next();
		      File f = new File(Modele.DOSSIER_FORMES + courant.getText());
		      this.formesConfig.add(f);
		}
	}
	
	public void saveFormeConfig() {
		Element formes = racine.getChild("matrice").getChild("formes");
		formes.removeChildren("forme");
		for (File f : formesConfig) {
			Element forme = new Element("forme");
			forme.setText(f.getName());
			formes.addContent(forme);
		}
	}
	
	public void loadReserveForme() {
		List<Element> listObjetColore = racine.getChild("formePool").getChildren("forme");
		Iterator<Element> i = listObjetColore.iterator();
		while(i.hasNext()) {
			Element courant = i.next();
		    ObjetColore o = new ObjetColore((Couleur.values()[Integer.parseInt(courant.getChild("couleur").getText())]), new File(Modele.DOSSIER_FORMES + courant.getChild("path").getText()));
			this.reserveForme.add(o);
		}
	}
	
	public void saveReserveConfig() {
		Element formes = racine.getChild("formePool");
		formes.removeChildren("forme");
		for (ObjetColore oc : reserveForme) {
			Element forme = new Element("forme");
			
			Element path = new Element("path");
			path.setText(oc.getOrigineFile().getName());
			
			Element couleur = new Element("couleur");
			couleur.setText(Integer.toString(oc.getCouleur().ordinal()));
			
			forme.addContent(path);
			forme.addContent(couleur);
			
			formes.addContent(forme);
		}
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

	public List<Couleur> getCouleursConfig() {
		return couleursConfig;
	}

	public void setCouleursConfig(List<Couleur> couleursConfig) {
		if (this.couleursConfig.size() != couleursConfig.size()) {	
			this.couleursConfig = couleursConfig;
			setEstModifie(true);
			notifier();
		}
	}

	public List<File> getFormesConfig() {
		return formesConfig;
	}

	public List<ObjetColore> getReserveForme() {
		return reserveForme;
	}

	public void setReserveForme(List<ObjetColore> reserveForme) {
		if (this.reserveForme.size() != reserveForme.size()) {
			this.reserveForme = reserveForme;
			setEstModifie(true);
			notifier();
		}
	}

	public void setFormesConfig(List<File> formesConfig) {
		if (this.formesConfig.size() != formesConfig.size()) {
			this.formesConfig = formesConfig;
			setEstModifie(true);
			notifier();
		}
	}

	public boolean isEstModifie() {
		return estModifie;
	}

	public void setEstModifie(boolean estModifie) {
		this.estModifie = estModifie;
	}
	
	
}
