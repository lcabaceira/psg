package com.wewebu.ow.server.field;

/**
 *<p>
 * Overridden OwSearchCriteria to set specific max, min, default and native 
 * values for custom controls.
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
public class OwSearchCriteriaFieldDefOverride extends OwSearchCriteria
{
    /** max value, overrides default template behavior and do not call inherited FieldDefinition */
    private Object m_maxValue;

    /** set max value, override default template behavior and do not call inherited FieldDefinition */
    public void setMaxValue(Object value_p)
    {
        m_maxValue = value_p;
    }

    /** min value, overrides default template behavior and do not call inherited FieldDefinition */
    private Object m_minValue;

    /** set min value, override default template behavior and do not call inherited FieldDefinition */
    public void setMinValue(Object value_p)
    {
        m_minValue = value_p;
    }

    /** default value, overrides default template behavior and do not call inherited FieldDefinition */
    private Object m_defaultValue;

    /** set default value, override default template behavior and do not call inherited FieldDefinition */
    public void setDefaultValue(Object value_p)
    {
        m_defaultValue = value_p;
    }

    /** Java Class Name, overrides default template behavior and do not call inherited FieldDefinition */
    private String m_javaclassname;

    /** set java class name value, override default template behavior and do not call inherited FieldDefinition */
    public void setJavaClassName(String value_p)
    {
        m_javaclassname = value_p;
    }

    public Object getMaxValue() throws Exception
    {
        return m_maxValue;
    }

    public Object getMinValue() throws Exception
    {
        return m_minValue;
    }

    public Object getDefaultValue() throws Exception
    {
        return m_defaultValue;
    }

    public String getJavaClassName()
    {
        if (null != m_javaclassname)
        {
            return m_javaclassname;
        }
        else
        {
            return super.getJavaClassName();
        }
    }
}