package rngGame.main;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import rngGame.buildings.Building;
import rngGame.tile.TextureHolder;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Input.
 */
public class Input {

	private static final int CLASSINDEX = 2;

	/**
	 * The  KeyHandlerKeyCodePair.
	 */
	private record KeyHandlerKeyCodePair(Consumer<ModKeysState> handler, KeyCode keyCode, boolean up) {}

	/**
	 * The  ModKeysState.
	 */
	public record ModKeysState(boolean isControlPressed, boolean isShiftPressed, boolean isCapsPressed,
			boolean isSuperPressed, boolean isAltPressed, boolean isAltgrPressed) {}

	/** The Constant INSTANCE. */
	private static final Input INSTANCE = new Input();

	private boolean blockInputs;

	/** The key up handlers. */
	private final Map<KeyCode, List<Consumer<ModKeysState>>> keyDownHandlers = new HashMap<>(),
			keyUpHandlers = new HashMap<>();

	/** The key handlers. */
	private final Map<String, KeyHandlerKeyCodePair> keyHandlers = new HashMap<>();

	/** The s. */
	private boolean n, s;

	/** The altgr state. */
	private boolean ctrlState, shiftState, altState, superState, capsState, altgrState;

	/** The resize. */
	private GameObject move, resize;

	/** The gamepanel. */
	private GamePanel gamepanel;

	/**
	 * Instantiates a new input.
	 */
	private Input() {
		setKeyHandler("ControlDown", mod -> {
			ctrlState = true;
		}, KeyCode.CONTROL, false);
		setKeyHandler("ControlUp", mod -> {
			ctrlState = false;
		}, KeyCode.CONTROL, true);
		setKeyHandler("ShiftDown", mod -> {
			shiftState = true;
		}, KeyCode.SHIFT, false);
		setKeyHandler("ShiftUp", mod -> {
			shiftState = false;
		}, KeyCode.SHIFT, true);
		setKeyHandler("AltDown", mod -> {
			altState = true;
		}, KeyCode.ALT, false);
		setKeyHandler("AltUp", mod -> {
			altState = false;
		}, KeyCode.ALT, true);
		setKeyHandler("SuperDown", mod -> {
			superState = true;
		}, KeyCode.WINDOWS, false);
		setKeyHandler("SuperUp", mod -> {
			superState = false;
		}, KeyCode.WINDOWS, true);
		setKeyHandler("CapsDown", mod -> {
			//			Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK); gget caps state swing/awt
			capsState = true;
		}, KeyCode.CAPS, false);
		setKeyHandler("CapsUp", mod -> {
			capsState = false;
		}, KeyCode.CAPS, true);
		setKeyHandler("AltgrDown", mod -> {
			altgrState = true;
		}, KeyCode.ALT_GRAPH, false);
		setKeyHandler("AltgrUp", mod -> {
			altgrState = false;
		}, KeyCode.ALT_GRAPH, true);
		setKeyHandler("Fullscreen", mod -> {
			toggleFullScreen();
		}, KeyCode.F11, false);
		setKeyHandler("ContextMenu", mod -> {
			// TODO tbd
		}, KeyCode.CONTEXT_MENU, false);
		setKeyHandler("AlternateTickUpdate", mod -> {
			if ("true".equals(System.getProperty("alternateUpdate"))) System.setProperty("alternateUpdate", "false");
			else System.setProperty("alternateUpdate", "true");
		}, KeyCode.M, false);
		setKeyHandler("Reload", mod -> {
			if (mod.isControlPressed()) if (gamepanel != null)
				gamepanel.reload();
		}, KeyCode.R, false);
		setKeyHandler("ToggleFPSLabel", mod -> {
			gamepanel.toggleFpsLabelVisible();
		}, KeyCode.F, false);
		setKeyHandler("Teleport", mod -> {
			if ("true".equals(System.getProperty("teleport"))) System.setProperty("teleport", "false");
			else System.setProperty("teleport", "true");
		}, KeyCode.T, false);
	}

