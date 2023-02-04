package rngGame.tile;

import java.io.InputStream;
import java.util.*;

import javafx.scene.image.*;
import rngGame.main.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Tile.
 */
public class Tile {

	/** The images. */
	public List<Image> images;

	/** The poly. */
	public List<Double> poly;

	/** The name. */
	public final String name;

	/** The fps. */
	public double fps = 7.5;

	/** The sprite counter. */
	public long spriteCounter = 0;

	/** The sprite num. */
	public int spriteNum = 0;

	/**
	 * Instantiates a new tile.
	 *
	 * @param name the name
	 * @param image the image
	 * @param gp the gp
	 */
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

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() { return "Tile [name=" + name + ", fps=" + fps + "]"; }

	/**
	 * Update.
	 */
	public void update() {
		if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
			spriteCounter = System.currentTimeMillis();
			spriteNum++;
		}
		if (spriteNum >= images.size()) spriteNum = 0;

	}

}
