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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import javax.swing.border.Border;

import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.Controleur;
import piixcolor.modele.Modele;
import piixcolor.utilitaire.Couleur;
import piixcolor.utilitaire.ImageFilter;
import piixcolor.utilitaire.Listing;

public class VueAdmin extends Vue {
	
	//positions des panels
	private static final int ATP_COULEURS_PANEL_P = 0;
	private static final int ATP_FORMES_PANEL_P = 1;
	private static final int ATP_FORMES_SELECT_PANEL_P = 2;
	
	//divers informations
	private static final int ADMIN_MARGIN = 15;
	private static final int ATP_NB_PANEL = 3;
	private static final int ABP_NB_PANEL = 3;
	private static final int NB_ADMIN_PART = 2;
	
	//taille des panels
	private static final int ATP_PANEL_WIDTH = Fenetre.FRAME_WIDTH / ATP_NB_PANEL - (ATP_NB_PANEL - 1) * ADMIN_MARGIN;
	private static final int ATP_PANEL_HEIGHT = Fenetre.FRAME_HEIGHT / NB_ADMIN_PART - NB_ADMIN_PART * ADMIN_MARGIN - 30;
	private static final int ATP_FORME_FRAME_HEIGHT = Fenetre.FRAME_HEIGHT / 10 + 40;
	
	//nom des panels
	private static final String PANEL_PRINCIPALE = "mainPanel";
	private static final String ADMIN_TOP_PANEL =  "adminTopPanel";
	private static final String ATP_FORMES_PANEL =  "atpFormesPanel";
	private static final String ATP_COULEURS_PANEL =  "atpCouleursPanel";
	private static final String ATP_FORMES_SELECT_PANEL =  "atpSelectedFormesPanel";
	private static final String ATP_MESSAGE_COULEURS_PANEL =  "atpMessageCouleursPanel";
	private static final String ATP_MESSAGE_FORMES_PANEL = "atpMessageFormesPanel";
	private static final String ADMIN_BOT_PANEL =  "adminBotPanel";
	private static final String ABP_COULEURS_PANEL =  "abpCouleursPanel";
	private static final String ADMIN_ACTION_PANEL = "adminActionPanel";
	
	//liste de tous les panels
	private Map<String, JPanel> panels;
	
	//liste de toutes les checkbox des formes
	private Map<String, JCheckBox> formesCheckBoxes;
	private Map<Couleur, JCheckBox> couleursCheckBoxes;

