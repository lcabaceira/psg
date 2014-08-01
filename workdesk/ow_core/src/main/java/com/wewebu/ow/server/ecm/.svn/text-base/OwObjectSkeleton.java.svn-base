package com.wewebu.ow.server.ecm;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.log.OwLogCore;
import com.wewebu.ow.server.util.OwXMLDOMUtil;
import com.wewebu.ow.server.util.OwXMLUtil;

/**
 *<p>
 * OwObject implementation to be used for new objects.<br/><br/>
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
public class OwObjectSkeleton implements OwObject
{
    /** package logger for the class */
    private static final Logger LOG = OwLogCore.getLogger(OwObjectSkeleton.class);

    /** ECM ID prefix to distinguish OwEmptyObject from other objects */
    protected static final String DMS_PREFIX = "_OW_EMPTY_OBJECT_";

    protected OwPropertyCollection m_PropertyMap;

    /** MIME type */
    protected String m_strMimeType = "undef";

    /** reference to the class description */
    protected OwObjectClass m_ClassDescription;

    /** map of default values to override ECM defaults */
    protected Map m_OverrideDefaultValuesMap = null;

    /** date format to interpret the default values config */
    protected String m_DateFormatString;

    /** Network */
    protected OwNetwork m_Network;

    /**
     * construct An Empty Object
     * @param network_p OwNetwork reference
     * @param objectClass_p OwObjectClass to use for creation
     * @throws Exception
     */
    public OwObjectSkeleton(OwNetwork network_p, OwObjectClass objectClass_p) throws Exception
    {
        //      get class
        m_ClassDescription = objectClass_p;

        // load the properties
        refreshProperties();

        m_Network = network_p;
    }

    /**
     * construct An Empty Object
     * @param network_p OwNetwork reference
     * @param objectClass_p OwObjectClass to use for creation
     * @throws Exception
     */
    public OwObjectSkeleton(OwNetwork network_p, OwObjectClass objectClass_p, OwXMLUtil overrideDefaultValuesConfig_p) throws Exception
    {
        // get class
        m_ClassDescription = objectClass_p;

        // load the override default values map
        if (overrideDefaultValuesConfig_p != null)
        {
            m_OverrideDefaultValuesMap = new HashMap();
            Iterator defaultValuesIt = overrideDefaultValuesConfig_p.getSafeNodeList().iterator();
            while (defaultValuesIt.hasNext())
            {
                Element configSubElem = (Element) defaultValuesIt.next();
                if (configSubElem.getTagName().equals("Property"))
                {
                    String propertyClassName = OwXMLDOMUtil.getSafeStringAttributeValue(configSubElem, "name", null);
                    String propertyDefaultValue = OwXMLDOMUtil.getElementText(configSubElem);
                    propertyDefaultValue = (propertyDefaultValue == null) ? "" : propertyDefaultValue;
                    if (propertyClassName != null)
                    {
                        m_OverrideDefaultValuesMap.put(propertyClassName, propertyDefaultValue);
                    }
                    else
                    {
                        LOG.error("OwObjectSkeleton.OwObjectSkeleton: the config node owbootstrap.xml / <EcmAdapter> / <CreationInitialValues> / <ObjectClass name=\""
                                + (objectClass_p.getClassName() != null ? objectClass_p.getClassName() : "[unknown]") + "\"> / <Property> has no 'name' attribute.");
                    }
                }
            }
        }

        // load the properties
        refreshProperties();

        m_Network = network_p;
    }

    /** get Object name property string
     * @return the name property string of the object
     */
    public String getName()
    {
        try
        {
            return this.getProperty(this.getObjectClass().getNamePropertyName()).getValue().toString();
        }
        catch (Exception e)
        {
            return "unknown";
        }
    }

    /** get Object symbolic name of the object which is unique among its siblings
     *  used for path construction
     *
     * @return the symbolic name of the object which is unique among its siblings
     */
    public String getID()
    {
        return getName();
    }

    /** get the class name of the object, the class names are defined by the ECM System
     * @return class name of object class
     */
    public String getClassName()
    {
        return m_ClassDescription.getClassName();
    }

    /** get the class description of the object, the class descriptions are defined by the ECM System
     * @return class description name of object class
     */
    public OwObjectClass getObjectClass()
    {
        return m_ClassDescription;
    }

    /** get the containing parents of this object
     * for folders it is several parent folders, for compound documents it is one parent document.
     * @return Parent Object, or null if object does not have any parents
     */
    public OwObjectCollection getParents() throws Exception
    {
        return null;
    }

    /** get the children of the object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of subfolders
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort Sortcriteria list to sort return list
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the children, can be null
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return list of child objects, or null
     */
    public OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    /** check if the FilterCriteria_p in getChilds is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     */
    public boolean canFilterChilds() throws Exception
    {
        return false;
    }

    /** get a collection of OwFieldDefinition's for a given list of names
     * 
     * @param propertynames_p Collection of property names the client wants to use as filter properties or null to retrieve all possible filter properties
     * @return Collection of OwFieldDefinition's that can actually be filtered, may be a subset of propertynames_p, or null if no filter properties are allowed
     * @throws Exception
     */
    public java.util.Collection getFilterProperties(java.util.Collection propertynames_p) throws Exception
    {
        return null;
    }

    /** get the version series object to this object, if the object is versionable
     * @return a list of object versions
     */
    public OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    /** check if a version series object is available, i.e. the object is versionable
     * @return true if object is versionable
     */
    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    /** get the current version object 
     * 
     * @return OwVersion Object identifying the currently set version, or null if versions not supported
     */
    public OwVersion getVersion() throws Exception
    {
        return null;
    }

    /** get Object type
     * @return the type of the object
     */
    public int getType()
    {
        return m_ClassDescription.getType();
    }

    /** get the ECM specific ID of the Object. 
     *  The DMSID is not interpreted by the Workdesk, nor does the Workdesk need to know the syntax.
     *  However, it must hold enough information, so that the ECM Adapter is able to reconstruct the Object.
     *  The reconstruction is done through OwNetwork.createObjectFromDMSID(...)
     *  The Workdesk uses the DMSID to store ObjectReferences as Strings. E.g.: in the task databases.
     *
     *  The syntax of the ID is up to the ECM Adapter,
     *  but would usually be made up like the following:
     *
     */
    public String getDMSID() throws Exception
    {
        return DMS_PREFIX;
    }

    /** retrieve the specified property from the object.
     * NOTE: if the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     * @return the requested property object 
     * @throws OwObjectNotFoundException if the requested property is not set or found for the given object 
     */
    public OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwProperty prop = findProperty(strPropertyName_p);
        if (null == prop)
        {
            String msg = "OwObjectSkeleton.getProperty: Cannot find the property, propertyName = " + strPropertyName_p;
            LOG.debug(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return prop;
    }

    /**
     *  Retrieve the specified property.
     *  
     * @param strPropertyName_p
     * @return the requested property object or null if not found
     * @since 4.1.1.2
     */
    protected OwProperty findProperty(String strPropertyName_p)
    {
        return (OwProperty) m_PropertyMap.get(strPropertyName_p);
    }

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        return m_PropertyMap;
    }

    /** filter properties, which are read only or null, or system
     *
     * @param iContext_p int OwPropertyClass context for read-only and ishidden
     *
     * @return OwPropertyCollection of filtered properties
     */
    public OwPropertyCollection getEditableProperties(int iContext_p) throws Exception
    {
        OwPropertyCollection ret = new OwStandardPropertyCollection();

        Iterator it = getProperties(null).values().iterator();
        while (it.hasNext())
        {
            OwProperty prop = (OwProperty) it.next();
            if (prop.isReadOnly(iContext_p) || prop.getPropertyClass().isSystemProperty() || prop.isHidden(iContext_p) || (prop.getValue() == null))
            {
                continue;
            }

            ret.put(prop.getPropertyClass().getClassName(), prop);
        }

        return ret;
    }

    /** retrieve the specified properties from the object as a copy
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param strPropertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getClonedProperties(java.util.Collection strPropertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, strPropertyNames_p);
    }

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public void setProperties(OwPropertyCollection properties_p) throws Exception
    {
        Iterator it = properties_p.values().iterator();

        while (it.hasNext())
        {
            OwProperty newProperty = (OwProperty) it.next();
            String strPropertyName = newProperty.getPropertyClass().getClassName();

            // overwrite property
            m_PropertyMap.put(strPropertyName, newProperty);
        }
    }

    /** check if object allows to set / change properties
     * @return true if allowed
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** check if property retrieval is allowed 
     * @return true if allowed
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock() throws Exception
    {
        return false;
    }

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public boolean setLock(boolean fLock_p) throws Exception
    {
        return false;
    }

    /** get the lock state of the object
     * @return the lock state of the object
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p OwStatusContextDefinitions
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined in {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    /** check if content can be set on this document with setContent
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true, if content can be set with setContent
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** check if content retrieval is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true if allowed
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** get the MIME Typesof the Object
     * @return MIME Types as String
     */
    public String getMIMEType() throws Exception
    {
        return m_strMimeType;
    }

    /** get the additional MIME Parameter of the Object
     * @return MIME Parameter as String
     */
    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    /** delete object and all references from DB
     */
    public void delete() throws Exception
    {
    }

    /** check if object can be deleted and if user has sufficient access rights
     * @return true, if delete operation works on object
     */
    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

    /** removes the reference of the given object from this object (OwEmptyObject)
     *  this object needs to be parent of given object
     * @param oObject_p OwObject reference to be removed from this object (OwEmptyObject)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {

    }

    /** checks if the reference can be removed
     *  this object needs to be parent of given object, and user needs to have sufficient access rights
     * @param oObject_p OwObject reference to be checked upon
     * @return true, if given OwObject reference can be removed from this object (folder)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** adds a object reference to this parent object (folder)
     *  @param oObject_p OwObject reference to add to
     */
    public void add(OwObject oObject_p) throws Exception
    {
    }

    /** checks if object supports add function and if user has sufficient access rights
     *
     * @param oObject_p OwObject reference to be added
     *
     * @return true if object supports add function
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** moves a object reference to this parent object (folder)
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        // not supported
    }

    /** check if move operation is allowed
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** get a search template associated with this Object
     *
     *  The search from the template can be used to refine the result in getChilds(...)
     *  ==> The search is automatically performed when calling getChilds(...)
     *
     *  The ColumnInfoList from the template can be used to format the result list of the children
     *  
     *
     *  NOTE: This function is especially used in virtual folders
     *
     *  @return OwSearchTemplate or null if not defined for the object
     */
    public OwSearchTemplate getSearchTemplate() throws Exception
    {
        return null;
    }

    /** get the column info list that describes the columns for the child list
     * @return List of OwSearchTemplate.OwObjectColumnInfos, or null if not defined
     */
    public java.util.Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    /** retrieve the number of pages in the objects
     * @return number of pages
     */
    public int getPageCount() throws Exception
    {
        return 0;
    }

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public OwResource getResource() throws Exception
    {
        return null;
    }

    /** get the permissions object
     *
     * @return OwPermissionCollection of the object
     */
    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    /** get the cloned permissions
     *
     * @return OwPermissionCollection clone of the object
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        throw new OwNotSupportedException("OwObjectSkeleton.getClonedPermissions: Not implemented.");
    }

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    /** check if permissions can be set
     *
     * @return true = permissions can be set
     */
    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwNotSupportedException("OwObjectSkeleton.setPermissions: Not implemented.");
    }

    /** set the content to the object
     *
     * @param content_p OwContentCollection to store in the object
    */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {

    }

    /** get the content of the object
     *
     * @return OwContentCollection
     */
    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    /** refresh the property cache  */
    public void refreshProperties() throws Exception
    {
        m_PropertyMap = new OwStandardPropertyCollection();
        Iterator it = m_ClassDescription.getPropertyClassNames().iterator();
        while (it.hasNext())
        {
            String propertyClassName = (String) it.next();
            OwPropertyClass classDescription = m_ClassDescription.getPropertyClass(propertyClassName);

            // get the default value
            Object defaultValue = null;
            if (m_OverrideDefaultValuesMap != null)
            {
                String overrideDefaultValueString = (String) m_OverrideDefaultValuesMap.get(propertyClassName);
                if (overrideDefaultValueString != null)
                {
                    if (overrideDefaultValueString.length() > 0)
                    {
                        String javaClassName = classDescription.getJavaClassName();
                        if (javaClassName.equals("java.util.Date"))
                        {
                            if (overrideDefaultValueString.equals("{today}"))
                            {
                                defaultValue = new Date();
                            }
                            else
                            {
                                // parse string representation to a Date object
                                defaultValue = OwStandardPropertyClass.getSkalarValueFromString(overrideDefaultValueString, javaClassName);
                            }
                        }
                        else
                        {
                            // convert scalar value to object
                            defaultValue = OwStandardPropertyClass.getSkalarValueFromString(overrideDefaultValueString, javaClassName);
                        }
                    }
                    else
                    {
                        // this property class name is defined with an empty tag. Means: null.
                        defaultValue = null;
                    }
                }
                else
                {
                    // this property class is not part of the override default values map. Use the value from the ECM system.
                    defaultValue = classDescription.getDefaultValue();
                }
            }
            else
            {
                // no override default values map defined. Use the value from the ECM system.
                defaultValue = classDescription.getDefaultValue();
            }

            if (defaultValue == null)
            {
                defaultValue = OwStandardObjectClass.createInitialNullValue(classDescription, false);
            }

            m_PropertyMap.put(propertyClassName, createNewProperty(defaultValue, classDescription));
        }
    }

    /** overridable to create the property
     * 
     * @param defaultValue_p
     * @param classDescription_p
     * @return OwProperty
     */
    public OwProperty createNewProperty(Object defaultValue_p, OwPropertyClass classDescription_p)
    {
        return new OwStandardProperty(defaultValue_p, classDescription_p);
    }

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     */
    public void refreshProperties(java.util.Collection props_p) throws Exception
    {
        refreshProperties();
    }

    /** get the native object from the ECM system 
     *
     *  WARNING: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return Object native to the ECM System
     */
    public Object getNativeObject() throws Exception
    {
        throw new OwObjectNotFoundException("OwObjectSkeleton.getNativeObject: Not implemented or Not supported.");
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws Exception, OwObjectNotFoundException
    {
        return getProperty(strFieldClassName_p);
    }

    /** get the source object that originally provided the fields.
     * e.g. the fieldprovider might be a template pattern implementation like a view,
     *      where the original provider would still be an OwObject
     *      
     * @return Object the original source object where the fields have been taken, can be a this pointer
     * */
    public Object getFieldProviderSource()
    {
        return this;
    }

    /** get the type of field provider as defined with TYPE_... */
    public int getFieldProviderType()
    {
        return TYPE_META_OBJECT;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p as defined in {@link OwStatusContextDefinitions}
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if object has children
     *
     * @param iContext_p OwStatusContextDefinitions
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the DMSID, the path is not necessarily unique,
     * but provides a readable information of the objects location.
     */
    public String getPath() throws Exception
    {
        throw new OwNotSupportedException("OwObjectSkeleton.getPath: Not implemented.");
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined in {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return 0;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p String 
     * @param properties_p OwPropertyCollection  (optional, can be null to set previous properties)
     * @param permissions_p OwPermissionCollection  (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwObjectSkeleton.changeClass: not implemented.");
    }

    /** check if object can change its class */
    public boolean canChangeClass() throws Exception
    {
        return false;
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getName();
    }

    /** modify a Field value, but does not save the value right away
     * 
     * @param sName_p
     * @param value_p
     * @throws Exception
     * @throws OwObjectNotFoundException
     */
    public void setField(String sName_p, Object value_p) throws Exception, OwObjectNotFoundException
    {
        getProperty(sName_p).setValue(value_p);
    }

    /** retrieve the value of a Field
     * 
     * @param sName_p
     * @param defaultvalue_p
     * @return Object the value of the Field of defaultvalue_p
     */
    public Object getSafeFieldValue(String sName_p, Object defaultvalue_p)
    {
        try
        {
            return getProperty(sName_p).getValue();
        }
        catch (Exception e)
        {
            return defaultvalue_p;
        }
    }

    /** get all the properties in the form
     * 
     * @return Collection of OwField
     * @throws Exception
     */
    public Collection getFields() throws Exception
    {
        return getProperties(null).values();
    }

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     */
    public String getResourceID() throws Exception
    {
        try
        {
            return getResource().getID();
        }
        catch (NullPointerException e)
        {
            throw new OwObjectNotFoundException("OwObjectSkeleton.getResourceID: Resource Id not found for DMSID = " + getDMSID(), e);
        }
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

}