package rngGame.tile;

import java.io.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.geometry.Point2D;
import rngGame.main.SpielPanel;

public class DungeonGen {

	private final SpielPanel gp;
	private final JsonObject mainmap, maps[];
	private final List<Tile> mainMapTiles, mapsTiles[];
	private final List<List<Integer>> mainmapTileNum, mapsTileNum[];
	private final List<String> connectors, connections, replacements;
	private final List<Point2D> mainmapConnectors, mapsConnectors[];

	@SuppressWarnings("unchecked")
	public DungeonGen(SpielPanel gp, JsonObject mainmap, JsonObject[] maps, List<String> connectors,
			List<String> connections, List<String> replacements) {
		this.gp = gp;
		this.mainmap = mainmap;
		this.maps = maps;
		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));
		mainmapTileNum = gp.getTileM().mapTileNum;
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
		gp.getTileM().mapTileNum = null;
		this.connectors = connectors;
		this.connections = connections;
		this.replacements = replacements;

		mainmapConnectors = new ArrayList<>();
		mapsConnectors = new List[maps.length];
		for (int i = 0; i < maps.length; i++) mapsConnectors[i] = new ArrayList<>();
	}

	public void findConnectors() {
		Map<Integer, Tile> connectorTile = new HashMap<>();
		for (int i = 0; i < mainMapTiles.size(); i++) if (connectors.contains(mainMapTiles.get(i).name))
			connectorTile.put(i, mainMapTiles.get(i));
		System.out.println(connectorTile);
	}

}
