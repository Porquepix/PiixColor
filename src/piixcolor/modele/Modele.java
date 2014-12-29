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
	 * Le modèle est sujet de nombreux observateurs qui sont régulièrement informés de ses modifications. Pour cela le modèle garde en mémoire la liste de tous les observateurs qui le concerne pour pouvoir les notifier.
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
	 * Liste des objets colorés qui compose la réserve de forme actuelle.
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
	 * Constante pour le nombre maximum d'objet colorés possibles dans une configuration.
	 */
	public static final int MAX_RESERVE_FORMES = 20;
	/**
	 * Chemin pour les images de tout le projet.
	 */
	public static final String DOSSIER_FORMES = "images/";
	/**
	 * Chemin pour les assets du projet (images utilisées pour les boutons etc).
	 */
	public static final String DOSSIER_ASSETS = DOSSIER_FORMES + "assets/";
	/**
	 * Extention utilisée par défaut pour sauvegarder une image dans le projet.
	 */
	public static final String FORMAT_IMAGE_SAVE = "png";
	/**
	 * Nom du fichier de configuration à utiliser pour charger et sauvegarder une configuration
	 */
	public static final String FICHIER_CONFIG = "config.xml";
	
	private static Modele instance = null;
	private Element racine;
	private Document document;
	/**
	 * Si le modèle est modifié ce booléen est à true et à false sinon.
	 */
	private boolean modifie;
	
	/**
	 * Chargement du modèle à partir du fichier de configuration. Utilisation de la librarie JDOM. Appelle de nombreuses sous-méthodes de chargment.
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
	 * Récupère l'instance unique du modèle car celui-ci est en pattern singleton (instance unique récupérable avec cette méthode)
	 * @return instance unique du modèle
	 */
	public static Modele getInstance() {
		if (instance == null) instance = new Modele(FICHIER_CONFIG);
		return instance;
	}
	
	/**
	 * Méthode de sauvegarde de la configuration courante dans le fichier de configuration xml. Utilisation de la librarie JDOM.
	 * @param path Chemin du fichier de configuration.
	 * @throws FileNotFoundException exception en cas de chemin du fichier de configuration non trouvé.
	 * @throws IOException exception lors de l'écriture dans le fichier.
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
	 * Méthode de chargment des couleurs, utilise JDOM.
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
	 * Méthode de sauvegarde des couleurs, utilise JDOM.
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
	 * Méthode de chargment des formes, utilise JDOM.
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
	 * Méthode de sauvegarde des formes, utilise JDOM.
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
	 * Méthode de chargment des objets colorés, utilise JDOM.
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
	 * Méthode de sauvegarde des objets colorés, utilise JDOM.
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
	 * Ajoute un observateur à la liste des observateurs du Modele.
	 * @param observateur Observateur à ajouter.
	 */
	public void ajoutObservateur(Observateur observateur) {
		observateurs.add(observateur);
	}

	/**
	 * Retire un observateur de la liste des observateurs du Modele.
	 * @param observateur Observateur à retirer.
	 */
	public void retireObservateur(Observateur observateur) {
		observateurs.remove(observateur);
	}
	
	/**
	 * Notifie tous les observateurs du Modele qu'une modification a été effectué sur celui-ci. Cette méthode est appelée dès qu'une méthode modifie les champs de Modele.
	 * On parcourt la liste des observateur et on appelle leur méthode actualise qui effectue des actions différentes en fonction du signal envoyé et de la classe qui l'apppelle.
	 * 
	 * @param signal Un entier représentant un signal spécifique qui permet de donner une information supplémentaire sur la modification qui a été effectuée.
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
	 * Méthode d'ajout simple d'une couleur à la configuration.
	 * 
	 * @param couleur Couleur à ajouter dans la liste.
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
	 * Méthode de suppression simple d'une couleur à la configuration.
	 * 
	 * @param couleur Couleur à supprimer dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void removeCouleur(Couleur couleur) {
		getCouleursConfig().remove(couleur);
		setModifie(true);
		notifier(Observateur.SIG_COLORS_UPDATE);
	}

	/**
	 * Accesseur de la liste des objets colorés de la configuration.
	 * 
	 * @return La liste des objets colorés de la configuration.
	 */
	public List<ObjetColore> getReserveForme() {
		return reserveForme;
	}
	
	/**
	 * Mutateur de la liste des objets colorés de la configuration.
	 * 
	 * @param reserveForme La nouvelle liste des objets colorés de la configuration.
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
	 * Méthode d'ajout simple d'un objet coloré à la configuration.
	 * 
	 * @param image Objet coloré à ajouter dans la liste.
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
	 * Méthode de suppression simple d'un objet coloré à la configuration.
	 * 
	 * @param image Objet coloré à supprimer dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void removeObjetColore(ObjetColore image) {
		getReserveForme().remove(image);
		setModifie(true);
		notifier(Observateur.SIG_RESERVE_UPDATE);
	}
	
	/**
	 * Méthode permettant de supprimer tous les objets colorés de la réserve qui ont la même image (et pas forcément la même couleur).
	 * 
	 * @param image Image sous forme de fichier qui correspond à tous les objets colorés que la méthode doit supprimer.
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
	 * Méthode d'ajout simple d'une forme à la configuration.
	 * 
	 * @param image Forme à ajouter dans la liste.
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
	 * Méthode de suppression simple d'une forme à la configuration.
	 * 
	 * @param image Forme à supprimer dans la liste.
	 * @see Modele#notifier(int)
	 */
	public void removeForme(File image) {
		getFormesConfig().remove(image);
		setModifie(true);
		notifier(Observateur.SIG_FORMES_UPDATE);
	}

	/**
	 * Méthode indiquant si le Modele a été modifié ou pas. Il retourne le booléen modifie.
	 * 
	 * @return Le booléen modifie.
	 * @see Modele#modifie
	 */
	public boolean isModifie() {
		return modifie;
	}

	/**
	 * Mutateur du booléen modifie.
	 * 
	 * @param estModifie Nouveau booléen.
	 * @see Modele#modifie
	 */
	public void setModifie(boolean estModifie) {
		this.modifie = estModifie;
	}
}
