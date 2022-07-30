package entity;

import java.util.ArrayList;
import java.util.Map.Entry;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGAME.*;
import tile.ImgUtil;

public class Player extends Entity implements Collidable {

	private final SpielPanel gp;
	private final Input keyH;
	private final double fps = 7.5;
	private final int size = 64;

	public final int screenX;
	public final int screenY;


	public Player(SpielPanel gp2, Input keyH2) {

		gp = gp2;
		keyH = keyH2;

		up = new ArrayList<>();
		upl = new ArrayList<>();
		down = new ArrayList<>();
		downl = new ArrayList<>();
		left = new ArrayList<>();
		right = new ArrayList<>();
		nomovel = new ArrayList<>();
		nomover = new ArrayList<>();
		nomoveup = new ArrayList<>();

		screenX = gp.SpielLaenge / 2 - size / 2;
		screenY = gp.SpielHoehe / 2 - size / 2;

		setDisable(true);

		setDefaultValues();
		getPlayerImage();

		iv = new ImageView();

		shape = new Polygon();
		shape.setFill(Color.color(1, 0, 1, 0.75));
		shape.setDisable(true);
		shape.setVisible(false);
		shape.getPoints().addAll(0d, 0d, 0d, 20d, 22d, 20d, 22d, 0d);

		getChildren().addAll(iv, shape);
	}

