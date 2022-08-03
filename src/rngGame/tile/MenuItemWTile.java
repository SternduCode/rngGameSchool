package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class MenuItemWTile extends MenuItem {

	private final Tile tile;

	public MenuItemWTile(String text, Node graphic, Tile tile) {
		super(text, graphic);
		this.tile = tile;
	}

	public Tile getTile() { return tile; }

}
