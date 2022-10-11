package rngGame.tile;

import java.io.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
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
	private final Map<Integer, Map.Entry<Tile, JsonObject>> mainMapConnectorTiles, mapsConnectorTiles[];

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

		mainMapConnectorTiles = new HashMap<>();
		mapsConnectorTiles = new HashMap[mapsTiles.length];
		mainMapConnectors = new ArrayList<>();
		mapsConnectors = new List[maps.length];
		for (int i = 0; i < maps.length; i++) mapsConnectors[i] = new ArrayList<>();
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
		final Rectangle block = new Rectangle(5, 5);

		Shape mainMapMap = new Rectangle();
		Shape[] mapsMap = new Shape[mapsTileNum.length];

		for (int i = 0; i < mainMapTileNum.size(); i++) for (int j = 0; j < mainMapTileNum.get(i).size(); j++)
			if (mainMapTileNum.get(i).get(j)!=mainMapVoidNum) {
				block.setX(block.getWidth()*j);
				block.setY(block.getHeight()*i);
				mainMapMap = Shape.union(mainMapMap, block);
			}


		//		Stage s = new Stage();
		//		s.setScene(new Scene(new Group(mainMapMap)));
		//		s.show();

		for (int k = 0; k < mapsTileNum.length; k++) {
			Shape mapMap = new Rectangle();

			for (int i = 0; i < mapsTileNum[k].size(); i++) for (int j = 0; j < mapsTileNum[k].get(i).size(); j++)
				if (mapsTileNum[k].get(i).get(j) != mapsVoidNum[k]) {
					block.setX(block.getWidth() * j);
					block.setY(block.getHeight() * i);
					mapMap = Shape.union(mapMap, block);
				}

			mapsMap[k] = mapMap;

			//			Stage sx = new Stage();
			//			sx.setScene(new Scene(new Group(mapMap)));
			//			sx.show();
		}

		// TODO resort to ensure 1st has down, 2nd has left, 3rd has up, 4th has right

		Boolean[][] hasConnector = new Boolean[4][mapsMap.length];

		Shape merge = null;
		int distance = 7;
		double xShift = 0, yShift = 0;

		for (int i = 0; i < mapsMap.length; i++) {
			int c_i = i;
			Shape m = mapsMap[i];
			switch (i) {
				case 0: {
					List<Point2D> downConnectors = mapsConnectors[i].stream().filter(p -> {
						String direction = ((StringValue) mapsConnectorTiles[c_i]
								.get(mapsTileNum[c_i].get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("down");
					}).toList();
					Random r = new Random();
					Point2D downConnector = downConnectors.get(r.nextInt(downConnectors.size()));

					List<Point2D> upConnectors = mainMapConnectors.stream().filter(p -> {
						String direction = ((StringValue) mainMapConnectorTiles
								.get(mainMapTileNum.get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("up");
					}).toList();
					Point2D upConnector = upConnectors.get(r.nextInt(upConnectors.size()));

					mainMapMap.setLayoutY(
							yShift = (downConnector.getX() + distance - upConnector.getX()) * block.getHeight());
					mainMapMap.setLayoutX(xShift = (downConnector.getY() - upConnector.getY()) * block.getWidth());

					merge = Shape.union(mainMapMap, m);

					//					Stage sx = new Stage();
					//					sx.setScene(new Scene(new Group(merge)));
					//					sx.show();

					System.out.println(upConnector + " " + downConnector);

					break;
				}
				case 1: {
					List<Point2D> leftConnectors = mapsConnectors[i].stream().filter(p -> {
						String direction = ((StringValue) mapsConnectorTiles[c_i]
								.get(mapsTileNum[c_i].get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("left");
					}).toList();
					Random r = new Random();
					Point2D leftConnector = leftConnectors.get(r.nextInt(leftConnectors.size()));

					List<Point2D> rightConnectors = mainMapConnectors.stream().filter(p -> {
						String direction = ((StringValue) mainMapConnectorTiles
								.get(mainMapTileNum.get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("right");
					}).toList();
					Point2D rightConnector = rightConnectors.get(r.nextInt(rightConnectors.size()));

					m.setLayoutY(
							-((leftConnector.getX() - rightConnector.getX()) * block.getHeight() - yShift));
					m.setLayoutX(-((leftConnector.getY() - distance - rightConnector.getY()) * block.getWidth()
							- xShift));

					merge = Shape.union(merge, m);

					//					Stage sx = new Stage();
					//					sx.setScene(new Scene(new Group(merge)));
					//					sx.show();

					System.out.println(rightConnector + " " + leftConnector);

					break;
				}
				case 2: {
					List<Point2D> upConnectors = mapsConnectors[i].stream().filter(p -> {
						String direction = ((StringValue) mapsConnectorTiles[c_i]
								.get(mapsTileNum[c_i].get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("up");
					}).toList();
					Random r = new Random();
					Point2D upConnector = upConnectors.get(r.nextInt(upConnectors.size()));

					List<Point2D> downConnectors = mainMapConnectors.stream().filter(p -> {
						String direction = ((StringValue) mainMapConnectorTiles
								.get(mainMapTileNum.get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("down");
					}).toList();
					Point2D downConnector = downConnectors.get(r.nextInt(downConnectors.size()));

					m.setLayoutY(
							-((upConnector.getX() - distance - downConnector.getX()) * block.getHeight() - yShift));
					m.setLayoutX(
							-((upConnector.getY() - downConnector.getY()) * block.getWidth() - xShift));

					merge = Shape.union(merge, m);

					//					Stage sx = new Stage();
					//					sx.setScene(new Scene(new Group(merge)));
					//					sx.show();

					System.out.println(downConnector + " " + upConnector);

					break;
				}
				case 3: {
					List<Point2D> rightConnectors = mapsConnectors[i].stream().filter(p -> {
						String direction = ((StringValue) mapsConnectorTiles[c_i]
								.get(mapsTileNum[c_i].get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("right");
					}).toList();
					Random r = new Random();
					Point2D rightConnector = rightConnectors.get(r.nextInt(rightConnectors.size()));

					List<Point2D> leftConnectors = mainMapConnectors.stream().filter(p -> {
						String direction = ((StringValue) mainMapConnectorTiles
								.get(mainMapTileNum.get((int) p.getX()).get((int) p.getY())).getValue()
								.get("direction"))
								.getValue();
						return direction.equals("left");
					}).toList();
					Point2D leftConnector = leftConnectors.get(r.nextInt(leftConnectors.size()));

					m.setLayoutY(
							-((rightConnector.getX() - leftConnector.getX()) * block.getHeight() - yShift));
					merge.setLayoutX(
							(rightConnector.getY() + distance - leftConnector.getY()) * block.getWidth() - xShift);

					merge = Shape.union(merge, m);

					Stage sx = new Stage();
					sx.setScene(new Scene(new Group(merge)));
					sx.show();

					System.out.println(leftConnector + " " + rightConnector);

					break;
				}
			}
		}

		// TODO stitch

	}

}
