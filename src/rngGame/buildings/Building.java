package rngGame.buildings;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;

public class Building extends GameObject {

	protected JsonObject buildingData;
	private boolean slave = false;
	private List<Building> slaves;
	private Building master;

	protected Building(SpielPanel gp, List<Building> buildings, ContextMenu cm, ObjectProperty<Building> requestorB) {
		super(gp, "building", buildings, cm, requestorB);
	}

	public Building(Building building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		this(gp, buildings, cm, requestorB);
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
		if (building.slaves == null) {
			Runnable[] r = new Runnable[1];
			master.removeCallbacks.add(r[0] = () -> {
				Building m = master.slaves.remove(0);
				m.slave = false;
				m.slaves = master.slaves;
				for (Building s: m.slaves)
					s.master = m;
				m.removeCallbacks.add(r[0]);
			});
			building.slaves = new ArrayList<>();
		}
		building.slaves.add(this);
		removeCallbacks.add(() -> {
			if (slave) master.slaves.remove(this);
		});

		addToView();
	}

	public Building(JsonObject building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		this(gp, buildings, cm, requestorB);
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

		addToView();

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

	public boolean isMaster() { return !slave; }

	public boolean isSlave() { return slave; }

	@Override
	public void update() {
		super.update();
		if (isVisible() && gp.getKeyH().b) setVisible(false);
	}

}