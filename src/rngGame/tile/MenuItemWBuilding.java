package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import rngGame.buildings.Building;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuItemWBuilding.
 */
public class MenuItemWBuilding extends MenuItem {

	/** The building. */
	private final Building building;

	/**
	 * Instantiates a new menu item W building.
	 *
	 * @param text the text
	 * @param graphic the graphic
	 * @param building the building
	 */
	public MenuItemWBuilding(String text, Node graphic, Building building) {
		super(text, graphic);
		this.building = building;
		setStyle("-fx-font-size: 20;");
	}

	/**
	 * Gets the building.
	 *
	 * @return the building
	 */
	public Building getBuilding() { return building; }

}
