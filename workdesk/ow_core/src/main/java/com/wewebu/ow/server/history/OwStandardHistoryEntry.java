package com.wewebu.ow.server.history;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwEnumCollection;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinitionProvider;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.util.OwString;

/**
 *<p>
 * Standard implementation of the OwHistoryEntry interface.
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
public class OwStandardHistoryEntry implements OwHistoryEntry
{
    /** cardinality serialize string to be written to the database */
    public static final String CARDINALITY_SCALAR = "0";
    /** cardinality serialize string to be written to the database */
    public static final String CARDINALITY_ARRAY = "1";

    /**
     *<p>
     * Property class description of history entry object properties.
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
    public static class OwStandardHistoryEntryPropertyClass extends OwStandardPropertyClass
    {
        /** init property class description */
        public OwStandardHistoryEntryPropertyClass(String strName_p, OwString displayName_p, String strJavaClassName_p, boolean fArray_p, boolean fNameProperty_p, OwEnumCollection enums_p)
        {
            this.m_fArray = fArray_p;
            this.m_fName = fNameProperty_p;
            this.m_fRequired = false;
            this.m_fSystem = true;
            this.m_Enums = enums_p;

            for (int i = 0; i < OwPropertyClass.CONTEXT_MAX; i++)
            {
                this.m_fHidden[i] = false;
                this.m_fReadOnly[i] = true;
            }

            this.m_strClassName = strName_p;
            this.m_DisplayName = displayName_p;
            this.m_strJavaClassName = strJavaClassName_p;
        }
    }

    /**
     *<p>
     * Object class description of history entry.
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
    public static class OwStandardHistoryEntryObjectClass extends OwStandardObjectClass
    {
        /** property class name */
        public static final String NAME_PROPERTY = "OW_HIST_Name";
        /** property class name */
        public static final String TIME_PROPERTY = "OW_HIST_Time";
        /** property class name */
        public static final String ID_PROPERTY = "OW_HIST_ID";
        /** property class name */
        public static final String TYPE_PROPERTY = "OW_HIST_Type";
        /** property class name */
        public static final String STATUS_PROPERTY = "OW_HIST_Status";
        /** property class name */
        public static final String SUMMARY_PROPERTY = "OW_HIST_Summary";
        /** property class name */
        public static final String USER_PROPERTY = "OW_HIST_User";

        /** property class name */
        public static final String OBJECTS_PROPERTY = "OW_HIST_Objects";
        /** property class name */
        public static final String PARENT_PROPERTY = "OW_HIST_Parent";
        /** property class name */
        public static final String MODIFIED_PROPS_PROPERTY = "OW_HIST_ModifiedProperties";

        /** init object class description */
        public OwStandardHistoryEntryObjectClass()
        {
            m_fCanCreateNewObject = false;
            m_fHidden = true;
            m_fVersionable = false;
            m_iType = OwObjectReference.OBJECT_TYPE_HISTORY;
            m_parent = null;
            m_strClassName = "OwStandardHistoryEntryObjectClass";
            m_Description = new OwString("historyimpl.OwStandardHistoryEntryObjectClass.description", "Standard class description for standard history items.");
            m_DisplayName = new OwString("historyimpl.OwStandardHistoryEntryObjectClass.displayname", "Standard history item class");
            m_strNamePropertyName = "Name";

            m_PropertyClassesMap = new HashMap();

            // === construct the property class descriptions
            addPropertyClass(NAME_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.nameprop", "Name"), "java.lang.String", false, true, null);
            addPropertyClass(TIME_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.timeprop", "Date / Time"), "java.util.Date", false, false, null);
            addPropertyClass(ID_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.idprop", "ID"), "java.lang.String", false, false, null);
            addPropertyClass(TYPE_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.typeprop", "Type"), "java.lang.Integer", false, false, OwStandardHistoryManager.getEventTypeEnum());
            addPropertyClass(STATUS_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.statusprop", "Status"), "java.lang.Integer", false, false, OwStandardHistoryManager.getEventStatusEnum());
            addPropertyClass(SUMMARY_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.summaryprop", "Summary"), "java.lang.String", false, false, null);
            addPropertyClass(USER_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.userprop", "User"), "java.lang.String", false, false, null);

            addPropertyClass(OBJECTS_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.objects", "Object"), "com.wewebu.ow.server.ecm.OwObject", true, false, null);
            addPropertyClass(PARENT_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.parentobject", "Parent Object"), "com.wewebu.ow.server.ecm.OwObject", false, false, null);
            addPropertyClass(MODIFIED_PROPS_PROPERTY, new OwString("historyimpl.OwStandardHistoryEntryObjectClass.modifiedproperties", "Modified Properties"), "com.wewebu.ow.server.history.OwHistoryModifiedPropertyValue", true, false, null);
        }

        /** add a new property class description */
        protected void addPropertyClass(String strName_p, OwString displayName_p, String strJavaClassName_p, boolean fArray_p, boolean fNameProperty_p, OwEnumCollection enums_p)
        {
            m_PropertyClassesMap.put(strName_p, new OwStandardHistoryEntryPropertyClass(strName_p, displayName_p, strJavaClassName_p, fArray_p, fNameProperty_p, enums_p));
        }
    }

    /** class description of history entry */
    private static OwStandardObjectClass m_ClassDescription = new OwStandardHistoryEntryObjectClass();

    /** properties */
    private OwPropertyCollection m_properties = new OwStandardPropertyCollection();

    /** construct a history entry for object modifying events
     *
     * @param time_p time of the event
     * @param id_p pluginid or function name the invoked event
     * @param iType_p type of the event as specified in OwEventManager.HISTORY_EVENT_TYPE_...
     * @param iStatus_p type of the event as specified in OwEventManager.HISTORY_STATUS_...
     * @param strSummary_p of the event as specified in OwEvent
     * @param parent_p OwObjectReference as specified in OwHistoryObjectChangeEvent
     * @param objects_p Collection of modified OwObjectReference's as specified in OwHistoryObjectChangeEvent
     *
     */
    public OwStandardHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int iType_p, int iStatus_p, String strSummary_p, String strUser_p, OwObjectReference parent_p, Collection objects_p) throws Exception
    {
        String name = getNameFromID(context_p, id_p, iType_p);
        setProperty(OwStandardHistoryEntryObjectClass.NAME_PROPERTY, name);
        setProperty(OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY, name + ": " + strSummary_p);

        setProperty(OwStandardHistoryEntryObjectClass.TIME_PROPERTY, time_p);
        setProperty(OwStandardHistoryEntryObjectClass.ID_PROPERTY, id_p);
        setProperty(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, Integer.valueOf(iType_p));
        setProperty(OwStandardHistoryEntryObjectClass.STATUS_PROPERTY, Integer.valueOf(iStatus_p));
        setProperty(OwStandardHistoryEntryObjectClass.USER_PROPERTY, strUser_p);

        setProperty(OwStandardHistoryEntryObjectClass.PARENT_PROPERTY, parent_p);
        setProperty(OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY, objects_p.toArray());
        setProperty(OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, null);
    }

    /** construct a history entry for property modifying events
     *
     * @param time_p time of the event
     * @param id_p pluginid or function name the invoked event
     * @param iType_p type of the event as specified in OwEventManager.HISTORY_EVENT_TYPE_...
     * @param iStatus_p type of the event as specified in OwEventManager.HISTORY_STATUS_...
     * @param strSummary_p of the event as specified in OwEvent
     * @param object_p modified OwObjectReference as specified in OwHistoryPropertyChangeEvent
     * @param fielddefinitionprovider_p OwFieldDefinitionProvider to lookup the property definitions
     * @param sResource_p String name of the resource to lookup the property definitions, or null to use default resource
     * @param propertycardinalitiesandnames_p Collection of modified property cardinality and names as specified in CARDINALITY_.... Each cardinality follows a propertyname.
     * 			in case of an array, the values are delimited with OwEscapedStringTokenizer.STANDARD_DELIMITER
     * @param oldProperties_p Collection of modified string values specified in OwHistoryPropertyChangeEvent
     * @param newProperties_p Collection of modified string values as specified in OwHistoryPropertyChangeEvent
     *
     */
    public OwStandardHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int iType_p, int iStatus_p, String strSummary_p, String strUser_p, OwObjectReference object_p, OwFieldDefinitionProvider fielddefinitionprovider_p,
            String sResource_p, Collection propertycardinalitiesandnames_p, Collection oldProperties_p, Collection newProperties_p) throws Exception
    {
        String name = getNameFromID(context_p, id_p, iType_p);
        setProperty(OwStandardHistoryEntryObjectClass.NAME_PROPERTY, name);
        setProperty(OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY, name + ": " + strSummary_p);

        setProperty(OwStandardHistoryEntryObjectClass.TIME_PROPERTY, time_p);
        setProperty(OwStandardHistoryEntryObjectClass.ID_PROPERTY, id_p);
        setProperty(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, Integer.valueOf(iType_p));
        setProperty(OwStandardHistoryEntryObjectClass.STATUS_PROPERTY, Integer.valueOf(iStatus_p));
        setProperty(OwStandardHistoryEntryObjectClass.USER_PROPERTY, strUser_p);

        setProperty(OwStandardHistoryEntryObjectClass.PARENT_PROPERTY, null);
        setProperty(OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY, new Object[] { object_p });

        // create the modified property value array
        Iterator itNames = propertycardinalitiesandnames_p.iterator();
        Iterator itOld = oldProperties_p.iterator();
        Iterator itNew = newProperties_p.iterator();

        Vector props = new Vector();

        while (itNames.hasNext() && itNew.hasNext())
        {
            String sCardinality = (String) itNames.next();
            String sName = (String) itNames.next();

            String sNew = (String) itNew.next();

            String sOld = null;
            if (itOld.hasNext())
            {
                sOld = (String) itOld.next();
            }

            boolean fIsArray = sCardinality.equals(CARDINALITY_ARRAY);

            props.add(new OwStandardHistoryModifiedPropertyValue(sName, fIsArray, sOld, sNew, fielddefinitionprovider_p, sResource_p));
        }

        if (props.size() > 0)
        {
            setProperty(OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, props.toArray());
        }
        else
        {
            setProperty(OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, null);
        }
    }

    /** construct a history entry for non modifying or generic events
     *
     * @param time_p time of the event
     * @param id_p pluginid or function name the invoked event
     * @param iType_p type of the event as specified in OwEventManager.HISTORY_EVENT_TYPE_...
     * @param iStatus_p type of the event as specified in OwEventManager.HISTORY_STATUS_...
     * @param strSummary_p of the event as specified in OwEvent
     *
     */
    public OwStandardHistoryEntry(OwHistoryManagerContext context_p, Date time_p, String id_p, int iType_p, int iStatus_p, String strSummary_p, String strUser_p) throws Exception
    {
        String name = getNameFromID(context_p, id_p, iType_p);
        setProperty(OwStandardHistoryEntryObjectClass.NAME_PROPERTY, name);
        setProperty(OwStandardHistoryEntryObjectClass.SUMMARY_PROPERTY, name + ": " + strSummary_p);

        setProperty(OwStandardHistoryEntryObjectClass.TIME_PROPERTY, time_p);
        setProperty(OwStandardHistoryEntryObjectClass.ID_PROPERTY, id_p);
        setProperty(OwStandardHistoryEntryObjectClass.TYPE_PROPERTY, Integer.valueOf(iType_p));
        setProperty(OwStandardHistoryEntryObjectClass.STATUS_PROPERTY, Integer.valueOf(iStatus_p));
        setProperty(OwStandardHistoryEntryObjectClass.USER_PROPERTY, strUser_p);

        setProperty(OwStandardHistoryEntryObjectClass.PARENT_PROPERTY, null);
        setProperty(OwStandardHistoryEntryObjectClass.OBJECTS_PROPERTY, null);
        setProperty(OwStandardHistoryEntryObjectClass.MODIFIED_PROPS_PROPERTY, null);
    }

    /** generate a display name out of the given pluginid or function name and type
     */
    private String getNameFromID(OwHistoryManagerContext context_p, String strID_p, int iType_p) throws Exception
    {
        switch (iType_p)
        {
            case OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_DSPATCH:
            case OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_EDIT:
            case OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_UI:
            case OwEventManager.HISTORY_EVENT_TYPE_PLUGIN_INVOKE_VIEW:
            {
                return context_p.getBaseConfiguration().getLocalizedPluginTitle(strID_p);
            }

            default:
                return OwStandardHistoryManager.getEventIDDisplayName(context_p.getLocale(), strID_p);
        }
    }

    /** set a property in the history object
     */
    protected void setProperty(String strClassName_p, Object value_p) throws Exception
    {
        OwPropertyClass propClass = getObjectClass().getPropertyClass(strClassName_p);
        m_properties.put(propClass.getClassName(), new OwStandardProperty(value_p, propClass));
    }

    public void add(com.wewebu.ow.server.ecm.OwObject oObject_p) throws Exception
    {
    }

    public boolean canAdd(com.wewebu.ow.server.ecm.OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canDelete(int iContext_p) throws Exception
    {
        return false;
    }

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

    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    public boolean canLock() throws Exception
    {
        return false;
    }

    public boolean canMove(com.wewebu.ow.server.ecm.OwObject oObject_p, com.wewebu.ow.server.ecm.OwObject oldParent_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canRemoveReference(com.wewebu.ow.server.ecm.OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canSetPermissions() throws Exception
    {
        return false;
    }

    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean canUndo() throws Exception
    {
        return false;
    }

    public void delete() throws Exception
    {
    }

    public com.wewebu.ow.server.ecm.OwObjectCollection getChilds(int[] iObjectTypes_p, java.util.Collection propertyNames_p, com.wewebu.ow.server.field.OwSort sort_p, int iMaxSize_p, int iVersionSelection_p,
            com.wewebu.ow.server.field.OwSearchNode filterCriteria_p) throws Exception
    {
        return null;
    }

    public String getClassName()
    {
        return getObjectClass().getClassName();
    }

    public com.wewebu.ow.server.ecm.OwPermissionCollection getClonedPermissions() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwPropertyCollection getClonedProperties(java.util.Collection strPropertyNames_p) throws Exception
    {
        return OwStandardPropertyClass.getClonedProperties(this, strPropertyNames_p);
    }

    public java.util.Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwContentCollection getContentCollection() throws Exception
    {
        return null;
    }

    public String getDMSID() throws Exception
    {
        return null;
    }

    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT;
    }

    /** get a name that identifies the field provider, can be used to create IDs 
     * 
     * @return String unique ID / Name of fieldprovider
     */
    public String getFieldProviderName()
    {
        return getName();
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
     * @param iContext_p OwStatusContextDefinitions
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    public String getMIMEType() throws Exception
    {
        return "ow_history/" + getObjectClass().getClassName();
    }

    public String getName()
    {
        try
        {
            return (String) getProperty(getObjectClass().getNamePropertyName()).getValue();
        }
        catch (Exception e)
        {
            return "[undef]";
        }
    }

    public Object getNativeObject() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwObjectClass getObjectClass()
    {
        return m_ClassDescription;
    }

    public static com.wewebu.ow.server.ecm.OwObjectClass getStaticObjectClass() throws Exception
    {
        return m_ClassDescription;
    }

    public int getPageCount() throws Exception
    {
        return 0;
    }

    public com.wewebu.ow.server.ecm.OwObjectCollection getParents() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwPropertyCollection getProperties(java.util.Collection propertyNames_p) throws Exception
    {
        return m_properties;
    }

    public com.wewebu.ow.server.ecm.OwProperty getProperty(String strPropertyName_p) throws Exception
    {
        OwProperty prop = (OwProperty) m_properties.get(strPropertyName_p);
        if (null == prop)
        {
            throw new OwObjectNotFoundException("OwStandardHistoryEntry.getProperty: Cannot find the property, propertyName = " + strPropertyName_p);
        }

        return prop;
    }

    public com.wewebu.ow.server.ecm.OwResource getResource() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.field.OwSearchTemplate getSearchTemplate() throws Exception
    {
        return null;
    }

    public String getID()
    {
        return null;
    }

    public int getType()
    {
        return OwObjectReference.OBJECT_TYPE_HISTORY;
    }

    public com.wewebu.ow.server.ecm.OwVersion getVersion() throws Exception
    {
        return null;
    }

    public com.wewebu.ow.server.ecm.OwVersionSeries getVersionSeries() throws Exception
    {
        return null;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p OwStatusContextDefinitions
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    public boolean hasVersionSeries() throws Exception
    {
        return false;
    }

    public void move(com.wewebu.ow.server.ecm.OwObject oObject_p, com.wewebu.ow.server.ecm.OwObject oldParent_p) throws Exception
    {
    }

    public void refreshProperties() throws Exception
    {

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

    public void removeReference(com.wewebu.ow.server.ecm.OwObject oObject_p) throws Exception
    {
    }

    public void setContentCollection(com.wewebu.ow.server.ecm.OwContentCollection content_p) throws Exception
    {
    }

    public boolean setLock(boolean fLock_p) throws Exception
    {
        return false;
    }

    public void setPermissions(com.wewebu.ow.server.ecm.OwPermissionCollection permissions_p) throws Exception
    {
    }

    public void setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection properties_p) throws Exception
    {
    }

    public void undo() throws Exception
    {
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
      * Unlike the symbol name and the dmsid, the path is not necessarily unique,
      * but provids a readable information of the objects location.
      */
    public String getPath() throws Exception
    {
        throw new OwNotSupportedException("OwStandardHistoryEntry.getPath: Not implemented.");
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object type (folder or document)
    * @param iContext_p OwStatusContextDefinitions
    * 
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return 0;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p String class name
     * @param properties_p OwPropertyCollection (optional, can be null to set previous properties)
     * @param permissions_p OwPermissionCollection (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwStandardHistoryEntry.changeClass: Not implemented.");
    }

    /** check if object can change its class */
    public boolean canChangeClass() throws Exception
    {
        return false;
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
            throw new OwObjectNotFoundException("OwStandardHistoryEntry.getResourceID: Resource Id not found", e);
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