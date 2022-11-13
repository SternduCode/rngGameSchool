package rngGame.buildings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import com.sterndu.json.JsonObject;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import rngGame.main.Input;
import rngGame.main.GamePanel;
import rngGame.tile.ImgUtil;

public class ContractsTable extends Building {

	public ContractsTable(Building building, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, buildings, cm, requestorB);
		init();
	}

	public ContractsTable(JsonObject building, GamePanel gp, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, buildings, cm, requestorB);
		init();
	}

	public ContractsTable(JsonObject building, GamePanel gp, String directory, List<Building> buildings, ContextMenu cm,
			ObjectProperty<Building> requestorB) {
		super(building, gp, directory, buildings, cm, requestorB);
		init();
	}

	private  ImageView contractBackround;
	private boolean iftest;
	private boolean inkreis;

	private void init() {
		try {
			Image wi = new Image(new FileInputStream("./res/Contractstuff/Test.png"));
			wi=ImgUtil.resizeImage(
					wi,(int) wi.getWidth(), (int) wi.getHeight(), (int) (wi.getWidth() * gamepanel.getScalingFactorX()),
					(int) (wi.getHeight() * gamepanel.getScalingFactorY()));
			contractBackround = new ImageView(wi);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		contractBackround.setX(gamepanel.getPlayer().getScreenX() - contractBackround.getImage().getWidth() / 2 + 48);
		contractBackround.setY(gamepanel.getPlayer().getScreenY() - contractBackround.getImage().getHeight() / 2 + 32);
		contractBackround.setVisible(false);
		gamepanel.getChildren().add(contractBackround);

		Input.getInstance().setKeyHandler("contractbackround", mod -> {
			if(inkreis) {
				if ((iftest = !iftest)) contractBackround.setVisible(true);
				else contractBackround.setVisible(false);
			}
		}, KeyCode.ENTER, false);

		getMiscBoxHandler().put("table", (gpt, self) -> {
			inkreis = true;
		});
	}
	@Override
	public void update(long milis) {
		inkreis = false;
		super.update(milis);
		if(!inkreis) {
			iftest = false;
			contractBackround.setVisible(false);
			gamepanel.getChildren().remove(contractBackround);
		} else {
			contractBackround.setX(gamepanel.getPlayer().getScreenX() - contractBackround.getImage().getWidth() / 2 + gamepanel.getPlayer().getWidth() / 2);
			contractBackround.setY(gamepanel.getPlayer().getScreenY() - contractBackround.getImage().getHeight() / 2 + gamepanel.getPlayer().getHeight() / 2);
			if(!gamepanel.getChildren().contains(contractBackround)) 
				gamepanel.getChildren().add(contractBackround);
		}
	}
}
