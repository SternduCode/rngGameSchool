package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuItemWTile.
 */
public class MenuItemWTile extends MenuItem {

	/** The tile. */
	private final Tile tile;

	/**
	 * Instantiates a new menu item W tile.
	 *
	 * @param text the text
	 * @param graphic the graphic
	 * @param tile the tile
	 */
	public MenuItemWTile(String text, Node graphic, Tile tile) {
		super(text, graphic);
		this.tile = tile;
		setStyle("-fx-font-size: 20;");
	}

	/**
	 * Gets the tile.
	 *
	 * @return the tile
	 */
	public Tile getTile() { return tile; }

}
