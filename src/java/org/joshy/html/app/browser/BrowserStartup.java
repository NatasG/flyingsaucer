package org.joshy.html.app.browser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joshy.html.*;
import org.joshy.html.box.*;
import org.joshy.html.swing.*;
import org.joshy.u;

import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class BrowserStartup {
    public static Logger logger = Logger.getLogger("app.browser");
    protected BrowserPanel panel;
    protected BrowserMenuBar menu;
    protected JFrame frame;
    protected HistoryManager history;
    protected JFrame validation_console = null;
    protected BrowserActions actions;
    
    protected ValidationHandler error_handler =  new ValidationHandler();
    public BrowserStartup() {
        logger.info("starting up");
        history = new HistoryManager();
    }
    
    public void init() {
        logger.info("creating UI");
        actions = new BrowserActions(this);
        actions.init();
        
        panel = new BrowserPanel(this);
        panel.init();
        panel.createLayout();
        panel.createActions();

        menu = new BrowserMenuBar(this);
        menu.init();
        menu.createLayout();
        menu.createActions();
        
        
    }
    
    public static void main(String[] args) throws Exception { 
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BrowserStartup bs = new BrowserStartup();
        bs.frame = frame;
        bs.init();
        frame.setJMenuBar(bs.menu);
        frame.getContentPane().add(bs.panel);
        frame.pack();
        frame.setSize(500,600);
        frame.show();
        if(args.length > 0) {
            bs.panel.loadPage(args[0]);
        }
    }
    
}



class SelectionMouseListener implements MouseListener, MouseMotionListener {
    protected HTMLPanel panel = null;    
    
    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mousePressed(MouseEvent e) {
        if(e.getComponent() instanceof HTMLPanel) {
            panel = (HTMLPanel)e.getComponent();
            Box box = panel.findBox(e.getX(),e.getY());
            if(box == null) return;
            // if box is text node then start selection
            if(box instanceof InlineBox) {
                int x = panel.findBoxX(e.getX(),e.getY());
                panel.getContext().setSelectionStart(box);
                panel.getContext().setSelectionStartX(x);
                panel.getContext().setSelectionEnd(box);
                panel.getContext().setSelectionEndX(x+1);
                panel.repaint();
            }
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        if(panel != null) {
            panel.getContext().clearSelection();
            panel.repaint();
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        if(e.getComponent() instanceof HTMLPanel) {
            panel = (HTMLPanel)e.getComponent();
            Box box = panel.findBox(e.getX(),e.getY());
            if(box == null) return;
            //u.p("pressed " + box);
            // if box is text node then start selection
            if((box.node != null &&
                box.node.getNodeName() != "body") &&
                !(box instanceof BlockBox)) {
                //u.p("box = " + box);
                int x = panel.findBoxX(e.getX(),e.getY());
                panel.getContext().setSelectionEnd(box);
                panel.getContext().setSelectionEndX(x);
                panel.repaint();
            }
        }
    }
    public void mouseMoved(MouseEvent e) { } 
}

















