package rngGame.entity;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;

public class Player extends Entity {

	private final int size = 64;

	private final AtomicBoolean p = new AtomicBoolean(false);
	private final AtomicBoolean w = new AtomicBoolean(false);
	private final AtomicBoolean a = new AtomicBoolean(false);
	private final AtomicBoolean s = new AtomicBoolean(false);
	private final AtomicBoolean d = new AtomicBoolean(false);

	private int screenX;
	private int screenY;
	private double oldX, oldY;

	public Player(SpielPanel gp, ContextMenu cm, ObjectProperty<? extends Entity> requestor) {
		super(null, 3 * 60, gp, "player", null, cm, requestor);
		setCurrentKey("down");

		fps = 10.5;

		reqWidth = (int) ((reqHeight = getSize()) * 1.5);

		this.gp = gp;

		screenX = gp.SpielLaenge / 2 - getSize() / 2;
		screenY = gp.SpielHoehe / 2 - getSize() / 2;

		setPosition(13, 37);

		getPlayerImage();

		textureFiles.forEach((key, file) -> {
			collisionBoxes.put(key, new Polygon());
		});

		generateCollisionBox();

		Input.getInstance().setKeyHandler("p", mod -> {
			p.set(!p.get());
		}, KeyCode.P, false);
		Input.getInstance().setKeyHandler("wDOWN", mod -> {
			w.set(true);
		}, KeyCode.W, false);
		Input.getInstance().setKeyHandler("aDOWN", mod -> {
			a.set(true);
		}, KeyCode.A, false);
		Input.getInstance().setKeyHandler("sDOWN", mod -> {
			if (!mod.isControlPressed()) s.set(true);
		}, KeyCode.S, false);
		Input.getInstance().setKeyHandler("dDOWN", mod -> {
			d.set(true);
		}, KeyCode.D, false);
		Input.getInstance().setKeyHandler("wUP", mod -> {
			w.set(false);
		}, KeyCode.W, true);
		Input.getInstance().setKeyHandler("aUP", mod -> {
			a.set(false);
		}, KeyCode.A, true);
		Input.getInstance().setKeyHandler("sUP", mod -> {
			s.set(false);
		}, KeyCode.S, true);
		Input.getInstance().setKeyHandler("dUP", mod -> {
			d.set(false);
		}, KeyCode.D, true);
	}

	public void generateCollisionBox() {
		double x = 33, y = 45, width = 31, height = 20;

		collisionBoxes.forEach((key, poly) -> {
			poly.getPoints().clear();
			poly.setFill(Color.color(1, 0, 1, 0.75));
			poly.getPoints().addAll(x * gp.getScalingFactorX()-0.5, y * gp.getScalingFactorY()-0.5, x * gp.getScalingFactorX()-0.5,
					(y + height) * gp.getScalingFactorY()+0.5, (x + width) * gp.getScalingFactorX()+0.5,
					(y + height) * gp.getScalingFactorY()+0.5, (x + width) * gp.getScalingFactorX()+0.5,
					y * gp.getScalingFactorY()-0.5);
		});
	}

	public void getPlayerImage() {

		try {
			origHeight = 64;
			origWidth = reqWidth;

			getAnimatedImages("idledown", "Stehen.png");
			getAnimatedImages("idledownL", "Stehen2.png");
			getAnimatedImages("right", "LaufenRechts.png");
			getAnimatedImages("left", "LaufenLinks.png");
			getAnimatedImages("down", "LaufenRunter.png");
			getAnimatedImages("downL", "LaufenRunterL.png");
			getAnimatedImages("up", "LaufenHochL.png");
			getAnimatedImages("upL", "LaufenHochL.png");
			getAnimatedImages("idleup", "IdleUp.png");
			getAnimatedImages("idleupL", "IdleUp.png");
			getAnimatedImages("idle", "IdleRight.png");
			getAnimatedImages("idleL", "IdleLeft.png");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public int getScreenX() {
		return screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public int getSize() {
		return size;
	}

	@Override
	public double getX() { return (long) x; }

	@Override
	public double getY() { return (long) y; }

	@Override
	public void setPosition(double x, double y) {
		setPosition(new double[] {x, y});
	}


	public void setPosition(double[] position) {
		x = (long) position[0];
		y = (long) position[1];
		oldX = x;
		oldY = y;
	}


	@Override
	public String toString() {
		return "Player [size=" + getSize() + ", screenX=" + getScreenX() + ", screenY=" + getScreenY() + ", speed="
				+ speed + ", x=" + x + ", y=" + y + ", fps=" + fps + ", images=" + images
				+ ", collisionBoxes=" + collisionBoxes + ", currentKey=" + getCurrentKey() + ", textureFiles="
				+ textureFiles + ", reqWidth=" + reqWidth + ", reqHeight=" + reqHeight + ", origWidth="
				+ origWidth + ", origHeight=" + origHeight + ", spriteCounter=" + spriteCounter
				+ ", spriteNum=" + spriteNum + ", background=" + background + "]";
	}


	@Override
	public void update(long milis) {

		Input keyH = Input.getInstance();

		double updateSpeed = speed / 1000 * milis;

		String lastKey = getCurrentKey();

		if (gp.isInLoadingScreen()) {
			w.set(false);
			a.set(false);
			s.set(false);
			d.set(false);
		}

		if (w.get()) { // Hoch
			if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("upL");
			else setCurrentKey("up");
			y -= updateSpeed * gp.getScalingFactorY();
		} else if (s.get()) { // Runter
			if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("downL");
			else setCurrentKey("down");
			y += updateSpeed * gp.getScalingFactorY();
		} else if (a.get()) { // Links
			setCurrentKey("left");
			x -= updateSpeed * gp.getScalingFactorX();
		} else if (d.get()) { // Rechts
			setCurrentKey("right");
			x += updateSpeed * gp.getScalingFactorX();
		} else if (lastKey.contains("down")) { // Idle Runter
			if (lastKey.endsWith("L") || lastKey.contains("left")) setCurrentKey("idledownL");
			else setCurrentKey("idledown");
		} else if (lastKey.contains("up")) { // Idle Hoch
			if (lastKey.endsWith("L") || lastKey.contains("left")) setCurrentKey("idleupL");
			else setCurrentKey("idleup");
		} else if (lastKey.endsWith("L") || lastKey.contains("left")) setCurrentKey("idleL");
		else setCurrentKey("idle");

		super.update(milis);

		setLayoutX(getScreenX());
		setLayoutY(getScreenY());

		getCollisionBox().setTranslateX(x - oldX);
		getCollisionBox().setTranslateY(y - oldY);

		if (isVisible() && p.get()) setVisible(false);


		gp.getBuildings().forEach(b -> {
			if (b.collides(this)) {
				x = oldX;
				y = oldY;
			}
		});

		gp.getNpcs().forEach(b -> {
			if (b.collides(this)) {
				x = oldX;
				y = oldY;
			}
		});

		if (gp.getTileM().collides(this)) {
			x = oldX;
			y = oldY;
		}

		oldX = x;
		oldY = y;

		screenX = (int) (gp.SpielLaenge / 2 - iv.getImage().getWidth() / 2);
		screenY = (int) (gp.SpielHoehe / 2 - iv.getImage().getHeight() / 2);

		//		shape.setTranslateX(0);
		//		shape.setTranslateY(0);


	}
}
