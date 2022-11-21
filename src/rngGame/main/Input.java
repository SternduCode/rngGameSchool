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

public class Input {

	private record KeyHandlerKeyCodePair(Consumer<ModKeysState> handler, KeyCode keyCode, boolean up) {}

	public record ModKeysState(boolean isControlPressed, boolean isShiftPressed, boolean isCapsPressed,
			boolean isSuperPressed, boolean isAltPressed, boolean isAltgrPressed) {}

	private static final Input INSTANCE = new Input();

	private final Map<KeyCode, List<Consumer<ModKeysState>>> keyDownHandlers = new HashMap<>(),
			keyUpHandlers = new HashMap<>();

	private final Map<String, KeyHandlerKeyCodePair> keyHandlers = new HashMap<>();

	private boolean n, s;

	private boolean ctrlState, shiftState, altState, superState, capsState, altgrState;

	private GameObject move, resize;

	private GamePanel gamepanel;

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
			if (System.getProperty("alternateUpdate").equals("true")) System.setProperty("alternateUpdate", "false");
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
			if (System.getProperty("teleport").equals("true")) System.setProperty("teleport", "false");
			else System.setProperty("teleport", "true");
		}, KeyCode.T, false);
	}

	public static Input getInstance() { return INSTANCE; }

	private void newC(Polygon p) {
		p.getPoints().clear();
		p.setVisible(false);
		if (p.getParent() instanceof TextureHolder th) if (th.getTile().poly != null) th.getTile().poly.clear();
	}

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
				raf.writeDouble((long) (element / ((s = !s) ? gamepanel.getScalingFactorX() : gamepanel.getScalingFactorY())));
			raf.setLength(4l + t.getPoints().size() * 8l);
			raf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(f);
	}


	protected void setGamePanel(GamePanel gp) {
		gamepanel=gp;
	}

	public void dragDetected(MouseEvent me) {}

	public boolean isAltgrPressed() { return altgrState; }

	public boolean isAltPressed() { return altState; }

	public boolean isCapsPressed() { return capsState; }

	public boolean isCtrlPressed() { return ctrlState; }

	public boolean isShiftPressed() { return shiftState; }

	public boolean isSuperPressed() { return superState; }

	public void keyPressed(KeyEvent e) {
		if (!gamepanel.isInLoadingScreen()) {
			KeyCode code = e.getCode();

			ModKeysState modKeysState = new ModKeysState(ctrlState, shiftState, capsState, superState, altState,
					altgrState);

			if (keyDownHandlers.containsKey(code)) keyDownHandlers.get(code).forEach(con -> con.accept(modKeysState));

			if (code == KeyCode.N) n = true;

			if (code == KeyCode.S) s = true;
		}

	}

	public void keyReleased(KeyEvent e) {
		if (!gamepanel.isInLoadingScreen()) {

			KeyCode code = e.getCode();

			ModKeysState modKeysState = new ModKeysState(ctrlState, shiftState, capsState, superState, altState,
					altgrState);

			if (keyUpHandlers.containsKey(code)) keyUpHandlers.get(code).forEach(con -> con.accept(modKeysState));

			if (e.getText().equalsIgnoreCase("ö")) saveMap();

			if (code == KeyCode.E) if (System.getProperty("edit").equals("true")) System.setProperty("edit", "false");
			else System.setProperty("edit", "true");

			if (code == KeyCode.C) if (System.getProperty("coll").equals("true")) System.setProperty("coll", "false");
			else System.setProperty("coll", "true");

			if (code == KeyCode.N) n = false;

			if (code == KeyCode.S) s = false;
		}
	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent me) {
		System.out.println("Dragged " + me);
		if (gamepanel != null && !gamepanel.isInLoadingScreen()) if (move != null) {
			move.setX((long) (me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX() - move.getWidth() / 2));
			move.setY((long) (me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY() - move.getHeight() / 2));
		}
		else if (resize != null) {
			resize.setReqWidth((int) (me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX() - resize.getX()));
			resize.setReqHeight(
					(int) (me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY() - resize.getY()));
			resize.reloadTextures();
		} else {
			Node target = gamepanel.getTileM().getObjectAt(me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX(),
					me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY());
			if (!gamepanel.getSelectTool().isDragging() && target instanceof TextureHolder)
				gamepanel.getSelectTool().startDrag(me);
			else if (gamepanel.getSelectTool().isDragging()) gamepanel.getSelectTool().doDrag(me);
			else if (target instanceof GameObject go) {

			} else {

			}
		}
	}

	public void mouseMoved(MouseEvent me) {
		if (gamepanel != null) if (!gamepanel.getSelectTool().isDragging() && !gamepanel.getTileM().getCM().isShowing())
			if (System.getProperty("edit").equals("true")) gamepanel.getSelectTool().drawOutlines(me);
			else gamepanel.getSelectTool().undrawOutlines();
	}

	public void mouseReleased(MouseEvent me) {
		System.out.println("Released " + me);
		if (gamepanel != null) {
			Node target = gamepanel.getTileM().getObjectAt(me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX(),
					me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY());
			if (System.getProperty("teleport").equals("true")) gamepanel.getPlayer().setPosition(
					me.getSceneX() - gamepanel.getPlayer().getScreenX() + gamepanel.getPlayer().getX()-gamepanel.getPlayer().getColliBoxX(),
					me.getSceneY() - gamepanel.getPlayer().getScreenY() + gamepanel.getPlayer().getY()-gamepanel.getPlayer().getColliBoxY());
			else if (target instanceof TextureHolder t) {

				if (gamepanel.getSelectTool().isDragging()) gamepanel.getSelectTool().endDrag();
				else
					if (System.getProperty("coll").equals("true")) if ((!ctrlState || !s) && (!ctrlState || !n)) {
						t.getPoly().getPoints().addAll(me.getX() - t.getLayoutX(), me.getY() - t.getLayoutY());
						if (t.getTile().poly == null) t.getTile().poly = new ArrayList<>();
						t.getTile().poly.add(me.getX() - t.getLayoutX());
						t.getTile().poly.add(me.getY() - t.getLayoutY());
					} else if (ctrlState && s) {
						String[] sp = t.getTile().name.split("[.]");
						save(t.getPoly(),
								gamepanel.getTileM().getDir() + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox");
					} else if (ctrlState && n) newC(t.getPoly());
			} else
				if (target instanceof Building b
						&& System.getProperty("coll").equals("true"))
					if ((!ctrlState || !s) && (!ctrlState || !n))
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

	public void moveGameObject(GameObject go) {
		move = go;
	}

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

	public void resizeGameObject(GameObject go) {
		resize = go;
	}

	public void saveMap() {
		if (gamepanel != null)
			gamepanel.saveMap();
	}

	public void setKeyHandler(String name, Consumer<ModKeysState> handler, KeyCode keyCode, boolean keyUp) {
		try {
			String className = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
			if (keyHandlers.containsKey(className + "|" + name)) {
				KeyHandlerKeyCodePair khkcp = keyHandlers.remove(className + "|" + name);
				if (khkcp.up()) keyUpHandlers.get(khkcp.keyCode()).remove(khkcp.handler());
				else keyDownHandlers.get(khkcp.keyCode()).remove(khkcp.handler());
			}
			keyHandlers.put(className + "|" + name, new KeyHandlerKeyCodePair(handler, keyCode, keyUp));
			if (keyUp) {
				if (keyUpHandlers.containsKey(keyCode)) keyUpHandlers.get(keyCode).add(handler);
				else {
					List<Consumer<ModKeysState>> li = new ArrayList<>();
					li.add(handler);
					keyUpHandlers.put(keyCode, li);
				}
			} else if (keyDownHandlers.containsKey(keyCode)) keyDownHandlers.get(keyCode).add(handler);
			else {
				List<Consumer<ModKeysState>> li = new ArrayList<>();
				li.add(handler);
				keyDownHandlers.put(keyCode, li);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void stopMoveingGameObject(GameObject go) {
		move = null;
	}

	public void stopResizeingGameObject(GameObject go) {
		resize = null;
	}

	public void toggleFullScreen() {
		double scaleFactorX, scaleFactorY;
		scaleFactorX = gamepanel.getScene().getWidth();
		scaleFactorY = gamepanel.getScene().getHeight();
		if (((Stage) gamepanel.getScene().getWindow()).isFullScreen())
			((Stage) gamepanel.getScene().getWindow()).setFullScreen(false);
		else((Stage) gamepanel.getScene().getWindow()).setFullScreen(true);
		if (gamepanel.getScalingFactorX() > 1) scaleFactorX = 1;
		else scaleFactorX = gamepanel.getScene().getWidth() / scaleFactorX;
		if (gamepanel.getScalingFactorY() > 1) scaleFactorY = 1;
		else scaleFactorY = gamepanel.getScene().getHeight() / scaleFactorY;
		System.out.println(scaleFactorX + " " + scaleFactorY);
		gamepanel.scaleTextures(scaleFactorX, scaleFactorY);
	}

	@Override
	public String toString() {
		return "Input [s=" + s + ", ctrlState=" + ctrlState + ", n=" + n + "]";
	}

	public void update(long ms) {

	}

}
