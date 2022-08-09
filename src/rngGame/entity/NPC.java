package rngGame.entity;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.SpielPanel;

public class NPC extends Entity implements JsonValue {

	protected JsonObject npcData;
	protected NPC master;
	protected List<NPC> slaves;
	protected boolean slave;

	protected NPC(JsonObject npc, SpielPanel gp, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN,String directory) {
		this(gp, directory);
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorN.set(NPC.this);
				cm.show(NPC.this, e.getScreenX(), e.getScreenY());
			}
		});
		init(npc, npcs, cm, requestorN);

		npcs.add(this);
		gp.getViewGroup().getChildren().add(this);
	}

	protected NPC(SpielPanel gp, String directory) {
		super(gp, directory, 0);
	}

	public NPC(JsonObject npc, SpielPanel gp, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		this(gp, "npc");
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorN.set(NPC.this);
				cm.show(NPC.this, e.getScreenX(), e.getScreenY());
			}
		});
		init(npc, npcs, cm, requestorN);

		npcs.add(this);
		gp.getViewGroup().getChildren().add(this);
	}

	public NPC(NPC npc, SpielPanel gp, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		this(gp, "npc");
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorN.set(NPC.this);
				cm.show(NPC.this, e.getScreenX(), e.getScreenY());
			}
		});
		x = npc.x;
		y = npc.y;
		origWidth = npc.origWidth;
		origHeight = npc.origHeight;
		reqWidth = npc.reqWidth;
		reqHeight = npc.reqHeight;
		fps = npc.fps;

		background = npc.background;
		currentKey = npc.currentKey;
		images = npc.images;
		iv.setImage(npc.getFirstImage());
		textureFiles = npc.textureFiles;
		npc.collisionBoxes.forEach((key, poly) -> {
			Polygon collisionBox = collisionBoxes.get(key);
			if (collisionBox == null) collisionBoxes.put(key, collisionBox = new Polygon());
			collisionBox.getPoints().clear();
			collisionBox.getPoints().addAll(poly.getPoints());
			collisionBox.setFill(poly.getFill());
		});

		npcData = npc.npcData;

		master = npc;
		slave = true;
		if (npc.slaves == null)
			npc.slaves = new ArrayList<>();
		npc.slaves.add(this);

		npcs.add(this);
		gp.getViewGroup().getChildren().add(this);
	}

	protected void init(JsonObject npc, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		origWidth = ((NumberValue) ((JsonArray) npc.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) npc.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) npc.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) npc.get("requestedSize")).get(1)).getValue().intValue();
		if (npc.containsKey("fps"))
			fps = ((NumberValue) npc.get("fps")).getValue().doubleValue();
		else fps = 7;

		if (npc.containsKey("background"))
			background = ((BoolValue) npc.get("background")).getValue();
		((JsonObject) npc.get("textures")).entrySet().parallelStream()
		.forEach(s -> {
			try {
				getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue());
				if (!collisionBoxes.containsKey(s.getKey())) collisionBoxes.put(s.getKey(), new Polygon());
				if (collisionBoxes.get(s.getKey()).getPoints().size() == 0) collisionBoxes.get(s.getKey()).getPoints().addAll(0d, 0d, 0d,
						images.get(s.getKey()).get(0).getHeight(),
						images.get(s.getKey()).get(0).getWidth(),
						images.get(s.getKey()).get(0).getHeight(), images.get(s.getKey()).get(0).getWidth(),
						0d);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		iv.setImage(getFirstImage());
		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 1, 1, 0.75)));

		npcData = (JsonObject) npc.get("npcData");

		if (((JsonArray) npc.get("position")).get(0) instanceof JsonArray ja) {
			try {
				slaves = new ArrayList<>();
				for (int i = 1; i < ((JsonArray) npc.get("position")).size(); i++) {
					NPC n = this
							.getClass().getDeclaredConstructor(this.getClass(), SpielPanel.class, List.class,
									ContextMenu.class, ObjectProperty.class)
							.newInstance(this, gp, npcs, cm, requestorN);
					n.x = ((NumberValue) ((JsonArray) ((JsonArray) npc.get("position")).get(i)).get(0)).getValue()
							.doubleValue();
					n.y = ((NumberValue) ((JsonArray) ((JsonArray) npc.get("position")).get(i)).get(1)).getValue()
							.doubleValue();
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			x = ((NumberValue) ((JsonArray) ((JsonArray) npc.get("position")).get(0)).get(0)).getValue()
					.doubleValue();
			y = ((NumberValue) ((JsonArray) ((JsonArray) npc.get("position")).get(0)).get(1)).getValue()
					.doubleValue();
		} else {
			x = ((NumberValue) ((JsonArray) npc.get("position")).get(0)).getValue().doubleValue();
			y = ((NumberValue) ((JsonArray) npc.get("position")).get(1)).getValue().doubleValue();
		}
	}

	public boolean isMaster() { return !slave; }

	public boolean isSlave() { return slave; }

	@Override
	public JsonValue toJsonValue() {
		if (!slave) {
			JsonObject jo = new JsonObject();
			JsonArray requestedSize = new JsonArray();
			requestedSize.add(reqWidth);
			requestedSize.add(reqHeight);
			jo.put("requestedSize", requestedSize);
			jo.put("textures", textureFiles);
			jo.put("npcData", npcData);
			JsonArray position = new JsonArray();
			if (slaves == null || slaves.size() == 0) {
				position.add(x);
				position.add(y);
			} else {
				JsonArray pos = new JsonArray();
				pos.add(x);
				pos.add(y);
				position.add(pos);
				for (NPC n: slaves) {
					pos = new JsonArray();
					pos.add(n.x);
					pos.add(n.y);
					position.add(pos);
				}
			}
			jo.put("position", position);
			JsonArray originalSize = new JsonArray();
			originalSize.add(origWidth);
			originalSize.add(origHeight);
			jo.put("originalSize", originalSize);
			jo.put("fps", fps);
			jo.put("type", getClass().getSimpleName());
			if (background) jo.put("background", background);
			return jo;
		} else return new StringValue("");
	}

	@Override
	public JsonValue toJsonValue(Function<Object, String> function) {
		return toJsonValue();
	}

	@Override
	public void update() {

		super.update();

		if (isVisible() && gp.getKeyH().h) setVisible(false);

	}

}
