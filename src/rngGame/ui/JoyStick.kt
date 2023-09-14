package rngGame.ui

import javafx.scene.layout.Pane
import rngGame.main.WindowManager
import rngGame.visual.AnimatedImage

object JoyStick: Pane() {

	private val joyStick = AnimatedImage("./res/gui/always/JoyschtickBaumwolle.png")

	private val background = AnimatedImage("./res/gui/always/JoyschtickRand.png")

	init {
		background.imgRequestedWidth = 128
		background.imgRequestedHeight = 128

		joyStick.imgRequestedWidth = 64
		joyStick.imgRequestedHeight = 64

		background.scaleF11()
		joyStick.scaleF11()

		children.addAll(background, joyStick)

		layoutX = WindowManager.getInstance().gameWidth * .1
		layoutY = WindowManager.getInstance().gameHeight * .8

		joyStick.layoutX = background.imgRequestedWidth * .5 - joyStick.imgRequestedWidth * .5
		joyStick.layoutY = background.imgRequestedHeight * .5 - joyStick.imgRequestedHeight * .5
	}

}
