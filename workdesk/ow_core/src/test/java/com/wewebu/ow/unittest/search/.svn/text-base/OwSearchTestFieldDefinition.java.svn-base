package com.wewebu.ow.unittest.search;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFormat;

/**
 *<p>
 * OwSearchTestFieldDefinition. 
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
public class OwSearchTestFieldDefinition implements OwFieldDefinition
{

    private static final Logger LOG = Logger.getLogger(OwSearchTestFieldDefinition.class);

    private static final String VALUE_TYPE_STRING = "S";
    private static final String VALUE_TYPE_DOUBLE = "D";
    private static final String VALUE_TYPE_LONG = "L";
    private static final String VALUE_TYPE_DATETIME = "DT";

    private String m_className;
    private String m_javaClassName;
    private boolean m_array = false;

    public OwSearchTestFieldDefinition(String name_p, Class<?> javaClass_p)
    {
        this(name_p, javaClass_p, false);
    }

    public OwSearchTestFieldDefinition(String name_p, Class<?> javaClass_p, boolean array_p)
    {
        super();
        m_className = name_p;
        m_javaClassName = javaClass_p.getName();
        m_array = array_p;
    }

    public String getClassName()
    {
        return m_className;
    }

    public final void setClassName(String className_p)
    {
        this.m_className = className_p;
    }

    public List getComplexChildClasses() throws Exception
    {

        return null;
    }

    public Object getDefaultValue() throws Exception
    {

        return null;
    }

    public String getDescription(Locale locale_p)
    {

        return null;
    }

    public String getDisplayName(Locale locale_p)
    {

        return null;
    }

    public OwEnumCollection getEnums() throws Exception
    {

        return null;
    }

    public OwFormat getFormat()
    {

        return null;
    }

    public String getJavaClassName()
    {
        return m_javaClassName;
    }

    public final void setJavaClassName(String javaClassName_p)
    {
        this.m_javaClassName = javaClassName_p;
    }

    public Object getMaxValue() throws Exception
    {

        return null;
    }

    public Object getMinValue() throws Exception
    {

        return null;
    }

    public Object getNativeType() throws Exception
    {

        return null;
    }

    public Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {

        return null;
    }

    public Collection getOperators() throws Exception
    {

        return null;
    }

    protected Object[] convertValues(String type_p, List<String> stringValues_p) throws OwInvalidOperationException, ParseException
    {
        Object[] valuesArray = null;
        if (type_p.equals(VALUE_TYPE_STRING))
        {
            valuesArray = new String[stringValues_p.size()];
        }
        else if (type_p.equals(VALUE_TYPE_LONG))
        {
            valuesArray = new Long[stringValues_p.size()];
        }
        else if (type_p.equals(VALUE_TYPE_DOUBLE))
        {
            valuesArray = new Double[stringValues_p.size()];
        }
        else if (type_p.equals(VALUE_TYPE_DATETIME))
        {
            valuesArray = new Date[stringValues_p.size()];
        }
        else
        {
            throw new OwInvalidOperationException("Unknown value type : " + type_p);
        }

        for (int i = 0; i < stringValues_p.size(); i++)
        {
            String value = stringValues_p.get(i);
            Object objectValue = null;
            if (type_p.equals(VALUE_TYPE_STRING))
            {
                objectValue = value;
            }
            else if (type_p.equals(VALUE_TYPE_LONG))
            {
                objectValue = Long.parseLong(value);
            }
            else if (type_p.equals(VALUE_TYPE_DOUBLE))
            {
                objectValue = Double.parseDouble(value);
            }
            else if (type_p.equals(VALUE_TYPE_DATETIME))
            {
                objectValue = createDate(value);
            }
            else
            {
                throw new OwInvalidOperationException("Unknown value type : " + type_p);
            }

            valuesArray[i] = objectValue;
        }

        return valuesArray;
    }

    protected Object createDate(String value) throws ParseException, OwInvalidOperationException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(value);
    }

    public Object getValueFromNode(Node node_p) throws Exception
    {
        String textContent = node_p.getTextContent();
        return getValueFromString(textContent);
    }

    public Object getValueFromString(String text_p) throws Exception
    {
        if (text_p == null || text_p.length() == 0)
        {
            return null;
        }

        if (!text_p.trim().endsWith("]"))
        {
            return text_p;
        }

        StringTokenizer valueTokenizer = new StringTokenizer(text_p, "[]");

        if (valueTokenizer.hasMoreTokens())
        {
            String vType = valueTokenizer.nextToken();
            boolean isArray = false;
            List<String> values = new ArrayList<String>();

            if (valueTokenizer.hasMoreTokens())
            {
                String valueString = valueTokenizer.nextToken();
                valueString = valueString.trim();
                if (valueString.startsWith("{") && valueString.endsWith("}"))
                {
                    //an array
                    isArray = true;
                    valueString = valueString.substring(1, valueString.length() - 1);
                    StringTokenizer arrayValuesTokenizer = new StringTokenizer(valueString, ",");
                    while (arrayValuesTokenizer.hasMoreTokens())
                    {
                        String arrayItem = arrayValuesTokenizer.nextToken();
                        values.add(arrayItem);
                    }
                }
                else
                {
                    //single value

                    values.add(valueString);
                }
            }

            Object[] objectValues = convertValues(vType, values);
            if (isArray)
            {
                return objectValues;
            }
            else
            {
                if (objectValues.length > 0)
                {
                    return objectValues[0];
                }
                else
                {
                    return null;
                }
            }

        }
        else
        {
            throw new OwInvalidOperationException("Ivalid test node value : " + text_p + ".Type missing!");
        }

    }

    public boolean isArray() throws Exception
    {

        return m_array;
    }

    public boolean isComplex()
    {

        return false;
    }

    public boolean isEnum() throws Exception
    {

        return false;
    }

    public boolean isRequired() throws Exception
    {

        return false;
    }

}
