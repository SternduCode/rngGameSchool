package rngGame.tile;

import javafx.geometry.Point2D;
import javafx.scene.image.*;
import org.w3c.dom.Node;
import rngGame.main.WindowManager;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The Class ImgUtil.
 */
public class ImgUtil {

	/**
	 * Gets the scaled image.
	 *
	 * @param path the path
	 * @return the scaled image
	 */
	public static Image getScaledImage(String path) {
		try {
			Image wi = new Image(new FileInputStream(path));
			return getScaledImage(path, (int)wi.getWidth(),  (int)wi.getHeight());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image getScaledImage(String path, boolean flip) {
		try {
			Image wi = new Image(new FileInputStream(path));
			return getScaledImage(path, (int)wi.getWidth(),  (int)wi.getHeight(), flip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the scaled image.
	 *
	 * @param path the path
	 * @param width the width
	 * @param height the height
	 * @return the scaled image
	 */
	public static Image getScaledImage(String path, int width, int height) {
		return getScaledImages(path, width, height)[0];
	}
	
	public static Image getScaledImage(String path, int width, int height, boolean flip) {
		return getScaledImages(path, width, height, flip)[0];
	}

	public static Point2D getoriginalSize(String path) {
		try {
			Image wi = new Image(new FileInputStream(path));
			return new Point2D(wi.getWidth(), wi.getHeight());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the scaled image.
	 *
	 * @param path the path
	 * @return the scaled image
	 */
	public static Image[] getScaledImages(String path) {
		try {
			Image wi = new Image(new FileInputStream(path));
			return getScaledImages(path, (int)wi.getWidth(),  (int)wi.getHeight());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image[] getScaledImages(String path, boolean flip) {
		try {
			Image wi = new Image(new FileInputStream(path));
			return getScaledImages(path, (int)wi.getWidth(),  (int)wi.getHeight(), flip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Image[] getScaledImages(String path, int width, int height) {
		return getScaledImages(path, width, height, false);
	}

	/**
	 * For gifs put {@code 			iv.setScaleX(gamepanel.getScalingFactorX());
	 * 			iv.setScaleY(gamepanel.getScalingFactorY());} to set the scale
	 *
	 * @param path the path
	 * @param width the width
	 * @param height the height
	 * @return the scaled image
	 */
	public static Image[] getScaledImages(String path, int width, int height, boolean flip) {
		String[] sp = path.split("[.]");
		try {
			if ("gif".equals(sp[sp.length - 1])) {
				ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();

				ImageInputStream iis = ImageIO
						.createImageInputStream(new FileInputStream(path));
				reader.setInput(iis, false);
				int noi = reader.getNumImages(true);

				BufferedImage[] imgs = new BufferedImage[noi];
				int[]			xOffsets	= new int[noi];
				int[]			yOffsets	= new int[noi];
				int				origWidth, origHeight;

				Image origImg = new Image(new FileInputStream(path));

				origWidth	= (int) origImg.getWidth();
				origHeight	= (int) origImg.getHeight();

				for (int i = 0; i < noi; i++) {
					Node node = reader.getImageMetadata(i).getAsTree("javax_imageio_gif_image_1.0");
					xOffsets[i]	= Integer.parseInt(node.getFirstChild().getAttributes().getNamedItem("imageLeftPosition").getNodeValue());
					yOffsets[i]	= Integer.parseInt(node.getFirstChild().getAttributes().getNamedItem("imageTopPosition").getNodeValue());

					imgs[i] = reader.read(i);
				}

				WritableImage[] awtToFx = new WritableImage[imgs.length];

				for (int i = 0; i < imgs.length; i++) {
					BufferedImage bi = imgs[i];
					awtToFx[i] = new WritableImage(origWidth, origHeight);
					PixelWriter pw = awtToFx[i].getPixelWriter();
					int[] data = new int[bi.getWidth() * bi.getHeight()];
					bi.getRGB(0, 0, bi.getWidth(), bi.getHeight(), data, 0, bi.getWidth());
					pw.setPixels(xOffsets[i], yOffsets[i], bi.getWidth(), bi.getHeight(), PixelFormat.getIntArgbInstance(), data, 0, bi.getWidth());
				}

				Image[] out = new Image[awtToFx.length];

				for (int i = 0; i < out.length; i++) out[i] = ImgUtil.resizeImage(
						awtToFx[i], (int) awtToFx[i].getWidth(), (int) awtToFx[i].getHeight(),
						(int) (width * WindowManager.getInstance().getScalingFactorX()),
						(int) (height * WindowManager.getInstance().getScalingFactorY()), flip);

				return out;
			}
			Image wi = new Image(new FileInputStream(path));
			Image[] imgs = new Image[1];
			for (int k = 0; k < imgs.length; k++) {
				imgs[k] = ImgUtil.resizeImage(
							wi, (int) wi.getWidth(), (int) wi.getHeight(),
							(int) (width * WindowManager.getInstance().getScalingFactorX()),
							(int) (height * WindowManager.getInstance().getScalingFactorY()), flip);
			}
			return imgs;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the scale factor.
	 *
	 * @param width the width
	 * @param height the height
	 * @param requestedWidth the requested width
	 * @param requestedHeight the requested height
	 * @return the scale factor
	 */
	public static Entry<Integer, Integer> getScaleFactor(int width, int height, int requestedWidth,
			int requestedHeight) {
		return Map.entry(requestedWidth / width, requestedHeight / height);
	}

	/**
	 * Resample.
	 *
	 * @param input the input
	 * @param scaleFactor the scale factor
	 * @return the image
	 */
	public static Image resample(Image input, Map.Entry<Integer, Integer> scaleFactor) {
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int WS = scaleFactor.getKey();
		final int HS = scaleFactor.getValue();

		WritableImage output = new WritableImage(
				W * WS,
				H * HS);

		PixelReader reader = input.getPixelReader();
		PixelWriter writer = output.getPixelWriter();

		for (int y = 0; y < H; y++) for (int x = 0; x < W; x++) {
			final int argb = reader.getArgb(x, y);
			for (int dy = 0; dy < HS; dy++)
				for (int dx = 0; dx < WS; dx++) writer.setArgb(x * WS + dx, y * HS + dy, argb);
		}

		return output;
	}

	/**
	 * Resize buffered image.
	 *
	 * @param input the input
	 * @param w1 the w 1
	 * @param h1 the h 1
	 * @param w2 the w 2
	 * @param h2 the h 2
	 * @return the buffered image
	 */
	public static BufferedImage resizeBufferedImage(BufferedImage input, int w1, int h1, int w2, int h2) {
		BufferedImage bi = new BufferedImage(w2, h2, input.getType());
		double x_ratio = w1 / (double) w2;
		double y_ratio = h1 / (double) h2;
		int px, py;
		for (int i = 0; i < h2; i++) for (int j = 0; j < w2; j++) {
			px = (int) Math.floor(j * x_ratio);
			py = (int) Math.floor(i * y_ratio);
			bi.setRGB(j, i, input.getRGB(px, py));
		}
		return bi;
	}
	
	public static Image resizeImage(Image input, int w1, int h1, int w2, int h2) {
		return resizeImage(input, w1, h1, w2, h2, false);
	}

	/**
	 * Resize image.
	 *
	 * @param input the input
	 * @param w1 the w 1
	 * @param h1 the h 1
	 * @param w2 the w 2
	 * @param h2 the h 2
	 * @return the image
	 */
	public static Image resizeImage(Image input, int w1, int h1, int w2, int h2, boolean flip) {
		WritableImage wi = new WritableImage(w2, h2);
		PixelReader reader = input.getPixelReader();
		PixelWriter writer = wi.getPixelWriter();
		double x_ratio = w1 / (double) w2;
		double y_ratio = h1 / (double) h2;
		int px, py;
		for (int i = 0; i < h2; i++) for (int j = 0; j < w2; j++) {
			px = (int) Math.floor(j * x_ratio);
			py = (int) Math.floor(i * y_ratio);
			if (flip)
				writer.setArgb(w2 - 1 - j, i, reader.getArgb(px, py));
			else
				writer.setArgb(j, i, reader.getArgb(px, py));
		}
		return wi;
	}

}
