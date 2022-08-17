package rngGame.main;

import java.io.*;
import java.util.*;
import java.util.function.*;
import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import rngGame.buildings.Building;
import rngGame.entity.*;
import rngGame.tile.*;

public class SpielPanel extends Pane {

	class GroupGroup extends Group {

		class OutList implements ObservableList<Group> {
			private class Itr implements Iterator<Group> {

				private final Iterator<Node> it;
				protected int cursor;
				protected int lastReturned;

				public Itr(Iterator<Node> it) {
					this.it = it;
				}

				@Override
				public boolean hasNext() {
					return it.hasNext();
				}

				@Override
				public Group next() {
					Group e = (Group) it.next();
					return e;
				}

				@Override
				public void remove() {
					try {
						it.remove();
					} catch (Exception e) {
						throw e;
					}
				}
			}

			private class ListItr extends Itr implements ListIterator<Group> {

				private final ListIterator<Node> lit;

				public ListItr(ListIterator<Node> it) {
					super(it);
					lit = it;
				}

				@Override
				public void add(Group e) {
					try {
						lit.add(e);
					} catch (Exception ex) {
						throw ex;
					}
					++cursor;
				}

				@Override
				public boolean hasPrevious() {
					return lit.hasPrevious();
				}

				@Override
				public int nextIndex() {
					return lit.nextIndex();
				}

				@Override
				public Group previous() {
					Group e = (Group) lit.previous();
					lastReturned = --cursor;
					return e;
				}

				@Override
				public int previousIndex() {
					return lit.previousIndex();
				}

				@Override
				public void set(Group e) {
					if (lastReturned == -1) throw new IllegalStateException();
					lit.set(e);
				}
			}

			class SubList implements List<Group> {

				private final List<Node> subList;
				private final int offset;

				public SubList(List<Node> subList, int offset) {
					this.subList = subList;
					this.offset = offset;
				}

				@Override
				public boolean add(Group e) {
					try {
						subList.add(e);
					} catch (Exception ex) {
						throw ex;
					}
					return true;
				}

				@Override
				public void add(int index, Group element) {
					try {
						subList.add(index, element);
					} catch (Exception e) {
						throw e;
					}
				}

				@Override
				public boolean addAll(Collection<? extends Group> c) {
					try {
						boolean res =  subList.addAll(c);
						return res;
					} catch (Exception e) {
						throw e;
					}
				}

				@Override
				public boolean addAll(int index, Collection<? extends Group> c) {
					try {
						boolean res = subList.addAll(index, c);
						return res;
					} catch (Exception e) {
						throw e;
					}
				}

				@Override
				public void clear() {
					try {
						subList.clear();
					} catch (Exception e) {
						throw e;
					}
				}

				@Override
				public boolean contains(Object o) {
					return subList.contains(o);
				}

				@Override
				public boolean containsAll(Collection<?> c) {
					return subList.containsAll(c);
				}

				@Override
				public boolean equals(Object obj) {
					return subList.equals(obj);
				}

				@Override
				public Group get(int index) {
					return (Group) subList.get(index);
				}

				@Override
				public int hashCode() {
					return subList.hashCode();
				}

				@Override
				public int indexOf(Object o) {
					return subList.indexOf(o);
				}

				@Override
				public boolean isEmpty() {
					return subList.isEmpty();
				}

				@Override
				public Iterator<Group> iterator() {
					return new Itr(subList.iterator());
				}

				@Override
				public int lastIndexOf(Object o) {
					return subList.lastIndexOf(o);
				}

				@Override
				public ListIterator<Group> listIterator() {
					return new ListItr(subList.listIterator());
				}

				@Override
				public ListIterator<Group> listIterator(int index) {
					return new ListItr(subList.listIterator(index));
				}

				@Override
				public Group remove(int index) {
					try {
						Group res = (Group) subList.remove(index);
						return res;
					} catch (Exception e) {
						throw e;
					}

				}

				@Override
				public boolean remove(Object o) {
					int i = indexOf(o);
					if (i != -1) {
						remove(i);
						return true;
					}
					return false;
				}

				@Override
				public boolean removeAll(Collection<?> c) {
					removeFromList(this, offset, c, false);
					try {
						boolean res = subList.removeAll(c);
						return res;
					} catch (Exception e) {
						throw e;
					}
				}

				@Override
				public boolean retainAll(Collection<?> c) {
					try {
						boolean res = subList.retainAll(c);
						return res;
					} catch (Exception e) {
						throw e;
					}
				}

				@Override
				public Group set(int index, Group element) {
					return (Group) subList.set(index, element);
				}

				@Override
				public int size() {
					return subList.size();
				}

				@Override
				public List<Group> subList(int fromIndex, int toIndex) {
					return new SubList(subList.subList(fromIndex, toIndex), offset + fromIndex);
				}

				@Override
				public Object[] toArray() {
					return subList.toArray();
				}

				@Override
				public <T> T[] toArray(T[] a) {
					return subList.toArray(a);
				}

