package rngGame.main;

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
import rngGame.buildings.*;
import rngGame.entity.*;
import rngGame.tile.*;

public class SpielPanel extends Pane {

	class GroupGroup extends Group {

		class OutList extends AbstractList<Group> {
			ObservableList<Node> li;

			public OutList(ObservableList<Node> li) {
				this.li = li;
			}

			@Override
			public void add(int index, Group element) {
				li.add(index, element);
			}

			@Override
			public Group get(int index) {
				int size = size();
				if (index >= li.size()) for (int i = 0; i <= index - size; i++) li.add(new Group());
				return (Group) li.get(index);
			}

			@Override
			public Group remove(int index) {
				return (Group) li.remove(index);
			}

			@Override
			public Group set(int index, Group element) {
				return (Group) li.set(index, element);
			}

			@Override
			public int size() {
				return li.size();
			}
		}

		public List<Group> getGroupChildren() {
			return new OutList(super.getChildren());
		}
	}
	public final int Bg = 48;
	public final int BildS = 20;
	public final int BildH = 11;
	public final int SpielLaenge = Bg * BildS;

	public final int SpielHoehe = Bg * BildH;


	private final int FPS = 80;

	private final ImageView inv;
	private final Input keyH;
	private final Player player;
	private final TileManager tileM;
	private final SelectTool selectTool;
	private final GroupGroup layerGroup;
	private final Group pointGroup;

	private List<Building> buildings;

	private List<NPC> npcs;

	private List<Long> frameTimes;
	private Long lastFrame;
	private Double fps = 0d;

	private final Label fpsLabel;

	private final Map<Point2D, Circle> points;

	public SpielPanel() throws FileNotFoundException {
		setPrefSize(SpielLaenge, SpielHoehe);

		keyH = Input.getInstance();

		pointGroup = new Group();
		pointGroup.setDisable(true);

		points = new HashMap<>();

		layerGroup = new GroupGroup();
		layerGroup.getChildren().add(new Group());

		selectTool = new SelectTool(this);

		tileM = new TileManager(this);

		player = new Player(this, tileM.getCM(), tileM.getRequestorN());

		fpsLabel = new Label(fps + "");
		fpsLabel.setBackground(new Background(new BackgroundFill(Color.color(.5, .5, .5, .5), null, null)));
		fpsLabel.setTextFill(Color.color(.1, .1, .1));
		fpsLabel.setDisable(true);
		fpsLabel.setVisible(false);

		inv = new ImageView(new Image(new FileInputStream("./res/gui/Inv.png")));
		inv.setX(player.screenX - inv.getImage().getWidth() / 2 + 20);
		inv.setY(player.screenY - inv.getImage().getHeight() / 2);
		inv.setVisible(false);

		setMap("./res/maps/lavaMap2.json");

		getChildren().addAll(tileM, layerGroup, pointGroup, selectTool, inv, fpsLabel);
	}

	public List<Building> getBuildings() { return buildings; }

	public Double getFps() { return fps; }

	public Input getKeyH() {
		return keyH;
	}

	public List<NPC> getNpcs() { return npcs; }

	public Player getPlayer() { return player; }

	public SelectTool getSelectTool() { return selectTool; }


	public TileManager getTileM() { return tileM; }

	public List<Group> getViewGroups() { return layerGroup.getGroupChildren(); }

