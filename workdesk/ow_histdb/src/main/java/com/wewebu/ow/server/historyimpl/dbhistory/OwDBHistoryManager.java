package com.wewebu.ow.server.historyimpl.dbhistory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.wewebu.ow.server.ecm.OwBatch;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwRepositoryContext;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectCollection;
import com.wewebu.ow.server.ecm.OwStandardObjectReference;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.event.OwEvent;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchOperator;
import com.wewebu.ow.server.field.OwSearchSQLPreparedStatementOperator;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.field.OwStandardWildCardDefinition;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.history.OwHistoryException;
import com.wewebu.ow.server.history.OwHistoryManagerContext;
import com.wewebu.ow.server.history.OwHistoryObjectChangeEvent;
import com.wewebu.ow.server.history.OwHistoryPropertyChangeEvent;
import com.wewebu.ow.server.history.OwStandardHistoryEntry;
import com.wewebu.ow.server.history.OwStandardHistoryManager;
import com.wewebu.ow.server.historyimpl.dbhistory.log.OwLog;
import com.wewebu.ow.server.util.OwEscapedStringTokenizer;
import com.wewebu.ow.server.util.OwString1;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * Implements the history manager for an arbitrary SQL Database.
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
public class OwDBHistoryManager extends OwStandardHistoryManager
{
    /** the name of the element defining the table name in history manager configuration*/
    private static final String DB_HISTORY_MANAGER_TABLE_NAME = "DbTableName";

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwDBHistoryManager.class);

    /** DMS prefix to identify this adapter */
    public static final String DMS_PREFIX = "owdh";

    /** the database to read and write to */
    private JdbcTemplate m_jdbcTemplate;

    /** filter of Events that should be tracked */
    private OwEventFilter m_filter;

    /** table name */
    protected static String HISTORY_TABLE_NAME = "OW_HISTORY";

    /** summary field name */
    protected static final String OW_HIST_SUMMARY = "OW_HIST_SUMMARY";

    /** comma separated list of DMSIDs of event objects TYPE: String */
    protected static final String DB_PROPRTY_OBJECT_DMSID = "ObjectDMSID";

    /** ref info of event object, contains serialized information to create a OwObjectReference TYPE: String */
    protected static final String DB_PROPRTY_OBJECT_REF_INFO = "ObjectName"; // new usage since 1.6.1 !

    /** DMSID of event object parent TYPE: String */
    protected static final String DB_PROPRTY_PARENT_OBJECT_DMSID = "ParentDMSID";

    /** ref info of event object parent, contains serialized information to create a OwObjectReference TYPE: String */
    protected static final String DB_PROPRTY_PARENT_OBJECT_REF_INFO = "ParentName"; // new usage since 1.6.1 !

    /** a comma separated list of modified property names TYPE: String */
    protected static final String DB_PROPRTY_MODIFIED_PROPERTIES = "Custom1"; // new column !

    /** a comma separated list of old property values in the same order as DB_PROPRTY_MODIFIED_PROPERTIES TYPE: String */
    protected static final String DB_PROPRTY_OLD_PROPERTY_VALUES = "Custom2"; // new column !

    /** a comma separated list of new property values in the same order as DB_PROPRTY_MODIFIED_PROPERTIES TYPE: String */
    protected static final String DB_PROPRTY_NEW_PROPERTY_VALUES = "Custom3"; // new column !

    /** like wild card definitions flyweight */
    private OwLikeWildCardDefinitions m_likewildcarddefinitions;

    /** table summary column length */
    private int m_summaryColumnLength;

    /**
     * init the DataSource of the db history manager
     * @see com.wewebu.ow.server.history.OwHistoryManager#init(OwHistoryManagerContext, OwXMLUtil)
     */
    public void init(OwHistoryManagerContext mainContext_p, OwXMLUtil configNode_p) throws Exception
    {
        super.init(mainContext_p, configNode_p);

        // get the JdbcTemplate DataSource from bootstrap
        m_jdbcTemplate = getContext().getJDBCTemplate();

        if (m_jdbcTemplate == null)
        {
            throw new OwConfigurationException(mainContext_p.localize("app.OwConfiguration.HistoryManagerDefaultDataSourceMissing",
                    "There is no DefaultDataSource for the History Manager defined in owbootstrap.xml. Please use OwSimpleHistoryManager."));
        }

        // initialize filter if configured
        m_filter = createEventFilter(getConfigNode());

        HISTORY_TABLE_NAME = configNode_p.getSafeTextValue(DB_HISTORY_MANAGER_TABLE_NAME, HISTORY_TABLE_NAME);

        m_summaryColumnLength = getColumnLength(OW_HIST_SUMMARY) - 1;
    }

    /**
     * (overridable) Create an event filter based on a configuration XML element.
     * The configuration is inspected using a {@link OwFilterConfigurationLoader}.
     * If no event filter configuration is found the a default {@link OwHistoryEventFilter} instance is returned.    
     * @param configuration_p the current &lt;HistoryManager&gt; element
     * @return an {@link OwEventFilter}  
     * @throws Exception if the configuration reading fails or the event filter cannot be instantiated.
     */
    protected OwEventFilter createEventFilter(OwXMLUtil configuration_p) throws Exception
    {
        OwFilterConfigurationLoader configLoader = new OwFilterConfigurationLoader(configuration_p);
        OwEventFilter configuredFilter = configLoader.loadEventFilter();
        if (configuredFilter != null)
        {
            return configuredFilter;
        }
        else
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwDBHistoryManager.createEventFilter: Event filter class is not configured. Proceeding with default OwHistoryEventFilter...");
            }

            //return default event filter implementation 
            return new OwHistoryEventFilter(getConfigNode());
        }
    }

    /** flag indicating if properties should be written and read */
    public boolean getHistorizeProperties()
    {
        return getConfigNode().getSafeBooleanValue("HistorizeProperties", false);
    }

    /** 
     * add a new history event to the history database if supported by the history manager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwHistoryEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, OwEvent event_p, int iStatus_p) throws Exception
    {

        super.addEvent(iEventType_p, strEventID_p, event_p, iStatus_p);
        // just add the event if the event does not match the filter
        if (getEventFilter().match(iEventType_p, strEventID_p, iStatus_p))
        {
            return;
        }

        if (event_p instanceof OwHistoryObjectChangeEvent)
        {
            try
            {
                addObjectChangeEvent(iEventType_p, strEventID_p, (OwHistoryObjectChangeEvent) event_p, iStatus_p);
                LOG.debug("OwHistoryObjectChangeEvent entry was written: attached to an object=[" + event_p.getSummary() + "]...");
            }
            catch (Exception e)
            {
                String msg = "OwDBHistoryManager: Exception adding history event (OwHistoryObjectChangeEvent) - using OwEvent...";
                LOG.error(msg, e);
                throw new OwHistoryException(msg, e);
            }
        }
        else if (event_p instanceof OwHistoryPropertyChangeEvent)
        {
            try
            {
                addPropertyChangeEvent(iEventType_p, strEventID_p, (OwHistoryPropertyChangeEvent) event_p, iStatus_p);
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("OwHistoryPropertyChangeEvent entry was written: attached to an object=[" + event_p.getSummary() + "]...");
                }
            }
            catch (Exception e)
            {
                String msg = "OwDBHistoryManager: Exception adding history event (OwHistoryPropertyChangeEvent) - using OwEvent...";
                LOG.error(msg, e);
                throw new OwHistoryException(msg, e);
            }
        }
        //else if (event_p instanceof OwHistoryAnnotationEvent)
        //else if (event_p instanceof OwHistoryObjectCreateEvent)
        //else if (event_p instanceof OwHistoryObjectDeleteEvent)
        else
        {
            //get the event summary
            String summary;
            try
            {
                summary = event_p.getSummary();
            }
            catch (Exception e)
            {
                String msg = "Exception adding history event (using OwEvent) - Error retrieving the summary of event...";
                LOG.error(msg, e);
                throw new OwHistoryException(msg, e);
            }

            // truncate summary string 
            if ((summary.length() > m_summaryColumnLength) && (m_summaryColumnLength > 0))
            {
                String fullString = summary;
                summary = truncate(summary, m_summaryColumnLength);
                LOG.warn("Summary description exceeded database field definition; Full summary description: " + fullString);
                LOG.debug("Summary database field length is defined with " + m_summaryColumnLength + ". Summary was truncated to length " + summary.length() + " [" + summary + "]");

            }

            // === standard or unknown OwEvent
            try
            {
                // write to database
                getJdbcTemplate().update(
                        new StringBuilder("insert into ").append(HISTORY_TABLE_NAME).append(" (").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY).append(",")
                                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY).append(",").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.STATUS_PROPERTY).append(",")
                                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TIME_PROPERTY).append(",").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.USER_PROPERTY).append(",")
                                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY).append(") values (?,?,?,?,?,?)").toString(),
                        new Object[] { Integer.valueOf(iEventType_p), strEventID_p, Integer.valueOf(iStatus_p), new Date(), getCurrentUser(), summary });
                if (LOG.isDebugEnabled())
                {
                    LOG.debug("DB History entry was written: using the summary of event=[" + summary + "]");
                }
            }
            catch (Exception e)
            {
                String msg = "OwDBHistoryManager: Exception adding history event - using OwEvent...";
                LOG.error(msg, e);
                throw new OwHistoryException(msg, e);
            }
        }
    }

    /**
     * Write a special event to the database tables
     * @param eventType_p
     * @param strEventID_p
     * @param event_p
     * @param status_p
     * @param currentUser_p
     * @param dmsid_p
     * @param refinfo_p
     * @param parentdmsid_p
     * @param parentrefinfo_p
     * @param sModifiedProperties_p
     * @param sOldPropertyValues_p
     * @param sNewPropertyValues_p
     * @throws OwException
     */
    protected void internalInsertSpecialEvent(int eventType_p, String strEventID_p, OwEvent event_p, int status_p, String currentUser_p, String dmsid_p, String refinfo_p, String parentdmsid_p, String parentrefinfo_p, String sModifiedProperties_p,
            String sOldPropertyValues_p, String sNewPropertyValues_p) throws OwException
    {
        //get the event summary
        String summary = null;
        try
        {
            summary = event_p.getSummary();
            // truncate summary string 
            if ((summary.length() > m_summaryColumnLength) && (m_summaryColumnLength > 0))
            {
                String fullString = summary;
                summary = truncate(summary, m_summaryColumnLength);
                LOG.warn("Summary description exceeded database field definition; Full summary description: " + fullString);

            }
        }
        catch (Exception e)
        {
            String msg = "Error retrieving the summary of event...";
            LOG.error(msg, e);
            throw new OwHistoryException(msg, e);
        }
        // assemble params
        Object[] params = new Object[13];
        int[] paramTypes = new int[13];
        params[0] = Integer.valueOf(eventType_p);
        paramTypes[0] = java.sql.Types.INTEGER;
        params[1] = strEventID_p;
        paramTypes[1] = java.sql.Types.VARCHAR;
        params[2] = Integer.valueOf(status_p);
        paramTypes[2] = java.sql.Types.INTEGER;
        params[3] = new Date();
        paramTypes[3] = java.sql.Types.TIMESTAMP;
        params[4] = currentUser_p;
        paramTypes[4] = java.sql.Types.VARCHAR;
        params[5] = summary;
        paramTypes[5] = java.sql.Types.VARCHAR;
        params[6] = dmsid_p;
        paramTypes[6] = java.sql.Types.VARCHAR;
        params[7] = refinfo_p;
        paramTypes[7] = java.sql.Types.VARCHAR;
        if ((null != sModifiedProperties_p) && getHistorizeProperties())
        {
            params[8] = sModifiedProperties_p;
            params[9] = sOldPropertyValues_p;
            params[10] = sNewPropertyValues_p;
        }
        else
        {
            params[8] = null;
            params[9] = null;
            params[10] = null;
        }
        paramTypes[8] = java.sql.Types.VARCHAR;
        paramTypes[9] = java.sql.Types.VARCHAR;
        paramTypes[10] = java.sql.Types.VARCHAR;
        if (null != parentdmsid_p)
        {
            params[11] = parentdmsid_p;
            params[12] = parentrefinfo_p;
        }
        else
        {
            params[11] = null;
            params[12] = null;
        }
        paramTypes[11] = java.sql.Types.VARCHAR;
        paramTypes[12] = java.sql.Types.VARCHAR;
        // write to database
        String sqlQuery = "insert into " + HISTORY_TABLE_NAME + " (" + OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY + "," + OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY + ","
                + OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.STATUS_PROPERTY + "," + OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TIME_PROPERTY + "," + OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.USER_PROPERTY
                + "," + OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY + "," + DB_PROPRTY_OBJECT_DMSID + "," + DB_PROPRTY_OBJECT_REF_INFO + "," + DB_PROPRTY_MODIFIED_PROPERTIES + "," + DB_PROPRTY_OLD_PROPERTY_VALUES + ","
                + DB_PROPRTY_NEW_PROPERTY_VALUES + "," + DB_PROPRTY_PARENT_OBJECT_DMSID + "," + DB_PROPRTY_PARENT_OBJECT_REF_INFO + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        getJdbcTemplate().update(sqlQuery, params, paramTypes);
    }

    /** add a new specialized history event to the history database if supported by the history manager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwHistoryEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    private void addPropertyChangeEvent(int iEventType_p, String strEventID_p, OwHistoryPropertyChangeEvent event_p, int iStatus_p) throws Exception
    {
        // === try a cast to OwStandardHistoryObjectChangeEvent
        OwObjectReference ref = event_p.getAffectedObject();
        String srefinfo = OwStandardObjectReference.getReferenceString(ref, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);

        // === create property names, old values and new values string in consistent order
        // NOTE: First property is the resource name
        // property names collection:  		<resource name>;<cardinality 1>;<property name 1>;<cardinality 2>;<property name 2>;...<cardinality n>;<property name n>
        // old property values collection: 	<property value string 1>;<property value string 2>;...<property value string n>
        // new property values collection: 	<property value string 1>;<property value string 2>;...<property value string n>
        //
        // The "cardinality" can have to following values: CARDINALITY_SCALAR = scalar, CARDINALITY_ARRAY = array.

        // create token strings
        LinkedList<String> propnames = new LinkedList<String>();
        LinkedList<String> oldvalues = new LinkedList<String>();
        LinkedList<String> newvalues = new LinkedList<String>();

        // first name is the resource
        propnames.add("");

        // now add the property names
        Iterator<?> it = event_p.getAffectedNewProperties().values().iterator();
        while (it.hasNext())
        {
            OwField newField = (OwField) it.next();
            OwField oldField = (OwField) event_p.getAffectedOldProperties().get(newField.getFieldDefinition().getClassName());

            // add property cardinality
            propnames.add(newField.getFieldDefinition().isArray() ? OwStandardHistoryEntry.CARDINALITY_ARRAY : OwStandardHistoryEntry.CARDINALITY_SCALAR);

            // add property name string
            propnames.add(newField.getFieldDefinition().getClassName());

            try
            {
                // add old property value string
                String sValue = getSerializePropertyValueString(oldField);
                oldvalues.add(sValue);
            }
            catch (NullPointerException e2)
            {
                oldvalues.add("");
            }

            // add new property value string
            try
            {
                String sValue = getSerializePropertyValueString(newField);
                newvalues.add(sValue);
            }
            catch (NullPointerException e2)
            {
                newvalues.add("");
            }
        }

        String propnamesstring = OwEscapedStringTokenizer.createDelimitedString(propnames);
        String oldvaluesstring = OwEscapedStringTokenizer.createDelimitedString(oldvalues);
        String newvaluesstring = OwEscapedStringTokenizer.createDelimitedString(newvalues);

        internalInsertSpecialEvent(iEventType_p, strEventID_p, event_p, iStatus_p, getCurrentUser(), ref.getDMSID(), srefinfo, null, null, propnamesstring, oldvaluesstring, newvaluesstring);
    }

    /** get a string representation of the given field value
     * 
     * @return a {@link String}
     * @throws Exception 
     */
    private String getSerializePropertyValueString(OwField field_p) throws Exception
    {
        if (field_p.getFieldDefinition().isArray())
        {
            Object[] objects = (Object[]) field_p.getValue();

            // create token string
            LinkedList<String> values = new LinkedList<String>();

            for (int i = 0; i < objects.length; i++)
            {
                values.add(OwStandardPropertyClass.getStringFromValue(objects[i], field_p.getFieldDefinition().getJavaClassName()));
            }

            return OwEscapedStringTokenizer.createDelimitedString(values);
        }
        else
        {
            return OwStandardPropertyClass.getStringFromValue(field_p.getValue(), field_p.getFieldDefinition().getJavaClassName());
        }
    }

    /** add a new specialized history event to the history database if supported by the history manager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param event_p OwHistoryEvent according to iEventType_p, contains additional information, such as the affected Objects or properties
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    private void addObjectChangeEvent(int iEventType_p, String strEventID_p, OwHistoryObjectChangeEvent event_p, int iStatus_p) throws Exception
    {
        // === try a cast to OwStandardHistoryObjectChangeEvent
        // === get parent ID 
        String sparentrefinfo = null;
        String sparentdmsid = null;

        if (null != event_p.getParent())
        {
            sparentdmsid = event_p.getParent().getDMSID();
            sparentrefinfo = OwStandardObjectReference.getReferenceString(event_p.getParent(), OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);
        }

        // === generate a entry for each object, so we can search later
        Iterator<?> it = event_p.getAffectedObjects().iterator();
        while (it.hasNext())
        {
            OwObjectReference ref = (OwObjectReference) it.next();
            String srefinfo = OwStandardObjectReference.getReferenceString(ref, OwStatusContextDefinitions.STATUS_CONTEXT_TIME_CRITICAL);

            internalInsertSpecialEvent(iEventType_p, strEventID_p, event_p, iStatus_p, getCurrentUser(), ref.getDMSID(), srefinfo, sparentdmsid, sparentrefinfo, null, null, null);
        }
    }

    /** add a new history event to the history database if supported by the history manager
     *
     * @param iEventType_p int type of event as defined in OwHistoryManager.HISTORY_EVENT_TYPE_...
     * @param strEventID_p Id of event such as a plugin id, can be null
     * @param iStatus_p int as defined in OwHistoryManager.HISTORY_STATUS_...
     */
    public void addEvent(int iEventType_p, String strEventID_p, int iStatus_p) throws Exception
    {
        try
        {
            // just add the event if the event does not match the filter
            if (!getEventFilter().match(iEventType_p, strEventID_p, iStatus_p))
            {
                getJdbcTemplate().update(
                        new StringBuilder("insert into ").append(HISTORY_TABLE_NAME).append(" (").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY).append(",")
                                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY).append(",").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.STATUS_PROPERTY).append(",")
                                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TIME_PROPERTY).append(",").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.USER_PROPERTY).append(") values (?,?,?,?,?)").toString(),
                        new Object[] { Integer.valueOf(iEventType_p), strEventID_p, Integer.valueOf(iStatus_p), new Date(), getCurrentUser() });
                //                if (LOG.isDebugEnabled())
                //                {
                //                    LOG.debug("DB History entry was written (without summary of event), eventType=" + iEventType_p + " eventId=[" + strEventID_p + "] status=" + iStatus_p);
                //                }
            }
        }
        catch (Exception e)
        {
            String msg = "OwDBHistoryManager: Exception adding history event, check the settings of the DefaultDataSource in the bootstrap.xml, is the History DB accessible?...";
            LOG.error(msg, e);
            throw new OwHistoryException(msg, e);
        }
    }

    /** retrieve the name of the currently logged on user
     */
    private String getCurrentUser() throws Exception
    {
        return getContext().getCurrentUser().getUserLongName();
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
        return doObjectSearch(null, filterCriteria_p, sortCriteria_p, propertyNames_p, null, iMaxSize_p);
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
    public OwObjectCollection doObjectSearch(OwObjectReference object_p, OwSearchNode filterCriteria_p, OwSort sortCriteria_p, java.util.Collection propertyNames_p, int[] includeSubObjectTypes_p, int iMaxSize_p) throws Exception
    {
        OwObjectCollection ret = super.doObjectSearch(object_p, filterCriteria_p, sortCriteria_p, propertyNames_p, includeSubObjectTypes_p, iMaxSize_p);
        if (null != ret)
        {
            return ret;
        }

        // the collection for the results
        final OwStandardObjectCollection retCollection = new OwStandardObjectCollection();

        // build the where clause
        StringBuffer whereClause = new StringBuffer("");
        Object[] params = new Object[0];
        if (object_p != null)
        {
            whereClause.append("(" + DB_PROPRTY_OBJECT_DMSID + " = ?)");
            params = new Object[1];
            params[0] = object_p.getDMSID();
        }
        if (filterCriteria_p != null)
        {
            OwSearchSQLPreparedStatementOperator searchoperator = new OwSearchSQLPreparedStatementOperator();
            java.io.StringWriter SQLStatement = new java.io.StringWriter();
            if (searchoperator.createSQLSearchCriteria(filterCriteria_p, SQLStatement))
            {
                // get the parameters from the generated SQL string
                Object[] searchOperatorParams = searchoperator.getParameterBindings();
                // append generated clause
                if (whereClause.length() > 0)
                {
                    whereClause.append(" and ");
                }
                whereClause.append(SQLStatement.toString());
                // compile new params array
                Object[] newParams = new Object[params.length + searchOperatorParams.length];
                System.arraycopy(params, 0, newParams, 0, params.length);
                System.arraycopy(searchOperatorParams, 0, newParams, params.length, searchOperatorParams.length);
                params = newParams;
            }
        }

        // build the order by clause
        StringBuffer orderbyClause = new StringBuffer("");
        boolean needComma = false;
        if (sortCriteria_p != null)
        {
            Iterator<?> itsort = sortCriteria_p.getCriteriaCollection().iterator();
            while (itsort.hasNext())
            {
                if (needComma)
                {
                    orderbyClause.append(",");
                }
                OwSort.OwSortCriteria sortcriteria = (OwSort.OwSortCriteria) itsort.next();
                orderbyClause.append(sortcriteria.getPropertyName());
                orderbyClause.append(sortcriteria.getAscFlag() ? " asc " : " desc ");
            }
        }

        // create the SQL statement
        StringBuffer sqlQuery = new StringBuffer("SELECT ").append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TYPE_PROPERTY).append(",") // 1
                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.ID_PROPERTY).append(",") // 2
                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.STATUS_PROPERTY).append(",") // 3
                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.TIME_PROPERTY).append(",") // 4
                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.USER_PROPERTY).append(",") // 5
                .append(OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY).append(",")// 6
                .append(DB_PROPRTY_OBJECT_DMSID).append(",") // 7
                .append(DB_PROPRTY_OBJECT_REF_INFO).append(",") // 8
                .append(DB_PROPRTY_PARENT_OBJECT_DMSID).append(",") // 9
                .append(DB_PROPRTY_PARENT_OBJECT_REF_INFO); // 10
        if (getHistorizeProperties())
        {
            sqlQuery.append("," + DB_PROPRTY_MODIFIED_PROPERTIES + "," + DB_PROPRTY_OLD_PROPERTY_VALUES + "," + DB_PROPRTY_NEW_PROPERTY_VALUES);
        }
        sqlQuery.append(" FROM " + HISTORY_TABLE_NAME);
        if (whereClause.length() != 0)
        {
            sqlQuery.append(" where ");
            sqlQuery.append(whereClause.toString());
        }
        if (orderbyClause.length() != 0)
        {
            sqlQuery.append(" order by ");
            sqlQuery.append(orderbyClause.toString());
        }

        // perform query
        final int max = iMaxSize_p;
        getJdbcTemplate().query(sqlQuery.toString(), params, new RowCallbackHandler() {
            int iRemaining = max;

            public void processRow(ResultSet resultSet_p) throws SQLException
            {
                if (iRemaining > 0)
                {
                    try
                    {
                        retCollection.add(createHistoryEntry(getContext(), resultSet_p));
                        iRemaining--;
                    }
                    catch (Exception e)
                    {
                        // we can not throw an exception since we are in an enclosed class
                        LOG.error("Error creating history result entry", e);
                    }
                }
            }
        });

        // return result collection
        return retCollection;
    }

    /** create a history entry out of a result set
     * 
     * @param context_p
     * @param resultSet_p
     * @return the newly created {@link OwStandardHistoryEntry}
     * @throws Exception
     */
    protected OwStandardHistoryEntry createHistoryEntry(OwHistoryManagerContext context_p, ResultSet resultSet_p) throws Exception
    {
        int iType = resultSet_p.getInt(1);
        String strID = resultSet_p.getString(2);
        int iStatus = resultSet_p.getInt(3);
        java.sql.Timestamp time = resultSet_p.getTimestamp(4);
        String strUserID = resultSet_p.getString(5);
        String strSummaryID = resultSet_p.getString(6);

        String dmsid = resultSet_p.getString(7);
        String refinfo = resultSet_p.getString(8);
        String parentdmsid = resultSet_p.getString(9);
        String parentrefinfo = resultSet_p.getString(10);

        Collection<?> propertynames = null;
        Collection<?> oldproperties = null;
        Collection<?> newproperties = null;
        String resource = null;

        if (getHistorizeProperties())
        {
            // === get the property collection strings
            // SCHEME:
            // property names collection:       <resource name>;<property name 1>;<property name 2>;...<property name n>
            // old property values collection:  <property value string 1>;<property value string 2>;...<property value string n>
            // new property values collection:  <property value string 1>;<property value string 2>;...<property value string n>

            String propnames = resultSet_p.getString(11);
            String oldprops = resultSet_p.getString(12);
            String newprops = resultSet_p.getString(13);

            propertynames = new OwEscapedStringTokenizer(propnames).toCollection();

            // first name is the resource
            if (propertynames.size() > 0)
            {
                Iterator<?> itFirst = propertynames.iterator();
                resource = (String) itFirst.next();
                if (resource.length() == 0)
                {
                    resource = null;
                }

                itFirst.remove();
            }

            oldproperties = new OwEscapedStringTokenizer(oldprops).toCollection();
            newproperties = new OwEscapedStringTokenizer(newprops).toCollection();
        }

        if (dmsid != null)
        {
            // === event with object reference
            OwStandardObjectReference parentobj = null;
            OwStandardObjectReference obj = new OwStandardObjectReference(dmsid, refinfo, getNetwork());

            if (parentdmsid != null)
            {
                parentobj = new OwStandardObjectReference(parentdmsid, parentrefinfo, getNetwork());
            }

            if ((propertynames != null) && (propertynames.size() > 0))
            {
                // property change event
                return new OwStandardHistoryEntry(context_p, time, strID, iType, iStatus, strSummaryID, strUserID, obj, getNetwork(), resource, propertynames, oldproperties, newproperties);
            }
            else
            {
                // object change event
                LinkedList<Object> col = new LinkedList<Object>();
                col.add(obj);

                return new OwStandardHistoryEntry(context_p, time, strID, iType, iStatus, strSummaryID, strUserID, parentobj, col);
            }
        }
        else
        {
            // === standard event
            return new OwStandardHistoryEntry(context_p, time, strID, iType, iStatus, strSummaryID, strUserID);
        }
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
        return OwStandardHistoryEntry.getStaticObjectClass().getPropertyClass(strFieldDefinitionName_p);
    }

    /** reconstructs an Object from ECM Id, see OwObject.getDMSID for details.
     *
     * @param strDMSID_p ECM ID for the requested object
     * @param fRefresh_p true = force refresh of object from ECM System, false = may use cached object
     *
     * @returns an Object Instance
     *
     */
    public OwObject getObjectFromDMSID(String strDMSID_p, boolean fRefresh_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwDBHistoryManager.getObjectFromDMSID: Not implemented or Not supported.");
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
        throw new OwObjectNotFoundException("OwDBHistoryManager.getObjectFromPath: Not implemented or Not supported.");
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
        throw new OwObjectNotFoundException("OwDBHistoryManager.getObjectClass: Not implemented or Not supported.");
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
        throw new OwNotSupportedException("OwDBHistoryManager.getObjectClassNames: Not implemented.");
    }

    /** get the resource with the specified key
     *
     * @param strID_p String resource ID, if strID_p is null, returns the default resource
     */
    public OwResource getResource(String strID_p) throws Exception
    {
        throw new OwNotSupportedException("OwDBHistoryManager.getResource: Not implemented.");
    }

    /** get a Iterator of available resource IDs
     * 
     * @return Collection of resource IDs used in getResource, or null if no resources are available
     */
    public java.util.Iterator getResourceIDs() throws Exception
    {
        throw new OwNotSupportedException("OwDBHistoryManager.getResourceIDs: Not implemented.");
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

    /**
     * Return the currently used event filter, which is used
     * by this manager to exclude unwanted events.
     * @see #createEventFilter(OwXMLUtil)
     * @return OwEventFilter which is defined
     */
    protected OwEventFilter getEventFilter()
    {
        return this.m_filter;
    }

    /**
     * Return the currently used JDBC template for 
     * DB connection. <br />Can be <code>null</code> if configuration
     * is missing or wrong.
     * @return org.springframework.jdbc.core.JDBCTemplate 
     */
    protected JdbcTemplate getJdbcTemplate()
    {
        return this.m_jdbcTemplate;
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
        private LinkedList<OwWildCardDefinition> m_definitions;

        public OwLikeWildCardDefinitions(OwRepositoryContext context_p)
        {
            m_definitions = new LinkedList<OwWildCardDefinition>();

            m_definitions.add(new OwStandardWildCardDefinition("*", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR, new OwString1(
                    "historyimpl.OwDBHistoryManager.WILD_CARD_TYPE_MULTI_CHAR", "(%1) replaces any characters")));
            m_definitions.add(new OwStandardWildCardDefinition("_", context_p.getClientWildCard(OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR), OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR, new OwString1(
                    "historyimpl.OwDBHistoryManager.WILD_CARD_TYPE_SINGLE_CHAR", "(%1) replaces any character")));
        }

        public Collection<? extends OwWildCardDefinition> getDefinitions()
        {
            return m_definitions;
        }
    }

    /**
    Truncate a String to the given length with no warnings
    or error raised if it is bigger.
    @param  value_p String to be truncated
    @param  length_p  Maximum length of string
    @since  3.1.0.0
    @return Returns value if value is null or value.length() is less or equal to than length, otherwise a String representing
      value truncated to length.
    */
    private static String truncate(String value_p, int length_p)
    {
        if (value_p != null && value_p.length() > length_p)
        {
            value_p = value_p.substring(0, length_p);
        }
        return value_p;
    }

    /**
     * Return column  size definition
     * @param columnName_p column Name
     * @return column size
     * @throws SQLException
     */
    private int getColumnLength(String columnName_p) throws SQLException
    {
        Connection con = null;
        int size = 0;
        try
        {
            con = getJdbcTemplate().getDataSource().getConnection();
            DatabaseMetaData meta = con.getMetaData();
            // ResultSet columnRes = dbMeta.getProcedureColumns(cat, schema, name, "%");
            ResultSet resultSet = meta.getColumns(null, null, HISTORY_TABLE_NAME, null);

            while (resultSet.next())
            {
                String name = resultSet.getString("COLUMN_NAME");
                String type = resultSet.getString("TYPE_NAME");
                if ((name.toUpperCase()).equals(columnName_p))
                {
                    size = resultSet.getInt("COLUMN_SIZE");
                    if (LOG.isDebugEnabled())
                    {
                        LOG.debug(new StringBuilder("Column name: [").append(name).append("]; type: [").append(type).append("];size: [").append(size).append("];"));
                    }
                    break;
                }
            }

        }
        catch (SQLException e)
        {
            LOG.error(e.getMessage(), e);
            throw e;
        }
        finally
        {
            if (con != null)
            {
                con.close();
                con = null;
            }
        }

        return size;
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
        throw new OwInvalidOperationException("OwDBHistoryManager.closeBatch: Not implemented or not supported");
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwRepository#openBatch()
     */
    public OwBatch openBatch() throws OwInvalidOperationException
    {
        throw new OwInvalidOperationException("OwDBHistoryManager.openBatch: Not implemented or not supported");
    }
}