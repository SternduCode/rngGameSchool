module rngGAME {
	exports rngGAME;
	exports tile;
	exports entity;
	exports buildings;

	requires javafx.base;
	requires transitive javafx.graphics;
	requires javafx.controls;

	requires transitive com.sterndu.JSONLib;

	opens rngGAME to javafx.graphics;
}