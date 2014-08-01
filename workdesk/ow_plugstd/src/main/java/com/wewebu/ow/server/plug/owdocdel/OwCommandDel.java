package com.wewebu.ow.server.plug.owdocdel;

import java.util.Collection;
import java.util.Iterator;

import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.command.OwCommand;
import com.wewebu.ow.server.command.OwProcessableObjectStrategy;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.history.OwHistoryManager;
import com.wewebu.ow.server.history.OwStandardSessionHistoryDeleteEvent;

/**
 *<p>
 * Delete command, use to delete one or more objects. This class is decoupled from GUI.
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
public class OwCommandDel extends OwCommand
{
    /**the clipboard*/
    protected OwClipboard m_clipboard;
    /**flag about clipboard status*/
    protected boolean m_clipboardCleaned;

    /**
     * Constructs a delete command instance
     * @param objects_p - the objects to be deleted
     * @param appContext_p - application context
     * @param processableObjectStrategy_p - strategy to decide if a given object is processable.
     */
    public OwCommandDel(Collection objects_p, OwMainAppContext appContext_p, OwProcessableObjectStrategy processableObjectStrategy_p)
    {
        super(objects_p, appContext_p, processableObjectStrategy_p);
        m_clipboard = appContext_p.getClipboard();
        m_clipboardCleaned = false; //just for readability
    }

    /**
     * Delete an object.
     * @see com.wewebu.ow.server.command.OwCommand#processObject(com.wewebu.ow.server.ecm.OwObject)
     */
    protected void processObject(OwObject obj_p) throws Exception
    {
        String dmisdDeletedObject = obj_p.getDMSID();
        obj_p.delete();
        OwHistoryManager hm = m_appContext.getHistoryManager();
        if (hm != null)
        {
            hm.addEvent(OwEventManager.HISTORY_EVENT_TYPE_CLEAR_SESSION_HISTORY_FOR_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DELETE, new OwStandardSessionHistoryDeleteEvent(dmisdDeletedObject), OwEventManager.HISTORY_STATUS_OK);
        }
        // check if object is in the clipboard as well and clear it if so
        if (m_clipboard.getContentType() == OwClipboard.CONTENT_TYPE_OW_OBJECT && !m_clipboardCleaned)
        {
            Iterator itClipboard = m_clipboard.getContent().iterator();
            while (itClipboard.hasNext())
            {
                OwClipboardContentOwObject clipboardItem = (OwClipboardContentOwObject) itClipboard.next();
                if (dmisdDeletedObject.equals(clipboardItem.getObject().getDMSID()))
                {
                    m_clipboard.clearContent();
                    m_clipboardCleaned = true;
                    break;
                }
            }
        }

    }

}