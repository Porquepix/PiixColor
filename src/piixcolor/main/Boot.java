package piixcolor.main;

import piixcolor.vue.Fenetre;
import piixcolor.vue.VueAccueil;

public class Boot {

	public static void main(String[] args) {
		Fenetre f = Fenetre.getInstance();
		f.switchPanel(new VueAccueil(f));	
	}

}
