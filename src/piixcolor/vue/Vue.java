package piixcolor.vue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import piixcolor.controleur.Controller;


public abstract class Vue extends JPanel {
	
	protected Fenetre fenetre;
	private Controller controller;
	
	public Vue(Fenetre fenetre, Controller controller) {
		this.fenetre = fenetre;
		this.controller = controller;
		setSize(fenetre.getWidth(), fenetre.getHeight());
	}
	
	public Controller getController() {
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
