package rngGame.stats;

public enum Rarity {

	COMMON,
	UNCOMMON,
	RARE,
	VERY_RARE,
	EPIC,
	LEGENDARY,
	GOD,
	VOID;


	public String getTextureName() {
		return toString().toCharArray()[0] + toString().substring(1).toLowerCase();
	}

}
