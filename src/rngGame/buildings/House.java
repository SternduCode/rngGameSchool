package rngGame.buildings;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.sterndu.json.*;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.main.GameObject;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class House.
 */
public class House extends Building {

	/** The map. */
	protected String map;

	/** The house. */
	private final Menu house;

	/** The map I. */
	private final MenuItem mapI;

	/** The entrance height. */
	private double openX, openY, openWidth, openHeight, entranceX, entranceY, entranceWidth, entranceHeight;

	/**
	 * Instantiates a new house.
	 *
	 * @param building the building
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public House(House building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);
		setCurrentKey("closed");
		house = new Menu("House");
		house.setStyle("-fx-font-size: 20;");
		mapI = new MenuItem();
		mapI.setStyle("-fx-font-size: 20;");
		mapI.setOnAction(this::handleContextMenu);
		map = building.map;
		house.getItems().add(mapI);

	}

	/**
	 * Instantiates a new house.
	 *
	 * @param building the building
	 * @param gp the gp
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public House(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		if (building.containsKey("map")) setMap(((StringValue) building.get("map")).getValue());
		setCurrentKey("closed");
		house = new Menu("House");
		house.setStyle("-fx-font-size: 20;");
		mapI = new MenuItem();
		mapI.setStyle("-fx-font-size: 20;");
		mapI.setOnAction(this::handleContextMenu);
		house.getItems().add(mapI);
		if (!getMiscBoxes().containsKey("open")) {
			Polygon entrance = new Polygon();
			openX = reqWidth / 2 - 65;
			openY = reqHeight / 2 + 50;
			openWidth = 130;
			openHeight = 100;
			entrance.getPoints().addAll(openX, openY, openX, openY + openHeight, openX + openWidth, openY + openHeight,
					openX + openWidth, openY);
			addMiscBox("open", entrance, (gpt, self) -> {
				self.setCurrentKey("open");
			});
		} else getMiscBoxHandler().put("open", (gpt, self) -> {
			self.setCurrentKey("open");
		});
		if (!getMiscBoxes().containsKey("entrance")) {
			Polygon entrance = new Polygon();
			entranceX = reqWidth / 2 - 25;
			entranceY = reqHeight / 2 + 50;
			entranceWidth = 50;
			entranceHeight = 60;
			entrance.getPoints().addAll(entranceX, entranceY, entranceX, entranceY + entranceHeight,
					entranceX + entranceWidth, entranceY + entranceHeight, entranceX + entranceWidth, entranceY);
			addMiscBox("entrance", entrance, (gpt, self) -> {
				if ( ((House) self).getMap() != null) gpt.getLgp().setMap("./res/maps/" + ((House) self).getMap());
			});
		} else getMiscBoxHandler().put("entrance", (gpt, self) -> {
			if ( ((House) self).getMap() != null) gpt.getLgp().setMap("./res/maps/" + ((House) self).getMap());
		});
	}

	/**
	 * Handle context menu.
	 *
	 * @param e the e
	 */
	private void handleContextMenu(ActionEvent e) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("./res/maps"));
		fc.setTitle("Select Map");
		fc.getExtensionFilters().add(new ExtensionFilter(
				"A file containing an JSON data", "*.json"));
		fc.setInitialFileName(map);
		File f = fc.showOpenDialog(getScene().getWindow());
		if (f != null && !f.toPath().startsWith("./res/maps")) {
			Path to = new File("./res/maps/"+f.getName()).toPath();
			if (Files.exists(to)) {
				Alert alert = new Alert(Alert.AlertType.NONE);
				alert.setTitle("The file already exists");
				alert.setContentText("Do you want to Override it?");
				ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
				ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
				ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
				alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
				Optional<ButtonType> res = alert.showAndWait();
				if (!res.isPresent()) return;
				if (res.get() == okButton) try {
					Files.copy(f.toPath(), to, StandardCopyOption.COPY_ATTRIBUTES,
							StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				else if (res.get() == cancelButton) return;

			} else try {
				Files.copy(f.toPath(), to, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println(f);
			System.out.println(to);
			map = f.getName();
		}
	}

	/**
	 * Sets the map.
	 *
	 * @param map the new map
	 */
	protected void setMap(String map) {
		this.map = map;
		if (slaves != null) for (GameObject slave:slaves) ((House) slave).setMap(map);
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public String getMap() { return map; }

	/**
	 * Gets the menus.
	 *
	 * @return the menus
	 */
	@Override
	public List<Menu> getMenus() {
		List<Menu> li = super.getMenus();
		li.add(house);
		mapI.setText("map: " + map);
		return li;
	}

	/**
	 * To json value.
	 *
	 * @return the json value
	 */
	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = (JsonObject) super.toJsonValue();
		if (map != null) jo.put("map", map);
		return jo;

	}

	/**
	 * Update.
	 *
	 * @param milis the milis
	 */
	@Override
	public void update(long milis) {

		setCurrentKey("closed");

		super.update(milis);

	}

}
