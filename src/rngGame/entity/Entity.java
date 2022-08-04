package rngGame.entity;

import rngGame.main.*;

public class Entity extends GameObject {

	protected double speed;

	public Entity(SpielPanel gp, String directory, double speed) {
		super(gp, directory);
		this.speed = speed;

	}

	public double getSpeed() { return speed; }
	public void setSpeed(double speed) { this.speed = speed; }

}
