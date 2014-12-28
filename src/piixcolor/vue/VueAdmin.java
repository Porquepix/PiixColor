package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import piixcolor.controleur.AccueilControleur;
import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;
import piixcolor.utilitaire.Couleur;
import piixcolor.utilitaire.ImageFilter;
import piixcolor.utilitaire.Listing;
import piixcolor.utilitaire.ObjetColore;
import piixcolor.utilitaire.Observateur;

@SuppressWarnings("serial")
public class VueAdmin extends Vue {

	/**
	 * Position dans les GridLayout du panel des couleurs.
	 */
	private static final int COULEURS_PANEL_POS = 0;

	/**
	 * Position dans les GridLayout du panel des formes.
	 */
	private static final int FORMES_PANEL_POS = 1;

	/**
	 * Position dans les GridLayout du panel des formes selectionées (ou des
	 * formes colorées).
	 */
	private static final int FORMES_SELECT_PANEL_POS = 2;

	/**
	 * Marge (en px) entre les differents panels.
	 */
	private static final int ADMIN_MARGIN = 15;

	/**
	 * Nombre de panels pour la partie d'administration
	 * "Configuration du plateau".
	 */
	private static final int PLATEAU_NB_PANEL = 3;

	/**
	 * Nombre de panels pour la partie d'administration
	 * "Configuration de la réserve".
	 */
	private static final int RESERVE_NB_PANEL = 3;

	/**
	 * Largeur d'un sous panel.
	 */
	private static final int PANEL_WIDTH = Fenetre.FRAME_WIDTH
			/ PLATEAU_NB_PANEL - (PLATEAU_NB_PANEL - 1) * ADMIN_MARGIN;

	/**
	 * Hauteur d'un sous panel.
	 */
	private static final int PANEL_HEIGHT = (int) (Fenetre.FRAME_HEIGHT * 0.8);

	/**
	 * Hauteur des boites contenant les formes.
	 */
	private static final int FORME_FRAME_HEIGHT = Fenetre.FRAME_HEIGHT / 10 + 40;

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel principal.
	 * 
	 * @see VueAdmin#panels
	 */
	private static final String PANEL_PRINCIPALE = "mainPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel contenant la
	 * partie d'administration "Configuration du plateau". Tous les panels qu'il
	 * contient commencent par 'AP_'.
	 * 
	 * @see VueAdmin#panels
	 */
	private static final String ADMIN_PLATEAU_PANEL = "adminPlateauPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel des formes de la
	 * partie "Configuration du plateau".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_PLATEAU_PANEL
	 */
	private static final String AP_FORMES_PANEL = "apFormesPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel des couleurs de la
	 * partie "Configuration du plateau".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_PLATEAU_PANEL
	 */
	private static final String AP_COULEURS_PANEL = "apCouleursPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel des formes
	 * sélectionées de la partie "Configuration du plateau".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_PLATEAU_PANEL
	 */
	private static final String AP_FORMES_SELECT_PANEL = "apSelectedFormesPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel contenant les
	 * messages d'erreur du panel couleurs de la partie
	 * "Configuration du plateau".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_PLATEAU_PANEL
	 */
	private static final String AP_MESSAGE_COULEURS_PANEL = "apMessageCouleursPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel contenant les
	 * messages d'erreur du panel formes de la partie
	 * "Configuration du plateau".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_PLATEAU_PANEL
	 */
	private static final String AP_MESSAGE_FORMES_PANEL = "apMessageFormesPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel contenant la
	 * partie d'administration "Configuration de la réserve". Tous les panels
	 * qu'il contient commencent par 'AR_'.
	 * 
	 * @see VueAdmin#panels
	 */
	private static final String ADMIN_RESERVE_PANEL = "adminReservePanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel des couleurs de la
	 * partie "Configuration de la réserve".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_RESERVE_PANEL
	 */
	private static final String AR_COULEURS_PANEL = "arCouleursPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel des formes de la
	 * partie "Configuration de la réserve".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_RESERVE_PANEL
	 */
	private static final String AR_FORMES_PANEL = "arFormesPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel des objets
	 * colorées de la partie "Configuration de la réserve".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_RESERVE_PANEL
	 */
	private static final String AR_FORMES_POOL_PANEL = "arSelectedFormesPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel contenants les
	 * messages d'erreur du panel objets colorées de la partie
	 * "Configuration de la réserve".
	 * 
	 * @see VueAdmin#panels
	 * @see VueAdmin#ADMIN_RESERVE_PANEL
	 */
	private static final String AR_MESSAGE_FORMES_PANEL = "arMessageFormesPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel de l'aperçus.
	 * 
	 * @see VueAdmin#panels
	 */
	private static final String ADMIN_APERCUS_PANEL = "adminApercusPanel";

	/**
	 * Nom, dans la Map reférencant tous les panels, du panel contenant les
	 * bouttons d'action de l'administration.
	 * 
	 * @see VueAdmin#panels
	 */
	private static final String ADMIN_ACTION_PANEL = "adminActionPanel";

	/**
	 * Chemin vers l'icone de la corbeille
	 */
	private static final String IMAGE_TRASH = Modele.DOSSIER_ASSETS
			+ "corbeille.jpg";

