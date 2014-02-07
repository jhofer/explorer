package ch.fazzo.gadget.explorer.controlling;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

import ch.fazzo.gadget.explorer.model.DriveContext;
import ch.fazzo.gadget.explorer.model.Modification;
import ch.fazzo.gadget.explorer.model.Node;
import ch.fazzo.gadget.explorer.paint.NodePane;

public class Navigator implements KeyListener {

	private final NodePane pane;
	private int delCounter = 0;
	private final DriveContext dc;
	private boolean filter = false;

	public Navigator(NodePane pane, DriveContext dc) {
		this.pane = pane;
		this.dc = dc;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
			this.dc.getActiveNode().modify(Modification.COPY);

			return;
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
			this.dc.getActiveNode().modify(Modification.CUT);
			// ctrl x
			return;
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
			if (this.dc.modificationContains(Modification.COPY,
					Modification.CUT)) {
				this.dc.getActiveNode().insertIntoParent(
						this.dc.getModifyNode());
				this.pane.addNodes(this.dc.getActiveNode().getParent()
						.getChilds());
			}

			return;
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			this.dc.getActiveNode().modify(Modification.DEL);
			if (this.delCounter++ == 1) {
				this.pane.removeNodes(this.dc.getActiveNode().getParent()
						.getChilds());

				this.dc.getActiveNode().delete();
				selectNextNode();
				this.pane.addNodes(this.dc.getActiveNode().getParent()
						.getChilds());
				this.dc.clearModify(Modification.DEL);

				this.delCounter = 0;

			}
			return;
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.delCounter = 0;
			this.filter = false;
			this.dc.addStringToFilter(null);
			this.dc.clearModify(Modification.DEL, Modification.CUT,
					Modification.COPY);
			return;

		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			if (this.dc.getActiveNode().isRoot()) {

			} else {
				selectPreviusNode();
			}
			return;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			if (this.dc.getActiveNode().isRoot()) {

			} else {
				selectNextNode();
			}
			return;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

			closeFolder();
			return;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			openFolder();
			return;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.dc.getActiveNode().run();
			return;

		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
			this.filter = true;
			return;

		} else {
			this.delCounter = 0;
			this.dc.clearModify(Modification.DEL);
			if (this.filter) {
				String text = KeyEvent.getKeyText(e.getKeyCode());
				if (text.length() == 1) {
					this.dc.addStringToFilter(text);
				} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (this.dc.getFilterText().equals("")) {
						this.dc.addStringToFilter(null);
						this.filter = false;
					} else {
						this.dc.removeOneLetterFromString();
					}

				}

			} else {

				goToFolder(e);
			}
			return;
		}

	}

	private void selectPreviusNode() {
		Set<Node> nodeList;
		Node currentActive = this.dc.getActiveNode();
		nodeList = this.dc.getActiveNode().getParent().getChilds();
		for (Node node : nodeList) {
			if (node.getIndex() == this.dc.getActiveNode().getIndex() - 1) {

				node.setActive();
				break;
			}
		}
		if (!currentActive.equals(this.dc.getActiveNode())) {
			this.pane.addNodes(nodeList);

		}
	}

	private void selectNextNode() {

		Node currentActive = this.dc.getActiveNode();
		Set<Node> nodeList;
		nodeList = this.dc.getActiveNode().getParent().getChilds();
		for (Node node : nodeList) {
			if (node.getIndex() == this.dc.getActiveNode().getIndex() + 1) {

				node.setActive();
				break;
			}
		}
		if (!currentActive.equals(this.dc.getActiveNode())) {
			this.pane.addNodes(nodeList);

		} else {
			for (Node node : nodeList) {
				if (node.getIndex() == 0) {

					node.setActive();

				}
			}
		}

	}

	private void closeFolder() {
		if (this.dc.getActiveNode().getParent() != null) {
			this.pane.removeNodes(this.dc.getActiveNode().getParent()
					.getChilds());
			this.dc.getActiveNode().getParent().setActive();
		}

	}

	private void openFolder() {
		Set<Node> childs = this.dc.getActiveNode().getChilds();
		for (Node node : childs) {
			if (node.getIndex() == 0) {
				node.setActive();
			}
		}
		this.pane.addNodes(childs);

	}

	private void goToFolder(KeyEvent e) {
		Node currentActive = this.dc.getActiveNode();
		if (!currentActive.isRoot()) {
			Set<Node> nodeList;
			nodeList = this.dc.getActiveNode().getParent().getChilds();
			for (Node node : nodeList) {
				if (node.toString().startsWith(
						KeyEvent.getKeyText(e.getKeyCode()))) {

					if (currentActive.toString().startsWith(
							KeyEvent.getKeyText(e.getKeyCode()))) {
						if (node.getIndex() == currentActive.getIndex() + 1) {
							node.setActive();
							break;
						}
					} else {

						node.setActive();
						break;
					}
				}
			}

			if (!currentActive.equals(this.dc.getActiveNode())) {
				this.pane.addNodes(nodeList);

			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
