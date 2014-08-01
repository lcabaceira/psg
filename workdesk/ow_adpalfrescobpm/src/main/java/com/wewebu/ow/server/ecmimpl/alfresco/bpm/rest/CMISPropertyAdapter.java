package com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest;

import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * First attempt to handle mappings between CMIS properties and Activiti variables.
 * Ex. D:bpm:task.bpm:dueDate <=> bpm_dueDate  
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
 *@since 4.2.0.0.0
 */
public class CMISPropertyAdapter
{
    public static String variableNameForProperty(OwFieldDefinition fieldDefinition)
    {
        String propClassName = fieldDefinition.getClassName();
        return variableNameForProperty(propClassName);
    }

    public static String variableNameForProperty(String propClassName)
    {
        //retry for CMIS dotted value
        String[] dotSplitPropertyName = propClassName.split("\\.");
        if (dotSplitPropertyName.length == 2)
        {
            return dotSplitPropertyName[1].replace(':', '_');
        }
        else
        {
            return propClassName.replace(':', '_');
        }
    }
}
