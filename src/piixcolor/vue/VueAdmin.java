package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.Controleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;
import piixcolor.utilitaire.Couleur;
import piixcolor.utilitaire.ImageFilter;
import piixcolor.utilitaire.Listing;
import piixcolor.utilitaire.ObjetColore;

public class VueAdmin extends Vue {

	// positions des panels
	private static final int ATP_COULEURS_PANEL_POS = 0;
	private static final int ATP_FORMES_PANEL_POS = 1;
	private static final int ATP_FORMES_SELECT_PANEL_POS = 2;

	// divers informations
	private static final int ADMIN_MARGIN = 15;
	private static final int ATP_NB_PANEL = 3;
	private static final int ABP_NB_PANEL = 3;
	private static final int NB_ADMIN_PART = 2;

	// taille des panels
	private static final int PANEL_WIDTH = Fenetre.FRAME_WIDTH / ATP_NB_PANEL
			- (ATP_NB_PANEL - 1) * ADMIN_MARGIN;
	private static final int PANEL_HEIGHT = (int) (Fenetre.FRAME_HEIGHT * 0.8);
	private static final int FORME_FRAME_HEIGHT = Fenetre.FRAME_HEIGHT / 10 + 40;

	// nom des panels
	private static final String PANEL_PRINCIPALE = "mainPanel";
	// atp (Admin Top Panel)
	private static final String ADMIN_TOP_PANEL = "adminTopPanel";
	private static final String ATP_FORMES_PANEL = "atpFormesPanel";
	private static final String ATP_COULEURS_PANEL = "atpCouleursPanel";
	private static final String ATP_FORMES_SELECT_PANEL = "atpSelectedFormesPanel";
	private static final String ATP_MESSAGE_COULEURS_PANEL = "atpMessageCouleursPanel";
	private static final String ATP_MESSAGE_FORMES_PANEL = "atpMessageFormesPanel";
	// abp (Admin Bottom Panel)
	private static final String ADMIN_BOT_PANEL = "adminBotPanel";
	private static final String ABP_COULEURS_PANEL = "abpCouleursPanel";
	private static final String ABP_FORMES_PANEL = "abpFormesPanel";
	private static final String ABP_FORMES_POOL_PANEL = "abpSelectedFormesPanel";
	private static final String ADMIN_APERCUS_PANEL = "adminApercusPanel";
	private static final String ADMIN_ACTION_PANEL = "adminActionPanel";

	private static final String IMAGE_TRASH = Modele.DOSSIER_ASSETS
			+ "corbeille.jpg";
	private static final String IMAGE_TRASH_HOVER = Modele.DOSSIER_ASSETS
			+ "corbeille-hover.jpg";
	private static final String IMAGE_CLOSE = Modele.DOSSIER_ASSETS + "close.jpg";
	private static final String IMAGE_CLOSE_HOVER = Modele.DOSSIER_ASSETS + "close-hover.jpg";

	// liste de tous les panels
	private Map<String, JPanel> panels;

	// liste de toutes les checkbox des formes
	private Map<String, JCheckBox> atpFormesCheckBoxes;
	private Map<Couleur, JCheckBox> atpCouleursCheckBoxes;
	private Map<String, JCheckBox> abpFormesCheckBoxes;
	private Map<Couleur, JCheckBox> abpCouleursCheckBoxes;

