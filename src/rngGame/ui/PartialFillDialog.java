package rngGame.ui;

import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import rngGame.tile.*;

// TODO: Auto-generated Javadoc
/**
 * The Class PartialFillDialog.
 */
public class PartialFillDialog extends Dialog<Boolean> {

	/** The grid. */
	private final GridPane grid = new GridPane();

	/** The rerandomize. */
	private Button rerandomize;

	/** The slider. */
	private Slider slider;

	/** The name. */
	private Label name;

	/** The space. */
	private final Label space = new Label("");

	/** The select tool. */
	private SelectTool selectTool;

	/** The tile. */
	private Tile tile;

	/** The matrix. */
	private Boolean[] matrix;

	/**
	 * Instantiates a new partial fill dialog.
	 *
	 * @param selectTool the select tool
	 * @param tile the tile
	 */
	public PartialFillDialog(SelectTool selectTool, Tile tile) {
		final DialogPane dialogPane = getDialogPane();

		this.selectTool = selectTool;
		this.tile = tile;

		name = new Label("50% of area are getting filled");

		slider = new Slider(0.0, 1.0, .5);
		slider.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(slider, Priority.ALWAYS);
		GridPane.setFillWidth(slider, true);
		slider.valueProperty().addListener(
				(ChangeListener<Number>) (observable, oldValue, newValue) -> {
					name.setText((int) ((double) newValue * 100) + "% of area are getting filled");
					randomize(newValue.doubleValue());
				});

		rerandomize = new Button("Rerandomize Pattern");
		GridPane.setHalignment(rerandomize, HPos.CENTER);
		GridPane.setValignment(rerandomize, VPos.CENTER);
		rerandomize.setOnAction(event -> {
			randomize(slider.getValue());
		});
		randomize(slider.getValue());

		space.setMaxHeight(Double.MAX_VALUE);

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
	 * Make view.
	 *
	 * @param img the img
	 * @return the image
	 */
	private Image makeView(Image img) {
		Pane pane = new Pane();
		pane.setPrefSize(selectTool.getWidth(), selectTool.getHeight());
		List<ImageView> ivs = new ArrayList<>();
		double wIdxs = selectTool.getWidth() / img.getWidth();
		for (int i = 0; i < matrix.length; i++) if (matrix[i]) {
			ImageView iv = new ImageView(img);
			iv.setLayoutX(i % wIdxs * img.getWidth());
			iv.setLayoutY((int) (i / wIdxs) * img.getHeight());
			ivs.add(iv);
		}
		pane.getChildren().addAll(ivs);
		new Scene(pane);
		SnapshotParameters parameters = new SnapshotParameters();
		parameters.setFill(Color.TRANSPARENT);
		WritableImage wi = pane.snapshot(parameters, null);
		PixelWriter pw = wi.getPixelWriter();
		PixelReader pr = wi.getPixelReader();
		for ( int i=0;i<wi.getWidth();i++) for (int j=0;j<wi.getHeight(); j++) if (pr.getArgb(i, j)== -723724) pw.setColor(i, j, Color.TRANSPARENT);
		return wi;
	}

	/**
	 * Randomize.
	 *
	 * @param percentage the percentage
	 */
	private void randomize(Double percentage) {
		Image img = tile.images[0];
		matrix = new Random()
				.doubles((long) (selectTool.getWidth() / img.getWidth() * (selectTool.getHeight() / img.getHeight())))
				.boxed().map(i -> i <= slider.getValue()).toArray(size -> new Boolean[size]);
		selectTool.setFill(new ImagePattern(makeView(img)));
	}

	/**
	 * Update grid.
	 */
	private void updateGrid() {
		grid.getChildren().clear();

		grid.add(name, 0, 0);
		grid.add(slider, 0, 1);
		grid.add(space, 0, 2);
		grid.add(rerandomize, 0, 3);
		getDialogPane().setContent(grid);

		Platform.runLater(() -> slider.requestFocus());
	}

	/**
	 * Gets the matrix.
	 *
	 * @return the matrix
	 */
	public Boolean[] getMatrix() { return matrix; }

}
