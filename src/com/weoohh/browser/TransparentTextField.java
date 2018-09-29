package com.weoohh.browser;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JTextField;

public class TransparentTextField extends JTextField {
	
	private static final long serialVersionUID = 1L;
	
	public TransparentTextField() {
		setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setColor(getBackground());
//	    g2d.setComposite(AlphaComposite.SrcOver.derive(0.5f));
	    g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
	    super.paintComponent(g2d);

	}

	@Override
	protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
    }
}
