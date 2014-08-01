package com.wewebu.ow.server.fieldimpl.filter;

import com.wewebu.ow.server.field.OwSearchCriteria;

/**
 *<p>
 * Simple number comparator.
 * Compare to number values with each other, if the objects implements the Comparable interface.
 * Otherwise the number compare is based on the long representation of the values.
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
@SuppressWarnings("unchecked")
public class OwNumberComparator extends OwAbstractComparator<Number>
{
    protected boolean matchBetween(OwSearchCriteria filter, Number value)
    {
        return matchGreaterEquals(filter, value) && matchLessEquals(filter.getSecondRangeCriteria(), value);
    }

    protected boolean matchLessEquals(OwSearchCriteria filter, Number value)
    {
        Number filterVal = (Number) filter.getValue();
        if (filterVal == null || value == null)
        {
            return false;
        }
        else
        {
            if (value instanceof Comparable)
            {
                return ((Comparable) value).compareTo(filterVal) <= 0;
            }
            else
            {
                return value.longValue() <= filterVal.longValue();
            }
        }
    }

    protected boolean matchGreaterEquals(OwSearchCriteria filter, Number value)
    {
        Number filterVal = (Number) filter.getValue();
        if (filterVal == null || value == null)
        {
            return false;
        }
        else
        {
            if (value instanceof Comparable)
            {
                return ((Comparable) value).compareTo(filterVal) >= 0;
            }
            else
            {
                return value.longValue() >= filterVal.longValue();
            }
        }
    }

    protected boolean matchLess(OwSearchCriteria filter, Number value)
    {
        Number filterVal = (Number) filter.getValue();
        if (filterVal == null || value == null)
        {
            return false;
        }
        else
        {
            if (value instanceof Comparable)
            {
                return ((Comparable) value).compareTo(filterVal) < 0;
            }
            else
            {
                return value.longValue() < filterVal.longValue();
            }
        }
    }

    protected boolean matchGreater(OwSearchCriteria filter, Number value)
    {
        Number filterVal = (Number) filter.getValue();
        if (filterVal == null || value == null)
        {
            return false;
        }
        else
        {
            if (value instanceof Comparable)
            {
                return ((Comparable) value).compareTo(filterVal) > 0;
            }
            else
            {
                return value.longValue() > filterVal.longValue();
            }
        }
    }

    protected boolean matchLike(OwSearchCriteria filter, Number value)
    {
        return false;
    }

    protected boolean matchEquals(OwSearchCriteria filter, Number value)
    {
        Number num = (Number) filter.getValue();
        if (num == null && value == null)
        {
            return true;
        }
        else
        {
            return num == null ? false : num.equals(value);
        }
    }

}
