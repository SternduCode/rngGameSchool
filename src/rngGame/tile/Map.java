package rngGame.tile;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.sterndu.json.*;

import javafx.geometry.Point2D;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Map.
 */
public class Map implements JsonValue {

	/** The map nums. */
	private final List<List<Integer>> mapNums;

	/** The tiles. */
	private final List<Tile> tiles;

	/** The buildings. */
	private final List<JsonObject> npcs, buildings;

	/** The texture directory. */
	private String exitMap, textureDirectory;

	/** The spawn point. */
	private Point2D exitSpawnPosition, exitPosition, spawnPoint;

	/** The player layer. */
	private int playerLayer;

	/**
	 * Instantiates a new map.
	 *
	 * @param map the map
	 * @param gp the gp
	 */
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
		for (Object texture: textures)
			tiles.add(new Tile(((StringValue) texture).getValue(), "./res/"+textureDirectory+"/"+((StringValue) texture).getValue(), gp));
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

	/**
	 * Instantiates a new map.
	 *
	 * @param mapNums the map nums
	 * @param tiles the tiles
	 * @param npcs the npcs
	 * @param buildings the buildings
	 * @param exitMap the exit map
	 * @param textureDirectory the texture directory
	 * @param exitSpawnPosition the exit spawn position
	 * @param exitPosition the exit position
	 * @param spawnPoint the spawn point
	 * @param playerLayer the player layer
	 */
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

	/**
	 * Gets the buildings.
	 *
	 * @return the buildings
	 */
	public List<JsonObject> getBuildings() { return buildings; }

	/**
	 * Gets the exit map.
	 *
	 * @return the exit map
	 */
	public String getExitMap() { return exitMap; }

	/**
	 * Gets the exit position.
	 *
	 * @return the exit position
	 */
	public Point2D getExitPosition() { return exitPosition; }

	/**
	 * Gets the exit spawn position.
	 *
	 * @return the exit spawn position
	 */
	public Point2D getExitSpawnPosition() { return exitSpawnPosition; }

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return mapNums.size();
	}

	/**
	 * Gets the map nums.
	 *
	 * @return the map nums
	 */
	public List<List<Integer>> getMapNums() { return mapNums; }

	/**
	 * Gets the npcs.
	 *
	 * @return the npcs
	 */
	public List<JsonObject> getNpcs() { return npcs; }

	/**
	 * Gets the player layer.
	 *
	 * @return the player layer
	 */
	public int getPlayerLayer() { return playerLayer; }

	/**
	 * Gets the spawn point.
	 *
	 * @return the spawn point
	 */
	public Point2D getSpawnPoint() { return spawnPoint; }

	/**
	 * Gets the texture directory.
	 *
	 * @return the texture directory
	 */
	public String getTextureDirectory() { return textureDirectory; }

	/**
	 * Gets the tiles.
	 *
	 * @return the tiles
	 */
	public List<Tile> getTiles() { return tiles; }

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return mapNums.parallelStream().mapToInt(List::size).max().orElse(0);
	}

	/**
	 * Sets the exit map.
	 *
	 * @param exitMap the new exit map
	 */
	public void setExitMap(String exitMap) { this.exitMap = exitMap; }

	/**
	 * Sets the exit position.
	 *
	 * @param exitPosition the new exit position
	 */
	public void setExitPosition(Point2D exitPosition) { this.exitPosition = exitPosition; }

	/**
	 * Sets the exit spawn position.
	 *
	 * @param exitSpawnPosition the new exit spawn position
	 */
	public void setExitSpawnPosition(Point2D exitSpawnPosition) { this.exitSpawnPosition = exitSpawnPosition; }

	/**
	 * Sets the player layer.
	 *
	 * @param playerLayer the new player layer
	 */
	public void setPlayerLayer(int playerLayer) { this.playerLayer = playerLayer; }

	/**
	 * Sets the spawn point.
	 *
	 * @param spawnPoint the new spawn point
	 */
	public void setSpawnPoint(Point2D spawnPoint) { this.spawnPoint = spawnPoint; }

	/**
	 * Sets the texture directory.
	 *
	 * @param textureDirectory the new texture directory
	 */
	public void setTextureDirectory(String textureDirectory) { this.textureDirectory = textureDirectory; }

	/**
	 * To json value.
	 *
	 * @return the json value
	 */
	@Override
	public JsonValue toJsonValue() {
		return null;// TODO
	}

	/**
	 * To json value.
	 *
	 * @param function the function
	 * @return the json value
	 */
	@Override
	public JsonValue toJsonValue(Function<Object, String> function) {
		return toJsonValue();
	}

}
