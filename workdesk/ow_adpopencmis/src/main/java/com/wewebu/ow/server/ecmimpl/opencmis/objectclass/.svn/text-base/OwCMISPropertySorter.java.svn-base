package com.wewebu.ow.server.ecmimpl.opencmis.objectclass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISNativePropertyClass;
import com.wewebu.ow.server.exceptions.OwException;

/**
 *<p>
 * Knows how to sort property classes according to the configuration in &lt;PreferedPropertyOrder&gt;.
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
public class OwCMISPropertySorter
{
    List<String> preferredPropertyOrder;

    public OwCMISPropertySorter(List<String> preferredPropertyOrder)
    {
        this.preferredPropertyOrder = preferredPropertyOrder;
    }

    public Map<String, OwCMISNativePropertyClass<?, ?, ?>> reorderProperties(Map<String, OwCMISNativePropertyClass<?, ?, ?>> allProperties_p) throws OwException
    {
        Map<String, OwCMISNativePropertyClass<?, ?, ?>> orderedProperties = new LinkedHashMap<String, OwCMISNativePropertyClass<?, ?, ?>>();
        for (String temp : preferredPropertyOrder)
        {
            OwCMISNativePropertyClass<?, ?, ?> propertyClass = allProperties_p.get(temp);
            if (propertyClass != null)
            {
                orderedProperties.put(temp, propertyClass);
            }
            else
            {
                for (Map.Entry<String, OwCMISNativePropertyClass<?, ?, ?>> entry : allProperties_p.entrySet())
                {
                    propertyClass = entry.getValue();

                    if (propertyClass.getClassName().equalsIgnoreCase(temp) || propertyClass.getNonQualifiedName().equalsIgnoreCase(temp))
                    {
                        orderedProperties.put(entry.getKey(), propertyClass);
                        break;
                    }
                }
            }
        }

        orderedProperties.putAll(allProperties_p);
        return orderedProperties;
    }
}
