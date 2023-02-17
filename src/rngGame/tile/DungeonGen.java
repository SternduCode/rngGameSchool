package rngGame.tile;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.sterndu.json.*;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.*;
import rngGame.buildings.*;
import rngGame.main.GamePanel;


// TODO: Auto-generated Javadoc
/**
 * Class DungeonGen for generating Dungeon maps.
 */
public class DungeonGen {

	// TODO: Auto-generated Javadoc
	/**
	 * The Enum Direction.
	 */
	private enum Direction {

		/** Connector direction up. */
		UP,
		/** Connector direction down. */
		DOWN,
		/** Connector direction left. */
		LEFT,
		/** Connector direction right. */
		RIGHT;
	}

	/**
	 * Map Size.
	 */
	private enum Size {

		/** Map size big. */
		BIG,
		/** Map size middle. */
		MIDDLE,
		/** Map size small. */
		SMALL;
	}

	/** The GamePanel. */
	private final GamePanel gp;

	/** The mainMap. */
	private final JsonObject mainMap;

	/** The maps. */
	private final List<Entry<JsonObject, Size>> maps;

	/** The map tiles. */
	private final List<Tile> mainMapTiles, mapsTiles[];

	/** The map tile numbers. */
	private final List<List<Integer>> mainMapTileNum, mapsTileNum[];

	/** The connectors, connections and replacements. */
	private final List<JsonObject> connectors, connections, replacements;

	/** The map connector locations and directions. */
	private final List<Entry<Point2D, Direction>> mainMapConnectors, mapsConnectors[];

	/** The mainMap void number. */
	private int mainMapVoidNum;

	/** The map void numbers. */
	private final int mapsVoidNum[];

	/** The additional data. */
	private final JsonObject additionalData;

	/** The map connector tiles. */
	private final Map<Integer, Map.Entry<Tile, JsonObject>> mainMapConnectorTiles, mapsConnectorTiles[];

	/** The difficulty. */
	private final Difficulty difficulty;

