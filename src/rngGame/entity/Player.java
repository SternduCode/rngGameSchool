package rngGame.entity;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;

public class Player extends Entity {

	public final int size = 64;

	public int screenX;

	public int screenY;
	private double oldX, oldY;

	public Player(SpielPanel gp, ContextMenu cm, ObjectProperty<? extends Entity> requestor) {
		super(null, 3, gp, "player", null, cm, requestor);
		setCurrentKey("down");

		fps = 10.5;

		reqWidth = (int) ((reqHeight = size) * 1.5);

		this.gp = gp;

		screenX = gp.SpielLaenge / 2 - size / 2;
		screenY = gp.SpielHoehe / 2 - size / 2;

		setPosition(13, 37);

		getPlayerImage();

		textureFiles.forEach((key, file) -> {
			collisionBoxes.put(key, new Polygon());
		});

		double x = 33, y = 45, width = 31, height = 20;

		collisionBoxes.forEach((key, poly) -> {
			poly.setFill(Color.color(1, 0, 1, 0.75));
			poly.getPoints().addAll(x, y, x, y + height, x + width, y + height, x + width, y);
		});
	}


	public void getPlayerImage() {

		try {
			origHeight = 64;
			origWidth = reqWidth;

			getAnimatedImages("idle", "Stehen.png");
			getAnimatedImages("idleL", "Stehen2.png");
			getAnimatedImages("right", "LaufenRechts.png");
			getAnimatedImages("left", "LaufenLinks.png");
			getAnimatedImages("down", "LaufenRunter.png");
			getAnimatedImages("downL", "LaufenRunterL.png");
			getAnimatedImages("up", "LaufenHochL.png");
			getAnimatedImages("upL", "LaufenHochL.png");
			getAnimatedImages("idleup", "IdleUp.png");
			getAnimatedImages("idleupL", "IdleUp.png");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
		return "Player [size=" + size + ", screenX=" + screenX + ", screenY=" + screenY + ", speed="
				+ speed + ", x=" + x + ", y=" + y + ", fps=" + fps + ", images=" + images
				+ ", collisionBoxes=" + collisionBoxes + ", currentKey=" + getCurrentKey() + ", textureFiles="
				+ textureFiles + ", reqWidth=" + reqWidth + ", reqHeight=" + reqHeight + ", origWidth="
				+ origWidth + ", origHeight=" + origHeight + ", spriteCounter=" + spriteCounter
				+ ", spriteNum=" + spriteNum + ", background=" + background + "]";
	}

	@Override
	public void update() {

		Input keyH = gp.getKeyH();

		if (keyH.w) {
			if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("upL");
			else setCurrentKey("up");
			y -= speed;
		}
		else if (keyH.s && !keyH.ctrlPressed) {
			if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("downL");
			else setCurrentKey("down");
			y += speed;
		}
		else if (keyH.a) {
			setCurrentKey("left");
			x -= speed;
		}
		else if (keyH.d) {
			setCurrentKey("right");
			x += speed;
		} else if (getCurrentKey().endsWith("L") && getCurrentKey().contains("up")) setCurrentKey("idleupL");
		else if (getCurrentKey().equals("up") || getCurrentKey().contains("up")) setCurrentKey("idleup");
		else if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("idleL");

		else setCurrentKey("idle");

		super.update();

		setLayoutX(screenX);
		setLayoutY(screenY);

		getCollisionBox().setTranslateX(x - oldX);
		getCollisionBox().setTranslateY(y - oldY);

		if (isVisible() && keyH.p) setVisible(false);


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
