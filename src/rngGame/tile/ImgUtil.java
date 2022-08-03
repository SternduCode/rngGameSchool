package rngGame.tile;

import java.util.Map;
import java.util.Map.Entry;
import javafx.scene.image.*;

public class ImgUtil {

	public static Entry<Integer, Integer> getScaleFactor(int width, int height, int requestedWidth,
			int requestedHeight) {
		return Map.entry(requestedWidth / width, requestedHeight / height);
	}

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
