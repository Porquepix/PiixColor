package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import javax.swing.UIManager;
import javax.swing.border.Border;

import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.Controleur;
import piixcolor.modele.Modele;
import piixcolor.utilitaire.Couleur;
import piixcolor.utilitaire.ImageFilter;
import piixcolor.utilitaire.Listing;
import piixcolor.utilitaire.ObjetColore;

public class VueAdmin extends Vue {
	
	//positions des panels
	private static final int ATP_COULEURS_PANEL_POS = 0;
	private static final int ATP_FORMES_PANEL_POS = 1;
	private static final int ATP_FORMES_SELECT_PANEL_POS = 2;
	
	//divers informations
	private static final int ADMIN_MARGIN = 15;
	private static final int ATP_NB_PANEL = 3;
	private static final int ABP_NB_PANEL = 3;
	private static final int NB_ADMIN_PART = 2;
	
	//taille des panels
	private static final int PANEL_WIDTH = Fenetre.FRAME_WIDTH / ATP_NB_PANEL - (ATP_NB_PANEL - 1) * ADMIN_MARGIN;
	private static final int PANEL_HEIGHT = Fenetre.FRAME_HEIGHT / NB_ADMIN_PART - NB_ADMIN_PART * ADMIN_MARGIN - 30;
	private static final int FORME_FRAME_HEIGHT = Fenetre.FRAME_HEIGHT / 10 + 40;
	
	//nom des panels
	private static final String PANEL_PRINCIPALE = "mainPanel";
	//atp (Admin Top Panel)
	private static final String ADMIN_TOP_PANEL =  "adminTopPanel";
	private static final String ATP_FORMES_PANEL =  "atpFormesPanel";
	private static final String ATP_COULEURS_PANEL =  "atpCouleursPanel";
	private static final String ATP_FORMES_SELECT_PANEL =  "atpSelectedFormesPanel";
	private static final String ATP_MESSAGE_COULEURS_PANEL =  "atpMessageCouleursPanel";
	private static final String ATP_MESSAGE_FORMES_PANEL = "atpMessageFormesPanel";
	//abp (Admin Bottom Panel)
	private static final String ADMIN_BOT_PANEL =  "adminBotPanel";
	private static final String ABP_COULEURS_PANEL =  "abpCouleursPanel";
	private static final String ABP_FORMES_PANEL =  "abpFormesPanel";
	private static final String ABP_FORMES_POOL_PANEL =  "abpSelectedFormesPanel";
	private static final String ADMIN_ACTION_PANEL = "adminActionPanel";
	
	//liste de tous les panels
	private Map<String, JPanel> panels;
	
	//liste de toutes les checkbox des formes
	private Map<String, JCheckBox> atpFormesCheckBoxes;
	private Map<Couleur, JCheckBox> atpCouleursCheckBoxes;
	private Map<String, JCheckBox> abpFormesCheckBoxes;
	private Map<Couleur, JCheckBox> abpCouleursCheckBoxes;

