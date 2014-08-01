package com.wewebu.ow.server.mock;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFormat;

public class OwMockPropertyClass implements OwPropertyClass
{

    private String className;
    private String javaClassName;

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getClassName()
    {
        return this.className;
    }

    public String getDisplayName(Locale locale_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDescription(Locale locale_p)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void setJavaClassName(String javaClassName)
    {
        this.javaClassName = javaClassName;
    }

    public String getJavaClassName()
    {
        return this.javaClassName;
    }

    public Object getNativeType() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isEnum() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public OwEnumCollection getEnums() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isRequired() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Object getMaxValue() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getMinValue() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getDefaultValue() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isArray() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getValueFromString(String text_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public OwFormat getFormat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getOperators() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public List getComplexChildClasses() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isComplex()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSystemProperty() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isNameProperty() throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isReadOnly(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isHidden(int iContext_p) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getCategory() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

}
