package ch.fazzo.gadget.explorer.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Set;

import ch.fazzo.gadget.explorer.configuration.Style;
import ch.fazzo.gadget.explorer.model.DriveContext;
import ch.fazzo.gadget.explorer.ui.actions.UIAction;

public class UIFilterText extends UIElement<DriveContext> {

	public UIFilterText(Style style, DriveContext dc) {
		super(style, dc);
	}

	@Override
	protected void paint(Graphics2D g2d) {
		String text = model().getFilterText();
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

	@Override
	protected void consumeKeyEvent(KeyEvent e, Set<UIAction> actions) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F) {
			model().switchOnOffFilter();
			return;
		} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			model().removeOneLetterFromString();
			return;
		}

		String text = KeyEvent.getKeyText(e.getKeyCode());
		if (text.length() == 1) {
			model().addStringToFilter(text);
		}

	}

	@Override
	protected boolean isVisible() {
		return model().isFilterActive();
	}

}
