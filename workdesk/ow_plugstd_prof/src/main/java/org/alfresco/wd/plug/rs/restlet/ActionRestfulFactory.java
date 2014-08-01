package org.alfresco.wd.plug.rs.restlet;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.wd.ext.restlet.auth.OwRestletAuthenticationHandler;
import org.restlet.Client;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;

/**
 *<p>
 * Simple Helper to create queued Actions.
 * Capable to create and post Actions into Alfresco's action queue.
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
public class ActionRestfulFactory
{
    public static final String ACTION_URI = "/service/api/actionQueue";
    private Client clientConnector;
    private String baseUrl;
    private OwRestletAuthenticationHandler authHandler;

    public ActionRestfulFactory(String baseUrl, OwRestletAuthenticationHandler authHandler)
    {
        List<Protocol> protocols = new ArrayList<Protocol>();
        protocols.add(Protocol.HTTPS);
        protocols.add(Protocol.HTTP);
        this.clientConnector = new Client(protocols);
        this.baseUrl = baseUrl;
        this.authHandler = authHandler;
    }

    /**
     * Send an Action to Alfresco action queue.
     * @param postAction QueuedAction
     * @throws OwException
     */
    public Representation processAction(QueuedAction<?> postAction) throws OwException
    {
        ClientResource cr = createClientResource(ACTION_URI);
        return cr.post(postAction);
    }

    /**
     * Factory method to create ClientResource.
     * @param uri String relative to base URL
     * @return ClientResource
     * @throws OwException
     */
    protected ClientResource createClientResource(String uri) throws OwException
    {
        if (null == this.clientConnector)
        {
            throw new OwInvalidOperationException("RESTFull Factory was already released.");
        }
        ClientResource cr = new ClientResource(this.baseUrl + uri);
        this.authHandler.prepareCall(cr);
        cr.setNext(this.clientConnector);

        return cr;
    }

    /**
     * Close and release all resources.
     * @throws Exception
     */
    public void close() throws Exception
    {
        this.clientConnector.stop();
        this.clientConnector = null;
        this.authHandler = null;
    }

    /**
     * Factory method to create specific QueuedAction POJO.
     * @param actionName String name of action to trigger
     * @param objectId String node-Id action is executed on.
     * @return QueuedAction
     */
    public <T> QueuedAction<T> createQueuedAction(String actionName, String objectId)
    {
        QueuedAction<T> action = new QueuedAction<T>();
        action.setActionDefinitionName(actionName);
        action.setActionedUponNode(objectId);
        return action;
    }
}
