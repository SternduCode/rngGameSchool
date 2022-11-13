package rngGame.entity;

import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.GamePanel;

public class MobRan extends Entity {

	public MobRan(JsonObject en, GamePanel gp, List<MobRan> entities,
			ContextMenu cm, ObjectProperty<MobRan> requestor) {
		super(en, 3*60, gp, "tmob", entities, cm, requestor);
		// TODO Auto-generated constructor stub
	}

	public MobRan(MobRan en, List< MobRan> entities, ContextMenu cm,
			ObjectProperty<MobRan> requestor) {
		super(en, entities, cm, requestor);
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
	
	public Double[] pathfinding (double x, double y, GamePanel sp) {
		long tileX;
		Double hi[] = null;
		return hi;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	//jennifer.graf-schabram@arbeitsagentur.de
	
	
	
	
}
