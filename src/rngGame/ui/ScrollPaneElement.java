package rngGame.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class ScrollPaneElement extends Pane {

	public ScrollPaneElement(Button background) {
		super(background);
	}

	public ScrollPaneElement(Button background, Node... children) {
		super(background);
		getChildren().addAll(children);
	}

	public Button getBackgroundElement() { return (Button) getChildren().get(0); }

	public void setBackgroundElement(Button background) {
		getChildren().set(0, background);
	}

}
