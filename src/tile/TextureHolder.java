package tile;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TextureHolder extends Pane {

	private final Tile tile;

	private final ImageView iv;

	private final Rectangle rect;

	private boolean dragging = false;

	public TextureHolder(Tile tile, int layoutX, int layoutY) {
		this.tile = tile;
		iv = new ImageView(tile.Image);
		// tile.Image.getPixelReader();
		// new WritableImage(null, layoutX, layoutY)
		iv.setDisable(true);
		getChildren().add(iv);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		rect = new Rectangle();
		getChildren().add(rect);
		rect.setStroke(Color.WHITE);
		rect.setFill(Color.TRANSPARENT);
		rect.setStrokeWidth(4.5);
		rect.setDisable(false);
		rect.setVisible(false);
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
		rect.setWidth(tile.Image.getWidth());
		rect.setHeight(tile.Image.getHeight());
		rect.setVisible(true);
	}

	public void endDrag() {
		dragging = false;
		undrawOutlines();
	}

	public boolean isDragging() { return dragging; }

	public void startDrag() {
		dragging = true;
		drawOutlines();
	}

	public void undrawOutlines() {
		rect.setVisible(false);
	}

}
