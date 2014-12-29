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
	
	/**
	 * Le mod�le est sujet de nombreux observateurs qui sont r�guli�rement inform�s de ses modifications. Pour cela le mod�le garde en m�moire la liste de tous les observateurs qui le concerne pour pouvoir les notifier.
	 */
	private List<Observateur> observateurs = new ArrayList<Observateur>();
	/**
	 * Liste de toutes les couleurs de la configuration actuelle.
	 */
	private List<Couleur> couleursConfig = new ArrayList<Couleur>();
	/**
	 * Liste de toutes les formes incolores de la configuration actuelle.
	 */
	private List<File> formesConfig = new ArrayList<File>();
	/**
	 * Liste des objets color�s qui compose la r�serve de forme actuelle.
	 */
	private List<ObjetColore> reserveForme = new ArrayList<ObjetColore>();
	
	/**
	 * Constante pour la taille des images.
	 */
	public static final int IMG_SIZE = 100;
	/**
	 * Constante pour le nombre maximum de forme possible dans une configuration.
	 */
	public static final int MAX_SELECTED_FORMES = 4;
	/**
	 * Constante pour le nombre maximum de couleur possible dans une configuration.
	 */
	public static final int MAX_SELECTED_COULEURS = 4;
	/**
	 * Constante pour le nombre maximum d'objet color�s possibles dans une configuration.
	 */
	public static final int MAX_RESERVE_FORMES = 20;
	/**
	 * Chemin pour les images de tout le projet.
	 */
	public static final String DOSSIER_FORMES = "images/";
	/**
	 * Chemin pour les assets du projet (images utilis�es pour les boutons etc).
	 */
	public static final String DOSSIER_ASSETS = DOSSIER_FORMES + "assets/";
	/**
	 * Extention utilis�e par d�faut pour sauvegarder une image dans le projet.
	 */
	public static final String FORMAT_IMAGE_SAVE = "png";
	/**
	 * Nom du fichier de configuration � utiliser pour charger et sauvegarder une configuration
	 */
	public static final String FICHIER_CONFIG = "config.xml";
	
	private static Modele instance = null;
	private Element racine;
	private Document document;
	/**
	 * Si le mod�le est modifi� ce bool�en est � true et � false sinon.
	 */
	private boolean modifie;
	
	/**
	 * Chargement du mod�le � partir du fichier de configuration. Utilisation de la librarie JDOM. Appelle de nombreuses sous-m�thodes de chargment.
	 * @param path Chemin vers le fichier de configuration.
	 * @see Modele#loadCouleurConfig()
	 * @see Modele#loadFormeConfig()
	 * @see Modele#loadReserveForme()
	 */
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
		setModifie(false);
	}
	
	/**
	 * R�cup�re l'instance unique du mod�le car celui-ci est en pattern singleton (instance unique r�cup�rable avec cette m�thode)
	 * @return instance unique du mod�le
	 */
	public static Modele getInstance() {
		if (instance == null) instance = new Modele(FICHIER_CONFIG);
		return instance;
	}
	
	/**
	 * M�thode de sauvegarde de la configuration courante dans le fichier de configuration xml. Utilisation de la librarie JDOM.
	 * @param path Chemin du fichier de configuration.
	 * @throws FileNotFoundException exception en cas de chemin du fichier de configuration non trouv�.
	 * @throws IOException exception lors de l'�criture dans le fichier.
	 */
	public void enregistrer(String path) throws FileNotFoundException, IOException {
		saveCouleurConfig();
		saveFormeConfig();
		saveReserveConfig();
		
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(document, new FileOutputStream(path));
        setModifie(false);
	}
	
	/**
	 * M�thode de chargment des couleurs, utilise JDOM.
	 * @see Modele#Modele(String)
	 */
	public void loadCouleurConfig() {
		List<Element> listCouleurs = racine.getChild("matrice").getChild("couleurs").getChildren("couleur");
		Iterator<Element> i = listCouleurs.iterator();
		while(i.hasNext()) {
		      Element courant = i.next();
		      this.couleursConfig.add((Couleur.values()[Integer.parseInt(courant.getText())]));
		}
	}
	
	/**
	 * M�thode de sauvegarde des couleurs, utilise JDOM.
	 * @see Modele#enregistrer(String)
	 */
	public void saveCouleurConfig() {
		Element couleurs = racine.getChild("matrice").getChild("couleurs");
		couleurs.removeChildren("couleur");
		for (Couleur c : couleursConfig) {
			Element couleur = new Element("couleur");
			couleur.setText(Integer.toString(c.ordinal()));
			couleurs.addContent(couleur);
		}
	}
	
	/**
	 * M�thode de chargment des formes, utilise JDOM.
	 * @see Modele#Modele(String)
	 */
	public void loadFormeConfig() {
		List<Element> listFormes = racine.getChild("matrice").getChild("formes").getChildren("forme");
		Iterator<Element> i = listFormes.iterator();
		while(i.hasNext()) {
		      Element courant = i.next();
		      File f = new File(Modele.DOSSIER_FORMES + courant.getText());
		      this.formesConfig.add(f);
		}
	}
	
	/**
	 * M�thode de sauvegarde des formes, utilise JDOM.
	 * @see Modele#enregistrer(String)
	 */
	public void saveFormeConfig() {
		Element formes = racine.getChild("matrice").getChild("formes");
		formes.removeChildren("forme");
		for (File f : formesConfig) {
			Element forme = new Element("forme");
			forme.setText(f.getName());
			formes.addContent(forme);
		}
	}
	
	/**
	 * M�thode de chargment des objets color�s, utilise JDOM.
	 * @see Modele#Modele(String)
	 */
	public void loadReserveForme() {
		List<Element> listObjetColore = racine.getChild("formePool").getChildren("forme");
		Iterator<Element> i = listObjetColore.iterator();
		while(i.hasNext()) {
			Element courant = i.next();
		    ObjetColore o = new ObjetColore((Couleur.values()[Integer.parseInt(courant.getChild("couleur").getText())]), new File(Modele.DOSSIER_FORMES + courant.getChild("path").getText()));
			this.reserveForme.add(o);
		}
	}
	
	/**
	 * M�thode de sauvegarde des objets color�s, utilise JDOM.
	 * @see Modele#enregistrer(String)
	 */
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
	/**
	 * Ajoute un observateur � la liste des observateurs du Modele.
	 * @param observateur Observateur � ajouter.
	 */
	public void ajoutObservateur(Observateur observateur) {
		observateurs.add(observateur);
	}

	/**
	 * Retire un observateur de la liste des observateurs du Modele.
	 * @param observateur Observateur � retirer.
	 */
	public void retireObservateur(Observateur observateur) {
		observateurs.remove(observateur);
	}
	
	/**
	 * Notifie tous les observateurs du Modele qu'une modification a �t� effectu� sur celui-ci. Cette m�thode est appel�e d�s qu'une m�thode modifie les champs de Modele.
	 * On parcourt la liste des observateur et on appelle leur m�thode actualise qui effectue des actions diff�rentes en fonction du signal envoy� et de la classe qui l'apppelle.
	 * 
	 * @param signal Un entier repr�sentant un signal sp�cifique qui permet de donner une information suppl�mentaire sur la modification qui a �t� effectu�e.
	 */
	public void notifier(int signal) {
		List<Observateur> observateurs2 = new ArrayList<Observateur>(observateurs);
		for (Observateur ob : observateurs2) {
			ob.actualise(signal);
		}
	}

	/**
	 * Accesseur de la liste des couleurs de la configuration.
	 * 
	 * @return La liste des couleurs de la configuration.
	 */
	public List<Couleur> getCouleursConfig() {
		return couleursConfig;
	}

	/**
	 * Mutateur de la liste des couleurs de la configuration.
	 * 
	 * @param couleursConfig La nouvelle liste des couleurs de la configuration.
	 * @see Modele#notifier(int)
	 */
	public void setCouleursConfig(List<Couleur> couleursConfig) {
		if (this.couleursConfig.size() != couleursConfig.size()) {	
			this.couleursConfig = couleursConfig;
			setModifie(true);
			notifier(Observateur.SIG_COLORS_UPDATE);
		}
	}
	
	/**
	 * M�thode d'ajout simple d'une couleur � la configuration.
	 * 
	 * @param couleur Couleur � ajouter dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void addCouleur(Couleur couleur) {
		if (getCouleursConfig().size() >= Modele.MAX_SELECTED_COULEURS) {
			return;
		}
		getCouleursConfig().add(0, couleur);
		setModifie(true);
		notifier(Observateur.SIG_COLORS_UPDATE);
	}

	/**
	 * M�thode de suppression simple d'une couleur � la configuration.
	 * 
	 * @param couleur Couleur � supprimer dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void removeCouleur(Couleur couleur) {
		getCouleursConfig().remove(couleur);
		setModifie(true);
		notifier(Observateur.SIG_COLORS_UPDATE);
	}

	/**
	 * Accesseur de la liste des objets color�s de la configuration.
	 * 
	 * @return La liste des objets color�s de la configuration.
	 */
	public List<ObjetColore> getReserveForme() {
		return reserveForme;
	}
	
	/**
	 * Mutateur de la liste des objets color�s de la configuration.
	 * 
	 * @param reserveForme La nouvelle liste des objets color�s de la configuration.
	 * @see Modele#notifier(int)
	 */
	public void setReserveForme(List<ObjetColore> reserveForme) {
		if (this.reserveForme.size() != reserveForme.size()) {
			this.reserveForme = reserveForme;
			setModifie(true);
			notifier(Observateur.SIG_RESERVE_UPDATE);
		}
	}

	/**
	 * M�thode d'ajout simple d'un objet color� � la configuration.
	 * 
	 * @param image Objet color� � ajouter dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void addObjetColore(ObjetColore image) {
		if (getReserveForme().size() >= Modele.MAX_RESERVE_FORMES) {
			return;
		}
		getReserveForme().add(0, image);
		setModifie(true);
		notifier(Observateur.SIG_RESERVE_UPDATE);
	}

	/**
	 * M�thode de suppression simple d'un objet color� � la configuration.
	 * 
	 * @param image Objet color� � supprimer dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void removeObjetColore(ObjetColore image) {
		getReserveForme().remove(image);
		setModifie(true);
		notifier(Observateur.SIG_RESERVE_UPDATE);
	}
	
	/**
	 * M�thode permettant de supprimer tous les objets color�s de la r�serve qui ont la m�me image (et pas forc�ment la m�me couleur).
	 * 
	 * @param image Image sous forme de fichier qui correspond � tous les objets color�s que la m�thode doit supprimer.
	 * @see Modele#notifier(int)
	 * @see Modele#setReserveForme(List)
	 */
	public void deleteObjetsColoresByImage(File image) {
		List<ObjetColore> listObjet = new ArrayList<ObjetColore>(reserveForme);
		for (ObjetColore oc : reserveForme) {
			if (oc.getOrigineFile().equals(image)) {
				listObjet.remove(oc);
			}
		}
		this.setReserveForme(listObjet);
		notifier(Observateur.SIG_IMAGE_DELETE);
	}
	
	/**
	 * Accesseur de la liste des formes de la configuration.
	 * 
	 * @return La liste des formes de la configuration.
	 */
	public List<File> getFormesConfig() {
		return formesConfig;
	}

	/**
	 * Mutateur de la liste des formes de la configuration.
	 * 
	 * @param formesConfig La nouvelle liste des formes de la configuration.
	 * @see Modele#notifier(int)
	 */
	public void setFormesConfig(List<File> formesConfig) {
		if (this.formesConfig.size() != formesConfig.size()) {
			this.formesConfig = formesConfig;
			setModifie(true);
			notifier(Observateur.SIG_FORMES_UPDATE);
		}
	}
	
	/**
	 * M�thode d'ajout simple d'une forme � la configuration.
	 * 
	 * @param image Forme � ajouter dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void addForme(File image) {
		if (getFormesConfig().size() >= Modele.MAX_SELECTED_FORMES) {
			return;
		}
		getFormesConfig().add(0, image);
		setModifie(true);
		notifier(Observateur.SIG_FORMES_UPDATE);
	}
	
	/**
	 * M�thode de suppression simple d'une forme � la configuration.
	 * 
	 * @param image Forme � supprimer dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void removeForme(File image) {
		getFormesConfig().remove(image);
		setModifie(true);
		notifier(Observateur.SIG_FORMES_UPDATE);
	}

	/**
	 * M�thode indiquant si le Modele a �t� modifi� ou pas. Il retourne le bool�en modifie.
	 * 
	 * @return Le bool�en modifie.
	 * @see Modele#modifie
	 */
	public boolean isModifie() {
		return modifie;
	}

	/**
	 * Mutateur du bool�en modifie.
	 * 
	 * @param estModifie Nouveau bool�en.
	 * @see Modele#modifie
	 */
	public void setModifie(boolean estModifie) {
		this.modifie = estModifie;
	}
}
