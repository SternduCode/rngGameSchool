package rngGame.ui;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;



// TODO: Auto-generated Javadoc
/**
 * The Class Inventory.
 */
public class Inventory extends Pane {

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The inv backround. */
	private final ImageView invBackround;

	/** The aus xb. */
	private final Button ausXb;



	/** The potionbutton. */
	private final Button potionbutton;

	/** The armorbutton. */
	private final Button armorbutton;

	/** The usebutton. */
	private final Button usebutton;

	/** The keybutton. */
	private final Button keybutton;

	/** The idkbutton. */
	private final Button idkbutton;

	/**
	 * Instantiates a new inventory.
	 *
	 * @param gamepanel the gamepanel
	 * @param tabm the tabm
	 */
	public Inventory(GamePanel gamepanel, TabMenu tabm) {
		this.gamepanel = gamepanel;
		// invBackround
		invBackround	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/InvBackround.png"));

		//Xbutton
		Image ausX = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Xbutton.png");
		Image ausX2 = ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/XbuttonC.png");
		ausXb = new Button(ausX);

		//PotionButtonFeld
		Image potionButton2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/PotionButtonClosed.png");
		Image potionButton1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/PotionButtonOpen.png");
		potionbutton = new Button(potionButton1);

		//ArmorButtonFeld
		Image armorButton2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/ArmorButtonClosed.png");
		Image armorButton1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/ArmorButtonOpen.png");
		armorbutton = new Button(armorButton2);

		//UseButtonFeld
		Image useButton2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/UseButtonClosed.png");
		Image useButton1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/UseButtonOpen.png");
		usebutton = new Button(useButton2);

		//KeyButtonFeld
		Image keyButton2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/KeyButtonClosed.png");
		Image keyButton1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/KeyButtonOpen.png");
		keybutton = new Button(keyButton2);

		//IdkButtonFeld
		Image idkButton2 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/IdkButtonClosed.png");
		Image idkButton1 = ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/IdkButtonOpen.png");
		idkbutton = new Button(idkButton2);


		getChildren().add(invBackround);
		getChildren().add(ausXb);

		getChildren().add(potionbutton);
		getChildren().add(armorbutton);
		getChildren().add(usebutton);
		getChildren().add(keybutton);
		getChildren().add(idkbutton);

		setVisible(false);


		ausXb.setOnMousePressed(me -> {
			ausXb.setImage(ausX2);
		});

		ausXb.setOnMouseReleased(me -> {
			ausXb.setImage(ausX);
			tabm.closeTabm(true);
			new Thread(()->{
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				endShow();
			}).start();

		});


		potionbutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton1);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton2);
		});

		armorbutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton1);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton2);
		});

		usebutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton1);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton2);
		});

		keybutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton1);
			idkbutton.setImage(idkButton2);
		});

		idkbutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton1);
		});


	}

	/**
	 * End show.
	 */
	public void endShow() {
		setVisible(false);
		setDisable(true);
	}

	/**
	 * Show.
	 */
	public void show() {
		setVisible(true);
		setDisable(false);
	}

}
