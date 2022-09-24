package rngGame.tile;

import java.util.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rngGame.main.SpielPanel;
import rngGame.ui.PartialFillDialog;

public class SelectTool extends Rectangle {

	private boolean dragging = false;
	private final SpielPanel gp;

	private int x, y;
	private Menu select, fill, partialFill;

	public SelectTool(SpielPanel gp) {
		this.gp = gp;
		init();
	}

	private void handleContextMenu(ActionEvent e) {
		System.out.println(e);
		if (((MenuItem) e.getSource()).getParentMenu() == partialFill) {// tileM.getTileAt(globalx, globaly);
			PartialFillDialog pfd = new PartialFillDialog(this, ((MenuItemWTile) e.getSource()).getTile());
			Optional<Boolean> result = pfd.showAndWait();
			setFill(null);
			System.out.println(result);
			if (result.isPresent() && result.get()) {
				Boolean[] matrix = pfd.getMatrix();
				System.out.println(Arrays.toString(matrix));
				int matrixIdx = 0;
				for (int i = 0; i < getHeight(); i += gp.Bg) for (int j = 0; j < getWidth(); j += gp.Bg, matrixIdx++)
					if (matrix[matrixIdx]) gp.getTileM().getTileAt(x + j, y + i)
					.setTile(((MenuItemWTile) e.getSource()).getTile());
			}
		} else for (int i = 0; i < getWidth(); i += gp.Bg) for (int j = 0; j < getHeight(); j += gp.Bg) gp.getTileM().getTileAt(x + i,
				y + j)
		.setTile(((MenuItemWTile) e.getSource()).getTile());
	}

	private void init() {
		setStroke(Color.color(1, 1, 1, .75));
		setFill(Color.TRANSPARENT);
		setStrokeWidth(2.5);
		setDisable(true);
		setVisible(false);
		select = new Menu("Select");
		fill = new Menu("Fill");
		partialFill = new Menu("Partial Fill");
		select.getItems().addAll(fill, partialFill);
	}

	public void doDrag(MouseEvent me) {
		if (dragging) {
			Node node = gp.getTileM().getTileAt(me.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
					me.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
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
		double xPos = (me.getSceneX() + gp.getPlayer().getX() - gp.getPlayer().getScreenX()) / gp.Bg,
				yPos = (me.getSceneY() + gp.getPlayer().getY() - gp.getPlayer().getScreenY()) / gp.Bg;
		if (xPos < 0) xPos -= 1;
		if (yPos < 0) yPos -= 1;
		x = (int) xPos * gp.Bg;
		y = (int) yPos * gp.Bg;
		setVisible(true);
	}

	public void endDrag() {
		dragging = false;
		ContextMenu cm = gp.getTileM().getCM();
		cm.getItems().clear();
		cm.getItems().add(select);
		fill.getItems().clear();
		partialFill.getItems().clear();
		for (Tile t: gp.getTileM().getTiles()) {
			MenuItemWTile menuitem = new MenuItemWTile(t.name,
					new ImageView(ImgUtil.resizeImage(t.images.get(0),
							(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 16, 16)),
					t);
			menuitem.setOnAction(this::handleContextMenu);
			fill.getItems().add(menuitem);
			menuitem = new MenuItemWTile(t.name,
					new ImageView(ImgUtil.resizeImage(t.images.get(0),
							(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 16, 16)),
					t);
			menuitem.setOnAction(this::handleContextMenu);
			partialFill.getItems().add(menuitem);
		}
		cm.show(this, getLayoutX() + getScene().getWindow().getX(),
				getLayoutY() + +getScene().getWindow().getY());

	}

	public boolean isDragging() { return dragging; }

	public void startDrag(MouseEvent me) {
		dragging = true;
		drawOutlines(me);
	}

	public void undrawOutlines() {
		if (!gp.getTileM().getCM().isShowing()
				|| gp.getTileM().getCM().getAnchorX() != getLayoutX() + getScene().getWindow().getX()
				|| gp.getTileM().getCM().getAnchorY() != getLayoutY() + getScene().getWindow().getY())
			setVisible(false);
	}

	public void update() {
		boolean moveCm = false;
		if (gp.getTileM().getCM().isShowing()
				&& gp.getTileM().getCM().getAnchorX() == getLayoutX() + getScene().getWindow().getX()
				&& gp.getTileM().getCM().getAnchorY() == getLayoutY() + getScene().getWindow().getY())
			moveCm = true;
		double screenX = x - gp.getPlayer().getX() + gp.getPlayer().getScreenX();
		double screenY = y - gp.getPlayer().getY() + gp.getPlayer().getScreenY();
		setLayoutX(screenX);
		setLayoutY(screenY);
		if (moveCm) {
			gp.getTileM().getCM().setAnchorX(screenX + getScene().getWindow().getX());
			gp.getTileM().getCM().setAnchorY(screenY + getScene().getWindow().getY());
		}
	}
}
