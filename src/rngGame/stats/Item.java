package rngGame.stats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import rngGame.buildings.TreasureChest;
import rngGame.main.Input;

public class Item {
	protected ImageView t1;
	
	public Item(String ordner, String item) {
		try {
			t1 = new ImageView(new Image(new FileInputStream(new File("./res/Items/" + ordner,item + ".png"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Item [t1=" + t1 + "]";
	}
}
