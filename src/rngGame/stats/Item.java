package rngGame.stats;

import javafx.scene.image.Image;
import rngGame.tile.ImgUtil;

/**
 * The Class Item.
 */
public class Item {

	/** The path. */
	protected String path;

	/** The rarity. */
	protected Rarity rarity;

	public static final Item NOITEM = new Item("Use", "noItem", Rarity.COMMON);

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
	 * @return the t1
	 */
	public Image getImage() { return ImgUtil.getScaledImage(path); }

	/**
	 * Gets the rarity.
	 *
	 * @return the rarity
	 */
	public Rarity getRarity() { return rarity; }

	public String getPath() { return path; }


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
