package rngGame.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

// TODO: Auto-generated Javadoc
/**
 * The Class ScrollPaneElement.
 */
public class ScrollPaneElement extends Pane {

	/**
	 * Instantiates a new scroll pane element.
	 *
	 * @param background the background
	 */
	public ScrollPaneElement(Button background) {
		super(background);
	}

	/**
	 * Instantiates a new scroll pane element.
	 *
	 * @param background the background
	 * @param children   the children
	 */
	public ScrollPaneElement(Button background, Node... children) {
		super(background);
		getChildren().addAll(children);
	}

	/**
	 * Gets the background element.
	 *
	 * @return the background element
	 */
	public Button getBackgroundElement() { return (Button) getChildren().get(0); }

	/**
	 * Sets the background element.
	 *
	 * @param background the new background element
	 */
	public void setBackgroundElement(Button background) {
		getChildren().set(0, background);
	}

}
