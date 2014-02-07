package ch.fazzo.gadget.explorer.controlling;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitAdapter extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0); // An Exit Listener
	}
}
