module rngGame {
	exports rngGame.main;
	exports rngGame.tile;
	exports rngGame.entity;
	exports rngGame.buildings;
	exports rngGame.ui;
	exports rngGame.stats;
	exports rngGame.visual;

	requires javafx.base;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;

	requires javafx.swing;
	requires transitive java.desktop;
	requires jdk.zipfs;
	requires java.xml;
	requires javafx.media;
	
	requires transitive Lib;

	opens rngGame.main to javafx.graphics;
}
