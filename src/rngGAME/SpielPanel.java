package rngGAME;

import java.util.*;
import buildings.Building;
import entity.*;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import tile.*;

public class SpielPanel extends Pane {

	public final int Bg = 48;
	public final int BildS = 20;
	public final int BildH = 11;
	public final int SpielLaenge = Bg * BildS;
	public final int SpielHoehe = Bg * BildH;

	private final int FPS = 60;


	private final ImageView inv;

	private final Input keyH;
	private final Player player;
	private final TileManager tileM;
	private final SelectTool selectTool;
	private final Group view;
	private List<Building> buildings;

	private List<NPC> npcs;

	public SpielPanel(Input keyH) {
		setPrefSize(SpielLaenge, SpielHoehe);

		this.keyH = keyH;

		player = new Player(this, getKeyH());

		selectTool = new SelectTool(this);

		tileM = new TileManager(this);

		inv = new ImageView(new Image(getClass().getResourceAsStream("/res/gui/Inv.png")));
		inv.setX(player.screenX - inv.getImage().getWidth() / 2 + 20);
		inv.setY(player.screenY - inv.getImage().getHeight() / 2);
		inv.setVisible(false);

		view = new Group();

		setMap("/res/maps/lavaMap2.txt");

		getChildren().addAll(tileM, view, selectTool, inv);

	}

	public void addBuildings() {
		view.getChildren().addAll(buildings);
	}

	public void addNPCs() {
		view.getChildren().addAll(npcs);
	}

	public List<Building> getBuildings() { return buildings; }

	public Input getKeyH() {
		return keyH;
	}

	public List<NPC> getNpcs() { return npcs; }

	public Player getPlayer() { return player; }

	public SelectTool getSelectTool() { return selectTool; }


	public TileManager getTileM() { return tileM; }

	public Group getViewGroup() { return view; }


	public void run() {
		update();

	}

	public void saveMap() {
		System.out.println("don");
		tileM.save();
		System.out.println("don2");
	}

	public void setMap(String path) {
		setMap(path, null);
	}


	public void setMap(String path, Map.Entry<Double, Double> position) {
		tileM.setMap(path);
		if (tileM.getBackgroundPath() != null)
			setBackground(new Background(
					new BackgroundImage(new Image(getClass().getResourceAsStream("/res/" + tileM.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(SpielLaenge, SpielHoehe, false, false, false, false))));
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		buildings = tileM.getBuildingsFromMap();
		npcs = tileM.getNPCSFromMap();
		if (position != null)
			player.setPosition(position);
		else player.setPosition(tileM.getStartingPosition());
		view.getChildren().clear();
		view.getChildren().add(player);
		addBuildings();
		addNPCs();
	}

	public void SST() {
		Timeline tl = new Timeline(
				new KeyFrame(Duration.millis(1000 / FPS),
						event -> {
							run();
						}));
		tl.setCycleCount(Animation.INDEFINITE);
		tl.play();
	}

	public void update() {

		player.update();

		selectTool.update();

		for (Building b: buildings) b.update(player, this);
		for (NPC n: npcs) n.update(player, this);

		List<Node> nodes = new ArrayList<>(view.getChildren());

		nodes.sort((n1, n2) -> {
			if (n1 instanceof Building b1) {
				if (n2 instanceof Building b2) return b1.isBackground() ^ b2.isBackground() ? b1.isBackground() ? -1 : 1
						: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
								n2.getLayoutY() + ((Pane) n2).getHeight());
				else return b1.isBackground() ? -1
						: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
								n2.getLayoutY() + ((Pane) n2).getHeight());
			} else if (n2 instanceof Building b2) return b2.isBackground() ? 1
					: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
							n2.getLayoutY() + ((Pane) n2).getHeight());
			else return Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
					n2.getLayoutY() + ((Pane) n2).getHeight());
		});

		view.getChildren().clear();
		view.getChildren().addAll(nodes);

		tileM.update();

		if (keyH.tabPressed) inv.setVisible(true);
		else inv.setVisible(false);

	}

}
