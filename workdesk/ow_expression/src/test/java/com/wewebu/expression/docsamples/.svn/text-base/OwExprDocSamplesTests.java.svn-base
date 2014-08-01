package com.wewebu.expression.docsamples;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.wewebu.expression.OwExprTestBase;
import com.wewebu.expression.language.OwExprBooleanValue;
import com.wewebu.expression.language.OwExprEvaluationException;
import com.wewebu.expression.language.OwExprReflectiveScope;
import com.wewebu.expression.language.OwExprTypeMissmatchException;
import com.wewebu.expression.parser.ParseException;

/**
*<p>
* OwExprDocSamplesTests. 
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
public class OwExprDocSamplesTests extends OwExprTestBase
{
    private List orderDocuments;

    protected Calendar date(int day_p, int month_p, int year_p)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day_p);
        c.set(Calendar.MONTH, month_p - 1);
        c.set(Calendar.YEAR, year_p);

        return c;
    }

    protected void setUp() throws Exception
    {
        orderDocuments = new ArrayList();

        orderDocuments.add(new OwExprOrderDocument("KW221", "SUBMITTED", 12, date(28, 4, 2009), 3000.0, 5000.0, 200.0, true, new boolean[] { true, false, false }));
        orderDocuments.add(new OwExprOrderDocument("WWU1", "INPROCESS", 3, date(10, 03, 2009), 2500.0, 5000.0, 300.0, false, new boolean[] { false, false, false }));
        orderDocuments.add(new OwExprOrderDocument("SVA2", "CLOSED", 5, date(22, 01, 2009), 2000.0, 5000.0, 300.0, false, new boolean[] { true, true, true }));
        orderDocuments.add(new OwExprOrderDocument("SPL3", "REJECTED", 300, date(23, 03, 2009), 3000.0, 4200.0, 312.0, false, new boolean[] { false, false, true }));
        orderDocuments.add(new OwExprOrderDocument("SVA2", "INPROCESS", 20, date(27, 02, 2009), 7000.0, 5000.0, 400.0, true, new boolean[] { false, false, false }));

    }

    protected List select(String expression_p) throws OwExprTypeMissmatchException, OwExprEvaluationException, ParseException
    {
        int index = 0;
        List selectedIndexesList = new ArrayList();
        for (Iterator i = orderDocuments.iterator(); i.hasNext(); index++)
        {

            OwExprOrderDocument doc = (OwExprOrderDocument) i.next();
            OwExprReflectiveScope docObjectScope = new OwExprReflectiveScope("object", doc);
            if (value(expression_p, docObjectScope).equals(OwExprBooleanValue.TRUE))
            {
                selectedIndexesList.add(new Integer(index + 1));
            }
        }

        return selectedIndexesList;
    }

    private String indexesString(int[] indexes_p)
    {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        if (i < indexes_p.length)
        {
            sb.append(indexes_p[i]);
            i++;
        }
        for (; i < indexes_p.length; i++)

        {
            sb.append(",");
            sb.append(indexes_p[i]);
        }
        return sb.toString();
    }

    private String indexesString(List indexesList_p)
    {
        StringBuffer sb = new StringBuffer();
        Iterator it = indexesList_p.iterator();
        if (it.hasNext())
        {
            Integer iIndex = (Integer) it.next();
            sb.append(iIndex);
        }
        for (; it.hasNext();)

        {
            Integer iIndex = (Integer) it.next();
            sb.append(",");
            sb.append(iIndex);
        }
        return sb.toString();
    }

    protected void assertSelect(String selectExpression_p, int[] expectedIndexes_p) throws OwExprTypeMissmatchException, OwExprEvaluationException, ParseException
    {
        List result = select(selectExpression_p);
        boolean wrongResult = (result.size() != expectedIndexes_p.length);
        if (!wrongResult)
        {
            for (int i = 0; i < expectedIndexes_p.length; i++)
            {
                Integer iIndex = new Integer(expectedIndexes_p[i]);
                if (!result.contains(iIndex))
                {
                    wrongResult = true;
                    break;
                }
            }
        }

        if (wrongResult)
        {
            fail("expexted<" + indexesString(expectedIndexes_p) + "> but was :<" + indexesString(result) + ">");
        }

    }

    public void testSampleExpressions() throws Exception
    {
        assertSelect("object.documentStatus='CLOSED'", new int[] { 3 });
        assertSelect("object.amount>100", new int[] { 4 });
        assertSelect("object.finalPrize/object.listPrize<0.5", new int[] { 3 });

        assertSelect("object.finalPrize/object.listPrize<0.5 or object.finalPrize<object.trasholdPrize*object.amount", new int[] { 3, 4, 5 });

        assertSelect("(object.finalPrize/object.listPrize<0.5 or object.finalPrize<object.trasholdPrize*object.amount) and not object.force", new int[] { 3, 4 });

        assertSelect("object.Force?object.Amount>15:object.Amount>100", new int[] { 4, 5 });

        assertSelect("object.approved[0] and not object.approved[1] and not object.approved[2]", new int[] { 1 });

        assertSelect("(date(2009,4,28)-object.uploadDate).inDays>90", new int[] { 3 });
        assertSelect("(date(2009,4,28)-days(90))>object.uploadDate", new int[] { 3 });

        assertSelect("object.clientId.like('S%[23]')", new int[] { 3, 4, 5 });
    }
}
