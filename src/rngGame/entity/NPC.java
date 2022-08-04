package rngGame.entity;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import rngGame.main.*;
import rngGame.tile.ImgUtil;

public class NPC extends Entity implements JsonValue {

	protected Map<String, List<Image>> images;
	protected String currentKey;
	protected double x, y;
	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected JsonObject npcData, origTextures;
	protected double fps;

	protected NPC() {
		iv = new ImageView();
		iv.setDisable(true);
		shape = new Polygon();
		shape.setVisible(false);
		shape.setFill(Color.color(0, 1, 1, 0.75));
		shape.setDisable(true);
		getChildren().addAll(iv, shape);
		currentKey = "idle";
	}

	public NPC(JsonObject npc, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		this();
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				requestorN.set(NPC.this);
				cm.show(NPC.this, e.getScreenX(), e.getScreenY());
			}
		});
		init(npc);
	}

	public NPC(NPC npc, List<NPC> npcs, SpielPanel gp) {
		this();
		x = npc.x;
		y = npc.y;
		shape.getPoints().addAll(npc.shape.getPoints());
		origWidth = npc.origWidth;
		origHeight = npc.origHeight;
		reqWidth = npc.reqWidth;
		reqHeight = npc.reqHeight;
		origTextures = npc.origTextures;
		images = npc.images;
		iv.setImage(npc.getFirstImage());
		npcData = npc.npcData;
		fps = npc.fps;
		npcs.add(this);
		gp.getViewGroup().getChildren().add(this);
	}

	protected List<Image> getAnimatedImages(String path) throws FileNotFoundException {
		List<Image> li = new ArrayList<>();
		Image img = new Image(new FileInputStream("./res/npc/" + path));
		for (int i = 0; i < img.getWidth(); i += origWidth) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
			li.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
		}
		return li;
	}

	protected void init(JsonObject npc) {
		x = ((NumberValue) ((JsonArray) npc.get("position")).get(0)).getValue().doubleValue();
		y = ((NumberValue) ((JsonArray) npc.get("position")).get(1)).getValue().doubleValue();
		origWidth = ((NumberValue) ((JsonArray) npc.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) npc.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) npc.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) npc.get("requestedSize")).get(1)).getValue().intValue();
		origTextures = (JsonObject) npc.get("textures");
		images = ((JsonObject) npc.get("textures")).entrySet().parallelStream()
				.map(s -> {
					try {
						return Map.entry(s.getKey(), getAnimatedImages(((StringValue) s.getValue()).getValue()));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						return null;
					}
				})
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		iv.setImage(getFirstImage());
		npcData = (JsonObject) npc.get("npcData");
		fps = ((NumberValue) npc.get("fps")).getValue().doubleValue();
		shape.getPoints().addAll(0d, 0d, 0d, iv.getImage().getHeight(), iv.getImage().getWidth(),
				iv.getImage().getHeight(), iv.getImage().getWidth(), 0d);
	}

	public boolean collides(GameObject collidable) {
		Shape intersect = Shape.intersect(collidable.getCollisionBox(), shape);
		return !intersect.getBoundsInLocal().isEmpty();
	}

	public Image getFirstImage() { return images.values().stream().findFirst().get().get(0); }

	public Polygon getPoly() { return shape; }

	public void setPosition(double layoutX, double layoutY) {
		x = layoutX;
		y = layoutY;
	}

	@Override
	public JsonValue toJsonValue() {
		JsonObject jo = new JsonObject();
		JsonArray requestedSize = new JsonArray();
		requestedSize.add(reqWidth);
		requestedSize.add(reqHeight);
		jo.put("requestedSize", requestedSize);
		jo.put("textures", origTextures);
		jo.put("npcData", npcData);
		JsonArray position = new JsonArray();
		position.add(x);
		position.add(y);
		jo.put("position", position);
		JsonArray originalSize = new JsonArray();
		originalSize.add(origWidth);
		originalSize.add(origHeight);
		jo.put("originalSize", originalSize);
		jo.put("fps", fps);
		jo.put("type", getClass().getSimpleName());
		return jo;
	}

	@Override
	public JsonValue toJsonValue(Function<Object, String> function) {
		return toJsonValue();
	}

	public void update(Player p, SpielPanel gp) {
		double screenX = x - p.worldX + p.screenX;
		double screenY = y - p.worldY + p.screenY;
		if (x + reqWidth > p.worldX - p.screenX
				&& x - reqWidth < p.worldX + p.screenX
				&& y + reqHeight > p.worldY - p.screenY
				&& y - reqHeight < p.worldY + p.screenY) {
			setVisible(true);
			setLayoutX(screenX);
			setLayoutY(screenY);
			List<Image> frames = images.get(currentKey);
			if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
				spriteCounter = System.currentTimeMillis();
				spriteNum++;
			}
			Image image = null;
			if (spriteNum >= frames.size()) spriteNum = 0;
			image = frames.get(spriteNum);
			iv.setImage(image);

			if (System.getProperty("coll").equals("true"))
				shape.setVisible(true);
			else
				shape.setVisible(false);

			if (!gp.getKeyH().h) setVisible(true);
			else setVisible(false);

		} else setVisible(false);
	}

}
