package buildings;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import com.sterndu.json.*;
import entity.Player;
import javafx.scene.image.*;
import rngGAME.SpielPanel;
import tile.ImgUtil;

public class Building extends ImageView {

	protected final Map<String, List<Image>> images;
	protected final Map<String, List<Image>> colls;
	protected String currentKey;
	protected final double x, y;
	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected final JsonObject buildingData;
	protected String map;

	protected int spriteCounter = 0;
	protected int spriteNum = 0;

	public Building(JsonObject building) {
		if (building.containsKey("map")) map = ((StringValue) building.get("map")).getValue();
		x = ((NumberValue) ((JsonArray) building.get("position")).get(0)).getValue().doubleValue();
		y = ((NumberValue) ((JsonArray) building.get("position")).get(1)).getValue().doubleValue();
		origWidth = ((NumberValue) ((JsonArray) building.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) building.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(1)).getValue().intValue();
		colls = new HashMap<>();
		images = ((JsonObject) building.get("textures")).entrySet().parallelStream()
				.map(s -> Map.entry(s.getKey(), getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		setImage(images.values().stream().findFirst().get().get(0));
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
		if (getClass().getResource("/res/collisions/building/" + path) != null) {
			List<Image> cli = new ArrayList<>();
			Image cimg = new Image(getClass().getResourceAsStream("/res/collisions/building/" + path));
			for (int i = 0; i < cimg.getWidth(); i += origWidth) {
				WritableImage wi = new WritableImage(cimg.getPixelReader(), i, 0, origWidth, origHeight);
				cli.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
			}
			colls.put(key, cli);
		}
		return li;
	}

	public void update(Player p, SpielPanel gp) {
		// TODO coll
		System.out.println(colls);
		if (colls.containsKey(currentKey)) {
			Image coll = colls.get(currentKey).get(colls.get(currentKey).size() > 1 ? spriteCounter : 0);
			PixelReader pr = coll.getPixelReader();
			//			for (int i = 0; i < coll.getWidth(); i++)
			//				for (int j = 0; j < coll.getHeight(); j++)
			//					System.out.println(pr.getArgb(i, j) >>> 31);
		}

		spriteCounter++;
		if (spriteCounter > 30 / images.get(currentKey).size()) {
			spriteNum++;
			spriteCounter = 0;
		}

		if (spriteNum >= images.get(currentKey).size()) spriteNum = 0;
		setImage(images.get(currentKey).get(spriteNum));
		double screenX = x - p.worldX + p.screenX;
		double screenY = y - p.worldY + p.screenY;
		if (x + reqWidth > p.worldX - p.screenX
				&& x - reqWidth < p.worldX + p.screenX
				&& y + reqHeight > p.worldY - p.screenY
				&& y - reqHeight < p.worldY + p.screenY) {
			setVisible(true);
			setX(screenX);
			setY(screenY);
		} else setVisible(false);
	}

}