package buildings;

import java.util.List;
import com.sterndu.json.JsonObject;
import entity.Player;
import rngGAME.SpielPanel;

public class House extends Building {

	protected House() {
		currentKey = "closed";
	}

	public House(House building, List<Building> buildings, SpielPanel gp) {
		super(building, buildings, gp);
		currentKey = "closed";
	}

	public House(JsonObject building, List<Building> buildings) {
		super(building, buildings);
		currentKey = "closed";
	}

	@Override
	public void update(Player p, SpielPanel gp) {
		super.update(p, gp);
		if (x + reqWidth / 2 - p.worldX < 105 && x + reqWidth / 2 - p.worldX > -45 &&
				y + reqHeight / 2 - p.worldY < -10 && y + reqHeight / 2 - p.worldY > -135)
			currentKey = "open";
		else currentKey = "closed";
		if (x + reqWidth / 2 - p.worldX < 105 && x + reqWidth / 2 - p.worldX > -45 &&
				y + reqHeight / 2 - p.worldY < -10 && y + reqHeight / 2 - p.worldY > -65)
			if (map != null) gp.setMap("/res/maps/" + map);
	}

}
