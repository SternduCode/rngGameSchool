package rngGame.ui;

import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import rngGame.buildings.ContractsTable;
import rngGame.main.*;
import rngGame.tile.ImgUtil;

public class MonsterSelction extends Pane {
	private final GamePanel gamepanel;
	private final ImageView MonsterBackground;
	private final Button ausXb;
	private final Button start;
	private final Button back;

	public MonsterSelction(GamePanel gamepanel, ContractsTable ct) {
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
			Input.getInstance().keyPressed(new KeyEvent(null, "", "", KeyCode.ENTER, false, false, false, false));
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
			ct.setMs(null);
		});
	}

	public void update(long milis) {}

}
