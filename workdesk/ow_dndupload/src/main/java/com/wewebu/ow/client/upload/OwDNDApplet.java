package com.wewebu.ow.client.upload;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JApplet;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import netscape.javascript.JSObject;

import com.wewebu.ow.client.upload.js.OwJSTransferFile;

/**
 *<p>
 * Applet to handle D&D events and to upload files to the server. 
 * This is a temporary replacement for HTML 5 + XmlHttpRequest 2 features.
 *</p>
 *
 *<p><font size="-2">
 * Alfresco Workdesk<br/>
 * Copyright (c) Alfresco Software, Inc.<br/>
 * All rights reserved.<br/>
 * <br/>
 * For licensing information read the license.txt file or<br/>
 * go to: http://wiki.alfresco.com<br/>
 *</font></p>
 *@since 3.2.0.0
 *@see OwUploadProcess
 *@see OwJSTransferFile
 */
@SuppressWarnings("serial")
public class OwDNDApplet extends JApplet implements HyperlinkListener
{
    private static final Logger LOGGER = Logger.getLogger(OwDNDApplet.class.getName());
    private String messageHTML;

    private OwUploadCfg uploadCfg;

    private OwMessages messages;
    private OwCfgProperties cfgProperties = OwCfgProperties.empty();

    private OwDNDPlatform platform;
    protected JPopupMenu pastePopupMenu;

    /* (non-Javadoc)
     * @see java.applet.Applet#init()
     */
    @Override
    public void init()
    {
        LOGGER.log(Level.INFO, "Initializing...");
        long startTime = 0;
        if (LOGGER.isLoggable(Level.FINER))
        {
            startTime = System.currentTimeMillis();
        }
        try
        {
            readConfigurations();
            this.platform = OwDNDPlatform.newInstance(this);

            this.platform.setup();

            //Execute a job on the event-dispatching thread; creating this applet's GUI.
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run()
                {
                    OwDisabledGlassPane glass = new OwDisabledGlassPane();
                    glass.setBackground(Color.LIGHT_GRAY);
                    OwDNDApplet.this.getRootPane().setGlassPane(glass);

                    final JEditorPane editor = new JEditorPane();
                    //Hack: See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6993691
                    editor.setEditorKit(new HTMLEditorKit() {
                        protected Parser getParser()
                        {
                            try
                            {
                                @SuppressWarnings("rawtypes")
                                Class c = Class.forName("javax.swing.text.html.parser.ParserDelegator");
                                Parser defaultParser = (Parser) c.newInstance();
                                return defaultParser;
                            }
                            catch (Throwable e)
                            {
                                LOGGER.log(Level.SEVERE, "Error configuring the HTML parser.", e);
                            }
                            return null;
                        }
                    });
                    editor.setDragEnabled(false);
                    editor.setEditable(false);
                    //                    pane.setMargin(new Insets(0,0,0,0));
                    editor.setContentType("text/html");
                    HTMLDocument doc = new HTMLDocument();
                    editor.setDocument(doc);
                    editor.setText(OwDNDApplet.this.messageHTML);
                    editor.setMargin(new Insets(0, 0, 0, 0));
                    editor.addHyperlinkListener(OwDNDApplet.this);

                    editor.setBorder(null);

                    getContentPane().setLayout(new BorderLayout());
                    getContentPane().add(editor, BorderLayout.CENTER);

                    TransferHandler owTransferHandler = new OwTransferHandler(OwDNDApplet.this.platform);
                    editor.setTransferHandler(owTransferHandler);
                    editor.getDropTarget().setDefaultActions(DnDConstants.ACTION_COPY_OR_MOVE);

                    OwDNDApplet.this.pastePopupMenu = new JPopupMenu();
                    JMenuItem pasteItem = new JMenuItem(OwDNDApplet.this.messages.localize(OwMessages.UPLOAD_MESSAGES_PASTEMENUITEM, "Paste"));
                    pasteItem.setActionCommand((String) TransferHandler.getPasteAction().getValue(Action.NAME));
                    pasteItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e_p)
                        {
                            LOGGER.log(Level.FINER, "Handling a paste action !!!!");
                            String command = e_p.getActionCommand();
                            Action action = editor.getActionMap().get(command);
                            if (null != action)
                            {
                                action.actionPerformed(new ActionEvent(editor, ActionEvent.ACTION_PERFORMED, command));
                            }
                        }
                    });
                    OwDNDApplet.this.pastePopupMenu.add(pasteItem);

