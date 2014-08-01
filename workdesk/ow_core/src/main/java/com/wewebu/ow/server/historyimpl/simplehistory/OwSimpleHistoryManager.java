package com.wewebu.ow.server.historyimpl.simplehistory;

import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.history.OwStandardHistoryManager;

/**
 *<p>
 * Simple implementation for the history manager, does not create a history.<br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwSimpleHistoryManager extends OwStandardHistoryManager
{
    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "owsh";

    /** add a new history event to the history database if supported by the historymanager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwHistoryEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws Exception
    {
        super.addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
    }

    /** add a new history event to the history database if supported by the historymanager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, int iStatus_p) throws Exception
    {
        // Ignore
    }

    /** search for entries in the database
     *
     * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
     * @param sortCriteria_p OwSort to apply
     * @param propertyNames_p Collection of properties to retrieve with the history entries
     * @param iMaxSize_p max size of entries to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     *
     * @return Collection of OwHistoryEntry
     */
    public OwObjectCollection doSearch(OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        return new OwStandardObjectCollection();
    }

    /** search for entries in the database for a specific ECM object
    *
    * @param object_p OwObject to find entries for
    * @param filterCriteria_p OwSearchNode to refine the search or null to retrieve all entries
    * @param sortCriteria_p OwSort to apply
    * @param propertyNames_p Collection of properties to retrieve with the history entries
    * @param includeSubObjectTypes_p array of child OwObject types to be included in history, or null
    * @param iMaxSize_p max size of entries to retrieve
    * 
    * @return Collection of OwHistoryEntry
    * 
    **/
    public OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {
        OwObjectCollection ret = super.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        if (null != ret)
        {
            return ret;
        }
        ret = new OwStandardObjectCollection();
        return ret;
    }

    /** get a field definition for the given name and resource
     *
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null 
     *
     * @return OwFieldDefinition or throws OwObjectNotFoundException
     */
    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        throw new OwObjectNotFoundException("OwSimpleHistoryManager.getField: Not implemented or Not supported.");
    }

    /** reconstructs an Object from ECM Id, see OwObject.getDMSID for details.
     *
     * @param strDMSID_p ECM ID for the requested object
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return an Object Instance
     *
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwSimpleHistoryManager.getObjectFromDMSID: Not implemented or Not supported, DMSID = " + strDMSID_p);
    }

    /** get object from given path
     *
     * @param strPath_p path to the object starting with "/..."
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @return OwObject 
     */
    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwSimpleHistoryManager.getObjectFromPath: Not implemented or Not supported.");
    }

    /** get a Property class description of the available object class descriptions 
     *
     * @param strClassName_p Name of class
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return OwObjectClass instance
     */
    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwSimpleHistoryManager.getObjectClass: Not implemented or Not supported.");
    }

    /** get a list of the available object class descriptions names
     *
     * @param iTypes_p int array of Object types as defined in OwObject, of null to retrieve all class names
     * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
     * @param fRootOnly_p true = gets only the root classes if we deal with a class tree, false = gets all classes
     * @param resource_p OwResource to retrieve the objects from, or null to use the default resource
     *
     * @return string array of OwObjectClass Names
     */
    public java.util.Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        throw new OwNotSupportedException("OwSimpleHistoryManager.getObjectClassNames: Not implemented.");
    }

    /** get the resource with the specified key
     *
     * @param strID_p String resource ID, if strID_p is null, returns the default resource
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwSimpleHistoryManager.getResourceID: Resource Id not found, resourceId = " + strID_p);
    }

    /** get a Iterator of available resource IDs
     * 
     * @return Collection of resource IDs used in getResource, or null if no resources are available
     */
    public java.util.Iterator getResourceIDs() throws Exception
    {
        throw new OwNotSupportedException("OwSimpleHistoryManager.getResourceIDs: Not implemented.");
    }

    /** get the instance of the history manager */
    public OwEventManager getEventManager()
    {
        return this;
    }

    /** force the network adapter to reload all the static class description data. */
    public void refreshStaticClassdescriptions() throws Exception
    {
        // ignore
    }

    /** check if reload of all the static class description data is supported / necessary. 
     *
     * @return boolean true = refresh is supported and should be done, false = refresh is not supported and not necessary.
     */
    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return false;
    }

    /** get a prefix which is used to distinguish the DMSID of objects from the repository */
    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    /** releases all resources that have been used during this session
     */
    public void releaseResources() throws Exception
    {
        // no resources to release
    }

    /** get a collection of wild card definitions that are allowed for the given field, resource and search operator
     * 
     * @param strFieldDefinitionName_p Name of the field definition class
     * @param strResourceName_p optional name of the resource if there are several different resources for field definitions, can be null
     * @param iOp_p search operator as defined in OwSearchOperator CRIT_OP_...
     * 
     * @return Collection of OwWildCardDefinition, or null if no wildcards are defined
     * @throws Exception
     */
    public java.util.Collection getWildCardDefinitions(String strFieldDefinitionName_p, String strResourceName_p, int iOp_p) throws Exception
    {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#canBatch()
     */
    public boolean canBatch()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#closeBatch(com.wewebu.ow.server.ecm.OwBatch)
     */
    public void closeBatch(OwBatch batch_p) throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("can not batch");
    }
}