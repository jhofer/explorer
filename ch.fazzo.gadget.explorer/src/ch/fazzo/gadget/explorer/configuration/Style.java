package ch.fazzo.gadget.explorer.configuration;

import java.awt.Color;

public class Style {

	private final Color normalColor = new Color(160, 217, 255);

	private final Color activeColor = new Color(255, 137, 51);

	private final int widghtNode = 200;
	private final int heightNode = 20;
	private final int margin = 10;
	private final int widghtRoot = 10;
	private final int heightRoot = 10;

	private final int startX = 400;
	private final int startY = 50;
	private final int xRoot = this.startX - (this.widghtRoot) - this.margin;

	private final int yRoot = this.startY - (this.heightRoot / 2);

	public Color getNormalColor() {
		return this.normalColor;
	}

	public Color getActiveColor() {
		return this.activeColor;
	}

	public int getWidghtNode() {
		return this.widghtNode;
	}

	public int getHeightNode() {
		return this.heightNode;
	}

	public int getMargin() {
		return this.margin;
	}

	public int getWidghtRoot() {
		return this.widghtRoot;
	}

	public int getHeightRoot() {
		return this.heightRoot;
	}

	public int getStartX() {
		return this.startX;
	}

	public int getStartY() {
		return this.startY;
	}

	public int getxRoot() {
		return this.xRoot;
	}

	public int getyRoot() {
		return this.yRoot;
	}

}
