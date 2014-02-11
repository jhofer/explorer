package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Node;

public class SelectPrevious implements UIAction {

	private final Node node;

	public SelectPrevious(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.selectPreviousNode();

	}

}
