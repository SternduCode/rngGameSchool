package rngGame.entity;

import java.io.ByteArrayInputStream;
import java.util.*;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.*;
import rngGame.tile.ImgUtil;

public class Demon extends NPC implements JsonValue {

	private String dir;

	public Demon(JsonObject npc, List<NPC> npcs, ContextMenu cm, ObjectProperty<NPC> requestorN) {
		super(npc, npcs, cm, requestorN);
	}

	@Override
	protected List<Image> getAnimatedImages(String path) {
		List<Image> li = new ArrayList<>();
		Image img = new Image(getClass().getResourceAsStream("/res/demons/" + dir + "/" + path));
		for (int i = 0; i < img.getWidth(); i += origWidth) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
			li.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
		}
		return li;
	}

	@Override
	protected void init(JsonObject npc) {
		dir = ((StringValue) npc.get("dir")).getValue();
		super.init(npc);
	}

	@Override
	public String toJson() {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(super.toJson().getBytes());
			JsonObject jo = (JsonObject) JsonParser.parse(bais);
			jo.put("dir", dir);
			return jo.toJson();
		} catch (JsonParseException e) {
			e.printStackTrace();
			return "";
		}
	}

}