                    //Add a popup menu for the paste action
                    editor.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e_p)
                        {
                            if (e_p.isPopupTrigger())
                            {
                                showPopup(e_p);
                            }
                        }

                        @Override
                        public void mouseReleased(MouseEvent e_p)
                        {
                            if (e_p.isPopupTrigger())
                            {
                                showPopup(e_p);
                            }
                        }

                        private void showPopup(MouseEvent e_p)
                        {
                            if (!OwDNDApplet.this.platform.isAcceptingUploads())
                            {
                                return;
                            }
                            OwDNDApplet.this.pastePopupMenu.show(e_p.getComponent(), e_p.getX(), e_p.getY());
                        }
                    });
                    //Disabled by default
                    LOGGER.info("Disabling applet at the end of init.");
                    OwDNDApplet.this.setEnabled(false);
                }
            });
            signalAppletFinishedLoading();
        }
        catch (Exception e)
        {
            LOGGER.log(Level.SEVERE, "Failed to initialize", e);
            throw new RuntimeException("Failed to initialize", e);
        }
        finally
        {
            if (LOGGER.isLoggable(Level.FINER))
            {
                long endTime = System.currentTimeMillis();
                LOGGER.log(Level.FINER, "The init method took: " + (endTime - startTime) + " ms to execute!");
            }
        }
    }

    /**
     * Signals the JS engine that the DND applet has finished initializing. 
     */
    private void signalAppletFinishedLoading()
    {
        LOGGER.log(Level.INFO, "signalAppletFinishedLoading");
        JSObject jsWindow = JSObject.getWindow(this);
        jsWindow.eval("onDnDAppletInitialized()");
        LOGGER.log(Level.INFO, "signalAppletFinishedLoading done");
    }

    private void readConfigurations() throws OwUploadCfgException
    {
        try
        {
            OwAppletParams params = new OwAppletParams(this);

            this.messageHTML = params.getString(OwAppletParams.PARAM_MESSAGE);

            if (params.has(OwAppletParams.PARAM_PROPS_FILE))
            {
                URL paramsUrl = params.getURL(OwAppletParams.PARAM_PROPS_FILE);
                LOGGER.log(Level.CONFIG, OwAppletParams.PARAM_PROPS_FILE + "=" + paramsUrl);
                this.cfgProperties = OwCfgProperties.loadFrom(paramsUrl);
            }

            this.uploadCfg = new OwUploadCfg(params, this.cfgProperties);

            LOGGER.log(Level.CONFIG, "upload cfg=[\n" + this.uploadCfg.toString() + "]");
            LOGGER.log(Level.CONFIG, "message=" + messageHTML);

            this.messages = OwMessages.loadFrom(this);
        }
        catch (IOException ioe_p)
        {
            throw new OwUploadCfgException("Could not read upload configurations/parameters.", ioe_p);
        }
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#start()
     */
    @Override
    public void start()
    {
        LOGGER.log(Level.INFO, "Starting...");
        super.start();
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#stop()
     */
    @Override
    public void stop()
    {
        LOGGER.log(Level.INFO, "Stopping...");
        super.stop();
    }

    /* (non-Javadoc)
     * @see java.applet.Applet#destroy()
     */
    @Override
    public void destroy()
    {
        LOGGER.log(Level.INFO, "Destroying");
        super.destroy();
    }

    /* (non-Javadoc)
     * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
     */
    public void hyperlinkUpdate(HyperlinkEvent e_p)
    {
        if (!this.platform.isAcceptingUploads())
        {
            LOGGER.log(Level.FINER, "Any interactions are rejected while an upload is in progress ...");
            return;
        }

        final URL u = e_p.getURL();
        LOGGER.log(Level.FINE, ("Link cliked: " + e_p.getEventType()));
        if (e_p.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            LOGGER.log(Level.FINE, ("directing browser to: " + u + " on thread " + Thread.currentThread()));
            //Lousy hack.
            SwingUtilities.invokeLater(new Runnable() {

                public void run()
                {
                    getAppletContext().showDocument(u);

                }
            });
        }
    }

    /**
     * @return the uploadCfg
     */
    public OwUploadCfg getUploadCfg()
    {
        return uploadCfg;
    }

    /**
     * @return the messages
     */
    public OwMessages getMessages()
    {
        return messages;
    }

    /* (non-Javadoc)
     * @see java.awt.Component#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        LOGGER.log(Level.INFO, "Set enabled: " + enabled);
        super.setEnabled(enabled);
        getRootPane().getGlassPane().setVisible(!enabled);
    }
}
