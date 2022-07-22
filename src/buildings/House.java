package buildings;

import com.sterndu.json.JsonObject;
import entity.Player;
import rngGAME.SpielPanel;

public class House extends Building {

	public House(JsonObject building) {
		super(building);
		setImage(images.get("closed").get(0));
	}

	@Override
	public void update(Player p, SpielPanel gp) {
		super.update(p, gp);
		if (x + reqWidth / 2 - p.worldX < 105 && x + reqWidth / 2 - p.worldX > -45 &&
				y + reqHeight / 2 - p.worldY < -10 && y + reqHeight / 2 - p.worldY > -135)
			setImage(images.get("open").get(0));
		else setImage(images.get("closed").get(0));
	}

}
