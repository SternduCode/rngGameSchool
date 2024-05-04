package rngGame.stats;

import javafx.scene.image.Image;
import rngGame.tile.ImgUtil;

public class Item {

	public static final Item NO_ITEM = new Item("Use", "noItem", Rarity.COMMON);

	private Rarity rarity;

	private String path;

	public Item(String ordner, String item, Rarity rarity) {
		this.rarity = rarity;

		path = "./res/Items/" + ordner + "/" + item + ".png";
	}

	protected void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	protected void setPath(String path) {
		this.path = path;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public String getPath() {
		return path;
	}

	public Image getImage() {
		return ImgUtil.getScaledImage(path);
	}

}
