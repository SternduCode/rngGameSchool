package rngGame.entity;

import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.*;

public abstract class Entity extends GameObject {

	protected double speed;

	public Entity(Entity en, List<? extends Entity> entities, ContextMenu cm,
			ObjectProperty<? extends Entity> requestor) {
		super(en, entities, cm, requestor);
		speed = en.speed;

	}

	public Entity(JsonObject en, double speed, SpielPanel gp, String directory, List<? extends Entity> entities,
			ContextMenu cm,
			ObjectProperty<? extends Entity> requestor) {
		super(en, gp, directory, entities, cm, requestor);
		this.speed = speed;

	}

	public double getSpeed() { return speed; }

	public void setSpeed(double speed) { this.speed = speed; }

}
