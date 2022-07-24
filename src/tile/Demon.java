package tile;

import java.util.*;
import com.sterndu.json.*;
import entity.NPC;
import javafx.scene.image.*;

public class Demon extends NPC {

	private String dir;

	public Demon(JsonObject npc) {
		super(npc);
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

}