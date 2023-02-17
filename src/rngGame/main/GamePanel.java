package rngGame.main;

import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import com.sterndu.json.*;
import com.sterndu.multicore.Updater;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rngGame.buildings.*;
import rngGame.entity.*;
import rngGame.tile.*;
import rngGame.ui.TabMenu;

// TODO: Auto-generated Javadoc
/**
 * The Class GamePanel.
 */
public class GamePanel extends Pane {

	/**
	 * The Class GroupGroup.
	 */
	class GroupGroup extends Group {

		/**
		 * The Class OutList.
		 */
		class OutList extends AbstractList<Group> {

			/** The li. */
			ObservableList<Node> li;

			/**
			 * Instantiates a new out list.
			 *
			 * @param li the li
			 */
			public OutList(ObservableList<Node> li) {
				this.li = li;
			}

			/**
			 * Adds the.
			 *
			 * @param index the index
			 * @param element the element
			 */
			@Override
			public void add(int index, Group element) {
				li.add(index, element);
			}

			/**
			 * Gets the.
			 *
			 * @param index the index
			 * @return the group
			 */
			@Override
			public Group get(int index) {
				int size = size();
				if (index >= li.size()) for (int i = 0; i <= index - size; i++) li.add(new Group());
				return (Group) li.get(index);
			}

			/**
			 * Removes the.
			 *
			 * @param index the index
			 * @return the group
			 */
			@Override
			public Group remove(int index) {
				return (Group) li.remove(index);
			}

			/**
			 * Sets the.
			 *
			 * @param index the index
			 * @param element the element
			 * @return the group
			 */
			@Override
			public Group set(int index, Group element) {
				return (Group) li.set(index, element);
			}

			/**
			 * Size.
			 *
			 * @return the int
			 */
			@Override
			public int size() {
				return li.size();
			}
		}

		/**
		 * Gets the group children.
		 *
		 * @return the group children
		 */
		public List<Group> getGroupChildren() {
			return new OutList(super.getChildren());
		}
	}

	/** The Bg. */
	private final int Bg = 48;

	/** The Bild S. */
	public final int BildS = 20;

	/** The Bild H. */
	public final int BildH = 11;

	/** The Bg Y. */
	public int BgX = Bg, BgY = Bg;

	/** The Spiel laenge. */
	public int SpielLaenge = BgX * BildS;

	/** The scaling factor Y. */
	public double scalingFactorX = 1, scalingFactorY = 1;

	/** The Spiel hoehe. */
	public int SpielHoehe = BgY * BildH;

	/** The fps. */
	private final int FPS = 80;

	/** The inv. */


	/** The difficulty. */
	private Difficulty difficulty;

	/** The input. */
	private final Input input;

	/** The player. */
	private final Player player;

	/** The tile M. */
	private final TileManager tileM;

	/** The select tool. */
	private final SelectTool selectTool;

	/** The layer group. */
	private final GroupGroup layerGroup;

	/** The point group. */
	private final Group pointGroup;

	/** The buildings. */
	private List<Building> buildings;

	/** The npcs. */
	private List<NPC> npcs;

	/** The test. */
	private List<MobRan> test;

	/** The frame times. */
	private List<Long> frameTimes;

	/** The last frame. */
	private Long lastFrame;

	/** The fps. */
	private Double fps = 0d;

	/** The loading screen. */
	private final ImageView loadingScreen;

	/** The fps label. */
	private final Label fpsLabel;

	/** The points. */
	private final Map<Point2D, Circle> points;

	/** The block user inputs. */
	private boolean blockUserInputs;

	/** The gamemenu. */
	private final TabMenu gamemenu;

	/** The aktionbutton. */
	private final AktionButton aktionbutton;

	/** The mp. */
	private MediaPlayer mp;

	/** The overlay. */
	private final ImageView overlay;

