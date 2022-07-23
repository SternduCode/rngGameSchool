package tile;

import java.util.*;
import com.sterndu.json.*;
import entity.NPC;
import javafx.scene.image.*;

public class Demon extends NPC {

	private final String dir;

	public Demon(JsonObject npc) {
		super(npc);
		dir = ((StringValue) npc.get("dir")).getValue();
	}

	@Override
	protected List<Image> getAnimatedImages(String path) {
		List<Image> li = new ArrayList<>();
		Image img = new Image(getClass().getResourceAsStream("/res/demnons/" + dir + "/" + path));
		for (int i = 0; i < img.getWidth(); i += origWidth) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
			li.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
		}
		return li;
	}

}
