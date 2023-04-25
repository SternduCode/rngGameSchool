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
import rngGame.visual.GamePanel;


// TODO: Auto-generated Javadoc
/**
 * The Class TileManager.
 */
public class TileManager extends Pane {

	/**
	 * The Class FakeTextureHolder for making tiles outside of the map.
	 */
	private class FakeTextureHolder extends TextureHolder {

		/**
		 * Instantiates a new fake texture holder.
		 *
		 * @param x the x coordinate
		 * @param y the y coordinate
		 */
		public FakeTextureHolder(double x, double y) {
			super(new Tile("", new ByteArrayInputStream(new byte[0]), null) {

				{
					images.add(new Image(new ByteArrayInputStream(new byte[0])));
				}

			}, null, x, y, null, null, 0, 0);
			setWidth(gp.getBlockSizeX());
			setHeight(gp.getBlockSizeY());

		}

		/**
		 * Sets the tile.
		 *
		 * @param tile the new tile
		 */
		@SuppressWarnings("unused")
		@Override
		public void setTile(Tile tile) {
			// TODO add row/column
			for (List<TextureHolder> x : map) {

			}

		}

	}

	/** The path to the map file. */
	private String path;

	/** The GamePanel. */
	private final GamePanel gp;

	/** The tiles. */
	private final List<Tile> tiles;

	/** The map tile numbers. */
	List<List<Integer>> mapTileNum;

	/** Is generated?. */
	private boolean generated;

	/** The map. */
	private List<List<TextureHolder>> map;

	/** The menus maps, insel_k, insel_m and insel_g. */
	private final Menu maps, insel_k, insel_m, insel_g;

	/** The ui group for the map. */
	private final Group group;

	/** The buildings. */
	private List<Building> buildings;

	/** The npcs. */
	private List<NPC> npcs;

	/** The background path, the path to the folder containing the tile textures and the path to the exit map. */
	private String exitMap, dir, backgroundPath;

	/** The context menu. */
	private final ContextMenu cm;