	/**
	 * Instantiates a new game panel.
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	public GamePanel() throws FileNotFoundException {
		setPrefSize(SpielLaenge, SpielHoehe);

		overlay = new ImageView();
		overlay.setDisable(true);

		difficulty = Difficulty.EASY;

		loadingScreen = new ImageView(new Image(new FileInputStream(new File("./res/gui/Loadingscreen.gif"))));
		loadingScreen.setDisable(true);

		input = Input.getInstance();

		pointGroup = new Group();
		pointGroup.setDisable(true);

		points = new HashMap<>();

		layerGroup = new GroupGroup();
		layerGroup.getChildren().add(new Group());

		selectTool = new SelectTool(this);

		tileM = new TileManager(this);

		player = new Player(this, tileM.getCM(), tileM.getRequestorN());

		fpsLabel = new Label(fps + "");
		fpsLabel.setBackground(new Background(new BackgroundFill(Color.color(.5, .5, .5, 1), null, null)));
		fpsLabel.setTextFill(Color.color(.1, .1, .1));
		fpsLabel.setOpacity(.6);
		fpsLabel.setDisable(true);
		fpsLabel.setVisible(false);

		gamemenu = new TabMenu(this);

		aktionbutton = new AktionButton(this);

		setMap("./res/maps/lavaMap2.json");

		System.out.println(aktionbutton.isDisable());

		getChildren().addAll(tileM, layerGroup, overlay, pointGroup, selectTool, aktionbutton, gamemenu, fpsLabel, loadingScreen);
	}

	/**
	 * Gets the aktionbutton.
	 *
	 * @return the aktionbutton
	 */
	public AktionButton getAktionbutton() { return aktionbutton; }

	/**
	 * Gets the buildings.
	 *
	 * @return the buildings
	 */
	public List<Building> getBuildings() { return buildings; }

	/**
	 * Gets the difficulty.
	 *
	 * @return the difficulty
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * Gets the fps.
	 *
	 * @return the fps
	 */
	public Double getFps() { return fps; }

	/**
	 * Gets the mob rans.
	 *
	 * @return the mob rans
	 */
	public List<MobRan> getMobRans() { return test; }

	/**
	 * Gets the npcs.
	 *
	 * @return the npcs
	 */
	public List<NPC> getNpcs() { return npcs; }

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() { return player; }

	/**
	 * Gets the scaling factor X.
	 *
	 * @return the scaling factor X
	 */
	public double getScalingFactorX() { return scalingFactorX; }

	/**
	 * Gets the scaling factor Y.
	 *
	 * @return the scaling factor Y
	 */
	public double getScalingFactorY() { return scalingFactorY; }

	/**
	 * Gets the select tool.
	 *
	 * @return the select tool
	 */
	public SelectTool getSelectTool() { return selectTool; }

	/**
	 * Gets the tile M.
	 *
	 * @return the tile M
	 */
	public TileManager getTileM() { return tileM; }

	/**
	 * Gets the view groups.
	 *
	 * @return the view groups
	 */
	public List<Group> getViewGroups() { return layerGroup.getGroupChildren(); }


	/**
	 * Checks if is block user inputs.
	 *
	 * @return true, if is block user inputs
	 */
	public boolean isBlockUserInputs() {
		return isInLoadingScreen() || blockUserInputs;
	}

	/**
	 * Checks if is in loading screen.
	 *
	 * @return true, if is in loading screen
	 */
	public boolean isInLoadingScreen() {
		return loadingScreen.getOpacity()>.5;
	}

