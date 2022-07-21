package entity;

import java.util.ArrayList;
import javafx.scene.image.*;
import rngGAME.*;

public class Player extends Entity {

	SpielPanel gp;
	Input keyH;

	public final int screenX;
	public final int screenY;


	public Player(SpielPanel gp2, Input keyH2) {

		gp = gp2;
		keyH = keyH2;

		up = new ArrayList<>();
		down = new ArrayList<>();
		left = new ArrayList<>();
		right = new ArrayList<>();
		nomovel = new ArrayList<>();
		nomover = new ArrayList<>();

		screenX = gp.SpielLaenge/2 - gp.Bg/2;
		screenY = gp.SpielHoehe/2 - gp.Bg/2;

		setDisable(true);

		setDefaultValues();
		getPlayerImage();
	}

	public void getPlayerImage() {

		try {
			Image up = new Image(getClass().getResourceAsStream("/Player/LaufenHoch.png"));

			for (int i = 0; i < up.getWidth(); i += 32)
				this.up.add(new WritableImage(up.getPixelReader(), i, 0, 32, 32));

			Image down = new Image(getClass().getResourceAsStream("/Player/LaufenRunter.png"));

			for (int i = 0; i < down.getWidth(); i += 32)
				this.down.add(new WritableImage(down.getPixelReader(), i, 0, 32, 32));

			Image right = new Image(getClass().getResourceAsStream("/Player/LaufenRechts.png"));

			for (int i = 0; i < right.getWidth(); i += 32)
				this.right.add(new WritableImage(right.getPixelReader(), i, 0, 32, 32));

			Image left = new Image(getClass().getResourceAsStream("/Player/LaufenLinks.png"));

			for (int i = 0; i < left.getWidth(); i += 32)
				this.left.add(new WritableImage(left.getPixelReader(), i, 0, 32, 32));

			Image nomover = new Image(getClass().getResourceAsStream("/Player/Stehen.png"));

			for (int i = 0; i < nomover.getWidth(); i += 32)
				this.nomover.add(new WritableImage(nomover.getPixelReader(), i, 0, 32, 32));

			Image nomovel = new Image(getClass().getResourceAsStream("/Player/Stehen2.png"));

			for (int i = 0; i < nomovel.getWidth(); i += 32)
				this.nomovel.add(new WritableImage(nomovel.getPixelReader(), i, 0, 32, 32));

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

	public void setDefaultValues() {

		worldX = gp.Bg * 13;
		worldY = gp.Bg * 37;
		speed = 4;
		direction = "down";
	}


	public void update() {
		if (keyH.upPressed) {
			direction = "up";
			worldY -= speed;
		}
		else if (keyH.downPressed) {
			direction = "down";
			worldY += speed;
		}
		else if (keyH.leftPressed) {
			direction = "left";
			worldX -= speed;
		}
		else if (keyH.rightPressed) {
			direction = "right";
			worldX += speed;
		} else if (direction.equals("left") || direction.equals("lnone")) direction = "lnone";
		else direction = "rnone";

		int div = switch (direction) {
			case "up" -> up.size();
			case "down" -> down.size();
			case "left" -> left.size();
			case "right" -> right.size();
			default -> 1;
		};

		spriteCounter++;
		if (spriteCounter > 30 / div) {
			spriteNum++;
			spriteCounter = 0;
		}

		Image image = null;

		switch(direction) {
			case "up":
				if (spriteNum >= up.size()) spriteNum = 0;
				image = up.get(spriteNum);
				break;
			case "down":
				if (spriteNum >= down.size()) spriteNum = 0;
				image = down.get(spriteNum);
				break;
			case "left":
				if (spriteNum >= left.size()) spriteNum = 0;
				image = left.get(spriteNum);
				break;
			case "right":
				if (spriteNum >= right.size()) spriteNum = 0;
				image = right.get(spriteNum);
				break;
			case "rnone":
				if (spriteNum >= nomover.size()) spriteNum = 0;
				image = nomover.get(spriteNum);
				break;
			case "lnone":
				if (spriteNum >= nomovel.size()) spriteNum = 0;
				image = nomovel.get(spriteNum);
				break;
		}
		setLayoutX(screenX);
		setLayoutY(screenY);
		setFitWidth(gp.Bg);
		setFitHeight(gp.Bg);
		setImage(image);
	}
}
