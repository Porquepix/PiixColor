package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import piixcolor.controleur.PlateauControleur;

public class VuePlateau extends Vue implements MouseListener, MouseMotionListener {
	JLayeredPane layeredPane;
	JPanel matrice;
	JLabel formeCourante;
	Container caseFormeCourante;
	int ajustementX;
	int ajustementY;


	public VuePlateau(Fenetre f, PlateauControleur controleur) {
		super(f, controleur);
		
		setLayout(new BorderLayout());

		// Utilisation du JLayeredPane
		Dimension dimensionVue = new Dimension(fenetre.getWidth(), fenetre.getHeight());
		layeredPane = new JLayeredPane();
		this.add(layeredPane);
		layeredPane.setPreferredSize(dimensionVue);
		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);

		// Ajout de la matrice
		matrice = new JPanel();
		layeredPane.add(matrice, JLayeredPane.DEFAULT_LAYER);
		matrice.setPreferredSize(dimensionVue);
		matrice.setLayout(new GridLayout((controleur.getNbCouleur()*2) + 1, controleur.getNbForme() + 1));
		matrice.setBounds(0, 0, dimensionVue.width, dimensionVue.height);
		
		for (int i = 0; i < ((controleur.getNbCouleur()*2) + 1) * (controleur.getNbForme() + 1); i++) {
			JPanel square = new JPanel(new BorderLayout());
			matrice.add(square);
			square.setBackground(Color.white);
			if(i < (controleur.getNbCouleur()+1) * (controleur.getNbForme() +1)) {
				square.setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}

		//###############Ajout des formes et couleurs de la matrice##############################
		
		JLabel image = new JLabel();
		JPanel panel = (JPanel) matrice.getComponent(0);
		panel.add(image);
		
		//Ajout Formes :
		for(int i = 1; i <= controleur.getNbForme(); i++){
			image = new JLabel(new ImageIcon(controleur.getModele().getFormesConfig().get(i-1).getAbsolutePath()));
			panel = (JPanel) matrice.getComponent(i);
			panel.add(image);
		}
		
		//Ajout Couleurs :
		int k = 0;
		for(int i = controleur.getNbForme()+1; i <= (controleur.getNbCouleur()*(controleur.getNbForme()+1)); i = i + controleur.getNbForme() + 1){
			image = new JLabel(new ImageIcon());
			panel =  (JPanel) matrice.getComponent(i);
			panel.setBackground(controleur.getModele().getCouleursConfig().get(k).getCouleur());
			panel.add(image);
			k++;
		}
		
		//###############Ajout des formes colorés à "formes"##############################
		int l = ((controleur.getNbCouleur()+1)*(controleur.getNbForme()+1));
		for(int i = 0; i < controleur.getNbObjetColore(); i++){
			JLabel objetColore = new JLabel(new ImageIcon(controleur.getModele().getReserveForme().get(i).getImage()));
			panel =  (JPanel) matrice.getComponent(l);
			l++;
			panel.add(objetColore);
		}		
	}


	public void mouseDragged(MouseEvent me) {
		if(SwingUtilities.isLeftMouseButton(me)) {
			if (formeCourante == null)
				return;
			formeCourante.setLocation(me.getX() + ajustementX, me.getY() + ajustementY);
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	public void mousePressed(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			formeCourante = null;
			Component c = matrice.findComponentAt(e.getX(), e.getY());
			caseFormeCourante =  c.getParent();
			if (c instanceof JPanel)
				return;
			Point emplacementParent = c.getParent().getLocation();
			if(emplacementParent.getY() >= (matrice.getComponent((((PlateauControleur)(controleur)).getNbCouleur()+1)*(((PlateauControleur)(controleur)).getNbForme()+1))).getLocation().getY()) {
				ajustementX = emplacementParent.x - e.getX();
				ajustementY = emplacementParent.y - e.getY();
				formeCourante = (JLabel) c;
				formeCourante.setLocation(e.getX() + ajustementX, e.getY() + ajustementY);
				formeCourante.setSize(formeCourante.getWidth(), formeCourante.getHeight());
				layeredPane.add(formeCourante, JLayeredPane.DRAG_LAYER);
			}
		}
	}


	public void mouseReleased(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			if (formeCourante == null)
				return;
			formeCourante.setVisible(false);
			Component c = matrice.findComponentAt(e.getX(), e.getY());
			Container parent = (Container) c;
			if (c instanceof JLabel || c == null || parent.getComponentCount() > 0) {
				caseFormeCourante.add(formeCourante);
			} else {
				parent.add(formeCourante);
			}
			formeCourante.setVisible(true);
		}
	}

	public void handleAction(ActionEvent e) {}

	@Override
	public void actualise() {
		// TODO Auto-generated method stub
		
	}

}
