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

	/**
	 * Instantiates a new item.
	 *
	 * @param ordner the ordner
	 * @param item   the item
	 */
	public Item(String ordner, String item) {
		try {
			t1 = new Image(new FileInputStream(new File("./res/Items/" + ordner, item + ".png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

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
