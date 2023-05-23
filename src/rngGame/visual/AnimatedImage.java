package rngGame.visual;

import javafx.scene.image.*;
import rngGame.tile.ImgUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class AnimatedImage.
 */
public class AnimatedImage extends ImageView {

	/** The gamepanel. */
	protected final GamePanel gamepanel;

	/** The fps. */
	private int fps;

	/** The path. */
	private String path;

	/** The last frame time. */
	private long lastFrameTime = 0l;

	/** The frames. */
	private Image[] frames;

	/** The frame index. */
	private int frameIndex = 0;

	/**
	 * Instantiates a new animated image.
	 *
	 * @param gamepanel the gamepanel
	 */
	public AnimatedImage(GamePanel gamepanel) {
		imgRequestedWidth        = -1;
        imgRequestedHeight        = -1;
		this.gamepanel = gamepanel;
		fps = 7;
		gamepanel.addAnimatedImage(this);
	}

	/**
	 * Instantiates a new animated image.
	 *
	 * @param path      the path
	 * @param gamepanel the gamepanel
	 */
	public AnimatedImage(String path, GamePanel gamepanel) {
		imgRequestedWidth        = -1;
        imgRequestedHeight        = -1;
		this.path		= path;
		this.gamepanel	= gamepanel;
		fps				= 7;
		gamepanel.addAnimatedImage(this);
		scaleF11();
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
		imgRequestedWidth        = -1;
        imgRequestedHeight        = -1;
		this.path		= path;
		this.gamepanel	= gamepanel;
		this.fps		= fps;
		gamepanel.addAnimatedImage(this);
		scaleF11();
		update();
	}

	/**
	 * Gets the fps.
	 *
	 * @return the fps
	 */
	public int getFps() { return fps; }

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() { return path; }

	/**
	 * Inits the.
	 *
	 * @param path the path
	 */
	public void init(String path) {
		this.path		= path;
		scaleF11();
		update();
	}

	/**
	 * Inits the.
	 *
	 * @param path the path
	 * @param fps the fps
	 */
	public void init(String path, int fps) {
		this.path		= path;
		this.fps		= fps;
		scaleF11();
		update();
	}

	/**
	 * Scale F 11.
	 */
	public void scaleF11() {
		if (path != null) {
            if (imgRequestedWidth != -1 && imgRequestedHeight != -1)
                frames = ImgUtil.getScaledImages(gamepanel, path, imgRequestedWidth, imgRequestedHeight);
            else frames = ImgUtil.getScaledImages(gamepanel, path);
            if (frameIndex >= frames.length)
                frameIndex = 0;
        } else for (int i = 0; i < frames.length; i++) {
            Image img = frames[i];
            if (imgRequestedWidth != -1 && imgRequestedHeight != -1)
                frames[i] = ImgUtil.resizeImage(img, (int) img.getWidth(), (int) img.getHeight(),
                        (int) (imgRequestedWidth * gamepanel.getScalingFactorX()), (int) (imgRequestedHeight * gamepanel.getScalingFactorY()));
            else
                frames[i] = ImgUtil.resizeImage(img, (int) img.getWidth(), (int) img.getHeight(),
                        (int) (img.getWidth() * gamepanel.getScalingFactorX()), (int) (img.getHeight() * gamepanel.getScalingFactorY()));
        }
		setImage(frames[frameIndex]);
	}

	/**
	 * Sets the fps.
	 *
	 * @param fps the new fps
	 */
	public void setFps(int fps) { this.fps = fps; }

	/**
	 * Uninit.
	 */
	public void uninit() {
		frames = null;
		frameIndex	= 0;
	}

	/**
	 * Update.
	 */
	public void update() {
		if (frames != null && System.currentTimeMillis() >= lastFrameTime + 1000.0 / fps) {
			frameIndex++;
			if (frameIndex >= frames.length) frameIndex = 0;
			lastFrameTime = System.currentTimeMillis();
			setImage(frames[frameIndex]);
		}
	}
	/**
     * Gets the img requested height.
     *
     * @return the img requested height
     */
    public int getImgRequestedHeight() { return imgRequestedHeight; }

    /**
     * Gets the img requested width.
     *
     * @return the img requested width
     */
    public int getImgRequestedWidth() { return imgRequestedWidth; }
    /** The img requested height. */
    private int imgRequestedWidth, imgRequestedHeight;
    /**
     * Sets the img requested height.
     *
     * @param imgRequestedHeight the new img requested height
     */
    public void setImgRequestedHeight(int imgRequestedHeight) { this.imgRequestedHeight = imgRequestedHeight; }

    /**
     * Sets the img requested width.
     *
     * @param imgRequestedWidth the new img requested width
     */
    public void setImgRequestedWidth(int imgRequestedWidth) { this.imgRequestedWidth = imgRequestedWidth; }
}
