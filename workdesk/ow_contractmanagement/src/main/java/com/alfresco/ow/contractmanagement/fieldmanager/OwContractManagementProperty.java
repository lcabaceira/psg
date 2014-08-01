package com.alfresco.ow.contractmanagement.fieldmanager;

import org.apache.log4j.Logger;

import com.alfresco.ow.contractmanagement.log.OwLog;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.field.OwFieldDefinition;

/**
 *<p>
 * OwContractManagementProperty.
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
public class OwContractManagementProperty implements OwProperty, Cloneable
{

    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwContractManagementProperty.class);
    private OwProperty m_wrappedProperty;
    private OwContractManagementFieldDefinition m_fieldDefinition;

    public OwContractManagementProperty(OwProperty wrappedProperty_p, String objectSubClassName_p)
    {
        m_wrappedProperty = wrappedProperty_p;
        try
        {
            m_fieldDefinition = new OwContractManagementFieldDefinition(wrappedProperty_p.getFieldDefinition(), objectSubClassName_p);
        }
        catch (Exception e)
        {
            LOG.warn("OwContractManagementProperty: error retrive field definition.", e);
        }

    }

    /**
     * Returns the value of the wrapped property
     */
    public Object getValue() throws Exception
    {
        return m_wrappedProperty.getValue();
    }

    /**
     * Returns the property class of the wrapped property
     */
    public OwPropertyClass getPropertyClass() throws Exception
    {
        return m_wrappedProperty.getPropertyClass();
    }

    /**
     * Sets the value of the wrapped property
     */
    public void setValue(Object oValue_p) throws Exception
    {
        m_wrappedProperty.setValue(oValue_p);
    }

    /**
     * Returns if of the wrapped property is read-only
     */
    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return m_wrappedProperty.isReadOnly(iContext_p);
    }

    /**
     * Returns the field definition
     */
    public OwFieldDefinition getFieldDefinition() throws Exception
    {
        if (m_fieldDefinition != null)
        {
            return m_fieldDefinition;
        }

        return m_wrappedProperty.getFieldDefinition();
    }

    /**
     * Returns if of the wrapped property is hidden
     */
    public boolean isHidden(int iContext_p) throws Exception
    {
        return m_wrappedProperty.isHidden(iContext_p);
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return m_wrappedProperty.clone();
    }

    /**
     * Returns the native object of the wrapped property
     */
    public Object getNativeObject() throws Exception
    {
        return m_wrappedProperty.getNativeObject();
    }

    /**
     * Compares the wrapped property to the given object
     */
    public int compareTo(Object o)
    {
        return m_wrappedProperty.compareTo(o);
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((m_wrappedProperty == null) ? 0 : m_wrappedProperty.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        return m_wrappedProperty.equals(obj);
    }
}
