package rngGame.entity;

import java.util.Map.Entry;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;

public class Player extends Entity {

	private final Input keyH;
	public final int size = 64;

	public final int screenX;

	public final int screenY;
	private double oldX, oldY;

	public Player(SpielPanel gp) {
		super(gp, "player", 3);
		currentKey = "down";

		fps = 7.5;

		reqWidth = reqHeight = size;

		this.gp = gp;
		keyH = gp.getKeyH();

		screenX = gp.SpielLaenge / 2 - size / 2;
		screenY = gp.SpielHoehe / 2 - size / 2;

		x = gp.Bg * 13;
		y = gp.Bg * 37;
		oldX = x;
		oldY = y;

		getPlayerImage();

		textureFiles.forEach((key, file) -> {
			collisionBoxes.put(key, new Polygon());
		});

		double x = 16, y = 30, width = 33, height = 27;

		collisionBoxes.forEach((key, poly) -> {
			poly.setFill(Color.color(1, 0, 1, 0.75));
			poly.getPoints().addAll(x, y, x, y + height, x + width, y + height, x + width, y);
		});
	}


	public void getPlayerImage() {

		try {
			origHeight = origWidth = 32;
			getAnimatedImages("up", "LaufenHochL.png");
			getAnimatedImages("upL", "LaufenHochL.png");
			getAnimatedImages("idleup", "IdleUp.png");
			getAnimatedImages("idleupL", "IdleUp.png");

			origHeight = origWidth = 64;
			getAnimatedImages("down", "LaufenRunter.png");
			getAnimatedImages("downL", "LaufenRunterL.png");
			getAnimatedImages("right", "LaufenRechts.png");
			getAnimatedImages("left", "LaufenLinks.png");
			getAnimatedImages("idle", "Stehen.png");
			getAnimatedImages("idleL", "Stehen2.png");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setPosition(Entry<Double, Double> startingPosition) {
		x = (int) (gp.Bg * startingPosition.getKey());
		y = (int) (gp.Bg * startingPosition.getValue());
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

		if (!keyH.p) setVisible(true);
		else setVisible(false);


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

		//		shape.setTranslateX(0);
		//		shape.setTranslateY(0);

	}
}
