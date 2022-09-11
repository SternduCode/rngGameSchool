package rngGame.tile;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import com.sterndu.json.*;
import javafx.geometry.Point2D;
import rngGame.main.SpielPanel;

public class DungeonGen {

	private final SpielPanel gp;
	private final JsonObject mainMap, maps[];
	private final List<Tile> mainMapTiles, mapsTiles[];
	private final List<List<Integer>> mainMapTileNum, mapsTileNum[];
	private final List<JsonObject> connectors, connections, replacements;
	private final List<Point2D> mainMapConnectors, mapsConnectors[];

	@SuppressWarnings("unchecked")
	public DungeonGen(SpielPanel gp, JsonObject mainmap, JsonObject[] maps, List<JsonObject> connectors,
			List<JsonObject> connections, List<JsonObject> replacements) {
		this.gp = gp;
		mainMap = mainmap;
		this.maps = maps;
		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));
		mainMapTileNum = gp.getTileM().mapTileNum;
		String dir = ((StringValue) ((JsonObject) mainmap.get("map")).get("dir")).getValue();
		mainMapTiles = new ArrayList<>();
		for (Object texture: (JsonArray) ((JsonObject) mainmap.get("map")).get("textures")) try {
			Tile t = new Tile(((StringValue) texture).getValue(),
					new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
					gp);
			mainMapTiles.add(t);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mapsTileNum = new List[maps.length];
		mapsTiles = new List[maps.length];
		for (int i =0;i<maps.length;i++) {
			gp.getTileM().loadMap((JsonArray) ((JsonObject) maps[i].get("map")).get("matrix"));
			mapsTileNum[i] = gp.getTileM().mapTileNum;
			dir = ((StringValue) ((JsonObject) maps[i].get("map")).get("dir")).getValue();
			mapsTiles[i] = new ArrayList<>();
			for (Object texture: (JsonArray) ((JsonObject) maps[i].get("map")).get("textures")) try {
				Tile t = new Tile(((StringValue) texture).getValue(),
						new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
						gp);
				mapsTiles[i].add(t);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		gp.getTileM().mapTileNum = new ArrayList<>();
		this.connectors = connectors;
		this.connections = connections;
		this.replacements = replacements;

		mainMapConnectors = new ArrayList<>();
		mapsConnectors = new List[maps.length];
		for (int i = 0; i < maps.length; i++) mapsConnectors[i] = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public void findConnectors() {
		Map<Integer, Map.Entry<Tile, JsonObject>> mainMapConnectorTiles = new HashMap<>();
		for (int i = 0; i < mainMapTiles.size(); i++) {
			int c_i = i;
			connectors.forEach(jobj -> {
				if (((StringValue) jobj.get("name")).getValue().equals(mainMapTiles.get(c_i).name))
					mainMapConnectorTiles.put(c_i, Map.entry(mainMapTiles.get(c_i), jobj));
			});
		}
		Map<Integer, Map.Entry<Tile, JsonObject>>[] mapsConnectorTiles = new HashMap[mapsTiles.length];
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
		System.out.println(mainMapConnectorTiles);
		for (Map<Integer, Entry<Tile, JsonObject>> connectorTiles: mapsConnectorTiles) System.out.println(connectorTiles);

	}

}
