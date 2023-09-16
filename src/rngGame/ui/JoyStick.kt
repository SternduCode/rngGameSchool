package rngGame.ui

import javafx.scene.layout.Pane
import rngGame.main.WindowManager
import rngGame.visual.AnimatedImage

object JoyStick: Pane() {

	private val joyStick = AnimatedImage("./res/gui/always/JoyschtickBaumwolle.png")

	private val line = Line(0.0, 0.0, 0.0, 0.0)

	private val background = AnimatedImage("./res/gui/always/JoyschtickRand.png")

	init {
		background.imgRequestedWidth = 128
		background.imgRequestedHeight = 128

		joyStick.imgRequestedWidth = 64
		joyStick.imgRequestedHeight = 64

		background.scaleF11()
		joyStick.scaleF11()

		line.strokeWidth = 5.0

		children.addAll(background, line, joyStick)

		layoutX = WindowManager.getInstance().gameWidth * .1
		layoutY = WindowManager.getInstance().gameHeight * .8

		line.startX = background.imgRequestedWidth * .5 - joyStick.imgRequestedWidth * .5
		line.startY = background.imgRequestedHeight * .5 - joyStick.imgRequestedHeight * .5

		joyStick.layoutX = background.imgRequestedWidth * .5 - joyStick.imgRequestedWidth * .5
		joyStick.layoutY = background.imgRequestedHeight * .5 - joyStick.imgRequestedHeight * .5

		line.startX = background.imgRequestedWidth * .5
		line.startY = background.imgRequestedHeight * .5

		line.endX = background.imgRequestedWidth * .5
		line.endY = background.imgRequestedHeight * .5

		setOnMousePressed {
			joyStick.layoutX = it.x - joyStick.imgRequestedWidth * .5
			joyStick.layoutY = it.y - joyStick.imgRequestedHeight * .5

			line.endX = it.x
			line.endY = it.y

			it.consume()
		}
		setOnMouseDragged {
			joyStick.layoutX = it.x - joyStick.imgRequestedWidth * .5
			joyStick.layoutY = it.y - joyStick.imgRequestedHeight * .5

			line.endX = it.x
			line.endY = it.y

			it.consume()
		}
		setOnMouseReleased {
			joyStick.layoutX = background.imgRequestedWidth * .5 - joyStick.imgRequestedWidth * .5
			joyStick.layoutY = background.imgRequestedHeight * .5 - joyStick.imgRequestedHeight * .5

			line.endX = background.imgRequestedWidth * .5
			line.endY = background.imgRequestedHeight * .5

			it.consume()
		}
	}

}
