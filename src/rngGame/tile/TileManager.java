package rngGame.tile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.*;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
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
import rngGame.main.UndoRedo.UndoRedoActionBase;

public class TileManager extends Pane {

	private class FakeTextureHolder extends TextureHolder {

		public FakeTextureHolder(double x, double y) {
			super(new Tile("", new ByteArrayInputStream(new byte[0]), null) {
				{
					images.add(new Image(new ByteArrayInputStream(new byte[0])));
				}
			}, null, x, y, null, null, 0, 0);
			setWidth(gp.BgX);
			setHeight(gp.BgY);
		}

		@SuppressWarnings("unused")
		@Override
		public void setTile(Tile tile) {
			// TODO add row/column
			for (List<TextureHolder> x: map) {

			}
		}

	}

	private String path;
	private final GamePanel gp;

	private final List<Tile> tiles;
	List<List<Integer>> mapTileNum;
	private boolean generated;
	private List<List<TextureHolder>> map;
	private final Menu maps, insel_k, insel_m, insel_g;

	private final Group group;
	private List<Building> buildings;
	private List<NPC> npcs;
	private String exitMap, dir, backgroundPath;
	private final ContextMenu cm;
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

	private final ObjectProperty<MobRan> requestorM = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requestor"; }
	};

	private double[] startingPosition, exitStartingPosition, exitPosition;
	private int playerLayer;
	private final Menu mtiles, mnpcs, mbuildings, mextra, mmobs;
	private List<MobRan> mobs;

	public TileManager(GamePanel gp) {
		cm = new ContextMenu();
		mtiles = new Menu("Tiles");
		mnpcs = new Menu("NPCs");
		mbuildings = new Menu("Buildings");
		mextra = new Menu("Extras");
		mmobs = new Menu("Mob Test");
		MenuItem save = new MenuItem("save");
		save.setOnAction(ae -> gp.saveMap());
		mextra.getItems().add(save);

		MenuItem backToSpawn = new MenuItem("Go back to Spawn");
		backToSpawn.setOnAction(ae -> {
			double[] posi = getStartingPosition();
			gp.getPlayer()
			.setPosition(new double[] {posi[0] * gp.getScalingFactorX(), posi[1] * gp.getScalingFactorY()});
		});
		mextra.getItems().add(backToSpawn);

		maps = new Menu("Maps");
		insel_k = new Menu("Insel_K");
		insel_m = new Menu("Insel_M");
		insel_g = new Menu("Insel_G");
		mextra.getItems().addAll(maps, insel_k, insel_m, insel_g);

		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true") && !cm.isShowing()) {
				System.out.println("dg");
				System.out.println();
				requestor.set(new FakeTextureHolder(e.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						e.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
				cm.getItems().clear();
				cm.getItems().addAll(gp.getTileM().getMenus());
				cm.show(this, e.getScreenX(), e.getScreenY());
			}
		});

		this.gp = gp;

		tiles = new ArrayList<>();
		map = new ArrayList<>();
		mapTileNum = new ArrayList<>();

		npcs = new ArrayList<>();
		buildings = new ArrayList<>();
		mobs = new ArrayList<>();

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
			if (requestor.get() instanceof FakeTextureHolder fth) {
				fth.setLayoutX(fth.getLayoutX() + gp.getPlayer().getScreenX() - gp.getPlayer().getX());
				fth.setLayoutY(fth.getLayoutY() + gp.getPlayer().getScreenY() - gp.getPlayer().getY());
			}
			if (e.getSource() instanceof MenuItemWTile miwt) {
				Tile t = requestor.get().getTile();
				TextureHolder th = requestor.get();
				UndoRedo.getInstance().addAction(new UndoRedoActionBase(() -> {
					th.setTile(t);
				}, () -> {
					th.setTile(miwt.getTile());
				}));
			} else if (e.getSource() instanceof MenuItemWBuilding miwb)
				miwb.getBuilding().getClass()
				.getDeclaredConstructor(miwb.getBuilding().getClass(), List.class,
						ContextMenu.class, ObjectProperty.class)
				.newInstance(miwb.getBuilding(), buildings, cm, requestorB)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItemWNPC miwn)
				miwn.getNPC().getClass()
				.getDeclaredConstructor(miwn.getNPC().getClass(), List.class, ContextMenu.class,
						ObjectProperty.class)
				.newInstance(miwn.getNPC(), npcs, cm, requestorN)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItemWMOB miwn)
				miwn.getMob().getClass()
				.getDeclaredConstructor(miwn.getMob().getClass(), List.class, ContextMenu.class,
						ObjectProperty.class)
				.newInstance(miwn.getMob(), mobs, cm, requestorN)
				.setPosition(requestor.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						requestor.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
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
							requestor.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requestor.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
					joN.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joN.put("originalSize", originalSize);

					NPC n = new NPC(joN, gp, npcs, cm, requestorN);
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
							requestor.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requestor.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
					joB.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joB.put("originalSize", originalSize);

					Building b = new Building(joB, gp, buildings, cm, requestorB);
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
			}else if(mi.getParentMenu() == mmobs) {
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
							requestor.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requestor.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
					joN.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joN.put("originalSize", originalSize);

					MobRan n = new MobRan(joN, gp, mobs, cm, requestorM);
					mmobs.getItems().remove(mi);
					mmobs.getItems()
					.add(new MenuItemWMOB(f.getName(),
							new ImageView(ImgUtil.resizeImage(n.getImages().get(n.getCurrentKey()).get(0),
									(int) n.getImages().get(n.getCurrentKey()).get(0).getWidth(),
									(int) n.getImages().get(n.getCurrentKey()).get(0).getHeight(), 16, 16)),
							n));
					mmobs.getItems().get(mmobs.getItems().size() - 1).setOnAction(this::contextMenu);
					mmobs.getItems().add(mi);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.out.println(f);
			} else if (mi.getParentMenu() == mtiles) {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png"));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.gif"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path p1 = f.toPath();
					Path p2 = new File("./res/" + getDir() + "/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Tile t = new Tile(f.getName(),
							new FileInputStream("./res/" + getDir() + "/" + f.getName()),
							gp);
					tiles.add(t);
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


	public ContextMenu getCM() { return cm; }

	public String getDir() {
		return dir;
	}

	public String getExitMap() { return exitMap; }

	public double[] getExitPosition() { return exitPosition; }

	public double[] getExitStartingPosition() { return exitStartingPosition; }

	public List<List<TextureHolder>> getMap() {
		return map;
	}

	public Menu[] getMenus() {
		maps.getItems().clear();
		insel_k.getItems().clear();
		insel_m.getItems().clear();
		insel_g.getItems().clear();
		for (File f: new File("./res/maps").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[] sp = f.getName().split("[.]");
			MenuItem map = new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setOnAction(ae -> gp.setMap("./res/maps/" + map.getText() + ".json"));
			maps.getItems().add(map);
		}
		for (File f: new File("./res/maps/insel_k").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[] sp = f.getName().split("[.]");
			MenuItem map = new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setOnAction(ae -> gp.setMap("./res/maps/insel_k/" + map.getText() + ".json"));
			insel_k.getItems().add(map);
		}
		for (File f: new File("./res/maps/insel_m").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[] sp = f.getName().split("[.]");
			MenuItem map = new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setOnAction(ae -> gp.setMap("./res/maps/insel_m/" + map.getText() + ".json"));
			insel_m.getItems().add(map);
		}
		for (File f: new File("./res/maps/insel_g").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[] sp = f.getName().split("[.]");
			MenuItem map = new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setOnAction(ae -> gp.setMap("./res/maps/insel_g/" + map.getText() + ".json"));
			insel_g.getItems().add(map);
		}
		return new Menu[] {mtiles, mnpcs, mbuildings, mmobs, mextra};
	}

	public List<MobRan> getMobFromMap() {
		return mobs;
	}

	public List<NPC> getNPCSFromMap() { return npcs; }

	public Node getObjectAt(double x, double y) {
		List<Node> nodes = gp.getViewGroups().stream().map(v -> v.getChildren()
				.filtered(n -> n.contains(x - ((GameObject) n).getX(), y - ((GameObject) n).getY()) && n.isVisible()))
				.flatMap(FilteredList::stream).toList();
		if (nodes.size() != 0) return nodes.get(nodes.size() - 1);
		else if (x < 0 || y < 0) return null;
		else
			return getTileAt(x, y);
	}

	public List<List<TextureHolder>> getPartOfMap(double layoutX, double layoutY, double width, double height) {
		int lx, ly, w, h;
		lx = (int) Math.floor((layoutX-gp.getPlayer().getScreenX()+gp.getPlayer().getX()) / gp.BgX);
		ly = (int) Math.floor((layoutY-gp.getPlayer().getScreenY()+gp.getPlayer().getY()) / gp.BgY);
		w = (int) Math.floor(width / gp.BgX);
		h = (int) Math.floor(height / gp.BgY);

		List<List<TextureHolder>> li = new ArrayList<>();

		for (int i = 0; i < h; i++) {
			li.add(new ArrayList<>());
			for (int j = 0; j < w; j++) li.get(i).add(map.get(ly + i).get(lx + j));
		}
		return li;
	}

	public String getPath() { return path; }

	public int getPlayerLayer() { return playerLayer; }

	public ObjectProperty<? extends Entity> getRequestorN() { return requestorN; }

	public Point2D getSpawnPoint() { return new Point2D(startingPosition[0], startingPosition[1]); }

	public double[] getStartingPosition() { return startingPosition; }

	public TextureHolder getTileAt(double x, double y) {
		int tx = (int) Math.floor(x / gp.BgX);
		int ty = (int) Math.floor(y / gp.BgY);
		if (x < 0) tx--;
		if (y < 0) ty--;
		try {
			return map.get(ty).get(tx);
		} catch (IndexOutOfBoundsException e) {
			return new FakeTextureHolder(tx * gp.BgX - gp.getPlayer().getX() + gp.getPlayer().getScreenX(),
					ty * gp.BgY - gp.getPlayer().getY() + gp.getPlayer().getScreenY());
		}
	}

	public List<Tile> getTiles() { return tiles; }

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
		if (!generated) try {
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
				npcs.addAll(mobs.stream().filter(Entity::isMaster).toList());
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
				map.put("dir", getDir());
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
			startingPosition = new double[] {0d, 0d};

			mapTileNum.clear();
			map.clear();

			group.getChildren().clear();
			tiles.clear();
			npcs.clear();
			mobs.clear();
			buildings.clear();
			mtiles.getItems().clear();
			mnpcs.getItems().clear();
			mmobs.getItems().clear();
			mbuildings.getItems().clear();
			mmobs.getItems().clear();
			playerLayer = 0;
			JsonObject jo = (JsonObject) JsonParser
					.parse(new FileInputStream(path));

			if (jo.containsKey("generated") && jo.get("generated") instanceof BoolValue bv && bv.getValue()) {

				generated = true;

				if (jo.containsKey("background")) backgroundPath = ((StringValue) jo.get("background")).getValue();
				else backgroundPath = null;

				JsonObject mainmap = (JsonObject) JsonParser
						.parse(new FileInputStream("./res/maps/" + ((StringValue) jo.get("mainmap")).getValue()));

				JsonArray ja_maps = (JsonArray) jo.get("maps");

				String voidImg = ((StringValue) jo.get("void")).getValue();

				DungeonGen d = new DungeonGen(gp, voidImg, mainmap, ja_maps,
						((JsonArray) jo.get("connectors")).stream().map(jOb -> (JsonObject) jOb).toList(),
						((JsonArray) jo.get("connections")).stream().map(jOb -> (JsonObject) jOb).toList(),
						((JsonArray) jo.get("replacments")).stream().map(jOb -> (JsonObject) jOb).toList());

				d.findFreeConnectors();

				d.stitchMaps();

				return;
			}

			generated = false;

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
				exitStartingPosition = new double[] {
						((NumberValue) spawnPosition.get(0)).getValue().doubleValue(),
						((NumberValue) spawnPosition.get(1)).getValue().doubleValue()
				};
				exitPosition = new double[] {
						((NumberValue) position.get(0)).getValue().doubleValue(),
						((NumberValue) position.get(1)).getValue().doubleValue()
				};
			}
			for (Object texture: textures) try {
				Tile t = new Tile(((StringValue) texture).getValue(),
						new FileInputStream("./res/" + getDir() + "/" + ((StringValue) texture).getValue()),
						gp);
				tiles.add(t);
				mtiles.getItems()
				.add(new MenuItemWTile(((StringValue) texture).getValue(),
						new ImageView(ImgUtil.resizeImage(t.images.get(0),
								(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 16, 16)),
						t));
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				if (new File("./res/collisions/" + getDir() + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
				+ ".collisionbox").exists())
					try {
						RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/" + getDir() + "/"
								+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
								+ ".collisionbox"), "rws");
						raf.seek(0l);
						int length = raf.readInt();
						t.poly = new ArrayList<>();
						boolean s = false;
						for (int i = 0; i < length; i++)
							t.poly.add(raf.readDouble() * ((s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (NullPointerException e) {
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				new IOException(getDir() + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1)), e)
				.printStackTrace();
			}
			this.startingPosition = new double[] {
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
			mobs = new ArrayList<>();
			for (Object building: buildings) {
				Building b = switch (((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building, gp, this.buildings, cm, requestorB);
					case "ContractsTable" -> new ContractsTable((JsonObject) building, gp, this.buildings, cm,
							requestorB);
					default -> new Building((JsonObject) building, gp, this.buildings, cm, requestorB);
				};
				mbuildings.getItems().add(new MenuItemWBuilding(
						((StringValue) ((JsonObject) ((JsonObject) building).get("textures")).values().stream()
								.findFirst().get()).getValue(),
						new ImageView(ImgUtil.resizeImage(b.getFirstImage(), (int) b.getFirstImage().getWidth(),
								(int) b.getFirstImage().getHeight(), 16, 16)),
						b));
			}
			for (Object npc: npcs) {
				Entity n = switch (((StringValue) ((JsonObject) npc).get("type")).getValue()) {
					case "Demon", "demon" -> new Demon((JsonObject) npc, gp, this.npcs, cm, requestorN);
					case "MobRan", "mobran" -> new MobRan((JsonObject) npc, gp, mobs, cm, requestorM);
					default -> new NPC((JsonObject) npc, gp, this.npcs, cm, requestorN);
				};
				if (n instanceof NPC np)
					mnpcs.getItems()
					.add(new MenuItemWNPC(
							((StringValue) ((JsonObject) ((JsonObject) npc).get("textures")).values().stream()
									.findFirst().get()).getValue(),
							new ImageView(
									ImgUtil.resizeImage(np.getFirstImage(), (int) n.getFirstImage().getWidth(),
											(int) np.getFirstImage().getHeight(), 16, 16)),
							np));
				else if (n instanceof MobRan mr)
					mmobs.getItems()
					.add(new MenuItemWMOB(
							((StringValue) ((JsonObject) ((JsonObject) npc).get("textures")).values().stream()
									.findFirst().get()).getValue(),
							new ImageView(
									ImgUtil.resizeImage(mr.getFirstImage(), (int) n.getFirstImage().getWidth(),
											(int) mr.getFirstImage().getHeight(), 16, 16)),
							mr));
			}
			mtiles.getItems().add(new MenuItem("add Texture"));
			mnpcs.getItems().add(new MenuItem("add Texture"));
			mmobs.getItems().add(new MenuItem("add Texture"));
			mbuildings.getItems().add(new MenuItem("add Texture"));
			for (MenuItem mi: mtiles.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi: mnpcs.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi: mbuildings.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi: mmobs.getItems()) mi.setOnAction(this::contextMenu);

		} catch (JsonParseException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void update() {

		Player p = gp.getPlayer();

		if (exitMap != null) {
			int worldX = (int) (exitPosition[0] * gp.getScalingFactorX());
			int worldY = (int) (exitPosition[1] * gp.getScalingFactorY());

			if (worldX + gp.BgX / 2 - p.getX() < 105 * gp.getScalingFactorX()
					&& worldX + gp.BgX / 2 - p.getX() > -45 * gp.getScalingFactorX() &&
					worldY + gp.BgY / 2 - p.getY() < 25 * gp.getScalingFactorY() && worldY + gp.BgY / 2 - p.getY() > 0)
				gp.setMap("./res/maps/" + exitMap, exitStartingPosition);
		}

		int worldCol = 0;
		int worldRow = 0;

		while (worldRow < mapTileNum.size()) {
			int tileNum = mapTileNum.get(worldRow).get(worldCol);

			int worldX = worldCol * gp.BgX;
			int worldY = worldRow * gp.BgY;
			double screenX = worldX - p.getX() + p.getScreenX();
			double screenY = worldY - p.getY() + p.getScreenY();

			if (map.size() == worldRow)
				map.add(new ArrayList<>());

			if (worldX + p.getSize() * 1.5 > p.getX() - p.getScreenX() && worldX - gp.BgX -
					p.getSize() * 1.5 < p.getX() + p.getScreenX()
					&& worldY + gp.BgY + p.getSize() > p.getY() - p.getScreenY()
					&& worldY - gp.BgY - p.getSize() < p.getY() + p.getScreenY()) {
				TextureHolder th = null;
				if (map.get(worldRow).size() > worldCol)
					th = map.get(worldRow).get(worldCol);
				if (th == null) {
					th = new TextureHolder(tiles.get(tileNum < tiles.size() ? tileNum : 0), gp, screenX, screenY,
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
					th = new TextureHolder(tiles.get(tileNum < tiles.size() ? tileNum : 0), gp, screenX, screenY, cm,
							requestor, worldX, worldY);
					th.setVisible(false);
					group.getChildren().add(th);
					map.get(worldRow).add(th);
				}
			}

			worldCol++;

			if (worldCol == mapTileNum.get(worldRow).size()) {
				worldCol = 0;
				worldRow++;
			}
		}

	}

}
