package piixcolor.utilitaire;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ObjetColore  {
	
	private Couleur couleur;
	private BufferedImage image;
	
	public ObjetColore(Couleur couleur, BufferedImage img) {
		this.couleur = couleur;
		this.image = img;
		this.filtreImage(couleur);
	}
	
	public void filtreImage(Couleur c) {
		this.changeMode(BufferedImage.TYPE_INT_ARGB);
		
		int w = image.getWidth();
		int h = image.getHeight();
		int pixels[] = new int[w * h];
		image.getRGB(0, 0, w, h, pixels, 0, w);
		for (int i = 0; i < w * h; i++) {
			int r = (pixels[i] & 0xFF0000) >> 16;
			int g = (pixels[i] & 0xFF00) >> 8;
			int b = (pixels[i] & 0xFF);

			pixels[i] = ((int) (r + g + b) / 3) < 190 ? c.getCouleur().getRGB() : 0x00FFFFFF;
			
		}
		
		image.setRGB(0, 0, w, h, pixels, 0, w);
	}
	
	public BufferedImage changeMode(int newMode) {
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), newMode);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public Couleur getCouleur() {
		return couleur;
	}

	public BufferedImage getImage() {
		return image;
	}

}
