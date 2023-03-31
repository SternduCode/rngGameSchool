package rngGame.ui;

import java.io.FileNotFoundException;
import java.util.Arrays;

import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import rngGame.main.*;
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

	/** The Constant currentDemonIndex. */
	private final int currentDemonIndex = 0;

	/** The potion array. */
	private final Potion[] potionArray = new Potion[40];

	/** The gear array. */
	private final Gear[] gearArray = new Gear[40];

	/** The use array. */
	private final Use[] useArray = new Use[40];

	/** The key array. */
	private final Key[] keyArray = new Key[40];

	/** The demon array. */
	private final Demon[] demonArray = new Demon[40];

	/** The current demon array. */
	private final Demon[] currentDemonArray = new Demon[5];

	/** The current tab. */
	private Tab currentTab = Tab.POTION;

	/** The name pane. */
	private Pane namePane;

	/** The element view. */
	private ImageView nameView, textBackroundCT, elementView, eIconView, itemOverlay, transp, itemSureBackround, backButton, applyButton, removeButton;

	/** The status. */
	private Pane status;

	/** The dgc view. */
	private ImageView hpView, atkView, resView, dgcView;

	/** The lvl view. */
	private ImageView expBar, expText, lvlView;

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The inv backround. */
	private ImageView invBackround;

	/** The aus xb. */
	private Button ausXb;

	/** The inv slots. */
	private final ImageView[][] invSlots = new ImageView[10][4];

	/** The Item 4 slots. */
	private final ImageView[] Item4Slots = new ImageView[4];

	/** The potionbutton. */
	private Button potionbutton;

	/** The armorbutton. */
	private Button armorbutton;

	/** The usebutton. */
	private Button usebutton;

	/** The keybutton. */
	private Button keybutton;

	/** The idkbutton. */
	private Button idkbutton;

	/** The tabm. */
	private final TabMenu tabm;

	/**
	 * Instantiates a new inventory.
	 *
	 * @param gamepanel the gamepanel
	 * @param tabm      the tabm
	 *
	 * @throws FileNotFoundException the file not found exception
	 */
	public Inventory(GamePanel gamepanel, TabMenu tabm) throws FileNotFoundException {
		this.gamepanel	= gamepanel;
		this.tabm		= tabm;
		Input.getInstance().setKeyHandler("Demons", mod -> {
			init();
		}, KeyCode.M, false);
		init();

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
				if (data[k] != null) invSlot[j].setImage(data[k].getImage(gamepanel));
				else invSlot[j].setImage(null);
				k++;
			}
		}
		for (int i = 0; i < Item4Slots.length; i++)
			if (currentDemonArray[currentDemonIndex] != null && currentDemonArray[currentDemonIndex].getItem4List()[i] != null)
				Item4Slots[i].setImage(currentDemonArray[currentDemonIndex].getItem4List()[i].getImage(gamepanel));
			else Item4Slots[i].setImage(null);

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
	 *
	 * @return the int
	 */
	public int findFirstNull(Item[] itemArray) {
		for (int i = 0; i < itemArray.length; i++) if (itemArray[i] == null) return i;
		return -1;

	}

	/**
	 * Gets the current demon.
	 *
	 * @return the current demon
	 */
	public Demon getCurrentDemon() { return currentDemonArray[currentDemonIndex]; }

	/**
	 * Give item 2 monster.
	 *
	 * @param idx the idx
	 */
	public void giveItem2Monster(int idx) {
		Gear g = gearArray[idx];
		if (gearArray[idx] instanceof Helmet) {
			gearArray[idx]											= currentDemonArray[currentDemonIndex].getItem4List()[0];
			currentDemonArray[currentDemonIndex].getItem4List()[0]	= g;
		} else if (gearArray[idx] instanceof Harnish) {
			gearArray[idx]											= currentDemonArray[currentDemonIndex].getItem4List()[1];
			currentDemonArray[currentDemonIndex].getItem4List()[1]	= g;
		} else if (gearArray[idx] instanceof Pants) {
			gearArray[idx]											= currentDemonArray[currentDemonIndex].getItem4List()[2];
			currentDemonArray[currentDemonIndex].getItem4List()[2]	= g;
		} else if (gearArray[idx] instanceof Sword) {
			gearArray[idx]											= currentDemonArray[currentDemonIndex].getItem4List()[3];
			currentDemonArray[currentDemonIndex].getItem4List()[3]	= g;
		}

		int i = findFirstNull(gearArray);

		loop: while (i < gearArray.length) {
			for (int j = i; j < gearArray.length; j++) if (gearArray[j] != null) {
				gearArray[i]	= gearArray[j];
				gearArray[j]	= null;
				i				= j;
				continue loop;
			}
			break loop;
		}

		moveFromArrayToView();

		Image hpText = Text.getInstance().convertText("HP:" + currentDemonArray[currentDemonIndex].getHp(), 48);
		hpText = ImgUtil.resizeImage(
				hpText, (int) hpText.getWidth(), (int) hpText.getHeight(),
				(int) (hpText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (hpText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image atkText = Text.getInstance().convertText("ATK:" + currentDemonArray[currentDemonIndex].getAtk(), 48);
		atkText = ImgUtil.resizeImage(
				atkText, (int) atkText.getWidth(), (int) atkText.getHeight(),
				(int) (atkText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (atkText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image resText = Text.getInstance().convertText(String.format("RES:%.2f%%", currentDemonArray[currentDemonIndex].getRes()), 48);
		resText = ImgUtil.resizeImage(
				resText, (int) resText.getWidth(), (int) resText.getHeight(),
				(int) (resText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (resText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image dgcText = Text.getInstance().convertText(String.format("DGC:%.2f%%", currentDemonArray[currentDemonIndex].getDgc()), 48);
		dgcText = ImgUtil.resizeImage(
				dgcText, (int) dgcText.getWidth(), (int) dgcText.getHeight(),
				(int) (dgcText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (dgcText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image nameText = Text.getInstance().convertText("" + currentDemonArray[currentDemonIndex].getMobName(), 48);
		nameText = ImgUtil.resizeImage(
				nameText, (int) nameText.getWidth(), (int) nameText.getHeight(),
				(int) (nameText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (nameText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image expMaxText = Text.getInstance()
				.convertText(currentDemonArray[currentDemonIndex].getCurrentExp() + ":" + currentDemonArray[currentDemonIndex].getMaxExp(), 32);
		expMaxText = ImgUtil.resizeImage(
				expMaxText, (int) expMaxText.getWidth(), (int) expMaxText.getHeight(),
				(int) (expMaxText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (expMaxText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image lvlText = Text.getInstance().convertText("lvl:" + currentDemonArray[currentDemonIndex].getLvl(), 48);
		lvlText = ImgUtil.resizeImage(
				lvlText, (int) lvlText.getWidth(), (int) lvlText.getHeight(),
				(int) (lvlText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (lvlText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		hpView.setImage(hpText);
		atkView.setImage(atkText);
		resView.setImage(resText);
		dgcView.setImage(dgcText);
		nameView.setImage(nameText);
		expText.setImage(expMaxText);
		lvlView.setImage(lvlText);

	}

	/**
	 * Inits the.
	 */
	public void init() {
		getChildren().clear();

		status		= new Pane();
		namePane	= new Pane();

		// invBackround
		invBackround		= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/InvBackround.png"));
		textBackroundCT		= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/invNameTitle.png"));
		elementView			= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementFire.png"));
		itemOverlay			= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/ItemAuswahlOverlay.png"));
		transp				= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/blackTransparent.png"));
		itemSureBackround	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/itemSureBackround.png"));

		/////////////

		Demon m1 = gamepanel.getMobRans().get(0).MobGen();
		m1.setLvl(140);
		m1.setCurrentExp(m1.getMaxExp() - 1);
		currentDemonArray[currentDemonIndex] = m1;

		Sword g1 = new Sword(Rarity.VOID);
		Harnish g2 = new Harnish(Rarity.VOID);
		Pants g3 = new Pants(Rarity.VOID);
		Helmet g4 = new Helmet(Rarity.VOID);
		// giveItem2Monster(g1);
		// giveItem2Monster(g2);
		// giveItem2Monster(g3);
		// giveItem2Monster(g4);
		itemToInventory(g1);
		itemToInventory(g2);
		itemToInventory(g3);
		itemToInventory(g4);

		/////////////

		Image hpText = Text.getInstance().convertText("HP:" + currentDemonArray[currentDemonIndex].getHp(), 48);
		hpText = ImgUtil.resizeImage(
				hpText, (int) hpText.getWidth(), (int) hpText.getHeight(),
				(int) (hpText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (hpText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image atkText = Text.getInstance().convertText("ATK:" + currentDemonArray[currentDemonIndex].getAtk(), 48);
		atkText = ImgUtil.resizeImage(
				atkText, (int) atkText.getWidth(), (int) atkText.getHeight(),
				(int) (atkText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (atkText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image resText = Text.getInstance().convertText(String.format("RES:%.2f%%", currentDemonArray[currentDemonIndex].getRes()), 48);
		resText = ImgUtil.resizeImage(
				resText, (int) resText.getWidth(), (int) resText.getHeight(),
				(int) (resText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (resText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image dgcText = Text.getInstance().convertText(String.format("DGC:%.2f%%", currentDemonArray[currentDemonIndex].getDgc()), 48);
		dgcText = ImgUtil.resizeImage(
				dgcText, (int) dgcText.getWidth(), (int) dgcText.getHeight(),
				(int) (dgcText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (dgcText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image nameText = Text.getInstance().convertText("" + currentDemonArray[currentDemonIndex].getMobName(), 48);
		nameText = ImgUtil.resizeImage(
				nameText, (int) nameText.getWidth(), (int) nameText.getHeight(),
				(int) (nameText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (nameText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image expMaxText = Text.getInstance()
				.convertText(currentDemonArray[currentDemonIndex].getCurrentExp() + ":" + currentDemonArray[currentDemonIndex].getMaxExp(), 32);
		expMaxText = ImgUtil.resizeImage(
				expMaxText, (int) expMaxText.getWidth(), (int) expMaxText.getHeight(),
				(int) (expMaxText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (expMaxText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		Image lvlText = Text.getInstance().convertText("lvl:" + currentDemonArray[currentDemonIndex].getLvl(), 48);
		lvlText = ImgUtil.resizeImage(
				lvlText, (int) lvlText.getWidth(), (int) lvlText.getHeight(),
				(int) (lvlText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
				(int) (lvlText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

		// Xbutton
		Image	ausX	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Xbutton.png");
		Image	ausX2	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/XbuttonC.png");
		ausXb = new Button(ausX);

		// backbutton
		Image	back1	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterBackbutton.png");
		Image	back2	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/MonsterBackbuttonC.png");
		backButton = new ImageView(back1);

		Image	remove1	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Removeb1.png");
		Image	remove2	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/Removeb2.png");
		removeButton = new ImageView(remove1);

		// backbutton
		Image	apply1	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/ApplyButton1.png");
		Image	apply2	= ImgUtil.getScaledImage(gamepanel, "./res/Contractstuff/ApplyButton2.png");
		applyButton = new ImageView(apply1);

		// PotionButtonFeld
		Image	potionButton2	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/PotionButtonClosed.png");
		Image	potionButton1	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/PotionButtonOpen.png");
		potionbutton = new Button(potionButton1);

		// ArmorButtonFeld
		Image	armorButton2	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/ArmorButtonClosed.png");
		Image	armorButton1	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/ArmorButtonOpen.png");
		armorbutton = new Button(armorButton2);

		// UseButtonFeld
		Image	useButton2	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/UseButtonClosed.png");
		Image	useButton1	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/UseButtonOpen.png");
		usebutton = new Button(useButton2);

		// KeyButtonFeld
		Image	keyButton2	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/KeyButtonClosed.png");
		Image	keyButton1	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/KeyButtonOpen.png");
		keybutton = new Button(keyButton2);

		// IdkButtonFeld
		Image	idkButton2	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/IdkButtonClosed.png");
		Image	idkButton1	= ImgUtil.getScaledImage(gamepanel, "./res/gui/InvButtonFolder/IdkButtonOpen.png");
		idkbutton = new Button(idkButton2);

		Pane	p			= new Pane();
		Pane	p2			= new Pane();
		Pane	itemStuff	= new Pane();

		p.setLayoutX(8 * gamepanel.getVgp().getScalingFactorX());
		p.setLayoutY(268 * gamepanel.getVgp().getScalingFactorY());
		for (int i = 0; i < 620; i += 62)
			for (int j = 0; j < 247; j += 62) {
				ImageView iv = new ImageView();
				iv.setLayoutX(i * gamepanel.getVgp().getScalingFactorX());
				iv.setLayoutY(j * gamepanel.getVgp().getScalingFactorY());
				invSlots[i / 62][j / 62] = iv;
				int _i = i;
				int _j = j;

				iv.setOnMousePressed(me -> {
					itemOverlay.setLayoutX( (_i + 8) * gamepanel.getVgp().getScalingFactorX());
					itemOverlay.setLayoutY( (_j + 268) * gamepanel.getVgp().getScalingFactorY());
					itemOverlay.setVisible(true);
				});

				iv.setOnMouseReleased(me -> {
					ImageView itemShowcase = new ImageView(iv.getImage());

					itemShowcase.setLayoutX(329 * gamepanel.getVgp().getScalingFactorX());
					itemShowcase.setLayoutY(45 * gamepanel.getVgp().getScalingFactorY());
					backButton.setLayoutX(302 * gamepanel.getVgp().getScalingFactorX());
					backButton.setLayoutY( (125 + 64) * gamepanel.getVgp().getScalingFactorY());
					applyButton.setLayoutX(302 * gamepanel.getVgp().getScalingFactorX());
					applyButton.setLayoutY(125 * gamepanel.getVgp().getScalingFactorY());

					itemStuff.getChildren().clear();
					itemStuff.getChildren().add(itemShowcase);
					itemStuff.getChildren().addAll(backButton, applyButton);
					transp.setVisible(true);
					itemSureBackround.setVisible(true);
					itemStuff.setVisible(true);

					Item[] itemTestArray = switch (currentTab) {
						case POTION -> potionArray;
						case ARMOR -> gearArray;
						case KEY -> null;
						case MONSTER -> null;
						case USE -> null;
					};
					if (itemTestArray[_j / 62 * 10 + _i / 62] instanceof Potion pp) {
						ImageView hpView2, rarityView2;

						Image itemhpText = Text.getInstance().convertText("HP:" + pp.getHp(), 48);
						itemhpText = ImgUtil.resizeImage(
								itemhpText, (int) itemhpText.getWidth(), (int) itemhpText.getHeight(),
								(int) (itemhpText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itemhpText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image itemRarityText = Text.getInstance().convertText("Rarity:" + pp.getRarity(), 48);
						itemRarityText = ImgUtil.resizeImage(
								itemRarityText, (int) itemRarityText.getWidth(), (int) itemRarityText.getHeight(),
								(int) (itemRarityText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itemRarityText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						applyButton.setOnMouseReleased(me2 -> {
							applyButton.setImage(apply1);
							transp.setVisible(false);
							itemSureBackround.setVisible(false);
							itemStuff.setVisible(false);
						});

						hpView2 = new ImageView(itemhpText);

						rarityView2 = new ImageView(itemRarityText);

						hpView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						hpView2.setLayoutY( (16 + 48) * gamepanel.getVgp().getScalingFactorY());
						rarityView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						rarityView2.setLayoutY(16 * gamepanel.getVgp().getScalingFactorY());

						itemStuff.getChildren().addAll(hpView2, rarityView2);

					} else {
						ImageView hpView2, atkView2, resView2, dgcView2, rarityView2;

						Gear g			= (Gear) itemTestArray[_j / 62 * 10 + _i / 62];
						Image itemhpText = Text.getInstance().convertText("HP:" + g.getHp(), 48);
						itemhpText = ImgUtil.resizeImage(
								itemhpText, (int) itemhpText.getWidth(), (int) itemhpText.getHeight(),
								(int) (itemhpText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itemhpText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image itematkText = Text.getInstance().convertText("ATK:" + g.getAtk(), 48);
						itematkText = ImgUtil.resizeImage(
								itematkText, (int) itematkText.getWidth(), (int) itematkText.getHeight(),
								(int) (itematkText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itematkText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image itemresText = Text.getInstance().convertText(String.format("RES:%.2f%%", g.getRes()), 48);
						itemresText = ImgUtil.resizeImage(
								itemresText, (int) itemresText.getWidth(), (int) itemresText.getHeight(),
								(int) (itemresText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itemresText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image itemdgcText = Text.getInstance().convertText(String.format("DGC:%.2f%%", g.getDgc()), 48);
						itemdgcText = ImgUtil.resizeImage(
								itemdgcText, (int) itemdgcText.getWidth(), (int) itemdgcText.getHeight(),
								(int) (itemdgcText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itemdgcText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image itemRarityText = Text.getInstance().convertText("Rarity:" + g.getRarity().toString().replace('_', ' '), 48);
						itemRarityText = ImgUtil.resizeImage(
								itemRarityText, (int) itemRarityText.getWidth(), (int) itemRarityText.getHeight(),
								(int) (itemRarityText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (itemRarityText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						applyButton.setOnMouseReleased(me2 -> {
							applyButton.setImage(apply1);
							giveItem2Monster(_j / 62 * 10 + _i / 62);
							transp.setVisible(false);
							itemSureBackround.setVisible(false);
							itemStuff.setVisible(false);
						});

						hpView2	= new ImageView(itemhpText);
						atkView2 = new ImageView(itematkText);
						resView2 = new ImageView(itemresText);
						dgcView2 = new ImageView(itemdgcText);
						rarityView2 = new ImageView(itemRarityText);

						rarityView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						rarityView2.setLayoutY(16 * gamepanel.getVgp().getScalingFactorY());

						hpView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						hpView2.setLayoutY( (16 + 48) * gamepanel.getVgp().getScalingFactorY());

						atkView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						atkView2.setLayoutY( (16 + 96) * gamepanel.getVgp().getScalingFactorY());

						resView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						resView2.setLayoutY( (16 + 144) * gamepanel.getVgp().getScalingFactorY());

						dgcView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
						dgcView2.setLayoutY( (16 + 192) * gamepanel.getVgp().getScalingFactorY());

						itemStuff.getChildren().addAll(hpView2, atkView2, resView2, dgcView2, rarityView2);
					}

					backButton.setOnMousePressed(me2 -> {
						backButton.setImage(back2);
					});

					backButton.setOnMouseReleased(me2 -> {
						backButton.setImage(back1);
						transp.setVisible(false);
						itemSureBackround.setVisible(false);
						itemStuff.setVisible(false);
					});

					applyButton.setOnMousePressed(me2 -> {
						applyButton.setImage(apply2);
					});
				});
				p.getChildren().add(iv);
			}

		for (int i = 0; i < Item4Slots.length; i++) {
			int _i = i;
			ImageView iv = new ImageView();
			if (currentDemonArray[currentDemonIndex] != null && currentDemonArray[currentDemonIndex].getItem4List()[i] != null) {
				Image iv2 = currentDemonArray[currentDemonIndex].getItem4List()[i].getImage(gamepanel);
				iv.setImage(iv2);
			}
			Item4Slots[i] = iv;
			iv.setOnMousePressed(me -> {
				itemOverlay.setLayoutX(6 * gamepanel.getVgp().getScalingFactorX());
				itemOverlay.setLayoutY(iv.getLayoutY() + 6 * gamepanel.getVgp().getScalingFactorY());
				itemOverlay.setVisible(true);
			});

			iv.setOnMouseReleased(me -> {
				ImageView itemShowcase = new ImageView(iv.getImage());

				itemShowcase.setLayoutX(329 * gamepanel.getVgp().getScalingFactorX());
				itemShowcase.setLayoutY(45 * gamepanel.getVgp().getScalingFactorY());
				backButton.setLayoutX(302 * gamepanel.getVgp().getScalingFactorX());
				backButton.setLayoutY( (125 + 64) * gamepanel.getVgp().getScalingFactorY());
				removeButton.setLayoutX(302 * gamepanel.getVgp().getScalingFactorX());
				removeButton.setLayoutY(125 * gamepanel.getVgp().getScalingFactorY());

				itemStuff.getChildren().clear();
				itemStuff.getChildren().add(itemShowcase);
				itemStuff.getChildren().addAll(backButton,removeButton);
				transp.setVisible(true);
				itemSureBackround.setVisible(true);
				itemStuff.setVisible(true);


				ImageView hpView2, atkView2, resView2, dgcView2, rarityView2;

				Gear g	= currentDemonArray[currentDemonIndex].getItem4List()[_i];
				Image itemhpText = Text.getInstance().convertText("HP:" + g.getHp(), 48);
				itemhpText = ImgUtil.resizeImage(
						itemhpText, (int) itemhpText.getWidth(), (int) itemhpText.getHeight(),
						(int) (itemhpText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
						(int) (itemhpText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

				Image itematkText = Text.getInstance().convertText("ATK:" + g.getAtk(), 48);
				itematkText = ImgUtil.resizeImage(
						itematkText, (int) itematkText.getWidth(), (int) itematkText.getHeight(),
						(int) (itematkText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
						(int) (itematkText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

				Image itemresText = Text.getInstance().convertText(String.format("RES:%.2f%%", g.getRes()), 48);
				itemresText = ImgUtil.resizeImage(
						itemresText, (int) itemresText.getWidth(), (int) itemresText.getHeight(),
						(int) (itemresText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
						(int) (itemresText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

				Image itemdgcText = Text.getInstance().convertText(String.format("DGC:%.2f%%", g.getDgc()), 48);
				itemdgcText = ImgUtil.resizeImage(
						itemdgcText, (int) itemdgcText.getWidth(), (int) itemdgcText.getHeight(),
						(int) (itemdgcText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
						(int) (itemdgcText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

				Image itemRarityText = Text.getInstance().convertText("Rarity:" + g.getRarity().toString().replace('_', ' '), 48);
				itemRarityText = ImgUtil.resizeImage(
						itemRarityText, (int) itemRarityText.getWidth(), (int) itemRarityText.getHeight(),
						(int) (itemRarityText.getWidth() * gamepanel.getVgp().getScalingFactorX()),
						(int) (itemRarityText.getHeight() * gamepanel.getVgp().getScalingFactorY()));

				hpView2	= new ImageView(itemhpText);
				atkView2 = new ImageView(itematkText);
				resView2 = new ImageView(itemresText);
				dgcView2 = new ImageView(itemdgcText);
				rarityView2 = new ImageView(itemRarityText);

				rarityView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
				rarityView2.setLayoutY(16 * gamepanel.getVgp().getScalingFactorY());

				hpView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
				hpView2.setLayoutY( (16 + 48) * gamepanel.getVgp().getScalingFactorY());

				atkView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
				atkView2.setLayoutY( (16 + 96) * gamepanel.getVgp().getScalingFactorY());

				resView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
				resView2.setLayoutY( (16 + 144) * gamepanel.getVgp().getScalingFactorY());

				dgcView2.setLayoutX(25 * gamepanel.getVgp().getScalingFactorX());
				dgcView2.setLayoutY( (16 + 192) * gamepanel.getVgp().getScalingFactorY());

				itemStuff.getChildren().addAll(hpView2, atkView2, resView2, dgcView2, rarityView2);


				backButton.setOnMousePressed(me2 -> {
					backButton.setImage(back2);
				});

				backButton.setOnMouseReleased(me2 -> {
					backButton.setImage(back1);
					transp.setVisible(false);
					itemSureBackround.setVisible(false);
					itemStuff.setVisible(false);
				});

				removeButton.setOnMousePressed(me2 -> {
					removeButton.setImage(remove2);
				});

				removeButton.setOnMouseReleased(me2 -> {
					removeButton.setImage(remove1);
					int z = findFirstNull(gearArray);
					if(z != -1) {
						gearArray[z] = currentDemonArray[currentDemonIndex].getItem4List()[_i];
						currentDemonArray[currentDemonIndex].getItem4List()[_i] = null;
						moveFromArrayToView();
						Image hpText1 = Text.getInstance().convertText("HP:" + currentDemonArray[currentDemonIndex].getHp(), 48);
						hpText1 = ImgUtil.resizeImage(
								hpText1, (int) hpText1.getWidth(), (int) hpText1.getHeight(),
								(int) (hpText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (hpText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image atkText1 = Text.getInstance().convertText("ATK:" + currentDemonArray[currentDemonIndex].getAtk(), 48);
						atkText1 = ImgUtil.resizeImage(
								atkText1, (int) atkText1.getWidth(), (int) atkText1.getHeight(),
								(int) (atkText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (atkText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image resText1 = Text.getInstance().convertText(String.format("RES:%.2f%%", currentDemonArray[currentDemonIndex].getRes()),
								48);
						resText1 = ImgUtil.resizeImage(
								resText1, (int) resText1.getWidth(), (int) resText1.getHeight(),
								(int) (resText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (resText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image dgcText1 = Text.getInstance().convertText(String.format("DGC:%.2f%%", currentDemonArray[currentDemonIndex].getDgc()),
								48);
						dgcText1 = ImgUtil.resizeImage(
								dgcText1, (int) dgcText1.getWidth(), (int) dgcText1.getHeight(),
								(int) (dgcText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (dgcText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image nameText1 = Text.getInstance().convertText("" + currentDemonArray[currentDemonIndex].getMobName(), 48);
						nameText1 = ImgUtil.resizeImage(
								nameText1, (int) nameText1.getWidth(), (int) nameText1.getHeight(),
								(int) (nameText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (nameText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image expMaxText1 = Text.getInstance()
								.convertText(
										currentDemonArray[currentDemonIndex].getCurrentExp() + ":" + currentDemonArray[currentDemonIndex].getMaxExp(),
										32);
						expMaxText1 = ImgUtil.resizeImage(
								expMaxText1, (int) expMaxText1.getWidth(), (int) expMaxText1.getHeight(),
								(int) (expMaxText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (expMaxText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						Image lvlText1 = Text.getInstance().convertText("lvl:" + currentDemonArray[currentDemonIndex].getLvl(), 48);
						lvlText1 = ImgUtil.resizeImage(
								lvlText1, (int) lvlText1.getWidth(), (int) lvlText1.getHeight(),
								(int) (lvlText1.getWidth() * gamepanel.getVgp().getScalingFactorX()),
								(int) (lvlText1.getHeight() * gamepanel.getVgp().getScalingFactorY()));

						hpView.setImage(hpText1);
						atkView.setImage(atkText1);
						resView.setImage(resText1);
						dgcView.setImage(dgcText1);
						nameView.setImage(nameText1);
						expText.setImage(expMaxText1);
						lvlView.setImage(lvlText1);
					}
					transp.setVisible(false);
					itemSureBackround.setVisible(false);
					itemStuff.setVisible(false);
				});

			});

			p2.getChildren().add(iv);
		}

		Item4Slots[1].setLayoutY( (61 + 3) * gamepanel.getVgp().getScalingFactorY());
		Item4Slots[2].setLayoutY( (122 + 6) * gamepanel.getVgp().getScalingFactorY());
		Item4Slots[3].setLayoutY( (183 + 8) * gamepanel.getVgp().getScalingFactorY());

		// System.out.println(currentDemonArray[currentDemonIndex].getItem4List()[3].toString());




		p2.setLayoutX(6 * gamepanel.getVgp().getScalingFactorX());
		p2.setLayoutY(6 * gamepanel.getVgp().getScalingFactorY());

		getChildren().add(invBackround);

		elementView	= new ImageView(showElementbr(currentDemonArray[currentDemonIndex].getElement()));
		eIconView	= new ImageView(showElementIcon(currentDemonArray[currentDemonIndex].getElement()));
		getChildren().add(elementView);

		eIconView.setLayoutX(785 * gamepanel.getVgp().getScalingFactorX());
		eIconView.setLayoutY(100 * gamepanel.getVgp().getScalingFactorY());
		getChildren().add(eIconView);

		getChildren().add(m1.getDemon());
		m1.getDemon().setFixToScreen(true);
		m1.getDemon().setReqHeight(192);
		m1.getDemon().setReqWidth(192);
		m1.getDemon().reloadTextures();
		m1.getDemon().setLayoutX(180 * gamepanel.getVgp().getScalingFactorX());
		m1.getDemon().setLayoutY(50 * gamepanel.getVgp().getScalingFactorX());

		nameView = new ImageView(nameText);
		nameView.setLayoutX(100 * gamepanel.getVgp().getScalingFactorX());
		nameView.setLayoutY(9 * gamepanel.getVgp().getScalingFactorY());
		namePane.getChildren().addAll(textBackroundCT, nameView);

		getChildren().add(namePane);

		hpView	= new ImageView(hpText);
		atkView	= new ImageView(atkText);
		resView	= new ImageView(resText);
		dgcView	= new ImageView(dgcText);
		lvlView	= new ImageView(lvlText);
		expText	= new ImageView(expMaxText);
		expBar	= new ImageView(showXPbr());

		expBar.setLayoutX(-30 * gamepanel.getVgp().getScalingFactorX());
		// expBar.setLayoutY(5*gamepanel.getVgp().getScalingFactorY());
		expText.setLayoutX(290 * gamepanel.getVgp().getScalingFactorX());
		expText.setLayoutY(17 * gamepanel.getVgp().getScalingFactorY());

		getChildren().add(status);
		status.getChildren().addAll(hpView, atkView, resView, dgcView, expBar, expText, lvlView);

		lvlView.setLayoutX( (10 + 32) * gamepanel.getVgp().getScalingFactorX());
		lvlView.setLayoutY( (15 + 32) * gamepanel.getVgp().getScalingFactorY());
		hpView.setLayoutX( (10 + 50) * gamepanel.getVgp().getScalingFactorX());
		hpView.setLayoutY( (15 + 64) * gamepanel.getVgp().getScalingFactorY());
		atkView.setLayoutX( (10 + 32) * gamepanel.getVgp().getScalingFactorX());
		atkView.setLayoutY( (15 + 64 + 32) * gamepanel.getVgp().getScalingFactorY());
		resView.setLayoutX( (10 + 32) * gamepanel.getVgp().getScalingFactorX());
		resView.setLayoutY( (15 + 64 + 32 * 2) * gamepanel.getVgp().getScalingFactorY());
		dgcView.setLayoutX( (10 + 32) * gamepanel.getVgp().getScalingFactorX());
		dgcView.setLayoutY( (15 + 64 + 32 * 3) * gamepanel.getVgp().getScalingFactorY());

		status.setLayoutX( (gamepanel.getVgp().getGameWidth() / 2 + 10) * gamepanel.getVgp().getScalingFactorX());
		status.setLayoutY(10 * gamepanel.getVgp().getScalingFactorY());

		getChildren().add(ausXb);

		// TODO fix f11

		getChildren().addAll(potionbutton, armorbutton, usebutton, keybutton, idkbutton);

		getChildren().add(itemOverlay);
		itemOverlay.setVisible(false);

		getChildren().addAll(p, p2);

		getChildren().addAll(transp, itemSureBackround, itemStuff);
		transp.setVisible(false);
		itemSureBackround.setVisible(false);

		itemStuff.setLayoutX(240 * gamepanel.getVgp().getScalingFactorX());
		itemStuff.setLayoutY(130 * gamepanel.getVgp().getScalingFactorY());
		itemStuff.setVisible(false);
		setVisible(false);

		ausXb.setOnMousePressed(me -> {
			ausXb.setImage(ausX2);
		});

		ausXb.setOnMouseReleased(me -> {
			ausXb.setImage(ausX);
			tabm.closeTabm(true);
			new Thread(() -> {
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
	 * Item to inventory.
	 *
	 * @param item the item
	 */
	public void itemToInventory(Item item) {
		System.out.println(item);
		if (item instanceof Potion p1) {
			int x = findFirstNull(potionArray);
			if (x != -1) potionArray[x] = p1;

		} else if (item instanceof Gear gear) {
			int x = findFirstNull(gearArray);
			if (x != -1) gearArray[x] = gear;

		} else if (item instanceof Use u1) {
			int x = findFirstNull(useArray);
			if (x != -1) useArray[x] = u1;

		} else if (item instanceof Key k1) {
			int x = findFirstNull(keyArray);
			if (x != -1) keyArray[x] = k1;

		} else System.err.println("UNEXPECTED TYPE OF ITEM ABTREIBUNG FEHLGESCHLAGEN!!!");

	}

	/**
	 * Scale F 11.
	 */
	public void scaleF11() { init(); }

	/**
	 * Show.
	 */
	public void show() {
		moveFromArrayToView();
		setVisible(true);
		setDisable(false);

	}

	/**
	 * Show elementbr.
	 *
	 * @param e the e
	 *
	 * @return the image
	 */
	public Image showElementbr(Element e) {
		Image test = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementFire.png");
		if (e == Element.Fire) {
			Image firebr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementFire.png");
			test = firebr;
		} else if (e == Element.Water) {
			Image waterbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementWater.png");
			test = waterbr;
		} else if (e == Element.Plant) {
			Image plantbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementPlant.png");
			test = plantbr;
		} else if (e == Element.Shadow) {
			Image shadowbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementShadow.png");
			test = shadowbr;
		} else if (e == Element.Light) {
			Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementLight.png");
			test = lightbr;
		} else if (e == Element.DimensionMaster) {
			Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementWorld_Ender.png");
			test = lightbr;
		} else {
			Image voidbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementVoid.png");
			test = voidbr;
		}
		return test;

	}

	/**
	 * Show element icon.
	 *
	 * @param e the e
	 *
	 * @return the image
	 */
	public Image showElementIcon(Element e) {
		Image test = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconFire.png");
		if (e == Element.Fire) {
			Image firebr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconFire.png");
			test = firebr;
		} else if (e == Element.Water) {
			Image waterbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconWater.png");
			test = waterbr;
		} else if (e == Element.Plant) {
			Image plantbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconPlant.png");
			test = plantbr;
		} else if (e == Element.Shadow) {
			Image shadowbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconShadow.png");
			test = shadowbr;
		} else if (e == Element.Light) {
			Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconLight.png");
			test = lightbr;
		} else if (e == Element.DimensionMaster) {
			Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconWorld_Ender.png");
			test = lightbr;
		} else {
			Image voidbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconVoid.png");
			test = voidbr;
		}
		return test;

	}

	/**
	 * Show X pbr.
	 *
	 * @return the image
	 */
	public Image showXPbr() {
		int		crExp		= currentDemonArray[currentDemonIndex].getCurrentExp();
		double	_1prozent	= currentDemonArray[currentDemonIndex].getMaxExp() / 100.0;
		Image	test;

		if (crExp <= _1prozent * 10) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar1.1.png");
			test = img;
		} else if (crExp <= _1prozent * 20) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar1.2.png");
			test = img;
		} else if (crExp <= _1prozent * 30) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar1.3.png");
			test = img;
		} else if (crExp <= _1prozent * 40) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar2.1.png");
			test = img;
		} else if (crExp <= _1prozent * 50) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar2.2.png");
			test = img;
		} else if (crExp <= _1prozent * 60) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar2.3.png");
			test = img;
		} else if (crExp <= _1prozent * 70) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar3.1.png");
			test = img;
		} else if (crExp <= _1prozent * 80) {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar3.2.png");
			test = img;
		} else {
			Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar3.3.png");
			test = img;
		}
		return test;

	}

}
