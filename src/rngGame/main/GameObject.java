package rngGame.main;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import com.sterndu.json.*;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;
import rngGame.entity.Player;
import rngGame.tile.ImgUtil;
import rngGame.ui.*;

public class GameObject extends Pane implements JsonValue {

	protected double x = 0d, y = 0d, fps = 7.5;
	protected Map<String, List<Image>> images;

	protected Map<String, Polygon> collisionBoxes;

	protected ImageView iv;

	protected List<Runnable> removeCallbacks;

	private Runnable updateXY, updateReqDim;

	private final Menu menu, imagesM;

	protected String currentKey, directory;

	protected int layer;

	protected JsonObject extraData;

	protected boolean slave = false;
	protected List<GameObject> slaves;
	protected GameObject master;
	protected Map<String, String> textureFiles;

	protected int reqWidth, reqHeight, origWidth, origHeight;

	protected SpielPanel gp;

	protected Group collisionBoxViewGroup;

	protected long spriteCounter = 0;
	protected int spriteNum = 0;
	private String lastKey;
	protected boolean background;
	private final MenuItem position, fpsI, currentKeyI, directoryI, origDim, reqDim, backgroundI, layerI,
	reloadTextures,
	remove;
	@SuppressWarnings("unchecked")
	public GameObject(GameObject gameObject, List<? extends GameObject> gameObjects,
			ContextMenu cm,
			ObjectProperty<? extends GameObject> requestor) {
		removeCallbacks = new ArrayList<>();

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

		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				((ObjectProperty<GameObject>) requestor).set(this);
				cm.getItems().clear();
				cm.getItems().addAll(getMenus());
				cm.show(gp.getViewGroups().get(layer), e.getScreenX(), e.getScreenY());
			}
		});

		x = gameObject.x;
		y = gameObject.y;
		origWidth = gameObject.origWidth;
		origHeight = gameObject.origHeight;
		reqWidth = gameObject.reqWidth;
		reqHeight = gameObject.reqHeight;
		fps = gameObject.fps;
		layer = gameObject.layer;

		gp = gameObject.gp;
		directory = gameObject.directory;
		background = gameObject.background;
		currentKey = gameObject.currentKey;
		images = gameObject.images;
		textureFiles = gameObject.textureFiles;
		gameObject.collisionBoxes.forEach((key, poly) -> {
			Polygon collisionBox = collisionBoxes.get(key);
			if (collisionBox == null) collisionBoxes.put(key, collisionBox = new Polygon());
			collisionBox.getPoints().addAll(poly.getPoints());
			collisionBox.setFill(poly.getFill());
		});

		extraData = gameObject.extraData;

		menu = new Menu("GameObject");
		imagesM = new Menu("Images");
		position = new MenuItem();
		fpsI = new MenuItem();
		currentKeyI = new MenuItem();
		directoryI = new MenuItem();
		origDim = new MenuItem();
		reqDim = new MenuItem();
		backgroundI = new MenuItem();
		layerI = new MenuItem();
		reloadTextures = new MenuItem("Reload Textures");
		remove = new MenuItem("Remove");
		position.setOnAction(this::handleContextMenu);
		fpsI.setOnAction(this::handleContextMenu);
		currentKeyI.setOnAction(this::handleContextMenu);
		directoryI.setOnAction(this::handleContextMenu);
		origDim.setOnAction(this::handleContextMenu);
		reqDim.setOnAction(this::handleContextMenu);
		backgroundI.setOnAction(this::handleContextMenu);
		layerI.setOnAction(this::handleContextMenu);
		reloadTextures.setOnAction(this::handleContextMenu);
		remove.setOnAction(this::handleContextMenu);
		menu.getItems().addAll(position, fpsI, imagesM, currentKeyI, directoryI, origDim, reqDim, backgroundI, layerI,
				reloadTextures, remove);

		master = gameObject;
		slave = true;
		if (master.slaves == null) {
			Runnable[] r = new Runnable[1];
			master.removeCallbacks.add(r[0] = () -> {
				GameObject m = master.slaves.remove(0);
				m.slave = false;
				if (master.slaves.size() > 0) {
					m.slaves = master.slaves;
					for (GameObject s: m.slaves)
						s.master = m;
					m.removeCallbacks.add(r[0]);
				}
			});
			master.slaves = new ArrayList<>();
		}
		master.slaves.add(this);
		removeCallbacks.add(() -> {
			if (slave) master.slaves.remove(this);
		});
		if (gameObjects != null) {
			((List<GameObject>) gameObjects).add(this);
			removeCallbacks.add(() -> {
				gameObjects.remove(this);
			});
		}

		addToView();
	}


	@SuppressWarnings("unchecked")
	public GameObject(JsonObject gameObject, SpielPanel gp, String directory, List<? extends GameObject> gameObjects,
			ContextMenu cm,
			ObjectProperty<? extends GameObject> requestor) {
		this.gp = gp;
		this.directory = directory;

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

		removeCallbacks = new ArrayList<>();
		setOnContextMenuRequested(e -> {
			if (System.getProperty("edit").equals("true")) {
				((ObjectProperty<GameObject>) requestor).set(this);
				cm.getItems().clear();
				cm.getItems().addAll(getMenus());
				cm.show(gp.getViewGroups().get(layer), e.getScreenX(), e.getScreenY());
			}
		});

		if (gameObject != null) {
			origWidth = ((NumberValue) ((JsonArray) gameObject.get("originalSize")).get(0)).getValue().intValue();
			origHeight = ((NumberValue) ((JsonArray) gameObject.get("originalSize")).get(1)).getValue().intValue();

			if (((JsonArray) gameObject.get("position")).get(0) instanceof JsonArray ja) {
				boolean secondMultiPlexer = ((JsonArray) gameObject.get("requestedSize")).get(0) instanceof JsonArray;
				try {
					slaves = new ArrayList<>();
					for (int i = 1; i < ((JsonArray) gameObject.get("position")).size(); i++) {
						GameObject b = this
								.getClass().getDeclaredConstructor(this.getClass(), List.class,
										ContextMenu.class, ObjectProperty.class)
								.newInstance(this, gameObjects, cm, requestor);
						b.setPosition(
								((NumberValue) ((JsonArray) ((JsonArray) gameObject.get("position")).get(i)).get(0))
								.getValue()
										.doubleValue(),
								((NumberValue) ((JsonArray) ((JsonArray) gameObject.get("position")).get(i)).get(1))
								.getValue()
										.doubleValue());
						if (!secondMultiPlexer) {
							b.reqWidth = ((NumberValue) ((JsonArray) gameObject.get("requestedSize")).get(0)).getValue()
									.intValue();
							b.reqHeight = ((NumberValue) ((JsonArray) gameObject.get("requestedSize")).get(1))
									.getValue().intValue();
						} else {
							JsonArray reqSize = (JsonArray) ((JsonArray) gameObject.get("requestedSize")).get(i);
							b.reqWidth = ((NumberValue) reqSize.get(0)).getValue().intValue();
							b.reqHeight = ((NumberValue) reqSize.get(1)).getValue().intValue();
						}
						if (gameObject.containsKey("fps"))
							if (secondMultiPlexer)
								b.fps = ((NumberValue) ((JsonArray) gameObject.get("fps")).get(i)).getValue()
								.intValue();
							else b.fps = ((NumberValue) gameObject.get("fps")).getValue().doubleValue();
						else b.fps = 7;
						if (gameObject.containsKey("layer"))
							if (secondMultiPlexer)
								b.setLayer(((NumberValue) ((JsonArray) gameObject.get("layer")).get(i)).getValue()
										.intValue());
							else
								b.setLayer(((NumberValue) gameObject.get("layer")).getValue().intValue());
						else b.setLayer(0);
						if (gameObject.containsKey("background"))
							if (secondMultiPlexer)
								b.background = ((BoolValue) ((JsonArray) gameObject.get("background")).get(i))
								.getValue();
							else
								b.background = ((BoolValue) gameObject.get("background")).getValue();

						if (gameObject.containsKey("extraData"))
							if (secondMultiPlexer)
								b.extraData = (JsonObject) ((JsonArray) gameObject.get("extraData")).get(i);
							else
								b.extraData = (JsonObject) gameObject.get("extraData");
						else b.extraData = new JsonObject();
						b.images = new HashMap<>();
						((JsonObject) gameObject.get("textures")).entrySet().parallelStream()
						.forEach(s -> {
							try {
								b.getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue());
							} catch (FileNotFoundException e1) {
								e1.printStackTrace();
							}
						});
					}
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
				x = ((NumberValue) ((JsonArray) ((JsonArray) gameObject.get("position")).get(0)).get(0)).getValue()
						.doubleValue();
				y = ((NumberValue) ((JsonArray) ((JsonArray) gameObject.get("position")).get(0)).get(1)).getValue()
						.doubleValue();
				if (!secondMultiPlexer) {
					reqWidth = ((NumberValue) ((JsonArray) gameObject.get("requestedSize")).get(0)).getValue()
							.intValue();
					reqHeight = ((NumberValue) ((JsonArray) gameObject.get("requestedSize")).get(1)).getValue()
							.intValue();
				} else {
					JsonArray reqSize = (JsonArray) ((JsonArray) gameObject.get("requestedSize")).get(0);
					reqWidth = ((NumberValue) reqSize.get(0)).getValue().intValue();
					reqHeight = ((NumberValue) reqSize.get(1)).getValue().intValue();
				}
				if (gameObject.containsKey("fps"))
					if (secondMultiPlexer)
						fps = ((NumberValue) ((JsonArray) gameObject.get("fps")).get(0)).getValue().intValue();
					else fps = ((NumberValue) gameObject.get("fps")).getValue().doubleValue();
				else fps = 7;
				if (gameObject.containsKey("layer"))
					if (secondMultiPlexer)
						layer = ((NumberValue) ((JsonArray) gameObject.get("layer")).get(0)).getValue().intValue();
					else
						layer = ((NumberValue) gameObject.get("layer")).getValue().intValue();
				else layer = 0;
				if (gameObject.containsKey("background"))
					if (secondMultiPlexer)
						background = ((BoolValue) ((JsonArray) gameObject.get("background")).get(0)).getValue();
					else
						background = ((BoolValue) gameObject.get("background")).getValue();

				if (gameObject.containsKey("extraData"))
					if (secondMultiPlexer)
						extraData = (JsonObject) ((JsonArray) gameObject.get("extraData")).get(0);
					else
						extraData = (JsonObject) gameObject.get("extraData");
				else extraData = new JsonObject();
			} else {
				x = ((NumberValue) ((JsonArray) gameObject.get("position")).get(0)).getValue().doubleValue();
				y = ((NumberValue) ((JsonArray) gameObject.get("position")).get(1)).getValue().doubleValue();
				if (((JsonArray) gameObject.get("requestedSize")).get(0) instanceof NumberValue nv) {
					reqWidth = nv.getValue().intValue();
					reqHeight = ((NumberValue) ((JsonArray) gameObject.get("requestedSize")).get(1)).getValue()
							.intValue();
				} else {
					JsonArray reqSize =(JsonArray) ((JsonArray) gameObject.get("requestedSize")).get(0);
					reqWidth = ((NumberValue) reqSize.get(0)).getValue().intValue();
					reqHeight = ((NumberValue) reqSize.get(1)).getValue().intValue();
				}
				if (gameObject.containsKey("fps"))
					if (gameObject.get("fps") instanceof JsonArray ja)
						fps = ((NumberValue) ja.get(0)).getValue().intValue();
					else fps = ((NumberValue) gameObject.get("fps")).getValue().doubleValue();
				else fps = 7;
				if (gameObject.containsKey("layer"))
					if (gameObject.get("layer") instanceof JsonArray ja)
						layer = ((NumberValue) ja.get(0)).getValue().intValue();
					else
						layer = ((NumberValue) gameObject.get("layer")).getValue().intValue();
				else layer = 0;
				if (gameObject.containsKey("background"))
					if (gameObject.get("background") instanceof JsonArray ja)
						background = ((BoolValue) ja.get(0)).getValue();
					else
						background = ((BoolValue) gameObject.get("background")).getValue();

				if (gameObject.containsKey("extraData"))
					if (gameObject.get("extraData") instanceof JsonArray ja)
						extraData = (JsonObject) ja.get(0);
					else
						extraData = (JsonObject) gameObject.get("extraData");
				else extraData = new JsonObject();
			}

			((JsonObject) gameObject.get("textures")).entrySet().parallelStream()
			.forEach(s -> {
				try {
					getAnimatedImages(s.getKey(), ((StringValue) s.getValue()).getValue());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			});
			iv.setImage(getFirstImage());
		}

		menu = new Menu("GameObject");
		imagesM = new Menu("Images");
		position = new MenuItem();
		fpsI = new MenuItem();
		currentKeyI = new MenuItem();
		directoryI = new MenuItem();
		origDim = new MenuItem();
		reqDim = new MenuItem();
		backgroundI = new MenuItem();
		layerI = new MenuItem();
		reloadTextures = new MenuItem("Reload Textures");
		remove = new MenuItem("Remove");
		position.setOnAction(this::handleContextMenu);
		fpsI.setOnAction(this::handleContextMenu);
		currentKeyI.setOnAction(this::handleContextMenu);
		directoryI.setOnAction(this::handleContextMenu);
		origDim.setOnAction(this::handleContextMenu);
		reqDim.setOnAction(this::handleContextMenu);
		backgroundI.setOnAction(this::handleContextMenu);
		layerI.setOnAction(this::handleContextMenu);
		reloadTextures.setOnAction(this::handleContextMenu);
		remove.setOnAction(this::handleContextMenu);
		menu.getItems().addAll(position, fpsI, imagesM, currentKeyI, directoryI, origDim, reqDim, backgroundI, layerI,
				reloadTextures, remove);

		if (gameObjects != null) {
			((List<GameObject>) gameObjects).add(this);
			removeCallbacks.add(() -> {
				gameObjects.remove(this);
			});
		}

		addToView();
	}
	private void handleContextMenu(ActionEvent e) {
		MenuItem source = (MenuItem) e.getSource();
		ContextMenu cm = source.getParentMenu().getParentPopup();
		if (source == position) {
			TwoTextInputDialog dialog = new TwoTextInputDialog(x + "", "X", y + "", "Y");

			updateXY = () -> {
				if (!dialog.getTextField1().getText().equals(x + ""))
					dialog.getTextField1().setText(x + "");
				if (!dialog.getTextField2().getText().equals(y + ""))
					dialog.getTextField2().setText(y + "");
			};

			dialog.initModality(Modality.NONE);
			dialog.setTitle("Position");
			gp.getKeyH().moveGameObject(this);
			Optional<List<String>> result = dialog.showAndWait();
			if (result.isPresent()) {
				try {
					x = Double.parseDouble(result.get().get(0));
				} catch (NumberFormatException e2) {
				}
				try {
					y = Double.parseDouble(result.get().get(1));
				} catch (NumberFormatException e2) {
				}
			}
			gp.getKeyH().stopMoveingGameObject(this);
		} else if (source == fpsI) {
			TextInputDialog dialog = new TextInputDialog("" + fps);
			dialog.setHeaderText("");
			dialog.getDialogPane().getStyleClass().remove("text-input-dialog");
			dialog.setTitle("FPS");
			dialog.setContentText("Please enter the new FPS value:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) try {
				fps = Double.parseDouble(result.get());
			} catch (NumberFormatException e2) {
			}
		} else if (source == currentKeyI) {
			TextInputDialog dialog = new TextInputDialog(currentKey);
			dialog.setHeaderText("");
			dialog.getDialogPane().getStyleClass().remove("text-input-dialog");
			dialog.setTitle("Current Key");
			dialog.setContentText("Please enter the new Key:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) if (images.containsKey(result.get())) currentKey = result.get();

		} else if (source == directoryI) {
			TextInputDialog dialog = new TextInputDialog(directory);
			dialog.setHeaderText("");
			dialog.getDialogPane().getStyleClass().remove("text-input-dialog");
			dialog.setTitle("Directory");
			dialog.setContentText("Please enter the name of an Directory located in res/ :");

			Optional<String> result = dialog.showAndWait();

			if (result.isPresent()) if (new File("./res/" + result.get()).exists()) directory = result.get();

		} else if (source == origDim) {
			TwoTextInputDialog dialog = new TwoTextInputDialog(origWidth + "", "Width", origHeight + "", "Height");
			dialog.setTitle("Original Dimension");
			Optional<List<String>> result = dialog.showAndWait();
			if (result.isPresent()) {
				try {
					origWidth = Integer.parseInt(result.get().get(0));
				} catch (NumberFormatException e2) {
				}
				try {
					origHeight = Integer.parseInt(result.get().get(1));
				} catch (NumberFormatException e2) {
				}
			}
		} else if (source == reqDim) {
			TwoTextInputDialog dialog = new TwoTextInputDialog(reqWidth + "", "Width", reqHeight + "", "Height");

			updateReqDim = () -> {
				if (!dialog.getTextField1().getText().equals(reqWidth + ""))
					dialog.getTextField1().setText(reqWidth + "");
				if (!dialog.getTextField2().getText().equals(reqHeight + ""))
					dialog.getTextField2().setText(reqHeight + "");
			};

			dialog.setTitle("Requested Dimension");
			dialog.initModality(Modality.NONE);
			gp.getKeyH().resizeGameObject(this);
			Optional<List<String>> result = dialog.showAndWait();
			if (result.isPresent()) {
				try {
					reqWidth = Integer.parseInt(result.get().get(0));
					reloadTextures();
				} catch (NumberFormatException e2) {
				}
				try {
					reqHeight = Integer.parseInt(result.get().get(1));
					reloadTextures();
				} catch (NumberFormatException e2) {
				}
			}
			gp.getKeyH().stopResizeingGameObject(this);
		} else if (source == backgroundI) {
			Alert alert = new Alert(Alert.AlertType.NONE);
			alert.setTitle("Background");
			alert.setContentText("Set the value for Background");
			ButtonType okButton = new ButtonType("true", ButtonBar.ButtonData.YES);
			ButtonType noButton = new ButtonType("false", ButtonBar.ButtonData.NO);
			ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
			Optional<ButtonType> res = alert.showAndWait();
			if (res.isPresent()) if (res.get() == okButton) background = true;
			else if (res.get() == noButton) background = false;
			System.out.println(background);
		} else if (source == layerI) {
			int origLayer = layer;
			LayerInputDialog lid = new LayerInputDialog(this::getLayer, this::setLayer);

			lid.setTitle("Layer");
			Optional<Boolean> result = lid.showAndWait();
			if (result.isPresent() && !result.get() || !result.isPresent()) layer = origLayer;

		} else if (source == reloadTextures) reloadTextures();
		else if (source == remove) remove();
		else {
			cm = source.getParentMenu().getParentMenu().getParentPopup();
			if (source.getText().equals("add Texture")) {

				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.setTitle("Select a texture file");
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				TextInputDialog dialog = new TextInputDialog("");
				dialog.setHeaderText("");
				dialog.getDialogPane().getStyleClass().remove("text-input-dialog");
				dialog.setTitle("Naming a new State");
				dialog.setContentText("Please enter a name for that State:");

				Optional<String> result = dialog.showAndWait();
				result.ifPresent(name -> {

					try {
						Path p1 = f.toPath();
						Path p2 = new File("./res/" + directory + "/" + f.getName()).toPath();
						if (Files.exists(p2)) {
							Alert alert = new Alert(Alert.AlertType.NONE);
							alert.setTitle("The file already exists");
							alert.setContentText("Do you want to Override it?");
							ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
							ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
							ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
							alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
							Optional<ButtonType> res = alert.showAndWait();
							if (res.isPresent()) {
								if (res.get() == okButton) try {
									Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES,
											StandardCopyOption.REPLACE_EXISTING);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								else if (res.get() == cancelButton) return;
							} else return;

						} else Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
						System.out.println(f);
						System.out.println(p2);
						getAnimatedImages(name, f.getName());
						if (!collisionBoxes.containsKey(name)) collisionBoxes.put(name, new Polygon());

					} catch (IOException e1) {
						e1.printStackTrace();
					}
				});
			} else {
				FileChooser fc = new FileChooser();
				fc.setInitialDirectory(new File("."));
				fc.setTitle("Select a texture file");
				fc.getExtensionFilters().add(new ExtensionFilter(
						"A file containing an Image", "*.png"));
				File f = fc.showOpenDialog(cm.getScene().getWindow());
				if (f == null || !f.exists()) return;
				try {
					Path p1 = f.toPath();
					Path p2 = new File("./res/" + directory + "/" + f.getName()).toPath();
					if (Files.exists(p2)) {
						Alert alert = new Alert(Alert.AlertType.NONE);
						alert.setTitle("The file already exists");
						alert.setContentText("Do you want to Override it?");
						ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
						ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
						ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
						alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
						Optional<ButtonType> res = alert.showAndWait();
						if (res.isPresent()) {
							if (res.get() == okButton) try {
								Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES,
										StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							else if (res.get() == cancelButton) return;
						} else return;

					} else Files.copy(p1, p2, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(f);
					System.out.println(p2);
					String name = source.getText().split(":")[0];
					name = name.substring(0, name.length() - 1);
					getAnimatedImages(name, f.getName());
					if (!collisionBoxes.containsKey(name)) collisionBoxes.put(name, new Polygon());

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	protected void addToView() {
		gp.getViewGroups().get(layer).getChildren().add(this);
	}

	protected List<Image> getAnimatedImages(String key, String path) throws FileNotFoundException {
		List<Image> li = new ArrayList<>();
		try {
			Image img = new Image(new FileInputStream("./res/" + directory + "/" + path));

			for (int i = 0; i < img.getWidth(); i += origWidth) {
				WritableImage wi = new WritableImage(img.getPixelReader(), i, 0, origWidth, origHeight);
				li.add(ImgUtil.resizeImage(wi,
						(int) wi.getWidth(), (int) wi.getHeight(), reqWidth, reqHeight));
			}
			String[] sp = path.split("[.]");
			Polygon collisionBox = collisionBoxes.get(key);
			if (collisionBox == null) collisionBoxes.put(key, collisionBox = new Polygon());
			if (new File("./res/collisions/" + directory + "/" + String.join(".", Arrays.copyOf(sp, sp.length - 1))
			+ ".collisionbox").exists())
				try {
					RandomAccessFile raf = new RandomAccessFile(new File("./res/collisions/" + directory + "/"
							+ String.join(".", Arrays.copyOf(sp, sp.length - 1))
							+ ".collisionbox"), "rws");
					raf.seek(0l);
					int length = raf.readInt();
					for (int i = 0; i < length; i++) collisionBox.getPoints().add(raf.readDouble());
				} catch (IOException e) {
					e.printStackTrace();
				}
			images.put(key, li);
			textureFiles.put(key, path);
		} catch (ArrayIndexOutOfBoundsException e) {
		}
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
	public int getLayer() { return layer; }
	public List<Menu> getMenus() {
		position.setText("Position: " + x + " " + y);
		fpsI.setText("FPS: " + fps);
		currentKeyI.setText("Current Key: " + currentKey);
		directoryI.setText("Directory: " + directory);
		origDim.setText("Original Dimensions: " + origWidth + " " + origHeight);
		reqDim.setText("Requested Dimensions: " + reqWidth + " " + reqHeight);
		backgroundI.setText("Background: " + background);
		layerI.setText("Layer: " + layer);
		imagesM.getItems().clear();
		for (Entry<String, String> en: textureFiles.entrySet()) {
			MenuItem img = new MenuItem(en.getKey() + " : " + en.getValue(),
					new ImageView(ImgUtil.resizeImage(images.get(en.getKey()).get(0),
							(int) images.get(en.getKey()).get(0).getWidth(),
							(int) images.get(en.getKey()).get(0).getHeight(), 16, 16)));
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
	public int getOrigHeight() { return origHeight; }
	public int getOrigWidth() { return origWidth; }
	public int getReqHeight() { return reqHeight; }
	public int getReqWidth() { return reqWidth; }

	public double getX() { return x; }

	public double getY() { return y; }

	public boolean isBackground() { return background; }

	public boolean isMaster() { return !slave; }

	public boolean isSlave() { return slave; }

	public void reloadTextures() {
		List<Entry<String, String>> textures = new ArrayList<>(textureFiles.entrySet());
		for (Entry<String, String> en: textures) try {
			getAnimatedImages(en.getKey(), en.getValue());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	public void remove() {
		if (gp.getViewGroups().get(layer).getChildren().contains(this))
			gp.getViewGroups().get(layer).getChildren().remove(this);
		removeCallbacks.forEach(Runnable::run);
	}

	public void setLayer(int layer) {
		if (gp.getViewGroups().get(this.layer).getChildren().contains(this))
			gp.getViewGroups().get(this.layer).getChildren().remove(this);
		this.layer = layer;
		addToView();
	}

	public void setOrigHeight(int origHeight) { this.origHeight = origHeight; }

	public void setOrigWidth(int origWidth) { this.origWidth = origWidth; }

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setReqHeight(int reqHeight) {
		this.reqHeight = reqHeight;
		if (updateReqDim != null)
			updateReqDim.run();
	}

	public void setReqWidth(int reqWidth) {
		this.reqWidth = reqWidth;
		if (updateReqDim != null)
			updateReqDim.run();
	}

	public void setX(double x) {
		this.x = x;
		if (updateXY != null)
			updateXY.run();
	}

	public void setY(double y) {
		this.y = y;
		if (updateXY != null)
			updateXY.run();
	}

	@Override
	public JsonValue toJsonValue() {
		if (!slave) {
			JsonObject jo = new JsonObject();
			jo.put("type", getClass().getSimpleName());
			jo.put("textures", textureFiles);
			JsonArray originalSize = new JsonArray();
			originalSize.add(origWidth);
			originalSize.add(origHeight);
			jo.put("originalSize", originalSize);
			JsonArray position = new JsonArray();
			JsonArray extraDatas = new JsonArray();
			JsonArray fpss = new JsonArray();
			JsonArray requestedSize = new JsonArray();
			JsonArray backgrounds = new JsonArray();
			JsonArray layers = new JsonArray();
			if (slaves == null || slaves.size() == 0) {
				position.add(x);
				position.add(y);
				extraDatas.add(extraData);
				fpss.add(fps);
				requestedSize.add(reqWidth);
				requestedSize.add(reqHeight);
				backgrounds.add(background);
				layers.add(layer);
			} else {
				JsonArray pos = new JsonArray();
				pos.add(x);
				pos.add(y);
				position.add(pos);
				extraDatas.add(extraData);
				fpss.add(fps);
				JsonArray reqSize = new JsonArray();
				reqSize.add(reqWidth);
				reqSize.add(reqHeight);
				requestedSize.add(reqSize);
				backgrounds.add(background);
				layers.add(layer);
				for (GameObject b: slaves) {
					pos = new JsonArray();
					pos.add(b.x);
					pos.add(b.y);
					position.add(pos);
					extraDatas.add(b.extraData);
					fpss.add(b.fps);
					reqSize = new JsonArray();
					reqSize.add(b.reqWidth);
					reqSize.add(b.reqHeight);
					requestedSize.add(reqSize);
					backgrounds.add(b.background);
					layers.add(b.layer);
				}
			}
			jo.put("position", position);
			jo.put("extraData", extraDatas);
			jo.put("fps", fpss);
			jo.put("requestedSize", requestedSize);
			jo.put("background", backgrounds);
			jo.put("layer", layers);
			return jo;
		} else return new StringValue("");
	}

	@Override
	public JsonValue toJsonValue(Function<Object, String> function) {
		return toJsonValue();
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

		if (x + getWidth() > p.x - p.screenX
				&& x < p.x + p.screenX + p.getWidth()
				&& y + reqHeight > p.y - p.screenY
				&& y < p.y + p.screenY + p.getHeight())
			setVisible(true);
		else setVisible(false);
	}

}
