package rngGame.buildings;

import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import rngGame.main.*;

public class Building extends GameObject {

	public Building(Building building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
	}

	public Building(JsonObject building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, "building", buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
	}

	public Building(JsonObject building, SpielPanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
	}

	@Override
	public void update() {
		super.update();

		if (isVisible() && gp.getKeyH().b) setVisible(false);
	}

}