	public VueAdmin(Fenetre fenetre, Controleur controleur) {
		super(fenetre, controleur);
		
		//init lists check box
		formesCheckBoxes = new LinkedHashMap<String, JCheckBox>();
		couleursCheckBoxes = new HashMap<Couleur, JCheckBox>();

		//init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put(PANEL_PRINCIPALE, new JPanel(new GridBagLayout()));
		panels.put(ADMIN_TOP_PANEL, new JPanel(new GridLayout(1, ATP_NB_PANEL, ADMIN_MARGIN, 0)));
		panels.put(ATP_FORMES_PANEL, initFormesPanel());
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.put(ATP_COULEURS_PANEL, initCouleursPanel());
		panels.put(ADMIN_BOT_PANEL, new JPanel(new GridLayout(1, ABP_NB_PANEL, ADMIN_MARGIN, 0)));
		panels.put(ABP_COULEURS_PANEL, initCouleursPanel());
		panels.put(ADMIN_ACTION_PANEL, initActionsPanel());
		
		//connection panel
		add(panels.get(PANEL_PRINCIPALE));
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_COULEURS_PANEL), ATP_COULEURS_PANEL_P);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL), ATP_FORMES_PANEL_P);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_P);
		panels.get(ADMIN_BOT_PANEL).add(panels.get(ABP_COULEURS_PANEL), ATP_COULEURS_PANEL_P);
		
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
	
	private JPanel initSelectedFormesPanel() {
		Box box = Box.createVerticalBox();
		Border border = BorderFactory.createLineBorder(Color.black);
		
		for (File value : getControleur().getModele().getFormesConfig()) {
			box.add(formeFrame(value, ATP_PANEL_WIDTH - 20, ATP_FORME_FRAME_HEIGHT, false));
	    }

		JScrollPane jsp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(null);
		jsp.getViewport().setBackground(Color.WHITE);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		JLabel message = new JLabel("Nombre maximum de formes atteint.");
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setForeground(Color.RED);
		messagePanel.add(message);
		messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		panels.put(ATP_MESSAGE_FORMES_PANEL, messagePanel);
		
		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT));
		container.setBorder(border);
		container.setBackground(Color.WHITE);
		container.add(jsp, BorderLayout.CENTER);
		container.add(messagePanel, BorderLayout.PAGE_END);

		if (getControleur().getModele().getFormesConfig().size() == 0) {
			message = new JLabel(" Vous n'avez pas encore selectionez de formes.");
			message.setHorizontalAlignment(JLabel.CENTER);
			container.add(message, BorderLayout.CENTER);
		}
		
		refreshFormesCheckBoxes();
		
		return container;
	}

	private JPanel initCouleursPanel() {
		Border border = BorderFactory.createLineBorder(Color.black);
		
		JPanel container = new JPanel(new BorderLayout()); 
		container.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		
		JPanel colorsTable = new JPanel();
		colorsTable.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT - 30));
		colorsTable.setBackground(Color.WHITE);
		colorsTable.setLayout(new GridLayout(5, 2));

		for (Couleur c : Couleur.values()) {
			colorsTable.add(colorFrame(c));
			if (controleur.getModele().getCouleursConfig().contains(c)) {
				couleursCheckBoxes.get(c).setSelected(true);
			}
		}
		
		//message erreur
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		JLabel message = new JLabel("Nombre maximum de couleurs atteint.");
		message.setHorizontalAlignment(JLabel.CENTER);
		message.setForeground(Color.RED);
		messagePanel.add(message);
		panels.put(ATP_MESSAGE_COULEURS_PANEL, messagePanel);
		
		container.add(colorsTable, BorderLayout.PAGE_START);
		container.add(messagePanel, BorderLayout.PAGE_END);
		
		refreshColorPanel();
		
		return container;
	}
	
	private JPanel colorFrame(Couleur c) {
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);
		
		JPanel colorIcon = new JPanel();
		colorIcon.setBackground(c.getCouleur());
		colorIcon.setPreferredSize(new Dimension(50, 20));
		
		JCheckBox cb = new JCheckBox();
		cb.setBackground(Color.WHITE);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshColorPanel()	;	
			}
		});
		
		couleursCheckBoxes.put(c, cb);
		
		container.add(colorIcon, BorderLayout.WEST);
		container.add(cb, BorderLayout.EAST);
		
		return container;
	}
	
	private void refreshColorPanel() {
		List<Couleur> selectedCouleurs = new ArrayList<Couleur>();
		for (Map.Entry<Couleur, JCheckBox> value : couleursCheckBoxes.entrySet()) {
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
			for (Map.Entry<Couleur, JCheckBox> value : couleursCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
			messagePanel.setVisible(true);
		}
		commit();
	}
	
	private JPanel initFormesPanel() {
		Box box = Box.createVerticalBox();
		Border border = BorderFactory.createLineBorder(Color.black);

		File[] images = Listing.listeImages(Modele.DOSSIER_FORMES);
		for (File image : images) {
			box.add(formeFrame(image, ATP_PANEL_WIDTH - 18, ATP_FORME_FRAME_HEIGHT, true));
			if (getControleur().getModele().getFormesConfig().contains(image)) {
				formesCheckBoxes.get(image.getName()).setSelected(true);
			}
		}

		JScrollPane jsp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT - 30));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(border);

		JButton b = new JButton("Ajouter une nouvelle image");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		
		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT));
		container.add(jsp, BorderLayout.NORTH);
		container.add(b, BorderLayout.SOUTH); 
		
		return container;
	}
	
	public JPanel formeFrame(final File image, int width, int height, boolean withActions) {
		Border border = BorderFactory.createLineBorder(Color.black);
		
		JPanel container = new JPanel();
		container.setMaximumSize(new Dimension(width, height));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		container.setLayout(new BorderLayout());

		JLabel imagePreview = new JLabel(new ImageIcon(image.getAbsolutePath()));
		imagePreview.setMaximumSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		imagePreview.setPreferredSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		imagePreview.setMinimumSize(new Dimension(Modele.IMG_SIZE, Modele.IMG_SIZE));
		container.add(imagePreview, BorderLayout.LINE_START);
		
		String name = image.getName().toUpperCase().split("\\.")[0];
		JLabel imageName = new JLabel(name);
		imageName.setHorizontalAlignment(JLabel.CENTER);
		container.add(imageName, BorderLayout.CENTER);
		
		JPanel actionPane = new JPanel(new BorderLayout());
		actionPane.setBackground(Color.WHITE);
		
		if (withActions) {
			JCheckBox cb;
			if (formesCheckBoxes.containsKey(image.getName())) {
				cb = formesCheckBoxes.get(image.getName());
			} else {	
				cb = new JCheckBox();
				cb.setBackground(Color.WHITE);
				cb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						refreshFormesCheckBoxes();
						refreshSelectedFormesPanel();	
					}
				});
				cb.setEnabled(false);
				formesCheckBoxes.put(image.getName(), cb);
			}
			actionPane.add(cb, BorderLayout.LINE_START);
		
			JLabel delete = new JLabel(new ImageIcon(Modele.DOSSIER_ASSETS + "corbeille.jpg"));
			delete.setPreferredSize(new Dimension(60, 30));
			delete.setHorizontalAlignment(JLabel.CENTER);
			delete.setForeground(Color.RED);
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
					int retour = BoiteDialogue.createOptionBox(JOptionPane.YES_NO_OPTION, "Confirmation", "Voulez-vous vraiment supprimer cette image ?", options, 1);
					if (retour == JOptionPane.OK_OPTION) {
						boolean deleteStatut = ((AdminControleur) getControleur()).deleteImage(image);
						if (deleteStatut) {
							BoiteDialogue.createModalBox(JOptionPane.INFORMATION_MESSAGE, "Information", "Votre image a bien été supprimée. La configuration va être automatiquement sauvegardée.");
							formesCheckBoxes.remove(image.getName());
							refreshFormesPanel();
							refreshFormesCheckBoxes();
							refreshSelectedFormesPanel();
							BoiteDialogue.enregistrerConfig(Modele.FICHIER_CONFIG);
						} else {
							BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Erreur", "Votre image n'a pas pu être supprimée.");
						}
					}	
				}
			});
			
			actionPane.add(delete, BorderLayout.CENTER);
		
		}
		
		container.add(actionPane, BorderLayout.LINE_END);
		
		return container;
	}
	
	private void refreshSelectedFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_SELECT_PANEL));
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_P);
		commit();
	}
	
	private void refreshFormesCheckBoxes() {
		List<File> selectedFormes = new ArrayList<File>();
		for (Map.Entry<String, JCheckBox> value : formesCheckBoxes.entrySet()) {
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
			for (Map.Entry<String, JCheckBox> value : formesCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
			messagePanel.setVisible(true);
		}
	}


	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Ajouter une image");
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new ImageFilter());
		
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			boolean saveStatut = ((AdminControleur) getControleur()).saveImage(fc.getSelectedFile());
			if (saveStatut) {
				BoiteDialogue.createModalBox(JOptionPane.INFORMATION_MESSAGE, "Information", "Votre image a bien été enregistrée.");
				refreshFormesPanel();
			} else {
				BoiteDialogue.createModalBox(JOptionPane.ERROR_MESSAGE, "Erreur", "Votre image n'a pas pu être enregistrée.");
			}
		}
	}
	
	private void refreshFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_PANEL));
		panels.put(ATP_FORMES_PANEL, initFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL), ATP_FORMES_PANEL_P);
		commit();
	}

	
	private void commit() {
		panels.get(PANEL_PRINCIPALE).revalidate();
		panels.get(PANEL_PRINCIPALE).repaint();
	}

	public void actualise() {
		
	}

}
