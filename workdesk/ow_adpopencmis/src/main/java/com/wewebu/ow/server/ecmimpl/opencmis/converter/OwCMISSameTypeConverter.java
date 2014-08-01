package com.wewebu.ow.server.ecmimpl.opencmis.converter;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

/**
 *<p>
 * OwCMISSameTypeConverter.
 * Simple list to array converter or list to single value, and vis versa.
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
public class OwCMISSameTypeConverter<S> implements OwCMISValueConverter<S, S>
{
    private Class<S> oClass;

    public OwCMISSameTypeConverter(Class<S> oClass_p)
    {
        this.oClass = oClass_p;
    }

    public List<S> fromArrayValue(Object[] owdValue_p)
    {
        List<S> result = new LinkedList<S>();
        if (owdValue_p != null)
        {
            for (int i = 0; i < owdValue_p.length; i++)
            {
                result.add((S) owdValue_p[i]);
            }
        }

        return result;

    }

    public List<S> fromValue(S owdValue_p)
    {
        LinkedList<S> cmisList = new LinkedList<S>();
        if (owdValue_p != null)
        {
            cmisList.add(owdValue_p);
        }
        return cmisList;
    }

    public S[] toArrayValue(List<S> cmisValue_p)
    {
        return toStaticArrayValue(cmisValue_p);
    }

    public S toValue(List<S> cmisValue_p)
    {
        return toStaticValue(cmisValue_p);
    }

    @SuppressWarnings("unchecked")
    public S[] toStaticArrayValue(List<S> cmisValue_p)
    {
        if (null == cmisValue_p || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            S[] typedArray = (S[]) Array.newInstance(oClass, 0);
            return cmisValue_p.toArray(typedArray);
        }
    }

    public S toStaticValue(List<S> cmisValue_p)
    {
        if (null == cmisValue_p || cmisValue_p.isEmpty())
        {
            return null;
        }
        else
        {
            return cmisValue_p.get(0);
        }
    }

}