	public void reload() {
		layerGroup.getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		points.clear();
		pointGroup.getChildren().clear();
		tileM.reload();
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
		player.setLayer(tileM.getPlayerLayer());
		buildings = tileM.getBuildingsFromMap();
		Circle spawn = new Circle(tileM.getSpawnPoint().getX(), tileM.getSpawnPoint().getY(), 15,
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
		npcs = tileM.getNPCSFromMap();
	}

	public void saveMap() {
		tileM.save();
		System.out.println("don");
	}

	public void setMap(String path) {
		setMap(path, null);
	}


	public void setMap(String path, double[] position) {
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
		buildings = tileM.getBuildingsFromMap();
		npcs = tileM.getNPCSFromMap();
		Circle spawn = new Circle(tileM.getSpawnPoint().getX(), tileM.getSpawnPoint().getY(), 15,
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
							Point2D p = new Point2D(((NumberValue) spawnPosition.get(0)).getValue().longValue(),
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
			player.setPosition(position);
		else player.setPosition(tileM.getStartingPosition());
		player.setLayer(tileM.getPlayerLayer());

	}

	public void SST() {

		frameTimes = new ArrayList<>();
		lastFrame = System.currentTimeMillis();

		AtomicReference<Runnable> runnable = new AtomicReference<>();
		AtomicReference<Timeline> arTl = new AtomicReference<>();
		Timeline tl = new Timeline(
				new KeyFrame(Duration.millis(1000 / FPS),
						event -> {
							update();
							if (System.getProperty("alternateUpdate").equals("true")) {
								arTl.get().stop();
								Platform.runLater(runnable.get());
							}
						}));
		arTl.set(tl);
		tl.setCycleCount(Animation.INDEFINITE);
		Runnable r = () -> {
			update();
			if (!MainClass.isStopping() && System.getProperty("alternateUpdate").equals("true"))
				Platform.runLater(runnable.get());
			else arTl.get().play();
		};
		runnable.set(r);

		if (System.getProperty("alternateUpdate").equals("false")) tl.play();
		else Platform.runLater(r);
	}

	public void toggleFpssLabelVisible() {
		fpsLabel.setVisible(!fpsLabel.isVisible());
	}

	@Override
	public String toString() {
		return "SpielPanel [inv=" + inv
				+ ", keyH=" + keyH + ", player=" + player + ", tileM=" + tileM + ", selectTool="
				+ selectTool + ", layerGroup=" + layerGroup.getChildren().size() + ", buildings=" + buildings
				+ ", npcs=" + npcs
				+ "]";
	}

	public void update() {

		long lastFrameTime = frameTimes.size() > 0 ? frameTimes.get(frameTimes.size() - 1) : 0;

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
		} catch (ConcurrentModificationException e) {
		}

		for (Node layer: layerGroup.getChildren()) {
			Group view = (Group) layer;
			List<Node> nodes = new ArrayList<>(view.getChildren());

			nodes.sort((n1, n2) -> {
				if (n1 instanceof GameObject b1) {
					if (n2 instanceof GameObject b2)
						return b1.isBackground() ^ b2.isBackground() ? b1.isBackground() ? -1 : 1
								: Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
										n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
					else return b1.isBackground() ? -1
							: Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
									n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
				} else if (n2 instanceof GameObject b2) return b2.isBackground() ? 1
						: Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
								n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
				else return Double.compare(n1.getLayoutY() + ((GameObject) n1).getTextureHeight(),
						n2.getLayoutY() + ((GameObject) n2).getTextureHeight());
			});

			view.getChildren().clear();
			view.getChildren().addAll(nodes);
		}

		for (Entry<Point2D, Circle> point: points.entrySet()) {
			point.getValue().setCenterX(point.getKey().getX() - player.x + player.screenX);
			point.getValue().setCenterY(point.getKey().getY() - player.y + player.screenY);
		}

		if (System.getProperty("edit").equals("true")) pointGroup.setVisible(true);
		else pointGroup.setVisible(false);

		tileM.update();

		if (keyH.tabPressed) inv.setVisible(true);
		else inv.setVisible(false);

		long frameTime = System.currentTimeMillis() - lastFrame;
		lastFrame = System.currentTimeMillis();
		frameTimes.add(frameTime);
		fps = frameTimes.stream().mapToLong(l -> l).average().getAsDouble();
		while (frameTimes.size() > fps + 20) frameTimes.remove(0);

	}

}
