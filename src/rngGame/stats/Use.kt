package rngGame.stats

class Use(itemRarity: Rarity): Item("Use", itemRarity.textureName, itemRarity) {

	var hp = when(rarity) {
		Rarity.COMMON -> 10
		Rarity.UNCOMMON -> 20
		Rarity.RARE -> 40
		Rarity.VERY_RARE -> 50
		Rarity.EPIC -> 70
		Rarity.LEGENDARY -> 90
		Rarity.GOD -> 130
		Rarity.VOID -> 180
	}

}
