package com.wewebu.ow.server.ecmimpl.fncm5.bpm.propertyclasses;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecmimpl.fncm5.bpm.OwFNBPM5WorkItemObjectClass;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.util.OwString;

import filenet.vw.api.VWFieldDefinition;
import filenet.vw.api.VWFieldType;

/**
 *<p>
 * Property Class of Workflow definitions.
 * This class is is used for caches of the property definitions
 * of Workflow definitions.
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
public class OwFNBPM5WorkflowPropertyClass implements OwPropertyClass
{
    private boolean isArray;
    private boolean editable;
    private boolean required;
    private OwPropertyTypeMapping type;
    private String name;
    private String defaultDescription;
    private OwFieldDefinition propClassCE;
    private Collection<Integer> operators;

    /**
     * Constructor which creates an instance with a default
     * property type mapping.
     * @param def_p VWFieldDefinition from WorkflowDefinition
     */
    public OwFNBPM5WorkflowPropertyClass(VWFieldDefinition def_p, OwFieldDefinition owdef_p)
    {
        this(def_p, owdef_p, OwPropertyTypeMapping.getRepresentationType(def_p.getFieldType()));
    }

    /**
     * Constructor to create an instance with an own property type mapping
     * @param def_p VWFieldDefinition from WorkflowDefinition
     * @param type_p OwPropertyTypeMapping for this field definition
     */
    public OwFNBPM5WorkflowPropertyClass(VWFieldDefinition def_p, OwFieldDefinition owdef_p, OwPropertyTypeMapping type_p)
    {
        type = type_p;
        required = VWFieldType.FIELD_TYPE_BOOLEAN == def_p.getFieldType();
        isArray = def_p.isArray();
        name = def_p.getName();
        editable = def_p.getCanAssign();
        this.defaultDescription = def_p.getDescription();
        propClassCE = owdef_p;
    }

    public String getCategory() throws Exception
    {
        return null;
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean isNameProperty() throws Exception
    {
        return OwFNBPM5WorkItemObjectClass.NAME_PROPERTY.equals(getClassName());
    }

    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return editable;
    }

    public boolean isSystemProperty() throws Exception
    {
        return !editable;
    }

    public String getClassName()
    {
        return this.name;
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
        return OwString.localize(locale_p, OwString.LABEL_PREFIX + getClassName() + ".description", defaultDescription);
    }

    public String getDisplayName(Locale locale_p)
    {
        return OwString.localize(locale_p, OwString.LABEL_PREFIX + getClassName(), getClassName());
    }

    public OwEnumCollection getEnums() throws Exception
    {
        return propClassCE != null ? propClassCE.getEnums() : null;
    }

    public OwFormat getFormat()
    {
        return null;
    }

    public String getJavaClassName()
    {
        return this.type.getJavaClassName();
    }

    public Object getMaxValue() throws Exception
    {
        return propClassCE != null ? propClassCE.getMaxValue() : null;
    }

    public Object getMinValue() throws Exception
    {
        return propClassCE != null ? propClassCE.getMinValue() : null;
    }

    public Object getNativeType() throws Exception
    {
        return this.type.getType();
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        return null;
    }

    public Collection<Integer> getOperators() throws Exception
    {
        if (this.operators == null)
        {
            this.operators = OwOperatorHandler.getMatchingOperator(getJavaClassName());
        }
        return this.operators;
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
        return isArray;
    }

    public boolean isComplex()
    {
        return false;
    }

    public boolean isEnum() throws Exception
    {
        return propClassCE != null ? propClassCE.isEnum() : false;
    }

    public boolean isRequired() throws Exception
    {
        return required;
    }

}