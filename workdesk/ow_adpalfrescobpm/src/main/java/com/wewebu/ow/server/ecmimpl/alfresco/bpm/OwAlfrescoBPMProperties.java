package com.wewebu.ow.server.ecmimpl.alfresco.bpm;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.log.OwLog;
import com.wewebu.ow.server.ecmimpl.opencmis.propertyclass.OwCMISPropertyClass;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwServerException;

/**
 *<p>
 * Property cache for Alfresco BPM.
 * Lookup and store is done based on the property class short name.
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
 *@since 4.2.0.0
 */
@SuppressWarnings("rawtypes")
public class OwAlfrescoBPMProperties
{
    private static final Logger LOG = OwLog.getLogger(OwAlfrescoBPMProperties.class);
    // contains only short names
    protected Map<String, OwProperty> properties = new HashMap<String, OwProperty>();

    public boolean contains(OwPropertyClass propertyClass)
    {
        String propertyClassShortName = getShortPropertyClassName(propertyClass);
        return this.properties.containsKey(propertyClassShortName);
    }

    /**
     * Always the unqualified name.
     * @param propertyClass
     * @return unqualified name of the property.
     */
    private String getShortPropertyClassName(OwPropertyClass propertyClass)
    {
        String propertyClassName = null;
        if (propertyClass instanceof OwCMISPropertyClass)
        {
            propertyClassName = ((OwCMISPropertyClass) propertyClass).getNonQualifiedName();
        }
        else
        {
            propertyClassName = propertyClass.getClassName();
        }
        return propertyClassName;
    }

    /**
     * Should always return a property with the exat same class as propertyClass. 
     * @param propertyClass
     * @return a property
     */
    public OwProperty get(OwPropertyClass propertyClass)
    {
        String propertyClassShortName = getShortPropertyClassName(propertyClass);
        String propertyClassSName = propertyClass.getClassName();
        OwProperty property = this.properties.get(propertyClassShortName);
        if (!propertyClassSName.equals(propertyClassShortName))
        {
            return new OwAlfrescoBPMProxyProperty(property, propertyClass);
        }
        return property;
    }

    public void put(OwProperty property) throws OwException
    {
        try
        {
            String propertyClassName = getShortPropertyClassName(property.getPropertyClass());
            this.properties.put(propertyClassName, property);
        }
        catch (Exception e)
        {
            String message = "Could not get property class for: " + property;
            LOG.error(message, e);
            throw new OwServerException(message, e);
        }
    }

    /**
     * 
     * @return only short names. Suitable for native calls.
     */
    public Set<String> names()
    {
        return this.properties.keySet();
    }
}
