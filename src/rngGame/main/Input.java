package rngGame.main;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.shape.Polygon;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
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

	private boolean n, s, r;

	private boolean ctrlState, shiftState, altState, superState, capsState, altgrState;

	private GameObject move, resize;

	public List<Image> comp = new ArrayList<>();

	private SpielPanel gp;

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
			((Stage) gp.getScene().getWindow()).setFullScreenExitHint("");
			((Stage) gp.getScene().getWindow()).setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			if (((Stage) gp.getScene().getWindow()).isFullScreen())
				((Stage) gp.getScene().getWindow()).setFullScreen(false);
			else((Stage) gp.getScene().getWindow()).setFullScreen(true);
		}, KeyCode.F11, false);
		setKeyHandler("ContextMenu", mod -> {
			// TODO tbd
		}, KeyCode.CONTEXT_MENU, false);
	}

	public static Input getInstance() { return INSTANCE; }

	private void newC(Polygon p) {
		p.getPoints().clear();
		p.setVisible(false);
		if (p.getParent() instanceof TextureHolder th) if (th.getTile().poly != null) th.getTile().poly.clear();
	}

	private void reload() {
		if (gp != null)
			gp.reload();
	}


	private void save(Polygon p) {
		ctrlState = false;
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("./res"));
		fc.getExtensionFilters().add(new ExtensionFilter(
				"A file containing the collision box of something", "*.collisionbox"));
		File f = fc.showSaveDialog(p.getScene().getWindow());
		if (f == null) return;
		if (!f.exists()) try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "rws");
			raf.seek(0l);
			raf.writeInt(p.getPoints().size());
			for (Double element: p.getPoints()) raf.writeDouble(element);
			raf.setLength(4l + p.getPoints().size() * 8l);
			raf.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(f);
	}

	protected void setSpielPanel(SpielPanel gp) {
		this.gp=gp;
	}

	public void dragDetected(MouseEvent me) {
		System.out.println("Drag " + me);
	}

	public boolean isAltgrPressed() { return altgrState; }

	public boolean isAltPressed() { return altState; }

	public boolean isCapsPressed() { return capsState; }

	public boolean isCtrlPressed() { return ctrlState; }

	public boolean isShiftPressed() { return shiftState; }

	public boolean isSuperPressed() { return superState; }

	public void keyPressed(KeyEvent e) {

		KeyCode code = e.getCode();

		ModKeysState modKeysState = new ModKeysState(ctrlState, shiftState, capsState, superState, altState,
				altgrState);

		if (keyDownHandlers.containsKey(code)) keyDownHandlers.get(code).forEach(con -> con.accept(modKeysState));

		if (code == KeyCode.N) n = true;

		if (code == KeyCode.S) s = true;

		if (code == KeyCode.R) r = true;

		if (ctrlState && r) reload();


	}

	public void keyReleased(KeyEvent e) {

		KeyCode code = e.getCode();

		ModKeysState modKeysState = new ModKeysState(ctrlState, shiftState, capsState, superState, altState,
				altgrState);

		if (keyUpHandlers.containsKey(code)) keyUpHandlers.get(code).forEach(con -> con.accept(modKeysState));

		if (code == KeyCode.L) print();

		if (e.getText().equalsIgnoreCase("ö")) saveMap();

		if (code == KeyCode.E) if (System.getProperty("edit").equals("true")) System.setProperty("edit", "false");
		else System.setProperty("edit", "true");

		if (code == KeyCode.C) if (System.getProperty("coll").equals("true")) System.setProperty("coll", "false");
		else System.setProperty("coll", "true");

		if (code == KeyCode.N) n = false;

		if (code == KeyCode.S) s = false;

		if (code == KeyCode.R) r = false;

		if (code == KeyCode.F) gp.toggleFpssLabelVisible();

		if (code == KeyCode.M)
			if (System.getProperty("alternateUpdate").equals("true")) System.setProperty("alternateUpdate", "false");
			else System.setProperty("alternateUpdate", "true");

	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent me) {
		System.out.println("Dragged " + me);
		if (gp != null) if (move != null) {
			move.setX((long) (me.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX() - move.getWidth() / 2));
			move.setY((long) (me.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY() - move.getHeight() / 2));
		}
		else if (resize != null) {
			resize.setReqWidth((int) (me.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX() - resize.getX()));
			resize.setReqHeight(
					(int) (me.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY() - resize.getY()));
			resize.reloadTextures();
		} else {
			Node target = gp.getTileM().getObjectAt(me.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
					me.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			if (!gp.getSelectTool().isDragging() && target instanceof TextureHolder)
				gp.getSelectTool().startDrag(me);
			else if (gp.getSelectTool().isDragging()) gp.getSelectTool().doDrag(me);
			else if (target instanceof GameObject go) {

			} else {

			}
		}
	}

	public void mouseMoved(MouseEvent me) {
		if (gp != null) if (!gp.getSelectTool().isDragging() && !gp.getTileM().getCM().isShowing())
			if (System.getProperty("edit").equals("true")) gp.getSelectTool().drawOutlines(me);
		else gp.getSelectTool().undrawOutlines();
	}

	public void mouseReleased(MouseEvent me) {
		System.out.println("Released " + me);
		if (gp != null) {
			Node target = gp.getTileM().getObjectAt(me.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
					me.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			if (target instanceof TextureHolder t) {

				if (gp.getSelectTool().isDragging()) gp.getSelectTool().endDrag();
				else
					if (System.getProperty("coll").equals("true")) if ((!ctrlState || !s) && (!ctrlState || !n)) {
						t.getPoly().getPoints().addAll(me.getX() - t.getLayoutX(), me.getY() - t.getLayoutY());
						if (t.getTile().poly == null) t.getTile().poly = new ArrayList<>();
						t.getTile().poly.add(me.getX() - t.getLayoutX());
						t.getTile().poly.add(me.getY() - t.getLayoutY());
					} else if (ctrlState && s) save(t.getPoly());
					else if (ctrlState && n) newC(t.getPoly());
			} else
				if (target instanceof Building b
						&& System.getProperty("coll").equals("true"))
					if ((!ctrlState || !s) && (!ctrlState || !n))
						b.getCollisionBox().getPoints().addAll(me.getX() - b.getLayoutX(), me.getY() - b.getLayoutY());
					else if (ctrlState && s) save(b.getCollisionBox());
					else if (ctrlState && n) newC(b.getCollisionBox());
		}
	}

	public void moveGameObject(GameObject go) {
		move = go;
	}

	public void print() {
		for (Image img:comp) {
			long result = 1;

			for (int i =0;i<img.getWidth();i++)for (int j=0;j<img.getHeight();j++)
				result = 31 * result + img.getPixelReader().getArgb(i, j);
			System.out.println(result);
		}
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
		if (gp != null)
			gp.saveMap();
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

	@Override
	public String toString() {
		return "Input [s=" + s + ", ctrlState=" + ctrlState + ", n=" + n + "]";
	}

	public void update(long ms) {

	}

}
