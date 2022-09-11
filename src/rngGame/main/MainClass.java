package rngGame.main;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.*;

public class MainClass extends Application {

	private static boolean stopping = false;

	public static boolean isStopping() { return stopping; }

	public static void main(String[] args) {

		System.setProperty("edit", "false"); // set edit mode to disabled
		System.setProperty("coll", "false"); // set collisions mode to disabled
		System.setProperty("alternateUpdate", "false"); // reverse Vsync more or less

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setResizable(false);
		primaryStage.setTitle("RNG fun");

		Input input = Input.getInstance();

		// set eventHandlers to detect Mouse and Key Events on the whole Window
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, input::keyPressed);
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, input::keyReleased);
		primaryStage.addEventHandler(KeyEvent.KEY_TYPED, input::keyTyped);
		primaryStage.addEventHandler(MouseEvent.MOUSE_RELEASED, input::mouseReleased);
		primaryStage.addEventHandler(MouseEvent.DRAG_DETECTED, input::dragDetected);
		primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED, input::mouseMoved);
		primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, input::mouseDragged);

		SpielPanel gamePanel = new SpielPanel();
		input.setSpielPanel(gamePanel); // pass instance of GamePanel to the Instance of Input
		Scene gameScene = new Scene(gamePanel);
		primaryStage.setScene(gameScene);

		gamePanel.SST();

		primaryStage.show();

		Window.getWindows().addListener((ListChangeListener<Window>) c -> {
			if (c.getList().size() == 0) stopping = true;
		});
	}
}
