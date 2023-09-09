package rngGame.ui;

import javafx.event.*;
import javafx.scene.input.MouseEvent;
import rngGame.visual.*;

/**
 * The Class Button.
 */
public class Button extends AnimatedImage {

	/**
	 * Instantiates a new button.
	 *
	 */
	public Button() { super(); }

	/**
	 * Instantiates a new button.
	 *
	 * @param path the path
	 */
	public Button(String path) { super(path); }


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
		setOnMouseReleased(i -> {
			SoundHandler.getInstance().makeSound("click.wav");
			mv.handle(i);
		});

	}

}
