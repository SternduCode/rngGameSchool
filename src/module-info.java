module rngGAME {
	exports rngGAME;
	exports tile;
	exports entity;

	requires javafx.base;
	requires transitive javafx.graphics;
	requires javafx.controls;

	opens rngGAME to javafx.graphics;
}