package com.wewebu.ow.server.field;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *<p>
 * OwFieldDefinition wrapper to implement a decorator pattern.
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
public abstract class OwStandardDecoratorFieldDefinition implements OwFieldDefinition
{
    /** get the decorated object, to be implemented be overridden classes */
    public abstract OwFieldDefinition getWrappedFieldDefinition();

    public String getClassName()
    {
        return getWrappedFieldDefinition().getClassName();
    }

    public List getComplexChildClasses() throws Exception
    {

        return getWrappedFieldDefinition().getComplexChildClasses();
    }

    public Object getDefaultValue() throws Exception
    {

        return getWrappedFieldDefinition().getDefaultValue();
    }

    public String getDescription(Locale locale_p)
    {

        return getWrappedFieldDefinition().getDescription(locale_p);
    }

    public String getDisplayName(Locale locale_p)
    {

        return getWrappedFieldDefinition().getDisplayName(locale_p);
    }

    public OwEnumCollection getEnums() throws Exception
    {

        return getWrappedFieldDefinition().getEnums();
    }

    public OwFormat getFormat()
    {

        return getWrappedFieldDefinition().getFormat();
    }

    public String getJavaClassName()
    {

        return getWrappedFieldDefinition().getJavaClassName();
    }

    public Object getMaxValue() throws Exception
    {

        return getWrappedFieldDefinition().getMaxValue();
    }

    public Object getMinValue() throws Exception
    {

        return getWrappedFieldDefinition().getMinValue();
    }

    public Object getNativeType() throws Exception
    {

        return getWrappedFieldDefinition().getNativeType();
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {

        return getWrappedFieldDefinition().getNodeFromValue(value_p, doc_p);
    }

    public Collection getOperators() throws Exception
    {

        return getWrappedFieldDefinition().getOperators();
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {

        return getWrappedFieldDefinition().getValueFromNode(node_p);
    }

    public Object getValueFromString(String text_p) throws Exception
    {

        return getWrappedFieldDefinition().getValueFromString(text_p);
    }

    public boolean isArray() throws Exception
    {

        return getWrappedFieldDefinition().isArray();
    }

    public boolean isComplex()
    {

        return getWrappedFieldDefinition().isComplex();
    }

    public boolean isEnum() throws Exception
    {

        return getWrappedFieldDefinition().isEnum();
    }

    public boolean isRequired() throws Exception
    {

        return getWrappedFieldDefinition().isRequired();
    }

}