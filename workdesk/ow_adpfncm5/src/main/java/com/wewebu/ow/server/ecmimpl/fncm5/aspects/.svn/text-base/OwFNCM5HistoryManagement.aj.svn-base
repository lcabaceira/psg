package com.wewebu.ow.server.ecmimpl.fncm5.aspects;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.history.OwStandardHistoryObjectCreateEvent;
import com.wewebu.ow.server.history.OwStandardHistoryPropertyChangeEvent;

/**
 *<p>
 * History management aspect.
 *</p>
 *Advises {@link Historized} annotated methods with history event additions
 *@see OwEventManager
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
public aspect OwFNCM5HistoryManagement 
{
    private OwEventManager getEventManager()
    {
        OwFNCM5Network localNetwork = OwFNCM5Network.localNetwork();
        OwEventManager eventManager = localNetwork.getEventManager();
        return eventManager;
    }

    private void addEvent(OwEventManager eventManager_p, int iEventType_p, String strEventID_p, int iStatus_p) throws OwException
    {
        try
        {

            eventManager_p.addEvent(iEventType_p, strEventID_p, iStatus_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Event manager exception!", e);
        }
    }

    private void addEvent(OwEventManager eventManager_p, int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws OwException
    {
        try
        {

            eventManager_p.addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
        }
        catch (OwException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new OwServerException("Event manager exception!", e);
        }
    }

    private void addEvent(int iEventType_p, String strEventID_p, int iStatus_p) throws OwException
    {
        OwEventManager eventManager = getEventManager();
        addEvent(eventManager, iEventType_p, strEventID_p, iStatus_p);
    }

    private void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws OwException
    {
        OwEventManager eventManager = getEventManager();
        addEvent(eventManager, iEventType_p, strEventID_p, event_p, iStatus_p);
    }
    

    pointcut newObject(Historized event) : execution(@Historized * *.createNewObject(..) ) && @annotation(event) ;

    pointcut setProperties(Historized event) : execution(@Historized * *.setProperties(..) ) && @annotation(event) ;

    after(Historized event, boolean fPromote_p, Object mode_p, OwResource resource_p, String strObjectClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p, OwContentCollection content_p, OwObject parent_p,
            String strMimeType_p, String strMimeParameter_p, boolean fKeepCheckedOut_p) returning(String dmsid) throws OwException : newObject(event) && args(fPromote_p,mode_p,resource_p,strObjectClassName_p,properties_p,permissions_p,content_p,parent_p,strMimeType_p, strMimeParameter_p, fKeepCheckedOut_p){
        OwStandardHistoryObjectCreateEvent owEvent=new OwStandardHistoryObjectCreateEvent(strObjectClassName_p, dmsid, properties_p, resource_p, parent_p, permissions_p);
        addEvent(event.type(), event.id(), owEvent, OwEventManager.HISTORY_STATUS_OK);
    }

           
    void around(Historized event, OwPropertyCollection properties_p,Object mode_p,OwObject object) throws OwException : setProperties(event) && target(object) && args(properties_p,mode_p){
        OwPropertyCollection oldProperties;
        try
        {
            oldProperties = object.getClonedProperties(properties_p.keySet());
        }
        catch (Exception e)
        {
            throw new OwServerException("",e);
        }
        proceed(event,properties_p,mode_p,object);
        OwStandardHistoryPropertyChangeEvent owEvent=new OwStandardHistoryPropertyChangeEvent(object, oldProperties, properties_p);
        addEvent(event.type(), event.id(), owEvent, OwEventManager.HISTORY_STATUS_OK);
    }

    after(Historized event) returning(Object o) throws OwException : !newObject(Historized) && !setProperties(Historized) && execution(@Historized * *(..) ) && @annotation(event) {
        addEvent(event.type(), event.id(), OwEventManager.HISTORY_STATUS_OK);
    }

    after(Historized event)  throwing(Exception thrownException) throws OwException: execution(@Historized * *(..) throws OwException) && @annotation(event) {
        addEvent(event.type(), event.id(), OwEventManager.HISTORY_STATUS_FAILED);
    }
}
