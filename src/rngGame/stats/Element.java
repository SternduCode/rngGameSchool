	package rngGame.stats;

// TODO: Auto-generated Javadoc
/**
 * The Enum Element.
 */
public enum Element {
	
	/** The Fire. */
	Fire(4, 0.01, 20, 0.005, 1), 
	
	/** The Water. */
	Water(4, 0.01, 20, 0.005, 1), 
	
	/** The Plant. */
	Plant(4, 0.01, 20, 0.005, 1), 
	
	/** The Shadow. */
	Shadow(6, 0.02, 24, 0.01, 1), 
	
	/** The Light. */
	Light(6, 0.02, 24, 0.01, 1), 
	
	/** The Void. */
	Void(10, 0.03, 30, 0.015, 1),
	
	/** The Dimension master. */
	DimensionMaster(15, 0.05, 40, 0.03, 1);


	/** The atk. */
	private int atk;
	
	/** The res. */
	private double res;
	
	/** The hp. */
	private int hp;
	
	/** The dgc. */
	private double dgc;
	
	/** The lvl. */
	private int lvl;



	/**
	 * Instantiates a new element.
	 *
	 * @param atkNeu the atk neu
	 * @param resNeu the res neu
	 * @param hpNeu the hp neu
	 * @param dgcNeu the dgc neu
	 * @param lvl the lvl
	 */
	Element(int atkNeu, double resNeu, int hpNeu, double dgcNeu, int lvl){
		atk = atkNeu;
		res = resNeu;
		hp = hpNeu;
		dgc = dgcNeu;
		this.lvl = lvl;
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
	 * Gets the lvl.
	 *
	 * @return the lvl
	 */
	public int getLvl() {
		return lvl;
	}
	
	/**
	 * Gets the res.
	 *
	 * @return the res
	 */
	public double getRes() {
		return res;
	}
}
