package rngGame.stats

open class Gear(ordner: String, item: String, rarity: Rarity): Item(ordner, item, rarity) {

	var hp = 0
		protected set

	var res = 0.0
		protected set

	var dgc = 0.0
		protected set

	var atk = 0
		protected set

	override fun toString(): String {
		return javaClass.getSimpleName() + " [rarity=" + rarity + ", hp=" + hp + ", res=" + res + ", dgc=" + dgc + ", atk=" + atk + "]"
	}

}
