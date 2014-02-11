package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Node;

public class OpenFolder implements UIAction {

	private final Node node;

	public OpenFolder(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.openFolder();
	}

}