	public void getPlayerImage() {

		try {
			Image up = new Image(getClass().getResourceAsStream("/res/player/LaufenHochL.png"));

			for (int i = 0; i < up.getWidth(); i += up.getHeight()) {
				WritableImage wi = new WritableImage(up.getPixelReader(), i, 0, (int) up.getHeight(),
						(int) up.getHeight());
				this.up.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image upl = new Image(getClass().getResourceAsStream("/res/player/LaufenHochL.png"));

			for (int i = 0; i < upl.getWidth(); i += upl.getHeight()) {
				WritableImage wi = new WritableImage(upl.getPixelReader(), i, 0, (int) upl.getHeight(),
						(int) upl.getHeight());
				this.upl.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image down = new Image(getClass().getResourceAsStream("/res/player/LaufenRunter.png"));

			for (int i = 0; i < down.getWidth(); i += down.getHeight()) {
				WritableImage wi = new WritableImage(down.getPixelReader(), i, 0, (int) down.getHeight(),
						(int) down.getHeight());
				this.down.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image downl = new Image(getClass().getResourceAsStream("/res/player/LaufenRunterL.png"));

			for (int i = 0; i < downl.getWidth(); i += downl.getHeight()) {
				WritableImage wi = new WritableImage(downl.getPixelReader(), i, 0, (int) downl.getHeight(),
						(int) downl.getHeight());
				this.downl.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image right = new Image(getClass().getResourceAsStream("/res/player/LaufenRechts.png"));

			for (int i = 0; i < right.getWidth(); i += right.getHeight()) {
				WritableImage wi = new WritableImage(right.getPixelReader(), i, 0, (int) right.getHeight(),
						(int) right.getHeight());
				this.right.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image left = new Image(getClass().getResourceAsStream("/res/player/LaufenLinks.png"));

			for (int i = 0; i < left.getWidth(); i += left.getHeight()) {
				WritableImage wi = new WritableImage(left.getPixelReader(), i, 0, (int) left.getHeight(),
						(int) left.getHeight());
				this.left.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image nomover = new Image(getClass().getResourceAsStream("/res/player/Stehen.png"));
			
			for (int i = 0; i < nomover.getWidth(); i += nomover.getHeight()) {
				WritableImage wi = new WritableImage(nomover.getPixelReader(), i, 0, (int) nomover.getHeight(),
						(int) nomover.getHeight());
				this.nomover.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image nomovel = new Image(getClass().getResourceAsStream("/res/player/Stehen2.png"));

			for (int i = 0; i < nomovel.getWidth(); i += nomovel.getHeight()) {
				WritableImage wi = new WritableImage(nomovel.getPixelReader(), i, 0, (int) nomovel.getHeight(),
						(int) nomovel.getHeight());
				this.nomovel.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

			Image nomoveup = new Image(getClass().getResourceAsStream("/res/player/IdleUp.png"));

			for (int i = 0; i < nomoveup.getWidth(); i += nomoveup.getHeight()) {
				WritableImage wi = new WritableImage(nomoveup.getPixelReader(), i, 0, (int) nomoveup.getHeight(),
						(int) nomoveup.getHeight());
				this.nomoveup.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), size, size));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Polygon getPoly() { return shape; }


	public void setDefaultValues() {

		worldX = gp.Bg * 13;
		worldY = gp.Bg * 37;
		oldWorldX = worldX;
		oldWorldY = worldY;
		speed = 3;
		direction = "down";
	}

	public void setPosition(Entry<Double, Double> startingPosition) {
		worldX = (int) (gp.Bg * startingPosition.getKey());
		worldY = (int) (gp.Bg * startingPosition.getValue());
		oldWorldX = worldX;
		oldWorldY = worldY;
	}

	public void update() {
		if (keyH.w) {
			if (direction.equals("left") || direction.endsWith("L")) direction = "upL";
			else direction = "up";
			worldY -= speed;
		}
		else if (keyH.s && !keyH.ctrlPressed) {
			if (direction.equals("left") || direction.endsWith("L")) direction = "downL";
			else direction = "down";
			worldY += speed;
		}
		else if (keyH.a) {
			direction = "left";
			worldX -= speed;
		}
		else if (keyH.d) {
			direction = "right";
			worldX += speed;
		} else if (direction.endsWith("L") && direction.contains("up")) direction = "noneupL";
		else if (direction.equals("up") || direction.contains("up")) direction = "noneup";
		else if (direction.equals("left") || direction.endsWith("L")) direction = "noneL";

		else direction = "none";

		if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
			spriteCounter = System.currentTimeMillis();
			spriteNum++;
		}

		Image image = null;

		switch(direction) {
			case "up":
				if (spriteNum >= up.size()) spriteNum = 0;
				image = up.get(spriteNum);
				break;
			case "upL":
				if (spriteNum >= upl.size()) spriteNum = 0;
				image = upl.get(spriteNum);
				break;
			case "down":
				if (spriteNum >= down.size()) spriteNum = 0;
				image = down.get(spriteNum);
				break;
			case "downL":
				if (spriteNum >= downl.size()) spriteNum = 0;
				image = downl.get(spriteNum);
				break;
			case "left":
				if (spriteNum >= left.size()) spriteNum = 0;
				image = left.get(spriteNum);
				break;
			case "right":
				if (spriteNum >= right.size()) spriteNum = 0;
				image = right.get(spriteNum);
				break;
			case "none":
				if (spriteNum >= nomover.size()) spriteNum = 0;
				image = nomover.get(spriteNum);
				break;
			case "noneL":
				if (spriteNum >= nomovel.size()) spriteNum = 0;
				image = nomovel.get(spriteNum);
				break;
			case "noneup", "noneupL":
				if (spriteNum >= nomoveup.size()) spriteNum = 0;
			image = nomoveup.get(spriteNum);
			break;
		}
		setLayoutX(screenX);
		setLayoutY(screenY);
		iv.setImage(image);

		shape.setTranslateX(12 + worldX - oldWorldX);
		shape.setTranslateY(30 + worldY - oldWorldY);

		if (System.getProperty("coll").equals("true"))
			shape.setVisible(true);
		else
			shape.setVisible(false);


		gp.getBuildings().forEach(b -> {
			if (b.collides(this)) {
				worldX = oldWorldX;
				worldY = oldWorldY;
			}
		});

		gp.getNpcs().forEach(b -> {
			if (b.collides(this)) {
				worldX = oldWorldX;
				worldY = oldWorldY;
			}
		});

		if (gp.getTileM().collides(this)) {
			worldX = oldWorldX;
			worldY = oldWorldY;
		}

		oldWorldX = worldX;
		oldWorldY = worldY;

		//		shape.setTranslateX(0);
		//		shape.setTranslateY(0);

	}
}
