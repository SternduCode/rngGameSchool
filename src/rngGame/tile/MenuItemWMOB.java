package rngGame.tile;

import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import rngGame.entity.MobRan;
import rngGame.entity.NPC;

public class MenuItemWMOB extends MenuItem {
	private final MobRan mob;

	public MenuItemWMOB(String text, Node graphic, MobRan mob) {
		super(text, graphic);
		this.mob = mob;
	}

	public MobRan getMob() { return mob; }

}
