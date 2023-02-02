package rngGame.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.entity.Player;
import rngGame.main.GamePanel;
import rngGame.main.Input;
import rngGame.tile.ImgUtil;

public class TabMenu extends Pane {
	private final ImageView gamemenu;
	
	private final Group buttongroup = new Group();
	
	private final ImageView invB;
	private Image invB1;
	private Image invB2;
	
	private final ImageView queB;
	private Image queB1;
	private Image queB2;
	
	private final ImageView leavB;
	private Image leavB1;
	private Image leavB2;
	
	
	private final GamePanel gamepanel;
	
	public TabMenu(GamePanel gamepanel) throws FileNotFoundException {
		gamemenu = new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/gamemenubackround.png"));
		
		invB1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/invAbutton1.png");
		invB2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/invAbutton2.png");
		invB = new ImageView(invB1);
		
		queB1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/queAbutton1.png");
		queB2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/queAbutton2.png");
		queB = new ImageView(queB1);
		
		leavB1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/LeavAbutton1.png");
		leavB2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/LeavAbutton2.png");
		leavB = new ImageView(leavB1);
		
		buttongroup.getChildren().addAll(invB,queB,leavB);
		
		getChildren().add(gamemenu);
		getChildren().add(buttongroup);
		
		gamemenu.setVisible(false);
		buttongroup.setVisible(false);
		
		this.setDisable(true);
		this.gamepanel = gamepanel; 
		
		AtomicBoolean ab = new AtomicBoolean(false);
		Input.getInstance().setKeyHandler("inv", mod -> {
			
			TranslateTransition ft = new TranslateTransition(Duration.millis(150), gamemenu);
			TranslateTransition ib1 = new TranslateTransition(Duration.millis(150), buttongroup);
			
			if (!ab.getAndSet(!ab.get())) {	
				gamepanel.setBlockUserInputs(true);
				this.setDisable(false);
				gamemenu.setVisible(true);
				buttongroup.setVisible(true);

				invB.setOnMousePressed(me -> {
					invB.setImage(invB2);
				});
				invB.setOnMouseReleased(me -> {
					invB.setImage(invB1);
				});
				
				
				queB.setOnMousePressed(me -> {
					queB.setImage(queB2);
				});
				queB.setOnMouseReleased(me -> {
					queB.setImage(queB1);
				});
				
				
				leavB.setOnMousePressed(me -> {
					leavB.setImage(leavB2);
				});
				leavB.setOnMouseReleased(me -> {
					leavB.setImage(leavB1);
				});
				
				ft.setFromY(gamepanel.SpielHoehe);
				ib1.setFromY(gamepanel.SpielHoehe);
				ft.setToY(0);
				ib1.setToY(0);
				ft.play();
				ib1.play();


			} else {
				
				ft.setToY(gamepanel.SpielHoehe);
				ib1.setToY(gamepanel.SpielHoehe);
				ft.play();
				ib1.play();
				
				gamepanel.setBlockUserInputs(false);
				
				new Thread(()->{
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gamemenu.setVisible(false);
				buttongroup.setVisible(false);
				this.setDisable(true);
				
				}).start();
			}
		}, KeyCode.TAB, false);
	}

	public void f11Scale() {
		gamemenu.setImage(ImgUtil.getScaledImage(gamepanel, "./res/gui/gamemenubackround.png"));
		
		invB1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/invAbutton1.png");
		invB2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/invAbutton2.png");
		invB.setImage(invB1);
		
		queB1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/queAbutton1.png");
		queB2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/queAbutton2.png");
		queB.setImage(queB1);
		
		leavB1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/LeavAbutton1.png");
		leavB2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/LeavAbutton2.png");
		leavB.setImage(leavB1);
	}
}
