package rngGame.tile;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class TextureHolder extends Pane {

	private Tile tile;

	private final ImageView iv;
	private final Polygon poly;

	private final double fps = 7.5;

	public long spriteCounter = 0;
	public int spriteNum = 0;

	private int hc;

	public TextureHolder(Tile tile, double layoutX, double layoutY, ContextMenu cm,
			ObjectProperty<TextureHolder> requestor) {
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				System.out.println("fth");
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

		poly = new Polygon();
		poly.setDisable(true);
		poly.setFill(Color.color(1, 0, 0, 0.75));
		poly.getPoints()
		.addAll(tile.poly != null ? tile.poly : new ArrayList<>());
		if (tile.poly != null)
			hc = tile.poly.hashCode();
		getChildren().add(poly);

		// TODO lighing iv.setOpacity(0.5);
	}



	public Polygon getPoly() {
		return poly;
	}

	public Tile getTile() {
		return tile;
	}

	public void setTile(Tile tile) {
		this.tile=tile;
		poly.getPoints().clear();
		poly.getPoints()
		.addAll(tile.poly != null ? tile.poly : new ArrayList<>());
		iv.setImage(tile.images.get(0));
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
