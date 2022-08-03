package rngGame.buildings;

import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.entity.Player;
import rngGame.main.SpielPanel;

public class House extends Building {

	protected House(SpielPanel gp) {
		super(gp);
		currentKey = "closed";
	}

	public House(House building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		currentKey = "closed";
	}

	public House(JsonObject building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		currentKey = "closed";
	}

	@Override
	public void update() {
		super.update();

		Player p = gp.getPlayer();

		if (x + reqWidth / 2 - p.worldX < 105 && x + reqWidth / 2 - p.worldX > -45 &&
				y + reqHeight / 2 - p.worldY < -10 && y + reqHeight / 2 - p.worldY > -135)
			currentKey = "open";
		else currentKey = "closed";
		if (x + reqWidth / 2 - p.worldX < 105 && x + reqWidth / 2 - p.worldX > -45 &&
				y + reqHeight / 2 - p.worldY < -10 && y + reqHeight / 2 - p.worldY > -65)
			if (map != null) gp.setMap("/res/maps/" + map);
	}

}
