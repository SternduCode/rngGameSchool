package rngGame.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;

import com.sterndu.json.DoubleValue;
import com.sterndu.json.IntegerValue;
import com.sterndu.json.JsonArray;
import com.sterndu.json.JsonObject;
import com.sterndu.json.StringValue;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import rngGame.entity.MobRan;
import rngGame.entity.MonsterNPC;
import rngGame.main.GamePanel;
import rngGame.main.Text;
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
	
	private final Demon[] currentDemonArray = new Demon[5];

	/** The current tab. */
	private Tab currentTab = Tab.POTION;
	
	private Pane namePane = new Pane();
	private ImageView nameView,textBackroundCT,elementView,eIconView;
	
	private Pane status = new Pane();
	private ImageView hpView,atkView,resView,dgcView;
	private ImageView expBar,expText,lvlView;

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The inv backround. */
	private final ImageView invBackround;

	/** The aus xb. */
	private final Button ausXb;

	/** The inv slots. */
	private final ImageView[][] invSlots = new ImageView[10][4];

	/** The potionbutton. */
	private final Button potionbutton,armorbutton,usebutton,keybutton,idkbutton;



	/**
	 * Instantiates a new inventory.
	 *
	 * @param gamepanel the gamepanel
	 * @param tabm the tabm
	 * @throws FileNotFoundException 
	 */
	public Inventory(GamePanel gamepanel, TabMenu tabm) throws FileNotFoundException {
		this.gamepanel = gamepanel;
		// invBackround
		invBackround	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/InvBackround.png"));
		textBackroundCT	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/invNameTitle.png"));
		elementView	= new ImageView(ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementFire.png"));
		/////////////
		
//		boolean testpopo = false;
		Demon m1 = gamepanel.getMobRans().get(0).MobGen();
//		while(!testpopo){
//			Demon m2 = gamepanel.getMobRans().get(0).MobGen();
//			
//			if(m2.getElement() == Element.Void) {
//				m1 = m2;
//				System.out.println();
//				System.out.println();
//				System.out.println();
//				System.out.println(m1.toString()); 
//				break;
//			}
//		}
		
		m1.setLvl(140);
		m1.setCurrentExp(m1.getMaxExp()-1);
		currentDemonArray[0] = m1;
		
		/////////////
		//TODO stats vom "Angeclickten" Monster
		Image hpText = Text.getInstance().convertText("HP:"+currentDemonArray[0].getHp(), 48);
		hpText = ImgUtil.resizeImage(
				hpText, (int) hpText.getWidth(), (int) hpText.getHeight(),
				(int) (hpText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (hpText.getHeight() * gamepanel.getScalingFactorY()));
		
		Image atkText = Text.getInstance().convertText("ATK:"+currentDemonArray[0].getAtk(), 48);
		atkText = ImgUtil.resizeImage(
				atkText, (int) atkText.getWidth(), (int) atkText.getHeight(),
				(int) (atkText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (atkText.getHeight() * gamepanel.getScalingFactorY()));
		
		Image resText = Text.getInstance().convertText("RES:"+currentDemonArray[0].getRes()+"%", 48);
		resText = ImgUtil.resizeImage(
				resText, (int) resText.getWidth(), (int) resText.getHeight(),
				(int) (resText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (resText.getHeight() * gamepanel.getScalingFactorY()));
		
		Image dgcText = Text.getInstance().convertText("DGC:"+currentDemonArray[0].getDgc()+"%", 48);
		dgcText = ImgUtil.resizeImage(
				dgcText, (int) dgcText.getWidth(), (int) dgcText.getHeight(),
				(int) (dgcText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (dgcText.getHeight() * gamepanel.getScalingFactorY()));
		
		Image nameText = Text.getInstance().convertText(""+currentDemonArray[0].getMobName(), 48);
		nameText = ImgUtil.resizeImage(
				nameText, (int) nameText.getWidth(), (int) nameText.getHeight(),
				(int) (nameText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (nameText.getHeight() * gamepanel.getScalingFactorY()));
		
		Image expMaxText = Text.getInstance().convertText(currentDemonArray[0].getCurrentExp()+":"+currentDemonArray[0].getMaxExp(), 32);
		expMaxText = ImgUtil.resizeImage(
				expMaxText, (int) expMaxText.getWidth(), (int) expMaxText.getHeight(),
				(int) (expMaxText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (expMaxText.getHeight() * gamepanel.getScalingFactorY()));
		
		Image lvlText = Text.getInstance().convertText("lvl:"+currentDemonArray[0].getLvl(), 48);
		lvlText = ImgUtil.resizeImage(
				lvlText, (int) lvlText.getWidth(), (int) lvlText.getHeight(),
				(int) (lvlText.getWidth() * gamepanel.getScalingFactorX()),
				(int) (lvlText.getHeight() * gamepanel.getScalingFactorY()));
		
		
		
		
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
		
		elementView = new ImageView(showElementbr(currentDemonArray[0].getElement()));
		eIconView = new ImageView(showElementIcon(currentDemonArray[0].getElement()));
		getChildren().add(elementView);
		
		eIconView.setLayoutX(785*gamepanel.getScalingFactorX());
		eIconView.setLayoutY(100*gamepanel.getScalingFactorY());
		getChildren().add(eIconView);
		
		getChildren().add(m1.getDemon());
		m1.getDemon().setFixToScreen(true);
		m1.getDemon().setReqHeight((int) (192*gamepanel.getScalingFactorX()));
		m1.getDemon().setReqWidth((int) (192*gamepanel.getScalingFactorY()));
		m1.getDemon().reloadTextures();
		m1.getDemon().setLayoutX(180*gamepanel.getScalingFactorX());
		m1.getDemon().setLayoutY(50*gamepanel.getScalingFactorX());
		
		nameView = new ImageView(nameText);
		nameView.setLayoutX(100*gamepanel.getScalingFactorX());
		nameView.setLayoutY(9*gamepanel.getScalingFactorY());
		namePane.getChildren().addAll(textBackroundCT,nameView);
		
		getChildren().add(namePane);
		
		hpView = new ImageView(hpText);
		atkView = new ImageView(atkText);
		resView = new ImageView(resText);
		dgcView = new ImageView(dgcText);
		lvlView = new ImageView(lvlText);
		expText = new ImageView(expMaxText);
		expBar = new ImageView(showXPbr());
		
		expBar.setLayoutX(-30*gamepanel.getScalingFactorX());
//		expBar.setLayoutY(5*gamepanel.getScalingFactorY());
		expText.setLayoutX(290*gamepanel.getScalingFactorX());
		expText.setLayoutY(17*gamepanel.getScalingFactorY());
		
		getChildren().add(status);
		status.getChildren().addAll(hpView,atkView,resView,dgcView,expBar,expText,lvlView);
		
		lvlView.setLayoutX(10+32*gamepanel.getScalingFactorX());
		lvlView.setLayoutY(15+32*gamepanel.getScalingFactorY());
		hpView.setLayoutX(10+50*gamepanel.getScalingFactorX());
		hpView.setLayoutY(15+64*gamepanel.getScalingFactorY());
		atkView.setLayoutX(10+32*gamepanel.getScalingFactorX());
		atkView.setLayoutY((15+64+32)*gamepanel.getScalingFactorY());
		resView.setLayoutX(10+32*gamepanel.getScalingFactorX());
		resView.setLayoutY((15+64+32*2)*gamepanel.getScalingFactorY());
		dgcView.setLayoutX(10+32*gamepanel.getScalingFactorX());
		dgcView.setLayoutY((15+64+32*3)*gamepanel.getScalingFactorY());
		
		status.setLayoutX((gamepanel.SpielLaenge/2+10)*gamepanel.getScalingFactorX());
		status.setLayoutY(10*gamepanel.getScalingFactorY());
		
		
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

	private void While(boolean b) {
		// TODO Auto-generated method stub
		
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
	
	public Image showElementbr(Element e) {
		Image test = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementFire.png");
		if(e == Element.Fire) {
	      Image firebr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementFire.png");
	      test = firebr;
		} else if(e == Element.Water) {
		      Image waterbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementWater.png");
		      test = waterbr;
		} else if(e == Element.Plant) {
		      Image plantbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementPlant.png");
		      test = plantbr;
		} else if(e == Element.Shadow) {
		      Image shadowbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementShadow.png");
		      test = shadowbr;
		} else if(e == Element.Light) {
		      Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementLight.png");
		      test = lightbr;
		} else if(e == Element.Supernova) {
		      Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementWorld_Ender.png");
		      test = lightbr;
		}else {
		      Image voidbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/invElementVoid.png");
		      test = voidbr;
		}
		return test;
	}
	
	public Image showElementIcon(Element e) {
		Image test = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconFire.png");
		if(e == Element.Fire) {
	      Image firebr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconFire.png");
	      test = firebr;
		} else if(e == Element.Water) {
		      Image waterbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconWater.png");
		      test = waterbr;
		} else if(e == Element.Plant) {
		      Image plantbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconPlant.png");
		      test = plantbr;
		} else if(e == Element.Shadow) {
		      Image shadowbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconShadow.png");
		      test = shadowbr;
		} else if(e == Element.Light) {
		      Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconLight.png");
		      test = lightbr;
		}else if(e == Element.Supernova) {
		      Image lightbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconWorld_Ender.png");
		      test = lightbr;
		} else {
		      Image voidbr = ImgUtil.getScaledImage(gamepanel, "./res/gui/IconVoid.png");
		      test = voidbr;
		}
		return test;
	}
	
	public Image showXPbr() {
		int crExp = currentDemonArray[0].getCurrentExp();
		double _1prozent = (currentDemonArray[0].getMaxExp()/100.0);
		Image test;
		
		
		if(crExp <= _1prozent*10) {
	      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar1.1.png");
	      test = img;
		} else if(crExp <= _1prozent*20) {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar1.2.png");
		      test = img;
		} else if(crExp <= _1prozent*30) {
		      Image img = ImgUtil.getScaledImage(gamepanel,"./res/gui/XPBar1.3.png");
		      test = img;
		} else if(crExp <= _1prozent*40) {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar2.1.png");
		      test = img;
		} else if(crExp <= _1prozent*50) {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar2.2.png");
		      test = img;
		} else if(crExp <= _1prozent*60) {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar2.3.png");
		      test = img;
		} else if(crExp <= _1prozent*70) {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar3.1.png");
		      test = img;
		} else if(crExp <= _1prozent*80) {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar3.2.png");
		      test = img;
		} else {
		      Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/XPBar3.3.png");
		      test = img;
		}
		return test;
	}

}
