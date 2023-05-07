package rngGame.tile;

import java.io.*;
import java.util.*;

import com.sterndu.json.*;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.ui.PartialFillDialog;
import rngGame.visual.GamePanel;


// TODO: Auto-generated Javadoc
/**
 * The Class SelectTool.
 */
public class SelectTool extends Rectangle {

	/** The dragging. */
	private boolean dragging = false;

	/** The gp. */
	private final GamePanel gp;

	/** The y. */
	private int x, y;

	/** The partial fill. */
	private Menu select, fill, partialFill;

	/** The save as map. */
	private MenuItem copy, saveAsMap;

	/**
	 * Instantiates a new select tool.
	 *
	 * @param gp the gp
	 */
	public SelectTool(GamePanel gp) {
		this.gp = gp;
		init();
	}

	/**
	 * Handle context menu.
	 *
	 * @param e the e
	 */
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
				for (int i = 0; i < getHeight(); i += gp.getBlockSizeY()) for (int j = 0; j < getWidth(); j += gp.getBlockSizeX(), matrixIdx++)
					if (matrix[matrixIdx]) gp.getTileManager().getTileAt(x + j, y + i)
					.setTile(((MenuItemWTile) e.getSource()).getTile());
			}
		} else if ( ((MenuItem) e.getSource()).getParentMenu() == fill) for (int i = 0; i < getWidth(); i += gp.getBlockSizeX())
			for (int j = 0; j < getHeight(); j += gp.getBlockSizeY()) gp.getTileManager().getTileAt(x + i,
					y + j)
			.setTile(((MenuItemWTile) e.getSource()).getTile());
		else if (e.getSource()==saveAsMap) {
			List<List<TextureHolder>>	map	= gp.getTileManager().getPartOfMap(getLayoutX(), getLayoutY(), getWidth(), getHeight());
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File("./res/maps"));
			fc.getExtensionFilters().add(new ExtensionFilter("A file containing a Map", "*.json"));
			File f = fc.showSaveDialog(getScene().getWindow());
			if (f != null) {
				JsonObject file = new JsonObject();
				JsonArray npcs = new JsonArray();
				file.put("npcs", npcs);
				JsonArray buildings = new JsonArray();
				file.put("buildings", buildings);
				JsonObject mapO = new JsonObject();
				file.put("map", mapO);
				JsonArray startingPosition = new JsonArray(Arrays.asList(48, 48));
				mapO.put("startingPosition", startingPosition);
				mapO.put("dir", gp.getTileManager().getDir());
				if (gp.getTileManager().getBackgroundPath() != null)
					mapO.put("background", gp.getTileManager().getBackgroundPath());
				JsonArray textures = new JsonArray();
				mapO.put("textures", textures);
				JsonArray matrix = new JsonArray();
				mapO.put("matrix", matrix);
				for (List<TextureHolder> liTh: map) {
					String line = "";
					for (TextureHolder th: liTh) {
						if (!textures.contains(th.getTile().name))
							textures.add(th.getTile().name);
						line += textures.indexOf(th.getTile().name) + " ";
					}
					matrix.add(line);
				}
				try {
					String jsonOut = file.toJson();
					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
					bw.write(jsonOut);
					bw.flush();
					bw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource()==copy) {
			List<List<TextureHolder>> map = gp.getTileManager().getPartOfMap(getLayoutX(), getLayoutY(), getWidth(), getHeight());
			gp.getLgp().setClipboard(map);
			System.out.println(map);
		}
		//TODO add copy
	}

	/**
	 * Inits the.
	 */
	private void init() {
		setStroke(Color.color(1, 1, 1, .75));
		setFill(Color.TRANSPARENT);
		setStrokeWidth(2.5);
		setDisable(true);
		setVisible(false);
		copy = new MenuItem("Copy");
		copy.setStyle("-fx-font-size: 20;");
		copy.setOnAction(this::handleContextMenu);
		saveAsMap = new MenuItem("Save as Map");
		saveAsMap.setStyle("-fx-font-size: 20;");
		saveAsMap.setOnAction(this::handleContextMenu);
		select = new Menu("Select");
		select.setStyle("-fx-font-size: 20;");
		fill = new Menu("Fill");
		fill.setStyle("-fx-font-size: 20;");
		partialFill = new Menu("Partial Fill");
		partialFill.setStyle("-fx-font-size: 20;");
		select.getItems().addAll(fill, partialFill);
	}

	/**
	 * Do drag.
	 *
	 * @param me the me
	 */
	public void doDrag(MouseEvent me) {
		if (dragging) {
			Node node = gp.getTileManager().getTileAt(me.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
					me.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			if (node instanceof TextureHolder t) {
				setWidth(t.getLayoutX() + t.getWidth() - getLayoutX());
				setHeight(t.getLayoutY() + t.getHeight() - getLayoutY());
			}
			System.out.println(me.getPickResult().getIntersectedNode());
		}
	}

	/**
	 * Draw outlines.
	 *
	 * @param me the me
	 */
	public void drawOutlines(MouseEvent me) {
		setWidth(gp.getBlockSizeX());
		setHeight(gp.getBlockSizeY());
		double xPos = (me.getSceneX() + gp.getPlayer().getX() - gp.getPlayer().getScreenX()) / gp.getBlockSizeX(),
				yPos = (me.getSceneY() + gp.getPlayer().getY() - gp.getPlayer().getScreenY()) / gp.getBlockSizeY();
		if (xPos < 0) xPos -= 1;
		if (yPos < 0) yPos -= 1;
		x	= (int) xPos * gp.getBlockSizeX();
		y	= (int) yPos * gp.getBlockSizeY();
		setVisible(true);
	}

	/**
	 * End drag.
	 */
	public void endDrag() {
		dragging = false;
		if (getWidth() <= gp.getBlockSizeX() && getHeight() <= gp.getBlockSizeY()) return;// Make dragging not drag if only short drag
		ContextMenu cm = gp.getTileManager().getCM();
		cm.getItems().clear();
		cm.getItems().add(select);
		cm.getItems().add(copy);
		cm.getItems().add(saveAsMap);
		fill.getItems().clear();
		partialFill.getItems().clear();
		for (Tile t : gp.getTileManager().getTiles()) {
			MenuItemWTile menuitem = new MenuItemWTile(t.name,
					new ImageView(ImgUtil.resizeImage(t.images.get(0),
							(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 48, 48)),
					t);
			menuitem.setOnAction(this::handleContextMenu);
			fill.getItems().add(menuitem);
			menuitem = new MenuItemWTile(t.name,
					new ImageView(ImgUtil.resizeImage(t.images.get(0),
							(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 48, 48)),
					t);
			menuitem.setOnAction(this::handleContextMenu);
			partialFill.getItems().add(menuitem);
		}
		fill.getItems().sort((item1, item2) -> {
			if (item1 instanceof MenuItemWTile mi1) {
				if (item2 instanceof MenuItemWTile mi2) return mi1.getText().compareTo(mi2.getText());
				return -1;
			}
			return 1;
		});
		partialFill.getItems().sort((item1, item2) -> {
			if (item1 instanceof MenuItemWTile mi1) {
				if (item2 instanceof MenuItemWTile mi2) return mi1.getText().compareTo(mi2.getText());
				return -1;
			}
			return 1;
		});
		if (!gp.isBlockUserInputs())
			cm.show(this, getLayoutX() + getScene().getWindow().getX(),
					getLayoutY() + +getScene().getWindow().getY());

	}

	/**
	 * Checks if is dragging.
	 *
	 * @return true, if is dragging
	 */
	public boolean isDragging() { return dragging; }

	/**
	 * Start drag.
	 *
	 * @param me the me
	 */
	public void startDrag(MouseEvent me) {
		dragging = true;
		drawOutlines(me);
	}

	/**
	 * Undraw outlines.
	 */
	public void undrawOutlines() {
		if (!gp.getTileManager().getCM().isShowing()
				|| gp.getTileManager().getCM().getAnchorX() != getLayoutX() + getScene().getWindow().getX()
				|| gp.getTileManager().getCM().getAnchorY() != getLayoutY() + getScene().getWindow().getY())
			setVisible(false);
	}

	/**
	 * Update.
	 */
	public void update() {
		boolean moveCm = false;
		if (gp.getTileManager().getCM().isShowing()
				&& gp.getTileManager().getCM().getAnchorX() == getLayoutX() + getScene().getWindow().getX()
				&& gp.getTileManager().getCM().getAnchorY() == getLayoutY() + getScene().getWindow().getY())
			moveCm = true;
		double screenX = x - gp.getPlayer().getX() + gp.getPlayer().getScreenX();
		double screenY = y - gp.getPlayer().getY() + gp.getPlayer().getScreenY();
		setLayoutX(screenX);
		setLayoutY(screenY);
		if (moveCm) {
			gp.getTileManager().getCM().setAnchorX(screenX + getScene().getWindow().getX());
			gp.getTileManager().getCM().setAnchorY(screenY + getScene().getWindow().getY());
		}
	}
}
