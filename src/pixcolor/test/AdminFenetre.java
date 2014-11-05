package pixcolor.test;

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

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

public class AdminFenetre extends JFrame {

	private HashMap<String, JPanel> panels;

	public AdminFenetre(int width, int height, String title) {
		super();

		//init frame
		setPreferredSize(new Dimension(width, height));
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		//init hashmap + madel
		panels = new HashMap<String, JPanel>();
		panels.put("contentPane", new JPanel());
		panels.put("adminTopPane", new JPanel(new GridLayout(1, 3, 15, 0)));
		panels.put("formesPane1", initFormPane());
		panels.put("colorsPane1", initColorPane());
		
		//connection panel
		getContentPane().add(panels.get("contentPane"));
		panels.get("adminTopPane").add(panels.get("formesPane1"), 0);
		panels.get("adminTopPane").add(panels.get("colorsPane1"), 1);
		panels.get("contentPane").add(panels.get("adminTopPane"), BorderLayout.PAGE_START);

		//end init frame
		requestFocus();
		pack();
		setLocationRelativeTo(null);
		
		//display panel
		commit();
	}

	private void commit() {
		panels.get("adminTopPane").revalidate();
		//adminBottomPane.revalidate();
		panels.get("contentPane").revalidate();
		panels.get("contentPane").repaint();
	}

	private JPanel initColorPane() {
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		JPanel cp = new JPanel();
		cp.setPreferredSize(new Dimension(300, 370));
		cp.setBackground(Color.WHITE);
		cp.setBorder(blackline);
		cp.setLayout(new GridLayout(5, 2));

		initColor(cp, Color.red);
		initColor(cp, Color.blue);
		initColor(cp, Color.green);
		initColor(cp, Color.yellow);
		initColor(cp, Color.orange);
		initColor(cp, Color.pink);
		initColor(cp, Color.black);
		initColor(cp, Color.gray);
		
		return cp;
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
		
		colorContainer.add(colorIcon, BorderLayout.NORTH);
		colorContainer.add(cb, BorderLayout.SOUTH);
		jp.add(colorContainer);
	}

	private JPanel initFormPane() {
		int width = 300, height = 400;

		Box box = Box.createVerticalBox();
		Border blackline = BorderFactory.createLineBorder(Color.black);

		File[] images = Listing.liste("images");
		for (File image : images) {
			box.add(formFrame(image, width - 20, 100));
		}

		JScrollPane sp = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
	
	public JPanel formFrame(File image, int width, int height) {
		Border blackline = BorderFactory.createLineBorder(Color.black);
		
		JPanel tmp = new JPanel();
		tmp.setPreferredSize(new Dimension(width, height));
		tmp.setMaximumSize(new Dimension(width, height));
		tmp.setBackground(Color.WHITE);
		tmp.setBorder(blackline);
		tmp.setLayout(new BorderLayout());

		String path = image.getAbsolutePath();
		JLabel i = new JLabel(new ImageIcon(path));
		i.setMaximumSize(new Dimension(100, 100));
		i.setPreferredSize(new Dimension(100, 100));
		i.setMinimumSize(new Dimension(100, 100));
		tmp.add(i, BorderLayout.LINE_START);
		
		JLabel name = new JLabel(image.getName().toUpperCase().split("\\.")[0]);
		name.setHorizontalAlignment(JLabel.CENTER);
		tmp.add(name, BorderLayout.CENTER);
		
		JCheckBox cb = new JCheckBox();
		cb.setMargin(new Insets(0, 0, 0, 15));
		cb.setBackground(Color.WHITE);
		tmp.add(cb, BorderLayout.LINE_END);
		
		return tmp;
	}
	
	/**
	 * Ouvre un explorateur de fichier
	 * Si un fichier est sélectionné appelle la fonction pour le sauvegarder dans le programme
	 */
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Ajouter une image");
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new ImageFilter());
		
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			saveImage(fc.getSelectedFile());
			panels.get("adminTopPane").remove(panels.get("formesPane1"));
			panels.replace("formesPane1", initFormPane());
			panels.get("adminTopPane").add(panels.get("formesPane1"), 0);
			commit();
		}
	}
	
	/**
	 * Enregistre une image en interne
	 * 
	 * @param file image a enregistré
	 */
	public void saveImage(File file) {
		try {
			BufferedImage i = ImageIO.read(file);
			ImageIO.write(ImageHelper.imageToBufferedImage(i.getScaledInstance(100, 100, Image.SCALE_SMOOTH)), "png", new File("images/" + file.getName() + ".png"));
			System.out.println("Image sauvegardée.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Un probleme est survenue lors de l'importaion de l'image");
		}
	}

	public static void main(String args[]) {
		new AdminFenetre(700, 500, "Administration - Test");
	}

}
