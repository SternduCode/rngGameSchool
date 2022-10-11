package rngGame.main;

import java.io.*;
import java.util.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.buildings.Building;
import rngGame.tile.TextureHolder;

public class Input {

	private record KeyHandlerKeyCodePair(Runnable handler, KeyCode keyCode, boolean up) {}

	private static final Input INSTANCE = new Input();

	private final Map<KeyCode, List<Runnable>> keyDownHandlers = new HashMap<>(), keyUpHandlers = new HashMap<>();

	private final Map<String, KeyHandlerKeyCodePair> keyHandlers = new HashMap<>();

	public boolean w, s, a, d, tabPressed, ctrlPressed, p, b, h, n, r;

	private GameObject move, resize;

	public List<Image> comp = new ArrayList<>();

	private SpielPanel gp;

	private Input() {
		setKeyHandler("ControlDown", () -> {
			ctrlPressed = true;
		}, KeyCode.CONTROL, false);
		setKeyHandler("ControlUp", () -> {
			ctrlPressed = false;
		}, KeyCode.CONTROL, true);
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
		ctrlPressed = false;
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

	public void keyPressed(KeyEvent e) {

		KeyCode code = e.getCode();

		if (keyDownHandlers.containsKey(code)) keyDownHandlers.get(code).forEach(Runnable::run);

		if (code == KeyCode.W) w = true;

		if (code == KeyCode.S) s = true;

		if (code == KeyCode.A) a = true;

		if (code == KeyCode.D) d = true;

		if (code == KeyCode.N) n = true;

		if (code == KeyCode.R) r = true;

		if (ctrlPressed && r) reload();


	}

	public void keyReleased(KeyEvent e) {

		KeyCode code = e.getCode();

		if (keyUpHandlers.containsKey(code)) keyUpHandlers.get(code).forEach(Runnable::run);

		if (code == KeyCode.W) w = false;

		if (code == KeyCode.S) s = false;

		if (code == KeyCode.A) a = false;

		if (code == KeyCode.D) d = false;

		if(code == KeyCode.TAB) tabPressed = !tabPressed;

		if (code == KeyCode.B) b = !b;

		if (code == KeyCode.P) p = !p;

		if (code == KeyCode.H) h = !h;

		if (code == KeyCode.L) print();

		if (e.getText().equalsIgnoreCase("ö")) saveMap();

		if (code == KeyCode.E) if (System.getProperty("edit").equals("true")) System.setProperty("edit", "false");
		else System.setProperty("edit", "true");

		if (code == KeyCode.C) if (System.getProperty("coll").equals("true")) System.setProperty("coll", "false");
		else System.setProperty("coll", "true");

		if (code == KeyCode.N) n = false;

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
		if (gp != null) if (System.getProperty("edit").equals("true")) gp.getSelectTool().drawOutlines(me);
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
					if (System.getProperty("coll").equals("true")) if ((!ctrlPressed || !s) && (!ctrlPressed || !n)) {
						t.getPoly().getPoints().addAll(me.getX() - t.getLayoutX(), me.getY() - t.getLayoutY());
						if (t.getTile().poly == null) t.getTile().poly = new ArrayList<>();
						t.getTile().poly.add(me.getX() - t.getLayoutX());
						t.getTile().poly.add(me.getY() - t.getLayoutY());
					} else if (ctrlPressed && s) save(t.getPoly());
					else if (ctrlPressed && n) newC(t.getPoly());
			} else
				if (target instanceof Building b
						&& System.getProperty("coll").equals("true"))
					if ((!ctrlPressed || !s) && (!ctrlPressed || !n))
						b.getCollisionBox().getPoints().addAll(me.getX() - b.getLayoutX(), me.getY() - b.getLayoutY());
					else if (ctrlPressed && s) save(b.getCollisionBox());
					else if (ctrlPressed && n) newC(b.getCollisionBox());
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

	public void setKeyHandler(String name, Runnable handler, KeyCode keyCode, boolean keyUp) {
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
					List<Runnable> li = new ArrayList<>();
					li.add(handler);
					keyUpHandlers.put(keyCode, li);
				}
			} else if (keyDownHandlers.containsKey(keyCode)) keyDownHandlers.get(keyCode).add(handler);
			else {
				List<Runnable> li = new ArrayList<>();
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
		return "Input [w=" + w + ", s=" + s + ", a=" + a + ", d=" + d + ", tabPressed="
				+ tabPressed + ", ctrlPressed=" + ctrlPressed + ", p=" + p + ", b=" + b + ", h="
				+ h + ", n=" + n + "]";
	}

	public void update(long ms) {

	}

}
