package rngGame.ui;

import java.util.function.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;
import rngGame.main.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class LayerInputDialog.
 */
public class LayerInputDialog extends Dialog<Boolean> {

	/** The grid. */
	private final GridPane grid = new GridPane();

	/** The down. */
	private Button up, down;

	/** The text field. */
	private TextField textField;

	/** The space 2. */
	private final Label space = new Label(""), space2 = new Label("");

	/** The gp. */
	private GamePanel gp;

	/**
	 * Instantiates a new layer input dialog.
	 *
	 * @param supplier the supplier
	 * @param consumer the consumer
	 * @param gp the gp
	 */
	public LayerInputDialog(Supplier<Integer> supplier, Consumer<Integer> consumer, GamePanel gp) {
		final DialogPane dialogPane = getDialogPane();

		this.gp = gp;
		textField = new TextField(supplier.get() + "");
		textField.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(textField, Priority.ALWAYS);
		GridPane.setFillWidth(textField, true);
		textField.textProperty().addListener(
				(ChangeListener<String>) (observable, oldValue, newValue) -> {
					try {
						int i = Integer.parseInt(newValue);
						consumer.accept(i < 0 ? 0 : i);
						if (i < 0) textField.setText(0 + "");
					} catch (NumberFormatException e) {
						if (!"".equals(newValue)) textField.setText(oldValue);
					}
				});

		up = new Button((char) 708 + "", gp);
		GridPane.setHalignment(up, HPos.CENTER);
		GridPane.setValignment(up, VPos.CENTER);
		up.setOnAction(event -> {
			try {
				textField.setText(Integer.parseInt(textField.getText()) + 1 + "");
			} catch (NumberFormatException e) {
			}
		});

		down = new Button((char) 709 + "", gp);
		GridPane.setHalignment(down, HPos.CENTER);
		GridPane.setValignment(down, VPos.CENTER);
		down.setOnAction(event -> {
			try {
				textField.setText(Integer.parseInt(textField.getText()) - 1 + "");
			} catch (NumberFormatException e) {
			}
		});

		space.setMaxHeight(Double.MAX_VALUE);
		space2.setMaxHeight(Double.MAX_VALUE);

		grid.setHgap(10);
		grid.setMaxWidth(Double.MAX_VALUE);
		grid.setAlignment(Pos.CENTER);

		dialogPane.contentTextProperty().addListener(o -> updateGrid());

		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		updateGrid();

		setResultConverter(dialogButton -> {
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return (data == ButtonData.OK_DONE) == true;
		});
	}

	/**
	 * Update grid.
	 */
	private void updateGrid() {
		grid.getChildren().clear();

		grid.add(up, 0, 0);
		grid.add(space, 0, 1);
		grid.add(textField, 0, 2);
		grid.add(space2, 0, 3);
		grid.add(down, 0, 4);
		getDialogPane().setContent(grid);

		Platform.runLater(() -> textField.requestFocus());
	}

}
