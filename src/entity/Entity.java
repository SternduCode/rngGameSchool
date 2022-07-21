package entity;

import java.util.List;
import javafx.scene.image.*;

public class Entity extends ImageView {

	public int worldX, worldY;
	public int speed;

	public List<Image> up, upl, down, downl, left, right, nomovel, nomover;
	public String direction;

	public int spriteCounter = 0;
	public int spriteNum = 0;

}
