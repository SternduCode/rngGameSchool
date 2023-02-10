package rngGame.ui;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;



// TODO: Auto-generated Javadoc
/**
 * The Class Inventory.
 */
public class Inventory extends Pane {

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The inv backround. */
	private final ImageView invBackround;

	/** The aus xb. */
	private final Button ausXb;


	/**
	 * Instantiates a new inventory.
	 *
	 * @param gamepanel the gamepanel
	 * @param tabm the tabm
	 */
	public Inventory(GamePanel gamepanel, TabMenu tabm) {
		this.gamepanel = gamepanel;
		invBackround	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/InvBackround.png"));
		Image ausX = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Xbutton.png");
		Image ausX2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/XbuttonC.png");
		ausXb = new Button(ausX);

		getChildren().add(invBackround);
		getChildren().add(ausXb);
		invBackround.setVisible(false);
		ausXb.setVisible(false);

		ausXb.setOnMousePressed(me -> {
			ausXb.setImage(ausX2);
		});

		ausXb.setOnMouseReleased(me -> {
			ausXb.setImage(ausX);
			endShow();
		});
	}

	/**
	 * End show.
	 */
	public void endShow() {
		invBackround.setVisible(false);
		ausXb.setVisible(false);
		setDisable(true);
	}

	/**
	 * Show.
	 */
	public void show() {
		invBackround.setVisible(true);
		ausXb.setVisible(true);
		setDisable(false);
	}

}
