package ch.fazzo.gadget.explorer.paint;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.Timer;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.DriveContext;
import ch.fazzo.gadget.explorer.model.Node;

public class NodePane extends JPanel {

	private static final long serialVersionUID = 1L;

	/** values for root animation */
	float step = 0.01f;
	float max = 0.7f;
	boolean up = true;
	float opacity = 0.0f;

	private final Set<Node> nodesToDraw = new HashSet<Node>();

	private final Style style;

	private final DriveContext dc;

	public NodePane(Node root, Style style, DriveContext dc) {
		this.nodesToDraw.add(root);
		this.style = style;
		this.dc = dc;
		setOpaque(false);

		createPaintTimer();

	}

	private void createPaintTimer() {
		new Timer(50, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actEvt) {
				doAnimation();

				repaint();
			}
		}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		// Again, we use anti-aliasing if possible.

		// Allow super to paint
		super.paintComponent(g);

		// Apply our own painting effect
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// 50% transparent Alpha

		g2d.setColor(this.style.getNormalColor());

		g2d.setStroke(new BasicStroke(1));
		for (Node node : getNodes()) {
			g2d.setStroke(new BasicStroke(1));

			if (node.isActive() && !node.isRoot()) {
				g2d.setColor(this.style.getActiveColor());
				drawConnections(g2d, node);
			}

			if (node.isRoot()) {
				if (node.isActive()) {
					g2d.setColor(this.style.getNormalColor());
				} else {
					g2d.setColor(this.style.getActiveColor());
				}

				Ellipse2D.Double circle = new Ellipse2D.Double(
						this.style.getxRoot(), this.style.getyRoot(),
						this.style.getWidghtRoot(), this.style.getHeightRoot());
				g2d.fill(circle);

				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, this.opacity));
				Ellipse2D.Double pulseCircle = new Ellipse2D.Double(
						this.style.getxRoot() - 5, this.style.getyRoot() - 5,
						20, 20);

				g2d.fill(pulseCircle);

			} else {
				g2d.setComposite(AlphaComposite.getInstance(
						AlphaComposite.SRC_OVER, 1f));
				setColor(g2d, node);
				drawBorder(g2d, node);
				drawName(g2d, node);
			}

		}

		drawFilterText(g2d);

		g2d.dispose();

	}

	private void drawFilterText(Graphics2D g2d) {
		String text = this.dc.getFilterText();
		if (text != null) {
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, 0.5f));
			int fontSize = 20;
			Font textFont = new Font("Arial", Font.BOLD, fontSize);
			g2d.setFont(textFont);
			g2d.setColor(Color.WHITE);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

			int x = (int) ((dim.getWidth() / 2) - ((text.length() / 2) * 3));
			int y = (int) (dim.getHeight() / 4);

			g2d.drawString("Filter:" + text, x, y);
		}
	}

	private Set<Node> getNodes() {

		Set<Node> nodes = new HashSet<Node>();

		for (Node node : this.nodesToDraw) {
			if (node.isVisible()) {
				nodes.add(node);
			}

		}

		return nodes;
	}

	private void drawConnections(Graphics2D g2d, Node node) {
		int widghtNode = this.style.getWidghtNode();
		int margin = this.style.getMargin();
		int heightNode = this.style.getHeightNode();
		int x = getX(widghtNode, margin, node);

		int y = getY(heightNode, margin, node);

		if (node.getParent() != null) {

			int xParent = getX(widghtNode, margin, node.getParent())
					+ widghtNode;
			int yParent = getY(heightNode, margin, node.getParent())
					+ (heightNode / 2);
			g2d.drawLine(xParent, yParent, xParent + (margin / 2), yParent);
			g2d.drawLine(xParent + (margin / 2), yParent, x - (margin / 2), y
					+ (heightNode / 2));
			g2d.drawLine(x - (margin / 2), y + (heightNode / 2), x, y
					+ (heightNode / 2));

			drawConnections(g2d, node.getParent());
		}
	}

	private int getY(int height, int margin, Node node) {
		return ((node.getRelativeIndex() * height) + (node.getRelativeIndex())
				* margin)
				+ this.style.getStartY() - (height / 2);
	}

	private int getX(int widght, int margin, Node node) {
		return (node.getRelativeLevel() * widght)
				+ (node.getRelativeLevel() * margin) + this.style.getStartX();
	}

	private void drawBorder(Graphics2D g2d, Node node) {
		int widghtNode = this.style.getWidghtNode();
		int margin = this.style.getMargin();
		int heightNode = this.style.getHeightNode();

		int x = getX(widghtNode, margin, node);
		int y = getY(heightNode, margin, node);
		// drawBorder
		g2d.draw(new Rectangle2D.Double(x, y, widghtNode, heightNode));

		if (node.isModify()) {
			markNode(g2d, node, Color.RED);
		} else if (node.isDir()) {
			markNode(g2d, node, null);
		}
		setColor(g2d, node);
		g2d.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 1f));
		g2d.draw(new Rectangle2D.Double(x + 1, y + 1, 5, heightNode - 2));

		g2d.setComposite(AlphaComposite
				.getInstance(AlphaComposite.SRC_OVER, 1f));
	}

	private void markNode(Graphics2D g2d, Node node, Color color) {
		int widghtNode = this.style.getWidghtNode();
		int margin = this.style.getMargin();
		int heightNode = this.style.getHeightNode();

		int x = getX(widghtNode, margin, node);
		int y = getY(heightNode, margin, node);

		if (color != null) {
			g2d.setColor(color);
		}

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.5f));
		Rectangle2D rect = new Rectangle2D.Double(x + 1, y + 1, 5,
				heightNode - 2);
		g2d.fill(rect);
		setColor(g2d, node);
	}

	private void setColor(Graphics2D g2d, Node node) {

		Node parent = this.dc.getActiveNode();

		boolean useActiveColor = false;
		while (parent != null) {
			if (node.equals(parent)) {
				useActiveColor = true;
				break;
			}
			parent = parent.getParent();

		}

		if (useActiveColor) {
			g2d.setColor(this.style.getActiveColor());
		} else {
			g2d.setColor(this.style.getNormalColor());
		}
	}

	private void drawName(Graphics2D g2d, Node node) {
		int widghtNode = this.style.getWidghtNode();
		int margin = this.style.getMargin();
		int heightNode = this.style.getHeightNode();

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

	public void addNodes(Set<Node> nodes) {
		this.nodesToDraw.removeAll(nodes);
		this.nodesToDraw.addAll(nodes);
	}

	public void removeNodes(Set<Node> nodes) {
		this.nodesToDraw.removeAll(nodes);

	}

	private void doAnimation() {
		animateRoot();
	}

	private void animateRoot() {
		if (this.dc.getActiveNode().isRoot()) {
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

}