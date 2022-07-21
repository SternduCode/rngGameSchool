package tile;

import java.util.Map;
import javafx.scene.image.*;

public class ImgUtil {

	public static Map.Entry<Double, Double> getScaleFactor(Image i, int requestedWidth, int requestedHeight) {
		return Map.entry(requestedWidth / i.getWidth(), requestedHeight / i.getHeight());
	}

	public static Image resample(Image input, Map.Entry<Double, Double> scaleFactor) {
		final int W = (int) input.getWidth();
		final int H = (int) input.getHeight();
		final int WS = (int) (double) scaleFactor.getKey();
		final int HS = (int) (double) scaleFactor.getValue();

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

}
