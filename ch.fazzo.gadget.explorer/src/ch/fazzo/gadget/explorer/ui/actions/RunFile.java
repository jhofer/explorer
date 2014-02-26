package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Node;

public class RunFile implements UIAction {

	private final Node node;

	public RunFile(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		if (!this.node.getDC().isFilterActive()) {
			this.node.run();
		}
	}

}
