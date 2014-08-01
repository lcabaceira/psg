package com.wewebu.ow.server.ecmimpl.fncm5.bpm;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.wewebu.ow.server.collections.OwIterable;
import com.wewebu.ow.server.collections.OwLoadContext;
import com.wewebu.ow.server.collections.OwPageableObject;
import com.wewebu.ow.server.ecm.OwContentCollection;
import com.wewebu.ow.server.ecm.OwNetworkContext;
import com.wewebu.ow.server.ecm.OwObject;
import com.wewebu.ow.server.ecm.OwObjectClass;
import com.wewebu.ow.server.ecm.OwObjectCollection;
import com.wewebu.ow.server.ecm.OwObjectReference;
import com.wewebu.ow.server.ecm.OwPermissionCollection;
import com.wewebu.ow.server.ecm.OwProperty;
import com.wewebu.ow.server.ecm.OwPropertyCollection;
import com.wewebu.ow.server.ecm.OwResource;
import com.wewebu.ow.server.ecm.OwStandardObjectClass;
import com.wewebu.ow.server.ecm.OwStandardPropertyClass;
import com.wewebu.ow.server.ecm.OwStatusContextDefinitions;
import com.wewebu.ow.server.ecm.OwStatusContextException;
import com.wewebu.ow.server.ecm.OwVersion;
import com.wewebu.ow.server.ecm.OwVersionSeries;
import com.wewebu.ow.server.ecm.bpm.OwWorkitem;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemContainer;
import com.wewebu.ow.server.ecm.bpm.OwWorkitemRepository;
import com.wewebu.ow.server.ecmimpl.fncm5.OwFNCM5Network;
import com.wewebu.ow.server.ecmimpl.fncm5.log.OwLog;
import com.wewebu.ow.server.exceptions.OwException;
import com.wewebu.ow.server.exceptions.OwInvalidOperationException;
import com.wewebu.ow.server.exceptions.OwNotSupportedException;
import com.wewebu.ow.server.exceptions.OwObjectNotFoundException;
import com.wewebu.ow.server.field.OwField;
import com.wewebu.ow.server.field.OwFieldDefinition;
import com.wewebu.ow.server.field.OwFieldProvider;
import com.wewebu.ow.server.field.OwSearchCriteria;
import com.wewebu.ow.server.field.OwSearchNode;
import com.wewebu.ow.server.field.OwSearchSQLOperator;
import com.wewebu.ow.server.field.OwSearchTemplate;
import com.wewebu.ow.server.field.OwSort;
import com.wewebu.ow.server.util.OwString1;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;

