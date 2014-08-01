package com.wewebu.ow.server.ecmimpl.fncm5.propertyclass;

import java.text.ParseException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.filenet.api.collection.EngineCollection;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.property.Property;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverter;
import com.wewebu.ow.server.ecmimpl.fncm5.helper.OwFNCM5ValueConverterClass;
import com.wewebu.ow.server.ecmimpl.fncm5.property.OwFNCM5EngineProperty;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.fieldctrlimpl.OwRelativeDate;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * OwFNCM5EnginePropertyClass.
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
public abstract class OwFNCM5EnginePropertyClass<C, N, O> implements OwFNCM5PropertyClass, OwFNCM5EngineBinding<C, N, O>
{
    private static final Logger LOG = OwLogCore.getLogger(OwFNCM5EnginePropertyClass.class);

    /** text format to use for date serialization, 
     * NOTE: We accept both the java timezone "+-HHMM" and the FileNet P8 timezone "+-HH:MM" 
     * */
    private static final String DATE_TEXT_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    // e.g.: 2007-06-06T15:47:00+02:00

    /** date format with millisecond information for exact date representation */
    public static final String EXACT_DATE_TEXT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S'Z'Z";
    // e.g.: 2007-06-06T15:47:00.799Z+02:00

    protected C nativeClass;

    protected OwFNCM5ValueConverterClass<N, O> converterClass;

    private String preferredType = null;

    public OwFNCM5EnginePropertyClass(C nativeClass_p, OwFNCM5ValueConverterClass<N, O> converterClass_p, String preferredType_p)
    {
        super();
        this.nativeClass = nativeClass_p;
        this.converterClass = converterClass_p;
        this.preferredType = preferredType_p;
    }

    public final C getNativeType() throws OwException
    {
        return this.nativeClass;
    }

    public C getEngineClass()
    {
        return this.nativeClass;
    }

    public final Class<O> getOClass()
    {
        return this.converterClass.getOClass();
    }

    public final String getJavaClassName()
    {
        return getOClass().getCanonicalName();
    }

    public final Object getValueFromString(String text_p) throws Exception
    {
        return getValueFromString(text_p, this);
    }

    public static Object getValueFromNode(org.w3c.dom.Node node_p, OwFieldDefinition definition_p) throws Exception
    {
        if (null == node_p)
        {
            // === a empty node
            return null;
        }
        else if (node_p.getNodeType() == Node.TEXT_NODE)
        {
            // === a text node
            return getValueFromString(node_p.getNodeValue(), definition_p);
        }
        else
        {
            // === a complex XML node    
            // create class using string XML node constructor
            Class newClass = Class.forName(definition_p.getJavaClassName());
            java.lang.reflect.Constructor constr = newClass.getConstructor(new Class[] { org.w3c.dom.Node.class });
            return constr.newInstance(new Object[] { node_p });
        }
    }

    public static Object getValueFromString(String text_p, OwFieldDefinition definition_p) throws Exception
    {
        String strJavaClassName = definition_p.getJavaClassName();
        if (definition_p.isArray())
        {
            // === get the comma separated tokens and return array of objects
            StringTokenizer arraytokens = new StringTokenizer(text_p, ",");

            Object[] retArray = new Object[arraytokens.countTokens()];

            int i = 0;
            while (arraytokens.hasMoreTokens())
            {
                String strToken = arraytokens.nextToken();

                retArray[i++] = getSkalarValueFromString(strToken, strJavaClassName);
            }

            return retArray;
        }
        else
        {
            // return scalar
            return getSkalarValueFromString(text_p, strJavaClassName);
        }
    }

