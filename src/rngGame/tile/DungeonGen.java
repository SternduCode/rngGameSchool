package rngGame.tile;

import java.io.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.geometry.Point2D;
import rngGame.main.GamePanel;

public class DungeonGen {

	private final GamePanel gp;
	private final JsonObject mainMap;
	private final List<JsonObject> maps;
	private final List<Tile> mainMapTiles, mapsTiles[];
	private final List<List<Integer>> mainMapTileNum, mapsTileNum[];
	private final List<JsonObject> connectors, connections, replacements;
	private final List<Point2D> mainMapConnectors, mapsConnectors[];
	private int mainMapVoidNum;
	private final int mapsVoidNum[];
	private final Map<Integer, Map.Entry<Tile, JsonObject>> mainMapConnectorTiles, mapsConnectorTiles[];

	@SuppressWarnings("unchecked")
	public DungeonGen(GamePanel gp, String voidImg, JsonObject mainmap, JsonArray maps, List<JsonObject> connectors,
			List<JsonObject> connections, List<JsonObject> replacements) {
		this.gp = gp;
		mainMap = mainmap;

		this.maps = new ArrayList<>();

		for (Object folder:maps) if (folder instanceof StringValue sv) {
			System.out.println(sv.getValue());
			File f = new File("./res/maps/" + sv.getValue());
			for (File m: f.listFiles((FilenameFilter) (dir, name) -> name.endsWith(".json"))) try {
				this.maps.add((JsonObject) JsonParser.parse(m));
			} catch (JsonParseException e) {
				e.printStackTrace();
			}
		}

		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));// load mainmap matrix
		mainMapTileNum = gp.getTileM().mapTileNum; // save loaded mainmap matrix
		gp.getTileM().mapTileNum = new ArrayList<>();

		String dir = ((StringValue) ((JsonObject) mainmap.get("map")).get("dir")).getValue();

		mainMapTiles = new ArrayList<>();

		for (Object texture: (JsonArray) ((JsonObject) mainmap.get("map")).get("textures")) try {// load mainmap tiles
			Tile t = new Tile(((StringValue) texture).getValue(),
					new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
					gp);
			mainMapTiles.add(t);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		for (List<Integer> xCol: mainMapTileNum) for (Integer v: xCol) if (mainMapTiles.get(v).name.equals(voidImg))
			mainMapVoidNum = v; // find void in mainmap tiles

		mapsTileNum = new List[maps.size()];
		mapsTiles = new List[maps.size()];
		mapsVoidNum = new int[maps.size()];

		for (int i = 0; i < maps.size(); i++) {// TODO check for validity
			gp.getTileM().loadMap((JsonArray) ((JsonObject) this.maps.get(i).get("map")).get("matrix"));
			mapsTileNum[i] = gp.getTileM().mapTileNum;
			gp.getTileM().mapTileNum = new ArrayList<>();
			dir = ((StringValue) ((JsonObject) this.maps.get(i).get("map")).get("dir")).getValue();
			mapsTiles[i] = new ArrayList<>();
			for (Object texture: (JsonArray) ((JsonObject) this.maps.get(i).get("map")).get("textures")) try {
				Tile t = new Tile(((StringValue) texture).getValue(),
						new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
						gp);
				mapsTiles[i].add(t);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			for (List<Integer> xCol: mapsTileNum[i]) for (Integer v: xCol) if (mapsTiles[i].get(v).name.equals(voidImg))
				mapsVoidNum[i] = v;
		}
		gp.getTileM().mapTileNum = new ArrayList<>();
		this.connectors = connectors;
		this.connections = connections;
		this.replacements = replacements;

		mainMapConnectorTiles = new HashMap<>();
		mapsConnectorTiles = new HashMap[mapsTiles.length];
		mainMapConnectors = new ArrayList<>();
		mapsConnectors = new List[maps.size()];
		for (int i = 0; i < maps.size(); i++) mapsConnectors[i] = new ArrayList<>();
		System.out.println(mainMapVoidNum);
	}

	@SuppressWarnings("unchecked")
	public void findFreeConnectors() {
		for (int i = 0; i < mainMapTiles.size(); i++) {
			int c_i = i;
			connectors.forEach(jobj -> {
				if (((StringValue) jobj.get("name")).getValue().equals(mainMapTiles.get(c_i).name))
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
					if (((StringValue) jobj.get("name")).getValue().equals(mapTiles.get(c_j).name))
						mapsConnectorTiles[c_i].put(c_j, Map.entry(mapTiles.get(c_j), jobj));
				});
			}
		}

		for (int i = 0; i < mainMapTileNum.size(); i++) for (int j = 0; j < mainMapTileNum.get(i).size(); j++)
			if (mainMapConnectorTiles.containsKey(mainMapTileNum.get(i).get(j)))
				mainMapConnectors.add(new Point2D(i, j));

		for (int s = 0; s < mapsTileNum.length; s++) {
			List<List<Integer>> mapTileNum = mapsTileNum[s];
			for (int i = 0; i < mapTileNum.size(); i++) for (int j = 0; j < mapTileNum.get(i).size(); j++)
				if (mapsConnectorTiles[s].containsKey(mapTileNum.get(i).get(j)))
					mapsConnectors[s].add(new Point2D(i, j));
		}

		Iterator<Point2D> it = mainMapConnectors.iterator();
		while (it.hasNext()) {
			Point2D p = it.next();
			String direction = ((StringValue) mainMapConnectorTiles
					.get(mainMapTileNum.get((int) p.getX()).get((int) p.getY())).getValue().get("direction"))
					.getValue();
			try {
				switch (direction) {
					case "up":
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getX() - 1).get((int) p.getY()))
							it.remove();
						break;
					case "down":
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getX() + 1).get((int) p.getY()))
							it.remove();
						break;
					case "left":
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getX()).get((int) p.getY() - 1))
							it.remove();
						break;
					case "right":
						if (mainMapVoidNum != mainMapTileNum.get((int) p.getX()).get((int) p.getY() + 1))
							it.remove();
						break;
				}
			} catch (Exception e) {
			}
		}

		for (int i = 0; i < mapsConnectors.length; i++) {
			it = mapsConnectors[i].iterator();
			while (it.hasNext()) {
				Point2D p = it.next();
				String direction = ((StringValue) mapsConnectorTiles[i]
						.get(mapsTileNum[i].get((int) p.getX()).get((int) p.getY())).getValue().get("direction"))
						.getValue();
				try {
					switch (direction) {
						case "up":
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getX() - 1).get((int) p.getY()))
								it.remove();
							break;
						case "down":
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getX() + 1).get((int) p.getY()))
								it.remove();
							break;
						case "left":
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getX()).get((int) p.getY() - 1))
								it.remove();
							break;
						case "right":
							if (mapsVoidNum[i] != mapsTileNum[i].get((int) p.getX()).get((int) p.getY() + 1))
								it.remove();
							break;
					}
				} catch (Exception e) {
				}
			}
		}

		System.out.println(mainMapConnectors.size() + " " + mainMapConnectors);
		for (List<Point2D> connectors: mapsConnectors) System.out.println(connectors.size() + " " + connectors);

	}

	public void stitchMaps() {
		List<List<List<Integer>>> upmaps = new ArrayList<>(),
				downmaps = new ArrayList<>(),
				leftmaps = new ArrayList<>(),
				rightmaps = new ArrayList<>();
	}

}
