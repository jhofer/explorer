package ch.fazzo.gadget.explorer.paint;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.DriveContext;
import ch.fazzo.gadget.explorer.model.Node;
import ch.fazzo.gadget.explorer.ui.UIConnection;
import ch.fazzo.gadget.explorer.ui.UIElement;
import ch.fazzo.gadget.explorer.ui.UIFile;
import ch.fazzo.gadget.explorer.ui.UIFilterText;
import ch.fazzo.gadget.explorer.ui.UIFolder;
import ch.fazzo.gadget.explorer.ui.UIRoot;
import ch.fazzo.gadget.explorer.ui.actions.UIAction;

public class Renderer extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;

	private final Style style;

	private final DriveContext dc;

	private final Set<UIElement<?>> elements = new HashSet<UIElement<?>>();

	public Renderer(Node root, Style style, DriveContext dc) {

		this.style = style;
		this.dc = dc;
		setOpaque(false);
		createPaintTimer();
		this.elements.addAll(createElements());
		dc.addObserver(this);
	}

	public void notifyKeyEvent(KeyEvent e) {
		Set<UIAction> actions = new HashSet<UIAction>();
		for (UIElement<?> uie : createElements()) {
			actions.addAll(uie.keyPressed(e));
		}
		for (UIAction uiAction : actions) {
			uiAction.run();
		}

	}

	private void createPaintTimer() {
		new Timer(30, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actEvt) {
				repaint();
			}
		}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		for (UIElement<?> e : this.elements) {

			e.paint(g);
		}
	}

	private Set<UIElement<?>> createElements() {
		Set<UIElement<?>> elements = new HashSet<UIElement<?>>();
		for (Node node : this.dc.getAllNodes()) {
			if (node.isDir()) {
				elements.add(new UIFolder(this.style, node));
				elements.add(new UIConnection(this.style, node));
			} else if (node.isRoot()) {
				elements.add(new UIRoot(this.style, node));
			} else {
				elements.add(new UIFile(this.style, node));
				elements.add(new UIConnection(this.style, node));
			}
		}
		elements.add(new UIFilterText(this.style, this.dc));
		return elements;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.elements.clear();
		Renderer.this.elements.addAll(createElements());

	}
}