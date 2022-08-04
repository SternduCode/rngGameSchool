package rngGame.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Stage;

public class MainClass extends Application {

	public static void main(String[] args) {

		System.setProperty("edit", "false");
		System.setProperty("coll", "false");

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("RNG fun");


		Input input = new Input();

		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, input::keyPressed);
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, input::keyReleased);
		primaryStage.addEventHandler(KeyEvent.KEY_TYPED, input::keyTyped);
		primaryStage.addEventHandler(MouseEvent.MOUSE_RELEASED, input::mouseReleased);
		primaryStage.addEventHandler(MouseEvent.DRAG_DETECTED, input::dragDetected);
		primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED, input::mouseMoved);
		primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, input::mouseDragged);

		SpielPanel spielebene = new SpielPanel(input);
		input.setSpielPanel(spielebene);
		Scene gameScene = new Scene(spielebene);
		primaryStage.setScene(gameScene);

		spielebene.SST();

		primaryStage.show();
	}
}
