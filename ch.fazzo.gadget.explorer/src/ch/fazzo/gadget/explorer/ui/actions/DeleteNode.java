package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Modification;
import ch.fazzo.gadget.explorer.model.Node;

public class DeleteNode implements UIAction {

	private final Node node;

	public DeleteNode(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.modify(Modification.DEL);

	}

}
