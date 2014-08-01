package com.wewebu.ow.server.plug.owdocdel;

import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.command.OwCommand;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * Delete Reference command, use to delete one or more objects references. This class is decoupled from GUI.
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
public class OwCommandDelRef extends OwCommand
{

    /**the clipboard*/
    private OwClipboard m_clipboard;
    /**the parent object*/
    private OwObject m_parent;

    /**
     * Construct a OwCommandDelRef instance.
     * @param objects_p - a collection of objects
     * @param parent_p - the parent
     * @param appContext_p - the application context
     * @param processableObjectStrategy_p - strategy to decide if a given object can be processed.
     */
    public OwCommandDelRef(Collection objects_p, OwObject parent_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        super(objects_p, appContext_p, processableObjectStrategy_p);
        m_clipboard = appContext_p.getClipboard();
        m_parent = parent_p;
    }

    /**
     * Delete an object reference.
     * @see com.wewebu.ow.server.command.OwCommand#processObject(com.wewebu.ow.server.ecm.OwObject)
     */
    protected void processObject(OwObject object_p) throws Exception
    {
        m_parent.removeReference(object_p);

        // check if object is in the clipboard as well and clear it if so
        if (m_clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT)
        {
            Iterator itClipboard = m_clipboard.getContent().iterator();
            while (itClipboard.hasNext())
            {
                OwClipboardContentOwObject item = (OwClipboardContentOwObject) itClipboard.next();
                if (item.getObject().getDMSID().equals(object_p.getDMSID()))
                {
                    m_clipboard.clearContent();
                    break;
                }
            }
        }
    }

}