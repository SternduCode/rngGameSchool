package rngGame.visual;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.entity.MobRan;
import rngGame.stats.Demon;
import rngGame.stats.Element;
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
			majyc.setDisable(true);
			leaf.setDisable(true);
			stych.setDisable(true);
		});
		
		majyc.setOnPressed(e-> majyc.init("./res/fight/Majyc2.png"));
		majyc.setOnReleased(e->{
			majyc.init("./res/fight/Majyc.png");
			majyc.setDisable(true);
			leaf.setDisable(true);
			stych.setDisable(true);
			sheeesh();
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
	
	
	
	public void sheeesh() {
		switch (eigenMob.getElement()) {
		case Fire -> {
			 if(demonMob.getElement() == Element.Plant) demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
			 else if(demonMob.getElement() == Element.Water || demonMob.getElement() == Element.Void || demonMob.getElement() == Element.DimensionMaster) 
				 demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*0.5));
			 else demonMob.changeCurrenthp(-eigenMob.getAtk());
		}
		case Water -> {
			 if(demonMob.getElement() == Element.Fire) demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
			 else if(demonMob.getElement() == Element.Plant || demonMob.getElement() == Element.Void || demonMob.getElement() == Element.DimensionMaster) 
				 demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*0.5));
			 else demonMob.changeCurrenthp(-eigenMob.getAtk());
		}
		case Plant -> {
			 if(demonMob.getElement() == Element.Water) demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
			 else if(demonMob.getElement() == Element.Fire || demonMob.getElement() == Element.Void || demonMob.getElement() == Element.DimensionMaster) 
				 demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*0.5));
			 else demonMob.changeCurrenthp(-eigenMob.getAtk());
		}
		case Light -> {
			 if(demonMob.getElement() == Element.Fire || demonMob.getElement() == Element.Plant || demonMob.getElement() == Element.Water)
				 demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
			 else if(demonMob.getElement() == Element.Void || demonMob.getElement() == Element.DimensionMaster) 
				 demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*0.5));
			 else if(demonMob.getElement() == Element.Shadow) demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*1.5));
			 else demonMob.changeCurrenthp(-eigenMob.getAtk());
		}
		case Shadow -> {
			 if(demonMob.getElement() == Element.Fire || demonMob.getElement() == Element.Plant || demonMob.getElement() == Element.Water)
				 demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
			 else if(demonMob.getElement() == Element.Void || demonMob.getElement() == Element.DimensionMaster) 
				 demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*0.5));
			 else if(demonMob.getElement() == Element.Light) demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*1.5));
			 else demonMob.changeCurrenthp(-eigenMob.getAtk());
		}
		case Void -> {
			 if(demonMob.getElement() == Element.DimensionMaster) demonMob.changeCurrenthp((int) (-eigenMob.getAtk()*0.5));
			 else if (demonMob.getElement() == Element.Void) demonMob.changeCurrenthp(-eigenMob.getAtk());
			 else demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
		}
		case DimensionMaster -> {
			 if (demonMob.getElement() == Element.DimensionMaster) demonMob.changeCurrenthp(-eigenMob.getAtk());
			 else if(demonMob.getElement() == Element.Fire || demonMob.getElement() == Element.Water || demonMob.getElement() == Element.Plant)
				 demonMob.changeCurrenthp(-eigenMob.getAtk()*3);
			 else demonMob.changeCurrenthp(-eigenMob.getAtk()*2);
		}
		
		
			
			
	}
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