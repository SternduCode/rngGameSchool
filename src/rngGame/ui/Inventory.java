package rngGame.ui;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
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
	private Button ausXb;


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
	}
	
	/**
	 * Show.
	 */
	public void show() {
		invBackround.setVisible(true);
		invBackround.setTranslateY(0);
		invBackround.setTranslateX(0);
		invBackround.setOpacity(1);
		
		ausXb.setVisible(true);
	}

}
