package rngGAME;

import java.io.*;
import java.util.ArrayList;
import buildings.Building;
import javafx.event.EventTarget;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import tile.*;

public class Input {

	public boolean upPressed, downPressed, leftPressed, rightPressed, tabPressed, ctrlPressed, save;
	private long timeout;


	public void dragDetected(MouseEvent me) {
		System.out.println("Drag " + me);
	}

	public void keyPressed(KeyEvent e) {

		KeyCode code = e.getCode();

		if (timeout < System.currentTimeMillis()) {


			if(code == KeyCode.W) upPressed = true;

			if (code == KeyCode.S && !ctrlPressed) downPressed = true;

			if(code == KeyCode.A) leftPressed = true;

			if(code == KeyCode.D) rightPressed = true;
		}

		if (code == KeyCode.CONTROL) ctrlPressed = true;

		if (code == KeyCode.S && ctrlPressed) save = true;


	}

	public void keyReleased(KeyEvent e) {

		KeyCode code = e.getCode();

		if(code == KeyCode.W) upPressed = false;

		if (code == KeyCode.S) downPressed = false;

		if(code == KeyCode.A) leftPressed = false;

		if(code == KeyCode.D) rightPressed = false;

		if(code == KeyCode.TAB) tabPressed = !tabPressed;

		if (code == KeyCode.E) if (System.getProperty("edit").equals("true")) System.setProperty("edit", "false");
		else System.setProperty("edit", "true");

		if (code == KeyCode.C) if (System.getProperty("coll").equals("true")) System.setProperty("coll", "false");
		else System.setProperty("coll", "true");

		if (code == KeyCode.CONTROL) {
			ctrlPressed = false;
			save = false;
		}

		if (code == KeyCode.S) save = false;

	}

	public void keyTyped(KeyEvent e) {

	}

	public void mouseDragged(MouseEvent me) {
		System.out.println("Dragged " + me);
		EventTarget target = me.getTarget();
		if (target instanceof TextureHolder t) if (!t.isDragging())
			t.startDrag();
		else t.doDrag(me.getPickResult());
	}

	public void mouseMoved(MouseEvent me) {
		if (System.getProperty("edit").equals("true")) {
			EventTarget et= me.getTarget();
			if (et instanceof TextureHolder t) {
				for (Node e: ((Group) t.getParent()).getChildren()) ((TextureHolder) e).undrawOutlines();
				t.drawOutlines();
			} else if (et instanceof TileManager tm) {
			} else {
			}
		} else {
			EventTarget et = me.getTarget();
			if (et instanceof TextureHolder t) for (Node e: ((Group) t.getParent()).getChildren()) ((TextureHolder) e).undrawOutlines();
		}
	}

	public void mouseReleased(MouseEvent me) {
		System.out.println("Released " + me);
		EventTarget target = me.getTarget();
		if (target instanceof TextureHolder t) {
			for (Node e: ((Group) t.getParent()).getChildren())
				if (((TextureHolder) e).isDragging()) ((TextureHolder) e).endDrag();
			if (System.getProperty("coll").equals("true")) if (!save) {
				t.getPoly().getPoints().addAll(me.getX() - t.getLayoutX(), me.getY() - t.getLayoutY());
				if (t.getTile().poly == null) t.getTile().poly = new ArrayList<>();
				t.getTile().poly.add(me.getX() - t.getLayoutX());
				t.getTile().poly.add(me.getY() - t.getLayoutY());
			} else {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing the collision box of something", ".collisionbox"));
				File f = fc.showSaveDialog(t.getScene().getWindow());
				if (!f.exists()) try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					RandomAccessFile raf = new RandomAccessFile(f, "rws");
					raf.seek(0l);
					raf.writeInt(t.getPoly().getPoints().size());
					for (Double element: t.getPoly().getPoints()) raf.writeDouble(element);
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(f);
			}
		} else
			if (target instanceof Building b
					&& System.getProperty("coll").equals("true")) if (!save)
						b.getPolygon().getPoints().addAll(me.getX() - b.getLayoutX(), me.getY() - b.getLayoutY());
					else {
						FileChooser fc = new FileChooser();
						fc.setInitialDirectory(new File("."));
						fc.getExtensionFilters().add(new ExtensionFilter(
								"A file containing the collision box of something", ".collisionbox"));
						File f = fc.showSaveDialog(b.getScene().getWindow());
						if (!f.exists()) try {
							f.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							RandomAccessFile raf = new RandomAccessFile(f, "rws");
							raf.seek(0l);
							raf.writeInt(b.getPolygon().getPoints().size());
							for (Double element: b.getPolygon().getPoints()) raf.writeDouble(element);
							raf.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println(f);
					}

	}

	public void setTimeout(long i) {
		timeout = i;
		upPressed = false;
		downPressed = false;
		leftPressed = false;
		rightPressed = false;
	}

}
