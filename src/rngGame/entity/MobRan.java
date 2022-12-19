package rngGame.entity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import com.sterndu.json.JsonObject;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import rngGame.main.GamePanel;
import rngGame.tile.TextureHolder;


// TODO: Auto-generated Javadoc
/**
 * The Class MobRan.
 */
public class MobRan extends NPC {

	/**
	 * The  PathElement.
	 */
	private record PathElement(int x, int y, int distance) {}

	/** The diff. */
	private final double[] diff = new double[2];

	/** The step. */
	private int step = 0;

	/** The steps. */
	private final int steps = 150;

	/**
	 * Instantiates a new mob ran.
	 *
	 * @param en the en
	 * @param gp the gp
	 * @param entities the entities
	 * @param cm the cm
	 * @param requestor the requestor
	 */
	public MobRan(JsonObject en, GamePanel gp, List<MobRan> entities,
			ContextMenu cm, ObjectProperty<MobRan> requestor) {
		super(en, gp, 3 * 60, entities, cm, requestor);
		init();
	}

	/**
	 * Instantiates a new mob ran.
	 *
	 * @param en the en
	 * @param entities the entities
	 * @param cm the cm
	 * @param requestor the requestor
	 */
	public MobRan(MobRan en, List< MobRan> entities, ContextMenu cm,
			ObjectProperty<MobRan> requestor) {
		super(en, entities, cm, requestor);
		init();
	}

	/**
	 * Inits the.
	 */
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

	/**
	 * Pathfinding.
	 *
	 * @param x the x
	 * @param y the y
	 * @param sp the sp
	 * @return the double[]
	 */
	public Double[] pathfinding (double x, double y,GamePanel sp) {
		int	tileX	= (int) Math.round(x / sp.BgX);
		int	tileY	= (int) Math.round(y / sp.BgY);

		List<List<TextureHolder>> map = sp.getTileM().getMap();
		if (map.size() > 0) {

			Stream<PathElement> stream = Stream.of(new PathElement(tileX, tileY, 0));
			for (int i = 0; i < 11; i++) stream=stream.mapMulti((PathElement pe, Consumer<PathElement> out) -> {
				out.accept(pe);
				if (map.size() > pe.y() && map.get(pe.y()).size() > pe.x() + 1
						&& map.get(pe.y()).get(pe.x() + 1).getPoly().getPoints().size() == 0) // rechts
					out.accept(new PathElement(pe.x() + 1, pe.y(), pe.distance() + 1));
				
				if (pe.x() != 0 && map.size() > pe.y() && map.get(pe.y()).size() > pe.x() - 1
						&& map.get(pe.y()).get(pe.x() - 1).getPoly().getPoints().size() == 0) // links
					out.accept(new PathElement(pe.x() - 1, pe.y(), pe.distance() + 1));
				
				if (pe.y() != 0 && map.size() > pe.y() - 1 && map.get(pe.y() - 1).size() > pe.x()
						&& map.get(pe.y() - 1).get(pe.x()).getPoly().getPoints().size() == 0) 	//hoch
					out.accept(new PathElement(pe.x(), pe.y() - 1, pe.distance() + 1));
				
				if (map.size() > pe.y() + 1 && map.get(pe.y() + 1).size() > pe.x()
						&& map.get(pe.y() + 1).get(pe.x()).getPoly().getPoints().size() == 0)	//runter
					out.accept(new PathElement(pe.x(), pe.y() + 1, pe.distance() + 1));
			}).sorted((a, b) -> {
				if (a.x() != b.x()) return a.x() < b.x() ? -1 : 1;
				if (a.y() == b.y()) return a.distance() < b.distance() ? -1 : 1;
				return a.y() < b.y() ? -1 : 1;
			}).distinct();

			List<PathElement> pels = stream.collect(Collectors.toList());

			System.out.println(pels);

			List<PathElement> pel = pels.parallelStream().filter(pe -> (Math.abs(pe.x() - (int) Math.round(MobRan.this.x / sp.BgX)) == 1
					|| Math.abs(pe.y() - (int) Math.round(MobRan.this.y / sp.BgY)) == 1)
					&& ( Math.abs(pe.x() - (int) Math.round(MobRan.this.x / sp.BgX)) != 1 || Math.abs(pe.y() - (int) Math.round(MobRan.this.y / sp.BgY)) != 1))
					.sorted(Comparator.comparing(PathElement::distance)).collect(Collectors.toList());
			System.out.println(pel);
			if (pel.size() > 0) {
				PathElement pe = pel.get(1);
				return new Double[] {(double) pe.x() * sp.BgX, (double) pe.y() * sp.BgY};
			}
		}
		return null;
	}

	/**
	 * Update.
	 *
	 * @param milis the milis
	 */
	@Override
	public void update(long milis) {
		super.update(milis);

		if (diff[0] > 0 || diff[1] > 0)
			step++;
		x += (long) (diff[0] / steps);
		y += (long) (diff[1] / steps);
		System.out.println(step);
		if (step == steps) {
			System.out.println("Finish: " + step);
			step = 0;
			diff[0] = 0;
			diff[1] = 0;
		}
	}




















}
