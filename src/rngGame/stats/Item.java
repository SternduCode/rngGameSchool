package rngGame.stats;

import java.io.*;

import javafx.scene.image.Image;

// TODO: Auto-generated Javadoc
/**
 * The Class Item.
 */
public class Item {

	/** The t 1. */
	protected Image t1;

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
		try {
			t1 = new Image(new FileInputStream(new File("./res/Items/" + ordner, item + ".png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.rarity = rarity;

	}

	/**
	 * Gets the rarity.
	 *
	 * @return the rarity
	 */
	public Rarity getRarity() { return rarity; }

	/**
	 * Gets the t1.
	 *
	 * @return the t1
	 */
	public Image getT1() { return t1; }

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Item [t1=" + t1 + "]";
	}
}
