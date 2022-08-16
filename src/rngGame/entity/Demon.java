package rngGame.entity;

import java.util.List;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.SpielPanel;

public class Demon extends NPC {

	protected String dir;

	public Demon(Demon npc, SpielPanel gp, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<? extends Entity> requestorN) {
		super(npc, gp, npcs, cm, requestorN);
	}

	public Demon(JsonObject npc, SpielPanel gp, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<? extends Entity> requestorN) {
		super(npc, gp, npcs, cm, requestorN);
	}

	@Override
	protected void init(JsonObject npc, List<NPC> npcs, ContextMenu cm, ObjectProperty<? extends Entity> requestorN) {
		dir = ((StringValue) npc.get("dir")).getValue();
		directory = "demons/" + dir;
		super.init(npc, npcs, cm, requestorN);
	}

	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = (JsonObject) super.toJsonValue();
		jo.put("dir", dir);
		return jo;
	}

}
