package tile;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TextureHolder extends Pane {

	private final Tile tile;

	private final ImageView iv;

	private final Rectangle rect;

	public TextureHolder(Tile tile, int layoutX, int layoutY) {
		this.tile = tile;
		iv = new ImageView(tile.Image);
		// tile.Image.getPixelReader();
		// new WritableImage(null, layoutX, layoutY)
		iv.setDisable(true);
		getChildren().add(iv);
		setLayoutX(layoutX);
		setLayoutY(layoutY);
		rect = new Rectangle(0, 0, tile.Image.getWidth(), tile.Image.getHeight());
		getChildren().add(rect);
		rect.setStroke(Color.WHITE);
		rect.setFill(Color.TRANSPARENT);
		rect.setStrokeWidth(5);
		rect.setDisable(false);
		rect.setVisible(false);
	}

	public void drawOutlines() {
		rect.setVisible(true);
	}

	public void undrawOutlines() {
		rect.setVisible(false);
	}

}
