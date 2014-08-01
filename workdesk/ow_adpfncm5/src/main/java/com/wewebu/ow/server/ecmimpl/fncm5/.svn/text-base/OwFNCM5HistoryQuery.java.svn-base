package com.wewebu.ow.server.ecmimpl.fncm5;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.filenet.api.constants.PropertyNames;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectsQuery;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass;

/**
 *<p>
 * History entries objects query.
 * The query relays on an search template {@link OwSearchNode} to generate an SQL statement that 
 * will be used to retrieve event objects. <br/>
 * The SQL statement is sorted for all properties except the name and the summary property (see {@link com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass#NAME_PROPERTY} and 
 * {@link com.wewebu.ow.server.history.OwStandardHistoryEntry.OwStandardHistoryEntryObjectClass#SUMMARY_PROPERTY}).
 * For the two properties mentioned above the sort is performed on the already retrieved object collection.
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
public class OwFNCM5HistoryQuery implements OwObjectsQuery
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNCM5HistoryQuery.class);

    /**AWD properties that can not be mapped to FNCM properties*/
    private static final Set OWD_ONLY_PROPERTIES_MAPPINGS = new HashSet();

    /**AWD properties that need FNCM mapping */
    private static final Map STANDARD_PROPERTIES_MAPPINGS = new HashMap();

    static
    {
        STANDARD_PROPERTIES_MAPPINGS.put(OwStandardHistoryEntryObjectClass.TIME_PROPERTY, PropertyNames.DATE_CREATED);
        STANDARD_PROPERTIES_MAPPINGS.put(OwStandardHistoryEntryObjectClass.USER_PROPERTY, PropertyNames.CREATOR);
        STANDARD_PROPERTIES_MAPPINGS.put(OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, PropertyNames.MODIFIED_PROPERTIES);
    }

    static
    {
        OWD_ONLY_PROPERTIES_MAPPINGS.add(OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY);
        OWD_ONLY_PROPERTIES_MAPPINGS.add(OwStandardHistoryEntryObjectClass.NAME_PROPERTY);
    }

    private OwFNCM5HistoryManager historyManager;

    private OwSearchNode filterCriteria;
    private Collection propertyNames;
    private int maxSize;
    private int versionSelection;
    private String additionalWhereClause;

    private String bpmObjectstoreName;

    /**
     * Constructor
     * @param historyManager_p the 
     * @param filterCriteria_p the 
     * @param propertyNames_p
     * @param iMaxSize_p
     * @param iVersionSelection_p
     * @param additionalWhereClause_p
     * @param bpmObjectstoreName_p
     */
    public OwFNCM5HistoryQuery(OwFNCM5HistoryManager historyManager_p, OwSearchNode filterCriteria_p, Collection propertyNames_p, int iMaxSize_p, int iVersionSelection_p, String additionalWhereClause_p, String bpmObjectstoreName_p)
    {
        historyManager = historyManager_p;

        filterCriteria = filterCriteria_p;
        propertyNames = propertyNames_p;
        if (null == propertyNames)
        {
            propertyNames = new Vector();
        }
        maxSize = iMaxSize_p;
        versionSelection = iVersionSelection_p;
        additionalWhereClause = additionalWhereClause_p;

        bpmObjectstoreName = bpmObjectstoreName_p;
    }

    public OwObjectCollection execute(OwSort sort_p) throws OwException
    {
        OwFNCM5Network network = (OwFNCM5Network) historyManager.getNetwork();
        OwObjectCollection result = network.doSearch(filterCriteria, sort_p, propertyNames, maxSize, versionSelection);
        return result;
    }
}
