package tile;

import java.io.InputStream;
import javafx.scene.image.Image;
import rngGAME.SpielPanel;

public class Tile {

	public Image Image;
	public boolean collision = false;

	public Tile(InputStream image, SpielPanel gp) {
		Image = new Image(image, gp.Bg, gp.Bg, false, false);
	}

}
