package com.wewebu.ow.server.plug.owdoccopy;

import java.util.Collection;

import com.wewebu.ow.server.app.OwClipboard.OwClipboardOperation;
import com.wewebu.ow.server.app.OwClipboardException;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.command.OwCommand;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.settingsimpl.OwSettingsPropertyClipboardBehavior;

/**
 *<p>
 * Command class for adding objects in clipboard.
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
 *@since 3.0.0.0
 */
public class OwCommandAddToClipboard extends OwCommand
{
    private static final String CLIPBOARD_BEHAVIOR_PROPERTY = "ClipboardBehavior";
    /** parent folder */
    private OwObject m_parent;
    /** flag for message warning status */
    private boolean m_isMessageWarningPosted = false;

    /**
     * Constructor.
     * @param parent_p - parent folder
     * @param objects_p - the collection of objects to be added in clipboard
     * @param appContext_p - application context.
     * @param clipboardOperation_p - the clipboard operation type (copy/cut)
     * @param plugin_p - the plugin that use this command.
     */
    public OwCommandAddToClipboard(OwObject parent_p, Collection objects_p, OwMainAppContext appContext_p, OwClipboardOperation clipboardOperation_p, OwDocumentFunction plugin_p)
    {
        super(objects_p, appContext_p, createProcessableStrategy(plugin_p, parent_p));
        this.m_parent = parent_p;
        if (m_appContext.getClipboard().getCut() != clipboardOperation_p.isCutOperation())
        {
            m_appContext.getClipboard().clearContent();
        }
        String clipboardType = m_appContext.getSafeStringAppSetting(CLIPBOARD_BEHAVIOR_PROPERTY, OwSettingsPropertyClipboardBehavior.OW_CLIPBOARD_BEHAVIOR_APPEND);
        if (OwSettingsPropertyClipboardBehavior.OW_CLIPBOARD_BEHAVIOR_APPEND.compareTo(clipboardType) != 0)
        {
            m_appContext.getClipboard().clearContent();
        }
        m_appContext.getClipboard().setCut(clipboardOperation_p.isCutOperation());
    }

    /**
     * Creates a {@link OwProcessableObjectStrategy} object for this command.
     * @param plugin_p
     * @param parent_p
     * @return the {@link OwProcessableObjectStrategy} object.
     */
    private static OwProcessableObjectStrategy createProcessableStrategy(final OwDocumentFunction plugin_p, final OwObject parent_p)
    {
        OwProcessableObjectStrategy processableObjectStrategy = new OwProcessableObjectStrategy() {
            /*
             * (non-Javadoc)
             * @see com.wewebu.ow.server.command.OwProcessableObjectStrategy#canBeProcessed(com.wewebu.ow.server.ecm.OwObject)
             */
            public boolean canBeProcessed(OwObject object_p) throws Exception
            {
                boolean result = plugin_p.isEnabled(object_p, parent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
                return result;
            }
        };
        return processableObjectStrategy;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.command.OwCommand#processObject(com.wewebu.ow.server.ecm.OwObject)
     */
    protected void processObject(OwObject object_p) throws Exception
    {
        if (m_appContext.getClipboard().canAdd())
        {
            m_appContext.getClipboard().addContent(object_p, m_parent);
        }
        else
        {
            if (!m_isMessageWarningPosted)
            {
                m_appContext.getClipboard().postWarningMessage();
                m_isMessageWarningPosted = true;
                //this is just an exception that will be treated specially, no i18n needed
            }
            throw new OwClipboardException("Clipboard is full: Cannot add more items in clipboard, the maximum clipboard size was reached!");
        }
    }
}
