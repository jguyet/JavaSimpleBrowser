package com.weoohh.browser;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class SimpleSwingBrowser {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
            	Window browser = new Window();
                browser.setVisible(true);
                browser.loadURL("google.com");
           }     
       });
    }

}
