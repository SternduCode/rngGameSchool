package rngGame.buildings;

import java.util.List;

import com.sterndu.json.JsonObject;

import javafx.animation.*;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.main.*;
import rngGame.tile.*;
import rngGame.ui.*;
import rngGame.visual.GamePanel;

/**
 * The Class ContractsTable.
 */
public class ContractsTable extends Building {

	/** The animation counter pin. */
	private long animationCounterPin = 0;

	/** The animation num pin. */
	private int animationNumPin = 0;

	/** The p 1. */
	private final Pane p1 = new Pane();

	/** The p 2. */
	private final Pane p2 = new Pane();

	/** The all panes. */
	private final Pane allPanes = new Pane();

	/** The infos. */
	private final Pane infos = new Pane();

	/** The ug sachen 5. */
	private LevelSelectionScrollPaneElement ugSachen1,ugSachen2,ugSachen3,ugSachen4,ugSachen5;

	/** The aus group. */
	private final Group ausGroup = new Group();

	/** The contract background. */
	private ImageView contractBackground;

	/** The contract nova. */
	private Button contractSaturn, contractNebel, contractGalactus, contractNova;

	/** The hudp. */
	private Image[] hudp;

	/** The hud 4. */
	private ImageView hud, hud2, hud3, hud4;

	/** The titlebanner 4. */
	private ImageView titlebanner;

	/** The text. */
	private ImageView text;

	/** The button L. */
	private Button button_R, button_L;

	/** The aus background. */
	private ImageView ausBackground;

	/** The lvl border. */
	private ImageView lvlBorder;

	/** The aus xb. */
	private Button ausXb,ausXb2;

	/** The index. */
	private int index = 0;

	private boolean inkreis;

