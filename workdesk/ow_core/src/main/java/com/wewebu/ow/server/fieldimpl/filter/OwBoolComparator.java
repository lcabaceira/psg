package com.wewebu.ow.server.fieldimpl.filter;

import com.wewebu.ow.server.field.OwSearchCriteria;

/**
 *<p>
 * Simple boolean comparator.
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
public class OwBoolComparator extends OwAbstractComparator<Boolean>
{
    protected boolean matchBetween(OwSearchCriteria filter, Boolean value)
    {
        return false;
    }

    protected boolean matchLessEquals(OwSearchCriteria filter, Boolean value)
    {
        return false;
    }

    protected boolean matchGreaterEquals(OwSearchCriteria filter, Boolean value)
    {
        return false;
    }

    protected boolean matchLess(OwSearchCriteria filter, Boolean value)
    {
        return false;
    }

    protected boolean matchGreater(OwSearchCriteria filter, Boolean value)
    {
        return false;
    }

    protected boolean matchLike(OwSearchCriteria filter, Boolean value)
    {
        return false;
    }

    protected boolean matchEquals(OwSearchCriteria filter, Boolean value)
    {
        Object val = filter.getValue();
        if (val == null && value == null)
        {
            return true;
        }
        else
        {
            if (val == null)
            {
                return value.equals(val);
            }
            else
            {
                return val.equals(value);
            }
        }
    }

}