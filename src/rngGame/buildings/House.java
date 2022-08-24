package rngGame.buildings;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.entity.Player;
import rngGame.main.SpielPanel;

public class House extends Building {

	protected String map;
	private final Menu house;
	private final MenuItem mapI;

	public House(House building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);
		map = building.map;
		currentKey = "closed";
		house = new Menu("Demon");
		mapI = new MenuItem();
		mapI.setOnAction(this::handleContextMenu);
		house.getItems().add(mapI);
	}

	public House(JsonObject building, SpielPanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		if (building.containsKey("map")) map = ((StringValue) building.get("map")).getValue();
		currentKey = "closed";
		house = new Menu("House");
		mapI = new MenuItem();
		mapI.setOnAction(this::handleContextMenu);
		house.getItems().add(mapI);
	}

	private void handleContextMenu(ActionEvent e) {
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File("./res/maps"));
		fc.setTitle("Select Map");
		fc.getExtensionFilters().add(new ExtensionFilter(
				"A file containing an JSON data", "*.json"));
		fc.setInitialFileName(map);
		File f = fc.showOpenDialog(getScene().getWindow());
		if (!f.toPath().startsWith("./res/maps")) {
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
				if (res.isPresent()) {
					if (res.get() == okButton) try {
						Files.copy(f.toPath(), to, StandardCopyOption.COPY_ATTRIBUTES,
								StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					else if (res.get() == cancelButton) return;
				} else return;

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

	@Override
	public List<Menu> getMenus() {
		List<Menu> li = super.getMenus();
		li.add(house);
		mapI.setText("map: " + map);
		return li;
	}

	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = (JsonObject) super.toJsonValue();
		if (map != null) jo.put("map", map);
		return jo;

	}

	@Override
	public void update() {
		super.update();

		Player p = gp.getPlayer();

		if (x + reqWidth / 2 - p.getX() < 105 && x + reqWidth / 2 - p.getX() > -45 &&
				y + reqHeight / 2 - p.getY() < -10 && y + reqHeight / 2 - p.getY() > -135)
			currentKey = "open";
		else currentKey = "closed";
		if (x + reqWidth / 2 - p.getX() < 105 && x + reqWidth / 2 - p.getX() > -45 &&
				y + reqHeight / 2 - p.getY() < -10 && y + reqHeight / 2 - p.getY() > -65)
			if (map != null) gp.setMap("./res/maps/" + map);
	}

}
