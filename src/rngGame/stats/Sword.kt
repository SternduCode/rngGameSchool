package rngGame.stats

import java.util.*

class Sword(itemRarity: Rarity): Gear("Sword", itemRarity.textureName, itemRarity) {

	init {

		val gen = Random()
		atk = when(rarity) {
			Rarity.COMMON -> 2 + gen.nextInt(3)     // 2-4
			Rarity.UNCOMMON -> 3 + gen.nextInt(3)   // 3-5
			Rarity.RARE -> 5 + gen.nextInt(3)       // 5-7
			Rarity.VERY_RARE -> 7 + gen.nextInt(4)  // 7-10
			Rarity.EPIC -> 9 + gen.nextInt(4)       // 9-12
			Rarity.LEGENDARY -> 11 + gen.nextInt(4) // 11-14
			Rarity.GOD -> 13 + gen.nextInt(4)       // 13-16
			Rarity.VOID -> 17 + gen.nextInt(4)      // 17-20
		}
		hp = when(rarity) {
			Rarity.VOID -> 23 + gen.nextInt(5)
			else -> 0
		}
		res = when(rarity) {
			Rarity.VOID -> 0.1 / 3
			else -> 0.0
		}
		dgc = when(rarity) {
			Rarity.VOID -> 0.03
			else -> 0.0
		}

	}

}
