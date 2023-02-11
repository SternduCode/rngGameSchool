package rngGame.main;

import java.awt.Button;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import rngGame.tile.ImgUtil;

public class AktionButton extends Pane{
	private final GamePanel gamepanel;
	private ImageView aktionbutton;
	private Image nichts,kann,druck;
	private boolean ifc;

	
	
	public AktionButton(GamePanel gamepanel) {
		this.gamepanel = gamepanel;
		aktionbutton = new ImageView(nichts);
		f11Scale();
		getChildren().add(aktionbutton);
		
		aktionbutton.setOnMousePressed(me -> {
			if(ifc) {
			aktionbutton.setImage(druck);
			}
		});
		aktionbutton.setOnMousePressed(me -> {
			if(ifc) {
			aktionbutton.setImage(kann);
			}
		});
		
		
	}
	
	public void f11Scale() {
		try {
			nichts = new Image(new FileInputStream("./res/gui/always/InteractionNichts.png"));
			kann = new Image(new FileInputStream("./res/gui/always/InteractionMoeglich.png"));
			druck = new Image(new FileInputStream("./res/gui/always/InteractionGedrueckt.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		nichts = ImgUtil.resizeImage(nichts, (int)nichts.getWidth(), (int)nichts.getHeight(), (int)(150* gamepanel.getScalingFactorX()), (int)(150*gamepanel.getScalingFactorY()));
		kann = ImgUtil.resizeImage(kann, (int)kann.getWidth(), (int)kann.getHeight(), (int)(150* gamepanel.getScalingFactorX()), (int)(150*gamepanel.getScalingFactorY()));
		druck = ImgUtil.resizeImage(druck, (int)druck.getWidth(), (int)druck.getHeight(), (int)(150* gamepanel.getScalingFactorX()), (int)(150*gamepanel.getScalingFactorY()));
		aktionbutton.setImage(nichts);
		
		aktionbutton.setLayoutX(gamepanel.SpielLaenge-220*gamepanel.getScalingFactorX());
		aktionbutton.setLayoutY(gamepanel.SpielHoehe-220*gamepanel.getScalingFactorY());
		
	}
	
	public void setInteractionbuttonKann(boolean ifc) {
		this.ifc = ifc;
		if(ifc) {
			aktionbutton.setImage(kann);
		} else {
			aktionbutton.setImage(nichts);
		} 
	}
}
