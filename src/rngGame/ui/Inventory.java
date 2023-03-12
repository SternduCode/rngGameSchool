package rngGame.ui;

import java.util.Arrays;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import rngGame.main.GamePanel;
import rngGame.stats.*;
import rngGame.tile.ImgUtil;



// TODO: Auto-generated Javadoc
/**
 * The Class Inventory.
 */
public class Inventory extends Pane {

	/**
	 * The Enum Tab.
	 */
	private enum Tab {

		/** The potion. */
		POTION,
		/** The monster. */
		MONSTER,
		/** The use. */
		USE,
		/** The armor. */
		ARMOR,
		/** The key. */
		KEY;
	}

	/** The potion array. */
	private final Potion[] potionArray = new Potion[40];

	/** The gear array. */
	private final Item[] gearArray = new Item[40];

	/** The use array. */
	private final Use[] useArray = new Use[40];

	/** The key array. */
	private final Key[] keyArray = new Key[40];

	/** The demon array. */
	private final Demon[] demonArray = new Demon[40];

	/** The current tab. */
	private Tab currentTab = Tab.POTION;


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
			currentTab = Tab.POTION;
			moveFromArrayToView();
		});

		armorbutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton1);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton2);
			currentTab = Tab.ARMOR;
			moveFromArrayToView();
		});

		usebutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton1);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton2);
			currentTab = Tab.USE;
			moveFromArrayToView();
		});

		keybutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton1);
			idkbutton.setImage(idkButton2);
			currentTab = Tab.KEY;
			moveFromArrayToView();
		});

		idkbutton.setOnMouseReleased(me -> {
			potionbutton.setImage(potionButton2);
			armorbutton.setImage(armorButton2);
			usebutton.setImage(useButton2);
			keybutton.setImage(keyButton2);
			idkbutton.setImage(idkButton1);
			currentTab = Tab.MONSTER;
			moveFromArrayToView();
		});


	}

	/**
	 * Move from array to view.
	 */
	private void moveFromArrayToView() {
		Item[] data = switch (currentTab) {
			case POTION -> potionArray;
			case ARMOR -> gearArray;
			case KEY -> keyArray;
			case MONSTER -> null;
			case USE -> useArray;
		};
		if (data == null) {
			// TODO monster
		} else {
			int k = 0;
			System.out.println(data + " " + Arrays.toString(data));
			for (int j = 0; j < invSlots[0].length; j++) for (ImageView[] invSlot : invSlots) {
				System.out.println(data[k]);
				if (data[k] != null) invSlot[j].setImage(data[k].getT1());
				else invSlot[j].setImage(null);
				k++;
			}
		}
	}

	/**
	 * End show.
	 */
	public void endShow() {
		setVisible(false);
		setDisable(true);
	}

	/**
	 * Find first null.
	 *
	 * @param itemArray the item array
	 * @return the int
	 */
	public int findFirstNull(Item[] itemArray) {
		for(int i = 0; i < itemArray.length; i++) if(itemArray[i]==null) return i;
		return -1;
	}

	/**
	 * Item to inventory.
	 *
	 * @param item the item
	 */
	public void itemToInventory(Item item) {
		System.out.println(item);
		if (item instanceof Potion p1) {
			int x = findFirstNull(potionArray);
			System.out.println(x);
			if(x != -1) potionArray[x] = p1;

		} else if(item instanceof Harnish || item instanceof Helmet || item instanceof Pants || item instanceof Sword) {
			int x = findFirstNull(gearArray);
			if(x != -1) gearArray[x] = item;

		} else if(item instanceof Use u1) {
			int x = findFirstNull(useArray);
			if(x != -1) useArray[x] = u1;
		} else if(item instanceof Key k1) {
			int x = findFirstNull(keyArray);
			if(x != -1) keyArray[x] = k1;

		} else System.err.println("UNEXPECTED TYPE OF ITEM ABTREIBUNG FEHLGESCHLAGEN!!!");
	}

	/**
	 * Show.
	 */
	public void show() {
		moveFromArrayToView();
		setVisible(true);
		setDisable(false);
	}

}