	public VueAdmin(Fenetre fenetre, Controleur controleur) {
		super(fenetre, controleur);
		
		//TEST^^
		/*
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {}
		*/

		//init lists check box
		atpFormesCheckBoxes = new LinkedHashMap<String, JCheckBox>();
		atpCouleursCheckBoxes = new HashMap<Couleur, JCheckBox>();
		abpFormesCheckBoxes = new HashMap<String, JCheckBox>();
		abpCouleursCheckBoxes = new HashMap<Couleur, JCheckBox>();

		//init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put(PANEL_PRINCIPALE, new JPanel(new GridBagLayout()));
		panels.put(ADMIN_TOP_PANEL, new JPanel(new GridLayout(1, ATP_NB_PANEL, ADMIN_MARGIN, 0)));
		panels.put(ATP_FORMES_PANEL, initAtpFormesPanel());
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.put(ATP_COULEURS_PANEL, initAtpCouleursPanel());
		panels.put(ADMIN_BOT_PANEL, new JPanel(new GridLayout(1, ABP_NB_PANEL, ADMIN_MARGIN, 0)));
		panels.put(ABP_FORMES_PANEL, initAbpFormesPanel());
		panels.put(ABP_FORMES_POOL_PANEL, initFormesPoolPanel());
		panels.put(ABP_COULEURS_PANEL, initAbpCouleursPanel());
		panels.put(ADMIN_ACTION_PANEL, initActionsPanel());
		
		//connection panel
		add(panels.get(PANEL_PRINCIPALE));
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_COULEURS_PANEL), ATP_COULEURS_PANEL_POS);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL), ATP_FORMES_PANEL_POS);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_POS);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_COULEURS_PANEL), ATP_COULEURS_PANEL_POS);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_PANEL), ATP_FORMES_PANEL_POS);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_POOL_PANEL), ATP_FORMES_SELECT_PANEL_POS);
		
		//gridbag du panel principal
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		panels.get(PANEL_PRINCIPALE).add(panels.get(ADMIN_TOP_PANEL), c);
		c.gridy = 1;
		c.insets = new Insets(ADMIN_MARGIN,0,0,0);
		panels.get(PANEL_PRINCIPALE).add(panels.get(ADMIN_BOT_PANEL), c);
		c.gridy = 2;
		c.anchor = GridBagConstraints.PAGE_START;
		c.insets = new Insets(ADMIN_MARGIN,0,0,0);
		panels.get(PANEL_PRINCIPALE).add(panels.get(ADMIN_ACTION_PANEL), c);
		
		//display panel
		commit();
	}
	
	/**
	 * Crée un conteneur de taille fixe
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
	 * Crée la structure du tableau pour les panel des couleurs
	 * 
	 * @return Le tableau (JPanel)
	 */
	private JPanel createColorsTable() {
		JPanel colorsTable = new JPanel();
		colorsTable.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - 30));
		colorsTable.setBackground(Color.WHITE);
		colorsTable.setLayout(new GridLayout(5, 2));
		return colorsTable;
	}
	
	/**
	 * Crée un panel scrollable a  partir d'une box
	 * 
	 * @param box Box contenant les elements a scrollé
	 * @return Un JScrollPane
	 */
	private JScrollPane createScrollPane(Box box) {
		JScrollPane jsp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(null);
		jsp.getViewport().setBackground(Color.WHITE);
		return jsp;
	}

	private JPanel initActionsPanel() {
		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		
		//Boutton Sauvegarder
		JButton bouttonSauvegarder = new JButton("Sauvegarder");
		bouttonSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
			}
		});
		container.add(bouttonSauvegarder);
		
		container.add(Box.createRigidArea(new Dimension(10, 0)));
		
		//Boutton Apercu
		JButton bouttonApercu = new JButton("Aperçu");
		bouttonApercu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		container.add(bouttonApercu);
		
		container.add(Box.createRigidArea(new Dimension(10, 0)));
		
		//Boutton Retour
		JButton bouttonRetour = new JButton("Retour accueil");
		bouttonRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenetre.switchPanel(new VueAccueil(fenetre));
			}
		});
		container.add(bouttonRetour);
		
		return container;
	}
	
	private JPanel initFormesPoolPanel() {
		Box box = Box.createVerticalBox();

		for (ObjetColore obj : getControleur().getModele().getReserveForme()) {
			box.add(formePoolFrame(obj));
		}
		
		JScrollPane jsp = createScrollPane(box);
		
		JPanel container = createContainer();
		container.add(jsp);
		
		return container;
	}

	private JPanel formePoolFrame(ObjetColore obj) {
		Border border = BorderFactory.createLineBorder(Color.black);
		
		JPanel container = new JPanel();
		container.setMaximumSize(new Dimension(PANEL_WIDTH - 18, FORME_FRAME_HEIGHT));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		container.setLayout(new BorderLayout());

		JLabel imagePreview = new JLabel(new ImageIcon(obj.getImage()));
		imagePreview.setMaximumSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		imagePreview.setMinimumSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		container.add(imagePreview, BorderLayout.LINE_START);
		
		String name = obj.getOrigineFile().getName().toUpperCase().split("\\.")[0] + " " + obj.getCouleur().toString();
		JLabel imageName = new JLabel(name);
		imageName.setHorizontalAlignment(JLabel.CENTER);
		container.add(imageName, BorderLayout.CENTER);
		
		return container;
	}

	private JPanel initAbpFormesPanel() {
		Box box = Box.createVerticalBox();

		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		for (File image : images) {
			box.add(formeFrame(image, true, abpFormesCheckBoxes));
		}

		JScrollPane jsp = createScrollPane(box);
		jsp.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT - 30));
		jsp.setBorder(BorderFactory.createLineBorder(Color.black));

		GridLayout grid = new GridLayout(1, 2);
		grid.setHgap(ADMIN_MARGIN);
		JPanel buttonPanel = new JPanel(grid);
		JButton ajouterButton = new JButton("Ajouter la forme au bac");
		ajouterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Couleur selectedCouleur = null;
				for (Map.Entry<Couleur, JCheckBox> value : abpCouleursCheckBoxes.entrySet()) {
					if (value.getValue().isSelected()) {
						selectedCouleur = value.getKey();
						break;
					}
				}
				if (selectedCouleur == null) {
					return;
				}
				
				String selectedForme = null;
				for (Map.Entry<String, JCheckBox> value : abpFormesCheckBoxes.entrySet()) {
					if (value.getValue().isSelected()) {
						selectedForme = value.getKey();
						break;
					}
				}
				if (selectedForme == null) {
					return;
				}
				
				List<ObjetColore> obj = new ArrayList<ObjetColore>();
				for (ObjetColore oc : getControleur().getModele().getReserveForme()) {
					obj.add(oc);
				}
				obj.add(new ObjetColore(selectedCouleur, new File(Modele.DOSSIER_FORMES + selectedForme)));
				getControleur().getModele().setReserveForme(obj);
				
				abpCouleursCheckBoxes.get(selectedCouleur).setSelected(false);
				abpFormesCheckBoxes.get(selectedForme).setSelected(false);
				refreshAbpColorsCheckBoxs();
				refreshAbpFormesCheckBoxs();
				refreshFormesPoolPanel();
			}
		});
		buttonPanel.add(ajouterButton, 0);
		JButton supprimerButton = new JButton("Supprimer la forme du bac");
		supprimerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		buttonPanel.add(supprimerButton, 1);
		
		JPanel container = createContainer();
		container.setBackground(null);
		container.setBorder(null);
		container.add(jsp, BorderLayout.NORTH);
		container.add(buttonPanel, BorderLayout.SOUTH); 
		
		refreshAbpFormesCheckBoxs();
		
		return container;
	}
	
	private JPanel initSelectedFormesPanel() {
		Box box = Box.createVerticalBox();
		
		for (File value : getControleur().getModele().getFormesConfig()) {
			box.add(formeFrame(value, false, null));
	    }

		JScrollPane jsp = createScrollPane(box);
		
		JPanel messagePanel = createMessagePanel("Nombre maximum de formes atteint.");
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panels.put(ATP_MESSAGE_FORMES_PANEL, messagePanel);
		
		JPanel container = createContainer();
		container.add(jsp, BorderLayout.CENTER);
		container.add(messagePanel, BorderLayout.PAGE_END);

		if (getControleur().getModele().getFormesConfig().size() == 0) {
			JLabel message = new JLabel(" Vous n'avez pas encore selectionez de formes.");
			message.setHorizontalAlignment(JLabel.CENTER);
			container.add(message, BorderLayout.CENTER);
		}
		
		refreshAtpFormesCheckBoxes();
		
		return container;
	}

	private JPanel initAtpCouleursPanel() {
		JPanel container = createContainer();
		JPanel colorsTable = createColorsTable();

		for (Couleur c : Couleur.values()) {
			colorsTable.add(colorFrame(c, atpCouleursCheckBoxes));
			if (controleur.getModele().getCouleursConfig().contains(c)) {
				atpCouleursCheckBoxes.get(c).setSelected(true);
			}
		}
		
		JPanel messagePanel = createMessagePanel("Nombre maximum de couleurs atteint.");
		panels.put(ATP_MESSAGE_COULEURS_PANEL, messagePanel);
		
		container.add(colorsTable, BorderLayout.PAGE_START);
		container.add(messagePanel, BorderLayout.PAGE_END);
		
		refreshAtpColorsCheckBoxs();
		
		return container;
	}
	
	private JPanel initAbpCouleursPanel() {
		JPanel container = createContainer();
		JPanel colorsTable = createColorsTable();

		for (Couleur c : Couleur.values()) {
			colorsTable.add(colorFrame(c, abpCouleursCheckBoxes));
		}
		
		container.add(colorsTable, BorderLayout.PAGE_START);
		
		return container;
	}
	
	private JPanel createMessagePanel(String message) {
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		
		JLabel messageLabel = new JLabel(message);
		messageLabel.setHorizontalAlignment(JLabel.CENTER);
		messageLabel.setForeground(Color.RED);
		messagePanel.add(messageLabel);
		
		return messagePanel;
	}

	private JPanel colorFrame(Couleur c, Map<Couleur, JCheckBox> whereSaveCheckBoxs) {
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);
		
		JPanel colorIcon = new JPanel();
		colorIcon.setBackground(c.getCouleur());
		colorIcon.setPreferredSize(new Dimension(50, 20));
		
		JCheckBox cb = new JCheckBox();
		cb.setBackground(Color.WHITE);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (atpCouleursCheckBoxes.containsValue(e.getSource())) {
					refreshAtpColorsCheckBoxs();	
				} else {
					refreshAbpColorsCheckBoxs();
				}
			}
		});
		
		whereSaveCheckBoxs.put(c, cb);
		
		container.add(colorIcon, BorderLayout.WEST);
		container.add(cb, BorderLayout.EAST);
		
		return container;
	}
	
	private void refreshAbpColorsCheckBoxs() {
		int nbSelectedCouleurs = 0;
		for (Map.Entry<Couleur, JCheckBox> value : abpCouleursCheckBoxes.entrySet()) {
			value.getValue().setEnabled(true);
			if (value.getValue().isSelected()) {
				nbSelectedCouleurs++;
				break;
			}
	    }
		if (nbSelectedCouleurs > 0) {
			for (Map.Entry<Couleur, JCheckBox> value : abpCouleursCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
		}
	}

	private void refreshAtpColorsCheckBoxs() {
		List<Couleur> selectedCouleurs = new ArrayList<Couleur>();
		for (Map.Entry<Couleur, JCheckBox> value : atpCouleursCheckBoxes.entrySet()) {
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
			for (Map.Entry<Couleur, JCheckBox> value : atpCouleursCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
			messagePanel.setVisible(true);
		}
		commit();
	}
	
	private JPanel initAtpFormesPanel() {
		Box box = Box.createVerticalBox();

		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		for (File image : images) {
			box.add(formeFrame(image, true, atpFormesCheckBoxes));
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
	
	public JPanel formeFrame(final File image, boolean withActions, Map<String, JCheckBox> whereSaveCheckBoxs) {
		if (whereSaveCheckBoxs == null) {
			withActions = false;
		}
		
		Border border = BorderFactory.createLineBorder(Color.black);
		
		JPanel container = new JPanel();
		container.setMaximumSize(new Dimension(PANEL_WIDTH - 18, FORME_FRAME_HEIGHT));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		container.setLayout(new BorderLayout());

		JLabel imagePreview = new JLabel(new ImageIcon(image.getAbsolutePath()));
		imagePreview.setMaximumSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		imagePreview.setMinimumSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		container.add(imagePreview, BorderLayout.LINE_START);
		
		String name = image.getName().toUpperCase().split("\\.")[0];
		JLabel imageName = new JLabel(name);
		imageName.setHorizontalAlignment(JLabel.CENTER);
		container.add(imageName, BorderLayout.CENTER);
		
		if (withActions) {
			JPanel actionPane = createActionPane(image, whereSaveCheckBoxs);
			container.add(actionPane, BorderLayout.LINE_END);
		}
		
		
		return container;
	}
	
	private JPanel createActionPane(final File image, final Map<String, JCheckBox> whereSaveCheckBoxs) {
		JPanel actionPane = new JPanel(new BorderLayout());
		actionPane.setBackground(Color.WHITE); 
		
		JCheckBox cb;
		String imageName = image.getName();
		if (whereSaveCheckBoxs.containsKey(imageName)) {
			cb = whereSaveCheckBoxs.get(imageName);
		} else {	
			cb = new JCheckBox();
			cb.setBackground(Color.WHITE);
			cb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (atpFormesCheckBoxes.containsValue(e.getSource())) {
						refreshAtpFormesCheckBoxes();
						refreshSelectedFormesPanel();	
					} else {
						refreshAbpFormesCheckBoxs();
					}
				}
			});
			cb.setEnabled(false);
			whereSaveCheckBoxs.put(imageName, cb);
		}
		actionPane.add(cb, BorderLayout.LINE_START);
	
		JLabel delete = new JLabel(new ImageIcon(Modele.DOSSIER_ASSETS + "corbeille.jpg"));
		delete.setPreferredSize(new Dimension(60, 30));
		delete.setHorizontalAlignment(JLabel.CENTER);
		delete.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {
				((JLabel) e.getSource()).setIcon(new ImageIcon(Modele.DOSSIER_ASSETS + "corbeille.jpg"));
			}
			
			public void mouseEntered(MouseEvent e) {
				((JLabel) e.getSource()).setIcon(new ImageIcon(Modele.DOSSIER_ASSETS + "corbeille-hover.jpg"));
			}
		
			public void mouseClicked(MouseEvent e) {
				String[] options = {"Oui", "Non"}; 
				int retour = BoiteDialogue.createOptionBox(JOptionPane.YES_NO_OPTION, "Confirmation", "Voulez-vous vraiment supprimer cette image ? (Si oui, la configuration va être automatiquement sauvegardée)", options, 1);
				if (retour == JOptionPane.OK_OPTION) {
					boolean deleteStatut = ((AdminControleur) getControleur()).deleteImage(image);
					if (deleteStatut) {
						BoiteDialogue.createModalBox(JOptionPane.INFORMATION_MESSAGE, "Information", "Votre image a bien été supprimée. La configuration va être automatiquement sauvegardée.");
						
						atpFormesCheckBoxes.remove(image.getName());
						abpFormesCheckBoxes.remove(image.getName());
						refreshAtpFormesPanel();
						refreshAtpFormesCheckBoxes();
						refreshSelectedFormesPanel();
						refreshAbpFormesPanel();
						refreshAbpFormesCheckBoxs();
						
						BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
					} else {
						BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Erreur", "Votre image n'a pas pu être supprimée.");
					}
				}	
			}
		});
		
		actionPane.add(delete, BorderLayout.CENTER);
		
		return actionPane;
	}

	private void refreshAtpFormesCheckBoxes() {
		List<File> selectedFormes = new ArrayList<File>();
		for (Map.Entry<String, JCheckBox> value : atpFormesCheckBoxes.entrySet()) {
			if (value.getValue().isSelected()) {
				selectedFormes.add(new File(Modele.DOSSIER_FORMES + value.getKey()));
			}
			value.getValue().setEnabled(true);
	    }
		getControleur().getModele().setFormesConfig(selectedFormes);
		
		JPanel messagePanel = panels.get(ATP_MESSAGE_FORMES_PANEL);
		if (getControleur().getModele().getFormesConfig().size() < Modele.MAX_SELECTED_FORMES) {
			messagePanel.setVisible(false);
		} else {
			for (Map.Entry<String, JCheckBox> value : atpFormesCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
			messagePanel.setVisible(true);
		}
	}
	
	private void refreshAbpFormesCheckBoxs() {
		int nbSelectedFormes = 0;
		for (Map.Entry<String, JCheckBox> value : abpFormesCheckBoxes.entrySet()) {
			value.getValue().setEnabled(true);
			if (value.getValue().isSelected()) {
				nbSelectedFormes++;
				break;
			}
	    }
		if (nbSelectedFormes > 0) {
			for (Map.Entry<String, JCheckBox> value : abpFormesCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
		}
	}

	/**
	 * Ouvre un explorateur de fichier qui prend en compte uniquement les images.
	 * Si une image est selectioné l'enregistre en fesant appelle au controlleur.
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
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			boolean saveStatut = ((AdminControleur) getControleur()).saveImage(fc.getSelectedFile());
			if (saveStatut) {
				BoiteDialogue.createModalBox(JOptionPane.INFORMATION_MESSAGE, "Information", "Votre image a bien été enregistrée.");
				refreshAtpFormesPanel();
			} else {
				BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Erreur", "Votre image n'a pas pu être enregistrée.");
			}
		}
	}
	
	/**
	 * Actualise le panel des formes qui sert dans la création de la matrice.
	 */
	private void refreshAtpFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_PANEL));
		panels.put(ATP_FORMES_PANEL, initAtpFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL), ATP_FORMES_PANEL_POS);
		commit();
	}
	
	/**
	 * Actualise le panel des formes qui sert dans la création d'intrus.
	 */
	private void refreshAbpFormesPanel() {
		panels.get(ADMIN_BOT_PANEL).remove(panels.get(ABP_FORMES_PANEL));
		panels.put(ABP_FORMES_PANEL, initAbpFormesPanel());
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_PANEL), ATP_FORMES_PANEL_POS);
		commit();
	}
	
	/**
	 * Actualise le panel des formes selectionnées.
	 */
	private void refreshSelectedFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_SELECT_PANEL));
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_POS);
		commit();
	}
	
	/**
	 * Actualise le panel des intrus.
	 */
	private void refreshFormesPoolPanel() {
		panels.get(ADMIN_BOT_PANEL).remove(panels.get(ABP_FORMES_POOL_PANEL));
		panels.put(ABP_FORMES_POOL_PANEL, initFormesPoolPanel());
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_FORMES_POOL_PANEL), ATP_FORMES_SELECT_PANEL_POS);
		commit();
	}

	/**
	 * Valide et repaint la vue.
	 */
	private void commit() {
		panels.get(PANEL_PRINCIPALE).revalidate();
		panels.get(PANEL_PRINCIPALE).repaint();
	}

	public void actualise() {
		
	}

}
