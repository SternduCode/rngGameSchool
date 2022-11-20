package rngGame.buildings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import com.sterndu.json.JsonObject;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.main.Input;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;

public class ContractsTable extends Building {

	public ContractsTable(Building building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);
		init();
	}

	public ContractsTable(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		init();
	}

	public ContractsTable(JsonObject building, GamePanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);
		init();
	}

	private  Pane p1 = new Pane();
	private  ImageView contractBackround;
	private  ImageView contractSaturn;
	private  ImageView contractNebel;
	private  ImageView buttonR;
	private  ImageView buttonL;
	private int index = 0;
	private boolean iftest;
	private boolean inkreis;

	private void init() {
		try {
			Image wi = new Image(new FileInputStream("./res/Contractstuff/Mainbackround.png"));
			Image saturn = new Image(new FileInputStream("./res/Contractstuff/Saturn.png"));
			Image nebel = new Image(new FileInputStream("./res/Contractstuff/Nebel.png"));
			Image buttonR = new Image(new FileInputStream("./res/Contractstuff/pfeilR.png"));
			Image buttonL = new Image(new FileInputStream("./res/Contractstuff/pfeilL.png"));
			
			wi=ImgUtil.resizeImage(
					wi,(int) wi.getWidth(), (int) wi.getHeight(), (int) (wi.getWidth() * gamepanel.getScalingFactorX()),
					(int) (wi.getHeight() * gamepanel.getScalingFactorY()));
			
			saturn=ImgUtil.resizeImage(
					saturn,(int) saturn.getWidth(), (int) saturn.getHeight(), (int) (saturn.getWidth() * gamepanel.getScalingFactorX()),
					(int) (saturn.getHeight() * gamepanel.getScalingFactorY()));
			
			nebel=ImgUtil.resizeImage(
					nebel,(int) nebel.getWidth(), (int) nebel.getHeight(), (int) (nebel.getWidth() * gamepanel.getScalingFactorX()),
					(int) (nebel.getHeight() * gamepanel.getScalingFactorY()));
			
			buttonR=ImgUtil.resizeImage(
					buttonR,(int) buttonR.getWidth(), (int) buttonR.getHeight(), (int) (buttonR.getWidth() * gamepanel.getScalingFactorX()),
					(int) (buttonR.getHeight() * gamepanel.getScalingFactorY()));
			
			buttonL=ImgUtil.resizeImage(
					buttonL,(int) buttonL.getWidth(), (int) buttonL.getHeight(), (int) (buttonL.getWidth() * gamepanel.getScalingFactorX()),
					(int) (buttonL.getHeight() * gamepanel.getScalingFactorY()));
			
			contractBackround = new ImageView(wi);
			contractSaturn = new ImageView(saturn);
			contractNebel = new ImageView(nebel);
			this.buttonR = new ImageView(buttonR);
			this.buttonL = new ImageView(buttonL);
			
			
			this.buttonR.setOnMouseReleased(me->{
				TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
				tt.setFromX(gamepanel.SpielLaenge*-index);
				tt.setToX(-gamepanel.SpielLaenge-gamepanel.SpielLaenge*index);
				TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackround);
				tth.setFromX((gamepanel.SpielLaenge*-index)/2);
				tth.setToX((-gamepanel.SpielLaenge-(gamepanel.SpielLaenge*index))/2);
				index++;
				tt.play();
				tth.play();
			});
			this.buttonL.setOnMouseReleased(me->{
				TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
				tt.setFromX(gamepanel.SpielLaenge*-index);
				tt.setToX(gamepanel.SpielLaenge+gamepanel.SpielLaenge*-index);
				TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackround);
				tth.setFromX((gamepanel.SpielLaenge*-index)/2);
				tth.setToX((gamepanel.SpielLaenge+(gamepanel.SpielLaenge*-index))/2);
				index--;
				tt.play();
				tth.play();
			});
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//Hintergrund
		contractBackround.setX(gamepanel.getPlayer().getScreenX() - contractBackround.getImage().getWidth() / 2 + 48);
		contractBackround.setY(gamepanel.getPlayer().getScreenY() - contractBackround.getImage().getHeight() / 2 + 32);
		contractBackround.setVisible(false);
		gamepanel.getChildren().add(contractBackround);
		
		gamepanel.getChildren().add(p1);
		gamepanel.getChildren().add(buttonR);
		gamepanel.getChildren().add(buttonL);
		//Saturn
		p1.getChildren().add(contractSaturn);
		//Nebel
		contractNebel.setX(gamepanel.SpielLaenge);
		//contractNebel.setY(gamepanel.getPlayer().getScreenY() - contractNebel.getImage().getHeight() / 2 + 32);
		p1.setVisible(false);
		p1.getChildren().add(contractNebel);
		//buttons
		buttonR.setVisible(false);
		buttonL.setVisible(false);

		Input.getInstance().setKeyHandler("contractbackround", mod -> {
			if(inkreis) {
				if ((iftest = !iftest)) {
					gamepanel.setBlockUserInputs(true);
					contractBackround.setVisible(true);
					p1.setVisible(true);
					buttonR.setVisible(true);
					buttonL.setVisible(true);
					
						FadeTransition ft = new FadeTransition(Duration.millis(500), contractBackround);
						ft.setFromValue(0);
						ft.setToValue(1);
						ft.play();
						
						FadeTransition ft2 = new FadeTransition(Duration.millis(1000), p1);
						ft2.setFromValue(0);
						ft2.setToValue(1);
						ft2.play();
						
						FadeTransition ft3 = new FadeTransition(Duration.millis(1000), buttonL);
						ft3.setFromValue(0);
						ft3.setToValue(1);
						ft3.play();
						
						FadeTransition ft4 = new FadeTransition(Duration.millis(1000), buttonR);
						ft4.setFromValue(0);
						ft4.setToValue(1);
						ft4.play();
				}
				else {
					contractBackround.setVisible(false);
					p1.setVisible(false);
					buttonR.setVisible(false);
					buttonL.setVisible(false);
					gamepanel.setBlockUserInputs(false);
					
				}
			}
		}, KeyCode.ENTER, false);

		getMiscBoxHandler().put("table", (gpt, self) -> {
			inkreis = true;
			gamepanel.getBuildings().parallelStream().filter(b -> b.getTextureFiles().values().contains("CTischCircle.png")).forEach(b -> {
				b.setCurrentKey("open");
			});
		});
	}
	@Override
	public void update(long milis) {
		inkreis = false;
		super.update(milis);
		if(!inkreis) {
			iftest = false;
			gamepanel.getBuildings().parallelStream().filter(b -> b.getTextureFiles().values().contains("CTischCircle.png")).forEach(b -> {
				b.setCurrentKey("default");
			});
			contractBackround.setVisible(false);
			gamepanel.getChildren().remove(contractBackround);
			gamepanel.getChildren().remove(p1);
			gamepanel.getChildren().remove(buttonR);
			gamepanel.getChildren().remove(buttonL);
			
			
			
			
			
		} else {
			
			contractBackround.setY(gamepanel.getPlayer().getScreenY() - contractBackround.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(contractBackround)) 
				gamepanel.getChildren().add(contractBackround);
			
			p1.setLayoutX(gamepanel.getPlayer().getScreenX() - gamepanel.SpielLaenge / 2 + gamepanel.getPlayer().getWidth() / 2);
			p1.setLayoutY(gamepanel.getPlayer().getScreenY() - p1.getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(p1)) 
				gamepanel.getChildren().add(p1);
			
			buttonR.setLayoutX(gamepanel.SpielLaenge - buttonR.getImage().getWidth());
			buttonR.setLayoutY(gamepanel.getPlayer().getScreenY() - buttonR.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(buttonR)) 
				gamepanel.getChildren().add(buttonR);
			
			buttonL.setLayoutY(gamepanel.getPlayer().getScreenY() - buttonL.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(buttonL)) 
				gamepanel.getChildren().add(buttonL);
		}
		
	}
}              
