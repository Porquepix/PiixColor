package piixcolor.vue;

import java.awt.Color;
import java.awt.event.ActionEvent;

public class VuePlateau extends Vue {
	
	public static Color BG_PLATEAU = Color.RED;

	public VuePlateau(Fenetre fenetre) {
		super(fenetre, null);

		setBackground(BG_PLATEAU);
	}

	public void handleAction(ActionEvent e) {}

}
