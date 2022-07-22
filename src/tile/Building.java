package tile;

import java.util.List;
import com.sterndu.json.*;
import javafx.scene.image.*;

public class Building extends ImageView {

	private final List<Image> images;
	private final double x, y;
	private final JsonObject buildingData;

	public Building(JsonObject building) {
		images = ((JsonArray) building.get("textures")).stream()
				.map(s -> new Image(getClass().getResourceAsStream(((StringValue) s).getValue()))).toList();
		x = ((FloatValue) ((JsonArray) building.get("position")).get(0)).getValue();
		y = ((FloatValue) ((JsonArray) building.get("position")).get(0)).getValue();
		buildingData = (JsonObject) building.get("buildingData");
	}

}
