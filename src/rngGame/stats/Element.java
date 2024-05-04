package rngGame.stats;

public enum Element {

	Fire(4, 0.01, 20, 0.005, 1),
	Water(4, 0.01, 20, 0.005, 1),
	Plant(4, 0.01, 20, 0.005, 1),
	Shadow(6, 0.02, 24, 0.01, 1),
	Light(6, 0.02, 24, 0.01, 1),
	Void(10, 0.03, 30, 0.015, 1),
	DimensionMaster(15, 0.05, 40, 0.03, 1);

	private final int atk;
	private final double res;
	private final int hp;
	private final double dgc;
	private final int lvl;

	public int getAtk() {
		return atk;
	}

	public double getRes() {
		return res;
	}

	public int getHp() {
		return hp;
	}

	public double getDgc() {
		return dgc;
	}

	public int getLvl() {
		return lvl;
	}

	Element(
			int atk,
			double res,
			int hp,
			double dgc,
			int lvl
	) {
		this.atk = atk;
		this.res = res;
		this.hp = hp;
		this.dgc = dgc;
		this.lvl = lvl;
	}

}
