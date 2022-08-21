package rngGame.buildings;

import java.util.List;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.entity.Player;
import rngGame.main.SpielPanel;

public class House extends Building {

	protected String map;

	public House(House building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		map = building.map;
		currentKey = "closed";
	}

	public House(JsonObject building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		if (building.containsKey("map")) map = ((StringValue) building.get("map")).getValue();
		currentKey = "closed";
	}

	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = (JsonObject) super.toJsonValue();
		if (map != null) jo.put("map", map);
		return jo;

	}

	@Override
	public void update() {
		super.update();

		Player p = gp.getPlayer();

		if (x + reqWidth / 2 - p.getX() < 105 && x + reqWidth / 2 - p.getX() > -45 &&
				y + reqHeight / 2 - p.getY() < -10 && y + reqHeight / 2 - p.getY() > -135)
			currentKey = "open";
		else currentKey = "closed";
		if (x + reqWidth / 2 - p.getX() < 105 && x + reqWidth / 2 - p.getX() > -45 &&
				y + reqHeight / 2 - p.getY() < -10 && y + reqHeight / 2 - p.getY() > -65)
			if (map != null) gp.setMap("./res/maps/" + map);
	}

}
