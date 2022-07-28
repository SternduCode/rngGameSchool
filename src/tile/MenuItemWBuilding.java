package tile;

import buildings.Building;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class MenuItemWBuilding extends MenuItem {

	private final Building building;

	public MenuItemWBuilding(String text, Node graphic, Building building) {
		super(text, graphic);
		this.building = building;
	}

	public Building getBuilding() { return building; }

}
