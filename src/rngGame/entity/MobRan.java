package rngGame.entity;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.*;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.GamePanel;
import rngGame.tile.TextureHolder;


public class MobRan extends NPC {

	private record PathElement(int x, int y, int distance) {}

	private final double[] diff = new double[2];

	private int step = 0;
	private final int steps = 150;

	public MobRan(JsonObject en, GamePanel gp, List<MobRan> entities,
			ContextMenu cm, ObjectProperty<MobRan> requestor) {
		super(en, gp, 3 * 60, entities, cm, requestor);
		init();
	}

	public MobRan(MobRan en, List< MobRan> entities, ContextMenu cm,
			ObjectProperty<MobRan> requestor) {
		super(en, entities, cm, requestor);
		init();
	}
	@Override
	public void init() {

		getMiscBoxHandler().put("fight", (gpt,self)->{

		});
		getMiscBoxHandler().put("visible", (gpt,self)->{
			if (step == 0) {
				Player p = gpt.getPlayer();
				Double[] pos = pathfinding(p.getX(), p.getY(), gpt);
				if (pos != null) {
					diff[0] = pos[0] - x;
					diff[1] = pos[1] - y;
				}
			}
		});
	}
	public Double[] pathfinding (double x, double y,GamePanel sp) {
		int tileX = (int) (x / sp.BgX);
		int tileY = (int) (y / sp.BgY);

		List<List<TextureHolder>> map = sp.getTileM().getMap();
		if (map.size() > 0) {

			Stream<PathElement> stream = Stream.of(new PathElement(tileX, tileY, 0));
			for (int i = 0; i < 11; i++) stream=stream.mapMulti((PathElement pe, Consumer<PathElement> out) -> {
				out.accept(pe);
				if (map.size() > pe.y() && map.get(pe.y()).size() > pe.x() + 1
						&& map.get(pe.y()).get(pe.x() + 1).getPoly().getPoints().size() == 0)
					out.accept(new PathElement(pe.x() + 1, pe.y(), pe.distance() + 1));
				if (pe.x() != 0 && map.size() > pe.y() && map.get(pe.y()).size() > pe.x() - 1
						&& map.get(pe.y()).get(pe.x() - 1).getPoly().getPoints().size() == 0)
					out.accept(new PathElement(pe.x() - 1, pe.y(), pe.distance() + 1));
				if (pe.y() != 0 && map.size() > pe.y() - 1 && map.get(pe.y() - 1).size() > pe.x()
						&& map.get(pe.y() - 1).get(pe.x()).getPoly().getPoints().size() == 0)
					out.accept(new PathElement(pe.x(), pe.y() - 1, pe.distance() + 1));
				if (map.size() > pe.y() + 1 && map.get(pe.y() + 1).size() > pe.x()
						&& map.get(pe.y() + 1).get(pe.x()).getPoly().getPoints().size() == 0)
					out.accept(new PathElement(pe.x(), pe.y() + 1, pe.distance() + 1));
			}).sorted((a, b) -> {
				if (a.x() == b.x()) {
					if (a.y() == b.y()) return a.distance() < b.distance() ? -1 : 1;
					else return a.y() < b.y() ? -1 : 1;
				} else return a.x() < b.x() ? -1 : 1;
			}).distinct();

			List<PathElement> pel = stream.filter(pe -> Math.abs(pe.x() - (int) (MobRan.this.x / sp.BgX)) == 1
					^ Math.abs(pe.y() - (int) (MobRan.this.y / sp.BgY)) == 1)
					.sorted((p1, p2) -> Integer.compare(p1.distance(), p2.distance())).collect(Collectors.toList());
			System.out.println(pel);
			if (pel.size() > 0) {
				PathElement pe = pel.get(0);
				return new Double[] {(double) pe.x() * sp.BgX, (double) pe.y() * sp.BgY};
			} else return null;
		}
		return null;
	}

	@Override
	public void update(long milis) {
		super.update(milis);

		if (diff[0] > 0 || diff[1] > 0)
			step++;
		x += (long) (diff[0] / steps);
		y += (long) (diff[1] / steps);
		if (step == steps) {
			step = 0;
			diff[0] = 0;
			diff[1] = 0;
		}
	}




















}
