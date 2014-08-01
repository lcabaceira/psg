package com.wewebu.ow.server.fieldimpl.filter;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwWildCardDefinition;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Simple string comparator.
 * Implements the LIKE operation through regular expression transformation.
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
public class OwStringComparator extends OwAbstractComparator<String>
{
    private static final Logger LOG = OwLogCore.getLogger(OwStringComparator.class);

    @Override
    protected boolean matchBetween(OwSearchCriteria filter, String value)
    {
        return false;
    }

    @Override
    protected boolean matchLessEquals(OwSearchCriteria filter, String value)
    {
        return false;
    }

    @Override
    protected boolean matchGreaterEquals(OwSearchCriteria filter, String value)
    {
        return false;
    }

    @Override
    protected boolean matchLess(OwSearchCriteria filter, String value)
    {
        return false;
    }

    @Override
    protected boolean matchGreater(OwSearchCriteria filter, String value)
    {
        return false;
    }

    @Override
    protected boolean matchLike(OwSearchCriteria filter, String value)
    {
        if (filter.getValue() == null || value == null)
        {
            return filter.getValue() == value;
        }
        else
        {
            StringBuffer val = new StringBuffer((String) filter.getValue());
            OwString.replaceAll(val, ".", "\\.");
            if (filter.getWildCardDefinitions() != null)
            {
                Iterator it = filter.getWildCardDefinitions().iterator();
                while (it.hasNext())
                {
                    OwWildCardDefinition def = (OwWildCardDefinition) it.next();
                    if (def.getType() == OwWildCardDefinition.WILD_CARD_TYPE_MULTI_CHAR)
                    {
                        OwString.replaceAll(val, def.getWildCard(), ".*");
                    }
                    else if (def.getType() == OwWildCardDefinition.WILD_CARD_TYPE_SINGLE_CHAR)
                    {
                        OwString.replaceAll(val, def.getWildCard(), "\\w");
                    }
                }
            }

            if (val.indexOf(".*") < 0)
            {
                val.append(".*");
                val.insert(0, ".*");
            }

            if (LOG.isDebugEnabled())
            {
                StringBuffer msg = new StringBuffer("OwStringComparator.matchLike: defined filter was [");
                msg.append(filter.getValue());
                msg.append("], pattern used for filtering is [");
                msg.append(val);
                LOG.debug(msg.append("]"));
            }
            Pattern p = Pattern.compile(val.toString());
            Matcher m = p.matcher(value);

            return m.matches();
        }
    }

    @Override
    protected boolean matchEquals(OwSearchCriteria filter, String value)
    {
        String val = (String) filter.getValue();
        if (val == null && value == null)
        {
            return true;
        }
        else
        {
            if (val != null)
            {
                return val.equals(value);
            }
            else
            {
                return value.equals(val);
            }
        }
    }
}