package rngGame.ui;

import java.io.*;
import java.util.stream.*;

import javafx.animation.FadeTransition;
import javafx.scene.image.*;
import javafx.util.Duration;
import rngGame.buildings.ContractsTable;
import rngGame.main.*;
import rngGame.tile.ImgUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class LevelSelectionScrollPaneElement.
 */
public class LevelSelectionScrollPaneElement extends ScrollPaneElement {

	/** The lvls. */
	private static Image[] lvls = Stream.of(new File("./res/lvl/vorgeschlagen").listFiles())
			.sorted((a, b) -> Integer.compare(Integer.parseInt(a.getName().substring(3, a.getName().length() - 4)),
					Integer.parseInt(b.getName().substring(3, b.getName().length() - 4))))
			.map(t -> {
		try {
			return new FileInputStream(t);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}).filter(s -> s != null).map(Image::new).collect(Collectors.toList()).toArray(new Image[0]);

	/** The start button. */
	private final Button background, startButton;

	/** The U gbc. */
	private final Image UGbc;

	/** The floor 1. */
	private final ImageView floor1;

	/** The vlvl. */
	private ImageView vlvl;

	/**
	 * Instantiates a new level selection scroll pane element.
	 *
	 * @param gamepanel the gamepanel
	 * @param ct the ct
	 * @param floor the floor
	 */
	public LevelSelectionScrollPaneElement(GamePanel gamepanel, ContractsTable ct,String floor) {
		super(new Button());
		background = getBackgroundElement();
		UGbc = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground.png");
		Image UGbc2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground2.png");
		setBackgroundImageToDefaullt();

		Image floor1 = Text.getInstance().convertText(floor, 64);
		floor1 = ImgUtil.resizeImage(
				floor1, (int) floor1.getWidth(), (int) floor1.getHeight(),
				(int) (floor1.getWidth() * gamepanel.getScalingFactorX()),
				(int) (floor1.getHeight() * gamepanel.getScalingFactorY()));
		this.floor1 = new ImageView(floor1);
		this.floor1.setDisable(true);

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
			ct.reloadUGtexture();
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

		getChildren().addAll(startButton,this.floor1);
		this.floor1.setLayoutX(5);
		this.floor1.setLayoutY(5*gamepanel.getScalingFactorY());
	}

	/**
	 * Gets the start button.
	 *
	 * @return the start button
	 */
	public Button getStartButton() { return startButton; }

	/**
	 * Sets the background image to defaullt.
	 */
	public void setBackgroundImageToDefaullt() {
		background.setImage(UGbc);
	}

	/**
	 * Start bvisible false.
	 */
	public void startBvisibleFalse() {
		startButton.setVisible(false);
	}

}
