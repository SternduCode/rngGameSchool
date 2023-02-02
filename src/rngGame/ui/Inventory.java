package rngGame.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.entity.Player;
import rngGame.main.GamePanel;
import rngGame.main.Input;
import rngGame.tile.ImgUtil;

public class Inventory extends Pane {
	private final ImageView gamemenu;
	private final GamePanel gamepanel;
	
	public Inventory(GamePanel gamepanel) throws FileNotFoundException {
		gamemenu = new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/gamemenubackround.png"));
		getChildren().add(gamemenu);

		gamemenu.setVisible(false);
		gamemenu.setDisable(true);
		this.gamepanel = gamepanel; 
		
		AtomicBoolean ab = new AtomicBoolean(false);
		Input.getInstance().setKeyHandler("inv", mod -> {
			
			TranslateTransition ft = new TranslateTransition(Duration.millis(150), gamemenu);
			if (!ab.getAndSet(!ab.get())) {	
				gamepanel.setBlockUserInputs(true);
				gamemenu.setVisible(true);
				ft.setFromY(gamepanel.SpielHoehe);
				ft.setToY(0);
				ft.play();
			} else {
				ft.setFromY(0);
				ft.setToY(gamepanel.SpielHoehe);
				ft.play();
				gamepanel.setBlockUserInputs(false);
				new Thread(()->{
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gamemenu.setVisible(false);
				}).start();
			}
		}, KeyCode.TAB, false);
	}

	public void f11Scale() {
		gamemenu.setImage(ImgUtil.getScaledImage(gamepanel, "./res/gui/gamemenubackround.png"));
	}
}
