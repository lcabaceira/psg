package com.wewebu.ow.client.upload;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 *<p>
 * Our own transfer handler to implement DnD and Paste actions.
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
@SuppressWarnings("serial")
public class OwTransferHandler extends TransferHandler
{
    private static final Logger LOGGER = Logger.getLogger(OwTransferHandler.class.getName());
    private OwDNDPlatform platform;

    public OwTransferHandler(OwDNDPlatform platform_p)
    {
        this.platform = platform_p;
    }

    @Override
    public boolean importData(JComponent comp_p, Transferable t_p)
    {
        if (!canImport(comp_p, t_p.getTransferDataFlavors()))
        {
            return false;
        }
        LOGGER.log(Level.FINER, "Handling a paste!");
        try
        {
            this.platform.getDropTarget().fire(OwDropTarget.EVT_TRANSFER, t_p);
        }
        catch (OwDNDException e)
        {
            LOGGER.log(Level.SEVERE, "Error handling drop/paste event. We will just ignore it.", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean canImport(JComponent comp_p, DataFlavor[] transferFlavors_p)
    {
        if (!this.platform.isAcceptingUploads())
        {
            LOGGER.log(Level.INFO, "Drops/Pastes are rejected while an upload is in progress ...");
            return false;
        }
        boolean supportsFiles = isDataFlavorSupported(transferFlavors_p, DataFlavor.javaFileListFlavor);
        return supportsFiles;
    }

    private boolean isDataFlavorSupported(DataFlavor[] transferFlavors_p, DataFlavor flavour_p)
    {
        for (DataFlavor dataFlavor : transferFlavors_p)
        {
            if (flavour_p.equals(dataFlavor))
            {
                return true;
            }
        }
        return false;
    }
}
