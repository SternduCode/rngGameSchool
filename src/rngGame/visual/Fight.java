package rngGame.visual;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.entity.MobRan;
import rngGame.main.*;
import rngGame.stats.Demon;
import rngGame.stats.Element;
import rngGame.tile.ImgUtil;
import rngGame.ui.*;
import rngGame.ui.SoundHandler;

/**
 * The Class Fight.
 */
public class Fight extends Pane{

	/** The battlebackgroundvisual. */
	private final ImageView fight, battlebackgroundvisual, eName, dName, eIcon, dIcon;
	
	
	private AnimatedImage hit;

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The demon mob. */
	private Demon eigenMob;

	/** The demon mob. */
	private final Demon demonMob;

	/** The hh. */
	private HealthBar h, hh;

	/** The stych. */
	private final Button leaf, majyc, stych;

	/** The mob. */
	private final MobRan mob;
	
	private final Group buttongroup = new Group();
	
	private boolean f = false;

	private boolean overlayVisible;
	
	
	
	

	/**
	 * Instantiates a new fight.
	 *
	 * @param gamepanel the gamepanel
	 * @param mob       the mob
	 */
	public Fight(GamePanel gamepanel, MobRan mob) {
		this.mob				= mob;
		fight					= new ImageView();
		eName 					= new ImageView();
		dName 					= new ImageView();
		eIcon					= new ImageView();
		dIcon					= new ImageView();
		hit 					= new AnimatedImage();
		hit.setLayoutY(-10);
		hit.setVisible(false);
		this.gamepanel			= gamepanel;
		demonMob				= MobRan.MobGen(gamepanel);
		battlebackgroundvisual	= new ImageView();
		leaf					= new Button();
		majyc					= new Button();
		stych					= new Button();
		
		overlayVisible = gamepanel.getOverlay().isVisible();
		gamepanel.getOverlay().setVisible(false);
		
		buttongroup.getChildren().addAll(leaf,majyc,stych);
		Random r = new Random();
		
		TranslateTransition ft = new TranslateTransition(Duration.millis(150), fight);
		TranslateTransition ib1 = new TranslateTransition(Duration.millis(150),buttongroup);
		
		leaf.setOnPressed(e -> leaf.init("./res/fight/Leaf2.png"));
		leaf.setOnReleased(e -> {
			leaf.init("./res/fight/Leaf.gif");
			leaf.setDisable(true);
			if(eigenMob.getCurrenthp() == 1) {
				eigenMob.changeCurrenthp(-1);
			} else {
				eigenMob.changeCurrenthp(-eigenMob.getCurrenthp()+1);
			}
			System.out.println("uno HP left");
			LoadingScreen.getInstance().goIntoLoadingScreen();
			SoundHandler.getInstance().endBackgroundMusic();
			if (!"".equals(gamepanel.getTileManager().getBackgroundMusic()))
				SoundHandler.getInstance().setBackgroundMusic(gamepanel.getTileManager().getBackgroundMusic());
			new Thread(() -> {
				try {
					Thread.sleep(2000);
					Platform.runLater(() -> {
						removeMobRan();
					});
					gamepanel.getAktionbutton().setVisible(true);
					gamepanel.getOverlay().setVisible(overlayVisible);
					eigenMob.getDemon().flipTextures();
					eigenMob.getDemon().reloadTextures();
					gamepanel.setBlockUserInputs(false);
					LoadingScreen.getInstance().goOutOfLoadingScreen();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}).start();
		});

		stych.setOnPressed(e -> stych.init("./res/fight/Stych2.png"));
		stych.setOnReleased(e -> {
			hit.init("./res/fight/stichA.gif");
			hit.setVisible(true);
			gamepanel.getLgp().makeSound("Enemy Hit.wav");
			stych.init("./res/fight/Stych.gif");
			System.out.println(demonMob.getCurrenthp());
			demonMob.changeCurrenthp(-eigenMob.getAtk());
			System.out.println(demonMob.getCurrenthp());
			majyc.setDisable(true);
			leaf.setDisable(true);
			stych.setDisable(true);
			ft.setToY(WindowManager.getInstance().getGameHeight() / 2);
			ib1.setToY(WindowManager.getInstance().getGameHeight() / 2);
			ft.play(); 
			ib1.play();
			int rr = r.nextInt(2)+1;
			new Thread(() -> {
				try {
					Thread.sleep(5000);
					hit.setVisible(false);
					if(demonMob.getCurrenthp()!=0) {
					if(rr==1) {
						gamepanel.getLgp().makeSound("Enemy Hit.wav");
						eigenMob.changeCurrenthp(-demonMob.getAtk());
						}else sheeesh(demonMob, eigenMob);	
					}
					majyc.setDisable(false);
					leaf.setDisable(false);
					stych.setDisable(false);
					ft.setToY(0); 
					ib1.setToY(0);
					ft.play();
					ib1.play();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}).start();
		});
		
		majyc.setOnPressed(e-> majyc.init("./res/fight/Majyc2.png"));
		majyc.setOnReleased(e->{
			hit.init("./res/fight/MagicA.gif");
			hit.setVisible(true);
			gamepanel.getLgp().makeSound("Enemy Hit2.wav");
			majyc.init("./res/fight/Majyc.gif");
			majyc.setDisable(true);
			leaf.setDisable(true);
			stych.setDisable(true);
			ft.setToY(WindowManager.getInstance().getGameHeight() / 2);
			ib1.setToY(WindowManager.getInstance().getGameHeight() / 2);
			ft.play();
			ib1.play();
			int rr = r.nextInt(2)+1;
			new Thread(() -> {
				try {
					Thread.sleep(5000);
					hit.setVisible(false);
					if(demonMob.getCurrenthp()!=0) {
					if(rr==1) {
						gamepanel.getLgp().makeSound("Enemy Hit.wav");
						eigenMob.changeCurrenthp(-demonMob.getAtk());
						}else sheeesh(demonMob, eigenMob);
					
					}
					majyc.setDisable(false);
					leaf.setDisable(false);
					stych.setDisable(false);
					ft.setToY(0);
					ib1.setToY(0);
					ft.play();
					ib1.play();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}).start();
			sheeesh(eigenMob, demonMob);
		});


		scaleF11();

	}

	/**
	 * Removes the mob ran.
	 */
	public void removeMobRan() {
		gamepanel.getLgp().getMobRans().remove(mob);
		gamepanel.getViewGroups().get(mob.getLayer()).getChildren().remove(mob);
	}
	
	
	
	public void sheeesh(Demon eigenMob, Demon demonMob) {
		gamepanel.getLgp().makeSound("Enemy Hit2.wav");
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
	public void demonDead(){
		gamepanel.getLgp().makeSound("Enemy Ded.wav");
		SoundHandler.getInstance().endBackgroundMusic();
		if (!"".equals(gamepanel.getTileManager().getBackgroundMusic()))
			SoundHandler.getInstance().setBackgroundMusic(gamepanel.getTileManager().getBackgroundMusic());
		Random r = new Random();
		int i = r.nextInt(3)+1;
		eigenMob.setCurrentExp(eigenMob.getCurrentExp()+i);
		gamepanel.getGamemenu().getInventory().addDemon2current(demonMob);
		new Thread(() -> {
			try {
				Thread.sleep(2000);
				Platform.runLater(() -> {
					removeMobRan();
				});
				gamepanel.getAktionbutton().setVisible(true);
				gamepanel.getOverlay().setVisible(overlayVisible);
				eigenMob.getDemon().flipTextures();
				eigenMob.getDemon().reloadTextures();
				gamepanel.setBlockUserInputs(false);
				LoadingScreen.getInstance().goOutOfLoadingScreen();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}).start();
	}

	/**
	 * Scale F 11.
	 */
	public void scaleF11() {
		fight.setImage(ImgUtil.getScaledImage("./res/fight/Auswahl.png"));
		battlebackgroundvisual.setImage(ImgUtil.getScaledImage("./res/fight/Fight.png", WindowManager.getInstance().getGameWidth(), WindowManager.getInstance().getGameHeight()));
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
		getChildren().clear();
		getChildren().addAll(battlebackgroundvisual, fight, buttongroup);
		getChildren().addAll(demonMob.getDemon(), eigenMob.getDemon(), h, hh, eName, dName,	eIcon, dIcon ,hit);
		resetName();
		

		leaf.init("./res/fight/Leaf.gif", 10);
		majyc.init("./res/fight/Majyc.gif", 10);
		stych.init("./res/fight/Stych.gif", 10);
	}

	
	
	/**
	 * Update.
	 */
	public void update() {
		Demon[] demonArray = gamepanel.getGamemenu().getInventory().getDemons();
		h.update();
		hh.update();
		if(demonMob.getCurrenthp() <= 0 && !f) {demonDead(); f = true;}
		if(eigenMob.getCurrenthp() <= 0) {
				eigenMob.getDemon().flipTextures();
				eigenMob.getDemon().reloadTextures();
				for(int i=0; i<demonArray.length;i++) {
					if(demonArray[i] != null &&demonArray[i].getCurrenthp() != 0) {
						eigenMob = demonArray[i];
						eigenMob.getDemon().setReqWidth(256);
						eigenMob.getDemon().setReqHeight(256);
						eigenMob.getDemon().setLayoutX(gamepanel.getWidth()/13);
						eigenMob.getDemon().setLayoutY(gamepanel.getHeight()/6.4);
						eigenMob.getDemon().flipTextures();
						eigenMob.getDemon().reloadTextures();
						resetName();
						if (!gamepanel.getLgp().getNpcs().contains(eigenMob.getDemon()))
							gamepanel.getLgp().getNpcs().add(eigenMob.getDemon());
						getChildren().set(4, eigenMob.getDemon());
						hh.setDemon(eigenMob);
						break;
					}
				}
				if (eigenMob.getCurrenthp() ==0 && !f) {
					f = true;
					new Thread(() -> {
						try {
							gamepanel.getLgp().makeSound("uDeath.wav");
							SoundHandler.getInstance().endBackgroundMusic();
							if (!"".equals(gamepanel.getTileManager().getBackgroundMusic()))
								SoundHandler.getInstance().setBackgroundMusic(gamepanel.getTileManager().getBackgroundMusic());
							Thread.sleep(2000);
							Platform.runLater(() -> {
								removeMobRan();
							});
							gamepanel.getAktionbutton().setVisible(true);
							gamepanel.getOverlay().setVisible(overlayVisible);
							eigenMob.getDemon().flipTextures();
							eigenMob.getDemon().reloadTextures();
							gamepanel.setBlockUserInputs(false);
							LoadingScreen.getInstance().goOutOfLoadingScreen();
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
					}).start();
				}
			}
	}
public void resetName() {
	Image eigenmobname = Text.getInstance().convertText(""+eigenMob.getMobName(), 48);
	eigenmobname = ImgUtil.resizeImage(
			eigenmobname, (int) eigenmobname.getWidth(), (int) eigenmobname.getHeight(),
			(int) (eigenmobname.getWidth() * WindowManager.getInstance().getScalingFactorX()),
			(int) (eigenmobname.getHeight() * WindowManager.getInstance().getScalingFactorY()));
	eName.setImage(eigenmobname);
	eName.setLayoutX(gamepanel.getWidth()/4+44);
	eName.setLayoutY(20);
	eIcon.setImage(gamepanel.getGamemenu().getInventory().showElementIcon(eigenMob.getElement()));
	
	Image gegenmobname = Text.getInstance().convertText(""+demonMob.getMobName(), 48);
	gegenmobname = ImgUtil.resizeImage(
			gegenmobname, (int) gegenmobname.getWidth(), (int) gegenmobname.getHeight(),
			(int) (gegenmobname.getWidth() * WindowManager.getInstance().getScalingFactorX()),
			(int) (gegenmobname.getHeight() * WindowManager.getInstance().getScalingFactorY()));
	dName.setImage(gegenmobname);
	dName.setLayoutX(gamepanel.getWidth()/2+210-gegenmobname.getWidth());
	dName.setLayoutY(20);
	dIcon.setImage(gamepanel.getGamemenu().getInventory().showElementIcon(demonMob.getElement()));
	dIcon.setLayoutX(gamepanel.getWidth()-96);
}
}
