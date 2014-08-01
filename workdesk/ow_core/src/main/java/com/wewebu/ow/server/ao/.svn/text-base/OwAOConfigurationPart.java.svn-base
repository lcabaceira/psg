package com.wewebu.ow.server.ao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wewebu.ow.server.ecmimpl.OwAOConstants;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 *Application objects configuration component abstraction.
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
public abstract class OwAOConfigurationPart
{
    private OwXMLUtil configuration;
    private Map<String, String> parameters = new HashMap<String, String>();

    public OwAOConfigurationPart(OwXMLUtil configuration) throws OwConfigurationException
    {
        this.configuration = configuration;

        try
        {
            OwXMLUtil params = configuration.getSubUtil(OwAOConstants.CONFIGNODE_PARAMS);
            if (params != null)
            {
                List<OwXMLUtil> paramList = params.getSafeUtilList(OwAOConstants.CONFIGNODE_PARAM);
                for (OwXMLUtil param : paramList)
                {
                    String name = param.getSafeStringAttributeValue(OwAOConstants.CONFIGATTRIBUTE_NAME, null);
                    String value = param.getSafeStringAttributeValue(OwAOConstants.CONFIGATTRIBUTE_VALUE, null);
                    parameters.put(name, value);
                }
            }
        }
        catch (Exception e)
        {
            throw new OwConfigurationException("Invalid configuration.", e);
        }

    }

    public String getParameterValue(String name) throws OwConfigurationException
    {
        return getParameterValue(name, null);
    }

    public String getParameterValue(String name, String defaultValue) throws OwConfigurationException
    {
        if (defaultValue == null && !parameters.containsKey(name))
        {
            throw new OwConfigurationException("Missing required configuration parameter " + name);
        }

        String value = parameters.get(name);
        return value == null ? defaultValue : value;
    }

    protected Class<?> safeClassFromName(String name, Class<?> superClass) throws OwConfigurationException
    {
        try
        {
            Class<?> theClass = Class.forName(name);
            if (!superClass.isAssignableFrom(theClass))
            {
                throw new OwConfigurationException("Invalid class " + name + ". A " + superClass + " implementation was expected.");
            }
            return theClass;
        }
        catch (ClassNotFoundException e)
        {
            throw new OwConfigurationException("Invalid class " + name, e);
        }
    }

    protected <I> I safeInstanceFromName(String name, Class<I> instanceClass) throws OwConfigurationException
    {
        Class<?> theClass = safeClassFromName(name, instanceClass);
        try
        {
            return (I) theClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new OwConfigurationException("Invalid class " + name, e);
        }
        catch (IllegalAccessException e)
        {
            throw new OwConfigurationException("Invalid class " + name, e);
        }
    }

}