/**
 *<p>
 * FileNet BPM Plugin. OwObject implementation for queues.
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
public abstract class OwFNBPM5BaseContainer implements OwWorkitemContainer, OwPageableObject<OwObject>
{
    /** package logger for the class */
    private static final Logger LOG = OwLog.getLogger(OwFNBPM5BaseContainer.class);

    /** static class to map content engine property names to BPM names,
     so we can reuse search templates and resultlist configuration 
     */
    private static OwInternalCEBPMPropertyMapping m_InternalCEBPMPropertyMapping = new OwInternalCEBPMPropertyMapping();

    /** static class for property names  that contain user IDs 
     */
    private static OwInternalUserProperties m_InternalUserProperties = new OwInternalUserProperties();

    /** property name for the resubmit property used to filter items which are resubmitted 
     * NOTE: This property must be declared exposed in the roster as type Time.
     */
    public static final String RESUBMIT_DATE_PROPERTY_NAME = "OW_RESUBMIT_DATE";

    /** the class description of the workitems 
     * NOTE: this is not the class description of this object
     */
    private OwFNBPM5WorkItemObjectClass m_ChildClassDescription = new OwFNBPM5WorkItemObjectClass();

    /** current filter type used in getChilds, see also setFilter */
    private int m_iFilterType = FILTER_TYPE_NORMAL;

    /** a reference to the network */
    private OwFNBPM5Repository m_repository;

    /** resource to find a compatible property class description from the DMS system to resolve choicelists */
    private String m_slookupresource;

    public OwFNBPM5BaseContainer(OwFNBPM5Repository repository_p) throws Exception
    {
        m_repository = repository_p;

        // find a lookup objectstore to lookup choicelists / property descriptions
        m_slookupresource = m_repository.getConfigNode().getSafeTextValue("DefaultObjectStoreLookup", null);
    }

    /** adds a object reference to this parent object / folder
     *  @param oObject_p OwObject reference to add to
     */
    public void add(OwObject oObject_p) throws Exception
    {
    }

    /** checks if object supports add function
     *
     * @param oObject_p OwObject reference to be added
     *
     * @return true if object supports add function
     */
    public boolean canAdd(OwObject oObject_p, int iContext_p)
    {
        return false;
    }

    /** check if object can change its class */
    public boolean canChangeClass()
    {
        return false;
    }

    /** check if object can be deleted
     * @return true, if delete operation works on object
     */
    public boolean canDelete(int iContext_p)
    {
        return false;
    }

    /** check if the FilterCriteria_p in getChilds is possible
     * NOTE:    The FilterCriteria_p parameter in getChilds is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *          The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     *
     * @return true = filter children with FilterCriteria_p is possible, false = filter is not possible / ignored
     */
    public boolean canFilterChilds()
    {
        return true;
    }

    /** check if content retrieval is allowed 
     * @param iContentType_p int designating the type of content (CONTENT_TYPE_DOCUMENT, CONTENT_TYPE_ANNOTATION,...)
     * @return true if allowed
     */
    public boolean canGetContent(int iContentType_p, int iContext_p)
    {
        return false;
    }

    /** check if permissions are accessible
     *
     * @return true = permissions can be retrieved
     */
    public boolean canGetPermissions()
    {
        return false;
    }

    /** check if property retrieval is allowed 
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return true if allowed
     */
    public boolean canGetProperties(int iContext_p)
    {
        return true;
    }

    /** check if object supports lock mechanism
     * @return true, if object supports lock, i.e. the setLock function works
     */
    public boolean canLock()
    {
        return false;
    }

    /** check if move operation is allowed
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public boolean canMove(OwObject oObject_p, OwObject oldParent_p, int iContext_p)
    {
        return false;
    }

    /** check if container supports work item pull, see pull
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * 
     * @return boolean 
      * @throws Exception
     */
    public boolean canPull(int iContext_p) throws Exception
    {
        return false;
    }

    /** checks if the reference can be removed
     *  this object needs to be parent of given object, and user needs to have sufficient access rights
     * @param oObject_p OwObject reference to be checked upon
     * @return true, if given OwObject reference can be removed from this object (folder)
     */
    public boolean canRemoveReference(OwObject oObject_p, int iContext_p)
    {
        return false;
    }

    /** check if resubmission is supported by the queue
     */
    public boolean canResubmit() throws Exception
    {
        try
        {
            // lookup the resubmit property in the child classdescriptions
            m_ChildClassDescription.getPropertyClass(RESUBMIT_DATE_PROPERTY_NAME);
        }
        catch (OwObjectNotFoundException e)
        {
            return false;
        }

        return true;
    }

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
        return false;
    }

    /** change the class of the object
     * 
     * @param strNewClassName_p <code>String</code> 
     * @param properties_p an {@link OwPropertyCollection}  (optional, can be null to set previous properties)
     * @param permissions_p an {@link OwPermissionCollection}  (optional, can be null to set previous permissions)
     * 
     */
    public void changeClass(String strNewClassName_p, OwPropertyCollection properties_p, OwPermissionCollection permissions_p) throws Exception
    {
        throw new OwInvalidOperationException("OwFNBPMBaseContainer.changeClass: Not implemented.");
    }

    /** create a query for queues and rosters, 
     *  uses the given criteria and in addition to that minds the getFilterType property
     *  to filter the query
     *
     * @param filterCriteria_p OwSearchNode to use as a filter
     *
     * @return OwFNBPMQueryInfo
     */
    protected OwFNBPMQueryInfo createFilterQueryParameter(OwSearchNode filterCriteria_p) throws Exception
    {
        OwFNBPMQueryInfo query = new OwFNBPMQueryInfo();

        List<Object> substitutionVarList = new LinkedList<Object>();

        // !!! F_CreateTime <> F_StartTime
        if (null != filterCriteria_p)
        {
            // === create query string
            OwFNBPMSearchSQLOperator sqlPropertyOperator = new OwFNBPMSearchSQLOperator(this);
            StringWriter sqlStatement = new StringWriter();

            if (sqlPropertyOperator.createSQLSearchCriteria(filterCriteria_p, sqlStatement))
            {
                query.m_strFiler = sqlStatement.toString();
                substitutionVarList.addAll(sqlPropertyOperator.getSubstionVars());
            }
        }

        // === filter according to filter type
        if (canResubmit())
        {
            // === no filter without resubmission capability
            int filterType = getFilterType();
            if (filterType == FILTER_TYPE_NORMAL || filterType == FILTER_TYPE_RESUBMISSION)
            {
                StringBuilder extendFilter = new StringBuilder();
                if (query.m_strFiler != null)
                {
                    extendFilter.append("(");
                    extendFilter.append(query.m_strFiler);
                    extendFilter.append(")");
                    extendFilter.append(" and ");

                }

                extendFilter.append("(");
                extendFilter.append(RESUBMIT_DATE_PROPERTY_NAME);
                if (filterType == FILTER_TYPE_NORMAL)
                {
                    extendFilter.append(" < ");//FILTER_TYPE_NORMAL
                }
                else
                {
                    extendFilter.append(" > ");//FILTER_TYPE_RESUBMISSION
                }
                extendFilter.append(OwFNBPMSearchSQLOperator.getPlaceHolder(substitutionVarList.size()));
                extendFilter.append(")");

                query.m_strFiler = extendFilter.toString();
                // filter against current date
                substitutionVarList.add(new Date());
            }
        }

        if (substitutionVarList.size() > 0)
        {
            query.m_substitionvars = substitutionVarList.toArray();
        }

        return query;
    }

    /** delete object and all references from DB
     */
    public void delete() throws Exception
    {
    }

    /** get the number of children
    *
    * @param iObjectTypes_p the requested object types (folder or document)
    * @param iContext_p as defined by {@link OwStatusContextDefinitions}
    * @return int number of children or throws OwStatusContextException
    */
    public int getChildCount(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        throw new OwStatusContextException("");
    }

    /** the one and only object class for the queue elements
     */
    protected OwFNBPM5WorkItemObjectClass getChildObjectClass() throws Exception
    {
        return m_ChildClassDescription;
    }

    /** get the children of the object, does NOT cache the returned object
     *  For Compound Documents returns the list of contained documents
     *  For Folders returns the list of sub folders
     *
     * @param iObjectTypes_p the requested object types (folder or document)
     * @param propertyNames_p properties to fetch from ECM system along with the children, can be null.
     * @param sort_p OwSort sort criteria list to sort returned result
     * @param iMaxSize_p int maximum number of objects to retrieve
     * @param iVersionSelection_p int Selects the versions as defined in OwSearchTemplate.VERSION_SELECT_...
     * @param filterCriteria_p optional OwSearchNode to filter the childs, can be null 
     *          NOTE:   This parameter is an additional filter to the internal SearchTemplate used in the getSearchTemplate(...) function
     *                  The internal SearchTemplate used in the getSearchTemplate(...) is used for virtual folders, the FilterCriteria_p is used to refine the result of a node
     * @param fLock_p lock the returned objects
     *
     * @return list of child objects, or null
     */
    protected abstract OwObjectCollection getChildsInternal(int[] iObjectTypes_p, Collection propertyNames_p, OwSort sort_p, int iMaxSize_p, int iVersionSelection_p, OwSearchNode filterCriteria_p, boolean fLock_p) throws Exception;

    /** get the class name of the object, the class names are defined by the ECM System
     * @return class name of object class
     */
    public String getClassName()
    {
        return "OwFNBPMBaseContainer";
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
    public OwPropertyCollection getClonedProperties(Collection strPropertyNames_p) throws Exception
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

    /** get the context
     */
    protected OwNetworkContext getContext()
    {
        return m_repository.getContext();
    }

    /**
     * The default user selection is by default disabled for the FNBPM base container.<br>
     * 
     * @return always <code>null</code>
     */
    public Collection getDefaultUsers()
    {
        return null;
    }

    /** try to find a compatible field description from the DMS system to resolve choicelists
     * 
     * @param sPropName_p String property to find
     * @return OwFieldDefinition from the DMS system
     * @throws OwException
     * @throws OwObjectNotFoundException 
     */
    public OwFieldDefinition getDmsCompatiblePropertyClass(String sPropName_p) throws OwException
    {
        if (null == m_slookupresource)
        {
            String msg = "OwFNBPMBaseContainer.getDmsCompatiblePropertyClass: The Resource to find a compatible property class description from the system to resolve choicelists is null, property = " + sPropName_p;
            LOG.error(msg);
            throw new OwObjectNotFoundException(msg);
        }

        return (m_repository.getNetwork()).getFieldDefinition(sPropName_p, m_slookupresource);
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
        return OwFNBPM5Repository.getDMSID(getID(), getResourceID());
    }

    /** implementation of the OwFieldProvider interface
     * get a field with the given field definition class name
     *
     * @param strFieldClassName_p String class name of requested fields
     *
     * @return OwField or throws OwObjectNotFoundException
     */
    public OwField getField(String strFieldClassName_p) throws OwObjectNotFoundException
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
    public Collection getFilterProperties(Collection propertynames_p) throws Exception
    {
        Collection<Object> filterprops = new LinkedList<Object>();

        Iterator<?> it = propertynames_p.iterator();
        while (it.hasNext())
        {
            String sPropertyName = it.next().toString();

            try
            {
                OwFNBPM5StandardWorkItemPropertyClass propclass = (OwFNBPM5StandardWorkItemPropertyClass) getRepository().getFieldDefinition(sPropertyName, getResource().getID());

                if (propclass.isFilterField())
                {
                    filterprops.add(propclass);
                }
            }
            catch (OwObjectNotFoundException e)
            {
            }
        }

        return filterprops;
    }

    /** get a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @return int as defined in FILTER_TYPE_...
     */
    public int getFilterType()
    {
        return m_iFilterType;
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

    /** get the lock state of the object for ALL users
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the lock user of the object
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the User ID of the user who locked the item, or null if it is not locked
     */
    public String getLockUserID(int iContext_p) throws Exception
    {
        return null;
    }

    /** retrieve the MIME parameter for the specified page number
     *
     * @return String Mimeparameter
     */
    public String getMIMEParameter() throws Exception
    {
        return "";
    }

    /** get the lock state of the object for the CURRENTLY logged on user
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return the lock state of the object
     */
    public boolean getMyLock(int iContext_p) throws Exception
    {
        return false;
    }

    /** get the native object from the ECM system
     *
     *  NOTE: The returned object is Opaque. 
     *           Using the native object makes the client dependent on the ECM System
     *
     * @return Object native to the ECM System
     */
    public Object getNativeObject() throws Exception
    {
        return null;
    }

    /** get a reference to the network */
    protected OwFNCM5Network getNetwork()
    {
        return m_repository.getNetwork();
    }

    /** the one and only object class for the queue
     */
    public OwObjectClass getObjectClass()
    {
        return new OwStandardObjectClass();
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
        return null;
    }

    /** get the path to the object, which can be used in OwNetwork.getObjectFromPath to recreate the object
     *
     * The path is build with the name property.
     * Unlike the symbol name and the dmsid, the path is not necessarily unique,
     * but provids a readable information of the objects location.
     */
    public String getPath() throws Exception
    {
        StringBuilder ret = new StringBuilder();

        ret.append(OwWorkitemRepository.PATH_DELIMITER);
        ret.append(OwWorkitemRepository.m_containerprefixmap.getContainerPrefix(getType()));
        ret.append(OwWorkitemRepository.PATH_DELIMITER);
        ret.append(getID());

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

    /** retrieve the specified properties from the object.
     * NOTE: The returned collection might contain more Properties than requested with PropertyNames_p
     * <br><br>
     * NOTE: if the properties where not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *
     * @param propertyNames_p  a collection of property names to retrieve, if null all properties are retrieved
     * @return a property list
     */
    public OwPropertyCollection getProperties(Collection propertyNames_p) throws Exception
    {
        throw new OwObjectNotFoundException("OwFNBPMBaseContainer.getProperties: Not implemented or Not supported.");
    }

    /** retrieve the specified property from the object.
     * NOTE: if the property was not already obtained from the archive (e.g. by OwNetwork.doSearch(...,PropertyList)), than the ECM Adapter has to launch a new query.
     *       It is therefore best practice to obtain the needed properties in advance in a call to OwNetwork.doSearch(...)
     *       ==> Alternatively you can use the getProperties Function to retrieve a whole bunch of properties in one step, making the ECM adaptor use only one new query.
     * @param strPropertyName_p the name of the requested property
     *
     * @return a property object
     */
    public OwProperty getProperty(String strPropertyName_p) throws OwObjectNotFoundException
    {
        throw new OwObjectNotFoundException("OwFNBPMBaseContainer.getProperty: Not implemented or Not supported, propertyName = " + strPropertyName_p);
    }

    /** get a display name for a reassign container name */
    public String getPublicReassignContainerDisplayName(String sName_p)
    {
        return sName_p;
    }

    /** get the possible reassign container names used in reassignToPublicContainer
     * 
     * @return Collection of names or null if not defined
     * @throws Exception
     */
    public Collection getPublicReassignContainerNames() throws Exception
    {
        return null;
    }

    /** get a reference to the network */
    protected OwFNBPM5Repository getRepository()
    {
        return m_repository;
    }

    /** get the resource the object belongs to in a multiple resource Network
     *
     * @return OwResource to identify the resource, or null for the default resource
     */
    public OwResource getResource() throws Exception
    {
        return new OwFNBPMBaseContainerResource();
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
        return getPath();
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

    /**
     * Get VWSession from Network
     *
     * @return VWSession object.
     */
    protected VWSession getVWSession() throws Exception
    {
        return m_repository.getVWSession();
    }

    /** check if object has children
     *
     * @param iContext_p as defined by {@link OwStatusContextDefinitions}
     * @return true, object has children
     */
    public boolean hasChilds(int[] iObjectTypes_p, int iContext_p) throws Exception
    {
        for (int i = 0; i < iObjectTypes_p.length; i++)
        {
            if (!OwStandardObjectClass.isContainerType(iObjectTypes_p[i]))
            {
                throw new OwStatusContextException("");
            }
        }

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
        return false;
    }

    /** check if item is in a user box */
    protected abstract boolean isUserContainer();

    /** moves a object reference to this parent object (folder)
     *
     *  @param oObject_p OwObject reference to add to
     *  @param oldParent_p OwObject Old Parent to remove from, used for move operation, can be null
     */
    public void move(OwObject oObject_p, OwObject oldParent_p) throws Exception
    {
        // not supported
    }

    /** pulls the next available work item out of the container and locks it for the user
     * 
     * @param sort_p OwSort optional sorts the items and takes the first available one, can be null
     * @param exclude_p Set of work item DMSIDs to be excluded, i.e. that may have already been pulled by the user
     * @return OwWorkitem or OwObjectNotFoundException if no object is available or OwServerException if no object could be pulled within timeout
     * @throws OwException 
     * @throws OwException for general error, or OwServerException if timed out or OwObjectNotFoundException if no work item is available
     */
    public OwWorkitem pull(OwSort sort_p, Set exclude_p) throws OwException
    {
        throw new OwObjectNotFoundException("OwFNBPMBaseContainer.pull: Not implemented or Not supported.");
    }

    /** refresh the property cache  */
    public void refreshProperties()
    {
    }

    /** refresh the property cache 
     * 
     * @param props_p Collection of property names to update
     */
    public void refreshProperties(Collection props_p)
    {
        refreshProperties();
    }

    /** removes the reference of the given object from this object (folder)
     *  this object needs to be parent of given object
     * @param oObject_p OwObject reference to be removed from this object (folder)
     */
    public void removeReference(OwObject oObject_p)
    {
    }

    /** save the lock state in a global array so we can track all the locked items
     * 
     * @param item_p an {@link OwFNBPM5QueueWorkItem}
     * @param lock_p boolean lock state
     * @throws Exception 
     */
    public void saveLocked(OwFNBPM5QueueWorkItem item_p, boolean lock_p) throws Exception
    {
        getRepository().saveLocked(item_p, lock_p);
    }

    /** set the content to the object
     *
     * @param content_p OwContentCollection to store in the object
    */
    public void setContentCollection(OwContentCollection content_p)
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

    /** set a filter to filter specific items in getChilds in addition to the getChilds OwSearchNode parameter
     * @param iFilterType_p <code>int</code>  type as defined in FILTER_TYPE_...
     */
    public void setFilterType(int iFilterType_p)
    {
        m_iFilterType = iFilterType_p;
    }

    /** lock / unlock object, make it unaccessible for other users
     * @param fLock_p true to lock it, false to unlock it.
     * @return the new lock state of the object
     */
    public boolean setLock(boolean fLock_p)
    {
        return false;
    }

    /** set the permissions object
     *
     * @param permissions_p OwPermissionCollection to set
     */
    public void setPermissions(OwPermissionCollection permissions_p)
    {
    }

    /** set the properties in the object
     * @param properties_p OwPropertyList list of OwProperties to set
     */
    public void setProperties(OwPropertyCollection properties_p)
    {

    }

    /* (non-Javadoc)
     * @see com.wewebu.ow.server.ecm.OwObject#setProperties(com.wewebu.ow.server.ecm.OwPropertyCollection, java.lang.Object)
     */
    public void setProperties(OwPropertyCollection properties_p, Object mode_p) throws Exception
    {
        setProperties(properties_p);
    }

    /** get a CE property name for a BPM name 
     *
     * @return String CE Name or null if not found
     */
    public static String getCorrespondingCEPropertyName(String strBPMName_p)
    {
        return m_InternalCEBPMPropertyMapping.getCorrespondingCEPropertyName(strBPMName_p);
    }

    /** get the names of properties that contain user IDs 
     * @return Set of property names
     * */
    protected static Set getUserProperties()
    {
        return m_InternalUserProperties.m_userproperties;
    }

    /** inner resource class */
    private class OwFNBPMBaseContainerResource implements OwResource
    {
        public String getDescription(Locale locale_p)
        {
            return getName();
        }

        public String getDisplayName(Locale locale_p)
        {
            return getName();
        }

        public String getID() throws Exception
        {
            return getPath();
        }
    }

    /**
     *<p>
     * Query info tuple for P8 BPM queries upon rosters or queues.
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
    protected static class OwFNBPMQueryInfo
    {
        // === FileNet P8 BPM filter parameters for createQuery function in rosters and queues
        public String m_index;
        public Object[] m_maxValues;
        public Object[] m_minValues;
        public String m_strFiler;
        public Object[] m_substitionvars;

        /** dump the query info 
         * @throws IOException */
        public void dump(Writer w_p) throws IOException
        {
            w_p.write("###### OwFNBPMQueryInfo ######\n");
            w_p.write("m_index: " + m_index + "\n");
            w_p.write("m_strFiler: " + m_strFiler + "\n");

            w_p.write("m_substitionvars ");
            if (m_substitionvars != null)
            {
                for (int i = 0; i < m_substitionvars.length; i++)
                {
                    w_p.write("\n  " + String.valueOf(i) + ": " + m_substitionvars[i]);
                }
            }

            w_p.write("\nm_minValues ");
            if (m_minValues != null)
            {
                for (int i = 0; i < m_minValues.length; i++)
                {
                    w_p.write("\n  " + String.valueOf(i) + ": " + m_minValues[i]);
                }
            }

            w_p.write("\nm_maxValues ");
            if (m_maxValues != null)
            {
                for (int i = 0; i < m_maxValues.length; i++)
                {
                    w_p.write("\n  " + String.valueOf(i) + ": " + m_maxValues[i]);
                }
            }

            w_p.write("\n\n");

            w_p.flush();
        }

        @Override
        public String toString()
        {
            StringBuilder queryDef = new StringBuilder("OwFNBPMQueryInfo{queryInfo.idx = " + m_index);
            queryDef.append(", minValues = [");
            if (m_minValues != null)
            {
                for (int i = 0; i < m_minValues.length; i++)
                {
                    queryDef.append(m_minValues[i]);
                    if (i + 1 < m_minValues.length)
                    {
                        queryDef.append(", ");
                    }
                }
            }
            queryDef.append("], maxValues = [");
            if (m_maxValues != null)
            {
                for (int i = 0; i < m_maxValues.length; i++)
                {
                    queryDef.append(m_maxValues[i]);
                    if (i + 1 < m_maxValues.length)
                    {
                        queryDef.append(", ");
                    }
                }
            }
            queryDef.append("], filter = ").append(m_strFiler);
            queryDef.append(", substitionVars = [");
            if (m_substitionvars != null)
            {
                for (int i = 0; i < m_substitionvars.length; i++)
                {
                    queryDef.append(m_substitionvars[i]);
                    if (i + 1 < m_substitionvars.length)
                    {
                        queryDef.append(", ");
                    }
                }
            }
            queryDef.append("]}");
            return queryDef.toString();
        }
    }

    /**
     *<p>
     * Overridden SQL operator to create the special pe substition clauses.
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
    private static class OwFNBPMSearchSQLOperator extends OwSearchSQLOperator
    {
        private OwFNBPM5BaseContainer m_parentContainer;
        private List<Object> m_substitionVars;

        /** create a new operator */
        public OwFNBPMSearchSQLOperator(OwFNBPM5BaseContainer parentContainer_p)
        {
            super(DATE_MODE_FNCM | DATE_MODE_TREAT_IGNORE_TIME_AS_RANGE);
            m_substitionVars = new LinkedList<Object>();
            m_parentContainer = parentContainer_p;
        }

        /** convert the wildcards to client format
         * 
         * @param criteria_p
         * @param value_p
         */
        protected String convertWildCards(OwSearchCriteria criteria_p, String value_p)
        {
            String convertedValue = super.convertWildCards(criteria_p, value_p);

            if (criteria_p.canWildCard())
            {
                if ((-1 == convertedValue.indexOf('%')) && (-1 == convertedValue.indexOf('_')))
                {
                    // === user did not type in any wildcards, so we append some for ease of use
                    convertedValue = "%" + convertedValue + "%";
                }
            }

            return convertedValue;
        }

        /** get the value of the criteria and convert it to a SQL conform string
         * @param criteria_p the criteria to convert
         * @param iDateMode_p int Date mode used to convert date types as defined with DATE_MODE_...
         *
         * @return String with SQL conform representation of value
         */
        protected String getSQLValueString(OwSearchCriteria criteria_p, Object value_p, int iDateMode_p) throws Exception
        {
            // create placeholder
            String placeHolder = getPlaceHolder(m_substitionVars.size());

            // store var
            if (getUserProperties().contains(criteria_p.getClassName()))
            {
                // user ID property convert from string to ID 
                try
                {
                    m_substitionVars.add(Integer.valueOf(m_parentContainer.getVWSession().convertUserNameToId((String) value_p)));
                }
                catch (ClassCastException e)
                {
                    // === a integer ID not a name
                    m_substitionVars.add(value_p);
                }
                catch (VWException e)
                {
                    throw new OwInvalidOperationException(new OwString1("fncm.bmp.OwFNBPMBaseContainer.usernotfound", "User name (%1) not found.", (String) value_p), e);
                }
            }
            else if (criteria_p.getJavaClassName().equals("java.lang.String"))
            {
                m_substitionVars.add(convertWildCards(criteria_p, value_p.toString()));
            }
            else
            {
                // normal property
                m_substitionVars.add(value_p);
            }

            // return placeholder
            return placeHolder;
        }

        /**
         * Return the list of objects, which will be used for
         * the substitution variables in PE query
         * @return List of Object's
         */
        public List<Object> getSubstionVars()
        {
            return m_substitionVars;
        }

        /** create a PE SQL conform placeholder
         * return String representing placeholder for query
         */
        public static String getPlaceHolder(int iIndex_p)
        {
            return ":var" + String.valueOf(iIndex_p);
        }
    }

    /**
     *<p>
     * Static class to map content engine property names to BPM names,
     * so we can reuse search templates and resultlist configuration .
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
    public static class OwInternalCEBPMPropertyMapping
    {
        private Map<String, String> m_cebpmMapping = new HashMap<String, String>();

        /** create the map of properties */
        public OwInternalCEBPMPropertyMapping()
        {
            // === map BPM keys to CE names
            m_cebpmMapping.put("F_Subject", "DocumentTitle");
            m_cebpmMapping.put("F_StartTime", "DateCreated");
            m_cebpmMapping.put("F_CreateTime", "DateCreated");
            m_cebpmMapping.put("F_EnqueueTime", "DateLastModified");
            m_cebpmMapping.put("F_Originator", "Creator");

            // NOTE: 	Rosters contain: 	F_BoundUser and F_Originator
            //			Queues contain:		F_BoundUser which acts as originator
        }

        /** get a CE property name for a BPM name 
         *
         * @return String CE Name or null if not found
         */
        public String getCorrespondingCEPropertyName(String strBPMName_p)
        {
            return m_cebpmMapping.get(strBPMName_p);
        }

    }

    /**
    *<p>
    * Static class for a set of property names that contain user IDs .
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
    public static class OwInternalUserProperties
    {
        private Set<String> m_userproperties = new HashSet<String>();

        /** create the map of properties */
        protected OwInternalUserProperties()
        {
            // === map BPM keys to CE names
            m_userproperties.add("F_LockUser");
            m_userproperties.add("F_BoundUser");
            m_userproperties.add("F_Originator");

            // NOTE: 	Rosters contain: 	F_BoundUser and F_Originator
            //			Queues contain:		F_BoundUser which acts as originator
        }
    }

    @Override
    public OwIterable<OwObject> getChildren(OwLoadContext loadContext) throws OwException
    {
        throw new OwNotSupportedException("Pageable handling not yet supported");
    }
}