package rngGame.main

import javafx.scene.Node

fun Node.setPosition(x: Double, y: Double) {
	this.layoutX = x
	this.layoutY = y
}

fun Node.setPositionScaling(x: Double, y: Double) {
	this.layoutX = x * WindowManager.getInstance().scalingFactorX
	this.layoutY = y * WindowManager.getInstance().scalingFactorY
}