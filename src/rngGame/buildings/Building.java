package rngGame.buildings;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import rngGame.main.*;

public class Building extends GameObject {

	private final AtomicBoolean b = new AtomicBoolean(false);

	public Building(Building building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
		Input.getInstance().setKeyHandler("b" + hashCode(), mod -> {
			b.set(!b.get());
		}, KeyCode.B, false);
	}

	public Building(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, "building", buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
		Input.getInstance().setKeyHandler("b" + hashCode(), mod -> {
			b.set(!b.get());
		}, KeyCode.B, false);
	}

	public Building(JsonObject building, GamePanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
		Input.getInstance().setKeyHandler("b" + hashCode(), mod -> {
			b.set(!b.get());
		}, KeyCode.B, false);
	}

	@Override
	public void update(long milis) {
		super.update(milis);

		if (isVisible() && b.get()) setVisible(false);
	}

}