				@Override
				public String toString() {
					return subList.toString();
				}
			}

			ObservableList<Node> li;

			public OutList(ObservableList<Node> li) {
				this.li = li;
			}

			private void removeFromList(List<Group> backingList, int offset, Collection<?> col, boolean complement) {
				int[] toBeRemoved = new int[2];
				int pointer = -1;
				for (int i = 0; i < backingList.size(); ++i) {
					final Group el = backingList.get(i);
					if (col.contains(el) ^ complement) if (pointer == -1) {
						toBeRemoved[pointer + 1] = offset + i;
						toBeRemoved[pointer + 2] = offset + i + 1;
						pointer += 2;
					} else if (toBeRemoved[pointer - 1] == offset + i) toBeRemoved[pointer - 1] = offset + i + 1;
					else {
						int[] tmp = new int[toBeRemoved.length + 2];
						System.arraycopy(toBeRemoved, 0, tmp, 0, toBeRemoved.length);
						toBeRemoved = tmp;
						toBeRemoved[pointer + 1] = offset + i;
						toBeRemoved[pointer + 2] = offset + i + 1;
						pointer += 2;
					}
				}
			}

			@Override
			public boolean add(Group e) {
				return li.add(e);
			}

			@Override
			public void add(int index, Group element) {
				li.add(index, element);
			}

			@Override
			public boolean addAll(Collection<? extends Group> c) {
				return li.addAll(c);
			}

			@Override
			public boolean addAll(Group... elements) {
				return li.addAll(elements);
			}

			@Override
			public boolean addAll(int index, Collection<? extends Group> c) {
				return li.addAll(index, c);
			}

			@Override
			public void addListener(InvalidationListener listener) {
				li.addListener(listener);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void addListener(ListChangeListener<? super Group> listener) {
				li.addListener((ListChangeListener<? super Node>) listener);
			}

			@Override
			public void clear() {
				li.clear();
			}

			@Override
			public boolean contains(Object o) {
				return li.contains(o);
			}

			@Override
			public boolean containsAll(Collection<?> c) {
				return li.containsAll(c);
			}

			@Override
			public boolean equals(Object o) {
				return li.equals(o);
			}

			@Override
			public void forEach(Consumer<? super Group> action) {
				li.forEach(e -> action.accept((Group) e));
			}

			@Override
			public Group get(int index) {
				if (index > li.size()) for (int i = 0; i < index - size(); i++) li.add(new Group());
				return (Group) li.get(index);
			}

			@Override
			public int hashCode() {
				return li.hashCode();
			}

			@Override
			public int indexOf(Object o) {
				return li.indexOf(o);
			}

			@Override
			public boolean isEmpty() { return li.isEmpty(); }

			@Override
			public Iterator<Group> iterator() {
				return new Itr(li.iterator());
			}

			@Override
			public int lastIndexOf(Object o) {
				return li.lastIndexOf(o);
			}

			@Override
			public ListIterator<Group> listIterator() {
				return new ListItr(li.listIterator());
			}

			@Override
			public ListIterator<Group> listIterator(int index) {
				return new ListItr(li.listIterator(index));
			}

			@Override
			public Group remove(int index) {
				return (Group) li.remove(index);
			}

			@Override
			public void remove(int from, int to) {
				li.remove(from, to);
			}

			@Override
			public boolean remove(Object o) {
				return li.remove(o);
			}

			@Override
			public boolean removeAll(Collection<?> c) {
				return li.removeAll(c);
			}

			@Override
			public boolean removeAll(Group... elements) {
				return li.removeAll(elements);
			}

			@Override
			public void removeListener(InvalidationListener listener) {
				li.removeListener(listener);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void removeListener(ListChangeListener<? super Group> listener) {
				li.removeListener((ListChangeListener<? super Node>) listener);
			}

			@Override
			public void replaceAll(UnaryOperator<Group> operator) {
				li.replaceAll(e -> operator.apply((Group) e));
			}

			@Override
			public boolean retainAll(Collection<?> c) {
				return li.retainAll(c);
			}

			@Override
			public boolean retainAll(Group... elements) {
				return li.retainAll(elements);
			}

			@Override
			public Group set(int index, Group element) {
				return (Group) li.set(index, element);
			}

			@Override
			public boolean setAll(Collection<? extends Group> col) {
				return li.setAll(col);
			}

			@Override
			public boolean setAll(Group... elements) {
				return li.setAll(elements);
			}

			@Override
			public int size() {
				return li.size();
			}

			@Override
			public List<Group> subList(int fromIndex, int toIndex) {
				return new SubList(li.subList(fromIndex, toIndex), fromIndex);
			}

			@Override
			public Object[] toArray() {
				return li.toArray();
			}

			@Override
			public <T> T[] toArray(IntFunction<T[]> generator) {
				return li.toArray(generator);
			}

			@Override
			public <T> T[] toArray(T[] a) {
				return li.toArray(a);
			}
		}

		public ObservableList<Group> getGroupChildren() {
			return new OutList(super.getChildren());
		}
	}
	public final int Bg = 48;
	public final int BildS = 20;
	public final int BildH = 11;
	public final int SpielLaenge = Bg * BildS;

