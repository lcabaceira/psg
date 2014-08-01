package com.wewebu.ow.client.upload.swing;

import java.awt.Container;
import java.awt.Frame;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import netscape.javascript.JSObject;

import com.wewebu.ow.client.upload.OwDNDApplet;
import com.wewebu.ow.client.upload.OwDNDPlatform;
import com.wewebu.ow.client.upload.OwDropTarget;
import com.wewebu.ow.client.upload.OwMessages;
import com.wewebu.ow.client.upload.OwUploadCfg;
import com.wewebu.ow.client.upload.OwUploader;

/**
 *<p>
 * Produces Swing based implementations.
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
 */
public class OwDNDPlatformSwing extends OwDNDPlatform
{
    private static final Logger LOGGER = Logger.getLogger(OwDNDPlatformSwing.class.getName());

    private static Frame rootFrame;

    private OwDropTargetSwing dropTarget;
    private OwUploaderSwing uploader;
    private OwUploadCfg uploadCfg;

    private OwDNDApplet owDNDApplet;

    private OwMessages messages;

    public OwDNDPlatformSwing(OwUploadCfg uploadCfg_p, OwMessages messages_p, OwDNDApplet owDNDApplet_p)
    {
        this.dropTarget = new OwDropTargetSwing();
        this.uploader = new OwUploaderSwing(uploadCfg_p, messages_p);
        this.uploadCfg = uploadCfg_p;
        this.owDNDApplet = owDNDApplet_p;
        this.messages = messages_p;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDNDPlatform#getDropTarget()
     */
    @Override
    public OwDropTarget getDropTarget()
    {
        return this.dropTarget;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDNDPlatform#setup()
     */
    @Override
    public void setup()
    {
        initializeRootFrame();
        this.dropTarget.addEventListener(OwDropTarget.EVT_TRANSFER, new OwDropTargetListenerImplSwing(this));
    }

    private void initializeRootFrame()
    {
        //OwDNDPlatformSwing.rootFrame = (Frame) SwingUtilities.windowForComponent(this.owDNDApplet);
        Container container = this.owDNDApplet;
        while (container != null)
        {
            if (container instanceof Frame)
            {
                OwDNDPlatformSwing.rootFrame = (Frame) container;
                LOGGER.info("Found root frame: " + OwDNDPlatformSwing.rootFrame);
            }
            container = container.getParent();
        }

        if (null == OwDNDPlatformSwing.rootFrame)
        {
            OwDNDPlatformSwing.rootFrame = JOptionPane.getRootFrame();
            LOGGER.info("Setting default root frame to: " + OwDNDPlatformSwing.rootFrame);
        }
    }

    protected void uploadCompleted()
    {
        JSObject jsWindow = JSObject.getWindow(this.owDNDApplet);
        jsWindow.call("uploadCompleted", new Object[] {});
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.client.upload.OwDNDPlatform#getUploader()
     */
    @Override
    public OwUploader getUploader()
    {
        return uploader;
    }

    /**
     * @return the rootFrame
     */
    public static Frame getRootFrame()
    {
        return rootFrame;
    }

    /**
     * @return the messages
     */
    public OwMessages getMessages()
    {
        return messages;
    }

    public OwUploadCfg getUploadCfg()
    {
        return uploadCfg;
    }
}
