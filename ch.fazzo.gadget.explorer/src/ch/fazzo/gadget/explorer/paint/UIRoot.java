package ch.fazzo.gadget.explorer.paint;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Modification;
import ch.fazzo.gadget.explorer.model.Node;

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
	public void keyPressed(KeyEvent e) {
		if (model().isActive()) {
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
				model().modify(Modification.COPY);
				return;
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
				model().insertIntoParent();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				// hide favs
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				// show favs
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				model().closeFolder();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				model().openFolder();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				model().run();
				return;

			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				model().clearModify(Modification.DEL, Modification.CUT,
						Modification.COPY);
			}
		}
	}

	@Override
	protected void doAnimation() {
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
