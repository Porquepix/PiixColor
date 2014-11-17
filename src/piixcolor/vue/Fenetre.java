package piixcolor.vue;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Fenetre extends JFrame {
	
	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 600;
	public static final String FRAME_TITLE = "PiixColor";

	public Fenetre () {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(FRAME_TITLE);
		
		setContentPane(new VueAccueil(this));
		
		setVisible(true);
		pack();
		
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Fenetre();

	}
	
	public void switchPanel(JPanel p) {
		this.getContentPane().removeAll();
		this.setContentPane(p);
		this.getContentPane().revalidate();
	}

}
