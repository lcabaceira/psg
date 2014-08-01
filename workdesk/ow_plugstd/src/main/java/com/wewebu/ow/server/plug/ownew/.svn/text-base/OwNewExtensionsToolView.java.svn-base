package com.wewebu.ow.server.plug.ownew;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwToolExtension;
import com.wewebu.ow.server.dmsdialogs.views.OwToolView;
import com.wewebu.ow.server.dmsdialogs.views.OwToolViewItem;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.plug.std.log.OwLog;

/**
 *<p>
 * New extensions tool items view.
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
 */
public class OwNewExtensionsToolView extends OwToolView
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwNewExtensionsToolView.class);

    private List m_newExtensions = new ArrayList();

    /**
     * Constructor
     * @param extensions_p  a {@link Collection} of {@link OwToolExtension}s
     */
    public OwNewExtensionsToolView(Collection extensions_p)
    {
        super();
        m_newExtensions.addAll(extensions_p);
    }

    public Collection getToolItems()
    {
        return m_newExtensions;
    }

    public void invoke(OwToolViewItem item_p)
    {
        try
        {
            if (item_p instanceof OwToolExtension)
            {
                OwToolExtension newExtension = (OwToolExtension) item_p;

                newExtension.onClickEvent();

            }
            else
            {
                LOG.error("OwNewExtensionsToolView.invoke : Invalid OwNewExtensionToolView configuration - found non OwNewExtension item!");
                throw new OwConfigurationException(getContext().localize("ownew.OwNewExtensionsToolView.invalidconfiguration", "Invalid OwNewExtensionsToolView configuration!"));
            }
        }
        catch (Exception e)
        {
            ((OwMainAppContext) getContext()).setError(e);
        }
    }

}