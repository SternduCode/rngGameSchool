package rngGame.stats

import java.util.*

class Harnish(itemRarity: Rarity): Gear("Harnish", itemRarity.textureName, itemRarity) {

	init {
		val gen = Random()
		hp = when(rarity) {
			Rarity.COMMON -> 3 + gen.nextInt(5)         //  5
			Rarity.UNCOMMON -> 5 + gen.nextInt(5)       //  7
			Rarity.RARE -> 7 + gen.nextInt(5)           //  9
			Rarity.VERY_RARE -> 9 + gen.nextInt(5)      // 11
			Rarity.EPIC -> 12 + gen.nextInt(5)          // 14
			Rarity.LEGENDARY -> 15 + gen.nextInt(5)     // 17
			Rarity.GOD -> 18 + gen.nextInt(5)           // 20
			Rarity.VOID -> 23 + gen.nextInt(5)          // 25
		}
		dgc = when(rarity) {
			Rarity.COMMON -> 0.01
			Rarity.UNCOMMON -> 0.0125
			Rarity.RARE -> 0.015
			Rarity.VERY_RARE -> 0.0175
			Rarity.EPIC -> 0.02
			Rarity.LEGENDARY -> 0.025
			Rarity.GOD -> 0.0275
			Rarity.VOID -> 0.03
		}
		res = when(rarity) {
			Rarity.COMMON -> 0.010
			Rarity.UNCOMMON -> 0.0150
			Rarity.RARE -> 0.0175
			Rarity.VERY_RARE -> 0.020
			Rarity.EPIC -> 0.025
			Rarity.LEGENDARY -> 0.0275
			Rarity.GOD -> 0.030
			Rarity.VOID -> 0.1 / 3
		}
		atk = when(rarity) {
			Rarity.VOID -> 17 + gen.nextInt(4) // 17-20
			else -> 0
		}
	}

}
