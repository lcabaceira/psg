package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.util.Collection;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyClass;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardProperty;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyCollection;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemProcessorInfo;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.event.OwEventManager;
import com.wewebu.ow.server.exceptions.OwConfigurationException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;

import filenet.vw.api.VWDataField;
import filenet.vw.api.VWException;
import filenet.vw.api.VWFieldType;
import filenet.vw.api.VWModeType;
import filenet.vw.api.VWStepProcessorInfo;

/**
 *<p>
 * FileNet BPM Plugin.<br/>
 * A single workitem.
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
public abstract class OwFNBPM5WorkItem implements OwWorkitem
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5WorkItem.class);

    /** property for the JSP page to be used as a stepprocessor form */
    public static final String STEPPROCESSOR_JSP_PAGE_PROPERTY = "OW_STEPPROCESSOR_JSP_PAGE";

    /** the class description of the workitems */
    private OwObjectClass m_ClassDescription;

    /** properties */
    protected OwPropertyCollection m_properties = new OwStandardPropertyCollection();

    /** reference to the containing queue */
    private OwFNBPM5BaseContainer m_queue;

    /** construct a work item wrapper 
     *
     * NOTE:    There are two kinds of datafiles
     *
     *          - System defined, which are contained in the queue tables 
     *            and can be accessed very fast. They may be accessed through a VWQueueElement.
     *
     *          - As well as user defined values (designer defined), 
     *            which have to be accessed through a VWWorkObject
     */
    public OwFNBPM5WorkItem(OwFNBPM5BaseContainer queue_p) throws Exception
    {
        m_queue = queue_p;
        m_ClassDescription = queue_p.getChildObjectClass();
    }

    /** adds a object reference to this parent object / folder
     *  @param oObject_p OwObject reference to add to
     */
    public void add(OwObject oObject_p) throws Exception
    {
    }

    /** add a generic value as a property 
     */
    protected void addSystemProperty(String strClassName_p, Object value_p) throws Exception
    {
        OwPropertyClass propClass = getObjectClass().getPropertyClass(strClassName_p);
        m_properties.put(propClass.getClassName(), new OwStandardProperty(value_p, propClass));
    }

    /** add a VWDatafield as a property 
    */
    protected void addSystemProperty(VWDataField datafield_p) throws Exception
    {
        OwPropertyClass propClass = OwFNBPM5WorkItemObjectClass.createPropertyClass(datafield_p.getName(), datafield_p.getFieldType(), datafield_p.getMode(), datafield_p.getIsSystemField(), datafield_p.isArray(), true, null);
        OwProperty prop = null;

        if (OwFNBPM5BaseContainer.getUserProperties().contains(datafield_p.getName()))
        {
            prop = new OwFNBPM5UserProperty(m_queue, propClass, ((Integer) datafield_p.getValue()).longValue());
        }
        else
        {
            switch (datafield_p.getFieldType())
            {

                case VWFieldType.FIELD_TYPE_PARTICIPANT:
                    prop = new OwFNBPM5ParticipantProperty(getQueue(), propClass, datafield_p);
                    break;

                case VWFieldType.FIELD_TYPE_ATTACHMENT:
                    prop = new OwFNBPM5AttachmentProperty(getQueue(), propClass, datafield_p);
                    break;

                default:
                    prop = new OwStandardProperty(datafield_p.getValue(), propClass);
                    break;

            }
        }

        addProperty(propClass, prop);
    }

    protected void addDatafieldBasedProperty(String workClassName_p, VWDataField datafield_p) throws Exception
    {
        OwObjectClass wClass = getQueue().getRepository().getObjectClass(workClassName_p, null);
        OwPropertyClass propClass = null;
        try
        {
            propClass = createPropertyClass(wClass.getPropertyClass(datafield_p.getName()), datafield_p, true, true);
        }
        catch (OwObjectNotFoundException onfEx)
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("OwFNBPMWorkItem.addDatafieldBasedProperty: ===Missing=== " + datafield_p.getName() + " , " + datafield_p.getFieldType() + ", " + datafield_p.getMode());
            }
            propClass = OwFNBPM5WorkItemObjectClass.createPropertyClass(datafield_p.getName(), datafield_p.getFieldType(), datafield_p.getMode(), datafield_p.getIsSystemField(), datafield_p.isArray(), true, null);
        }

        OwProperty prop = null;

        if (OwFNBPM5BaseContainer.getUserProperties().contains(datafield_p.getName()))
        {
            prop = new OwFNBPM5UserProperty(m_queue, propClass, ((Integer) datafield_p.getValue()).longValue());
        }
        else
        {
            switch (datafield_p.getFieldType())
            {

                case VWFieldType.FIELD_TYPE_PARTICIPANT:
                    prop = new OwFNBPM5ParticipantProperty(getQueue(), propClass, datafield_p);
                    break;

                case VWFieldType.FIELD_TYPE_ATTACHMENT:
                    prop = new OwFNBPM5AttachmentProperty(getQueue(), propClass, datafield_p);
                    break;

                default:
                    prop = new OwStandardProperty(datafield_p.getValue(), propClass);
                    break;

            }
        }
        //Properties rule over data fields
        if (!m_properties.containsKey(prop.getPropertyClass().getClassName()))
        {
            addProperty(propClass, prop);
        }
    }

    protected OwFNBPM5RuntimePropertyClass createPropertyClass(OwPropertyClass propClass_p, VWDataField dataField_p, boolean isQueue_p, boolean isFilterable_p) throws Exception
    {
        if (dataField_p.getFieldType() != VWFieldType.FIELD_TYPE_ATTACHMENT && dataField_p.getFieldType() != VWFieldType.FIELD_TYPE_PARTICIPANT)
        {
            try
            {
                // OwFieldDefinition compatibleCEClass = getQueue().getDmsCompatiblePropertyClass(propClass_p.getClassName());
                return createPropertyClass(propClass_p, dataField_p, isQueue_p, isFilterable_p, null);//compatibleCEClass);
            }
            catch (OwObjectNotFoundException nfEx)
            {
                //ignore there is no field with the same name for that field in CE
            }
        }
        return createPropertyClass(propClass_p, dataField_p, isQueue_p, isFilterable_p, null);
    }

    protected OwFNBPM5RuntimePropertyClass createPropertyClass(OwPropertyClass propClass_p, VWDataField dataField_p, boolean isQueue_p, boolean isFilterable_p, OwFieldDefinition compatibleCEClass_p) throws Exception
    {
        return createPropertyClass(propClass_p, dataField_p.getMode(), dataField_p.getIsSystemField(), isQueue_p, isFilterable_p);//, compatibleCEClass_p);
    }

    protected OwFNBPM5RuntimePropertyClass createPropertyClass(OwPropertyClass propClass_p, int ifieldMode_p, boolean isSystemProp_p, boolean isQueue_p, boolean isFilterable_p) throws Exception
    {
        if (!isSystemProp_p)
        {//AWD specific property which should be handled as system property
            isSystemProp_p = OwFNBPM5BaseContainer.RESUBMIT_DATE_PROPERTY_NAME.equals(propClass_p.getClassName());
        }
        // === normal property
        OwFNBPM5RuntimePropertyClass propClass = null;
        switch (ifieldMode_p)
        {
            case VWModeType.MODE_TYPE_IN_OUT:
                propClass = new OwFNBPM5RuntimePropertyClass(propClass_p, false, OwFNBPM5StandardWorkItemPropertyClass.FILTER_OW_ELEMENTS.contains(propClass_p.getClassName()), isSystemProp_p, isQueue_p, isFilterable_p);
                break;

            case VWModeType.MODE_TYPE_IN:
                propClass = new OwFNBPM5RuntimePropertyClass(propClass_p, true, OwFNBPM5StandardWorkItemPropertyClass.FILTER_OW_ELEMENTS.contains(propClass_p.getClassName()), isSystemProp_p, isQueue_p, isFilterable_p);
                break;

            case VWModeType.MODE_TYPE_OUT:
                propClass = new OwFNBPM5RuntimePropertyClass(propClass_p, false, true, isSystemProp_p, isQueue_p, isFilterable_p);
                break;
            default://return null
                ;
        }
        return propClass;
    }

    protected void addProperty(OwPropertyClass propClass_p, OwProperty prop_p)
    {
        // Map
        m_properties.put(propClass_p.getClassName(), prop_p);

        // === some properties are mapped with there corresponding ECM parameter name
        // Map CE Properties
        String strCEName = OwFNBPM5BaseContainer.getCorrespondingCEPropertyName(propClass_p.getClassName());
        if (null != strCEName)
        {
            m_properties.put(strCEName, prop_p);
        }
    }

    /** checks if object supports add function
     *
     * @param oObject_p OwObject reference to be added
     *
     * @return true if object supports add function
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** check if object can change its class */
    public boolean canChangeClass() throws Exception
    {
        return false;
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

    /** check if content retrieval is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true if allowed
     */
    public boolean canGetContent(int iContentType_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public boolean canGetPermissions() throws Exception
    {
        return false;
    }

    /** check if property retrieval is allowed 
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return true if allowed
     */
    public boolean canGetProperties(int iContext_p) throws Exception
    {
        return false;
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

    /** checks if the reference can be removed
     *  this object needs to be parent of given object, and user needs to have sufficient access rights
     * @param oObject_p OwObject reference to be checked upon
     * @return true, if given OwObject reference can be removed from this object (folder)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p) throws Exception
    {
        return false;
    }

    // === Implementation of OwObject interface    

    /** check if you can set a response
     */
    public abstract boolean canResponse();

    /** check if content can be set on this document with setContent
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true, if content can be set with setContent
     */
    public boolean canSetContent(int iContentType_p, int iContext_p) throws Exception
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

    /** check if object allows to set / change properties
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return true if allowed
     */
    public boolean canSetProperties(int iContext_p) throws Exception
    {
        return true;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p the <code>String</code> class name 
     * @param properties_p {@link OwPropertyCollection}  (optional, can be null to set previous properties)
     * @param permissions_p {@link OwPermissionCollection}  (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwFNBPMWorkItem.changeClass: Not implemented.");
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return 0;
    }

    /** get the children of the object, does NOT cache the returned object
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from DMS system along with the children, can be null.
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

    /** get the class name of the object, the class names are defined by the ECM System
     * @return class name of object class
     */
    public String getClassName()
    {
        return m_ClassDescription.getClassName();
    }

    /** get the cloned permissions
     *
     * @return OwPermissionCollection clone of the object
     */
    public OwPermissionCollection getClonedPermissions() throws Exception
    {
        return null;
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

    /** get the column info list that describes the columns for the child list
     * @return List of OwSearchTemplate.OwObjectColumnInfos, or null if not defined
     */
    public Collection getColumnInfoList() throws Exception
    {
        return null;
    }

    /** get the content of the object
     *
     * @return OwContentCollection
     */
    public OwContentCollection getContentCollection() throws Exception
    {
        return null;
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

    /** get the type of field provider as defined with TYPE_... */
    public int getFieldProviderType()
    {
        return OwFieldProvider.TYPE_META_OBJECT;
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

    /** get a instance from this reference
     * 
     * @return OwObject or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     */
    public OwObject getInstance() throws Exception
    {
        return this;
    }

    /** retrieve the MIME parameter for the specified page number
     *
     * @return String Mimeparameter
     */
    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    /** get Object name property string
     * @return the name property string of the object
     */
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

    /** get the native stepprocessor info
     * 
     * @return a {@link VWStepProcessorInfo}
     */
    protected abstract VWStepProcessorInfo getNativeStepProcessorInfo() throws VWException;

    /** get the class description of the object, the class descriptions are defined by the ECM System
     * @return class description name of object class
     */
    public OwObjectClass getObjectClass()
    {
        return m_ClassDescription;
    }

    /** retrieve the number of pages in the objects
     * @return number of pages
     */
    public int getPageCount() throws Exception
    {
        return 0;
    }

    /** get the containing parent of this object, does NOT cache returned objects
     * @return OwObjectCollection with parent objects, or null if no parents are available
     */
    public OwObjectCollection getParents() throws Exception
    {
        OwObjectCollection retList = getQueue().getRepository().createObjectCollection(); // new OwStandardObjectCollection();
        retList.add(m_queue);

        return retList;
    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the dmsid, the path is not necessarily unique,
     * but provids a readable information of the objects location.
     */
    public String getPath() throws Exception
    {
        StringBuffer ret = new StringBuffer();

        ret.append(getQueue().getPath());
        ret.append(OwWorkitemRepository.PATH_DELIMITER);
        ret.append(getName());

        return ret.toString();
    }

    /** get the permissions object
     *
     * @return OwPermissionCollection of the object
     */
    public OwPermissionCollection getPermissions() throws Exception
    {
        return null;
    }

    /** get reference to the containing queue
     */
    protected OwFNBPM5BaseContainer getQueue()
    {
        return m_queue;
    }

    /** get the queue name
     * @return String representing the name of the current queue
     */
    protected abstract String getQueueName() throws Exception;

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public OwResource getResource() throws Exception
    {
        return null;
    }

    /** get the ID / name identifying the resource the object belongs to
     * 
     * @return String ID of resource or throws OwObjectNotFoundException
     * @throws Exception, OwObjectNotFoundException
     * @see OwResource
     * @see OwObjectReference#getResourceID()
     */
    public String getResourceID() throws Exception
    {
        return getQueue().getResourceID();
    }

    /** get a response
     */
    public abstract String getResponse() throws Exception;

    /** get a list of possible responses
     * @return Collection of OwEnum
     */
    public abstract Collection getResponses() throws Exception;

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
        // file objects do not support search templates
        return null;
    }

    /** get a stepprocessor info class for the work item
     * @return OwFNBPMStepProcessorInfo
     */
    public OwWorkitemProcessorInfo getStepProcessorInfo() throws OwObjectNotFoundException
    {
        String strJspPage = "";
        try
        {
            strJspPage = getProperty(STEPPROCESSOR_JSP_PAGE_PROPERTY).getValue().toString();
        }
        catch (Exception e)
        {
            // ignore
        }

        try
        {
            if (strJspPage.length() == 0)
            {
                return new OwFNBPMStepProcessorInfoWorkItem(this, getNativeStepProcessorInfo());
            }
            else
            {
                return new OwFNBPMStepProcessorInfoWorkItem(this, getNativeStepProcessorInfo(), strJspPage);
            }
        }
        catch (VWException e)
        {
            throw new OwObjectNotFoundException("OwFNBPMWorkItem.getStepProcessorInfo", e);
        }
    }

    /** get the current version object 
    * 
    * @return OwVersion Object identifying the currently set version, or null if versions not supported
    */
    public OwVersion getVersion() throws Exception
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

    /** check if object has children
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        return false;
    }

    /** check if the object contains a content, which can be retrieved using getContentCollection 
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     *
     * @return boolean true = object contains content, false = object has no content
     */
    public boolean hasContent(int iContext_p) throws Exception
    {
        return false;
    }

    /** check if a version series object is available, i.e. the object is versionable
     * @return true if object is versionable
     */
    public boolean hasVersionSeries() throws Exception
    {
        // File documents do not support versioning
        return m_ClassDescription.hasVersionSeries();
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

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     * @throws Exception
     */
    public void refreshProperties(java.util.Collection props_p) throws Exception
    {
        refreshProperties();
    }

    /** removes the reference of the given object from this object (folder)
     *  this object needs to be parent of given object
     * @param oObject_p OwObject reference to be removed from this object (folder)
     */
    public void removeReference(OwObject oObject_p) throws Exception
    {
        throw new OwNotSupportedException("OwFNBPMWorkItem.removeReference: Not supported, removeReference = " + getName());
    }

    /** set the content to the object
     *
     * @param content_p OwContentCollection to store in the object
    */
    public void setContentCollection(OwContentCollection content_p) throws Exception
    {
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

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public void setPermissions(OwPermissionCollection permissions_p) throws Exception
    {
        // signal event for history
        getQueue().getRepository().getEventManager().addEvent(OwEventManager.HISTORY_EVENT_TYPE_OBJECT, OwEventManager.HISTORY_EVENT_ID_OBJECT_MODIFY_PERMISSIONS, OwEventManager.HISTORY_STATUS_OK);
    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

    /** set a response
     */
    public abstract void setResponse(String strResponse_p) throws Exception;

    /**
     *<p>
     * Implementation of the stepprocessor interface.
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
    public static class OwFNBPMStepProcessorInfoWorkItem implements OwWorkitemProcessorInfo
    {
        /** reference to the native P8 stepprocessor */
        private VWStepProcessorInfo m_nativeinfo;
        private OwFNBPM5WorkItem m_parent;
        /** path to a JSP page to use */
        private String m_strJspPage;

        /** construct a step processor info */
        public OwFNBPMStepProcessorInfoWorkItem(OwFNBPM5WorkItem parent_p, VWStepProcessorInfo nativeinfo_p)
        {
            m_nativeinfo = nativeinfo_p;
            m_parent = parent_p;
        }

        /** construct a step processor info */
        public OwFNBPMStepProcessorInfoWorkItem(OwFNBPM5WorkItem parent_p, VWStepProcessorInfo nativeinfo_p, String strJspPage_p)
        {
            m_strJspPage = strJspPage_p;
            m_parent = parent_p;
        }

        private String getBaseURL() throws Exception
        {
            String sURL = m_parent.getQueue().getRepository().getConfigNode().getSafeTextValue("WorkplaceRedirectURL", null);

            if (sURL == null)
            {
                String msg = "OwFNBPMWorkItem$OwFNBPMStepProcessorInfoWorkItem.getBaseURL: Please define WorkplaceRedirectURL in bootstrap.";
                LOG.fatal(msg);
                throw new OwConfigurationException(msg);
            }

            return sURL;
        }

        /** get the context of the stepprocessor as defined in STEPPROCESSOR_CONTEXT_...*/
        public int getContextType()
        {
            switch (m_nativeinfo.getProcessorType())
            {
                case VWStepProcessorInfo.PROCESSOR_LAUNCH_DEFAULT:
                case VWStepProcessorInfo.PROCESSOR_LAUNCH:
                    return STEPPROCESSOR_CONTEXT_LAUNCH;

                case VWStepProcessorInfo.PROCESSOR_STEP_DEFAULT:
                case VWStepProcessorInfo.PROCESSOR_STEP:
                    return STEPPROCESSOR_CONTEXT_STEP;

                default:
                    return STEPPROCESSOR_CONTEXT_UNKNOWN;
            }
        }

        /** get the name  of the stepprocessor, or null if not defined */
        public String getDisplayName(Locale locale_p)
        {
            if (null != m_nativeinfo)
            {
                return m_nativeinfo.getName();
            }

            return null;
        }

        /** get the height of the processor, or 0 if not defined */
        public int getHeight()
        {
            if (m_strJspPage != null)
            {
                return 0;
            }

            if (null != m_nativeinfo)
            {
                return m_nativeinfo.getHeight();
            }

            return 0;
        }

        /** get the ID  of the stepprocessor, or null if not defined */
        public String getID()
        {
            if (m_strJspPage != null)
            {
                return null;
            }

            if (null != m_nativeinfo)
            {
                return String.valueOf(m_nativeinfo.getId());
            }

            return null;
        }

        /** get the name of a JSP form to use
         * @return path to a JSP formpage, or null if no form is associated
         */
        public String getJspFormPage()
        {
            return m_strJspPage;
        }

        /** get the native processor object, or null if not available */
        public Object getNativeProcessor()
        {
            if (m_strJspPage != null)
            {
                return null;
            }

            return m_nativeinfo;
        }

        /** get the specified property
         * 
         * @param name_p
         * @return the <code>String</code> property value 
         * @throws Exception
         */
        private String getPropertyValue(String name_p) throws Exception
        {
            return m_parent.getProperty(name_p).getValue().toString();
        }

        /** get the script to the processor to use
         * @return script command, or null if no script is associated
         */
        public String getScript() throws Exception
        {
            StringBuffer ret = new StringBuffer();

            ret.append("var x=(screen.width-");
            ret.append(getWidth());
            ret.append(")/2;");
            ret.append("var y=(screen.height-");
            ret.append(getHeight());
            ret.append(")/2;");
            ret.append("var f='resizable=yes,scrollbars=yes,status=yes,width=");
            ret.append(getWidth());
            ret.append(",height=");
            ret.append(getHeight());
            ret.append(",top='+y+',left='+x;");
            ret.append("var w=window.open('");

            ret.append(getURL());

            ret.append("','");
            ret.append(m_parent.getName());
            ret.append("',f);");

            ret.append("w.focus();");

            return ret.toString();
        }

        /** get the type of the stepprocessor as defined in STEPPROCESSOR_TYPE_...*/
        public int getType()
        {
            if (m_strJspPage != null)
            {
                return STEPPROCESSOR_TYPE_JSP_FORM;
            }

            switch (m_nativeinfo.getAppType())
            {
                case VWStepProcessorInfo.APP_TYPE_URL:
                case VWStepProcessorInfo.APP_TYPE_JAVA:
                    // both a script command and a URL is available to launch the processor
                    return STEPPROCESSOR_TYPE_JAVASCRIPT_AND_URL;

                default:
                    return STEPPROCESSOR_TYPE_UNKNOWN;
            }
        }

        /** get the URL to the processor to use
         * @return url, or null if no URL is associated
         */
        public String getURL() throws Exception
        {
            StringBuffer ret = new StringBuffer();

            try
            {
                ret.append(getBaseURL());
                ret.append("/getProcessor?processorType=");

                if (getContextType() == STEPPROCESSOR_CONTEXT_LAUNCH)
                {
                    ret.append("launch");
                }
                else
                {
                    ret.append("step");
                }

                ret.append("&queueName=");
                ret.append(getPropertyValue("F_QueueName"));
                ret.append("&wobNum=");
                ret.append(getPropertyValue("F_WobNum"));
                ret.append("&stepName=");
                ret.append(getPropertyValue("F_StepName"));
                ret.append("&stepProcId=");
                ret.append(this.m_nativeinfo.getId());
                ret.append("&ut=");
                ret.append(m_parent.getQueue().getRepository().getSecurityToken(null));
            }
            catch (Exception e)
            {
                return null;
            }

            return ret.toString();
        }

        /** get the width of the processor, or 0 if not defined */
        public int getWidth()
        {
            if (m_strJspPage != null)
            {
                return 0;
            }

            if (null != m_nativeinfo)
            {
                return m_nativeinfo.getWidth();
            }

            return 0;
        }

    }

}