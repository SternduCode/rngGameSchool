package rngGame.visual;

import javafx.scene.image.*;
import rngGame.tile.ImgUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class AnimatedImage.
 */
public class AnimatedImage extends ImageView {

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The fps. */
	private final int fps;

	/** The path. */
	private final String path;

	/** The last frame time. */
	private long lastFrameTime = 0l;

	/** The frames. */
	private Image[] frames;

	/** The frame index. */
	private int frameIndex = 0;

	/**
	 * Instantiates a new animated image.
	 *
	 * @param path      the path
	 * @param gamepanel the gamepanel
	 */
	public AnimatedImage(String path, GamePanel gamepanel) {
		this.path		= path;
		this.gamepanel	= gamepanel;
		fps				= 7;
		gamepanel.addAnimatedImage(this);
		update();
	}

	/**
	 * Instantiates a new animated image.
	 *
	 * @param path      the path
	 * @param gamepanel the gamepanel
	 * @param fps       the fps
	 */
	public AnimatedImage(String path, GamePanel gamepanel, int fps) {
		this.path		= path;
		this.gamepanel	= gamepanel;
		this.fps		= fps;
		gamepanel.addAnimatedImage(this);
		update();
	}

	/**
	 * Scale F 11.
	 */
	public void scaleF11() {
		frames = ImgUtil.getScaledImages(gamepanel, path);
		setImage(frames[frameIndex]);
	}

	/**
	 * Update.
	 */
	public void update() {
		if (System.currentTimeMillis() >= lastFrameTime + 1000.0 / fps) {
			frameIndex++;
			if (frameIndex >= frames.length) frameIndex = 0;
			lastFrameTime = 0l;
			setImage(frames[frameIndex]);
		}
	}

}
