package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
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
import piixcolor.utilitaire.Config;
import piixcolor.utilitaire.Couleur;
import piixcolor.utilitaire.ImageFilter;
import piixcolor.utilitaire.Listing;

public class VueAdmin extends Vue {
	
	//positions des panels
	private static final int ATP_COULEURS_PANEL_P = 0;
	private static final int ATP_FORMES_PANEL_P = 1;
	private static final int ATP_FORMES_SELECT_PANEL_P = 2;
	
	//divers informations
	private static final int ATP_MARGIN = 15;
	private static final int ATP_NB_PANEL = 3;
	private static final int NB_ADMIN_PART = 2;
	
	//taille des panels
	private static final int ATP_PANEL_WIDTH = Fenetre.FRAME_WIDTH / ATP_NB_PANEL - (ATP_NB_PANEL - 1) * ATP_MARGIN;
	private static final int ATP_PANEL_HEIGHT = Fenetre.FRAME_HEIGHT / NB_ADMIN_PART - (NB_ADMIN_PART - 1) * ATP_MARGIN;
	private static final int ATP_FORME_FRAME_HEIGHT = Fenetre.FRAME_HEIGHT / 10 + 40;
	
	//nom des panels
	private static final String PANEL_PRINCIPALE = "mainPanel";
	private static final String ADMIN_TOP_PANEL =  "adminTopPanel";
	private static final String ATP_FORMES_PANEL =  "atpFormesPanel";
	private static final String ATP_COULEURS_PANEL =  "atpCouleursPanel";
	private static final String ATP_FORMES_SELECT_PANEL =  "atpSelectedFormesPanel";
	private static final String ATP_MESSAGE_COULEURS_PANEL =  "atpMessageCouleursPanel";
	private static final String ATP_MESSAGE_FORMES_PANEL = "atpMessageFormesPanel";
	
	//liste de tous les panels
	private Map<String, JPanel> panels;
	
	//liste de toutes les checkbox des formes
	private Map<String, JCheckBox> formesCheckBoxes;
	private Map<Integer, JCheckBox> couleursCheckBoxes;

