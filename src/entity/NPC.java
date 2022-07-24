package entity;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.sterndu.json.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import rngGAME.*;
import tile.ImgUtil;

public class NPC extends Entity implements Collidable {

	protected Map<String, List<Image>> images;
	protected String currentKey;
	protected double x, y;
	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected JsonObject npcData;
	protected int fps;

	public NPC(JsonObject npc) {
		init(npc);
	}

	protected List<Image> getAnimatedImages(String path) {
		List<Image> li = new ArrayList<>();
		Image img = new Image(getClass().getResourceAsStream("/res/npc/" + path));
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
		images = ((JsonObject) npc.get("textures")).entrySet().parallelStream()
				.map(s -> Map.entry(s.getKey(), getAnimatedImages(((StringValue) s.getValue()).getValue())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		iv = new ImageView();
		iv.setImage(images.values().stream().findFirst().get().get(0));
		npcData = (JsonObject) npc.get("npcData");
		currentKey = "idle";
		fps = ((NumberValue) npc.get("fps")).getValue().intValue();
		shape = new Rectangle(48, 48);
		shape.setFill(Color.color(0, 1, 1, 0.75));
		getChildren().addAll(iv, shape);
	}

	public boolean collides(Collidable collidable) {
		Shape intersect = Shape.intersect(collidable.getPoly(), shape);
		return !intersect.getBoundsInLocal().isEmpty();
	}

	@Override
	public Rectangle getPoly() { return shape; }

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

			shape.setTranslateX(0);
			shape.setTranslateY(0);
			if (System.getProperty("coll").equals("true"))
				shape.setVisible(true);
			else
				shape.setVisible(false);

		} else setVisible(false);
	}

}
