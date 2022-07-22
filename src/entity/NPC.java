package entity;

import java.util.List;
import com.sterndu.json.*;
import javafx.scene.image.*;

public class NPC extends ImageView {

	private final List<Image> images;
	private final double x, y;
	private final JsonObject npcData;

	protected NPC(double x, double y, JsonObject npcData) {
		images = null;
		this.x =x;
		this.y = y;
		this.npcData =npcData;
	}

	public NPC(JsonObject npc) {
		images = ((JsonArray) npc.get("textures")).stream()
				.map(s -> new Image(getClass().getResourceAsStream(((StringValue) s).getValue()))).toList();
		x = ((DoubleValue) ((JsonArray) npc.get("position")).get(0)).getValue();
		y = ((DoubleValue) ((JsonArray) npc.get("position")).get(0)).getValue();
		npcData = (JsonObject) npc.get("npcData");
	}

}
