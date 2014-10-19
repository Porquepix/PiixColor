package pixcolor.test;

import java.awt.Dimension;
import java.awt.Panel;
import java.io.File;

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
	
	private void openFileChooser() {
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Ajouter une image");
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new ImageFilter());
		
		int returnVal = fc.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println(fc.getSelectedFile().getName());
		}
	}

	public Panel getPanel() {
		return p;
	}

	public static void main(String args[]) {
		new Fenetre(500, 200, "Test");
	}
	
}
