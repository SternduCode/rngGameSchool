package rngGame.main;

import java.io.*;
import java.util.*;
import javafx.scene.Group;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import rngGame.entity.Player;
import rngGame.tile.ImgUtil;

public abstract class GameObject extends Pane {

	protected double x = 0d, y = 0d, fps = 7.5;
	protected Map<String, List<Image>> images;

	protected Map<String, Polygon> collisionBoxes;

	protected ImageView iv;

	protected String currentKey, directory;
	protected Map<String, String> textureFiles;

	protected int reqWidth, reqHeight, origWidth, origHeight;
	protected SpielPanel gp;
	protected Group collisionBoxViewGroup;
	protected long spriteCounter = 0;
	protected int spriteNum = 0;
	private String lastKey;
	protected boolean background;
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
		else setVisible(true);
	}

}
