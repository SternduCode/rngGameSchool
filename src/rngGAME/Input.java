package rngGAME;

import javafx.event.EventTarget;
import javafx.scene.*;
import javafx.scene.input.*;
import tile.*;

public class Input {

	public boolean upPressed, downPressed, leftPressed, rightPressed;


	public void dragDetected(MouseEvent me) {
		System.out.println("Drag " + me);
	}

	public void keyPressed(KeyEvent e) {

		KeyCode code = e.getCode();

		if(code == KeyCode.W) upPressed = true;

		if(code == KeyCode.S) downPressed = true;

		if(code == KeyCode.A) leftPressed = true;

		if(code == KeyCode.D) rightPressed = true;

	}

	public void keyReleased(KeyEvent e) {

		KeyCode code = e.getCode();

		if(code == KeyCode.W) upPressed = false;

		if(code == KeyCode.S) downPressed = false;

		if(code == KeyCode.A) leftPressed = false;

		if(code == KeyCode.D) rightPressed = false;

		if (code == KeyCode.E) if (System.getProperty("edit").equals("true")) System.setProperty("edit", "false");
		else System.setProperty("edit", "true");

	}

	public void keyTyped(KeyEvent e) {

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
	}

}
