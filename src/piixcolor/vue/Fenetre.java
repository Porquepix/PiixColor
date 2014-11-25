package piixcolor.vue;
import javax.swing.JFrame;
import javax.swing.JPanel;

import piixcolor.modele.Modele;


public class Fenetre extends JFrame {
	
	public static int FRAME_WIDTH = 1000;
	public static int FRAME_HEIGHT = 600;
	public static final String FRAME_TITLE = "PiixColor";
	
	private static Fenetre instance = null;

	private Fenetre () {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(FRAME_TITLE);
		setVisible(true);
		pack();
		setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40));
		
		FRAME_WIDTH = this.getWidth();
		FRAME_HEIGHT = this.getHeight();
		
		setLocationRelativeTo(null);
	}
	
	public static Fenetre getInstance() {
		if (instance == null) instance = new Fenetre();
		return instance;
	}

	public void switchPanel(JPanel p) {
		this.getContentPane().removeAll();
		this.setContentPane(p);
		this.getContentPane().revalidate();
	}

}
