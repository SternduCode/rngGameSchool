package rngGame.ui

import javafx.scene.layout.Pane
import rngGame.main.WindowManager
import rngGame.stats.Item

class InventoryScrollPane: Pane() {
	private val elements = Array(40) { index ->
		InventoryScrollPaneElement(Item.NOITEM).also {
			it.layoutY = it.imageHeight * index + (5 * WindowManager.getInstance().scalingFactorY) * index
		}
	}

	init {
		elements.forEach(children::add)
	}

}