package rngGame.main;

import java.io.*;

import javafx.scene.Group;
import javafx.scene.image.*;
import rngGame.tile.ImgUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AktionButton.
 */
public class AktionButton extends Group {

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The aktionbutton. */
	private final ImageView aktionbutton;

	/** The druck. */
	private Image nichts,kann,druck;

	/** The ifc. */
	private boolean ifc;



	/**
	 * Instantiates a new aktion button.
	 *
	 * @param gamepanel the gamepanel
	 */
	public AktionButton(GamePanel gamepanel) {
		this.gamepanel = gamepanel;
		aktionbutton = new ImageView(nichts);
		f11Scale();
		getChildren().add(aktionbutton);

		aktionbutton.setOnMousePressed(me -> {
			if(ifc) aktionbutton.setImage(druck);
		});
		aktionbutton.setOnMousePressed(me -> {
			if(ifc) aktionbutton.setImage(kann);
		});


	}

	/**
	 * F 11 scale.
	 */
	public void f11Scale() {
		try {
			nichts = new Image(new FileInputStream("./res/gui/always/InteractionNichts.png"));
			kann = new Image(new FileInputStream("./res/gui/always/InteractionMoeglich.png"));
			druck = new Image(new FileInputStream("./res/gui/always/InteractionGedrueckt.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		nichts = ImgUtil.resizeImage(nichts, (int)nichts.getWidth(), (int)nichts.getHeight(), (int)(150* gamepanel.getScalingFactorX()), (int)(150*gamepanel.getScalingFactorY()));
		kann = ImgUtil.resizeImage(kann, (int)kann.getWidth(), (int)kann.getHeight(), (int)(150* gamepanel.getScalingFactorX()), (int)(150*gamepanel.getScalingFactorY()));
		druck = ImgUtil.resizeImage(druck, (int)druck.getWidth(), (int)druck.getHeight(), (int)(150* gamepanel.getScalingFactorX()), (int)(150*gamepanel.getScalingFactorY()));
		aktionbutton.setImage(nichts);

		aktionbutton.setLayoutX(gamepanel.SpielLaenge-220*gamepanel.getScalingFactorX());
		aktionbutton.setLayoutY(gamepanel.SpielHoehe-220*gamepanel.getScalingFactorY());

	}

	/**
	 * Sets the interactionbutton kann.
	 *
	 * @param ifc the new interactionbutton kann
	 */
	public void setInteractionbuttonKann(boolean ifc) {
		this.ifc = ifc;
		if(ifc) aktionbutton.setImage(kann);
		else aktionbutton.setImage(nichts);
	}
}
