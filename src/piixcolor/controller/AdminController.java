package piixcolor.controller;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import piixcolor.model.Model;
import piixcolor.test.ImageHelper;
import piixcolor.util.Config;

public class AdminController extends Controller {

	public AdminController(Model m) {
		super(m);
	}
	
	public void saveImage(File image) {
		try {
			BufferedImage i = ImageIO.read(image);
			String imageName = image.getName().split("\\.")[0];
			
			//systeme pour empecher l'ecrasement d'image
			File f = new File("images/" + imageName + ".png");
			int j = 1;
			while (f.exists()) {
				f = new File("images/" + imageName + j + ".png");
				j++;
			}
			
			ImageIO.write(ImageHelper.imageToBufferedImage(i.getScaledInstance(Config.IMG_SIZE, Config.IMG_SIZE, Image.SCALE_SMOOTH)), "png", f);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Un probleme est survenue lors de l'importaion de l'image");
		}
	}
	

}
