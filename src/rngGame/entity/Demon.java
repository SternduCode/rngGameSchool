package rngGame.entity;

import java.io.File;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import rngGame.main.GamePanel;

public class Demon extends NPC {

	protected String dir;
	private final Menu demon;
	private final MenuItem dirI;

	public Demon(Demon npc, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, npcs, cm, requestorN);
		dir = npc.dir;
		demon = new Menu("Demon");
		dirI = new MenuItem();
		dirI.setOnAction(this::handleContextMenu);
		demon.getItems().add(dirI);
	}

	public Demon(JsonObject npc, GamePanel gp, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, gp, "demons/" + ((StringValue) npc.get("dir")).getValue(),npcs, cm, requestorN);
		dir = ((StringValue) npc.get("dir")).getValue();
		demon = new Menu("Demon");
		dirI=new MenuItem();
		dirI.setOnAction(this::handleContextMenu);
		demon.getItems().add(dirI);
	}

	private void handleContextMenu(ActionEvent e) {
		TextInputDialog dialog = new TextInputDialog(dir);
		dialog.setHeaderText("Please move all the texture files required to it after creating it!");
		dialog.getDialogPane().getStyleClass().remove("text-input-dialog");
		dialog.setTitle("Subdirectory");
		dialog.setContentText("Please enter the new subdirectory of demons:");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			File f = new File("./res/demons/" + dir);
			if (!f.exists())
				f.mkdir();
			dir = result.get();
			directory = "demons/" + dir;
		}
	}

	@Override
	public List<Menu> getMenus() {
		List<Menu> li = super.getMenus();
		li.add(demon);
		dirI.setText("Dir: demons/" + dir);
		return li;
	}

	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = (JsonObject) super.toJsonValue();
		jo.put("dir", dir);
		return jo;
	}

}
