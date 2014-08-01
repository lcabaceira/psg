package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwLockDeniedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.exceptions.OwServerException;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwStandardEnum;
import com.wewebu.ow.server.history.OwStandardSessionHistoryDeleteEvent;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

import filenet.vw.api.VWDataField;
import filenet.vw.api.VWException;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWQueueElement;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWStepProcessorInfo;
import filenet.vw.api.VWWorkObject;

/**
 *<p>
 * FileNet BPM Repository. <br/>
 * A single work item which is visible in queue's and can be
 * forwarded, edited or passed back. 
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
public class OwFNBPM5QueueWorkItem extends OwFNBPM5StepWorkItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5QueueWorkItem.class);

    /** reference to the wrapped queue-element */
    private VWQueueElement m_queueelement;

    /** reference to the wrapped workobject */
    protected VWWorkObject m_workobject;

    /** reference to the wrapped stepelement */
    protected VWStepElement m_stepelement;

    /** snapshot of the locked status */
    protected boolean m_fLocked = false;

    /** Creates a new instance of OwFNBPMQueueWorkItem */
    public OwFNBPM5QueueWorkItem(OwFNBPM5BaseContainer queue_p, VWQueueElement item_p) throws Exception
    {
        super(queue_p);
        m_queueelement = item_p;

        // === init the system defined values
        // Set name 
        addSystemProperty(OwFNBPM5WorkItemObjectClass.STEP_NAME_PROPERTY, item_p.getStepName());
        m_fLocked = (VWQueueElement.LOCKED_BY_NONE != item_p.getLockedStatus());
    }

    /** get the native stepprocessor info
     * 
     * @return a {@link VWStepProcessorInfo}
     * @throws VWException 
     */
    protected VWStepProcessorInfo getNativeStepProcessorInfo() throws VWException
    {
        return m_queueelement.fetchStepProcessorInfo();
    }

    /** get the queue element
       */
    protected VWQueueElement getQueueElement()
    {
        return m_queueelement;
    }

    protected VWStepElement getStepElement() throws Exception
    {
        return getStepElement(false);
    }

    /** Get the step element.
     * <p>NOTE: Only if necessary, always try to use the queue element!</p>
     * @param fLock_p boolean if step element should be locked
     * @return VWStepElement of current queue element
     */
    protected VWStepElement getStepElement(boolean fLock_p) throws Exception
    {
        if (null == m_stepelement)
        {
            m_stepelement = getQueueElement().fetchStepElement(fLock_p, false);
        }

        return m_stepelement;
    }

    protected VWWorkObject getWorkObject() throws Exception
    {
        if (null == m_workobject)
        {
            m_workobject = getQueueElement().fetchWorkObject(false, false);
        }

        return m_workobject;
    }

    public void returnToSource() throws Exception
    {
        try
        {
            getStepElement().doReturnToSource();

            // signal event for history
            OwEventManager eventManager = getQueue().getRepository().getEventManager();
            if (eventManager != null)
            {
                eventManager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DISPATCH, OwEventManager.HISTORY_STATUS_OK);
                // trigger clear session history event
                eventManager.addEvent(OwEventManager.HISTORY_EVENT_TYPE_CLEAR_SESSION_HISTORY_FOR_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DISPATCH, new OwStandardSessionHistoryDeleteEvent(getDMSID()), OwEventManager.HISTORY_STATUS_OK);
            }
        }
        catch (Exception e)
        {
            throw new OwInvalidOperationException(new OwString("ecmimpl.fncm.OwFNBPMWorkItem.errorreturntosource", "This work item cannot be passed back. Probably it is already on its place of origin."), e);
        }
    }

    /**
     * reassign the workitem.
     * 
     * @param containername_p String 
     * @param delegateFlag_p a boolean value. Specify true if you delegate the assignment and plan to review the completed assignment; otherwise specify false.
     *
     * @throws Exception
     */
    public void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception
    {
        try
        {
            OwPropertyCollection props = new OwStandardPropertyCollection();

            OwProperty prop = getProperty(OwWorkitemContainer.GROUPBOX_PROPERTY_NAME);

            prop.setValue(containername_p);

            props.put(prop.getPropertyClass().getClassName(), prop);

            setProperties(props);
        }
        catch (OwObjectNotFoundException e)
        {
            throw new OwInvalidOperationException(this.getQueue().getContext().localize("fncm.bpm.OwFNBPMQueueWorkItem.errorreassign", "Object cannot be forwarded."), e);
        }

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MOVE, OwEventManager.HISTORY_STATUS_OK);
    }

    /**
     * reassign the workitem.
     * 
     * @param participant_p String ID of user, which can be used to get the additional information
     * @param delegateFlag_p A boolean value. Specify true if you delegate the assignment and plan to review the completed assignment; otherwise specify false.
     *
     * @throws Exception
     */
    public void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception
    {
        /*see Bug 980, use the full qualified name with domain, e.g. user@mydomain.net*/
        OwFNBPM5BaseContainer queue = this.getQueue();
        OwFNCM5Network network = queue.getNetwork();
        OwUserInfo userFromID = network.getUserFromID(participant_p);
        if (null == userFromID)
        {
            throw new OwServerException("Could not find user for ID: " + participant_p);
        }
        String sUserName = userFromID.getUserLongName();
        try
        {
            getStepElement().doReassign(sUserName, delegateFlag_p, null);
            LOG.info("OwFNBPMQueueWorkItem.reassignToUserContainer: participant = " + participant_p + " userName = " + sUserName + " delegate = " + Boolean.toString(delegateFlag_p));
        }
        catch (VWException ex)
        {
            LOG.debug("OwFNBPMQueueWorkItem.reassignToUserContainer: Failed to forward, with user name = " + sUserName, ex);
            /* Can also deliver the Display name, and the login will fail!
             * This exception handling uses the old reassign functionality before version 2.5.2*/
            sUserName = userFromID.getUserName();
            getStepElement().doReassign(sUserName, delegateFlag_p, null);
            LOG.info("OwFNBPMQueueWorkItem.reassignToUserContainer: participant = " + participant_p + " userName = " + sUserName + " delegate = " + Boolean.toString(delegateFlag_p));
        }

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MOVE, OwEventManager.HISTORY_STATUS_OK);
    }

    /** check if you can reassign the work item
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canReassignToPublicContainer(int iContext_p) throws Exception
    {
        return (!getQueue().isUserContainer()) && m_properties.containsKey(OwWorkitemContainer.GROUPBOX_PROPERTY_NAME);
    }

    /** check if you can reassign the work item
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canReassignToUserContainer(int iContext_p) throws Exception
    {
        //    	if ( getQueue().isUserContainer() )
        //    		return false;

        switch (iContext_p)
        {
            case OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS:
                return getStepElement().getCanReassign();

            default:
                return true;
        }
    }

    /** check if you can return the work item to the source from where it has been moved
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canReturnToSource(int iContext_p) throws Exception
    {
        switch (iContext_p)
        {
            case OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS:
                return getStepElement().getCanReturnToSource();

            default:
                return true;
        }
    }

    /** check if you can resubmit the work item to
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canResubmit(int iContext_p) throws Exception
    {
        try
        {
            OwProperty resubmitProp = getProperty(OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME);
            return !resubmitProp.isReadOnly(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        }
        catch (OwObjectNotFoundException e)
        {
            return false;
        }
    }

    /** resubmit the work item to the given date
     * @param date_p the date to resubmit, or null to clear resubmission
     */
    public void resubmit(Date date_p) throws Exception
    {
        // === set the resubmit date property
        try
        {
            OwPropertyCollection properties = new OwStandardPropertyCollection();
            OwProperty resubmitProperty = getProperty(OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME);
            resubmitProperty.setValue(date_p);
            properties.put(resubmitProperty.getPropertyClass().getClassName(), resubmitProperty);

            setProperties(properties);
        }
        catch (OwObjectNotFoundException e)
        {
            LOG.error("No exposed resubmit property defined: " + OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME + " in workitem: " + getName(), e);
            throw new OwConfigurationException(new OwString("ecmimpl.fncm.bpm.resubmissioninvalid", "The work item allows no resubmission. Probably the queue is not configured with this property (%1).") + " "
                    + OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME, e);
        }

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_RESUBMIT, OwEventManager.HISTORY_STATUS_OK);
    }

    /** get the currently set resubmit date
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * 
     * @return Date or null if no resubmission is active 
     */
    public java.util.Date getResubmitDate(int iContext_p) throws Exception
    {
        try
        {
            OwProperty resubmitProperty = getProperty(OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME);

            Date resdate = (Date) resubmitProperty.getValue();

            if ((resdate == null) || (resdate.getTime() <= new Date().getTime()))
            {
                return null;
            }

            return resdate;
        }
        catch (OwObjectNotFoundException e)
        {
            return null;
        }
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return getQueueElement().getLockedUser();
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        if (OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL == iContext_p)
        {
            if (null == getQueueElement().getLockedUserPx())
            {
                return false;
            }
            return (getQueueElement().getLockedUserPx().getUserId() == getQueue().getVWSession().getCurrentUserSecId());
        }
        else
        {
            VWStepElement stepElement = fetchStepElement();
            if (null == stepElement || null == stepElement.getLockedUserPx())
            {
                return false;
            }
            return (stepElement.getLockedUserPx().getUserId() == getQueue().getVWSession().getCurrentUserSecId());
        }
    }

    /**
     * Fetch the current step element.
     * @return - current step element.
     * @throws VWException
     */
    private VWStepElement fetchStepElement() throws VWException
    {
        return getQueueElement().fetchStepElement(false, false);
    }

    /** get the ECM specific ID of the Object. 
       *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
       *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
       *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
       *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
       *
       *  The syntax of the ID is up to the ECM Adapter,
       *  but would usually be made up like the following:
       *
       */
    public String getDMSID() throws Exception
    {
        return OwFNBPM5Repository.getDMSID(getQueueElement().getWorkObjectNumber(), getResourceID());
    }

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public String getID()
    {
        try
        {
            return getQueueElement().getWorkObjectNumber();
        }
        catch (Exception e)
        {
            LOG.error("Could not resolve work item ID of work item: " + getName(), e);
            return null;
        }
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OBJECT_TYPE_WORKITEM;
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitem/item";
    }

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock() throws Exception
    {
        return true;
    }

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        // save the lock state in a global array so we can track all the locked items
        getQueue().saveLocked(this, fLock_p);

        if (fLock_p)
        {
            // === Lock

            if (!getMyLock(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS))
            {
                try
                {
                    if (null == m_stepelement)
                    {
                        m_stepelement = getStepElement(fLock_p);
                    }
                    else
                    {
                        // === Is the stepelement locked ?
                        // lock it, but don't override
                        // if the stepelement was locked by another user, or was moved in other queue, an exception is thrown.
                        m_stepelement.doRefresh(true, false);
                    }
                }
                catch (Exception e)
                {
                    handleCannotLockIssue(e);
                }
            }

            m_fLocked = true;

        }
        else
        {
            // === Unlock
            VWStepElement[] stepElements = new VWStepElement[1];
            stepElements[0] = getStepElement();
            try
            {
                //try to unlock on VWStepElement level
                VWException[] error = VWStepElement.doUnlockMany(stepElements, false, false);
                /* P8 4.x PE-API throws exception only if the
                 * stepElements contains null values or step elements
                 * form different queues. So we must check the returned array of 
                 * VWException's if they are null or exception array is empty.  
                 */
                if (error.length == 0 || error[0] == null)
                {//unlock succeed on VWStepElement level
                    m_fLocked = false;
                }
                else
                {//exception not empty, Element was locked on VWWorkObject level
                    getWorkObject().doSave(Boolean.TRUE.booleanValue());
                    m_fLocked = false;
                }
            }
            catch (Exception e)
            {
                try
                {
                    VWWorkObject[] arr = new VWWorkObject[1];
                    arr[0] = getWorkObject();
                    //P8 3.5 PE-API throws the exception if cannot unlock
                    VWWorkObject.doUnlockMany(arr, Boolean.FALSE.booleanValue(), Boolean.FALSE.booleanValue());
                    m_fLocked = false;
                }
                catch (VWException ex)
                {
                    VWParticipant participant = stepElements[0].getLockedUserPx();
                    if (participant != null)
                    {
                        throw new OwLockDeniedException(new OwString1("owfnbpm.OwFNBPMWorkItem.cannotunlock.lockedbyotheruser", "Cannot unlock the item loked by user (%1).", participant.getDisplayName()), e);
                    }
                    else
                    {
                        throw new OwLockDeniedException(new OwString("owfnbpm.OwFNBPMWorkItem.cannotunlock.unknown", "Cannot perform unlock operation."), e);
                    }
                }
            }
        }

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_LOCK, OwEventManager.HISTORY_STATUS_OK);

        return getLock(OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
    }

    /**
     * Handle the case when the lock cannot be acquired
     * @param exception_p - the exception thrown - can be <code>null</code> 
     * @throws Exception
     */
    private void handleCannotLockIssue(Exception exception_p) throws Exception
    {
        String user = getLockUserID(OwStatusContextDefinitions.STATUS_CONTEXT_CORRECT_STATUS);
        //if the work item was not updates from a several time, and somebody locked the item from another place
        //the getLockUserId return null. The step element may contain the correct info.
        if (user == null)
        {
            VWParticipant participant = fetchStepElement().getLockedUserPx();
            if (participant != null)
            {
                user = participant.getDisplayName();
            }
        }
        if (user != null)
        {
            throw new OwLockDeniedException(new OwString1("owfnbpm.OwFNBPMWorkItem.lockedbyotheruser", "The work item is used by another user (%1).", user), exception_p);
        }
        else
        {
            throw new OwLockDeniedException(new OwString("owfnbpm.OwFNBPMWorkItem.itemalreadylocked", "Work item already locked, please reload your work items list."), exception_p);
        }
    }

    /** get the lock state of the object for ALL users
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return m_fLocked;
    }

    /** get the native object from the ECM system
     *
     *  NOTE: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return Object native to the ECM System
     */
    public Object getNativeObject() throws Exception
    {
        return getStepElement();
    }

    protected String getQueueName() throws Exception
    {
        String strQueueName = getQueueElement().getQueueName();

        // remove parenthesis
        int iParanthesis = strQueueName.lastIndexOf('(');
        if (-1 != iParanthesis)
        {
            return strQueueName.substring(0, iParanthesis);
        }
        else
        {
            return strQueueName;
        }
    }

    /** set a response
     */
    public void setResponse(String strResponse_p) throws Exception
    {
        getStepElement().setSelectedResponse(strResponse_p);
        getStepElement().doSave(false);
    }

    /** get a response
     */
    public String getResponse() throws Exception
    {
        return getStepElement().getSelectedResponse();
    }

    /** check if you can set a response
     */
    public boolean canResponse()
    {
        try
        {
            return (null != getStepElement().getStepResponses());
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** get a list of possible responses
     * @return Collection of OwEnum
     */
    public Collection<OwEnum> getResponses() throws Exception
    {
        java.lang.String[] responses = getStepElement().getStepResponses();

        List<OwEnum> retList = new ArrayList<OwEnum>(responses.length);

        // generate list of OwEnum tuple with display label
        for (int i = 0; i < responses.length; i++)
        {
            retList.add(new OwStandardEnum(responses[i], responses[i]));
        }

        return retList;
    }

    /** delete object and all references, regardless of any state
     */
    public void forcedelete() throws Exception
    {
        getWorkObject().doDelete(true, true);

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DELETE, OwEventManager.HISTORY_STATUS_OK);
    }

    /** check if deletion of object and all references, regardless of any state is possible / allowed
     */
    public boolean canForcedelete(int iContext_p) throws Exception
    {
        return true;
    }

    /** delete object and all references
     */
    public void delete() throws Exception
    {
        getWorkObject().doDelete(true, false);

        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_DELETE, OwEventManager.HISTORY_STATUS_OK);
    }

    /** check if object can be deleted
     * @return true, if delete operation works on object
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        return (!this.getLock(iContext_p));
    }

    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        if (!m_fParametersLoaded)
        {
            loadParameters();
            loadDataFields();
        }
        return super.getProperties(propertyNames_p);
    }

    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        if (!m_fParametersLoaded)
        {
            loadParameters();
            loadDataFields();
        }
        return super.getProperty(strPropertyName_p);
    }

    /**
     * @throws VWException
     * @throws Exception
     */
    private void loadDataFields() throws VWException, Exception
    {
        // set the internal data fields
        VWQueueElement queueElement = getQueueElement();
        VWDataField[] datafields = queueElement.getDataFields();
        String workflowClass = queueElement.getWorkClassName();
        for (int i = 0; i < datafields.length; i++)
        {
            addDatafieldBasedProperty(workflowClass, datafields[i]);
        }
    }
}