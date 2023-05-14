package rngGame.tile;

import java.io.InputStream;
import java.util.*;

import javafx.scene.image.*;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Tile.
 */
public class Tile {

	/** The images. */
	public Image[] images;

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
	 * @param path the image
	 * @param gp the gp
	 */
	public Tile(String name, String path, GamePanel gp) {

		this.name = name;
		
		images = ImgUtil.getScaledImages(gp, path,48,48);


	}

	/**
	 * Update.
	 */
	public void update() {
		if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
			spriteCounter = System.currentTimeMillis();
			spriteNum++;
		}
		if (spriteNum >= images.length) spriteNum = 0;
	}

}
