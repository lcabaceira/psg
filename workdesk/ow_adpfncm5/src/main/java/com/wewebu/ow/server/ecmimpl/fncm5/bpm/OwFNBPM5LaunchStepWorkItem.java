package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.conf.OwBaseUserInfo;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;

import filenet.vw.api.VWException;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWStepProcessorInfo;
import filenet.vw.api.VWWorkObject;

/**
 *<p>
 * A FN BPM launch step work item. 
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
public class OwFNBPM5LaunchStepWorkItem extends OwFNBPM5StepWorkItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5Repository.class);

    private VWStepElement m_stepElement;

    public OwFNBPM5LaunchStepWorkItem(OwFNBPM5LaunchContainer launchContainer_p, VWStepElement stepElement_p) throws Exception
    {
        super(launchContainer_p);
        this.m_stepElement = stepElement_p;
        loadProperties();
    }

    private void loadProperties() throws Exception
    {
        if (!m_fParametersLoaded)
        {
            // === also load the name parameter
            OwProperty nameProp = new OwStandardProperty("", getObjectClass().getPropertyClass(OwFNBPM5WorkItemObjectClass.NAME_PROPERTY));
            m_properties.put(nameProp.getPropertyClass().getClassName(), nameProp);

            super.loadParameters();
        }
    }

    public boolean canResponse()
    {
        return false;
    }

    protected VWStepProcessorInfo getNativeStepProcessorInfo() throws VWException
    {
        return m_stepElement.fetchStepProcessorInfo();
    }

    protected String getQueueName() throws Exception
    {
        return getQueue().getName();
    }

    public String getResponse() throws Exception
    {
        return null;
    }

    public Collection getResponses() throws Exception
    {
        return null;
    }

    public void setResponse(String strResponse_p) throws Exception
    {

    }

    public boolean canForcedelete(int context_p) throws Exception
    {
        return false;
    }

    public boolean canReassignToPublicContainer(int context_p) throws Exception
    {
        return false;
    }

    public boolean canReassignToUserContainer(int context_p) throws Exception
    {
        return false;
    }

    public boolean canResubmit(int context_p) throws Exception
    {
        return false;
    }

    public boolean canReturnToSource(int context_p) throws Exception
    {
        return false;
    }

    public void forcedelete() throws Exception
    {

    }

    public Date getResubmitDate(int context_p) throws Exception
    {
        return null;
    }

    public void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception
    {

    }

    public void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception
    {

    }

    public void resubmit(Date date_p) throws Exception
    {

    }

    public void returnToSource() throws Exception
    {

    }

    public boolean canDelete(int context_p) throws Exception
    {
        return false;
    }

    public boolean canLock() throws Exception
    {
        return false;
    }

    public void delete() throws Exception
    {

    }

    public boolean getLock(int context_p) throws Exception
    {
        return true;
    }

    public String getLockUserID(int context_p) throws Exception
    {
        OwFNBPM5BaseContainer container = getQueue();
        OwNetworkContext context = container.getContext();
        OwBaseUserInfo currentUser = context.getCurrentUser();
        return currentUser.getUserName();
    }

    public boolean getMyLock(int context_p) throws Exception
    {
        return false;
    }

    public Object getNativeObject() throws Exception
    {
        return m_stepElement;
    }

    public boolean setLock(boolean lock_p) throws Exception
    {
        return false;
    }

    protected VWStepElement getStepElement() throws Exception
    {
        return m_stepElement;
    }

    protected VWWorkObject getWorkObject() throws Exception
    {

        return m_stepElement.fetchWorkObject(true, true);
    }

    public String getDMSID() throws Exception
    {
        return OwFNBPM5Repository.getDMSID(m_stepElement.getWorkObjectNumber(), getResourceID());
    }

    public String getID()
    {
        try
        {
            return m_stepElement.getWorkObjectNumber();
        }
        catch (Exception e)
        {
            LOG.error("OwFNBPMLaunchStepWorkItem.getID: Could not resolve work item ID of work item: " + getName(), e);
            return null;
        }
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitem/item";
    }

    public int getType()
    {
        return OBJECT_TYPE_WORKITEM;
    }

    protected boolean isAutoSave()
    {
        return false;
    }

}
