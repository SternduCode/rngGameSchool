package rngGame.main;

import javafx.scene.shape.Shape;

@FunctionalInterface
public interface Collidable {

	Shape getCollisionBox();

}
