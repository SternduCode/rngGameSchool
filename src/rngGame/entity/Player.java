package rngGame.entity;

import java.util.concurrent.atomic.AtomicBoolean;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.Input;
import rngGame.main.WindowManager;
import rngGame.tile.TileManager;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * A class that defines the Player.
 *
 * @author Sterndu
 * @author ICEBLUE
 * @author neo30
 */

public class Player extends Entity {

	/** The size. */
	private final int size = 64; // The value that reqHeight will be set to

	/** The p. */
	private final AtomicBoolean p = new AtomicBoolean(false);

	/** The w. */
	private final AtomicBoolean w = new AtomicBoolean(false);

	/** The a. */
	private final AtomicBoolean a = new AtomicBoolean(false);

	/** The s. */
	private final AtomicBoolean s = new AtomicBoolean(false);

	/** The d. */
	private final AtomicBoolean d = new AtomicBoolean(false);

	/** The colli box height. */
	private final double colliBoxX = 33, colliBoxY = 45, colliBoxWidth = 31, colliBoxHeight = 20;

	/** The screen X. */
	private int screenX; // the players X position in the window

	/** The screen Y. */
	private int screenY; // the players Y position in the window

	/** The old Y. */
	private double oldX, oldY; // used for collision detection

	/**
	 * Player is not defined in map file but some attributes of it are.
	 *
	 * @param gamePanel A reference to the {@link GamePanel}
	 * @param cm        A reference to the {@link TileManager#getCM() ContextMenu} via {@link GamePanel#getTileManager()}.
	 * @param requestor Is used to know on what the {@link TileManager#getCM() ContextMenu} was triggered
	 */
	public Player(GamePanel gamePanel, ContextMenu cm, ObjectProperty<? extends Entity> requestor) {
		super(null, 3 * 60, gamePanel, "player", null, cm, requestor);
		setCurrentKey("down");

		fps = 10.5;

		reqWidth = (int) ((reqHeight = getSize()) * 1.5); // Set reqHeight to 64 and reqWidth to 96; Player size is
		// rectangular in this case

		gamepanel = gamePanel;

		screenX	= WindowManager.getInstance().getGameWidth() / 2 - getSize() / 2;	// Place the player in the middle of the screen
		screenY	= WindowManager.getInstance().getGameHeight() / 2 - getSize() / 2;

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
	 * Sets all collisionBoxes to the correct size and position.
	 */
	public void generateCollisionBox() {


		collisionBoxes.forEach((key, poly) -> {
			poly.getPoints().clear();
			poly.setFill(Color.color(1, 0, 1, 0.75));
			poly.getPoints().addAll(colliBoxX * WindowManager.getInstance().getScalingFactorX() - 0.5,
					colliBoxY * WindowManager.getInstance().getScalingFactorY() - 0.5, colliBoxX * WindowManager.getInstance().getScalingFactorX() - 0.5,
					(colliBoxY + colliBoxHeight) * WindowManager.getInstance().getScalingFactorY() + 0.5,
					(colliBoxX + colliBoxWidth) * WindowManager.getInstance().getScalingFactorX() + 0.5,
					(colliBoxY + colliBoxHeight) * WindowManager.getInstance().getScalingFactorY() + 0.5,
					(colliBoxX + colliBoxWidth) * WindowManager.getInstance().getScalingFactorX() + 0.5,
					colliBoxY * WindowManager.getInstance().getScalingFactorY() - 0.5);
		});
	}

	/**
	 * Gets the colli box height.
	 *
	 * @return the colli box height
	 */
	public double getColliBoxHeight() { return colliBoxHeight * WindowManager.getInstance().getScalingFactorY(); }

	/**
	 * Gets the colli box width.
	 *
	 * @return the colli box width
	 */
	public double getColliBoxWidth() { return colliBoxWidth * WindowManager.getInstance().getScalingFactorX(); }

	/**
	 * Gets the colli box X.
	 *
	 * @return the colli box X
	 */
	public double getColliBoxX() { return colliBoxX * WindowManager.getInstance().getScalingFactorX(); }

	/**
	 * Gets the colli box Y.
	 *
	 * @return the colli box Y
	 */
	public double getColliBoxY() { return colliBoxY * WindowManager.getInstance().getScalingFactorY(); }

	/**
	 * Gets the player image.
	 *
	 * @return the player image
	 */
	public void getPlayerImage() {

		try {
			origHeight = reqHeight;
			origWidth = reqWidth;

			/*
			 * Load the textures for all states of the player
			 */

			getAnimatedImages("idledown", "SE_idle.gif");
			getAnimatedImages("idledownL", "SW_idle.gif");
			getAnimatedImages("right", "LaufenRechts.png");
			getAnimatedImages("left", "LaufenLinks.png");
			getAnimatedImages("down", "LaufenRunter.png");
			getAnimatedImages("downL", "LaufenRunterL.png");
			getAnimatedImages("up", "LaufenHochL.png");
			getAnimatedImages("upL", "LaufenHochL.png");
			getAnimatedImages("idleup", "IdleUp.png");
			getAnimatedImages("idleupL", "IdleUp.png");
			getAnimatedImages("idle", "E_idle.gif");
			getAnimatedImages("idleL", "W_idle.gif");


//			//Norden
//			getAnimatedImages("NW_run", "LaufenHochL.png");
//			getAnimatedImages("NE_run", "LaufenHochL.png");
//			getAnimatedImages("NE_idle", "IdleUp.png");
//			getAnimatedImages("NW_idle", "IdleUp.png");
//
//			//Seiten
//			getAnimatedImages("R_run", "LaufenRechts.png");
//			getAnimatedImages("W_run", "LaufenLinks.png");
//			getAnimatedImages("E_idle", "IdleRight.png");
//			getAnimatedImages("W_idle", "IdleLeft.png");
//
//			//Sueden
//			getAnimatedImages("SE_run", "LaufenRunter.png");
//			getAnimatedImages("SW_run", "LaufenRunterL.png");
//			getAnimatedImages("SE_ilde", "Stehen.gif");
//			getAnimatedImages("SW_idle", "Stehen2.png");





		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the screen X.
	 *
	 * @return the players X position in the window
	 */
	public int getScreenX() {
		return screenX;
	}

	/**
	 * Gets the screen Y.
	 *
	 * @return the players Y position in the window
	 */
	public int getScreenY() {
		return screenY;
	}

	/**
	 * Gets the size.
	 *
	 * @return the height of the Player texture
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the x.
	 *
	 * @return the players X position in the map
	 */
	@Override
	public double getX() { return (long) x; }

	/**
	 * Gets the y.
	 *
	 * @return the players Y position in the map
	 */
	@Override
	public double getY() { return (long) y; }

	/**
	 * Sets the players map position to {@code x} and {@code y}.
	 *
	 * @param x the players X position in the map
	 * @param y the players Y position in the map
	 */
	@Override
	public void setPosition(double x, double y) {
		setPosition(new double[] {x, y});
	}

	/**
	 * Sets the players map position to {@code x} and {@code y}.
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

	/**
	 * To string.
	 *
	 * @return the string
	 */
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


	/**
	 * Update.
	 *
	 * @param milis the milis
	 */
	@Override
	public void update(long milis) {

		double updateSpeed = speed / 1000 * milis;

		String lastKey = getCurrentKey();

		if (gamepanel.isBlockUserInputs()) {
			w.set(false);
			a.set(false);
			s.set(false);
			d.set(false);
		}

		if (w.get()) { // Hoch
			if ("left".equals(getCurrentKey()) || getCurrentKey().endsWith("L")) setCurrentKey("upL");
			else setCurrentKey("up");
			y -= updateSpeed * WindowManager.getInstance().getScalingFactorY();
		} else if (s.get()) { // Runter
			if ("left".equals(getCurrentKey()) || getCurrentKey().endsWith("L")) setCurrentKey("downL");
			else setCurrentKey("down");
			y += updateSpeed * WindowManager.getInstance().getScalingFactorY();
		} else if (a.get()) { // Links
			setCurrentKey("left");
			x -= updateSpeed * WindowManager.getInstance().getScalingFactorX();
		} else if (d.get()) { // Rechts
			setCurrentKey("right");
			x += updateSpeed * WindowManager.getInstance().getScalingFactorX();
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

		getCollisionBox().setTranslateX( (x - oldX) * 3);
		getCollisionBox().setTranslateY( (y - oldY) * 3);

		if (isVisible() && p.get()) setVisible(false);

		gamepanel.getLgp().getBuildings().forEach(b -> {
			if (b.collides(this)) {
				x = oldX;
				y = oldY;
			}
		});

		gamepanel.getLgp().getNpcs().forEach(b -> {
			if (b.collides(this)) {
				x = oldX;
				y = oldY;
			}
		});

		if (gamepanel.getTileManager().collides(this)) {
			x = oldX;
			y = oldY;
		}

		oldX = x;
		oldY = y;

		screenX	= (int) (WindowManager.getInstance().getGameWidth() / 2 - iv.getImage().getWidth() / 2);
		screenY	= (int) (WindowManager.getInstance().getGameHeight() / 2 - iv.getImage().getHeight() / 2);

	}
}
