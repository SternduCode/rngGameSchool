@file:JvmName("LoadingScreen")
package rngGame.main

import javafx.animation.FadeTransition
import javafx.util.Duration
import rngGame.visual.AnimatedImage

object LoadingScreen: AnimatedImage() {

	init {
		init("./res/gui/Loadingscreen.gif")
		isDisable = true
		opacity = 0.0
	}

	fun isInLoadingScreen(): Boolean {
		return opacity > .5
	}

	fun goIntoLoadingScreen() {
		if (!isInLoadingScreen()) {
			fitWidth = image.width * WindowManager.getInstance().scalingFactorX
			fitHeight = image.height * WindowManager.getInstance().scalingFactorY
			val ft = FadeTransition(Duration.millis(250.0), this)
			ft.fromValue = 0.0
			ft.toValue = 1.0
			ft.play()
		}
	}

	fun goOutOfLoadingScreen() {
		if (isInLoadingScreen()) {
			fitWidth = image.width * WindowManager.getInstance().scalingFactorX
			fitHeight = image.height * WindowManager.getInstance().scalingFactorY
			val ft = FadeTransition(Duration.millis(250.0), this)
			ft.fromValue = 1.0
			ft.toValue = 0.0
			ft.play()
		}
	}

}