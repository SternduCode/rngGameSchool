package rngGame.tile;

import java.io.InputStream;
import java.util.*;
import javafx.scene.image.*;
import rngGame.main.GamePanel;

public class Tile {

	public List<Image> images;
	public List<Double> poly;
	public final String name;
	public double fps = 7.5;

	public Tile(String name, InputStream image, GamePanel gp) {

		this.name = name;

		images = new ArrayList<>();

		Image img = new Image(image);

		for (int i = 0; i < img.getWidth(); i += img.getHeight()) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, (int) img.getHeight(),
					(int) img.getHeight());
			images.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), gp.BgX, gp.BgY));
		}
	}

}
