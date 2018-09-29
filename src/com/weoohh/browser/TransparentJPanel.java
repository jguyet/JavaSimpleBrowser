package com.weoohh.browser;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class TransparentJPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private float panelAlfa;

	public TransparentJPanel(LayoutManager layout, float panelAlfa) {
		super(layout);
	    this.panelAlfa = panelAlfa;
	}

	@Override
	public void paintComponent(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setColor(getBackground());
	    g2d.setComposite(AlphaComposite.SrcOver.derive(panelAlfa));
	    super.paintComponent(g2d);

	}

	@Override
	protected void paintChildren(Graphics g) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setColor(getBackground());
	    g2d.setComposite(AlphaComposite.SrcOver.derive(panelAlfa));
	    super.paintChildren(g); 
	}
	
}
