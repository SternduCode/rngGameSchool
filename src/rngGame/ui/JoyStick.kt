package rngGame.ui

import javafx.scene.layout.Pane
import javafx.scene.shape.Polygon
import rngGame.main.Direction
import rngGame.main.WindowManager
import rngGame.visual.AnimatedImage
import kotlin.math.*

object JoyStick: Pane() {

	private val joyStick = AnimatedImage("./res/gui/always/JoyschtickBaumwolle.png")

	private var _x = 0.0

	private var _y = 0.0

	private var _theta = 0.0

	val x
		get() = _x

	val y
		get() = _y

	val theta
		get() = _theta

	private val line = Polygon(
		0.0, 0.0,
		0.0, 0.0,
		0.0, 0.0
	)

	private val background = AnimatedImage("./res/gui/always/JoyschtickRand.png")

	init {
		background.imgRequestedWidth = 128
		background.imgRequestedHeight = 128

		joyStick.imgRequestedWidth = 64
		joyStick.imgRequestedHeight = 64

		background.scaleF11()
		joyStick.scaleF11()

		line.strokeWidth = 5.0
		line.fill = Color(1.0,1.0,1.0,0.5)


		children.addAll(background, line, joyStick)

		layoutX = WindowManager.getInstance().gameWidth * .1
		layoutY = WindowManager.getInstance().gameHeight * .8



		// function of A & B: f1(x) = ( ( y(B) - y(A) ) / ( x(B) - x(A) ) ) * x + y(A) - ( ( y(B) - y(A) ) / ( x(B) - x(A) ) ) * x(A)

		// orthogonal function: f2(x) = -( ( y(B) - y(A) ) / ( x(B) - x(A) ) ) ^ (-1) * x

		// Zero points are of distance 1 from center: sqrt((-(((y(B)-y(A))/(x(B)-x(A))))^(-1) * x)^(2)+x^(2))-1

		// Take Zero points as X in f2(x) for Y

		// X01 = -X02

		// X01 = ( (y(B)-y(A)) / sqrt( x(B)^(2) - 2 * x(B) * x(A) + x(A)^(2) + y(B)^(2) - 2 * y(B) * y(A) + y(A)^(2) ) )

		joyStick.layoutX = background.imgRequestedWidth * .5 - joyStick.imgRequestedWidth * .5
		joyStick.layoutY = background.imgRequestedHeight * .5 - joyStick.imgRequestedHeight * .5

		line.points[0] = background.imgRequestedWidth * .5
		line.points[1] = background.imgRequestedHeight * .5

		line.points[2] = background.imgRequestedWidth * .5
		line.points[3] = background.imgRequestedHeight * .5

		line.points[4] = background.imgRequestedWidth * .5
		line.points[5] = background.imgRequestedHeight * .5

		setOnMousePressed {
			joyStick.layoutX = it.x - joyStick.imgRequestedWidth * .5
			joyStick.layoutY = it.y - joyStick.imgRequestedHeight * .5

			val x = calculateX(it.x, it.y);

			val y1 = calculateY(x, it.x, it.y) * joyStick.imgRequestedHeight / 2

			val y2 = calculateY(-x, it.x, it.y) * joyStick.imgRequestedHeight / 2

			line.points[2] = it.x + x * joyStick.imgRequestedWidth / 2
			line.points[3] = it.y + y1

			line.points[4] = it.x - x * joyStick.imgRequestedWidth / 2
			line.points[5] = it.y + y2

			_x = (it.x - background.imgRequestedWidth * .5) / joyStick.imgRequestedWidth
			_y = (it.y - background.imgRequestedHeight * .5) / joyStick.imgRequestedHeight
			_theta = atan2(_y, _x)

			it.consume()
		}
		setOnMouseDragged {

			val (newX, newY) = pythagorasDistanceCap(it.x - background.imgRequestedWidth * .5, it.y - background.imgRequestedHeight * .5)
				.let { (x, y) -> (x + background.imgRequestedWidth * .5) to (y + background.imgRequestedHeight * .5) }

			joyStick.layoutX = newX - joyStick.imgRequestedWidth * .5
			joyStick.layoutY = newY - joyStick.imgRequestedHeight * .5

			val x = calculateX(newX, newY)

			val y1 = if (x != 0.0)
				calculateY(x, newX, newY) * joyStick.imgRequestedHeight / 2
			else
				joyStick.imgRequestedHeight * .5

			val y2 = if (x != 0.0)
				calculateY(-x, newX, newY) * joyStick.imgRequestedHeight / 2
			else
				-joyStick.imgRequestedHeight * .5

			line.points[2] = newX + x * joyStick.imgRequestedWidth / 2
			line.points[3] = newY + y1

			line.points[4] = newX - x * joyStick.imgRequestedWidth / 2
			line.points[5] = newY + y2

			_x = (newX - background.imgRequestedWidth * .5) / joyStick.imgRequestedWidth
			_y = (newY - background.imgRequestedHeight * .5) / joyStick.imgRequestedHeight
			_theta = atan2(_y, _x)

			it.consume()
		}
		setOnMouseReleased {
			joyStick.layoutX = background.imgRequestedWidth * .5 - joyStick.imgRequestedWidth * .5
			joyStick.layoutY = background.imgRequestedHeight * .5 - joyStick.imgRequestedHeight * .5

			line.points[2] = background.imgRequestedWidth * .5
			line.points[3] = background.imgRequestedHeight * .5

			line.points[4] = background.imgRequestedWidth * .5
			line.points[5] = background.imgRequestedHeight * .5

			_x = 0.0
			_y = 0.0

			it.consume()
		}
	}

	private fun pythagorasDistanceCap(x: Double, y: Double): Pair<Double, Double> {
		val a = x.pow(2)
		val b = y.pow(2)
		val sum = a + b
		val fraction = a / sum
		val distance64pow2 = (background.imgRequestedWidth/2.0).pow(2)
		val distancePow2 = a + b
		return if (distancePow2 > distance64pow2) {
			val newA = distance64pow2 * fraction
			val newB = distance64pow2 - newA
			(sqrt(newA) * if (x < 0) -1 else 1) to (sqrt(newB) * if (y < 0) -1 else 1)
		} else x to y
	}

	private fun calculateX(joyStickX: Double, joyStickY: Double): Double {
		return (joyStickY + (background.imgRequestedHeight * -.5)) /
				sqrt( joyStickX.pow(2) - joyStickX * background.imgRequestedWidth + (background.imgRequestedWidth * .5).pow(2)
						+ joyStickY.pow(2) - joyStickY * background.imgRequestedHeight + (background.imgRequestedHeight * .5).pow(2) )
	}

	private fun calculateY(x: Double, joyStickX: Double, joyStickY: Double): Double {
		return ( joyStickX + background.imgRequestedWidth * -.5 ) / ( joyStickY + background.imgRequestedHeight * -.5 ) * -x
	}

	fun getDirection(): Direction {
		return Direction.getDirectionFromAngle(theta)
	}

}
