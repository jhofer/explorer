package ch.fazzo.gadget.explorer.ui.actions;

import ch.fazzo.gadget.explorer.model.Modification;
import ch.fazzo.gadget.explorer.model.Node;

public class CutFile implements UIAction {

	private final Node node;

	public CutFile(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		this.node.modify(Modification.CUT);

	}

}
