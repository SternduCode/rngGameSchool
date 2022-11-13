package rngGame.entity;

import java.util.concurrent.atomic.AtomicBoolean;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.*;
import rngGame.tile.TileManager;

/**
 *
 * A class that defines the Player
 *
 * @author Sterndu
 * @author ICEBLUE
 * @author neo30
 */

public class Player extends Entity {

	private final int size = 64; // The value that reqHeight will be set to

	private final AtomicBoolean p = new AtomicBoolean(false);
	private final AtomicBoolean w = new AtomicBoolean(false);
	private final AtomicBoolean a = new AtomicBoolean(false);
	private final AtomicBoolean s = new AtomicBoolean(false);
	private final AtomicBoolean d = new AtomicBoolean(false);

	private int screenX; // the players X position in the window
	private int screenY; // the players Y position in the window
	private double oldX, oldY; // used for collision detection

	/**
	 * Player is not defined in map file but some attributes of it are
	 *
	 * @param gp        A reference to the {@link GamePanel}
	 * @param cm        A reference to the {@link TileManager#getCM() ContextMenu}
	 *                  via {@link GamePanel#getTileM()}.
	 * @param requestor Is used to know on what the {@link TileManager#getCM()
	 *                  ContextMenu} was triggered
	 */
	public Player(GamePanel gp, ContextMenu cm, ObjectProperty<? extends Entity> requestor) {
		super(null, 3 * 60, gp, "player", null, cm, requestor);
		setCurrentKey("down");

		fps = 10.5;

		reqWidth = (int) ((reqHeight = getSize()) * 1.5); // Set reqHeight to 64 and reqWidth to 96; Player size is
		// rectangular in this case

		this.gamepanel = gp;

		screenX = gp.SpielLaenge / 2 - getSize() / 2; // Place the player in the middle of the screen
		screenY = gp.SpielHoehe / 2 - getSize() / 2;

		setPosition(0, 0); // Put player on upper left corner of the map; can be overridden in map file

		getPlayerImage();

		textureFiles.forEach((key, file) -> {
			collisionBoxes.put(key, new Polygon()); // Add collisionBoxes for all textures
		});

		generateCollisionBox();

		/*
		 * KeyHandlers for: P > sets the Player invisible W > move forward A > move left
		 * S > move down D > move right
		 */

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

	/**
	 * Sets all collisionBoxes to the correct size and position
	 */
	public void generateCollisionBox() {
		double x = 33, y = 45, width = 31, height = 20;

		collisionBoxes.forEach((key, poly) -> {
			poly.getPoints().clear();
			poly.setFill(Color.color(1, 0, 1, 0.75));
			poly.getPoints().addAll(x * gamepanel.getScalingFactorX()-0.5, y * gamepanel.getScalingFactorY()-0.5, x * gamepanel.getScalingFactorX()-0.5,
					(y + height) * gamepanel.getScalingFactorY()+0.5, (x + width) * gamepanel.getScalingFactorX()+0.5,
					(y + height) * gamepanel.getScalingFactorY()+0.5, (x + width) * gamepanel.getScalingFactorX()+0.5,
					y * gamepanel.getScalingFactorY()-0.5);
		});
	}

	public void getPlayerImage() {

		try {
			origHeight = reqHeight;
			origWidth = reqWidth;

			/*
			 * Load the textures for all states of the player
			 */

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

	/**
	 * @return the players X position in the window
	 */
	public int getScreenX() {
		return screenX;
	}

	/**
	 * @return the players Y position in the window
	 */
	public int getScreenY() {
		return screenY;
	}

	/**
	 * @return the height of the Player texture
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the players X position in the map
	 */
	@Override
	public double getX() { return (long) x; }

	/**
	 * @return the players Y position in the map
	 */
	@Override
	public double getY() { return (long) y; }

	/**
	 * Sets the players map position to {@code x} and {@code y}
	 *
	 * @param x the players X position in the map
	 * @param y the players Y position in the map
	 */
	@Override
	public void setPosition(double x, double y) {
		setPosition(new double[] {x, y});
	}

	/**
	 * Sets the players map position to {@code x} and {@code y}
	 *
	 * @param position an array containing the players x ({@code position[0]}) and y
	 *                 ({@code position[1]}) positions
	 */
	public void setPosition(double[] position) {
		x = (long) position[0];
		y = (long) position[1];
		oldX = x;
		oldY = y;
	}

	@Override
	public String toString() {
		return "Player [size=" + getSize() + ", p=" + p + ", w=" + w + ", a=" + a + ", s=" + s
				+ ", d=" + d + ", screenX=" + getScreenX() + ", screenY=" + getScreenY() + ", oldX=" + oldX
				+ ", oldY=" + oldY + ", speed=" + getSpeed() + ", x=" + getX() + ", y=" + getY() + ", fps="
				+ fps + ", images=" + getImages() + ", collisionBoxes=" + collisionBoxes + ", directory="
				+ directory + ", layer=" + getLayer() + ", extraData=" + extraData + ", slave=" + isSlave()
				+ ", textureFiles=" + textureFiles + ", reqWidth=" + getReqWidth() + ", reqHeight="
				+ getReqHeight() + ", origWidth=" + getOrigWidth() + ", origHeight=" + getOrigHeight()
				+ ", animationCounter=" + animationCounter + ", animationNum=" + animationNum
				+ ", background=" + isBackground() + "]";
	}


	@Override
	public void update(long milis) {

		double updateSpeed = speed / 1000 * milis;

		String lastKey = getCurrentKey();

		if (gamepanel.isInLoadingScreen()) {
			w.set(false);
			a.set(false);
			s.set(false);
			d.set(false);
		}

		if (w.get()) { // Hoch
			if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("upL");
			else setCurrentKey("up");
			y -= updateSpeed * gamepanel.getScalingFactorY();
		} else if (s.get()) { // Runter
			if (getCurrentKey().equals("left") || getCurrentKey().endsWith("L")) setCurrentKey("downL");
			else setCurrentKey("down");
			y += updateSpeed * gamepanel.getScalingFactorY();
		} else if (a.get()) { // Links
			setCurrentKey("left");
			x -= updateSpeed * gamepanel.getScalingFactorX();
		} else if (d.get()) { // Rechts
			setCurrentKey("right");
			x += updateSpeed * gamepanel.getScalingFactorX();
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

		gamepanel.getBuildings().forEach(b -> {
			if (b.collides(this)) {
				x = oldX;
				y = oldY;
			}
		});

		gamepanel.getNpcs().forEach(b -> {
			if (b.collides(this)) {
				x = oldX;
				y = oldY;
			}
		});

		if (gamepanel.getTileM().collides(this)) {
			x = oldX;
			y = oldY;
		}

		oldX = x;
		oldY = y;

		screenX = (int) (gamepanel.SpielLaenge / 2 - iv.getImage().getWidth() / 2);
		screenY = (int) (gamepanel.SpielHoehe / 2 - iv.getImage().getHeight() / 2);

	}
}
