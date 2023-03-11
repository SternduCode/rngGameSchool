package rngGame.ui;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import rngGame.main.GamePanel;
import rngGame.stats.Demon;
import rngGame.stats.Harnish;
import rngGame.stats.Helmet;
import rngGame.stats.Item;
import rngGame.stats.Key;
import rngGame.stats.Pants;
import rngGame.stats.Potion;
import rngGame.stats.Sword;
import rngGame.stats.Use;
import rngGame.tile.ImgUtil;



// TODO: Auto-generated Javadoc
/**
 * The Class Inventory.
 */
public class Inventory extends Pane {

	private Potion[] potionArray = new Potion[30];
	
	private Item[] gearArray = new Item[30];
	
	private Use[] useArray = new Use[30];
	
	private Key[] keyArray = new Key[30];
	
	private Demon[] demonArray = new Demon[30];
	
	
	
	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The inv backround. */
	private final ImageView invBackround;

	/** The aus xb. */
	private final Button ausXb;

	/** The inv slots. */
	private final ImageView[][] invSlots = new ImageView[10][4];

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

		Pane p = new Pane();
		p.setLayoutX(8 * gamepanel.getScalingFactorX());
		p.setLayoutY(268 * gamepanel.getScalingFactorY());
		for (int i = 0; i < 620; i += 62)
			for (int j = 0; j < 247; j += 62) {
				ImageView iv = new ImageView();
				iv.setLayoutX(i * gamepanel.getScalingFactorX());
				iv.setLayoutY(j * gamepanel.getScalingFactorY());
				invSlots[i / 62][j / 62] = iv;
				p.getChildren().add(iv);
			}


		getChildren().add(invBackround);
		getChildren().add(ausXb);

		getChildren().add(potionbutton);
		getChildren().add(armorbutton);
		getChildren().add(usebutton);
		getChildren().add(keybutton);
		getChildren().add(idkbutton);

		getChildren().add(p);

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
	
	public void itemToInventory(Item item) {
		if (item instanceof Potion p1) {
			int x = findFirstNull(potionArray);
			if(x != -1) {
				potionArray[x] = p1;
			}
			
		} else if(item instanceof Harnish || item instanceof Helmet || item instanceof Pants || item instanceof Sword) {
			int x = findFirstNull(gearArray);
			if(x != -1) {
				gearArray[x] = item;
			}
			
		} else if(item instanceof Use u1) {
			int x = findFirstNull(useArray);
			if(x != -1) {
				useArray[x] = u1;
			}
		} else if(item instanceof Key k1) {
			int x = findFirstNull(keyArray);
			if(x != -1) {
				keyArray[x] = k1;
			}
			
		} else {
			System.err.println("UNEXPECTED TYPE OF ITEM ABTREIBUNG FEHLGESCHLAGEN!!!");
		}
	}
	
	public int findFirstNull(Item[] itemArray) {
		for(int i = 0; i < itemArray.length; i++) {
			if(itemArray[i]==null) {
				return i;
			} 
		}
		return -1;
	}

}