	public VueAdmin(Fenetre fenetre, Controleur controleur) {
		super(fenetre, controleur);

		getControleur().getModele().ajoutObservateur(this);

		// init lists check box
		atpFormesCheckBoxes = new LinkedHashMap<String, JCheckBox>();
		atpCouleursCheckBoxes = new HashMap<Couleur, JCheckBox>();
		abpFormesCheckBoxes = new HashMap<String, JCheckBox>();
		abpCouleursCheckBoxes = new HashMap<Couleur, JCheckBox>();

		// init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put(PANEL_PRINCIPALE, new JPanel(new GridBagLayout()));
		panels.put(ADMIN_TOP_PANEL, new JPanel(new GridLayout(1, ATP_NB_PANEL,
				ADMIN_MARGIN, 0)));
		panels.put(ATP_FORMES_PANEL, initAtpFormesPanel());
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.put(ATP_COULEURS_PANEL, initAtpCouleursPanel());
		panels.put(ADMIN_BOT_PANEL, new JPanel(new GridLayout(1, ABP_NB_PANEL,
				ADMIN_MARGIN, 0)));
		panels.put(ABP_FORMES_PANEL, initAbpFormesPanel());
		panels.put(ABP_FORMES_POOL_PANEL, initFormesPoolPanel());
		panels.put(ABP_COULEURS_PANEL, initAbpCouleursPanel());
		panels.put(ADMIN_APERCUS_PANEL, initApercusPanel());
		panels.put(ADMIN_ACTION_PANEL, initActionsPanel());

		// connection panel
		add(panels.get(PANEL_PRINCIPALE));
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_COULEURS_PANEL),
				ATP_COULEURS_PANEL_POS);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL),
				ATP_FORMES_PANEL_POS);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL),
				ATP_FORMES_SELECT_PANEL_POS);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_COULEURS_PANEL),
				ATP_COULEURS_PANEL_POS);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_PANEL),
				ATP_FORMES_PANEL_POS);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_POOL_PANEL),
				ATP_FORMES_SELECT_PANEL_POS);

		// cardlayout du panel principal
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Paramétrer le plateau",
				panels.get(ADMIN_TOP_PANEL));
		tabbedPane.addTab("Paramétrer la réserve de formes",
				panels.get(ADMIN_BOT_PANEL));
		tabbedPane.addTab("Aperçus",
				panels.get(ADMIN_APERCUS_PANEL));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		panels.get(PANEL_PRINCIPALE).add(tabbedPane, c);
		c.gridy = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(ADMIN_MARGIN, 0, 0, 0);
		panels.get(PANEL_PRINCIPALE).add(panels.get(ADMIN_ACTION_PANEL), c);

		// display panel
		commit();
	}

	private JPanel initApercusPanel() {
		JPanel container = new VuePlateau(fenetre, new PlateauControleur(getControleur().getModele()), new Dimension((int)(Fenetre.FRAME_WIDTH * 0.96), PANEL_HEIGHT));
		return container;
	}

	/**
	 * Créer un conteneur de taille fixe (PANEL_WIDTH, PANEL_HEIGHT), avec un
	 * fond blanc et une bordure noir.
	 * 
	 * @return Le conteneur (JPanel)
	 */
	private JPanel createContainer() {
		Border border = BorderFactory.createLineBorder(Color.black);

		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
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
	 * Créer un panel avec un message é l'interrieur. Le fond du panel est
	 * blanc et la couleur du texte est passé en paramétre.
	 * 
	 * @param message
	 *            Message a afficher dans le panel
	 * @péram color Couleur du message
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
				PANEL_HEIGHT - 30));
		colorsTable.setBackground(Color.WHITE);
		colorsTable.setLayout(new GridLayout(
				(int) (Couleur.values().length / 2), 2));
		return colorsTable;
	}

	/**
	 * Créer un cadre (JPanel) pour une couleur. Ce cadre est blanc. Il se
	 * compose d'un rectangle montrant la couleur et d'une checkbox. La checkbox
	 * est ajouté é la liste passé en paramétre pour qu'elle puisse
	 * étre utilisé. Un ActionListener est aussi ajouté si une action doit
	 * étre réalisé.
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
	 * Créer un cadre (JPanel) pour une forme é partir d'un fichier.
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
	 * Créer un cadre (JPanel) pour une forme é partir d'un fichier. Ce
	 * cadre posséde des actions (Possibilité de le cocher ou de le
	 * supprimer).
	 * 
	 * @param image
	 *            Fichier represetant une image
	 * @param whereSaveCheckBoxs
	 *            Map ou doit etre sauvegarder la checkbox, la clé est le nom
	 *            de l'image
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

		JPanel actionPane = createFormesActionPane(image, whereSaveCheckBoxs, ae, ml);
		container.add(actionPane, BorderLayout.LINE_END);

		return container;
	}
	
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
	 *            Map ou doit etre sauvegarder la checkbox, la clé est le nom
	 *            de l'image
	 * @param ae
	 *            ActionListener s'appliquant sur la chekcbox
	 * @param ml
	 *            MouseListener s'appliquant sur le bouton supprimer
	 * @return Un panel (JPanel) contenant les les actions
	 * 
	 * @see VueAdmin#createFormeFrame(File, Map)
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

		actionPane.add(delete, BorderLayout.CENTER);

		return actionPane;
	}
	
	private JPanel createFormesColoreesActionPane(MouseListener ml) {
		JPanel actionPane = new JPanel(new BorderLayout());
		actionPane.setBackground(Color.WHITE);

		JLabel delete = new JLabel(new ImageIcon(IMAGE_CLOSE));
		delete.setPreferredSize(new Dimension(60, 30));
		delete.setHorizontalAlignment(JLabel.CENTER);
		delete.addMouseListener(ml);

		actionPane.add(delete, BorderLayout.CENTER);

		return actionPane;
	}
	
	private JPanel createFormePoolFrame(ObjetColore obj) {
		String title = obj.getOrigineFile().getName().toUpperCase()
				.split("\\.")[0]
				+ " " + obj.getCouleur().toString();
		JPanel container = createFormeFrame(obj, title);
		return container;
	}

	private JPanel initAtpCouleursPanel() {
		JPanel container = createContainer();
		JPanel colorsTable = createColorsTable();

		ActionListener ae = new AtpCouleurListener();
		for (Couleur c : Couleur.values()) {
			colorsTable.add(createColorFrame(c, atpCouleursCheckBoxes, ae));
			if (controleur.getModele().getCouleursConfig().contains(c)) {
				atpCouleursCheckBoxes.get(c).setSelected(true);
			}
		}

		JPanel messagePanel = createMessagePane(
				"Nombre maximum de couleurs atteint.", Color.RED);
		panels.put(ATP_MESSAGE_COULEURS_PANEL, messagePanel);

		container.add(colorsTable, BorderLayout.PAGE_START);
		container.add(messagePanel, BorderLayout.PAGE_END);

		refreshAtpColorsCheckBoxs();

		return container;
	}
	
	private JPanel initAtpFormesPanel() {
		Box box = Box.createVerticalBox();

		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		ActionListener ae = new AtpFormeListener();
		for (File image : images) {
			MouseListener ml = new DeleteFormeMouseListener(image);
			box.add(createFormeFrame(image, atpFormesCheckBoxes, ae, ml));
			if (getControleur().getModele().getFormesConfig().contains(image)) {
				atpFormesCheckBoxes.get(image.getName()).setSelected(true);
			}
		}

		JScrollPane jsp = createScrollPane(box);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - 30));
		jsp.setBorder(BorderFactory.createLineBorder(Color.black));

		JButton b = new JButton("Ajouter une nouvelle image");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});

		JPanel container = createContainer();
		container.setBackground(null);
		container.setBorder(null);
		container.add(jsp, BorderLayout.NORTH);
		container.add(b, BorderLayout.SOUTH);

		return container;
	}
	
	private JPanel initSelectedFormesPanel() {
		Box box = Box.createVerticalBox();

		for (File value : getControleur().getModele().getFormesConfig()) {
			box.add(createFormeFrame(value));
		}

		JScrollPane jsp = createScrollPane(box);

		JPanel messagePanel = createMessagePane(
				"Nombre maximum de formes atteint.", Color.RED);
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panels.put(ATP_MESSAGE_FORMES_PANEL, messagePanel);

		JPanel container = createContainer();
		container.add(jsp, BorderLayout.CENTER);
		container.add(messagePanel, BorderLayout.PAGE_END);

		if (getControleur().getModele().getFormesConfig().size() == 0) {
			messagePanel = createMessagePane(
					"Vous n'avez pas encore selectionez de formes.",
					Color.BLACK);
			container.add(messagePanel, BorderLayout.CENTER);
		}

		refreshAtpFormesCheckBoxes();

		return container;
	}

	private JPanel initAbpCouleursPanel() {
		JPanel container = createContainer();
		JPanel colorsTable = createColorsTable();

		ActionListener ae = new AbpCouleurListener();
		for (Couleur c : Couleur.values()) {
			colorsTable.add(createColorFrame(c, abpCouleursCheckBoxes, ae));
		}

		container.add(colorsTable, BorderLayout.PAGE_START);

		return container;
	}
	
	private JPanel initAbpFormesPanel() {
		Box box = Box.createVerticalBox();

		ActionListener ae = new AbpFormeListener();
		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		for (File image : images) {
			MouseListener ml = new DeleteFormeMouseListener(image);
			box.add(createFormeFrame(image, abpFormesCheckBoxes, ae, ml));
		}

		JScrollPane jsp = createScrollPane(box);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - 30));
		jsp.setBorder(BorderFactory.createLineBorder(Color.black));

		JButton ajouterButton = new JButton("Ajouter la forme à la réserve");
		ajouterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Couleur selectedCouleur = null;
				for (Map.Entry<Couleur, JCheckBox> value : abpCouleursCheckBoxes
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
				for (Map.Entry<String, JCheckBox> value : abpFormesCheckBoxes
						.entrySet()) {
					if (value.getValue().isSelected()) {
						selectedForme = value.getKey();
						break;
					}
				}
				if (selectedForme == null) {
					return;
				}

				getControleur().addObjetColore(new ObjetColore(selectedCouleur, new File(
						Modele.DOSSIER_FORMES + selectedForme)));

				abpCouleursCheckBoxes.get(selectedCouleur).setSelected(false);
				abpFormesCheckBoxes.get(selectedForme).setSelected(false);
				refreshAbpColorsCheckBoxs();
				refreshAbpFormesCheckBoxs();
				refreshFormesPoolPanel();
			}
		});


		JPanel container = createContainer();
		container.setBackground(null);
		container.setBorder(null);
		container.add(jsp, BorderLayout.NORTH);
		container.add(ajouterButton, BorderLayout.SOUTH);

		refreshAbpFormesCheckBoxs();

		return container;
	}
	
	private JPanel initFormesPoolPanel() {
		Box box = Box.createVerticalBox();

		for (ObjetColore obj : getControleur().getModele().getReserveForme()) {
			box.add(createFormePoolFrame(obj));
		}

		JScrollPane jsp = createScrollPane(box);

		JPanel container = createContainer();
		container.add(jsp);

		return container;
	}

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
				fenetre.switchPanel(new VueAccueil(fenetre));
			}
		});
		container.add(bouttonRetour);

		return container;
	}

	private void refreshAtpColorsCheckBoxs() {
		List<Couleur> selectedCouleurs = new ArrayList<Couleur>();
		for (Map.Entry<Couleur, JCheckBox> value : atpCouleursCheckBoxes
				.entrySet()) {
			if (value.getValue().isSelected()) {
				selectedCouleurs.add(value.getKey());
			}
			value.getValue().setEnabled(true);
		}
		getControleur().getModele().setCouleursConfig(selectedCouleurs);

		JPanel messagePanel = panels.get(ATP_MESSAGE_COULEURS_PANEL);
		if (getControleur().getModele().getCouleursConfig().size() < Modele.MAX_SELECTED_COULEURS) {
			messagePanel.setVisible(false);
		} else {
			for (Map.Entry<Couleur, JCheckBox> value : atpCouleursCheckBoxes
					.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
			}
			messagePanel.setVisible(true);
		}
		commit();
	}
	
	private void refreshAtpFormesCheckBoxes() {
		List<File> selectedFormes = new ArrayList<File>();
		for (Map.Entry<String, JCheckBox> value : atpFormesCheckBoxes
				.entrySet()) {
			if (value.getValue().isSelected()) {
				selectedFormes.add(new File(Modele.DOSSIER_FORMES
						+ value.getKey()));
			}
			value.getValue().setEnabled(true);
		}
		getControleur().getModele().setFormesConfig(selectedFormes);

		JPanel messagePanel = panels.get(ATP_MESSAGE_FORMES_PANEL);
		if (getControleur().getModele().getFormesConfig().size() < Modele.MAX_SELECTED_FORMES) {
			messagePanel.setVisible(false);
		} else {
			for (Map.Entry<String, JCheckBox> value : atpFormesCheckBoxes
					.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
			}
			messagePanel.setVisible(true);
		}
	}
	
	private void refreshAbpColorsCheckBoxs() {
		int nbSelectedCouleurs = 0;
		for (Map.Entry<Couleur, JCheckBox> value : abpCouleursCheckBoxes
				.entrySet()) {
			value.getValue().setEnabled(true);
			if (value.getValue().isSelected()) {
				nbSelectedCouleurs++;
				break;
			}
		}
		if (nbSelectedCouleurs > 0) {
			for (Map.Entry<Couleur, JCheckBox> value : abpCouleursCheckBoxes
					.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
			}
		}
	}

	private void refreshAbpFormesCheckBoxs() {
		int nbSelectedFormes = 0;
		for (Map.Entry<String, JCheckBox> value : abpFormesCheckBoxes
				.entrySet()) {
			value.getValue().setEnabled(true);
			if (value.getValue().isSelected()) {
				nbSelectedFormes++;
				break;
			}
		}
		if (nbSelectedFormes > 0) {
			for (Map.Entry<String, JCheckBox> value : abpFormesCheckBoxes
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
						"Information",
						"Votre image a bien été enregistrée.");
				refreshAtpFormesPanel();
				refreshAtpFormesCheckBoxes();
				refreshAbpFormesPanel();
			} else {
				BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE,
						"Erreur",
						"Votre image n'a pas pu étre enregistrée.");
			}
		}
	}

	/**
	 * Actualise le panel des formes qui sert dans la création de la matrice.
	 */
	private void refreshAtpFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_PANEL));
		panels.put(ATP_FORMES_PANEL, initAtpFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL),
				ATP_FORMES_PANEL_POS);
		commit();
	}

	/**
	 * Actualise le panel des formes selectionnées.
	 */
	private void refreshSelectedFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_SELECT_PANEL));
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL),
				ATP_FORMES_SELECT_PANEL_POS);
		commit();
	}

	/**
	 * Actualise le panel des formes qui sert dans la création d'intrus.
	 */
	private void refreshAbpFormesPanel() {
		panels.get(ADMIN_BOT_PANEL).remove(panels.get(ABP_FORMES_PANEL));
		panels.put(ABP_FORMES_PANEL, initAbpFormesPanel());
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_PANEL),
				ATP_FORMES_PANEL_POS);
		commit();
	}

	/**
	 * Actualise le panel des formes présentent dans la réserve.
	 */
	private void refreshFormesPoolPanel() {
		panels.get(ADMIN_BOT_PANEL).remove(panels.get(ABP_FORMES_POOL_PANEL));
		panels.put(ABP_FORMES_POOL_PANEL, initFormesPoolPanel());
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_POOL_PANEL),
				ATP_FORMES_SELECT_PANEL_POS);
		commit();
	}

	/**
	 * Valide et repaint la vue.
	 */
	private void commit() {
		panels.get(PANEL_PRINCIPALE).revalidate();
		panels.get(PANEL_PRINCIPALE).repaint();
	}

	class AtpCouleurListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			refreshAtpColorsCheckBoxs();
		}
	}

	class AbpCouleurListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			refreshAbpColorsCheckBoxs();
		}
	}

	class AtpFormeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			refreshAtpFormesCheckBoxes();
			refreshSelectedFormesPanel();
		}
	}

	class AbpFormeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			refreshAbpFormesCheckBoxs();
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

					atpFormesCheckBoxes.remove(image.getName());
					abpFormesCheckBoxes.remove(image.getName());
					getControleur().getModele().deleteObjetsColoresByImage(
							image);
					refreshAtpFormesPanel();
					refreshAtpFormesCheckBoxes();
					refreshSelectedFormesPanel();
					refreshAbpFormesPanel();
					refreshFormesPoolPanel();

					BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
				} else {
					BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE,
							"Erreur",
							"Votre image n'a pas pu étre supprimée.");
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
			getControleur().removeObjetColore(this.image);
			refreshAbpColorsCheckBoxs();
			refreshAbpFormesCheckBoxs();
			refreshFormesPoolPanel();
		}

	}

	public void actualise(List l) {
		((VuePlateau) panels.get(ADMIN_APERCUS_PANEL)).refreshVue();
		//A FAIRE
	}

}
