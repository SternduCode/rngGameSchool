package rngGame.main;

import java.io.*;
import java.util.*;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import rngGame.buildings.Building;
import rngGame.entity.*;
import rngGame.tile.*;
import rngGame.ui.SoundHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class GamePanel.
 */
public class GamePanel extends Pane {

	/** The difficulty. */
	private Difficulty difficulty;

	/** The input. */
	private final Input input;

	/** The buildings. */
	private List<Building> buildings;

	/** The npcs. */
	private List<NPC> npcs;

	/** The test. */
	private List<MobRan> test;

	/** The fps. */
	private Double fps = 0d;

	/** The points. */
	private final Map<Point2D, Circle> points;

	/** The fps. */
	private final int FPS = 80;

	/** The bubble. */
	private final Pane bubble;

	/** The mp. */
	private MediaPlayer mp;

	/** The sgp. */
	private rngGame.visual.GamePanel vgp;

	/** The frame times. */
	private List<Long> frameTimes;

	/** The last frame. */
	private Long lastFrame;

	/** The clipboard. */
	private List<List<TextureHolder>> clipboard;

	/**
	 * Instantiates a new game panel.
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	public GamePanel() throws FileNotFoundException {

		frameTimes	= new ArrayList<>();
		lastFrame	= System.currentTimeMillis();

		bubble = new Pane();

		difficulty = Difficulty.EASY;

		input = Input.getInstance();

		points = new HashMap<>();

		clipboard = new ArrayList<>();

		setVgp(new rngGame.visual.GamePanel(this));

	}

	/**
	 * Gets the bubble.
	 *
	 * @return the bubble
	 */
	public Pane getBubble() {
		return bubble;
	}

	/**
	 * Gets the buildings.
	 *
	 * @return the buildings
	 */
	public List<Building> getBuildings() { return buildings; }

	/**
	 * Gets the clipboard.
	 *
	 * @return the clipboard
	 */
	public List<List<TextureHolder>> getClipboard() { return clipboard; }

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
	 * Gets the points.
	 *
	 * @return the points
	 */
	public Map<Point2D, Circle> getPoints() { return points; }

	/**
	 * Gets the vgp.
	 *
	 * @return the vgp
	 */
	public rngGame.visual.GamePanel getVgp() { return vgp; }

	/**
	 * Make sound.
	 *
	 * @param soundname the soundname
	 */
	public void makeSound(String soundname){
		MediaPlayer mp = new MediaPlayer(new Media(new File("./res/music/" + soundname).toURI().toString()));
		mp.setAutoPlay(true);
		mp.setVolume(.2);
	}

	/**
	 * Reload.
	 */
	public void reload() {
		getPoints().clear();

		vgp.reload();
	}

	/**
	 * Save map.
	 */
	public void saveMap() {
		vgp.getTileManager().save();
		System.out.println("don");
	}

	/**
	 * Sets the buildings.
	 *
	 * @param buildings the new buildings
	 */
	public void setBuildings(List<Building> buildings) { this.buildings = buildings; }

	/**
	 * Sets the clipboard.
	 *
	 * @param clipboard the new clipboard
	 */
	public void setClipboard(List<List<TextureHolder>> clipboard) {
		this.clipboard = clipboard;
	}

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

		LoadingScreen.getInstance().goIntoLoadingScreen();

		UndoRedo.getInstance().clearActions();

		SoundHandler.getInstance().endBackgroundMusic();


		getPoints().clear();

		vgp.setMap(path, position);

		if (!"".equals(vgp.getTileManager().getBackgroundMusic())) {
			SoundHandler.getInstance().setBackgroundMusic(vgp.getTileManager().getBackgroundMusic());
		} else mp = null;

		new Thread(() -> {
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			// LoadingScreen.INSTANCE.goOutOfLoadingScreen(); With 500ms run time
			FadeTransition ft = new FadeTransition(Duration.millis(500), LoadingScreen.getInstance());
			ft.setFromValue(1);
			ft.setToValue(0);
			ft.play();
		}).start();

	}

	/**
	 * Sets the npcs.
	 *
	 * @param npcs the new npcs
	 */
	public void setNpcs(List<NPC> npcs) { this.npcs = npcs; }

	/**
	 * Sets the test.
	 *
	 * @param test the new test
	 */
	public void setTest(List<MobRan> test) { this.test = test; }

	/**
	 * Sets the vgp.
	 *
	 * @param vgp the new vgp
	 */
	public void setVgp(rngGame.visual.GamePanel vgp) { this.vgp = vgp; }

	/**
	 * Sst.
	 */
	public void SST() {

		frameTimes	= new ArrayList<>();
		lastFrame	= System.currentTimeMillis();

		AtomicReference<Runnable>	runnable	= new AtomicReference<>();
		AtomicReference<Timeline>	arTl		= new AtomicReference<>();
		Timeline					tl			= new Timeline(
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
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "SpielPanel ["
				+ ", input=" + input + ", buildings=" + buildings
				+ ", npcs=" + npcs
				+ "]";
	}

	/**
	 * Update.
	 */
	public void update() {

		long lastFrameTime = frameTimes.size() > 0 ? frameTimes.get(frameTimes.size() - 1) : 0;

		input.update(lastFrameTime);

		try {
			for (Building b : buildings) b.update(lastFrameTime);
			for (Entity n : npcs) n.update(lastFrameTime);
			for (Entity n : test) n.update(lastFrameTime);
		} catch (ConcurrentModificationException e) {}

		for (Node layer : getVgp().getLayerGroup().getChildren()) {
			Group		view	= (Group) layer;
			List<Node>	nodes	= new ArrayList<>(view.getChildren());

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

		if ("true".equals(System.getProperty("edit"))) getVgp().getPointGroup().setVisible(true);
		else getVgp().getPointGroup().setVisible(false);

		long frameTime = System.currentTimeMillis() - lastFrame;
		lastFrame = System.currentTimeMillis();
		frameTimes.add(frameTime);
		fps = frameTimes.stream().mapToLong(l -> l).average().getAsDouble();
		while (frameTimes.size() > Math.pow(fps * 12, 1.2)) frameTimes.remove(0);

		SoundHandler.getInstance().update();

	}

}
