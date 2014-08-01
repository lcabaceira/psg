package com.wewebu.ow.server.ecmimpl.opencmis.nativeapi;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.alfresco.cmis.client.impl.AlfrescoObjectFactoryImpl;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.RepositoryInfo;

import com.wewebu.ow.server.ecmimpl.opencmis.util.OwCMISDateTime;

public class DateSearchTest extends AbstractNativeTest
{

    private String to2Digits(int n)
    {
        return to2Digits(n, false);
    }

    private String to2Digits(int n, boolean signed)
    {

        String twoDigits = signed ? (n > 0 ? "+" : (n == 0 ? "" : "-")) : "";

        int abs = Math.abs(n);

        if (abs < 10)
        {
            twoDigits += "0";
        }

        twoDigits += abs;

        return twoDigits;

    }

    private String cmisDate(int year, int month, int day, int hour, int minute, int second, int zoneOffsetInHours)
    {
        String offset = "Z";
        if (zoneOffsetInHours != 0)
        {
            offset = to2Digits(zoneOffsetInHours, true) + ":00";

        }

        return "" + to2Digits(year) + "-" + to2Digits(month) + "-" + to2Digits(day) + "T" + to2Digits(hour) + ":" + to2Digits(minute) + ":" + to2Digits(second) + ".000" + offset;
    }

    private ItemIterable<QueryResult> query(String date1, String date2)
    {
        String sql = "SELECT cmis:objectId,cmis:name,cmis:lastModificationDate FROM cmis:document WHERE (cmis:lastModificationDate>=TIMESTAMP'" + date1 + "' AND cmis:lastModificationDate<=TIMESTAMP'" + date2
                + "') ORDER BY cmis:lastModificationDate ASC";

        Session session = createSession(true, AlfrescoObjectFactoryImpl.class.getName());

        RepositoryInfo rInfo = session.getRepositoryInfo();

        System.out.println(rInfo.getProductName() + " v " + rInfo.getProductVersion());

        System.out.println("QUERY : " + sql);

        OperationContext opCtx = getSession().createOperationContext();
        opCtx.setMaxItemsPerPage(10);

        ItemIterable<QueryResult> result = session.query(sql, false, opCtx);

        return result;
    }

    public void testSystemProperties() throws Exception
    {
        OperationContext opCtx = getSession().createOperationContext();
        opCtx.setMaxItemsPerPage(10);

        ItemIterable<QueryResult> r1;
        {

            String sqlDate1 = cmisDate(2013, 1, 15, 0, 0, 0, 0);
            String sqlDate2 = cmisDate(2013, 2, 15, 7, 15, 0, 0);

            r1 = query(sqlDate1, sqlDate2);
            trace(r1);
        }

        ItemIterable<QueryResult> r2;
        {
            String sqlDate1 = cmisDate(2013, 1, 15, 2, 0, 0, 2);
            String sqlDate2 = cmisDate(2013, 2, 15, 9, 15, 0, 2);

            r2 = query(sqlDate1, sqlDate2);
            trace(r2);
        }

        assertSameResults(r1, r2);
    }

    private List<String> idList(ItemIterable<QueryResult> result)
    {
        List<String> ids = new ArrayList<String>();

        result.iterator();
        Iterator<QueryResult> i = result.iterator();

        while (i.hasNext())
        {
            QueryResult qr = i.next();
            String id = qr.getPropertyValueById("cmis:objectId");
            ids.add(id);
        }

        return ids;
    }

    private void assertSameResults(ItemIterable<QueryResult> r1, ItemIterable<QueryResult> r2)
    {
        List<String> r1Ids = idList(r1);
        List<String> r2Ids = idList(r2);

        //same contents && same order 
        assertTrue(r1Ids.equals(r2Ids));
    }

    private void trace(ItemIterable<QueryResult> result)
    {
        Iterator<QueryResult> i = result.iterator();
        while (i.hasNext())
        {
            QueryResult qr = i.next();
            trace(qr);
        }

    }

    private void trace(QueryResult qr)
    {
        String name = qr.getPropertyValueById("cmis:name");
        GregorianCalendar modified = qr.getPropertyValueById("cmis:lastModificationDate");
        OwCMISDateTime modifiedDateTime = new OwCMISDateTime(modified);
        System.out.println(name + "@" + modifiedDateTime.toString());
    }
}
