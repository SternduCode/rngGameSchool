package rngGame.ui;

import javafx.scene.image.ImageView;
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


	/**
	 * Instantiates a new inventory.
	 *
	 * @param gamepanel the gamepanel
	 * @param tabm the tabm
	 */
	public Inventory(GamePanel gamepanel, TabMenu tabm) {
		this.gamepanel = gamepanel;
		invBackround	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/InvBackround.png"));
		getChildren().add(invBackround);
		invBackround.setVisible(false);


	}

	/**
	 * End show.
	 */
	public void endShow() {
		invBackround.setVisible(false);
	}

	/**
	 * Show.
	 */
	public void show() {
		invBackround.setVisible(true);
		invBackround.setTranslateY(0);
		invBackround.setTranslateX(0);
		invBackround.setOpacity(1);
	}

}