	/** The requester for the context menu containing the TextureHolder. */
	private final ObjectProperty<TextureHolder> requester = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requester"; }

	};

	/** The requester for the context menu containing the Building. */
	private final ObjectProperty<Building> requesterB = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requester"; }

	};

	/** The requester for the context menu containing the NPC. */
	private final ObjectProperty<NPC> requestorN = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requester"; }

	};

	/** The requester for the context menu containing the MobRan. */
	private final ObjectProperty<MobRan> requestorM = new ObjectPropertyBase<>() {

		@Override
		public Object getBean() { return this; }

		@Override
		public String getName() { return "requester"; }

	};

	/** The exit position, the starting position and the position one starts at when using the exit. */
	private double[] startingPosition, exitStartingPosition, exitPosition;

	/** The player layer. */
	private int playerLayer;

	/** The menus for tiles, npcs, buildings, mobs and extras. */
	private final Menu mtiles, mnpcs, mbuildings, mextra, mmobs;

	/** The mobs. */
	private List<MobRan> mobs;

	/** The background music. */
	private String backgroundMusic;

	/** The overlay. */
	private String overlay;

	/**
	 * Instantiates a new tile manager.
	 *
	 * @param gp the GamePanel
	 */
	public TileManager(GamePanel gp) {
		cm			= new ContextMenu();
		mtiles		= new Menu("Tiles");
		mnpcs		= new Menu("NPCs");
		mbuildings	= new Menu("Buildings");
		mextra		= new Menu("Extras");
		mmobs		= new Menu("Mob Test");
		mtiles.setStyle("-fx-font-size: 20;");
		mnpcs.setStyle("-fx-font-size: 20;");
		mbuildings.setStyle("-fx-font-size: 20;");
		mextra.setStyle("-fx-font-size: 20;");
		mmobs.setStyle("-fx-font-size: 20;");
		MenuItem save = new MenuItem("save");
		save.setStyle("-fx-font-size: 20;");
		save.setOnAction(ae -> gp.getLgp().saveMap());
		mextra.getItems().add(save);

		MenuItem backToSpawn = new MenuItem("Go back to Spawn");
		backToSpawn.setStyle("-fx-font-size: 20;");
		backToSpawn.setOnAction(ae -> {
			double[] posi = getStartingPosition();
			gp.getPlayer()
			.setPosition(new double[] {
					posi[0] * gp.getScalingFactorX(), posi[1] * gp.getScalingFactorY()
			});
		});
		mextra.getItems().add(backToSpawn);

		maps	= new Menu("Maps");
		insel_k	= new Menu("Insel_K");
		insel_m	= new Menu("Insel_M");
		insel_g	= new Menu("Insel_G");
		maps.setStyle("-fx-font-size: 20;");
		insel_k.setStyle("-fx-font-size: 20;");
		insel_m.setStyle("-fx-font-size: 20;");
		insel_g.setStyle("-fx-font-size: 20;");
		mextra.getItems().addAll(maps, insel_k, insel_m, insel_g);

		setOnContextMenuRequested(e -> {
			if ("true".equals(System.getProperty("edit")) && !cm.isShowing()) {
				System.out.println("dg");
				System.out.println();
				requester.set(new FakeTextureHolder(e.getSceneX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						e.getSceneY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
				cm.getItems().clear();
				cm.getItems().addAll(gp.getTileManager().getMenus());
				cm.show(this, e.getScreenX(), e.getScreenY());
			}
		});

		this.gp = gp;

		tiles		= new ArrayList<>();
		map			= new ArrayList<>();
		mapTileNum	= new ArrayList<>();

		npcs		= new ArrayList<>();
		buildings	= new ArrayList<>();
		mobs		= new ArrayList<>();

		group = new Group();
		getChildren().add(group);

	}

	/**
	 * Collides?.
	 *
	 * @param collidable the collidable
	 *
	 * @return true, if collides
	 */
	public boolean collides(GameObject collidable) {

		List<TextureHolder>	ths	= new ArrayList<>();

		int x = (int) collidable.getX() / gp.getBlockSizeX(), y = (int) collidable.getY() / gp.getBlockSizeY();

		for (int i = -2; i < 3; i++) for (int j = -2; j < 3; j++)
			if (x + i >= 0 && y + j >= 0 && y + j < map.size() && x + i < map.get(y + j).size()) ths.add(map.get(y + j).get(x + i));

		for (TextureHolder th : ths)
			if ( th.getPoly().getPoints().size() > 0) {
				Shape intersect = Shape.intersect(collidable.getCollisionBox(), th.getPoly());
				if (!intersect.getBoundsInLocal().isEmpty()) return true;
			}

		return false;

	}

	/**
	 * handles the context menus.
	 *
	 * @param e the event
	 */
	public void contextMenu(ActionEvent e) {
		try {
			if (requester.get() instanceof FakeTextureHolder fth) {
				fth.setLayoutX(fth.getLayoutX() + gp.getPlayer().getScreenX() - gp.getPlayer().getX());
				fth.setLayoutY(fth.getLayoutY() + gp.getPlayer().getScreenY() - gp.getPlayer().getY());
			}
			if (e.getSource() instanceof MenuItemWTile miwt) {
				Tile			t	= requester.get().getTile();
				TextureHolder	th	= requester.get();
				UndoRedo.getInstance().addAction(new UndoRedoActionBase(() -> {
					th.setTile(t);
				}, () -> {
					th.setTile(miwt.getTile());
				}));
			} else if (e.getSource() instanceof MenuItemWBuilding miwb)
				miwb.getBuilding().getClass()
				.getDeclaredConstructor(miwb.getBuilding().getClass(), List.class,
						ContextMenu.class, ObjectProperty.class)
				.newInstance(miwb.getBuilding(), buildings, cm, requesterB)
				.setPosition(requester.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						requester.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItemWNPC miwn)
				miwn.getNPC().getClass()
				.getDeclaredConstructor(miwn.getNPC().getClass(), List.class, ContextMenu.class,
						ObjectProperty.class)
				.newInstance(miwn.getNPC(), npcs, cm, requestorN)
				.setPosition(requester.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						requester.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItemWMOB miwn)
				miwn.getMob().getClass()
				.getDeclaredConstructor(miwn.getMob().getClass(), List.class, ContextMenu.class,
						ObjectProperty.class)
				.newInstance(miwn.getMob(), mobs, cm, requestorN)
				.setPosition(requester.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX(),
						requester.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY());
			else if (e.getSource() instanceof MenuItem mi && "add Texture".equals(mi.getText())) if (mi.getParentMenu() == mnpcs) {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png", "*.gif"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path	p1	= f.toPath();
					Path	p2	= new File("./res/npc/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Image		img				= new Image(new FileInputStream(p2.toFile()));
					JsonObject	joN				= new JsonObject();
					JsonArray	requestedSize	= new JsonArray();
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
							requester.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requester.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
					joN.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joN.put("originalSize", originalSize);

					NPC n = new NPC(joN, gp, npcs, cm, requestorN);
					mnpcs.getItems().remove(mi);
					ImageView lIV;
					if (n.isGif(n.getCurrentKey())) {
						lIV = new ImageView(n.getImages().get(n.getCurrentKey()).get(0));
						lIV.setFitWidth(16);
						lIV.setFitHeight(16);
					} else lIV = new ImageView(ImgUtil.resizeImage(n.getImages().get(n.getCurrentKey()).get(0),
							(int) n.getImages().get(n.getCurrentKey()).get(0).getWidth(),
							(int) n.getImages().get(n.getCurrentKey()).get(0).getHeight(), 16, 16));
					mnpcs.getItems()
					.add(new MenuItemWNPC(f.getName(),
							lIV,
							n));
					mnpcs.getItems().get(mnpcs.getItems().size() - 1).setOnAction(this::contextMenu);
					mnpcs.getItems().add(mi);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.out.println(f);
			} else if (mi.getParentMenu() == getMbuildings()) {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png", "*.gif"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path	p1	= f.toPath();
					Path	p2	= new File("./res/building/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Image		img				= new Image(new FileInputStream(p2.toFile()));
					JsonObject	joB				= new JsonObject();
					JsonArray	requestedSize	= new JsonArray();
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
							requester.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requester.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
					joB.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joB.put("originalSize", originalSize);

					Building b = new Building(joB, gp, buildings, cm, requesterB);
					getMbuildings().getItems().remove(mi);
					ImageView lIV;
					if (b.isGif(b.getCurrentKey())) {
						lIV = new ImageView(b.getImages().get(b.getCurrentKey()).get(0));
						lIV.setFitWidth(16);
						lIV.setFitHeight(16);
					} else lIV = new ImageView(ImgUtil.resizeImage(b.getImages().get(b.getCurrentKey()).get(0),
							(int) b.getImages().get(b.getCurrentKey()).get(0).getWidth(),
							(int) b.getImages().get(b.getCurrentKey()).get(0).getHeight(), 16, 16));
					getMbuildings().getItems()
					.add(new MenuItemWBuilding(f.getName(),
							lIV,
							b));
					getMbuildings().getItems().get(getMbuildings().getItems().size() - 1).setOnAction(this::contextMenu);
					getMbuildings().getItems().add(mi);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				System.out.println(f);
			} else if (mi.getParentMenu() == mmobs) {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png", "*.gif"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path	p1	= f.toPath();
					Path	p2	= new File("./res/npc/" + f.getName()).toPath();
					Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(p2);
					Image		img				= new Image(new FileInputStream(p2.toFile()));
					JsonObject	joN				= new JsonObject();
					JsonArray	requestedSize	= new JsonArray();
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
							requester.get().getLayoutX() - gp.getPlayer().getScreenX() + gp.getPlayer().getX()));
					position.add(new DoubleValue(
							requester.get().getLayoutY() - gp.getPlayer().getScreenY() + gp.getPlayer().getY()));
					joN.put("position", position);
					JsonArray originalSize = new JsonArray();
					originalSize.add(new DoubleValue(img.getWidth()));
					originalSize.add(new DoubleValue(img.getHeight()));
					joN.put("originalSize", originalSize);

					MobRan n = new MobRan(joN, gp, mobs, cm, requestorM);
					mmobs.getItems().remove(mi);
					ImageView lIV;
					if (n.isGif(n.getCurrentKey())) {
						lIV = new ImageView(n.getImages().get(n.getCurrentKey()).get(0));
						lIV.setFitWidth(16);
						lIV.setFitHeight(16);
					} else lIV = new ImageView(ImgUtil.resizeImage(n.getImages().get(n.getCurrentKey()).get(0),
							(int) n.getImages().get(n.getCurrentKey()).get(0).getWidth(),
							(int) n.getImages().get(n.getCurrentKey()).get(0).getHeight(), 16, 16));
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
						"A file containing an Image", "*.png", "*.gif"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path	p1	= f.toPath();
					Path	p2	= new File("./res/" + getDir() + "/" + f.getName()).toPath();
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

	/**
	 * Gets the background music.
	 *
	 * @return the background music
	 */
	public String getBackgroundMusic() {
		return backgroundMusic;
	}

	/**
	 * Gets the background path.
	 *
	 * @return the background path
	 */
	public String getBackgroundPath() { return backgroundPath; }

	/**
	 * Gets the buildings from map.
	 *
	 * @return the buildings from map
	 */
	public List<Building> getBuildingsFromMap() { return buildings; }

	/**
	 * Gets the context menu.
	 *
	 * @return the cm
	 */
	public ContextMenu getCM() { return cm; }

	/**
	 * Gets the directory where the tile textures are stored.
	 *
	 * @return the directory where the tile textures are stored
	 */
	public String getDir() { return dir; }

	/**
	 * Gets the exit map path.
	 *
	 * @return the exit map path
	 */
	public String getExitMap() { return exitMap; }

	/**
	 * Gets the exit position.
	 *
	 * @return the exit position
	 */
	public double[] getExitPosition() { return exitPosition; }

	/**
	 * Gets position one starts at when using the exit.
	 *
	 * @return the position one starts at when using the exit
	 */
	public double[] getExitStartingPosition() { return exitStartingPosition; }

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	public List<List<TextureHolder>> getMap() { return map; }

	/**
	 * Gets the mbuildings.
	 *
	 * @return the mbuildings
	 */
	public Menu getMbuildings() { return mbuildings; }

	/**
	 * Gets the context menu menus.
	 *
	 * @return the menus
	 */
	public Menu[] getMenus() {
		maps.getItems().clear();
		insel_k.getItems().clear();
		insel_m.getItems().clear();
		insel_g.getItems().clear();
		for (File f : new File("./res/maps").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[]	sp	= f.getName().split("[.]");
			MenuItem	map	= new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setStyle("-fx-font-size: 20;");
			map.setOnAction(ae -> gp.getLgp().setMap("./res/maps/" + map.getText() + ".json"));
			maps.getItems().add(map);
		}
		for (File f : new File("./res/maps/insel_k").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[]	sp	= f.getName().split("[.]");
			MenuItem	map	= new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setStyle("-fx-font-size: 20;");
			map.setOnAction(ae -> gp.getLgp().setMap("./res/maps/insel_k/" + map.getText() + ".json"));
			insel_k.getItems().add(map);
		}
		for (File f : new File("./res/maps/insel_m").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[]	sp	= f.getName().split("[.]");
			MenuItem	map	= new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setStyle("-fx-font-size: 20;");
			map.setOnAction(ae -> gp.getLgp().setMap("./res/maps/insel_m/" + map.getText() + ".json"));
			insel_m.getItems().add(map);
		}
		for (File f : new File("./res/maps/insel_g").listFiles((dir, f) -> f.endsWith(".json"))) {
			String[]	sp	= f.getName().split("[.]");
			MenuItem	map	= new MenuItem(String.join(".", Arrays.copyOf(sp, sp.length - 1)));
			map.setStyle("-fx-font-size: 20;");
			map.setOnAction(ae -> gp.getLgp().setMap("./res/maps/insel_g/" + map.getText() + ".json"));
			insel_g.getItems().add(map);
		}
		return new Menu[] {
				mtiles, mnpcs, getMbuildings(), mmobs, mextra
		};

	}

	/**
	 * Gets the mobs from map.
	 *
	 * @return the mobs from map
	 */
	public List<MobRan> getMobsFromMap() { return mobs; }

	/**
	 * Gets the NPCS from map.
	 *
	 * @return the NPCS from map
	 */
	public List<NPC> getNPCSFromMap() { return npcs; }

	/**
	 * Gets the object at.
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 *
	 * @return the object at x and y
	 */
	public Node getObjectAt(double x, double y) {
		List<Node> nodes = gp.getViewGroups().stream().map(v -> v.getChildren()
				.filtered(n -> n.contains(x - ((GameObject) n).getX(), y - ((GameObject) n).getY()) && n.isVisible()))
				.flatMap(FilteredList::stream).toList();
		if (nodes.size() != 0) return nodes.get(nodes.size() - 1);
		if (x < 0 || y < 0) return null;
		return getTileAt(x, y);

	}

	/**
	 * Gets the overlay.
	 *
	 * @return the overlay
	 */
	public String getOverlay() { return overlay; }

	/**
	 * Gets a part of the map.
	 *
	 * @param x      the x position on screen
	 * @param y      the y position on screen
	 * @param width  the width
	 * @param height the height
	 *
	 * @return the part of the map
	 */
	public List<List<TextureHolder>> getPartOfMap(double x, double y, double width, double height) {
		int lx, ly, w, h;
		lx	= (int) Math.floor( (x - gp.getPlayer().getScreenX() + gp.getPlayer().getX()) / gp.getBlockSizeX());
		ly	= (int) Math.floor( (y - gp.getPlayer().getScreenY() + gp.getPlayer().getY()) / gp.getBlockSizeY());
		w	= (int) Math.floor(width / gp.getBlockSizeX());
		h	= (int) Math.floor(height / gp.getBlockSizeY());

		List<List<TextureHolder>> li = new ArrayList<>();

		for (int i = 0; i < h; i++) {
			li.add(new ArrayList<>());
			for (int j = 0; j < w; j++) li.get(i).add(map.get(ly + i).get(lx + j));
		}
		return li;

	}

	/**
	 * Gets the path to the map file.
	 *
	 * @return the path to the map file
	 */
	public String getPath() { return path; }

	/**
	 * Gets the player layer.
	 *
	 * @return the player layer
	 */
	public int getPlayerLayer() { return playerLayer; }

	/**
	 * Gets the requester B.
	 *
	 * @return the requester B
	 */
	public ObjectProperty<Building> getRequesterB() { return requesterB; }

	/**
	 * Gets the requestor for the context menu containing the NPC.
	 *
	 * @return the requestor for the context menu containing the NPC
	 */
	public ObjectProperty<NPC> getRequestorN() { return requestorN; }

	/**
	 * Gets the spawn point.
	 *
	 * @return the spawn point
	 */
	public Point2D getSpawnPoint() { return new Point2D(startingPosition[0], startingPosition[1]); }

	/**
	 * Gets the starting position.
	 *
	 * @return the starting position
	 */
	public double[] getStartingPosition() { return startingPosition; }

	/**
	 * Gets the tile at x and y.
	 *
	 * @param x the x position
	 * @param y the y position
	 *
	 * @return the tile at x and y
	 */
	public TextureHolder getTileAt(double x, double y) {
		int	tx	= (int) Math.floor(x / gp.getBlockSizeX());
		int	ty	= (int) Math.floor(y / gp.getBlockSizeY());
		if (x < 0) tx--;
		if (y < 0) ty--;
		try {
			return map.get(ty).get(tx);
		} catch (IndexOutOfBoundsException e) {
			return new FakeTextureHolder(tx * gp.getBlockSizeX() - gp.getPlayer().getX() + gp.getPlayer().getScreenX(),
					ty * gp.getBlockSizeY() - gp.getPlayer().getY() + gp.getPlayer().getScreenY());
		}

	}

	/**
	 * Gets the tiles.
	 *
	 * @return the tiles
	 */
	public List<Tile> getTiles() { return tiles; }

	/**
	 * Load map.
	 *
	 * @param data the map data/matrix
	 */
	public void loadMap(JsonArray data) {

		int	col	= 0;
		int	row	= 0;

		try {

			int idx = 0;

			while (row < data.size()) {

				String		line	= ((StringValue) data.get(idx)).getValue();
				String[]	numbers	= line.split(" ");

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

	/**
	 * Reload the map.
	 */
	public void reload() { setMap(path); }

	/**
	 * Save the map.
	 */
	public void save() {
		if (!generated) try {
			File out = new File(path).getAbsoluteFile();
			System.out.println(out);
			if (out.exists()) {
				JsonObject	jo			= (JsonObject) JsonParser.parse(out);
				JsonArray	buildings	= (JsonArray) jo.get("buildings");
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
				for (List<TextureHolder> mapi : this.map) {
					StringBuilder sb = new StringBuilder();
					for (TextureHolder th : mapi) if (textures.contains(th.getTile().name))
						sb.append(textures.indexOf(th.getTile().name) + " ");
					else {
						sb.append(textures.size() + " ");
						textures.add(th.getTile().name);
					}
					matrix.add(sb.toString().substring(0, sb.toString().length() - 1));
				}
				String			jsonOut	= jo.toJson();
				BufferedWriter	bw		= new BufferedWriter(new FileWriter(out));
				bw.write(jsonOut);
				bw.flush();
				bw.close();
			} else {

			}
		} catch (JsonParseException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the map.
	 *
	 * @param path the path to the new map
	 */
	public void setMap(String path) {
		try {

			this.path = path;

			exitPosition			= null;
			exitStartingPosition	= null;
			exitMap					= null;
			startingPosition		= new double[] {
					0d, 0d
			};

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
			getMbuildings().getItems().clear();
			mmobs.getItems().clear();
			playerLayer = 0;
			JsonObject jo = (JsonObject) JsonParser
					.parse(new FileInputStream(path));

			//overlay

			if (jo.containsKey("generated") && jo.get("generated") instanceof BoolValue bv && bv.getValue()) {

				if (jo.containsKey("BackgroundMusic")) backgroundMusic = ((StringValue) jo.get("BackgroundMusic")).getValue();
				else backgroundMusic = "";

				if (jo.containsKey("overlay")) overlay = ((StringValue) jo.get("overlay")).getValue();
				else overlay = "";

				generated = true;

				if (jo.containsKey("background")) backgroundPath = ((StringValue) jo.get("background")).getValue();
				else backgroundPath = null;

				JsonObject mainmap = (JsonObject) JsonParser
						.parse(new FileInputStream("./res/maps/" + ((StringValue) jo.get("mainmap")).getValue()));

				JsonObject endmap = (JsonObject) JsonParser
						.parse(new FileInputStream("./res/maps/" + ((StringValue) jo.get("endmap")).getValue()));

				JsonArray ja_maps = (JsonArray) jo.get("maps");

				String voidImg = ((StringValue) jo.get("void")).getValue();

				DungeonGen d = new DungeonGen(gp, voidImg, mainmap, ja_maps,endmap,
						((JsonArray) jo.get("connectors")).stream().map(jOb -> (JsonObject) jOb).toList(),
						((JsonArray) jo.get("connections")).stream().map(jOb -> (JsonObject) jOb).toList(),
						((JsonArray) jo.get("replacments")).stream().map(jOb -> (JsonObject) jOb).toList(), (JsonObject) jo.get("additionalData"),
						gp.getLgp().getDifficulty());

				d.findFreeConnectors();

				d.stitchMaps();

				return;
			}

			generated = false;

			JsonObject	map					= (JsonObject) jo.get("map");
			JsonArray	textures			= (JsonArray) map.get("textures");
			JsonArray	npcs				= (JsonArray) jo.get("npcs");
			JsonArray	buildings			= (JsonArray) jo.get("buildings");
			JsonArray	startingPosition	= (JsonArray) map.get("startingPosition");

			dir = ((StringValue) map.get("dir")).getValue();

			if (map.containsKey("BackgroundMusic")) backgroundMusic = ((StringValue) map.get("BackgroundMusic")).getValue();
			else backgroundMusic = "";

			if (map.containsKey("overlay")) overlay = ((StringValue) map.get("BackgroundMusic")).getValue();
			else overlay = "";

			if (map.containsKey("background")) backgroundPath = ((StringValue) map.get("background")).getValue();
			else backgroundPath = null;

			if (jo.containsKey("exit")) {
				JsonObject exit = (JsonObject) jo.get("exit");
				exitMap = ((StringValue) exit.get("map")).getValue();
				JsonArray	spawnPosition	= (JsonArray) exit.get("spawnPosition");
				JsonArray	position		= (JsonArray) exit.get("position");
				exitStartingPosition	= new double[] {
						((NumberValue) spawnPosition.get(0)).getValue().doubleValue(), ((NumberValue) spawnPosition.get(1)).getValue().doubleValue()
				};
				exitPosition			= new double[] {
						((NumberValue) position.get(0)).getValue().doubleValue(), ((NumberValue) position.get(1)).getValue().doubleValue()
				};
			}
			for (Object texture : textures) try {
				Tile t = new Tile( ((StringValue) texture).getValue(),
						new FileInputStream("./res/" + getDir() + "/" + ((StringValue) texture).getValue()),
						gp);
				tiles.add(t);
				mtiles.getItems()
				.add(new MenuItemWTile( ((StringValue) texture).getValue(),
						new ImageView(ImgUtil.resizeImage(t.images.get(0),
								(int) t.images.get(0).getWidth(), (int) t.images.get(0).getHeight(), 48, 48)),
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
							t.poly.add(raf.readDouble() * ( (s = !s) ? gp.getScalingFactorX() : gp.getScalingFactorY()));
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (NullPointerException e) {
				String[] sp = ((StringValue) texture).getValue().split("[.]");
				new IOException(getDir() + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1)), e)
				.printStackTrace();
			}
			this.startingPosition = new double[] {
					((NumberValue) startingPosition.get(0)).getValue().doubleValue(), ((NumberValue) startingPosition.get(1)).getValue().doubleValue()
			};
			if (map.containsKey("playerLayer"))
				playerLayer = ((NumberValue) map.get("playerLayer")).getValue().intValue();
			else playerLayer = 0;
			mapTileNum	= new ArrayList<>();
			this.map	= new ArrayList<>();
			loadMap((JsonArray) map.get("matrix"));
			this.buildings	= new ArrayList<>();
			this.npcs		= new ArrayList<>();
			mobs			= new ArrayList<>();
			for (Object building : buildings) {
				Building b = switch ( ((StringValue) ((JsonObject) building).get("type")).getValue()) {
					case "House" -> new House((JsonObject) building, gp, this.buildings, cm, requesterB);
					case "ContractsTable" -> new ContractsTable((JsonObject) building, gp, this.buildings, cm,
							requesterB);
					case "TreasureChest" -> new TreasureChest((JsonObject) building, gp, this.buildings, cm, requesterB);
					default -> new Building((JsonObject) building, gp, this.buildings, cm, requesterB);
				};
				System.err.println(building + " " + b.getCurrentKey() + " " + b.getImages());
				ImageView	lIV;
				if (b.isGif(b.getCurrentKey())) {
					lIV = new ImageView(b.getImages().get(b.getCurrentKey()).get(0));
					lIV.setFitWidth(16);
					lIV.setFitHeight(16);
				} else lIV = new ImageView(ImgUtil.resizeImage(b.getImages().get(b.getCurrentKey()).get(0),
						(int) b.getImages().get(b.getCurrentKey()).get(0).getWidth(),
						(int) b.getImages().get(b.getCurrentKey()).get(0).getHeight(), 48, 48));
				getMbuildings().getItems().add(new MenuItemWBuilding(
						((StringValue) ((JsonObject) ((JsonObject) building).get("textures")).values().stream()
								.findFirst().get()).getValue(),
						lIV,
						b));
			}
			for (Object npc : npcs) {
				Entity n = switch ( ((StringValue) ((JsonObject) npc).get("type")).getValue()) {
					case "MonsterNPC", "monsternpc", "Demon", "demon" -> new MonsterNPC((JsonObject) npc, gp, this.npcs, cm,
							requestorN);
					case "MobRan", "mobran" -> new MobRan((JsonObject) npc, gp, mobs, cm, requestorM);
					default -> new NPC((JsonObject) npc, gp, this.npcs, cm, requestorN);
				};
				ImageView	lIV;
				if (n.isGif(n.getCurrentKey())) {
					lIV = new ImageView(n.getImages().get(n.getCurrentKey()).get(0));
					lIV.setFitWidth(16);
					lIV.setFitHeight(16);
				} else lIV = new ImageView(ImgUtil.resizeImage(n.getImages().get(n.getCurrentKey()).get(0),
						(int) n.getImages().get(n.getCurrentKey()).get(0).getWidth(),
						(int) n.getImages().get(n.getCurrentKey()).get(0).getHeight(), 48, 48));
				if (n instanceof MobRan mr)
					mmobs.getItems()
					.add(new MenuItemWMOB(
							((StringValue) ((JsonObject) ((JsonObject) npc).get("textures")).values().stream()
									.findFirst().get()).getValue(),
							lIV,
							mr));
				else if (n instanceof NPC np)
					mnpcs.getItems()
					.add(new MenuItemWNPC(
							((StringValue) ((JsonObject) ((JsonObject) npc).get("textures")).values().stream()
									.findFirst().get()).getValue(),
							lIV,
							np));

			}
			mtiles.getItems().add(new MenuItem("add Texture"));
			mnpcs.getItems().add(new MenuItem("add Texture"));
			mmobs.getItems().add(new MenuItem("add Texture"));
			mtiles.getItems().get(mtiles.getItems().size() - 1).setStyle("-fx-font-size: 20;");
			mnpcs.getItems().get(mnpcs.getItems().size() - 1).setStyle("-fx-font-size: 20;");
			mmobs.getItems().get(mmobs.getItems().size() - 1).setStyle("-fx-font-size: 20;");
			getMbuildings().getItems().add(new MenuItem("add Texture"));
			getMbuildings().getItems().get(getMbuildings().getItems().size() - 1).setStyle("-fx-font-size: 20;");
			for (MenuItem mi : mtiles.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi : mnpcs.getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi : getMbuildings().getItems()) mi.setOnAction(this::contextMenu);
			for (MenuItem mi : mmobs.getItems()) mi.setOnAction(this::contextMenu);

		} catch (JsonParseException | FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets the starting position.
	 *
	 * @param startingPosition the new starting position
	 */
	public void setStartingPosition(double[] startingPosition) { this.startingPosition = startingPosition; }

	/**
	 * Update.
	 */
	public void update() {

		Player p = gp.getPlayer();

		if (exitMap != null) {
			int	worldX	= (int) (exitPosition[0] * gp.getScalingFactorX());
			int	worldY	= (int) (exitPosition[1] * gp.getScalingFactorY());

			if (worldX + gp.getBlockSizeX() / 2 - p.getX() < 105 * gp.getScalingFactorX()
					&& worldX + gp.getBlockSizeX() / 2 - p.getX() > -45 * gp.getScalingFactorX() &&
					worldY + gp.getBlockSizeY() / 2 - p.getY() < 25 * gp.getScalingFactorY()
					&& worldY + gp.getBlockSizeY() / 2 - p.getY() > 0)
				gp.getLgp().setMap("./res/maps/" + exitMap, exitStartingPosition);
		}

		int	worldCol	= 0;
		int	worldRow	= 0;

		while (worldRow < mapTileNum.size()) {
			int tileNum = mapTileNum.get(worldRow).get(worldCol);

			int		worldX	= worldCol * gp.getBlockSizeX();
			int		worldY	= worldRow * gp.getBlockSizeY();
			double	screenX	= worldX - p.getX() + p.getScreenX();
			double	screenY	= worldY - p.getY() + p.getScreenY();

			if (map.size() == worldRow)
				map.add(new ArrayList<>());

			if (worldX + p.getSize() * 1.5 > p.getX() - p.getScreenX() && worldX - gp.getBlockSizeX() -
					p.getSize() * 1.5 < p.getX() + p.getScreenX()
					&& worldY + gp.getBlockSizeY() + p.getSize() > p.getY() - p.getScreenY()
					&& worldY - gp.getBlockSizeY() - p.getSize() < p.getY() + p.getScreenY()) {
				TextureHolder th = null;
				if (map.get(worldRow).size() > worldCol)
					th = map.get(worldRow).get(worldCol);
				if (th == null) {
					th = new TextureHolder(tiles.get(tileNum < tiles.size() ? tileNum : 0), gp, screenX, screenY,
							cm, requester, worldX, worldY);
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
							requester, worldX, worldY);
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
