package rngGame.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import rngGame.tile.ImgUtil;
import rngGame.visual.GamePanel;

public class HealthBar extends Pane {
	
	private ImageView healthB;
	private GamePanel gp;
	private Canvas c;

	public HealthBar(GamePanel gp) {
		healthB = new ImageView();
		this.gp = gp;
		scaleF11();
	}
	
	public void scaleF11() {
		healthB.setImage(ImgUtil.getScaledImage(gp, "./res/gui/HealthBar.png",256 ,64));
		c = new Canvas(healthB.getImage().getWidth(), healthB.getImage().getHeight());
		getChildren().clear();
		getChildren().addAll(c, healthB);
		
		
	}

	public void update() {
		c.getGraphicsContext2D().setFill(Color.LIMEGREEN);
		c.getGraphicsContext2D().fillRect(44*gp.getScalingFactorX(), 16*gp.getScalingFactorY(), 165*gp.getScalingFactorX()/* * healtPercent */, 14*gp.getScalingFactorY());
		c.getGraphicsContext2D().fillRect(44*gp.getScalingFactorX() + 165*gp.getScalingFactorX()/* * healtPercent */, 18*gp.getScalingFactorY(), gp.getScalingFactorX(), 10*gp.getScalingFactorY());
	}
}
