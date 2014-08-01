package com.wewebu.ow.server.ecmimpl.owdummy;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwFileObject;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecmimpl.owdummy.log.OwLog;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.history.OwHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryManager;
import com.wewebu.ow.server.util.OwEscapedStringTokenizer;

/**
 *<p>
 * Dummy implementation of the history manager interface to simulate history.
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
public class OwDummyHistoryManager extends OwStandardHistoryManager
{
    /** package logger for the class */
    public static final Logger LOG = OwLog.getLogger(OwDummyHistoryManager.class);

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "owdmhist";

    /** create a dummy entry
     * 
     * @param sID_p
     * @param sName_p
     * @return the newly created {@link OwHistoryEntry} 
     * @throws Exception 
     */
    private OwHistoryEntry createDummyEntry(String sID_p, String sName_p) throws Exception
    {
        return new OwDummyHistoryEntry(getContext(), new Date(), sID_p, OwEventManager.HISTORY_EVENT_TYPE_GENERIC, OwEventManager.HISTORY_STATUS_OK, "Summary for " + sName_p, getContext().getCurrentUser().getUserID());
    }

    /** create a dummy entry with modified properties
     * 
     * @param sID_p
     * @param sName_p
     * @return the newly created {@link OwHistoryEntry}
     * @throws Exception 
     */
    private OwHistoryEntry createDummyPropertyEntry(String sID_p, String sName_p) throws Exception
    {
        OwObject object = getNetwork().getObjectFromPath("/Adresse.owc", false);

        Collection propertycardinalitiesandnames = new Vector();

        propertycardinalitiesandnames.add(String.valueOf(OwStandardHistoryEntry.CARDINALITY_SCALAR));
        propertycardinalitiesandnames.add(OwFileObject.OwFileObjectClass.NAME_PROPERTY);

        propertycardinalitiesandnames.add(String.valueOf(OwStandardHistoryEntry.CARDINALITY_SCALAR));
        propertycardinalitiesandnames.add(OwFileObject.OwFileObjectClass.LAST_MODIFIED_PROPERTY);

        propertycardinalitiesandnames.add(String.valueOf(OwStandardHistoryEntry.CARDINALITY_SCALAR));
        propertycardinalitiesandnames.add(OwDummyFileObject.OwDummyFileObjectClass.MALE_PROPERTY);

        propertycardinalitiesandnames.add(String.valueOf(OwStandardHistoryEntry.CARDINALITY_SCALAR));
        propertycardinalitiesandnames.add(OwDummyFileObject.OwDummyFileObjectClass.STATUS_PROPERTY);

        propertycardinalitiesandnames.add(String.valueOf(OwStandardHistoryEntry.CARDINALITY_ARRAY));
        propertycardinalitiesandnames.add(OwDummyFileObject.OwDummyFileObjectClass.NOTES_PROPERTY);

        // old props
        Collection oldProperties = new Vector();

        // name
        oldProperties.add("Alter Name");

        // last modified
        oldProperties.add("2006-02-12T00:00:00.0Z+0200");

        // male
        oldProperties.add("true");

        // status
        oldProperties.add("true");

        // notes
        Collection oldNoteValue = new Vector();
        oldNoteValue.add("Note 1");
        oldNoteValue.add("Note 2");
        oldProperties.add(OwEscapedStringTokenizer.createDelimitedString(oldNoteValue));

        // new props
        Collection newProperties = new Vector();

        // name
        newProperties.add("Neuer Name");

        // last modified
        newProperties.add("2008-07-12T00:10:00.0Z+0200");

        // male
        newProperties.add("false");

        // status
        newProperties.add("false");

        // notes
        Collection newNoteValue = new Vector();
        newNoteValue.add("Note A");
        newNoteValue.add("Note B");
        newNoteValue.add("Note C");
        newProperties.add(OwEscapedStringTokenizer.createDelimitedString(newNoteValue));

        return new OwDummyHistoryEntry(getContext(), new Date(), sID_p, OwEventManager.HISTORY_EVENT_TYPE_GENERIC, OwEventManager.HISTORY_STATUS_OK, "Summary for " + sName_p, getContext().getCurrentUser().getUserID(), object, this, null,
                propertycardinalitiesandnames, oldProperties, newProperties);
    }

    /** create a dummy entry with modified objects
     * 
     * @param sID_p
     * @param sName_p
     * @return the newly created {@link OwHistoryEntry}
     * @throws Exception 
     */
    private OwHistoryEntry createDummyParentEntry(String sID_p, String sName_p) throws Exception
    {
        OwObject parent = getNetwork().getObjectFromPath("/akte1/Vertrag_1/Fall 1", false);

        Collection objects = parent.getChilds(new int[] { OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS }, null, new OwSort(), 100, 0, null);

        return new OwDummyHistoryEntry(getContext(), new Date(), sID_p, OwEventManager.HISTORY_EVENT_TYPE_GENERIC, OwEventManager.HISTORY_STATUS_OK, "Summary for " + sName_p, getContext().getCurrentUser().getUserID(), parent, objects);
    }

    public OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {
        OwObjectCollection ret = super.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        if (null != ret)
        {
            return ret;
        }
        ret = new OwStandardObjectCollection();
        // just return dummy entries one for each Event ID
        Iterator it = getEventIDs().iterator();
        while (it.hasNext())
        {
            String sID = (String) it.next();

            if (sID.equals(OwEventManager.HISTORY_EVENT_ID_OBJECT_REMOVE_REF))
            {
                ret.add(createDummyParentEntry(sID, "(" + object_p.getName() + ")"));
            }
            else if (sID.equals(OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PROPERTIES))
            {
                ret.add(createDummyPropertyEntry(sID, "(" + object_p.getName() + ")"));
            }
            else
            {
                ret.add(createDummyEntry(sID, "(" + object_p.getName() + ")"));
            }
        }

        return ret;
    }

    public void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws Exception
    {
        LOG.info("OwDummyHistoryManager.addEvent1: iEventType_p: " + String.valueOf(iEventType_p) + ", strEventID_p: " + strEventID_p + ", event_p.getSummary(): " + event_p.getSummary() + ", iStatus_p: " + iStatus_p);
        super.addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
    }

    public void addEvent(int iEventType_p, String strEventID_p, int iStatus_p) throws Exception
    {
        LOG.info("OwDummyHistoryManager.addEvent2: iEventType_p: " + String.valueOf(iEventType_p) + ", strEventID_p: " + strEventID_p + ", iStatus_p: " + iStatus_p);
    }

    public boolean canRefreshStaticClassdescriptions() throws Exception
    {
        return false;
    }

    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        OwObjectCollection ret = new OwStandardObjectCollection();

        for (int i = 0; i < 10; i++)
        {
            ret.add(createDummyEntry(String.valueOf(i), "doSearch: " + String.valueOf(i)));
        }

        return ret;
    }

    public String getDMSPrefix()
    {
        return DMS_PREFIX;
    }

    public OwEventManager getEventManager()
    {
        return this;
    }

    public OwObjectClass getObjectClass(String strClassName_p, OwResource resource_p) throws Exception
    {
        return OwStandardHistoryEntry.getStaticObjectClass();
    }

    public Map getObjectClassNames(int[] iTypes_p, boolean fExcludeHiddenAndNonInstantiable_p, boolean fRootOnly_p, OwResource resource_p) throws Exception
    {
        Map ret = new HashMap();

        ret.put(OwStandardHistoryEntry.getStaticObjectClass().getClassName(), OwStandardHistoryEntry.getStaticObjectClass().getDisplayName(getContext().getLocale()));

        return ret;
    }

    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        return createDummyEntry(strDMSID_p, "getObjectFromDMSID");
    }

    public OwObject getObjectFromPath(String strPath_p, boolean fRefresh_p) throws Exception
    {
        return createDummyEntry(strPath_p, "getObjectFromPath");
    }

    public OwResource getResource(String strID_p) throws Exception
    {
        return null;
    }

    public Iterator getResourceIDs() throws Exception
    {
        return null;
    }

    public void refreshStaticClassdescriptions() throws Exception
    {
    }

    public void releaseResources() throws Exception
    {
    }

    public OwFieldDefinition getFieldDefinition(String strFieldDefinitionName_p, String strResourceName_p) throws Exception, OwObjectNotFoundException
    {
        try
        {
            return OwStandardHistoryEntry.getStaticObjectClass().getPropertyClass(strFieldDefinitionName_p);
        }
        catch (OwObjectNotFoundException e)
        {
            return getNetwork().getFieldDefinition(strFieldDefinitionName_p, strResourceName_p);
        }
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
        return this.getNetwork().getWildCardDefinitions(strFieldDefinitionName_p, strResourceName_p, iOp_p);
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
        throw new OwInvalidOperationException("Can not batch...");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("Can not batch...");
    }
}