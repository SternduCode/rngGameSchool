package rngGame.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;
import javafx.scene.image.*;



public class Inventory extends Pane {
	private final GamePanel gamepanel;
	private final ImageView invBackround;
	
	
	public Inventory(GamePanel gamepanel, TabMenu tabm) {
		this.gamepanel = gamepanel;
		invBackround = new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/gamemenubackround.png"));
		getChildren().add(invBackround);
		invBackround.setVisible(false);
		
		
	}
	
	public void show() {
		invBackround.setVisible(true);
		System.out.println("banana");
	}
	
}
