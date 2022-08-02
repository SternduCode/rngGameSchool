package buildings;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.sterndu.json.*;
import entity.Player;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import rngGAME.*;
import tile.ImgUtil;

public class Building extends Pane implements JsonValue {

	protected Map<String, List<Image>> images;
	protected String currentKey;
	protected double x, y;
	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected JsonObject buildingData, origTextures;
	protected String map;
	protected Polygon poly;
	protected ImageView iv;
	protected double fps;
	protected boolean background;
	private boolean slave = false;
	private List<Building> slaves;
	private Building master;

	protected long spriteCounter = 0;
	protected int spriteNum = 0;

	protected Building() {
		poly = new Polygon();
		poly.setVisible(false);
		poly.setFill(Color.color(0, 0, 1, 0.75));
		poly.setDisable(true);
		iv = new ImageView();
		iv.setDisable(true);
		getChildren().addAll(iv, poly);
		currentKey = "default";
	}

	public Building(Building building, List<Building> buildings, SpielPanel gp) {
		this();
		x = building.x;
		y = building.y;
		poly.getPoints().addAll(building.poly.getPoints());
		origWidth = building.origWidth;
		origHeight = building.origHeight;
		reqWidth = building.reqWidth;
		reqHeight = building.reqHeight;
		background = building.background;
		fps = building.fps;
		images = building.images;
		iv.setImage(building.getFirstImage());
		buildingData = building.buildingData;
		map = building.map;
		master = building;
		slave = true;
		if (building.slaves == null)
			building.slaves = new ArrayList<>();
		building.slaves.add(this);
		buildings.add(this);
		gp.getViewGroup().getChildren().add(this);
	}

	public Building(JsonObject building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		this();
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorB.set(Building.this);
				cm.show(Building.this, e.getScreenX(), e.getScreenY());
			}
		});
		if (building.containsKey("map")) map = ((StringValue) building.get("map")).getValue();
		buildings.add(this);
		origTextures = (JsonObject) building.get("textures");
		origWidth = ((NumberValue) ((JsonArray) building.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) building.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(1)).getValue().intValue();
		if (building.containsKey("fps"))
			fps = ((NumberValue) building.get("fps")).getValue().doubleValue();
		else fps = 7;
		if (building.containsKey("background"))
			background = ((BoolValue) building.get("background")).getValue();
		images = ((JsonObject) building.get("textures")).entrySet().parallelStream()
				.map(s -> Map.entry(s.getKey(), getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		iv.setImage(getFirstImage());
		buildingData = (JsonObject) building.get("buildingData");
		if (((JsonArray) building.get("position")).get(0) instanceof JsonArray ja) {
			try {
				slaves = new ArrayList<>();
				for (int i = 1; i < ((JsonArray) building.get("position")).size(); i++) {
					Building b = this.getClass().getDeclaredConstructor().newInstance();
					b.x = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(i)).get(0)).getValue()
							.doubleValue();
					b.y = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(i)).get(1)).getValue()
							.doubleValue();
					b.poly.getPoints().addAll(poly.getPoints());
					b.origWidth = origWidth;
					b.origHeight = origHeight;
					b.reqWidth = reqWidth;
					b.reqHeight = reqHeight;
					b.background = background;
					b.fps = fps;
					b.images = images;
					b.iv.setImage(b.getFirstImage());
					b.buildingData = buildingData;
					b.map = map;
					b.master = this;
					b.slave = true;
					slaves.add(b);
					buildings.add(b);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			x = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(0)).get(0)).getValue()
					.doubleValue();
			y = ((NumberValue) ((JsonArray) ((JsonArray) building.get("position")).get(0)).get(1)).getValue()
					.doubleValue();
		} else {
			x = ((NumberValue) ((JsonArray) building.get("position")).get(0)).getValue().doubleValue();
			y = ((NumberValue) ((JsonArray) building.get("position")).get(1)).getValue().doubleValue();
		}
	}

	private List<Image> getAnimatedImages(String key, String path) {
		List<Image> li = new ArrayList<>();
		Image img = new Image(getClass().getResourceAsStream("/res/building/" + path));

		for (int i = 0; i < img.getWidth(); i += origWidth) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
			li.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
		}
		String[] sp = path.split("[.]");
		if (getClass().getResource("/res/collisions/building/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
		+ ".collisionbox") != null) try {
			RandomAccessFile raf = new RandomAccessFile(getClass()
					.getResource("/res/collisions/building/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
					+ ".collisionbox")
					.getFile(), "rws");
			raf.seek(0l);
			int length = raf.readInt();
			for (int i = 0; i < length; i++) poly.getPoints().add(raf.readDouble());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return li;
	}

	public boolean collides(Collidable collidable) {
		if (poly.getPoints().size() > 0) {
			Shape intersect = Shape.intersect(collidable.getPoly(), poly);
			return !intersect.getBoundsInLocal().isEmpty();
		} else return false;
	}

	public Image getFirstImage() {
		return images.values().stream().findFirst().get().get(0);
	}

	public Polygon getPolygon() { return poly; }

	public boolean isBackground() { return background;
	}

	public void setPosition(double layoutX, double layoutY) {
		x = layoutX;
		y = layoutY;
	}

	@Override
	public String toJson() {
		if (!slave) {
			JsonObject jo = new JsonObject();
			jo.put("type", getClass().getSimpleName());
			jo.put("textures", origTextures);
			JsonArray position = new JsonArray();
			if (slaves == null || slaves.size() == 0) {
				position.add(x);
				position.add(y);
			} else {
				JsonArray pos = new JsonArray();
				pos.add(x);
				pos.add(y);
				position.add(pos);
				for (Building b: slaves) {
					pos = new JsonArray();
					pos.add(b.x);
					pos.add(b.y);
					position.add(pos);
				}
			}
			jo.put("position", position);
			JsonArray originalSize = new JsonArray();
			originalSize.add(origWidth);
			originalSize.add(origHeight);
			jo.put("originalSize", originalSize);
			JsonArray requestedSize = new JsonArray();
			requestedSize.add(reqWidth);
			requestedSize.add(reqHeight);
			jo.put("requestedSize", requestedSize);
			if (background) jo.put("background", background);
			if (map != null) jo.put("map", map);
			jo.put("buildingData", buildingData);
			System.out.println(slaves);
			System.out.println(jo.toJson());
			return jo.toJson();
		} else return "";
	}

	@Override
	public String toJson(Function<Object, String> function) {
		return toJson();
	}

	public void update(Player p, SpielPanel gp) {
		if (System.getProperty("coll").equals("true"))
			poly.setVisible(true);
		else
			poly.setVisible(false);

		if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
			spriteCounter = System.currentTimeMillis();
			spriteNum++;
		}

		if (spriteNum >= images.get(currentKey).size()) spriteNum = 0;
		iv.setImage(images.get(currentKey).get(spriteNum));
		double screenX = x - p.worldX + p.screenX;
		double screenY = y - p.worldY + p.screenY;
		if (x + reqWidth > p.worldX - p.screenX
				&& x - reqWidth < p.worldX + p.screenX
				&& y + reqHeight > p.worldY - p.screenY
				&& y - reqHeight < p.worldY + p.screenY) {
			setVisible(true);
			setLayoutX(screenX);
			setLayoutY(screenY);

			if (!gp.getKeyH().b) setVisible(true);
			else setVisible(false);
		} else setVisible(false);
	}

}