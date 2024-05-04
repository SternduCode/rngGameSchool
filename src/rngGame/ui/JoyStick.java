package rngGame.ui;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import rngGame.main.Direction;
import rngGame.main.WindowManager;
import rngGame.visual.AnimatedImage;

public class JoyStick extends Pane {

	private static final JoyStick INSTANCE = new JoyStick();

	private final AnimatedImage joyStick = new AnimatedImage("./res/gui/always/JoyschtickBaumwolle.png");

	private double x = 0.0;

	private double y = 0.0;

	private double theta = 0.0;

	private final Polygon line = new Polygon(
			0.0, 0.0,
			0.0, 0.0,
			0.0, 0.0
	);

	private final AnimatedImage background = new AnimatedImage("./res/gui/always/JoyschtickRand.png");

	private JoyStick() {
		background.setImgRequestedWidth(128);
		background.setImgRequestedHeight(128);

		joyStick.setImgRequestedWidth(64);
		joyStick.setImgRequestedHeight(64);

		background.scaleF11();
		joyStick.scaleF11();

		line.setStrokeWidth(5.0);
		line.setFill(new Color(1.0,1.0,1.0,0.5));


		getChildren().addAll(background, line, joyStick);

		setLayoutX(WindowManager.getInstance().getGameWidth() * .1);
		setLayoutY(WindowManager.getInstance().getGameHeight() * .8);



		// function of A & B: f1(x) = ( ( y(B) - y(A) ) / ( x(B) - x(A) ) ) * x + y(A) - ( ( y(B) - y(A) ) / ( x(B) - x(A) ) ) * x(A)

		// orthogonal function: f2(x) = -( ( y(B) - y(A) ) / ( x(B) - x(A) ) ) ^ (-1) * x

		// Zero points are of distance 1 from center: sqrt((-(((y(B)-y(A))/(x(B)-x(A))))^(-1) * x)^(2)+x^(2))-1

		// Take Zero points as X in f2(x) for Y

		// X01 = -X02

		// X01 = ( (y(B)-y(A)) / sqrt( x(B)^(2) - 2 * x(B) * x(A) + x(A)^(2) + y(B)^(2) - 2 * y(B) * y(A) + y(A)^(2) ) )

		joyStick.setLayoutX(background.getImgRequestedWidth() * .5 - joyStick.getImgRequestedWidth() * .5);
		joyStick.setLayoutY(background.getImgRequestedHeight() * .5 - joyStick.getImgRequestedHeight() * .5);

		line.getPoints().set(0, background.getImgRequestedWidth() * .5);
		line.getPoints().set(1, background.getImgRequestedHeight() * .5);

		line.getPoints().set(2, background.getImgRequestedWidth() * .5);
		line.getPoints().set(3, background.getImgRequestedHeight() * .5);

		line.getPoints().set(4, background.getImgRequestedWidth() * .5);
		line.getPoints().set(5, background.getImgRequestedHeight() * .5);

		setOnMousePressed(me -> {
			joyStick.setLayoutX(me.getX() - joyStick.getImgRequestedWidth() * .5);
			joyStick.setLayoutY(me.getY() - joyStick.getImgRequestedHeight() * .5);

			double x = calculateX(me.getX(), me.getY());

			double y1 = calculateY(x, me.getX(), me.getY()) * joyStick.getImgRequestedHeight() / 2.0;

			double y2 = calculateY(-x, me.getX(), me.getY()) * joyStick.getImgRequestedHeight() / 2.0;

			line.getPoints().set(2, me.getX() + x * joyStick.getImgRequestedWidth() / 2);
			line.getPoints().set(3, me.getY() + y1);

			line.getPoints().set(4, me.getX() - x * joyStick.getImgRequestedWidth() / 2);
			line.getPoints().set(5, me.getY() + y2);

			this.x = (me.getX() - background.getImgRequestedWidth() * .5) / joyStick.getImgRequestedWidth();
			this.y = (me.getY() - background.getImgRequestedHeight() * .5) / joyStick.getImgRequestedHeight();
			theta = Math.atan2(y, x);

			me.consume();
		});
		setOnMouseDragged(me -> {

			Point2D newPosition = pythagorasDistanceCap(
					me.getX() - background.getImgRequestedWidth() * .5,
					me.getY() - background.getImgRequestedHeight() * .5
			).add(background.getImgRequestedWidth() * .5, background.getImgRequestedHeight() * .5);

			double newX = newPosition.getX();
			double newY = newPosition.getY();

			joyStick.setLayoutX(newX - joyStick.getImgRequestedWidth() * .5);
			joyStick.setLayoutY(newY - joyStick.getImgRequestedHeight() * .5);

			double x = calculateX(newX, newY);

			double y1;
			double y2;

			if (x != 0.0)
				y1 = calculateY(x, newX, newY) * joyStick.getImgRequestedHeight() / 2;
			else
				y1 = joyStick.getImgRequestedHeight() * .5;

			if (x != 0.0)
				y2 = calculateY(-x, newX, newY) * joyStick.getImgRequestedHeight() / 2;
			else
				y2 = -joyStick.getImgRequestedHeight() * .5;

			line.getPoints().set(2, newX + x * joyStick.getImgRequestedWidth() / 2);
			line.getPoints().set(3, newY + y1);

			line.getPoints().set(4, newX - x * joyStick.getImgRequestedWidth() / 2);
			line.getPoints().set(5, newY + y2);

			this.x = (newX - background.getImgRequestedWidth() * .5) / joyStick.getImgRequestedWidth();
			this.y = (newY - background.getImgRequestedHeight() * .5) / joyStick.getImgRequestedHeight();
			theta = Math.atan2(this.y, this.x);

			me.consume();
		});
		setOnMouseReleased(me -> {
			joyStick.setLayoutX(background.getImgRequestedWidth() * .5 - joyStick.getImgRequestedWidth() * .5);
			joyStick.setLayoutY(background.getImgRequestedHeight() * .5 - joyStick.getImgRequestedHeight() * .5);

			line.getPoints().set(2, background.getImgRequestedWidth() * .5);
			line.getPoints().set(3, background.getImgRequestedHeight() * .5);

			line.getPoints().set(4, background.getImgRequestedWidth() * .5);
			line.getPoints().set(5, background.getImgRequestedHeight() * .5);

			x = 0.0;
			y = 0.0;

			me.consume();
		});
	}

