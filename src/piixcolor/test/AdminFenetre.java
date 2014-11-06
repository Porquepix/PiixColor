package piixcolor.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

public class AdminFenetre extends JFrame {
	
	//positions des panels
	private static final int ATP_FORMES_PANEL = 0;
	private static final int ATP_FORMES_SELECT_PANEL = 1;
	private static final int ATP_COULEURS_PANEL = 2;
	
	//autres (potentiellement a supprimer)
	private static final int IMG_SIZE = 100;
	private static final int MAX_SELECTED_FORMES = 4;
	private static final int MAX_SELECTED_COULEURS = 4;
	
	//liste de tous les panels
	private Map<String, JPanel> panels;
	
	//liste de toutes les checkbox des formes
	private Map<JCheckBox, String> formeCheckBox;
	private Map<JCheckBox, String> couleurCheckBox;

	public AdminFenetre(int width, int height, String title) {
		super();

		//init frame
		setPreferredSize(new Dimension(width, height));
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		//init lists check box
		formeCheckBox = new LinkedHashMap<JCheckBox, String>();
		couleurCheckBox = new HashMap<JCheckBox, String>();

		//init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put("contentPane", new JPanel());
		panels.put("adminTopPane", new JPanel(new GridLayout(1, 3, 15, 0)));
		panels.put("formesPane1", initFormesPanel());
		panels.put("colorsPane1", initColorPanel());
		panels.put("selectedFormes", initSelectedFormesPanel());
		
		//connection panel
		getContentPane().add(panels.get("contentPane"));
		panels.get("adminTopPane").add(panels.get("formesPane1"), ATP_FORMES_PANEL);
		panels.get("adminTopPane").add(panels.get("selectedFormes"), ATP_FORMES_SELECT_PANEL);
		panels.get("adminTopPane").add(panels.get("colorsPane1"), ATP_COULEURS_PANEL);
		panels.get("contentPane").add(panels.get("adminTopPane"), BorderLayout.PAGE_START);

		//end init frame
		requestFocus();
		pack();
		setLocationRelativeTo(null);
		
		//display panel
		commit();
	}


	private JPanel initSelectedFormesPanel() {
		int width = 300, height = 400;
		
		Box box = Box.createVerticalBox();
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		int nbSelected = 0;
		for (Map.Entry<JCheckBox, String> value : formeCheckBox.entrySet()) {
			if (value.getKey().isSelected()) {
				box.add(formFrame(new File("images/" + value.getValue()), width - 20, 100, false));
				nbSelected++;
			}
	    }

		JScrollPane sp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setPreferredSize(new Dimension(width, height));
		sp.getVerticalScrollBar().setUnitIncrement(8);
		sp.setBorder(null);
		
		JPanel p2 = new JPanel(new BorderLayout());
		p2.setPreferredSize(new Dimension(width, height));
		p2.setBorder(blackline);
		p2.add(sp, BorderLayout.CENTER);

		if (nbSelected == 0) {
			JLabel message = new JLabel(" Vous n'avez pas encore selectionez de formes.");
			message.setHorizontalAlignment(JLabel.CENTER);
			p2.add(message, BorderLayout.CENTER);
		}
		
		return p2;
	}

	private JPanel initColorPanel() {
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		JPanel main = new JPanel(new BorderLayout()); 
		main.setPreferredSize(new Dimension(300, 370));
		main.setBackground(Color.WHITE);
		main.setBorder(blackline);
		
		JPanel cp = new JPanel();
		cp.setPreferredSize(new Dimension(300, 370));
		cp.setBackground(Color.WHITE);
		cp.setLayout(new GridLayout(5, 2));

		initColor(cp, new Color(244, 67, 54));
		initColor(cp, new Color(33, 150, 243));
		initColor(cp, new Color(156, 39, 176));
		initColor(cp, new Color(236, 64, 122));
		initColor(cp, new Color(139, 195, 74));
		initColor(cp, new Color(255, 235, 59));
		initColor(cp, new Color(255, 152, 0));
		initColor(cp, new Color(121, 85, 72));
		initColor(cp, new Color(158, 158, 158));
		initColor(cp, new Color(0, 0, 0));
		
		JPanel messagePane = new JPanel();
		messagePane.setBackground(Color.WHITE);
		panels.put("messageColorPane", messagePane);
		
		main.add(cp, BorderLayout.PAGE_START);
		main.add(messagePane, BorderLayout.PAGE_END);
		
		return main;
	}
	
	//fonction asupprimer par la suite (remplacer par boucle for)
	private void initColor(JPanel jp, Color c) {
		JPanel colorContainer = new JPanel();
		colorContainer.setBackground(Color.WHITE);
		
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
		couleurCheckBox.put(cb, c.toString());
		
		colorContainer.add(colorIcon, BorderLayout.NORTH);
		colorContainer.add(cb, BorderLayout.SOUTH);
		jp.add(colorContainer);
	}
	
