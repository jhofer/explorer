package ch.fazzo.gadget.explorer.ui.actions;

import javax.swing.SwingUtilities;

import ch.fazzo.gadget.explorer.model.Node;

public class OpenFolder implements UIAction {

	private final Node node;

	public OpenFolder(Node node) {
		this.node = node;
	}

	@Override
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				OpenFolder.this.node.openFolder();
			}
		});
	}
}
