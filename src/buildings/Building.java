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
	protected String currentKey;
	protected final double x, y;
	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected final JsonObject buildingData;

	public Building(JsonObject building) {
		x = ((NumberValue) ((JsonArray) building.get("position")).get(0)).getValue().doubleValue();
		y = ((NumberValue) ((JsonArray) building.get("position")).get(1)).getValue().doubleValue();
		origWidth = ((NumberValue) ((JsonArray) building.get("originalSize")).get(0)).getValue().intValue();
		origHeight = ((NumberValue) ((JsonArray) building.get("originalSize")).get(1)).getValue().intValue();
		reqWidth = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(0)).getValue().intValue();
		reqHeight = ((NumberValue) ((JsonArray) building.get("requestedSize")).get(1)).getValue().intValue();
		images = ((JsonObject) building.get("textures")).entrySet().parallelStream()
				.map(s -> Map.entry(s.getKey(), getAnimatedImages(((StringValue) s.getValue()).getValue())))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		setImage(images.values().stream().findFirst().get().get(0));
		buildingData = (JsonObject) building.get("buildingData");
	}

	private List<Image> getAnimatedImages(String path) {
		List<Image> li = new ArrayList<>();
		Image img = new Image(getClass().getResourceAsStream(path));

		for (int i = 0; i < img.getWidth(); i += origWidth) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
			li.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
		}
		return li;
	}

	public void update(Player p, SpielPanel gp) {
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