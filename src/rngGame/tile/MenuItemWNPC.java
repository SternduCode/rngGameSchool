package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import rngGame.entity.NPC;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuItemWNPC.
 */
public class MenuItemWNPC extends MenuItem {

	/** The npc. */
	private final NPC npc;

	/**
	 * Instantiates a new menu item WNPC.
	 *
	 * @param text the text
	 * @param graphic the graphic
	 * @param npc the npc
	 */
	public MenuItemWNPC(String text, Node graphic, NPC npc) {
		super(text, graphic);
		this.npc = npc;
		setStyle("-fx-font-size: 20;");
	}

	/**
	 * Gets the npc.
	 *
	 * @return the npc
	 */
	public NPC getNPC() { return npc; }

}
