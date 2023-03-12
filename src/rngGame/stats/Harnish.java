package rngGame.stats;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Harnish.
 */
public class Harnish extends Item{

	/** The hp. */
	private final int hp;

	/** The res. */
	private final double res;

	/** The dgc. */
	private final double dgc;

	/** The atk. */
	private final int atk;

	/**
	 * Instantiates a new harnish.
	 *
	 * @param ty the ty
	 */
	public Harnish(Rarity ty) {
		super("Harnish", ty.getTextureName(), ty);
		Random gen = new Random();

		hp = switch(rarity) {
			case COMMON -> 3 + gen.nextInt(5); //5
			case UNCOMMON -> 5 + gen.nextInt(5);	//7
			case RARE -> 7 + gen.nextInt(5);	//9
			case VERYRARE -> 9 + gen.nextInt(5); //11
			case EPIC -> 12 + gen.nextInt(5); //14
			case LEGENDARY -> 15 + gen.nextInt(5); //17
			case GOD ->	18 + gen.nextInt(5); //20
			case VOID -> 23 + gen.nextInt(5); //25
		};

		dgc = switch(rarity) {
			case COMMON -> 0.01;
			case UNCOMMON -> 0.0125;
			case RARE -> 0.015;
			case VERYRARE -> 0.0175;
			case EPIC -> 0.02;
			case LEGENDARY -> 0.025;
			case GOD ->	0.0275;
			case VOID -> 0.03;
		};

		res = switch(rarity) {
			case COMMON -> 0.010;
			case UNCOMMON -> 0.0150;
			case RARE -> 0.0175;
			case VERYRARE -> 0.020;
			case EPIC -> 0.025;
			case LEGENDARY -> 0.0275;
			case GOD ->	0.030;
			case VOID -> 0.1/3;
		};

		atk = switch(rarity) {
			case VOID -> 17 + gen.nextInt(4);// 17-20
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
		return "Harnish [rarity=" + rarity + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]";
	}
}
