package rngGame.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import rngGame.stats.Demon;
import rngGame.tile.ImgUtil;
import rngGame.visual.GamePanel;

public class HealthBar extends Pane {
	
	private ImageView healthB;
	private GamePanel gp;
	private Canvas c;
	private Demon d;

	public HealthBar(GamePanel gp, Demon d) {
		healthB = new ImageView();
		this.gp = gp;
		this.d = d;
		scaleF11();
	}
	
	public void scaleF11() {
		healthB.setImage(ImgUtil.getScaledImage(gp, "./res/gui/HealthBar.png",256 ,64));
		c = new Canvas(healthB.getImage().getWidth(), healthB.getImage().getHeight());
		getChildren().clear();
		getChildren().addAll(c, healthB);
	}

	public void update() {
		c.getGraphicsContext2D().setFill(Color.TRANSPARENT);
		c.getGraphicsContext2D().fillRect(44*gp.getScalingFactorX(), 16*gp.getScalingFactorY(), 165*gp.getScalingFactorX(), 14*gp.getScalingFactorY());
		c.getGraphicsContext2D().fillRect(44*gp.getScalingFactorX() + 165*gp.getScalingFactorX(), 18*gp.getScalingFactorY(), gp.getScalingFactorX(), 10*gp.getScalingFactorY());
		c.getGraphicsContext2D().setFill(Color.LIMEGREEN);
		if(d.getCurrenthp() > 0) {
		c.getGraphicsContext2D().fillRect(44*gp.getScalingFactorX(), 16*gp.getScalingFactorY(), 165*gp.getScalingFactorX()*(d.getCurrenthp()/(double)d.getMaxHp()), 14*gp.getScalingFactorY());
		c.getGraphicsContext2D().fillRect(44*gp.getScalingFactorX() + 165*gp.getScalingFactorX()*(d.getCurrenthp()/(double)d.getMaxHp()), 18*gp.getScalingFactorY(), gp.getScalingFactorX(), 10*gp.getScalingFactorY());
		}
	}
}