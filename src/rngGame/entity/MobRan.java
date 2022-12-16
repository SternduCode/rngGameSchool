package rngGame.entity;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.GamePanel;
import rngGame.tile.TextureHolder;


public class MobRan extends Entity {

	private record PathElement(int x, int y, int distance) {}

	public MobRan(JsonObject en, GamePanel gp, List<MobRan> entities,
			ContextMenu cm, ObjectProperty<MobRan> requestor) {
		super(en, 3*60, gp, "npc", entities, cm, requestor);
		init();
	}
	
	public MobRan(MobRan en, List< MobRan> entities, ContextMenu cm,
			ObjectProperty<MobRan> requestor) {
		super(en, entities, cm, requestor);
		init();
	}
	public void init() {

        getMiscBoxHandler().put("fight", (gpt,self)->{

        });
        getMiscBoxHandler().put("visible", (gpt,self)->{
            Player p = gpt.getPlayer();
            Double[] pos = pathfinding(p.getX(), p.getY(), gpt);
        });
    }
	public Double[] pathfinding (double x, double y,GamePanel sp) {
        int tileX = (int) (x / sp.BgX);
        int tileY = (int) (y / sp.BgY);

        List<List<TextureHolder>> map = sp.getTileM().getMap();
        
        Stream<PathElement> stream = Stream.of(new PathElement(tileX, tileY, 0));
        for (int i = 0; i < 11; i++) stream=stream.mapMulti((PathElement pe, Consumer<PathElement> out) -> {
            out.accept(pe);
            if (map.size()>pe.y() && map.get(pe.y()).size() > pe.x()+1 &&map.get(pe.y()).get(pe.x()+1).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x() + 1, pe.y(), pe.distance() + 1));
            if (pe.x()!=0&&map.size()>pe.y() && map.get(pe.y()).size() > pe.x()-1 &&map.get(pe.y()).get(pe.x()-1).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x() - 1, pe.y(), pe.distance() + 1));
            if (pe.y()!=0&&map.size()>pe.y()-1 && map.get(pe.y()-1).size() > pe.x() &&map.get(pe.y()-1).get(pe.x()).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x(), pe.y() - 1, pe.distance() + 1));
            if (map.size()>pe.y()+1 && map.get(pe.y()+1).size() > pe.x() &&map.get(pe.y()+1).get(pe.x()).getPoly().getPoints().size()>0) out.accept(new PathElement(pe.x(), pe.y() + 1, pe.distance() + 1));
        }).sorted((a, b) -> {
            if (a.x() == b.x()) {
                if (a.y() == b.y()) return a.distance() < b.distance() ? -1 : 1;
                else return a.y() < b.y() ? -1 : 1;
            } else return a.x() < b.x() ? -1 : 1;
        }).distinct(); 
        
        //System.out.println(stream.collect(Collectors.toList()));

        List<PathElement> pel = stream.collect(Collectors.toList());
        System.out.println(pel);
        PathElement pe = pel.get(pel.size()-1);
        return new Double[] {(double) pe.x(), (double) pe.y()};
    }
	
	@Override
	public void update(long milis) {
		super.update(milis);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
