package piixcolor.vue;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Fenetre extends JFrame {
	
	public static int FRAME_WIDTH = 1000;
	public static int FRAME_HEIGHT = 600;
	public static final String FRAME_TITLE = "PiixColor";

	public Fenetre () {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(FRAME_TITLE);
		setVisible(true);
		pack();
		setSize((int)getToolkit().getScreenSize().getWidth(), ((int)getToolkit().getScreenSize().getHeight() - 40));
		
		FRAME_WIDTH = this.getWidth();
		FRAME_HEIGHT = this.getHeight();
		
		setContentPane(new VueAccueil(this));
		
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new Fenetre();
		
		/** TEST XML
		
		Config.getInstance().getMatriceCouleur();
		System.out.println(".----------------.");
		Config.getInstance().addMatriceCouleur(3);
		try {
			Config.getInstance().enregistrer(Config.FICHIER_CONFIG);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Config.getInstance().getMatriceCouleur();
		
		**/
	}
	
	public void switchPanel(JPanel p) {
		this.getContentPane().removeAll();
		this.setContentPane(p);
		this.getContentPane().revalidate();
	}

}
