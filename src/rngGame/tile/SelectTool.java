package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rngGame.main.SpielPanel;

public class SelectTool extends Rectangle {

	private boolean dragging = false;
	private final SpielPanel gp;

	private int x, y;

	public SelectTool(SpielPanel gp) {
		this.gp = gp;
		init();
	}

	private void init() {
		setStroke(Color.WHITE);
		setFill(Color.TRANSPARENT);
		setStrokeWidth(4.5);
		setDisable(true);
		setVisible(false);
	}

	public void doDrag(MouseEvent me) {
		if (dragging) {
			Node node = gp.getTileM().getMap()
					.get((int) Math.floor((me.getSceneY() - gp.getPlayer().screenY + gp.getPlayer().getY()) / gp.Bg))
					.get((int) Math.floor((me.getSceneX() - gp.getPlayer().screenX + gp.getPlayer().getX()) / gp.Bg));
			if (node instanceof TextureHolder t) {
				setWidth(t.getLayoutX() + t.getWidth() - getLayoutX());
				setHeight(t.getLayoutY() + t.getHeight() - getLayoutY());
			}
			System.out.println(me.getPickResult().getIntersectedNode());
		}
	}

	public void drawOutlines(MouseEvent me) {
		setWidth(gp.Bg);
		setHeight(gp.Bg);
		double xPos = (me.getSceneX() + gp.getPlayer().getX() - gp.getPlayer().screenX) / gp.Bg,
				yPos = (me.getSceneY() + gp.getPlayer().getY() - gp.getPlayer().screenY) / gp.Bg;
		if (xPos < 0) xPos -= 1;
		if (yPos < 0) yPos -= 1;
		x = (int) xPos * gp.Bg;
		y = (int) yPos * gp.Bg;
		setVisible(true);
	}

	public void endDrag() {
		dragging = false;
		undrawOutlines();
	}

	public boolean isDragging() { return dragging; }

	public void startDrag(MouseEvent me) {
		dragging = true;
		drawOutlines(me);
	}

	public void undrawOutlines() {
		setVisible(false);
	}

	public void update() {
		double screenX = x - gp.getPlayer().getX() + gp.getPlayer().screenX;
		double screenY = y - gp.getPlayer().getY() + gp.getPlayer().screenY;
		setLayoutX(screenX);
		setLayoutY(screenY);
	}
}
