package rngGame.buildings;

import java.util.List;
import java.util.Random;

import com.sterndu.json.BoolValue;
import com.sterndu.json.JsonObject;

import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Ellipse;
import rngGame.main.GamePanel;
import rngGame.main.Input;
import rngGame.stats.Element;
import rngGame.stats.Harnish;
import rngGame.stats.Helmet;
import rngGame.stats.Item;
import rngGame.stats.Pants;
import rngGame.stats.Potion;
import rngGame.stats.Rarity;
import rngGame.stats.Sword;

import rngGame.stats.Use;


// TODO: Auto-generated Javadoc
/**
 * The Class TreasureChest.
 */
public class TreasureChest extends Building {

	/** The is open. */
	private boolean isOpen = false;
	private boolean ifEndchest;



	/**
	 * Instantiates a new treasure chest.
	 *
	 * @param building   the building
	 * @param buildings  the buildings
	 * @param cm         the cm
	 * @param requestorB the requestor B
	 */
	public TreasureChest(Building building, List<Building> buildings, ContextMenu cm, ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);
		init();
	}

	/**
	 * Instantiates a new treasure chest.
	 *
	 * @param building   the building
	 * @param gp         the gp
	 * @param buildings  the buildings
	 * @param cm         the cm
	 * @param requestorB the requestor B
	 */
	public TreasureChest(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm, ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		init();
		this.ifEndchest = false;
		if(building.containsKey("endChest")) {
			this.ifEndchest=((BoolValue) building.get("endChest")).getValue();
		}
		Input.getInstance().setKeyHandler("Items", mod -> {
			TestItem();
		}, KeyCode.I, false);
	}

	/**
	 * Instantiates a new treasure chest.
	 *
	 * @param building   the building
	 * @param gp         the gp
	 * @param directory  the directory
	 * @param buildings  the buildings
	 * @param cm         the cm
	 * @param requestorB the requestor B
	 */
	public TreasureChest(JsonObject building, GamePanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);
		init();
		this.ifEndchest = false;
		if(building.containsKey("endChest")) {
			this.ifEndchest=((BoolValue) building.get("endChest")).getValue();
		}
	}

	/**
	 * Inits the.
	 */
	public void init() {
		if (!getMiscBoxHandler().containsKey("action")) addMiscBox("action", new Ellipse(getReqWidth() * gamepanel.getScalingFactorX() / 2, getReqHeight() * gamepanel.getScalingFactorY() / 2,
				gamepanel.BgX / 2, gamepanel.BgY / 2), (gpt, self) -> {
					if (!isOpen)
						gpt.getAktionbutton().setInteractionbuttonKann(true, gp2 -> {
							isOpen = true;
							TreasureChest.this.setCurrentKey("open");
							giveItem();
						});
				});
		else getMiscBoxHandler().put("action", (gpt, self) -> {
			if (!isOpen)
				gpt .getAktionbutton().setInteractionbuttonKann(true, gp2 -> {
					isOpen = true;
					TreasureChest.this.setCurrentKey("open");
					giveItem();
				});
		});
	}

	public void giveItem() {
		if(ifEndchest) {
			Item r1 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r1);
			Item r2 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r2);
			Item r3 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r3);
			System.out.println(r1.toString()); 
			System.out.println(r2.toString()); 
			System.out.println(r3.toString()); 
		} else {
			Item r1 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r1);
		}
	}
	
	
	
	
	
	
	public void TestItem() {
			Item r1 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r1); 
	}


	public Item createItem() {
		Random gen = new Random();
		Rarity wahl = Rarity.COMMON;
		
		int r = gen.nextInt(100*2)+1;
		////////////
		if(r <= 60) wahl = Rarity.COMMON; //30%
		else if(r <= 100) wahl = Rarity.UNCOMMON; //20%
		else if(r <= 130) wahl = Rarity.RARE; //15%
		else if(r <= 160) wahl = Rarity.VERYRARE; //15%
		else if(r <= 178) wahl = Rarity.EPIC; //9%		
		else if(r <= 190) wahl = Rarity.LEGENDARY; //6%
		else if(r <= 199) wahl = Rarity.GOD; //4,5%
		else wahl = Rarity.VOID; 	// 0,5%
		////////////
		return switch (gen.nextInt(5)+1) {
		case 1 -> new Harnish(wahl);
		case 2 -> new Helmet(wahl);
		case 3 -> new Pants(wahl);
		case 4 -> new Sword(wahl);
		case 5 -> new Potion(wahl);
		
		default -> new Potion(wahl);
		}; 
	}
	
	
	

}
