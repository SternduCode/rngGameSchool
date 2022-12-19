package rngGame.tile;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.sterndu.json.*;
import javafx.geometry.Point2D;
import rngGame.main.GamePanel;

public class Map implements JsonValue {

	private final List<List<Integer>> mapNums;
	private final List<Tile> tiles;
	private final List<JsonObject> npcs, buildings;
	private String exitMap, textureDirectory;
	private Point2D exitSpawnPosition, exitPosition, spawnPoint;
	private int playerLayer;

	public Map(JsonObject map, GamePanel gp) {
		JsonObject exit = (JsonObject) map.get("exit");
		JsonArray npcs = (JsonArray) map.get("npcs");
		JsonArray buildings = (JsonArray) map.get("buildings");
		JsonObject mapObj = (JsonObject) map.get("map");

		JsonArray spawnPosition = (JsonArray) exit.get("spawnPosition");
		JsonArray position = (JsonArray) exit.get("position");

		JsonArray startingPosition = (JsonArray) mapObj.get("startingPosition");
		JsonArray textures = (JsonArray) mapObj.get("textures");
		JsonArray matrix = (JsonArray) mapObj.get("matrix");

		StringValue exitMap = (StringValue) exit.get("map");

		textureDirectory = ((StringValue) mapObj.get("dir")).getValue();
		mapNums = new ArrayList<>();
		for (Object y: matrix) {
			List<Integer> li = new ArrayList<>();
			mapNums.add(li);
			String[] xs = ((StringValue) y).getValue().split(" ");
			for (String x: xs) try {
				li.add(Integer.parseInt(x));
			} catch (NumberFormatException e) {
				li.add(0);
			}
		}
		tiles = new ArrayList<>();
		for (Object texture: textures) try {
			tiles.add(new Tile(((StringValue) texture).getValue(), new FileInputStream(new File("./res/"+textureDirectory+"/"+((StringValue) texture).getValue())), gp));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.npcs = npcs.parallelStream().map(jo -> (JsonObject) jo).collect(Collectors.toList());
		this.buildings = buildings.parallelStream().map(jo -> (JsonObject) jo).collect(Collectors.toList());
		this.exitMap = exitMap.getValue();
		exitSpawnPosition = new Point2D(((NumberValue) spawnPosition.get(0)).getValue().longValue(),
				((NumberValue) spawnPosition.get(1)).getValue().longValue());
		exitPosition = new Point2D(((NumberValue) position.get(0)).getValue().longValue(),
				((NumberValue) position.get(1)).getValue().longValue());
		spawnPoint = new Point2D(((NumberValue) startingPosition.get(0)).getValue().longValue(),
				((NumberValue) startingPosition.get(1)).getValue().longValue());
		playerLayer = ((NumberValue) mapObj.get("playerLayer")).getValue().intValue();
	}

	public Map(List<List<Integer>> mapNums, List<Tile> tiles, List<JsonObject> npcs, List<JsonObject> buildings,
			String exitMap, String textureDirectory, Point2D exitSpawnPosition, Point2D exitPosition,
			Point2D spawnPoint, int playerLayer) {
		this.mapNums = mapNums;
		this.tiles = tiles;
		this.npcs = npcs;
		this.buildings = buildings;
		this.exitMap = exitMap;
		this.textureDirectory = textureDirectory;
		this.exitSpawnPosition = exitSpawnPosition;
		this.exitPosition = exitPosition;
		this.spawnPoint = spawnPoint;
		this.playerLayer = playerLayer;
	}

	public List<JsonObject> getBuildings() { return buildings; }

	public String getExitMap() { return exitMap; }

	public Point2D getExitPosition() { return exitPosition; }

	public Point2D getExitSpawnPosition() { return exitSpawnPosition; }

	public int getHeight() {
		return mapNums.size();
	}

	public List<List<Integer>> getMapNums() { return mapNums; }

	public List<JsonObject> getNpcs() { return npcs; }

	public int getPlayerLayer() { return playerLayer; }

	public Point2D getSpawnPoint() { return spawnPoint; }

	public String getTextureDirectory() { return textureDirectory; }

	public List<Tile> getTiles() { return tiles; }

	public int getWidth() {
		return mapNums.parallelStream().mapToInt(List::size).max().orElse(0);
	}

	public void setExitMap(String exitMap) { this.exitMap = exitMap; }

	public void setExitPosition(Point2D exitPosition) { this.exitPosition = exitPosition; }

	public void setExitSpawnPosition(Point2D exitSpawnPosition) { this.exitSpawnPosition = exitSpawnPosition; }

	public void setPlayerLayer(int playerLayer) { this.playerLayer = playerLayer; }

	public void setSpawnPoint(Point2D spawnPoint) { this.spawnPoint = spawnPoint; }

	public void setTextureDirectory(String textureDirectory) { this.textureDirectory = textureDirectory; }

	@Override
	public JsonValue toJsonValue() {
		return null;// TODO
	}

	@Override
	public JsonValue toJsonValue(Function<Object, String> function) {
		return toJsonValue();
	}

}
