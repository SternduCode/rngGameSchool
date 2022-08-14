package rngGame.main;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.entity.Player;
import rngGame.tile.ImgUtil;

public abstract class GameObject extends Pane {

	protected double x = 0d, y = 0d, fps = 7.5;
	protected Map<String, List<Image>> images;

	protected Map<String, Polygon> collisionBoxes;

	protected ImageView iv;

	private final Menu menu, imagesM;

	protected String currentKey, directory;
	protected Map<String, String> textureFiles;

	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected SpielPanel gp;
	protected Group collisionBoxViewGroup;
	protected long spriteCounter = 0;
	protected int spriteNum = 0;
	private String lastKey;
	protected boolean background;
	private final MenuItem position, fpsI, currentKeyI, directoryI, origDim, reqDim, backgroundI, reloadTextures;

	public GameObject(SpielPanel gp, String directory) {
		images = new HashMap<>();
		textureFiles = new HashMap<>();
		collisionBoxes = new HashMap<>();
		currentKey = "default";
		lastKey = currentKey;
		collisionBoxes.put(currentKey, new Polygon());
		collisionBoxViewGroup = new Group(collisionBoxes.get(currentKey));
		collisionBoxViewGroup.setDisable(true);
		collisionBoxViewGroup.setVisible(false);
		iv = new ImageView();
		iv.setDisable(true);
		getChildren().addAll(iv, collisionBoxViewGroup);
		this.gp = gp;
		this.directory = directory;

		menu = new Menu("GameObject");
		imagesM = new Menu("Images");
		position = new MenuItem();
		fpsI = new MenuItem();
		currentKeyI = new MenuItem();
		directoryI = new MenuItem();
		origDim = new MenuItem();
		reqDim = new MenuItem();
		backgroundI = new MenuItem();
		reloadTextures = new MenuItem("Reload Textures");
		position.setOnAction(this::handleContextMenu);
		fpsI.setOnAction(this::handleContextMenu);
		currentKeyI.setOnAction(this::handleContextMenu);
		directoryI.setOnAction(this::handleContextMenu);
		origDim.setOnAction(this::handleContextMenu);
		reqDim.setOnAction(this::handleContextMenu);
		backgroundI.setOnAction(this::handleContextMenu);
		reloadTextures.setOnAction(this::handleContextMenu);
		menu.getItems().addAll(position, fpsI, imagesM, currentKeyI, directoryI, origDim, reqDim, backgroundI,
				reloadTextures);
	}

