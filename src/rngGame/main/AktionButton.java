package rngGame.main;


import java.util.function.Consumer;


import javafx.scene.layout.Pane;

import rngGame.ui.Button;
import rngGame.ui.SoundHandler;
import rngGame.visual.GamePanel;

/**
 * The Class AktionButton.
 */
public class AktionButton extends Pane {

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The aktionbutton. */
	private final Button aktionbutton;

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
		aktionbutton = new Button("./res/gui/always/InteractionNichts.png");
		f11Scale();
		getChildren().add(aktionbutton);
		aktionbutton.setImgRequestedWidth(200);
		aktionbutton.setImgRequestedHeight(200);
		aktionbutton.setOnPressed(me -> {
			if (ifc) {
				aktionbutton.init("./res/gui/always/InteractionGedrueckt.png");
				if (handler != null) handler.accept(gamepanel);
			} else {
				SoundHandler.getInstance().makeSound("NotClickable.wav");
			}
		});
		aktionbutton.setOnReleased(me -> {
			if(ifc) aktionbutton.init("./res/gui/always/InteractionMoeglich.png");
		});


	}

	/**
	 * F 11 scale.
	 */
	public void f11Scale() {
		aktionbutton.init("./res/gui/always/InteractionNichts.png");

		setLayoutX(WindowManager.getInstance().getGameWidth() - 270 * WindowManager.getInstance().getScalingFactorX());
		setLayoutY(WindowManager.getInstance().getGameHeight() - 270 * WindowManager.getInstance().getScalingFactorY());

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
			if (!aktionbutton.getPath().equals("./res/gui/always/InteractionGedrueckt.png"))
				aktionbutton.init("./res/gui/always/InteractionMoeglich.png");
			lastSetToTrue = System.currentTimeMillis();
		} else {
			aktionbutton.init("./res/gui/always/InteractionNichts.png");
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
