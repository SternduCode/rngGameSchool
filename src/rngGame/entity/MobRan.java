package rngGame.entity;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.tile.TextureHolder;
import rngGame.main.GamePanel;


public class MobRan extends Entity {

	public MobRan(JsonObject en, GamePanel gp, List<MobRan> entities,
			ContextMenu cm, ObjectProperty<MobRan> requestor) {
		super(en, 3*60, gp, "tmob", entities, cm, requestor);
	}

	public MobRan(MobRan en, List< MobRan> entities, ContextMenu cm,
			ObjectProperty<MobRan> requestor) {
		super(en, entities, cm, requestor);
	}
	
	public void init() {

        getMiscBoxHandler().put("fight", (gpt,self)->{

        });
        getMiscBoxHandler().put("visible", (gpt,self)->{
            Player p = gpt.getPlayer();
            pathfinding(p.getX(), p.getY(), gpt);
        });
    }
	private record PathElement(int x, int y, int distance) {};
	public Double[] pathfinding (double x, double y,GamePanel sp) {
        int tileX = (int) (x / sp.BgX);
        int tileY = (int) (y / sp.BgY);

        List<List<TextureHolder>> map = sp.getTileM().getMap();
        
        Stream<PathElement> stream = Stream.of(new PathElement(tileX, tileY, 0));
        for (int i = 0; i < 11; i++) stream=stream.mapMulti((PathElement pe, Consumer<PathElement> out) -> {
            out.accept(pe);
            if (map.get(pe.y()).get(pe.x()+1).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x() + 1, pe.y(), pe.distance() + 1));
            if (map.get(pe.y()).get(pe.x()-1).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x() - 1, pe.y(), pe.distance() + 1));
            if (map.get(pe.y()-1).get(pe.x()).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x(), pe.y() - 1, pe.distance() + 1));
            if (map.get(pe.y()+1).get(pe.x()).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x(), pe.y() + 1, pe.distance() + 1));
        }).sorted((a, b) -> {
            if (a.x() == b.x()) {
                if (a.y() == b.y()) return a.distance() < b.distance() ? -1 : 1;
                else return a.y() < b.y() ? -1 : 1;
            } else return a.x() < b.x() ? -1 : 1;
        }).distinct(); 

        return null;
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
