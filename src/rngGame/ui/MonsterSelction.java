package rngGame.ui;

import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import rngGame.buildings.ContractsTable;
import rngGame.main.Input;
import rngGame.tile.ImgUtil;
import rngGame.visual.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class MonsterSelction.
 */
public class MonsterSelction extends Pane {

	/** The Monster background. */
	private final ImageView MonsterBackground;

	/** The aus xb. */
	private final Button ausXb;

	/** The start. */
	private final Button start;

	/** The back. */
	private final Button back;

	/**
	 * Instantiates a new monster selction.
	 *
	 * @param gamepanel the gamepanel
	 * @param ct the ct
	 */
	public MonsterSelction(GamePanel gamepanel, ContractsTable ct) {
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
		start.setY(gamepanel.getGameHeight() / 2 + gamepanel.getGameHeight() / 8);

		back = new Button(BackB);
		back.setX(32);
		back.setY(gamepanel.getGameHeight() / 2 + gamepanel.getGameHeight() / 4 + 10);

		getChildren().add(MonsterBackground);
		getChildren().add(ausXb);
		getChildren().add(start);
		getChildren().add(back);


		ausXb.setOnMousePressed(me -> {
			ausXb.setImage(ausX2);
		});
		ausXb.setOnMouseReleased(me -> {
			ausXb.setImage(ausX);
			ct.setMs(null);
			Input.getInstance().keyPressed(new KeyEvent(null, "", "", KeyCode.ENTER, false, false, false, false));
		});


		start.setOnMousePressed(me -> {
			start.setImage(startB2);
		});
		start.setOnMouseReleased(me -> {
			start.setImage(startB);
			Input.getInstance().keyPressed(new KeyEvent(null, "", "", KeyCode.ENTER, false, false, false, false));
			gamepanel.getLgp().setMap("./res/maps/Dungeon.json");
			ct.setMs(null);
			ct.removeEnterAbbility();
		});


		back.setOnMousePressed(me -> {
			back.setImage(BackB2);
		});
		back.setOnMouseReleased(me -> {
			back.setImage(BackB);
			ct.setMs(null);
		});
	}

	/**
	 * Update.
	 *
	 * @param milis the milis
	 */
	public void update(long milis) {}

}
