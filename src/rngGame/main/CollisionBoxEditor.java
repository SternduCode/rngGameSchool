package rngGame.main;

import java.io.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.tile.ImgUtil;

public class CollisionBoxEditor extends Application {

	private File collboxFile;
	private double posX, posY, movX, movY, sf = 1;
	private Image imgi;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("CollisionBoxEditor");

		Pane p = new Pane();

		p.setPrefWidth(1280);
		p.setPrefHeight(720);

		Pane sidebar = new Pane();
		sidebar.setPrefWidth(60);
		sidebar.setPrefHeight(720);
		sidebar.setBackground(new Background(new BackgroundFill(Color.GREY.darker(), null, null)));
		Pane mainView = new Pane();
		mainView.setLayoutX(60);
		mainView.setPrefWidth(1280 - 60);
		mainView.setPrefHeight(720);

		ImageView iv = new ImageView();

		Button newB = new Button("New");
		newB.setLayoutY(25);
		newB.setPrefWidth(60);
		Button save = new Button("Save");
		save.setLayoutY(50);
		save.setPrefWidth(60);
		Button redo = new Button("Redo");
		redo.setLayoutY(75);
		redo.setPrefWidth(60);
		Button undo = new Button("Undo");
		undo.setLayoutY(100);
		undo.setPrefWidth(60);
		Button all = new Button("Select All");
		all.setFont(Font.font(11));
		all.setLayoutY(125);
		all.setPrefWidth(60);
		Label lblXS = new Label("Width");
		lblXS.setLayoutY(150);
		lblXS.setPrefWidth(60);
		TextField xS = new TextField("48");
		xS.textProperty().addListener((obV, oldV, newV) -> {
			try {
				if (!newV.equals("")) Integer.parseInt(newV);
			} catch (NumberFormatException e) {
				xS.setText(oldV);
			}
		});
		xS.setLayoutY(175);
		xS.setPrefWidth(60);
		Label lblYS = new Label("Height");
		lblYS.setLayoutY(200);
		lblYS.setPrefWidth(60);
		TextField yS = new TextField("48");
		yS.textProperty().addListener((obV, oldV, newV) -> {
			try {
				if (!newV.equals("")) Integer.parseInt(newV);
			} catch (NumberFormatException e) {
				yS.setText(oldV);
			}
		});
		yS.setLayoutY(225);
		yS.setPrefWidth(60);

		Polygon ply = new Polygon();

		Polygon plyV = new Polygon();
		plyV.setFill(Color.color(1, 0, 0, .35));

