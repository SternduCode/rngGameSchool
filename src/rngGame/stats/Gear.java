package rngGame.stats;

import java.util.Random;

public class Gear extends Item{

	/** The hp. */
	protected int hp;

	/** The res. */
	protected double res;

	/** The dgc. */
	protected double dgc;

	/** The atk. */
	protected int atk;
	
	public Gear(String ordner, String item, Rarity rarity) {
		super(ordner, item, rarity);
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
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [rarity=" + rarity + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]";
	}
}