	public final int SpielHoehe = Bg * BildH;


	private final int FPS = 60;

	private final ImageView inv;
	private final Input keyH;
	private final Player player;
	private final TileManager tileM;
	private final SelectTool selectTool;
	private final GroupGroup layerGroup;

	private List<Building> buildings;

	private List<NPC> npcs;

	public SpielPanel(Input keyH) throws FileNotFoundException {
		setPrefSize(SpielLaenge, SpielHoehe);

		this.keyH = keyH;

		layerGroup = new GroupGroup();

		layerGroup.getChildren().add(new Group());

		selectTool = new SelectTool(this);

		tileM = new TileManager(this);

		player = new Player(this, tileM.getNpcCM(), tileM.getRequestorN());

		inv = new ImageView(new Image(new FileInputStream("./res/gui/Inv.png")));
		inv.setX(player.screenX - inv.getImage().getWidth() / 2 + 20);
		inv.setY(player.screenY - inv.getImage().getHeight() / 2);
		inv.setVisible(false);

		setMap("./res/maps/lavaMap2.json");

		getChildren().addAll(tileM, layerGroup, selectTool, inv);
	}

	public List<Building> getBuildings() { return buildings; }

	public Input getKeyH() {
		return keyH;
	}

	public List<NPC> getNpcs() { return npcs; }

	public Player getPlayer() { return player; }

	public SelectTool getSelectTool() { return selectTool; }

	public TileManager getTileM() { return tileM; }


	public ObservableList<Group> getViewGroups() { return layerGroup.getGroupChildren(); }

	public void reload() {
		layerGroup.getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		tileM.reload();
		if (tileM.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileM.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(SpielLaenge, SpielHoehe, false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		player.setLayer(tileM.getPlayerLayer());
		buildings = tileM.getBuildingsFromMap();
		npcs = tileM.getNPCSFromMap();
	}

	public void saveMap() {
		System.out.println("don");
		tileM.save();
		System.out.println("don2");
	}

	public void setMap(String path) {
		setMap(path, null);
	}

	public void setMap(String path, Map.Entry<Double, Double> position) {
		layerGroup.getChildren().stream().map(n -> ((Group) n).getChildren()).forEach(ObservableList::clear);
		tileM.setMap(path);
		if (tileM.getBackgroundPath() != null) try {
			setBackground(new Background(
					new BackgroundImage(new Image(new FileInputStream("./res/" + tileM.getBackgroundPath())),
							BackgroundRepeat.NO_REPEAT,
							BackgroundRepeat.NO_REPEAT, null,
							new BackgroundSize(SpielLaenge, SpielHoehe, false, false, false, false))));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		else setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		buildings = tileM.getBuildingsFromMap();
		npcs = tileM.getNPCSFromMap();
		if (position != null)
			player.setPosition(position);
		else player.setPosition(tileM.getStartingPosition());
		player.setLayer(tileM.getPlayerLayer());
	}


	public void SST() {
		Timeline tl = new Timeline(
				new KeyFrame(Duration.millis(1000 / FPS),
						event -> {
							update();
						}));
		tl.setCycleCount(Animation.INDEFINITE);
		tl.play();
	}

	@Override
	public String toString() {
		return "SpielPanel [inv=" + inv
				+ ", keyH=" + keyH + ", player=" + player + ", tileM=" + tileM + ", selectTool="
				+ selectTool + ", layerGroup=" + layerGroup.getChildren().size() + ", buildings=" + buildings
				+ ", npcs=" + npcs
				+ "]";
	}

	public void update() {

		player.update();

		selectTool.update();

		for (Building b: buildings) b.update();
		for (Entity n: npcs) n.update();

		for (Node layer: layerGroup.getChildren()) {
			Group view = (Group) layer;
			List<Node> nodes = new ArrayList<>(view.getChildren());

			nodes.sort((n1, n2) -> {
				if (n1 instanceof GameObject b1) {
					if (n2 instanceof GameObject b2)
						return b1.isBackground() ^ b2.isBackground() ? b1.isBackground() ? -1 : 1
								: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
										n2.getLayoutY() + ((Pane) n2).getHeight());
					else return b1.isBackground() ? -1
							: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
									n2.getLayoutY() + ((Pane) n2).getHeight());
				} else if (n2 instanceof GameObject b2) return b2.isBackground() ? 1
						: Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
								n2.getLayoutY() + ((Pane) n2).getHeight());
				else return Double.compare(n1.getLayoutY() + ((Pane) n1).getHeight(),
						n2.getLayoutY() + ((Pane) n2).getHeight());
			});

			view.getChildren().clear();
			view.getChildren().addAll(nodes);
		}

		tileM.update();

		if (keyH.tabPressed) inv.setVisible(true);
		else inv.setVisible(false);

	}

}