	// TODO: Auto-generated Javadoc
	/**
	 * Instantiates a new dungeon generation class.
	 *
	 * @param gp           the GamePanel
	 * @param voidImg      the void image path
	 * @param mainmap      the mainMap
	 * @param maps         the maps
	 * @param connectors   the connectors
	 * @param connections  the connections
	 * @param replacements the replacements
	 * @param additionalData the additional data
	 * @param difficulty   the difficulty
	 */
	@SuppressWarnings("unchecked")
	public DungeonGen(GamePanel gp, String voidImg, JsonObject mainmap, JsonArray maps, List<JsonObject> connectors, List<JsonObject> connections,
			List<JsonObject> replacements, JsonObject additionalData, Difficulty difficulty) {
		this.gp	= gp;
		mainMap	= mainmap;

		this.additionalData = additionalData;

		this.difficulty = difficulty;

		this.maps = new ArrayList<>();

		for (Object folder : maps) if (folder instanceof JsonObject map && map.get("folder") instanceof StringValue folderName
				&& map.get("size") instanceof StringValue sizeStr) {
			System.out.println(folderName.getValue() + " " + sizeStr.getValue());
			Size size = Size.valueOf(sizeStr.getValue());
			File f	= new File("./res/maps/" + folderName.getValue());
			for (File m : f.listFiles((FilenameFilter) (dir, name) -> name.endsWith(".json"))) try {
				this.maps.add(Map.entry((JsonObject) JsonParser.parse(m), size));
			} catch (JsonParseException e) {
				e.printStackTrace();
			}

		}
		System.out.println(difficulty);

		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));// load mainmap matrix
		mainMapTileNum				= gp.getTileM().mapTileNum;	// save loaded mainmap matrix
		gp.getTileM().mapTileNum	= new ArrayList<>();

		String dir = ((StringValue) ((JsonObject) mainmap.get("map")).get("dir")).getValue();

		mainMapTiles = new ArrayList<>();

		for (Object texture : (JsonArray) ((JsonObject) mainmap.get("map")).get("textures")) try {// load mainmap tiles
			Tile t = new Tile( ((StringValue) texture).getValue(), new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
					gp);
			mainMapTiles.add(t);
			String[] sp = ((StringValue) texture).getValue().split("[.]");
			if (new File("./res/collisions/" + dir + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
			+ ".collisionbox").exists())
				try {
					RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/" + dir + "/"
							+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
							+ ".collisionbox"), "rws");
					raf.seek(0l);
					int length = raf.readInt();
					t.poly = new ArrayList<>();
					boolean s = false;
					for (int i = 0; i < length; i++)
						t.poly.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (List<Integer> xCol : mainMapTileNum) for (Integer v : xCol) if (mainMapTiles.get(v).name.equals(voidImg)) mainMapVoidNum = v; // find
		// void in
		// mainmap
		// tiles

		mapsTileNum	= new List[this.maps.size()];
		mapsTiles	= new List[this.maps.size()];
		mapsVoidNum	= new int[this.maps.size()];

		for (int i = 0; i < this.maps.size(); i++) {// TODO check for validity
			gp.getTileM().loadMap((JsonArray) ((JsonObject) this.maps.get(i).getKey().get("map")).get("matrix"));
			mapsTileNum[i]				= gp.getTileM().mapTileNum;
			gp.getTileM().mapTileNum	= new ArrayList<>();
			dir							= ((StringValue) ((JsonObject) this.maps.get(i).getKey().get("map")).get("dir")).getValue();
			mapsTiles[i]				= new ArrayList<>();
			for (Object texture : (JsonArray) ((JsonObject) this.maps.get(i).getKey().get("map")).get("textures")) try {
				Tile t = new Tile( ((StringValue) texture).getValue(), new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
						gp);
				mapsTiles[i].add(t);
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				if (new File("./res/collisions/" + dir + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
				+ ".collisionbox").exists())
					try {
						RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/" + dir + "/"
								+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox"), "rws");
						raf.seek(0l);
						int length = raf.readInt();
						t.poly = new ArrayList<>();
						boolean s = false;
						for (int ij = 0; ij < length; ij++)
							t.poly.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			for (List<Integer> xCol : mapsTileNum[i]) for (Integer v : xCol) if (mapsTiles[i].get(v).name.equals(voidImg)) mapsVoidNum[i] = v;
		}
		gp.getTileM().mapTileNum = new ArrayList<>();
		gp.getTileM().getTiles().clear();
		this.connectors		= connectors;
		this.connections	= connections;
		this.replacements	= replacements;

		mainMapConnectorTiles	= new HashMap<>();
		mapsConnectorTiles		= new HashMap[mapsTiles.length];
		mainMapConnectors		= new ArrayList<>();
		mapsConnectors			= new List[this.maps.size()];
		for (int i = 0; i < this.maps.size(); i++) mapsConnectors[i] = new ArrayList<>();
		System.out.println(mainMapVoidNum);

	}

	/**
	 * Decorate.
	 */
	@SuppressWarnings("unchecked")
	public void decorate() {
		// TODO fancy bridges (after 2)
		// TODO add objects (Faggel 25% on corners and non border tiles have 1/7 to have book on them)

		JsonArray	cornerPieces	= (JsonArray) additionalData.get("cornerPieces");
		List<Integer>	corners			= cornerPieces.parallelStream().map(o -> ((StringValue) o).getValue())
				.map(str -> gp.getTileM().getTiles().parallelStream().filter(t -> t.name.equals(str)).findAny().orElse(null)).filter(v -> v != null)
				.map(t -> gp.getTileM().getTiles().indexOf(t))
				.collect(Collectors.toList());
		System.out.println(corners);

		JsonArray	cornerObject	= (JsonArray) additionalData.get("cornerObjects");
		Entry<String, Entry<JsonArray, JsonArray>>[]	cornerObjects	= new Entry[cornerObject.size()];
		for (int i = 0; i < cornerObject.size(); i++) {
			String		cornerTexture			= ((StringValue) ((JsonObject) cornerObject.get(i)).get("texture")).getValue();
			JsonArray	cornerRequestedSize	= (JsonArray) ((JsonObject) cornerObject.get(i)).get("requestedSize");
			JsonArray	cornerPos			= (JsonArray) ((JsonObject) cornerObject.get(i)).get("position");
			cornerObjects[i] = Map.entry(cornerTexture, Map.entry(cornerRequestedSize, cornerPos));
		}


		JsonArray	randomPieces	= (JsonArray) additionalData.get("randomPieces");
		List<Integer>	randoms			= randomPieces.parallelStream().map(o -> ((StringValue) o).getValue())
				.map(str -> gp.getTileM().getTiles().parallelStream().filter(t -> t.name.equals(str)).findAny().orElse(null)).filter(v -> v != null)
				.map(t -> gp.getTileM().getTiles().indexOf(t))
				.collect(Collectors.toList());
		System.out.println(randoms);

		JsonArray	randomObject	= (JsonArray) additionalData.get("randomObjects");
		Entry<String, Entry<JsonArray, JsonArray>>[]	randomObjects	= new Entry[randomObject.size()];
		for (int i = 0; i < randomObject.size(); i++) {
			String		cornerTexture			= ((StringValue) ((JsonObject) randomObject.get(i)).get("texture")).getValue();
			JsonArray	cornerRequestedSize	= (JsonArray) ((JsonObject) randomObject.get(i)).get("requestedSize");
			JsonArray	cornerPos			= (JsonArray) ((JsonObject) randomObject.get(i)).get("position");
			randomObjects[i] = Map.entry(cornerTexture, Map.entry(cornerRequestedSize, cornerPos));
		}

		// TODO tiles and books

		Random r = new Random();

		List<Entry<Integer, Integer>> availChestSpots = new ArrayList<>();

		for (int i = 0; i < gp.getTileM().mapTileNum.size(); i++) for (int j = 0; j < gp.getTileM().mapTileNum.get(i).size(); j++) {
			Integer th = gp.getTileM().mapTileNum.get(i).get(j);

			if (corners.contains(th)) {
				// TODO spawn faggel

				if (.25 > r.nextDouble()) {
					Entry<String, Entry<JsonArray, JsonArray>> object = cornerObjects[r.nextInt(cornerObjects.length)];
					try {
						Path						p2		= new File("./res/building/" + object.getKey()).toPath();
						Image						img		= new Image(new FileInputStream(p2.toFile()));

						JsonObject joB = new JsonObject();
						joB.put("requestedSize", object.getValue().getKey());
						JsonObject textures = new JsonObject();
						textures.put("default", new StringValue(object.getKey()));
						joB.put("textures", textures);
						JsonObject buildingData = new JsonObject();
						joB.put("buildingData", buildingData);
						joB.put("type", new StringValue("Building"));
						JsonArray position = new JsonArray();
						position.add(new DoubleValue(
								j * (gp.BgX / gp.getScalingFactorX()) + ((NumberValue) object.getValue().getValue().get(0)).getValue().intValue()));
						position.add(new DoubleValue(
								i * (gp.BgY / gp.getScalingFactorY()) + ((NumberValue) object.getValue().getValue().get(1)).getValue().intValue()));
						joB.put("position", position);
						JsonArray originalSize = new JsonArray();
						originalSize.add(new DoubleValue(img.getHeight()));
						originalSize.add(new DoubleValue(img.getHeight()));
						joB.put("originalSize", originalSize);

						gp.getBuildings().add(
								new Building(joB, gp, gp.getTileM().getBuildingsFromMap(), gp.getTileM().getCM(), gp.getTileM().getRequesterB()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}


			} else if (randoms.contains(th)) if ( 1.0 / 8.0 > r.nextDouble()) {

				Entry<String, Entry<JsonArray, JsonArray>> object = randomObjects[r.nextInt(randomObjects.length)];
				try {
					Path	p2	= new File("./res/building/" + object.getKey()).toPath();
					Image	img	= new Image(new FileInputStream(p2.toFile()));

					JsonObject joB = new JsonObject();
					joB.put("requestedSize", object.getValue().getKey());
					JsonObject textures = new JsonObject();
					textures.put("default", new StringValue(object.getKey()));
					joB.put("textures", textures);
					JsonObject buildingData = new JsonObject();
					joB.put("buildingData", buildingData);
					joB.put("type", new StringValue("Building"));
					JsonArray position = new JsonArray();
					position.add(new DoubleValue(
							j * (gp.BgX / gp.getScalingFactorX()) + ((NumberValue) object.getValue().getValue().get(0)).getValue().intValue()));
					position.add(new DoubleValue(
							i * (gp.BgY / gp.getScalingFactorY()) + ((NumberValue) object.getValue().getValue().get(1)).getValue().intValue()));
					joB.put("position", position);
					JsonArray originalSize = new JsonArray();
					JsonArray	background		= new JsonArray();
					background.add(new BoolValue(true));
					joB.put("background", background);
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joB.put("originalSize", originalSize);

					gp.getBuildings().add(
							new Building(joB, gp, gp.getTileM().getBuildingsFromMap(), gp.getTileM().getCM(), gp.getTileM().getRequesterB()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else availChestSpots.add(Map.entry(j, i));
		}
		switch (difficulty) {
			case EASY:
				for (int i = 0; i < 3; i++) {
					Entry<Integer, Integer>	en	= availChestSpots.get(r.nextInt(availChestSpots.size()));
					try {
						Path	p2	= new File("./res/building/NormalChestClosed.png").toPath();
						Image	img	= new Image(new FileInputStream(p2.toFile()));

						JsonObject	joB		= new JsonObject();
						JsonArray	reqSize	= new JsonArray();
						reqSize.add(new DoubleValue(32));
						reqSize.add(new DoubleValue(32));
						joB.put("requestedSize", reqSize);
						JsonObject textures = new JsonObject();
						textures.put("default", new StringValue("NormalChestClosed.png"));
						joB.put("textures", textures);
						JsonObject buildingData = new JsonObject();
						joB.put("buildingData", buildingData);
						joB.put("type", new StringValue("Building"));
						JsonArray position = new JsonArray();
						position.add(new DoubleValue(
								en.getKey() * (gp.BgX / gp.getScalingFactorX())));
						position.add(new DoubleValue(
								en.getValue() * (gp.BgY / gp.getScalingFactorY())));
						joB.put("position", position);
						JsonArray	originalSize	= new JsonArray();
						JsonArray	background		= new JsonArray();
						background.add(new BoolValue(false));
						joB.put("background", background);
						originalSize.add(new DoubleValue(img.getWidth()));
						originalSize.add(new DoubleValue(img.getHeight()));
						joB.put("originalSize", originalSize);
						TreasureChest tc = new TreasureChest(joB, gp, gp.getTileM().getBuildingsFromMap(), gp.getTileM().getCM(),
								gp.getTileM().getRequesterB());
						gp.getBuildings().add(tc);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					availChestSpots.remove(en);
				}
				break;
			case MIDDLE:
				for (int i = 0; i < 4; i++) {
					Entry<Integer, Integer>	en	= availChestSpots.get(r.nextInt(availChestSpots.size()));
					try {
						Path	p2	= new File("./res/building/NormalChestClosed.png").toPath();
						Image	img	= new Image(new FileInputStream(p2.toFile()));

						JsonObject	joB		= new JsonObject();
						JsonArray	reqSize	= new JsonArray();
						reqSize.add(new DoubleValue(32));
						reqSize.add(new DoubleValue(32));
						joB.put("requestedSize", reqSize);
						JsonObject textures = new JsonObject();
						textures.put("default", new StringValue("NormalChestClosed.png"));
						joB.put("textures", textures);
						JsonObject buildingData = new JsonObject();
						joB.put("buildingData", buildingData);
						joB.put("type", new StringValue("Building"));
						JsonArray position = new JsonArray();
						position.add(new DoubleValue(
								en.getKey() * gp.BgX));
						position.add(new DoubleValue(
								en.getValue() * gp.BgY));
						joB.put("position", position);
						JsonArray	originalSize	= new JsonArray();
						JsonArray	background		= new JsonArray();
						background.add(new BoolValue(false));
						joB.put("background", background);
						originalSize.add(new DoubleValue(img.getWidth()));
						originalSize.add(new DoubleValue(img.getHeight()));
						joB.put("originalSize", originalSize);
						TreasureChest tc = new TreasureChest(joB, gp, gp.getTileM().getBuildingsFromMap(), gp.getTileM().getCM(),
								gp.getTileM().getRequesterB());
						gp.getBuildings().add(tc);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					availChestSpots.remove(en);
				}
				break;
			case HARD:
				for (int i = 0; i < 5; i++) {
					Entry<Integer, Integer>	en	= availChestSpots.get(r.nextInt(availChestSpots.size()));
					try {
						Path	p2	= new File("./res/building/NormalChestClosed.png").toPath();
						Image	img	= new Image(new FileInputStream(p2.toFile()));

						JsonObject	joB		= new JsonObject();
						JsonArray	reqSize	= new JsonArray();
						reqSize.add(new DoubleValue(32));
						reqSize.add(new DoubleValue(32));
						joB.put("requestedSize", reqSize);
						JsonObject textures = new JsonObject();
						textures.put("default", new StringValue("NormalChestClosed.png"));
						joB.put("textures", textures);
						JsonObject buildingData = new JsonObject();
						joB.put("buildingData", buildingData);
						joB.put("type", new StringValue("Building"));
						JsonArray position = new JsonArray();
						position.add(new DoubleValue(
								en.getKey() * gp.BgX));
						position.add(new DoubleValue(
								en.getValue() * gp.BgY));
						joB.put("position", position);
						JsonArray	originalSize	= new JsonArray();
						JsonArray	background		= new JsonArray();
						background.add(new BoolValue(false));
						joB.put("background", background);
						originalSize.add(new DoubleValue(img.getWidth()));
						originalSize.add(new DoubleValue(img.getHeight()));
						joB.put("originalSize", originalSize);
						TreasureChest tc = new TreasureChest(joB, gp, gp.getTileM().getBuildingsFromMap(), gp.getTileM().getCM(),
								gp.getTileM().getRequesterB());
						gp.getBuildings().add(tc);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					availChestSpots.remove(en);
				}
				break;
		}
	}

	/**
	 * Find free connectors.
	 */
	public void findFreeConnectors() {
		for (int i = 0; i < mainMapTiles.size(); i++) {
			int c_i = i;
			connectors.forEach(jobj -> {
				if ( ((StringValue) jobj.get("name")).getValue().equals(mainMapTiles.get(c_i).name))
					mainMapConnectorTiles.put(c_i, Map.entry(mainMapTiles.get(c_i), jobj));
			});
		}
		for (int i = 0; i < mapsConnectorTiles.length; i++) {
			int c_i = i;
			mapsConnectorTiles[i] = new HashMap<>();
			List<Tile> mapTiles = mapsTiles[i];
			for (int j = 0; j < mapTiles.size(); j++) {
				int c_j = j;
				connectors.forEach(jobj -> {
					if ( ((StringValue) jobj.get("name")).getValue().equals(mapTiles.get(c_j).name))
						mapsConnectorTiles[c_i].put(c_j, Map.entry(mapTiles.get(c_j), jobj));
				});
			}
		}

		for (int i = 0; i < mainMapTileNum.size(); i++)
			for (int j = 0; j < mainMapTileNum.get(i).size(); j++) if (mainMapConnectorTiles.containsKey(mainMapTileNum.get(i).get(j))) {
				String direction = ((StringValue) mainMapConnectorTiles.get(mainMapTileNum.get(i).get(j)).getValue().get("direction")).getValue();
				mainMapConnectors.add(Map.entry(new Point2D(i, j), Direction.valueOf(direction.toUpperCase())));
			}

		for (int s = 0; s < mapsTileNum.length; s++) {
			List<List<Integer>> mapTileNum = mapsTileNum[s];
			for (int i = 0; i < mapTileNum.size(); i++)
				for (int j = 0; j < mapTileNum.get(i).size(); j++) if (mapsConnectorTiles[s].containsKey(mapTileNum.get(i).get(j))) {
					String direction = ((StringValue) mapsConnectorTiles[s].get(mapTileNum.get(i).get(j)).getValue().get("direction")).getValue();
					mapsConnectors[s].add(Map.entry(new Point2D(i, j), Direction.valueOf(direction.toUpperCase())));
				}
		}

		Iterator<Entry<Point2D, Direction>> it = mainMapConnectors.iterator();
		while (it.hasNext()) {
			Entry<Point2D, Direction> p = it.next();
			try {
				switch (p.getValue()) {
					case UP:
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getKey().getX() - 1).get((int) p.getKey().getY())) it.remove();
						break;
					case DOWN:
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getKey().getX() + 1).get((int) p.getKey().getY())) it.remove();
						break;
					case LEFT:
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getKey().getX()).get((int) p.getKey().getY() - 1)) it.remove();
						break;
					case RIGHT:
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getKey().getX()).get((int) p.getKey().getY() + 1)) it.remove();
						break;
				}
			} catch (Exception e) {}
		}

		for (int i = 0; i < mapsConnectors.length; i++) {
			it = mapsConnectors[i].iterator();
			while (it.hasNext()) {
				Entry<Point2D, Direction> p = it.next();
				try {
					switch (p.getValue()) {
						case UP:
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getKey().getX() - 1).get((int) p.getKey().getY())) it.remove();
							break;
						case DOWN:
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getKey().getX() + 1).get((int) p.getKey().getY())) it.remove();
							break;
						case LEFT:
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getKey().getX()).get((int) p.getKey().getY() - 1)) it.remove();
							break;
						case RIGHT:
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getKey().getX()).get((int) p.getKey().getY() + 1)) it.remove();
							break;
					}
				} catch (Exception e) {}
			}
		}

		System.out.println(mainMapConnectors.size() + " " + mainMapConnectors);
		for (List<Entry<Point2D, Direction>> connectors : mapsConnectors) System.out.println(connectors.size() + " " + connectors);

	}

	/**
	 * Stitch maps.
	 */
	public void stitchMaps() {
		List<Entry<Point2D, Integer>> upmaps = new ArrayList<>(),
				downmaps = new ArrayList<>(),
				leftmaps = new ArrayList<>(),
				rightmaps = new ArrayList<>();

		for (int m = 0; m < mapsTileNum.length; m++) for (Entry<Point2D, Direction> conn : mapsConnectors[m]) switch (conn.getValue()) {
			case UP -> { if (!upmaps.contains(Map.entry(conn.getKey(), m))) upmaps.add(Map.entry(conn.getKey(), m)); }
			case DOWN -> { if (!downmaps.contains(Map.entry(conn.getKey(), m))) downmaps.add(Map.entry(conn.getKey(), m)); }
			case LEFT -> { if (!leftmaps.contains(Map.entry(conn.getKey(), m))) leftmaps.add(Map.entry(conn.getKey(), m)); }
			case RIGHT -> { if (!rightmaps.contains(Map.entry(conn.getKey(), m))) rightmaps.add(Map.entry(conn.getKey(), m)); }
		}

		Random r = new Random();

		List<Entry<Entry<Point2D, Integer>, Direction>> avail = new ArrayList<>();

		Map<Integer, Point2D> mapPositions = new HashMap<>();

		int x = r.nextInt(75), y = r.nextInt(75);

		mapPositions.put(-1, new Point2D(x, y));

		avail.addAll(mainMapConnectors.parallelStream().map(en -> Map.entry(Map.entry(en.getKey(), -1), en.getValue())).collect(Collectors.toList()));

		int bigMaps = 0, middleMaps = 0, smallMaps = 0;

		Shape map = new Rectangle(x, y, mainMapTileNum.parallelStream().mapToInt(List::size).max().orElse(0), mainMapTileNum.size());

		List<Entry<Point2D, Point2D>> bridges = new ArrayList<>();

		while (true) {
			int idx = r.nextInt(avail.size());

			Entry<Entry<Point2D, Integer>, Direction> conn = avail.get(idx);
			switch (conn.getValue()) {
				case UP -> {
					if (downmaps.size() > 0) {
						int						idx2	= r.nextInt(downmaps.size());
						Entry<Point2D, Integer>	conn2	= downmaps.get(idx2);
						if (maps.get(conn2.getValue()).getValue() == Size.SMALL && smallMaps < difficulty.getSmallMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.MIDDLE && middleMaps < difficulty.getMiddleMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.BIG && bigMaps < difficulty.getBigMaps()) {
							System.out.println(mapPositions.get(conn.getKey().getValue()).getX() + " " + conn.getKey().getKey().getY() + " "
									+ conn2.getKey().getY());
							int			x2	= (int) (mapPositions.get(conn.getKey().getValue()).getX() + conn.getKey().getKey().getY()
									- conn2.getKey().getY());
							int			y2	= (int) (mapPositions.get(conn.getKey().getValue()).getY() - mapsTileNum[conn2.getValue()].size()
									- r.nextInt(5) - 1);
							Rectangle	mr	= new Rectangle(x2, y2,
									mapsTileNum[conn2.getValue()].parallelStream().mapToInt(List::size).max().orElse(0),
									mapsTileNum[conn2.getValue()].size());
							if (Shape.intersect(mr, map).getBoundsInLocal().isEmpty()) {
								map = Shape.union(mr, map);
								if (maps.get(conn2.getValue()).getValue() == Size.SMALL) smallMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.MIDDLE) middleMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.BIG) bigMaps++;
								mapPositions.put(conn2.getValue(), new Point2D(x2, y2));
								avail.addAll(mapsConnectors[conn2.getValue()].parallelStream()
										.map(en -> Map.entry(Map.entry(en.getKey(), conn2.getValue()), en.getValue())).collect(Collectors.toList()));
								avail.remove(Map.entry(conn2, Direction.DOWN));
								avail.remove(conn);

								downmaps.removeIf(en -> en.getValue() == conn2.getValue());
								upmaps.removeIf(en -> en.getValue() == conn2.getValue());
								leftmaps.removeIf(en -> en.getValue() == conn2.getValue());
								rightmaps.removeIf(en -> en.getValue() == conn2.getValue());

								bridges.add(Map.entry(
										new Point2D(conn.getKey().getKey().getY(), conn.getKey().getKey().getX())
										.add(mapPositions.get(conn.getKey().getValue())),
										new Point2D(conn2.getKey().getY(), conn2.getKey().getX()).add(x2, y2)));
							}
						}
					}
				}
				case DOWN -> {
					if (upmaps.size() > 0) {
						int						idx2	= r.nextInt(upmaps.size());
						Entry<Point2D, Integer>	conn2	= upmaps.get(idx2);
						if (maps.get(conn2.getValue()).getValue() == Size.SMALL && smallMaps < difficulty.getSmallMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.MIDDLE && middleMaps < difficulty.getMiddleMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.BIG && bigMaps < difficulty.getBigMaps()) {
							System.out.println(mapPositions.get(conn.getKey().getValue()).getX() + " " + conn.getKey().getKey().getX() + " "
									+ conn2.getKey().getX());
							int			x2	= (int) (mapPositions.get(conn.getKey().getValue()).getX() + conn.getKey().getKey().getY()
									- conn2.getKey().getY());
							int			y2	= (int) (mapPositions.get(conn.getKey().getValue()).getY()
									+ (conn.getKey().getValue() == -1 ? mainMapTileNum : mapsTileNum[conn.getKey().getValue()]).size() + r.nextInt(5)
									+ 1);
							Rectangle	mr	= new Rectangle(x2, y2,
									mapsTileNum[conn2.getValue()].parallelStream().mapToInt(List::size).max().orElse(0),
									mapsTileNum[conn2.getValue()].size());
							if (Shape.intersect(mr, map).getBoundsInLocal().isEmpty()) {
								map = Shape.union(mr, map);
								if (maps.get(conn2.getValue()).getValue() == Size.SMALL) smallMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.MIDDLE) middleMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.BIG) bigMaps++;
								mapPositions.put(conn2.getValue(), new Point2D(x2, y2));
								avail.addAll(mapsConnectors[conn2.getValue()].parallelStream()
										.map(en -> Map.entry(Map.entry(en.getKey(), conn2.getValue()), en.getValue())).collect(Collectors.toList()));
								avail.remove(Map.entry(conn2, Direction.UP));
								avail.remove(conn);

								downmaps.removeIf(en -> en.getValue() == conn2.getValue());
								upmaps.removeIf(en -> en.getValue() == conn2.getValue());
								leftmaps.removeIf(en -> en.getValue() == conn2.getValue());
								rightmaps.removeIf(en -> en.getValue() == conn2.getValue());

								bridges.add(Map.entry(
										new Point2D(conn.getKey().getKey().getY(), conn.getKey().getKey().getX())
										.add(mapPositions.get(conn.getKey().getValue())),
										new Point2D(conn2.getKey().getY(), conn2.getKey().getX()).add(x2, y2)));
							}
						}
					}
				}
				case LEFT -> {
					if (rightmaps.size() > 0) {
						int						idx2	= r.nextInt(rightmaps.size());
						Entry<Point2D, Integer>	conn2	= rightmaps.get(idx2);
						if (maps.get(conn2.getValue()).getValue() == Size.SMALL && smallMaps < difficulty.getSmallMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.MIDDLE && middleMaps < difficulty.getMiddleMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.BIG && bigMaps < difficulty.getBigMaps()) {
							int			x2	= (int) (mapPositions.get(conn.getKey().getValue()).getX()
									- mapsTileNum[conn2.getValue()].parallelStream().mapToInt(List::size).max().orElse(0) - r.nextInt(5) - 1);
							int			y2	= (int) (mapPositions.get(conn.getKey().getValue()).getY() + conn.getKey().getKey().getX()
									- conn2.getKey().getX());
							Rectangle	mr	= new Rectangle(x2, y2,
									mapsTileNum[conn2.getValue()].parallelStream().mapToInt(List::size).max().orElse(0),
									mapsTileNum[conn2.getValue()].size());
							if (Shape.intersect(mr, map).getBoundsInLocal().isEmpty()) {
								map = Shape.union(mr, map);
								if (maps.get(conn2.getValue()).getValue() == Size.SMALL) smallMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.MIDDLE) middleMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.BIG) bigMaps++;
								mapPositions.put(conn2.getValue(), new Point2D(x2, y2));
								avail.addAll(mapsConnectors[conn2.getValue()].parallelStream()
										.map(en -> Map.entry(Map.entry(en.getKey(), conn2.getValue()), en.getValue())).collect(Collectors.toList()));
								avail.remove(Map.entry(conn2, Direction.RIGHT));
								avail.remove(conn);

								downmaps.removeIf(en -> en.getValue() == conn2.getValue());
								upmaps.removeIf(en -> en.getValue() == conn2.getValue());
								leftmaps.removeIf(en -> en.getValue() == conn2.getValue());
								rightmaps.removeIf(en -> en.getValue() == conn2.getValue());

								bridges.add(Map.entry(
										new Point2D(conn.getKey().getKey().getY(), conn.getKey().getKey().getX())
										.add(mapPositions.get(conn.getKey().getValue())),
										new Point2D(conn2.getKey().getY(), conn2.getKey().getX()).add(x2, y2)));
							}
						}
					}
				}
				case RIGHT -> {
					if (leftmaps.size() > 0) {
						int						idx2	= r.nextInt(leftmaps.size());
						Entry<Point2D, Integer>	conn2	= leftmaps.get(idx2);
						if (maps.get(conn2.getValue()).getValue() == Size.SMALL && smallMaps < difficulty.getSmallMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.MIDDLE && middleMaps < difficulty.getMiddleMaps()
								|| maps.get(conn2.getValue()).getValue() == Size.BIG && bigMaps < difficulty.getBigMaps()) {
							int			x2	= (int) (mapPositions.get(conn.getKey().getValue()).getX()
									+ (conn.getKey().getValue() == -1 ? mainMapTileNum : mapsTileNum[conn.getKey().getValue()]).parallelStream()
									.mapToInt(List::size).max().orElse(0)
									+ r.nextInt(5) + 1);
							int			y2	= (int) (mapPositions.get(conn.getKey().getValue()).getY() + conn.getKey().getKey().getX()
									- conn2.getKey().getX());
							Rectangle	mr	= new Rectangle(x2, y2,
									mapsTileNum[conn2.getValue()].parallelStream().mapToInt(List::size).max().orElse(0),
									mapsTileNum[conn2.getValue()].size());
							if (Shape.intersect(mr, map).getBoundsInLocal().isEmpty()) {
								map = Shape.union(mr, map);
								if (maps.get(conn2.getValue()).getValue() == Size.SMALL) smallMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.MIDDLE) middleMaps++;
								else if (maps.get(conn2.getValue()).getValue() == Size.BIG) bigMaps++;
								mapPositions.put(conn2.getValue(), new Point2D(x2, y2));
								avail.addAll(mapsConnectors[conn2.getValue()].parallelStream()
										.map(en -> Map.entry(Map.entry(en.getKey(), conn2.getValue()), en.getValue())).collect(Collectors.toList()));
								avail.remove(Map.entry(conn2, Direction.LEFT));
								avail.remove(conn);

								downmaps.removeIf(en -> en.getValue() == conn2.getValue());
								upmaps.removeIf(en -> en.getValue() == conn2.getValue());
								leftmaps.removeIf(en -> en.getValue() == conn2.getValue());
								rightmaps.removeIf(en -> en.getValue() == conn2.getValue());

								bridges.add(Map.entry(
										new Point2D(conn.getKey().getKey().getY(), conn.getKey().getKey().getX())
										.add(mapPositions.get(conn.getKey().getValue())),
										new Point2D(conn2.getKey().getY(), conn2.getKey().getX()).add(x2, y2)));
							}

						}
					}
				}
			}
			if (smallMaps >= difficulty.getSmallMaps() && middleMaps >= difficulty.getMiddleMaps() && bigMaps >= difficulty.getBigMaps()) break;
		}
		int xOffset = (int) (mapPositions.entrySet().parallelStream().map(Entry::getValue).mapToDouble(Point2D::getX).filter(lx -> lx <= 0.0).min()
				.orElseGet(() -> 1.0) - 1.0),
				yOffset = (int) (mapPositions.entrySet().parallelStream().map(Entry::getValue).mapToDouble(Point2D::getY).filter(ly -> ly <= 0.0)
						.min().orElse(1.0) - 1.0);
		mapPositions.entrySet().parallelStream().forEach(en -> { en.setValue(en.getValue().subtract(xOffset, yOffset)); });
		System.out.println(avail);
		System.out.println(mapPositions);

		// Calculate Max Size
		int	width	= (int) mapPositions.entrySet().parallelStream()
				.mapToDouble(en -> en.getValue().getX()
						+ (en.getKey() == -1 ? mainMapTileNum : mapsTileNum[en.getKey()]).parallelStream().mapToInt(List::size).max().orElse(0))
				.max().orElse(0);
		int	height	= (int) mapPositions.entrySet().parallelStream()
				.mapToDouble(en -> en.getValue().getY() + (en.getKey() == -1 ? mainMapTileNum : mapsTileNum[en.getKey()]).size()).max().orElse(0);

		for (int i = 0; i < height + 1; i++) for (int j = 0; j < width + 1; j++) {
			int _j = j, _i = i;

			List<Entry<Integer, Integer>> tile = mapPositions.entrySet().parallelStream()
					.filter(en -> en.getValue().getX() <= _j && en.getValue().getY() <= _i)
					.map(en -> ( (en.getKey() != -1 ? mapsTileNum[en.getKey()] : mainMapTileNum).size() > _i - en.getValue().getY()
							&& (en.getKey() != -1 ? mapsTileNum[en.getKey()] : mainMapTileNum).get((int) (_i - en.getValue().getY())).size() > _j
							- en.getValue().getX()
							? Map.entry(en.getKey(),
									(en.getKey() != -1 ? mapsTileNum[en.getKey()] : mainMapTileNum)
									.get((int) (_i - en.getValue().getY())).get((int) (_j - en.getValue().getX())))
									: null))
					.filter(Objects::nonNull).collect(Collectors.toList());
			if (gp.getTileM().mapTileNum.size() <= i) gp.getTileM().mapTileNum.add(new ArrayList<>());
			if (tile.size() > 0) try {
				Tile t = gp.getTileM().getTiles().parallelStream()
						.filter(ti -> ti.name.equals(
								(tile.get(0).getKey() != -1 ? mapsTiles[tile.get(0).getKey()] : mainMapTiles).get(tile.get(0).getValue()).name))
						.findFirst().get();
				gp.getTileM().mapTileNum.get(i).add(gp.getTileM().getTiles().indexOf(t));
			} catch (NoSuchElementException e) {
				gp.getTileM().getTiles()
				.add( (tile.get(0).getKey() != -1 ? mapsTiles[tile.get(0).getKey()] : mainMapTiles).get(tile.get(0).getValue()));
				gp.getTileM().mapTileNum.get(i).add(gp.getTileM().getTiles().size() - 1);
			}
			else try {
				Tile t = gp.getTileM().getTiles().parallelStream().filter(ti -> ti.name.equals(mainMapTiles.get(mainMapVoidNum).name)).findFirst()
						.get();
				gp.getTileM().mapTileNum.get(i).add(gp.getTileM().getTiles().indexOf(t));
			} catch (NoSuchElementException e) {
				gp.getTileM().getTiles().add(mainMapTiles.get(mainMapVoidNum));
				gp.getTileM().mapTileNum.get(i).add(gp.getTileM().getTiles().size() - 1);
			}
		}

		bridges.forEach(bridge -> {

			if (Math.abs(bridge.getKey().getX() - bridge.getValue().getX()) > 0)
				for (int i = 1; i < Math.abs(bridge.getKey().getX() - bridge.getValue().getX()); i++)
					gp.getTileM().mapTileNum.get((int) bridge.getKey().getY() - yOffset)
					.set((int) (Math.min(bridge.getKey().getX(), bridge.getValue().getX()) + i) - xOffset, gp.getTileM().getTiles().size());
			else for (int i = 1; i < Math.abs(bridge.getKey().getY() - bridge.getValue().getY()); i++)
				gp.getTileM().mapTileNum.get((int) (Math.min(bridge.getKey().getY(), bridge.getValue().getY()) + i) - yOffset)
				.set((int) bridge.getKey().getX() - xOffset, gp.getTileM().getTiles().size() + 1);
		});

		try {
			gp.getTileM().getTiles().add(new Tile( ((StringValue) ((JsonArray) connections.get(0).get("name")).get(0)).getValue(),
					new FileInputStream("./res/insel/" + ((StringValue) ((JsonArray) connections.get(0).get("name")).get(0)).getValue()), gp));
			gp.getTileM().getTiles().add(new Tile( ((StringValue) ((JsonArray) connections.get(1).get("name")).get(0)).getValue(),
					new FileInputStream("./res/insel/" + ((StringValue) ((JsonArray) connections.get(1).get("name")).get(0)).getValue()), gp));
			String[] sp = ((StringValue) ((JsonArray) connections.get(0).get("name")).get(0)).getValue().split("[.]");
			if (new File("./res/collisions/insel/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
			+ ".collisionbox").exists())
				try {
					RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/insel/"
							+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
							+ ".collisionbox"), "rws");
					raf.seek(0l);
					int length = raf.readInt();
					gp.getTileM().getTiles().get(gp.getTileM().getTiles().size() - 2).poly = new ArrayList<>();
					boolean s = false;
					for (int ij = 0; ij < length; ij++)
						gp.getTileM().getTiles().get(gp.getTileM().getTiles().size() - 2).poly
						.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			sp = ((StringValue) ((JsonArray) connections.get(0).get("name")).get(0)).getValue().split("[.]");
			if (new File("./res/collisions/insel/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
			+ ".collisionbox").exists())
				try {
					RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/insel/"
							+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
							+ ".collisionbox"), "rws");
					raf.seek(0l);
					int length = raf.readInt();
					gp.getTileM().getTiles().get(gp.getTileM().getTiles().size() - 1).poly = new ArrayList<>();
					boolean s = false;
					for (int ij = 0; ij < length; ij++)
						gp.getTileM().getTiles().get(gp.getTileM().getTiles().size() - 1).poly
						.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		gp.getTileM().setStartingPosition(new double[] {
				(x - xOffset + .5) * 48, (y - yOffset + .5) * 48
		});

		Map<Direction, List<Tile>>	replacements	= new HashMap<>();
		List<Tile>					plus			= new ArrayList<>();

		for (JsonObject replacement : this.replacements) {
			Direction	dir		= Direction.valueOf( ((StringValue) replacement.get("direction")).getValue().toUpperCase());
			List<Tile>	tiles	= new ArrayList<>();
			for (Object tileName : (JsonArray) replacement.get("name")) try {
				Tile tile = new Tile( ((StringValue) tileName).getValue(), new FileInputStream("./res/insel/" + ((StringValue) tileName).getValue()),
						gp);
				tiles.add(tile);
				gp.getTileM().getTiles().add(tile);
				String[] sp = ((StringValue) tileName).getValue().split("[.]");
				if (new File("./res/collisions/insel/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
				+ ".collisionbox").exists())
					try {
						RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/insel/"
								+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox"), "rws");
						raf.seek(0l);
						int length = raf.readInt();
						tile.poly = new ArrayList<>();
						boolean s = false;
						for (int ij = 0; ij < length; ij++)
							tile.poly.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			replacements.put(dir, tiles);
			if (replacement.containsKey("plus")) for (Object tileName : (JsonArray) replacement.get("plus")) try {
				Tile tile = new Tile( ((StringValue) tileName).getValue(), new FileInputStream("./res/insel/" + ((StringValue) tileName).getValue()),
						gp);
				plus.add(tile);
				gp.getTileM().getTiles().add(tile);
				String[] sp = ((StringValue) tileName).getValue().split("[.]");
				if (new File("./res/collisions/insel/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
				+ ".collisionbox").exists())
					try {
						RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/insel/"
								+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox"), "rws");
						raf.seek(0l);
						int length = raf.readInt();
						tile.poly = new ArrayList<>();
						boolean s = false;
						for (int ij = 0; ij < length; ij++)
							tile.poly.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		for (Entry<Entry<Point2D, Integer>, Direction> en : avail) {
			Direction				dir				= en.getValue();
			List<Tile>				tiles			= replacements.get(dir);
			Random					random			= new Random();
			Tile					replacementTile	= tiles.get(random.nextInt(tiles.size()));
			Entry<Point2D, Integer>	key				= en.getKey();
			Point2D					correctedPoint	= new Point2D(key.getKey().getY(), key.getKey().getX()).add(mapPositions.get(key.getValue()));
			gp.getTileM().mapTileNum.get((int) correctedPoint.getY()).set((int) correctedPoint.getX(),
					gp.getTileM().getTiles().indexOf(replacementTile));
			if (Direction.DOWN == dir) {
				replacementTile = plus.get(random.nextInt(plus.size()));
				gp.getTileM().mapTileNum.get((int) correctedPoint.getY() + 1).set((int) correctedPoint.getX(),
						gp.getTileM().getTiles().indexOf(replacementTile));
			}
		}

		decorate();

		// TODO make menus work
	}

}
