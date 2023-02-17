package rngGame.stats;

// TODO: Auto-generated Javadoc
/**
 * The Enum Rarity.
 */
public enum Rarity {

	/** The common. */
	COMMON,
	/** The uncommon. */
	UNCOMMON,
	/** The rare. */
	RARE,
	/** The veryrare. */
	VERYRARE,
	/** The epic. */
	EPIC,
	/** The legendary. */
	LEGENDARY,
	/** The god. */
	GOD,
	/** The void. */
	VOID;

	/** The texture name. */
	String textureName;



	/**
	 * Instantiates a new rarity.
	 */
	Rarity() {
		textureName = toString().charAt(0) + toString().substring(1).toLowerCase();
	}

	/**
	 * Gets the texture name.
	 *
	 * @return the texture name
	 */
	public String getTextureName() {
		return textureName;
	}
}
