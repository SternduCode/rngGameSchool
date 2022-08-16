package rngGame.entity;

import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.*;

public abstract class Entity extends GameObject {

	protected double speed;

	public Entity(SpielPanel gp, String directory, double speed, List<? extends Entity> entities, ContextMenu cm,
			ObjectProperty<? extends Entity> requestor) {
		super(gp, directory, entities, cm, requestor);
		this.speed = speed;

	}

	public double getSpeed() { return speed; }

	public abstract boolean isMaster();

	public abstract boolean isSlave();

	public void setSpeed(double speed) { this.speed = speed; }

}
