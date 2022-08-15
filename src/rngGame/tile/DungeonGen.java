package rngGame.tile;

import java.util.List;
import com.sterndu.json.*;
import rngGame.main.SpielPanel;

public class DungeonGen {

	private final SpielPanel gp;
	private final JsonObject mainmap, maps[];
	private final List<List<Integer>> mainmapTileNum;
	private List<List<Integer>> mapsTileNum[];

	public DungeonGen(SpielPanel gp, JsonObject mainmap, JsonObject[] maps) {
		this.gp = gp;
		this.mainmap = mainmap;
		this.maps = maps;
		gp.getTileM().loadMap((JsonArray) ((JsonObject) mainmap.get("map")).get("matrix"));
		mainmapTileNum = gp.getTileM().mapTileNum;
		for (int i =0;i<maps.length;i++) {
			gp.getTileM().loadMap((JsonArray) ((JsonObject) maps[i].get("map")).get("matrix"));
			mapsTileNum[i] = gp.getTileM().mapTileNum;
		}
		gp.getTileM().mapTileNum = null;
	}

	public void findConnectors() {

	}

}
