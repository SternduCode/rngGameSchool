package rngGame.main;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import rngGame.visual.AnimatedImage;

public class LoadingScreen extends AnimatedImage {

	private static final LoadingScreen INSTANCE = new LoadingScreen();

	private LoadingScreen() {
		init("./res/gui/Loadingscreen.gif");
		setDisable(true);
		setOpacity(0.0);
	}

	public static LoadingScreen getInstance() {
		return INSTANCE;
	}

		public boolean isInLoadingScreen() {
			return getOpacity() > 0.5;
		}

		public void goIntoLoadingScreen() {
			if (!isInLoadingScreen()) {
				setFitWidth(getImage().getWidth() * WindowManager.getInstance().getScalingFactorX());
				setFitHeight(getImage().getHeight() * WindowManager.getInstance().getScalingFactorY());
				final FadeTransition ft = new FadeTransition(Duration.millis(250.0), this);
				ft.setFromValue(0.0);
				ft.setToValue(1.0);
				ft.play();
			}
		}

		public void goOutOfLoadingScreen() {
			if (isInLoadingScreen()) {
				setFitWidth(getImage().getWidth() * WindowManager.getInstance().getScalingFactorX());
				setFitHeight(getImage().getHeight() * WindowManager.getInstance().getScalingFactorY());
				final FadeTransition ft = new FadeTransition(Duration.millis(250.0), this);
				ft.setFromValue(1.0);
				ft.setToValue(0.0);
				ft.play();
			}
		}

}
