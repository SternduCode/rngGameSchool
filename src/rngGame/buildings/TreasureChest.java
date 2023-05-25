package rngGame.buildings;

import java.util.*;

import com.sterndu.json.*;

import javafx.animation.FadeTransition;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;
import rngGame.main.Input;
import rngGame.main.Text;
import rngGame.main.Text.AnimatedText;
import rngGame.stats.*;
import rngGame.tile.ImgUtil;
import rngGame.visual.GamePanel;


// TODO: Auto-generated Javadoc
/**
 * The Class TreasureChest.
 */
public class TreasureChest extends Building {

	/** The voidi. */
	private static int common = 0, uncommon = 0, rare = 0, veryrare = 0, epic = 0, legendary = 0, god = 0, voidi = 0;

	/** The is open. */
	private boolean isOpen = false;

	/** The if endchest. */
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
		ifEndchest = false;
		if(building.containsKey("endChest")) ifEndchest=((BoolValue) building.get("endChest")).getValue();
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
		ifEndchest = false;
		if(building.containsKey("endChest")) ifEndchest=((BoolValue) building.get("endChest")).getValue();
	}

	/**
	 * Creates the item.
	 *
	 * @return the item
	 */
	public Item createItem() {
		Random gen = new Random();
		Rarity wahl = Rarity.COMMON;

		int r = gen.nextInt(100*2)+1;
		////////////
		if (r <= 80) wahl = Rarity.COMMON; // 40%
		else if (r <= 130) wahl = Rarity.UNCOMMON; // 25%
		else if (r <= 160) wahl = Rarity.RARE; // 15%
		else if (r <= 180) wahl = Rarity.VERY_RARE; // 10%
		else if (r <= 192) wahl = Rarity.EPIC; // 6%
		else if (r <= 197) wahl = Rarity.LEGENDARY; // 2,5%
		else if (r <= 199) wahl = Rarity.GOD; // 1%
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

	/**
	 * Give item.
	 */
	public void giveItem() {
		
		if(ifEndchest) {
			if(gamepanel.getLgp().getBuildings().stream().filter(b -> b instanceof TreasureChest).map(b -> ((TreasureChest) b)).filter(t -> !(t.isOpen()||t.isIfEndchest())).count() == 0 &&
				gamepanel.getLgp().getMobRans().size() == 0) {
			Item r1 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r1);
			Item r2 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r2);
			Item r3 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r3);
			FadeTransition ft2 = new FadeTransition(Duration.millis(250), gamepanel.getLoadingScreen());
			ft2.setFromValue(1);
			ft2.setToValue(0);
			ft2.play();
			isOpen = true;
			TreasureChest.this.setCurrentKey("open");
			gamepanel.getLgp().setMap("./res/maps/lavaMap2.json", new double[] {
					1464.0, 372.0
			});
			} else {
				gamepanel.setBlockUserInputs(true);
				Image img = ImgUtil.getScaledImage(gamepanel, "./res/gui/bubble/SpeakBubbledrai.png");
				ImageView kek = new ImageView(img);
				gamepanel.getLgp().getBubble().getChildren().add(kek);
				AnimatedText at = Text.getInstance().convertText("Cant open this Chest \nKill all Enemies and \nfind all Treasurechests", 64, false, Color.WHITE);
				at.setOnMouseReleased(e->{
					gamepanel.setBlockUserInputs(false);
					gamepanel.getLgp().getBubble().getChildren().clear();
					gamepanel.getBubbleText().getChildren().clear();
				});
				kek.setOnMouseReleased(e->{
					gamepanel.setBlockUserInputs(false);
					gamepanel.getLgp().getBubble().getChildren().clear();
					gamepanel.getBubbleText().getChildren().clear();
				});
				gamepanel.getBubbleText().getChildren().add(at);
				gamepanel.getBubbleText().setLayoutX(gamepanel.getGameWidth() / 2 - at.getImgWidth() / 2);
				gamepanel.getBubbleText().setLayoutY(gamepanel.getGameHeight() / 1.3 - at.getImgHeight() / 2.0);
			}
		} else {
			Item r1 = createItem();
			gamepanel.getGamemenu().getInventory().itemToInventory(r1);
			isOpen = true;
			TreasureChest.this.setCurrentKey("open");
		}
	}

	/**
	 * Inits the.
	 */
	public void init() {
		if (!getMiscBoxHandler().containsKey("action")) addMiscBox("action",
				new Ellipse(getReqWidth() * gamepanel.getScalingFactorX() / 2, getReqHeight() * gamepanel.getScalingFactorY() / 2,
						gamepanel.getBlockSizeX() / 2, gamepanel.getBlockSizeY() / 2),
				(gpt, self) -> {
					if (!isOpen)
						gpt.getAktionbutton().setInteractionbuttonKann(true, gp2 -> {
							isOpen = true;
							TreasureChest.this.setCurrentKey("open");
							giveItem();
						});
				});
		else getMiscBoxHandler().put("action", (gpt, self) -> {
			if (!isOpen)
				gpt.getAktionbutton().setInteractionbuttonKann(true, gp2 -> {
					giveItem();
				});
		});
	}

	/**
	 * Test item.
	 */
	public void TestItem() {
		Item r1 = createItem();
		switch (r1.getRarity()) {
			case COMMON -> common++;
			case UNCOMMON -> uncommon++;
			case RARE -> rare++;
			case VERY_RARE -> veryrare++;
			case EPIC -> epic++;
			case LEGENDARY -> legendary++;
			case GOD -> god++;
			case VOID -> voidi++;
			default ->
			throw new IllegalArgumentException("Unexpected value: " + r1.getRarity());
		}
		gamepanel.getGamemenu().getInventory().itemToInventory(r1);
		double sum = (common + uncommon + rare + veryrare + epic + legendary + god + voidi) / 100.0;
		System.out.printf("Common %.2f%% Uncommon %.2f%% Rare %.2f%% Very Rare %.2f%% Epic %.2f%% Legendary %.2f%% God %.2f%% Void %.2f%% Items %d\n",
				common / sum, uncommon / sum, rare / sum, veryrare / sum, epic / sum, legendary / sum, god / sum, voidi / sum, (long) (sum * 100));
	}

	public boolean isOpen() {
		return isOpen;
	}

	public boolean isIfEndchest() {
		return ifEndchest;
	}


}
