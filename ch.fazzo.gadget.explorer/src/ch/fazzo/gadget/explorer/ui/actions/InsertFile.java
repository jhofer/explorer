package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Node;

public class InsertFile implements UIAction {

	private final Node node;

	public InsertFile(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.insertIntoParent();

	}

}
