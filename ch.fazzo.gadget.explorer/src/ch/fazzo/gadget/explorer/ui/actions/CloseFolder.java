package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Node;

public class CloseFolder implements UIAction {

	private final Node node;

	public CloseFolder(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.closeFolder();
	}

}
