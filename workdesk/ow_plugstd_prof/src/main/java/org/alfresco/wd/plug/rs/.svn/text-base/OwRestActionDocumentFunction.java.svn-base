package org.alfresco.wd.plug.rs;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.alfresco.wd.plug.rs.restlet.ActionRestfulFactory;
import org.alfresco.wd.plug.rs.restlet.QueuedAction;
import org.apache.log4j.Logger;

import com.wewebu.ow.server.app.OwClientRefreshContext;
import com.wewebu.ow.server.app.OwDocumentFunction;
import com.wewebu.ow.server.app.OwMainAppContext;
import com.wewebu.ow.server.app.OwUpdateCodes;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.plug.std.prof.log.OwLog;
import com.wewebu.ow.server.role.OwRoleManagerContext;
import com.wewebu.ow.server.util.OwObjectIDCodeUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * DocumentFunction for REST based calls.
 * Basic integration for handling of Alfresco actions.
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
 *@since 4.2.0.0
 */
public class OwRestActionDocumentFunction extends OwDocumentFunction
{
    private static final Logger LOG = OwLog.getLogger(OwRestActionDocumentFunction.class);
    private String propertyId, baseUrl, actionDefinitionName;

    @Override
    public void init(OwXMLUtil node_p, OwMainAppContext context_p) throws Exception
    {
        super.init(node_p, context_p);
        propertyId = node_p.getSafeTextValue("ObjectIdProperty", "alfcmis:nodeRef");
        baseUrl = node_p.getSafeTextValue("RestBaseUrl", null);
        if (baseUrl == null)
        {
            baseUrl = getContext().getConfiguration().getGlobalParameters().getSafeString("EcmBaseUrl", null);
        }
        actionDefinitionName = node_p.getSafeTextValue("ActionDefinitionName", null);
    }

    @Override
    public void onClickEvent(OwObject obj_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        List<OwObject> col = new LinkedList<OwObject>();
        col.add(obj_p);
        onMultiselectClickEvent(col, parent_p, refreshCtx_p);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void onMultiselectClickEvent(Collection objCol_p, OwObject parent_p, OwClientRefreshContext refreshCtx_p) throws Exception
    {
        addHistoryEvent(objCol_p, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_BEGIN);
        Iterator<?> it = objCol_p.iterator();
        ActionRestfulFactory restFactory = createActionRestfulFactory();
        Collection<OwObject> succeed = new LinkedList<OwObject>();
        Collection<OwObject> failed = new LinkedList<OwObject>();
        while (it.hasNext())
        {
            OwObject obj = (OwObject) it.next();
            try
            {
                processObject(restFactory, obj, parent_p);
                succeed.add(obj);
            }
            catch (Exception e)
            {
                failed.add(obj);
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("Failed to process object", e);
                }
            }
        }

        try
        {
            restFactory.close();
        }
        catch (Exception e)
        {
            String msg = "Could not release Restlet resources";
            if (LOG.isDebugEnabled())
            {
                LOG.warn(msg, e);
            }
            else
            {
                LOG.warn(msg);
            }
        }

        getContext().postMessage(processResults(parent_p, objCol_p, succeed, failed));

        if (refreshCtx_p != null)
        {
            refreshCtx_p.onClientRefreshContextUpdate(OwUpdateCodes.UPDATE_OBJECT_CHILDS, null);
        }
    }

