package piixcolor.utilitaire;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ObjetColore {

	private Couleur couleur;
	private BufferedImage image;
	private File origineFile;

	public ObjetColore(Couleur couleur, File image) {
		this.couleur = couleur;
		this.origineFile = image;
		try {
			this.image = ImageIO.read(image);
			this.filtreImage(couleur);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void filtreImage(Couleur c) {
		this.changeImageMode(BufferedImage.TYPE_INT_ARGB);

		int w = image.getWidth();
		int h = image.getHeight();
		int pixels[] = new int[w * h];
		image.getRGB(0, 0, w, h, pixels, 0, w);

		for (int i = 0; i < w * h; i++) {
			int a = (pixels[i] & 0xFF000000) >> 24;
			int r = (pixels[i] & 0xFF0000) >> 16;
			int g = (pixels[i] & 0xFF00) >> 8;
			int b = (pixels[i] & 0xFF);

			int threshhold = 230;

			if (r > threshhold && b > threshhold && g > threshhold) {
				pixels[i] = 0x00FFFFFF;
			} else {
				pixels[i] = (a << 24) | (c.getCouleur().getRGB() & 0x00FFFFFF);
			}
		}

		image.setRGB(0, 0, w, h, pixels, 0, w);
	}

	public void changeImageMode(int newMode) {
		BufferedImage newImage = new BufferedImage(image.getWidth(),
				image.getHeight(), newMode);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		this.image = newImage;
	}

	public Couleur getCouleur() {
		return couleur;
	}

	public BufferedImage getImage() {
		return image;
	}

	/**
	 * @return the origineFile
	 */
	public File getOrigineFile() {
		return origineFile;
	}

	public boolean equals(Object o) {
		if (o.getClass() != this.getClass()) {
			return false;
		}
		ObjetColore oc = (ObjetColore) o;
		return this.getOrigineFile().equals(oc.getOrigineFile())
				&& this.getCouleur().equals(oc.getCouleur());
	}
	
	public String toString() {
		return origineFile.getName() + " " + couleur.toString();
	}

}
