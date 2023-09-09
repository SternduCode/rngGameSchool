package rngGame.main;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.stage.*;

/**
 * The Class MainClass.
 */
public class MainClass extends Application {

	/** The stopping. */
	private static boolean stopping;

	/**
	 * Checks if is stopping.
	 *
	 * @return true, if is stopping
	 */
	public static boolean isStopping() { return stopping; }

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		System.setProperty("debug", "false"); // more debug messages

		System.setProperty("edit", "false"); // set edit mode to disabled
		System.setProperty("coll", "false"); // set collisions mode to disabled
		System.setProperty("alternateUpdate", "false"); // reverse Vsync more or less
		System.setProperty("teleport", "false");

		launch(args);

		System.exit(0);

	}

	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		TitleScreen ts = new TitleScreen();

		primaryStage.setFullScreen(false);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Demon Universe");

		primaryStage.setFullScreenExitHint("");
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		primaryStage.getIcons().add(new Image(new FileInputStream ("./res/gui/GameIschcon.png")));
		Input input = Input.getInstance();
		input.setBlockInputs(true);

		// set eventHandlers to detect Mouse and Key Events on the whole Window
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, input::keyPressed);
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, input::keyReleased);
		primaryStage.addEventHandler(KeyEvent.KEY_TYPED, input::keyTyped);
		primaryStage.addEventHandler(MouseEvent.MOUSE_RELEASED, input::mouseReleased);
		primaryStage.addEventHandler(MouseEvent.DRAG_DETECTED, input::dragDetected);
		primaryStage.addEventHandler(MouseEvent.MOUSE_MOVED, input::mouseMoved);
		primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, input::mouseDragged);

		Scene gameScene = new Scene(ts);
		primaryStage.setScene(gameScene);

		ts.scaleF11();

		primaryStage.show();

		// input.toggleFullScreen();

		Window.getWindows().addListener((ListChangeListener<Window>) c -> {
			if (c.getList().isEmpty()) stopping = true;
		});
	}
}
