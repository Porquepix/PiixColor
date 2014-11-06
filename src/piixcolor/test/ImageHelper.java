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

	public static BufferedImage imageToBufferedImage(Image im) {
		BufferedImage bi = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics bg = bi.getGraphics();
		bg.drawImage(im, 0, 0, null);
		bg.dispose();
		return bi;
	}

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

	public static void floodFill(BufferedImage image, Color targetColor,
			Color replacementColor) {
		Point node = new Point(0, 0);
		int width = image.getWidth();
		int height = image.getHeight();
		int target = targetColor.getRGB();
		int replacement = replacementColor.getRGB();
		while (node.y < height && image.getRGB(node.x, node.y) != target) {
			if (node.x < width) {
				node.x++;
			} else {
				node.x = 0;
				node.y++;
			}
		}
		if (target != replacement) {
			Deque<Point> queue = new LinkedList<Point>();
			do {
				int x = node.x;
				int y = node.y;
				while (x > 0 && image.getRGB(x - 1, y) == target) {
					x--;
				}
				boolean spanUp = false;
				boolean spanDown = false;
				while (x < width && image.getRGB(x, y) == target) {
					image.setRGB(x, y, replacement);
					if (!spanUp && y > 0 && image.getRGB(x, y - 1) == target) {
						queue.add(new Point(x, y - 1));
						spanUp = true;
					} else if (spanUp && y > 0
							&& image.getRGB(x, y - 1) != target) {
						spanUp = false;
					}
					if (!spanDown && y < height - 1
							&& image.getRGB(x, y + 1) == target) {
						queue.add(new Point(x, y + 1));
						spanDown = true;
					} else if (spanDown && y < height - 1
							&& image.getRGB(x, y + 1) != target) {
						spanDown = false;
					}
					x++;
				}
			} while ((node = queue.pollFirst()) != null);
		}
	}

	public static void main(String args[]) {
		try {

			BufferedImage in = ImageIO.read(new File("test.jpg"));
			BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = newImage.createGraphics();
			g.drawImage(in, 0, 0, null);
			g.dispose();

			ImageHelper.filterImage(newImage, Color.BLUE);

			File outputfile = new File("saved.png");
			ImageIO.write(newImage, "PNG", outputfile);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
