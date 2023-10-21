package rngGame.main

import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.Pane
import rngGame.visual.AnimatedImage

class PaneScope: Pane() {

	operator fun Node.unaryPlus() {
		this@PaneScope.children.add(this@unaryPlus)
	}

}

class GroupScope: Group() {

	operator fun Node.unaryPlus() {
		this@GroupScope.children.add(this@unaryPlus)
	}

}

class ImageViewScope: AnimatedImage {

	constructor() : super()
	constructor(path: String?) : super(path)
	constructor(path: String?, fps: Int) : super(path, fps)

}

fun pane(block: PaneScope.() -> Unit): Pane {
	val pane = PaneScope()
	pane.block()
	return pane
}

fun imageView(block: ImageViewScope.() -> Unit): AnimatedImage {
	val imageView = ImageViewScope()
	imageView.block()
	return imageView
}

fun group(block: GroupScope.() -> Unit): Group {
	val group = GroupScope()
	group.block()
	return group
}



fun main() {
	pane {
		+imageView {

		}
		+group {
			+imageView {

			}
		}
	}
}