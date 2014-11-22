package piixcolor.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class VuePlateau extends Vue implements MouseListener, MouseMotionListener {
	JLayeredPane layeredPane;
	JPanel matrice;
	JLabel formeCourante;
	Container caseFormeCourante;
	int ajustementX;
	int ajustementY;
	int nbcouleur = 3;
	int nbforme = 3;

	public VuePlateau(Fenetre fenetre) {
		super(fenetre, null, null);
		// Utilisation du JLayeredPane
		Dimension dimensionVue = new Dimension(fenetre.FRAME_WIDTH, fenetre.FRAME_HEIGHT);
		layeredPane = new JLayeredPane();
		this.add(layeredPane);
		layeredPane.setPreferredSize(dimensionVue);

		layeredPane.addMouseListener(this);
		layeredPane.addMouseMotionListener(this);

		// Ajout de la matrice
		matrice = new JPanel();
		layeredPane.add(matrice, JLayeredPane.DEFAULT_LAYER);
		matrice.setPreferredSize(new Dimension(dimensionVue.width, dimensionVue.height));
		matrice.setLayout(new GridLayout((nbcouleur*2) + 1, nbforme + 1));
		matrice.setBounds(0, 0, dimensionVue.width, dimensionVue.height);
		
		for (int i = 0; i < ((nbcouleur*2) + 1) * (nbforme + 1); i++) {
			JPanel square = new JPanel(new BorderLayout());
			matrice.add(square);
			square.setBackground(Color.white);
			if(i < (nbcouleur+1) * (nbforme +1)) {
				square.setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}

		//###############Ajout des formes et couleurs de la matrice##############################
		
		JLabel vide = new JLabel();
		JPanel panel = (JPanel) matrice.getComponent(0);
		panel.add(vide);
		
		//Ajout couleurs :
		JLabel image = new JLabel(new ImageIcon("images/blue.png"));
		panel =  (JPanel) matrice.getComponent(4);
		panel.add(image);
		
		image = new JLabel(new ImageIcon("images/red.png"));
		panel = (JPanel) matrice.getComponent(8);
		panel.add(image);
		
		
		image = new JLabel(new ImageIcon("images/green.png"));
		panel = (JPanel) matrice.getComponent(12);
		panel.add(image);
		
		//Ajout formes :
		image = new JLabel(new ImageIcon("images/triangle.png"));
		panel = (JPanel) matrice.getComponent(1);
		panel.add(image);
		
		image = new JLabel(new ImageIcon("images/cercle.png"));
		panel = (JPanel) matrice.getComponent(2);
		panel.add(image);
		
		image = new JLabel(new ImageIcon("images/carre.png"));
		panel = (JPanel) matrice.getComponent(3);
		panel.add(image);
		//####################################################################################
		
		//###############Ajout des formes colorés à "formes"##############################
		
		//Ajout couleurs :
		JLabel objetColore = new JLabel(new ImageIcon("images/carreRouge.png"));
		panel =  (JPanel) matrice.getComponent(17);
		panel.add(objetColore);
		
		objetColore = new JLabel(new ImageIcon("images/cercleVert.png"));
		panel = (JPanel) matrice.getComponent(18);
		panel.add(objetColore);
		
		objetColore = new JLabel(new ImageIcon("images/triangleVert.png"));
		panel = (JPanel) matrice.getComponent(19);
		panel.add(objetColore);
		
		objetColore = new JLabel(new ImageIcon("images/carreBleu.png"));
		JPanel panel1 =  (JPanel) matrice.getComponent(21);
		panel1.add(objetColore);
		
		objetColore = new JLabel(new ImageIcon("images/triangleRouge.png"));
		panel1 = (JPanel) matrice.getComponent(22);
		panel1.add(objetColore);
		
		objetColore = new JLabel(new ImageIcon("images/cercleBleu.png"));
		panel1 = (JPanel) matrice.getComponent(23);
		panel1.add(objetColore);
		//####################################################################################
		
	}
	
	public void init() {
		
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
			if(emplacementParent.getY() >= matrice.getComponent((nbcouleur+1)*(nbforme+1)).getLocation().getY()) {
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
			if (c instanceof JLabel || c == null) {
				caseFormeCourante.add(formeCourante);
			} else {
				Container parent = (Container) c;
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
