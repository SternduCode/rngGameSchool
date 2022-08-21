package rngGame.entity;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;

public class Player extends Entity {

	private final Input keyH;
	public final int size = 64;

	public int screenX;

	public int screenY;
	private double oldX, oldY;

	public Player(SpielPanel gp, ContextMenu cm, ObjectProperty<? extends Entity> requestor) {
		super(null, 3, gp, "player", null, cm, requestor);
		currentKey = "down";

		fps = 10.5;

		reqWidth = (int) ((reqHeight = size) * 1.5);

		this.gp = gp;
		keyH = gp.getKeyH();

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
		setPosition(new Double[] {x,y});
	}

	public void setPosition(Double[] position) {
		x = (long) (gp.Bg * position[0]);
		y = (long) (gp.Bg * position[1]);
		oldX = x;
		oldY = y;
	}

	@Override
	public String toString() {
		return "Player [size=" + size + ", screenX=" + screenX + ", screenY=" + screenY + ", speed="
				+ speed + ", x=" + x + ", y=" + y + ", fps=" + fps + ", images=" + images
				+ ", collisionBoxes=" + collisionBoxes + ", currentKey=" + currentKey + ", textureFiles="
				+ textureFiles + ", reqWidth=" + reqWidth + ", reqHeight=" + reqHeight + ", origWidth="
				+ origWidth + ", origHeight=" + origHeight + ", spriteCounter=" + spriteCounter
				+ ", spriteNum=" + spriteNum + ", background=" + background + "]";
	}

	@Override
	public void update() {
		if (keyH.w) {
			if (currentKey.equals("left") || currentKey.endsWith("L")) currentKey = "upL";
			else currentKey = "up";
			y -= speed;
		}
		else if (keyH.s && !keyH.ctrlPressed) {
			if (currentKey.equals("left") || currentKey.endsWith("L")) currentKey = "downL";
			else currentKey = "down";
			y += speed;
		}
		else if (keyH.a) {
			currentKey = "left";
			x -= speed;
		}
		else if (keyH.d) {
			currentKey = "right";
			x += speed;
		} else if (currentKey.endsWith("L") && currentKey.contains("up")) currentKey = "idleupL";
		else if (currentKey.equals("up") || currentKey.contains("up")) currentKey = "idleup";
		else if (currentKey.equals("left") || currentKey.endsWith("L")) currentKey = "idleL";

		else currentKey = "idle";

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
