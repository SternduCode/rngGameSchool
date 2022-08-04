package rngGame.tile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.buildings.*;
import rngGame.entity.*;
import rngGame.main.*;

public class TileManager extends Pane {

	private String path;
	private final SpielPanel gp;
	private final List<Tile> tile;
	private List<List<Integer>> mapTileNum;
	private List<List<TextureHolder>> map;
	private final Group group;
	private List<Building> buildings;
	private List<NPC> npcs;
	private String exitMap, dir, backgroundPath;
	private final ContextMenu cm, npcCM, buildingCM;
	private final ObjectProperty<TextureHolder> requestor = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requestor"; }
	};
	private final ObjectProperty<Building> requestorB = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requestor"; }
	};
	private final ObjectProperty<NPC> requestorN = new ObjectPropertyBase<>() {

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
		buildingCM = new ContextMenu();
		npcCM = new ContextMenu();

		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true") && !cm.isShowing()) {
				System.out.println("dg");
				requestor.set(null);
				cm.show(this, e.getScreenX(), e.getScreenY());
			}
		});

		this.gp = gp;

		tile = new ArrayList<>();

		group = new Group();
		getChildren().add(group);
	}

	public boolean collides(GameObject collidable) {

		for (Node th: group.getChildren())
			if (((TextureHolder) th).getPoly().getPoints().size() > 0) {
				Shape intersect = Shape.intersect(collidable.getCollisionBox(), ((TextureHolder) th).getPoly());
				if (!intersect.getBoundsInLocal().isEmpty()) return true;
			}

		return false;
	}

	public void contextMenu(ActionEvent e) {
		try {
			if (requestor.get() == null) System.out.println(e);
			else if (e.getSource() instanceof MenuItemWTile miwt) requestor.get().setTile(miwt.getTile());
			else if (e.getSource() instanceof MenuItemWBuilding miwb)
				miwb.getBuilding().getClass()
				.getDeclaredConstructor(miwb.getBuilding().getClass(), List.class, gp.getClass())
				.newInstance(miwb.getBuilding(), buildings, gp)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItemWNPC miwn)
				miwn.getNPC().getClass()
				.getDeclaredConstructor(miwn.getNPC().getClass(), List.class, gp.getClass())
				.newInstance(miwn.getNPC(), npcs, gp)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItem mi && mi.getText().equals("add Texture")) if (mi.getParentMenu() == mnpcs) {
				System.out.println("npcs");
				System.out.println("pfuck");
			} else if (mi.getParentMenu() == mbuildings){
				System.out.println("buzildib");
				System.out.println("pfuck");
			} else if (mi.getParentMenu() == mtiles) {
				System.out.println("tiles");
				System.out.println("pfuck");
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path p1 = f.toPath();
					Path p2 = new File("./res/" + dir + "/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Tile t = new Tile(f.getName(),
							new FileInputStream("./res/" + dir + "/" + f.getName()),
							gp);
					tile.add(t);
					mtiles.getItems().remove(mi);
					mtiles.getItems()
					.add(new MenuItemWTile(f.getName(), new ImageView(ImgUtil.resizeImage(t.images.get(0),
							(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 16, 16)), t));
					mtiles.getItems().get(mtiles.getItems().size() - 1).setOnAction(this::contextMenu);
					mtiles.getItems().add(mi);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.out.println(f);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public String getBackgroundPath() { return backgroundPath; }

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

			while (row < data.size()) {

				String line = ((StringValue) data.get(idx)).getValue();
				String[] numbers = line.split(" ");

				if (mapTileNum.size() == row)
					mapTileNum.add(new ArrayList<>());

				while (col < numbers.length) {

					int num = Integer.parseInt(numbers[col]);

					mapTileNum.get(row).add(num);
					col++;
				}
				if (col == numbers.length) {
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
			File out = new File(path).getAbsoluteFile();
			System.out.println(out);
			if (out.exists()) {
				JsonObject jo = (JsonObject) JsonParser.parse(out);
				JsonArray buildings = (JsonArray) jo.get("buildings");
				buildings.clear();
				buildings.addAll(this.buildings.stream().filter(Building::isMaster).toList());
				JsonArray npcs = (JsonArray) jo.get("npcs");
				npcs.clear();
				npcs.addAll(this.npcs);
				JsonObject map = (JsonObject) jo.get("map");
				if (backgroundPath != null)
					map.put("background", backgroundPath);
				JsonArray textures = (JsonArray) map.get("textures");
				textures.clear();
				JsonArray startingPosition = (JsonArray) map.get("startingPosition");
				startingPosition.clear();
				startingPosition.add(this.startingPosition.getKey());
				startingPosition.add(this.startingPosition.getValue());
				jo.put("dir", dir);
				JsonArray matrix = (JsonArray) map.get("matrix");
				matrix.clear();
				for (List<TextureHolder> mapi: this.map) {
					StringBuilder sb = new StringBuilder();
					for (TextureHolder th: mapi) if (textures.contains(th.getTile().name))
						sb.append(textures.indexOf(th.getTile().name) + " ");
					else {
						sb.append(textures.size() + " ");
						textures.add(th.getTile().name);
					}
					matrix.add(sb.toString().substring(0, sb.toString().length() - 1));
				}
				String jsonOut = jo.toJson();
				BufferedWriter bw = new BufferedWriter(new FileWriter(out));
				bw.write(jsonOut);
				bw.flush();
				bw.close();
			} else {

			}
		} catch (JsonParseException | IOException e) {
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
					.parse(new FileInputStream(path));
			JsonObject map = (JsonObject) jo.get("map");
			JsonArray textures = (JsonArray) map.get("textures");
			JsonArray npcs = (JsonArray) jo.get("npcs");
			JsonArray buildings = (JsonArray) jo.get("buildings");
			JsonArray startingPosition = (JsonArray) map.get("startingPosition");

			dir = ((StringValue) map.get("dir")).getValue();

			if (map.containsKey("background")) backgroundPath = ((StringValue) map.get("background")).getValue();

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
				Tile t = new Tile(((StringValue) texture).getValue(),
						new FileInputStream("./res/" + dir + "/" + ((StringValue) texture).getValue()),
						gp);
				tile.add(t);
				mtiles.getItems()
				.add(new MenuItemWTile(((StringValue) texture).getValue(),
						new ImageView(ImgUtil.resizeImage(t.images.get(0),
								(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 16, 16)),
						t));
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				if (new File("./res/collisions/" + dir + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
				+ ".collisionbox").exists())
					try {
						RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/" + dir + "/"
								+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox"), "rws");
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
			this.startingPosition = Map.entry(((NumberValue) startingPosition.get(0)).getValue().doubleValue(),
					((NumberValue) startingPosition.get(1)).getValue().doubleValue());
			mapTileNum = new ArrayList<>();
			this.map = new ArrayList<>();
			loadMap((JsonArray) map.get("matrix"));
			this.buildings = new ArrayList<>();
			this.npcs = new ArrayList<>();
			for (Object building: buildings) {
				switch (((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building, gp, this.buildings, buildingCM, requestorB);
					default -> new Building((JsonObject) building, gp, this.buildings, buildingCM, requestorB);
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
					case "Demon", "demon" -> new Demon((JsonObject) npc, gp, this.npcs, npcCM, requestorN);
					default -> new NPC((JsonObject) npc, gp, this.npcs, npcCM, requestorN);
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
			for (MenuItem mi: mtiles.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi: mnpcs.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi: mbuildings.getItems()) mi.setOnAction(this::contextMenu);

		} catch (JsonParseException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void update() {

		Player p = gp.getPlayer();

		if (exitMap != null) {
			int worldX = (int) (gp.Bg * exitPosition.getKey());
			int worldY = (int) (gp.Bg * exitPosition.getValue());

			if (worldX + gp.Bg / 2 - p.getX() < 105 && worldX + gp.Bg / 2 - p.getX() > -45 &&
					worldY + gp.Bg / 2 - p.getY() < 25 && worldY + gp.Bg / 2 - p.getY() > 0)
				gp.setMap("./res/maps/" + exitMap, exitStartingPosition);
		}

		int worldCol = 0;
		int worldRow = 0;

		while (worldRow < mapTileNum.size() && worldCol < mapTileNum.get(worldRow).size()) {
			int tileNum = mapTileNum.get(worldRow).get(worldCol);

			int worldX = worldCol * gp.Bg;
			int worldY = worldRow * gp.Bg;
			double screenX = worldX - p.getX() + p.screenX;
			double screenY = worldY - p.getY() + p.screenY;

			if (map.size() == worldCol)
				map.add(new ArrayList<>());

			if (worldX + p.size > p.getX() - p.screenX
					&& worldX - p.size < p.getX() + p.screenX
					&& worldY + p.size > p.getY() - p.screenY
					&& worldY - p.size < p.getY() + p.screenY) {
				TextureHolder th = null;
				if (map.get(worldRow).size() > worldCol)
					th = map.get(worldRow).get(worldCol);
				if (th == null) {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY,
							cm,
							requestor);
					group.getChildren().add(th);
					map.get(worldRow).add(th);
				} else {
					th.setLayoutX(screenX);
					th.setLayoutY(screenY);
					th.update();
				}
				th.setVisible(true);
			} else {
				TextureHolder th = null;
				if (map.get(worldRow).size() > worldCol)
					th = map.get(worldRow).get(worldCol);
				if (th != null) {
					th.setVisible(false);
					th.update();
				} else {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), screenX, screenY, cm,
							requestor);
					th.setVisible(false);
					group.getChildren().add(th);
					map.get(worldRow).add(th);
				}
			}

			worldCol++;

			if (worldCol == mapTileNum.size()) {
				worldCol = 0;
				worldRow++;
			}
		}

	}

}
