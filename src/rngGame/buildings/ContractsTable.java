package rngGame.buildings;

import java.io.*;
import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.main.*;
import rngGame.tile.ImgUtil;

public class ContractsTable extends Building {

	private final  Pane p1 = new Pane();
	private final  Pane p2 = new Pane();

	private  ImageView contractBackround;
	private  ImageView contractRifts;

	private  ImageView contractSaturn;
	private  ImageView contractNebel;
	private  ImageView contractGalactus;
	private  ImageView contractNova;
	private  ImageView button_R;
	private  ImageView button_L;
	private int index = 0;
	private boolean iftest;
	private boolean inkreis;
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

	private void init() {
		try {
			Image wi = new Image(new FileInputStream("./res/Contractstuff/Mainbackround.png"));
			Image saturn = new Image(new FileInputStream("./res/Contractstuff/Saturn.png"));
			Image nebel = new Image(new FileInputStream("./res/Contractstuff/Nebel.png"));
			Image galactus = new Image(new FileInputStream("./res/Contractstuff/Galactus.png"));
			Image Nova = new Image(new FileInputStream("./res/Contractstuff/nova.png"));
			Image TearStone = new Image(new FileInputStream("./res/Contractstuff/TearStoneCaveLogo.png"));
			Image buttonR = new Image(new FileInputStream("./res/Contractstuff/pfeilR.png"));
			Image buttonL = new Image(new FileInputStream("./res/Contractstuff/pfeilL.png"));
			Image buttonRL = new Image(new FileInputStream("./res/Contractstuff/pfeilRLeu.png"));
			Image buttonLL = new Image(new FileInputStream("./res/Contractstuff/pfeilLLeu.png"));
			
			wi=ImgUtil.resizeImage(
					wi,(int) wi.getWidth(), (int) wi.getHeight(), (int) (wi.getWidth() * gamepanel.getScalingFactorX()),
					(int) (wi.getHeight() * gamepanel.getScalingFactorY()));

			saturn=ImgUtil.resizeImage(
					saturn,(int) saturn.getWidth(), (int) saturn.getHeight(), (int) (saturn.getWidth() * gamepanel.getScalingFactorX()),
					(int) (saturn.getHeight() * gamepanel.getScalingFactorY()));

			nebel=ImgUtil.resizeImage(
					nebel,(int) nebel.getWidth(), (int) nebel.getHeight(), (int) (nebel.getWidth() * gamepanel.getScalingFactorX()),
					(int) (nebel.getHeight() * gamepanel.getScalingFactorY()));
			
			galactus=ImgUtil.resizeImage(
					galactus,(int) galactus.getWidth(), (int) galactus.getHeight(), (int) (galactus.getWidth() * gamepanel.getScalingFactorX()),
					(int) (galactus.getHeight() * gamepanel.getScalingFactorY()));
			
			Nova=ImgUtil.resizeImage(
					Nova,(int) Nova.getWidth(), (int) Nova.getHeight(), (int) (Nova.getWidth() * gamepanel.getScalingFactorX()),
					(int) (Nova.getHeight() * gamepanel.getScalingFactorY()));
			
			TearStone=ImgUtil.resizeImage(
					TearStone,(int) TearStone.getWidth(), (int) TearStone.getHeight(), (int) (TearStone.getWidth() * gamepanel.getScalingFactorX()),
					(int) (TearStone.getHeight() * gamepanel.getScalingFactorY()));

			buttonR=ImgUtil.resizeImage(
					buttonR,(int) buttonR.getWidth(), (int) buttonR.getHeight(), (int) (buttonR.getWidth() * gamepanel.getScalingFactorX()),
					(int) (buttonR.getHeight() * gamepanel.getScalingFactorY()));

			buttonL=ImgUtil.resizeImage(
					buttonL,(int) buttonL.getWidth(), (int) buttonL.getHeight(), (int) (buttonL.getWidth() * gamepanel.getScalingFactorX()),
					(int) (buttonL.getHeight() * gamepanel.getScalingFactorY()));
			
			buttonRL=ImgUtil.resizeImage(
					buttonRL,(int) buttonRL.getWidth(), (int) buttonRL.getHeight(), (int) (buttonRL.getWidth() * gamepanel.getScalingFactorX()),
					(int) (buttonRL.getHeight() * gamepanel.getScalingFactorY()));
			
			buttonLL=ImgUtil.resizeImage(
					buttonLL,(int) buttonLL.getWidth(), (int) buttonLL.getHeight(), (int) (buttonLL.getWidth() * gamepanel.getScalingFactorX()),
					(int) (buttonLL.getHeight() * gamepanel.getScalingFactorY()));

			contractBackround = new ImageView(wi);
			contractBackround.setTranslateY(-1);
			contractBackround.setTranslateX(gamepanel.SpielLaenge / 2 * 3);
			contractRifts = new ImageView(TearStone);
			contractSaturn = new ImageView(saturn);
			contractNebel = new ImageView(nebel);
			contractGalactus = new ImageView(galactus);
			contractNova = new ImageView(Nova);
			button_R = new ImageView(buttonR);
			button_L = new ImageView(buttonL);
			Image brl = buttonRL;
			Image bll = buttonLL;
			Image br = buttonR;
			Image bl = buttonL;


			button_R.setOnMouseReleased(me->{
				button_R.setImage(brl);
				TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
				//tt.setFromX(gamepanel.SpielLaenge*-index);
				tt.setToX(-gamepanel.SpielLaenge-gamepanel.SpielLaenge*index);
				
				TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackround);
				//tth.setFromX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);
				tth.setToX(gamepanel.SpielLaenge / 2 * -(index + 1) + gamepanel.SpielLaenge+gamepanel.getScalingFactorX()*24);
				
				TranslateTransition tth2 = new TranslateTransition(Duration.millis(1050), p2);
				tth2.setToX(-gamepanel.SpielLaenge-gamepanel.SpielLaenge*index);
				
				new Thread(()->{
					try {
						Thread.sleep(125);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					button_R.setImage(br);
				}).start();
				tt.play();
				tth.play();
				tth2.play();
				
				index++;
				button_L.setVisible(true);
				if(index == 4) {
					button_R.setVisible(false);
				}
			});
			this.button_L.setOnMouseReleased(me->{
				button_L.setImage(bll);
				TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
				//tt.setFromX(gamepanel.SpielLaenge*-index);
				tt.setToX(gamepanel.SpielLaenge+gamepanel.SpielLaenge*-index);
				
				TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackround);
				//tth.setFromX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);
				tth.setToX(gamepanel.SpielLaenge / 2 * -(index - 1) + gamepanel.SpielLaenge);
				
				TranslateTransition tth2 = new TranslateTransition(Duration.millis(1050), p2);
				tth2.setToX(gamepanel.SpielLaenge+gamepanel.SpielLaenge*-index);
				
				new Thread(()->{
					try {
						Thread.sleep(125);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					button_L.setImage(bl);
				}).start();
				tt.play();
				tth.play();
				tth2.play();
				index--;
				button_R.setVisible(true);
				if(index == 0) {
					button_L.setVisible(false);
				}
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
		gamepanel.getChildren().add(p2);
		gamepanel.getChildren().add(button_R);
		gamepanel.getChildren().add(button_L);
		//Saturn
		p1.getChildren().add(contractSaturn);
		//Nebel
		contractNebel.setX(gamepanel.SpielLaenge);
		//contractNebel.setY(gamepanel.getPlayer().getScreenY() - contractNebel.getImage().getHeight() / 2 + 32);
		contractGalactus.setX(gamepanel.SpielLaenge*2);
		contractNova.setX(gamepanel.SpielLaenge*3);
		//add
		p1.setVisible(false);
		p1.getChildren().add(contractNebel);
		p1.getChildren().add(contractGalactus);
		p1.getChildren().add(contractNova);
		//Rifts
		p2.setVisible(false);
		p2.getChildren().add(contractRifts);
		
		//buttons
		button_R.setVisible(false);
		button_L.setVisible(false);

		Input.getInstance().setKeyHandler("contractbackround", mod -> {

			if (inkreis) if (iftest = !iftest) {
				contractBackround.setTranslateX(gamepanel.SpielLaenge);
				gamepanel.setBlockUserInputs(true);
				contractBackround.setVisible(true);
				p1.setVisible(true);
				button_R.setVisible(true);
				p2.setVisible(true);


				FadeTransition ft = new FadeTransition(Duration.millis(500), contractBackround);
				ft.setFromValue(0);
				ft.setToValue(1);
				ft.play();

				FadeTransition ft2 = new FadeTransition(Duration.millis(1000), p1);
				ft2.setFromValue(0);
				ft2.setToValue(1);
				ft2.play();

				FadeTransition ft3 = new FadeTransition(Duration.millis(1000), button_L);
				ft3.setFromValue(0);
				ft3.setToValue(1);
				ft3.play();

				FadeTransition ft4 = new FadeTransition(Duration.millis(1000), button_R);
				ft4.setFromValue(0);
				ft4.setToValue(1);
				ft4.play();
				
				FadeTransition ft5 = new FadeTransition(Duration.millis(1000), p2);
				ft5.setFromValue(0);
				ft5.setToValue(1);
				ft5.play();
			}
			else {
				contractBackround.setVisible(false);
				p1.setVisible(false);
				p2.setVisible(false);
				button_R.setVisible(false);
				button_L.setVisible(false);
				gamepanel.setBlockUserInputs(false);
				index = 0;
				p1.setTranslateX(0);
				contractBackround.setTranslateX(gamepanel.SpielLaenge);
			}
		}, KeyCode.ENTER, false);

		getMiscBoxHandler().put("table", (gpt, self) -> {
			inkreis = true;
			gamepanel.getBuildings().parallelStream().filter(b -> b.getTextureFiles().containsValue("CTischCircle.png")).forEach(b -> {
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
			gamepanel.getBuildings().parallelStream().filter(b -> b.getTextureFiles().containsValue("CTischCircle.png")).forEach(b -> {
				b.setCurrentKey("default");
			});
			contractBackround.setVisible(false);
			gamepanel.getChildren().remove(contractBackround);
			gamepanel.getChildren().remove(p1);
			gamepanel.getChildren().remove(p2);
			gamepanel.getChildren().remove(button_R);
			gamepanel.getChildren().remove(button_L);

		} else {
			
			contractBackround.setY(gamepanel.getPlayer().getScreenY() - contractBackround.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(contractBackround))
				gamepanel.getChildren().add(contractBackround);

			p1.setLayoutX(gamepanel.getPlayer().getScreenX() - gamepanel.SpielLaenge / 2 + gamepanel.getPlayer().getWidth() / 2);
			p1.setLayoutY(gamepanel.getPlayer().getScreenY() - p1.getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(p1))
				gamepanel.getChildren().add(p1);
			
			p2.setLayoutX(gamepanel.getPlayer().getScreenX() - gamepanel.SpielLaenge / 2 + gamepanel.getPlayer().getWidth() / 2);
			p2.setLayoutY(gamepanel.getPlayer().getScreenY() - p2.getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(p2))
				gamepanel.getChildren().add(p2);

			button_R.setLayoutX(gamepanel.SpielLaenge - button_R.getImage().getWidth());
			button_R.setLayoutY(gamepanel.getPlayer().getScreenY() - button_R.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(button_R))
				gamepanel.getChildren().add(button_R);

			button_L.setLayoutY(gamepanel.getPlayer().getScreenY() - button_L.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(button_L))
				gamepanel.getChildren().add(button_L);
		}

	}
}