		Button open = new Button("Open");
		open.setPrefWidth(60);
		open.setOnAction(ae -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File("./res/collisions"));
			fc.getExtensionFilters().add(new ExtensionFilter(
					"A file containing the collision box of something", "*.collisionbox"));
			File f = fc.showOpenDialog(primaryStage);
			if (f != null && f.getPath().contains("res" + File.separator + "collisions")) {
				int idx = f.getPath().lastIndexOf("res" + File.separator + "collisions");
				String subPath = f.getPath().substring(idx + ("res" + File.separator + "collisions").length() + 1,
						f.getPath().length() - ".collisionbox".length());

				System.out.println(subPath);
				File img = new File("./res/" + subPath + ".gif");
				try {
					iv.setImage(new Image(new FileInputStream(img)));
					imgi = iv.getImage();
					posX = 0;
					posY = 0;
					movX = 0;
					movY = 0;
					iv.setLayoutX(mainView.getWidth() / 2 - iv.getImage().getWidth() / 2);
					iv.setLayoutY(mainView.getHeight() / 2 - iv.getImage().getHeight() / 2);
					plyV.setLayoutX(iv.getLayoutX());
					plyV.setLayoutY(iv.getLayoutY());
					collboxFile = f;
					ply.getPoints().clear();
					plyV.getPoints().clear();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		save.setOnAction(ae -> {
			try {
				double factor = 48 / imgi.getHeight();
				RandomAccessFile raf = new RandomAccessFile(collboxFile, "rws");
				raf.seek(0l);
				raf.writeInt(ply.getPoints().size());
				for (Double element: ply.getPoints()) raf.writeDouble(element * factor);
				raf.setLength(4l + ply.getPoints().size() * 8l);
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		all.setOnAction(ae -> {
			ply.getPoints().addAll(0d, 0d, imgi.getWidth(), 0d, imgi.getWidth(), imgi.getHeight(), 0d,
					imgi.getHeight());
			plyV.getPoints().addAll(0d, 0d, imgi.getWidth() * sf, 0d, imgi.getWidth() * sf, imgi.getHeight() * sf, 0d,
					imgi.getHeight() * sf);
		});

		primaryStage.addEventFilter(ScrollEvent.SCROLL, e -> {
			if (e.isControlDown()) {
				int idx = collboxFile.getPath().lastIndexOf("res" + File.separator + "collisions");
				String subPath = collboxFile.getPath().substring(
						idx + ("res" + File.separator + "collisions").length() + 1,
						collboxFile.getPath().length() - ".collisionbox".length());
				File img = new File("./res/" + subPath + ".gif");
				try {
					Image imgi = new Image(new FileInputStream(img));
					sf = (int) (iv.getImage().getWidth() + iv.getImage().getWidth() * .005 * e.getDeltaY())
							/ imgi.getWidth();
					iv.setImage(ImgUtil.resizeImage(imgi, (int) imgi.getWidth(),
							(int) imgi.getHeight(),
							(int) (iv.getImage().getWidth() + iv.getImage().getWidth() * .005 * e.getDeltaY()),
							(int) (iv.getImage().getHeight() + iv.getImage().getHeight() * .005 * e.getDeltaY())));
					iv.setLayoutX(mainView.getWidth() / 2 - iv.getImage().getWidth() / 2 + movX);
					iv.setLayoutY(mainView.getHeight() / 2 - iv.getImage().getHeight() / 2 + movY);
					plyV.setLayoutX(iv.getLayoutX());
					plyV.setLayoutY(iv.getLayoutY());
					for (int i = 0; i < ply.getPoints().size(); i++) plyV.getPoints().set(i, ply.getPoints().get(i) * sf);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});

		primaryStage.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
			if (e.getButton() == MouseButton.PRIMARY) {
				ply.getPoints().add(
						(double) (int) ((e.getSceneX()
								- (mainView.getWidth() / 2 - iv.getImage().getWidth() / 2 + movX) - 60)
								/ sf));
				ply.getPoints()
				.add((double) (int) ((e.getSceneY()
						- (mainView.getHeight() / 2 - iv.getImage().getHeight() / 2 + movY))
						/ sf));
				plyV.getPoints()
				.add((int) ((e.getSceneX()
						- (mainView.getWidth() / 2 - iv.getImage().getWidth() / 2 + movX)
						- 60) / sf)
						* sf);
				plyV.getPoints().add(
						(int) ((e.getSceneY()
								- (mainView.getHeight() / 2 - iv.getImage().getHeight() / 2 + movY)) / sf)
						* sf);
			}
		});

		primaryStage.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			if (e.isMiddleButtonDown()) {
				if (Math.abs(e.getSceneX() - posX) > 20) posX = e.getSceneX();
				if (Math.abs(e.getSceneY() - posY) > 20) posY = e.getSceneY();
				movX += e.getSceneX() - posX;
				posX = e.getSceneX();
				movY += e.getSceneY() - posY;
				posY = e.getSceneY();
				iv.setLayoutX(mainView.getWidth() / 2 - iv.getImage().getWidth() / 2 + movX);
				iv.setLayoutY(mainView.getHeight() / 2 - iv.getImage().getHeight() / 2 + movY);
				plyV.setLayoutX(iv.getLayoutX());
				plyV.setLayoutY(iv.getLayoutY());
			}
		});

		sidebar.getChildren().addAll(open, newB, save, redo, undo, all, lblXS, xS, lblYS, yS);

		mainView.getChildren().addAll(iv, plyV);

		p.getChildren().addAll(mainView, sidebar);

		primaryStage.setScene(new Scene(p));

		primaryStage.show();

		p.getScene().heightProperty().addListener((obV, oldV, newV) -> {
			sidebar.setPrefHeight(newV.doubleValue());
			mainView.setPrefHeight(newV.doubleValue());
		});

		p.getScene().widthProperty().addListener((obV, oldV, newV) -> {
			mainView.setPrefWidth(newV.doubleValue() - 60);

		});
	}

}
