package ch.fazzo.gadget.explorer.ui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Model;
import ch.fazzo.gadget.explorer.ui.actions.UIAction;

public abstract class UIElement<T extends Model<?>> {

	private Graphics2D g2d;
	private final Style style;
	private final T model;
	private int animationsspeed = 1;
	private int animationCounter = 0;

	public void setAnimationsspeed(int animationsspeed) {
		this.animationsspeed = animationsspeed;
	}

	public UIElement(Style style, T model) {
		this.model = model;
		this.style = style;
	}

	protected abstract boolean isVisible();

	public void paint(Graphics g) {

		if (isVisible()) {
			create2d(g);
			animate();
			paint(this.g2d);
			dispose2d();
		}
	}

	private void dispose2d() {
		this.g2d.dispose();
	}

	protected abstract void paint(Graphics2D g2d);

	public void create2d(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setStroke(new BasicStroke(1));
		this.g2d = g2d;
	}

	public void animate() {
		if (this.animationCounter == Integer.MAX_VALUE) {
			this.animationCounter = 0;
		}
		if (this.animationCounter % this.animationsspeed == 0) {
			doAnimation();

		}
		this.animationCounter++;

	}

	protected void doAnimation() {
		// Does nothing
	}

	public Style getStyle() {
		return this.style;
	}

	public T model() {
		return this.model;
	}

	public Graphics2D getG2d() {
		return this.g2d;
	}

	protected boolean consumesKeyEvent() {
		return true;
	}

	public Collection<? extends UIAction> keyPressed(KeyEvent e) {
		Set<UIAction> actions = new HashSet<UIAction>();
		if (consumesKeyEvent()) {
			consumeKeyEvent(e, actions);
		}
		return actions;
	}

	protected void consumeKeyEvent(KeyEvent e, Set<UIAction> actions) {

	}

}