    /** (overridable)
     * Called after provided Objects were processed.
     * <p>By default it will check the different collections, create corresponding audit event and return a message for the results</p>  
     * @param parent_p OwObject current parent (can be null)
     * @param providedObj Collection of objects to be processed
     * @param succeeded Collection of successful processed objects
     * @param failed Collection of objects which could not be processed successfully
     * @return String message to be shown in UI
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    protected String processResults(OwObject parent_p, Collection providedObj, Collection<OwObject> succeeded, Collection<OwObject> failed) throws Exception
    {
        String processedMsg;
        //Audit of function
        if (!succeeded.isEmpty())
        {
            addHistoryEvent(succeeded, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_OK);
        }

        if (!failed.isEmpty())
        {
            addHistoryEvent(failed, parent_p, OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI, OwEventManager.HISTORY_STATUS_FAILED);
            //User Feedback message
            if (!succeeded.isEmpty() && succeeded.size() < providedObj.size())
            {
                processedMsg = getContext().localize2("plug." + getPluginID() + ".msg.failed.some", "Only %1 out of %2 were processed successful", Integer.toString(succeeded.size()), Integer.toString(providedObj.size()));
            }
            else
            {
                processedMsg = getContext().localize("plug." + getPluginID() + ".msg.failed.all", "Failed to process selected Object(s)");
            }
        }
        else
        {
            processedMsg = getContext().localize1("plug." + getPluginID() + ".msg.succeed", "Action \"%1\" successful processed", getPluginTitle());
        }
        return processedMsg;
    }

    /**
     * Main method to process REST service call, used by {@link #onMultiselectClickEvent(Collection, OwObject, OwClientRefreshContext)}.
     * @param restFactory ActionRestfulFactory current REST factory class
     * @param processedObj OwObject current processed object
     * @param parent OwObject parent (can be null)
     * @throws Exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void processObject(ActionRestfulFactory restFactory, OwObject processedObj, OwObject parent) throws Exception
    {
        QueuedAction action = restFactory.createQueuedAction(getActionDefinitionName(), getObjectId(processedObj));
        action.setParameterValues(createParameterValues(processedObj, parent));
        restFactory.processAction(action);
    }

    /**
     * Create an parameterValues object base on processed object and parent.
     * <p>By default method returns null, any returned reference will
     * be add to action which is send as request.</p>
     * @param processedObj OwObject currently processed
     * @param parent OwObject (can be null)
     * @return Object (or null)
     */
    protected Object createParameterValues(OwObject processedObj, OwObject parent)
    {
        return null;
    }

    /**
     * Get an representation Id of the processed object.
     * Will retrieve the configured &lt;ObjectIdProperty&gt; which is by default &quot;alfcmis:nodeRef&quot;.
     * @param processedObj OwObject to get Id from
     * @return String Id representation
     * @throws Exception
     * @see #getPropertyId()
     */
    protected String getObjectId(OwObject processedObj) throws Exception
    {
        Object value = processedObj.getProperty(getPropertyId()).getValue();
        if (value instanceof OwObject)
        {
            OwObject nodeRef = ((OwObject) value);
            String id;
            if (nodeRef.hasVersionSeries())
            {
                id = nodeRef.getVersionSeries().getId();
            }
            else
            {
                id = nodeRef.getID();
            }
            return OwObjectIDCodeUtil.decode(id);
        }
        else
        {
            return value.toString();
        }
    }

    /**
     * Factory to create ActionRestfulFactory instance
     * @return ActionRestfulFactory
     * @throws OwException
     */
    protected ActionRestfulFactory createActionRestfulFactory() throws OwException
    {
        OwRoleManagerContext ctx = getContext().getRegisteredInterface(OwRoleManagerContext.class);
        if (ctx.getNetwork().hasInterface(OwRestletAuthenticationHandler.class.getCanonicalName()))
        {
            OwRestletAuthenticationHandler authHandler;
            try
            {
                authHandler = (OwRestletAuthenticationHandler) ctx.getNetwork().getInterface(OwRestletAuthenticationHandler.class.getCanonicalName(), null);
            }
            catch (Exception e)
            {
                throw new OwServerException("Unable to retrieve OwRestletAuthenticationHandler", e);
            }
            return new ActionRestfulFactory(getRestBaseUrl(), authHandler);
        }
        else
        {
            throw new OwServerException("Missing Authentication Interface");
        }
    }

    @Override
    public boolean isEnabled(OwObject oObject_p, OwObject oParent_p, int iContext_p) throws Exception
    {
        return getPropertyId() != null && getActionDefinitionName() != null && super.isEnabled(oObject_p, oParent_p, iContext_p);
    }

    /**
     * Get configured value of &lt;RestBaseUrl&gt;.
     * @return String base URL for Rest Call or null.
     */
    protected String getRestBaseUrl()
    {
        return this.baseUrl;
    }

    /**
     * Return the &lt;ObjectIdProperty&gt; value, which is by default <b>alfcmis:nodeRef</b>.
     * @return String (can return null)
     */
    protected String getPropertyId()
    {
        return this.propertyId;
    }

    /**
     * Get the &lt;ActionDefinitionName&gt; value, defining the action to trigger.
     * @return String (can be null)
     */
    protected String getActionDefinitionName()
    {
        return this.actionDefinitionName;
    }
}
