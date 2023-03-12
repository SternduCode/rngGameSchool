package rngGame.stats;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Sword.
 */
public class Sword extends Item{

	/** The hp. */
	private final int hp;

	/** The res. */
	private final double res;

	/** The dgc. */
	private final double dgc;

	/** The atk. */
	private final int atk;



	/**
	 * Instantiates a new sword.
	 *
	 * @param ty the ty
	 */
	public Sword(Rarity ty) {
		super("Sword", ty.getTextureName(), ty);
		Random gen = new Random();
		atk = switch(rarity) {
			case COMMON -> 2 + gen.nextInt(3); 							// 2-4
			case UNCOMMON -> 3 + gen.nextInt(3);						// 3-5
			case RARE -> 5 + gen.nextInt(3);							// 5-7
			case VERYRARE -> 7 + gen.nextInt(4); 						// 7-10
			case EPIC -> 9 + gen.nextInt(4); 							// 9-12
			case LEGENDARY -> 11 + gen.nextInt(4);						// 11-14
			case GOD ->	13 + gen.nextInt(4);							// 13-16
			case VOID -> 17 + gen.nextInt(4);							// 17-20
		};

		hp = switch(rarity) {
			case VOID -> 23 + gen.nextInt(5);//
			default -> 0;
		};

		res = switch(rarity) {
			case VOID -> 0.1/3;
			default -> 0;
		};

		dgc = switch(rarity) {
			case VOID -> 0.03;
			default -> 0;
		};
	}

	/**
	 * Gets the atk.
	 *
	 * @return the atk
	 */
	public int getAtk() {
		return atk;
	}

	/**
	 * Gets the dgc.
	 *
	 * @return the dgc
	 */
	public double getDgc() {
		return dgc;
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
	 * Gets the res.
	 *
	 * @return the res
	 */
	public double getRes() {
		return res;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Sword [rarity=" + rarity + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]";
	}

}
