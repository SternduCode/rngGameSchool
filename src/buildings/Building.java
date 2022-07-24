package buildings;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.sterndu.json.*;
import entity.Player;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import rngGAME.SpielPanel;
import tile.ImgUtil;

public class Building extends Pane {

	protected final Map<String, List<Image>> images;
	protected String currentKey;
	protected final double x, y;
	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected final JsonObject buildingData;
	protected String map;
	protected Polygon poly;
	protected ImageView iv;

	protected int spriteCounter = 0;
	protected int spriteNum = 0;

	public Building(JsonObject building) {
		poly = new Polygon();
		poly.setVisible(false);
		iv = new ImageView();
		getChildren().addAll(iv, poly);
		if (building.containsKey("map")) map = ((StringValue) building.get("map")).getValue();
		x = ((NumberValue) ((JsonArray) building.get("position")).get(0)).getValue().doubleValue();
		y = ((NumberValue) ((JsonArray) building.get("position")).get(1)).getValue().doubleValue();
		origWidth = ((NumberValue) ((JsonArray) building.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) building.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(1)).getValue().intValue();
		images = ((JsonObject) building.get("textures")).entrySet().parallelStream()
				.map(s -> Map.entry(s.getKey(), getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		iv.setImage(images.values().stream().findFirst().get().get(0));
		buildingData = (JsonObject) building.get("buildingData");
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
		System.out.println(String.join(".", Arrays.copyOf(sp, sp.length - 1))
				+ ".collisionbox");
		if (getClass().getResource("/res/collisions/building/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
		+ ".collisionbox") != null) try {
			System.out.println(String.join(".", Arrays.copyOf(sp, sp.length - 1))
					+ ".collisionbox");
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

	public Polygon getPolygon() { return poly; }

	public void update(Player p, SpielPanel gp) {
		System.out.println(!Shape.intersect(p.getShape(), poly).getBoundsInLocal().isEmpty());
		if (System.getProperty("coll").equals("true"))
			poly.setVisible(true);
		else
			poly.setVisible(false);
		// TODO coll

		spriteCounter++;
		if (spriteCounter > 30 / images.get(currentKey).size()) {
			spriteNum++;
			spriteCounter = 0;
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
			//			iv.setX(screenX);
			//			iv.setY(screenY);
		} else setVisible(false);
		//		setWidth(iv.getImage().getWidth());
	}

}