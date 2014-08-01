package com.wewebu.ow.server.ecmimpl.fncm5.object;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.filenet.api.core.WorkflowDefinition;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5LaunchableWorkflowDescription;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5Repository;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.ecmimpl.fncm5.objectclass.OwFNCM5WorkflowDefinitionClass;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

/**
 *<p>
 * Wrappes a {@link WorkflowDefinition} instance.
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
public class OwFNCM5WorkflowDefinition extends OwFNCM5Document<WorkflowDefinition> implements OwFNBPM5LaunchableWorkflowDescription
{
    private static final Logger LOG = OwLog.getLogger(OwFNCM5WorkflowDefinition.class);

    /**
     * @param nativeObject_p
     * @param clazz_p
     * @throws OwInvalidOperationException
     */
    public OwFNCM5WorkflowDefinition(WorkflowDefinition nativeObject_p, OwFNCM5WorkflowDefinitionClass clazz_p) throws OwInvalidOperationException
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
        WorkflowDefinition definition = getNativeObject();
        return fnbpmRepository_p.launch(definition, attachmentobjects_p);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecmimpl.fncm5.object.OwFNCM5ContainableObject#getName()
     */
    @Override
    public String getName()
    {
        return getNativeObject().get_Name();
    }
}
