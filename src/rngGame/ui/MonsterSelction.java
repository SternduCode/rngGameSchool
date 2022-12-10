package rngGame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;

public class MonsterSelction extends Pane {
	private GamePanel gamepanel;
	private ImageView MonsterBackground;
	private Button ausXb;
	private Button start;
	private Button back;
	
	public MonsterSelction(GamePanel gamepanel) {
		this.gamepanel = gamepanel;
		
		Image bi = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Monsterauswahlbackround.png");
		
		Image ausX = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterXbutton.png");
		Image ausX2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterXbuttonC.png");
		
		Image startB = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterStartbutton.png");
		Image startB2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterStartbuttonC.png");
		
		Image BackB = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterBackbutton.png");
		Image BackB2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterBackbuttonC.png");
		
		
		MonsterBackground = new ImageView(bi);
		MonsterBackground.setTranslateY(-1);
		
		ausXb = new Button(ausX);
		
		start = new Button(startB);
		start.setX(32);
		start.setY(gamepanel.SpielHoehe / 2 + gamepanel.SpielHoehe / 8);
		
		back = new Button(BackB);
		back.setX(32);
		back.setY(gamepanel.SpielHoehe / 2 + gamepanel.SpielHoehe / 4 + 10);
		
		getChildren().add(MonsterBackground);
		getChildren().add(ausXb);
		getChildren().add(start);
		getChildren().add(back);
		
		
		ausXb.setOnMousePressed(me -> {
			ausXb.setImage(ausX2);
		});
		ausXb.setOnMouseReleased(me -> {
			ausXb.setImage(ausX);
		});
		
		
		start.setOnMousePressed(me -> {
			start.setImage(startB2);
		});
		start.setOnMouseReleased(me -> {
			start.setImage(startB);
		});
		
		
		back.setOnMousePressed(me -> {
			back.setImage(BackB2);
		});
		back.setOnMouseReleased(me -> {
			back.setImage(BackB);
		});
	}

	public void update(long milis) {}

}
