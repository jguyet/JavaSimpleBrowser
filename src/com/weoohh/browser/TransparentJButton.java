package com.weoohh.browser;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class TransparentJButton extends JButton {
	
	private static final long serialVersionUID = 1L;

	public TransparentJButton(String txt) {
		super(txt);
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}
}
