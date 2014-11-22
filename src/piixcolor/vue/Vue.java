package piixcolor.vue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import piixcolor.controleur.Controleur;


public abstract class Vue extends JPanel {
	
	protected Fenetre fenetre;
	private Controleur controller;
	
	public Vue(Fenetre fenetre, Controleur controleur) {
		this.fenetre = fenetre;
		this.controller = controller;
		setSize(fenetre.getWidth(), fenetre.getHeight());
	}
	
	public Controleur getControleur() {
		return this.controller;
	}

	class SwitchViewListener implements ActionListener{
		
		private Vue v;
		
		public SwitchViewListener(Vue v) {
			this.v = v;
		}
		
	    public void actionPerformed(ActionEvent e) {
	      fenetre.switchPanel(v);
	    }    
	    
	}

}
