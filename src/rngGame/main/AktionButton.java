package rngGame.main;

import java.io.*;
import java.util.function.Consumer;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import rngGame.tile.ImgUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AktionButton.
 */
public class AktionButton extends Pane {

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The aktionbutton. */
	private final ImageView aktionbutton;

	/** The druck. */
	private Image nichts,kann,druck;

	/** The ifc. */
	private boolean ifc = false;

	/** The time the button was last set to true. */
	private long lastSetToTrue = 0l;

	/** The handler. */
	private Consumer<GamePanel> handler = null;



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
			if (ifc) {
				aktionbutton.setImage(druck);
				if (handler != null) handler.accept(gamepanel);
			}
		});
		aktionbutton.setOnMouseReleased(me -> {
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

		setLayoutX(gamepanel.SpielLaenge - 220 * gamepanel.getScalingFactorX());
		setLayoutY(gamepanel.SpielHoehe - 220 * gamepanel.getScalingFactorY());

	}

	/**
	 * Sets the interactionbutton kann.
	 *
	 * @param ifc the new interactionbutton kann
	 * @param handler the handler
	 */
	public void setInteractionbuttonKann(boolean ifc, Consumer<GamePanel> handler) {
		this.ifc = ifc;
		if (ifc) {
			this.handler = handler;
			if (aktionbutton.getImage() != druck)
				aktionbutton.setImage(kann);
			lastSetToTrue = System.currentTimeMillis();
		} else {
			aktionbutton.setImage(nichts);
			this.handler = null;
		}
	}

	/**
	 * Update.
	 */
	public void update() {
		if (System.currentTimeMillis() - lastSetToTrue > 50) setInteractionbuttonKann(false, null);
	}

}
