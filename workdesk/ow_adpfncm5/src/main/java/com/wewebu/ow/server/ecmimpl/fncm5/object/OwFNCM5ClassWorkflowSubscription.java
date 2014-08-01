/**
 * 
 */
package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.filenet.api.core.WorkflowDefinition;
import com.filenet.api.events.ClassWorkflowSubscription;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5LaunchableWorkflowDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5Repository;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5ClassWorkflowSubscriptionClass;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Wrappes an instance of {@link ClassWorkflowSubscription}.
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
public class OwFNCM5ClassWorkflowSubscription extends OwFNCM5Subscription<ClassWorkflowSubscription> implements OwFNBPM5LaunchableWorkflowDescription
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNCM5ClassWorkflowSubscription.class);

    /**
     * @param nativeObject_p
     * @param clazz_p
     */
    public OwFNCM5ClassWorkflowSubscription(ClassWorkflowSubscription nativeObject_p, OwFNCM5ClassWorkflowSubscriptionClass clazz_p)
    {
        super(nativeObject_p, clazz_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription#getStepProcessorInfo()
     */
    public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.bpm.OwWorkflowDescription#getId()
     */
    public String getId()
    {
        return getID();
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5LaunchableWorkflowDescription#createLaunchableItem(com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5Repository, java.util.Collection)
     */
    public OwWorkitem createLaunchableItem(OwFNBPM5Repository fnbpmRepository_p, Collection attachmentobjects_p) throws Exception
    {
        WorkflowDefinition definition = getNativeObject().get_WorkflowDefinition();
        return fnbpmRepository_p.launch(definition, attachmentobjects_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5EngineObject#getName()
     */
    @Override
    public String getName()
    {
        return getNativeObject().get_Name();
    }
}
