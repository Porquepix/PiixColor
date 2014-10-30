package pixcolor.test;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Fenetre extends JFrame {
	
	private Panel p;
	
	public Fenetre(int width, int height, String title) {
		super();
		
		setPreferredSize(new Dimension(width, height));
		setTitle(title);
		
		p = new Panel();
		getContentPane().add(p);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		requestFocus();
		pack();
		
		openFileChooser();
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
			ImageIO.write(ImageHelper.imageToBufferedImage(i.getScaledInstance(100, 100, Image.SCALE_SMOOTH)), "png", new File("images/" + file.getName()));
			System.out.println("Image sauvegardée.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Un probleme est survenue lors de l'importaion de l'image");
		}
	}

	public Panel getPanel() {
		return p;
	}

	public static void main(String args[]) {
		new Fenetre(500, 200, "Test");
		
		//Listing.liste("images");
	}
	
}
