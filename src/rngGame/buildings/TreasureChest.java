package rngGame.buildings;

import java.util.List;
import java.util.Random;

import com.sterndu.json.BoolValue;
import com.sterndu.json.JsonObject;

import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.shape.Ellipse;
import rngGame.main.GamePanel;
import rngGame.stats.Element;
import rngGame.stats.Harnisch;
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
			for(int i = 0 ; i < 3; i++){
				createItem();
			} 
		} else {
			createItem();
		}
	}


	public Item createItem() {
		Random gen = new Random();
		int r = gen.nextInt(146)+1;
		Rarity wahl;

		////////////
		if(r <= 30) wahl = Rarity.COMMON;
		else if(r <= 60) wahl = Rarity.UNCOMMON; //30
		else if(r <= 90) wahl = Rarity.RARE;
		else if(r <= 120) wahl = Rarity.VERYRARE;

		else if(r <= 130) wahl = Rarity.EPIC;		//10
		else if(r <= 140) wahl = Rarity.LEGENDARY;

		else if(r <= 145) wahl = Rarity.GOD; // 5

		else wahl = Rarity.VOID; 	//1
		////////////
		return switch (gen.nextInt(6)+1) {
		case 1 -> new Harnisch(wahl);
		case 2 -> new Helmet(wahl);
		case 3 -> new Pants(wahl);
		case 4 -> new Sword(wahl);
		case 5 -> new Potion(wahl);
		case 6 -> new Use(wahl);
		default -> new Potion(wahl);
		}; 
	}

}
