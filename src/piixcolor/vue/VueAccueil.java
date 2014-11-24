package piixcolor.vue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import piixcolor.controleur.AdminControleur;

public class VueAccueil extends Vue {

	private JButton playButton = new JButton("Jouer");
	private JButton adminButton = new JButton("Espace enseignant");

	public VueAccueil(Fenetre f) {
		super(f, null, null);

		setLayout(new FlowLayout());

		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenetre.switchPanel(new VuePlateau(fenetre));
			}
		});
		adminButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenetre.switchPanel(new VueAdmin(fenetre, new AdminControleur(null)));
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
