package rngGame.entity;

import java.util.List;

import com.sterndu.json.JsonObject;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.buildings.House;
import rngGame.main.SpielPanel;
import rngGame.tile.TileManager;

public class MobRan extends Entity {

	public MobRan(MobRan en, List< MobRan> entities, ContextMenu cm,
			ObjectProperty<MobRan> requestor) {
		super(en, entities, cm, requestor);
		// TODO Auto-generated constructor stub
	}

	public MobRan(JsonObject en, SpielPanel gp, List<MobRan> entities,
			ContextMenu cm, ObjectProperty<MobRan> requestor) {
		super(en, 3*60, gp, "tmob", entities, cm, requestor);
		// TODO Auto-generated constructor stub
	}

	public void init() {
		getMiscBoxHandler().put("fight", (gpt,self)->{
			
		});
		getMiscBoxHandler().put("visible", (gpt,self)->{
			Player p = gpt.getPlayer();
			pathfinding(p.getX(), p.getY(), gpt);
		});
	} 
	
	public Double[] pathfinding (double x, double y, SpielPanel sp) {
		long tileX =  (long) (x / sp.BgX);
		long tileY = (long) (y / sp.BgY);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//jennifer.graf-schabram@arbeitsagentur.de
	
	
	
	
}