	/**
	 * Reload.
	 */
	public void reload() {
		layerGroup.getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		points.clear();
		pointGroup.getChildren().clear();
		tileM.reload();
		gamemenu.f11Scale();
		aktionbutton.f11Scale();
		if (tileM.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileM.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(SpielLaenge, SpielHoehe + scalingFactorY, false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		player.setLayer(tileM.getPlayerLayer());
		buildings = tileM.getBuildingsFromMap();

		if (!"".equals(tileM.getOverlay())) overlay.setImage(ImgUtil.getScaledImage(this, "./res/gui/" + tileM.getOverlay()));
		else overlay.setImage(null);

		Circle spawn = new Circle(tileM.getSpawnPoint().getX() * getScalingFactorX(),
				tileM.getSpawnPoint().getY() * getScalingFactorY(), 15,
				Color.color(0, 1, 0, .75));
		points.put(tileM.getSpawnPoint(), spawn);
		pointGroup.getChildren().add(spawn);
		buildings.forEach(b -> {
			if (b instanceof House h) {
				String map = h.getMap();
				try {
					JsonObject jo = (JsonObject) JsonParser.parse(new File("./res/maps/" + map));
					if (jo.containsKey("exit")) {
						JsonObject exit = (JsonObject) jo.get("exit");
						if (tileM.getPath().endsWith(((StringValue) exit.get("map")).getValue())) {
							JsonArray spawnPosition = (JsonArray) exit.get("spawnPosition");
							Point2D p = new Point2D(((NumberValue) spawnPosition.get(0)).getValue().longValue(),
									((NumberValue) spawnPosition.get(1)).getValue().longValue());
							Circle respawn = new Circle(p.getX() * getScalingFactorX(), p.getY() * getScalingFactorY(),
									15,
									Color.color(0, 1, 0, .75));
							points.put(p, respawn);
							pointGroup.getChildren().add(respawn);
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				}
			}
		});
		npcs = tileM.getNPCSFromMap();
		test = tileM.getMobsFromMap();

		//		ImageView iv = new ImageView(Text.getInstance()
		//				.convertText("ABCDEFGH\nIJKLMNOP\nQRSTUVWX\nYZabcdef\nghijklmn\nopqrstuv\nwxyz1234\n567890?!\n%\"# #",
		//						48));
		//		ImageView iv = new ImageView(Text.getInstance()
		//				.convertText("Programmieren ist LW"));
		//		getChildren().add(iv);
		// TODO rem
	}

	/**
	 * Save map.
	 */
	public void saveMap() {
		tileM.save();
		System.out.println("don");
	}

	/**
	 * Scale textures.
	 *
	 * @param scaleFactorX the scale factor X
	 * @param scaleFactorY the scale factor Y
	 */
	public void scaleTextures(double scaleFactorX, double scaleFactorY) {
		player.setPosition(player.getX() * (scaleFactorX / scalingFactorX),
				player.getY() * (scaleFactorY / scalingFactorY));
		scalingFactorX = scaleFactorX;
		scalingFactorY = scaleFactorY;
		BgX = (int) (Bg * scaleFactorX);
		BgY = (int) (Bg * scaleFactorY);
		SpielLaenge = BgX * BildS;
		SpielHoehe = BgY * BildH;
		reload();
		player.getPlayerImage();
		player.generateCollisionBox();
		System.out.println(player.getX() + " " + player.getY());
	}

	/**
	 * Sets the block user inputs.
	 *
	 * @param blockUserInputs the new block user inputs
	 */
	public void setBlockUserInputs(boolean blockUserInputs) { this.blockUserInputs = blockUserInputs; }


	/**
	 * Sets the difficulty.
	 *
	 * @param difficulty the new difficulty
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	/**
	 * Sets the map.
	 *
	 * @param path the new map
	 */
	public void setMap(String path) {
		setMap(path, null);
	}

	/**
	 * Sets the map.
	 *
	 * @param path the path
	 * @param position the position
	 */
	public void setMap(String path, double[] position) {


		UndoRedo.getInstance().clearActions();

		loadingScreen.setFitWidth(loadingScreen.getImage().getWidth() * getScalingFactorX());
		loadingScreen.setFitHeight(loadingScreen.getImage().getHeight() * getScalingFactorY());
		loadingScreen.setOpacity(1);

		layerGroup.getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		points.clear();
		pointGroup.getChildren().clear();
		tileM.setMap(path);
		if (tileM.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileM.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(SpielLaenge, SpielHoehe, false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		if (mp != null) mp.stop();

		if (!"".equals(tileM.getBackgroundMusic())) {
			mp = new MediaPlayer(new Media(new File("./res/" + tileM.getBackgroundMusic()).toURI().toString()));
			mp.setAutoPlay(true);
			mp.setVolume(.2);
			// mp.setCycleCount(MediaPlayer.INDEFINITE);
			Updater.getInstance().add((Runnable) () -> {
				Duration	duration	= mp.getMedia().getDuration();
				Duration	curr		= mp.getCurrentTime();
				// System.out.println("meow " + duration + " " + curr);
				if (duration.subtract(curr).lessThan(Duration.millis(34.2))) mp.seek(Duration.millis(2.5));
			}, "CheckIfMusicIsDone");
		} else mp = null;

		if (!"".equals(tileM.getOverlay())) overlay.setImage(ImgUtil.getScaledImage(this, "./res/gui/"+tileM.getOverlay()));
		else overlay.setImage(null);

		buildings = tileM.getBuildingsFromMap();
		npcs = tileM.getNPCSFromMap();
		test = tileM.getMobsFromMap();
		Circle spawn = new Circle(tileM.getSpawnPoint().getX() * scalingFactorX,
				tileM.getSpawnPoint().getY() * scalingFactorY, 15,
				Color.color(0, 1, 0, .75));
		points.put(tileM.getSpawnPoint(), spawn);
		pointGroup.getChildren().add(spawn);
		buildings.forEach(b->{
			if (b instanceof House h) {
				String map = h.getMap();
				try {
					JsonObject jo = (JsonObject) JsonParser.parse(new File("./res/maps/"+map));
					if (jo.containsKey("exit")) {
						JsonObject exit = (JsonObject) jo.get("exit");
						if (tileM.getPath().endsWith(((StringValue) exit.get("map")).getValue())) {
							JsonArray spawnPosition = (JsonArray) exit.get("spawnPosition");
							Point2D p = new Point2D(
									((NumberValue) spawnPosition.get(0)).getValue().longValue(),
									((NumberValue) spawnPosition.get(1)).getValue().longValue());
							Circle respawn = new Circle(p.getX(), p.getY(), 15,
									Color.color(0, 1, 0, .75));
							points.put(p, respawn);
							pointGroup.getChildren().add(respawn);
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				}
			}
		});

		if (position != null)
			player.setPosition(new double[] {position[0] * getScalingFactorX(), position[1] * getScalingFactorY()});
		else {
			double[] posi = tileM.getStartingPosition();
			player.setPosition(new double[] {posi[0] * getScalingFactorX(), posi[1] * getScalingFactorY()});
		}
		player.setLayer(tileM.getPlayerLayer());

		new Thread(() -> {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			FadeTransition ft = new FadeTransition(Duration.millis(500), loadingScreen);
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.play();

		}).start();

	}

	/**
	 * Sst.
	 */
	public void SST() {

		frameTimes = new ArrayList<>();
		lastFrame = System.currentTimeMillis();

		AtomicReference<Runnable> runnable = new AtomicReference<>();
		AtomicReference<Timeline> arTl = new AtomicReference<>();
		Timeline tl = new Timeline(
				new KeyFrame(Duration.millis(1000 / FPS),
						event -> {
							update();
							if ("true".equals(System.getProperty("alternateUpdate"))) {
								arTl.get().stop();
								Platform.runLater(runnable.get());
							}
						}));
		arTl.set(tl);
		tl.setCycleCount(Animation.INDEFINITE);
		Runnable r = () -> {
			update();
			if (!MainClass.isStopping() && "true".equals(System.getProperty("alternateUpdate")))
				Platform.runLater(runnable.get());
			else arTl.get().play();
		};
		runnable.set(r);

		if ("false".equals(System.getProperty("alternateUpdate"))) tl.play();
		else Platform.runLater(r);
	}

	/**
	 * Toggle fps label visible.
	 */
	public void toggleFpsLabelVisible() {
		fpsLabel.setVisible(!fpsLabel.isVisible());
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "SpielPanel ["
				+ ", input=" + input + ", player=" + player + ", tileM=" + tileM + ", selectTool="
				+ selectTool + ", layerGroup=" + layerGroup.getChildren().size() + ", buildings=" + buildings
				+ ", npcs=" + npcs
				+ "]";
	}

	/**
	 * Update.
	 */
	public void update() {

		long lastFrameTime = frameTimes.size() > 0 ? frameTimes.get(frameTimes.size() - 1) : 0;

		aktionbutton.setInteractionbuttonKann(false);

		input.update(lastFrameTime);

		try {
			player.update(lastFrameTime);
		} catch (ConcurrentModificationException e) {
		}

		selectTool.update();

		fpsLabel.setText(String.format("%.2f", 1000 / fps));
		fpsLabel.setLayoutX(SpielLaenge - fpsLabel.getWidth());

		try {
			for (Building b: buildings) b.update(lastFrameTime);
			for (Entity n: npcs) n.update(lastFrameTime);
			for (Entity n: test) n.update(lastFrameTime);
		} catch (ConcurrentModificationException e) {
		}

		for (Node layer: layerGroup.getChildren()) {
			Group view = (Group) layer;
			List<Node> nodes = new ArrayList<>(view.getChildren());

			nodes.sort((n1, n2) -> {
				if (n1 instanceof GameObject b1) if (n2 instanceof GameObject b2)
					return b1.isBackground() ^ b2.isBackground() ? b1.isBackground() ? -1 : 1
							: Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
									n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
				else return b1.isBackground() ? -1
						: Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
								n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
				if (n2 instanceof GameObject b21) return b21.isBackground() ? 1
						: Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
								n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
				return Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
						n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
			});

			view.getChildren().clear();
			view.getChildren().addAll(nodes);
		}

		for (Entry<Point2D, Circle> point: points.entrySet()) {
			point.getValue().setCenterX(point.getKey().getX() * getScalingFactorX() - player.x + player.getScreenX());
			point.getValue().setCenterY(point.getKey().getY() * getScalingFactorY() - player.y + player.getScreenY());
		}

		if ("true".equals(System.getProperty("edit"))) pointGroup.setVisible(true);
		else pointGroup.setVisible(false);

		tileM.update();

		long frameTime = System.currentTimeMillis() - lastFrame;
		lastFrame = System.currentTimeMillis();
		frameTimes.add(frameTime);
		fps = frameTimes.stream().mapToLong(l -> l).average().getAsDouble();
		while (frameTimes.size() > Math.pow(fps * 12, 1.2)) frameTimes.remove(0);

	}

}
