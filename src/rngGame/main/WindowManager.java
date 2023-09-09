package rngGame.main;

import rngGame.visual.AnimatedImage;
import rngGame.visual.GamePanel;

import java.util.ArrayList;
import java.util.List;

public class WindowManager {

	private WindowManager() {}

	/** The animated images. */
	private final List<AnimatedImage> animatedImages = new ArrayList<>();

	private static final WindowManager INSTANCE = new WindowManager();

	/** The target FPS. */
	private final int targetFPS = 80;

	/** The scaling factor Y. */
	private double scalingFactorX = 1, scalingFactorY = 1;

	/** The block size. */
	private final int blockSize = 128;

	/** The x blocks. */
	private final int xBlocks = 20;

	/** The y blocks. */
	private final int yBlocks = 11;

	/** The scleed blockSizes. */
	private int blockSizeX = blockSize, blockSizeY = blockSize;

	/** The game height. */
	private int gameHeight = blockSizeY * yBlocks;

	/** The game width. */
	private int gameWidth = blockSizeX * xBlocks;

	public static WindowManager getInstance() {
	return INSTANCE;
	}

	/**
	 * Gets the block size.
	 *
	 * @return the block size
	 */
	public int getBlockSize() { return blockSize; }

	/**
	 * Gets the block size X.
	 *
	 * @return the block size X
	 */
	public int getBlockSizeX() { return blockSizeX; }

	/**
	 * Gets the block size Y.
	 *
	 * @return the block size Y
	 */
	public int getBlockSizeY() { return blockSizeY; }

	/**
	 * Gets the game height.
	 *
	 * @return the game height
	 */
	public int getGameHeight() { return gameHeight; }

	/**
	 * Gets the game width.
	 *
	 * @return the game width
	 */
	public int getGameWidth() { return gameWidth; }

	/**
	 * Gets the scaling factor X.
	 *
	 * @return the scaling factor X
	 */
	public double getScalingFactorX() { return scalingFactorX; }

	/**
	 * Gets the scaling factor Y.
	 *
	 * @return the scaling factor Y
	 */
	public double getScalingFactorY() { return scalingFactorY; }

	/**
	 * Gets the target FPS.
	 *
	 * @return the target FPS
	 */
	public int getTargetFPS() { return targetFPS; }

	/**
	 * Gets the x blocks.
	 *
	 * @return the x blocks
	 */
	public int getxBlocks() { return xBlocks; }

	/**
	 * Gets the y blocks.
	 *
	 * @return the y blocks
	 */
	public int getyBlocks() { return yBlocks; }

	/**
	 * Adds the animated image.
	 *
	 * @param animatedImage the animated image
	 */
	public void addAnimatedImage(AnimatedImage animatedImage) {
		animatedImages.add(animatedImage);
	}

	public void changeScalingFactor(double scaleFactorX, double scaleFactorY) {
		scalingFactorX = scaleFactorX;
		scalingFactorY = scaleFactorY;
		blockSizeX		= (int) (blockSize * scaleFactorX);
		blockSizeY		= (int) (blockSize * scaleFactorY);
		gameWidth		= blockSizeX * xBlocks;
		gameHeight		= blockSizeY * yBlocks;
	}

	public void update() {
		animatedImages.forEach(AnimatedImage::update);
	}

}