	private void handleContextMenu(ActionEvent e) {
		MenuItem source = (MenuItem) e.getSource();
		ContextMenu cm = source.getParentPopup();
		if (source == position) {

		} else if (source == fpsI) {

		} else if (source == currentKeyI) {

		} else if (source == directoryI) {

		} else if (source == origDim) {

		} else if (source == reqDim) {

		} else if (source == backgroundI) {

		} else if (source == reloadTextures) {

		} else if (source.getText().equals("add Texture")) {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File("."));
			fc.getExtensionFilters().add(new ExtensionFilter(
					"A file containing an Image", "*.png"));
			File f = fc.showOpenDialog(cm.getScene().getWindow());
			if (f == null || !f.exists()) return;
			try {
				Path p1 = f.toPath();
				Path p2 = new File("./res/" + directory + "/" + f.getName()).toPath();
				Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
				System.out.println(p2);
				TextInputDialog dialog = new TextInputDialog("walter");
				dialog.setTitle("Text Input Dialog");
				dialog.setHeaderText("Look, a Text Input Dialog");
				dialog.setContentText("Please enter your name:");

				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) System.out.println("Your name: " + result.get());

				// The Java 8 way to get the response value (with lambda expression).
				result.ifPresent(name -> System.out.println("Your name: " + name));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			System.out.println(f);
		} else {

		}
	}

	protected List<Image> getAnimatedImages(String key, String path) throws FileNotFoundException {
		List<Image> li = new ArrayList<>();
		Image img = new Image(new FileInputStream("./res/" + directory + "/" + path));

		for (int i = 0; i < img.getWidth(); i += origWidth) {
			WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
			li.add(ImgUtil.resizeImage(wi,
					(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
		}
		String[] sp = path.split("[.]");
		if (new File("./res/collisions/" + directory + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
		+ ".collisionbox").exists())
			try {
				RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/" + directory + "/"
						+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
						+ ".collisionbox"), "rws");
				raf.seek(0l);
				int length = raf.readInt();
				Polygon collisionBox = collisionBoxes.get(key);
				if (collisionBox == null) collisionBoxes.put(key, collisionBox = new Polygon());
				for (int i = 0; i < length; i++) collisionBox.getPoints().add(raf.readDouble());
			} catch (IOException e) {
				e.printStackTrace();
			}
		images.put(key, li);
		textureFiles.put(key, path);
		return li;
	}

	public boolean collides(GameObject collidable) {
		if (getCollisionBox().getPoints().size() > 0) {
			Shape intersect = Shape.intersect(collidable.getCollisionBox(), getCollisionBox());
			return !intersect.getBoundsInLocal().isEmpty();
		} else return false;
	}
	public Polygon getCollisionBox() {
		return collisionBoxes.get(currentKey);
	}
	public String getCurrentKey() { return currentKey; }

	public Image getFirstImage() { return images.values().stream().findFirst().get().get(0); }

	public Map<String, List<Image>> getImages() { return images; }

	public List<Menu> getMenus() {
		position.setText("Position: " + x + " " + y);
		fpsI.setText("FPS: " + fps);
		currentKeyI.setText("Current Key: " + currentKey);
		directoryI.setText("Directory: " + directory);
		origDim.setText("Original Dimensions: " + origWidth + " " + origHeight);
		reqDim.setText("Requested Dimensions: " + reqWidth + " " + reqHeight);
		backgroundI.setText("Background: " + background);
		imagesM.getItems().clear();
		for (Entry<String, String> en: textureFiles.entrySet()) {
			MenuItem img = new MenuItem(en.getKey() + " : " + en.getValue());
			img.setOnAction(this::handleContextMenu);
			imagesM.getItems().add(img);
		}
		MenuItem img = new MenuItem("add Texture");
		img.setOnAction(this::handleContextMenu);
		imagesM.getItems().add(img);
		List<Menu> li = new ArrayList<>();
		li.add(menu);
		return li;
	}

	public double getX() { return x; }

	public double getY() { return y; }

	public boolean isBackground() { return background; }

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void update() {
		if (System.getProperty("coll").equals("true"))
			collisionBoxViewGroup.setVisible(true);
		else
			collisionBoxViewGroup.setVisible(false);

		if (System.currentTimeMillis() > spriteCounter + 1000 / fps) {
			spriteCounter = System.currentTimeMillis();
			spriteNum++;
			if (spriteNum >= images.get(currentKey).size()) spriteNum = 0;
			iv.setImage(images.get(currentKey).get(spriteNum));
		}
		if (!lastKey.equals(currentKey)) {
			if (spriteNum >= images.get(currentKey).size()) spriteNum = 0;
			iv.setImage(images.get(currentKey).get(spriteNum));
			lastKey = currentKey;
			collisionBoxViewGroup.getChildren().clear();
			collisionBoxViewGroup.getChildren().add(getCollisionBox());
		}

		Player p = gp.getPlayer();

		double screenX = x - p.x + p.screenX;
		double screenY = y - p.y + p.screenY;

		setLayoutX(screenX);
		setLayoutY(screenY);

		if (x + reqWidth > p.x - p.screenX
				&& x - reqWidth < p.x + p.screenX
				&& y + reqHeight > p.y - p.screenY
				&& y - reqHeight < p.y + p.screenY)
			setVisible(true);
		else setVisible(false);
	}

}
