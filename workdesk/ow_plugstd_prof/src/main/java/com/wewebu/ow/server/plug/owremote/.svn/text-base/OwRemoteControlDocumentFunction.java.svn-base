package com.wewebu.ow.server.plug.owremote;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Remote Control Link Function, creates links for remote control functions and sends them via email.
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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OwRemoteControlDocumentFunction extends OwDocumentFunction
{
    /**
    * Logger for this class
    */
    //private static final Logger LOG = OwLog.getLogger(OwRemoteControlDocumentFunction.class);

    /** how many links you can send per email */
    public int MAX_EMAIL_LINK_TO_SEND = 5;

    /** get the URL to the info icon
     * @return String URL
     */
    public String getIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("icon", "/images/plug/owdocemaillink/sendlink.png");
    }

    /** get the URL to the info icon
     * @return String URL
     */
    public String getBigIcon() throws Exception
    {
        return getContext().getDesignURL() + getConfigNode().getSafeTextValue("iconbig", "/images/plug/owdocemaillink/sendlink_24.png");
    }

    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
    }

    /** event called when user clicked the plugin label / icon
    *
    *  @param oObject_p OwObject where event was triggered
    *  @param oParent_p Parent which listed the Object
    *  @param refreshCtx_p OwClientRefreshContext call back interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onClickEvent(OwObject oObject_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        Collection coll_Objects_p = new LinkedList();
        coll_Objects_p.add(oObject_p);
        this.onMultiselectClickEvent(coll_Objects_p, oParent_p, refreshCtx_p);
    }

    /** event called when user clicked the plugin for multiple selected items
    *
    *  @param objects_p Collection of OwObject
    *  @param oParent_p Parent which listed the Objects
    *  @param refreshCtx_p OwClientRefreshContext call back interface for the function plugins to signal refresh events to clients, can be null if no refresh is needed
    *
    */
    public void onMultiselectClickEvent(Collection objects_p, OwObject oParent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List objects = null;

        if (objects_p != null)
        {
            objects = new LinkedList();
            for (Iterator i = objects_p.iterator(); i.hasNext();)
            {
                OwObject object = (OwObject) i.next();
                if (isEnabled(object, oParent_p, OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
                {
                    objects.add(object);
                }
            }
        }

        if (null != objects && (objects.size() <= MAX_EMAIL_LINK_TO_SEND))
        {
            boolean useLatestVersion = getConfigNode().getSafeBooleanValue("AlwaysUseLatestVersion", false);
            OwRemoteLinkBuilder builder = new OwRemoteLinkBuilder();
            OwRemoteLinkConfiguration linkConfiguration = new OwRemoteLinkConfiguration(getConfigNode());
            String script = builder.createJScript(objects, getContext(), linkConfiguration, "self.location.href", useLatestVersion);
            getContext().addFinalScript("//" + script.length() + "\n" + script);
            addHistoryEvent(objects, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }
        else
        {
            addHistoryEvent(objects, oParent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            throw new OwRemoteException(getContext().localize1("plugin.com.wewebu.ow.owdocemaillink.exception.too_many_elements_to_send", "It is not allowed to send more than %1 documents as e-mail link.", Integer.toString(MAX_EMAIL_LINK_TO_SEND)));
        }
    }

}