	/**
	 * Instantiates a new contracts table.
	 *
	 * @param building the building
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public ContractsTable(Building building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);
		init();
	}

	/**
	 * Instantiates a new contracts table.
	 *
	 * @param building the building
	 * @param gp the gp
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public ContractsTable(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		init();
	}

	/**
	 * Instantiates a new contracts table.
	 *
	 * @param building the building
	 * @param gp the gp
	 * @param directory the directory
	 * @param buildings the buildings
	 * @param cm the cm
	 * @param requestorB the requestor B
	 */
	public ContractsTable(JsonObject building, GamePanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		Image wi = ImgUtil.getScaledImage("./res/Contractstuff/Mainbackground.png");

		hudp = ImgUtil.getScaledImages("./res/Contractstuff/Ping.gif");
		Image titlep = ImgUtil.getScaledImage("./res/Contractstuff/title.png");
		Image ka = Text.getInstance().convertText("Underwater Caverns", 48);
		ka = ImgUtil.resizeImage(
				ka, (int) ka.getWidth(), (int) ka.getHeight(),
				(int) (ka.getWidth() * WindowManager.getInstance().getScalingFactorX()),
				(int) (ka.getHeight() * WindowManager.getInstance().getScalingFactorY()));

		Image ausbc = ImgUtil.getScaledImage("./res/Contractstuff/MapAuswahlBackground.png");

		Image lvlb = ImgUtil.getScaledImage("./res/lvl/richtig/LvLBorder.png");
		lvlb = ImgUtil.resizeImage(
				lvlb, (int) lvlb.getWidth(), (int) lvlb.getHeight(),
				(int) (96 * WindowManager.getInstance().getScalingFactorX()),
				(int) (96 * WindowManager.getInstance().getScalingFactorY()));

		Image buttonRL = ImgUtil.getScaledImage("./res/Contractstuff/pfeilRLeu.png");
		Image buttonLL = ImgUtil.getScaledImage("./res/Contractstuff/pfeilLLeu.png");

		Image difE = ImgUtil.getScaledImage("./res/Contractstuff/difE.png");
		Image difH = ImgUtil.getScaledImage("./res/Contractstuff/difH.png");
		Image difM = ImgUtil.getScaledImage("./res/Contractstuff/difM.png");

		contractBackground = new ImageView(wi);
		contractBackground.setTranslateY(-1);
		// contractBackground.setTranslateX(gamepanel.SpielLaenge / 2 * 3);

		contractSaturn		= new Button("./res/Contractstuff/Himmel_Galaxy.gif", 25);
		contractNebel		= new Button("./res/Contractstuff/Lava_Galaxy.gif", 25);
		contractGalactus	= new Button("./res/Contractstuff/Strand_Galaxy.gif", 25);
		contractNova		= new Button("./res/Contractstuff/Ice_Galaxy.gif", 25);

		text = new ImageView(ka);

		hud = new ImageView(hudp[0]);
		hud.setOpacity(0);

		hud2 = new ImageView(hudp[0]);
		hud2.setOpacity(0);

		hud3 = new ImageView(hudp[0]);
		hud3.setOpacity(0);

		hud4 = new ImageView(hudp[0]);
		hud4.setOpacity(0);

		titlebanner = new ImageView(titlep);
		titlebanner.setOpacity(0);
//		titlebanner2 = new ImageView(titlep);
//		titlebanner2.setOpacity(0);
//		titlebanner3 = new ImageView(titlep);
//		titlebanner3.setOpacity(0);
//		titlebanner4 = new ImageView(titlep);
//		titlebanner4.setOpacity(0);

		ausBackground = new ImageView(ausbc);
		lvlBorder = new ImageView(lvlb);

		ausXb = new Button("./res/Contractstuff/Xbutton.png");
		ausXb2 = new Button("./res/Contractstuff/Xbutton.png");
		ausXb2.setVisible(false);
		
		button_R	= new Button("./res/Contractstuff/pfeilR.png");
		button_L	= new Button("./res/Contractstuff/pfeilL.png");

		Image brl = buttonRL;
		Image bll = buttonLL;



		ugSachen1	= new LevelSelectionScrollPaneElement(gamepanel, this, " Eingang", index * 25 + 0, "Icon_Eingang", Difficulty.EASY);
		ugSachen2	= new LevelSelectionScrollPaneElement(gamepanel, this, " Passage", index * 25 + 5, "Icon_Passage", Difficulty.EASY);
		ugSachen3	= new LevelSelectionScrollPaneElement(gamepanel, this, " Kreuzung", index * 25 + 10, "Icon_Kreuzung", Difficulty.MIDDLE);
		ugSachen4	= new LevelSelectionScrollPaneElement(gamepanel, this, " Untergrund", index * 25 + 15, "Icon_Untergrund", Difficulty.MIDDLE);
		ugSachen5	= new LevelSelectionScrollPaneElement(gamepanel, this, " Zentrum", index * 25 + 20, "Icon_Zentrum", Difficulty.HARD);

		button_R.setOnAction(me -> {
			button_R.setImage(brl);
			TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackground);

			// tt.setFromX(gamepanel.SpielLaenge*-index);
			tt.setToX(-WindowManager.getInstance().getGameWidth() - WindowManager.getInstance().getGameWidth() * index);

			// tth.setFromX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);
			tth.setToX(WindowManager.getInstance().getGameWidth() / 2 * - (index + 1) + WindowManager.getInstance().getGameWidth()
			+ WindowManager.getInstance().getScalingFactorX() * 24);

			new Thread(() -> {
				try {
					Thread.sleep(125);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				button_R.init("./res/Contractstuff/pfeilR.png");
			}).start();
			tt.play();
			tth.play();
			FadeTransition pin1 = new FadeTransition(Duration.millis(500), hud);
			FadeTransition pin2 = new FadeTransition(Duration.millis(500), hud2);
			FadeTransition pin3 = new FadeTransition(Duration.millis(500), hud3);
			FadeTransition pin4 = new FadeTransition(Duration.millis(500), hud4);
			FadeTransition tit1 = new FadeTransition(Duration.millis(1250), titlebanner);
//			FadeTransition tit2 = new FadeTransition(Duration.millis(1250), titlebanner2);
//			FadeTransition tit3 = new FadeTransition(Duration.millis(1250), titlebanner3);
//			FadeTransition tit4 = new FadeTransition(Duration.millis(1250), titlebanner4);
			FadeTransition _tiw = new FadeTransition(Duration.millis(750), text);

			if (index == 0) {
				_tiw.setToValue(0);
				pin1.setToValue(0);
				tit1.setToValue(0);
//				tit2.setToValue(1);
				pin2.setToValue(1);

				_tiw.play();
				pin1.play();
				tit1.play();
//				tit2.play();
				pin2.play();
			}
			if (index == 1) {
				pin2.setToValue(0);
//				tit2.setToValue(0);
//				tit3.setToValue(1);
				pin3.setToValue(1);

				pin2.play();
//				tit2.play();
//				tit3.play();
				pin3.play();
			}
			if (index == 2) {
				pin3.setToValue(0);
//				tit3.setToValue(0);
//				tit4.setToValue(1);
				pin4.setToValue(1);

				pin3.play();
//				tit3.play();
//				tit4.play();
				pin4.play();
			}

			index++;
			button_L.setVisible(true);
			if (index >= 4) button_R.setVisible(false);
		});
		button_L.setOnAction(me -> {
			button_L.setImage(bll);
			TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(1000), contractBackground);

			// tt.setFromX(gamepanel.SpielLaenge*-index);
			tt.setToX(WindowManager.getInstance().getGameWidth() + WindowManager.getInstance().getGameWidth() * -index);

			// tth.setFromX(gamepanel.SpielLaenge / 2 * -index + gamepanel.SpielLaenge);
			tth.setToX(WindowManager.getInstance().getGameWidth() / 2 * - (index - 1) + WindowManager.getInstance().getGameWidth());

			new Thread(() -> {
				try {
					Thread.sleep(125);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				button_L.init("./res/Contractstuff/pfeilL.png");
			}).start();
			tt.play();
			tth.play();
			FadeTransition pin1 = new FadeTransition(Duration.millis(500), hud);
			FadeTransition pin2 = new FadeTransition(Duration.millis(500), hud2);
			FadeTransition pin3 = new FadeTransition(Duration.millis(500), hud3);
			FadeTransition pin4 = new FadeTransition(Duration.millis(500), hud4);
			FadeTransition tit1 = new FadeTransition(Duration.millis(1250), titlebanner);
//			FadeTransition tit2 = new FadeTransition(Duration.millis(1250), titlebanner2);
//			FadeTransition tit3 = new FadeTransition(Duration.millis(1250), titlebanner3);
//			FadeTransition tit4 = new FadeTransition(Duration.millis(1250), titlebanner4);
			FadeTransition _tiw = new FadeTransition(Duration.millis(750), text);

			if (index == 1) {
				pin2.setToValue(0);
//				tit2.setToValue(0);
				tit1.setToValue(1);
				pin1.setToValue(1);
				_tiw.setToValue(1);

				_tiw.play();
				pin2.play();
//				tit2.play();
				tit1.play();
				pin1.play();
			}
			if (index == 2) {
				pin3.setToValue(0);
//				tit3.setToValue(0);
//				tit2.setToValue(1);
				pin2.setToValue(1);

				pin3.play();
//				tit3.play();
//				tit2.play();
				pin2.play();
			}
			if (index == 3) {
				pin4.setToValue(0);
//				tit4.setToValue(0);
//				tit3.setToValue(1);
				pin3.setToValue(1);

				pin4.play();
//				tit4.play();
//				tit3.play();
				pin3.play();
			}
			index--;
			button_R.setVisible(true);
			if (index <= 0) button_L.setVisible(false);
		});

		///////////////////////
		contractSaturn.setOnAction(me -> {
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackground);

			tt.setToX(-WindowManager.getInstance().getGameWidth() / 4 - WindowManager.getInstance().getGameWidth() * index);
			tth.setToX(WindowManager.getInstance().getGameWidth() / 2 * - (index + 1) + WindowManager.getInstance().getGameWidth() / 1.5
					+ WindowManager.getInstance().getScalingFactorX() * 24);

			ugSachen1.setBackgroundImageToDefaullt();
			ugSachen2.setBackgroundImageToDefaullt();
			ugSachen3.setBackgroundImageToDefaullt();
			ugSachen4.setBackgroundImageToDefaullt();
			ugSachen5.setBackgroundImageToDefaullt();

			FadeTransition UG = new FadeTransition(Duration.millis(350), allPanes);
			UG.setFromValue(0);
			UG.setToValue(1);
			UG.play();

			FadeTransition _tiw = new FadeTransition(Duration.millis(1250), text);
			_tiw.setFromValue(0);
			_tiw.setToValue(1);

			allPanes.setVisible(true);
			infos.setVisible(true);
			ugSachen1.getStartButton().setVisible(false);
			ugSachen2.getStartButton().setVisible(false);
			ugSachen3.getStartButton().setVisible(false);
			ugSachen4.getStartButton().setVisible(false);
			ugSachen5.getStartButton().setVisible(false);
			text.setVisible(true);
			button_R.setVisible(false);
			button_L.setVisible(false);
			_tiw.play();
			tt.play();
			tth.play();

		});
		///////////////////////
//		contractNebel.setOnAction(me -> {
//			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
//			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackground);
//
//			tt.setToX(-gamepanel.getGameWidth() / 4 - gamepanel.getGameWidth() * index);
//			tth.setToX(gamepanel.getGameWidth() / 2 * - (index + 1) + gamepanel.getGameWidth() / 1.5
//					+ gamepanel.getScalingFactorX() * 24);
//
//			FadeTransition ft5 = new FadeTransition(Duration.millis(350), allPanes);
//			ft5.setFromValue(0);
//			ft5.setToValue(1);
//			ft5.play();
//
//			allPanes.setVisible(true);
//			button_R.setVisible(false);
//			button_L.setVisible(false);
//			tt.play();
//			tth.play();
//
//		});
//		///////////////////////
//		contractGalactus.setOnAction(me -> {
//			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
//			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackground);
//
//			tt.setToX(-gamepanel.getGameWidth() / 4 - gamepanel.getGameWidth() * index);
//			tth.setToX(gamepanel.getGameWidth() / 2 * - (index + 1) + gamepanel.getGameWidth() / 1.5
//					+ gamepanel.getScalingFactorX() * 24);
//
//			FadeTransition ft5 = new FadeTransition(Duration.millis(350), allPanes);
//			ft5.setFromValue(0);
//			ft5.setToValue(1);
//			ft5.play();
//
//			allPanes.setVisible(true);
//			button_R.setVisible(false);
//			button_L.setVisible(false);
//
//			tt.play();
//			tth.play();
//
//		});
//		///////////////////////
//		contractNova.setOnAction(me -> {
//			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
//			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackground);
//
//			tt.setToX(-gamepanel.getGameWidth() / 4 - gamepanel.getGameWidth() * index);
//			tth.setToX(gamepanel.getGameWidth() / 2 * - (index + 1) + gamepanel.getGameWidth() / 1.5
//					+ gamepanel.getScalingFactorX() * 24);
//
//			FadeTransition ft5 = new FadeTransition(Duration.millis(350), allPanes);
//			ft5.setFromValue(0);
//			ft5.setToValue(1);
//			ft5.play();
//
//			allPanes.setVisible(true);
//			button_R.setVisible(false);
//			button_L.setVisible(false);
//			tt.play();
//			tth.play();
//
//		});
		///////////////////////
		ausXb.setOnMousePressed(me -> {
			ausXb.init("./res/Contractstuff/XbuttonC.png");
		});



