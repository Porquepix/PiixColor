package piixcolor.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import piixcolor.controller.AdminController;
import piixcolor.controller.Controller;
import piixcolor.util.ImageFilter;
import piixcolor.test.Listing;
import piixcolor.util.Config;

public class VueAdmin extends Vue {
	
	//positions des panels
	private static final int ATP_COULEURS_PANEL_P = 0;
	private static final int ATP_FORMES_PANEL_P = 1;
	private static final int ATP_FORMES_SELECT_PANEL_P = 2;
	
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
	private Map<JCheckBox, String> formesCheckBoxes;
	private Map<JCheckBox, String> couleursCheckBoxes;

	public VueAdmin(Fenetre fenetre, Controller controller) {
		super(fenetre, controller);
		
		//init lists check box
		formesCheckBoxes = new LinkedHashMap<JCheckBox, String>();
		couleursCheckBoxes = new HashMap<JCheckBox, String>();

		//init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put(PANEL_PRINCIPALE, new JPanel());
		panels.put(ADMIN_TOP_PANEL, new JPanel(new GridLayout(1, 3, 15, 0)));
		panels.put(ATP_FORMES_PANEL, initFormesPanel());
		panels.put(ATP_COULEURS_PANEL, initCouleursPanel());
		panels.put(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		
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
		int width = 300, height = 400;
		
		Box box = Box.createVerticalBox();
		Border border = BorderFactory.createLineBorder(Color.black);
		
		int nbSelectedFormes = 0;
		for (Map.Entry<JCheckBox, String> value : formesCheckBoxes.entrySet()) {
			if (value.getKey().isSelected()) {
				box.add(formeFrame(new File("images/" + value.getValue()), width - 20, 100, false));
				nbSelectedFormes++;
			}
	    }

		JScrollPane jsp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(width, height));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(null);
		jsp.getViewport().setBackground(Color.WHITE);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		panels.put(ATP_MESSAGE_FORMES_PANEL, messagePanel);
		
		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(width, height));
		container.setBorder(border);
		container.setBackground(Color.WHITE);
		container.add(jsp, BorderLayout.CENTER);
		container.add(messagePanel, BorderLayout.PAGE_END);

		if (nbSelectedFormes == 0) {
			JLabel message = new JLabel(" Vous n'avez pas encore selectionez de formes.");
			message.setHorizontalAlignment(JLabel.CENTER);
			container.add(message, BorderLayout.CENTER);
		}
		
