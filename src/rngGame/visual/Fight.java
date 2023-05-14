package rngGame.visual;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.entity.MobRan;
import rngGame.stats.Demon;
import rngGame.tile.ImgUtil;
import rngGame.ui.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Fight.
 */
public class Fight extends Pane{

	/** The battlebackgroundvisual. */
	private final ImageView fight, battlebackgroundvisual;

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The demon mob. */
	private Demon eigenMob;

	/** The demon mob. */
	private final Demon demonMob;

	/** The hh. */
	private HealthBar h, hh;

	/** The stych. */
	private final Button leaf, majyc, stych, tescht;

	/** The mob. */
	private final MobRan mob;

	/**
	 * Instantiates a new fight.
	 *
	 * @param gamepanel the gamepanel
	 * @param mob       the mob
	 */
	public Fight(GamePanel gamepanel, MobRan mob) {
		this.mob				= mob;
		fight					= new ImageView();
		this.gamepanel			= gamepanel;
		demonMob				= MobRan.MobGen(gamepanel);
		battlebackgroundvisual	= new ImageView();
		leaf					= new Button(gamepanel);
		majyc					= new Button(gamepanel);
		stych					= new Button(gamepanel);
		tescht					= new Button(gamepanel);

		leaf.setOnPressed(e -> leaf.init("./res/fight/Leaf2.png"));
		leaf.setOnReleased(e -> {
			leaf.init("./res/fight/Leaf.gif");
			gamepanel.goIntoLoadingScreen();
			new Thread(() -> {
				try {
					gamepanel.getAktionbutton().setVisible(true);
					Platform.runLater(() -> {
						removeMobRan();
					});
					Thread.sleep(2000);
					gamepanel.setBlockUserInputs(false);
					FadeTransition ft = new FadeTransition(Duration.millis(250), gamepanel.getLoadingScreen());
					ft.setFromValue(1);
					ft.setToValue(0);
					ft.play();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}).start();
		});

		stych.setOnPressed(e -> stych.init("./res/fight/Stych2.png"));
		stych.setOnReleased(e -> {
			stych.init("./res/fight/Stych.gif");
			demonMob.changeCurrenthp(-eigenMob.getAtk());

		});
		
		tescht.setOnPressed(e -> tescht.init("./res/fight/Stych2.png"));
		tescht.setOnReleased(e -> tescht.init("./res/npc/rdmDemon.gif"));


		getChildren().addAll(battlebackgroundvisual, fight, leaf, majyc, stych, tescht);
		scaleF11();

	}

	/**
	 * Removes the mob ran.
	 */
	public void removeMobRan() {
		gamepanel.getLgp().getMobRans().remove(mob);
		gamepanel.getViewGroups().get(mob.getLayer()).getChildren().remove(mob);
	}

	/**
	 * Scale F 11.
	 */
	public void scaleF11() {
		fight.setImage(ImgUtil.getScaledImage(gamepanel, "./res/fight/Auswahl.png"));
		battlebackgroundvisual.setImage(ImgUtil.getScaledImage(gamepanel, "./res/fight/Fight.png", gamepanel.getGameWidth(), gamepanel.getGameHeight()));
		demonMob.getDemon().setReqWidth(256);
		demonMob.getDemon().setReqHeight(256);
		demonMob.getDemon().setLayoutX(gamepanel.getWidth()/1.5);
		demonMob.getDemon().setLayoutY(gamepanel.getHeight()/6.4);
		demonMob.getDemon().reloadTextures();
		Demon[] demonArray = gamepanel.getGamemenu().getInventory().getDemons();
		eigenMob = demonArray[0];
		eigenMob.getDemon().setReqWidth(256);
		eigenMob.getDemon().setReqHeight(256);
		eigenMob.getDemon().setLayoutX(gamepanel.getWidth()/13);
		eigenMob.getDemon().setLayoutY(gamepanel.getHeight()/6.4);
		eigenMob.getDemon().flipTextures();
		eigenMob.getDemon().reloadTextures();
		h = new HealthBar(gamepanel, demonMob);
		hh = new HealthBar(gamepanel, eigenMob);
		h.setLayoutX(gamepanel.getWidth()/2);
		hh.setLayoutX(gamepanel.getWidth()/4);
		getChildren().addAll(demonMob.getDemon(), eigenMob.getDemon(), h, hh);

		leaf.init("./res/fight/Leaf.gif", 10);
		majyc.init("./res/fight/Majyc.gif", 10);
		stych.init("./res/fight/Stych.gif", 10);
		tescht.init("./res/npc/rdmDemon.gif", 20);
	}

	/**
	 * Update.
	 */
	public void update() {
		h.update();
		hh.update();
	}

}
