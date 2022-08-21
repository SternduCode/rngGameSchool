package rngGame.entity;

import java.util.List;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.SpielPanel;

public class Demon extends NPC {

	protected String dir;

	public Demon(Demon npc, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, npcs, cm, requestorN);
	}

	public Demon(JsonObject npc, SpielPanel gp, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, gp, "demons/" + ((StringValue) npc.get("dir")).getValue(),npcs, cm, requestorN);
		dir = ((StringValue) npc.get("dir")).getValue();
	}

	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = (JsonObject) super.toJsonValue();
		jo.put("dir", dir);
		return jo;
	}

}
