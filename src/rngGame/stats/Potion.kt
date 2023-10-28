package rngGame.stats

import java.util.*

class Potion(itemRarity: Rarity): Item("Potions", itemRarity.textureName, itemRarity) {

	@JvmField
	val hp: Int

	init {
		val gen = Random()
		hp = when(rarity) {
			Rarity.COMMON -> 8 + gen.nextInt(5)         //  10
			Rarity.UNCOMMON -> 15 + gen.nextInt(11)     //  20
			Rarity.RARE -> 35 + gen.nextInt(11)         //  40
			Rarity.VERY_RARE -> 40 + gen.nextInt(21)    //  50
			Rarity.EPIC -> 55 + gen.nextInt(31)         //  70
			Rarity.LEGENDARY -> 75 + gen.nextInt(31)    //  90
			Rarity.GOD -> 110 + gen.nextInt(41)         // 130
			Rarity.VOID -> 155 + gen.nextInt(51)        // 180
		}
	}

	override fun toString(): String {
		return "Potion [rarity=$rarity, hp=$hp]"
	}

}
