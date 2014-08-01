package com.wewebu.ow.server.ecm;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFormat;
import com.wewebu.ow.server.fieldctrlimpl.OwRelativeDate;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;
import com.wewebu.ow.server.util.OwString1;

/**
 *<p>
 * Standard Implementation for property class descriptions.
 * Class descriptions are defined by the ECM System, the contain information about
 * the property type.<br/><br/>
 * To be implemented with the specific ECM system.
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
public class OwStandardPropertyClass implements OwPropertyClass
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardPropertyClass.class);

    /** text format to use for date serialization, 
     * NOTE: We accept both the java timezone "+-HHMM" and the FileNet P8 timezone "+-HH:MM" 
     * */
    private static final String DATE_TEXT_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    // e.g.: 2007-06-06T15:47:00+02:00

    /** date format with millisecond information for exact date representation */
    public static final String EXACT_DATE_TEXT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S'Z'Z";
    // e.g.: 2007-06-06T15:47:00.799Z+02:00

    /** the name of the class */
    protected String m_strClassName;

    /** class name of the java object associated with this property */
    protected String m_strJavaClassName;

    /** the displayable name of the class */
    protected OwString m_DisplayName;

    /** the displayable description of the class */
    protected OwString m_description;

    /** flag indicating, if property is a internal system property and contains no custom object information */
    protected boolean m_fSystem;

    /** flag indicating, if property is read only for each context */
    protected boolean[] m_fReadOnly = new boolean[CONTEXT_MAX];

    /** flag indicating, if property is the name property i.e. identical to OwObject.getName. */
    protected boolean m_fName;

    /* a list of enum objects for the enum type */
    protected com.wewebu.ow.server.field.OwEnumCollection m_Enums;

    /** flag indicating, if property is required to be set, i.e. Value can not be null */
    protected boolean m_fRequired;

    /** flag indicating, if property is an array of values */
    protected boolean m_fArray;

    /** the ECM system specific type of the property */
    protected Object m_DMSType;

    /** a collection of possible filter / search operators for the field */
    protected Collection m_operators;

    /** object representing the maximum value */
    protected Object m_MaxValue;
    /** object representing the minimum value */
    protected Object m_MinValue;
    /** object representing the default value */
    protected Object m_Default;

    /** flag indicating, that property should not be displayed */
    protected boolean[] m_fHidden = new boolean[CONTEXT_MAX];

    public OwStandardPropertyClass()
    {
        // void
    }

    /** get the name of the class
     * @return class name
     */
    public String getClassName()
    {
        return m_strClassName;
    }

    /** get the formatter object for string representation 
     * 
     * @return Format, can be null to use the default format
     */
    public OwFormat getFormat()
    {
        return null;
    }

    /** set the java classname parameter
     *  @param strJavaClassName_p a {@link String} 
     */
    public void setJavaClassName(String strJavaClassName_p)
    {
        m_strJavaClassName = strJavaClassName_p;
    }

    /** get the displayable name of the type as defined by the ECM System
     * can be identical to getClassName
     * @return type displayable name of property
     */
    public String getDisplayName(java.util.Locale locale_p)
    {
        return m_DisplayName.getString(locale_p);
    }

    /** get the description defined by the DMS System
     *
     * @return type description of property
     */
    public String getDescription(java.util.Locale locale_p)
    {
        if (null != m_description)
        {
            return m_description.getString(locale_p);
        }
        else
        {
            return getDisplayName(locale_p);
        }
    }

    /** get the java class name of java object associated with this property
     * @return java class name
     */
    public String getJavaClassName()
    {
        return m_strJavaClassName;
    }

    /** get the native type which is defined by the underlying system
     *
     *  WARNING:    The returned object is opaque. 
     *              Using this object makes the client dependent on the underlying system
     *
     * @return Object native Type Object
     */
    public Object getNativeType() throws Exception
    {
        return m_DMSType;
    }

    /** check if property contains a list of values
     * @return true if property is array, false if property is scalar
     */
    public boolean isArray() throws Exception
    {
        return m_fArray;
    }

    /** check if property is a enum type  (see getEnums)
     * @return true if property is a enum type
     */
    public boolean isEnum() throws Exception
    {
        return (m_Enums != null);
    }

    /** get a list of enum objects for the enum type (see isEnum)
     * @return list of OwEnum objects, which can be used in a select box.
     */
    public com.wewebu.ow.server.field.OwEnumCollection getEnums() throws Exception
    {
        return m_Enums;
    }

    /** check if property is a internal system property and contains no custom object information
     */
    public boolean isSystemProperty() throws Exception
    {
        return m_fSystem;
    }

    /** check if property is identical the the OwObject.getName Property
     */
    public boolean isNameProperty() throws Exception
    {
        return m_fName;
    }

    /** check if property is required, i.e. must be set by the user
     * @return true if property is required
     */
    public boolean isRequired() throws Exception
    {
        return m_fRequired;
    }

    /** check if property is read only on the class level.
     *  NOTE:   isReadOnly is also defined in OwProperty on the instance level.
     *          I.e. A Property might be defined as readable on the class level, but still be write protected on a specific instance
     *
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     * @return true if property is read only
     */
    public boolean isReadOnly(int iContext_p) throws Exception
    {
        return m_fReadOnly[iContext_p];
    }

    /** get the max allowed value for the field or null if not defined
     */
    public Object getMaxValue() throws Exception
    {
        return m_MaxValue;
    }

    /** get the min allowed value for the field or null if not defined
     */
    public Object getMinValue() throws Exception
    {
        return m_MinValue;
    }

    /** get the default value
     */
    public Object getDefaultValue() throws Exception
    {
        return m_Default;
    }

    /** check if property is visible to the user
     *
     * @param iContext_p Context in which the property is read-only as defined by CONTEXT_...
     *
     * @return true if property is visible to the user
     */
    public boolean isHidden(int iContext_p) throws Exception
    {
        return m_fHidden[iContext_p];
    }

    /** create a value for the field described by this class with the given XML Node serialization
    *
    * @param node_p the serialized value as a XML DOM Node
    * @return Object the value of the field 
    */
    public Object getValueFromNode(org.w3c.dom.Node node_p) throws Exception
    {
        return getValueFromNode(node_p, this);
    }

    /** create a value for the field described by this class with the given XML Node serialization
     *
     * @param node_p the serialized value as a XML DOM Node
     * @return Object the value of the field 
     */
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

    /** create a value for the field described by this class with the given String serialization
    *
    * @param text_p String the serialized value
    * @return Object the value of the field 
    */
    public Object getValueFromString(String text_p) throws Exception
    {
        return getValueFromString(text_p, this);
    }

    /** create a value for the field described by this class with the given String serialization
    *
    * @param text_p String the serialized value
    * @return Object the value of the field 
    */
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

    /** get a default empty value for the given class
     * e.g.: 0 for Integer or "" for String
     * 
     * @param strJavaClassName_p
     * @return Object value
     * @throws Exception 
     */
    public static Object getSkalarEmptyValue(String strJavaClassName_p) throws Exception
    {
        try
        {
            return getSkalarValueFromString("", strJavaClassName_p);
        }
        catch (Exception e)
        {
            return getSkalarValueFromString("0", strJavaClassName_p);
        }
    }

    /** create a object from string literal
     * 
     * @param strLiteral_p
     * @param strJavaClassName_p
     * @return an {@link Object}
     * @throws Exception
     */
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

    /** create a XML serialization of the given field value
     *
     * @param value_p Object with field value
     * @param doc_p DOM Document to add to
     *
     * @return DOM Node
     */
    public org.w3c.dom.Node getNodeFromValue(Object value_p, org.w3c.dom.Document doc_p) throws Exception
    {
        return getNodeFromValue(value_p, doc_p, getJavaClassName());
    }

    public static org.w3c.dom.Node getNodeFromValue(Object value_p, org.w3c.dom.Document doc_p, String javaClassName_p) throws Exception
    {
        if (null == value_p)
        {
            return null;
        }

        if (javaClassName_p.equals("java.util.Date"))
        {
            return doc_p.createTextNode(new java.text.SimpleDateFormat(EXACT_DATE_TEXT_FORMAT).format((java.util.Date) value_p));
        }
        else
        {
            return doc_p.createTextNode(value_p.toString());
        }
    }

    /** get a standard string representation of the given value that can be used in getValueFromString
     * 
     * @param value_p
     * @return String
     * @see #getValueFromString(String)
     */
    public static String getStringFromValue(Object value_p, String strJavaClassName_p)
    {
        try
        {
            if (strJavaClassName_p.equals("java.util.Date"))
            {
                return new java.text.SimpleDateFormat(EXACT_DATE_TEXT_FORMAT).format((java.util.Date) value_p);
            }
            else
            {
                return value_p.toString();
            }
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    /** get the property category, or an empty string of no category is set
     * 
     * @return Category of Property
     * @throws Exception
     */
    public String getCategory() throws Exception
    {

        return "";
    }

    /** get a collection of possible filter / search operators for the field
     * 
     * @return Collection of operators as defined with OwSearchOperator.CRIT_OP_..., or null if no operators are defined
     */
    public Collection getOperators() throws Exception
    {
        return m_operators;
    }

    /** check if given java class name is base type
     * 
     * @param base_p
     * @param sSubJavaClassName_p
     * @return a boolean
     * @throws ClassNotFoundException 
     */
    public static boolean isType(Class base_p, String sSubJavaClassName_p) throws ClassNotFoundException
    {
        return (base_p.isAssignableFrom(Class.forName(sSubJavaClassName_p)));
    }

    /** check if given java class name is base type
     * 
     * @param base_p a {@link Class}
     * @return a boolean
     * @throws ClassNotFoundException 
     */
    public boolean isType(Class base_p) throws ClassNotFoundException
    {
        return (base_p.isAssignableFrom(Class.forName(this.getJavaClassName())));
    }

    /** retrieve the specified properties from the object as a copy
     * NOTE: The returned collection contains exactly the requested
     *  <b>propertyNames_p</b>, or all if <code> propertyNames_p == <b>null</b></code>
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the DMS Adaptor has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param object_p OwObject to retrieve cloned properties from
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public static OwPropertyCollection getClonedProperties(OwObject object_p, java.util.Collection propertyNames_p) throws Exception
    {
        // === create clones of properties
        // get cached properties
        OwPropertyCollection Properties = object_p.getProperties(propertyNames_p);

        // create new return list
        OwPropertyCollection retList = new OwStandardPropertyCollection();

        if (null == propertyNames_p)
        {
            // iterate over the properties
            Iterator it = Properties.values().iterator();
            while (it.hasNext())
            {
                OwProperty Property = (OwProperty) it.next();
                retList.put(Property.getPropertyClass().getClassName(), Property.clone());
            }
        }
        else
        {
            // iterate over the property name
            Iterator it = propertyNames_p.iterator();
            while (it.hasNext())
            {
                String propname = (String) it.next();
                OwProperty property = (OwProperty) Properties.get(propname);

                if (property == null)
                {
                    String classname = "...";
                    try
                    {
                        classname = " [" + object_p.getObjectClass().getClassName() + "]...";
                    }
                    catch (Exception e)
                    {
                        //do nothing, Exception check if object_p or object_p.getObjectClass() is null; 
                    }
                    String msg = "OwStandardPropertyClass.getClonedProperties: Error accessing the property=[" + propname + "], the property is not valid for the object class" + classname + " Please check your configuration!";
                    LOG.debug(msg);
                    throw new OwObjectNotFoundException(msg);
                }
                else
                {
                    retList.put(property.getPropertyClass().getClassName(), property.clone());
                }
            }
        }

        // return cloned list
        return retList;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldDefinition#isComplex()
     */
    public boolean isComplex()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see com.wewebu.ow.server.field.OwFieldDefinition#getComplexChildClasses()
     */
    public List getComplexChildClasses() throws Exception
    {
        // not complex, so return null
        return null;
    }
}