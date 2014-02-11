package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Node;

public class SelectNext implements UIAction {

	private final Node node;

	public SelectNext(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.selectNextNode();
	}

}
