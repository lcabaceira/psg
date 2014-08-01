package com.wewebu.ow.server.historyimpl.dbhistory;

/**
*<p>
* OwDBHistoryDummyAdapterFilterTest_MsSql. 
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
public class OwDBHistoryDummyAdapterFilterTest_MsSql extends OwDBHistoryDummyAdapterFilterTest
{

    public OwDBHistoryDummyAdapterFilterTest_MsSql(String arg0_p)
    {
        super(arg0_p);
    }

    protected String getTestBasename()
    {
        return "dbhistory_mssql";
    }

}
