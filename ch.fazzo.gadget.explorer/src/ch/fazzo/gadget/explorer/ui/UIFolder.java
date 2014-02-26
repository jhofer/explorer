package ch.fazzo.gadget.explorer.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.Set;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.Node;
import ch.fazzo.gadget.explorer.ui.actions.ClearModify;
import ch.fazzo.gadget.explorer.ui.actions.CloseFolder;
import ch.fazzo.gadget.explorer.ui.actions.CopyFile;
import ch.fazzo.gadget.explorer.ui.actions.CutFile;
import ch.fazzo.gadget.explorer.ui.actions.DeleteNode;
import ch.fazzo.gadget.explorer.ui.actions.InsertFile;
import ch.fazzo.gadget.explorer.ui.actions.OpenFolder;
import ch.fazzo.gadget.explorer.ui.actions.RunFile;
import ch.fazzo.gadget.explorer.ui.actions.SelectNext;
import ch.fazzo.gadget.explorer.ui.actions.SelectNextFiltered;
import ch.fazzo.gadget.explorer.ui.actions.SelectPrevious;
import ch.fazzo.gadget.explorer.ui.actions.UIAction;

public class UIFolder extends UIElement<Node> {

	private final int textLength = 20;
	private int textStart = 0;
	private boolean up = true;

	public UIFolder(Style style, Node node) {
		super(style, node);
		setAnimationsspeed(20);
	}

	@Override
	protected void paint(Graphics2D g2d) {
		int widghtNode = getStyle().getWidghtNode();
		int margin = getStyle().getMargin();
		int heightNode = getStyle().getHeightNode();

		int x = getX(widghtNode, margin, model());
		int y = getY(heightNode, margin, model());
		g2d.setColor(Color.BLACK);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		g2d.fill(new Rectangle2D.Double(x, y, widghtNode, heightNode));
		setColor(g2d, model());

		if (model().isFiltered()) {
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.2f));
		} else {
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 1f));
		}

		if (model().isModify()) {
			markNode(g2d, model(), Color.RED);
		} else if (model().isDir()) {
			markNode(g2d, model(), null);
		} else {
			// markNode(g2d, model(), Color.CYAN);
		}

		// drawBorder
		if (model().isNew()) {
			g2d.setColor(Color.WHITE);
		} else {
			setColor(g2d, model());
		}
		g2d.draw(new Rectangle2D.Double(x, y, widghtNode, heightNode));

		g2d.draw(new Rectangle2D.Double(x, y, 5, heightNode));
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

		Rectangle2D rect = new Rectangle2D.Double(x, y, 5, heightNode);
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
		int nodeEnd = this.textLength + this.textStart;
		if (nodeText.length() > this.textLength && nodeEnd <= nodeText.length()) {

			nodeText = node.toString().substring(this.textStart, nodeEnd);

			if (node.toString().length() == nodeEnd) {

				this.up = false;
			}
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
	protected void doAnimation() {
		if (model().isActive()) {
			if (this.up) {
				this.textStart++;
			} else {
				this.textStart--;
			}
			if (this.textStart == 0) {
				this.up = true;
			}
		}
	}

	@Override
	protected boolean consumesKeyEvent() {
		return model().isActive();
	}

	@Override
	protected void consumeKeyEvent(KeyEvent e, Set<UIAction> actions) {

		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_C) {
			actions.add(new CopyFile(model()));
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_X) {
			actions.add(new CutFile(model()));
		} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V) {
			actions.add(new InsertFile(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			actions.add(new DeleteNode(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			actions.add(new SelectPrevious(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			actions.add(new SelectNext(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			actions.add(new CloseFolder(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			actions.add(new OpenFolder(model()));
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			actions.add(new RunFile(model()));
			actions.add(new SelectNextFiltered(model().getDC()));
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			actions.add(new ClearModify(model()));
		}
	}

	@Override
	protected boolean isVisible() {
		return model().isVisible();
	}

}
