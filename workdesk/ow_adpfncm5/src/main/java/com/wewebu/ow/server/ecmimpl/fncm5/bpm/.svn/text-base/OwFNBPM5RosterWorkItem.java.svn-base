package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;

import filenet.vw.api.VWDataField;
import filenet.vw.api.VWException;
import filenet.vw.api.VWFieldType;
import filenet.vw.api.VWStepProcessorInfo;
import filenet.vw.api.VWWorkObject;

/**
 *<p>
 * FileNet BPM Plugin.<br/>
 * A single workitem.
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
public class OwFNBPM5RosterWorkItem extends OwFNBPM5WorkItem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5RosterWorkItem.class);

    /** reference to the wrapped workobject */
    private VWWorkObject m_workobject;
    /** flag for lazy properties loaded */
    protected boolean propsLoaded;

    /** Creates a new instance of OwFNBPMRosterWorkItem based upon a VWWorkObject */
    public OwFNBPM5RosterWorkItem(OwFNBPM5BaseContainer queue_p, VWWorkObject item_p) throws Exception
    {
        super(queue_p);
        m_workobject = item_p;
        propsLoaded = false;
    }

    /**
     * Method for lazy initialization of properties 
     * @throws Exception
     */
    protected void loadProperties() throws Exception
    {
        if (!propsLoaded)
        {
            // === init the system defined values
            // set the internal data fields
            VWDataField[] datafields = getWorkObject().getDataFields(VWFieldType.ALL_FIELD_TYPES, VWWorkObject.FIELD_USER_AND_SYSTEM_DEFINED);
            String workflowClass = getWorkObject().getWorkClassName();
            for (int i = 0; i < datafields.length; i++)
            {
                addDatafieldBasedProperty(workflowClass, datafields[i]);
            }
            propsLoaded = true;
        }
    }

    /** get the native stepprocessor info
     * 
     * @return a {@link VWStepProcessorInfo}
     * @throws VWException 
     */
    protected VWStepProcessorInfo getNativeStepProcessorInfo() throws VWException
    {
        return m_workobject.fetchStepProcessorInfo();
    }

    /** get the workobject
       * NOTE: Only if necessary, always try to use the queue or roster element
       */
    protected VWWorkObject getWorkObject() throws Exception
    {
        return m_workobject;
    }

    /** return the work item to the source from where it has been moved
     */
    public void returnToSource() throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.returnToSource: Can not resubmit roster element: " + getName());
    }

    /**
     * reassign the workitem.
     * 
     * @param containername_p <code>String</code> 
     * @param delegateFlag_p A boolean value. Specify true if you delegate the assignment and plan to review the completed assignment; otherwise specify false.
     *
     * @throws Exception
     */
    public void reassignToPublicContainer(String containername_p, boolean delegateFlag_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.reassignToPublicContainer: Can not reaassign roster element: " + getName());
    }

    /**
     * reassign the workitem.
     * 
     * @param participant_p <code>String</code> 
     * @param delegateFlag_p A boolean value. Specify true if you delegate the assignment and plan to review the completed assignment; otherwise specify false.
     *
     * @throws Exception
     */
    public void reassignToUserContainer(String participant_p, boolean delegateFlag_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.reassignToUserContainer: Can not reaassign roster element: " + getName());
    }

    /** check if you can reassign the work item
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canReassignToPublicContainer(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if you can reassign the work item
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canReassignToUserContainer(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if you can return the work item to the source from where it has been moved
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canReturnToSource(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if you can resubmit the work item to
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canResubmit(int iContext_p) throws Exception
    {
        return false;
    }

    /** resubmit the work item to the given date
     * @param date_p the date to resubmit, or null to clear resubmission
     */
    public void resubmit(Date date_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.resubmit: Can not resubmit roster element = " + getName());
    }

    /** get the currently set resubmit date
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * 
     * @return Date or null if no resubmission is active 
     */
    public java.util.Date getResubmitDate(int iContext_p) throws Exception
    {
        return null;
    }

    /** dispatch the workitem, move it to the next public queue
     */
    public void dispatch() throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.dispatch: Can not dispatch roster element = " + getName());
    }

    /** check if you can dispatch the workitem, move it to the next public queue
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     */
    public boolean canDispatch(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
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
        return OwFNBPM5Repository.getDMSID(getWorkObject().getWorkObjectNumber(), getResourceID());
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
            return getWorkObject().getWorkObjectNumber();
        }
        catch (Exception e)
        {
            LOG.error("Could not resolve work item ID of work item: " + getName(), e);
            return null;
        }
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return OBJECT_TYPE_ROSTERITEM;
    }

    public String getMIMEType() throws Exception
    {
        return "ow_workitem/roster";
    }

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock() throws Exception
    {
        return false;
    }

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.setLock: Cannot lock roster element = " + getName());
    }

    /** get the lock state of the object for ALL users
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        loadProperties();
        return m_properties;
    }

    /** retrieve the specified property from the object.
     * NOTE: if the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     *
     * @return a property object
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        loadProperties();
        OwProperty prop = (OwProperty) m_properties.get(strPropertyName_p);
        if (null == prop)
        {
            String msg = "OwFNBPMRosterWorkItem.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return prop;
    }

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.setProperties: Can not setProperties in roster element = " + getName());
    }

    /** refresh the property cache  */
    public void refreshProperties() throws Exception
    {
        propsLoaded = false;
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
        return getWorkObject();
    }

    /** get the queue name
     */
    protected String getQueueName() throws Exception
    {
        StringBuilder strQueueName = new StringBuilder(getWorkObject().getCurrentQueueName());
        // remove parenthesis
        int iParanthesis = strQueueName.lastIndexOf("(");
        if (-1 != iParanthesis)
        {
            strQueueName = strQueueName.deleteCharAt(iParanthesis);
        }

        // now add the bound user
        String strBoundUser = getProperty("F_BoundUser").getValue().toString();
        if ((strBoundUser.length() > 0) && (!strBoundUser.equalsIgnoreCase("(NONE)")))
        {
            strQueueName.append("(").append(strBoundUser).append(")");
        }

        return strQueueName.toString();
    }

    /** set a response
     */
    public void setResponse(String strResponse_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.setResponse: Not implemented.");
    }

    /** get a response
     */
    public String getResponse() throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.getResponse: Not implemented.");
    }

    /** check if you can set a response
     */
    public boolean canResponse()
    {
        return false;
    }

    /** get a list of possible responses
     * @return Collection of OwEnum
     */
    public Collection getResponses() throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMRosterWorkItem.getResponses: Not implemented.");
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
}