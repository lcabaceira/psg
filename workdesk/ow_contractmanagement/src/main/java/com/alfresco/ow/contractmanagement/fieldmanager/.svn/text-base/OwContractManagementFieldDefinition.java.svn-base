package com.alfresco.ow.contractmanagement.fieldmanager;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * OwContractManagementFieldDefinition.
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
public class OwContractManagementFieldDefinition implements OwFieldDefinition
{
    private OwFieldDefinition m_wrappedFieldDefinition;
    private String m_propertySubClassName = null;

    public OwContractManagementFieldDefinition(OwFieldDefinition wrapppedFieldDefinition_p, String objectSubClassName_p)
    {
        m_wrappedFieldDefinition = wrapppedFieldDefinition_p;
        if (objectSubClassName_p != null)
        {
            int index = wrapppedFieldDefinition_p.getClassName().indexOf(".");
            if (index != -1)
            {
                m_propertySubClassName = objectSubClassName_p + wrapppedFieldDefinition_p.getClassName().substring(index);
            }
        }
    }

    /**
     * Returns the sub class name 
     */
    public String getSubClassName()
    {
        return m_propertySubClassName;
    }

    /**
     * Returns the class name of the wrapped field definition
     */
    public String getClassName()
    {
        return m_wrappedFieldDefinition.getClassName();
    }

    /**
     * Returns the display name of the wrapped field definition
     */
    public String getDisplayName(Locale locale_p)
    {
        return m_wrappedFieldDefinition.getDisplayName(locale_p);
    }

    /**
     * Returns the description of the wrapped field definition
     */
    public String getDescription(Locale locale_p)
    {
        return m_wrappedFieldDefinition.getDescription(locale_p);
    }

    /**
     * Returns the java class name of the wrapped field definition
     */
    public String getJavaClassName()
    {
        return m_wrappedFieldDefinition.getJavaClassName();
    }

    /**
     * Returns the native type of the wrapped field definition
     */
    public Object getNativeType() throws Exception
    {
        return m_wrappedFieldDefinition.getNativeType();
    }

    /**
     * Checks if the wrapped field definition is enum
     */
    public boolean isEnum() throws Exception
    {
        return m_wrappedFieldDefinition.isEnum();
    }

    /**
     * Returns the enums from the wrapped field definition
     */
    public OwEnumCollection getEnums() throws Exception
    {
        return m_wrappedFieldDefinition.getEnums();
    }

    /**
     * Checks if the wrapped field definition is required
     */
    public boolean isRequired() throws Exception
    {
        return m_wrappedFieldDefinition.isRequired();
    }

    /**
     * Returns the max value of the wrapped field definition
     */
    public Object getMaxValue() throws Exception
    {
        return m_wrappedFieldDefinition.getMaxValue();
    }

    /**
     * Returns the min value of the wrapped field definition
     */
    public Object getMinValue() throws Exception
    {
        return m_wrappedFieldDefinition.getMinValue();
    }

    /**
     * Returns the default vaue of the wrapped field definition
     */
    public Object getDefaultValue() throws Exception
    {
        return m_wrappedFieldDefinition.getDefaultValue();
    }

    /**
     * Checks if the wrapped field definition is an array
     */
    public boolean isArray() throws Exception
    {
        return m_wrappedFieldDefinition.isArray();
    }

    /**
     * Returns a value from the node by the wrapped field definition
     */
    public Object getValueFromNode(Node node_p) throws Exception
    {
        return m_wrappedFieldDefinition.getValueFromNode(node_p);
    }

    /**
     * Returns a value from a string by the wrapped field definition
     */
    public Object getValueFromString(String text_p) throws Exception
    {
        return m_wrappedFieldDefinition.getValueFromString(text_p);
    }

    /**
     * Returns a node from a value by the wrapped field definition
     */
    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        return m_wrappedFieldDefinition.getNodeFromValue(value_p, doc_p);
    }

    /**
     * Returns the format from the wrapped field definition
     */
    public OwFormat getFormat()
    {
        return m_wrappedFieldDefinition.getFormat();
    }

    /**
     * Returns operator from the wrapped field definition
     */
    public Collection getOperators() throws Exception
    {
        return m_wrappedFieldDefinition.getOperators();
    }

    /**
     * Returns complex child classed from the wrapped field definition
     */
    public List getComplexChildClasses() throws Exception
    {
        return m_wrappedFieldDefinition.getComplexChildClasses();
    }

    /**
     * Checks if  the wrapped field definition is a complex type 
     */
    public boolean isComplex()
    {
        return m_wrappedFieldDefinition.isComplex();
    }
}
