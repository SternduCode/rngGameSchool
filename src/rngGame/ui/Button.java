package rngGame.ui;

import java.util.function.Consumer;

import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import rngGame.main.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class Button.
 */
public class Button extends ImageView {

	/** The gp. */
	GamePanel gp;

	/**
	 * Instantiates a new button.
	 *
	 * @param gpNeu the gp neu
	 */
	public Button(GamePanel gpNeu) { gp = gpNeu; }

	/**
	 * Instantiates a new button.
	 *
	 * @param image the image
	 * @param gpNeu the gp neu
	 */
	public Button(Image image, GamePanel gpNeu) {
		super(image);
		gp = gpNeu;
	}

	/**
	 * Instantiates a new button.
	 *
	 * @param url the url
	 * @param gpNeu the gp neu
	 */
	public Button(String url, GamePanel gpNeu) {
		super(url);
		gp = gpNeu;
	}


	/**
	 * Sets the on action.
	 *
	 * @param ev the new on action
	 */
	public void setOnAction(EventHandler<ActionEvent> ev) {
		setOnReleased(me -> ev.handle(new ActionEvent(me.getSource(), me.getTarget())));
	}


	/**
	 * Sets the on pressed.
	 *
	 * @param mv the new on pressed
	 */
	public void setOnPressed(EventHandler<MouseEvent> mv) {
		setOnMousePressed(mv);
	}

	/**
	 * Sets the on released.
	 *
	 * @param mv the new on released
	 */
	public void setOnReleased(EventHandler<MouseEvent> mv) {
		setOnMouseReleased(i -> ((Consumer<MouseEvent>) e -> gp.makeSound("click.wav")).andThen(e -> mv.handle(e)).accept(i));

	}

}