	/**
	 * Chemin vers l'icone de la corbeille quand la souris passe dessus
	 */
	private static final String IMAGE_TRASH_HOVER = Modele.DOSSIER_ASSETS
			+ "corbeille-hover.jpg";

	/**
	 * Chemin vers l'icone de la croix
	 */
	private static final String IMAGE_CLOSE = Modele.DOSSIER_ASSETS
			+ "close.jpg";

	/**
	 * Chemin vers l'icone de la croix quand la souris passe dessus
	 */
	private static final String IMAGE_CLOSE_HOVER = Modele.DOSSIER_ASSETS
			+ "close-hover.jpg";

	/**
	 * Liste contenant tous les panels de l'administration. Sert pour rafraichir
	 * les bons panels lors d'actions de l'utilisateur. La clef est une
	 * constante defini par cette classe.
	 */
	private Map<String, JPanel> panels;

	/**
	 * Liste des checkboxs de la liste des formes de la partie
	 * "Configuration du plateau". La clef est le nom du fichier
	 * (File#getName()) associé à la checkbox.
	 */
	private Map<String, JCheckBox> apFormesCheckBoxes;

	/**
	 * Liste des checkboxs de la liste des couleurs de la partie
	 * "Configuration du plateau". La clef est la couleur (Couleur) associé à la
	 * checkbox.
	 * 
	 * @see Couleur
	 */
	private Map<Couleur, JCheckBox> apCouleursCheckBoxes;

	/**
	 * Liste des checkboxs de la liste des formes de la partie
	 * "Configuration de la réserve". La clef est le nom du fichier
	 * (File#getName()) associé à la checkbox.
	 */
	private Map<String, JCheckBox> arFormesCheckBoxes;

	/**
	 * Liste des checkboxs de la liste des couleurs de la partie
	 * "Configuration de la réserve". La clef est la couleur (Couleur) associé à
	 * la checkbox.
	 * 
	 * @see Couleur
	 */
	private Map<Couleur, JCheckBox> arCouleursCheckBoxes;

	/**
	 * Construit la partie graphique de l'administration. Le constructeur
	 * initialise tout l'aspect graphique de la vue. L'initialisation repose sur
	 * des fonctions qui initialisent des plus petits bouts.
	 * 
	 * @param fenetre
	 *            Fenetre associée à la vue.
	 * @param controleur
	 *            Controlleur de la vue.
	 */
	public VueAdmin(Fenetre fenetre, AdminControleur controleur) {
		super(fenetre, controleur);

		getControleur().getModele().ajoutObservateur(this);

		// Initialisation des listes des checkboxs
		apFormesCheckBoxes = new LinkedHashMap<String, JCheckBox>();
		apCouleursCheckBoxes = new HashMap<Couleur, JCheckBox>();
		arFormesCheckBoxes = new HashMap<String, JCheckBox>();
		arCouleursCheckBoxes = new HashMap<Couleur, JCheckBox>();

		// Initialisation des composants de la vue
		panels = new HashMap<String, JPanel>();
		panels.put(PANEL_PRINCIPALE, new JPanel(new GridBagLayout()));
		panels.put(ADMIN_PLATEAU_PANEL, new JPanel(new GridLayout(1,
				PLATEAU_NB_PANEL, ADMIN_MARGIN, 0)));
		panels.put(AP_FORMES_PANEL, initApFormesPanel());
		panels.put(AP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.put(AP_COULEURS_PANEL, initApCouleursPanel());
		panels.put(ADMIN_RESERVE_PANEL, new JPanel(new GridLayout(1,
				RESERVE_NB_PANEL, ADMIN_MARGIN, 0)));
		panels.put(AR_FORMES_POOL_PANEL, initFormesPoolPanel());
		panels.put(AR_FORMES_PANEL, initArFormesPanel());
		panels.put(AR_COULEURS_PANEL, initArCouleursPanel());
		panels.put(ADMIN_APERCUS_PANEL, initApercusPanel());
		panels.put(ADMIN_ACTION_PANEL, initActionsPanel());

		// Connexion des panels entre eux
		add(panels.get(PANEL_PRINCIPALE));
		panels.get(ADMIN_PLATEAU_PANEL).add(panels.get(AP_COULEURS_PANEL),
				COULEURS_PANEL_POS);
		panels.get(ADMIN_PLATEAU_PANEL).add(panels.get(AP_FORMES_PANEL),
				FORMES_PANEL_POS);
		panels.get(ADMIN_PLATEAU_PANEL).add(panels.get(AP_FORMES_SELECT_PANEL),
				FORMES_SELECT_PANEL_POS);
		panels.get(ADMIN_RESERVE_PANEL).add(panels.get(AR_COULEURS_PANEL),
				COULEURS_PANEL_POS);
		panels.get(ADMIN_RESERVE_PANEL).add(panels.get(AR_FORMES_PANEL),
				FORMES_PANEL_POS);
		panels.get(ADMIN_RESERVE_PANEL).add(panels.get(AR_FORMES_POOL_PANEL),
				FORMES_SELECT_PANEL_POS);

		// Initialisation du panel principal
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Paramétrer le plateau",
				panels.get(ADMIN_PLATEAU_PANEL));
		tabbedPane.addTab("Paramétrer la réserve de formes",
				panels.get(ADMIN_RESERVE_PANEL));
		tabbedPane.addTab("Aperçus", panels.get(ADMIN_APERCUS_PANEL));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		panels.get(PANEL_PRINCIPALE).add(tabbedPane, c);
		c.gridy = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(ADMIN_MARGIN, 0, 0, 0);
		panels.get(PANEL_PRINCIPALE).add(panels.get(ADMIN_ACTION_PANEL), c);

		// Valide les changements de la vue et l'affiche
		commit();
	}
	
