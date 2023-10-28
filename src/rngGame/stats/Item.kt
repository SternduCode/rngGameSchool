package rngGame.stats

import javafx.scene.image.Image
import rngGame.tile.ImgUtil

open class Item(ordner: String, item: String, rarity: Rarity) {

	var rarity: Rarity = rarity
		protected set

	var path = "./res/Items/$ordner/$item.png"
		protected set

	val image: Image
		get() = ImgUtil.getScaledImage(path)

	override fun toString(): String {
		return "Item [path=$path, rarity=$rarity]"
	}

	companion object {
		val NOITEM = Item("Use", "noItem", Rarity.COMMON)
	}

}
