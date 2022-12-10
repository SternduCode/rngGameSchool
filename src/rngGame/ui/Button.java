package rngGame.ui;

import javafx.event.*;
import javafx.scene.image.*;

public class Button extends ImageView {

	public Button() {}

	public Button(Image image) {
		super(image);
	}

	public Button(String url) {
		super(url);
	}

	public void setOnAction(EventHandler<ActionEvent> ev) {
		setOnMouseReleased(me -> ev.handle(new ActionEvent(me.getSource(), me.getTarget())));
		setOnTouchReleased(te -> ev.handle(new ActionEvent(te.getSource(), te.getTarget())));
	}

}
