package rngGame.tile;

import java.io.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.geometry.Point2D;
import javafx.scene.shape.*;
import rngGame.main.SpielPanel;

public class DungeonGen {

	private final SpielPanel gp;
	private final JsonObject mainMap, maps[];
	private final List<Tile> mainMapTiles, mapsTiles[];
	private final List<List<Integer>> mainMapTileNum, mapsTileNum[];
	private final List<JsonObject> connectors, connections, replacements;
	private final List<Point2D> mainMapConnectors, mapsConnectors[];
	private int mainMapVoidNum;
	private final int mapsVoidNum[];

	@SuppressWarnings("unchecked")
	public DungeonGen(SpielPanel gp, String voidImg, JsonObject mainmap, JsonObject[] maps, List<JsonObject> connectors,
			List<JsonObject> connections, List<JsonObject> replacements) {
		this.gp = gp;
		mainMap = mainmap;
		this.maps = maps;
		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));
		mainMapTileNum = gp.getTileM().mapTileNum;
		gp.getTileM().mapTileNum = new ArrayList<>();
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
		for (List<Integer> xCol: mainMapTileNum) for (Integer v: xCol) if (mainMapTiles.get(v).name.equals(voidImg))
			mainMapVoidNum = v;
		mapsTileNum = new List[maps.length];
		mapsTiles = new List[maps.length];
		mapsVoidNum = new int[maps.length];
		for (int i =0;i<maps.length;i++) {
			gp.getTileM().loadMap((JsonArray) ((JsonObject) maps[i].get("map")).get("matrix"));
			mapsTileNum[i] = gp.getTileM().mapTileNum;
			gp.getTileM().mapTileNum = new ArrayList<>();
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
			for (List<Integer> xCol: mapsTileNum[i]) for (Integer v: xCol) if (mapsTiles[i].get(v).name.equals(voidImg))
				mapsVoidNum[i] = v;
		}
		gp.getTileM().mapTileNum = new ArrayList<>();
		this.connectors = connectors;
		this.connections = connections;
		this.replacements = replacements;

		mainMapConnectors = new ArrayList<>();
		mapsConnectors = new List[maps.length];
		for (int i = 0; i < maps.length; i++) mapsConnectors[i] = new ArrayList<>();
		System.out.println(mainMapVoidNum);
	}

	@SuppressWarnings("unchecked")
	public void findFreeConnectors() {
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
		final Rectangle block = new Rectangle(50, 50);

		Shape map = new Rectangle();
		
		for (int i=0;i<mainMapTileNum.size();i++) for (int j =0;j<mainMapTileNum.get(i).size();j++) {
			//TODO create shape map with block at non void tiles
			
		}
				
	}

}
