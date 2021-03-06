package ch.fazzo.gadget.explorer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class DriveContext extends Observable implements Model<DriveContext>,
		Observer {

	private static final int MIN_LEVEL_START = 1;
	private Node activeNode;
	private Node modifyNode;
	private Modification modification;

	private final int levelRange = 6;
	private int levelRangeStart = MIN_LEVEL_START;
	private int levelRangeEnd = this.levelRangeStart + this.levelRange;
	private final int indexRange = 10;
	private int indexRangeStart = 0;
	private int indexRangeEnd = this.indexRangeStart + this.indexRange;

	private final Map<Integer, List<Integer>> levelToIndex = new HashMap<Integer, List<Integer>>();
	private final List<Integer> visiblLevel = new ArrayList<Integer>();
	private String filterText = "";

	private int delCounter = 0;

	private final Set<Node> allNodes = new HashSet<Node>();
	private boolean filter;

	private void resetDel() {
		this.delCounter = 0;
		clearModify(Modification.DEL);
	}

	public void setActive(Node node) {
		this.activeNode = node;
		resetDel();
		calcVisibility();

	}

	public void add(Node node) {

		this.allNodes.add(node);

		setChanged();
		notifyObservers(this);
	}

	public void removeAll(Set<Node> nodes) {

		this.allNodes.removeAll(nodes);
		setChanged();
		notifyObservers(this);
	}

	public Set<Node> getAllNodes() {
		return this.allNodes;

	}

	public boolean isActive(Node node) {
		return node.equals(this.activeNode);
	}

	public void modify(Node node, Modification mod) {
		this.modifyNode = node;
		this.modification = mod;
		if (!Modification.DEL.equals(mod)) {
			resetDel();
		} else {
			if (this.delCounter++ == 1) {
				// this.pane.removeNodes(this.dc.getActiveNode().getParent()
				// .loadChilds());

				node.delete();
				if (node.getParent().getChilds().size() == 1) {
					node.getParent().setActive();
				} else {
					selectNextNode();
				}

				resetDel();
				removeNode(node);
			}
		}
	}

	private void removeNode(Node node) {
		this.allNodes.remove(node);
		setChanged();
		notifyObservers(this);
	}

	private void selectNextNode() {

		Node currentActive = getActiveNode();
		Set<Node> nodeList;
		nodeList = getActiveNode().getParent().openFolder();
		for (Node node : nodeList) {
			if (node.getIndex() == currentActive.getIndex() + 1) {

				node.setActive();
				break;
			}
		}

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
			if (this.levelRangeStart - level <= MIN_LEVEL_START) {
				this.levelRangeStart = MIN_LEVEL_START;
				this.levelRangeEnd = this.levelRangeStart + this.levelRange;
			} else {
				this.levelRangeStart = level;
				this.levelRangeEnd = this.levelRangeStart + this.levelRange;
			}
		} else if (level > this.levelRangeEnd) {
			this.levelRangeEnd = level;
			this.levelRangeStart = this.levelRangeEnd - this.levelRange;
		} else if (level < this.levelRangeEnd) {
			this.levelRangeEnd = level;
			if (this.levelRangeEnd - this.levelRange <= MIN_LEVEL_START) {
				this.levelRangeStart = MIN_LEVEL_START;
				this.levelRangeEnd = this.levelRangeStart + this.levelRange;
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
		this.filterText = this.filterText + keyText;
	}

	public boolean isFilterActive() {
		return this.filter;
	}

	public boolean isFiltered(String string) {
		if (!this.filter) {
			return false;
		}
		return !string.toLowerCase().contains(this.filterText.toLowerCase());
	}

	public String getFilterText() {
		return this.filterText;
	}

	public void removeOneLetterFromString() {
		if (this.filterText.length() > 0) {
			this.filterText = this.filterText.substring(0,
					this.filterText.length() - 1);
		}

		this.filter = this.filterText.length() > 0;
	}

	@Override
	public int compareTo(DriveContext o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String id() {
		// TODO Auto-generated method stub
		return null;
	}

	public void switchOnOffFilter() {
		this.filter = !this.filter;

	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
