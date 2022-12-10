package rngGame.ui;

import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import rngGame.buildings.ContractsTable;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;

public class LevelSelectionScrollPaneElement extends ScrollPaneElement {

	private final Button background, startButton;
	private final Image UGbc;

	public LevelSelectionScrollPaneElement(GamePanel gamepanel, ContractsTable ct) {
		super(new Button());
		background = getBackgroundElement();
		UGbc = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground.png");
		Image UGbc2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground2.png");
		setBackgroundImageToDefaullt();

		// Start Button
		Image sButton = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Startbutton.png");
		Image sButton2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Startbutton2.png");
		startButton = new Button(sButton);
		startButton.setLayoutX(383 * gamepanel.getScalingFactorX());
		startButton.setLayoutY(4 * gamepanel.getScalingFactorY());
		startButton.setOnMousePressed(me -> {
			startButton.setImage(sButton2);
		});
		startButton.setOnMouseReleased(me -> {
			startButton.setImage(sButton);
			ct.setMs(new MonsterSelction(gamepanel, ct));// TODO add needed parameters
		});

		background.setOnMouseReleased(me -> {
			background.setImage(UGbc2);

			FadeTransition st = new FadeTransition(Duration.millis(100), startButton);
			FadeTransition st2 = new FadeTransition(Duration.millis(100), ct.getInfos());
			st.setFromValue(0);
			st.setToValue(1);
			st.play();
			st2.setFromValue(0);
			st2.setToValue(1);
			st2.play();
			startButton.setVisible(true);
			ct.getInfos().setVisible(true);

		});

		// TODO make other stuffs

		getChildren().addAll(startButton);
	}

	public Button getStartButton() { return startButton; }

	public void setBackgroundImageToDefaullt() {
		background.setImage(UGbc);
	}

}
