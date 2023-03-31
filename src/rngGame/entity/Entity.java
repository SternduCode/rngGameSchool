package rngGame.entity;

import java.util.List;

import com.sterndu.json.JsonObject;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.GameObject;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity.
 */
public abstract class Entity extends GameObject {

	/** The speed. */
	protected double speed;

	/**
	 * Instantiates a new entity.
	 *
	 * @param en the en
	 * @param entities the entities
	 * @param cm the cm
	 * @param requestor the requestor
	 */
	public Entity(Entity en, List<? extends Entity> entities, ContextMenu cm,
			ObjectProperty<? extends Entity> requestor) {
		super(en, entities, cm, requestor);
		speed = en.speed;

	}

	/**
	 * Instantiates a new entity.
	 *
	 * @param en the en
	 * @param speed the speed
	 * @param gp the gp
	 * @param directory the directory
	 * @param entities the entities
	 * @param cm the cm
	 * @param requestor the requestor
	 */
	public Entity(JsonObject en, double speed, GamePanel gp, String directory, List<? extends Entity> entities,
			ContextMenu cm,
			ObjectProperty<? extends Entity> requestor) {
		super(en, gp, directory, entities, cm, requestor);
		this.speed = speed;

	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public double getSpeed() { return speed; }

	/**
	 * Sets the speed.
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(double speed) { this.speed = speed; }

}
