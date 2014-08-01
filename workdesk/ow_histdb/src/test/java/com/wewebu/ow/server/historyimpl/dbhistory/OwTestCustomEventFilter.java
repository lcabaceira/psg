package com.wewebu.ow.server.historyimpl.dbhistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
*<p>
* OwTestCustomEventFilter. 
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
public class OwTestCustomEventFilter implements OwEventFilter
{
    public static final Integer TEST_FILTERED_EVENT_TYPE_1_INDEX = new Integer(8991);
    public static final Integer TEST_FILTERED_EVENT_TYPE_2_INDEX = new Integer(8992);
    public static final String TEST_FILTERED_EVENT_TYPE_1 = "TEST_FILTERED_EVENT_TYPE_1";
    public static final String TEST_FILTERED_EVENT_TYPE_2 = "TEST_FILTERED_EVENT_TYPE_2";
    public static final Integer TEST_FILTERED_STATUS_1_INDEX = new Integer(2901);
    public static final Integer TEST_FILTERED_STATUS_2_INDEX = new Integer(2902);
    public static final String TEST_FILTERED_STATUS_1 = "TEST_FILTERED_STATUS_1";
    public static final String TEST_FILTERED_STATUS_2 = "TEST_FILTERED_STATUS_2";
    public static final String COM_WEWEBU_OW_TEST_EVENTID_1 = "com.wewebu.ow.test.eventid_1";
    public static final String COM_WEWEBU_OW_TEST_EVENTID_2 = "com.wewebu.ow.test.eventid_2";

    private static final Map EVENT_INDEXES_MAP = new HashMap();
    private static final Map STATUS_INDEXES_MAP = new HashMap();
    private static final List ALL_EVENT_IDS = new ArrayList();

    static
    {
        EVENT_INDEXES_MAP.put(TEST_FILTERED_EVENT_TYPE_1, TEST_FILTERED_EVENT_TYPE_1_INDEX);
        EVENT_INDEXES_MAP.put(TEST_FILTERED_EVENT_TYPE_2, TEST_FILTERED_EVENT_TYPE_2_INDEX);

        STATUS_INDEXES_MAP.put(TEST_FILTERED_STATUS_1, TEST_FILTERED_STATUS_1_INDEX);
        STATUS_INDEXES_MAP.put(TEST_FILTERED_STATUS_2, TEST_FILTERED_STATUS_2_INDEX);

        ALL_EVENT_IDS.add(COM_WEWEBU_OW_TEST_EVENTID_1);
        ALL_EVENT_IDS.add(COM_WEWEBU_OW_TEST_EVENTID_2);
    }

    private List filteredEventTypes = new ArrayList();
    private List filteredStatuses = new ArrayList();
    private List filteredEventIds = new ArrayList();

    public OwTestCustomEventFilter(OwXMLUtil configuration_p) throws Exception
    {
        OwFilterConfigurationLoader configLoader = new OwFilterConfigurationLoader(configuration_p) {
            protected void loadTypeFilter(String typeFilter_p) throws Exception
            {
                Integer typeIndex = (Integer) EVENT_INDEXES_MAP.get(typeFilter_p);
                if (typeFilter_p != null)
                {
                    filteredEventTypes.add(typeIndex);
                }
                else
                {
                    throw new OwConfigurationException("Invalid event type : " + typeFilter_p);
                }
            }

            protected void loadStatusFilter(String statusFilter_p) throws Exception
            {
                Integer statusIndex = (Integer) STATUS_INDEXES_MAP.get(statusFilter_p);
                if (statusIndex != null)
                {
                    filteredStatuses.add(statusIndex);
                }
                else
                {
                    throw new OwConfigurationException("Invalid status : " + statusFilter_p);
                }

            }

            protected void loadEventIdFilter(String eventIdFilter_p) throws Exception
            {
                if (ALL_EVENT_IDS.contains(eventIdFilter_p))
                {
                    filteredEventIds.add(eventIdFilter_p);
                }
                else
                {
                    throw new OwConfigurationException("Invalid status : " + eventIdFilter_p);
                }

            }
        };

        configLoader.loadConfiguration();
    }

    public boolean match(int eventType_p, String strEventID_p, int status_p)
    {
        return filteredEventTypes.contains(new Integer(eventType_p)) || filteredEventIds.contains(strEventID_p) || filteredStatuses.contains(new Integer(status_p));
    }

}
