package com.weoohh.browser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sun.javafx.webkit.WebConsoleListener;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

public class SimpleSwingBrowser extends JFrame {

	private static final long serialVersionUID = 1L;
	private final JFXPanel jfxPanel = new JFXPanel();
    private WebEngine engine;
 
    private final JPanel panel = new JPanel(new BorderLayout());
    private final JLabel lblStatus = new JLabel();


    private final JButton btnHistoryLeft = new TransparentJButton("\u2190");
    private final JButton btnHistoryRight = new TransparentJButton("\u2192");
    private final JButton btnRefresh = new TransparentJButton("\u21BA");
    private final JTextField txtURL = new TransparentTextField();
    private final JProgressBar progressBar = new JProgressBar();
 
    public SimpleSwingBrowser() {
        super();
        initComponents();
    }

    
    private void initComponents() {
        createScene();
 
        ActionListener al = new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                loadURL(txtURL.getText());
            }
        };
        txtURL.addActionListener(al);
        
        ActionListener historyLeft = new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                engine.getHistory().go(-1);
            }
        };
        btnHistoryLeft.addActionListener(historyLeft);
        
        ActionListener historyRight = new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
                engine.getHistory().go(-1);
            }
        };
        btnHistoryRight.addActionListener(historyRight);
        
        ActionListener refresh = new ActionListener() {
            @Override 
            public void actionPerformed(ActionEvent e) {
            	loadURL(txtURL.getText());
            }
        };
        btnRefresh.addActionListener(refresh);
        
        progressBar.setPreferredSize(new Dimension(150, 18));
        progressBar.setStringPainted(true);
  
        JPanel topBar = new JPanel(new BorderLayout(5, 0));
        topBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        JPanel topLeftBar = new JPanel(new BorderLayout(2, 0));
        topLeftBar.add(btnHistoryLeft, BorderLayout.WEST);
        topLeftBar.add(btnHistoryRight, BorderLayout.CENTER);
        topLeftBar.add(btnRefresh, BorderLayout.EAST);
        topBar.add(topLeftBar, BorderLayout.WEST);
        topBar.add(txtURL, BorderLayout.CENTER);
        
        
        
 
        JPanel statusBar = new TransparentJPanel(new BorderLayout(5, 0), 1f);

        statusBar.setOpaque(true);
        statusBar.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        statusBar.add(lblStatus, BorderLayout.WEST);
        statusBar.add(progressBar, BorderLayout.EAST);
        
        panel.add(topBar, BorderLayout.NORTH);
        panel.add(jfxPanel, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);
        
        getContentPane().add(panel);
        
        setPreferredSize(new Dimension(1024, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

    }
 
    private void createScene() {
 
        Platform.runLater(new Runnable() {
            @Override 
            public void run() {
 
                WebView view = new WebView();
                engine = view.getEngine();
 
                
                jfxPanel.setScene(new Scene(view));
            }
        });
    }
    
    public class JavaBridge
    {
        public void log(String text)
        {
            System.out.println(text);
        }
    }
 
    public void loadURL(final String url) {
        Platform.runLater(new Runnable() {
            @Override 
            public void run() {
                String tmp = url;
 
                if (tmp.charAt(0) != '.' && toURL(tmp) == null) {
                	tmp = toURL("http://" + url);
                }
            	
            	if (toURL(tmp) == null) {
            		 try {
     					engine.loadContent(new String(Files.readAllBytes(Paths.get(tmp))));
     				} catch (IOException e) {
     					// TODO Auto-generated catch block
     					e.printStackTrace();
     				}
            	} else {
            		engine.load(tmp);
            	}
                
            	engine.titleProperty().addListener(new ChangeListener<String>() {

            		@Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                SimpleSwingBrowser.this.setTitle(newValue);
                            }
                        });
                    }
            		
            	});
            	
            	engine.locationProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> ov, String oldValue, final String newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                txtURL.setText(newValue);
                            }
                        });
                    }
                });
            	
            	engine.setOnStatusChanged(new EventHandler<WebEvent<String>>() {
                    @Override
                    public void handle(final WebEvent<String> event) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                lblStatus.setText(event.getData());
                            }
                        });
                    }
                });
            	
            	engine.getLoadWorker().workDoneProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, final Number newValue) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setValue(newValue.intValue());
                            }
                        });
                    }
                });
            	
            	WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> {
            	    System.out.println(message);
            	});
            	
            	WebConsoleListener.setDefaultListener(new WebConsoleListener(){
            	    @Override
            	    public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
            	        System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
            	    }
            	});
            	
            	engine.getLoadWorker().stateProperty().addListener(
                        new ChangeListener<State>() {
                    @SuppressWarnings("rawtypes")
					public void changed(ObservableValue ov, State oldState, State newState) {
                        if (newState == State.SUCCEEDED) {
                            engine.executeScript(""
                            		+ "console.log('COUCOU', [1,2,3]);"
                            		+ "document.getElementById(\"body\").style.display = \"none\";");
                        }
                    }
                });
            }
        });
    }

    private static String toURL(String str) {
        try {
            return new URL(str).toExternalForm();
        } catch (MalformedURLException exception) {
                return null;
        }
    }

   

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SimpleSwingBrowser browser = new SimpleSwingBrowser();
                browser.setVisible(true);
                browser.loadURL("google.com");
           }     
       });
    }

}
