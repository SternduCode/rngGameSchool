module rngGame {
	exports rngGame.main;
	exports rngGame.tile;
	exports rngGame.entity;
	exports rngGame.buildings;
	exports rngGame.ui;

	requires javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;

	requires transitive com.sterndu.JSONLib;
	requires javafx.swing;
	requires java.desktop;
	requires jdk.zipfs;

	opens rngGame.main to javafx.graphics;
}