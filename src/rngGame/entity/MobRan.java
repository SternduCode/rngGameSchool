package rngGame.entity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import com.sterndu.json.JsonObject;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.shape.Circle;
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
	private record PathElement(int x, int y, int distance) {

		/**
		 * Equals.
		 *
		 * @param obj the obj
		 *
		 * @return true, if successful
		 */
		@Override
		public boolean equals(Object obj) {
			if (! (obj instanceof PathElement pe)) return false;
			return pe.x() == x() && pe.y() == y();

		}

	}

	/** The diff. */
	private final double[] diff = new double[2];

	/** The step. */
	private int step = 0;

	/** The steps. */
	private final int steps = 40;

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
	 * Checks if is shit.
	 *
	 * @param pe the pe
	 * @param mobX the mob X
	 * @param mobY the mob Y
	 * @return true, if is shit
	 */
	private boolean isShit(PathElement pe, int mobX, int mobY) {
		int diffX = Math.abs(pe.x() - mobX);
		int diffY = Math.abs(pe.y() - mobY);
		//		System.out.println(pe);
		//		System.out.println("diffX: "+diffX + " diffY: "+ diffY);
		return (diffX == 1 || diffY == 1) && diffX != diffY && diffX <= 1 && diffY <= 1;
	}

	/**
	 * Inits the.
	 */
	@Override
	public void init() {
		if (!getMiscBoxes().containsKey("fight"))
			getMiscBoxes().put("fight", new Circle(32, 32, 64));
		if (!getMiscBoxes().containsKey("visible"))
			getMiscBoxes().put("visible", new Circle(32, 32, 528));
		super.init();
		getMiscBoxHandler().put("fight", (gpt,self)->{

		});
		getMiscBoxHandler().put("visible", (gpt,self)->{
			if (step == 0) {
				Double[] pos = pathfinding(gpt);
				if (pos != null) {
					diff[0]	= pos[0] - x;
					diff[1]	= pos[1] - y;
					//					x	= pos[0];
					//					y	= pos[1];
				}
			}
		});
	}

	/**
	 * Pathfinding.
	 *
	 * @param sp the sp
	 * @return the double[]
	 */
	public Double[] pathfinding(GamePanel sp) {
		Player	p	= sp.getPlayer();
		double	x	= p.getX() + p.getColliBoxX(), y = p.getY() + p.getColliBoxY() / 2;

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
				if (a.x() == b.x() && a.y() == b.y()) return a.distance() < b.distance() ? -1 : 1;
				if (a.x() != b.x()) return a.x() < b.x() ? -1 : 1;
				return a.y() < b.y() ? -1 : 1;
			}).distinct();

			List<PathElement> pels = stream.collect(Collectors.toList());

			if (pels.parallelStream().filter(pe -> pe.distance() == 0)
					.anyMatch(pe -> map.get(pe.y()).get(pe.x()).getPoly().getPoints().size() != 0)) pels.removeIf(pe -> pe.distance() == 0);

			int	minX	= pels.parallelStream().mapToInt(PathElement::x).min().orElse(0);
			int	minY	= pels.parallelStream().mapToInt(PathElement::y).min().orElse(0);

			int	maxX	= pels.parallelStream().mapToInt(PathElement::x).max().orElse(0);
			int	maxY	= pels.parallelStream().mapToInt(PathElement::y).max().orElse(0);


//			for (int yU = minY; yU <= maxY; yU++) {
//				for (int xU = minX; xU <= maxX; xU++) {
//					int xU_ = xU, yU_ = yU;
//					System.out.print(switch (pels.parallelStream().filter(pe -> pe.x() == xU_ && pe.y() == yU_).findFirst()
//							.orElse(new PathElement(0, 0, -1)).distance()) {
//								case -1 -> "  ";
//								case 0 -> "P ";
//								default -> pels.parallelStream().filter(pe -> pe.x() == xU_ && pe.y() == yU_).findFirst().orElse(new PathElement(0, 0, -1))
//								.distance() + " ";
//					});
//				}
//				System.out.println();
//			}

			// System.out.println(pels);

			int mobX = (int) Math.round(MobRan.this.x / sp.BgX),
					mobY = (int) Math.round(MobRan.this.y / sp.BgY);

			// System.out.println(mobX + " " + mobY + " "
			// + mobX * sp.BgX + " " + mobY * sp.BgY);

			List<PathElement> pel = pels.parallelStream().filter(pe -> isShit(pe, mobX, mobY))
					.sorted(Comparator.comparing(PathElement::distance)).distinct().collect(Collectors.toList());
			// System.out.println(pel);
			if (pel.size() > 0) {
				PathElement pe = pel.get(0);
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
		super.update(milis);// TODO make speed like with player

		if (diff[0] > 0 || diff[1] > 0)
			step++;
		x	+= diff[0] / steps;
		y	+= diff[1] / steps;
		// System.out.println(step);
		if (step <= steps) {
			//			System.out.println(Arrays.toString(diff));
			//			System.out.println("Finish: " + step);
			step	= 0;
			diff[0]	= 0;
			diff[1]	= 0;
		}
	}
















}
