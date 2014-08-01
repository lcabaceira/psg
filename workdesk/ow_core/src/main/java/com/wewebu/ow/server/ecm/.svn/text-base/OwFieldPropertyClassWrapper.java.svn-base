package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * Property class wrapper for OwFields.
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
public class OwFieldPropertyClassWrapper implements OwPropertyClass
{
    /** the wrapped field definition
     * 
     */
    private OwFieldDefinition m_fielddefinition;

    /** create a property class out of a field definition
     * 
     * @param fielddefinition_p
     */
    public OwFieldPropertyClassWrapper(OwFieldDefinition fielddefinition_p)
    {
        super();

        m_fielddefinition = fielddefinition_p;
    }

    /** get the displayable name of the type as defined by the ECM System
     * can be identical to getClassName
     * @return type displayable name of property
     */
    public String getDisplayName(java.util.Locale locale_p)
    {
        return m_fielddefinition.getDisplayName(locale_p);
    }

    /** get the description defined by the DMS System
     *
     * @return type description of property
     */
    public String getDescription(java.util.Locale locale_p)
    {
        return m_fielddefinition.getDescription(locale_p);
    }

    public String getCategory() throws Exception
    {
        return null;
    }

    public boolean isHidden(int context_p) throws Exception
    {
        return false;
    }

    public boolean isNameProperty() throws Exception
    {
        return false;
    }

    public boolean isReadOnly(int context_p) throws Exception
    {
        return false;
    }

    public boolean isSystemProperty() throws Exception
    {
        return false;
    }

    public String getClassName()
    {
        return m_fielddefinition.getClassName();
    }

    public Object getDefaultValue() throws Exception
    {
        return m_fielddefinition.getDefaultValue();
    }

    public OwEnumCollection getEnums() throws Exception
    {
        return m_fielddefinition.getEnums();
    }

    public OwFormat getFormat()
    {
        return m_fielddefinition.getFormat();
    }

    public String getJavaClassName()
    {
        return m_fielddefinition.getJavaClassName();
    }

    public Object getMaxValue() throws Exception
    {
        return m_fielddefinition.getMaxValue();
    }

    public Object getMinValue() throws Exception
    {
        return m_fielddefinition.getMinValue();
    }

    public Object getNativeType() throws Exception
    {
        return m_fielddefinition.getNativeType();
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        return m_fielddefinition.getNodeFromValue(value_p, doc_p);
    }

    public Collection getOperators() throws Exception
    {
        return m_fielddefinition.getOperators();
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {
        return m_fielddefinition.getValueFromNode(node_p);
    }

    public Object getValueFromString(String text_p) throws Exception
    {
        return m_fielddefinition.getValueFromString(text_p);
    }

    public boolean isArray() throws Exception
    {
        return m_fielddefinition.isArray();
    }

    public boolean isEnum() throws Exception
    {
        return m_fielddefinition.isEnum();
    }

    public boolean isRequired() throws Exception
    {
        return m_fielddefinition.isRequired();
    }

    public boolean isComplex()
    {
        return m_fielddefinition.isComplex();
    }

    public List getComplexChildClasses() throws Exception
    {
        return m_fielddefinition.getComplexChildClasses();
    }
}