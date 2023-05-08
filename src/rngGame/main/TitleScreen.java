package rngGame.main;

import java.io.FileNotFoundException;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.tile.ImgUtil;
import rngGame.ui.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class TitleScreen.
 */
public class TitleScreen extends Pane{

	/** The gp. */
	private GamePanel gp;

	/** The iv. */
	private final ImageView iv;

	/** The last. */
	private long last = 0l;

	/** The frames. */
	private Image[] frames;

	/** The curr frame. */
	private int currFrame = 0;

	/** The pfail. */
	private Button ploy, settins, clous, pfail;

	/**
	 * Instantiates a new title screen.
	 */
	public TitleScreen() {
		try {
			gp = new GamePanel();
			Input.getInstance().setGamePanel(gp.getVgp()); // pass instance of GamePanel to the Instance of Input
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		iv = new ImageView();

		Image	clous1	= ImgUtil.getScaledImage(gp, "./res/backgrounds/Clous.png");
		Image	clous2	= ImgUtil.getScaledImage(gp, "./res/backgrounds/Clous2.png");
		clous = new Button(clous1, gp);
		clous.setOnPressed(e -> clous.setImage(clous2));
		clous.setOnReleased(e -> {
			clous.setImage(clous1);
			System.exit(0);
		});

		ploy = new Button(ImgUtil.getScaledImage(gp, "./res/backgrounds/Ploy.png"), gp);
		Image	ploy1	= ImgUtil.getScaledImage(gp, "./res/backgrounds/Ploy.png");
		Image	ploy2	= ImgUtil.getScaledImage(gp, "./res/backgrounds/Ploy2.png");
		ploy.setOnMousePressed(e -> ploy.setImage(ploy2));
		ploy.setOnMouseReleased(e -> {
			gp.getVgp().goIntoLoadingScreen();
			new Thread(() -> {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				Platform.runLater(() -> {
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

		Image	settins1	= ImgUtil.getScaledImage(gp, "./res/backgrounds/Settins.png");
		Image	settins2	= ImgUtil.getScaledImage(gp, "./res/backgrounds/Settins2.png");
		Image	pfail1		= ImgUtil.getScaledImage(gp, "./res/backgrounds/Pfail.png");
		Image	pfail2		= ImgUtil.getScaledImage(gp, "./res/backgrounds/Pfail2.png");
		settins	= new Button(settins1, gp);
		pfail	= new Button(pfail1, gp);
		pfail.setVisible(false);
		settins.setOnPressed(e -> {
			settins.setImage(settins2);
		});
		settins.setOnReleased(e -> {
			settins.setImage(settins1);
			ploy.setVisible(false);
			clous.setVisible(false);
			settins.setVisible(false);
			pfail.setVisible(true);
			pfail.setOnPressed(_e -> {
				pfail.setImage(pfail2);
			});
			pfail.setOnReleased(__e -> {
				pfail.setImage(pfail1);
				pfail.setVisible(false);
				ploy.setVisible(true);
				clous.setVisible(true);
				settins.setVisible(true);
				settins.setImage(settins1);
			});

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
					if (t - last > 1000 / 30) {
						Platform.runLater(() -> {
							iv.setImage(frames[currFrame]);
						});
						currFrame++;
						if (currFrame >= frames.length) currFrame = 0;
						last = t;
					}
				}
			}
		}).start();
	}

	/**
	 * Scale F 11.
	 */
	public void scaleF11() {
		frames = ImgUtil.getScaledImages(gp.getVgp(), "./res/backgrounds/Main BG.gif");
		iv.setImage(frames[0]);
		gp.getVgp().setBlockUserInputs(true);

		gp.getVgp().startLogicThread();
	}

}