package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import rngGame.entity.NPC;

public class MenuItemWNPC extends MenuItem {

	private final NPC npc;

	public MenuItemWNPC(String text, Node graphic, NPC npc) {
		super(text, graphic);
		this.npc = npc;
	}

	public NPC getNPC() { return npc; }

}
