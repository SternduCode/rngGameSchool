package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import rngGame.entity.MobRan;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuItemWMOB.
 */
public class MenuItemWMOB extends MenuItem {
	
	/** The mob. */
	private final MobRan mob;

	/**
	 * Instantiates a new menu item WMOB.
	 *
	 * @param text the text
	 * @param graphic the graphic
	 * @param mob the mob
	 */
	public MenuItemWMOB(String text, Node graphic, MobRan mob) {
		super(text, graphic);
		this.mob = mob;
	}

	/**
	 * Gets the mob.
	 *
	 * @return the mob
	 */
	public MobRan getMob() { return mob; }

}
