package ch.fazzo.gadget.explorer.paint;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Model;

public abstract class UIElement<T extends Model<?>> {

	private Graphics2D g2d;
	private final Style style;
	private final T model;

	public UIElement(Style style, T model) {
		this.model = model;
		this.style = style;
	}

	protected abstract boolean isVisible();

	public void paint(Graphics g) {
		if (isVisible()) {
			create2d(g);
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

	protected void keyPressed(KeyEvent e) {
		// Does nothing
	}

}
