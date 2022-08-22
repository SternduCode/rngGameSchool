package rngGame.main;

import java.io.*;
import java.util.*;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import rngGame.buildings.Building;
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


	private final int FPS = 60;

	private final ImageView inv;
	private final Input keyH;
	private final Player player;
	private final TileManager tileM;
	private final SelectTool selectTool;
	private final GroupGroup layerGroup;

	private List<Building> buildings;

	private List<NPC> npcs;

	public SpielPanel(Input keyH) throws FileNotFoundException {
		setPrefSize(SpielLaenge, SpielHoehe);

		this.keyH = keyH;

		layerGroup = new GroupGroup();

		layerGroup.getChildren().add(new Group());

		selectTool = new SelectTool(this);

		tileM = new TileManager(this);

		player = new Player(this, tileM.getNpcCM(), tileM.getRequestorN());

		inv = new ImageView(new Image(new FileInputStream("./res/gui/Inv.png")));
		inv.setX(player.screenX - inv.getImage().getWidth() / 2 + 20);
		inv.setY(player.screenY - inv.getImage().getHeight() / 2);
		inv.setVisible(false);

		setMap("./res/maps/lavaMap2.json");

		getChildren().addAll(tileM, layerGroup, selectTool, inv);
	}

	public List<Building> getBuildings() { return buildings; }

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
		npcs = tileM.getNPCSFromMap();
	}

	public void saveMap() {
		System.out.println("don");
		tileM.save();
		System.out.println("don2");
	}

	public void setMap(String path) {
		setMap(path, null);
	}

	public void setMap(String path, Double[] position) {
		layerGroup.getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
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
		if (position != null)
			player.setPosition(position);
		else player.setPosition(tileM.getStartingPosition());
		player.setLayer(tileM.getPlayerLayer());
	}


	public void SST() {
		Timeline tl = new Timeline(
				new KeyFrame(Duration.millis(1000 / FPS),
						event -> {
							update();
						}));
		tl.setCycleCount(Animation.INDEFINITE);
		tl.play();
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

		player.update();

		selectTool.update();

		for (Building b: buildings) b.update();
		for (Entity n: npcs) n.update();

		for (Node layer: layerGroup.getChildren()) {
			Group view = (Group) layer;
			List<Node> nodes = new ArrayList<>(view.getChildren());

			nodes.sort((n1, n2) -> {
				if (n1 instanceof GameObject b1) {
					if (n2 instanceof GameObject b2)
						return b1.isBackground() ^ b2.isBackground() ? b1.isBackground() ? -1 : 1
								: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
										n2.getLayoutY() + ((Pane) n2).getHeight());
					else return b1.isBackground() ? -1
							: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
									n2.getLayoutY() + ((Pane) n2).getHeight());
				} else if (n2 instanceof GameObject b2) return b2.isBackground() ? 1
						: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
								n2.getLayoutY() + ((Pane) n2).getHeight());
				else return Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
						n2.getLayoutY() + ((Pane) n2).getHeight());
			});

			view.getChildren().clear();
			view.getChildren().addAll(nodes);
		}

		tileM.update();

		if (keyH.tabPressed) inv.setVisible(true);
		else inv.setVisible(false);

	}

}
