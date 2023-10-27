package rngGame.ui

import javafx.scene.layout.Pane
import rngGame.stats.Item
import rngGame.visual.AnimatedImage

class InventoryScrollPaneElement(var item: Item): Pane() {

	val background: AnimatedImage = AnimatedImage("path")
	val itemView: AnimatedImage = AnimatedImage()

	init {
		children.addAll(background, itemView)
		itemView.init(item.path)
	}

}