package tile;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.ImageView;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class TextureHolder extends Pane {

	private Tile tile;

	private final ImageView iv;

	private final Rectangle rect;
	private final Polygon poly;

	private final double fps = 8;

	public long spriteCounter = 0;
	public int spriteNum = 0;

	private int hc;

	private boolean dragging = false;

	public TextureHolder(Tile tile, int layoutX, int layoutY, ContextMenu cm, ObjectProperty<TextureHolder> requestor) {
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestor.set(TextureHolder.this);
				cm.show(TextureHolder.this, e.getScreenX(), e.getScreenY());
			}
		});
		this.tile = tile;
		iv = new ImageView(tile.images.get(0));
		// tile.Image.getPixelReader();
		// new WritableImage(null, layoutX, layoutY)
		iv.setDisable(true);
		getChildren().add(iv);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		rect = new Rectangle();
		rect.setStroke(Color.WHITE);
		rect.setFill(Color.TRANSPARENT);
		rect.setStrokeWidth(4.5);
		rect.setDisable(true);
		rect.setVisible(false);

		poly = new Polygon();
		poly.setDisable(true);
		poly.setFill(Color.color(1, 0, 0, 0.75));
		poly.getPoints()
		.addAll(tile.poly != null ? tile.poly : new ArrayList<>());
		if (tile.poly != null)
			hc = tile.poly.hashCode();
		getChildren().add(poly);
		getChildren().add(rect);

		// TODO lighing iv.setOpacity(0.5);
	}

	public void doDrag(PickResult pickResult) {
		if (dragging) {
			Node node = pickResult.getIntersectedNode();
			if (node instanceof TextureHolder t) {
				rect.setWidth(t.getLayoutX() + t.getWidth() - rect.getParent().getLayoutX());
				rect.setHeight(t.getLayoutY() + t.getHeight() - rect.getParent().getLayoutY());
			}
			System.out.println(pickResult.getIntersectedNode());
		}
	}

	public void drawOutlines() {
		rect.setWidth(tile.images.get(spriteNum).getWidth());
		rect.setHeight(tile.images.get(spriteNum).getHeight());
		rect.setVisible(true);
	}

	public void endDrag() {
		dragging = false;
		undrawOutlines();
	}

	public Polygon getPoly() {
		return poly;
	}

	public Tile getTile() {
		return tile;
	}

	public boolean isDragging() { return dragging; }

	public void setTile(Tile tile) {
		this.tile=tile;
		poly.getPoints().clear();
		poly.getPoints()
		.addAll(tile.poly != null ? tile.poly : new ArrayList<>());
		iv.setImage(tile.images.get(0));
	}

	public void startDrag() {
		dragging = true;
		drawOutlines();
	}

	public void undrawOutlines() {
		rect.setVisible(false);
	}

	public void update() {

		if (tile.poly != null && tile.poly.hashCode() != hc) {
			poly.getPoints().clear();
			poly.getPoints().addAll(tile.poly);
			hc = tile.poly.hashCode();
		}

		if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
			spriteCounter = System.currentTimeMillis();
			spriteNum++;
		}

		if (spriteNum >= tile.images.size()) spriteNum = 0;
		iv.setImage(tile.images.get(spriteNum));

		if (System.getProperty("coll").equals("true") && poly.getPoints().size() > 0)
			poly.setVisible(true);
		else
			poly.setVisible(false);
	}

}
