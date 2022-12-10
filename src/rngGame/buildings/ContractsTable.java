package rngGame.buildings;

import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
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
	private final  Pane allPanes = new Pane();
	private final  Pane infos = new Pane();
	private final Group UGsachen = new Group();
	private final Group Ausgroup = new Group();
	private  ImageView contractBackround;
	private  ImageView contractSaturn;
	private  ImageView contractNebel;
	private  ImageView contractGalactus;
	private  ImageView contractNova;

	private  ImageView HUD;
	private  ImageView HUD2;
	private  ImageView HUD3;
	private  ImageView HUD4;

	private  ImageView titlebanner;
	private  ImageView titlebanner2;
	private  ImageView titlebanner3;
	private  ImageView titlebanner4;

	private  ImageView text;

	private  ImageView button_R;
	private  ImageView button_L;

	private  ImageView ausBackround;

	private  ImageView UGbackround;
	private  ImageView Startbutton;
	private ImageView lvlBorder;

	private  ImageView ausXb;
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
		Image wi = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Mainbackround.png");
		Image saturn = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Saturn.png");
		Image nebel = ImgUtil.getScaledImage(gamepanel,"./res/Contractstuff/Nebel.png");
		Image galactus = ImgUtil.getScaledImage(gamepanel,"./res/Contractstuff/Galactus.png");
		Image nova =  ImgUtil.getScaledImage(gamepanel,"./res/Contractstuff/nova.png");

		Image hudp = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Ping.gif");
		Image titlep = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/title.png");
		Image ka = Text.getInstance().convertText("Underwater Caverns", 48);
		ka = ImgUtil.resizeImage(
				ka, (int) ka.getWidth(), (int) ka.getHeight(),
				(int) (ka.getWidth() * gamepanel.getScalingFactorX()),
				(int) (ka.getHeight() * gamepanel.getScalingFactorY()));


		Image ausbc = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MapAuswahlBackround.png");

		Image UGbc = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackround.png");
		Image UGbc2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/UGbackround2.png");
		Image sButton = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Startbutton.png");
		Image sButton2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Startbutton2.png");

		Image lvlb = ImgUtil.getScaledImage(gamepanel, "./res/lvl/richtig/LvLBorder.png");
		lvlb = ImgUtil.resizeImage(
				lvlb, (int) lvlb.getWidth(), (int) lvlb.getHeight(),
				(int) (96 * gamepanel.getScalingFactorX()),
				(int) (96 * gamepanel.getScalingFactorY()));

		Image ausX = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Xbutton.png");
		Image ausX2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/XbuttonC.png");

		Image buttonR = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/pfeilR.png");
		Image buttonL = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/pfeilL.png");
		Image buttonRL = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/pfeilRLeu.png");
		Image buttonLL = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/pfeilLLeu.png");

		contractBackround = new ImageView(wi);
		contractBackround.setTranslateY(-1);
		contractBackround.setTranslateX(gamepanel.SpielLaenge / 2 * 3);

		contractSaturn = new ImageView(saturn);
		contractNebel = new ImageView(nebel);
		contractGalactus = new ImageView(galactus);
		contractNova = new ImageView(nova);

		text = new ImageView(ka);

		HUD = new ImageView(hudp);
		HUD.setScaleX(gamepanel.getScalingFactorX());
		HUD.setScaleY(gamepanel.getScalingFactorY());
		HUD.setOpacity(0);

		HUD2 = new ImageView(hudp);
		HUD2.setScaleX(gamepanel.getScalingFactorX());
		HUD2.setScaleY(gamepanel.getScalingFactorY());
		HUD2.setOpacity(0);

		HUD3 = new ImageView(hudp);
		HUD3.setScaleX(gamepanel.getScalingFactorX());
		HUD3.setScaleY(gamepanel.getScalingFactorY());
		HUD3.setOpacity(0);

		HUD4 = new ImageView(hudp);
		HUD4.setScaleX(gamepanel.getScalingFactorX());
		HUD4.setScaleY(gamepanel.getScalingFactorY());
		HUD4.setOpacity(0);

		titlebanner = new ImageView(titlep);
		titlebanner.setOpacity(0);
		titlebanner2 = new ImageView(titlep);
		titlebanner2.setOpacity(0);
		titlebanner3 = new ImageView(titlep);
		titlebanner3.setOpacity(0);
		titlebanner4 = new ImageView(titlep);
		titlebanner4.setOpacity(0);


		ausBackround = new ImageView(ausbc);

		UGbackround = new ImageView(UGbc);
		Startbutton = new ImageView(sButton);
		lvlBorder = new ImageView(lvlb);

		Image UG_1 = UGbc;
		Image UG_2 = UGbc2;

		ausXb = new ImageView(ausX);

		button_R = new ImageView(buttonR);
		button_L = new ImageView(buttonL);
		Image x1 = ausX;
		Image x2 = ausX2;
		Image brl = buttonRL;
		Image bll = buttonLL;
		Image br = buttonR;
		Image bl = buttonL;

		button_R.setOnMouseReleased(me->{
			button_R.setImage(brl);
			TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackround);

			//tt.setFromX(gamepanel.SpielLaenge*-index);
			tt.setToX(-gamepanel.SpielLaenge-gamepanel.SpielLaenge*index);

			//tth.setFromX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);
			tth.setToX(gamepanel.SpielLaenge / 2 * -(index + 1) + gamepanel.SpielLaenge+gamepanel.getScalingFactorX()*24);


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
			FadeTransition pin1 = new FadeTransition(Duration.millis(500), HUD);
			FadeTransition pin2 = new FadeTransition(Duration.millis(500), HUD2);
			FadeTransition pin3 = new FadeTransition(Duration.millis(500), HUD3);
			FadeTransition pin4 = new FadeTransition(Duration.millis(500), HUD4);
			FadeTransition tit1 = new FadeTransition(Duration.millis(1250), titlebanner);
			FadeTransition tit2 = new FadeTransition(Duration.millis(1250), titlebanner2);
			FadeTransition tit3 = new FadeTransition(Duration.millis(1250), titlebanner3);
			FadeTransition tit4 = new FadeTransition(Duration.millis(1250), titlebanner4);
			FadeTransition _tiw = new FadeTransition(Duration.millis(750), text);

			if(index == 0) {
				_tiw.setToValue(0);
				pin1.setToValue(0);
				tit1.setToValue(0);
				tit2.setToValue(1);
				pin2.setToValue(1);

				_tiw.play();
				pin1.play();
				tit1.play();
				tit2.play();
				pin2.play();
			}
			if(index == 1) {
				pin2.setToValue(0);
				tit2.setToValue(0);
				tit3.setToValue(1);
				pin3.setToValue(1);

				pin2.play();
				tit2.play();
				tit3.play();
				pin3.play();
			}
			if(index == 2) {
				pin3.setToValue(0);
				tit3.setToValue(0);
				tit4.setToValue(1);
				pin4.setToValue(1);

				pin3.play();
				tit3.play();
				tit4.play();
				pin4.play();
			}


			index++;
			button_L.setVisible(true);
			if(index >= 4) button_R.setVisible(false);
		});
		button_L.setOnMouseReleased(me->{
			button_L.setImage(bll);
			TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackround);

			//tt.setFromX(gamepanel.SpielLaenge*-index);
			tt.setToX(gamepanel.SpielLaenge+gamepanel.SpielLaenge*-index);

			//tth.setFromX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);
			tth.setToX(gamepanel.SpielLaenge / 2 * -(index - 1) + gamepanel.SpielLaenge);


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
			FadeTransition pin1 = new FadeTransition(Duration.millis(500), HUD);
			FadeTransition pin2 = new FadeTransition(Duration.millis(500), HUD2);
			FadeTransition pin3 = new FadeTransition(Duration.millis(500), HUD3);
			FadeTransition pin4 = new FadeTransition(Duration.millis(500), HUD4);
			FadeTransition tit1 = new FadeTransition(Duration.millis(1250), titlebanner);
			FadeTransition tit2 = new FadeTransition(Duration.millis(1250), titlebanner2);
			FadeTransition tit3 = new FadeTransition(Duration.millis(1250), titlebanner3);
			FadeTransition tit4 = new FadeTransition(Duration.millis(1250), titlebanner4);
			FadeTransition _tiw = new FadeTransition(Duration.millis(750), text);

			if(index == 1) {
				pin2.setToValue(0);
				tit2.setToValue(0);
				tit1.setToValue(1);
				pin1.setToValue(1);
				_tiw.setToValue(1);

				_tiw.play();
				pin2.play();
				tit2.play();
				tit1.play();
				pin1.play();
			}
			if(index == 2) {
				pin3.setToValue(0);
				tit3.setToValue(0);
				tit2.setToValue(1);
				pin2.setToValue(1);

				pin3.play();
				tit3.play();
				tit2.play();
				pin2.play();
			}
			if(index == 3) {
				pin4.setToValue(0);
				tit4.setToValue(0);
				tit3.setToValue(1);
				pin3.setToValue(1);

				pin4.play();
				tit4.play();
				tit3.play();
				pin3.play();
			}
			index--;
			button_R.setVisible(true);
			if(index <= 0) button_L.setVisible(false);
		});

		UGbackround.setOnMouseReleased(me->{
			UGbackround.setImage(UG_2);

			FadeTransition st = new FadeTransition(Duration.millis(100), Startbutton);
			FadeTransition st2 = new FadeTransition(Duration.millis(100), infos);
			st.setFromValue(0);
			st.setToValue(1);
			st.play();
			st2.setFromValue(0);
			st2.setToValue(1);
			st2.play();
			Startbutton.setVisible(true);
			infos.setVisible(true);


		});

		Startbutton.setOnMousePressed(me->{
			Startbutton.setImage(sButton2);
		});
		Startbutton.setOnMouseReleased(me->{
			Startbutton.setImage(sButton);
		});

		///////////////////////
		contractSaturn.setOnMouseReleased(me->{
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackround);

			tt.setToX(-gamepanel.SpielLaenge/4-gamepanel.SpielLaenge*index);
			tth.setToX(gamepanel.SpielLaenge / 2 * -(index + 1) + gamepanel.SpielLaenge/1.5+gamepanel.getScalingFactorX()*24);


			UGbackround.setImage(UG_1);

			FadeTransition UG = new FadeTransition(Duration.millis(350), allPanes);
			UG.setFromValue(0);
			UG.setToValue(1);
			UG.play();

			FadeTransition _tiw = new FadeTransition(Duration.millis(1250), text);
			_tiw.setFromValue(0);
			_tiw.setToValue(1);

			allPanes.setVisible(true);
			infos.setVisible(false);
			Startbutton.setVisible(false);
			text.setVisible(true);
			button_R.setVisible(false);
			button_L.setVisible(false);
			_tiw.play();
			tt.play();
			tth.play();

		});
		///////////////////////
		contractNebel.setOnMouseReleased(me->{
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackround);

			tt.setToX(-gamepanel.SpielLaenge/4-gamepanel.SpielLaenge*index);
			tth.setToX(gamepanel.SpielLaenge / 2 * -(index + 1) + gamepanel.SpielLaenge/1.5+gamepanel.getScalingFactorX()*24);

			FadeTransition ft5 = new FadeTransition(Duration.millis(350), allPanes);
			ft5.setFromValue(0);
			ft5.setToValue(1);
			ft5.play();



			allPanes.setVisible(true);
			button_R.setVisible(false);
			button_L.setVisible(false);
			tt.play();
			tth.play();

		});
		///////////////////////
		contractGalactus.setOnMouseReleased(me->{
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackround);

			tt.setToX(-gamepanel.SpielLaenge/4-gamepanel.SpielLaenge*index);
			tth.setToX(gamepanel.SpielLaenge / 2 * -(index + 1) + gamepanel.SpielLaenge/1.5+gamepanel.getScalingFactorX()*24);

			FadeTransition ft5 = new FadeTransition(Duration.millis(350), allPanes);
			ft5.setFromValue(0);
			ft5.setToValue(1);
			ft5.play();



			allPanes.setVisible(true);
			button_R.setVisible(false);
			button_L.setVisible(false);


			tt.play();
			tth.play();

		});
		///////////////////////
		contractNova.setOnMouseReleased(me->{
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackround);

			tt.setToX(-gamepanel.SpielLaenge/4-gamepanel.SpielLaenge*index);
			tth.setToX(gamepanel.SpielLaenge / 2 * -(index + 1) + gamepanel.SpielLaenge/1.5+gamepanel.getScalingFactorX()*24);

			FadeTransition ft5 = new FadeTransition(Duration.millis(350), allPanes);
			ft5.setFromValue(0);
			ft5.setToValue(1);
			ft5.play();



			allPanes.setVisible(true);
			button_R.setVisible(false);
			button_L.setVisible(false);
			tt.play();
			tth.play();

		});
		///////////////////////
		ausXb.setOnMousePressed(me->{
			ausXb.setImage(x2);
		});

		ausXb.setOnMouseReleased(me->{

			UGbackround.setImage(UG_1);
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackround);

			tt.setToX(gamepanel.SpielLaenge+gamepanel.SpielLaenge*(-index-1));
			tth.setToX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);

			FadeTransition UG = new FadeTransition(Duration.millis(200), allPanes);
			UG.setFromValue(1);
			UG.setToValue(0);
			UG.play();




			new Thread(()->{
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				allPanes.setVisible(false);
				ausXb.setImage(x1);
			}).start();

			button_R.setVisible(true);
			button_L.setVisible(true);


			FadeTransition pin1 = new FadeTransition(Duration.millis(0), HUD);
			FadeTransition pin2 = new FadeTransition(Duration.millis(0), HUD2);
			FadeTransition pin3 = new FadeTransition(Duration.millis(0), HUD3);
			FadeTransition pin4 = new FadeTransition(Duration.millis(0), HUD4);
			FadeTransition tit1 = new FadeTransition(Duration.millis(2000), titlebanner);
			FadeTransition tit2 = new FadeTransition(Duration.millis(2000), titlebanner2);
			FadeTransition tit3 = new FadeTransition(Duration.millis(2000), titlebanner3);
			FadeTransition tit4 = new FadeTransition(Duration.millis(2000), titlebanner4);

			pin1.setFromValue(0);
			pin1.setToValue(1);
			tit1.setFromValue(0);
			tit1.setToValue(1);
			tit2.setFromValue(0);
			tit2.setToValue(1);
			tit3.setFromValue(0);
			tit3.setToValue(1);
			tit4.setFromValue(0);
			tit4.setToValue(1);

			if(index == 0) {
				pin1.play();
				tit2.play();
				HUD.setVisible(true);
				titlebanner.setVisible(true);
				titlebanner2.setVisible(true);

			}
			if(index == 1) {
				pin2.play();
				tit3.play();
				HUD2.setVisible(true);
				titlebanner2.setVisible(true);
				titlebanner3.setVisible(true);

			}
			if(index == 2) {
				pin3.play();
				tit4.play();
				HUD3.setVisible(true);
				titlebanner3.setVisible(true);
				titlebanner4.setVisible(true);
			}
			if(index == 4) {
				pin4.play();
				HUD4.setVisible(true);
				titlebanner4.setVisible(true);
			}
			tt.play();
			tth.play();
		});
		/////////////////////
		//Hintergrund
		contractBackround.setX(gamepanel.getPlayer().getScreenX() - contractBackround.getImage().getWidth() / 2 + 48);
		contractBackround.setY(gamepanel.getPlayer().getScreenY() - contractBackround.getImage().getHeight() / 2 + 32);
		contractBackround.setVisible(false);
		gamepanel.getChildren().add(contractBackround);
		gamepanel.getChildren().add(p1);
		gamepanel.getChildren().add(button_R);
		gamepanel.getChildren().add(button_L);

		Ausgroup.getChildren().addAll(ausBackround, ausXb);

		//contractNebel.setY(gamepanel.getPlayer().getScreenY() - contractNebel.getImage().getHeight() / 2 + 32);

		contractNebel.setX(gamepanel.SpielLaenge);
		contractGalactus.setX(gamepanel.SpielLaenge*2);
		contractNova.setX(gamepanel.SpielLaenge*3);

		titlebanner2.setX(gamepanel.SpielLaenge);
		titlebanner3.setX(gamepanel.SpielLaenge*2);
		titlebanner4.setX(gamepanel.SpielLaenge*3);

		HUD.setX(HUD.getImage().getWidth() / 2 * gamepanel.getScalingFactorX() - HUD.getImage().getWidth() / 2);
		HUD2.setX(gamepanel.SpielLaenge + HUD2.getImage().getWidth() / 2 * gamepanel.getScalingFactorX()
				- HUD2.getImage().getWidth() / 2);
		HUD3.setX(gamepanel.SpielLaenge * 2 + HUD3.getImage().getWidth() / 2 * gamepanel.getScalingFactorX()
				- HUD3.getImage().getWidth() / 2);
		HUD4.setX(gamepanel.SpielLaenge * 3 + HUD4.getImage().getWidth() / 2 * gamepanel.getScalingFactorX()
				- HUD4.getImage().getWidth() / 2);
		HUD.setY(HUD.getImage().getHeight() / 2 * gamepanel.getScalingFactorY() - HUD.getImage().getHeight() / 2);
		HUD2.setY(HUD2.getImage().getHeight() / 2 * gamepanel.getScalingFactorY() - HUD2.getImage().getHeight() / 2);
		HUD3.setY(HUD3.getImage().getHeight() / 2 * gamepanel.getScalingFactorY() - HUD3.getImage().getHeight() / 2);
		HUD4.setY(HUD4.getImage().getHeight() / 2 * gamepanel.getScalingFactorY() - HUD4.getImage().getHeight() / 2);

		//add
		p1.setVisible(false);
		p1.getChildren().addAll(contractSaturn,contractNebel,contractGalactus,contractNova,HUD,HUD2,HUD3,HUD4,titlebanner,titlebanner2,titlebanner3,titlebanner4,text);


		text.setX(gamepanel.SpielLaenge/2-164*gamepanel.getScalingFactorX());
		text.setY(gamepanel.SpielHoehe/4+gamepanel.SpielHoehe/2+20*gamepanel.getScalingFactorY());

		text.setVisible(false);

		//buttons
		button_R.setVisible(false);
		button_L.setVisible(false);

		// P2
		UGsachen.getChildren().addAll(UGbackround, Startbutton);
		Startbutton.setX(383 * gamepanel.getScalingFactorX());
		Startbutton.setY(4 * gamepanel.getScalingFactorY());
		p2.getChildren().addAll(UGsachen);

		// Infos
		infos.setLayoutX(gamepanel.SpielLaenge / 2 + 11 * gamepanel.getScalingFactorX());
		infos.getChildren().add(lvlBorder);

		allPanes.setVisible(false);
		allPanes.getChildren().addAll(Ausgroup, p2, infos);

		Input.getInstance().setKeyHandler("contractbackround", mod -> {

			if (inkreis) if (iftest = !iftest) {
				contractBackround.setTranslateX(gamepanel.SpielLaenge);
				gamepanel.setBlockUserInputs(true);
				contractBackround.setVisible(true);
				p1.setVisible(true);
				text.setVisible(true);
				HUD.setVisible(true);
				titlebanner.setVisible(true);
				button_R.setVisible(true);
				titlebanner.setOpacity(1);
				HUD.setOpacity(1);

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

			}
			else {
				contractBackround.setVisible(false);
				p1.setVisible(false);
				text.setVisible(false);
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
			gamepanel.getChildren().remove(allPanes);
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
			///////////////////////////////////////////////
			if(!gamepanel.getChildren().contains(allPanes))
				gamepanel.getChildren().add(allPanes);

			p2.setLayoutX(gamepanel.SpielLaenge / 2 + 11 * gamepanel.getScalingFactorX());
			p2.setLayoutY(108 * gamepanel.getScalingFactorY());
			//////////////////////////////////////////////
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
