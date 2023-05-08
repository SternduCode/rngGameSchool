package rngGame.entity;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.*;

import com.sterndu.json.*;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.*;
import javafx.scene.shape.Circle;
import rngGame.stats.*;
import rngGame.tile.*;
import rngGame.ui.HealthBar;
import rngGame.visual.GamePanel;


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

	/** The voidi. */
	private static int fire = 0, water = 0, plant = 0, light = 0, shadow = 0, voidi = 0, dimensionMaster = 0;

	/** The demons. */
	private static Map<String, Integer> demons = new HashMap<>();

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
	 * Macht dir ein Mob vallah.
	 *
	 * @param gamepanel the gamepanel
	 * @return the demon
	 */
	public static Demon MobGen(GamePanel gamepanel) {
		Random gen = new Random();
		String pnG, mobName;
		Element wahl;

		int r = gen.nextInt(101)+1;
		if(r <= 30) wahl = Element.Fire;
		else if(r <= 60 && r >= 31) wahl = Element.Water;
		else if(r <= 90 && r >= 61) wahl = Element.Plant;
		else if(r <= 95 && r >= 91) wahl = Element.Light;
		else if(r <= 100 && r >= 96) wahl = Element.Shadow;
		else wahl = Element.Void;

		String[] mobs = {

				"May", "Booky", "Mello", "Nobarius", "Slyzer", "Howl", "Cultist", "Vardum", "Endor", "Seraph", "Malag", "Spye", "Legion"

		};

		int mr = gen.nextInt(mobs.length);
		mobName = mobs[mr];
		//		mobName = "May";
		//		wahl = Element.Void;
		//The making of "NaberiusDev" and "CultistKing" very hard to get #Nebl
		if ("Nobarius".equals(mobName)) {
			r = gen.nextInt(10)+1;
			if (r == 5) mobName = "Naberius";
		}else if ("CultistKing".equals(mobName)||"Cultist".equals(mobName)) {
			mobName = "Cultist";
			if (wahl==Element.Void) {
				r = gen.nextInt(200)+1;
				//			r = 13;
				if(r == 13) { mobName = "CultistKing"; wahl = Element.DimensionMaster; }

				else mobName = "Cultist";
			}
		}


		if (new File("./res/demons/"+wahl+"/"+mobName+".png").exists())
			pnG = "./res/demons/"+wahl+"/"+mobName+".png";
		else
			pnG = "./res/demons/"+wahl+"/"+mobName+".gif";

		switch (wahl) {
			case Fire -> fire++;
			case Water -> water++;
			case Plant -> plant++;
			case Light -> light++;
			case Shadow -> shadow++;
			case Void -> voidi++;
			case DimensionMaster -> dimensionMaster++;
			default -> throw new IllegalArgumentException("Unexpected value: " + wahl);
		}

		if (demons.containsKey(mobName)) demons.put(mobName, demons.get(mobName) + 1);
		else demons.put(mobName, 1);

		double sum = (fire + water + plant + light + shadow + voidi + dimensionMaster) / 100.0;
		System.out.printf("Fire %.2f%% Water %.2f%% Plant %.2f%% Light %.2f%% Shadow %.2f%% Void %.2f%% DimensionMaster %.2f%% Items %d\n",
				fire / sum, water / sum, plant / sum, light / sum, shadow / sum, voidi / sum, dimensionMaster / sum, (long) (sum * 100));

		sum = demons.values().stream().mapToInt(i -> i).sum() / 100.0;

		double _sum = sum;

		Object[] data = demons.values().stream().map(i -> i / _sum).toList().toArray(new Object[demons.size() + 1]);

		data[data.length - 1] = (long) (sum * 100);

		System.out.printf(demons.keySet().stream().collect(Collectors.joining(" %.2f%% ")) + " %.2f%% Items %d\n",
				data);

		try {
			Path p2	= new File(pnG).toPath();
			Image img = new Image(new FileInputStream(p2.toFile()));

			JsonArray reqSize = new JsonArray();
			JsonArray position = new JsonArray();
			JsonObject joB = new JsonObject();
			reqSize.add(new IntegerValue(64));
			reqSize.add(new IntegerValue(64));
			joB.put("requestedSize", reqSize);
			JsonObject textures = new JsonObject();
			if (new File("./res/demons/"+wahl+"/"+mobName+".png").exists())
				textures.put("default", new StringValue(mobName + ".png"));
			else
				textures.put("default", new StringValue(mobName + ".gif"));
			joB.put("textures", textures);
			JsonObject buildingData = new JsonObject();
			joB.put("buildingData", buildingData);
			joB.put("type", new StringValue("Building"));
			joB.put("dir", new StringValue(wahl.toString()));
			position.add(new DoubleValue(1730));
			position.add(new DoubleValue(1113));

			joB.put("position", position);
			JsonArray originalSize = new JsonArray();
			originalSize.add(new DoubleValue(img.getHeight()));
			originalSize.add(new DoubleValue(img.getHeight()));
			joB.put("originalSize", originalSize);

			MonsterNPC mnpc = new MonsterNPC(joB, gamepanel, gamepanel.getTileManager().getNPCSFromMap(), gamepanel.getTileManager().getCM(),
					gamepanel.getTileManager().getRequestorN());
			gamepanel.getViewGroups().get(mnpc.getLayer()).getChildren().remove(mnpc);
			mnpc.setFixToScreen(true);
			gamepanel.getLgp().getNpcs().add(mnpc);

			return new Demon(wahl, mobName, mnpc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
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
			getMiscBoxes().put("fight", new Circle(getReqWidth() / 2, getReqHeight() / 2, 32));
		if (!getMiscBoxes().containsKey("visible"))
			getMiscBoxes().put("visible", new Circle(getReqWidth() / 2, getReqHeight() / 2, 528));
		super.init();
		getMiscBoxHandler().put("fight", (gpt,self)->{
			Demon eigenMob;
			Demon demonMob = MobGen(gamepanel);
			HealthBar h, hh;
			System.out.println(demonMob);
			if (demonMob != null) {

				gpt.getLgp().getMobRans().remove(MobRan.this);
				gpt.getViewGroups().get(layer).getChildren().remove(MobRan.this);
				Image bbg = ImgUtil.getScaledImage(gpt, "./res/fight/Fight.png", gpt.getGameWidth(), gpt.getGameHeight());
				ImageView bbgv = new ImageView(bbg);
				demonMob.getDemon().setReqWidth(256);
				demonMob.getDemon().setReqHeight(256);
				demonMob.getDemon().setLayoutX(gpt.getWidth()/1.5);
				demonMob.getDemon().setLayoutY(gpt.getHeight()/6.4);
				demonMob.getDemon().reloadTextures();
				Demon[] demonArray = gpt.getGamemenu().getInventory().getDemons();
				eigenMob = demonArray[0];
				eigenMob.getDemon().setReqWidth(256);
				eigenMob.getDemon().setReqHeight(256);
				eigenMob.getDemon().setLayoutX(gpt.getWidth()/13);
				eigenMob.getDemon().setLayoutY(gpt.getHeight()/6.4);
				eigenMob.getDemon().flipTextures();
				eigenMob.getDemon().reloadTextures();
				h = new HealthBar(gpt);
				hh = new HealthBar(gpt);
				h.setLayoutX(gpt.getWidth()/2);
				hh.setLayoutX(gpt.getWidth()/4);
				h.update();
				hh.update();

				gpt.getChildren().addAll(bbgv, demonMob.getDemon(), eigenMob.getDemon(), h, hh);


			}
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

	//Durchlaufen lassen bis void LOL OMG 360
	//	for(int i = 1; wahl!="void"; i++) {
	//		int r = gen.nextInt(101)+1;
	//		if(r <= 30) {
	//			wahl = "fire";
	//		}else if(r <= 60 && r >= 31) {
	//			wahl = "water";
	//		}else if(r <= 90 && r >= 61) {
	//			wahl = "plant";
	//		}else if(r <= 95 && r >= 91) {
	//			wahl = "light";
	//			plantc++;
	//		}else if(r <= 100 && r >= 96) {
	//			wahl = "shadow";
	//			plantc++;
	//		}else {
	//			wahl = "void";
	//		}
	//		System.out.println(wahl + " " + i + " " + plantc);
	//		}

	/**
	 * Pathfinding.
	 *
	 * @param sp the sp
	 * @return the double[]
	 */
	public Double[] pathfinding(GamePanel sp) {
		Player	p	= sp.getPlayer();
		double	x	= p.getX() + p.getColliBoxX(), y = p.getY() + p.getColliBoxY() / 2;

		int	tileX	= (int) Math.round(x / sp.getBlockSizeX());
		int	tileY	= (int) Math.round(y / sp.getBlockSizeY());

		List<List<TextureHolder>> map = sp.getTileManager().getMap();
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

			//			int	minX	= pels.parallelStream().mapToInt(PathElement::x).min().orElse(0);
			//			int	minY	= pels.parallelStream().mapToInt(PathElement::y).min().orElse(0);
			//
			//			int	maxX	= pels.parallelStream().mapToInt(PathElement::x).max().orElse(0);
			//			int	maxY	= pels.parallelStream().mapToInt(PathElement::y).max().orElse(0);


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

			int mobX = (int) Math.round(MobRan.this.x / sp.getBlockSizeX()),
					mobY = (int) Math.round(MobRan.this.y / sp.getBlockSizeY());

			// System.out.println(mobX + " " + mobY + " "
			// + mobX * sp.BgX + " " + mobY * sp.BgY);

			List<PathElement> pel = pels.parallelStream().filter(pe -> isShit(pe, mobX, mobY))
					.sorted(Comparator.comparing(PathElement::distance)).distinct().collect(Collectors.toList());
			// System.out.println(pel);
			if (pel.size() > 0) {
				PathElement pe = pel.get(0);
				return new Double[] {
						(double) pe.x() * sp.getBlockSizeX(), (double) pe.y() * sp.getBlockSizeY()
				};
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