package rngGame.visual;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import com.sterndu.json.*;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rngGame.buildings.House;
import rngGame.entity.Player;
import rngGame.main.*;
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
	public class GroupGroup extends Group {

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
			public OutList(ObservableList<Node> li) { this.li = li; }

			/**
			 * Adds the.
			 *
			 * @param index   the index
			 * @param element the element
			 */
			@Override
			public void add(int index, Group element) { li.add(index, element); }

			/**
			 * Gets the.
			 *
			 * @param index the index
			 *
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
			 *
			 * @return the group
			 */
			@Override
			public Group remove(int index) { return (Group) li.remove(index); }

			/**
			 * Sets the.
			 *
			 * @param index   the index
			 * @param element the element
			 *
			 * @return the group
			 */
			@Override
			public Group set(int index, Group element) { return (Group) li.set(index, element); }

			/**
			 * Size.
			 *
			 * @return the int
			 */
			@Override
			public int size() { return li.size(); }

		}

		/**
		 * Gets the group children.
		 *
		 * @return the group children
		 */
		public List<Group> getGroupChildren() { return new OutList(super.getChildren()); }

	}

	/** The loading screen. */
	private final ImageView loadingScreen;

	/** The fps label. */
	private final Label fpsLabel;

	/** The overlay. */
	private final ImageView overlay;

	/** The bubble text. */
	private final Pane bubbleText;

	/** The layer group. */
	private final GroupGroup layerGroup;

	/** The point group. */
	private final Group pointGroup;

	/** The frame times. */
	private List<Long> frameTimes;

	/** The last frame. */
	private Long lastFrame;

	/** The tile M. */
	private final TileManager tileManager;

	/** The aktionbutton. */
	private final AktionButton aktionbutton;

	/** The select tool. */
	private final SelectTool selectTool;

	/** The gamemenu. */
	private final TabMenu gamemenu;

	/** The target FPS. */
	private final int targetFPS = 80;

	/** The scaling factor Y. */
	private double scalingFactorX = 1, scalingFactorY = 1;

	/** The block size. */
	private final int blockSize = 48;

	/** The x blocks. */
	private final int xBlocks = 20;

	/** The y blocks. */
	private final int yBlocks = 11;

	/** The scleed blockSizes. */
	private int blockSizeX = blockSize, blockSizeY = blockSize;

	/** The game height. */
	private int gameHeight = blockSizeY * yBlocks;

	/** The game width. */
	private int gameWidth = blockSizeX * xBlocks;

	/** The fps. */
	private Double fps = 0d;

	/** The block user inputs. */
	private boolean blockUserInputs;

	/** The player. */
	private final Player player;

	/** The lgp. */
	private final rngGame.main.GamePanel lgp;

	/**
	 * Instantiates a new game panel.
	 *
	 * @param lgp the lgp
	 * @throws FileNotFoundException the file not found exception
	 */
	public GamePanel(rngGame.main.GamePanel lgp)
			throws FileNotFoundException {
		setPrefSize(gameWidth, gameHeight);

		lgp.setVgp(this);

		this.lgp	= lgp;

		bubbleText = new Pane();

		overlay = new ImageView();

		getOverlay().setDisable(true);

		loadingScreen = new ImageView(new Image(new FileInputStream(new File("./res/gui/Loadingscreen.gif"))));
		getLoadingScreen().setDisable(true);

		fpsLabel = new Label(fps + "");
		fpsLabel.setBackground(new Background(new BackgroundFill(Color.color(.5, .5, .5, 1), null, null)));
		fpsLabel.setTextFill(Color.color(.1, .1, .1));
		fpsLabel.setOpacity(.6);
		fpsLabel.setDisable(true);
		fpsLabel.setVisible(false);

		pointGroup = new Group();
		getPointGroup().setDisable(true);

		layerGroup = new GroupGroup();
		getLayerGroup().getChildren().add(new Group());

		selectTool = new SelectTool(this);

		tileManager = new TileManager(this);
		tileManager.setPrefSize(gameWidth, gameHeight);

		player = new Player(this, tileManager.getCM(), tileManager.getRequestorN());

		aktionbutton = new AktionButton(this);

		lgp.setMap("./res/maps/lavaMap2.json");
		gamemenu = new TabMenu(getLgp());

		getChildren().addAll(tileManager, getLayerGroup(), getOverlay(), getPointGroup(), selectTool, aktionbutton, lgp.getBubble(), bubbleText,
				gamemenu, fpsLabel,
				getLoadingScreen());
	}

	/**
	 * Scale textures.
	 *
	 * @param scaleFactorX the scale factor X
	 * @param scaleFactorY the scale factor Y
	 */
	public void changeScalingFactor(double scaleFactorX, double scaleFactorY) {
		player.setPosition(player.getX() * (scaleFactorX / scalingFactorX),
				player.getY() * (scaleFactorY / scalingFactorY));
		scalingFactorX = scaleFactorX;
		scalingFactorY = scaleFactorY;
		blockSizeX		= (int) (blockSize * scaleFactorX);
		blockSizeY		= (int) (blockSize * scaleFactorY);
		gameWidth		= blockSizeX * xBlocks;
		gameHeight		= blockSizeY * yBlocks;
		reload();
		player.getPlayerImage();
		player.generateCollisionBox();
		System.out.println(player.getX() + " " + player.getY());
	}

	/**
	 * Gets the aktionbutton.
	 *
	 * @return the aktionbutton
	 */
	public AktionButton getAktionbutton() { return aktionbutton; }

	/**
	 * Gets the block size.
	 *
	 * @return the block size
	 */
	public int getBlockSize() { return blockSize; }

	/**
	 * Gets the block size X.
	 *
	 * @return the block size X
	 */
	public int getBlockSizeX() { return blockSizeX; }

	/**
	 * Gets the block size Y.
	 *
	 * @return the block size Y
	 */
	public int getBlockSizeY() { return blockSizeY; }

	/**
	 * Gets the bubble text.
	 *
	 * @return the bubble text
	 */
	public Pane getBubbleText() {
		return bubbleText;
	}

	/**
	 * Gets the fps.
	 *
	 * @return the fps
	 */
	public Double getFps() { return fps; }

	/**
	 * Gets the game height.
	 *
	 * @return the game height
	 */
	public int getGameHeight() { return gameHeight; }

	/**
	 * Gets the gamemenu.
	 *
	 * @return the gamemenu
	 */
	public TabMenu getGamemenu() { return gamemenu; }

	/**
	 * Gets the game width.
	 *
	 * @return the game width
	 */
	public int getGameWidth() { return gameWidth; }

	/**
	 * Gets the layer group.
	 *
	 * @return the layer group
	 */
	public GroupGroup getLayerGroup() { return layerGroup; }

	/**
	 * Gets the lgp.
	 *
	 * @return the lgp
	 */
	public rngGame.main.GamePanel getLgp() { return lgp; }

	/**
	 * Gets the loading screen.
	 *
	 * @return the loading screen
	 */
	public ImageView getLoadingScreen() { return loadingScreen; }

	/**
	 * Gets the overlay.
	 *
	 * @return the overlay
	 */
	public ImageView getOverlay() { return overlay; }

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() { return player; }

	/**
	 * Gets the point group.
	 *
	 * @return the point group
	 */
	public Group getPointGroup() { return pointGroup; }

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
	 * Gets the target FPS.
	 *
	 * @return the target FPS
	 */
	public int getTargetFPS() { return targetFPS; }

	/**
	 * Gets the tile M.
	 *
	 * @return the tile M
	 */
	public TileManager getTileManager() { return tileManager; }

	/**
	 * Gets the view groups.
	 *
	 * @return the view groups
	 */
	public List<Group> getViewGroups() { return getLayerGroup().getGroupChildren(); }

	/**
	 * Gets the x blocks.
	 *
	 * @return the x blocks
	 */
	public int getxBlocks() { return xBlocks; }

	/**
	 * Gets the y blocks.
	 *
	 * @return the y blocks
	 */
	public int getyBlocks() { return yBlocks; }

	/**
	 * Go into loading screen.
	 */
	public void goIntoLoadingScreen() {
		getLoadingScreen().setFitWidth(getLoadingScreen().getImage().getWidth() * getScalingFactorX());
		getLoadingScreen().setFitHeight(getLoadingScreen().getImage().getHeight() * getScalingFactorY());

		FadeTransition ft = new FadeTransition(Duration.millis(250), getLoadingScreen());
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.play();
	}

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
	public boolean isInLoadingScreen() { return getLoadingScreen().getOpacity() > .5; }

	/**
	 * Reload.
	 */
	public void reload() {
		getLayerGroup().getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		getPointGroup().getChildren().clear();

		if (!"".equals(getTileManager().getOverlay()))
			getOverlay().setImage(ImgUtil.getScaledImage(this, "./res/gui/" + getTileManager().getOverlay()));
		else getOverlay().setImage(null);

		Circle spawn = new Circle(getTileManager().getSpawnPoint().getX() * getScalingFactorX(),
				getTileManager().getSpawnPoint().getY() * getScalingFactorY(), 15,
				Color.color(0, 1, 0, .75));
		getPointGroup().getChildren().add(spawn);

		getLgp().getBuildings().forEach(b -> {
			if (b instanceof House h) {
				String map = h.getMap();
				try {
					JsonObject jo = (JsonObject) JsonParser.parse(new File("./res/maps/" + map));
					if (jo.containsKey("exit")) {
						JsonObject exit = (JsonObject) jo.get("exit");
						if (getTileManager().getPath().endsWith( ((StringValue) exit.get("map")).getValue())) {
							JsonArray	spawnPosition	= (JsonArray) exit.get("spawnPosition");
							Point2D		p				= new Point2D( ((NumberValue) spawnPosition.get(0)).getValue().longValue(),
									((NumberValue) spawnPosition.get(1)).getValue().longValue());
							Circle		respawn			= new Circle(p.getX() * getScalingFactorX(), p.getY() * getScalingFactorY(),
									15,
									Color.color(0, 1, 0, .75));
							getPointGroup().getChildren().add(respawn);
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				}
			}
		});

		tileManager.reload();
		aktionbutton.f11Scale();
		if (tileManager.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileManager.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(getGameWidth(), getGameHeight() + getScalingFactorY(), false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		player.setLayer(tileManager.getPlayerLayer());
		getLgp().setBuildings(tileManager.getBuildingsFromMap());

		getLgp().getPoints().put(tileManager.getSpawnPoint(), spawn);

		getLgp().getBuildings().forEach(b -> {
			if (b instanceof House h) {
				String map = h.getMap();
				try {
					JsonObject jo = (JsonObject) JsonParser.parse(new File("./res/maps/" + map));
					if (jo.containsKey("exit")) {
						JsonObject exit = (JsonObject) jo.get("exit");
						if (tileManager.getPath().endsWith( ((StringValue) exit.get("map")).getValue())) {
							JsonArray	spawnPosition	= (JsonArray) exit.get("spawnPosition");
							Point2D		p				= new Point2D( ((NumberValue) spawnPosition.get(0)).getValue().longValue(),
									((NumberValue) spawnPosition.get(1)).getValue().longValue());
							Circle		respawn			= new Circle(p.getX() * getScalingFactorX(), p.getY() * getScalingFactorY(),
									15,
									Color.color(0, 1, 0, .75));
							lgp.getPoints().put(p, respawn);
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				}
			}
		});
		getLgp().setNpcs(tileManager.getNPCSFromMap());
		getLgp().setTest(tileManager.getMobsFromMap());
		gamemenu.f11Scale();
		if (gamemenu != null && gamemenu.getInventory().getCurrentDemon() != null && gamemenu.getInventory().getCurrentDemon().getDemon() != null)
			getLgp().getNpcs().add(gamemenu.getInventory().getCurrentDemon().getDemon());

		// ImageView iv = new ImageView(Text.getInstance()
		// .convertText("ABCDEFGH\nIJKLMNOP\nQRSTUVWX\nYZabcdef\nghijklmn\nopqrstuv\nwxyz1234\n567890?!\n%\"# #",
		// 48));
		// ImageView iv = new ImageView(Text.getInstance()
		// .convertText("Programmieren ist LW"));
		// getChildren().add(iv);
		// TODO rem
	}

	/**
	 * Sets the block user inputs.
	 *
	 * @param blockUserInputs the new block user inputs
	 */
	public void setBlockUserInputs(boolean blockUserInputs) { this.blockUserInputs = blockUserInputs; }

	/**
	 * Sets the map.
	 *
	 * @param path the path
	 * @param position the position
	 */
	public void setMap(String path, double[] position) {

		getLayerGroup().getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		getPointGroup().getChildren().clear();

		tileManager.setMap(path);
		if (tileManager.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileManager.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(getGameWidth(), getGameHeight(), false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		if (!"".equals(tileManager.getOverlay())) getOverlay().setImage(ImgUtil.getScaledImage(this, "./res/gui/" + tileManager.getOverlay()));
		else getOverlay().setImage(null);

		lgp.setBuildings(tileManager.getBuildingsFromMap());
		lgp.setNpcs(tileManager.getNPCSFromMap());
		lgp.setTest(tileManager.getMobsFromMap());
		Circle spawn = new Circle(tileManager.getSpawnPoint().getX() * getScalingFactorX(),
				tileManager.getSpawnPoint().getY() * getScalingFactorY(), 15,
				Color.color(0, 1, 0, .75));
		lgp.getPoints().put(tileManager.getSpawnPoint(), spawn);
		getPointGroup().getChildren().add(spawn);
		lgp.getBuildings().forEach(b -> {
			if (b instanceof House h) {
				String map = h.getMap();
				try {
					JsonObject jo = (JsonObject) JsonParser.parse(new File("./res/maps/" + map));
					if (jo.containsKey("exit")) {
						JsonObject exit = (JsonObject) jo.get("exit");
						if (tileManager.getPath().endsWith( ((StringValue) exit.get("map")).getValue())) {
							JsonArray	spawnPosition	= (JsonArray) exit.get("spawnPosition");
							Point2D		p				= new Point2D(
									((NumberValue) spawnPosition.get(0)).getValue().longValue(),
									((NumberValue) spawnPosition.get(1)).getValue().longValue());
							Circle		respawn			= new Circle(p.getX(), p.getY(), 15,
									Color.color(0, 1, 0, .75));
							lgp.getPoints().put(p, respawn);
							getPointGroup().getChildren().add(respawn);
						}
					}
				} catch (JsonParseException e) {
					e.printStackTrace();
				}
			}
		});

		if (position != null)
			player.setPosition(new double[] {
					position[0] * getScalingFactorX(), position[1] * getScalingFactorY()
			});
		else {
			double[] posi = tileManager.getStartingPosition();
			player.setPosition(new double[] {
					posi[0] * getScalingFactorX(), posi[1] * getScalingFactorY()
			});
		}
		player.setLayer(tileManager.getPlayerLayer());

		if (gamemenu != null && gamemenu.getInventory().getCurrentDemon() != null && gamemenu.getInventory().getCurrentDemon().getDemon() != null)
			getLgp().getNpcs().add(gamemenu.getInventory().getCurrentDemon().getDemon());
	}

	/**
	 * Start logic thread.
	 */
	public void startLogicThread() {

		frameTimes	= new ArrayList<>();
		lastFrame	= System.currentTimeMillis();

		AtomicReference<Runnable>	runnable	= new AtomicReference<>();
		AtomicReference<Timeline>	arTl		= new AtomicReference<>();
		Timeline					tl			= new Timeline(
				new KeyFrame(Duration.millis(1000 / targetFPS),
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
	 * Update.
	 */
	public void update() {

		long lastFrameTime = frameTimes.size() > 0 ? frameTimes.get(frameTimes.size() - 1) : 0;

		fpsLabel.setText(String.format("%.2f", 1000 / fps));
		fpsLabel.setLayoutX(gameWidth - fpsLabel.getWidth());

		try {
			player.update(lastFrameTime);
		} catch (ConcurrentModificationException e) {}

		selectTool.update();

		aktionbutton.update();

		for (Entry<Point2D, Circle> point : lgp.getPoints().entrySet()) {
			point.getValue().setCenterX(point.getKey().getX() * getScalingFactorX() - player.getX() + player.getScreenX());
			point.getValue().setCenterY(point.getKey().getY() * getScalingFactorY() - player.getY() + player.getScreenY());
		}

		tileManager.update();

		getLgp().update();

		long frameTime = System.currentTimeMillis() - lastFrame;
		lastFrame = System.currentTimeMillis();
		frameTimes.add(frameTime);
		fps = frameTimes.stream().mapToLong(l -> l).average().getAsDouble();
		while (frameTimes.size() > Math.pow(fps * 12, 1.2)) frameTimes.remove(0);

		Text.getInstance().update(lastFrameTime);

	}

}
