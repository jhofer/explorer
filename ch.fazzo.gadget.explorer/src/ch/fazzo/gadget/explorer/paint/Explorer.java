package ch.fazzo.gadget.explorer.paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;

import javax.swing.JDialog;

import ch.fazzo.gadget.explorer.configuration.StyleFactory;
import ch.fazzo.gadget.explorer.controlling.ExitAdapter;
import ch.fazzo.gadget.explorer.model.DriveContext;
import ch.fazzo.gadget.explorer.model.Node;

public class Explorer extends JDialog {

	private static final long serialVersionUID = 1L;
	private int mouseX = 0;
	private int mouseY = 0;
	private final Renderer pane;
	private final DriveContext dc = new DriveContext();

	public Explorer(String drive) {

		File f = new File(drive + ":\\");
		Node root = new Node(f, this.dc);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		setLocation(0, 0);
		setSize(dim);
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));

		this.pane = new Renderer(root, StyleFactory.createStyle(drive), this.dc);

		setContentPane(this.pane);
		setLayout(new BorderLayout());

		addListeners();
		setVisible(true);

	}

	private void addListeners() {
		addWindowListener(new ExitAdapter());
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				Explorer.this.mouseX = e.getX();
				Explorer.this.mouseY = e.getY();

			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(
						getLocation().x + (e.getX() - Explorer.this.mouseX),
						getLocation().y + (e.getY() - Explorer.this.mouseY));
			}
		});

		addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				Explorer.this.pane.notifyKeyEvent(e);

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

}