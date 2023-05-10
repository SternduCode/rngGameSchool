package rngGame.visual;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import rngGame.ui.Button;


// TODO: Auto-generated Javadoc
/**
 * The Class Inventory.
 */
public class Inventory extends Pane {

	/**
	 * The inventory, name and element background, item overlay, black transparent, coming soon, inventory are you sure background, demon heal/switch
	 * foregrounds, x button, name, element icon, back/apply/remove buttons, health points, attack, resistance, dodge chance, experience bar,
	 * experience text, level text, selected demon overlay, selected demon line pointer.
	 */
	private final ImageView inventoryBackground, nameBackground, elementBackground, itemOverlay, blackTransparent, comingSoon,
	inventoryAreYouSureBackground, demonSwitchForeground, demonHealForeground, xButton, name, elementIcon, backButton, applyButton,
	removeButton, healthPoints, attack, resistance, dodgeChance, experienceBar, experienceText, levelText, selectedDemonOverlay,
	selectedDemonLinePointer;

	/** The demon item slots. */
	private ImageView[] demonItemSlots;

	/** The inventory slots. */
	private ImageView[][] inventorySlots;

	/** The misc button. */
	private Button portionButton, armorButton, useButton, keyButton, miscButton;

	/** The name pane and status pane. */
	private final Pane namePane, statusPane;

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/**
	 * Instantiates a new inventory.
	 *
	 * @param gamepanel the gamepanel
	 */
	public Inventory(GamePanel gamepanel) {
		inventoryBackground				= new ImageView();
		nameBackground					= new ImageView();
		elementBackground				= new ImageView();
		itemOverlay						= new ImageView();
		blackTransparent				= new ImageView();
		comingSoon						= new ImageView();
		inventoryAreYouSureBackground	= new ImageView();
		demonSwitchForeground			= new ImageView();
		demonHealForeground				= new ImageView();
		xButton							= new ImageView();
		name							= new ImageView();
		elementIcon						= new ImageView();
		backButton						= new ImageView();
		applyButton						= new ImageView();
		removeButton					= new ImageView();
		healthPoints					= new ImageView();
		attack							= new ImageView();
		resistance						= new ImageView();
		dodgeChance						= new ImageView();
		experienceBar					= new ImageView();
		experienceText					= new ImageView();
		levelText						= new ImageView();
		selectedDemonOverlay			= new ImageView();
		selectedDemonLinePointer		= new ImageView();

		namePane	= new Pane();
		statusPane	= new Pane();

		this.gamepanel = gamepanel;
	}

}