	public VueAdmin(Fenetre fenetre, Controleur controleur) {
		super(fenetre, controleur, null);
		
		//init lists check box
		formesCheckBoxes = new LinkedHashMap<String, JCheckBox>();
		couleursCheckBoxes = new HashMap<Integer, JCheckBox>();

		//init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put(PANEL_PRINCIPALE, new JPanel());
		panels.put(ADMIN_TOP_PANEL, new JPanel(new GridLayout(1, ATP_NB_PANEL, ATP_MARGIN, 0)));
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.put(ATP_FORMES_PANEL, initFormesPanel());
		panels.put(ATP_COULEURS_PANEL, initCouleursPanel());
		
		//connection panel
		add(panels.get(PANEL_PRINCIPALE));
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_COULEURS_PANEL), ATP_COULEURS_PANEL_P);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL), ATP_FORMES_PANEL_P);
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_P);
		panels.get(PANEL_PRINCIPALE).add(panels.get(ADMIN_TOP_PANEL), BorderLayout.PAGE_START);
		
		//display panel
		commit();
	}

	private JPanel initSelectedFormesPanel() {
		Box box = Box.createVerticalBox();
		Border border = BorderFactory.createLineBorder(Color.black);
		
		int nbSelectedFormes = 0;
		for (Map.Entry<String, JCheckBox> value : formesCheckBoxes.entrySet()) {
			if (value.getValue().isSelected()) {
				box.add(formeFrame(new File(Config.DOSSIER_FORME + value.getKey()), ATP_PANEL_WIDTH - 20, ATP_FORME_FRAME_HEIGHT, false));
				nbSelectedFormes++;
			}
	    }

		JScrollPane jsp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(null);
		jsp.getViewport().setBackground(Color.WHITE);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		panels.put(ATP_MESSAGE_FORMES_PANEL, messagePanel);
		
		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT));
		container.setBorder(border);
		container.setBackground(Color.WHITE);
		container.add(jsp, BorderLayout.CENTER);
		container.add(messagePanel, BorderLayout.PAGE_END);

		if (nbSelectedFormes == 0) {
			JLabel message = new JLabel(" Vous n'avez pas encore selectionez de formes.");
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
		
		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(ATP_PANEL_WIDTH, ATP_PANEL_HEIGHT - 30));
		jp.setBackground(Color.WHITE);
		jp.setLayout(new GridLayout(5, 2));

		for (Couleur c : Couleur.values()) {
			initColor(jp, c);
		}
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		panels.put(ATP_MESSAGE_COULEURS_PANEL, messagePanel);
		
		container.add(jp, BorderLayout.PAGE_START);
		container.add(messagePanel, BorderLayout.PAGE_END);
		
		return container;
	}
	
	private void initColor(JPanel jp, Couleur c) {
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);
		
		JPanel colorIcon = new JPanel();
		colorIcon.setBackground(c.getCoul());
		colorIcon.setPreferredSize(new Dimension(50, 20));
		
		JCheckBox cb = new JCheckBox();
		cb.setBackground(Color.WHITE);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshColorPanel()	;	
			}
		});
		
		couleursCheckBoxes.put(c.ordinal(), cb);
		
		container.add(colorIcon, BorderLayout.NORTH);
		container.add(cb, BorderLayout.SOUTH);
		jp.add(container);
	}
	
	private void refreshColorPanel() {
		JPanel messagePanel = panels.get(ATP_MESSAGE_COULEURS_PANEL);
		messagePanel.removeAll();
		
		int nbSelectedCouleurs = 0;
		for (Map.Entry<Integer, JCheckBox> value : couleursCheckBoxes.entrySet()) {
			if (value.getValue().isSelected()) {
				nbSelectedCouleurs++;
			}
			value.getValue().setEnabled(true);
	    }
		if (nbSelectedCouleurs >= Config.MAX_SELECTED_COULEURS) {
			for (Map.Entry<Integer, JCheckBox> value : couleursCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
			JLabel message = new JLabel("Nombre maximum de couleurs atteint.");
			message.setHorizontalAlignment(JLabel.CENTER);
			message.setForeground(Color.RED);
			messagePanel.add(message);
		}
		commit();
	}
	
	private JPanel initFormesPanel() {
		Box box = Box.createVerticalBox();
		Border border = BorderFactory.createLineBorder(Color.black);

		File[] images = Listing.listeImages(Config.DOSSIER_FORME);
		for (File image : images) {
			box.add(formeFrame(image, ATP_PANEL_WIDTH - 18, ATP_FORME_FRAME_HEIGHT, true));
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
		container.setPreferredSize(new Dimension(width, height));
		container.setMaximumSize(new Dimension(width, height));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		container.setLayout(new BorderLayout());

		String imagePath = image.getAbsolutePath();
		JLabel imagePreview = new JLabel(new ImageIcon(imagePath));
		imagePreview.setMaximumSize(new Dimension(Config.IMG_SIZE, Config.IMG_SIZE));
		imagePreview.setPreferredSize(new Dimension(Config.IMG_SIZE, Config.IMG_SIZE));
		imagePreview.setMinimumSize(new Dimension(Config.IMG_SIZE, Config.IMG_SIZE));
		container.add(imagePreview, BorderLayout.LINE_START);
		
		String name = image.getName().toUpperCase().split("\\.")[0];
		JLabel imageName = new JLabel(name);
		imageName.setHorizontalAlignment(JLabel.CENTER);
		container.add(imageName, BorderLayout.CENTER);
		
		JPanel action = new JPanel(new BorderLayout());
		action.setBackground(Color.WHITE);
		
		if (withActions) {
			JCheckBox cb;
			
			if (formesCheckBoxes.containsKey(image.getName())) {
				cb = formesCheckBoxes.get(image.getName());
			} else {	
				cb = new JCheckBox();
				cb.setMargin(new Insets(0, 0, 0, 15));
				cb.setBackground(Color.WHITE);
				cb.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						refreshSelectedFormesPanel();	
					}
				});
				cb.setEnabled(false);
				
				formesCheckBoxes.put(image.getName(), cb);
			}
			
			action.add(cb, BorderLayout.LINE_START);
		
			JLabel delete = new JLabel("Supprimer");
			delete.setPreferredSize(new Dimension(100, 100));
			delete.setHorizontalAlignment(JLabel.CENTER);
			delete.addMouseListener(new MouseListener() {
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseExited(MouseEvent e) {}
				public void mouseEntered(MouseEvent e) {}
			
				public void mouseClicked(MouseEvent e) {
					Object[] options = {"Oui", "Non"};
					int retour = JOptionPane.showOptionDialog(fenetre, "Voulez-vous vraiment supprimer cette image ?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]); 
					if (retour == JOptionPane.OK_OPTION) {
						boolean deleteStatut = ((AdminControleur) getControleur()).deleteImage(image);
						if (deleteStatut) {
							JOptionPane.showMessageDialog(fenetre, "Votre image a bien Ã©tÃ© supprimÃ©e.", "Information", JOptionPane.INFORMATION_MESSAGE);
							formesCheckBoxes.remove(image.getName());
							refreshFormesPanel();
							refreshSelectedFormesPanel();
						} else {
							JOptionPane.showMessageDialog(fenetre, "L'image n'a pas pu Ãªtre supprimÃ©e.", "Erreur", JOptionPane.ERROR_MESSAGE);
						}
					}	
				}
			});
			
			action.add(delete, BorderLayout.CENTER);
		
		}
		
		container.add(action, BorderLayout.LINE_END);
		
		refreshFormesCheckBoxes();
		
		return container;
	}
	
	private void refreshSelectedFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_SELECT_PANEL));
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_P);
		commit();
	}
	
	private void refreshFormesCheckBoxes() {
		JPanel messagePanel = panels.get(ATP_MESSAGE_FORMES_PANEL);
		messagePanel.removeAll();
		messagePanel.setBorder(null);
		
		int nbSelectedFormes = 0;
		for (Map.Entry<String, JCheckBox> value : formesCheckBoxes.entrySet()) {
			if (value.getValue().isSelected()) {
				nbSelectedFormes++;
			}
			value.getValue().setEnabled(true);
	    }
		if (nbSelectedFormes >= Config.MAX_SELECTED_FORMES) {
			for (Map.Entry<String, JCheckBox> value : formesCheckBoxes.entrySet()) {
				if (!value.getValue().isSelected()) {
					value.getValue().setEnabled(false);
				}
		    }
			JLabel message = new JLabel("Nombre maximum de formes atteint.");
			message.setHorizontalAlignment(JLabel.CENTER);
			message.setForeground(Color.RED);
			messagePanel.add(message);
			messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
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
				JOptionPane.showMessageDialog(fenetre, "Votre image a bien été enregistrée.", "Information", JOptionPane.INFORMATION_MESSAGE);
				refreshFormesPanel();
			} else {
				JOptionPane.showMessageDialog(fenetre, "L'image n'a pas pu être enregistrée.", "Erreur", JOptionPane.ERROR_MESSAGE);
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


	@Override
	public void actualise() {
		// TODO Auto-generated method stub
		
	}

}
