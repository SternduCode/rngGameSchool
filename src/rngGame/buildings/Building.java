package rngGame.buildings;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.sterndu.json.JsonObject;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import rngGame.main.*;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Building.
 */
public class Building extends GameObject {

	/** The b. */
	private final AtomicBoolean b = new AtomicBoolean(false);

	/**
	 * Instantiates a new building.
	 *
	 * @param building the building
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public Building(Building building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
		Input.getInstance().setKeyHandler("b" + hashCode(), mod -> {
			b.set(!b.get());
		}, KeyCode.B, false);
	}

	/**
	 * Instantiates a new building.
	 *
	 * @param building the building
	 * @param gp the gp
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public Building(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, "building", buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
		Input.getInstance().setKeyHandler("b" + hashCode(), mod -> {
			b.set(!b.get());
		}, KeyCode.B, false);
	}

	/**
	 * Instantiates a new building.
	 *
	 * @param building the building
	 * @param gp the gp
	 * @param directory the directory
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public Building(JsonObject building, GamePanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);

		collisionBoxes.forEach((key, poly) -> poly.setFill(Color.color(0, 0, 1, 0.75)));
		Input.getInstance().setKeyHandler("b" + hashCode(), mod -> {
			b.set(!b.get());
		}, KeyCode.B, false);
	}

	/**
	 * Update.
	 *
	 * @param milis the milis
	 */
	@Override
	public void update(long milis) {
		super.update(milis);
		if (isVisible() && b.get()) setVisible(false);
	}

}