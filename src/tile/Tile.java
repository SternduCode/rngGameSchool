package tile;

import java.io.InputStream;
import java.util.Map;
import javafx.scene.image.Image;
import rngGAME.SpielPanel;

public class Tile {

	public Image Image;
	public boolean collision = false;

	public Tile(InputStream image, SpielPanel gp) {

		Image = ImgUtil.resample(new Image(image), Map.entry(3, 3));
	}

}
