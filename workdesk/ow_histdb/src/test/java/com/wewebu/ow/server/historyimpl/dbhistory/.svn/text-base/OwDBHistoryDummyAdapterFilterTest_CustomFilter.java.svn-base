package com.wewebu.ow.server.historyimpl.dbhistory;

/**
*<p>
* OwDBHistoryDummyAdapterFilterTest_CustomFilter. 
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
public class OwDBHistoryDummyAdapterFilterTest_CustomFilter extends OwDBHistoryTestBase
{

    public OwDBHistoryDummyAdapterFilterTest_CustomFilter(String name_p)
    {
        super(name_p);
    }

    public void testCustomFilteredEvents() throws Exception
    {

        final int filteredStatusId = OwTestCustomEventFilter.TEST_FILTERED_STATUS_1_INDEX.intValue();
        final int filteredType = OwTestCustomEventFilter.TEST_FILTERED_EVENT_TYPE_1_INDEX.intValue();
        final String filteredEventId = OwTestCustomEventFilter.COM_WEWEBU_OW_TEST_EVENTID_1;

        final int allowedStatusId = OwTestCustomEventFilter.TEST_FILTERED_STATUS_2_INDEX.intValue();
        final int allowedType = OwTestCustomEventFilter.TEST_FILTERED_EVENT_TYPE_2_INDEX.intValue();
        final String allowedEventId = OwTestCustomEventFilter.COM_WEWEBU_OW_TEST_EVENTID_2;

        int iSize = findStandardEvents(filteredEventId, filteredType).size();
        m_historyManager.addEvent(filteredType, filteredEventId, filteredStatusId);
        int iNewSize = findStandardEvents(filteredEventId, filteredType).size();

        assertEquals(iSize, iNewSize);

        int iSizeAllowed = findStandardEvents(allowedEventId, allowedType).size();
        m_historyManager.addEvent(allowedType, allowedEventId, allowedStatusId);
        int iNewSizeAllowed = findStandardEvents(allowedEventId, allowedType).size();

        assertEquals(iSizeAllowed, iNewSizeAllowed);

    }

    protected String getTestBasename()
    {
        return "dbhistory_mssql_custom_filter";
    }

}
