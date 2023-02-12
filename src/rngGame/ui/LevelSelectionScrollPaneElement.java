package rngGame.ui;

import java.io.*;
import java.util.stream.*;

import javafx.animation.FadeTransition;
import javafx.scene.image.*;
import javafx.util.Duration;
import rngGame.buildings.ContractsTable;
import rngGame.main.*;
import rngGame.tile.Difficulty;
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
				} catch (
						/** The e. */
						FileNotFoundException e) {
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
	private final ImageView vlvl;
	
	private final ImageView picture;
	
	private final ImageView difficul;

	
	


	

	/**
	 * Instantiates a new level selection scroll pane element.
	 *
	 * @param gamepanel the gamepanel
	 * @param ct the ct
	 * @param floor the floor
	 * @param lvlneu the lvlneu
	 */
	public LevelSelectionScrollPaneElement(GamePanel gamepanel, ContractsTable ct, String floor, int lvlneu, String pic, Difficulty dif) {
		super(new Button());
		background = getBackgroundElement();
		UGbc = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground.png");
		Image UGbc2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground2.png");
		setBackgroundImageToDefaullt();

		 
		
		Image difE = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/difE.png");
		Image difM = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/difM.png");
		Image difH = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/difH.png");
		
		difficul = new ImageView(difE);
		
		vlvl = new ImageView(ImgUtil.resizeImage(lvls[lvlneu/5], (int)lvls[lvlneu/5].getWidth(), (int) lvls[lvlneu/5].getHeight(), (int)(48 * gamepanel.getScalingFactorX()), (int)(48 * gamepanel.getScalingFactorY())));
		vlvl.setVisible(false);
		vlvl.setLayoutX(24*gamepanel.getScalingFactorX());
		vlvl.setLayoutY(30*gamepanel.getScalingFactorY());
		
		picture = new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/"+pic+".png",64,64));
		picture.setDisable(true);
		
		floor1 = new ImageView();
		setFloor(gamepanel,floor);
		floor1.setDisable(true);
		
		
		
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
			vlvl.setVisible(true);
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
			
			if(dif == Difficulty.MIDDLE) {
				difficul.setImage(difM);
			}
			if(dif == Difficulty.HARD) {
				difficul.setImage(difH);
			}	
			if(dif == Difficulty.EASY) {
				difficul.setImage(difE);
			}
			
			
		});

		// TODO make other stuffs
		
		getChildren().addAll(startButton,floor1,picture);
		floor1.setLayoutX(5*gamepanel.getScalingFactorX());
		floor1.setLayoutY(5*gamepanel.getScalingFactorY());
		picture.setLayoutX(7*gamepanel.getScalingFactorX());
		picture.setLayoutY(7*gamepanel.getScalingFactorY());
		
		difficul.setLayoutX(360*gamepanel.getScalingFactorX());
		difficul.setLayoutY(65*gamepanel.getScalingFactorY());
	}

	/**
	 * Gets the start button.
	 *
	 * @return the start button
	 */
	public Button getStartButton() { return startButton; }

	/**
	 * Gets the vlvl.
	 *
	 * @return the vlvl
	 */
	public ImageView getVlvl() { return vlvl; }

	/**
	 * Sets the background image to defaullt.
	 */
	public void setBackgroundImageToDefaullt() {
		background.setImage(UGbc);
	}

	/**
	 * Sets the floor.
	 *
	 * @param gamepanel the gamepanel
	 * @param floor the floor
	 */
	public void setFloor(GamePanel gamepanel,String floor) {
		Image floor1 = Text.getInstance().convertText(floor, 64);
		floor1 = ImgUtil.resizeImage(
				floor1, (int) floor1.getWidth(), (int) floor1.getHeight(),
				(int) (floor1.getWidth() * gamepanel.getScalingFactorX()),
				(int) (floor1.getHeight() * gamepanel.getScalingFactorY()));
		this.floor1.setImage(floor1);
	}

	/**
	 * Sets the lvlfalse.
	 */
	public void setLvlfalse() {
		vlvl.setVisible(false);
	}

	/**
	 * Start bvisible false.
	 */
	public void startBvisibleFalse() {
		startButton.setVisible(false);
	}
	
	public ImageView getDifficul() {
		return difficul;
	}
}
