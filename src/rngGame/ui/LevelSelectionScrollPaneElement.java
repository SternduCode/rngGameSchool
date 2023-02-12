package rngGame.ui;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;
import javafx.animation.FadeTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import rngGame.buildings.ContractsTable;
import rngGame.main.GamePanel;
import rngGame.main.Text;
import rngGame.tile.ImgUtil;

public class LevelSelectionScrollPaneElement extends ScrollPaneElement {

	private final Button background, startButton;
	private final Image UGbc;
	
	private ImageView floor1;

	private ImageView vlvl;

	private static Image[] lvls = Stream.of(new File("./res/lvl/vorgeschlagen").listFiles()).sorted((a,b)-> Integer.compare(Integer.parseInt(a.getName().substring(3,a.getName().length()-4)), Integer.parseInt(b.getName().substring(3,b.getName().length()-4)))).map(t -> {
        try {
            return new FileInputStream(t);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }).filter(s -> s != null).map(Image::new).collect(Collectors.toList()).toArray(new Image[0]);

	public LevelSelectionScrollPaneElement(GamePanel gamepanel, ContractsTable ct,String floor, int lvlneu) {
		super(new Button());
		background = getBackgroundElement();
		UGbc = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground.png");
		Image UGbc2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackground2.png");
		setBackgroundImageToDefaullt();
		
		
		
		vlvl = new ImageView(ImgUtil.resizeImage(lvls[lvlneu/5], (int)lvls[lvlneu/5].getWidth(), (int) lvls[lvlneu/5].getHeight(), (int)(48 * gamepanel.getScalingFactorX()), (int)(48 * gamepanel.getScalingFactorY())));
		vlvl.setVisible(false);
		vlvl.setLayoutX(24*gamepanel.getScalingFactorX());
		vlvl.setLayoutY(30*gamepanel.getScalingFactorY());
		


		this.floor1 = new ImageView();
		this.setFloor(gamepanel,floor);
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

		});

		// TODO make other stuffs

		getChildren().addAll(startButton,this.floor1);
		this.floor1.setLayoutX(5);
		this.floor1.setLayoutY(5*gamepanel.getScalingFactorY());
	}

	public Button getStartButton() { return startButton; }
	
	public ImageView getVlvl() { return vlvl; }

	public void setBackgroundImageToDefaullt() {
		background.setImage(UGbc);
	}
	
	public void setFloor(GamePanel gamepanel,String floor) {
		Image floor1 = Text.getInstance().convertText(floor, 64);
		floor1 = ImgUtil.resizeImage(
				floor1, (int) floor1.getWidth(), (int) floor1.getHeight(),
				(int) (floor1.getWidth() * gamepanel.getScalingFactorX()),
				(int) (floor1.getHeight() * gamepanel.getScalingFactorY()));
		this.floor1.setImage(floor1);
	}

	public void setLvlfalse() {
		vlvl.setVisible(false);
	}
	
	public void startBvisibleFalse() {
		startButton.setVisible(false);
	}

}
