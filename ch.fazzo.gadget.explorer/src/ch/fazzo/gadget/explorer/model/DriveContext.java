package ch.fazzo.gadget.explorer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriveContext {

	private Node activeNode;
	private Node modifyNode;
	private Modification modification;

	private final int levelRange = 3;
	private int levelRangeStart = 1;
	private int levelRangeEnd = this.levelRangeStart + this.levelRange;
	private final int indexRange = 10;
	private int indexRangeStart = 0;
	private int indexRangeEnd = this.indexRangeStart + this.indexRange;

	private final Map<Integer, List<Integer>> levelToIndex = new HashMap<Integer, List<Integer>>();
	private final List<Integer> visiblLevel = new ArrayList<Integer>();
	private String filterText;

	public void setActive(Node node) {
		this.activeNode = node;
		calcVisibility();

	}

	public boolean isActive(Node node) {
		return node.equals(this.activeNode);
	}

	public void modify(Node node, Modification mod) {
		this.modifyNode = node;
		this.modification = mod;

	}

	public boolean isModify(Node node) {
		return node.equals(this.modifyNode);
	}

	public void clearModify(Modification... mods) {
		for (Modification modification : mods) {
			if (modification.equals(this.modification)) {
				this.modifyNode = null;
				this.modification = null;
			}
		}

	}

	private void calcVisibility() {
		int index = this.activeNode.getIndex();
		int level = this.activeNode.getLevel();

		if (index < this.indexRangeStart) {
			this.indexRangeStart = index;
			this.indexRangeEnd = this.indexRangeStart + this.indexRange;
		} else if (index > this.indexRangeEnd) {
			this.indexRangeEnd = index;
			this.indexRangeStart = this.indexRangeEnd - this.indexRange;
		}

		if (level < this.levelRangeStart) {
			this.levelRangeStart = level;
			this.levelRangeEnd = this.levelRangeStart + this.levelRange;
		} else if (level > this.levelRangeEnd) {
			this.levelRangeEnd = level;
			this.levelRangeStart = this.levelRangeEnd - this.levelRange;
		} else if (level < this.levelRangeEnd && level > this.levelRange) {
			this.levelRangeEnd = level;
			if (this.levelRangeEnd - this.levelRange <= 0) {
				this.levelRangeStart = 0;
			} else {
				this.levelRangeStart = this.levelRangeEnd - this.levelRange;
			}
		}

		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = this.indexRangeStart; i <= this.indexRangeEnd; i++) {
			indexes.add(i);
		}
		this.levelToIndex.put(level, indexes);
		this.visiblLevel.clear();
		for (int i = this.levelRangeStart; i <= this.levelRangeEnd; i++) {
			this.visiblLevel.add(i);
		}

	}

	public int getRelativeLevel(int level) {
		int result = level - (this.visiblLevel.get(0));
		return result;
	}

	public int getRelativeIndex(int level, int index) {
		return index - this.levelToIndex.get(level).get(0);
	}

	public boolean isVisible(int level, int index) {
		if (this.levelToIndex.isEmpty() || this.visiblLevel.isEmpty()) {
			return true;
		}
		boolean indexCheck = this.levelToIndex.get(level).contains(index);
		boolean levelCheck = this.visiblLevel.contains(level);
		return indexCheck && levelCheck;

	}

	public Node getActiveNode() {
		return this.activeNode;
	}

	public boolean modificationContains(Modification... mod) {
		for (Modification modification : mod) {
			if (modification.equals(this.modification)) {
				return true;
			}
		}
		return false;
	}

	public Node getModifyNode() {
		return this.modifyNode;
	}

	public void addStringToFilter(String keyText) {
		if (keyText == null) {
			this.filterText = null;
		}
		if (this.filterText != null) {
			this.filterText = this.filterText + keyText;
		} else {
			this.filterText = keyText;
		}
	}

	public boolean isFilterActive() {
		return this.filterText != null;
	}

	public boolean isFiltered(String string) {
		return !this.filterText.contains(string);
	}

	public String getFilterText() {
		return this.filterText;
	}

	public void removeOneLetterFromString() {
		this.filterText = this.filterText.substring(0,
				this.filterText.length() - 2);
	}

}
