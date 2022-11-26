package rngGame.main;

import java.io.*;
import java.util.*;
import javafx.scene.image.*;
import rngGame.tile.ImgUtil;

public class Text {

	private static final Text INSTANCE = new Text();

	private final Map<Character, Image> charmap;

	private Text() {
		charmap = new HashMap<>();
		try {
			loadCharacterFiles();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Text getInstance() { return INSTANCE; }

	public Image convertText(String text) {
		int lines = text.replaceAll("[^\n]", "").length() + 1;
		int length = 0;
		for (String line: text.split("\n")) length = Math.max(length, line.length());
		WritableImage im = new WritableImage(length * 32, lines * 32);
		PixelWriter pw = im.getPixelWriter();
		char[] chars = text.toCharArray();
		int x = 0, y = 0;
		for (char c: chars) if (c == '\n') {
			x = 0;
			y++;
		} else if (c == ' ') x += 20;
		else {
			pw.setPixels(x, y * 32, (int) charmap.get(c).getWidth(), 32, charmap.get(c).getPixelReader(), 0, 0);
			x += charmap.get(c).getWidth();
		}
		return im;
	}

	public Image convertText(String text, int fontSize) {
		int lines = text.replaceAll("[^\n]", "").length() + 1;
		int length = 0;
		for (String line: text.split("\n")) length = Math.max(length, line.length());
		WritableImage im = new WritableImage(length * 32, lines * 32);
		PixelWriter pw = im.getPixelWriter();
		char[] chars = text.toCharArray();
		int x = 0, y = 0;
		for (char c: chars) if (c == '\n') {
			x = 0;
			y++;
		} else if (c == ' ') x += (int) (20 / 32.0 * fontSize);
		else {
			Image wi = ImgUtil.resizeImage(charmap.get(c), (int) charmap.get(c).getWidth(), 32,
					(int) (charmap.get(c).getWidth() / 32.0 * fontSize), fontSize);
			pw.setPixels(x, y * fontSize, (int) wi.getWidth(), fontSize, wi.getPixelReader(), 0, 0);
			x += charmap.get(c).getWidth();
		}
		return im;
	}

	public void loadCharacterFiles() throws FileNotFoundException {
		File dir = new File("./res/letters/");

		File big = new File(dir, "Groß.png");
		File small = new File(dir, "Klein.png");
		File numbers = new File(dir, "Zahlen.png");

		Image bigI = new Image(new FileInputStream(big)),
				smallI = new Image(new FileInputStream(small)),
				numbersI = new Image(new FileInputStream(numbers));

		char[][] bigC = new char[][] {
			{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'},
			{'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R'},
			{'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '.'}
		}, smallC = new char[][] {
			{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'},
			{'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r'},
			{'s', 't', 'u', 'v', 'w', 'x', 'y', 'z'}
		}, numbersC = new char[][] {
			{'1', '2', '3', '4', '5', '6', '7', '8', '9'},
			{'0', '?', '!', '%', '"', '#'}
		};

		PixelReader bigPR = bigI.getPixelReader(),
				smallPR = smallI.getPixelReader(),
				numbersPR = numbersI.getPixelReader();

		for (int x = 0; x < bigI.getWidth() / 32; x++)
			for (int y = 0; y < bigI.getHeight() / 32; y++) if (y<bigC.length && x<bigC[y].length) {
				WritableImage cha = new WritableImage(16, 32);
				PixelWriter pw = cha.getPixelWriter();
				pw.setPixels(0, 0, 16, 32, bigPR, x * 32 + 8, y * 32);
				charmap.put(bigC[y][x], cha);
			}
		for (int x = 0; x < smallI.getWidth() / 32; x++)
			for (int y = 0; y < smallI.getHeight() / 32; y++) if (y<smallC.length && x<smallC[y].length) {
				WritableImage cha = new WritableImage(16, 32);
				PixelWriter pw = cha.getPixelWriter();
				pw.setPixels(0, 0, 16, 32, smallPR, x * 32 + 8, y * 32);
				charmap.put(smallC[y][x], cha);
			}
		for (int x = 0; x < numbersI.getWidth() / 32; x++)
			for (int y = 0; y < numbersI.getHeight() / 32; y++) if (y<numbersC.length && x<numbersC[y].length) {
				WritableImage cha = new WritableImage(20, 32);
				PixelWriter pw = cha.getPixelWriter();
				pw.setPixels(0, 0, 20, 32, numbersPR, x * 32 + 6, y * 32);
				charmap.put(numbersC[y][x], cha);
			}
	}

}
