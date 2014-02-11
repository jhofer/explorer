package ch.fazzo.gadget.explorer.ui;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.Set;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Node;
import ch.fazzo.gadget.explorer.ui.actions.ClearModify;
import ch.fazzo.gadget.explorer.ui.actions.CloseFolder;
import ch.fazzo.gadget.explorer.ui.actions.CopyFile;
import ch.fazzo.gadget.explorer.ui.actions.InsertFile;
import ch.fazzo.gadget.explorer.ui.actions.OpenFolder;
import ch.fazzo.gadget.explorer.ui.actions.RunFile;
import ch.fazzo.gadget.explorer.ui.actions.UIAction;

public class UIRoot extends UIElement<Node> {

	/** values for root animation */
	float step = 0.01f;
	float max = 0.7f;
	boolean up = true;
	float opacity = 0.0f;

	public UIRoot(Style style, Node node) {
		super(style, node);
	}

	@Override
	protected void paint(Graphics2D g2d) {
		if (model().isActive()) {
			g2d.setColor(getStyle().getNormalColor());
		} else {
			g2d.setColor(getStyle().getActiveColor());
		}

		Ellipse2D.Double circle = new Ellipse2D.Double(getStyle().getxRoot(),
				getStyle().getyRoot(), getStyle().getWidghtRoot(), getStyle()
						.getHeightRoot());
		g2d.fill(circle);

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				this.opacity));
		Ellipse2D.Double pulseCircle = new Ellipse2D.Double(getStyle()
				.getxRoot() - 5, getStyle().getyRoot() - 5, 20, 20);

		g2d.fill(pulseCircle);

	}

	@Override
	protected boolean consumesKeyEvent() {
		return model().isActive();
	}

	@Override
	protected void consumeKeyEvent(KeyEvent e, Set<UIAction> actions) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
			actions.add(new CopyFile(model()));
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
			actions.add(new InsertFile(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			// hide favs
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			// show favs
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			actions.add(new CloseFolder(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			actions.add(new OpenFolder(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			actions.add(new RunFile(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			actions.add(new ClearModify(model()));
		}

	}

	@Override
	public void doAnimation() {
		if (model().isActive()) {
			if (this.up) {
				this.opacity = this.opacity + this.step;
				if (this.opacity > this.max) {
					this.opacity = this.max;
					this.up = false;
				}
			} else {
				this.opacity = this.opacity - this.step;
				if (this.opacity < 0.0f) {
					this.opacity = 0.0f;
					this.up = true;
				}
			}
		} else {
			this.opacity = 0.0f;
		}
	}

	@Override
	protected boolean isVisible() {
		return model().isVisible();
	}

}
