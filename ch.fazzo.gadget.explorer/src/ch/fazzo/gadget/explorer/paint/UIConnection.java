package ch.fazzo.gadget.explorer.paint;

import java.awt.Graphics2D;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Node;

public class UIConnection extends UIElement<Node> {

	public UIConnection(Style style, Node node) {
		super(style, node);
	}

	@Override
	protected void paint(Graphics2D g2d) {
		int widghtNode = getStyle().getWidghtNode();
		int margin = getStyle().getMargin();
		int heightNode = getStyle().getHeightNode();
		int x = getX(widghtNode, margin, model());
		int y = getY(heightNode, margin, model());

		int xParent = getX(widghtNode, margin, model().getParent())
				+ widghtNode;
		int yParent = getY(heightNode, margin, model().getParent())
				+ (heightNode / 2);
		g2d.drawLine(xParent, yParent, xParent + (margin / 2), yParent);
		g2d.drawLine(xParent + (margin / 2), yParent, x - (margin / 2), y
				+ (heightNode / 2));
		g2d.drawLine(x - (margin / 2), y + (heightNode / 2), x, y
				+ (heightNode / 2));

	}

	private int getY(int height, int margin, Node node) {
		return ((node.getRelativeIndex() * height) + (node.getRelativeIndex())
				* margin)
				+ getStyle().getStartY() - (height / 2);
	}

	private int getX(int widght, int margin, Node node) {
		return (node.getRelativeLevel() * widght)
				+ (node.getRelativeLevel() * margin) + getStyle().getStartX();
	}

	@Override
	protected boolean isVisible() {
		return model().isInActiveHierarchy() && model().getParent().isVisible();
	}

}
