package piixcolor.vue;
import java.awt.FlowLayout;

import javax.swing.JButton;

import piixcolor.controleur.AdminControleur;

public class VueAccueil extends Vue {

	private JButton playButton = new JButton("Jouer");
	private JButton adminButton = new JButton("Espace enseignant");

	public VueAccueil(Fenetre fenetre) {
		super(fenetre, null);

		setLayout(new FlowLayout());

		playButton.addActionListener(new SwitchViewListener(new VuePlateau(fenetre)));
		adminButton.addActionListener(new SwitchViewListener(new VueAdmin(fenetre, new AdminControleur(null))));

		add(playButton);
		add(adminButton);
	}

}
