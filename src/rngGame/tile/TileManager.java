package rngGame.tile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
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
	List<List<Integer>> mapTileNum;
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
	private Double[] startingPosition, exitStartingPosition, exitPosition;

	private int playerLayer;

	private Menu mtiles, mnpcs, mbuildings, mextra;

	public TileManager(SpielPanel gp) {
		cm = new ContextMenu();
		cm.getItems().addAll(mtiles = new Menu("Tiles"), mnpcs = new Menu("NPCs"), mbuildings = new Menu("Buildings"),
				mextra = new Menu("Extras"));
		buildingCM = new ContextMenu();
		npcCM = new ContextMenu();

		MenuItem save = new MenuItem("save");
		save.setOnAction(ae -> gp.saveMap());
		mextra.getItems().add(save);

		MenuItem backToSpawn = new MenuItem("Go back to Spawn");
		backToSpawn.setOnAction(ae -> gp.getPlayer().setPosition(getStartingPosition()));
		mextra.getItems().add(backToSpawn);

		Menu maps = new Menu("Maps");
		mextra.getItems().add(maps);

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
				.getDeclaredConstructor(miwb.getBuilding().getClass(), gp.getClass(), List.class,
						ContextMenu.class, ObjectProperty.class)
				.newInstance(miwb.getBuilding(), gp, buildings, buildingCM, requestorB)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItemWNPC miwn)
				miwn.getNPC().getClass()
				.getDeclaredConstructor(miwn.getNPC().getClass(), gp.getClass(), List.class, ContextMenu.class,
						ObjectProperty.class)
				.newInstance(miwn.getNPC(), gp, npcs, npcCM, requestorN)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItem mi && mi.getText().equals("add Texture")) if (mi.getParentMenu() == mnpcs) {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path p1 = f.toPath();
					Path p2 = new File("./res/npc/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Image img = new Image(new FileInputStream(p2.toFile()));
					JsonObject joN = new JsonObject();
					JsonArray requestedSize = new JsonArray();
					requestedSize.add(new DoubleValue(img.getWidth()));
					requestedSize.add(new DoubleValue(img.getHeight()));
					joN.put("requestedSize", requestedSize);
					JsonObject textures = new JsonObject();
					textures.put("default", new StringValue(f.getName()));
					joN.put("textures", textures);
					JsonObject npcData = new JsonObject();
					joN.put("npcData", npcData);
					joN.put("type", new StringValue("NPC"));
					joN.put("fps", new DoubleValue(7d));
					JsonArray position = new JsonArray();
					position.add(new DoubleValue(
							requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().getY()));
					joN.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joN.put("originalSize", originalSize);

					NPC n = new NPC(joN, gp, npcs, npcCM, requestorN);
					mnpcs.getItems().remove(mi);
					mnpcs.getItems()
					.add(new MenuItemWNPC(f.getName(),
							new ImageView(ImgUtil.resizeImage(n.getImages().get(n.getCurrentKey()).get(0),
									(int) n.getImages().get(n.getCurrentKey()).get(0).getWidth(),
									(int) n.getImages().get(n.getCurrentKey()).get(0).getHeight(), 16, 16)),
							n));
					mnpcs.getItems().get(mnpcs.getItems().size() - 1).setOnAction(this::contextMenu);
					mnpcs.getItems().add(mi);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.out.println(f);
			} else if (mi.getParentMenu() == mbuildings){
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path p1 = f.toPath();
					Path p2 = new File("./res/building/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Image img = new Image(new FileInputStream(p2.toFile()));
					JsonObject joB = new JsonObject();
					JsonArray requestedSize = new JsonArray();
					requestedSize.add(new DoubleValue(img.getWidth()));
					requestedSize.add(new DoubleValue(img.getHeight()));
					joB.put("requestedSize", requestedSize);
					JsonObject textures = new JsonObject();
					textures.put("default", new StringValue(f.getName()));
					joB.put("textures", textures);
					JsonObject buildingData = new JsonObject();
					joB.put("buildingData", buildingData);
					joB.put("type", new StringValue("Building"));
					JsonArray position = new JsonArray();
					position.add(new DoubleValue(
							requestor.get().getLayoutX() - gp.getPlayer().screenX + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requestor.get().getLayoutY() - gp.getPlayer().screenY + gp.getPlayer().getY()));
					joB.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joB.put("originalSize", originalSize);

					Building b = new Building(joB, gp, buildings, buildingCM, requestorB);
					mbuildings.getItems().remove(mi);
					mbuildings.getItems()
					.add(new MenuItemWBuilding(f.getName(),
							new ImageView(ImgUtil.resizeImage(b.getImages().get(b.getCurrentKey()).get(0),
									(int) b.getImages().get(b.getCurrentKey()).get(0).getWidth(),
									(int) b.getImages().get(b.getCurrentKey()).get(0).getHeight(), 16, 16)),
							b));
					mbuildings.getItems().get(mbuildings.getItems().size() - 1).setOnAction(this::contextMenu);
					mbuildings.getItems().add(mi);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.out.println(f);
			} else if (mi.getParentMenu() == mtiles) {
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

	public Double[] getExitPosition() { return exitPosition; }

	public Double[] getExitStartingPosition() { return exitStartingPosition; }

	public List<List<TextureHolder>> getMap() {
		return map;
	}

	public MenuItem[] getMenus() {
		((Menu) mextra.getItems().get(mextra.getItems().size() - 1)).getItems().clear();
		for (File f: new File("./res/maps").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[] sp = f.getName().split("[.]");
			MenuItem map = new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setOnAction(ae -> gp.setMap("./res/maps/" + map.getText() + ".json"));
			((Menu) mextra.getItems().get(mextra.getItems().size() - 1)).getItems().add(map);
		}
		return new MenuItem[] {mtiles, mnpcs, mbuildings, mextra};
	}

	public ContextMenu getNpcCM() { return npcCM; }

	public List<NPC> getNPCSFromMap() { return npcs; }

	public Node getObjectAt(double x, double y) {
		List<Node> nodes = gp.getViewGroups().stream().map(v -> v.getChildren()
				.filtered(n -> n.contains(x - ((GameObject) n).getX(), y - ((GameObject) n).getY()) && n.isVisible()))
				.flatMap(FilteredList::stream).toList();
		if (nodes.size() != 0) return nodes.get(nodes.size() - 1);
		else
			return map.get((int) Math.floor(y / gp.Bg)).get((int) Math.floor(x / gp.Bg));
	}

	public int getPlayerLayer() { return playerLayer; }

	public ObjectProperty<? extends Entity> getRequestorN() { return requestorN; }

	public Double[] getStartingPosition() { return startingPosition; }

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

	public void reload() {
		setMap(path);
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
				npcs.addAll(this.npcs.stream().filter(Entity::isMaster).toList());
				JsonObject map = (JsonObject) jo.get("map");
				for (int i = 0; i < gp.getViewGroups().size(); i++) {
					Group v = gp.getViewGroups().get(i);
					if (v.getChildren().contains(gp.getPlayer()))
						map.put("playerLayer", i);
				}
				if (backgroundPath != null)
					map.put("background", backgroundPath);
				JsonArray textures = (JsonArray) map.get("textures");
				textures.clear();
				JsonArray startingPosition = (JsonArray) map.get("startingPosition");
				startingPosition.clear();
				startingPosition.add(this.startingPosition[0]);
				startingPosition.add(this.startingPosition[1]);
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

			if (jo.containsKey("generated")) {

				if (jo.containsKey("background")) backgroundPath = ((StringValue) jo.get("background")).getValue();
				else backgroundPath = null;

				JsonObject mainmap = (JsonObject) JsonParser
						.parse(new FileInputStream("./res/maps/" + ((StringValue) jo.get("mainmap")).getValue()));

				JsonArray ja_maps = (JsonArray) jo.get("maps");

				JsonObject[] maps = new JsonObject[4];

				Random r = new Random();
				for (int i = 0; i < maps.length; i++) maps[i] = (JsonObject) JsonParser.parse(new FileInputStream(
						"./res/maps/" + ((StringValue) ja_maps.remove(r.nextInt(ja_maps.size()))).getValue()));

				DungeonGen d = new DungeonGen(gp, mainmap, maps,
						((JsonArray) jo.get("connectors")).stream().map(sv -> ((StringValue) sv).getValue()).toList(),
						((JsonArray) jo.get("connections")).stream().map(sv -> ((StringValue) sv).getValue()).toList(),
						((JsonArray) jo.get("replacments")).stream().map(sv -> ((StringValue) sv).getValue()).toList());

				d.findConnectors();// TODO fiax that it expects List<String> insted of JsonObject's

				return;
			}

			JsonObject map = (JsonObject) jo.get("map");
			JsonArray textures = (JsonArray) map.get("textures");
			JsonArray npcs = (JsonArray) jo.get("npcs");
			JsonArray buildings = (JsonArray) jo.get("buildings");
			JsonArray startingPosition = (JsonArray) map.get("startingPosition");

			dir = ((StringValue) map.get("dir")).getValue();

			if (map.containsKey("background")) backgroundPath = ((StringValue) map.get("background")).getValue();
			else backgroundPath = null;

			if (jo.containsKey("exit")) {
				JsonObject exit = (JsonObject) jo.get("exit");
				exitMap = ((StringValue) exit.get("map")).getValue();
				JsonArray spawnPosition = (JsonArray) exit.get("spawnPosition");
				JsonArray position = (JsonArray) exit.get("position");
				exitStartingPosition = new Double[] {
						((NumberValue) spawnPosition.get(0)).getValue().doubleValue(),
						((NumberValue) spawnPosition.get(1)).getValue().doubleValue()
				};
				exitPosition = new Double[] {
						((NumberValue) position.get(0)).getValue().doubleValue(),
						((NumberValue) position.get(1)).getValue().doubleValue()
				};
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
			this.startingPosition = new Double[] {
					((NumberValue) startingPosition.get(0)).getValue().doubleValue(),
					((NumberValue) startingPosition.get(1)).getValue().doubleValue()
			};
			if (map.containsKey("playerLayer"))
				playerLayer = ((NumberValue) map.get("playerLayer")).getValue().intValue();
			else playerLayer = 0;
			mapTileNum = new ArrayList<>();
			this.map = new ArrayList<>();
			loadMap((JsonArray) map.get("matrix"));
			this.buildings = new ArrayList<>();
			this.npcs = new ArrayList<>();
			for (Object building: buildings) {
				Building b = switch (((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building, gp, this.buildings, buildingCM, requestorB);
					default -> new Building((JsonObject) building, gp, this.buildings, buildingCM, requestorB);
				};
				mbuildings.getItems().add(new MenuItemWBuilding(
						((StringValue) ((JsonObject) ((JsonObject) building).get("textures")).values().stream()
								.findFirst().get()).getValue(),
						new ImageView(ImgUtil.resizeImage(b.getFirstImage(), (int) b.getFirstImage().getWidth(),
								(int) b.getFirstImage().getHeight(), 16, 16)),
						b));
			}
			for (Object npc: npcs) {
				NPC n = switch (((StringValue) ((JsonObject) npc).get("type")).getValue()) {
					case "Demon", "demon" -> new Demon((JsonObject) npc, gp, this.npcs, npcCM, requestorN);
					default -> new NPC((JsonObject) npc, gp, this.npcs, npcCM, requestorN);
				};
				mnpcs.getItems()
				.add(new MenuItemWNPC(
						((StringValue) ((JsonObject) ((JsonObject) npc).get("textures")).values().stream()
								.findFirst().get()).getValue(),
						new ImageView(ImgUtil.resizeImage(n.getFirstImage(), (int) n.getFirstImage().getWidth(),
								(int) n.getFirstImage().getHeight(), 16, 16)),
						n));
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
			int worldX = (int) (gp.Bg * exitPosition[0]);
			int worldY = (int) (gp.Bg * exitPosition[1]);

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

			if (worldX + p.size * 1.5 > p.getX() - p.screenX
					&& worldX - p.size * 1.5 < p.getX() + p.screenX
					&& worldY + p.size > p.getY() - p.screenY
					&& worldY - p.size < p.getY() + p.screenY) {
				TextureHolder th = null;
				if (map.get(worldRow).size() > worldCol)
					th = map.get(worldRow).get(worldCol);
				if (th == null) {
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), gp, screenX, screenY,
							cm, requestor, worldX, worldY);
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
					th = new TextureHolder(tile.get(tileNum < tile.size() ? tileNum : 0), gp, screenX, screenY, cm,
							requestor, worldX, worldY);
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
