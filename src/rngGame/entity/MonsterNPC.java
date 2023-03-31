package rngGame.entity;

import java.io.File;
import java.util.*;

import com.sterndu.json.*;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class MonsterNPC.
 */
public class MonsterNPC extends NPC {

	/** The dir. */
	protected String dir;

	/** The demon. */
	private final Menu demon;

	/** The dir I. */
	private final MenuItem dirI;

	/**
	 * Instantiates a new monster NPC.
	 *
	 * @param npc the npc
	 * @param gp the gp
	 * @param npcs the npcs
	 * @param cm the cm
	 * @param requestorN the requestor N
	 */
	public MonsterNPC(JsonObject npc, GamePanel gp, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, gp, "demons/" + ((StringValue) npc.get("dir")).getValue(),npcs, cm, requestorN);
		dir = ((StringValue) npc.get("dir")).getValue();
		demon = new Menu("Demon");
		dirI=new MenuItem();
		dirI.setOnAction(this::handleContextMenu);
		demon.getItems().add(dirI);
	}

	/**
	 * Instantiates a new monster NPC.
	 *
	 * @param npc the npc
	 * @param npcs the npcs
	 * @param cm the cm
	 * @param requestorN the requestor N
	 */
	public MonsterNPC(MonsterNPC npc, List<NPC> npcs, ContextMenu cm,
			ObjectProperty<NPC> requestorN) {
		super(npc, npcs, cm, requestorN);
		dir = npc.dir;
		demon = new Menu("Demon");
		dirI = new MenuItem();
		dirI.setOnAction(this::handleContextMenu);
		demon.getItems().add(dirI);
	}

	/**
	 * Handle context menu.
	 *
	 * @param e the e
	 */
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

	/**
	 * Gets the menus.
	 *
	 * @return the menus
	 */
	@Override
	public List<Menu> getMenus() {
		List<Menu> li = super.getMenus();
		li.add(demon);
		dirI.setText("Dir: demons/" + dir);
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
		jo.put("dir", dir);
		return jo;
	}

}
