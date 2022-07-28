package tile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import com.sterndu.json.*;
import buildings.*;
import entity.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import rngGAME.*;

public class TileManager extends Pane {

	private String path;
	private final SpielPanel gp;
	private final List<Tile> tile;
	private int mapTileNum[][];
	private final Group group;
	private List<Building> buildings;
	private List<NPC> npcs;
	private int maxCol, maxRow;
	private String exitMap, dir;
	private final ContextMenu cm;
	private final ObjectProperty<TextureHolder> requestor = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requestor"; }
	};

	private Map.Entry<Double, Double> startingPosition, exitStartingPosition, exitPosition;

	private Menu mtiles, mnpcs, mbuildings;


	public TileManager(SpielPanel gp) {
		cm = new ContextMenu();
		cm.getItems().addAll(mtiles = new Menu("Tiles"), mnpcs = new Menu("NPCs"), mbuildings = new Menu("Buildings"));

		this.gp = gp;

		tile = new ArrayList<>();

		group = new Group();
		getChildren().add(group);
	}

	public boolean collides(Collidable collidable) {

		for (Node th: group.getChildren())
			if (((TextureHolder) th).getPoly().getPoints().size() > 0) {
				Shape intersect = Shape.intersect(collidable.getPoly(), ((TextureHolder) th).getPoly());
				if (!intersect.getBoundsInLocal().isEmpty()) return true;
			}

		return false;
	}

	public void contMen(ActionEvent e) {
		try {
			if (e.getSource() instanceof MenuItemWTile miwt)
				requestor.get().setTile(miwt.getTile());
			else if (e.getSource() instanceof MenuItemWBuilding miwb)
				miwb.getBuilding().getClass()
				.getDeclaredConstructor(miwb.getBuilding().getClass(), List.class, gp.getClass())
				.newInstance(miwb.getBuilding(), buildings, gp)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().worldX,
						requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().worldY);
			else if (e.getSource() instanceof MenuItemWNPC miwn)
				miwn.getNPC().getClass()
				.getDeclaredConstructor(miwn.getNPC().getClass(), List.class, gp.getClass())
				.newInstance(miwn.getNPC(), npcs, gp)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().worldX,
						requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().worldY);
			else if (e.getSource() instanceof MenuItem mi) if (mi.getText().equals("add Texture"))
				System.out.println(mi.getText());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public List<Building> getBuildingsFromMap() { return buildings; }

	public String getExitMap() { return exitMap; }

	public Map.Entry<Double, Double> getExitPosition() { return exitPosition; }

	public Map.Entry<Double, Double> getExitStartingPosition() { return exitStartingPosition; }

	public List<NPC> getNPCSFromMap() { return npcs; }

	public Map.Entry<Double, Double> getStartingPosition() { return startingPosition; }

	public void loadMap(JsonArray data) {

		int col = 0;
		int row = 0;

		try {

			int idx = 0;

			while (row < data.size() && row < maxRow) {

				String line = ((StringValue) data.get(idx)).getValue();
				String[] numbers = line.split(" ");

				while (col < maxCol && col < numbers.length) {

					int num = Integer.parseInt(numbers[col]);

					mapTileNum[col][row] = num;
					col++;
				}
				if (col == maxCol || col == numbers.length) {
					col = 0;
					row++;
					idx++;
				}
			}

		} catch (Exception e) {
			new Exception(row + 1 + " " + (col + 1), e).printStackTrace();
		}

	}

	public void save() {
		try {
			File out = new File(getClass().getResource(path).toURI());
			System.out.println(out);
			if (out.exists()) {
				JsonObject jo = (JsonObject) JsonParser.parse(out);
				JsonArray buildings = (JsonArray) jo.get("buildings");
				buildings.clear();
				buildings.addAll(this.buildings);
				BufferedWriter bw = new BufferedWriter(new FileWriter(out));
				bw.write(jo.toJson());
				bw.flush();
				bw.close();
			} else {

			}
		} catch (URISyntaxException | JsonParseException | IOException e) {
			e.printStackTrace();
		}
	}

	public void setMap(String path) {
		try {
			this.path=path;

			exitPosition = null;
			exitStartingPosition = null;
			exitMap = null;

			group.getChildren().clear();
			tile.clear();
			mtiles.getItems().clear();
			mnpcs.getItems().clear();
			mbuildings.getItems().clear();
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
			for (Object texture: textures) try {
				Tile t = new Tile(
						getClass().getResourceAsStream("/res/" + dir + "/" + ((StringValue) texture).getValue()),
						gp);
				tile.add(t);
				mtiles.getItems()
				.add(new MenuItemWTile(((StringValue) texture).getValue(),
						new ImageView(ImgUtil.resizeImage(t.images.get(0),
								(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 16, 16)),
						t));
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				if (getClass()
						.getResource("/res/collisions/" + dir + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
						+ ".collisionbox") != null)
					try {
						RandomAccessFile raf = new RandomAccessFile(getClass()
								.getResource(
										"/res/collisions/" + dir + "/"
												+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
												+ ".collisionbox")
								.getFile(), "rws");
						raf.seek(0l);
						int length = raf.readInt();
						t.poly = new ArrayList<>();
						for (int i = 0; i < length; i++) t.poly.add(raf.readDouble());
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (NullPointerException e) {
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				new IOException(dir + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1)), e)
				.printStackTrace();
			}
			maxCol = ((NumberValue) size.get(0)).getValue().intValue();
			maxRow = ((NumberValue) size.get(1)).getValue().intValue();
			this.startingPosition = Map.entry(((NumberValue) startingPosition.get(0)).getValue().doubleValue(),
					((NumberValue) startingPosition.get(1)).getValue().doubleValue());
			mapTileNum = new int[maxCol][maxRow];
			if (map.get("matrix") instanceof StringValue sv) loadMap(new JsonArray(
					Arrays.asList(sv.getValue().replaceAll("\r", "\n").replaceAll("\n\n", "\n").split("\n")).stream()
							.map(StringValue::new).toList()));
			else loadMap((JsonArray) map.get("matrix"));
			this.buildings = new ArrayList<>();
			this.npcs = new ArrayList<>();
			for (Object building: buildings) {
				switch (((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building, this.buildings);
					default -> new Building((JsonObject) building, this.buildings);
				}
				mbuildings.getItems().add(new MenuItemWBuilding(
						((StringValue) ((JsonObject) ((JsonObject) building).get("textures")).values().stream()
								.findFirst().get()).getValue(),
						new ImageView(ImgUtil.resizeImage(this.buildings.get(this.buildings.size() - 1).getFirstImage(),
								(int) this.buildings.get(this.buildings.size() - 1).getFirstImage().getWidth(),
								(int) this.buildings.get(this.buildings.size() - 1).getFirstImage().getHeight(),
								16, 16)),
						this.buildings.get(this.buildings.size() - 1)));
			}
			for (Object npc: npcs) {
				this.npcs.add(switch (((StringValue) ((JsonObject) npc).get("type")).getValue()) {
					case "npc" -> new NPC((JsonObject) npc);
					case "demon" -> new Demon((JsonObject) npc);
					default -> new NPC((JsonObject) npc);
				});
				mnpcs.getItems()
				.add(new MenuItemWNPC(
						((StringValue) ((JsonObject) ((JsonObject) npc).get("textures")).values().stream()
								.findFirst().get()).getValue(),
						new ImageView(ImgUtil.resizeImage(this.npcs.get(this.npcs.size() - 1).getFirstImage(),
								(int) this.npcs.get(this.npcs.size() - 1).getFirstImage().getWidth(),
								(int) this.npcs.get(this.npcs.size() - 1).getFirstImage().getHeight(),
								16, 16)),
						this.npcs.get(this.npcs.size() - 1)));
			}
			mtiles.getItems().add(new MenuItem("add Texture"));
			mnpcs.getItems().add(new MenuItem("add Texture"));
			mbuildings.getItems().add(new MenuItem("add Texture"));
			for (MenuItem mi: mtiles.getItems()) mi.setOnAction(this::contMen);
			for (MenuItem mi: mnpcs.getItems()) mi.setOnAction(this::contMen);
			for (MenuItem mi: mbuildings.getItems()) mi.setOnAction(this::contMen);

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
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY, cm,
							requestor);
					group.getChildren().add(worldRow * maxCol + worldCol, th);
				} else {
					th.setLayoutX(screenX);
					th.setLayoutY(screenY);
					th.update();
				}
				th.setVisible(true);
			} else {
				TextureHolder th = null;
				if (group.getChildren().size() > worldRow * maxCol + worldCol)
					th = (TextureHolder) group.getChildren().get(worldRow * maxCol + worldCol);
				if (th != null) {
					th.setVisible(false);
					th.update();
				}
				else {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY, cm,
							requestor);
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
