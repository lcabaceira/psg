package com.wewebu.ow.server.ecmimpl.alfresco.bpm.property.classes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwUserInfo;
import com.wewebu.ow.server.ecmimpl.alfresco.bpm.rest.beans.VariableDefinition;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * OwAlfrescoBPMPropertyClass.
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
public class OwAlfrescoBPMPropertyClass implements OwPropertyClass
{
    private VariableDefinition definition;

    public OwAlfrescoBPMPropertyClass(VariableDefinition varDef)
    {
        definition = varDef;
    }

    @Override
    public String getClassName()
    {
        return definition.getName();
    }

    @Override
    public String getDisplayName(Locale locale_p)
    {
        return definition.getTitle() != null ? definition.getTitle() : getClassName();
    }

    @Override
    public String getDescription(Locale locale_p)
    {
        return definition.getTitle();
    }

    @Override
    public String getJavaClassName()
    {
        switch (definition.getDataType())
        {
            case "d:boolean":
            {
                return Boolean.class.getCanonicalName();
            }
            case "d:int":
            {
                return Integer.class.getCanonicalName();
            }
            case "d:date":
            {
                return Date.class.getCanonicalName();
            }
            case "d:float":
            case "d:double":
            {
                return BigDecimal.class.getCanonicalName();
            }
            case "cm:person":
                return OwUserInfo.class.getCanonicalName();
            default:
                return String.class.getCanonicalName();
        }
    }

    @Override
    public Object getNativeType() throws Exception
    {
        return null;
    }

    @Override
    public boolean isEnum() throws Exception
    {
        return false;//definition.getAllowedValues() != null && !definition.getAllowedValues().isEmpty();
    }

    @Override
    public OwEnumCollection getEnums() throws Exception
    {
        return null;
    }

    @Override
    public boolean isRequired() throws Exception
    {
        return definition.isRequired();
    }

    @Override
    public Object getMaxValue() throws Exception
    {
        return null;
    }

    @Override
    public Object getMinValue() throws Exception
    {
        return null;
    }

    @Override
    public Object getDefaultValue() throws Exception
    {
        return definition.getDefaultValue();
    }

    @Override
    public boolean isArray() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getValueFromNode(Node node_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getValueFromString(String text_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public OwFormat getFormat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection getOperators() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List getComplexChildClasses() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isComplex()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isSystemProperty() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isNameProperty() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReadOnly(int iContext_p) throws Exception
    {
        switch (definition.getDataType())
        {
            case "d:boolean":
            case "d:int":
            case "d:date":
            case "d:float":
            case "d:double":
            case "d:text":
            case "cm:person":
            {
                return false;
            }
            default:
                return true;
        }
    }

    @Override
    public boolean isHidden(int iContext_p) throws Exception
    {
        //        switch(definition.getDataType())
        //        {
        //            case "d:boolean":
        //            case "d:int":
        //            case "d:date":
        //            case "d:float":
        //            case "d:double":
        //            case "d:text":
        //            {
        //                return false;
        //            }
        //            default:
        //                return true;
        //        }
        return false;
    }

    @Override
    public String getCategory() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}