		ausXb.setOnMouseReleased(me -> {
			ugSachen1.setBackgroundImageToDefaullt();
			ugSachen2.setBackgroundImageToDefaullt();
			ugSachen3.setBackgroundImageToDefaullt();
			ugSachen4.setBackgroundImageToDefaullt();
			ugSachen5.setBackgroundImageToDefaullt();
			TranslateTransition tt = new TranslateTransition(Duration.millis(750), p1);
			TranslateTransition tth = new TranslateTransition(Duration.millis(750), contractBackground);

			tt.setToX(WindowManager.getInstance().getGameWidth() + WindowManager.getInstance().getGameWidth() * (-index - 1));
			tth.setToX(WindowManager.getInstance().getGameWidth() / 2 * -index + WindowManager.getInstance().getGameWidth());

			FadeTransition UG = new FadeTransition(Duration.millis(200), allPanes);
			UG.setFromValue(1);
			UG.setToValue(0);
			UG.play();

			new Thread(() -> {
				try {
					Thread.sleep(350);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				allPanes.setVisible(false);
				ausXb.init("./res/Contractstuff/Xbutton.png");
			}).start();

			if (index < 4)
				button_R.setVisible(true);
			if (index > 0)
				button_L.setVisible(true);

			FadeTransition pin1 = new FadeTransition(Duration.millis(0), hud);
			FadeTransition pin2 = new FadeTransition(Duration.millis(0), hud2);
			FadeTransition pin3 = new FadeTransition(Duration.millis(0), hud3);
			FadeTransition pin4 = new FadeTransition(Duration.millis(0), hud4);
			FadeTransition tit1 = new FadeTransition(Duration.millis(2000), titlebanner);
//			FadeTransition tit2 = new FadeTransition(Duration.millis(2000), titlebanner2);
//			FadeTransition tit3 = new FadeTransition(Duration.millis(2000), titlebanner3);
//			FadeTransition tit4 = new FadeTransition(Duration.millis(2000), titlebanner4);

			pin1.setFromValue(0);
			pin1.setToValue(1);
			tit1.setFromValue(0);
			tit1.setToValue(1);
//			tit2.setFromValue(0);
//			tit2.setToValue(1);
//			tit3.setFromValue(0);
//			tit3.setToValue(1);
//			tit4.setFromValue(0);
//			tit4.setToValue(1);

			if (index == 0) {
				pin1.play();
//				tit2.play();
				hud.setVisible(true);
				titlebanner.setVisible(true);
//				titlebanner2.setVisible(true);

			}
			if (index == 1) {
				pin2.play();
//				tit3.play();
				hud2.setVisible(true);
//				titlebanner2.setVisible(true);
//				titlebanner3.setVisible(true);

			}
			if (index == 2) {
				pin3.play();
//				tit4.play();
				hud3.setVisible(true);
//				titlebanner3.setVisible(true);
//				titlebanner4.setVisible(true);
			}
			if (index == 4) {
				pin4.play();
				hud4.setVisible(true);
//				titlebanner4.setVisible(true);
			}
			tt.play();
			tth.play();
		});
		ausXb2.setOnMousePressed(me -> {
			ausXb2.init("./res/Contractstuff/XbuttonC.png");
		});
		ausXb2.setOnMouseReleased(me -> {
			removeStuffs();
			ausXb2.init("./res/Contractstuff/Xbutton.png");
		});
		
		/////////////////////
		// Hintergrund
		contractBackground
		.setLayoutX(gamepanel.getPlayer().getScreenX() - contractBackground.getImage().getWidth() / 2
				+ gamepanel.getPlayer().getWidth());// TODO maybe
		contractBackground.setVisible(false);
		gamepanel.getChildren().add(contractBackground);
		gamepanel.getChildren().add(p1);
		gamepanel.getChildren().add(button_R);
		gamepanel.getChildren().add(button_L);
		gamepanel.getChildren().add(ausXb2);

		ausGroup.getChildren().add(ausBackground);

		// contractNebel.setY(gamepanel.getPlayer().getScreenY() -
		// contractNebel.getImage().getHeight() / 2 + 32);

		contractNebel.setLayoutX(WindowManager.getInstance().getGameWidth());
		contractGalactus.setLayoutX(WindowManager.getInstance().getGameWidth() * 2);
		contractNova.setLayoutX(WindowManager.getInstance().getGameWidth() * 3);

//		titlebanner2.setLayoutX(gamepanel.getGameWidth());
//		titlebanner3.setLayoutX(gamepanel.getGameWidth() * 2);
//		titlebanner4.setLayoutX(gamepanel.getGameWidth() * 3);

		hud2.setLayoutX(WindowManager.getInstance().getGameWidth());
		hud3.setLayoutX(WindowManager.getInstance().getGameWidth() * 2);
		hud4.setLayoutX(WindowManager.getInstance().getGameWidth() * 3);

		// add
		p1.setVisible(false);
		p1.getChildren().addAll(contractSaturn, contractNebel, contractGalactus, contractNova, hud, hud2, hud3, hud4,
				titlebanner, text);

		text.setX(WindowManager.getInstance().getGameWidth() / 2 - 164 * WindowManager.getInstance().getScalingFactorX());
		text.setY(WindowManager.getInstance().getGameHeight() / 4 + WindowManager.getInstance().getGameHeight() / 2 + 20 * WindowManager.getInstance().getScalingFactorY());

		text.setVisible(false);

		// buttons
		button_R.setVisible(false);
		button_L.setVisible(false);

		// P2
		p2.getChildren().addAll(ugSachen1,ugSachen2,ugSachen3,ugSachen4,ugSachen5);

		// Infos
		infos.setLayoutX(WindowManager.getInstance().getGameWidth() / 2 + 11 * WindowManager.getInstance().getScalingFactorX());
		infos.getChildren().add(lvlBorder);
		infos.getChildren().add(ugSachen1.getVlvl());
		infos.getChildren().add(ugSachen2.getVlvl());
		infos.getChildren().add(ugSachen3.getVlvl());
		infos.getChildren().add(ugSachen4.getVlvl());
		infos.getChildren().add(ugSachen5.getVlvl());

		infos.getChildren().add(ugSachen1.getDifficul());
		infos.getChildren().add(ugSachen2.getDifficul());
		infos.getChildren().add(ugSachen3.getDifficul());
		infos.getChildren().add(ugSachen4.getDifficul());
		infos.getChildren().add(ugSachen5.getDifficul());
		allPanes.setVisible(false);
		allPanes.getChildren().addAll(ausGroup,infos,p2,ausXb);

		getMiscBoxHandler().put("table", (gpt, self) -> {
			
			inkreis = true;
			
			gamepanel.getAktionbutton().setInteractionbuttonKann(true, mod -> {

					contractBackground.setTranslateX(WindowManager.getInstance().getGameWidth());
					gamepanel.setBlockUserInputs(true);
					contractBackground.setVisible(true);
					p1.setVisible(true);
					text.setVisible(true);
					hud.setVisible(true);
					ausXb2.setVisible(true);
					titlebanner.setVisible(true);
					button_R.setVisible(true);
					titlebanner.setOpacity(1);
					hud.setOpacity(1);

					FadeTransition ft = new FadeTransition(Duration.millis(500), contractBackground);
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

				
			});
			
			gamepanel.getLgp().getBuildings().parallelStream().filter(b -> b.getTextureFiles().containsValue("CTischCircle.png"))
			.forEach(b -> {
				b.setCurrentKey("open");
			});
		});
	}

