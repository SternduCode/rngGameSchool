package rngGame.main;

import java.util.*;
import javafx.scene.input.KeyCode;

public class UndoRedo {

	public interface UndoRedoAction {
		void redo();

		void undo();
	}

	public static class UndoRedoActionBase implements UndoRedoAction {

		private final Runnable undo, redo;

		public UndoRedoActionBase(Runnable undo, Runnable redo) {
			this.undo = undo;
			this.redo = redo;
		}

		@Override
		public void redo() {
			redo.run();
		}

		@Override
		public void undo() {
			undo.run();
		}

	}

	private static UndoRedo INSTANCE;

	private final List<UndoRedoAction> actions;
	private int position;

	private UndoRedo() {
		actions = new ArrayList<>();
		position = 0;
		Input.getInstance().setKeyHandler("Undo", mod -> {
			if (mod.isControlPressed()) undo();
		}, KeyCode.Z, false);
		Input.getInstance().setKeyHandler("Redo", mod -> {
			if (mod.isControlPressed()) redo();
		}, KeyCode.Y, false);
	}

	public static UndoRedo getInstance() { return INSTANCE != null ? INSTANCE : (INSTANCE = new UndoRedo()); }

	public void addAction(UndoRedoAction action) {
		if (position<actions.size()-1)
			actions.removeIf(ac -> (actions.indexOf(ac)>position));
		actions.add(action);
		position++;
	}

	public void clearActions() {
		actions.clear();
		position = 0;
	}

	public void redo() {// CTRL+Y
		if (position < actions.size() - 1) {
			position++;
			actions.get(position).redo();
		}
	}

	public void undo() {//CTRL+Z
		if (position>0) {
			actions.get(position).undo();
			position--;
		}
	}

}
