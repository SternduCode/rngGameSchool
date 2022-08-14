package rngGame.ui;

import java.util.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;

public class TwoTextInputDialog extends Dialog<List<String>> {

	GridPane grid = new GridPane();
	TextField textField1, textField2;
	Label label1, label2, space = new Label("");

	public TwoTextInputDialog(String textField1defaultText, String label1text, String textField2defaultText,
			String label2text) {
		final DialogPane dialogPane = getDialogPane();

		// -- textfield 1

		textField1 = new TextField(textField1defaultText);
		textField1.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(textField1, Priority.ALWAYS);
		GridPane.setFillWidth(textField1, true);

		textField2 = new TextField(textField2defaultText);
		textField2.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(textField2, Priority.ALWAYS);
		GridPane.setFillWidth(textField2, true);

		// -- label 1
		label1 = new Label(label1text);
		label1.setMaxWidth(Double.MAX_VALUE);
		label1.setMaxHeight(Double.MAX_VALUE);
		label1.getStyleClass().add("content");
		label1.setWrapText(true);
		label1.setPrefWidth(360);
		label1.setPrefWidth(Region.USE_COMPUTED_SIZE);

		label2 = new Label(label2text);
		label2.setMaxWidth(Double.MAX_VALUE);
		label2.setMaxHeight(Double.MAX_VALUE);
		label2.getStyleClass().add("content");
		label2.setWrapText(true);
		label2.setPrefWidth(360);
		label2.setPrefWidth(Region.USE_COMPUTED_SIZE);

		space.setMaxHeight(Double.MAX_VALUE);

		grid.setHgap(10);
		grid.setMaxWidth(Double.MAX_VALUE);
		grid.setAlignment(Pos.CENTER_LEFT);

		dialogPane.contentTextProperty().addListener(o -> updateGrid());

		TextInputDialog dialog = new TextInputDialog();

		//		setTitle(dialog.getTitle());
		//		dialogPane.getStyleClass().add("text-input-dialog");
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		updateGrid();

		setResultConverter(dialogButton -> {
			ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
			return data == ButtonData.OK_DONE ? Arrays.asList(textField1.getText(), textField2.getText())
					: null;
		});
	}

	private void updateGrid() {
		grid.getChildren().clear();

		grid.add(label1, 0, 0);
		grid.add(label2, 0, 2);
		grid.add(space, 0, 1);
		grid.add(textField1, 1, 0);
		grid.add(textField2, 1, 2);
		getDialogPane().setContent(grid);

		Platform.runLater(() -> textField1.requestFocus());
	}

}