	private void refreshColorPanel() {
		JPanel message = panels.get("messageColorPane");
		message.removeAll();
		int nbSelected = 0;
		for (Map.Entry<JCheckBox, String> value : couleurCheckBox.entrySet()) {
			if (value.getKey().isSelected()) {
				nbSelected++;
			}
			value.getKey().setEnabled(true);
	    }
		if (nbSelected >= MAX_SELECTED_COULEURS) {
			for (Map.Entry<JCheckBox, String> value : couleurCheckBox.entrySet()) {
				if (!value.getKey().isSelected()) {
					value.getKey().setEnabled(false);
				}
		    }
			JLabel label = new JLabel("Nombre maximum de couleurs atteint.");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setForeground(Color.RED);
			message.add(label);
		}
		commit();
	}
	
	private JPanel initFormesPanel() {
		int width = 300, height = 400;

		Box box = Box.createVerticalBox();
		Border blackline = BorderFactory.createLineBorder(Color.black);

		File[] images = Listing.liste("images");
		for (File image : images) {
			box.add(formFrame(image, width - 20, 100, true));
		}

		JScrollPane sp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setPreferredSize(new Dimension(width, height - 30));
		sp.getVerticalScrollBar().setUnitIncrement(8);
		sp.setBorder(blackline);

		JButton b = new JButton("Ajouter une nouvelle image");
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFileChooser();
			}
		});
		
		JPanel p2 = new JPanel(new BorderLayout());
		p2.setPreferredSize(new Dimension(width, height));
		p2.add(sp, BorderLayout.NORTH);
		p2.add(b, BorderLayout.SOUTH); 
		
		return p2;
	}
	
	public JPanel formFrame(File image, int width, int height, boolean withCheckBox) {
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		JPanel tmp = new JPanel();
		tmp.setPreferredSize(new Dimension(width, height));
		tmp.setMaximumSize(new Dimension(width, height));
		tmp.setBackground(Color.WHITE);
		tmp.setBorder(blackline);
		tmp.setLayout(new BorderLayout());

		String path = image.getAbsolutePath();
		JLabel i = new JLabel(new ImageIcon(path));
		i.setMaximumSize(new Dimension(IMG_SIZE, IMG_SIZE));
		i.setPreferredSize(new Dimension(IMG_SIZE, IMG_SIZE));
		i.setMinimumSize(new Dimension(IMG_SIZE, IMG_SIZE));
		tmp.add(i, BorderLayout.LINE_START);
		
		JLabel name = new JLabel(image.getName().toUpperCase().split("\\.")[0]);
		name.setHorizontalAlignment(JLabel.CENTER);
		tmp.add(name, BorderLayout.CENTER);
		
		if (withCheckBox) {
			JCheckBox cb = new JCheckBox();
			cb.setMargin(new Insets(0, 0, 0, 15));
			cb.setBackground(Color.WHITE);
			tmp.add(cb, BorderLayout.LINE_END);
			cb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshSelectedFormePanel();	
				}
			});
			
			formeCheckBox.put(cb, image.getName());
		}
		
		return tmp;
	}
	
	private void refreshSelectedFormePanel() {
		panels.get("adminTopPane").remove(panels.get("selectedFormes"));
		panels.replace("selectedFormes", initSelectedFormesPanel());
		panels.get("adminTopPane").add(panels.get("selectedFormes"), ATP_FORMES_SELECT_PANEL);
		commit();
	}
	
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Ajouter une image");
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new ImageFilter());
		
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			saveImage(fc.getSelectedFile());
			JOptionPane.showMessageDialog(this, "Votre image a bien été enregistré.", "Information", JOptionPane.INFORMATION_MESSAGE);
			refreshFormesPanel();
		}
	}
	
	private void saveImage(File image) {
		try {
			BufferedImage i = ImageIO.read(image);
			String imageName = image.getName().split("\\.")[0];
			
			//systeme pour empecher l'ecrasement d'image
			File f = new File("images/" + imageName + ".png");
			int j = 1;
			while (f.exists()) {
				f = new File("images/" + imageName + j + ".png");
				j++;
			}
			
			ImageIO.write(ImageHelper.imageToBufferedImage(i.getScaledInstance(IMG_SIZE, IMG_SIZE, Image.SCALE_SMOOTH)), "png", f);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Un probleme est survenue lors de l'importaion de l'image");
		}
	}
	
	private void refreshFormesPanel() {
		panels.get("adminTopPane").remove(panels.get("formesPane1"));
		panels.replace("formesPane1", initFormesPanel());
		panels.get("adminTopPane").add(panels.get("formesPane1"), ATP_FORMES_PANEL);
		commit();
	}

	
	private void commit() {
		panels.get("adminTopPane").revalidate();
		panels.get("contentPane").revalidate();
		panels.get("contentPane").repaint();
	}

	public static void main(String args[]) {
		new AdminFenetre(1100, 600, "Administration - Test");
	}

}
