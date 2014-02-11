package ch.fazzo.gadget.explorer.model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Node implements Model<Node> {

	private Node parent;
	private File file;
	private int level = 0;
	private int index = 0;
	private final DriveContext dc;
	private Set<Node> childs = new HashSet<Node>();

	public Node(Node parent, DriveContext context, File f, int level, int index) {
		this.dc = context;
		this.parent = parent;
		this.file = f;
		if (parent == null) {
			setActive();
		}
		this.level = level;
		this.index = index;
		context.add(this);
	}

	public Node(File f, DriveContext context) {
		this.file = f;
		this.dc = context;
		setActive();
		context.add(this);

	}

	public void closeFolder() {
		if (this.dc.getActiveNode().getParent() != null) {
			this.dc.removeAll(this.dc.getActiveNode().getParent().loadChilds());
			this.dc.getActiveNode().getParent().setActive();
		}

	}

	public Set<Node> openFolder() {
		Set<Node> childs = loadChilds();
		for (Node node : childs) {
			if (node.getIndex() == 0) {
				node.setActive();
			}
		}
		return childs;
	}

	public void selectPreviusNode() {
		Set<Node> nodeList;
		nodeList = getParent().loadChilds();
		for (Node node : nodeList) {
			if (node.getIndex() == getIndex() - 1) {
				node.setActive();
				break;
			}
		}
	}

	public void selectNextNode() {

		Set<Node> nodeList;
		nodeList = getParent().loadChilds();
		for (Node node : nodeList) {
			if (node.getIndex() == this.dc.getActiveNode().getIndex() + 1) {
				node.setActive();
				break;
			}
		}

	}

	private Set<Node> loadChilds() {
		if (this.file.isDirectory() && this.file.listFiles() != null
				&& this.file.listFiles().length > 0) {
			List<File> list = Arrays.asList(this.file.listFiles());
			Collections.sort(list, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			Set<Node> childs = new HashSet<Node>();
			if (list != null) {
				int i = 0;
				for (File f : list) {
					if (this.file.canRead()) {
						Node node = new Node(this, this.dc, f, this.level + 1,
								i++);
						childs.add(node);
					}

				}
			}
			this.childs = childs;
			return childs;
		} else {
			return Collections.emptySet();
		}
	}

	public Set<Node> getChilds() {
		return this.childs;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setFile(File f) {
		this.file = f;

	}

	@Override
	public String toString() {
		return this.file.getName();
	}

	public boolean isDir() {
		return this.file.isDirectory() && !isRoot();
	}

	public void run() {
		try {
			Desktop.getDesktop().open(this.file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getLevel() {
		return this.level;
	}

	public int getIndex() {
		return this.index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.file == null) ? 0 : this.file.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;
		if (this.file == null) {
			if (other.file != null) {
				return false;
			}
		} else if (!this.file.equals(other.file)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(Node o) {
		return Integer.compare(this.index, o.index);
	}

	public boolean isRoot() {
		return this.parent == null;
	}

	public void delete() {
		try {
			Files.deleteIfExists(this.file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertIntoParent() {
		if (this.dc.modificationContains(Modification.COPY, Modification.CUT)) {

			try {
				Files.copy(
						this.dc.getModifyNode().file.toPath(),
						getParent().file.toPath().resolve(
								this.dc.getModifyNode().file.getName()));
			} catch (FileAlreadyExistsException e) {
				try {
					Files.copy(
							this.dc.getModifyNode().file.toPath(),
							getParent().file.toPath().resolve(
									this.dc.getModifyNode().file.getName()
											+ " copy"));

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.dc.clearModify(Modification.COPY);
			this.dc.clearModify(Modification.CUT);
		}

	}

	public boolean isModify() {
		return this.dc.isModify(this);
	}

	public boolean isVisible() {
		return this.dc.isVisible(this.level, this.index);
	}

	public int getRelativeLevel() {
		return this.dc.getRelativeLevel(this.level);

	}

	public int getRelativeIndex() {
		return this.dc.getRelativeIndex(this.level, this.index);
	}

	public void setActive() {
		this.dc.setActive(this);
	}

	public boolean isActive() {
		return this.dc.isActive(this);
	}

	public void modify(Modification mod) {
		this.dc.modify(this, mod);
	}

	public boolean isFiltered() {
		return this.dc.isFiltered(toString());
	}

	public boolean isInActiveHierarchy() {
		Node parent = this.dc.getActiveNode();

		boolean result = false;
		while (parent != null) {
			if (equals(parent)) {
				result = true;
				break;
			}
			parent = parent.getParent();
		}
		return result;
	}

	@Override
	public String id() {
		return this.file.getAbsolutePath();
	}

	public void clearModify(Modification... mods) {
		this.dc.clearModify(mods);
	}
}
