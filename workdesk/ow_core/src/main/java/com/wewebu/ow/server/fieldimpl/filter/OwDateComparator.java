package com.wewebu.ow.server.fieldimpl.filter;

import java.util.Calendar;
import java.util.Date;

import com.wewebu.ow.server.field.OwSearchCriteria;

/**
 *<p>
 * Simple date comparator.
 * Implements a simple compare functionality of the provided filter and value. Dynamically
 * compare full, only date or only time depending on filter definition. 
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
 *@since 3.2.0.0
 */
public class OwDateComparator extends OwAbstractComparator<java.util.Date>
{
    protected boolean matchBetween(OwSearchCriteria filter, Date value)
    {
        Date val = (Date) filter.getValue();
        Date val2 = (Date) filter.getSecondRangeCriteria().getValue();

        if (val == null || val2 == null || value == null)
        {
            return false;
        }
        else
        {
            long filterVal = getSpecificValue(filter, val);
            long filterVal2 = getSpecificValue(filter, val2);
            long longValue = getSpecificValue(filter, value);

            return filterVal <= longValue && longValue <= filterVal2;
        }
    }

    protected boolean matchLessEquals(OwSearchCriteria filter, Date value)
    {
        Date val = (Date) filter.getValue();
        if (val == null || value == null)
        {
            return false;
        }
        else
        {
            long filterVal = getSpecificValue(filter, val);
            return getSpecificValue(filter, value) <= filterVal;
        }
    }

    protected boolean matchGreaterEquals(OwSearchCriteria filter, Date value)
    {
        Date val = (Date) filter.getValue();
        if (val == null || value == null)
        {
            return false;
        }
        else
        {
            long filterVal = getSpecificValue(filter, val);
            return getSpecificValue(filter, value) >= filterVal;
        }
    }

    protected boolean matchLess(OwSearchCriteria filter, Date value)
    {
        Date val = (Date) filter.getValue();
        if (val == null || value == null)
        {
            return false;
        }
        else
        {
            long filterVal = getSpecificValue(filter, val);
            return getSpecificValue(filter, value) < filterVal;
        }
    }

    protected boolean matchGreater(OwSearchCriteria filter, Date value)
    {
        Date val = (Date) filter.getValue();
        if (val == null || value == null)
        {
            return false;
        }
        else
        {
            long filterVal = getSpecificValue(filter, val);
            return getSpecificValue(filter, value) > filterVal;
        }
    }

    protected boolean matchLike(OwSearchCriteria filter, Date value)
    {
        return false;
    }

    protected boolean matchEquals(OwSearchCriteria filter, Date value)
    {
        Date val = (Date) filter.getValue();
        if (val == null || value == null)
        {
            return false;
        }
        else
        {
            long filterVal = getSpecificValue(filter, val);
            return filterVal == getSpecificValue(filter, value);
        }
    }

    private long getSpecificValue(OwSearchCriteria filter, Date value)
    {
        Calendar ret = Calendar.getInstance();
        ret.setTimeInMillis(value.getTime());

        if (filter.ignoreTime())
        {
            ret.add(Calendar.MILLISECOND, -1 * ret.get(Calendar.MILLISECOND));
            ret.add(Calendar.SECOND, -1 * ret.get(Calendar.SECOND));
            ret.add(Calendar.MINUTE, -1 * ret.get(Calendar.MINUTE));
            ret.add(Calendar.HOUR_OF_DAY, -1 * ret.get(Calendar.HOUR_OF_DAY));
        }
        else if (filter.ignoreDate())
        {
            ret.set(Calendar.DAY_OF_YEAR, 1);
            ret.clear(Calendar.YEAR);
        }

        return ret.getTimeInMillis();
    }

}