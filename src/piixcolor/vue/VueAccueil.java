package piixcolor.vue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import piixcolor.controleur.AdminControleur;
import piixcolor.controleur.PlateauControleur;
import piixcolor.modele.Modele;

public class VueAccueil extends Vue {

	private JButton playButton = new JButton("Jouer");
	private JButton adminButton = new JButton("Espace enseignant");

	public VueAccueil(Fenetre f) {
		super(f, null);
		setLayout(new FlowLayout());

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Modele m = Modele.getInstance();
				PlateauControleur pc = new PlateauControleur(m);
				fenetre.switchPanel(new VuePlateau(fenetre, pc));
			}
		});
		
		adminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Modele m = Modele.getInstance();
				AdminControleur ac = new AdminControleur(m);
				fenetre.switchPanel(new VueAdmin(fenetre, ac));
			}
		});

		add(playButton);
		add(adminButton);
	}

	@Override
	public void actualise() {
		// TODO Auto-generated method stub
		
	}

}
