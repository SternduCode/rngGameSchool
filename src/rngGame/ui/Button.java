package rngGame.ui;

import java.util.function.Consumer;

import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import rngGame.main.GamePanel;

public class Button extends ImageView {
	GamePanel gp;
	public Button() {}

	public Button(Image image, GamePanel gpNeu) {
		super(image);
		this.gp = gpNeu;
	}

	public Button(String url, GamePanel gpNeu) {
		super(url);
		this.gp = gpNeu;
	}
	

	public void setOnAction(EventHandler<ActionEvent> ev) {
		setOnMouseReleased(me -> ev.handle(new ActionEvent(me.getSource(), me.getTarget())));
		setOnTouchReleased(te -> ev.handle(new ActionEvent(te.getSource(), te.getTarget())));
	}
	

	public void setOnReleased(EventHandler<MouseEvent> mv) {
		
		setOnMouseReleased(i ->((Consumer<MouseEvent>) e->gp.makeSound("click")).andThen(e -> mv.handle(e)));
		
	}

}