	/**
	 * Gets the infos.
	 *
	 * @return the infos
	 */
	public Pane getInfos() { return infos; }

	/**
	 * Gets the ms.
	 *
	 * @return the ms
	 */


	/**
	 * Reload U gtexture.
	 */
	public void reloadUGtexture() {
		ugSachen1.setBackgroundImageToDefaullt();
		ugSachen2.setBackgroundImageToDefaullt();
		ugSachen3.setBackgroundImageToDefaullt();
		ugSachen4.setBackgroundImageToDefaullt();
		ugSachen5.setBackgroundImageToDefaullt();

		ugSachen1.startBvisibleFalse();
		ugSachen2.startBvisibleFalse();
		ugSachen3.startBvisibleFalse();
		ugSachen4.startBvisibleFalse();
		ugSachen5.startBvisibleFalse();

		ugSachen1.setLvlfalse();
		ugSachen2.setLvlfalse();
		ugSachen3.setLvlfalse();
		ugSachen4.setLvlfalse();
		ugSachen5.setLvlfalse();
	}

	/**
	 * Update.
	 *
	 * @param milis the milis
	 */
	@Override
	public void update(long milis) {
		gamepanel.getAktionbutton().setInteractionbuttonKann(false, null);
		
		inkreis = false;
		
		super.update(milis);
		if (!inkreis) {
			gamepanel.getLgp().getBuildings().parallelStream().filter(b -> b.getTextureFiles().containsValue("CTischCircle.png"))
			.forEach(b -> {
				b.setCurrentKey("default");
			});
			contractBackground.setVisible(false);
			gamepanel.getChildren().remove(contractBackground);
			gamepanel.getChildren().remove(p1);
			gamepanel.getChildren().remove(allPanes);
			gamepanel.getChildren().remove(ausXb2);
			gamepanel.getChildren().remove(button_R);
			gamepanel.getChildren().remove(button_L);

		} else {
			contractBackground.setY(gamepanel.getPlayer().getScreenY() - contractBackground.getImage().getHeight() / 2
					+ gamepanel.getPlayer().getHeight() / 2);
			if (!gamepanel.getChildren().contains(contractBackground))
				gamepanel.getChildren().add(contractBackground);

			p1.setLayoutX(gamepanel.getPlayer().getScreenX() - WindowManager.getInstance().getGameWidth() / 2
					+ gamepanel.getPlayer().getWidth() / 2);
			//			p1.setLayoutY(
			//					gamepanel.getPlayer().getScreenY() - p1.getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if (!gamepanel.getChildren().contains(p1))
				gamepanel.getChildren().add(p1);
			
			if (!gamepanel.getChildren().contains(ausXb2))
				gamepanel.getChildren().add(ausXb2);
			///////////////////////////////////////////////
			if (!gamepanel.getChildren().contains(allPanes))
				gamepanel.getChildren().add(allPanes);

			p2.setLayoutX(WindowManager.getInstance().getGameWidth() / 2 + 11 * WindowManager.getInstance().getScalingFactorX());
			p2.setLayoutY(108 * WindowManager.getInstance().getScalingFactorY());

			ugSachen2.setLayoutY(82 * WindowManager.getInstance().getScalingFactorY());
			ugSachen3.setLayoutY(82 * 2 * WindowManager.getInstance().getScalingFactorY());
			ugSachen4.setLayoutY(82 * 3 * WindowManager.getInstance().getScalingFactorY());
			ugSachen5.setLayoutY(82 * 4 * WindowManager.getInstance().getScalingFactorY());

			//////////////////////////////////////////////
			button_R.setLayoutX(WindowManager.getInstance().getGameWidth() - button_R.getImage().getWidth());
			button_R.setLayoutY(gamepanel.getPlayer().getScreenY() - button_R.getImage().getHeight() / 2
					+ gamepanel.getPlayer().getHeight() / 2);
			if (!gamepanel.getChildren().contains(button_R))
				gamepanel.getChildren().add(button_R);

			button_L.setLayoutY(gamepanel.getPlayer().getScreenY() - button_L.getImage().getHeight() / 2
					+ gamepanel.getPlayer().getHeight() / 2);
			if (!gamepanel.getChildren().contains(button_L))
				gamepanel.getChildren().add(button_L);
		}
		if (System.currentTimeMillis() > animationCounterPin + 1000 / 10) {
			animationCounterPin = System.currentTimeMillis();
			animationNumPin++;

			if (animationNumPin >= hudp.length) animationNumPin = 0;

			ImageView iv = switch (index) {
				case 0 -> hud;
				case 1 -> hud2;
				case 2 -> hud3;
				case 3 -> hud4;
				default -> hud;
			};

			iv.setImage(hudp[animationNumPin]);
		}
	}

	public void removeStuffs() {
		contractBackground.setVisible(false);
		p1.setVisible(false);
		text.setVisible(false);
		button_R.setVisible(false);
		button_L.setVisible(false);
		allPanes.setVisible(false);
		ausXb2.setVisible(false);
		gamepanel.setBlockUserInputs(false);
		index = 0;
		p1.setTranslateX(0);
		contractBackground.setTranslateX(WindowManager.getInstance().getGameWidth());
	}

}
