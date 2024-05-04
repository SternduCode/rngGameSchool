package rngGame.main;

import javafx.scene.Node;

public class UtilityFunctions {

	public static void setNodePosition(Node node, double x, double y) {
		node.setLayoutX(x);
		node.setLayoutY(y);
	}

	public static void setNodePositionScaling(Node node, double x, double y) {
		node.setLayoutX(x * WindowManager.getInstance().getScalingFactorX());
		node.setLayoutY(y * WindowManager.getInstance().getScalingFactorY());
	}

}
