package rngGame.main;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import javax.imageio.ImageIO;
import javafx.animation.*;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.stage.Stage;
import javafx.util.Duration;
import rngGame.tile.ImgUtil;

public class TestCollBoxGen extends Application {

	private final List<Double> li = new ArrayList<>();
	private Image img;
	private Polyline pl;

	private double wr, hr;

	public static void main(String[] args) {
		launch(args);
	}

	public void findPoints() {
		record Point(double x, double y) {}
		List<Point> li = new ArrayList<>();
		PixelReader pr = img.getPixelReader();
		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++) {
				int a = pr.getArgb(i, j) << 24 >> 24;
				if (a != 0) li.add(new Point(i, j));

			}
		System.out.println(li.size());
		ListIterator<Point> liIt = li.listIterator();
		List<Point> rem = new ArrayList<>();
		while (liIt.hasNext()) {
			Point p = liIt.next();
			AtomicInteger ai = new AtomicInteger(0);
			li.forEach(pi->{
				if (pi.x() == p.x() && Math.abs(pi.y() - p.y()) == 1) ai.incrementAndGet();
				if (pi.y() == p.y() && Math.abs(pi.x() - p.x()) == 1) ai.incrementAndGet();
			});
			if (ai.get() == 4)
				rem.add(p);
		}
		System.out.println(li);
		li.removeAll(rem);
		System.out.println(li);
		AtomicReference<Point> ap = new AtomicReference<>(li.get(0)), lastPoint = new AtomicReference<>();
		List<String> matrix = Arrays.asList("-11", "01", "11", "10", "1-1", "0-1", "-1-1", "-10");
		List<Point> li2 = new ArrayList<>();
		li.forEach(p -> {
			if (p.x() < ap.get().x() || p.x() == ap.get().x() && p.y() < ap.get().y()) ap.set(p);
		});
		li2.add(ap.get());
		li.remove(ap.get());
		lastPoint.set(ap.get());

		int si = li.size();
		for (int i = 0; i < si; i++) {
			ap.set(null);
			li.forEach(p -> {
				if (Math.abs(lastPoint.get().x() - p.x()) <= 1 && Math.abs(lastPoint.get().y() - p.y()) <= 1) if (ap.get() == null) ap.set(p);
				else {
					int apX = (int) (ap.get().x() - lastPoint.get().x()),
							apY = (int) (ap.get().y() - lastPoint.get().y()),
							pX = (int) (p.x() - lastPoint.get().x()),
							pY = (int) (p.y() - lastPoint.get().y());
					if (matrix.indexOf(pX + "" + pY) != -1)
						if (matrix.indexOf(pX + "" + pY) < matrix.indexOf(apX + "" + apY)) ap.set(p);
				}
			});
			if (ap.get() == null) break;

			Point p = ap.get();
			AtomicInteger ai = new AtomicInteger(0);
			li.forEach(pi -> {
				if (pi.x() == p.x() && Math.abs(pi.y() - p.y()) == 1) ai.incrementAndGet();
				if (pi.y() == p.y() && Math.abs(pi.x() - p.x()) == 1) ai.incrementAndGet();
			});
			System.out.println(ai.get());
			li2.add(ap.get());
			// if (ai.get() < 2)
			li.remove(ap.get());
			lastPoint.set(ap.get());
		}
		System.out.println(li2.size());
		System.out.println(li2);
		synchronized (this.li) {
			li2.forEach(p -> {
				this.li.add(p.x() * wr);
				this.li.add(p.y() * hr);
			});
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FileInputStream fis = new FileInputStream(new File("./res/npc/FritzA.png"));
		Pane p = new Pane();
		p.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		ImageView iv = new ImageView();
		iv.setImage(ImgUtil.resizeImage(new Image(fis), 64, 32, 300, 150));
		int w = 135, h = 75;
		wr = w / iv.getImage().getWidth();
		hr = h / iv.getImage().getHeight();

		img = iv.getImage();
		pl = new Polyline();
		pl.setVisible(true);
		pl.setStroke(Color.color(1, 0, 0, .5));
		// pl.getPoints().addAll(20.0, 20.0, 40.0, 40.0);
		new Thread(this::findPoints).start();
		Timeline tl = new Timeline(new KeyFrame(Duration.millis(1000 / 60), event -> {
			synchronized (li) {
				if (li.size() > 0) {
					pl.getPoints().addAll(li);
					li.clear();
					WritableImage wi = p.snapshot(null, null);
					BufferedImage bi = SwingFXUtils.fromFXImage(wi, null);
					try {
						ImageIO.write(bi, "png", new File("./img.png"));
						RandomAccessFile raf = new RandomAccessFile(
								new File("./res/collisions/npc/FritzA.collisionbox"), "rws");
						raf.seek(0l);
						raf.writeInt(pl.getPoints().size());
						for (double d: pl.getPoints())
							raf.writeDouble(d);
						raf.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}));
		tl.setCycleCount(Animation.INDEFINITE);
		tl.play();
		p.getChildren().addAll(iv, pl);
		primaryStage.setScene(new Scene(p));
		primaryStage.show();
	}

}
