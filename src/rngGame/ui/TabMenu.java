package rngGame.ui;

import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import rngGame.main.*;
import rngGame.tile.ImgUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class TabMenu.
 */
public class TabMenu extends Pane {

	/** The gamemenu. */
	private final ImageView gamemenu;

	/** The buttongroup. */
	private final Group buttongroup = new Group();

	/** The Y nbuttongroup. */
	private final Group YNbuttongroup = new Group();


	/** The inv B. */
	private final Button invB, queB, leavB, sureY, sureN;

	/** The blank. */
	private final ImageView blank;

	/** The surebackround. */
	private final ImageView surebackround;

	/** The sure Y 1. */
	private Image sureY1;

	/** The sure N 1. */
	private Image sureN1;

	/** The sure Y 2. */
	private Image sureY2;

	/** The sure N 2. */
	private Image sureN2;

	/** The inventory. */
	private final Inventory Inventory;

	/** The gamepanel. */
	private final GamePanel gamepanel;

	/** The no input. */
	private final AtomicBoolean noInput = new AtomicBoolean(false);

	/** The ab. */
	private final AtomicBoolean ab = new AtomicBoolean(false);
	/**
	 * Instantiates a new tab menu.
	 *
	 * @param gamepanel the gamepanel
	 * @throws FileNotFoundException the file not found exception
	 */
	public TabMenu(GamePanel gamepanel) throws FileNotFoundException {
		gamemenu = new ImageView(ImgUtil.getScaledImage("./res/gui/gamemenubackround.png"));

		blank = new ImageView(ImgUtil.getScaledImage("./res/gui/blackTransparent.png"));


		invB = new Button("./res/gui/invAbutton1.png");

		queB = new Button("./res/gui/queAbutton1.png");

		leavB = new Button("./res/gui/leavAbutton1.png");

		surebackround = new ImageView(ImgUtil.getScaledImage("./res/gui/Sure.png"));

		sureY = new Button("./res/gui/SureY.png");

		sureN = new Button("./res/gui/SureN.png");


		buttongroup.getChildren().addAll(invB,queB,leavB);
		YNbuttongroup.getChildren().addAll(sureY, sureN);


		getChildren().add(gamemenu);
		getChildren().add(buttongroup);
		getChildren().add(blank);
		getChildren().add(surebackround);
		getChildren().add(YNbuttongroup);


		blank.setVisible(false);
		gamemenu.setVisible(false);
		buttongroup.setVisible(false);
		YNbuttongroup.setVisible(false);
		surebackround.setVisible(false);

		setDisable(true);
		this.gamepanel = gamepanel;

		Inventory = new Inventory(gamepanel, this);
		getChildren().add(Inventory);
		Inventory.setDisable(true);

		Input.getInstance().setKeyHandler("inv", mod -> {
			if(!noInput.get()) {
				TranslateTransition ft = new TranslateTransition(Duration.millis(150), gamemenu);
				TranslateTransition ib1 = new TranslateTransition(Duration.millis(150), buttongroup);


				if (!ab.getAndSet(!ab.get())) {
					gamepanel.getVgp().setBlockUserInputs(true);
					setDisable(false);
					gamemenu.setVisible(true);
					buttongroup.setVisible(true);


					invB.setOnPressed(me -> {
						invB.init("./res/gui/invAbutton2.png");
					});
					invB.setOnReleased(me -> {
						invB.init("./res/gui/invAbutton1.png");

						Inventory.show();
					});

					queB.setOnPressed(me -> {
						queB.init("./res/gui/queAbutton2.png");
					});
					queB.setOnReleased(me -> {
						queB.init("./res/gui/queAbutton1.png");
					});


					leavB.setOnPressed(me -> {
						leavB.init("./res/gui/leavAbutton2.png");
					});
					leavB.setOnReleased(me -> {
						blank.setVisible(true);
						leavB.init("./res/gui/leavAbutton1.png");
						noInput.set(true);
						surebackround.setVisible(true);
						YNbuttongroup.setVisible(true);
					});

					sureN.setOnPressed(me -> {
						sureN.init("./res/gui/SureN2.png");
					});
					sureN.setOnReleased(me -> {
						sureN.init("./res/gui/SureN.png");
						blank.setVisible(false);
						noInput.set(false);
						surebackround.setVisible(false);
						YNbuttongroup.setVisible(false);
					});

					sureY.setOnPressed(me -> {
						sureY.init("./res/gui/SureY2.png");
					});
					sureY.setOnReleased(me -> {
						sureY.init("./res/gui/SureY.png");
						System.exit(0);
					});


					ft.setFromY(WindowManager.getInstance().getGameHeight() / 2);
					ib1.setFromY(WindowManager.getInstance().getGameHeight() / 2);
					ft.setToY(0);
					ib1.setToY(0);
					ft.play();
					ib1.play();


				} else {
					ft.setToY(WindowManager.getInstance().getGameHeight() / 2);
					ib1.setToY(WindowManager.getInstance().getGameHeight() / 2);
					ft.play();
					ib1.play();
					closeTabm(false);
					Inventory.endShow();
				}
			}
		}, KeyCode.TAB, false);
	}


	/**
	 * Close tabmenu.
	 *
	 * @param toggleState the toggle state
	 */
	public void closeTabm(boolean toggleState) {
		if(toggleState) ab.getAndSet(!ab.get());
		gamepanel.getVgp().setBlockUserInputs(false);

		Inventory.setDisable(true);

		new Thread(()->{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			gamemenu.setVisible(false);
			blank.setVisible(false);
			buttongroup.setVisible(false);
			setDisable(true);

		}).start();
	}
	/**
	 * F 11 scale.
	 */
	public void f11Scale() {
		gamemenu.setImage(ImgUtil.getScaledImage("./res/gui/gamemenubackround.png"));
		blank.setImage(ImgUtil.getScaledImage("./res/gui/blackTransparent.png"));

		invB.init("./res/gui/invAbutton1.png");

		queB.init("./res/gui/queAbutton1.png");

		leavB.init("./res/gui/leavAbutton1.png");

		surebackround.setImage(ImgUtil.getScaledImage("./res/gui/Sure.png"));
		sureY1 = ImgUtil.getScaledImage("./res/gui/SureY.png");
		sureY2 = ImgUtil.getScaledImage("./res/gui/SureY2.png");
		sureY.setImage(sureY1);

		sureN1 = ImgUtil.getScaledImage("./res/gui/SureN.png");
		sureN2 = ImgUtil.getScaledImage("./res/gui/SureN2.png");
		sureN.setImage(sureN1);

		Inventory.scaleF11();
	}

	/**
	 * Gets the inventory.
	 *
	 * @return the inventory
	 */
	public Inventory getInventory() { return Inventory; }
}
