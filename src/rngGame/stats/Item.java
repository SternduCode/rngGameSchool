package rngGame.stats;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Item {
	protected ImageView t1;
	
	public Item(String ordner, String item) {
		try {
			t1 = new ImageView(new Image(new FileInputStream(new File("./res/Items/" + ordner,item + ".png"))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
