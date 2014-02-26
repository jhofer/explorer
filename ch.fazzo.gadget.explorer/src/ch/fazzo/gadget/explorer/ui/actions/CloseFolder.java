package ch.fazzo.gadget.explorer.ui.actions;

import javax.swing.SwingUtilities;

import ch.fazzo.gadget.explorer.model.Node;

public class CloseFolder implements UIAction {

	private final Node node;

	public CloseFolder(Node node) {
		this.node = node;
	}

	@Override
	public void run() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CloseFolder.this.node.closeFolder();
			}
		});

	}

}