		return container;
	}

	private JPanel initCouleursPanel() {
		Border border = BorderFactory.createLineBorder(Color.black);
		
		JPanel container = new JPanel(new BorderLayout()); 
		container.setPreferredSize(new Dimension(300, 370));
		container.setBackground(Color.WHITE);
		container.setBorder(border);
		
		JPanel jp = new JPanel();
		jp.setPreferredSize(new Dimension(300, 370));
		jp.setBackground(Color.WHITE);
		jp.setLayout(new GridLayout(5, 2));

		initColor(jp, new Color(244, 67, 54));
		initColor(jp, new Color(33, 150, 243));
		initColor(jp, new Color(156, 39, 176));
		initColor(jp, new Color(236, 64, 122));
		initColor(jp, new Color(139, 195, 74));
		initColor(jp, new Color(255, 235, 59));
		initColor(jp, new Color(255, 152, 0));
		initColor(jp, new Color(121, 85, 72));
		initColor(jp, new Color(158, 158, 158));
		initColor(jp, new Color(0, 0, 0));
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(Color.WHITE);
		panels.put(ATP_MESSAGE_COULEURS_PANEL, messagePanel);
		
		container.add(jp, BorderLayout.PAGE_START);
		container.add(messagePanel, BorderLayout.PAGE_END);
		
		return container;
	}
	
	//fonction asupprimer par la suite (remplacer par boucle for)
	private void initColor(JPanel jp, Color c) {
		JPanel container = new JPanel();
		container.setBackground(Color.WHITE);
		
		JPanel colorIcon = new JPanel();
		colorIcon.setBackground(c);
		colorIcon.setPreferredSize(new Dimension(50, 20));
		
		JCheckBox cb = new JCheckBox();
		cb.setBackground(Color.WHITE);
		cb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshColorPanel()	;	
			}
		});
		
		//A MODIFIER
		couleursCheckBoxes.put(cb, c.toString());
		
		container.add(colorIcon, BorderLayout.NORTH);
		container.add(cb, BorderLayout.SOUTH);
		jp.add(container);
	}
	
	private void refreshColorPanel() {
		JPanel messagePanel = panels.get(ATP_MESSAGE_COULEURS_PANEL);
		messagePanel.removeAll();
		
		int nbSelectedCouleurs = 0;
		for (Map.Entry<JCheckBox, String> value : couleursCheckBoxes.entrySet()) {
			if (value.getKey().isSelected()) {
				nbSelectedCouleurs++;
			}
			value.getKey().setEnabled(true);
	    }
		if (nbSelectedCouleurs >= Config.MAX_SELECTED_COULEURS) {
			for (Map.Entry<JCheckBox, String> value : couleursCheckBoxes.entrySet()) {
				if (!value.getKey().isSelected()) {
					value.getKey().setEnabled(false);
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
		int width = 300, height = 400;

		Box box = Box.createVerticalBox();
		Border border = BorderFactory.createLineBorder(Color.black);

		File[] images = Listing.liste("images");
		for (File image : images) {
			box.add(formeFrame(image, width - 20, 100, true));
		}

		JScrollPane jsp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(width, height - 30));
		jsp.getVerticalScrollBar().setUnitIncrement(8);
		jsp.setBorder(border);

		JButton b = new JButton("Ajouter une nouvelle image");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		
		JPanel container = new JPanel(new BorderLayout());
		container.setPreferredSize(new Dimension(width, height));
		container.add(jsp, BorderLayout.NORTH);
		container.add(b, BorderLayout.SOUTH); 
		
		return container;
	}
	
	public JPanel formeFrame(File image, int width, int height, boolean withCheckBox) {
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
		
		JLabel imageName = new JLabel(image.getName().toUpperCase().split("\\.")[0]);
		imageName.setHorizontalAlignment(JLabel.CENTER);
		container.add(imageName, BorderLayout.CENTER);
		
		if (withCheckBox) {
			JCheckBox cb = new JCheckBox();
			cb.setMargin(new Insets(0, 0, 0, 15));
			cb.setBackground(Color.WHITE);
			container.add(cb, BorderLayout.LINE_END);
			cb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshSelectedFormesPanel();	
				}
			});
			
			formesCheckBoxes.put(cb, image.getName());
		}
		
		return container;
	}
	
	private void refreshSelectedFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_SELECT_PANEL));
		panels.replace(ATP_FORMES_SELECT_PANEL, initSelectedFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_SELECT_PANEL), ATP_FORMES_SELECT_PANEL_P);
		
		JPanel messagePanel = panels.get(ATP_MESSAGE_FORMES_PANEL);
		messagePanel.removeAll();
		messagePanel.setBorder(null);
		
		int nbSelectedFormes = 0;
		for (Map.Entry<JCheckBox, String> value : formesCheckBoxes.entrySet()) {
			if (value.getKey().isSelected()) {
				nbSelectedFormes++;
			}
			value.getKey().setEnabled(true);
	    }
		if (nbSelectedFormes >= Config.MAX_SELECTED_FORMES) {
			for (Map.Entry<JCheckBox, String> value : formesCheckBoxes.entrySet()) {
				if (!value.getKey().isSelected()) {
					value.getKey().setEnabled(false);
				}
		    }
			JLabel message = new JLabel("Nombre maximum de formes atteint.");
			message.setHorizontalAlignment(JLabel.CENTER);
			message.setForeground(Color.RED);
			messagePanel.add(message);
			messagePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		}
		
		commit();
	}
	
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Ajouter une image");
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new ImageFilter());
		
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			boolean saveStatut = ((AdminController) getController()).saveImage(fc.getSelectedFile());
			if (saveStatut) {
				JOptionPane.showMessageDialog(this, "Votre image a bien été enregistré.", "Information", JOptionPane.INFORMATION_MESSAGE);
				refreshFormesPanel();
			} else {
				JOptionPane.showMessageDialog(this, "L'image n'a pas pu être enregistré.", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void refreshFormesPanel() {
		panels.get(ADMIN_TOP_PANEL).remove(panels.get(ATP_FORMES_PANEL));
		panels.replace(ATP_FORMES_PANEL, initFormesPanel());
		panels.get(ADMIN_TOP_PANEL).add(panels.get(ATP_FORMES_PANEL), ATP_FORMES_PANEL_P);
		commit();
	}

	
	private void commit() {
		panels.get(PANEL_PRINCIPALE).revalidate();
		panels.get(PANEL_PRINCIPALE).repaint();
	}

}
