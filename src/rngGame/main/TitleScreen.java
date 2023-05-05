package rngGame.main;

import java.io.FileNotFoundException;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.tile.ImgUtil;
import rngGame.ui.Button;

public class TitleScreen extends Pane{
	private GamePanel gp;
	private ImageView iv;
	private long last = 0l;
	private Image[] frames;
	private int currFrame = 0;
	private Button ploy, settins, clous, pfail;

	public TitleScreen() {
		try {
			gp = new GamePanel();
			Input.getInstance().setGamePanel(gp.getVgp()); // pass instance of GamePanel to the Instance of Input
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		iv = new ImageView();
		ploy = new Button(ImgUtil.getScaledImage(gp, "./res/backgrounds/Ploy.png"));
		Image ploy1 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Ploy.png");
		Image ploy2 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Ploy2.png");
		ploy.setOnMousePressed(e -> ploy.setImage(ploy2));
		ploy.setOnMouseReleased(e->{
			gp.getVgp().goIntoLoadingScreen();
			new Thread(()->{
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				Platform.runLater(()->{
					ploy.setImage(ploy1);
					iv.setVisible(false);
					ploy.setVisible(false);
					settins.setVisible(false);
					clous.setVisible(false);
					gp.getVgp().setBlockUserInputs(false);
				});
				
				try {
					Thread.sleep(2000);
					FadeTransition ft = new FadeTransition(Duration.millis(250), gp.getVgp().getLoadingScreen());
			        ft.setFromValue(1);
			        ft.setToValue(0);
			        ft.play();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}).start();
			
		});
		
		Image settins1 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Settins.png");
		Image settins2 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Settins2.png");
		Image pfail1 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Pfail.png");
		Image pfail2 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Pfail2.png");
		settins = new Button(settins1);
		pfail = new Button(pfail1);
		pfail.setVisible(false);
		settins.setOnMousePressed(e->{
			settins.setImage(settins2);
		});
		settins.setOnMouseReleased(e->{
			settins.setImage(settins1);
			ploy.setVisible(false);
			clous.setVisible(false);
			settins.setVisible(false);
			pfail.setVisible(true);
				pfail.setOnMousePressed(_e->{
					pfail.setImage(pfail2);
				});
				pfail.setOnMouseReleased(__e->{
					pfail.setImage(pfail1);
					pfail.setVisible(false);
					ploy.setVisible(true);
					clous.setVisible(true);
					settins.setVisible(true);
					settins.setImage(settins1);
				});
				
				
				
		});
		
		Image clous1 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Clous.png");
		Image clous2 = ImgUtil.getScaledImage(gp, "./res/backgrounds/Clous2.png");
		clous = new Button(clous1);
		clous.setOnMousePressed(e -> clous.setImage(clous2));
		clous.setOnMouseReleased(e->{
			clous.setImage(clous1);
			System.exit(0);
		});
		
		
		
		
		getChildren().addAll(gp.getVgp(), iv, ploy, settins, clous, pfail);
		new Thread(()->{
			while (true) {
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (frames != null) {
					long t = System.currentTimeMillis();
					if ((t - last) > 1000 / 30) {
						Platform.runLater(() -> {
							iv.setImage(frames[currFrame]);
						});
						currFrame++;
						if (currFrame >= frames.length) {
							currFrame = 0;
						}
						last = t;
					}
				}
			}
		}).start();
	}
	
	public void scaleF11() {
		frames = ImgUtil.getScaledImages(gp.getVgp(), "./res/backgrounds/Main BG.gif");
		iv.setImage(frames[0]);
		gp.getVgp().setBlockUserInputs(true);
		
		gp.getVgp().startLogicThread();
	}
	
}
