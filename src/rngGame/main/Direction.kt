package rngGame.main

//0.3927 1.1781 1.9635 2.7489
enum class Direction(val minAngle: Double, val maxAngle: Double) {
	N(-1.9635, -1.1781),
	NE(-1.1781, -0.3927),
	E(-0.3927, 0.3927),
	SE(0.3927, 1.1781),
	S(1.1781, 1.9635),
	SW(1.9635, 2.7489),
	W(2.7489, -2.7489),
	NW(-2.7489, -1.9635);

	companion object {
		fun getDirectionFromAngle(angle: Double): Direction {
			return entries.firstOrNull {
				if (it.minAngle > it.maxAngle)
					angle > it.minAngle || angle < it.maxAngle
				else
					angle > it.minAngle && angle < it.maxAngle
			} ?: E
		}
	}
}