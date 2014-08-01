package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * Property Class for runtime.
 * This class handles the existing property class definition of the workflow
 * at runtime, because the visibility and edit-ability of a property can change from
 * step to step of a workflow.
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
public class OwFNBPM5RuntimePropertyClass implements OwPropertyClass, OwFNBPM5WorkItemPropertyClass
{
    public static final int READONLY = 0x0001;
    public static final int HIDDEN = 0x0002;
    public static final int SYSTEM_PROP = 0x0004;
    public static final int FILTERABLE = 0x0008;
    public static final int QUEUE_PROP = 0x0010;

    private int flags;
    private OwPropertyClass propClassPE;

    public OwFNBPM5RuntimePropertyClass(OwPropertyClass propClass_p, boolean readOnly_p, boolean hidden_p, boolean isSystemProp_p, boolean isQueueParam_p, boolean isFilterable_p)
    {
        propClassPE = propClass_p;
        if (readOnly_p)
        {
            flags += READONLY;
        }
        if (hidden_p)
        {
            flags += HIDDEN;
        }
        if (isSystemProp_p)
        {
            flags += SYSTEM_PROP;
        }
        if (isQueueParam_p)
        {
            flags += QUEUE_PROP;
        }
        if (isFilterable_p)
        {
            flags += FILTERABLE;
        }
    }

    public String getCategory() throws Exception
    {
        return null;
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        return (flags & HIDDEN) != 0;
    }

    public boolean isNameProperty() throws Exception
    {
        return propClassPE.isNameProperty();
    }

    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return (flags & READONLY) != 0;
    }

    public boolean isSystemProperty() throws Exception
    {
        return propClassPE.isSystemProperty();
    }

    public String getClassName()
    {
        return propClassPE.getClassName();
    }

    public List<?> getComplexChildClasses() throws Exception
    {
        return null;
    }

    public Object getDefaultValue() throws Exception
    {
        return null;
    }

    public String getDescription(Locale locale_p)
    {
        return propClassPE.getDescription(locale_p);
    }

    public String getDisplayName(Locale locale_p)
    {
        return propClassPE.getDisplayName(locale_p);
    }

    public OwEnumCollection getEnums() throws Exception
    {
        return propClassPE.getEnums();
    }

    public OwFormat getFormat()
    {
        return propClassPE.getFormat();
    }

    public String getJavaClassName()
    {
        return propClassPE.getJavaClassName();
    }

    public Object getMaxValue() throws Exception
    {
        return propClassPE.getMaxValue();
    }

    public Object getMinValue() throws Exception
    {
        return propClassPE.getMinValue();
    }

    public Object getNativeType() throws Exception
    {
        //Return the VWFieldType as Integer instance
        return propClassPE.getNativeType();
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        return null;
    }

    public Collection<?> getOperators() throws Exception
    {
        return propClassPE.getOperators();
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {
        return null;
    }

    public Object getValueFromString(String text_p) throws Exception
    {
        return null;
    }

    public boolean isArray() throws Exception
    {
        return propClassPE.isArray();
    }

    public boolean isComplex()
    {
        return propClassPE.isComplex();
    }

    public boolean isEnum() throws Exception
    {
        return propClassPE.isEnum();
    }

    public boolean isRequired() throws Exception
    {
        return propClassPE.isRequired();
    }

    public boolean isFilterField()
    {
        return (flags & FILTERABLE) != 0;
    }

    public boolean isQueueDefinitionField()
    {
        return (flags & QUEUE_PROP) != 0;
    }
}