package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Class descriptions are defined by the ECM System, the contain information about
 * the object type.<br/><br/>
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
public class OwStandardObjectClass implements OwObjectClass
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwStandardObjectClass.class);

    /** map containing the property class descriptions of the class */
    protected Map m_PropertyClassesMap;
    /** optional backup list for predictable order of keys (ClassNames) */
    protected Collection m_propertyKeyList;

    protected boolean m_fCanCreateNewObject;
    protected String m_strClassName;
    protected String m_strNamePropertyName;

    /** the parent class of this class */
    protected OwObjectClass m_parent;

    protected OwString m_DisplayName;
    protected boolean m_fHidden;
    protected int m_iType;
    /** flag indicating if a version series objectclass is available, i.e. the object is versionable*/
    protected boolean m_fVersionable;
    /** a description of the object class */
    protected OwString m_Description;

    /** default constructor, override with subclass
     * 
     */
    public OwStandardObjectClass()
    {

    }

    /** simple constructor
     * 
     * @param classname_p
     */
    public OwStandardObjectClass(String classname_p, int iType_p)
    {
        m_strClassName = classname_p;
        m_iType = iType_p;
        m_DisplayName = new OwString(classname_p);
        m_Description = new OwString(classname_p);
        // empty property map
        m_PropertyClassesMap = new HashMap();
    }

    /** get the base defining type for the given object type.
     * e.g. get OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS for OwObjectReference.OBJECT_TYPE_DOCUMENT
     *
     * @param iObjectType_p object type as defined in OwObjectReference OBJECT_TYPE_...
     *
     * @return int base type OwObjectReference.OBJECT_TYPE_ALL_...
     */
    public static int getBaseObjectType(int iObjectType_p)
    {
        return (iObjectType_p & 0xF000);
    }

    /** check if a given type is a container type like a folder
     * @param iObjectType_p int object type
     *
     * @return true = container type, false = otherwise
     */
    public static boolean isContainerType(int iObjectType_p)
    {
        return (getBaseObjectType(iObjectType_p) == OwObjectReference.OBJECT_TYPE_ALL_CONTAINER_OBJECTS);
    }

    /** check if a given type is a Content type, like a document
     * @param iObjectType_p int object type
     *
     * @return true = Content type, false = otherwise
     */
    public static boolean isContentType(int iObjectType_p)
    {
        return (getBaseObjectType(iObjectType_p) == OwObjectReference.OBJECT_TYPE_ALL_CONTENT_OBJECTS);
    }

    /** check if a given type is a Tuple type, like a custom object
     * @param iObjectType_p int object type
     *
     * @return true = Tuple type, false = otherwise
     */
    public static boolean isTupleType(int iObjectType_p)
    {
        return (getBaseObjectType(iObjectType_p) == OwObjectReference.OBJECT_TYPE_ALL_TUPLE_OBJECTS);
    }

    /** check if a given type is a Tuple type, like a custom object
     * @param iObjectType_p int object type
     *
     * @return true = Tuple type, false = otherwise
     */
    public static boolean isWorkflowObjectType(int iObjectType_p)
    {
        return (getBaseObjectType(iObjectType_p) == OwObjectReference.OBJECT_TYPE_ALL_WORKFLOW_OBJECTS);
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return m_iType;
    }

    /** get the child classes of this class if we deal with a class tree
    *
    * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
    * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
    *
    * @return Map of child class symbolic names, mapped to display names, or null if no class tree is supported
    * @throws Exception 
    */
    public java.util.Map getChildNames(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception
    {
        return null;
    }

    /** check if children are available
    *
    * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
    * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
    * @param context_p OwStatusContextDefinitions
    * 
    * @return Map of child class symbolic names, mapped to display names, or null if no class tree is supported
    */
    public boolean hasChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p, int context_p)
    {
        return false;
    }

    /** get the child classes of this class if we deal with a class tree
     *
     * @param network_p OwNetwork, in case the class description is static for all users, we can still dynamically load the class members
     * @param fExcludeHiddenAndNonInstantiable_p boolean true = exclude all hidden and non instantiable class descriptions
     *
     * @return List of child classes, or null if no children are available
     */
    public java.util.List getChilds(OwNetwork network_p, boolean fExcludeHiddenAndNonInstantiable_p) throws Exception
    {
        return null;
    }

    /** get the name of the class
     *
     * @return class name
     */
    public String getClassName()
    {
        return m_strClassName;
    }

    /** get the unique ID of the class
     *
     * @return class ID 
     */
    public String getID()
    {
        return getClassName();
    }

    /** get the displayable name of the type as defined by the ECM System
     * @param locale_p Local to use
     * @return type displayable name of property
     */
    public String getDisplayName(java.util.Locale locale_p)
    {
        return m_DisplayName.getString(locale_p);
    }

    /** get a map of the available property class descriptions 
     *
     * @param strClassName_p Name of class
     * @return OwPropertyClass instance
     */
    public OwPropertyClass getPropertyClass(String strClassName_p) throws Exception
    {
        OwPropertyClass propertyClassDescription = (OwPropertyClass) m_PropertyClassesMap.get(strClassName_p);
        if (null == propertyClassDescription)
        {
            String msg = "OwStandardObjectClass.getPropertyClass: Cannot find the class for property = " + strClassName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return propertyClassDescription;
    }

    /** get a list of the available property class descriptions names
     *
     * @return string array of OwPropertyClass Names
     */
    public java.util.Collection getPropertyClassNames() throws Exception
    {
        if (null == m_propertyKeyList)
        {
            return m_PropertyClassesMap.keySet();
        }
        else
        {
            return m_propertyKeyList;
        }
    }

    /** get the name of the name property
     * @return String name of the name property
     */
    public String getNamePropertyName() throws Exception
    {
        return m_strNamePropertyName;
    }

    /** check, if new object instances can be created for this class
     *
     * @return true, if object can be created
     */
    public boolean canCreateNewObject() throws Exception
    {
        // currently not supported for file objects
        return m_fCanCreateNewObject;
    }

    /** check if a version series objectclass is available, i.e. the object is versionable
     *
     * @return true if objectclass is versionable
     */
    public boolean hasVersionSeries() throws Exception
    {
        return m_fVersionable;
    }

    /** retrieve a description of the object class
     *
     * @param locale_p Local to use
     * @return String Description of the object class
     */
    public String getDescription(java.util.Locale locale_p)
    {
        return m_Description.getString(locale_p);
    }

    /** check if class is visible to the user
     *
     * @return true if property is visible to the user
     */
    public boolean isHidden() throws Exception
    {
        return m_fHidden;
    }

    /** get the parent class of this class 
     *
     * @return OwObjectClass parent or null if topmost class
     */
    public OwObjectClass getParent() throws Exception
    {
        return m_parent;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        if (m_DisplayName != null)
        {
            return m_DisplayName.getString(Locale.getDefault());
        }
        else
        {
            return super.toString();
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObjectClass#getModes(int)
     */
    public List getModes(int operation_p) throws Exception
    {
        return null;
    }

    /** create a null value for the given field definition
     * in case of a complex value, returns an array of null's for the complex properties
     * 
     * @param fieldDefinition_p
     * @param fArrayInit_p true = create a initial value for an array element, false = create initial value for the whole array
     * @return Object
     * @throws Exception 
     */
    public static Object createInitialNullValue(OwFieldDefinition fieldDefinition_p, boolean fArrayInit_p) throws Exception
    {
        Object[] ret = null;

        if (fieldDefinition_p.isComplex() && (fArrayInit_p || (!fieldDefinition_p.isArray())))
        {
            List subfielddefinitions = fieldDefinition_p.getComplexChildClasses();

            ret = new Object[subfielddefinitions.size()];

            for (int i = 0; i < subfielddefinitions.size(); i++)
            {
                ret[i] = createInitialNullValue((OwFieldDefinition) subfielddefinitions.get(i), false);
            }
        }

        return ret;
    }
}