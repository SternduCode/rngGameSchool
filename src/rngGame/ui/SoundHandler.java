package rngGame.ui;

import java.io.File;

import javafx.scene.media.*;
import javafx.util.Duration;


public class SoundHandler {

	private static SoundHandler INSTANCE;

	private MediaPlayer backgroundMusicPlayer = null;

	private double backgroundMusicVolume = .2, soundVolume = .2;

	private SoundHandler() {}

	public static SoundHandler getInstance() { return INSTANCE != null ? INSTANCE : (INSTANCE = new SoundHandler()); }

	public void endBackgroundMusic() {
		if (backgroundMusicPlayer != null) backgroundMusicPlayer.stop();
		backgroundMusicPlayer = null;
	}

	public double getBackgroundMusicVolume() { return backgroundMusicVolume; }

	public double getSoundVolume() { return soundVolume; }

	public void makeSound(String soundname) {
		MediaPlayer mp = new MediaPlayer(new Media(new File("./res/music/" + soundname).toURI().toString()));
		mp.setAutoPlay(true);
		mp.setVolume(soundVolume);

	}

	public void pauseBackgroundMusic() {
		if (backgroundMusicPlayer != null) backgroundMusicPlayer.pause();
	}

	public void resumeBackgroundMusic() {
		if (backgroundMusicPlayer != null) backgroundMusicPlayer.play();
	}

	public void setBackgroundMusic(String pathToMusic) {
		backgroundMusicPlayer = new MediaPlayer(new Media(new File("./res/" + pathToMusic).toURI().toString()));
		backgroundMusicPlayer.setAutoPlay(true);
		backgroundMusicPlayer.setVolume(backgroundMusicVolume);
		// backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}

	public void setBackgroundMusicVolume(double volume) { backgroundMusicVolume = volume; }

	public void setSoundVolume(double volume) {
		soundVolume = volume;
	}

	public void update() {
		if (backgroundMusicPlayer != null) {
			Duration	duration	= backgroundMusicPlayer.getMedia().getDuration();
			Duration	curr		= backgroundMusicPlayer.getCurrentTime();
			// System.out.println("meow " + duration + " " + curr);
			if (duration.subtract(curr).lessThan(Duration.millis(34.2))) backgroundMusicPlayer.seek(Duration.millis(2.5));
		}
	}

}
