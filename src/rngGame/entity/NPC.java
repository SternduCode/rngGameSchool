package rngGame.entity;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import rngGame.main.*;

public class NPC extends Entity implements JsonValue {

	private final AtomicBoolean h = new AtomicBoolean(false);

	public NPC(JsonObject npc, GamePanel gp, double speed, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, speed, gp, "npc", npcs, cm, requestorN);
		init();
	}

	public NPC(JsonObject npc, GamePanel gp, double speed, String directory, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, speed, gp, directory, npcs, cm, requestorN);
		init();
	}

	public NPC(JsonObject npc, GamePanel gp, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		super(npc, 0, gp, "npc", npcs, cm, requestorN);
		init();
	}

	public NPC(JsonObject npc, GamePanel gp, String directory, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, 0, gp, directory, npcs, cm, requestorN);
		init();
	}

	public NPC(NPC npc, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		super(npc, npcs, cm, requestorN);
		init();
	}

	protected void init() {
		collisionBoxes.entrySet().parallelStream()
		.forEach(s -> {
			if (s.getValue().getPoints().size() == 0) s.getValue().getPoints().addAll(0d, 0d, 0d,
					images.get(s.getKey()).get(0).getHeight(),
					images.get(s.getKey()).get(0).getWidth(),
					images.get(s.getKey()).get(0).getHeight(), images.get(s.getKey()).get(0).getWidth(),
					0d);
		});
		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 1, 1, 0.75)));
		Input.getInstance().setKeyHandler("h" + hashCode(), mod -> {
			h.set(!h.get());
		}, KeyCode.H, false);
	}

	@Override
	public void update(long milis) {
		super.update(milis);

		if (isVisible() && h.get()) setVisible(false);
	}

}
