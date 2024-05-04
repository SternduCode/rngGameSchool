package rngGame.stats;

public class Gear extends Item {

	private int hp;

	private double res;

	private double dgc;

	private int atk;

	public Gear(String ordner, String item, Rarity rarity) {
		super(ordner, item, rarity);
	}

	public int getHp() {
		return hp;
	}

	public double getRes() {
		return res;
	}

	public double getDgc() {
		return dgc;
	}

	public int getAtk() {
		return atk;
	}

	protected void setHp(int hp) {
		this.hp = hp;
	}

	protected void setRes(double res) {
		this.res = res;
	}

	protected void setDgc(double dgc) {
		this.dgc = dgc;
	}

	protected void setAtk(int atk) {
		this.atk = atk;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [rarity=" + getRarity() + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]";
	}

}
