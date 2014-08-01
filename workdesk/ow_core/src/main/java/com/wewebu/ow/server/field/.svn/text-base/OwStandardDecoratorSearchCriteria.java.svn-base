package com.wewebu.ow.server.field;

import java.text.Format;
import java.util.Collection;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *<p>
 * OwSearchCriteria wrapper to implement a decorator pattern.
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
public abstract class OwStandardDecoratorSearchCriteria extends OwSearchCriteria
{
    /** get the decorated object, to be implemented be overridden classes */
    public abstract OwSearchCriteria getWrappedCriteria();

    public boolean canWildCard()
    {

        return getWrappedCriteria().canWildCard();
    }

    public int getAttributes()
    {

        return getWrappedCriteria().getAttributes();
    }

    public String getClassName()
    {

        return getWrappedCriteria().getClassName();
    }

    public Object getDefaultValue() throws Exception
    {

        return getWrappedCriteria().getDefaultValue();
    }

    public String getDescription(Locale locale_p)
    {

        return getWrappedCriteria().getDescription(locale_p);
    }

    public String getDisplayName(Locale locale_p)
    {

        return getWrappedCriteria().getDisplayName(locale_p);
    }

    public OwEnumCollection getEnums() throws Exception
    {

        return getWrappedCriteria().getEnums();
    }

    public OwFieldDefinition getFieldDefinition() throws Exception
    {

        return getWrappedCriteria().getFieldDefinition();
    }

    public OwFormat getFormat()
    {

        return getWrappedCriteria().getFormat();
    }

    public String getInstruction()
    {

        return getWrappedCriteria().getInstruction();
    }

    public String getJavaClassName()
    {

        return getWrappedCriteria().getJavaClassName();
    }

    public Object getMaxValue() throws Exception
    {

        return getWrappedCriteria().getMaxValue();
    }

    public Object getMinValue() throws Exception
    {

        return getWrappedCriteria().getMinValue();
    }

    public Object getNativeType() throws Exception
    {

        return getWrappedCriteria().getNativeType();
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {

        return getWrappedCriteria().getNodeFromValue(value_p, doc_p);
    }

    public int getOperator()
    {

        return getWrappedCriteria().getOperator();
    }

    public String getOperatorDisplayName(Locale locale_p)
    {

        return getWrappedCriteria().getOperatorDisplayName(locale_p);
    }

    public Collection getOperators() throws Exception
    {

        return getWrappedCriteria().getOperators();
    }

    public String getOriginalJavaClassName()
    {

        return getWrappedCriteria().getOriginalJavaClassName();
    }

    public OwSearchCriteria getSecondRangeCriteria()
    {

        return getWrappedCriteria().getSecondRangeCriteria();
    }

    public Format getTextFormat(int fieldProviderType_p)
    {

        return getWrappedCriteria().getTextFormat(fieldProviderType_p);
    }

    public String getUniqueName()
    {

        return getWrappedCriteria().getUniqueName();
    }

    public Object getValue()
    {

        return getWrappedCriteria().getValue();
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {

        return getWrappedCriteria().getValueFromNode(node_p);
    }

    public Object getValueFromString(String text_p) throws Exception
    {

        return getWrappedCriteria().getValueFromString(text_p);
    }

    public Collection getWildCardDefinitions()
    {

        return getWrappedCriteria().getWildCardDefinitions();
    }

    public boolean ignoreTime()
    {

        return getWrappedCriteria().ignoreTime();
    }

    public boolean isAllowWildcard()
    {

        return getWrappedCriteria().isAllowWildcard();
    }

    public boolean isArray() throws Exception
    {

        return getWrappedCriteria().isArray();
    }

    public boolean isCriteriaOperatorRange()
    {

        return getWrappedCriteria().isCriteriaOperatorRange();
    }

    public boolean isDateType()
    {

        return getWrappedCriteria().isDateType();
    }

    public boolean isEnum() throws Exception
    {

        return getWrappedCriteria().isEnum();
    }

    public boolean isHidden()
    {

        return getWrappedCriteria().isHidden();
    }

    public boolean isIgnoreTime()
    {

        return getWrappedCriteria().isIgnoreTime();
    }

    public boolean isReadonly()
    {

        return getWrappedCriteria().isReadonly();
    }

    public boolean isRequired()
    {

        return getWrappedCriteria().isRequired();
    }

    public boolean isType(Class base_p) throws ClassNotFoundException
    {

        return getWrappedCriteria().isType(base_p);
    }

    public void setInitialAndDefaultValue(Object value_p)
    {

        getWrappedCriteria().setInitialAndDefaultValue(value_p);
    }

    public void setOperator(int op_p)
    {

        getWrappedCriteria().setOperator(op_p);
    }

    public void setValue(Object value_p)
    {

        getWrappedCriteria().setValue(value_p);
    }

    public void setWildCardDefinitions(Collection wildcarddefinitions_p)
    {

        getWrappedCriteria().setWildCardDefinitions(wildcarddefinitions_p);
    }

}