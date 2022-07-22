package tile;

import java.util.*;
import com.sterndu.json.*;
import buildings.*;
import entity.*;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import rngGAME.SpielPanel;

public class TileManager extends Pane {

	private final SpielPanel gp;
	private final List<Tile> tile;
	private int mapTileNum[][];
	private Group group;
	private List<Building> buildings;
	private List<NPC> npcs;
	private int maxCol, maxRow;
	private Map.Entry<Integer, Integer> startingPosition;


	public TileManager(SpielPanel gp) {
		this.gp = gp;

		tile = new ArrayList<>();
		try {
			JsonObject jo = (JsonObject) JsonParser
					.parse(getClass().getResourceAsStream("/res/maps/InnenContracts.txt"));
			JsonObject map = (JsonObject) jo.get("map");
			JsonArray textures = (JsonArray) map.get("textures");
			JsonArray npcs = (JsonArray) jo.get("npcs");
			JsonArray buildings = (JsonArray) jo.get("buildings");
			JsonArray size = (JsonArray) map.get("size");
			JsonArray startingPosition = (JsonArray) map.get("startingPosition");
			for (Object texture: textures)
				tile.add(new Tile(getClass().getResourceAsStream(((StringValue) texture).getValue()), gp));
			maxCol = ((NumberValue) size.get(0)).getValue().intValue();
			maxRow = ((NumberValue) size.get(1)).getValue().intValue();
			this.startingPosition = Map.entry(((NumberValue) startingPosition.get(0)).getValue().intValue(),
					((NumberValue) startingPosition.get(1)).getValue().intValue());
			mapTileNum = new int[maxCol][maxRow];
			loadMap(((StringValue) map.get("matrix")).getValue());
			this.buildings = new ArrayList<>();
			this.npcs = new ArrayList<>();
			for (Object building: buildings)
				this.buildings.add(switch (((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building);
					default -> new Building((JsonObject) building);
				});
			for (Object npc: npcs) this.npcs.add(new NPC((JsonObject) npc));

		} catch (JsonParseException e) {
			e.printStackTrace();
		}
	}

	public List<Building> getBuildingsFromMap() { return buildings; }

	public List<NPC> getNPCSFromMap() { return npcs; }

	public Map.Entry<Integer, Integer> getStartingPosition() { return startingPosition; }

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

	public void update() {
		int worldCol = 0;
		int worldRow = 0;

		if (group == null) {
			group = new Group();
			getChildren().add(group);
		}

		while (worldCol < maxCol && worldRow < maxRow) {
			int tileNum = mapTileNum[worldCol][worldRow];

			Player p = gp.getPlayer();

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
