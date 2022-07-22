package buildings;

import com.sterndu.json.JsonObject;

public class House extends Building {

	public House(JsonObject building) {
		super(building);
		setImage(images.get("open").get(0));
	}

}
