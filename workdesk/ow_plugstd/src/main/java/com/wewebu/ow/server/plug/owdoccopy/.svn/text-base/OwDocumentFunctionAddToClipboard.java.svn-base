package com.wewebu.ow.server.plug.owdoccopy;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwClipboardException;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.command.OwMultipleObjectsProcessCollector.OwObjectCollectData;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Implementation of common behavior for Cut and Copy document functions.
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
public abstract class OwDocumentFunctionAddToClipboard extends OwDocumentFunction
{
    /**
     * Create the concrete {@link OwCommandAddToClipboard} object.
     * @param oParent_p - the parent folder
     * @param objects_p - the collection of objects
     * @return - the {@link OwCommandAddToClipboard} object.
     */
    protected abstract OwCommandAddToClipboard createAddToClipboardCommand(final OwObject oParent_p, List objects_p);

    /**
     * Executes the addToClipboard command
     * @param oParent_p
     * @param objects_p
     * @throws Exception
     * @since 3.0.0.0
     */
    protected void executeAddToClipboadCommand(final OwObject oParent_p, List objects_p) throws Exception
    {
        OwCommandAddToClipboard command = createAddToClipboardCommand(oParent_p, objects_p);

        command.execute();

        if (command.hasDisabledObjects())
        {
            addHistoryEvent(command.getDisabledObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_DISABLED);
        }
        if (command.hasProcessedObjects())
        {
            addHistoryEvent(command.getProcessedObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }
        if (command.hasErrors())
        {
            addHistoryEvent(command.getAllErrorObjects(), oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            //check errors
            List failedObjectCollectData = command.getFailedObjectsData();
            //filter error messages
            Iterator collectDataIterator = failedObjectCollectData.iterator();
            StringBuffer messages = new StringBuffer();
            while (collectDataIterator.hasNext())
            {
                OwObjectCollectData objectCollectData = (OwObjectCollectData) collectDataIterator.next();
                if (objectCollectData.getFailureReason() != null)
                {
                    if (!(objectCollectData.getFailureReason() instanceof OwClipboardException))
                    {
                        messages.append(objectCollectData.getFailureReason().getLocalizedMessage());
                    }
                }
            }
            if (!messages.toString().equals(""))
            {
                throw new Exception(messages.toString());
            }
        }
    }

    /** 
     *  event called when user clicked the plugin for multiple selected items
     *  @param objects_p Collection of OwObject 
     *  @param oParent_p Parent which listed the Objects
     *  @param refreshCtx_p OwClientRefreshContext callback interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
     */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        executeAddToClipboadCommand(oParent_p, new LinkedList(objects_p));
    }

    /**
     * This method is called after the click event was processed. 
     * @param oObject_p
     * @param oParent_p
     * @throws Exception
     * @throws OwInvalidOperationException
     */
    protected abstract void onClickEventPostProcessing(OwObject oObject_p, final OwObject oParent_p) throws Exception, OwInvalidOperationException;

    /**  
     *  event called when user clicked the plugin label / icon
     *  @param oObject_p OwObject where event was triggered
     *  @param oParent_p Parent which listed the Object
     */
    public void onClickEvent(OwObject oObject_p, final OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List objects = new LinkedList();
        objects.add(oObject_p);

        executeAddToClipboadCommand(oParent_p, objects);
        onClickEventPostProcessing(oObject_p, oParent_p);
    }

}
