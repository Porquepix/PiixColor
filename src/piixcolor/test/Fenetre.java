package piixcolor.test;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Fenetre extends JFrame {
	
	private JPanel p;
	
	public Fenetre(int width, int height, String title) {
		super();
		
		setPreferredSize(new Dimension(width, height));
		setTitle(title);
		
		p = new JPanel();
		getContentPane().add(p);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		requestFocus();
		pack();
		
	}
	

	public JPanel getPanel() {
		return p;
	}

	public static void main(String args[]) {
		new Fenetre(500, 200, "Test");
		
		//Listing.liste("images");
	}
	
}
