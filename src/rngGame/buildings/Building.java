package rngGame.buildings;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;

public class Building extends GameObject implements JsonValue {

	protected JsonObject buildingData;
	private boolean slave = false;
	private List<Building> slaves;
	@SuppressWarnings("unused")
	private Building master;

	protected Building(SpielPanel gp) {
		super(gp, "building");
	}

	public Building(Building building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		this(gp);
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorB.set(Building.this);
				cm.show(Building.this, e.getScreenX(), e.getScreenY());
			}
		});
		x = building.x;
		y = building.y;
		origWidth = building.origWidth;
		origHeight = building.origHeight;
		reqWidth = building.reqWidth;
		reqHeight = building.reqHeight;
		fps = building.fps;

		background = building.background;
		currentKey = building.currentKey;
		images = building.images;
		iv.setImage(building.getFirstImage());
		textureFiles = building.textureFiles;
		building.collisionBoxes.forEach((key, poly) -> {
			Polygon collisionBox = collisionBoxes.get(key);
			if (collisionBox == null) collisionBoxes.put(key, collisionBox = new Polygon());
			collisionBox.getPoints().addAll(poly.getPoints());
			collisionBox.setFill(poly.getFill());
		});

		buildingData = building.buildingData;

		master = building;
		slave = true;
		if (building.slaves == null)
			building.slaves = new ArrayList<>();
		building.slaves.add(this);

		buildings.add(this);
		gp.getViewGroup().getChildren().add(this);
	}

	public Building(JsonObject building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		this(gp);
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorB.set(Building.this);
				cm.show(Building.this, e.getScreenX(), e.getScreenY());
			}
		});
		origWidth = ((NumberValue) ((JsonArray) building.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) building.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(1)).getValue().intValue();
		if (building.containsKey("fps"))
			fps = ((NumberValue) building.get("fps")).getValue().doubleValue();
		else fps = 7;

		if (building.containsKey("background"))
			background = ((BoolValue) building.get("background")).getValue();
		((JsonObject) building.get("textures")).entrySet().parallelStream()
				.forEach(s -> {
					try {
						getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue());
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				});
		iv.setImage(getFirstImage());
		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));

		buildingData = (JsonObject) building.get("buildingData");

		buildings.add(this);
		gp.getViewGroup().getChildren().add(this);
		if (((JsonArray) building.get("position")).get(0) instanceof JsonArray ja) {
			try {
				slaves = new ArrayList<>();
				for (int i = 1; i < ((JsonArray) building.get("position")).size(); i++) {
					Building b = this
							.getClass().getDeclaredConstructor(this.getClass(), SpielPanel.class, List.class,
									ContextMenu.class, ObjectProperty.class)
							.newInstance(this, gp, buildings, cm, requestorB);
					b.x = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(i)).get(0)).getValue()
							.doubleValue();
					b.y = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(i)).get(1)).getValue()
							.doubleValue();
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			x = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(0)).get(0)).getValue()
					.doubleValue();
			y = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(0)).get(1)).getValue()
					.doubleValue();
		} else {
			x = ((NumberValue) ((JsonArray) building.get("position")).get(0)).getValue().doubleValue();
			y = ((NumberValue) ((JsonArray) building.get("position")).get(1)).getValue().doubleValue();
		}
	}

	public boolean isBackground() { return background;
	}

	public boolean isMaster() { return !slave; }

	public boolean isSlave() { return slave; }

	public void setPosition(double layoutX, double layoutY) {
		x = layoutX;
		y = layoutY;
	}

	@Override
	public JsonValue toJsonValue() {
		if (!slave) {
			JsonObject jo = new JsonObject();
			jo.put("type", getClass().getSimpleName());
			jo.put("textures", textureFiles);
			JsonArray position = new JsonArray();
			if (slaves == null || slaves.size() == 0) {
				position.add(x);
				position.add(y);
			} else {
				JsonArray pos = new JsonArray();
				pos.add(x);
				pos.add(y);
				position.add(pos);
				for (Building b: slaves) {
					pos = new JsonArray();
					pos.add(b.x);
					pos.add(b.y);
					position.add(pos);
				}
			}
			jo.put("position", position);
			JsonArray originalSize = new JsonArray();
			originalSize.add(origWidth);
			originalSize.add(origHeight);
			jo.put("originalSize", originalSize);
			JsonArray requestedSize = new JsonArray();
			requestedSize.add(reqWidth);
			requestedSize.add(reqHeight);
			jo.put("requestedSize", requestedSize);
			if (background) jo.put("background", background);
			jo.put("buildingData", buildingData);
			return jo;
		} else return new StringValue("");
	}

	@Override
	public JsonValue toJsonValue(Function<Object, String> function) {
		return toJsonValue();
	}

}