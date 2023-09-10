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
		setPrefSize(WindowManager.getInstance().getGameWidth(), WindowManager.getInstance().getGameHeight());

		frameTimes	= new ArrayList<>();
		lastFrame	= System.currentTimeMillis();

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
		tileManager.setPrefSize(WindowManager.getInstance().getGameWidth(), WindowManager.getInstance().getGameHeight());

		player = new Player(this, tileManager.getCM(), tileManager.getRequestorN());

		aktionbutton = new AktionButton(this);

		lgp.setMap("./res/maps/lavaMap2.json");
		gamemenu = new TabMenu(getLgp());

		getChildren().addAll(tileManager, getLayerGroup(), getOverlay(), getPointGroup(), selectTool, aktionbutton, lgp.getBubble(), bubbleText,
				gamemenu, fpsLabel,
				getLoadingScreen());
	}

	/**
	 * Adds the animated image.
	 *
	 * @param animatedImage the animated image
	 */
	public void addAnimatedImage(AnimatedImage animatedImage) {
		animatedImages.add(animatedImage);
	}

	/**
	 * Scale textures.
	 *
	 * @param scaleFactorX the scale factor X
	 * @param scaleFactorY the scale factor Y
	 */
	public void changeScalingFactor(double scaleFactorX, double scaleFactorY) {
		player.setPosition(player.getX() * (scaleFactorX / WindowManager.getInstance().getScalingFactorX() ),
				player.getY() * (scaleFactorY / WindowManager.getInstance().getScalingFactorY() ));
		WindowManager.getInstance().changeScalingFactor(scaleFactorX, scaleFactorY);
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
	 * Gets the gamemenu.
	 *
	 * @return the gamemenu
	 */
	public TabMenu getGamemenu() { return gamemenu; }

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
	public TileManager getTileManager() { return tileManager; }

	/**
	 * Gets the view groups.
	 *
	 * @return the view groups
	 */
	public List<Group> getViewGroups() { return getLayerGroup().getGroupChildren(); }

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
			getOverlay().setImage(ImgUtil.getScaledImage("./res/gui/" + getTileManager().getOverlay()));
		else getOverlay().setImage(null);

		tileManager.reload();
		tileManager.update();
		aktionbutton.f11Scale();
		if (tileManager.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileManager.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(WindowManager.getInstance().getGameWidth(), WindowManager.getInstance().getGameHeight() + WindowManager.getInstance().getScalingFactorY(), false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		player.setLayer(tileManager.getPlayerLayer());

		getLgp().setBuildings(tileManager.getBuildingsFromMap());
		getLgp().setNpcs(tileManager.getNPCSFromMap());
		getLgp().setTest(tileManager.getMobsFromMap());

		Circle spawn = new Circle(getTileManager().getSpawnPoint().getX() * WindowManager.getInstance().getScalingFactorX(),
				getTileManager().getSpawnPoint().getY() * WindowManager.getInstance().getScalingFactorY(), 15,
				Color.color(0, 1, 0, .75));
		lgp.getPoints().put(tileManager.getSpawnPoint(), spawn);
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
							Circle		respawn			= new Circle(p.getX() * WindowManager.getInstance().getScalingFactorX(), p.getY() * WindowManager.getInstance().getScalingFactorY(),
									15,
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

		gamemenu.f11Scale();
		if (gamemenu != null && gamemenu.getInventory().getCurrentDemon() != null && gamemenu.getInventory().getCurrentDemon().getDemon() != null)
			getLgp().getNpcs().add(gamemenu.getInventory().getCurrentDemon().getDemon());

	}

	/**
	 * Sets the block user inputs.
	 *
	 * @param blockUserInputs the new block user inputs
	 */
	public void setBlockUserInputs(boolean blockUserInputs) { this.blockUserInputs = blockUserInputs; }

	public void setLayout(Positions pos, ImageView bild) {
		
		int px = (int) (pos.x*WindowManager.getInstance().getScalingFactorX());
		int py = (int) (pos.y*WindowManager.getInstance().getScalingFactorY());
		
		int bx = (int) bild.getImage().getWidth();
		int by = (int) bild.getImage().getHeight();
		
		switch (pos) {
			case Topleft 		-> 	{bild.setLayoutX(px); 		bild.setLayoutY(py);} 
			case Topmiddle 		-> 	{bild.setLayoutX(px-bx/2); 	bild.setLayoutY(py);}
			case Topright 		-> 	{bild.setLayoutX(px-bx); 	bild.setLayoutY(py);}
		
			case Middleleft 	-> 	{bild.setLayoutX(px); 		bild.setLayoutY(py-by/2);}
			case Mcenter 		-> 	{bild.setLayoutX(px-bx/2); 	bild.setLayoutY(py-by/2);}
			case Middleright 	->	{bild.setLayoutX(px-by); 	bild.setLayoutY(py-by/2);}
		
			case Bottomleft 	-> 	{bild.setLayoutX(px); 		bild.setLayoutY(py-by);}
			case Bottommiddle 	-> 	{bild.setLayoutX(px-bx/2); 	bild.setLayoutY(py-by);}
			case Bottomright 	-> 	{bild.setLayoutX(px-bx);    bild.setLayoutY(py-by);}
		}
	}
	
	public void setLayout(int x, int y, ImageView bild) {
		bild.setLayoutX(x);
		bild.setLayoutY(y);
	}
	
	
	
	
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
							new BackgroundSize(WindowManager.getInstance().getGameWidth(), WindowManager.getInstance().getGameHeight(), false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

		if (!"".equals(tileManager.getOverlay())) getOverlay().setImage(ImgUtil.getScaledImage("./res/gui/" + tileManager.getOverlay()));
		else getOverlay().setImage(null);

		lgp.setBuildings(tileManager.getBuildingsFromMap());
		lgp.setNpcs(tileManager.getNPCSFromMap());
		lgp.setTest(tileManager.getMobsFromMap());
		Circle spawn = new Circle(tileManager.getSpawnPoint().getX() * WindowManager.getInstance().getScalingFactorX(),
				tileManager.getSpawnPoint().getY() * WindowManager.getInstance().getScalingFactorY(), 15,
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
					position[0] * WindowManager.getInstance().getScalingFactorX(), position[1] * WindowManager.getInstance().getScalingFactorY()
			});
		else {
			double[] posi = tileManager.getStartingPosition();
			player.setPosition(new double[] {
					posi[0] * WindowManager.getInstance().getScalingFactorX(), posi[1] * WindowManager.getInstance().getScalingFactorY()
			});
		}
		player.setLayer(tileManager.getPlayerLayer());

		if (gamemenu != null && gamemenu.getInventory().getCurrentDemon() != null && gamemenu.getInventory().getCurrentDemon().getDemon() != null)
			getLgp().getNpcs().add(gamemenu.getInventory().getCurrentDemon().getDemon());
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
		fpsLabel.setLayoutX(WindowManager.getInstance().getGameWidth() - fpsLabel.getWidth());

		try {
			player.update(lastFrameTime);
		} catch (ConcurrentModificationException e) {}

		selectTool.update();

		aktionbutton.update();

		for (Entry<Point2D, Circle> point : lgp.getPoints().entrySet()) {
			point.getValue().setCenterX(point.getKey().getX() * WindowManager.getInstance().getScalingFactorX() - player.getX() + player.getScreenX());
			point.getValue().setCenterY(point.getKey().getY() * WindowManager.getInstance().getScalingFactorY() - player.getY() + player.getScreenY());
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
