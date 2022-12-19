package rngGame.tile;

import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.sterndu.json.*;

import javafx.geometry.Point2D;
import javafx.scene.shape.*;
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
	 */
	@SuppressWarnings("unchecked")
	public DungeonGen(GamePanel gp, String voidImg, JsonObject mainmap, JsonArray maps, List<JsonObject> connectors, List<JsonObject> connections,
			List<JsonObject> replacements) {
		this.gp	= gp;
		mainMap	= mainmap;

		difficulty = Difficulty.EASY;

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

		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));// load mainmap matrix
		mainMapTileNum				= gp.getTileM().mapTileNum;	// save loaded mainmap matrix
		gp.getTileM().mapTileNum	= new ArrayList<>();

		String dir = ((StringValue) ((JsonObject) mainmap.get("map")).get("dir")).getValue();

		mainMapTiles = new ArrayList<>();

		for (Object texture : (JsonArray) ((JsonObject) mainmap.get("map")).get("textures")) try {// load mainmap tiles
			Tile t = new Tile( ((StringValue) texture).getValue(), new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
					gp);
			mainMapTiles.add(t); 
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
								
								bridges.add(Map.entry(conn.getKey().getKey() , conn2.getKey())); 
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
								
								bridges.add(Map.entry(conn.getKey().getKey() , conn2.getKey())); 
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
								
								bridges.add(Map.entry(conn.getKey().getKey() , conn2.getKey())); 
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
							
								bridges.add(Map.entry(conn.getKey().getKey() , conn2.getKey())); 
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
			if(Math.abs(bridge.getKey().getX() - bridge.getValue().getX()) > 0) {
				for(int i = 1 ; i < Math.abs(bridge.getKey().getX() - bridge.getValue().getX()) ; i++) {
				gp.getTileM().mapTileNum.get((int) bridge.getKey().getY()).set((int) (Math.min(bridge.getKey().getX(), bridge.getValue().getX()) + i) , gp.getTileM().getTiles().size());
			}
			} else {
				for(int i = 1 ; i < Math.abs(bridge.getKey().getY() - bridge.getValue().getY()) ; i++) {
					gp.getTileM().mapTileNum.get((int) (Math.min(bridge.getKey().getY(), bridge.getValue().getY()) + i) ).set((int) bridge.getKey().getX(), gp.getTileM().getTiles().size() + 1);
				}
			}
		});
		
		try {
			gp.getTileM().getTiles().add(new Tile(((StringValue) ((JsonArray) connections.get(0).get("name")).get(0)).getValue(), new FileInputStream("./res/insel/" + (((StringValue) ((JsonArray) connections.get(0).get("name")).get(0)).getValue())), gp));
			gp.getTileM().getTiles().add(new Tile(((StringValue) ((JsonArray) connections.get(1).get("name")).get(0)).getValue(), new FileInputStream("./res/insel/" + (((StringValue) ((JsonArray) connections.get(1).get("name")).get(0)).getValue())), gp));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		gp.getTileM().setStartingPosition(new double[] {
				(x - xOffset + .5) * gp.BgX, (y - yOffset + .5) * gp.BgY
		});

		// TODO clean up (code & map)
		// TODO make bridges
	}

}
