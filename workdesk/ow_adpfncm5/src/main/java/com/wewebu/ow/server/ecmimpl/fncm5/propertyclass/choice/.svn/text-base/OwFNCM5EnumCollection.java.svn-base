package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass.choice;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import com.filenet.api.admin.Choice;
import com.filenet.api.collection.ChoiceList;
import com.wewebu.ow.server.field.OwEnum;
import com.wewebu.ow.server.field.OwEnumCollection;

/**
 *<p>
 * Specific P8 5 OwEnumCollection implementation.
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
@SuppressWarnings("rawtypes")
public class OwFNCM5EnumCollection extends LinkedList implements OwEnumCollection
{
    /** generated serial version UID*/
    private static final long serialVersionUID = 7850699055166334694L;

    public OwFNCM5EnumCollection(com.filenet.api.admin.ChoiceList nativeList)
    {
        super();
        init(nativeList.get_ChoiceValues(), nativeList.get_Name());
    }

    public OwFNCM5EnumCollection(ChoiceList nativeList, String prefix)
    {
        super();
        init(nativeList, prefix);
    }

    @SuppressWarnings("unchecked")
    protected void init(ChoiceList nativeList, String prefix)
    {
        Iterator it = nativeList.iterator();
        while (it.hasNext())
        {
            Choice c = (Choice) it.next();
            add(createEnum(c, prefix + "."));
        }
    }

    public String getDisplayName(Locale local_p, Object object_p)
    {
        if (object_p instanceof OwEnum)
        {
            return ((OwEnum) object_p).getDisplayName(local_p);
        }
        else
        {
            Iterator it = iterator();
            while (it.hasNext())
            {
                OwEnum part = (OwEnum) it.next();
                if (part.getValue() == object_p || part.getValue().equals(object_p))
                {
                    return part.getDisplayName(local_p);
                }
            }
        }
        return null;
    }

    protected OwFNCM5Enum createEnum(Choice c, String prefix)
    {
        return new OwFNCM5Enum(c, prefix);
    }

}