	/**
	 * Créer un conteneur de taille definie par les paramètres, avec un
	 * fond blanc et une bordure noir.
	 * 
	 * @param width Largeur du conteneur
	 * @param height Hauteur du conteneur
	 * @return Le conteneur (JPanel)
	 */
	private JPanel createContainer(int width, int height) {
		Border border = BorderFactory.createLineBorder(Color.black);

		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(width, height));
		container.setBackground(Color.WHITE);
		container.setBorder(border);

		return container;
	}

	/**
	 * Créer un panel scrollable a partir d'une box.
	 * 
	 * @param box
	 *            Box contenant les elements a scrollé
	 * @return Le panel scrollable (JScrollPane)
	 */
	private JScrollPane createScrollPane(Box box) {
		JScrollPane jsp = new JScrollPane(box,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(null);
		jsp.getViewport().setBackground(Color.WHITE);
		return jsp;
	}

	/**
	 * Créer un panel avec un message à l'interrieur. Le fond du panel est blanc
	 * et la couleur du texte est passé en paramétre.
	 * 
	 * @param message
	 *            Message a afficher dans le panel
	 * @param color
	 *            Couleur du message
	 * @return Le panel (JPanel) avec le message a l'interrieur
	 */
	private JPanel createMessagePane(String message, Color color) {
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);

		JLabel messageLabel = new JLabel(message);
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		messageLabel.setForeground(color);
		messagePanel.add(messageLabel);

		return messagePanel;
	}

	/**
	 * Créer la structure du tableau pour les panels des couleurs.
	 * 
	 * @return Le tableau (JPanel)
	 */
	private JPanel createColorsTable() {
		JPanel colorsTable = new JPanel();
		colorsTable.setPreferredSize(new Dimension(PANEL_WIDTH,
				PANEL_HEIGHT - 55));
		colorsTable.setBackground(Color.WHITE);
		colorsTable.setLayout(new GridLayout(
				(int) (Couleur.values().length / 2), 2));
		return colorsTable;
	}

	/**
	 * Créer un cadre (JPanel) pour une couleur. Ce cadre est blanc. Il se
	 * compose d'un rectangle montrant la couleur et d'une checkbox. La checkbox
	 * est ajouté à la liste passé en paramétre pour qu'elle puisse étre
	 * utilisé. Un ActionListener est aussi ajouté si une action doit étre
	 * réalisé.
	 * 
	 * @param c
	 *            Couleur correspont au cadre
	 * @param whereSaveCheckBoxs
	 *            Map ou doit etre sauvegarder la checkbox, la clé est la
	 *            couleur
	 * @param ae
	 *            ActionListener s'appliquant sur la chekcbox
	 * @return Le cadre (JPanel) de la couleur
	 */
	private JPanel createColorFrame(Couleur c,
			Map<Couleur, JCheckBox> whereSaveCheckBoxs, ActionListener ae) {
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);

		JPanel colorIcon = new JPanel();
		colorIcon.setBackground(c.getCouleur());
		colorIcon.setPreferredSize(new Dimension(50, 20));

		JCheckBox cb = new JCheckBox();
		cb.setBackground(Color.WHITE);
		cb.addActionListener(ae);

		whereSaveCheckBoxs.put(c, cb);

		container.add(colorIcon, BorderLayout.WEST);
		container.add(cb, BorderLayout.EAST);

		return container;
	}

	/**
	 * Créer un cadre (JPanel) pour une forme. Ce cadre est blanc avec un
	 * contour noir. A sa gauche se trouve l'apercu de l'image et au centre le
	 * nom du cadre.
	 * 
	 * @param image
	 *            Apercus de l'image
	 * @param title
	 *            Nom du cadre
	 * @return Un cadre (JPanel) pour une forme
	 */
	private JPanel createFormeFrame(BufferedImage image, String title) {
		Border border = BorderFactory.createLineBorder(Color.black);

		JPanel container = new JPanel();
		container.setMaximumSize(new Dimension(PANEL_WIDTH - 18,
				FORME_FRAME_HEIGHT));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		container.setLayout(new BorderLayout());

		JLabel imagePreview = new JLabel(new ImageIcon(image));
		imagePreview.setMaximumSize(new Dimension(Modele.IMG_SIZE,
				Modele.IMG_SIZE));
		imagePreview.setMinimumSize(new Dimension(Modele.IMG_SIZE,
				Modele.IMG_SIZE));
		container.add(imagePreview, BorderLayout.LINE_START);

		String name = title;
		JLabel imageName = new JLabel(name);
		imageName.setHorizontalAlignment(JLabel.CENTER);
		container.add(imageName, BorderLayout.CENTER);

		return container;
	}

	/**
	 * Créer un cadre (JPanel) pour une forme à partir d'un fichier.
	 * 
	 * @param image
	 *            Fichier represetant une image
	 * @return Un cadre (JPanel) pour une forme
	 * 
	 * @see VueAdmin#createFormeFrame(BufferedImage, String)
	 */
	public JPanel createFormeFrame(File image) {
		String title = image.getName().toUpperCase().split("\\.")[0];
		JPanel container = null;

		try {
			container = createFormeFrame(ImageIO.read(image), title);
		} catch (IOException e) {
			BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Erreur",
					"Un probléme est survenu lors du chargement des images.");
			System.exit(-1);
		}

		return container;
	}

	/**
	 * Créer un cadre (JPanel) pour une forme à partir d'un fichier. Ce cadre
	 * posséde des actions (Possibilité de le cocher ou de le supprimer).
	 * 
	 * @param image
	 *            Fichier represetant une image
	 * @param whereSaveCheckBoxs
	 *            Map ou doit etre sauvegarder la checkbox, la clé est le nom de
	 *            l'image
	 * @param ae
	 *            ActionListener s'appliquant sur la chekcbox
	 * @param ml
	 *            MouseListener s'appliquant sur le bouton supprimer
	 * @return Un cadre (JPanel) pour une forme
	 * 
	 * @see VueAdmin#createFormeFrame(File)
	 * @see VueAdmin#createFormeFrame(BufferedImage, String)
	 */
	public JPanel createFormeFrame(File image,
			Map<String, JCheckBox> whereSaveCheckBoxs, ActionListener ae,
			MouseListener ml) {
		JPanel container = createFormeFrame(image);

		JPanel actionPane = createFormesActionPane(image, whereSaveCheckBoxs,
				ae, ml);
		container.add(actionPane, BorderLayout.LINE_END);

		return container;
	}

	/**
	 * Créer un cadre (JPanel) pour une forme à partir d'un ObjetColore. Ce
	 * cadre posséde une actions (Possibilité de le supprimer).
	 * 
	 * @param obj
	 *            ObjetColore servant pour l'image du cadre
	 * @param title
	 *            Nom du cadre
	 * @return Un cadre (JPanel) pour une forme
	 * 
	 * @see VueAdmin#createFormeFrame(BufferedImage, String)
	 * @see ObjetColore
	 */
	private JPanel createFormeFrame(ObjetColore obj, String title) {
		JPanel container = createFormeFrame(obj.getImage(), title);

		MouseListener ml = new DeleteFormeColoreeMouseListener(obj);
		JPanel actionPane = createFormesColoreesActionPane(ml);
		container.add(actionPane, BorderLayout.LINE_END);

		return container;
	}

	/**
	 * Créer un panel (JPanel) des action pour un cadre de forme. Deux actions
	 * sont possible : le selectioner via une checkbox, le supprimmer via un
	 * bouton.
	 * 
	 * @param image
	 *            Fichier de l'image a supprimer si le bouton est cliqué
	 * @param whereSaveCheckBoxs
	 *            Map ou doit etre sauvegarder la checkbox, la clé est le nom de
	 *            l'image
	 * @param ae
	 *            ActionListener s'appliquant sur la chekcbox
	 * @param ml
	 *            MouseListener s'appliquant sur le bouton supprimer
	 * @return Un panel (JPanel) contenant les actions
	 * 
	 * @see VueAdmin#createFormeFrame(File, Map, ActionListener, MouseListener)
	 */
	private JPanel createFormesActionPane(File image,
			Map<String, JCheckBox> whereSaveCheckBoxs, ActionListener ae,
			MouseListener ml) {
		JPanel actionPane = new JPanel(new BorderLayout());
		actionPane.setBackground(Color.WHITE);

		JCheckBox cb;
		String imageName = image.getName();
		if (whereSaveCheckBoxs.containsKey(imageName)) {
			cb = whereSaveCheckBoxs.get(imageName);
		} else {
			cb = new JCheckBox();
			cb.setBackground(Color.WHITE);
			cb.addActionListener(ae);
			cb.setEnabled(false);
			whereSaveCheckBoxs.put(imageName, cb);
		}
		actionPane.add(cb, BorderLayout.LINE_START);

		JLabel delete = new JLabel(new ImageIcon(IMAGE_TRASH));
		delete.setPreferredSize(new Dimension(60, 30));
		delete.setHorizontalAlignment(JLabel.CENTER);
		delete.addMouseListener(ml);
		delete.setCursor(new Cursor(Cursor.HAND_CURSOR));

		actionPane.add(delete, BorderLayout.CENTER);

		return actionPane;
	}

	/**
	 * Créer un panel (JPanel) des action pour un cadre de forme colorée. Une
	 * seul action est possible : la supprimmer via un bouton.
	 * 
	 * @param ml
	 *            MouseListener s'appliquant sur le bouton supprimer
	 * @return Un panel (JPanel) contenant l'actions
	 */
	private JPanel createFormesColoreesActionPane(MouseListener ml) {
		JPanel actionPane = new JPanel(new BorderLayout());
		actionPane.setBackground(Color.WHITE);

		JLabel delete = new JLabel(new ImageIcon(IMAGE_CLOSE));
		delete.setPreferredSize(new Dimension(60, 30));
		delete.setHorizontalAlignment(JLabel.CENTER);
		delete.addMouseListener(ml);
		delete.setCursor(new Cursor(Cursor.HAND_CURSOR));

		actionPane.add(delete, BorderLayout.CENTER);

		return actionPane;
	}

	/**
	 * Créer un cadre (JPanel) pour une forme colorée à partir d'un ObjetColore.
	 * Ce cadre posséde une actions (Possibilité de le supprimer).
	 * 
	 * @param obj
	 *            ObjetColore servant pour l'image du cadre
	 * @return Un cadre (JPanel) pour une forme
	 * 
	 * @see VueAdmin#createFormeFrame(ObjetColore, String)
	 * @see ObjetColore
	 */
	private JPanel createFormePoolFrame(ObjetColore obj) {
		String title = obj.getOrigineFile().getName().toUpperCase()
				.split("\\.")[0]
				+ " " + obj.getCouleur().toString();
		JPanel container = createFormeFrame(obj, title);
		return container;
	}

	/**
	 * Initialisation du panel des couleurs de la partie "Configuration du plateau".
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initApCouleursPanel() {
		JPanel colorsContainer = createContainer(PANEL_WIDTH, PANEL_HEIGHT - 25);
		JPanel colorsTable = createColorsTable();

		ActionListener ae = null;
		for (Couleur c : Couleur.values()) {
			ae = new AtpCouleurListener(c);
			colorsTable.add(createColorFrame(c, apCouleursCheckBoxes, ae));
		}

		JPanel messagePanel = createMessagePane(
				"Nombre maximum de couleurs atteint.", Color.RED);
		panels.put(AP_MESSAGE_COULEURS_PANEL, messagePanel);
		
		colorsContainer.add(colorsTable, BorderLayout.PAGE_START);
		colorsContainer.add(messagePanel, BorderLayout.PAGE_END);
		
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		container.add(new JLabel("Couleurs dans le tableau"), BorderLayout.PAGE_START);
		container.add(colorsContainer, BorderLayout.PAGE_END);

		refreshApColorsCheckBoxs();

		return container;
	}

	/**
	 * Initialisation du panel des formes de la partie "Configuration du plateau".
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initApFormesPanel() {
		Box box = Box.createVerticalBox();

		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		ActionListener ae = null;
		MouseListener ml = null;
		for (File image : images) {
			ae = new AtpFormeListener(image);
			ml = new DeleteFormeMouseListener(image);
			box.add(createFormeFrame(image, apFormesCheckBoxes, ae, ml));
		}

		JScrollPane jsp = createScrollPane(box);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - 60));
		jsp.setBorder(BorderFactory.createLineBorder(Color.black));

		JButton b = new JButton("Ajouter une nouvelle image");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});

		JPanel formesContainer = createContainer(PANEL_WIDTH, PANEL_HEIGHT - 30);
		formesContainer.setBackground(null);
		formesContainer.setBorder(null);
		formesContainer.add(jsp, BorderLayout.NORTH);
		formesContainer.add(b, BorderLayout.SOUTH);
		
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		container.add(new JLabel("Formes dans le tableau"), BorderLayout.PAGE_START);
		container.add(formesContainer, BorderLayout.PAGE_END);

		return container;
	}

	/**
	 * Initialisation du panel des formes sélectionnées de la partie "Configuration du plateau".
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initSelectedFormesPanel() {
		Box box = Box.createVerticalBox();

		for (File value : getControleur().getModele().getFormesConfig()) {
			box.add(createFormeFrame(value));
		}

		JScrollPane jsp = createScrollPane(box);

		JPanel messagePanel = createMessagePane(
				"Nombre maximum de formes atteint.", Color.RED);
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panels.put(AP_MESSAGE_FORMES_PANEL, messagePanel);

		JPanel selectedFormesContainer = createContainer(PANEL_WIDTH, PANEL_HEIGHT - 25);
		selectedFormesContainer.add(jsp, BorderLayout.CENTER);
		selectedFormesContainer.add(messagePanel, BorderLayout.PAGE_END);

		if (getControleur().getModele().getFormesConfig().size() == 0) {
			messagePanel = createMessagePane(
					"Vous n'avez pas encore selectionez de formes.",
					Color.BLACK);
			selectedFormesContainer.add(messagePanel, BorderLayout.CENTER);
		}
		
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		container.add(new JLabel("Récapitulatif des formes dans le tableau"), BorderLayout.PAGE_START);
		container.add(selectedFormesContainer, BorderLayout.PAGE_END);

		refreshApFormesCheckBoxes();
		
		return container;
	}

	/**
	 * Initialisation du panel des couleurs de la partie "Configuration de la réserve".
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initArCouleursPanel() {
		JPanel colorsContainer = createContainer(PANEL_WIDTH, PANEL_HEIGHT - 25);
		JPanel colorsTable = createColorsTable();

		ActionListener ae = new AbpCouleurListener();
		for (Couleur c : Couleur.values()) {
			colorsTable.add(createColorFrame(c, arCouleursCheckBoxes, ae));
		}

		colorsContainer.add(colorsTable, BorderLayout.PAGE_START);
		
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		container.add(new JLabel("Sélectionnez une couleur"), BorderLayout.PAGE_START);
		container.add(colorsContainer, BorderLayout.PAGE_END);

		return container;
	}

	/**
	 * Initialisation du panel des formes de la partie "Configuration de la réserve".
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initArFormesPanel() {
		Box box = Box.createVerticalBox();

		ActionListener ae = new AbpFormeListener();
		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		for (File image : images) {
			MouseListener ml = new DeleteFormeMouseListener(image);
			box.add(createFormeFrame(image, arFormesCheckBoxes, ae, ml));
		}

		JScrollPane jsp = createScrollPane(box);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - 60));
		jsp.setBorder(BorderFactory.createLineBorder(Color.black));

		JButton ajouterButton = new JButton("Ajouter la forme colorée à la réserve");
		ajouterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Couleur selectedCouleur = null;
				for (Map.Entry<Couleur, JCheckBox> value : arCouleursCheckBoxes
						.entrySet()) {
					if (value.getValue().isSelected()) {
						selectedCouleur = value.getKey();
						break;
					}
				}
				if (selectedCouleur == null) {
					return;
				}

				String selectedForme = null;
				for (Map.Entry<String, JCheckBox> value : arFormesCheckBoxes
						.entrySet()) {
					if (value.getValue().isSelected()) {
						selectedForme = value.getKey();
						break;
					}
				}
				if (selectedForme == null) {
					return;
				}
				
				arCouleursCheckBoxes.get(selectedCouleur).setSelected(false);
				arFormesCheckBoxes.get(selectedForme).setSelected(false);

				getControleur().getModele().addObjetColore(
						new ObjetColore(selectedCouleur, new File(
								Modele.DOSSIER_FORMES + selectedForme)));
			}
		});

		JPanel formesContainer = createContainer(PANEL_WIDTH, PANEL_HEIGHT - 30);
		formesContainer.setBackground(null);
		formesContainer.setBorder(null);
		formesContainer.add(jsp, BorderLayout.NORTH);
		formesContainer.add(ajouterButton, BorderLayout.SOUTH);
		
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		container.add(new JLabel("Sélectionnez une forme"), BorderLayout.PAGE_START);
		container.add(formesContainer, BorderLayout.PAGE_END);

		refreshArFormesCheckBoxs();

		return container;
	}

	/**
	 * Initialisation du panel des objets colorées de la partie "Configuration de la réserve".
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initFormesPoolPanel() {
		Box box = Box.createVerticalBox();

		int i = 0;
		for (ObjetColore obj : getControleur().getModele().getReserveForme()) {
			JPanel frame = createFormePoolFrame(obj);
			box.add(frame);
			if (i >= getControleur().getMaxObjColore()) {
				JLabel message = new JLabel(" Objet non affichable");
				message.setForeground(Color.RED);
				frame.add(message, BorderLayout.PAGE_END);
			}
			i++;
		}

		JScrollPane jsp = createScrollPane(box);

		JPanel messagePanel = createMessagePane(
				"Nombre maximum de formes dans la réserve atteint.", Color.RED);
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panels.put(AR_MESSAGE_FORMES_PANEL, messagePanel);

		JPanel formesColoreesContainer = createContainer(PANEL_WIDTH, PANEL_HEIGHT - 25);
		formesColoreesContainer.add(jsp, BorderLayout.CENTER);
		formesColoreesContainer.add(messagePanel, BorderLayout.PAGE_END);
		
		
		JPanel container = new JPanel();
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		container.add(new JLabel("Formes colorées dans la réserve"), BorderLayout.PAGE_START);
		container.add(formesColoreesContainer, BorderLayout.PAGE_END);

		refreshArFormesCheckBoxs();

		return container;
	}

	/**
	 * Initialisation du panel contenant les actions possible dans l'administration (Sauvegarder et Retour accueil)
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initActionsPanel() {
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));

		// Boutton Sauvegarder
		JButton bouttonSauvegarder = new JButton("Sauvegarder");
		bouttonSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
			}
		});
		container.add(bouttonSauvegarder);

		container.add(Box.createRigidArea(new Dimension(10, 0)));

		container.add(Box.createRigidArea(new Dimension(10, 0)));

		// Boutton Retour
		JButton bouttonRetour = new JButton("Retour accueil");
		bouttonRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getControleur().getModele().retireObservateur(getView());
				fenetre.switchPanel(new VueAccueil(fenetre,
						new AccueilControleur(Modele.getInstance())));
			}
		});
		container.add(bouttonRetour);

		return container;
	}

	/**
	 * Initialisation du panel génerant l'aperçus de la configuration.
	 * 
	 * @return Le panel (JPanel)
	 */
	private JPanel initApercusPanel() {
		JPanel container = new VuePlateau(fenetre, new PlateauControleur(
				getControleur().getModele()), new Dimension(
				(int) (Fenetre.FRAME_WIDTH * 0.96), PANEL_HEIGHT));
		return container;
	}

	/**
	 * Actualise les checkboxs du panel des couleurs de la partie "Configuration du plateau".
	 */
	private void refreshApColorsCheckBoxs() {
		int nbCouleur = getControleur().getNbCouleur();
		for (Map.Entry<Couleur, JCheckBox> value : apCouleursCheckBoxes
				.entrySet()) {
			if (nbCouleur < Modele.MAX_SELECTED_COULEURS) {
				value.getValue().setEnabled(true);
			} else {
				value.getValue().setEnabled(false);
			}
		}

		for (Couleur c : getControleur().getModele().getCouleursConfig()) {
			apCouleursCheckBoxes.get(c).setSelected(true);
			apCouleursCheckBoxes.get(c).setEnabled(true);
		}

		JPanel messagePanel = panels.get(AP_MESSAGE_COULEURS_PANEL);
		if (nbCouleur < Modele.MAX_SELECTED_COULEURS) {
			messagePanel.setVisible(false);
		} else {
			messagePanel.setVisible(true);
		}
	}

	/**
	 * Actualise les checkboxs du panel des formes de la partie "Configuration du plateau".
	 */
	private void refreshApFormesCheckBoxes() {
		int nbForme = getControleur().getNbForme();

		for (Entry<String, JCheckBox> value : apFormesCheckBoxes.entrySet()) {
			if (nbForme < Modele.MAX_SELECTED_FORMES) {
				value.getValue().setEnabled(true);
			} else {
				value.getValue().setEnabled(false);
			}
		}

		for (File f : getControleur().getModele().getFormesConfig()) {
			apFormesCheckBoxes.get(f.getName()).setSelected(true);
			apFormesCheckBoxes.get(f.getName()).setEnabled(true);
		}

		JPanel messagePanel = panels.get(AP_MESSAGE_FORMES_PANEL);
		if (nbForme < Modele.MAX_SELECTED_FORMES) {
			messagePanel.setVisible(false);
		} else {
			messagePanel.setVisible(true);
		}
	}

	/**
	 * Actualise les checkboxs du panel des couleurs de la partie "Configuration due la réserve".
	 */
	private void refreshArColorsCheckBoxs() {
		int nbSelectedCouleurs = 0;
		if (getControleur().getNbObjetColore() >= getControleur().getMaxObjColore()) {
			nbSelectedCouleurs = 1;
		} else {
			for (Map.Entry<Couleur, JCheckBox> value : arCouleursCheckBoxes
					.entrySet()) {
				value.getValue().setEnabled(true);
				if (value.getValue().isSelected()) {
					nbSelectedCouleurs++;
					break;
				}
			}
		}
		if (nbSelectedCouleurs > 0) {
			for (Map.Entry<Couleur, JCheckBox> value : arCouleursCheckBoxes
					.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
			}
		}
	}

	/**
	 * Actualise les checkboxs du panel des formes de la partie "Configuration due la réserve".
	 */
	private void refreshArFormesCheckBoxs() {
		int nbSelectedFormes = 0;
		if (getControleur().getNbObjetColore() >= getControleur().getMaxObjColore()) {
			nbSelectedFormes = 1;
			panels.get(AR_MESSAGE_FORMES_PANEL).setVisible(true);
		} else {
			for (Map.Entry<String, JCheckBox> value : arFormesCheckBoxes
					.entrySet()) {
				value.getValue().setEnabled(true);
				if (value.getValue().isSelected()) {
					nbSelectedFormes++;
					break;
				}
			}
			panels.get(AR_MESSAGE_FORMES_PANEL).setVisible(false);
		}
		if (nbSelectedFormes > 0) {
			for (Map.Entry<String, JCheckBox> value : arFormesCheckBoxes
					.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
			}
		}
	}

	/**
	 * Ouvre un explorateur de fichier qui prend en compte uniquement les
	 * images. Si une image est selectioné l'enregistre en fesant appelle au
	 * controlleur.
	 * 
	 * @see ImageFilter
	 * @see AdminControleur#saveImage(File)
	 */
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Ajouter une image");
		fc.setApproveButtonText("Ajouter");

		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new ImageFilter());

		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			boolean saveStatut = ((AdminControleur) getControleur())
					.saveImage(fc.getSelectedFile());
			if (saveStatut) {
				BoiteDialogue.createModalBox(JOptionPane.INFORMATION_MESSAGE,
						"Information", "Votre image a bien été enregistrée.");
			} else {
				BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE,
						"Erreur", "Votre image n'a pas pu étre enregistrée.");
			}
		}
	}

	/**
	 * Actualise le panel des formes qui sert dans la création de la matrice.
	 */
	private void refreshApFormesPanel() {
		panels.get(ADMIN_PLATEAU_PANEL).remove(panels.get(AP_FORMES_PANEL));
		panels.put(AP_FORMES_PANEL, initApFormesPanel());
		panels.get(ADMIN_PLATEAU_PANEL).add(panels.get(AP_FORMES_PANEL),
				FORMES_PANEL_POS);
		commit();
	}

	/**
	 * Actualise le panel des formes selectionnées.
	 */
	private void refreshSelectedFormesPanel() {
		panels.get(ADMIN_PLATEAU_PANEL).remove(
				panels.get(AP_FORMES_SELECT_PANEL));
		panels.put(AP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.get(ADMIN_PLATEAU_PANEL).add(panels.get(AP_FORMES_SELECT_PANEL),
				FORMES_SELECT_PANEL_POS);
		commit();
	}

	/**
	 * Actualise le panel des formes qui sert dans la création d'intrus.
	 */
	private void refreshArFormesPanel() {
		panels.get(ADMIN_RESERVE_PANEL).remove(panels.get(AR_FORMES_PANEL));
		panels.put(AR_FORMES_PANEL, initArFormesPanel());
		panels.get(ADMIN_RESERVE_PANEL).add(panels.get(AR_FORMES_PANEL),
				FORMES_PANEL_POS);
		commit();
	}

	/**
	 * Actualise le panel des formes présentent dans la réserve.
	 */
	private void refreshFormesPoolPanel() {
		panels.get(ADMIN_RESERVE_PANEL)
				.remove(panels.get(AR_FORMES_POOL_PANEL));
		panels.put(AR_FORMES_POOL_PANEL, initFormesPoolPanel());
		panels.get(ADMIN_RESERVE_PANEL).add(panels.get(AR_FORMES_POOL_PANEL),
				FORMES_SELECT_PANEL_POS);
		commit();
	}

	/**
	 * Valide et repaint la vue.
	 */
	private void commit() {
		panels.get(PANEL_PRINCIPALE).revalidate();
		panels.get(PANEL_PRINCIPALE).repaint();
	}
	
	/**
	 * Acutalise la vue en fonction des evénements recus (signaux).
	 * Fonction actualise du pattern Observer.
	 * 
	 * @see Observateur
	 */
	public void actualise(int sig) {
		((VuePlateau) panels.get(ADMIN_APERCUS_PANEL)).refreshVue();

		if (sig == SIG_COLORS_UPDATE) {
			refreshApColorsCheckBoxs();
		}

		if (sig == SIG_FORMES_UPDATE) {
			refreshApFormesPanel();
			refreshApFormesCheckBoxes();
			refreshSelectedFormesPanel();
		}

		if (sig == SIG_RESERVE_UPDATE || sig == SIG_FORMES_UPDATE || sig == SIG_COLORS_UPDATE) {
			refreshArColorsCheckBoxs();
			refreshArFormesCheckBoxs();
			refreshFormesPoolPanel();
		}

		if (sig == SIG_IMAGE_DELETE) {
			refreshArFormesPanel();
		}

		if (sig == SIG_IMAGE_SAVE) {
			refreshApFormesPanel();
			refreshApFormesCheckBoxes();
			refreshArFormesPanel();
		}

	}

	class AtpCouleurListener implements ActionListener {

		private Couleur c;

		public AtpCouleurListener(Couleur c) {
			this.c = c;
		}

		public void actionPerformed(ActionEvent e) {
			if (((JCheckBox) e.getSource()).isSelected()) {
				getControleur().getModele().addCouleur(c);
			} else {
				getControleur().getModele().removeCouleur(c);
			}
		}
	}

	class AbpCouleurListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			refreshArColorsCheckBoxs();
		}
	}

	class AtpFormeListener implements ActionListener {

		private File image;

		public AtpFormeListener(File image) {
			this.image = image;
		}

		public void actionPerformed(ActionEvent e) {
			if (((JCheckBox) e.getSource()).isSelected()) {
				getControleur().getModele().addForme(image);
			} else {
				getControleur().getModele().removeForme(image);
			}
		}
	}

	class AbpFormeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			refreshArFormesCheckBoxs();
		}
	}

	class DeleteFormeMouseListener implements MouseListener {

		private File image;

		public DeleteFormeMouseListener(File imageToDelete) {
			this.image = imageToDelete;
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_TRASH));
		}

		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_TRASH_HOVER));
		}

		public void mouseClicked(MouseEvent e) {
			String[] options = { "Oui", "Non" };
			int retour = BoiteDialogue
					.createOptionBox(
							JOptionPane.YES_NO_OPTION,
							"Confirmation",
							"Voulez-vous vraiment supprimer cette image ? (Si oui, la configuration va étre automatiquement sauvegardée)",
							options, 1);
			if (retour == JOptionPane.OK_OPTION) {
				boolean deleteStatut = ((AdminControleur) getControleur())
						.deleteImage(image);
				if (deleteStatut) {
					BoiteDialogue
							.createModalBox(
									JOptionPane.INFORMATION_MESSAGE,
									"Information",
									"Votre image a bien été supprimée. La configuration va étre automatiquement sauvegardée.");

					apFormesCheckBoxes.remove(image.getName());
					arFormesCheckBoxes.remove(image.getName());
					getControleur().getModele().deleteObjetsColoresByImage(
							image);
					getControleur().getModele().removeForme(image);
					BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
				} else {
					BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE,
							"Erreur", "Votre image n'a pas pu étre supprimée.");
				}
			}
		}

	}

	class DeleteFormeColoreeMouseListener implements MouseListener {

		private ObjetColore image;

		public DeleteFormeColoreeMouseListener(ObjetColore imageToDelete) {
			this.image = imageToDelete;
		}

		public void mouseReleased(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_CLOSE));
		}

		public void mouseEntered(MouseEvent e) {
			((JLabel) e.getSource()).setIcon(new ImageIcon(IMAGE_CLOSE_HOVER));
		}

		public void mouseClicked(MouseEvent e) {
			getControleur().getModele().removeObjetColore(this.image);
		}

	}
	
}
