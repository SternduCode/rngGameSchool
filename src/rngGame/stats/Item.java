package rngGame.stats;

import javafx.scene.image.Image;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public class Item {

	/** The path. */
	protected String path;

	/** The rarity. */
	protected Rarity rarity;

	/**
	 * Instantiates a new item.
	 *
	 * @param ordner the ordner
	 * @param item   the item
	 * @param rarity the rarity
	 */
	public Item(String ordner, String item, Rarity rarity) {
		path		= "./res/Items/" + ordner + "/" + item + ".png";
		this.rarity	= rarity;

	}

	/**
	 * Gets the t1.
	 *
	 * @param gp the gp
	 * @return the t1
	 */
	public Image getImage(GamePanel gp) { return ImgUtil.getScaledImage(gp, path); }

	/**
	 * Gets the rarity.
	 *
	 * @return the rarity
	 */
	public Rarity getRarity() { return rarity; }


	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Item [path=" + path + ", rarity=" + rarity + "]";
	}
}
