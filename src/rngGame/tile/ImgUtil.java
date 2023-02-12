package rngGame.tile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.image.*;
import rngGame.main.GamePanel;

// TODO: Auto-generated Javadoc
/**
 * The Class ImgUtil.
 */
public class ImgUtil {

	/**
	 * Gets the scaled image.
	 *
	 * @param gamepanel the gamepanel
	 * @param path the path
	 * @return the scaled image
	 */
	public static Image getScaledImage(GamePanel gamepanel, String path) {
		try {
			Image wi = new Image(new FileInputStream(path));
			return getScaledImage(gamepanel, path, (int)wi.getWidth(),  (int)wi.getHeight());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * For gifs put {@code 			iv.setScaleX(gamepanel.getScalingFactorX());
	 * 			iv.setScaleY(gamepanel.getScalingFactorY());} to set the scale
	 *
	 * @param gamepanel the gamepanel
	 * @param path the path
	 * @param width the width
	 * @param height the height
	 * @return the scaled image
	 */
	public static Image getScaledImage(GamePanel gamepanel, String path, int width, int height) {
		String[] sp = path.split("[.]");
		try {
			if ("gif".equals(sp[sp.length - 1])) {
				// ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
				// ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
				// PipedInputStream pis = new PipedInputStream();
				// PipedOutputStream pos = new PipedOutputStream(pis);
				// ImageInputStream iis = ImageIO
				// .createImageInputStream(new FileInputStream("./res/Contractstuff/EZ.gif"));
				// reader.setInput(iis, false);
				// ImageOutputStream ios = ImageIO.createImageOutputStream(pos);
				// writer.setOutput(ios);
				// int noi = reader.getNumImages(true);
				// System.out.println(noi);
				//
				// // IIOMetadata streamMeta = reader.getStreamMetadata();
				// // System.out.println(Arrays.asList(streamMeta.getMetadataFormatNames()));
				// // Node node =
				// streamMeta.getAsTree("javax_imageio_gif_stream_1.0").getFirstChild().getNextSibling();
				// // node.getAttributes().getNamedItem("logicalScreenHeight").setNodeValue(
				// // (int)
				// (Integer.parseInt(node.getAttributes().getNamedItem("logicalScreenHeight").getNodeValue())
				// // * gamepanel.getScalingFactorY()) + "");
				// // node.getAttributes().getNamedItem("logicalScreenWidth").setNodeValue(
				// // (int)
				// (Integer.parseInt(node.getAttributes().getNamedItem("logicalScreenWidth").getNodeValue())
				// // * gamepanel.getScalingFactorY()) + "");
				// writer.prepareWriteSequence(null);
				// for (int i = 0; i < noi; i++) {
				// BufferedImage bi = reader.read(i);
				// System.out.println(bi);
				// bi = ImgUtil.resizeBufferedImage(
				// bi, bi.getWidth(), bi.getHeight(), (int) (bi.getWidth() *
				// gamepanel.getScalingFactorX()),
				// (int) (bi.getHeight() * gamepanel.getScalingFactorY()));
				// writer.writeToSequence(new IIOImage(bi, null, null), null);
				// }
				// writer.endWriteSequence();
				// writer.dispose();
				// ios.flush();
				// pos.flush();
				Image wi = new Image(new FileInputStream(path));
				return wi;
			}
			Image wi = new Image(new FileInputStream(path));
			return ImgUtil.resizeImage(
					wi, (int) wi.getWidth(), (int) wi.getHeight(),
					(int) (width * gamepanel.getScalingFactorX()),
					(int) (height * gamepanel.getScalingFactorY()));
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
	public static Image resizeImage(Image input, int w1, int h1, int w2, int h2) {
		WritableImage wi = new WritableImage(w2, h2);
		PixelReader reader = input.getPixelReader();
		PixelWriter writer = wi.getPixelWriter();
		double x_ratio = w1 / (double) w2;
		double y_ratio = h1 / (double) h2;
		int px, py;
		for (int i = 0; i < h2; i++) for (int j = 0; j < w2; j++) {
			px = (int) Math.floor(j * x_ratio);
			py = (int) Math.floor(i * y_ratio);
			writer.setArgb(j, i, reader.getArgb(px, py));
		}
		return wi;
	}

}
