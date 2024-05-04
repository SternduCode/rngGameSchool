package rngGame.main;

import java.util.ArrayList;
import java.util.List;

public enum Direction {

	N(-1.9635, -1.1781),
	NE(-1.1781, -0.3927),
	E(-0.3927, 0.3927),
	SE(0.3927, 1.1781),
	S(1.1781, 1.9635),
	SW(1.9635, 2.7489),
	W(2.7489, -2.7489),
	NW(-2.7489, -1.9635);

	private final double minAngle;
	private final double maxAngle;
	private boolean active = false;

	Direction(double minAngle, double maxAngle) {
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
	}

	public double getMinAngle() {
		return minAngle;
	}

	public double getMaxAngle() {
		return maxAngle;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		for (Direction direction: Direction.values()) {
			direction.active = false;
		}
		this.active = active;
	}

	public static Direction getDirectionFromAngle(double angle) {
		List<Direction> directions = new ArrayList<>();
		for (Direction direction: Direction.values()) {
			boolean isBetween = angle > direction.minAngle && angle < direction.maxAngle;
			if (
					(direction.minAngle > direction.maxAngle && (angle > direction.minAngle || angle < direction.maxAngle))
					    || isBetween
			) {
				directions.add(direction);
			}
		}
		if (!directions.isEmpty()) {
			return directions.get(0);
		} else {
			return E;
		}
	}
}
