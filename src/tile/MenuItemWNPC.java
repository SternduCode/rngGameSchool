package tile;

import entity.NPC;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class MenuItemWNPC extends MenuItem {

	private final NPC npc;

	public MenuItemWNPC(String text, Node graphic, NPC npc) {
		super(text, graphic);
		this.npc = npc;
	}

	public NPC getNPC() { return npc; }

}
