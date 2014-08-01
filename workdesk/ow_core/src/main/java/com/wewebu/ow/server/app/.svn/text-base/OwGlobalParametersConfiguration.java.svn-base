package com.wewebu.ow.server.app;

import java.util.List;

import org.alfresco.wd.ui.conf.OwUniformParamConfiguration;

import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * simple Helper to work with GlobalParameters configuration. 
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
public class OwGlobalParametersConfiguration
{
    private OwXMLUtil root;

    public OwGlobalParametersConfiguration(OwXMLUtil root)
    {
        this.root = root;
    }

    /**
     * Return a boolean value for requested parameter.
     * @param paramName String name of parameter
     * @param defaultValue boolean default value
     * @return boolean value from configuration if existing, else defaultValue
     */
    public boolean getSafeBoolean(String paramName, boolean defaultValue)
    {
        OwXMLUtil util = getXMLUtil(paramName);
        if (util != null)
        {
            String value = util.getSafeTextValue(null);
            if (value != null)
            {
                return Boolean.parseBoolean(value);
            }
        }
        return defaultValue;
    }

    /**
     * Return a boolean value for requested parameter.
     * @param paramName String name of parameter
     * @param defaultValue integer default value
     * @return integer value from configuration if existing, else defaultValue
     */
    public int getSafeInteger(String paramName, int defaultValue)
    {
        OwXMLUtil util = getXMLUtil(paramName);
        if (util != null)
        {
            String value = util.getSafeTextValue(null);
            if (value != null)
            {
                try
                {
                    return Integer.parseInt(value);
                }
                catch (NumberFormatException nfex)
                {
                }
            }
        }
        return defaultValue;
    }

    /**
     * Return a boolean value for requested parameter.
     * @param paramName String name of parameter
     * @param defaultValue String default value
     * @return String value from configuration if existing, else defaultValue
     */
    public String getSafeString(String paramName, String defaultValue)
    {
        OwXMLUtil util = getXMLUtil(paramName);
        if (util != null)
        {
            return util.getSafeTextValue(defaultValue);
        }
        else
        {
            return defaultValue;
        }
    }

    /**
     * Get the OwXMLUtil which is has the parameter name
     * @param paramName String
     * @return OwXMLUtil or null
     */
    @SuppressWarnings("unchecked")
    protected OwXMLUtil getXMLUtil(String paramName)
    {
        if (root != null)
        {
            List<OwXMLUtil> utils = root.getSafeUtilList(null);
            for (OwXMLUtil util : utils)
            {
                if (OwUniformParamConfiguration.ELEM_PARAM.equals(util.getName()))
                {
                    String name = util.getSafeStringAttributeValue(OwUniformParamConfiguration.ATT_NAME, null);
                    if (name != null && name.equals(paramName))
                    {
                        return util;
                    }
                }
            }
        }
        return null;
    }
}
