package rngGame.stats;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Potion.
 */
public class Potion extends Item{

	/** The hp. */
	private final int hp;


	/**
	 * Instantiates a new potion.
	 *
	 * @param ty the ty
	 */
	public Potion(Rarity ty) {
		super("Potions", ty.getTextureName(), ty);

		Random gen = new Random();
		hp = switch(rarity) {
			case COMMON -> 8 + gen.nextInt(5); //10
			case UNCOMMON -> 15 + gen.nextInt(11); //20
			case RARE -> 35 + gen.nextInt(11); //40
			case VERYRARE -> 40 + gen.nextInt(21); //50
			case EPIC -> 55 + gen.nextInt(31); //70
			case LEGENDARY -> 75 + gen.nextInt(31); //90
			case GOD ->	110 + gen.nextInt(41); //130
			case VOID -> 155 + gen.nextInt(51); //180
		};
	}

	/**
	 * Gets the hp.
	 *
	 * @return the hp
	 */
	public int getHp() {
		return hp;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Potion [rarity=" + rarity + ", hp=" + hp + "]";
	}

}
