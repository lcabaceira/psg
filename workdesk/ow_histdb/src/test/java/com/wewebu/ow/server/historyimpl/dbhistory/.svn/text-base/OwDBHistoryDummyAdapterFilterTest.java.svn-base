package com.wewebu.ow.server.historyimpl.dbhistory;

import com.wewebu.ow.server.event.OwEventManager;

/**
 *<p>
 * DBHistory Test Case: Test all history functions against dummy adapter and a filter history manager.
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
public abstract class OwDBHistoryDummyAdapterFilterTest extends OwDBHistoryTestBase
{
    //private static final Logger LOG = Logger.getLogger(OwDBHistoryDummyAdapterFilterTest.class);

    public OwDBHistoryDummyAdapterFilterTest(String arg0_p)
    {
        super(arg0_p);
    }

    public void testStandardEvents() throws Exception
    {
        //OwStandardHistoryManager.getEventIDs();

        String sID = OwEventManager.HISTORY_EVENT_ID_LOGIN;
        int iEventType = OwEventManager.HISTORY_EVENT_TYPE_GENERIC;

        int iSize = findStandardEvents(sID, iEventType).size();
        m_historyManager.addEvent(iEventType, sID, OwEventManager.HISTORY_STATUS_OK);
        int iNewSize = findStandardEvents(sID, iEventType).size();

        assertEquals(iSize, iNewSize);

    }

    /** override with testcase
     * 
     * @return String
     */
    protected String getHistoryManagerSubNode()
    {
        return "HistoryManagerFilter";
    }

}
