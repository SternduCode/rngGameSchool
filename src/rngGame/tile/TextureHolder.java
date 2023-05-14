package rngGame.tile;

import java.util.*;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class TextureHolder.
 */
public class TextureHolder extends Pane {

	/** The tile. */
	private Tile tile;

	/** The gp. */
	@SuppressWarnings("unused")
	private GamePanel gp;

	/** The iv. */
	private final ImageView iv;

	/** The poly. */
	private final Polygon poly;

	/** The fps. */
	private double fps = 7.5;

	/** The y. */
	private double x, y;

	/** The menu. */
	private final Menu menu;

	/** The fps I. */
	private final MenuItem position, fpsI;

	/** The hc. */
	private int hc;

	/**
	 * Instantiates a new texture holder.
	 *
	 * @param tile the tile
	 * @param gp the gp
	 * @param layoutX the layout X
	 * @param layoutY the layout Y
	 * @param cm the cm
	 * @param requestor the requestor
	 * @param x the x
	 * @param y the y
	 */
	public TextureHolder(Tile tile, GamePanel gp, double layoutX, double layoutY, ContextMenu cm,
			ObjectProperty<TextureHolder> requestor, double x, double y) {
		menu = new Menu("Texture Holder");
		position = new MenuItem();
		fpsI = new MenuItem();
		menu.setStyle("-fx-font-size: 20;");
		position.setStyle("-fx-font-size: 20;");
		fpsI.setStyle("-fx-font-size: 20;");
		fpsI.setOnAction(this::setFPS);
		menu.getItems().addAll(position,fpsI);
		setOnContextMenuRequested(e -> {
			if ("true".equals(System.getProperty("edit"))) {
				requestor.set(TextureHolder.this);
				position.setText("Position: "+x+" "+y);
				fpsI.setText("FPS: "+fps);
				cm.getItems().clear();
				cm.getItems().addAll(gp.getTileManager().getMenus());
				cm.getItems().add(menu);
				cm.show(TextureHolder.this, e.getScreenX(), e.getScreenY());
			}
		});
		this.tile = tile;
		fps = tile.fps;
		iv = new ImageView(tile.images[0]);
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

	/**
	 * Sets the fps.
	 *
	 * @param e the new fps
	 */
	private void setFPS(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog("" + fps);
		dialog.setTitle("FPS");
		dialog.setGraphic(null);
		dialog.setHeaderText("");
		dialog.setContentText("Please enter the new FPS value:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) try {
			tile.fps = Double.parseDouble(result.get());
		} catch (NumberFormatException e2) {
		}
	}

	/**
	 * Gets the poly.
	 *
	 * @return the poly
	 */
	public Polygon getPoly() {
		return poly;
	}

	/**
	 * Gets the tile.
	 *
	 * @return the tile
	 */
	public Tile getTile() {
		return tile;
	}



	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public double getX() { return x; }

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public double getY() { return y; }

	/**
	 * Sets the tile.
	 *
	 * @param tile the new tile
	 */
	public void setTile(Tile tile) {
		this.tile=tile;
		poly.getPoints().clear();
		poly.getPoints()
		.addAll(tile.poly != null ? tile.poly : new ArrayList<>());
		iv.setImage(tile.images[0]);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "TextureHolder [tile=" + tile.name + "]";
	}

	/**
	 * Update.
	 */
	public void update() {

		tile.update();

		if (tile.poly != null && tile.poly.hashCode() != hc) {
			poly.getPoints().clear();
			poly.getPoints().addAll(tile.poly);
			hc = tile.poly.hashCode();
		}
		if (tile.fps != fps) fps = tile.fps;

		iv.setImage(tile.images[tile.spriteNum]);

		if ("true".equals(System.getProperty("coll")) && poly.getPoints().size() > 0)
			poly.setVisible(true);
		else
			poly.setVisible(false);
	}

}
