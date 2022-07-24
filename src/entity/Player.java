package entity;

import java.util.ArrayList;
import java.util.Map.Entry;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rngGAME.*;
import tile.ImgUtil;

public class Player extends Entity implements Collidable {

	private final SpielPanel gp;
	private final Input keyH;
	private final double fps = 7.5;

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

		screenX = gp.SpielLaenge/2 - gp.Bg/2;
		screenY = gp.SpielHoehe/2 - gp.Bg/2;

		setDisable(true);

		setDefaultValues();
		getPlayerImage();

		iv = new ImageView();

		shape = new Rectangle(48, 48);
		shape.setFill(Color.color(1, 0, 1, 0.75));

		getChildren().addAll(iv, shape);
	}

	public void getPlayerImage() {

		try {
			Image up = new Image(getClass().getResourceAsStream("/res/player/LaufenHochL.png"));

			for (int i = 0; i < up.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(up.getPixelReader(), i, 0, 32, 32);
				this.up.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image upl = new Image(getClass().getResourceAsStream("/res/player/LaufenHochL.png"));

			for (int i = 0; i < upl.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(upl.getPixelReader(), i, 0, 32, 32);
				this.upl.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image down = new Image(getClass().getResourceAsStream("/res/player/LaufenRunter.png"));

			for (int i = 0; i < down.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(down.getPixelReader(), i, 0, 32, 32);
				this.down.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image downl = new Image(getClass().getResourceAsStream("/res/player/LaufenRunterL.png"));

			for (int i = 0; i < downl.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(downl.getPixelReader(), i, 0, 32, 32);
				this.downl.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image right = new Image(getClass().getResourceAsStream("/res/player/LaufenRechts.png"));

			for (int i = 0; i < right.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(right.getPixelReader(), i, 0, 32, 32);
				this.right.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image left = new Image(getClass().getResourceAsStream("/res/player/LaufenLinks.png"));

			for (int i = 0; i < left.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(left.getPixelReader(), i, 0, 32, 32);
				this.left.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image nomover = new Image(getClass().getResourceAsStream("/res/player/Stehen.png"));

			for (int i = 0; i < nomover.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(nomover.getPixelReader(), i, 0, 32, 32);
				this.nomover.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}

			Image nomovel = new Image(getClass().getResourceAsStream("/res/player/Stehen2.png"));

			for (int i = 0; i < nomovel.getWidth(); i += 32) {
				WritableImage wi = new WritableImage(nomovel.getPixelReader(), i, 0, 32, 32);
				this.nomovel.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), gp.Bg, gp.Bg));
			}


			//			up1 = new Image(getClass().getResourceAsStream("/Player/Character-Hoch-1.png"), gp.Bg, gp.Bg, false, false);
			//			up2 = new Image(getClass().getResourceAsStream("/Player/Character-Hoch-2.png"), gp.Bg, gp.Bg, false, false);
			//			down1 = new Image(getClass().getResourceAsStream("/Player/Character-Runter-1.png"), gp.Bg, gp.Bg, false,
			//					false);
			//			down2 = new Image(getClass().getResourceAsStream("/Player/Character-Runter-2.png"), gp.Bg, gp.Bg, false,
			//					false);
			//			left1 = new Image(getClass().getResourceAsStream("/Player/Character-Links-1.png"), gp.Bg, gp.Bg, false,
			//					false);
			//			left2 = new Image(getClass().getResourceAsStream("/Player/Character-Links-2.png"), gp.Bg, gp.Bg, false,
			//					false);
			//			right1 = new Image(getClass().getResourceAsStream("/Player/Character-Rechts-1.png"), gp.Bg, gp.Bg, false,
			//					false);
			//			right2 = new Image(getClass().getResourceAsStream("/Player/Character-Rechts-2.png"), gp.Bg, gp.Bg, false,
			//					false);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Rectangle getPoly() { return shape; }


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
		int oldWorldX = worldX;
		int oldWorldY = worldY;
		if (keyH.upPressed) {
			if (direction.equals("left") || direction.endsWith("L")) direction = "upL";
			else direction = "up";
			worldY -= speed;
		}
		else if (keyH.downPressed) {
			if (direction.equals("left") || direction.endsWith("L")) direction = "downL";
			else direction = "down";
			worldY += speed;
		}
		else if (keyH.leftPressed) {
			direction = "left";
			worldX -= speed;
		}
		else if (keyH.rightPressed) {
			direction = "right";
			worldX += speed;
		} else if (direction.equals("left") || direction.endsWith("L")) direction = "noneL";
		else direction = "none";

		int div = switch (direction) {
			case "up" -> up.size();
			case "upL" -> upl.size();
			case "down" -> down.size();
			case "downL" -> downl.size();
			case "left" -> left.size();
			case "right" -> right.size();
			case "none" -> nomover.size();
			case "noneL" -> nomovel.size();
			default -> 1;
		};

		if (div == 0) div = 1;

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
		}
		setLayoutX(screenX);
		setLayoutY(screenY);
		iv.setFitWidth(gp.Bg);
		iv.setFitHeight(gp.Bg);
		iv.setImage(image);

		shape.setTranslateX(worldX - this.oldWorldX);
		shape.setTranslateY(worldY - this.oldWorldY);

		if (System.getProperty("coll").equals("true"))
			shape.setVisible(true);
		else
			shape.setVisible(false);


		gp.getBuildings().forEach(b -> {
			if (b.collides(this)) {
				worldX = this.oldWorldX;
				worldY = this.oldWorldY;
			}
		});

		gp.getNpcs().forEach(b -> {
			if (b.collides(this)) {
				worldX = this.oldWorldX;
				worldY = this.oldWorldY;
			}
		});

		if (gp.getTileM().collides(this)) {
			worldX = this.oldWorldX;
			worldY = this.oldWorldY;
		}

		this.oldWorldX = worldX;
		this.oldWorldY = worldY;

		//		shape.setTranslateX(0);
		//		shape.setTranslateY(0);

	}
}
