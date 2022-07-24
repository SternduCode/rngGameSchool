package tile;

import java.io.*;
import java.util.*;
import com.sterndu.json.*;
import buildings.*;
import entity.*;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import rngGAME.*;

public class TileManager extends Pane {

	private final SpielPanel gp;
	private final List<Tile> tile;
	private int mapTileNum[][];
	private final Group group;
	private List<Building> buildings;
	private List<NPC> npcs;
	private int maxCol, maxRow;
	private String exitMap, dir;

	private Map.Entry<Double, Double> startingPosition, exitStartingPosition, exitPosition;


	public TileManager(SpielPanel gp) {
		this.gp = gp;

		tile = new ArrayList<>();

		group = new Group();
		getChildren().add(group);
	}

	public boolean collides(Collidable collidable) {

		return false;
	}

	public List<Building> getBuildingsFromMap() { return buildings; }

	public String getExitMap() { return exitMap; }

	public Map.Entry<Double, Double> getExitPosition() { return exitPosition; }

	public Map.Entry<Double, Double> getExitStartingPosition() { return exitStartingPosition; }

	public List<NPC> getNPCSFromMap() { return npcs; }

	public Map.Entry<Double, Double> getStartingPosition() { return startingPosition; }

	public void loadMap(String data) {

		int col = 0;
		int row = 0;

		try {

			String[] lines = data.replaceAll("\r", "").split("\n");
			int idx = 0;

			while (col < maxCol && row < maxRow) {

				String line = lines[idx];
				String numbers[] = line.split(" ");

				while (col < maxCol) {

					int num = Integer.parseInt(numbers[col]);

					mapTileNum[col][row] = num;
					col++;
				}
				if (col == maxCol) {
					col = 0;
					row++;
					idx++;
				}
			}

		} catch (Exception e) {
			new Exception(row + 1 + " " + (col + 1), e).printStackTrace();
		}

	}

	public void setMap(String path) {
		try {
			exitPosition = null;
			exitStartingPosition = null;
			exitMap = null;

			group.getChildren().clear();
			tile.clear();
			JsonObject jo = (JsonObject) JsonParser
					.parse(getClass().getResourceAsStream(path));
			JsonObject map = (JsonObject) jo.get("map");
			JsonArray textures = (JsonArray) map.get("textures");
			JsonArray npcs = (JsonArray) jo.get("npcs");
			JsonArray buildings = (JsonArray) jo.get("buildings");
			JsonArray size = (JsonArray) map.get("size");
			JsonArray startingPosition = (JsonArray) map.get("startingPosition");

			dir = ((StringValue) map.get("dir")).getValue();

			if (jo.containsKey("exit")) {
				JsonObject exit = (JsonObject) jo.get("exit");
				exitMap = ((StringValue) exit.get("map")).getValue();
				JsonArray spawnPosition = (JsonArray) exit.get("spawnPosition");
				JsonArray position = (JsonArray) exit.get("position");
				exitStartingPosition = Map.entry(((NumberValue) spawnPosition.get(0)).getValue().doubleValue(),
						((NumberValue) spawnPosition.get(1)).getValue().doubleValue());
				exitPosition = Map.entry(((NumberValue) position.get(0)).getValue().doubleValue(),
						((NumberValue) position.get(1)).getValue().doubleValue());
			}
			for (Object texture: textures) {
				Tile t = new Tile(
						getClass().getResourceAsStream("/res/" + dir + "/" + ((StringValue) texture).getValue()),
						gp);
				tile.add(t);
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				if (getClass()
						.getResource("/res/collisions/tiles/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
						+ ".collisionbox") != null)
					try {
						RandomAccessFile raf = new RandomAccessFile(getClass()
								.getResource(
										"/res/collisions/tiles/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
										+ ".collisionbox")
								.getFile(), "rws");
						raf.seek(0l);
						int length = raf.readInt();
						t.poly = new ArrayList<>();
						for (int i = 0; i < length; i++) t.poly.add(raf.readDouble());
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			maxCol = ((NumberValue) size.get(0)).getValue().intValue();
			maxRow = ((NumberValue) size.get(1)).getValue().intValue();
			this.startingPosition = Map.entry(((NumberValue) startingPosition.get(0)).getValue().doubleValue(),
					((NumberValue) startingPosition.get(1)).getValue().doubleValue());
			mapTileNum = new int[maxCol][maxRow];
			loadMap(((StringValue) map.get("matrix")).getValue());
			this.buildings = new ArrayList<>();
			this.npcs = new ArrayList<>();
			for (Object building: buildings)
				this.buildings.add(switch (((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building);
					default -> new Building((JsonObject) building);
				});
			for (Object npc: npcs)
				this.npcs.add(switch (((StringValue) ((JsonObject) npc).get("type")).getValue()) {
					case "npc" -> new NPC((JsonObject) npc);
					case "demon" -> new Demon((JsonObject) npc);
					default -> new NPC((JsonObject) npc);
				});

		} catch (JsonParseException e) {
			e.printStackTrace();
		}
	}

	public void update() {

		Player p = gp.getPlayer();

		if (exitMap != null) {
			int worldX = (int) (gp.Bg * exitPosition.getKey());
			int worldY = (int) (gp.Bg * exitPosition.getValue());

			if (worldX + gp.Bg / 2 - p.worldX < 105 && worldX + gp.Bg / 2 - p.worldX > -45 &&
					worldY + gp.Bg / 2 - p.worldY < 25 && worldY + gp.Bg / 2 - p.worldY > 0)
				gp.setMap("/res/maps/" + exitMap, exitStartingPosition);
		}

		int worldCol = 0;
		int worldRow = 0;

		while (worldCol < maxCol && worldRow < maxRow) {
			int tileNum = mapTileNum[worldCol][worldRow];

			int worldX = worldCol * gp.Bg;
			int worldY = worldRow * gp.Bg;
			int screenX = worldX - p.worldX + p.screenX;
			int screenY = worldY - p.worldY + p.screenY;

			if (worldX + gp.Bg > p.worldX - p.screenX
				&& worldX - gp.Bg < p.worldX + p.screenX
				&& worldY + gp.Bg > p.worldY - p.screenY
				&& worldY - gp.Bg < p.worldY + p.screenY) {
				TextureHolder th = null;
				if (group.getChildren().size() > worldRow * maxCol + worldCol)
					th = (TextureHolder) group.getChildren().get(worldRow * maxCol + worldCol);
				if (th == null) {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY);
					group.getChildren().add(worldRow * maxCol + worldCol, th);
				} else {
					th.setLayoutX(screenX);
					th.setLayoutY(screenY);
				}
				th.setVisible(true);
			} else {
				TextureHolder th = null;
				if (group.getChildren().size() > worldRow * maxCol + worldCol)
					th = (TextureHolder) group.getChildren().get(worldRow * maxCol + worldCol);
				if (th != null) th.setVisible(false);
				else {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY);
					th.setVisible(false);
					group.getChildren().add(worldRow * maxCol + worldCol, th);
				}
			}

			worldCol++;

			if (worldCol == maxCol) {
				worldCol = 0;
				worldRow++;
			}
		}

	}

}
