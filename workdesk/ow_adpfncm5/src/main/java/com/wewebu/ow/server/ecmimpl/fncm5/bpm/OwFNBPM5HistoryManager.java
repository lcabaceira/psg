package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchSQLOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.history.OwHistoryManagerContext;
import com.wewebu.ow.server.history.OwStandardHistoryManager;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

import filenet.vw.api.VWException;
import filenet.vw.api.VWLog;
import filenet.vw.api.VWLogElement;
import filenet.vw.api.VWLogQuery;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObjectNumber;

/**
 *<p>
 * FileNet BPM History Manager. Reads the FileNet BPM event log. Does not write anything.<br/>
 * A FileNet queue wrapper
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
public class OwFNBPM5HistoryManager extends OwStandardHistoryManager
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5HistoryManager.class);

    public static final String DMS_PREFIX = "fnbpm";

    private OwFNBPM5Repository m_bpmrepository;

    /** list of hidden event types */
    private Set<Integer> m_hiddenEventTypes = new HashSet<Integer>();

    /** get the FileNet BPM VW Session
     * 
     * @return a {@link VWSession}
     * @throws Exception
     */
    public VWSession getVWSession() throws Exception
    {
        return getBpmRepository().getVWSession();
    }

    /** get the BPM Repository
     * 
     * @return an {@link OwFNBPM5Repository}
     * @throws Exception
     */
    protected OwFNBPM5Repository getBpmRepository() throws Exception
    {
        if (null == m_bpmrepository)
        {
            if (!getNetwork().hasInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository"))
            {
                String msg = "OwFNBPMHistoryManager.getBpmRepository: The ECM adapter does not offer an interface for P8 BPM Repository (OwWorkitemRepository).";
                LOG.info(msg);
                throw new OwConfigurationException(getContext().localize("owfnbpm.OwFNBPMDocument.noInterfaceSupport", "The ECM adapter does not offer an interface for P8 BPM Repository (OwWorkitemRepository)."));
            }
            m_bpmrepository = (OwFNBPM5Repository) getNetwork().getInterface("com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository", null);
        }
        return m_bpmrepository;
    }

    /** init the manager, set context
     * @param configNode_p OwXMLUtil node with configuration information
     * @param mainContext_p reference to the main app context of the application 
     * @exception Exception
     */
    public void init(OwHistoryManagerContext mainContext_p, OwXMLUtil configNode_p) throws Exception
    {
        // call super init
        super.init(mainContext_p, configNode_p);

        // read hidden event types
        List hetList = getConfigNode().getSafeStringList("HiddenEventTypes");
        Iterator it = hetList.iterator();
        while (it.hasNext())
        {
            String strHet = (String) it.next();
            try
            {
                int iHet = Integer.parseInt(strHet);
                m_hiddenEventTypes.add(Integer.valueOf(iHet));
            }
            catch (Exception e)
            {
            }
        }
    }

    /** add a new history event to the history database if supported by the historymanager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwHistoryEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws Exception
    {
        // Implement if plugin events need to be audited
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
        // Implement if plugin events need to be audited
    }

    /**
     *<p>
     * Static class for a set of property names that contain user IDs.
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
    public static class OwInternalUserProperties
    {
        private Set m_userproperties = new HashSet();

        /** create the map of properties */
        public OwInternalUserProperties()
        {
            // === map BPM keys to CE names
            m_userproperties.add("F_LockUser");
            m_userproperties.add("F_BoundUser");
            m_userproperties.add("F_Originator");
            m_userproperties.add("F_UserId");

            // NOTE:     Rosters contain:    F_BoundUser and F_Originator
            //           Queues contain:     F_BoundUser which acts as originator
        }
    }

    /** static class for property names  that contain user IDs */
    private static OwInternalUserProperties m_InternalUserProperties = new OwInternalUserProperties();

    /** get the names of properties that contain user IDs 
     * @return Set of property names
     * */
    protected static Set getUserProperties()
    {
        return m_InternalUserProperties.m_userproperties;
    }

    /**
     *<p>
     * Overridden SQL operator to create the special pe substition clauses.
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
    private static class OwFNBPMSearchSQLOperator extends OwSearchSQLOperator
    {
        private List m_substitionVars = new ArrayList();
        private OwFNBPM5HistoryManager m_parentHistoryManager;

        /** create a new operator */
        public OwFNBPMSearchSQLOperator(OwFNBPM5HistoryManager parentHistoryManager_p)
        {
            super(DATE_MODE_FNCM);
            m_parentHistoryManager = parentHistoryManager_p;
        }

        /** get the value of the criteria and convert it to a SQL conform string
         * @param criteria_p the criteria to convert
         * @param iDateMode_p int Date mode used to convert date types as defined with DATE_MODE_...
         *
         * @return String with SQL conform representation of value
         */
        protected String getSQLValueString(OwSearchCriteria criteria_p, Object value_p, int iDateMode_p) throws Exception
        {
            // create placeholder
            String placeHolder = getPlaceHolder(m_substitionVars.size());

            // store var
            if (getUserProperties().contains(criteria_p.getClassName()))
            {
                // user ID property convert from string to ID 
                try
                {
                    m_substitionVars.add(Integer.valueOf(m_parentHistoryManager.getVWSession().convertUserNameToId((String) value_p)));
                }
                catch (ClassCastException e)
                {
                    // === a integer ID not a name
                    m_substitionVars.add(value_p);
                }
                catch (VWException e)
                {
                    throw new OwInvalidOperationException(new OwString1("fncm.bmp.OwFNBPMBaseContainer.usernotfound", "User name (%1) not found.", (String) value_p), e);
                }
            }
            else
            {
                // normal property
                m_substitionVars.add(value_p);
            }

            // return placeholder
            return placeHolder;
        }

        /** create a pe SQL conform placeholder
         */
        public static String getPlaceHolder(int iIndex_p)
        {
            return ":" + String.valueOf(iIndex_p);
        }

        /** get the substition vars used in pe query */
        public List getSubstionVars()
        {
            return m_substitionVars;
        }
    }

    /** check if given event type is a date modified event type
     * 
     * @param type_p
     * @return a <code>boolean</code>
     */
    private boolean isDateModifiedEventType(int type_p)
    {
        switch (type_p)
        {
            case 120:
            case 365:
                return true;

            default:
                return false;
        }
    }

    /** search for entries in the database for a specific ECM object
    *
    * @param sQueryString_p SQL query
    * @param substitutionVars_p array of substitution vars
     * @param object_p 
    * 
    * @return Collection of OwHistoryEntry
    */
    private OwObjectCollection doSearchInternal(String sQueryString_p, Object[] substitutionVars_p, int iMaxSize_p, String resource_p, OwObjectReference object_p) throws Exception
    {
        // Preconditions check
        if (iMaxSize_p <= 0)
        {
            String msg = "Invalid max size in BPM history search query : " + iMaxSize_p;
            LOG.error("OwFNBPMHistoryManager.doSearchInternal():" + msg);
            throw new OwInvalidOperationException(msg);
        }

        // Create return type
        OwObjectCollection ret = getBpmRepository().createObjectCollection(); // new OwStandardObjectCollection();
        String eventLogName = "DefaultEventLog";
        if (object_p != null && object_p instanceof OwWorkitem)
        {
            eventLogName = getVWSession().fetchWorkflowDefinition(-1, ((VWStepElement) ((OwWorkitem) object_p).getNativeObject()).getWorkClassName(), false).getEventLogName();
        }
        // Get FileNet BPM event log
        VWLog log = getVWSession().fetchEventLog(eventLogName);

        // set max size
        log.setBufferSize(iMaxSize_p);

        // Create new query
        VWLogQuery logQuery = log.startQuery("F_LogTime", null, null, 0, sQueryString_p, substitutionVars_p); // Using index "F_LogTime" is important because in this way the results are ordered by date and time

        // Store results of query into an array list so they are more flexible to access
        ArrayList queryResults = new ArrayList();
        while (logQuery.hasNext() && queryResults.size() <= iMaxSize_p)
        {
            queryResults.add(logQuery.next());
        }

        // not every original entry is copied to the output. So we have to count all generated output log entries to
        // fulfill the iMaxSize_p condition
        int iRemainingOutputEntries = iMaxSize_p;

        // Traverse the query results in reversed order and add the history events to the return collection.
        // Reversing the order makes the most recent entry be the first entry in the array list and also the first entry when rendered.
        int i = queryResults.size() - 1;
        while ((iRemainingOutputEntries > 0) && (i >= 0))
        {
            // get current log event
            VWLogElement currentLE = (VWLogElement) queryResults.get(i);
            // get event type of current log event
            int iEventType = currentLE.getEventType();
            // filter out hidden event types
            if (m_hiddenEventTypes.contains(Integer.valueOf(iEventType)))
            {
                // do nothing here
            }
            // Add all history events to the return collection, except the ones of the type "VW_WONameChangedMsg"
            else if (!isDateModifiedEventType(iEventType))
            {
                // add event with empty description
                ret.add(new OwFNBPM5HistoryEntry(this, currentLE, null, null, iEventType));
                iRemainingOutputEntries--;
            }
            // special handling for all "VW_WONameChangedMsg" (120) events:
            //   1.) get the previous history entry
            //   2.) compare all non system fields to the current entry
            //   3.) list all changed fields
            //   4.) if GROUPBOX_PROPERTY_NAME is the only changed field, create a custom redirect log entry
            else
            {
                // var for the list of changed fields
                StringBuffer sChangedFields = new StringBuffer();

                // collection of OwHistoryModifiedPropertyValue
                Collection modprops = new Vector();

                // if GROUPBOX_PROPERTY_NAME has been changed, we generate a special event
                OwFNBPM5HistoryEntry potentialRedirectEvent = null;

                // get ID of previous x event
                int iPreviousNameChangedEntry = i - 1;
                while (iPreviousNameChangedEntry > 0)
                {
                    if (isDateModifiedEventType(((VWLogElement) queryResults.get(iPreviousNameChangedEntry)).getEventType()))
                    {
                        break;
                    }

                    iPreviousNameChangedEntry--;
                }

                // look up previous entry only if current entry is not the last one
                if (iPreviousNameChangedEntry > 0)
                {
                    // get previous log event
                    VWLogElement previousLE = (VWLogElement) queryResults.get(iPreviousNameChangedEntry);

                    // iterate over all field names of currentLE
                    String[] fnames = currentLE.getFieldNames();
                    for (int fni = 0; fni < fnames.length; fni++)
                    {
                        try
                        {
                            String checkField = fnames[fni];
                            boolean bSysField = ((checkField.length() >= 2) && checkField.substring(0, 2).equals("F_"));
                            if (!bSysField)
                            {
                                Object newValue = currentLE.getFieldValue(checkField);
                                Object oldValue = previousLE.getFieldValue(checkField);

                                if (checkField.equals(OwWorkitemContainer.GROUPBOX_PROPERTY_NAME))
                                {
                                    String description = getContext().localize2("fncm.bpm.OwFNBPMHistoryManager.redirectdescription", "Reassign from %1 to %2", oldValue.toString(), newValue.toString());
                                    potentialRedirectEvent = new OwFNBPM5HistoryEntry(this, currentLE, description, null, OwFNBPM5HistoryEntry.CUSTOM_EVENT_TYPE_REASSIGN);
                                }
                                else
                                {
                                    if (!newValue.equals(oldValue))
                                    {
                                        // new modified properties entry
                                        modprops.add(new OwFNBPM5HistoryModifiedPropertyValue(checkField, false, oldValue, newValue, this, resource_p));

                                        if (sChangedFields.length() > 0)
                                        {
                                            sChangedFields.append(", ");
                                        }
                                        sChangedFields.append(checkField);
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }

                // create a special redirect event if GROUPBOX_PROPERTY_NAME has been changed
                if (potentialRedirectEvent != null)
                {
                    ret.add(potentialRedirectEvent);
                    iRemainingOutputEntries--;
                }
                // create log entry with list of changed fields
                if ((potentialRedirectEvent == null) || (sChangedFields.length() > 0))
                {
                    ret.add(new OwFNBPM5HistoryEntry(this, currentLE, sChangedFields.toString(), modprops.toArray(), iEventType));
                    iRemainingOutputEntries--;
                }
            }
            // advance to the next log entry. remember: we go backwards
            i--;
        }

        // return history entries
        return ret;
    }

    /** search for entries in the database
     *
     * @param searchCriteria_p {@link OwSearchNode} to refine the search or null to retrieve all entries
     * @param sortCriteria_p {@link OwSort} to apply
     * @param propertyNames_p {@link Collection} of properties to retrieve with the history entries
     * @param iMaxSize_p max size of entries to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_... or 0 to use default version
     *
     * @return Collection of OwHistoryEntry
     */
    public OwObjectCollection doSearch(OwSearchNode searchCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p) throws Exception
    {
        // create list for substitution vars
        List substitutionVarList = new ArrayList();
        String sqlStatement = "";

        // create statement from filter criteria
        // !!! F_CreateTime <> F_StartTime
        if (null != searchCriteria_p)
        {
            // create query string
            OwFNBPMSearchSQLOperator sqlPropertyOperator = new OwFNBPMSearchSQLOperator(this);
            StringWriter SQLStatement = new StringWriter();
            if (sqlPropertyOperator.createSQLSearchCriteria(searchCriteria_p, SQLStatement))
            {
                sqlStatement = SQLStatement.toString();
                substitutionVarList.addAll(sqlPropertyOperator.getSubstionVars());
            }

            ////////////////////////////////////
            // TODO: Get resource from search
            String resource = null;

            // delegate to internal search function
            return doSearchInternal(sqlStatement, substitutionVarList.toArray(), iMaxSize_p, resource, null);
        }

        // no search criteria. return empty list
        return getBpmRepository().createObjectCollection(); // new OwStandardObjectCollection();
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
    */
    public OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {
        OwObjectCollection ret = super.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        if (null != ret)
        {
            return ret;
        }

        // create list for substitution vars
        List substitutionVarList = new ArrayList();
        String sqlStatement = "";

        // create statement from filter criteria
        // !!! F_CreateTime <> F_StartTime
        if (null != filterCriteria_p)
        {
            // create query string
            OwFNBPMSearchSQLOperator sqlPropertyOperator = new OwFNBPMSearchSQLOperator(this);
            StringWriter SQLStatement = new StringWriter();
            if (sqlPropertyOperator.createSQLSearchCriteria(filterCriteria_p, SQLStatement))
            {
                sqlStatement = SQLStatement.toString();
                substitutionVarList.addAll(sqlPropertyOperator.getSubstionVars());
            }
        }

        // append filter for WorkObjectNumber to SQL statement
        if (sqlStatement.length() > 0)
        {
            sqlStatement = "( " + sqlStatement + " ) and ";
        }
        sqlStatement = sqlStatement + "(F_WobNum = " + OwFNBPMSearchSQLOperator.getPlaceHolder(substitutionVarList.size()) + ")";
        substitutionVarList.add(new VWWorkObjectNumber(object_p.getID()));

        String resource = null;
        try
        {
            resource = object_p.getResourceID();
        }
        catch (OwObjectNotFoundException e)
        {
            // ignore
        }

        // delegate to internal search function
        return doSearchInternal(sqlStatement, substitutionVarList.toArray(), iMaxSize_p, resource, object_p);

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
        try
        {
            return OwFNBPM5HistoryEntry.getStaticObjectClass().getPropertyClass(strFieldDefinitionName_p);
        }
        catch (OwObjectNotFoundException e)
        {
            try
            {
                return getBpmRepository().getFieldDefinition(strFieldDefinitionName_p, strResourceName_p);
            }
            catch (Exception ex)
            {
                return getNetwork().getFieldDefinition(strFieldDefinitionName_p, null);
            }
        }
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
        throw new OwObjectNotFoundException("OwFNBPMHistoryManager.getObjectFromDMSID: Not implemented or Not supported, DMSID = " + strDMSID_p);
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
        throw new OwObjectNotFoundException("OwFNBPMHistoryManager.getObjectFromPath: Not implemented or Not supported.");
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
        throw new OwObjectNotFoundException("OwFNBPMHistoryManager.getObjectClass: Not implemented or Not supported.");
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
        throw new OwNotSupportedException("OwFNBPMHistoryManager.getObjectClassNames: Not implemented.");
    }

    /** get the resource with the specified key
     *
     * @param strID_p String resource ID, if strID_p is null, returns the default resource
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwFNBPMHistoryManager.getResourceID: Resource Id not found, resourceId = " + strID_p);
    }

    /** get a Iterator of available resource IDs
     * 
     * @return Collection of resource IDs used in getResource, or null if no resources are available
     */
    public java.util.Iterator getResourceIDs() throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMHistoryManager.getResourceIDs: Not implemented.");
    }

    /** get the instance of the history manager */
    public OwEventManager getEventManager()
    {
        return this;
    }

    /** force the network adapter to reload all the static class description data. */
    public void refreshStaticClassdescriptions() throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMHistoryManager.refreshStaticClassdescriptions: Not implemented.");
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

    /**
     *<p>
     * Like wild card definitions flyweight.
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
    private static class OwLikeWildCardDefinitions
    {
        private Vector m_definitions;

        public OwLikeWildCardDefinitions(OwRepositoryContext context_p)
        {
            m_definitions = new Vector();

            m_definitions.add(new OwStandardWildCardDefinition("%", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1(
                    "ecmimpl.OwFNBPMHistoryManager.WILD_CARD_TYPE_MULTI_CHAR", "(%1) ersetzt mehrere beliebige Zeichen.")));
            m_definitions.add(new OwStandardWildCardDefinition("_", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR, new OwString1(
                    "ecmimpl.OwFNBPMHistoryManager.WILD_CARD_TYPE_SINGLE_CHAR", "(%1) ersetzt ein beliebiges Zeichen.")));
        }

        public Collection getDefinitions()
        {
            return m_definitions;
        }
    }

    /** like wild card definitions flyweight */
    private OwLikeWildCardDefinitions m_likewildcarddefinitions;

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
        switch (iOp_p)
        {
            case OwSearchOperator.CRIT_OP_NOT_LIKE:
            case OwSearchOperator.CRIT_OP_LIKE:
            {
                if (m_likewildcarddefinitions == null)
                {
                    m_likewildcarddefinitions = new OwLikeWildCardDefinitions(getContext());
                }

                return m_likewildcarddefinitions.getDefinitions();
            }

            default:
                return null;
        }
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
