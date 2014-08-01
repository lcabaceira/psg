package com.wewebu.ow.server.plug.owdoccopy;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboard;
import com.wewebu.ow.server.app.OwClipboardContentOwObject;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;

/**
 *<p>
 * Represents a Cut/Copy or Move Operation performed on the clipboard.
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
 *@since 4.0.0.0.
 */
public abstract class OwClipboardMoveOperation
{

    private OwClipboard clipboard;
    private HashSet<OwObject> parentFoldersToBeRefreshed;
    private Map propertyMap;

    /**
     * @param clipboard The clipboard we are working on.
     * @param propertyMap
     */
    public OwClipboardMoveOperation(OwClipboard clipboard, Map propertyMap)
    {
        this.clipboard = clipboard;
        this.propertyMap = propertyMap;
        parentFoldersToBeRefreshed = new HashSet<OwObject>();
    }

    /**
     * @throws Exception 
     * 
     */
    public void execute() throws Exception
    {
        Iterator<?> it = clipboard.getContent().iterator();
        while (it.hasNext())
        {
            OwClipboardContentOwObject element = (OwClipboardContentOwObject) it.next();
            OwObject owObject = element.getObject();
            if (OwObjectReference.OBJECT_TYPE_FOLDER == owObject.getType())
            {
                OwObject parent = element.getParent();
                if (null != parent)
                {
                    parentFoldersToBeRefreshed.add(parent);
                }
            }
            processClipboardElement(element, this.propertyMap);
        }

        // empty clipboard after paste from cut
        clipboard.clearContent();
    }

    /**
     * @param element
     * @param propertyMap
     * @throws Exception 
     */
    protected abstract void processClipboardElement(OwClipboardContentOwObject element, Map propertyMap) throws Exception;

    /**
     * Send all accumulated updates to the given client context.
     * @param refreshCtx_p
     * @throws Exception 
     */
    public void sendUpdates(OwClientRefreshContext refreshCtx_p) throws Exception
    {
        if (null != refreshCtx_p)
        {
            if (!parentFoldersToBeRefreshed.isEmpty())
            {
                for (OwObject owFolder : parentFoldersToBeRefreshed)
                {
                    refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, owFolder);
                }
            }
        }
    }
}
