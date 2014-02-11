package ch.fazzo.gadget.explorer.paint;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Modification;
import ch.fazzo.gadget.explorer.model.Node;

public class UIFolder extends UIElement<Node> {

	public UIFolder(Style style, Node node) {
		super(style, node);
	}

	@Override
	protected void paint(Graphics2D g2d) {
		int widghtNode = getStyle().getWidghtNode();
		int margin = getStyle().getMargin();
		int heightNode = getStyle().getHeightNode();

		int x = getX(widghtNode, margin, model());
		int y = getY(heightNode, margin, model());

		setColor(g2d, model());
		// drawBorder
		g2d.draw(new Rectangle2D.Double(x, y, widghtNode, heightNode));

		if (model().isModify()) {
			markNode(g2d, model(), Color.RED);
		} else if (model().isDir()) {
			markNode(g2d, model(), null);
		}

		g2d.draw(new Rectangle2D.Double(x + 1, y + 1, 5, heightNode - 2));
		drawName(g2d, model());

	}

	private void markNode(Graphics2D g2d, Node node, Color color) {
		int widghtNode = getStyle().getWidghtNode();
		int margin = getStyle().getMargin();
		int heightNode = getStyle().getHeightNode();

		int x = getX(widghtNode, margin, node);
		int y = getY(heightNode, margin, node);

		if (color != null) {
			g2d.setColor(color);
		}

		Rectangle2D rect = new Rectangle2D.Double(x + 1, y + 1, 5,
				heightNode - 2);
		g2d.fill(rect);

	}

	private void drawName(Graphics2D g2d, Node node) {
		int widghtNode = getStyle().getWidghtNode();
		int margin = getStyle().getMargin();
		int heightNode = getStyle().getHeightNode();

		int x = getX(widghtNode, margin, node);
		int y = getY(heightNode, margin, node);
		int fontSize = 14;
		Font textFont = new Font("Arial", Font.BOLD, fontSize);
		g2d.setFont(textFont);
		String nodeText = node.toString();
		int textLength = 20;
		if (nodeText.length() > textLength) {
			nodeText = node.toString().substring(0, textLength) + "...";
		}
		g2d.drawString(nodeText, x + 2 + fontSize, y + 2 + fontSize);
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

	private void setColor(Graphics2D g2d, Node node) {

		if (node.isInActiveHierarchy()) {
			g2d.setColor(getStyle().getActiveColor());
		} else {
			g2d.setColor(getStyle().getNormalColor());
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (model().isActive()) {
			if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
				model().modify(Modification.COPY);
				return;
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
				model().modify(Modification.CUT);
				return;
			} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
				model().insertIntoParent();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				model().modify(Modification.DEL);
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				model().selectPreviusNode();
				return;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				model().selectNextNode();
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
	protected boolean isVisible() {
		return model().isVisible();
	}

}
