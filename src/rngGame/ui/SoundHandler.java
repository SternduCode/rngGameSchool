package rngGame.ui;

import java.io.File;

import com.sterndu.multicore.Updater;

import javafx.scene.media.*;
import javafx.util.Duration;


// TODO: Auto-generated Javadoc
/**
 * The Class SoundHandler.
 */
public class SoundHandler {

	/** The instance. */
	private static SoundHandler INSTANCE;

	/** The background music player. */
	private MediaPlayer backgroundMusicPlayer = null;

	/** The sound volume and the background music volume. */
	private double backgroundMusicVolume = .2, soundVolume = .2;

	/**
	 * Instantiates a new sound handler.
	 */
	private SoundHandler() {}

	/**
	 * Gets the single instance of SoundHandler.
	 *
	 * @return single instance of SoundHandler
	 */
	public static SoundHandler getInstance() { return INSTANCE != null ? INSTANCE : (INSTANCE = new SoundHandler()); }

	/**
	 * End background music.
	 */
	public void endBackgroundMusic() {
		if (backgroundMusicPlayer != null) backgroundMusicPlayer.stop();
		backgroundMusicPlayer = null;
	}

	/**
	 * Gets the background music volume.
	 *
	 * @return the background music volume
	 */
	public double getBackgroundMusicVolume() { return backgroundMusicVolume; }

	/**
	 * Gets the sound volume.
	 *
	 * @return the sound volume
	 */
	public double getSoundVolume() { return soundVolume; }

	/**
	 * Make sound.
	 *
	 * @param soundname the soundname
	 */
	public void makeSound(String soundname) {
		MediaPlayer mp = new MediaPlayer(new Media(new File("./res/music/" + soundname).toURI().toString()));
		mp.setAutoPlay(true);
		mp.setVolume(soundVolume);

	}

	/**
	 * Pause background music.
	 */
	public void pauseBackgroundMusic() {
		if (backgroundMusicPlayer != null) backgroundMusicPlayer.pause();
	}

	/**
	 * Resume background music.
	 */
	public void resumeBackgroundMusic() {
		if (backgroundMusicPlayer != null) backgroundMusicPlayer.play();
	}

	/**
	 * Sets the background music.
	 *
	 * @param pathToMusic the new background music
	 */
	public void setBackgroundMusic(String pathToMusic) {
		backgroundMusicPlayer = new MediaPlayer(new Media(new File("./res/" + pathToMusic).toURI().toString()));
		backgroundMusicPlayer.setAutoPlay(true);
		backgroundMusicPlayer.setVolume(backgroundMusicVolume);
		// backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		Updater.getInstance().add((Runnable) () -> {
			if (backgroundMusicPlayer != null) {
				Duration	duration	= backgroundMusicPlayer.getMedia().getDuration();
				Duration	curr		= backgroundMusicPlayer.getCurrentTime();
				// System.out.println("meow " + duration + " " + curr);
				if (duration.subtract(curr).lessThan(Duration.millis(34.2))) backgroundMusicPlayer.seek(Duration.millis(2.5));
			}
		}, "CheckIfMusicIsDone");

	}

	/**
	 * Sets the background music volume.
	 *
	 * @param volume the new background music volume
	 */
	public void setBackgroundMusicVolume(double volume) { backgroundMusicVolume = volume; }

	/**
	 * Sets the sound volume.
	 *
	 * @param volume the new sound volume
	 */
	public void setSoundVolume(double volume) {
		soundVolume = volume;
	}

}