    public static Object getSkalarValueFromString(String strLiteral_p, String strJavaClassName_p) throws Exception
    {
        if (strJavaClassName_p.equals("java.util.Date"))
        {
            if (strLiteral_p.length() == 0)
            {
                return null;
            }
            else
            {
                try
                {
                    // do we have a exact date format
                    if (-1 != strLiteral_p.indexOf('.'))
                    {
                        // date contains milliseconds so use exact date format
                        return new java.text.SimpleDateFormat(EXACT_DATE_TEXT_FORMAT).parse(strLiteral_p);
                    }

                    // accept FileNet p8 timezone as well
                    if (strLiteral_p.length() > 24)
                    {
                        // === includes p8 timezone remove p8 semicolon
                        strLiteral_p = strLiteral_p.substring(0, strLiteral_p.length() - 3) + strLiteral_p.substring(strLiteral_p.length() - 2);
                    }
                    try
                    {
                        return new java.text.SimpleDateFormat(DATE_TEXT_FORMAT).parse(strLiteral_p);
                    }
                    catch (ParseException e)
                    {
                        //not a date try relative-date string values - see ticket 3958
                        OwRelativeDate relativeDate = OwRelativeDate.fromString(strLiteral_p);
                        if (relativeDate != null)
                        {
                            return relativeDate;
                        }
                        else
                        {
                            //re-throw now LOG needed
                            throw e;
                        }
                    }
                }
                catch (Exception e)
                {
                    String msg = "Invalid date format for :" + strLiteral_p;
                    LOG.error(msg, e);
                    throw new OwInvalidOperationException(new OwString1("app.OwStandardPropertyClass.invalid.date.format", "Invalid date format %1", strLiteral_p), e);
                }
            }
        }

        // create class using string constructor
        Class newClass = Class.forName(strJavaClassName_p);
        java.lang.reflect.Constructor constr = newClass.getConstructor(new Class[] { java.lang.String.class });
        return constr.newInstance(new Object[] { strLiteral_p });
    }

    public final Object getValueFromNode(Node node_p) throws Exception
    {
        return getValueFromNode(node_p, this);
    }

    public final Node getNodeFromValue(Object value_p, Document doc_p) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public final OwFormat getFormat()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public final List getComplexChildClasses() throws Exception
    {
        return null;
    }

    public final boolean isComplex()
    {
        return false;
    }

    @Override
    public String toString()
    {
        Class<?> javaClass = getClass();
        return javaClass.getName() + "{" + getClassName() + "}";
    }

    public final boolean isArray() throws OwException
    {
        return isNativeList() || isNativeEnum();
    }

    public abstract boolean isNativeEnum();

    public abstract boolean isNativeList();

    public final Object toNativeValue(Object owdValue_p) throws OwException
    {
        OwFNCM5ValueConverter<N, O> converter = this.converterClass.createConverter(this);
        Object nativeValue = null;
        if (isArray())
        {
            nativeValue = converter.toEngineCollection((O[]) owdValue_p);
        }
        else
        {
            nativeValue = converter.toNativeValue((O) owdValue_p);
        }

        return nativeValue;
    }

    public final Object fromNativeValue(Object nativeValue_p) throws OwException
    {
        OwFNCM5ValueConverter<N, O> converter = this.converterClass.createConverter(this);
        Object owdValue = null;
        if (isArray())
        {
            owdValue = converter.fromEngineCollection((EngineCollection) nativeValue_p);
        }
        else
        {
            owdValue = converter.fromNativeValue((N) nativeValue_p);
        }

        return owdValue;
    }

    public OwFNCM5EngineProperty<N, O> from(Property nativeProperty_p) throws OwException
    {
        return new OwFNCM5EngineProperty<N, O>(nativeProperty_p, this);
    }

    protected boolean isReadOnly(PropertySettability settability_p, int context_p)
    {
        switch (settability_p.getValue())
        {
            case PropertySettability.READ_ONLY_AS_INT:
                return true;
            case PropertySettability.SETTABLE_ONLY_BEFORE_CHECKIN_AS_INT:
                return CONTEXT_ON_CHECKIN != context_p;
            case PropertySettability.SETTABLE_ONLY_ON_CREATE_AS_INT:
                return CONTEXT_ON_CREATE != context_p;
            default:
                return false;
        }
    }

    protected abstract boolean isEngineSystemProperty() throws OwException;

    public final boolean isSystemProperty() throws OwException
    {

        if (preferredType != null)
        {
            return OwFNCM5Network.PROPERTY_TYPE_SYSTEM.equals(preferredType);
        }
        else
        {
            return isEngineSystemProperty();
        }

        //        Boolean preferredPropertyType = network_p.getPreferredPropertyType(m_strClassName);
        //        if (preferredPropertyType == null)
        //        {
        //            // use P8 default
        //            m_fSystem = propDesc_p.getPropertyBooleanValue(Property.IS_SYSTEM_GENERATED);
        //        }
        //        else
        //        {
        //            // use preferred value
        //            m_fSystem = preferredPropertyType.booleanValue();
        //        }

    }

    public OwFNCM5EngineBinding<C, N, O> getEngineBinding() throws OwException
    {
        return this;
    }

    public final OwFNCM5VirtualBinding getVirtualBinding() throws OwException
    {
        return null;
    }
}
