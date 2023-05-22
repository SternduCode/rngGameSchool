package rngGame.main;

import java.io.FileNotFoundException;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.tile.ImgUtil;
import rngGame.ui.Button;
import rngGame.visual.AnimatedImage;

// TODO: Auto-generated Javadoc
/**
 * The Class TitleScreen.
 */
public class TitleScreen extends Pane{

	/** The gp. */
	private GamePanel gp;

	/** The iv. */
	private final ImageView iv;
	
	private AnimatedImage storyView;

	/** The last. */
	private long last = 0l;

	/** The frames. */
	private Image[] frames;

	/** The curr frame. */
	private int currFrame = 0;
	
	private int index = 0;

	/** The pfail. */
	private Button ploy = null, settins = null, clous = null, pfail = null;

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

		storyView = new AnimatedImage("./res/story/Story0.gif", gp.getVgp(), 7);
		
		storyView.setOnMouseReleased(me -> {
			if(index < 6) {
			index++;
			storyView.init("./res/story/Story"+ index +".gif");
			} else {
				storyView.setVisible(false);
			}
		});
		
		clous = new Button("./res/backgrounds/Clous.png", gp.getVgp());
		clous.setOnPressed(e -> clous.init("./res/backgrounds/Clous2.png"));
		clous.setOnReleased(e -> {
			clous.init("./res/backgrounds/Clous.png");
			System.exit(0);
		});

		ploy = new Button("./res/backgrounds/Ploy.png", gp.getVgp());
		ploy.setOnMousePressed(e -> ploy.init("./res/backgrounds/Ploy2.png"));
		ploy.setOnMouseReleased(e -> {
			gp.getVgp().goIntoLoadingScreen();
			new Thread(() -> {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				Platform.runLater(() -> {
					ploy.init("./res/backgrounds/Ploy.png");
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

		settins	= new Button("./res/backgrounds/Settins.png", gp.getVgp());
		pfail	= new Button("./res/backgrounds/Pfail.png", gp.getVgp());
		pfail.setVisible(false);
		settins.setOnPressed(e -> {
			settins.init("./res/backgrounds/Settins2.png");
		});
		settins.setOnReleased(e -> {
			settins.init("./res/backgrounds/Settins.png");
			ploy.setVisible(false);
			clous.setVisible(false);
			settins.setVisible(false);
			pfail.setVisible(true);
			pfail.setOnPressed(_e -> {
				pfail.init("./res/backgrounds/Pfail2.png");
			});
			pfail.setOnReleased(__e -> {
				pfail.init("./res/backgrounds/Pfail.png");
				pfail.setVisible(false);
				ploy.setVisible(true);
				clous.setVisible(true);
				settins.setVisible(true);
				settins.init("./res/backgrounds/Settins.png");
			});

		});

		getChildren().addAll(gp.getVgp(), iv, ploy, settins, clous, pfail,storyView);
		new Thread(()->{
			while (true) {
				try {
					storyView.update();
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
