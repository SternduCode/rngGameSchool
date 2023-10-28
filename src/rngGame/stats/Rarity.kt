package rngGame.stats

enum class Rarity {

	COMMON,
	UNCOMMON,
	RARE,
	VERY_RARE,
	EPIC,
	LEGENDARY,
	GOD,
	VOID;

	@JvmField
	val textureName: String = "${toString().first()}${toString().drop(1).lowercase()}"

}
