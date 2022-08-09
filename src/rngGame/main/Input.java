package rngGame.main;

import java.io.*;
import java.util.ArrayList;
import javafx.event.EventTarget;
import javafx.scene.input.*;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.buildings.Building;
import rngGame.entity.NPC;
import rngGame.tile.TextureHolder;

public class Input {

	public boolean w, s, a, d, tabPressed, ctrlPressed, p, b, h, n;

	private SpielPanel gp;


	private void newC(Polygon p) {
		ctrlPressed = false;
		p.getPoints().clear();
		p.setVisible(false);
		if (p.getParent() instanceof TextureHolder th) th.getTile().poly.clear();
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

		if (code == KeyCode.W) w = true;

		if (code == KeyCode.S) s = true;

		if (code == KeyCode.A) a = true;

		if (code == KeyCode.D) d = true;

		if (code == KeyCode.CONTROL) ctrlPressed = true;


		if (code == KeyCode.N) n = true;


	}

	public void keyReleased(KeyEvent e) {

		KeyCode code = e.getCode();

		if (code == KeyCode.W) w = false;

		if (code == KeyCode.S) s = false;

		if (code == KeyCode.A) a = false;

		if (code == KeyCode.D) d = false;

		if(code == KeyCode.TAB) tabPressed = !tabPressed;

		if (code == KeyCode.B) b = !b;

		if (code == KeyCode.P) p = !p;

		if (code == KeyCode.H) h = !h;

		if (e.getText().equalsIgnoreCase("ö")) saveMap();

		if (code == KeyCode.E) if (System.getProperty("edit").equals("true")) System.setProperty("edit", "false");
		else System.setProperty("edit", "true");

		if (code == KeyCode.C) if (System.getProperty("coll").equals("true")) System.setProperty("coll", "false");
		else System.setProperty("coll", "true");

		if (code == KeyCode.CONTROL) ctrlPressed = false;

		if (code == KeyCode.N) n = false;

	}

	public void keyTyped(KeyEvent e) {}

	public void mouseDragged(MouseEvent me) {
		System.out.println("Dragged " + me);
		if (!gp.getSelectTool().isDragging() && me.getTarget() instanceof TextureHolder)
			gp.getSelectTool().startDrag(me);
		else if (me.getTarget() instanceof TextureHolder) gp.getSelectTool().doDrag(me);
		else if (me.getTarget() instanceof Building) {

		} else if (me.getTarget() instanceof NPC) {

		} else {

		}
	}

	public void mouseMoved(MouseEvent me) {
		if (System.getProperty("edit").equals("true")) gp.getSelectTool().drawOutlines(me);
		else gp.getSelectTool().undrawOutlines();
	}

	public void mouseReleased(MouseEvent me) {
		System.out.println("Released " + me);
		EventTarget target = me.getTarget();
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

	public void saveMap() {
		gp.saveMap();
	}

}