	private Point2D pythagorasDistanceCap(double x, double y) {
		double a = Math.pow(x, 2);
		double b = Math.pow(y, 2);
		double distancePow2 = a + b;
		double fraction = a / distancePow2;
		double distance64pow2 = Math.pow(background.getImgRequestedWidth()/2.0, 2);
		if (distancePow2 > distance64pow2) {
			double newA = distance64pow2 * fraction;
			double newB = distance64pow2 - newA;
			int signX = 1;
			int signY = 1;
			if (x < 0) {
				signX = -1;
			}
			if (y < 0) {
				signY = -1;
			}
			return new Point2D(Math.sqrt(newA) * signX, Math.sqrt(newB) * signY);
		} else return new Point2D(x, y);
	}

	private double calculateX(double joyStickX, double joyStickY) {
		return (joyStickY + (background.getImgRequestedHeight() * -.5)) /
				       Math.sqrt(
							   Math.pow(joyStickX, 2) - joyStickX * background.getImgRequestedWidth() + Math.pow(background.getImgRequestedWidth() * .5, 2)
						             + Math.pow(joyStickY, 2) - joyStickY * background.getImgRequestedHeight() + Math.pow(background.getImgRequestedHeight() * .5, 2)
				       );
	}

	private double calculateY(double x, double joyStickX, double joyStickY) {
		return ( joyStickX + background.getImgRequestedWidth() * -.5 ) / ( joyStickY + background.getImgRequestedHeight() * -.5 ) * -x;
	}

	public Direction getDirection() {
		return Direction.getDirectionFromAngle(theta);
	}

	public static JoyStick getInstance() {
		return INSTANCE;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getTheta() {
		return theta;
	}

}
