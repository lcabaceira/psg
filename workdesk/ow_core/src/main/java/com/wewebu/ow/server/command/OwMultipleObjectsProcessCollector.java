package com.wewebu.ow.server.command;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.wewebu.ow.server.ecm.OwObject;

/**
 *<p>
 * This is an utility class which holds the elements affected by a multiple objects operation.
 * This class holds the names of correct processed objects, their DMSIDs, the names of objects 
 * who fail to be processed, and the localized error messages in case of failure.
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
public class OwMultipleObjectsProcessCollector
{

    /**
     * Holds the list of reason why the objects cannot be processed.
     */
    private List m_failToProcessMessages;

    /**Holds collect data for objects that were deliberately not processed */
    private List m_unprocessedObjectsData;

    /**Holds collect data for objects that were successfully processed */
    private List m_processedObjectsData;

    /**Holds collect data for objects that have failed to be processed */
    private List m_failedObjectsData;

    /**
     *Object data caching class. 
     *Object data needs to be cached before object processing as data to be collected 
     *could be affected by the object's processing sequence.
     *The {@link OwMultipleObjectsProcessCollector} class creates ready to use 
     *collect via   the {@link OwMultipleObjectsProcessCollector#createCollectData(OwObject)}
     *method. This collect data can be collected later (egg. after processing or in a processing 
     *failure handling sequence).  
     */
    public static class OwObjectCollectData
    {
        /**The object the collect data is based on */
        private OwObject m_object;
        /**An object DMSID cache*/
        private String m_objectDMSIDCache;
        /**An object name cahe*/
        private String m_objectNameCache;
        /**The reason why this object was not processed*/
        private Exception failureReason;

        /**
         * Constructor
         * @param object_p The object the collect data is based on
         * @throws Exception if object data caching fails
         */
        public OwObjectCollectData(OwObject object_p) throws Exception
        {
            super();
            this.m_object = object_p;
            this.m_objectDMSIDCache = object_p.getDMSID();
            this.m_objectNameCache = object_p.getName();
        }

        /**
         * 
         * @return The object the collect data is based on
         */
        public OwObject getObject()
        {
            return m_object;
        }

        /**
         * 
         * @return The cached object DMSID
         */
        public String getCachedObjectDMSID()
        {
            return m_objectDMSIDCache;
        }

        /**
         * 
         * @return The cached object name 
         */
        public String getCachedObjectName()
        {
            return m_objectNameCache;
        }

        /**
         * Set the reason why this object cannot be processed.
         * @param failureReason_p - the {@link Exception} that cause process failure for this object. 
         * @since 3.0.0.0
         */
        public void setFailureReason(Exception failureReason_p)
        {
            this.failureReason = failureReason_p;
        }

        /**
         * Get the reason why this object cannot be processed.
         * @return the {@link Exception} that cause process failure for this object. Can be <code>null</code>.
         * @since 3.0.0.0
         */
        public Exception getFailureReason()
        {
            return failureReason;
        }

    }

    /**
     * Constructor.
     */
    public OwMultipleObjectsProcessCollector()
    {

        this.m_failToProcessMessages = new LinkedList();
        //---
        this.m_unprocessedObjectsData = new LinkedList();
        this.m_processedObjectsData = new LinkedList();
        this.m_failedObjectsData = new LinkedList();
    }

    public OwObjectCollectData createCollectData(OwObject object_p) throws Exception
    {
        return new OwObjectCollectData(object_p);
    }

    public void addUnprocessedObjectData(OwObjectCollectData collectData_p)
    {
        m_unprocessedObjectsData.add(collectData_p);
    }

    public void addProcessedObjectData(OwObjectCollectData collectData_p)
    {
        m_processedObjectsData.add(collectData_p);
    }

    public void addFailedObjectData(OwObjectCollectData collectData_p)
    {
        m_failedObjectsData.add(collectData_p);
    }

    /**
     * Clear all lists for this instance.
     */
    public void clear()
    {
        m_failToProcessMessages.clear();
        //--
        m_processedObjectsData.clear();
        m_unprocessedObjectsData.clear();
        m_failedObjectsData.clear();
    }

    /**
     * Add the reason why an object cannot be deleted.
     * @param localizedMessage_p - the message.
     */
    public void addProcessedObjectFailureMessages(String localizedMessage_p)
    {
        m_failToProcessMessages.add(localizedMessage_p);
    }

    /**
     * Get the reason why the objects cannot be processed.
     * @return - the error messages.
     */
    public String getFailToProcessErrorMessages()
    {
        return getMessages(m_failToProcessMessages);
    }

    /**
     * Check if some of the objects were successfully processed.
     * @return -<code>true</code> - if some of the objects were successfully deleted.
     */
    public boolean hasProcessedObjects()
    {
        return !m_processedObjectsData.isEmpty();
    }

    /**
     * Check if the process operation has errors
     * @return - <code>true</code> - delete operation has errors.
     */
    public boolean hasProcessErrors()
    {
        return !m_failedObjectsData.isEmpty();
    }

    /**
     * Get the list of processed DMSIDs. 
     * @return - the list of DMSIDs.
     */
    public Set getProcessedDmsIds()
    {
        Set processedDMSIDS = new HashSet();
        for (Iterator i = m_processedObjectsData.iterator(); i.hasNext();)
        {
            OwObjectCollectData collectData = (OwObjectCollectData) i.next();
            processedDMSIDS.add(collectData.getCachedObjectDMSID());
        }
        return processedDMSIDS;
    }

    /**
     * Get the names of processed objects.
     * @return - the list with object names.
     */
    public List getProcessedNames()
    {
        List processedNames = new LinkedList();
        for (Iterator i = m_processedObjectsData.iterator(); i.hasNext();)
        {
            OwObjectCollectData collectData = (OwObjectCollectData) i.next();
            processedNames.add(collectData.getCachedObjectName());
        }
        return processedNames;
    }

    /**
     * create a list with strings representing the names of owobject
     * @param collectDataList_p
     * @return <code>java.util.List</code> the list with object names.
     */
    private List createListOfObjectNames(List collectDataList_p)
    {
        List result = new LinkedList();
        for (Iterator iterator = collectDataList_p.iterator(); iterator.hasNext();)
        {
            OwObjectCollectData collectData = (OwObjectCollectData) iterator.next();
            result.add(collectData.getCachedObjectName());

        }
        return result;
    }

    /**
     * create a list with objects from object collect datas
     * @param collectDataList_p
     * @return <code>java.util.List</code> the list with object names.
     */
    private List createListOfObjects(List collectDataList_p)
    {
        List result = new LinkedList();
        for (Iterator iterator = collectDataList_p.iterator(); iterator.hasNext();)
        {
            OwObjectCollectData collectData = (OwObjectCollectData) iterator.next();
            result.add(collectData.getObject());

        }
        return result;
    }

    /**
     * Get the list with successfully processed objects
     * @return <code>java.util.List</code> the list of objects.
     */
    public List getProcessedObjects()
    {
        List processedObjects = new LinkedList();
        for (Iterator i = m_processedObjectsData.iterator(); i.hasNext();)
        {
            OwObjectCollectData collectData = (OwObjectCollectData) i.next();
            processedObjects.add(collectData.getObject());
        }
        return processedObjects;
    }

    /**
     * Get the list with names of objects that fail to be processed.
     * @return - the list with names.
     */
    public List getFailToProcessNames()
    {
        return createListOfObjectNames(m_failedObjectsData);
    }

    /**
     * Creates a string from a list of strings.
     * @param objectsMessages_p the list of strings
     * @return a created strings.
     */
    private String getMessages(List objectsMessages_p)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < objectsMessages_p.size(); i++)
        {
            buf.append(objectsMessages_p.get(i));
            if (i < objectsMessages_p.size() - 1)
            {
                buf.append(", ");
            }
        }
        return buf.toString();
    }

    /**
     * Check if the process operation has errors.
     * @return - <code>true</code> if the process operation has errors.
     */
    public boolean hasErrors()
    {
        return hasProcessErrors();
    }

    /**
     * Get the list with all object names that fail to be processed.
     * @return the list with object names.
     */
    public List getAllErrorNames()
    {
        List result = getFailToProcessNames();
        return result;
    }

    /**
     * Get the list with all objects that fail to be processed.
     * @return the list with objects.
     */
    public List getAllErrorObjects()
    {
        return createListOfObjects(m_failedObjectsData);
    }

    /**
     * Get all error messages as a unified string.
     * @return - the error messages.
     */
    public String getAllErrorMessages()
    {
        List result = m_failToProcessMessages;
        return getMessages(result);
    }

    /**
     * The list with disabled objects.
     * @return disabled object list.
     */
    public List getDisabledObjects()
    {
        return createListOfObjects(m_unprocessedObjectsData);
    }

    /**
     * The list of disabled objects names.
     * @return list
     */
    public List getDisabledObjectNames()
    {
        return createListOfObjectNames(m_unprocessedObjectsData);
    }

    /**
     * Returns the list of {@link OwObjectCollectData} objects that are not processed.
     * @return - a {@link List} of {@link OwObjectCollectData} objects that are not processed.
     * @since 3.0.0.0
     */
    public List getFailedObjectsData()
    {
        return m_failedObjectsData;
    }
}