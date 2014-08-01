package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue;
import com.wewebu.ow.server.history.OwStandardHistoryModifiedPropertyValue;

/**
 *<p>
 * Implementation of OwHistoryModifiedPropertyValue for FileNet BPM History Entry.
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
public class OwFNBPM5HistoryModifiedPropertyValue implements OwHistoryModifiedPropertyValue
{

    /**
     *<p>
     * OwModifiedField.
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
    public static class OwModifiedField implements OwField
    {

        private OwFieldDefinition m_fielddefinition;
        private Object m_value;

        public OwModifiedField(Object value_p, OwFieldDefinition fielddefinition_p)
        {
            m_fielddefinition = fielddefinition_p;
            m_value = value_p;
        }

        public OwFieldDefinition getFieldDefinition() throws Exception
        {
            return m_fielddefinition;
        }

        public Object getValue() throws Exception
        {
            return m_value;
        }

        public void setValue(Object value_p) throws Exception
        {
            m_value = value_p;
        }

    }

    /** field definition provider to resolve the values */
    private OwFieldDefinitionProvider m_fielddefprovider;
    private String m_className;
    private Object m_oldValue;
    private Object m_newValue;
    private String m_resource;

    private OwFieldDefinition m_fielddefinition;

    /** construct a modified property value
     * 
     * @param className_p String
     * @param isArray_p 
     * @param oldValue_p String
     * @param newValue_p String
     * @param fielddefprovider_p OwFieldDefinitionProvider field definition provider to resolve the values
     * @param resource_p String name of the resource to look up field definitions
     */
    public OwFNBPM5HistoryModifiedPropertyValue(String className_p, boolean isArray_p, Object oldValue_p, Object newValue_p, OwFieldDefinitionProvider fielddefprovider_p, String resource_p)
    {
        m_className = className_p;

        m_oldValue = oldValue_p;
        m_newValue = newValue_p;

        m_fielddefprovider = fielddefprovider_p;
        m_resource = resource_p;
    }

    /** get the modified property class name
     * 
     * @return String classname of property
     */
    public String getClassName()
    {
        return m_className;
    }

    /** get the modified property field definition
     * 
     * @return OwFieldDefinition field definition of the property
     * @throws Exception 
     * @throws OwObjectNotFoundException 
     */
    public OwFieldDefinition getFieldDefinition() throws OwObjectNotFoundException, Exception
    {
        if (null == m_fielddefinition)
        {
            if (null == m_fielddefprovider)
            {
                throw new OwObjectNotFoundException("OwFNBPMHistoryModifiedPropertyValue.getFieldDefinition: Missing fielddefprovider.");
            }

            m_fielddefinition = m_fielddefprovider.getFieldDefinition(getClassName(), m_resource);
        }

        return m_fielddefinition;
    }

    /** try to get the old value before the modification happens
     * 
     * @return an {@link OwField}
     * @throws OwObjectNotFoundException if value could not be resolved
     */
    public OwField getOldValue() throws OwObjectNotFoundException, Exception
    {
        OwFieldDefinition fielddefinition = getFieldDefinition();
        return new OwModifiedField(m_oldValue, fielddefinition);
    }

    /** try to get the new value before the modification happens
     * 
     * @return an {@link OwField}
     * @throws OwObjectNotFoundException if value could not be resolved
     */
    public OwField getNewValue() throws OwObjectNotFoundException, Exception
    {
        OwFieldDefinition fielddefinition = getFieldDefinition();
        return new OwModifiedField(m_newValue, fielddefinition);
    }

    /** get a string representation of the old value
     * 
     * @return String
     */
    public String getOldValueString()
    {
        try
        {
            return m_oldValue.toString();
        }
        catch (NullPointerException e)
        {
            return OwStandardHistoryModifiedPropertyValue.NULL_DISPLAY_VALUE;
        }
    }

    /** get a string representation of the new value
     * 
     * @return String
     */
    public String getNewValueString()
    {
        try
        {
            return m_newValue.toString();
        }
        catch (NullPointerException e)
        {
            return OwStandardHistoryModifiedPropertyValue.NULL_DISPLAY_VALUE;
        }
    }

    public String toString()
    {
        StringBuffer ret = new StringBuffer();

        // we return only the members that we have already, everthing else takes to much runtime.
        // if user wants to see detaild field information he must invoke a fieldmanagercontrol or a plugin to display detailed information
        ret.append(getClassName());
        ret.append(": ");

        ret.append(getOldValueString());

        ret.append(" > ");

        ret.append(getNewValueString());

        return ret.toString();
    }
}
