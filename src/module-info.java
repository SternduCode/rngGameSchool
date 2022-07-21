module rngGAME {
	exports rngGAME;
	exports tile;
	exports entity;

	requires javafx.base;
	requires javafx.graphics;

	opens rngGAME to javafx.graphics;
}