package rngGame.ui;

import java.util.*;
import javafx.css.*;
import javafx.event.EventDispatchChain;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.shape.*;
import javafx.util.Callback;

public class Ring extends Path {

	private final double x,y,radius,innerRadius;
	private final Shape ring;

	public Ring(double x, double y, double radius, double innerRadius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.innerRadius = innerRadius;
		ring = Shape.subtract(new Circle(x, y, radius), new Circle(x, y, innerRadius));
	}

	@Override
	public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
		return ring.buildEventDispatchChain(tail);
	}

	@Override
	public double computeAreaInScreen() {
		return ring.computeAreaInScreen();
	}

	@Override
	public boolean contains(double localX, double localY) {
		return ring.contains(localX, localY);
	}

	@Override
	public boolean contains(Point2D localPoint) {
		return ring.contains(localPoint);
	}

	@Override
	public boolean equals(Object obj) {
		return ring.equals(obj);
	}

	@Override
	public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
		ring.executeAccessibleAction(action, parameters);
	}

	@Override
	public double getBaselineOffset() { return ring.getBaselineOffset(); }

	@Override
	public Orientation getContentBias() { return ring.getContentBias(); }

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() { return ring.getCssMetaData(); }

	public double getInnerRadius() { return innerRadius; }

	public double getRadius() { return radius; }

	@Override
	public Node getStyleableNode() { return ring.getStyleableNode(); }

	@Override
	public Styleable getStyleableParent() { return ring.getStyleableParent(); }

	@Override
	public String getTypeSelector() { return ring.getTypeSelector(); }

	@Override
	public Object getUserData() { return ring.getUserData(); }

	public double getX() { return x; }

	public double getY() { return y; }

	@Override
	public int hashCode() {
		return ring.hashCode();
	}

	@Override
	public boolean hasProperties() {
		return ring.hasProperties();
	}

	@Override
	public boolean intersects(Bounds localBounds) {
		return ring.intersects(localBounds);
	}

	@Override
	public boolean intersects(double localX, double localY, double localWidth, double localHeight) {
		return ring.intersects(localX, localY, localWidth, localHeight);
	}

	@Override
	public boolean isResizable() { return ring.isResizable(); }

	@Override
	public Bounds localToParent(Bounds localBounds) {
		return ring.localToParent(localBounds);
	}

	@Override
	public Point2D localToParent(double localX, double localY) {
		return ring.localToParent(localX, localY);
	}

	@Override
	public Point3D localToParent(double x, double y, double z) {
		return ring.localToParent(x, y, z);
	}

	@Override
	public Point2D localToParent(Point2D pt) {
		return ring.localToParent(pt);
	}

	@Override
	public Point3D localToParent(Point3D localPoint) {
		return ring.localToParent(localPoint);
	}

	@Override
	public Bounds localToScene(Bounds localBounds) {
		return ring.localToScene(localBounds);
	}

	@Override
	public Bounds localToScene(Bounds localBounds, boolean rootScene) {
		return ring.localToScene(localBounds, rootScene);
	}

	@Override
	public Point2D localToScene(double localX, double localY) {
		return ring.localToScene(localX, localY);
	}

	@Override
	public Point2D localToScene(double x, double y, boolean rootScene) {
		return ring.localToScene(x, y, rootScene);
	}

	@Override
	public Point3D localToScene(double x, double y, double z) {
		return ring.localToScene(x, y, z);
	}

	@Override
	public Point3D localToScene(double x, double y, double z, boolean rootScene) {
		return ring.localToScene(x, y, z, rootScene);
	}

	@Override
	public Point2D localToScene(Point2D pt) {
		return ring.localToScene(pt);
	}

	@Override
	public Point2D localToScene(Point2D localPoint, boolean rootScene) {
		return ring.localToScene(localPoint, rootScene);
	}

	@Override
	public Point3D localToScene(Point3D localPoint) {
		return ring.localToScene(localPoint);
	}

	@Override
	public Point3D localToScene(Point3D localPoint, boolean rootScene) {
		return ring.localToScene(localPoint, rootScene);
	}

	@Override
	public Bounds localToScreen(Bounds localBounds) {
		return ring.localToScreen(localBounds);
	}

	@Override
	public Point2D localToScreen(double localX, double localY) {
		return ring.localToScreen(localX, localY);
	}

	@Override
	public Point2D localToScreen(double localX, double localY, double localZ) {
		return ring.localToScreen(localX, localY, localZ);
	}

	@Override
	public Point2D localToScreen(Point2D localPoint) {
		return ring.localToScreen(localPoint);
	}

	@Override
	public Point2D localToScreen(Point3D localPoint) {
		return ring.localToScreen(localPoint);
	}

	@Override
	public Node lookup(String selector) {
		return ring.lookup(selector);
	}

	@Override
	public Set<Node> lookupAll(String selector) {
		return ring.lookupAll(selector);
	}

	@Override
	public double maxHeight(double width) {
		return ring.maxHeight(width);
	}

	@Override
	public double maxWidth(double height) {
		return ring.maxWidth(height);
	}

	@Override
	public double minHeight(double width) {
		return ring.minHeight(width);
	}

	@Override
	public double minWidth(double height) {
		return ring.minWidth(height);
	}

	@Override
	public Bounds parentToLocal(Bounds parentBounds) {
		return ring.parentToLocal(parentBounds);
	}

	@Override
	public Point2D parentToLocal(double parentX, double parentY) {
		return ring.parentToLocal(parentX, parentY);
	}

	@Override
	public Point3D parentToLocal(double parentX, double parentY, double parentZ) {
		return ring.parentToLocal(parentX, parentY, parentZ);
	}

	@Override
	public Point2D parentToLocal(Point2D pt) {
		return ring.parentToLocal(pt);
	}

	@Override
	public Point3D parentToLocal(Point3D parentPoint) {
		return ring.parentToLocal(parentPoint);
	}

	@Override
	public double prefHeight(double width) {
		return ring.prefHeight(width);
	}

	@Override
	public double prefWidth(double height) {
		return ring.prefWidth(height);
	}

	@Override
	public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
		return ring.queryAccessibleAttribute(attribute, parameters);
	}

	@Override
	public void relocate(double x, double y) {
		ring.relocate(x, y);
	}

	@Override
	public void requestFocus() {
		ring.requestFocus();
	}

	@Override
	public void resize(double width, double height) {
		ring.resize(width, height);
	}

	@Override
	public void resizeRelocate(double x, double y, double width, double height) {
		ring.resizeRelocate(x, y, width, height);
	}

	@Override
	public Bounds sceneToLocal(Bounds sceneBounds) {
		return ring.sceneToLocal(sceneBounds);
	}

	@Override
	public Bounds sceneToLocal(Bounds bounds, boolean rootScene) {
		return ring.sceneToLocal(bounds, rootScene);
	}

	@Override
	public Point2D sceneToLocal(double sceneX, double sceneY) {
		return ring.sceneToLocal(sceneX, sceneY);
	}

	@Override
	public Point2D sceneToLocal(double x, double y, boolean rootScene) {
		return ring.sceneToLocal(x, y, rootScene);
	}

	@Override
	public Point3D sceneToLocal(double sceneX, double sceneY, double sceneZ) {
		return ring.sceneToLocal(sceneX, sceneY, sceneZ);
	}

	@Override
	public Point2D sceneToLocal(Point2D pt) {
		return ring.sceneToLocal(pt);
	}

	@Override
	public Point2D sceneToLocal(Point2D point, boolean rootScene) {
		return ring.sceneToLocal(point, rootScene);
	}

	@Override
	public Point3D sceneToLocal(Point3D scenePoint) {
		return ring.sceneToLocal(scenePoint);
	}

	@Override
	public Bounds screenToLocal(Bounds screenBounds) {
		return ring.screenToLocal(screenBounds);
	}

	@Override
	public Point2D screenToLocal(double screenX, double screenY) {
		return ring.screenToLocal(screenX, screenY);
	}

	@Override
	public Point2D screenToLocal(Point2D screenPoint) {
		return ring.screenToLocal(screenPoint);
	}

	@Override
	public void setUserData(Object value) {
		ring.setUserData(value);
	}

	@Override
	public void snapshot(Callback<SnapshotResult, Void> callback, SnapshotParameters params, WritableImage image) {
		ring.snapshot(callback, params, image);
	}

	@Override
	public WritableImage snapshot(SnapshotParameters params, WritableImage image) {
		return ring.snapshot(params, image);
	}

	@Override
	public Dragboard startDragAndDrop(TransferMode... transferModes) {
		return ring.startDragAndDrop(transferModes);
	}

	@Override
	public void startFullDrag() {
		ring.startFullDrag();
	}

	@Override
	public void toBack() {
		ring.toBack();
	}

	@Override
	public void toFront() {
		ring.toFront();
	}

	@Override
	public boolean usesMirroring() {
		return ring.usesMirroring();
	}



}
