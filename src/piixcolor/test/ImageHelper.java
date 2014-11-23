package piixcolor.test;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class ImageHelper {

	public static void filterImage(BufferedImage img, Color c) {
		int w = img.getWidth();
		int h = img.getHeight();
		int pixels[] = new int[w * h];
		img.getRGB(0, 0, w, h, pixels, 0, w);
		for (int i = 0; i < w * h; i++) {
			int a = (pixels[i] & 0xFF000000) >> 24;
			int r = (pixels[i] & 0xFF0000) >> 16;
			int g = (pixels[i] & 0xFF00) >> 8;
			int b = (pixels[i] & 0xFF);

			pixels[i] = ((int) (r + g + b) / 3) < 190 ? c.getRGB() : 0x00FFFFFF;
			
		}
		img.setRGB(0, 0, w, h, pixels, 0, w);
	}
	
	public static BufferedImage changeMode(BufferedImage in, int newMode) {
		BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), newMode);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(in, 0, 0, null);
		g.dispose();
		return newImage;
	}

	public static void main(String args[]) {
		try {

			BufferedImage in = ImageIO.read(new File("test.jpg"));
			BufferedImage newImage = changeMode(in, BufferedImage.TYPE_INT_ARGB);

			ImageHelper.filterImage(newImage, Color.BLUE);

			File outputfile = new File("saved.png");
			ImageIO.write(newImage, "PNG", outputfile);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