	/**
	 * Gets the single instance of Input.
	 *
	 * @return single instance of Input
	 */
	public static Input getInstance() { return INSTANCE; }

	/**
	 * New C.
	 *
	 * @param p the p
	 */
	private void newC(Polygon p) {
		p.getPoints().clear();
		p.setVisible(false);
		if (p.getParent() instanceof TextureHolder th) if (th.getTile().poly != null) th.getTile().poly.clear();
	}

	public void setBlockInputs(boolean b) { blockInputs = b; }

	public boolean isBlockInputs() { return blockInputs || (gamepanel != null && LoadingScreen.INSTANCE.isInLoadingScreen()); }

	/**
	 * Save.
	 *
	 * @param t the t
	 * @param path the path
	 */
	private void save(Polygon t, String path) {
		File f = new File("./res/collisions/" + path);
		if (!f.exists()) try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "rws");
			raf.seek(0l);
			raf.writeInt(t.getPoints().size());
			boolean s = false;
			for (Double element: t.getPoints())
				raf.writeDouble((long) (element / ( ( s = !s ) ? WindowManager.getInstance().getScalingFactorX() : WindowManager.getInstance().getScalingFactorY())));
			raf.setLength(4l + t.getPoints().size() * 8l);
			raf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(f);
	}


	/**
	 * Sets the game panel.
	 *
	 * @param gp the new game panel
	 */
	protected void setGamePanel(GamePanel gp) {
		gamepanel=gp;
	}

	/**
	 * Drag detected.
	 *
	 * @param me the me
	 */
	public void dragDetected(MouseEvent me) {}

	/**
	 * Checks if is altgr pressed.
	 *
	 * @return true, if is altgr pressed
	 */
	public boolean isAltgrPressed() { return altgrState; }

	/**
	 * Checks if is alt pressed.
	 *
	 * @return true, if is alt pressed
	 */
	public boolean isAltPressed() { return altState; }

	/**
	 * Checks if is caps pressed.
	 *
	 * @return true, if is caps pressed
	 */
	public boolean isCapsPressed() { return capsState; }

	/**
	 * Checks if is ctrl pressed.
	 *
	 * @return true, if is ctrl pressed
	 */
	public boolean isCtrlPressed() { return ctrlState; }

	/**
	 * Checks if is shift pressed.
	 *
	 * @return true, if is shift pressed
	 */
	public boolean isShiftPressed() { return shiftState; }

	/**
	 * Checks if is super pressed.
	 *
	 * @return true, if is super pressed
	 */
	public boolean isSuperPressed() { return superState; }

	/**
	 * Key pressed.
	 *
	 * @param e the e
	 */
	public void keyPressed(KeyEvent e) {
		if (!isBlockInputs()) {
			KeyCode code = e.getCode();

			ModKeysState modKeysState = new ModKeysState(ctrlState, shiftState, capsState, superState, altState,
					altgrState);

			if (keyDownHandlers.containsKey(code)) keyDownHandlers.get(code).forEach(con -> con.accept(modKeysState));

			if (code == KeyCode.N) n = true;

			if (code == KeyCode.S) s = true;
		}

	}

	/**
	 * Key released.
	 *
	 * @param e the e
	 */
	public void keyReleased(KeyEvent e) {
		if (!isBlockInputs()) {

			KeyCode code = e.getCode();

			ModKeysState modKeysState = new ModKeysState(ctrlState, shiftState, capsState, superState, altState,
					altgrState);

			if (keyUpHandlers.containsKey(code)) keyUpHandlers.get(code).forEach(con -> con.accept(modKeysState));

			if ("ö".equalsIgnoreCase(e.getText())) saveMap();

			if (code == KeyCode.E) if ("true".equals(System.getProperty("edit"))) System.setProperty("edit", "false");
			else System.setProperty("edit", "true");

			if (code == KeyCode.C) if ("true".equals(System.getProperty("coll"))) System.setProperty("coll", "false");
			else System.setProperty("coll", "true");

			if (code == KeyCode.N) n = false;

			if (code == KeyCode.S) s = false;
		}
	}

	/**
	 * Key typed.
	 *
	 * @param e the e
	 */
	public void keyTyped(KeyEvent e) {}

	/**
	 * Mouse dragged.
	 *
	 * @param me the me
	 */
	public void mouseDragged(MouseEvent me) {
		if (!isBlockInputs()) if (move != null) {
			move.setX((long) (me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX() - move.getWidth() / 2));
			move.setY((long) (me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY() - move.getHeight() / 2));
		}
		else if (resize != null) {
			resize.setReqWidth((int) (me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX() - resize.getX()));
			resize.setReqHeight(
					(int) (me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY() - resize.getY()));
			resize.reloadTextures();
		} else {
			Node target = gamepanel.getTileManager().getObjectAt(me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX(),
					me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY());
			if (!gamepanel.getSelectTool().isDragging() && target instanceof TextureHolder)
				gamepanel.getSelectTool().startDrag(me);
			else if (gamepanel.getSelectTool().isDragging()) gamepanel.getSelectTool().doDrag(me);
			else if (target instanceof GameObject go) {

			} else {

			}
		}
	}

	/**
	 * Mouse moved.
	 *
	 * @param me the me
	 */
	public void mouseMoved(MouseEvent me) {
		if (gamepanel != null) if (!gamepanel.getSelectTool().isDragging() && !gamepanel.getTileManager().getCM().isShowing())
			if ("true".equals(System.getProperty("edit"))) gamepanel.getSelectTool().drawOutlines(me);
			else gamepanel.getSelectTool().undrawOutlines();
	}

	/**
	 * Mouse released.
	 *
	 * @param me the me
	 */
	public void mouseReleased(MouseEvent me) {
		if ("true".equals(System.getProperty("debug")))
			System.out.println("Released " + me);
		if (gamepanel != null) {
			Node target = gamepanel.getTileManager().getObjectAt(me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX(),
					me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY());
			if ("true".equals(System.getProperty("teleport"))) gamepanel.getPlayer().setPosition(
					me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX()-gamepanel.getPlayer().getColliBoxX(),
					me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY()-gamepanel.getPlayer().getColliBoxY());
			else if (target instanceof TextureHolder t) {

				if (gamepanel.getSelectTool().isDragging()) gamepanel.getSelectTool().endDrag();
				else
					if ("true".equals(System.getProperty("coll"))) if (!ctrlState || !s && !n) {
						t.getPoly().getPoints().addAll(me.getX() - t.getLayoutX(), me.getY() - t.getLayoutY());
						if (t.getTile().poly == null) t.getTile().poly = new ArrayList<>();
						t.getTile().poly.add(me.getX() - t.getLayoutX());
						t.getTile().poly.add(me.getY() - t.getLayoutY());
					} else if (ctrlState && s) {
						String[] sp = t.getTile().name.split("[.]");
						save(t.getPoly(),
								gamepanel.getTileManager().getDir() + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox");
					} else if (ctrlState && n) newC(t.getPoly());
			} else
				if (target instanceof Building b
						&& "true".equals(System.getProperty("coll")))
					if (!ctrlState || !s && !n)
						b.getCollisionBox().getPoints().addAll((double) Math.round(me.getX() - b.getLayoutX()),
								(double) Math.round(me.getY() - b.getLayoutY()));
					else if (ctrlState && s) {
						String[] sp = b.textureFiles.get(b.getCurrentKey()).split("[.]");
						save(b.getCollisionBox(), b.directory + "/"
								+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox");
					} else if (ctrlState && n) newC(b.getCollisionBox());
		}
	}

	/**
	 * Move game object.
	 *
	 * @param go the go
	 */
	public void moveGameObject(GameObject go) {
		move = go;
	}

	/**
	 * Removes the key handler.
	 *
	 * @param name the name
	 */
	public void removeKeyHandler(String name) {
		try {
			String className = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
			KeyHandlerKeyCodePair khkcp = keyHandlers.remove(className + "|" + name);
			if (khkcp.up()) keyUpHandlers.get(khkcp.keyCode()).remove(khkcp.handler());
			else keyDownHandlers.get(khkcp.keyCode()).remove(khkcp.handler());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resize game object.
	 *
	 * @param go the go
	 */
	public void resizeGameObject(GameObject go) {
		resize = go;
	}

	/**
	 * Save map.
	 */
	public void saveMap() {
		if (gamepanel != null)
			gamepanel.getLgp().saveMap();
	}

	/**
	 * Sets the key handler.
	 *
	 * @param name the name
	 * @param handler the handler
	 * @param keyCode the key code
	 * @param keyUp the key up
	 */
	public void setKeyHandler(String name, Consumer<ModKeysState> handler, KeyCode keyCode, boolean keyUp) {
		try {
			String className = Class.forName(Thread.currentThread().getStackTrace()[CLASSINDEX].getClassName()).getSimpleName();
			if (keyHandlers.containsKey(className + "|" + name)) {
				KeyHandlerKeyCodePair keyHandlerKeyCodePair = keyHandlers.remove(className + "|" + name);

				// Remove the previous key-up or -down handler
				if (keyHandlerKeyCodePair.up()) keyUpHandlers.get(keyHandlerKeyCodePair.keyCode()).remove(keyHandlerKeyCodePair.handler());
				else keyDownHandlers.get(keyHandlerKeyCodePair.keyCode()).remove(keyHandlerKeyCodePair.handler());
			}

			keyHandlers.put(className + "|" + name, new KeyHandlerKeyCodePair(handler, keyCode, keyUp));

			// Add the new key-up or -down handler
			if (keyUp) {
				if (keyUpHandlers.containsKey(keyCode)) keyUpHandlers.get(keyCode).add(handler);
				else {
					List<Consumer<ModKeysState>> li = new ArrayList<>();
					li.add(handler);
					keyUpHandlers.put(keyCode, li);
				}
			} else
				if (keyDownHandlers.containsKey(keyCode)) keyDownHandlers.get(keyCode).add(handler);
				else {
					List<Consumer<ModKeysState>> li = new ArrayList<>();
					li.add(handler);
					keyDownHandlers.put(keyCode, li);
				}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop moveing game object.
	 *
	 * @param go the go
	 */
	public void stopMoveingGameObject(GameObject go) {
		move = null;
	}

	/**
	 * Stop resizeing game object.
	 *
	 * @param go the go
	 */
	public void stopResizeingGameObject(GameObject go) {
		resize = null;
	}

	/**
	 * Toggle full screen.
	 */
	public void toggleFullScreen() {
		double scaleFactorX, scaleFactorY;
		scaleFactorX = gamepanel.getScene().getWidth();
		scaleFactorY = gamepanel.getScene().getHeight();
		if (((Stage) gamepanel.getScene().getWindow()).isFullScreen())
			((Stage) gamepanel.getScene().getWindow()).setFullScreen(false);
		else((Stage) gamepanel.getScene().getWindow()).setFullScreen(true);
		if (WindowManager.getInstance().getScalingFactorX() > 1) scaleFactorX = 1;
		else scaleFactorX = gamepanel.getScene().getWidth() / scaleFactorX;
		if (WindowManager.getInstance().getScalingFactorY() > 1) scaleFactorY = 1;
		else scaleFactorY = gamepanel.getScene().getHeight() / scaleFactorY;
		System.out.println(scaleFactorX + " " + scaleFactorY);
		gamepanel.changeScalingFactor(scaleFactorX, scaleFactorY);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Input [s=" + s + ", ctrlState=" + ctrlState + ", n=" + n + "]";
	}

	/**
	 * Update.
	 *
	 * @param ms the ms
	 */
	public void update(long ms) {

	}

}
